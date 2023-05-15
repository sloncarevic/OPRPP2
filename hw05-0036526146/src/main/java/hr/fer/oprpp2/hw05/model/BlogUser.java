package hr.fer.oprpp2.hw05.model;

import java.util.List;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * Model of blog user
 *
 */
@NamedQueries({
	@NamedQuery(name="BlogUser.getBlogUserByNick",query="select u from BlogUser as u where u.nick=:nick"),
	@NamedQuery(name="BlogUser.getBlogUsers",query="select u from BlogUser as u")
})

@Entity
@Table(name = "blog_users")
public class BlogUser {
	
	private Long id;
	private String firstName;
	private String lastName;
	private String nick;
	private String email;
	private String passwordHash;
	private List<BlogEntry> blogEntries;
	
	@Id @GeneratedValue
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	@Column(length = 200, nullable = false )
	public String getFirstName() {
		return firstName;
	}
	
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	@Column(length = 200, nullable = false )
	public String getLastName() {
		return lastName;
	}
	
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	@Column(length = 200, nullable = false, unique = true )
	public String getNick() {
		return nick;
	}
	
	public void setNick(String nick) {
		this.nick = nick;
	}
	
	@Column(length = 200, nullable = false )
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	@Column(length = 200, nullable = false )
	public String getPasswordHash() {
		return passwordHash;
	}
	
	public void setPasswordHash(String passwordHash) {
		this.passwordHash = passwordHash;
	}
	
	
	@OneToMany(mappedBy = "author", fetch = FetchType.LAZY, cascade=CascadeType.PERSIST, orphanRemoval=true)
	public List<BlogEntry> getBlogEntries() {
		return blogEntries;
	}

	public void setBlogEntries(List<BlogEntry> blogEntries) {
		this.blogEntries = blogEntries;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BlogUser other = (BlogUser) obj;
		return Objects.equals(id, other.id);
	}
	

}
