package eOSB.game.controller;

import java.util.ArrayList;

/**
 * Object encapsulating a question.
 * @author cjette
 * 
 */
public class Question {

  /**
   * Enumeration of {@link Question} types (tossup vs bonus)
   */
  public enum Type {

    /** a tossup question */
    TOSSUP,

    /** a bonus question */
    BONUS;
  };

  /**
   * Enumeration of {@link Question} formats (multiple choice vs short answer)
   */
  public enum Format {

    /** a multiple-choice question */
    MULTIPLE_CHOICE {
      public String toString() {
        return "MULTIPLE CHOICE";
      }
    },

    /** a short-answer question */
    SHORT_ANSWER {
      public String toString() {
        return "SHORT ANSWER";
      }
    },
  };

  private String number;
  private String category;
  private String text;
  private Type type;
  private Format format;

  private ArrayList<String> answerOptions;
  private ArrayList<String> correctAnswers;

  /**
   * Constructor.
   */
  public Question() {
    this.answerOptions = new ArrayList<String>();
    this.correctAnswers = new ArrayList<String>();
  }

  /**
   * Sets the number for this {@link Question}.
   * @param number the number to assign to this {@link Question}
   */
  public void setNumber(String number) {
    this.number = number;
  }

  /**
   * @return the number associated with this {@link Question}
   */
  public String getNumber() {
    return this.number;
  }

  /**
   * Sets the {@link Question.Type} for this {@link Question}
   * @param type the {@link Question.Type} of this {@link Question}
   */
  public void setType(Type type) {
    this.type = type;
  }

  /**
   * @return the {@link Question.Type} for this {@link Question}
   */
  public Type getType() {
    return this.type;
  }

  /**
   * Sets the {@link Question.Format} for this {@link Question}
   * @param format the {@link Question.Format} to assign
   */
  public void setFormat(Format format) {
    this.format = format;
  }

  /**
   * @return the {@link Question.Format} for this {@link Question}
   */
  public Format getFormat() {
    return this.format;
  }

  /**
   * Sets the category for this {@link Question}
   * @param category the category to assign
   */
  public void setCategory(String category) {
    this.category = category;
  }

  /**
   * @return the category of this {@link Question}
   */
  public String getCategory() {
    return this.category;
  }

  /**
   * Sets the text for this {@link Question}
   * @param text the text to assign
   */
  public void setText(String text) {
    this.text = text;
  }

  /**
   * @return the text of this {@link Question}
   */
  public String getText() {
    return this.text;
  }

  /**
   * Adds an answer option to this {@link Question}
   * @param option the answer option to add
   */
  public void addAnswerOption(String option) {
    this.answerOptions.add(option);
  }

  /**
   * @return the answer options for this {@link Question}
   */
  public ArrayList<String> getAnswerOptions() {
    return this.answerOptions;
  }

  /**
   * Adds a correct answer to this {@link Question}
   * @param answer the correct answer to add
   */
  public void addCorrectAnswer(String answer) {
    this.correctAnswers.add(answer);
  }

  /**
   * @return the list of correct answers for this {@link Question}
   */
  public ArrayList<String> getCorrectAnswers() {
    return this.correctAnswers;
  }
  
  @Override
  public String toString() {
  	StringBuilder builder = new StringBuilder();
  	builder.append("Number: " + getNumber() + "\n");
  	builder.append("Type: " + getType() + "\n");
  	builder.append("Category: " + getCategory() + "\n");
  	builder.append("Format: " + getFormat() + "\n");
  	builder.append("Text: " + getText() + "\n");
  	for (String string : getAnswerOptions()) {
  		builder.append("\tAnswer Option: " + string + "\n");
  	}
  	builder.append("Correct Answer: " + getCorrectAnswers().get(0) + "\n");
  	return builder.toString();
  }
}
