package eOSB.game.controller;

import java.awt.Font;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JRadioButton;

public class Round {
	private int currentQuestion = -1;
	private ArrayList<Question> questions = new ArrayList<Question>();
	private boolean previouslyOpened = false;
	private String file;
	private String name;
	private List<Tcq> tcqs;
	private List<Tcq> tcqSolutions;

	public Round(String roundName, String file, List<Tcq> tcqs, List<Tcq> tcqSolutions) {
		this.name = roundName;
		this.file = file;
		this.tcqs = tcqs;
		this.tcqSolutions = tcqSolutions;
	}

	public List<Question> getQuestions() {
		return this.questions;
	}

	public void addQuestion(Question question) {
		this.questions.add(question);
	}

	public Question getNextQuestion() {
		this.currentQuestion = this.currentQuestion + 1;

		if (this.currentQuestion < this.questions.size()) {
			return this.questions.get(this.currentQuestion);
		}

		else {
			return null;
		}
	}

	public boolean hasNextQuestion() {
		return (this.currentQuestion + 1) < this.questions.size();
	}

	public Question getPreviousQuestion() {
		this.currentQuestion = this.currentQuestion - 1;

		if (this.currentQuestion >= 0) {
			return this.questions.get(this.currentQuestion);
		} else {
			return null;
		}
	}

	public boolean hasPreviousQuestion() {
		return this.currentQuestion >= 0;
	}

	public Question getCurrentQuestion() {
		return this.questions.get(this.currentQuestion);
	}
	
	public int getCurrentQuestionIndex() {
		return this.currentQuestion;
	}

	public void setOpened() {
		this.previouslyOpened = true;
	}

	public boolean wasOpened() {
		return this.previouslyOpened;
	}

	public String getLocation() {
		return this.file;
	}

	public String getName() {
		return this.name;
	}

	public List<Tcq> getTcqs() {

		return this.tcqs;
	}

	public List<Tcq> getTcqSolutions() {
		return this.tcqSolutions;
	}

	public void setCurrentQuestion(int questionNumber) {
		this.currentQuestion = questionNumber;
	}
}
