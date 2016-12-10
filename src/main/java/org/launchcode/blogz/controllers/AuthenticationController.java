package org.launchcode.blogz.controllers;

import java.util.List;

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
	public UserDao userdao;
	
	@RequestMapping(value = "/signup", method = RequestMethod.GET)
	public String signupForm() {
		return "signup";
	}
	
	@RequestMapping(value = "/signup", method = RequestMethod.POST)
	public String signup(HttpServletRequest request, Model model) {
		boolean valUsername=false;
		boolean valPassword=false;
		boolean valVerify=false;
		// TODO - implement signup
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String verify = request.getParameter("verify");
		
		if (User.isValidUsername(username)){
			valUsername=true;
		}else{
			model.addAttribute("username_error", "Incorrect User credentials");
			return "signup";
		}
		if (User.isValidPassword(password)){
			valPassword=true;
			
		}else{
			model.addAttribute("password_error", "Incorrect Password credentials");
			return "signup";
		}
		if (password.equals(verify)){
			valVerify=true;
		}else{
			model.addAttribute("verify_error", "Passwords did not match");
			return "signup";
		}
		if(valUsername&&valPassword&&valVerify){
		    User newUser = new User(username,password);
		    userdao.save(newUser);
		    HttpSession currentSession = request.getSession();
		    setUserInSession(currentSession, newUser);
		    
		    System.out.println("everything is good");
			return "redirect:blog/newpost";
		}else{
			 System.out.println("everything is not good");
			return "signup";
		}
		
		//Session currentSession = request.getSession();
		//return "redirect:blog/newpost";
		
	}
	
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String loginForm() {
		return "login";
	}
	
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public String login(HttpServletRequest request, Model model) {
		boolean valUsername = false;
		boolean valPassword = false;
		// TODO - implement login
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		model.addAttribute("username", username);
		
		
		if(User.isValidUsername(username)){
			if(userdao.findByUsername(username)!=null){
				System.out.println("username exists");
			valUsername=true;
			}
		}else{
			model.addAttribute("error","Username incorrect");
			return "login";
		}
		if(User.isValidPassword(password)){
			User user = new User(username,password);
			if(user.isMatchingPassword(password)){
					valPassword=true;
			}
		}else{
			model.addAttribute("error","Password incorrect");
			return "login";
		}
		
		if(valUsername&&valPassword){
			User oldUser = userdao.findByUsername(username);
		    HttpSession currentSession = request.getSession();
		    setUserInSession(currentSession, oldUser);
		    
		    System.out.println("everything is good");
			return "redirect:blog/newpost";
		}else {
			System.out.println("everything is not good");
			return "login";
		}
		
	}
	
	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public String logout(HttpServletRequest request){
        request.getSession().invalidate();
		return "redirect:/";
	}
}
