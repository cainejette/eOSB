package eOSB.game.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.swing.JFrame;

import org.bushe.swing.event.EventBus;
import org.bushe.swing.event.EventServiceEvent;
import org.bushe.swing.event.EventSubscriber;

import eOSB.binder.controller.Binder;
import eOSB.binder.controller.NextQuestionEvent;
import eOSB.binder.controller.OpenTcqPreambleEvent;
import eOSB.binder.controller.RemindTcqEvent;
import eOSB.binder.controller.TcqPreambleDialog;
import eOSB.binder.controller.TeamNameEvent;
import eOSB.binder.controller.UpdateQuestionEvent;
import eOSB.binder.controller.UserAuthenticatedEvent;
import eOSB.binder.ui.HideBuzzerQuestionsEvent;
import eOSB.binder.ui.actions.RoundSelectedEvent;
import eOSB.game.data.PathStore;
import eOSB.game.data.QuestionXMLParser;
import eOSB.game.ui.CloseProgramEvent;
import eOSB.game.ui.SelectRoundDialog;
import eOSB.score.controller.LastTurnEvent;
import eOSB.score.controller.Scorekeeper;
import eOSB.score.controller.TeamScoreNumberEvent;
import eOSB.time.controller.Timekeeper;

public class Handler implements EventSubscriber<EventServiceEvent> {

	private QuestionXMLParser questionReader;
	private Binder binder;
	private Scorekeeper scorekeeper;
	private Timekeeper timekeeper;

	private List<Round> availableRounds = new ArrayList<Round>();
	private Round currentRound;

	private boolean usedScoreboardLastRound = false;
	private boolean usedTimerLastRound = false;

	private boolean shouldUseScoreboard = false;
	private boolean shouldUseTimer = false;

	private Team teamA;
	private Team teamB;

	private InformationFrame infoFrame;
	
	private String token = null;

	public Handler() {
		this.teamA = new Team("Team A");
		this.teamB = new Team("Team B");
		this.binder = new Binder(this);
		this.timekeeper = new Timekeeper();
		this.scorekeeper = new Scorekeeper(this.teamA, this.teamB);

		this.populateRounds();

		EventBus.subscribe(NewRoundEvent.class, this);
		EventBus.subscribe(LastTurnEvent.class, this);
		EventBus.subscribe(TeamNameEvent.class, this);
		EventBus.subscribe(RoundSelectedEvent.class, this);
		EventBus.subscribe(ShowSelectPackagesDialogEvent.class, this);
		EventBus.subscribe(RoundLoadedEvent.class, this);
		EventBus.subscribe(RemindTcqEvent.class, this);
		EventBus.subscribe(OpenTcqPreambleEvent.class, this);
		EventBus.subscribe(NextQuestionEvent.class, this);
		EventBus.subscribe(UserAuthenticatedEvent.class, this);
		EventBus.subscribe(CloseProgramEvent.class, this);
	}

	private class RoundInfo {
		private String name;
		private String filePath;
		private String tcqADuration;
		private String tcqBDuration;

		public void setName(String name) { this.name = name; }
		public void setFilePath(String path) { this.filePath = path; }
		public void setTcqADuration(String duration) { this.tcqADuration = duration; }
		public void setTcqBDuration(String duration) { this.tcqBDuration = duration; }

		@Override
		public String toString() {
			return name + ", " + filePath + ", " + tcqADuration + ", " + tcqBDuration;
		}
	}

	private List<RoundInfo> getRoundInfos() {
		List<RoundInfo> roundInfos = new ArrayList<RoundInfo>();
		BufferedReader br = new BufferedReader(new InputStreamReader( Handler.getResourceAsStream(PathStore.INFO)));

		try {
			String name = br.readLine();

			while (name != null) {
				String filePath = br.readLine();
				String aDuration = br.readLine();
				String bDuration = br.readLine();

				if (aDuration != null && bDuration != null) {
					RoundInfo roundInfo = new RoundInfo();

					roundInfo.setName(name.split(":")[1].trim());
					roundInfo.setFilePath(filePath.split(":")[1].trim());

					roundInfo.setTcqADuration(aDuration.split(":", 2)[1].trim());
					roundInfo.setTcqBDuration(bDuration.split(":", 2)[1].trim());

					System.out.println(roundInfo);
					roundInfos.add(roundInfo);
				}

				br.readLine();
				name = br.readLine();
			}
		}
		catch (IOException e) {
			System.out.println("Something bad happened reading file");
		}
		finally {
			try {
				br.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.out.println("Failed to clean up round info reader");
				e.printStackTrace();
			}
		}

		return roundInfos;
	}

