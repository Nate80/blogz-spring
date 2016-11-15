package org.launchcode.blogz.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.launchcode.blogz.models.User;
import org.launchcode.blogz.models.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class AuthenticationController extends AbstractController {
	
	
	@Autowired
	private UserDao userDao;
	
	@RequestMapping(value = "/signup", method = RequestMethod.GET)
	public String signupForm() {
		return "signup";
	}
	
	@RequestMapping(value = "/signup", method = RequestMethod.POST)
	public String signup(HttpServletRequest request, Model model) {
		
		// TODO - implement signup
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String verify = request.getParameter("verify"); //uses verification from template "signup.html"
		
		if(!User.isValidUsername(username)){  
			  model.addAttribute("username_error", "Invalid username.");
			  return "signup";
		  }
		  else if(!User.isValidPassword(password)){  
			  model.addAttribute("password_error","Invalid password.");
			  model.addAttribute("username", username);
			  return "signup";
		  }
		  else if(!User.isValidPassword(verify)){ //(!password.equals(verify))
			  model.addAttribute("verify_error", "The passwords do not match.");
			  model.addAttribute("username",  username);
			  return "signup";
		  } else {
		
		User newUser = new User(username, password);
		userDao.save(newUser);
		HttpSession thisSession =  request.getSession();
		this.setUserInSession(thisSession, newUser);
		return "redirect:blog/newpost";
		}
	}
	
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String loginForm() {
		return "login";
	}
	
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public String login(HttpServletRequest request, Model model) {
		
		// TODO - implement login
		
		return "redirect:blog/newpost";
	}
	
	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public String logout(HttpServletRequest request){
        request.getSession().invalidate();
		return "redirect:/";
	}
}
