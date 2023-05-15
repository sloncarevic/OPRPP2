package hr.fer.oprpp2.hw05.forms;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import hr.fer.oprpp2.hw05.model.BlogComment;

/**
 * Blog comment form for validating web form
 *
 */
public class BlogCommentForm {
	
	private String message;
	private String email;
	
	private Map<String, String> errors = new HashMap<String, String>();

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	public boolean hasErrors() {
		return !errors.isEmpty();
	}
	
	public boolean hasError(String errorName) {
		return errors.containsKey(errorName);
	}
	
	public String getError(String errorName) {
		return errors.get(errorName);
	}


	public void fillFromHttpRequest(HttpServletRequest req) {
		this.message = prepare(req.getParameter("message"));
		this.email = prepare(req.getParameter("email"));
	}
	
	public void fillFromEntityModel(BlogComment blogComment) {
		this.message = blogComment.getMessage();
		this.email = blogComment.getUsersEMail();
	}

	public void fillEntityModel(BlogComment blogComment) {
		blogComment.setMessage(this.message);
		blogComment.setUsersEMail(this.email);
	}
	
	public String prepare(String s) {
		if (s == null) return "";
		return s.trim();
	}

	public void validate() {
		if (this.message.isEmpty()) {
			errors.put("message", "Message is required!");
		} else if (this.message.length() > 4096) {
			errors.put("Message", "Message can not be longer than 4096 characters!");
		}
		if (this.email.isEmpty()) {
			errors.put("email", "Email is required!");
		} else if (this.email.length() > 100) {
			errors.put("email", "Email can not be longer than 100 characters!");
		} else {
			int l = email.length();
			int p = email.indexOf('@');
			if(l<3 || p==-1 || p==0 || p==l-1) {
				errors.put("email", "Invalid Email.");
			}
		}
	}

}
