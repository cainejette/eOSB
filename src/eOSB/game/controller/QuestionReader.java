package eOSB.game.controller;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.swing.JOptionPane;

import eOSB.game.controller.Question.Format;
import eOSB.game.controller.Question.Type;

/**
 * Class responsible for reading files from disk and translating them into
 * @author cjette
 * 
 */
public class QuestionReader {
  
  private Round round;

  private String previousLine = "";
  private String currentLine = "";

  private BufferedReader reader;

  private int questionsRead = 0;

  /**
   * @param round the {@link Round} to read from disk.
   *
   */
  
  
  public QuestionReader(Round round) {
  	this.round = round;
    try {
    	
    	InputStream is = Handler.getResourceAsStream(round.getLocation());
    	
      this.reader = new BufferedReader(new InputStreamReader(is));
      
      this.loadQuestions();
    }
    catch (NullPointerException npe) {
    	JOptionPane.showMessageDialog(null, "Unable to read questions");
    }

    catch (FileNotFoundException e) {
      System.err.println("tried to read: " + round.getLocation());
      System.err.println("cannot read file: file not found");
      System.exit(0);
    }
  }

  /**
   * Adds {@link Question}s to the {@link Round}
   * @throws FileNotFoundException thrown if FNF
   */
  public void loadQuestions() throws FileNotFoundException {
    Question question = new Question();

    try {
      currentLine = reader.readLine();
      previousLine = currentLine;
      currentLine = reader.readLine();
    }

    catch (IOException ioe) {
      ioe.printStackTrace();
    }

    while (questionsRead < 40 && currentLine != null) {
      if (currentLine.startsWith("Question Type")) {
        question = new Question();

        int questionNumber = (int) ((questionsRead / 2) + 1);

        if (questionsRead % 2 == 0) {
          question.setNumber(Integer.toString(questionNumber));
        }

        else {
          question.setNumber(Integer.toString(questionNumber) + "b");
        }

        if (currentLine.substring(15).contains("Toss")) {
          question.setType(Type.TOSSUP);
        }
        else if (currentLine.substring(15).contains("Bonus")) {
          question.setType(Type.BONUS);
        }
      }

      else if (currentLine.startsWith("Question Format")) {
        if (currentLine.contains("Multiple")) {
          question.setFormat(Format.MULTIPLE_CHOICE);
        }
        else if (currentLine.contains("Short")) {
          question.setFormat(Format.SHORT_ANSWER);
        }
      }

      else if (currentLine.startsWith("Category")) {
        question.setCategory(currentLine.substring(10));
      }

      else if (previousLine.startsWith("Question:")) {
        question.setText(currentLine);
      }

      else if (previousLine.startsWith("Answer")) {
        question.addAnswerOption(currentLine);
      }

      else if (currentLine.startsWith("Correct Answer:")
          && question.getFormat().equals(Format.MULTIPLE_CHOICE)) {
        question.addCorrectAnswer(currentLine.substring(17));
        this.round.addQuestion(question);
        this.questionsRead = questionsRead + 1;
      }

      else if (previousLine.startsWith("Correct Answer:")
          && question.getFormat().equals(Format.SHORT_ANSWER)) {
        question.addCorrectAnswer(currentLine);
        this.round.addQuestion(question);
        this.questionsRead = questionsRead + 1;
      }

      try {
        previousLine = currentLine;
        currentLine = reader.readLine();
      }

      catch (IOException exception) {
        exception.printStackTrace();
      }
    }
  }

  /**
   * @return the {@link Round}
   */
  public Round getRound() {
    return this.round;
  }
}
