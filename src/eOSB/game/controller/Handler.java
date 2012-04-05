package eOSB.game.controller;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

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
import eOSB.binder.ui.ShowBuzzerQuestionsEvent;
import eOSB.binder.ui.actions.RoundSelectedEvent;
import eOSB.game.data.RoundFactory;
import eOSB.game.data.TcqFactory;
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
  
  public static final int BINDER = 0;
  public static final int BINDER_TIMEKEEPER = 1;
  public static final int BINDER_SCOREKEEPER = 2;
  public static final int BINDER_TIMEKEEPER_SCOREKEEPER = 3;
  
  public static final String QUESTION_CODE = "120405-0413N";

  /**
   * Initializes the game controller object.
   */
  public Handler() {
    this.teamA = new Team("Team A");
    this.teamB = new Team("Team B");
    this.binder = new Binder(this);
    this.timekeeper = new Timekeeper(this);
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

  /**
   * Populates the round list with the questions
   */
  private void populateRounds() {
    List<Tcq> tcqs = new ArrayList<Tcq>();
    List<Tcq> tcqSolutions = new ArrayList<Tcq>();
    
//    tcqs.add(new Tcq("Practice A", TcqFactory.TCQ_1_A));
//    tcqs.add(new Tcq("Practice B", TcqFactory.TCQ_1_B));
//    tcqSolutions.add(new Tcq("Practice A Solutions", TcqFactory.TCQ_1_A_SOLUTIONS));
//    tcqSolutions.add(new Tcq("Practice B Solutions", TcqFactory.TCQ_1_B_SOLUTIONS));
//    
    
    this.availableRounds.add(new Round("Six-question warm up", RoundFactory.PRACTICE, tcqs, tcqSolutions));
    
    tcqs.add(new Tcq("Round 1 A", TcqFactory.TCQ_1_A, "Biology", "4:00"));
    tcqs.add(new Tcq("Round 1 B", TcqFactory.TCQ_1_B, "Social Science", "4:00"));
    tcqSolutions.add(new Tcq("Round 1 A Solutions", TcqFactory.TCQ_1_A_SOLUTIONS));
    tcqSolutions.add(new Tcq("Round 1 B Solutions", TcqFactory.TCQ_1_B_SOLUTIONS));
    
    this.availableRounds.add(new Round("Round Robin / Round 1", RoundFactory.ROUND_1, tcqs, tcqSolutions));
    
    tcqs = new ArrayList<Tcq>();
    tcqs.add(new Tcq("Round 2 A", TcqFactory.TCQ_2_A, "Technology", "3:00"));
    tcqs.add(new Tcq("Round 2 B", TcqFactory.TCQ_2_B, "Geology", "5:00"));
    tcqSolutions = new ArrayList<Tcq>();
    tcqSolutions.add(new Tcq("Round 2 A Solutions", TcqFactory.TCQ_2_A_SOLUTIONS));
    tcqSolutions.add(new Tcq("Round 2 B Solutions", TcqFactory.TCQ_2_B_SOLUTIONS));
    
    this.availableRounds.add(new Round("Round Robin / Round 2", RoundFactory.ROUND_2, tcqs, tcqSolutions));
    
    tcqs = new ArrayList<Tcq>();
    tcqs.add(new Tcq("Round 3 A", TcqFactory.TCQ_3_A, "Biology", "2:00"));
    tcqs.add(new Tcq("Round 3 B", TcqFactory.TCQ_3_B, "Geography", "3:00"));

    tcqSolutions = new ArrayList<Tcq>();
    tcqSolutions.add(new Tcq("Round 3 A Solutions", TcqFactory.TCQ_3_A_SOLUTIONS));
    tcqSolutions.add(new Tcq("Round 3 B Solutions", TcqFactory.TCQ_3_B_SOLUTIONS));
    
    this.availableRounds.add(new Round("Round Robin / Round 3", RoundFactory.ROUND_3, tcqs, tcqSolutions));
    
    tcqs = new ArrayList<Tcq>();
    tcqs.add(new Tcq("Round 4 A", TcqFactory.TCQ_4_A, "Geology", "4:00"));
    tcqs.add(new Tcq("Round 4 B", TcqFactory.TCQ_4_B, "Technology", "3:00"));

    tcqSolutions = new ArrayList<Tcq>();
    tcqSolutions.add(new Tcq("Round 4 A Solutions", TcqFactory.TCQ_4_A_SOLUTIONS));
    tcqSolutions.add(new Tcq("Round 4 B Solutions", TcqFactory.TCQ_4_B_SOLUTIONS));

    this.availableRounds.add(new Round("Round Robin / Round 4", RoundFactory.ROUND_4, tcqs, tcqSolutions));

    tcqs = new ArrayList<Tcq>();
    tcqs.add(new Tcq("Round 5 A", TcqFactory.TCQ_5_A, "Physical", "3:00"));
    tcqs.add(new Tcq("Round 5 B", TcqFactory.TCQ_5_B, "Biology", "3:00"));

    tcqSolutions = new ArrayList<Tcq>();
    tcqSolutions.add(new Tcq("Round 5 A Solutions", TcqFactory.TCQ_5_A_SOLUTIONS));
    tcqSolutions.add(new Tcq("Round 5 B Solutions", TcqFactory.TCQ_5_B_SOLUTIONS));

    this.availableRounds.add(new Round("Round Robin / Round 5", RoundFactory.ROUND_5, tcqs, tcqSolutions));

    tcqs = new ArrayList<Tcq>();
    tcqs.add(new Tcq("Round 6 A", TcqFactory.TCQ_6_A, "Marine Policy", "3:00"));
    tcqs.add(new Tcq("Round 6 B", TcqFactory.TCQ_6_B, "Chemistry", "5:00"));

    tcqSolutions = new ArrayList<Tcq>();
    tcqSolutions.add(new Tcq("Round 6 A Solutions", TcqFactory.TCQ_6_A_SOLUTIONS));
    tcqSolutions.add(new Tcq("Round 6 B Solutions", TcqFactory.TCQ_6_B_SOLUTIONS));

    this.availableRounds.add(new Round("Double Elimination / Round 1", RoundFactory.ROUND_6, tcqs, tcqSolutions));

    tcqs = new ArrayList<Tcq>();
    tcqs.add(new Tcq("Round 7 A", TcqFactory.TCQ_7_A, "Social Science", "3:00"));
    tcqs.add(new Tcq("Round 7 B", TcqFactory.TCQ_7_B, "Geology", "5:00"));

    tcqSolutions = new ArrayList<Tcq>();
    tcqSolutions.add(new Tcq("Round 7 A Solutions", TcqFactory.TCQ_7_A_SOLUTIONS));
    tcqSolutions.add(new Tcq("Round 7 B Solutions", TcqFactory.TCQ_7_B_SOLUTIONS));

    this.availableRounds.add(new Round("Double Elimination / Round 2", RoundFactory.ROUND_7, tcqs, tcqSolutions));

    tcqs = new ArrayList<Tcq>();
    tcqs.add(new Tcq("Round 8 A", TcqFactory.TCQ_8_A, "Geology", "3:00"));
    tcqs.add(new Tcq("Round 8 B", TcqFactory.TCQ_8_B, "Chemistry", "3:00"));

    tcqSolutions = new ArrayList<Tcq>();
    tcqSolutions.add(new Tcq("Round 8 A Solutions", TcqFactory.TCQ_8_A_SOLUTIONS));
    tcqSolutions.add(new Tcq("Round 8 B Solutions", TcqFactory.TCQ_8_B_SOLUTIONS));

    this.availableRounds.add(new Round("Double Elimination / Round 3", RoundFactory.ROUND_8, tcqs, tcqSolutions));

    tcqs = new ArrayList<Tcq>();
    tcqs.add(new Tcq("Round 9 A", TcqFactory.TCQ_9_A, "Geology", "4:00"));
    tcqs.add(new Tcq("Round 9 B", TcqFactory.TCQ_9_B, "Marine Policy", "3:00"));

    tcqSolutions = new ArrayList<Tcq>();
    tcqSolutions.add(new Tcq("Round 9 A Solutions", TcqFactory.TCQ_9_A_SOLUTIONS));
    tcqSolutions.add(new Tcq("Round 9 B Solutions", TcqFactory.TCQ_9_B_SOLUTIONS));

    this.availableRounds.add(new Round("Double Elimination / Round 4", RoundFactory.ROUND_9, tcqs, tcqSolutions));

    tcqs = new ArrayList<Tcq>();
    tcqs.add(new Tcq("Round 10 A", TcqFactory.TCQ_10_A, "Biology", "4:00"));
    tcqs.add(new Tcq("Round 10 B", TcqFactory.TCQ_10_B, "Chemistry", "5:00"));

    tcqSolutions = new ArrayList<Tcq>();
    tcqSolutions.add(new Tcq("Round 10 A Solutions", TcqFactory.TCQ_10_A_SOLUTIONS));
    tcqSolutions.add(new Tcq("Round 10 B Solutions", TcqFactory.TCQ_10_B_SOLUTIONS));

    this.availableRounds.add(new Round("Double Elimination / Round 5", RoundFactory.ROUND_10, tcqs, tcqSolutions));

    tcqs = new ArrayList<Tcq>();
    tcqs.add(new Tcq("Round 11 A", TcqFactory.TCQ_11_A, "Physical", "5:00"));
    tcqs.add(new Tcq("Round 11 B", TcqFactory.TCQ_11_B, "Biology", "5:00"));

    tcqSolutions = new ArrayList<Tcq>();
    tcqSolutions.add(new Tcq("Round 11 A Solutions", TcqFactory.TCQ_11_A_SOLUTIONS));
    tcqSolutions.add(new Tcq("Round 11 B Solutions", TcqFactory.TCQ_11_B_SOLUTIONS));

    this.availableRounds.add(new Round("Double Elimination / Round 6", RoundFactory.ROUND_11, tcqs, tcqSolutions));
    
    tcqs = new ArrayList<Tcq>();
    tcqs.add(new Tcq("Round 12 A", TcqFactory.TCQ_12_A, "Physical", "5:00"));
    tcqs.add(new Tcq("Round 12 B", TcqFactory.TCQ_12_B, "Technology", "5:00"));

    tcqSolutions = new ArrayList<Tcq>();
    tcqSolutions.add(new Tcq("Round A Solutions", TcqFactory.TCQ_12_A_SOLUTIONS));
    tcqSolutions.add(new Tcq("Round B Solutions", TcqFactory.TCQ_12_B_SOLUTIONS));

    this.availableRounds.add(new Round("Double Elimination / Round 7", RoundFactory.ROUND_12, tcqs, tcqSolutions));

    tcqs = new ArrayList<Tcq>();
    tcqs.add(new Tcq("Round 13 A", TcqFactory.TCQ_13_A, "Biology", "4:00"));
    tcqs.add(new Tcq("Round 13 B", TcqFactory.TCQ_13_B, "Geography", "5:00"));

    tcqSolutions = new ArrayList<Tcq>();
    tcqSolutions.add(new Tcq("Round 13 A Solutions", TcqFactory.TCQ_13_A_SOLUTIONS));
    tcqSolutions.add(new Tcq("Round 13 B Solutions", TcqFactory.TCQ_13_B_SOLUTIONS));

    this.availableRounds.add(new Round("Double Elimination / Round 8", RoundFactory.ROUND_13, tcqs, tcqSolutions));

    tcqs = new ArrayList<Tcq>();
    tcqs.add(new Tcq("Round 14 A", TcqFactory.TCQ_14_A, "Social Science", "4:00"));
    tcqs.add(new Tcq("Round 14 B", TcqFactory.TCQ_14_B, "Chemistry", "4:00"));

    tcqSolutions = new ArrayList<Tcq>();
    tcqSolutions.add(new Tcq("Round 14 A Solutions", TcqFactory.TCQ_14_A_SOLUTIONS));
    tcqSolutions.add(new Tcq("Round 14 B Solutions", TcqFactory.TCQ_14_B_SOLUTIONS));

    this.availableRounds.add(new Round("Double Elimination / Round 9", RoundFactory.ROUND_14, tcqs, tcqSolutions));

    tcqs = new ArrayList<Tcq>();
    tcqs.add(new Tcq("Extra Round A", TcqFactory.TCQ_15_A, "Marine Policy", "3:00"));
    tcqs.add(new Tcq("Extra Round B", TcqFactory.TCQ_15_B, "Physical", "4:00"));

    tcqSolutions = new ArrayList<Tcq>();
    tcqSolutions.add(new Tcq("Extra Round A Solutions", TcqFactory.TCQ_15_A_SOLUTIONS));
    tcqSolutions.add(new Tcq("Extra B Solutions", TcqFactory.TCQ_15_B_SOLUTIONS));

    this.availableRounds.add(new Round("Extra Round", RoundFactory.EXTRA, tcqs, tcqSolutions));
    
    tcqs = new ArrayList<Tcq>();
    tcqs.add(new Tcq("Tiebreaker Round A", TcqFactory.TCQ_15_A, "Marine Policy", "3:00"));
    tcqs.add(new Tcq("Tiebreaker Round B", TcqFactory.TCQ_15_B, "Physical", "4:00"));

    tcqSolutions = new ArrayList<Tcq>();
    tcqSolutions.add(new Tcq("Tiebreaker Round A Solutions", TcqFactory.TCQ_15_A_SOLUTIONS));
    tcqSolutions.add(new Tcq("Tiebreaker B Solutions", TcqFactory.TCQ_15_B_SOLUTIONS));

    this.availableRounds.add(new Round("Tiebreaker Round", RoundFactory.TIEBREAKER, tcqs, tcqSolutions));

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

		EventBus.publish(new TeamNameEvent(this, this.teamA.getName(), this.teamB
				.getName()));
		EventBus.publish(new TeamScoreNumberEvent(this, this.teamA.getScore(),
				this.teamB.getScore()));

		this.currentRound = round;

		this.questionReader = new QuestionXMLParser(this.currentRound);
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
    }
    else {
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
	  JOptionPane.showMessageDialog(this.binder.getFrame(), "Password authentication failed.");
  }

  /**
   * Dialog to select which software packages to enable.
   */
	public void displayEula() {
		if (!this.hasReadTerms) {
			DisplayEulaDialog eulaDialog = new DisplayEulaDialog(this);
			eulaDialog.setVisible(true);
		} else {
			SelectPackageDialog dialog = new SelectPackageDialog(this.getFrame(), this.mostRecentPackageSelection);
			dialog.setVisible(true);
		}
	}
  
  public void setHasReadEula(boolean hasRead) {
	  this.hasReadTerms = hasRead;
  }

  /**
   * Proceeds to the next (appropriate) question. (If current question is
   * tossup, move to corresponding bonus. else, current question is bonus, move
   * to next tossup.)
   */
  public void nextQuestion(boolean enableTeamA, boolean enableTeamB, Turn turn) {
    this.scorekeeper.addTurn(turn);
    
    EventBus.publish(new UpdateQuestionEvent(this, this.currentRound.getNextQuestion(),
        enableTeamA, enableTeamB));
  }

  /**
   * Withdraws the most recent question.
   */
  public void previousQuestion() {
//    System.out.println("PREVIOUS");
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

      EventBus.publish(new UpdateQuestionEvent(this, this.currentRound.getPreviousQuestion(),
          enableTeamA, enableTeamB));
    }
  }

  /**
   * Flags the appropriate packages to use for the current round.
   * @param shouldUseScoreboard whether the scoreboard will be used this round
   * @param shouldUseTimer whether the timer will be used this round
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
    	System.out.println("[Handler/onEvent] received NewRoundEvent");
      NewRoundEvent nre = (NewRoundEvent) event;
      try {
    	  this.setCurrentRound(this.getRoundFromName(nre.getName()));
      }
      catch (NoRoundFoundException e) {
    	  System.err.println("no round called: " + e.getName() + " found!");
      }
    }
    else if (event instanceof TeamNameEvent) {
    	System.out.println("[Handler/onEvent] received TeamNameEvent");
      TeamNameEvent tne = (TeamNameEvent) event;
      this.teamA.setName(tne.getTeamAName());
      this.teamB.setName(tne.getTeamBName());
    }
    else if (event instanceof ShowSelectPackagesDialogEvent) {
    	System.out.println("[Handler/onEvent] received ShowSelectPackagesDialogEvent");
    	this.setHasReadEula(true);
    	this.showSelectPackagesDialog();
    }
    
    else if (event instanceof PackageSelectionListEvent) {
    	PackageSelectionListEvent e = (PackageSelectionListEvent) event;
    	this.mostRecentPackageSelection = e.getSelection();
    	System.out.println("[Handler/onEvent] received PackageSelectionListEvent -- \"" + this.getPackageSelectionString() + "\"");
    }
    else if (event instanceof PackagesSelectedEvent) {
    	System.out.println("[Handler/onEvent] received PackagesSelectedEvent -- \"" + this.getPackageSelectionString() + "\"");
    	if (this.mostRecentPackageSelection == BINDER_TIMEKEEPER || this.mostRecentPackageSelection == BINDER_TIMEKEEPER_SCOREKEEPER) {
    		this.shouldUseTimer = true;
    	}
    	else {
    		this.shouldUseTimer = false;
    	}
    	
    	if (this.mostRecentPackageSelection == BINDER_SCOREKEEPER || this.mostRecentPackageSelection == BINDER_TIMEKEEPER_SCOREKEEPER) {
    		this.shouldUseScoreboard = true;
    	}
    	else {
    		this.shouldUseScoreboard = false;
    	}
    	
    	this.showSelectRoundDialog();
    }
    else if (event instanceof RoundSelectionListEvent) {
    	RoundSelectionListEvent e = (RoundSelectionListEvent) event;
    	this.mostRecentRoundSelection = e.getName();
    	System.out.println("[Handler/onEvent] received RoundSelectionListEvent -- \"" + this.mostRecentRoundSelection + "\"");
    }
    else if (event instanceof RoundSelectedEvent) {
    	System.out.println("[Handler/onEvent] received RoundSelectedEvent -- \"" + this.mostRecentRoundSelection + "\"");
    	EventBus.publish(new NewRoundEvent(this, this.mostRecentRoundSelection));
    }
    else if (event instanceof RemindTcqEvent) {
    	System.out.println("[Handler/onEvent] received RemindTcqEvent");
    	this.showHalfwayDoneDialog();
    }
    else if (event instanceof OpenTcqPreambleEvent) {
    	System.out.println("[Handler/onEvent] received OpenTcqPreambleEvent");
    	this.showTcqPreambleDialog();
    }
    else if (event instanceof ShowBuzzerQuestionsEvent) {
    	System.out.println("[Handler/onEvent] received ShowBuzzerQuestionsEvent");
//    	UpdateQuestionEvent event2 = new UpdateQuestionEvent(this, this.currentRound.getNextQuestion(), true, true);
//    	EventBus.publish(event2);
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

		if (this.currentRound.getQuestions().size() != 40) {
			System.err.println("[handler#setCurrentRound] round: "
					+ this.currentRound.getName() + " did not parse correctly.");
			System.err.println("[handler#setCurrentRound] contains only "
					+ this.currentRound.getQuestions().size() + " questions");
		}

    this.timekeeper.close();
    this.timekeeper = new Timekeeper(this);

    this.scorekeeper = new Scorekeeper(this.teamA, this.teamB);

    if ((this.shouldUseScoreboard) || (this.shouldUseTimer)) {
      if (this.infoFrame != null) {
        this.infoFrame.close();
      }
      this.infoFrame = new InformationFrame(this.shouldUseScoreboard, this.shouldUseTimer);
    }
    else if (this.infoFrame != null) {
      this.infoFrame.close();
    }

    System.out.println("[handler/processNewRound] setting binder to new round");
    this.binder.setBinderToNewRound(this.currentRound.getName());

    System.out.println("[handler/processNewRound] firing update question event");
    EventBus.publish(new UpdateQuestionEvent(this, this.currentRound.getNextQuestion(), true, 
      true));		
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
	  SelectPackageDialog dialog = new SelectPackageDialog(this.getFrame(), this.mostRecentPackageSelection);
	  dialog.setVisible(true);
  }
	
	private void showSelectRoundDialog() {
    SelectRoundDialog dialog = new SelectRoundDialog(this.getFrame(), this.availableRounds);
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

	public static InputStream getResourceAsStream(String s){
  	System.out.println("input: " + s);
  	InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream(s);
  	System.out.println("input stream: " + is);
  	System.out.println("url: " + ClassLoader.getSystemClassLoader().getResource(s));
  	return is;
  }
}
