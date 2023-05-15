package hr.fer.oprpp2.hw05.forms;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import hr.fer.oprpp2.hw05.util.CryptoUtil;

/**
 * Login form for validating web form
 *
 */
public class LoginForm {
	
	private String nick;
	private String passwordHash;
	
	private Map<String, String> errors = new HashMap<String, String>();
	
	public LoginForm() {
		
	}

	public LoginForm(String nick, String passwordHash) {
		this.nick = nick;
		this.passwordHash = passwordHash;
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
		this.nick = prepare(req.getParameter("nick"));
		this.passwordHash = prepare(req.getParameter("password"));
		if (!this.passwordHash.isEmpty())
			this.passwordHash = CryptoUtil.encrypt(passwordHash);
	}
	
	
	public String prepare(String s) {
		if (s == null) return "";
		return s.trim();
	}

	public void validate() {
		if (this.nick.isEmpty()) {
			errors.put("nick", "Nickname is required!");
		}
		if (this.passwordHash.isEmpty()) {
			errors.put("password", "Password is required!");
		}
	}

}
