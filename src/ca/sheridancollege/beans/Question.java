package ca.sheridancollege.beans;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Question implements Serializable{

	private static final long serialVersionUID = 7661733905534404706L;

	@Id
	@GeneratedValue
	private int id;
	
	@ManyToOne(cascade=CascadeType.ALL)
	private QuestionCategory category;
	
	private String question;
	private String answer;
	private String[] wrongAnswers;
	
	public Question(){
		
	}

	public Question(QuestionCategory category, String question, String answer, String[] wrongAnswers) {
		this.category = category;
		this.question = question;
		this.answer = answer;
		this.wrongAnswers = wrongAnswers;
	}

	public int getId() {
		return id;
	}

	public QuestionCategory getCategory() {
		return category;
	}

	public String getQuestion() {
		return question;
	}

	public String getAnswer() {
		return answer;
	}

	public String[] getWrongAnswers() {
		return wrongAnswers;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setCategory(QuestionCategory category) {
		this.category = category;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public void setWrongAnswers(String[] wrongAnswers) {
		this.wrongAnswers = wrongAnswers;
	}
}
