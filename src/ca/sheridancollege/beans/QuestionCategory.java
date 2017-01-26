package ca.sheridancollege.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class QuestionCategory implements Serializable{
	
	private static final long serialVersionUID = -3967170761086255708L;
	
	@Id
	@GeneratedValue
	private int id;
	
	private String name;
	
	@OneToMany(cascade=CascadeType.ALL)
	private List<Question> questions = new ArrayList<Question>();
	
	public QuestionCategory(){
		
	}
	
	public QuestionCategory(String name){
		this.name = name;
	}
	
	public QuestionCategory(String name, List<Question> questions){
		this.name = name;
		this.questions = questions;
	}

	public String getName() {
		return name;
	}

	public List<Question> getQuestions() {
		return questions;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setQuestions(List<Question> questions) {
		this.questions = questions;
	}
}
