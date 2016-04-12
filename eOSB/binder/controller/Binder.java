package eOSB.binder.controller;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Insets;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.font.TextAttribute;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import javax.swing.AbstractButton;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextPane;
import javax.swing.border.TitledBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import org.bushe.swing.event.EventBus;
import org.bushe.swing.event.EventServiceEvent;
import org.bushe.swing.event.EventSubscriber;

import eOSB.binder.ui.HideBuzzerQuestionsEvent;
import eOSB.binder.ui.ScorePanel;
import eOSB.binder.ui.ShowBuzzerQuestionsEvent;
import eOSB.binder.ui.SplashPanel;
import eOSB.binder.ui.TimePanel;
import eOSB.binder.ui.eOSBMenuBar;
import eOSB.binder.ui.actions.BackButtonAction;
import eOSB.binder.ui.actions.ConfirmExitListener;
import eOSB.binder.ui.actions.IncorrectAction;
import eOSB.binder.ui.actions.InterruptAction;
import eOSB.binder.ui.actions.OpenValidateUserDialogAction;
import eOSB.binder.ui.actions.SubmitAction;
import eOSB.game.controller.GameState;
import eOSB.game.controller.Handler;
import eOSB.game.controller.NewRoundEvent;
import eOSB.game.controller.Question;
import eOSB.game.data.IconFactory;
import eOSB.game.data.PathStore;
import net.miginfocom.swing.MigLayout;

public class Binder implements EventSubscriber<EventServiceEvent> {
	private Handler handler;
	private JFrame frame = new JFrame();

	private JButton teamA_correct;
	private JButton teamA_incorrect;
	private JButton teamA_interrupt;
	private ButtonState teamA;

	private JButton teamB_correct;
	private JButton teamB_incorrect;
	private JButton teamB_interrupt;
	private ButtonState teamB;

	private JButton backButton;
	private JButton nextQuestionButton;
	private JButton nextTossupButton;
	private JButton submitButton;

	private GameState gameState;
	private int fontSize = 20;

	private boolean hidingQuestions = false;
	private JScrollPane questionPanelScroller;
	private JScrollPane answerPanelScroller;

	private static final String REGULAR_STYLE = "regular";
	private static final String PRONUNCIATION_STYLE = "pronunciation";
	private static final String META_SMALL_STYLE = "metaSmall";
	private static final String META_BOLD_STYLE = "metaBold";

	private final String TAB = "            ";
	private final String HALF_TAB = "      ";

	private boolean hasSeenTcqReminder = false;
	private boolean shouldEnableTcqs = false;

	private String roundName;

