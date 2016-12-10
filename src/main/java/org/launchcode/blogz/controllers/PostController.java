package org.launchcode.blogz.controllers;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.launchcode.blogz.models.Post;
import org.launchcode.blogz.models.User;
import org.launchcode.blogz.models.dao.PostDao;
import org.launchcode.blogz.models.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class PostController extends AbstractController {
	
	@Autowired
	public PostDao postdao;
	@Autowired
	public UserDao userdao;

	@RequestMapping(value = "/blog/newpost", method = RequestMethod.GET)
	public String newPostForm() {
		
		return "newpost";
	}
	
	@RequestMapping(value = "/blog/newpost", method = RequestMethod.POST)
	public String newPost(HttpServletRequest request, Model model) {
		boolean valTitle= false;
		boolean valBody= false;
		
		String title = request.getParameter("title");
		String body = request.getParameter("body");
		
		model.addAttribute("value", title);
		model.addAttribute("body", body);
		if(!title.equals("")){
			valTitle=true;
		}
		if(!body.equals("")){
			valBody=true;
		}
		if(valTitle&&valBody){
			HttpSession currentSession = request.getSession();
			User currentUser = getUserFromSession(currentSession);
			Post newpost = new Post(title,body,currentUser);
			postdao.save(newpost);
			return "redirect:/blog/"+currentUser.getUsername()+"/"+ newpost.getUid();
		}else{
			model.addAttribute("error", "Please make sure your entries are not blank");
			return "/blog/newpost";
		}
		
		// TODO - implement newPost
		//return "redirect:/blog/newpost/{uid}"; // TODO - this redirect should go to the new post's page  		
	}
	
	@RequestMapping(value = "/blog/{username}/{uid}", method = RequestMethod.GET)
	public String singlePost(@PathVariable String username, @PathVariable int uid, Model model) {
		
		// TODO - implement singlePost
		Post post = postdao.findByUid(uid);
		model.addAttribute("post", post);
		//model.addAttribute("username", username);
		
		return "post";
	}
	
	@RequestMapping(value = "/blog/{username}", method = RequestMethod.GET)
	public String userPosts(@PathVariable String username, Model model) {
		
		// TODO - implement userPosts
		
		User user = userdao.findByUsername(username);
		int userid = user.getUid();
		
		List<Post> posts = user.getPosts();
		if(posts.size()==0){
			model.addAttribute("none", "This user doesn't have any posts at the moement");
		}else{
		model.addAttribute("posts", user.getPosts());
		}
		return "blog";
	}
	
}
