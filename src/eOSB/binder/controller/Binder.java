package eOSB.binder.controller;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;

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
import eOSB.game.controller.Question.Format;
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

	private JTextArea currentQuestionText;
	private JTextArea currentQuestionAnswer;

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

	private String teamAName = "Team A";
	private String teamBName = "Team B";
	private int teamAScore = 0;
	private int teamBScore = 0;

	private TitledBorder teamABorder = new TitledBorder(this.teamAName);
	private TitledBorder teamBBorder = new TitledBorder(this.teamBName);
	private GameState gameState;
	private int fontSize = 16;
	
	private boolean hidingQuestions = false;

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
		this.frame.setLocationRelativeTo(null);
		this.frame.setMinimumSize(this.frame.getPreferredSize());
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
		this.initFrame();
		this.initComponents();

		this.frame.setTitle(roundName
				+ " -- electronic Ocean Sciences Bowl");
		this.frame.setResizable(true);
		this.frame.addKeyListener(new ButtonKeyListener(this.teamA_correct,
				this.teamA_incorrect, this.teamA_interrupt, this.teamB_correct,
				this.teamB_incorrect, this.teamB_interrupt));

		JPanel panel = new JPanel();

		if (this.handler.isUsingTimer()) {
			panel.setLayout(new MigLayout("fill, insets 0"));
			panel.add(this.createQuestionTextPanel(), "grow, pushy, dock north");
			panel.add(this.createQuestionAnswerPanel(), "growx, sizegroup group1, wrap");
			panel.add(this.createTimePanel(), "dock east");
			panel.add(this.createAnswerPanel(), "growx, sizegroup group1");
		} 
		else {
			panel.setLayout(new MigLayout("wrap 1, fill, insets 0"));
			panel.add(this.createQuestionTextPanel(), "grow, pushy, sizegroupx group1");
			panel.add(this.createQuestionAnswerPanel(), "growx, sizegroupx group1");
			panel.add(this.createAnswerPanel(), "growx, sizegroupx group1, sizegroupy group2");
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
	 * @return the panel containing the question text component
	 */
	private JPanel createQuestionTextPanel() {
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder("Question Text"));
		panel.setLayout(new MigLayout("insets 0, fill"));

		JScrollPane scrollPane = new JScrollPane(this.currentQuestionText);
		scrollPane.setPreferredSize(new Dimension(600, 250));
		panel.add(scrollPane, "grow");

		return panel;
	}

	/**
	 * @return the panel containing the question answer component
	 */
	private JPanel createQuestionAnswerPanel() {
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder("Correct Answer"));
		panel.setLayout(new MigLayout("insets 0, fill"));

		JScrollPane scrollPane = new JScrollPane(this.currentQuestionAnswer);
		scrollPane.setPreferredSize(new Dimension(700, 75));
		scrollPane.setMinimumSize(new Dimension(700, 70));
		panel.add(scrollPane, "grow");

		return panel;
	}

	/**
	 * @return the panel containing the team-associated (+scoring) and navigation
	 *         buttons
	 */
	private JPanel createAnswerPanel() {
		final JPanel teamAPanel = new JPanel();
		teamAPanel.setBorder(this.teamABorder);
		teamAPanel.setLayout(new MigLayout("wrap 2, fillx, insets 0"));
		this.teamA_interrupt.setMinimumSize(new Dimension(35, 35));
		teamAPanel.add(this.teamA_interrupt, "span, growx");
		teamAPanel.add(this.teamA_correct,
				"sizegroupy group1y, sizegroupx group1x, growx");
		teamAPanel.add(this.teamA_incorrect,
				"sizegroupy group1y, sizegroupx group1x, growx");

		final JPanel teamBPanel = new JPanel();
		teamBPanel.setBorder(this.teamBBorder);
		teamBPanel.setLayout(new MigLayout("wrap 2, fillx, insets 0"));
		this.teamB_interrupt.setMinimumSize(new Dimension(35, 35));
		teamBPanel.add(this.teamB_interrupt, "span, growx");
		teamBPanel.add(this.teamB_correct,
				"sizegroupy group2y, sizegroupx group2x, growx");
		teamBPanel.add(this.teamB_incorrect,
				"sizegroupy group2y, sizegroupx group2x, growx");

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
		this.currentQuestionText = new JTextArea();
		this.currentQuestionText.setEditable(false);
		this.currentQuestionText.setLineWrap(true);
		this.currentQuestionText.setWrapStyleWord(true);
		this.currentQuestionText.setMargin(new Insets(5, 5, 5, 5));

		this.currentQuestionAnswer = new JTextArea();
		this.currentQuestionAnswer.setEditable(false);
		this.currentQuestionAnswer.setLineWrap(true);
		this.currentQuestionAnswer.setWrapStyleWord(true);
		this.currentQuestionAnswer.setMargin(new Insets(5, 5, 5, 5));

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
		this.setFontSize(16);
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

		this.teamA_incorrect.setAction(incorrectAction);
		this.teamA_incorrect.setActionCommand("TEAM_A");
		this.teamA_incorrect.setIcon(incorrectIcon);

		this.teamA_interrupt.setAction(new InterruptAction(this.teamA_interrupt));
		this.teamA_interrupt.setText("Interrupt");
		this.teamA_interrupt.setMinimumSize(new Dimension(50, 30));

		this.teamB_correct.setAction(submitAction);
		this.teamB_correct.setActionCommand("TEAM_B");
		this.teamB_correct.setIcon(correctIcon);

		this.teamB_incorrect.setAction(incorrectAction);
		this.teamB_incorrect.setActionCommand("TEAM_B");
		this.teamB_incorrect.setIcon(incorrectIcon);

		this.teamB_interrupt.setAction(new InterruptAction(this.teamB_interrupt));
		this.teamB_interrupt.setText("Interrupt");

		this.submitButton.setAction(submitAction);
		this.submitButton.setIcon(new ImageIcon(ClassLoader.getSystemClassLoader().getResource(
				IconFactory.NEXT)));
		this.submitButton.setText("Submit");
		this.submitButton.setHorizontalTextPosition(SwingConstants.LEFT);

		this.backButton.setAction(new BackButtonAction(this.handler));
		this.backButton.setText("Back");
		this.backButton.setIcon(new ImageIcon(ClassLoader.getSystemClassLoader().getResource(
				IconFactory.BACK)));
		this.backButton.setHorizontalTextPosition(SwingConstants.RIGHT);
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
	 * Updates the binder to display a new {@link Question}.
	 * 
	 * @param question
	 *          the question to display on the binder
	 */
	private void updateQuestion(Question question, boolean shouldEnableTeamA, boolean shouldEnableTeamB) {
			System.out.println("new question: " + question);

			this.resetButtons();

			// set Team A's buttons
			if (shouldEnableTeamA) {
				this.enableTeamA();
			} else {
				this.disableTeamA();
			}

			// set Team B's buttons
			if (shouldEnableTeamB) {
				this.enableTeamB();
			} else {
				this.disableTeamB();
			}

			final String spacer = "          ";

			String questionTextString = "QUESTION #";
			String questionAnswerString = "";

			questionTextString += question.getNumber();
			questionTextString += " is a " + question.getType() + ", "
					+ question.getFormat() + ", " + question.getCategory().toUpperCase()
					+ ":";
			questionTextString += "\n\n" + spacer + question.getText();

			if (question.getFormat().equals(Format.MULTIPLE_CHOICE)) {
				questionTextString += "\n\n" + spacer + spacer + "W -- "
						+ question.getAnswerOptions().get(0);
				questionTextString += "\n\n" + spacer + spacer + "X -- "
						+ question.getAnswerOptions().get(1);
				questionTextString += "\n\n" + spacer + spacer + "Y -- "
						+ question.getAnswerOptions().get(2);
				questionTextString += "\n\n" + spacer + spacer + "Z -- "
						+ question.getAnswerOptions().get(3);

				/** populate the correct answer text field */
				questionAnswerString += spacer + spacer
						+ question.getCorrectAnswers().get(0);
				if (question.getCorrectAnswers().get(0).contains("W"))
					questionAnswerString += " -- " + question.getAnswerOptions().get(0);
				else if (question.getCorrectAnswers().get(0).contains("X"))
					questionAnswerString += " -- " + question.getAnswerOptions().get(1);
				else if (question.getCorrectAnswers().get(0).contains("Y"))
					questionAnswerString += " --  " + question.getAnswerOptions().get(2);
				else if (question.getCorrectAnswers().get(0).contains("Z"))
					questionAnswerString += " -- " + question.getAnswerOptions().get(3);
			} else {
				questionAnswerString += spacer + spacer
						+ question.getCorrectAnswers().get(0);
			}

			this.currentQuestionText.setText(questionTextString);
			this.currentQuestionText.setFont(new Font("Helvetica", Font.PLAIN,
					this.fontSize));
			this.currentQuestionAnswer.setText(questionAnswerString);
			this.currentQuestionAnswer.setFont(new Font("Helvetica", Font.PLAIN,
					this.fontSize));

			if (question.getNumber().equals("1")) {
				this.backButton.setEnabled(false);
			} else {
				this.backButton.setEnabled(true);
			}

			this.submitButton.setEnabled(true);

			if (question.getType() == Question.Type.TOSSUP) {
				// both teams get to answer a tossup
				this.enableTeamA();
				this.enableTeamB();

				// color the background white for a tossup
				this.currentQuestionText.setBackground(Color.WHITE);
				this.currentQuestionAnswer.setBackground(Color.WHITE);
			} else {
				this.currentQuestionText.setBackground(new Color(255, 255, 150));
				this.currentQuestionAnswer.setBackground(new Color(255, 255, 150));
			}

			this.updateBorders();

			if (this.handler.isUsingTimer()) {
				if (question.getType() == Question.Type.TOSSUP) {
					this.handler.getTimekeeper().getQuestionClock().setTime(5000, true);
					this.handler.getTimekeeper().getQuestionClock().clearThresholds();
				} else {
					this.handler.getTimekeeper().getQuestionClock().setTime(20000, true);
					this.handler.getTimekeeper().getQuestionClock().addThreshold(5000);
				}
			}
			
			if (question.getNumber().equals("11")) {
				this.hideQuestions();

				RemindTcqEvent event = new RemindTcqEvent(this);
				EventBus.publish(event);
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
	 * Displays end of round text in the question area
	 */
	public void roundOver() {
		String text = "Round over! Select new round from file menu.";
		this.currentQuestionText.setText(text);
		this.currentQuestionAnswer.setText("");
		this.disableTeamA();
		this.disableTeamB();
		this.submitButton.setEnabled(false);
	}

	/**
	 * Sets the font size of the question area
	 * 
	 * @param size
	 *          the new font size
	 */
	public void setFontSize(int size) {
		this.fontSize  = size;
		Font currentFont = currentQuestionText.getFont();
		Font newFont = new Font("Helvetica", currentFont.getStyle(), this.fontSize);
		this.currentQuestionText.setFont(newFont);
		this.currentQuestionAnswer.setFont(newFont);
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
			boolean shouldEnableTeamA = aqe.shouldEnableTeamA();
			boolean shouldEnableTeamB = aqe.shouldEnableTeamB();

			if (question != null) {
				this.updateQuestion(question, shouldEnableTeamA, shouldEnableTeamB);
			} 
			else {
				this.roundOver();
			}
		}
		else if (ese instanceof UpdateQuestionEvent) {
			System.out.println("[Binder/onEvent] received UpdateQuestionEvent");
			UpdateQuestionEvent aqe = (UpdateQuestionEvent) ese;
			Question question = aqe.getQuestion();
			boolean shouldEnableTeamA = aqe.shouldEnableTeamA();
			boolean shouldEnableTeamB = aqe.shouldEnableTeamB();

			if (question != null) {
				this.updateQuestion(question, shouldEnableTeamA, shouldEnableTeamB);
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
		if (this.handler.isUsingScoreboard()) {
			this.teamABorder.setTitle(this.teamAName + " -- Score: "
					+ this.teamAScore);
			this.teamBBorder.setTitle(this.teamBName + " -- Score: "
					+ this.teamBScore);
		} else {
			this.teamABorder.setTitle(this.teamAName);
			this.teamBBorder.setTitle(this.teamBName);
		}

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

		System.out.println("game state text: " + this.gameState.getQuestionText());
		this.currentQuestionText.setText(this.gameState.getQuestionText());
		this.currentQuestionText.setBackground(this.gameState.getQuestionColor());
		this.currentQuestionText.setEnabled(true);
		this.currentQuestionAnswer.setText(this.gameState.getQuestionAnswerText());
		this.currentQuestionAnswer.setBackground(this.gameState.getQuestionColor());
		this.currentQuestionAnswer.setEnabled(true);
		
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
					this.submitButton.isEnabled(), this.currentQuestionText.getText(),
					this.currentQuestionAnswer.getText(), this.currentQuestionText
							.getBackground());
	
			this.currentQuestionText
					.setText("Close the TCQ dialog to proceed with the buzzer portion of the competition.");
			this.currentQuestionText.setEnabled(false);
			this.currentQuestionAnswer.setText("");
			this.currentQuestionAnswer.setEnabled(false);
	
			this.disableTeamA();
			this.disableTeamB();
	
			this.disableBackButton();
			this.submitButton.setEnabled(false);
		}
	}
}
