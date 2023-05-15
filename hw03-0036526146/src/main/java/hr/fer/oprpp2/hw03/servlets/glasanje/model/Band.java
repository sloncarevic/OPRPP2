package hr.fer.oprpp2.hw03.servlets.glasanje.model;

import java.io.Serializable;
import java.util.Objects;

/**
 * Class implementation of Band
 *
 */
public class Band implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private int ID;
	
	private String name;
	
	private String link;
	
	private int votes;

	public Band(int iD, String name, String link) {
		ID = iD;
		this.name = name;
		this.link = link;
	}

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public int getVotes() {
		return votes;
	}

	public void setVotes(int votes) {
		this.votes = votes;
	}

	@Override
	public String toString() {
		return getID() + "\t" + getName() + "\t" + getLink();
	}

	@Override
	public int hashCode() {
		return Objects.hash(ID, link, name);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Band other = (Band) obj;
		return ID == other.ID && Objects.equals(link, other.link) && Objects.equals(name, other.name);
	}
	
	
	
	

}
