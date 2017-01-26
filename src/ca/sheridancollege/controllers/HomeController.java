package ca.sheridancollege.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import ca.sheridancollege.beans.User;

@Controller
public class HomeController {
	
	final static String REDIRECT_HOME = "redirect:/";
	
	private UserController userCtrl = new UserController();
	private GameController gameCtrl = new GameController();
	
	@RequestMapping("/")
	public String index(HttpSession session){
		gameCtrl.loadGame(session);
		return "home";
	}
	
	/*
	 * Game Control
	 */
	@RequestMapping("/game/newGame")
	public String newGame(HttpSession session){
		gameCtrl.newGame(session);
		return REDIRECT_HOME;
	}
	
	@RequestMapping("/game/selectQuestion/{id}")
	public String selectQuestion(HttpSession session, @PathVariable int id){
		gameCtrl.selectQuestion(session, id);
		return REDIRECT_HOME;
	}
	
	@RequestMapping(value="/game/answerQuestion", method=RequestMethod.POST)
	public String answerQuestion(HttpSession session, @RequestParam String answer){
		gameCtrl.answerQuestion(session, answer);
		return REDIRECT_HOME;
	}
	/*
	 * User Access Control
	 */
	@RequestMapping("/login")
	public String login(Model model, @RequestParam(value = "error", required = false) String error, @RequestParam(value = "accountCreated", required = false) String accountCreated){
		if(error != null){
			model.addAttribute("error", "Login Failed");
		}
		if(accountCreated != null){
			model.addAttribute("accountCreated", "true");
		}
		return userCtrl.login(model);
	}
	
	@RequestMapping("/register")
	public String register(Model model){
		return userCtrl.register(model);
	}
	
	@RequestMapping("/debug/{command}")
	public String debugCommand(@PathVariable String command){
		switch(command){
		case "admin":
			userCtrl.createAdminAccount();
			break;
		default:
				//do nothing
		}
		return REDIRECT_HOME;
	}
	
	@RequestMapping(value="/saveuser", method=RequestMethod.POST)
	public String saveuser(Model model, @ModelAttribute User user){
		return userCtrl.saveNewUser(model, user);
	}
	
	@RequestMapping("/logout")
	public String logout (HttpServletRequest request, HttpServletResponse response) {
		return userCtrl.logout(request, response);
	}
}
