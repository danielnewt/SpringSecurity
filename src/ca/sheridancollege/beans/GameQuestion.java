package ca.sheridancollege.beans;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class GameQuestion implements Serializable{
	
	private static final long serialVersionUID = 9208005212312952789L;
	
	public enum State{
		UNANSWERED, CORRECT, INCORRECT
	}
	
	@Id
	@GeneratedValue
	private int id;
	
	private int scoreValue;
	
	@ManyToOne(cascade=CascadeType.ALL)
	private GameState game;
	@ManyToOne
	private Question question;
	
	@Enumerated(EnumType.STRING)
	private State state;
	
	public GameQuestion(){
		
	}

	public GameQuestion(GameState game, Question question, int scoreValue) {
		this(game, question, State.UNANSWERED, scoreValue);
	}
	
	public GameQuestion(GameState game, Question question, State state, int scoreValue) {
		this.game = game;
		this.question = question;
		this.state = state;
		this.scoreValue = scoreValue;
	}

	public int getId() {
		return id;
	}

	public GameState getGame() {
		return game;
	}

	public Question getQuestion() {
		return question;
	}

	public State getState() {
		return state;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setGame(GameState game) {
		this.game = game;
	}

	public void setQuestion(Question question) {
		this.question = question;
	}

	public void setState(State state) {
		this.state = state;
	}

	public int getScoreValue() {
		return scoreValue;
	}

	public void setScoreValue(int scoreValue) {
		this.scoreValue = scoreValue;
	}
}
