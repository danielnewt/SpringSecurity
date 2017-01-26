package ca.sheridancollege.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

@Entity
public class GameState implements Serializable{

	private static final long serialVersionUID = 6563968188072469010L;
	
	@Id
	@GeneratedValue
	private int id;
	
	@OneToOne
	private User user;
	
	private int score;
	
	@OneToOne
	private GameQuestion selectedQuestion = null;
	
	@OneToMany(cascade=CascadeType.ALL, mappedBy="game")
	private List<GameQuestion> questions = new ArrayList<GameQuestion>();
	
	public GameState(){
		
	}

	public GameState(User user, int score){
		this.user = user;
		this.score = score;
	}
	
	public GameState(User user, int score, List<GameQuestion> questions) {
		this.user = user;
		this.score = score;
		this.questions = questions;
	}

	public int getId(){
		return id;
	}
	
	public User getUser() {
		return user;
	}

	public int getScore() {
		return score;
	}

	public List<GameQuestion> getQuestions() {
		return questions;
	}

	public void setId(int id){
		this.id = id;
	}
	
	public void setUser(User user) {
		this.user = user;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public void setQuestions(List<GameQuestion> questions) {
		this.questions = questions;
	}

	public GameQuestion getSelectedQuestion() {
		return selectedQuestion;
	}

	public void setSelectedQuestion(GameQuestion selectedQuestion) {
		this.selectedQuestion = selectedQuestion;
	}

	public void sortQuestions(){
		List<GameQuestion> sortedQuestions = new ArrayList<GameQuestion>();
		
		HashMap<String, List<GameQuestion>> catMap = new HashMap<String, List<GameQuestion>>();
		
		for(GameQuestion g : questions){
			String cat = g.getQuestion().getCategory().getName();
			if(catMap.containsKey(cat)){
				boolean added = false;
				for(int i = 0; i < catMap.get(cat).size(); i++){
					if(g.getScoreValue() < catMap.get(cat).get(i).getScoreValue()){
						catMap.get(cat).add(i, g);
						added = true;
						break;
					}
				}
				if(!added){
					catMap.get(cat).add(g);
				}
			} else {
				catMap.put(cat, new ArrayList<GameQuestion>());
				catMap.get(cat).add(g);
			}
		}
		
		for(List<GameQuestion> gq : catMap.values()){
			sortedQuestions.addAll(gq);
		}
		
		questions = sortedQuestions;
	}
}
