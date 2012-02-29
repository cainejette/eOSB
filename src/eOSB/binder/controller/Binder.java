package eOSB.binder.controller;

import java.awt.Color;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.font.TextAttribute;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextPane;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import net.miginfocom.swing.MigLayout;

import org.bushe.swing.event.EventBus;
import org.bushe.swing.event.EventServiceEvent;
import org.bushe.swing.event.EventSubscriber;

import eOSB.binder.ui.HideBuzzerQuestionsEvent;
import eOSB.binder.ui.RoundPreambleDialog;
import eOSB.binder.ui.ShowBuzzerQuestionsEvent;
import eOSB.binder.ui.actions.BackButtonAction;
import eOSB.binder.ui.actions.ConfirmExitListener;
import eOSB.binder.ui.actions.IncorrectAction;
import eOSB.binder.ui.actions.InterruptAction;
import eOSB.binder.ui.actions.OpenConfirmExitDialogAction;
import eOSB.binder.ui.actions.OpenPdfAction;
import eOSB.binder.ui.actions.OpenSetTeamNamesDialogAction;
import eOSB.binder.ui.actions.OpenValidateUserDialogAction;
import eOSB.binder.ui.actions.SetFontSizeAction;
import eOSB.binder.ui.actions.SubmitAction;
import eOSB.game.controller.GameState;
import eOSB.game.controller.Handler;
import eOSB.game.controller.NewRoundEvent;
import eOSB.game.controller.Question;
import eOSB.game.controller.Round;
import eOSB.game.data.IconFactory;
import eOSB.game.data.PdfFactory;
import eOSB.score.controller.TeamScoreNumberEvent;

/**
 * The eOSB UI, serving as a replacement for the traditional binder. Also
 * provides functionality for displaying the score and time.
 * 
 * @author Caine Jette
 * 
 */
public class Binder implements EventSubscriber<EventServiceEvent> {

	private Handler handler;
	private JFrame frame = new JFrame();
	private BufferedImage splash;

	private JButton teamA_correct;
	private JButton teamA_incorrect;
	private JButton teamA_interrupt;
	private ButtonState teamA;

	private JButton teamB_correct;
	private JButton teamB_incorrect;
	private JButton teamB_interrupt;
	private ButtonState teamB;

	private JButton backButton;
	private JButton submitButton;

	private TitledBorder teamABorder = new TitledBorder(this.teamAName);
	private TitledBorder teamBBorder = new TitledBorder(this.teamBName);
	private GameState gameState;
	private int fontSize = 16;
	
	private boolean hidingQuestions = false;
	private Question currentQuestion;
	private JScrollPane questionPanelScroller;
	private JScrollPane answerPanelScroller;
	
	private static final String REGULAR_STYLE = "regular";
	private static final String PRONUNCIATION_STYLE = "pronunciation";
	private static final String META_SMALL_STYLE = "metaSmall";
	private static final String META_BOLD_STYLE = "metaBold";
	
	private final String TAB = "            ";
	private final String HALF_TAB = "      ";

	private JLabel teamALabel = new JLabel();
	private JLabel teamBLabel = new JLabel();
	private String teamAName = "Team A";
	private String teamBName = "Team B";
	private int teamAScore = 0;
	private int teamBScore = 0;
	private boolean hasSeenTcqReminder = false;
	private boolean shouldEnableTcqs = false;
	
	private Dimension frameSize;
	private Point framePosition;

	/**
	 * @param handler
	 *          the {@link Handler} object
	 */
	public Binder(Handler handler) {
		this.handler = handler;

		EventBus.subscribe(TeamNameEvent.class, this);
		EventBus.subscribe(TeamScoreNumberEvent.class, this);
		EventBus.subscribe(UpdateQuestionEvent.class, this);
		EventBus.subscribe(NewRoundEvent.class, this);
		EventBus.subscribe(HideBuzzerQuestionsEvent.class, this);
		EventBus.subscribe(ShowBuzzerQuestionsEvent.class, this);
		EventBus.subscribe(UpdateAndShowQuestionEvent.class, this);
		EventBus.subscribe(OpenTcqPreambleEvent.class, this);

		final Font titleFont = new Font("Helvetica", Font.BOLD, 14);
		this.teamABorder.setTitleFont(titleFont);
		this.teamBBorder.setTitleFont(titleFont);
		this.updateBorders();

		this.initFrame();
		this.frame.setTitle("electronic Ocean Sciences Bowl");
		this.frame.setResizable(false);

		Container contentPane = this.frame.getContentPane();
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.LINE_AXIS));
		contentPane.add(this.createSplashPanel());

		this.displayFrame();
	}

	/**
	 * Initializes the frame's characteristics.
	 */
	private void initFrame() {
		this.frame.setVisible(false);
		this.frame.dispose();

		this.frame = new JFrame();
		this.frame.setIconImage(new ImageIcon(ClassLoader.getSystemResource(
				IconFactory.LOGO)).getImage());
		this.frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.frame.addWindowListener(new ConfirmExitListener(this.handler));
		this.frame.setJMenuBar(this.createMenuBar());
	}

	/**
	 * Packs and displays the frame.
	 */
	private void displayFrame() {
		this.frame.pack();
		this.frame.setMinimumSize(this.frame.getPreferredSize());
		
		// remember the old frameSize, or set to maximized for first run
		if (this.frameSize != null) {
			this.frame.setSize(this.frameSize);
		}
		else {
			this.frame.setExtendedState(Frame.MAXIMIZED_BOTH);
		}
		
		// remember the previous location, or set to centered for first run
		if (this.framePosition != null) {
			this.frame.setLocation(this.framePosition);
		}
		else {
			this.frame.setLocationRelativeTo(null);
		}
		this.frame.setVisible(true);
	}

	/**
	 * @return the menu bar for the {@link Binder}
	 */
	private JMenuBar createMenuBar() {
		JMenuBar menuBar = new JMenuBar();

		// file menu
		JMenu menu = new JMenu("File");
		menu.setMnemonic(KeyEvent.VK_F);

		// open
		JMenuItem menuItem = new JMenuItem();
		menuItem.setAction(new OpenValidateUserDialogAction(this.handler));
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O,
				ActionEvent.CTRL_MASK));
		menuItem.setMnemonic(KeyEvent.VK_R);
		menuItem.setText("Open Round");

		menu.add(menuItem);

		menuItem = new JMenuItem();
		menuItem.setAction(new FireTcqPreambleEventAction());