	private void populateRounds() {				
		for (RoundInfo roundInfo : this.getRoundInfos()) {
			String buzzer = PathStore.BASE + roundInfo.filePath;			
			String number = roundInfo.filePath.split("(_)|(\\.)")[1];

			List<Tcq> tcqs = new ArrayList<Tcq>();
			tcqs.add(new Tcq("TCQ A", PathStore.MakeTcqPath(number, true, false), roundInfo.tcqADuration));
			tcqs.add(new Tcq("TCQ B", PathStore.MakeTcqPath(number, false, false), roundInfo.tcqBDuration));

			List<Tcq> tcqSolutions = new ArrayList<Tcq>();
			tcqSolutions.add(new Tcq("TCQ A Solutions", PathStore.MakeTcqPath(number, true, true)));
			tcqSolutions.add(new Tcq("TCQ B Solutions", PathStore.MakeTcqPath(number, false, true)));

			this.availableRounds.add(new Round(roundInfo.name, buzzer, tcqs, tcqSolutions));
		}
	}

	public List<Round> getAvailableRounds() {
		return this.availableRounds;
	}

	private void setCurrentRound(Round round) {
		if (this.currentRound != null) {
			this.currentRound.setCurrentQuestion(-1);
		}

		this.teamA = new Team("Team A");
		this.teamB = new Team("Team B");

		EventBus.publish(new TeamNameEvent(this, this.teamA.getName(), this.teamB.getName()));
		EventBus.publish(new TeamScoreNumberEvent(this, this.teamA.getScore(), this.teamB.getScore()));

		this.currentRound = round;

		this.questionReader = new QuestionXMLParser(this.currentRound, this.token);
		this.token = null;
		this.processNewRound();
	}

	public JFrame getFrame() {
		return this.binder.getFrame();
	}

	public Timekeeper getTimekeeper() {
		if (this.shouldUseTimer) {
			return this.timekeeper;
		} else {
			return null;
		}
	}

	public Round getCurrentRound() {
		return this.currentRound;
	}

	public void previousQuestion() {
		if (this.isUsingScoreboard()) {
			Turn turn = this.scorekeeper.removeTurn();

			if (turn != null) {
				boolean enableTeamA = false;
				if (turn.getTeamAScore().getWorth() > 0) {
					enableTeamA = true;
				}

				boolean enableTeamB = false;
				if (turn.getTeamBScore().getWorth() > 0) {
					enableTeamB = true;
				}

				EventBus.publish(new UpdateQuestionEvent(this,
						this.currentRound.getPreviousQuestion(), enableTeamA,
						enableTeamB));
			}
		} else {
			EventBus.publish(new UpdateQuestionEvent(this, this.currentRound
					.getPreviousQuestion(), true, true));
		}
	}

	public void setPackages(boolean shouldUseScoreboard, boolean shouldUseTimer) {
		this.shouldUseScoreboard = shouldUseScoreboard;
		this.shouldUseTimer = shouldUseTimer;
	}

	public boolean isUsingScoreboard() {
		return this.shouldUseScoreboard;
	}

	public boolean usedScoreboardLastRound() {
		return this.usedScoreboardLastRound;
	}

	public boolean isUsingTimer() {
		return this.shouldUseTimer;
	}

	public boolean usedTimerLastRound() {
		return this.usedTimerLastRound;
	}

	public void cleanUp() {
		if (this.shouldUseTimer) {
			this.timekeeper.close();
		}

		this.binder.close();
	}

	public Team getTeamA() {
		return this.teamA;
	}

	public Team getTeamB() {
		return this.teamB;
	}

