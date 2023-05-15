package hr.fer.oprpp2.hw05.forms;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import hr.fer.oprpp2.hw05.model.BlogEntry;

/**
 * Blog entry form for validating web form
 *
 */
public class BlogEntryForm {

	private String title;
	private String text;
	
	private Map<String, String> errors = new HashMap<String, String>();
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
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
		this.title = prepare(req.getParameter("title"));
		this.text = prepare(req.getParameter("text"));
	}
	
	public void fillFromEntityModel(BlogEntry blogEntry) {
		this.title = blogEntry.getTitle();
		this.text = blogEntry.getText();
	}

	public void fillEntityModel(BlogEntry blogEntry) {
		blogEntry.setTitle(this.title);
		blogEntry.setText(this.text);
	}
	
	public String prepare(String s) {
		if (s == null) return "";
		return s.trim();
	}

	public void validate() {
		if (this.title.isEmpty()) {
			errors.put("title", "Title is required!");
		} else if (this.title.length() > 200) {
			errors.put("title", "Title can not be longer than 200 characters!");
		}
		if (this.text.isEmpty()) {
			errors.put("text", "Text is required!");
		} else if (this.text.length() > 4096) {
			errors.put("text", "Text can not be longer than 4096 characters!");
		}
		
	}
	
}