//		menuItem.setAction(new OpenTcqPreambleDialogAction(this.handler));
		menuItem.setText("Open TCQs");
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_T,
				ActionEvent.CTRL_MASK));
		menuItem.setMnemonic(KeyEvent.VK_T);
		menuItem.setEnabled(this.shouldEnableTcqs);

		menu.add(menuItem);
		menu.addSeparator();

		// exit
		menuItem = new JMenuItem();
		menuItem.setAction(new OpenConfirmExitDialogAction(this.handler));
		menuItem.setMnemonic(KeyEvent.VK_X);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q,
				ActionEvent.CTRL_MASK));
		menu.add(menuItem);

		menuBar.add(menu);

		// options menu
		menu = new JMenu("Options");
		menu.setMnemonic(KeyEvent.VK_O);

		// font size submenu
		JMenu fontSizeMenu = new JMenu("Font Size");
		fontSizeMenu.setEnabled(this.handler.getCurrentRound() != null);
		fontSizeMenu.setMnemonic(KeyEvent.VK_S);
		ButtonGroup buttonGroup = new ButtonGroup();

		JRadioButtonMenuItem radioItem = new JRadioButtonMenuItem("12 point");
		radioItem.setAction(new SetFontSizeAction(this, "12 point", 12));
		buttonGroup.add(radioItem);
		fontSizeMenu.add(radioItem);
		if (this.fontSize == 12) {
			radioItem.setSelected(true);
		}

		radioItem = new JRadioButtonMenuItem("14 point");
		radioItem.setAction(new SetFontSizeAction(this, "14 point", 14));
		radioItem.setSelected(true);
		buttonGroup.add(radioItem);
		fontSizeMenu.add(radioItem);
		if (this.fontSize == 14) {
			radioItem.setSelected(true);
		}

		radioItem = new JRadioButtonMenuItem("16 point");
		radioItem.setAction(new SetFontSizeAction(this, "16 point", 16));
		buttonGroup.add(radioItem);
		fontSizeMenu.add(radioItem);
		if (this.fontSize == 16) {
			radioItem.setSelected(true);
		}

		radioItem = new JRadioButtonMenuItem("18 point");
		radioItem.setAction(new SetFontSizeAction(this, "18 point", 18));
		buttonGroup.add(radioItem);
		fontSizeMenu.add(radioItem);
		if (this.fontSize == 18) {
			radioItem.setSelected(true);
		}
		
		radioItem = new JRadioButtonMenuItem("20 point");
		radioItem.setAction(new SetFontSizeAction(this, "20 point", 20));
		buttonGroup.add(radioItem);
		fontSizeMenu.add(radioItem);
		if (this.fontSize == 20) {
			radioItem.setSelected(true);
		}

		radioItem = new JRadioButtonMenuItem("24 point");
		radioItem.setAction(new SetFontSizeAction(this, "24 point", 24));
		buttonGroup.add(radioItem);
		fontSizeMenu.add(radioItem);
		if (this.fontSize == 24) {
			radioItem.setSelected(true);
		}

		menu.add(fontSizeMenu);
		menu.addSeparator();

		menuItem = new JMenuItem("Set Team Names");
		menuItem.setAction(new OpenSetTeamNamesDialogAction(this.handler));
		menuItem.setEnabled(this.handler.getCurrentRound() != null);
		menuItem.setMnemonic(KeyEvent.VK_N);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N,
				ActionEvent.CTRL_MASK));
		menu.add(menuItem);

		menuBar.add(menu);

		// help menu
		menu = new JMenu("Help");
		menu.setMnemonic(KeyEvent.VK_H);

		menuItem = new JMenuItem();
		menuItem.setAction(new OpenPdfAction(PdfFactory.COMPETITION_RULES));
		menuItem.setText("Competition Rules");
		menuItem.setMnemonic(KeyEvent.VK_C);
		menu.add(menuItem);
		menu.addSeparator();

		menuItem = new JMenuItem();
		menuItem.setAction(new OpenPdfAction(PdfFactory.USER_MANUAL));
		menuItem.setText("User Manual");
		menuItem.setMnemonic(KeyEvent.VK_U);
		menu.add(menuItem);

		menuItem = new JMenuItem();
		menuItem.setAction(new OpenPdfAction(PdfFactory.EULA_PDF));
		menuItem.setText("Terms of Agreement");
		menuItem.setMnemonic(KeyEvent.VK_T);
		menu.addSeparator();
		menu.add(menuItem);
		
		menuItem = new JMenuItem();
		menuItem.setAction(new OpenAboutEOSBAction(this.frame));
		menuItem.setText("About eOSB");
		menu.add(menuItem);

		menuBar.add(menu);

		return menuBar;
	}

	/**
	 * Sets the UI to display a new {@link Round}, as selected via File --> Open
	 * Round.
	 * 
	 * @param roundName
	 *          the name of the new round
	 */
	public void setBinderToNewRound(String roundName) {
		// save the frame's size and location
		if (!(roundName.equals("Round 1")) && !(roundName.equals("Six-question warm up"))) {
			this.frameSize = this.frame.getSize();
			this.framePosition = this.frame.getLocationOnScreen();
		}
		
		this.initFrame();
		this.initComponents();
		
		// disable TCQs on the Tiebreaker round
		if (roundName.contains("Tie")) {
			this.shouldEnableTcqs = false;
		}
		else {
			this.shouldEnableTcqs = true;
		}
		this.frame.setJMenuBar(this.createMenuBar());

		this.frame.setTitle(roundName
				+ " -- electronic Ocean Sciences Bowl");
		this.frame.setResizable(true);
		this.frame.addKeyListener(new ButtonKeyListener(this.teamA_correct,
				this.teamA_incorrect, this.teamA_interrupt, this.teamB_correct,
				this.teamB_incorrect, this.teamB_interrupt));

		JPanel panel = new JPanel();

		if (this.handler.isUsingTimer()) {
			panel.setLayout(new MigLayout("fill, insets 0"));
			panel.add(this.questionPanelScroller, "grow, pushy, dock north");
			panel.add(this.answerPanelScroller, "growx, sizegroup group1, wrap");
			panel.add(this.createTimePanel(), "dock east");
			panel.add(this.createSubmissionPanel(), "growx, sizegroup group1");
		} 
		else {
			JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
					this.questionPanelScroller, this.answerPanelScroller);
			splitPane.setResizeWeight(1);
			panel.setLayout(new MigLayout("wrap 1, fill, insets 0"));
			panel.add(splitPane, "grow, push, span");
			panel.add(this.createSubmissionPanel(), "growx, sizegroupx group1");
		}

		Container contentPane = this.frame.getContentPane();
		contentPane.setLayout(new MigLayout("fill, insets 0 5 5 5"));
		contentPane.add(panel, "grow");

		this.displayFrame();
	}

	/**
	 * @return the panel containing the splash image
	 */
	private JPanel createSplashPanel() {
		try {
			this.splash = ImageIO.read(ClassLoader.getSystemResource(
					IconFactory.SPLASH));
		} catch (IOException e) {
			e.printStackTrace();
		}

		JPanel panel = new JPanel() {
			@Override
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.drawImage(Binder.this.splash, 0, 0, null);
			}
		};

		panel.setPreferredSize(new Dimension(this.splash.getWidth(), this.splash
				.getHeight()));
		return panel;
	}

	/**
	 * @return the panel containing the timing-related components
	 */
	private JPanel createTimePanel() {
		final JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));

		JPanel subPanel = this.handler.getTimekeeper().getClockPanel(
				this.handler.getTimekeeper().getRoundClock());
		subPanel.setBorder(new TitledBorder("Round Timer"));
		panel.add(subPanel);

		subPanel = this.handler.getTimekeeper().getClockPanel(
				this.handler.getTimekeeper().getQuestionClock());
		subPanel.setBorder(new TitledBorder("Question Timer"));
		panel.add(subPanel);

		return panel;
	}

	/**
	 * @return the panel containing the team-associated (+scoring) and navigation
	 *         buttons
	 */
	private JPanel createSubmissionPanel() {
		final JPanel teamAPanel = new JPanel();
//		teamAPanel.setBorder(this.teamABorder);
		teamAPanel.setLayout(new MigLayout("wrap 2, fillx, insets 0"));
		this.teamA_interrupt.setMinimumSize(new Dimension(35, 35));

		this.updateBorders();
		
		teamAPanel.add(this.teamALabel, "span, wrap, align left, gaptop 5, gapleft 15");
		teamAPanel.add(this.teamA_interrupt, "span, growx");
		teamAPanel.add(this.teamA_correct, "sizegroupy group1y, sizegroupx group1x, growx");
		teamAPanel.add(this.teamA_incorrect, "sizegroupy group1y, sizegroupx group1x, growx");

		final JPanel teamBPanel = new JPanel();
		teamBPanel.setLayout(new MigLayout("wrap 2, fillx, insets 0"));
		this.teamB_interrupt.setMinimumSize(new Dimension(35, 35));
		
		teamBPanel.add(this.teamBLabel, "span, wrap, align left, gaptop 5, gapleft 15");
		teamBPanel.add(this.teamB_interrupt, "span, growx");
		teamBPanel.add(this.teamB_correct, "sizegroupy group2y, sizegroupx group2x, growx");
		teamBPanel.add(this.teamB_incorrect, "sizegroupy group2y, sizegroupx group2x, growx");

		final JPanel submitBackPanel = new JPanel();
		submitBackPanel.setLayout(new MigLayout("fillx, insets 0"));
		submitBackPanel.add(this.backButton, "sizegroup group3, growx");
		submitBackPanel.add(this.submitButton, "sizegroup group3, growx");

		JPanel answerPanel = new JPanel();
		answerPanel.setLayout(new MigLayout("wrap 2, fillx, insets 0"));
		answerPanel.add(teamAPanel, "growx");
		answerPanel.add(teamBPanel, "growx");
		answerPanel.add(submitBackPanel, "growx, span");

		return answerPanel;
	}

	/**
	 * Initializes the main components used by the UI
	 */
	private void initComponents() {
		this.initQuestionPanel();
		this.initAnswerPanel();
		
		this.teamA_correct = new JButton();
		this.teamA_incorrect = new JButton();
		this.teamA_interrupt = new JButton();
		this.teamA_interrupt.setMinimumSize(new Dimension(50, 30));
		this.teamA = new ButtonState(this.teamA_correct, this.teamA_incorrect,
				this.teamA_interrupt);

		this.teamB_correct = new JButton();
		this.teamB_incorrect = new JButton();
		this.teamB_interrupt = new JButton();
		this.teamB_interrupt.setMinimumSize(new Dimension(50, 30));
		this.teamB = new ButtonState(this.teamB_correct, this.teamB_incorrect,
				this.teamB_interrupt);

		this.submitButton = this.createNavigationButton();
		this.backButton = this.createNavigationButton();

		this.configureButtons();
		this.hasSeenTcqReminder = false;
//		this.setFontSize(16);
	}

	/**
	 * Initializing the components in the question panel
	 */
	private void initQuestionPanel() {
		JTextPane questionTextPane = new JTextPane();
		questionTextPane.setBorder(new TitledBorder("Question Text"));

		this.questionPanelScroller = new JScrollPane();
		this.questionPanelScroller.setPreferredSize(new Dimension(700, 300));
		this.questionPanelScroller.setMinimumSize(new Dimension(700, 200));
		this.questionPanelScroller.getViewport().setView(questionTextPane);
	}

	/**
	 * Initializing the components in the answer panel
	 */
	private void initAnswerPanel() {
		JTextPane answerTextPane = new JTextPane();
		answerTextPane.setBorder(new TitledBorder("Question Answer"));

		this.answerPanelScroller = new JScrollPane();
		this.answerPanelScroller.setPreferredSize(new Dimension(700, 110));
		this.answerPanelScroller.setMinimumSize(new Dimension(700, 75));
		this.answerPanelScroller.getViewport().setView(answerTextPane);
	}

	/**
	 * @return a Navigation button
	 */
	private JButton createNavigationButton() {
		JButton button = new JButton();
		button.setFont(new Font("Helvetica", Font.BOLD, 16));
		return button;
	}

	private void configureButtons() {
		SubmitAction submitAction = new SubmitAction(this.handler, this.teamA,
				this.teamB);
		IncorrectAction incorrectAction = new IncorrectAction(this.handler,
				this.teamA, this.teamB);

		ImageIcon correctIcon = new ImageIcon(ClassLoader.getSystemClassLoader().getResource(
				IconFactory.CORRECT));
		ImageIcon incorrectIcon = new ImageIcon(ClassLoader.getSystemClassLoader().getResource(
				IconFactory.INCORRECT));

		this.teamA_correct.setAction(submitAction);
		this.teamA_correct.setActionCommand("TEAM_A");
		this.teamA_correct.setIcon(correctIcon);
		this.teamA_correct.setFocusable(false);

		this.teamA_incorrect.setAction(incorrectAction);
		this.teamA_incorrect.setActionCommand("TEAM_A");
		this.teamA_incorrect.setIcon(incorrectIcon);
		this.teamA_incorrect.setFocusable(false);

		this.teamA_interrupt.setAction(new InterruptAction(this.teamA_interrupt));
		this.teamA_interrupt.setText("Interrupt");
		this.teamA_interrupt.setMinimumSize(new Dimension(50, 30));
		this.teamA_interrupt.setFocusable(false);

		this.teamB_correct.setAction(submitAction);
		this.teamB_correct.setActionCommand("TEAM_B");
		this.teamB_correct.setIcon(correctIcon);
		this.teamB_correct.setFocusable(false);

		this.teamB_incorrect.setAction(incorrectAction);
		this.teamB_incorrect.setActionCommand("TEAM_B");
		this.teamB_incorrect.setIcon(incorrectIcon);
		this.teamB_incorrect.setFocusable(false);

		this.teamB_interrupt.setAction(new InterruptAction(this.teamB_interrupt));
		this.teamB_interrupt.setText("Interrupt");
		this.teamB_interrupt.setFocusable(false);

		this.submitButton.setAction(submitAction);
		this.submitButton.setIcon(new ImageIcon(ClassLoader.getSystemClassLoader().getResource(
				IconFactory.NEXT)));
		this.submitButton.setText("Submit");
		this.submitButton.setHorizontalTextPosition(SwingConstants.LEFT);
		this.submitButton.setFocusable(false);

		this.backButton.setAction(new BackButtonAction(this.handler));
		this.backButton.setText("Back");
		this.backButton.setIcon(new ImageIcon(ClassLoader.getSystemClassLoader().getResource(
				IconFactory.BACK)));
		this.backButton.setHorizontalTextPosition(SwingConstants.RIGHT);
		this.backButton.setFocusable(false);
	}

	/**
	 * @return the frame
	 */
	public JFrame getFrame() {
		return this.frame;
	}

	/**
	 * Enables Team A's buttons
	 */
	private void enableTeamA() {
		this.teamA_correct.setEnabled(true);
		this.teamA_incorrect.setEnabled(true);

		if (this.handler.isUsingScoreboard()) {
			this.teamA_interrupt.setEnabled(true);
		}
	}

	/**
	 * Disable Team A's buttons
	 */
	private void disableTeamA() {
		this.teamA_correct.setSelected(false);
		this.teamA_correct.setEnabled(false);
		this.teamA_incorrect.setSelected(false);
		this.teamA_incorrect.setEnabled(false);

		if (this.handler.isUsingScoreboard()) {
			this.teamA_interrupt.setSelected(false);
			this.teamA_interrupt.setEnabled(false);
		}
	}

	/**
	 * Enable Team B's buttons
	 */
	private void enableTeamB() {
		this.teamB_correct.setEnabled(true);
		this.teamB_incorrect.setEnabled(true);

		if (this.handler.isUsingScoreboard()) {
			this.teamB_interrupt.setEnabled(true);
		}
	}

	/**
	 * Disable Team B's buttons
	 */
	private void disableTeamB() {
		this.teamB_correct.setSelected(false);
		this.teamB_correct.setEnabled(false);
		this.teamB_incorrect.setSelected(false);
		this.teamB_incorrect.setEnabled(false);

		if (this.handler.isUsingScoreboard()) {
			this.teamB_interrupt.setSelected(false);
			this.teamB_interrupt.setEnabled(false);
		}
	}
	
	/**
	 * Updates the question displayed.
	 * @param question the new question
	 */
	private void updateQuestion(Question question) {
			System.out.println("[binder/updateQuestion] updating question to " + question.getNumber());
			
			this.currentQuestion = question;
			
			if (question.getNumber().equals("11") && !this.hasSeenTcqReminder) {
				this.hideQuestions();

				RemindTcqEvent event = new RemindTcqEvent(this);
				EventBus.publish(event);
				this.hasSeenTcqReminder = true;
			}
			
			// update question text and answer panels and buttons
			this.createQuestionPanel(question);
			this.createAnswerPanel(question);
			this.updateButtonStates(question);
			
			// update team scores if scorekeeping package enabled
//		this.updateTeamScores();
			
			// update question clock if timekeeping package enabled
		this.updateQuestionClock(question);
	}
	
	/**
	 * Updates the clocks for the given question
	 * @param question
	 */
	private void updateQuestionClock(Question question) {
		if (this.handler.isUsingTimer()) {
			if (question.getType() == Question.Type.TOSSUP) {
				this.handler.getTimekeeper().getQuestionClock().setTime(5000, true);
				this.handler.getTimekeeper().getQuestionClock().clearThresholds();
			} else {
				this.handler.getTimekeeper().getQuestionClock().setTime(20000, true);
				this.handler.getTimekeeper().getQuestionClock().addThreshold(5000);
			}
		}
	}

	/**
	 * Creates a new text panel to display the question 
	 * @param question the new question 
	 */
	private void createQuestionPanel(Question question) {
		this.questionPanelScroller.getViewport().removeAll();
		
		JTextPane textPane = this.createJTextPane(this.getQuestionBackground(question));
//		textPane.setBorder(new TitledBorder(new EtchedBorder(), "Question " + question.getNumber()));
//		textPane.setBorder(new LineBorder(Color.GRAY, 10 ,true));

		StyledDocument document = this.configureStyledDocument(textPane);

		try {
			// formats and inserts the question metadata to precede question content
			String questionNumber = "Question " + question.getNumber() + " is a ";
			String questionType = question.getType().toString().toLowerCase() + ", " + question.getFormat();
			String questionCategoryAndFormat =  question.getCategory().toLowerCase() + ":";
			document.insertString(document.getLength(), questionNumber, document.getStyle(META_SMALL_STYLE));
			document.insertString(document.getLength(), questionType, document.getStyle(META_BOLD_STYLE));
			document.insertString(document.getLength(), " in ", document.getStyle(META_SMALL_STYLE));
			document.insertString(document.getLength(), questionCategoryAndFormat, document.getStyle(META_BOLD_STYLE));

			// inserts the question text
			this.addTextToDocument("\n\n" + HALF_TAB + question.getText(), document);

			// inserts the question choices
			List<String> questionChoices = question.getAnswerOptions();
			List<String> questionChoicePrefixes = new ArrayList<String>();
			questionChoicePrefixes.add("W");
			questionChoicePrefixes.add("X");
			questionChoicePrefixes.add("Y");
			questionChoicePrefixes.add("Z");

			for (int i = 0; i < questionChoices.size(); i++) {
				this.addTextToDocument("\n\n" + TAB + questionChoicePrefixes.get(i) + ") " + questionChoices.get(i), document);
			}
		} 
		catch (BadLocationException e) {
			e.printStackTrace();
		}

		textPane.setCaretPosition(0);
		this.questionPanelScroller.getViewport().setView(textPane);
	}
	
	/**
	 * Creates a new text panel to display the question answer.
	 */
	private void createAnswerPanel(Question question) {
		this.answerPanelScroller.getViewport().remove(0);
		
		JTextPane textPane = this.createJTextPane(this.getQuestionBackground(question));
//		textPane.setBorder(new TitledBorder("Correct Answer"));
		
		StyledDocument document = this.configureStyledDocument(textPane);
		
		String textToAdd = "";
		if (question.getFormat() == Question.Format.MULTIPLE_CHOICE) {
			String answerLetter = question.getCorrectAnswers().get(0).trim();
			if (answerLetter.equals("W")) {
				textToAdd = "W) " + question.getAnswerOptions().get(0);
			}
			else if (answerLetter.equals("X")) {
				textToAdd = "X) " + question.getAnswerOptions().get(1);
			}
			else if (answerLetter.equals("Y")) {
				textToAdd = "Y) " + question.getAnswerOptions().get(2);
			}
			else if (answerLetter.equals("Z")) {
				textToAdd = "Z) " + question.getAnswerOptions().get(3);
			}
		}
		else {
			textToAdd = question.getCorrectAnswers().get(0).trim();
		}
		
		try {
			document.insertString(document.getLength(), "Correct answer:", document.getStyle(META_SMALL_STYLE));
			this.addTextToDocument("\n\n" + this.TAB + textToAdd, document);
		} 
		catch (BadLocationException e) {
			e.printStackTrace();
		}
		
		textPane.setCaretPosition(0);
		this.answerPanelScroller.getViewport().setView(textPane);
	}
	
	/**
	 * Updates the various buttons based on the type of question
	 * @param question the current question, to update button states against
	 */
	private void updateButtonStates(Question question) {
		// disable back button if viewing the very first question
		this.backButton.setEnabled(!question.getNumber().equals("1"));

		this.submitButton.setEnabled(true);

		// ensure both teams enabled if tossup
		if (question.getType() == Question.Type.TOSSUP) {
			this.toggleTeamButtons(true, true);
			this.toggleTeamButtons(true, false);
		}
	}

	/**
	 * Toggles the state of the team buttons
	 * @param shouldEnable whether the buttons should be enabled or not
	 * @param isTeamA whether the buttons to operate on are TeamA or TeamB
	 */
	private void toggleTeamButtons(boolean shouldEnable, boolean isTeamA) {
		if (isTeamA) {
			this.teamA_correct.setEnabled(shouldEnable);
			this.teamA_incorrect.setEnabled(shouldEnable);
			if (this.handler.isUsingScoreboard()) {
				this.teamA_interrupt.setEnabled(shouldEnable);
			}
		}
		else {
			this.teamB_correct.setEnabled(shouldEnable);
			this.teamB_incorrect.setEnabled(shouldEnable);
			if (this.handler.isUsingScoreboard()) {
				this.teamB_interrupt.setEnabled(shouldEnable);
			}
		}
	}

	/**
	 * Adds the text to the question doc
	 * @param text the text to add
	 * @throws BadLocationException
	 */
	private void addTextToDocument(String text, StyledDocument document) throws BadLocationException {
		boolean done = false;
		while (!done) {
			if (text.contains("[") && text.contains("]")) {
				int pronunciationStart = text.indexOf("[");
				int pronunciationEnd = text.indexOf("]");
				
				document.insertString(document.getLength(),
						text.substring(0, pronunciationStart), document.getStyle(REGULAR_STYLE));
				document.insertString(document.getLength(),
						text.substring(pronunciationStart, pronunciationEnd + 1),
						document.getStyle(PRONUNCIATION_STYLE));
				text = text.substring(pronunciationEnd + 1);
			} 
			else {
				done = true;
			}
		}
		document.insertString(document.getLength(), text, document.getStyle(REGULAR_STYLE));
	}

	
	/**
	 * Configures and returns the styled document for a given textPane
	 * @param textPane the input text pane
	 * @return a styled document loaded with styles
	 */
	private StyledDocument configureStyledDocument(JTextPane textPane) {
		StyledDocument document = (StyledDocument) textPane.getDocument();
		
		// add the various formatting styles to the document
		Style regular = document.addStyle(REGULAR_STYLE, null);
		StyleConstants.setFontFamily(regular, "Helvetica");
		StyleConstants.setFontSize(regular, this.fontSize);

		Style pronunciation = document.addStyle(PRONUNCIATION_STYLE, null);
		StyleConstants.setFontFamily(pronunciation, "Helvetica");
		StyleConstants.setFontSize(pronunciation, this.fontSize - 2);
		StyleConstants.setForeground(pronunciation, Color.GRAY);
		
		Style metaRegular = document.addStyle(META_SMALL_STYLE, null);
		StyleConstants.setFontFamily(metaRegular, "Helvetica");
		StyleConstants.setFontSize(metaRegular, this.fontSize - 2);
		StyleConstants.setForeground(metaRegular, Color.GRAY);
		
		Style metaBold = document.addStyle(META_BOLD_STYLE, null);
		StyleConstants.setFontFamily(metaBold, "Helvetica");
		StyleConstants.setFontSize(metaBold, this.fontSize - 2);
		StyleConstants.setForeground(metaBold, Color.GRAY);
		StyleConstants.setBold(metaBold, true);

		return document;
	}

	
	/**
	 * Creates a formatted JTextPane
	 * @param background background color for the jTextPane
	 * @return a formatted JTextPane
	 */
	private JTextPane createJTextPane(Color background) {
		JTextPane textPane = new JTextPane();
		textPane.setEditable(false);
		textPane.setMargin(new Insets(10, 10, 10, 10));
		textPane.setBackground(background);
		textPane.setCaretPosition(0);
		return textPane;
	}
	
	/**
	 * Returns a white background if question is tossup, yellow if question is bonus
	 * @param question the input question
	 * @return the appropriate background color
	 */
	private Color getQuestionBackground(Question question) {
		if (question.getNumber().contains("b")) {
			return new Color(255, 255, 200);
		} 
		else {
			return Color.WHITE;
		}
	}
	
	/**
	 * Disables the back button
	 */
	public void disableBackButton() {
		this.backButton.setSelected(false);
		this.backButton.setEnabled(false);
	}

	/**
	 * Updates UI to represent the end of a round
	 */
	private void roundOver() {
		this.currentQuestion = null;
		final JButton newRoundButton = new JButton("Round over! Open a new round here.");
		newRoundButton.setFont(new Font("Helvetica", Font.PLAIN, this.fontSize));
		newRoundButton.setBorderPainted(false);
		newRoundButton.setContentAreaFilled(false);
		newRoundButton.setAction(new OpenValidateUserDialogAction(this.handler));
		newRoundButton.setText("Round over! Open a new round here.");
		newRoundButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

		newRoundButton.addMouseListener(new java.awt.event.MouseAdapter() {
			Font originalFont = null;
	 
	    public void mouseEntered(java.awt.event.MouseEvent evt) {
	        originalFont = newRoundButton.getFont();
	        Map attributes = originalFont.getAttributes();
	        attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
	        newRoundButton.setFont(originalFont.deriveFont(attributes));
	    }
	 
	    public void mouseExited(java.awt.event.MouseEvent evt) {
	        newRoundButton.setFont(originalFont);
	    }
		});

		this.questionPanelScroller.getViewport().remove(0);

		JTextPane questionPane = this.createJTextPane(Color.WHITE);
		StyledDocument document = this.configureStyledDocument(questionPane);

		try {
			this.addTextToDocument("\n\n\n\n\n" + this.HALF_TAB, document);
			questionPane.insertComponent(newRoundButton);
		} 
		catch (BadLocationException e) {
			e.printStackTrace();
		}
		
		this.questionPanelScroller.getViewport().add(questionPane);

		this.answerPanelScroller.getViewport().remove(0);
		JTextPane answerPane = this.createJTextPane(Color.WHITE);
		this.answerPanelScroller.getViewport().add(answerPane);

		this.toggleTeamButtons(false, true);
		this.toggleTeamButtons(false, false);
		this.submitButton.setEnabled(false);
	}


	/**
	 * Sets the size of the font
	 * @param size the new size
	 */
	public void setFontSize(int size) {
		System.out.println("[Binder/setFontSize] setting font size to: " + size);
		this.fontSize = size;
		
		this.updateQuestion(this.currentQuestion);
	}

	/**
	 * Disposes of the frame.
	 */
	public void close() {
		this.frame.dispose();
	}

	/** {@inheritDoc} */
	// @Override
	public void onEvent(EventServiceEvent ese) {
		if (ese instanceof TeamNameEvent) {
			System.out.println("[Binder/onEvent] received TeamNameEvent");
			TeamNameEvent tne = (TeamNameEvent) ese;
			this.teamAName = tne.getTeamAName();
			this.teamBName = tne.getTeamBName();
			this.updateBorders();
		} 
		else if (ese instanceof TeamScoreNumberEvent) {
			System.out.println("[Binder/onEvent] received TeamScoreNumberEvent");
			TeamScoreNumberEvent tsne = (TeamScoreNumberEvent) ese;
			this.teamAScore = tsne.getTeamAScore();
			this.teamBScore = tsne.getTeamBScore();
			this.updateBorders();
		} 
		else if (ese instanceof UpdateAndShowQuestionEvent) {
			System.out.println("[Binder/onEvent] received UpdateAndShowQuestionEvent");
			this.showQuestions();
			
			UpdateAndShowQuestionEvent aqe = (UpdateAndShowQuestionEvent) ese;
			Question question = aqe.getQuestion();

			if (question != null) {
				this.resetButtons();

				if (aqe.shouldEnableTeamA())
					this.toggleTeamButtons(true, true);
				else {
					this.toggleTeamButtons(false, true);
				}

				if (aqe.shouldEnableTeamB())
					this.toggleTeamButtons(true, false);
				else {
					this.toggleTeamButtons(false, false);
				}

				this.updateQuestion(question);
			} 
			else {
				this.roundOver();
			}
		}
		else if (ese instanceof UpdateQuestionEvent) {
			System.out.println("[Binder/onEvent] received UpdateQuestionEvent");
			UpdateQuestionEvent aqe = (UpdateQuestionEvent) ese;
			Question question = aqe.getQuestion();

			if (question != null) {
				this.resetButtons();

				if (aqe.shouldEnableTeamA())
					this.toggleTeamButtons(true, true);
				else {
					this.toggleTeamButtons(false, true);
				}

				if (aqe.shouldEnableTeamB())
					this.toggleTeamButtons(true, false);
				else {
					this.toggleTeamButtons(false, false);
				}

				this.updateQuestion(question);
			} 
			else {
				this.roundOver();
			}
		} 
		else if (ese instanceof NewRoundEvent) {
			System.out.println("[Binder/onEvent] received NewRoundEvent");
			this.displayRoundPreamble();
		}
		else if (ese instanceof HideBuzzerQuestionsEvent) {
			System.out.println("[Binder/onEvent] hiding buzzer questions");
			this.hideQuestions();
		}
		else if (ese instanceof ShowBuzzerQuestionsEvent) {
			System.out.println("[Binder/onEvent] showing buzzer questions");
			this.showQuestions();
		}
		else if (ese instanceof OpenTcqPreambleEvent) {
			this.hasSeenTcqReminder = true;
		}
	}

	private void displayRoundPreamble() {
		RoundPreambleDialog dialog = new RoundPreambleDialog(this.handler);
		dialog.setVisible(true);
	}

	private void resetButtons() {
		this.teamA_correct.setSelected(false);
		this.teamA_incorrect.setSelected(false);
		this.teamA_interrupt.setSelected(false);

		this.teamA_correct.setEnabled(false);
		this.teamA_incorrect.setEnabled(false);
		this.teamA_interrupt.setEnabled(false);

		this.teamB_correct.setSelected(false);
		this.teamB_incorrect.setSelected(false);
		this.teamB_interrupt.setSelected(false);

		this.teamB_correct.setEnabled(false);
		this.teamB_incorrect.setEnabled(false);
		this.teamB_interrupt.setEnabled(false);

		this.backButton.setSelected(false);
		this.submitButton.setSelected(false);
	}

	private void updateBorders() {
		String labelA = this.teamAName;
		String labelB = this.teamBName;
		
		if (this.handler.isUsingScoreboard()) {
			labelA += ": " + this.teamAScore;
			labelB += ": " + this.teamBScore;
		}
		
		this.teamALabel.setText(labelA);
		this.teamALabel.setFont(new Font("Helvetica", Font.BOLD, 20));
		this.teamBLabel.setText(labelB);
		this.teamBLabel.setFont(new Font("Helvetica", Font.BOLD, 20));

		this.frame.repaint();
	}

	private void showQuestions() {
		System.out.println("[Binder/showQuestions] showing questions");
		
		this.teamA_correct.setEnabled(this.gameState.isTeamACorrectEnabled());
		this.teamA_incorrect.setEnabled(this.gameState.isTeamAIncorrectEnabled());
		this.teamA_interrupt.setEnabled(this.gameState.isTeamAInterruptEnabled());

		this.teamB_correct.setEnabled(this.gameState.isTeamBCorrectEnabled());
		this.teamB_incorrect.setEnabled(this.gameState.isTeamBIncorrectEnabled());
		this.teamB_interrupt.setEnabled(this.gameState.isTeamBInterruptEnabled());

		this.backButton.setEnabled(this.gameState.isBackEnabled());
		this.submitButton.setEnabled(this.gameState.isSubmitEnabled());

		this.hidingQuestions = false;
		this.frame.setState(Frame.NORMAL);
	}

	private void hideQuestions() {
		if (!this.hidingQuestions) {
			this.hidingQuestions = true;
			this.frame.setState(Frame.ICONIFIED);
			System.out.println("[Binder/hideQuestions] hiding questions");
			this.gameState = new GameState(this.teamA_correct.isEnabled(),
					this.teamA_incorrect.isEnabled(), this.teamA_interrupt.isEnabled(),
					this.teamB_correct.isEnabled(), this.teamB_incorrect.isEnabled(),
					this.teamB_interrupt.isEnabled(), this.backButton.isEnabled(),
					this.submitButton.isEnabled());
	
			this.disableTeamA();
			this.disableTeamB();
	
			this.disableBackButton();
			this.submitButton.setEnabled(false);
		}
	}
}