	public void onEvent(EventServiceEvent event) {
		if (event instanceof UserAuthenticatedEvent) {
			this.showSelectRoundDialog();
		}
		else if (event instanceof NewRoundEvent) {
			NewRoundEvent nre = (NewRoundEvent) event;
			try {
				this.setCurrentRound(this.getRoundFromName(nre.getName()));
			} catch (NoRoundFoundException e) {
				System.err.println("no round called: " + e.getName() + " found!");
			}
		} else if (event instanceof TeamNameEvent) {
			TeamNameEvent tne = (TeamNameEvent) event;
			this.teamA.setName(tne.getTeamAName());
			this.teamB.setName(tne.getTeamBName());
		} else if (event instanceof RoundSelectedEvent) {
			RoundSelectedEvent rse = (RoundSelectedEvent) event;
			this.shouldUseTimer = rse.useTimekeeper();
			this.usedTimerLastRound = this.shouldUseTimer;
			this.shouldUseScoreboard = rse.useScorekeeper();
			this.usedScoreboardLastRound = this.shouldUseScoreboard;

			EventBus.publish(new NewRoundEvent(this, rse.getRoundName()));
		} else if (event instanceof RemindTcqEvent) {
			this.showHalfwayDoneDialog();
		} else if (event instanceof OpenTcqPreambleEvent) {
			this.showTcqPreambleDialog();
		} else if (event instanceof NextQuestionEvent) {
			NextQuestionEvent nqe = (NextQuestionEvent)event;
			Turn turn = nqe.getTurn();
			if (turn != null) {
				this.scorekeeper.addTurn(turn);
			}

			boolean enableTeamA = nqe.getEnableTeamA();
			boolean enableTeamB = nqe.getEnableTeamB();
			
			EventBus.publish(new UpdateQuestionEvent(this, this.currentRound
					.getNextQuestion(), enableTeamA, enableTeamB));

		} else if (event instanceof CloseProgramEvent) {
			System.exit(0);
		}
	}

	private void processNewRound() {
		this.currentRound = this.questionReader.getRound();
		this.currentRound.setOpened();

		this.timekeeper.close();
		this.timekeeper = new Timekeeper();

		this.scorekeeper = new Scorekeeper(this.teamA, this.teamB);

		if ((this.shouldUseScoreboard) || (this.shouldUseTimer)) {
			if (this.infoFrame != null) {
				this.infoFrame.close();
			}
			this.infoFrame = new InformationFrame(this.shouldUseScoreboard,
					this.shouldUseTimer);
		} else if (this.infoFrame != null) {
			this.infoFrame.close();
		}
		
		binder.setBinderToNewRound(currentRound.getName());
	}

	private Round getRoundFromName(String name) throws NoRoundFoundException {
		for (Round round : this.availableRounds) {
			if (round.getName().equals(name)) {
				return round;
			}
		}
		throw new NoRoundFoundException(name);
	}

	private void showSelectRoundDialog() {
		SelectRoundDialog dialog = new SelectRoundDialog(this.getFrame(), this.availableRounds, this.usedTimerLastRound, this.usedScoreboardLastRound);
		dialog.setVisible(true);
	}

	private void showHalfwayDoneDialog() {
		HalfwayDoneDialog dialog = new HalfwayDoneDialog(this.getFrame());
		dialog.setVisible(true);
	}

	private void showTcqPreambleDialog() {
		HideBuzzerQuestionsEvent event = new HideBuzzerQuestionsEvent(this);
		EventBus.publish(event);

		TcqPreambleDialog dialog = new TcqPreambleDialog(this, this.binder);
		dialog.setVisible(true);
	}

	public static InputStream getResourceAsStream(String resource) {
		System.out.println("input: " + resource);
		InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream(resource);
		System.out.println("input stream: " + is);
		System.out.println("url: " + ClassLoader.getSystemClassLoader().getResource(resource));
		return is;
	}

	public static InputStream getDecryptedStream(String key, InputStream encryptedInputStream)
			throws Exception {
		return new CipherInputStream(encryptedInputStream, makeCipher(key,
				Cipher.DECRYPT_MODE));
	}

	/**
	 * Encrypts the given output stream using the provided key
	 * @param key the key to use to encrypt the output stream
	 * @param unencryptedOutputStream the output stream to encrypt
	 * @return a {@link CipherOutputStream} representing the encrypted stream
	 * @throws Exception when something breaks
	 */
	public static OutputStream encrypt(String key, OutputStream unencryptedOutputStream)
			throws Exception {
		return new CipherOutputStream(unencryptedOutputStream, makeCipher(key,
				Cipher.ENCRYPT_MODE));
	}

	public static Cipher makeCipher(String key, int mode) throws Exception {
		DESKeySpec dks = new DESKeySpec(key.getBytes());
		SecretKeyFactory skf = SecretKeyFactory.getInstance("DES");
		SecretKey desKey = skf.generateSecret(dks);
		Cipher cipher = Cipher.getInstance("DES"); // DES/ECB/PKCS5Padding for SunJCE
		cipher.init(mode, desKey);
		return cipher;
	}

	public void setRoundToken(String token) {
		this.token = token;
	}
}
