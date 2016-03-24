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
import javax.swing.JOptionPane;

import org.bushe.swing.event.EventBus;
import org.bushe.swing.event.EventServiceEvent;
import org.bushe.swing.event.EventSubscriber;

import eOSB.binder.controller.Binder;
import eOSB.binder.controller.OpenTcqPreambleEvent;
import eOSB.binder.controller.RemindTcqEvent;
import eOSB.binder.controller.TcqPreambleDialog;
import eOSB.binder.controller.TeamNameEvent;
import eOSB.binder.controller.UpdateQuestionEvent;
import eOSB.binder.ui.HideBuzzerQuestionsEvent;
import eOSB.binder.ui.actions.RoundSelectedEvent;
import eOSB.game.data.PathStore;
import eOSB.game.data.QuestionXMLParser;
import eOSB.game.ui.PackageSelectionListEvent;
import eOSB.game.ui.PackagesSelectedEvent;
import eOSB.game.ui.RoundSelectionListEvent;
import eOSB.game.ui.SelectPackageDialog;
import eOSB.game.ui.SelectRoundDialog;
import eOSB.score.controller.LastTurnEvent;
import eOSB.score.controller.Scorekeeper;
import eOSB.score.controller.TeamScoreNumberEvent;
import eOSB.time.controller.Timekeeper;

/**
 * The main eOSB handler
 * 
 * @author Caine Jette
 * 
 */
public class Handler implements EventSubscriber<EventServiceEvent> {

	/** the object used to read questions from files */
	private QuestionXMLParser questionReader;

	/** the gui for the binder */
	private Binder binder;

	/** the object used to keep score */
	private Scorekeeper scorekeeper;

	/** the object used to keep time */
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

	private boolean hasReadTerms = false;

	private String mostRecentRoundSelection = "Round 1";
	private int mostRecentPackageSelection = BINDER;

	private String token = null;

	public static final int BINDER = 0;
	public static final int BINDER_TIMEKEEPER = 1;
	public static final int BINDER_SCOREKEEPER = 2;
	public static final int BINDER_TIMEKEEPER_SCOREKEEPER = 3;

	public static final String QUESTION_CODE = "160101-MOP";

	// flag to indicate MOP or actual competition version
	public static final boolean IS_ORIENTATION_VERSION = true;

	/**
	 * Initializes the game controller object.
	 */
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
		EventBus.subscribe(RoundSelectionListEvent.class, this);
		EventBus.subscribe(RoundSelectedEvent.class, this);
		EventBus.subscribe(PackageSelectionListEvent.class, this);
		EventBus.subscribe(PackagesSelectedEvent.class, this);
		EventBus.subscribe(ShowSelectPackagesDialogEvent.class, this);
		EventBus.subscribe(RoundLoadedEvent.class, this);
		EventBus.subscribe(RemindTcqEvent.class, this);
		EventBus.subscribe(OpenTcqPreambleEvent.class, this);
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
					System.out.println("adding round info\n");
					System.out.println(name + " / " + filePath + " / " + aDuration + " / " + bDuration);
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
			tcqs.add(new Tcq(roundInfo.name + " TCQ A", PathStore.MakeTcqPath(number, true, false), roundInfo.tcqADuration));
			tcqs.add(new Tcq(roundInfo.name + " TCQ B", PathStore.MakeTcqPath(number, false, false), roundInfo.tcqBDuration));

			List<Tcq> tcqSolutions = new ArrayList<Tcq>();
			tcqSolutions.add(new Tcq(roundInfo.name + " TCQ A Solutions", PathStore.MakeTcqPath(number, true, true)));
			tcqSolutions.add(new Tcq(roundInfo.name + " TCQ B Solutions", PathStore.MakeTcqPath(number, false, true)));

