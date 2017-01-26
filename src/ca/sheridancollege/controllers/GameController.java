package ca.sheridancollege.controllers;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import ca.sheridancollege.beans.GameQuestion;
import ca.sheridancollege.beans.GameState;
import ca.sheridancollege.beans.Question;
import ca.sheridancollege.beans.QuestionCategory;
import ca.sheridancollege.beans.User;
import ca.sheridancollege.dao.DAO;

public class GameController {

	private static boolean initialized = false;
	private DAO dao = DAO.getInstance();
	
	public GameController(){
		if(!initialized){
			initialize();
		}
	}
	
	private void initialize(){
		QuestionCategory mario = new QuestionCategory("Super Mario");
		QuestionCategory zelda = new QuestionCategory("The Legend of Zelda");
		QuestionCategory kirby = new QuestionCategory("Kirby");
		QuestionCategory megaman = new QuestionCategory("Megaman");
		
		mario.getQuestions().add(new Question(mario, 
				"In the Super Mario Bros 2 instruction booklet, The character Birdo was described as: ", 
				"A male", 
				new String[]{"A mother", "A bird", "A mutant"}));
		
		mario.getQuestions().add(new Question(mario, 
				"Which Mario game first introduced the Koopalings?", 
				"Super Mario Bros 3", 
				new String[]{"Super Mario Bros", "Super Mario Bros 2", "Super Mario World"}));
		
		mario.getQuestions().add(new Question(mario, 
				"What was the first appearance of mario in a Nintendo game?", 
				"Donkey Kong", 
				new String[]{"Super Mario Bros", "Mario Bros", "Super Mario Land"}));
		
		mario.getQuestions().add(new Question(mario, 
				"Which of the following games did not feature Shy Guy as a character?", 
				"Super Mario World", 
				new String[]{"Super Mario Bros 2", "Super Mario World 2: Yoshi's Island", "Doki Doki Panic"}));
		
		zelda.getQuestions().add(new Question(zelda, 
				"What is the name of the villain in the original Legend of Zelda?", 
				"Ganon", 
				new String[]{"Ganondorf", "Agahnim", "Manjora"}));
		
		zelda.getQuestions().add(new Question(zelda, 
				"What is the title of the third Legend of Zelda game?", 
				"A Link to the Past", 
				new String[]{"Link's Adventure", "Link's Awakening", "Ocarina of Time"}));
		
		zelda.getQuestions().add(new Question(zelda, 
				"In the original Legend of Zelda you could unlock a different version of the game by entering the name: ", 
				"Zelda", 
				new String[]{"Link", "Legend", "Ganon"}));
		
		zelda.getQuestions().add(new Question(zelda, 
				"The currency in the Legend of Zelda games are called: ", 
				"Rupees", 
				new String[]{"Gold", "Gil", "Gems"}));
		
		kirby.getQuestions().add(new Question(kirby, 
				"In the first game featuring Kirby he was not Pink, what colour was he?", 
				"White", 
				new String[]{"Yellow", "Green", "Red"}));
		
		kirby.getQuestions().add(new Question(kirby, 
				"What is Kirby's home planet?", 
				"Pop Star", 
				new String[]{"Earth", "Cookieland", "Luna"}));
		
		kirby.getQuestions().add(new Question(kirby, 
				"What was the first Kirby game to be released?", 
				"Kirby's Dreamland", 
				new String[]{"Kirby's Adventure", "Kirby's Avalanche", "Kirby Super Star"}));
		
		kirby.getQuestions().add(new Question(kirby, 
				"Kirby copies the abilities of his enemies by: ", 
				"Eating them", 
				new String[]{"Beating them", "Touching them", "Crushing them"}));
		
		megaman.getQuestions().add(new Question(megaman, 
				"What was the first Megaman game that allowed Megaman to slide?", 
				"Megaman 3", 
				new String[]{"Megaman 2", "Megaman 4", "Megaman 5"}));
		
		megaman.getQuestions().add(new Question(megaman, 
				"What was the first Megaman game that allowed Megaman to charge his gun?", 
				"Megaman 4", 
				new String[]{"Megaman 2", "Megaman 3", "Megaman 5"}));
		
		megaman.getQuestions().add(new Question(megaman, 
				"In which century do the Megaman games take place?", 
				"21st Century", 
				new String[]{"22nd Century", "30th Century", "25th Century"}));
		
		megaman.getQuestions().add(new Question(megaman, 
				"Who was the main antagonist in the original Megaman games?", 
				"Dr. Wiley", 
				new String[]{"Evil Robots", "Dr. Light", "Sigma"}));
		
		dao.save(mario);
		dao.save(zelda);
		dao.save(kirby);
		dao.save(megaman);
		
		initialized = true;
	}
	
