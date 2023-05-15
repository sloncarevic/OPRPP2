package hr.fer.oprpp2.hw05.forms;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import hr.fer.oprpp2.hw05.model.BlogUser;
import hr.fer.oprpp2.hw05.util.CryptoUtil;

/**
 * Registration form for validating web form
 *
 */
public class RegistrationForm {
	
	private String firstName;
	private String lastName;
	private String email;
	private String nick;
	private String passwordHash;
	
	private Map<String, String> errors = new HashMap<String, String>();
	
	public RegistrationForm() {
		
	}
	
	public RegistrationForm(String firstName, String lastName, String email, String nick, String passwordHash) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.nick = nick;
		this.passwordHash = passwordHash;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getNick() {
		return nick;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}

	public String getPasswordHash() {
		return passwordHash;
	}

	public void setPasswordHash(String passwordHash) {
		this.passwordHash = passwordHash;
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
		this.firstName = prepare(req.getParameter("firstName"));
		this.lastName = prepare(req.getParameter("lastName"));
		this.email = prepare(req.getParameter("email"));
		this.nick = prepare(req.getParameter("nick"));
		this.passwordHash = prepare(req.getParameter("password"));
		if (!this.passwordHash.isEmpty())
			this.passwordHash = CryptoUtil.encrypt(passwordHash);
	}
	
	public void fillFromEntityModel(BlogUser blogUser) {
		this.firstName = blogUser.getFirstName();
		this.lastName = blogUser.getLastName();
		this.nick = blogUser.getNick();
		this.email = blogUser.getEmail();
		this.passwordHash = blogUser.getPasswordHash();
	}

	public void fillEntityModel(BlogUser blogUser) {
		blogUser.setFirstName(this.firstName);
		blogUser.setLastName(this.lastName);
		blogUser.setNick(this.nick);
		blogUser.setEmail(this.email);
		blogUser.setPasswordHash(this.passwordHash);
	}
	
	public String prepare(String s) {
		if (s == null) return "";
		return s.trim();
	}

	public void validate() {
		if (this.firstName.isEmpty()) {
			errors.put("firstName", "First name is required!");
		}
		if (this.lastName.isEmpty()) {
			errors.put("lastName", "Last name is required!");
		}
		if (this.email.isEmpty()) {
			errors.put("email", "Email is required!");
		} else {
			int l = email.length();
			int p = email.indexOf('@');
			if(l<3 || p==-1 || p==0 || p==l-1) {
				errors.put("email", "Invalid Email.");
			}
		}
		if (this.nick.isEmpty()) {
			errors.put("nick", "Nickname is required!");
		}
		if (this.passwordHash.isEmpty()) {
			errors.put("password", "Password is required!");
		}
	}
	
}