			this.availableRounds.add(new Round(roundInfo.name, buzzer, tcqs, tcqSolutions));
		}
	}

	/**
	 * @return the list of rounds available in this version.
	 */
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

	/**
	 * @return the frame containing the {@link Binder}
	 */
	public JFrame getFrame() {
		return this.binder.getFrame();
	}

	/**
	 * @return the {@link Timekeeper}
	 */
	public Timekeeper getTimekeeper() {
		if (this.shouldUseTimer) {
			return this.timekeeper;
		} else {
			return null;
		}
	}

	/**
	 * @return the current round of the competition
	 */
	public Round getCurrentRound() {
		return this.currentRound;
	}

	/**
	 * When a user is not validated (did not type in the correct password.)
	 */
	public void failedValidation(String password, String input) {
		// enter code to tell user what happened
		JOptionPane.showMessageDialog(this.binder.getFrame(),
				"Password authentication failed.");
	}

	/**
	 * Dialog to select which software packages to enable.
	 */
	public void displayEula() {
		if (!this.hasReadTerms) {
			DisplayEulaDialog eulaDialog = new DisplayEulaDialog(this);
			eulaDialog.setVisible(true);
		} else {
			SelectPackageDialog dialog = new SelectPackageDialog(getFrame(), this.mostRecentPackageSelection);
			dialog.setVisible(true);
		}
	}

	public void setHasReadEula(boolean hasRead) {
		this.hasReadTerms = hasRead;
	}

	/**
	 * Proceeds to the next (appropriate) question. (If current question is
	 * tossup, move to corresponding bonus. else, current question is bonus,
	 * move to next tossup.)
	 */
	public void nextQuestion(boolean enableTeamA, boolean enableTeamB, Turn turn) {
		if (turn != null) {
			this.scorekeeper.addTurn(turn);
		}

		EventBus.publish(new UpdateQuestionEvent(this, this.currentRound
				.getNextQuestion(), enableTeamA, enableTeamB));
	}

	/**
	 * Withdraws the most recent question.
	 */
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

	/**
	 * Flags the appropriate packages to use for the current round.
	 * 
	 * @param shouldUseScoreboard
	 *            whether the scoreboard will be used this round
	 * @param shouldUseTimer
	 *            whether the timer will be used this round
	 */
	public void setPackages(boolean shouldUseScoreboard, boolean shouldUseTimer) {
		this.shouldUseScoreboard = shouldUseScoreboard;
		this.shouldUseTimer = shouldUseTimer;
	}

	/**
	 * @return true if scorekeeping has been enabled, false otherwise
	 */
	public boolean isUsingScoreboard() {
		return this.shouldUseScoreboard;
	}

	/**
	 * @return true if scorekeeping was enabled last round, false otherwise
	 */
	public boolean usedScoreboardLastRound() {
		return this.usedScoreboardLastRound;
	}

	/**
	 * @return true if timekeeping has been enabled, false otherwise
	 */
	public boolean isUsingTimer() {
		return this.shouldUseTimer;
	}

	/**
	 * @return true if timekeeping was enabled last round, false otherwise
	 */
	public boolean usedTimerLastRound() {
		return this.usedTimerLastRound;
	}

	/**
	 * Cleans up the frames
	 */
	public void cleanUp() {
		if (this.shouldUseTimer) {
			this.timekeeper.close();
		}

		this.binder.close();
	}

	/**
	 * @return {@link Team} A
	 */
	public Team getTeamA() {
		return this.teamA;
	}

	/**
	 * @return {@link Team} B
	 */
	public Team getTeamB() {
		return this.teamB;
	}

	/** {@inheritDoc} */
	public void onEvent(EventServiceEvent event) {
		if (event instanceof NewRoundEvent) {

//			RoundPreambleDialog dialog = new RoundPreambleDialog();
//			dialog.setVisible(true);

			System.out.println("[Handler/onEvent] received NewRoundEvent");
			NewRoundEvent nre = (NewRoundEvent) event;
			try {
				this.setCurrentRound(this.getRoundFromName(nre.getName()));
			} catch (NoRoundFoundException e) {
				System.err.println("no round called: " + e.getName() + " found!");
			}
		} else if (event instanceof TeamNameEvent) {
			System.out.println("[Handler/onEvent] received TeamNameEvent");
			TeamNameEvent tne = (TeamNameEvent) event;
			this.teamA.setName(tne.getTeamAName());
			this.teamB.setName(tne.getTeamBName());
		} else if (event instanceof ShowSelectPackagesDialogEvent) {
			System.out.println("[Handler/onEvent] received ShowSelectPackagesDialogEvent");
			this.setHasReadEula(true);
			this.showSelectPackagesDialog();
		}

		else if (event instanceof PackageSelectionListEvent) {
			PackageSelectionListEvent e = (PackageSelectionListEvent) event;
			this.mostRecentPackageSelection = e.getSelection();
		} else if (event instanceof PackagesSelectedEvent) {
			if (this.mostRecentPackageSelection == BINDER_TIMEKEEPER || this.mostRecentPackageSelection == BINDER_TIMEKEEPER_SCOREKEEPER) {
				this.shouldUseTimer = true;
			} else {
				this.shouldUseTimer = false;
			}

			if (this.mostRecentPackageSelection == BINDER_SCOREKEEPER || this.mostRecentPackageSelection == BINDER_TIMEKEEPER_SCOREKEEPER) {
				this.shouldUseScoreboard = true;
			} else {
				this.shouldUseScoreboard = false;
			}

			this.showSelectRoundDialog();
		} else if (event instanceof RoundSelectionListEvent) {
			RoundSelectionListEvent e = (RoundSelectionListEvent) event;
			this.mostRecentRoundSelection = e.getName();
		} else if (event instanceof RoundSelectedEvent) {
			EventBus.publish(new NewRoundEvent(this, this.mostRecentRoundSelection));
		} else if (event instanceof RemindTcqEvent) {
			System.out.println("[Handler/onEvent] received RemindTcqEvent");
			this.showHalfwayDoneDialog();
		} else if (event instanceof OpenTcqPreambleEvent) {
			System.out
			.println("[Handler/onEvent] received OpenTcqPreambleEvent");
			this.showTcqPreambleDialog();
		}
	}

	private String getPackageSelectionString() {
		switch (this.mostRecentPackageSelection) {
		case Handler.BINDER:
			return "Binder";
		case Handler.BINDER_SCOREKEEPER:
			return "Binder + Scorekeeper";
		case Handler.BINDER_TIMEKEEPER:
			return "Binder + Timekeeper";
		case Handler.BINDER_TIMEKEEPER_SCOREKEEPER:
			return "Binder + Timekeeper + Scorekeeper";
		}
		return "None?";
	}

	private void processNewRound() {
		this.currentRound = this.questionReader.getRound();
		this.currentRound.setOpened();

		System.out.println("[handler#setCurrentRound] contains "
				+ this.currentRound.getQuestions().size() + " questions");

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

//		EventBus.publish(new UpdateQuestionEvent(this, this.currentRound.getNextQuestion(), true, true));
	}

	private Round getRoundFromName(String name) throws NoRoundFoundException {
		for (Round round : this.availableRounds) {
			if (round.getName().equals(name)) {
				return round;
			}
		}
		throw new NoRoundFoundException(name);
	}

	private void showSelectPackagesDialog() {
		SelectPackageDialog dialog = new SelectPackageDialog(this.getFrame(),
				this.mostRecentPackageSelection);
		dialog.setVisible(true);
	}

	private void showSelectRoundDialog() {
		SelectRoundDialog dialog = new SelectRoundDialog(this.getFrame(),
				this.availableRounds);
		dialog.setVisible(true);
	}

	private void showHalfwayDoneDialog() {
		HalfwayDoneDialog dialog = new HalfwayDoneDialog(this.getFrame());
		dialog.setVisible(true);
	}

	private void showTcqPreambleDialog() {
		HideBuzzerQuestionsEvent event = new HideBuzzerQuestionsEvent(this);
		EventBus.publish(event);

		TcqPreambleDialog dialog = new TcqPreambleDialog(this);
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