	public void loadGame(HttpSession session){
		GameState currentGame = getCurrentGame(session);
		
		if(currentGame != null && currentGame.getSelectedQuestion() == null){
			currentGame.sortQuestions();
			
			boolean finished = true;
			
			for(GameQuestion q : currentGame.getQuestions()){
				if(q.getState() == GameQuestion.State.UNANSWERED){
					finished = false;
					break;
				}
			}
			
			if(finished){
				session.setAttribute("gameWon", "true");
			}
		}
	}
	
	public void newGame(HttpSession session){
		GameState currentGame = getCurrentGame(session);
		
		if(currentGame != null){
			dao.remove(currentGame);
		}
		session.removeAttribute("gameState");
		session.removeAttribute("gameWon");
		
		User user = GetUser();
		
		if(user == null) return;
		
		GameState game = new GameState(user, 0);
		
		List<QuestionCategory> categories = dao.getAllQuestionCategories();
		
		if(categories.size() < 4) return;
		
		int baseScore = 100;
		
		for(int i = 0; i < 4; i++){
			game.getQuestions().add(new GameQuestion(game, categories.get(0).getQuestions().get(i), (i + 1) * baseScore));
			game.getQuestions().add(new GameQuestion(game, categories.get(1).getQuestions().get(i), (i + 1) * baseScore));
			game.getQuestions().add(new GameQuestion(game, categories.get(2).getQuestions().get(i), (i + 1) * baseScore));
			game.getQuestions().add(new GameQuestion(game, categories.get(3).getQuestions().get(i), (i + 1) * baseScore));
		}
		
		dao.save(game);
	}
	
	public void selectQuestion(HttpSession session, int questionId){
		GameState currentGame = getCurrentGame(session);
		
		if(currentGame != null){
			for(GameQuestion q : currentGame.getQuestions()){
				if(q.getId() == questionId){
					currentGame.setSelectedQuestion(q);
					dao.update(currentGame);
					break;
				}
			}
		}
	}
	
	public void answerQuestion(HttpSession session, String answer){
		GameState currentGame = getCurrentGame(session);
		
		if(currentGame != null && currentGame.getSelectedQuestion() != null){
			if(currentGame.getSelectedQuestion().getQuestion().getAnswer().equals(answer)){
				currentGame.getSelectedQuestion().setState(GameQuestion.State.CORRECT);
				currentGame.setScore(currentGame.getScore() + currentGame.getSelectedQuestion().getScoreValue());
			} else {
				currentGame.getSelectedQuestion().setState(GameQuestion.State.INCORRECT);
			}
			currentGame.setSelectedQuestion(null);
			dao.update(currentGame);
		}
	}
	
	private User GetUser(){
		org.springframework.security.core.userdetails.User userDetails = null;
		User user = null;
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if(auth.isAuthenticated()){
			Object prin = auth.getPrincipal();
			if(prin instanceof org.springframework.security.core.userdetails.User){
				userDetails = (org.springframework.security.core.userdetails.User)prin;
				user = dao.findByUsername(userDetails.getUsername(), false);
			}
		}
		return user;
	}
	
	private GameState getCurrentGame(HttpSession session){
		GameState currentGame = (GameState)session.getAttribute("gameState");
		
		User user = GetUser();
		
		if(user != null){
			if(currentGame == null || !currentGame.getUser().getUsername().equals(user.getUsername())){
				currentGame = dao.findGameByUser(user);
			}
			session.setAttribute("gameState", currentGame);
		}
		
		return currentGame;
	}
}