	public Binder(Handler handler) {
		this.handler = handler;

		EventBus.subscribe(UpdateQuestionEvent.class, this);
		EventBus.subscribe(HideBuzzerQuestionsEvent.class, this);
		EventBus.subscribe(ShowBuzzerQuestionsEvent.class, this);
		EventBus.subscribe(OpenTcqPreambleEvent.class, this);
		EventBus.subscribe(NewRoundEvent.class, this);

		this.initFrame();
		this.frame.setTitle("electronic Ocean Sciences Bowl");
		this.frame.setResizable(false);

		Container contentPane = this.frame.getContentPane();
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.LINE_AXIS));
		contentPane.add(new SplashPanel());

		this.displayFrame();
	}

	private void initFrame() {
		this.frame.setVisible(false);
		this.frame.dispose();

		this.frame = new JFrame();
		this.frame.setIconImage(new ImageIcon(ClassLoader.getSystemResource(IconFactory.LOGO)).getImage());
		this.frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.frame.addWindowListener(new ConfirmExitListener(this.handler));
		this.frame.setJMenuBar(new eOSBMenuBar(this.handler, this));
	}

	private void displayFrame() {
		this.frame.pack();
		this.frame.setMinimumSize(this.frame.getPreferredSize());
		this.frame.setExtendedState(Frame.MAXIMIZED_BOTH);
		this.frame.setLocationRelativeTo(null);

		this.frame.setVisible(true);
	}

	public void setBinderToNewRound(String roundName) {
		this.roundName = roundName;

		this.initFrame();
		this.initComponents();

		// disable TCQs on the Tiebreaker round
		if (roundName.contains("Tie") || roundName.contains("warm")) {
			this.shouldEnableTcqs = false;
		} else {
			this.shouldEnableTcqs = true;
		}
		this.frame.setJMenuBar(new eOSBMenuBar(this.handler, this));

		this.frame.setTitle(roundName + " -- electronic Ocean Sciences Bowl");
		this.frame.setResizable(true);

		JPanel panel = new JPanel();
		panel.setLayout(new MigLayout("fill, insets 0"));

		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, this.questionPanelScroller,
				this.answerPanelScroller);
		splitPane.setResizeWeight(0.8);

		panel.add(splitPane, "grow, push, span, wrap");
		panel.add(this.createSubmissionPanel(), "grow, sizegroupy 1y");

		if (this.handler.isUsingTimer()) {
			panel.add(new TimePanel(this.handler), "grow, sizegroupy 1y");
		}

		Container contentPane = this.frame.getContentPane();
		contentPane.setLayout(new MigLayout("fill, insets 0 5 5 5"));
		contentPane.add(panel, "grow");

		this.displayFrame();
	}

	private JPanel createSubmissionPanel() {
		if (this.handler.isUsingScoreboard()) {
			JPanel teamAPanel = new JPanel();

			teamAPanel.setLayout(new MigLayout("fillx, insets 0"));
			this.teamA_interrupt.setMinimumSize(new Dimension(35, 35));

			ImageIcon interruptIcon = new ImageIcon(
					ClassLoader.getSystemClassLoader().getResource(IconFactory.INTERRUPT));
			this.teamA_interrupt.setIcon(interruptIcon);
			teamA_interrupt.setFont(
					new Font(teamA_interrupt.getFont().getName(), Font.PLAIN, teamA_interrupt.getFont().getSize() + 4));
			this.teamB_interrupt.setIcon(interruptIcon);
			teamB_interrupt.setFont(
					new Font(teamB_interrupt.getFont().getName(), Font.PLAIN, teamB_interrupt.getFont().getSize() + 4));

			teamAPanel.add(this.teamA_correct, "sizegroupy group1y, sizegroupx group1x, growx, h 100!");
			teamAPanel.add(this.teamA_incorrect, "sizegroupy group1y, sizegroupx group1x, growx");
			teamAPanel.add(this.teamA_interrupt, "sizegroupy group1y, sizegroupx group1x, growx");

			JPanel teamBPanel = new JPanel();
			teamBPanel.setLayout(new MigLayout("fillx, insets 0"));
			this.teamB_interrupt.setMinimumSize(new Dimension(35, 35));

			teamBPanel.add(this.teamB_correct, "sizegroupy group2y, sizegroupx group2x, growx, h 100!");
			teamBPanel.add(this.teamB_incorrect, "sizegroupy group2y, sizegroupx group2x, growx");
			teamBPanel.add(this.teamB_interrupt, "sizegroupy group2y, sizegroupx group2x, growx");

			JPanel submitBackPanel = new JPanel();
			submitBackPanel.setLayout(new MigLayout("fillx, insets 0"));
			submitBackPanel.add(this.backButton, "sizegroup group3, growx, h 100!");
			submitBackPanel.add(this.submitButton, "sizegroup group3, growx, h 100!");

			JPanel answerPanel = new JPanel();
			answerPanel.setLayout(new MigLayout("wrap 2, fillx, insets 0"));
			answerPanel.add(new ScorePanel(), "growx, wrap, span");
			answerPanel.add(teamAPanel, "growx");
			answerPanel.add(teamBPanel, "growx");
			answerPanel.add(submitBackPanel, "growx, span");

			return answerPanel;
		} else {
			JPanel submitBackPanel = new JPanel();
			submitBackPanel.setLayout(new MigLayout("fill, insets 0"));
			submitBackPanel.add(this.backButton, "sizegroupx group3, hmin 100, growx, growy, gapafter 20");
			submitBackPanel.add(this.nextTossupButton, "sizegroupx group3, hmin 100, growx, growy, gapafter 20");
			submitBackPanel.add(this.nextQuestionButton, "sizegroupx group3, hmin 100, growx, growy");

			JPanel answerPanel = new JPanel();
			answerPanel.setLayout(new MigLayout("wrap 2, fill, insets 0"));
			answerPanel.add(submitBackPanel, "grow, span");

			return answerPanel;
		}
	}

	private void initComponents() {
		this.initQuestionPanel();
		this.initAnswerPanel();

		this.teamA_correct = new JButton();
		teamA_correct.setFont(
				new Font(teamA_correct.getFont().getName(), Font.PLAIN, teamA_correct.getFont().getSize() + 4));
		this.teamA_incorrect = new JButton();
		teamA_incorrect.setFont(
				new Font(teamA_incorrect.getFont().getName(), Font.PLAIN, teamA_incorrect.getFont().getSize() + 4));
		this.teamA_interrupt = new JButton();
		this.teamA = new ButtonState(this.teamA_correct, this.teamA_incorrect, this.teamA_interrupt);

		this.teamB_correct = new JButton();
		teamB_correct.setFont(
				new Font(teamB_correct.getFont().getName(), Font.PLAIN, teamB_correct.getFont().getSize() + 4));
		this.teamB_incorrect = new JButton();
		teamB_incorrect.setFont(
				new Font(teamB_incorrect.getFont().getName(), Font.PLAIN, teamB_incorrect.getFont().getSize() + 4));
		this.teamB_interrupt = new JButton();
		this.teamB = new ButtonState(this.teamB_correct, this.teamB_incorrect, this.teamB_interrupt);

		this.backButton = this.createNavigationButton();
		this.nextQuestionButton = this.createNavigationButton();
		this.nextTossupButton = this.createNavigationButton();
		this.submitButton = this.createNavigationButton();

		this.configureButtons();
		this.hasSeenTcqReminder = false;
	}

	private void initQuestionPanel() {
		JTextPane questionTextPane = new JTextPane();
		questionTextPane.setBorder(new TitledBorder("Introduction"));
		questionTextPane.setEditable(false);

		StyledDocument document = this.configureStyledDocument(questionTextPane);
		try {
			this.addTextToDocument(this.getRoundPreambleText(), document);
		} catch (BadLocationException e) {
			e.printStackTrace();
		}

		questionTextPane.setCaretPosition(0);

		this.questionPanelScroller = new JScrollPane();
		this.questionPanelScroller.setPreferredSize(new Dimension(400, 300));
		this.questionPanelScroller.setMinimumSize(new Dimension(400, 300));
		this.questionPanelScroller.getViewport().setView(questionTextPane);
	}

	private String getRoundPreambleText() {
		Scanner scanner = new Scanner(ClassLoader.getSystemClassLoader().getResourceAsStream(PathStore.ROUND_PREAMBLE))
				.useDelimiter("\\Z");
		return scanner.next();
	}

	private void initAnswerPanel() {
		JTextPane answerTextPane = new JTextPane();

		this.answerPanelScroller = new JScrollPane();
		this.answerPanelScroller.setPreferredSize(new Dimension(250, 300));
		this.answerPanelScroller.setMinimumSize(new Dimension(250, 300));
		this.answerPanelScroller.getViewport().setView(answerTextPane);
	}

	private JButton createNavigationButton() {
		JButton button = new JButton();

		button.setFont(new Font(button.getFont().getName(), Font.PLAIN, button.getFont().getSize() + 4));

		button.addKeyListener(new KeyListener() {

			@Override
			public void keyPressed(KeyEvent arg0) {
				int keyCode = arg0.getKeyCode();
				switch (keyCode) {
				case KeyEvent.VK_UP:
					System.out.println("up");
					nextTossupButton.doClick();
					break;

				case KeyEvent.VK_LEFT:
					System.out.println("left");
					backButton.doClick();
					break;

				case KeyEvent.VK_RIGHT:
					System.out.println("right");
					nextQuestionButton.doClick();
					break;
				}
			}

			@Override
			public void keyReleased(KeyEvent arg0) {

			}

			@Override
			public void keyTyped(KeyEvent arg0) {

			}
		});

		return button;
	}

	private void configureButtons() {
		if (this.handler.isUsingScoreboard()) {
			SubmitAction submitAction = new SubmitAction(this.handler, this.teamA, this.teamB);
			IncorrectAction incorrectAction = new IncorrectAction(this.handler, this.teamA, this.teamB);

			ImageIcon correctIcon = new ImageIcon(
					ClassLoader.getSystemClassLoader().getResource("eOSB/game/data/images/checkmark2.png"));
			ImageIcon incorrectIcon = new ImageIcon(
					ClassLoader.getSystemClassLoader().getResource("eOSB/game/data/images/redXmark2.png"));

			this.teamA_correct.setAction(submitAction);
			this.teamA_correct.setActionCommand("TEAM_A");
			this.teamA_correct.setIcon(correctIcon);
			teamA_correct.setText("Correct");
			this.teamA_correct.setFocusable(false);
			teamA_correct.setVerticalTextPosition(AbstractButton.BOTTOM);
			teamA_correct.setHorizontalTextPosition(AbstractButton.CENTER);
			teamA_correct.setEnabled(false);

			this.teamA_incorrect.setAction(incorrectAction);
			this.teamA_incorrect.setActionCommand("TEAM_A");
			this.teamA_incorrect.setIcon(incorrectIcon);
			teamA_incorrect.setText("Incorrect");
			this.teamA_incorrect.setFocusable(false);
			teamA_incorrect.setVerticalTextPosition(AbstractButton.BOTTOM);
			teamA_incorrect.setHorizontalTextPosition(AbstractButton.CENTER);
			teamA_incorrect.setEnabled(false);

			this.teamA_interrupt.setAction(new InterruptAction(handler, teamA, teamB));
			this.teamA_interrupt.setText("Interrupt");
			this.teamA_interrupt.setActionCommand("TEAM_A");
			this.teamA_interrupt.setMinimumSize(new Dimension(50, 30));
			this.teamA_interrupt.setFocusable(false);
			teamA_interrupt.setVerticalTextPosition(AbstractButton.BOTTOM);
			teamA_interrupt.setHorizontalTextPosition(AbstractButton.CENTER);
			teamA_interrupt.setEnabled(false);

			this.teamB_correct.setAction(submitAction);
			this.teamB_correct.setActionCommand("TEAM_B");
			this.teamB_correct.setIcon(correctIcon);
			teamB_correct.setText("Correct");
			teamB_correct.setVerticalTextPosition(AbstractButton.BOTTOM);
			teamB_correct.setHorizontalTextPosition(AbstractButton.CENTER);
			teamB_correct.setEnabled(false);
			this.teamB_correct.setFocusable(false);

			this.teamB_incorrect.setAction(incorrectAction);
			this.teamB_incorrect.setActionCommand("TEAM_B");
			teamB_incorrect.setText("Incorrect");
			this.teamB_incorrect.setIcon(incorrectIcon);
			this.teamB_incorrect.setFocusable(false);
			teamB_incorrect.setVerticalTextPosition(AbstractButton.BOTTOM);
			teamB_incorrect.setHorizontalTextPosition(AbstractButton.CENTER);
			teamB_incorrect.setEnabled(false);

			this.teamB_interrupt.setAction(new InterruptAction(handler, teamA, teamB));
			this.teamB_interrupt.setText("Interrupt");
			this.teamB_interrupt.setActionCommand("TEAM_B");
			this.teamB_interrupt.setFocusable(false);
			teamB_interrupt.setVerticalTextPosition(AbstractButton.BOTTOM);
			teamB_interrupt.setHorizontalTextPosition(AbstractButton.CENTER);
			teamB_interrupt.setEnabled(false);

			this.submitButton.setAction(submitAction);
			this.submitButton.setIcon(new ImageIcon(
					ClassLoader.getSystemClassLoader().getResource("eOSB/game/data/images/nextArrow2.png")));
			this.submitButton.setText("Begin");
			this.submitButton.setHorizontalTextPosition(2);
			this.submitButton.setFocusable(false);

			this.backButton.setAction(new BackButtonAction(this.handler));
			this.backButton.setText("Back");
			this.backButton.setIcon(new ImageIcon(
					ClassLoader.getSystemClassLoader().getResource("eOSB/game/data/images/backArrow2.png")));
			this.backButton.setHorizontalTextPosition(4);
			this.backButton.setFocusable(false);
			this.backButton.setEnabled(false);
		} else {
			SubmitAction submitAction = new SubmitAction(this.handler, this.teamA, this.teamB);

			this.nextQuestionButton.setAction(new NextQuestionAction());
			this.nextQuestionButton
					.setIcon(new ImageIcon(ClassLoader.getSystemClassLoader().getResource(IconFactory.NEXT)));
			this.nextQuestionButton.setMinimumSize(new Dimension(50, 25));
			this.nextQuestionButton.setToolTipText("Next question (tossup or bonus)");

			this.nextTossupButton.setAction(submitAction);
			this.nextTossupButton
					.setIcon(new ImageIcon(ClassLoader.getSystemClassLoader().getResource(IconFactory.INCORRECT)));
			this.nextTossupButton.setMinimumSize(new Dimension(50, 25));
			this.nextTossupButton.setToolTipText("Next tossup");
			this.nextTossupButton.setEnabled(false);

			this.backButton.setAction(new BackButtonAction(this.handler));
			this.backButton.setIcon(new ImageIcon(ClassLoader.getSystemClassLoader().getResource(IconFactory.BACK)));
			this.backButton.setMinimumSize(new Dimension(50, 25));
			this.backButton.setToolTipText("Previous tossup or bonus");
			this.backButton.setEnabled(false);
		}
	}

	public JFrame getFrame() {
		return this.frame;
	}

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

	private void updateQuestion(Question question) {
		if (question.getNumber().equals("11") && !this.hasSeenTcqReminder && !this.roundName.contains("Tie")) {
			this.hideQuestions();

			RemindTcqEvent event = new RemindTcqEvent(this);
			EventBus.publish(event);
			this.hasSeenTcqReminder = true;
		}

		if (this.submitButton != null) {
			this.submitButton.setText("Submit");
		}
		this.createQuestionPanel(question);
		this.createAnswerPanel(question);
		this.updateButtonStates(question);
	}

	private void createQuestionPanel(Question question) {
		this.questionPanelScroller.getViewport().removeAll();

		JTextPane textPane = this.createJTextPane(this.getQuestionBackground(question));
		StyledDocument document = this.configureStyledDocument(textPane);

		try {
			String questionNumber = "Question " + question.getNumber() + " is a ";
			String questionType = question.getType().toString().toLowerCase() + ", " + question.getFormat() + ":";
			document.insertString(document.getLength(), questionNumber, document.getStyle(META_SMALL_STYLE));
			document.insertString(document.getLength(), questionType, document.getStyle(META_BOLD_STYLE));

			this.addTextToDocument("\n\n" + HALF_TAB + question.getText(), document);

			List<String> questionChoices = question.getAnswerOptions();
			List<String> questionChoicePrefixes = new ArrayList<String>();
			questionChoicePrefixes.add("W");
			questionChoicePrefixes.add("X");
			questionChoicePrefixes.add("Y");
			questionChoicePrefixes.add("Z");

			for (int i = 0; i < questionChoices.size(); i++) {
				this.addTextToDocument("\n\n" + TAB + questionChoicePrefixes.get(i) + ") " + questionChoices.get(i),
						document);
			}
		} catch (BadLocationException e) {
			e.printStackTrace();
		}

		textPane.setCaretPosition(0);
		this.questionPanelScroller.getViewport().setView(textPane);
	}

	private void createAnswerPanel(Question question) {
		this.answerPanelScroller.getViewport().remove(0);

		JTextPane textPane = this.createJTextPane(Color.LIGHT_GRAY);

		StyledDocument document = this.configureStyledDocument(textPane);

		String textToAdd = "";
		if (question.getFormat() == Question.Format.MULTIPLE_CHOICE) {
			String answerLetter = question.getCorrectAnswers().get(0).trim();
			if (answerLetter.equalsIgnoreCase("W")) {
				textToAdd = "W) " + question.getAnswerOptions().get(0);
			} else if (answerLetter.equalsIgnoreCase("X")) {
				textToAdd = "X) " + question.getAnswerOptions().get(1);
			} else if (answerLetter.equalsIgnoreCase("Y")) {
				textToAdd = "Y) " + question.getAnswerOptions().get(2);
			} else if (answerLetter.equalsIgnoreCase("Z")) {
				textToAdd = "Z) " + question.getAnswerOptions().get(3);
			}
		} else {
			textToAdd = question.getCorrectAnswers().get(0).trim();
		}

		try {
			document.insertString(document.getLength(), "Correct answer:", document.getStyle(META_SMALL_STYLE));
			this.addTextToDocument("\n\n" + this.TAB + textToAdd, document);
		} catch (BadLocationException e) {
			e.printStackTrace();
		}

		textPane.setCaretPosition(0);
		this.answerPanelScroller.getViewport().setView(textPane);
	}

	private void updateButtonStates(Question question) {
		// disable back button if viewing the very first question
		this.backButton.setEnabled(!question.getNumber().equals("1"));

		this.nextQuestionButton.setEnabled(true);
		this.nextTossupButton.setEnabled(true);
		this.submitButton.setEnabled(true);

		String correctValue = question.getType() == Question.Type.TOSSUP ? "(+4)" : "(+6)";
		teamA_correct.setText("Correct " + correctValue);
		teamB_correct.setText("Correct " + correctValue);

		teamA_incorrect.setText("Incorrect (+0)");
		teamB_incorrect.setText("Incorrect (+0)");

		teamA_interrupt.setText("Interrupt (-4)");
		teamB_interrupt.setText("Interrupt (-4)");

		if (question.getType() == Question.Type.TOSSUP) {
			this.toggleTeamButtons(true, true);
			this.toggleTeamButtons(true, false);
		} else {
			teamA_interrupt.setEnabled(false);
			teamB_interrupt.setEnabled(false);
		}
	}

	private void toggleTeamButtons(boolean shouldEnable, boolean isTeamA) {
		if (isTeamA) {
			this.teamA_correct.setEnabled(shouldEnable);
			this.teamA_incorrect.setEnabled(shouldEnable);
			if (this.handler.isUsingScoreboard()) {
				this.teamA_interrupt.setEnabled(shouldEnable);
			}
		} else {
			this.teamB_correct.setEnabled(shouldEnable);
			this.teamB_incorrect.setEnabled(shouldEnable);
			if (this.handler.isUsingScoreboard()) {
				this.teamB_interrupt.setEnabled(shouldEnable);
			}
		}
	}

	private void addTextToDocument(String text, StyledDocument document) throws BadLocationException {
		boolean done = false;
		while (!done) {
			if (text.contains("[") && text.contains("]")) {
				int pronunciationStart = text.indexOf("[");
				int pronunciationEnd = text.indexOf("]");

				document.insertString(document.getLength(), text.substring(0, pronunciationStart),
						document.getStyle(REGULAR_STYLE));
				document.insertString(document.getLength(), text.substring(pronunciationStart, pronunciationEnd + 1),
						document.getStyle(PRONUNCIATION_STYLE));
				text = text.substring(pronunciationEnd + 1);
			} else {
				done = true;
			}
		}

		document.insertString(document.getLength(), text, document.getStyle(REGULAR_STYLE));
	}

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
		StyleConstants.setForeground(metaRegular, Color.BLACK);

		Style metaBold = document.addStyle(META_BOLD_STYLE, null);
		StyleConstants.setFontFamily(metaBold, "Helvetica");
		StyleConstants.setFontSize(metaBold, this.fontSize - 2);
		StyleConstants.setForeground(metaBold, Color.BLACK);
		StyleConstants.setBold(metaBold, true);

		return document;
	}

	private JTextPane createJTextPane(Color background) {
		JTextPane textPane = new JTextPane();
		textPane.setEditable(false);
		textPane.setMargin(new Insets(10, 10, 10, 10));
		textPane.setBackground(background);
		textPane.setCaretPosition(0);
		return textPane;
	}

	private Color getQuestionBackground(Question question) {
		if (question.getNumber().contains("b")) {
			return new Color(255, 255, 100);
		} else {
			return Color.WHITE;
		}
	}

	public void disableBackButton() {
		this.backButton.setSelected(false);
		this.backButton.setEnabled(false);
	}

	private void roundOver() {
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
		} catch (BadLocationException e) {
			e.printStackTrace();
		}

		this.questionPanelScroller.getViewport().add(questionPane);

		this.answerPanelScroller.getViewport().remove(0);
		JTextPane answerPane = this.createJTextPane(Color.WHITE);
		this.answerPanelScroller.getViewport().add(answerPane);

		this.toggleTeamButtons(false, true);
		this.toggleTeamButtons(false, false);
		this.nextQuestionButton.setEnabled(false);
		this.nextTossupButton.setEnabled(false);
		this.submitButton.setEnabled(false);
	}

	public void setFontSize(int size) {
		this.fontSize = size;

		Component component = this.questionPanelScroller.getViewport().getComponent(0);
		JTextPane questionPane = (JTextPane) component;

		StyledDocument document = (StyledDocument) questionPane.getDocument();
		Style style = document.getStyle(REGULAR_STYLE);
		StyleConstants.setFontSize(style, this.fontSize);
		document.setCharacterAttributes(0, document.getLength(), style, false);

		component = this.answerPanelScroller.getViewport().getComponent(0);
		JTextPane answerPane = (JTextPane) component;
		document = (StyledDocument) answerPane.getDocument();
		style = document.getStyle(REGULAR_STYLE);
		if (style != null) {
			StyleConstants.setFontSize(style, this.fontSize);
			document.setCharacterAttributes(0, document.getLength(), style, false);
		}
	}

	public void close() {
		this.frame.dispose();
	}

	public void onEvent(EventServiceEvent ese) {
		if (ese instanceof UpdateQuestionEvent) {
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
			} else {
				this.roundOver();
			}
		} else if (ese instanceof HideBuzzerQuestionsEvent) {
			this.hideQuestions();
		} else if (ese instanceof ShowBuzzerQuestionsEvent) {
			this.showQuestions();
		} else if (ese instanceof OpenTcqPreambleEvent) {
			this.hasSeenTcqReminder = true;
		}
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
		this.nextQuestionButton.setSelected(false);
		this.submitButton.setSelected(false);
	}

	private void showQuestions() {
		this.teamA_correct.setEnabled(this.gameState.isTeamACorrectEnabled());
		this.teamA_incorrect.setEnabled(this.gameState.isTeamAIncorrectEnabled());
		this.teamA_interrupt.setEnabled(this.gameState.isTeamAInterruptEnabled());

		this.teamB_correct.setEnabled(this.gameState.isTeamBCorrectEnabled());
		this.teamB_incorrect.setEnabled(this.gameState.isTeamBIncorrectEnabled());
		this.teamB_interrupt.setEnabled(this.gameState.isTeamBInterruptEnabled());

		this.backButton.setEnabled(this.gameState.isBackEnabled());
		this.nextQuestionButton.setEnabled(this.gameState.isSubmitEnabled());
		this.submitButton.setEnabled(this.gameState.isSubmitEnabled());

		this.hidingQuestions = false;
		this.frame.setState(Frame.NORMAL);
	}

	private void hideQuestions() {
		if (!this.hidingQuestions) {
			this.hidingQuestions = true;
			this.frame.setState(Frame.ICONIFIED);
			this.gameState = new GameState(this.teamA_correct.isEnabled(), this.teamA_incorrect.isEnabled(),
					this.teamA_interrupt.isEnabled(), this.teamB_correct.isEnabled(), this.teamB_incorrect.isEnabled(),
					this.teamB_interrupt.isEnabled(), this.backButton.isEnabled(), this.nextQuestionButton.isEnabled());

			this.disableTeamA();
			this.disableTeamB();

			this.disableBackButton();
			this.nextQuestionButton.setEnabled(false);
			this.submitButton.setEnabled(false);
		}
	}

	public int getFontSize() {
		return this.fontSize;
	}

	public boolean shouldEnableTcqs() {
		return this.shouldEnableTcqs;
	}
}
