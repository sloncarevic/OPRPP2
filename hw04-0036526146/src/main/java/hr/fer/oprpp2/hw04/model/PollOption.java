package hr.fer.oprpp2.hw04.model;

import java.io.Serializable;

/**
 * Model of poll option
 *
 */
public class PollOption implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private long pollOptionId;
	private String optionTitle;
	private String optionLink;
	private long pollId;
	private long votesCount;
	
	public PollOption(long pollOptionId, String optionTitle, String optionLink, long pollId, long votesCount) {
		this.pollOptionId = pollOptionId;
		this.optionTitle = optionTitle;
		this.optionLink = optionLink;
		this.pollId = pollId;
		this.votesCount = votesCount;
	}

	public long getPollOptionId() {
		return pollOptionId;
	}

	public void setPollOptionId(long pollOptionId) {
		this.pollOptionId = pollOptionId;
	}

	public String getOptionTitle() {
		return optionTitle;
	}

	public void setOptionTitle(String optionTitle) {
		this.optionTitle = optionTitle;
	}

	public String getOptionLink() {
		return optionLink;
	}

	public void setOptionLink(String optionLink) {
		this.optionLink = optionLink;
	}

	public long getPollId() {
		return pollId;
	}

	public void setPollId(long pollId) {
		this.pollId = pollId;
	}

	public long getVotesCount() {
		return votesCount;
	}

	public void setVotesCount(long votesCount) {
		this.votesCount = votesCount;
	}
	
	

}
