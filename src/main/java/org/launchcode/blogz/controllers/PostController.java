package org.launchcode.blogz.controllers;

import java.sql.Date;
import java.util.Calendar;
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
		String body = request.getParameter("body");
		String title = request.getParameter("title");
		HttpSession thisSession = request.getSession();
		User author = this.getUserFromSession(thisSession);
        
		if(title == "" || title == null){
			model.addAttribute("error", "Title required.");
			return "newpost";
		}
		else if(body == "" || body == null){
			model.addAttribute("error", "Content required.");
			model.addAttribute("title", title);
			return "newpost";
		}
		else{//if title and body != null & != ""
			Post newPost = new Post(title, body, author);
			postDao.save(newPost);
			int postUid = newPost.getUid();
			return "redirect:" + newPost.getAuthor().getUsername() + "/" + postUid;		
		}
	}
	
	@RequestMapping(value = "/blog/{username}/{uid}", method = RequestMethod.GET)
	public String singlePost(@PathVariable String username, @PathVariable int uid, Model model) {
		Post currentPost = postDao.findByUid(uid);
		model.addAttribute("post", currentPost);
		return "post";
	}
	
	@RequestMapping(value = "/blog/{username}", method = RequestMethod.GET)
	public String userPosts(@PathVariable String username, Model model) {
		User currentUser = userDao.findByUsername(username);
		List<Post> userPosts = currentUser.getPosts();
		model.addAttribute("posts", userPosts);
		return "blog";
	}
}

