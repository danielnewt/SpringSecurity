package ca.sheridancollege.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;

import ca.sheridancollege.beans.GameState;
import ca.sheridancollege.beans.User;
import ca.sheridancollege.dao.DAO;
import ca.sheridancollege.util.Util;

public class UserController {
	
	private DAO dao = DAO.getInstance();
	private GameState currentGame = null;
	
	public void LoadGame(Model model){
		User user = null;
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if(auth.isAuthenticated()){
			user = (User)auth.getPrincipal();
		}
		
		if(user != null){
			if(currentGame == null || !currentGame.getUser().getUsername().equals(user.getUsername())){
				currentGame = dao.findGameByUser(user);
				if(currentGame == null){
					return;
				}
			}
			model.addAttribute("gameState", currentGame);
		} else {
			currentGame = null;
		}
	}
	
	public String login(Model model){
		model.addAttribute("context", "Login");
		return "user";
	}
	
	public String createAdminAccount(){
		String admin = "admin";
		User adminUser = dao.findByUsername(admin, false);
		if(adminUser == null){
			adminUser = new User(admin, admin, true, true);
			dao.save(adminUser, false);
		}
		
		return HomeController.REDIRECT_HOME;
	}
	
	public String register(Model model){
		model.addAttribute("user", new User());
		model.addAttribute("context", "Register");
		return "user";
	}
	
	public String saveNewUser(Model model, @ModelAttribute User user){
		boolean success = true;
		
		if(Util.isEmpty(user.getUsername()) || Util.isEmpty(user.getPassword())){
			success = false;
		} else {
			user = new User(user.getUsername(), new BCryptPasswordEncoder().encode(user.getPassword()), true, false);
			success = dao.save(user, false);
		}
		
		if(!success){
			model.addAttribute("error", "Failed to add new user: " + user.getUsername());
			return register(model);
		}
		
		model.addAttribute("accountCreated", "true");
		return HomeController.REDIRECT_HOME + "login";
	}
	
	public String logout (HttpServletRequest request, HttpServletResponse response) {
	    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	    if (auth != null){    
	        new SecurityContextLogoutHandler().logout(request, response, auth);
	    }
	    return HomeController.REDIRECT_HOME;
	}
}
