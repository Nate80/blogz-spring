package org.launchcode.blogz.controllers;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.launchcode.blogz.models.Post;
import org.launchcode.blogz.models.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class PostController extends AbstractController {

	@RequestMapping(value = "/blog/newpost", method = RequestMethod.GET)
	public String newPostForm() {
		return "newpost";
	}
	
	@RequestMapping(value = "/blog/newpost", method = RequestMethod.POST)
	public String newPost(HttpServletRequest request, Model model) {
		
		// TODO - implement newPost
		String body = request.getParameter("body");
		String title = request.getParameter("title");
		HttpSession thisSession = request.getSession();
		User author = this.getUserFromSession(thisSession);
		
		if(title == "" || title == null) {
			model.addAttribute("Error", "Title required");
			return "newpost";
			} 
		
		else if(body == "" || body == null) {
			model.addAttribute("Error", "Content Required");
			model.addAttribute("title", title);
			return "newpost";
		} 
		else { //if title and body != null and != ""
			Post newPost = new Post(title, body, author);
			postDao.save(newPost);
			int postUid = newPost.getUid();
			return "redirect:" + newPost.getAuthor().getUsername() + "/" + postUid; //redirects to new page
		}
		
		  		
	}
	
	@RequestMapping(value = "/blog/{username}/{uid}", method = RequestMethod.GET)
	public String singlePost(@PathVariable String username, @PathVariable int uid, Model model) {
		
		// TODO - implement singlePost
		Post currentPost = postDao.findByUid(uid);
		model.addAttribute("post", currentPost);		
		return "post";
	}
	
	@RequestMapping(value = "/blog/{username}", method = RequestMethod.GET)
	public String userPosts(@PathVariable String username, Model model) {
		
		// TODO - implement userPosts
		User currentUser = userDao.findByUsername(username);
		List<Post> userPosts = currentUser.getPosts();
		model.addAttribute("posts", userPosts);		
		return "blog";
	}
	
}
