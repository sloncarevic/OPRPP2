package hr.fer.oprpp2.hw04.model;

import java.io.Serializable;

/**
 * Model of poll
 * Serializable
 *
 */
public class Poll implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private long pollId;
	private String title;
	private String message;
	
	public Poll() {
	}
	
	public Poll(long pollId, String title, String message) {
		this.pollId = pollId;
		this.title = title;
		this.message = message;
	}

	public long getPollId() {
		return pollId;
	}

	public void setPollId(long pollId) {
		this.pollId = pollId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	

}
