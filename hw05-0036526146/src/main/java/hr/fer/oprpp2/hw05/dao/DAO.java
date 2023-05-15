package hr.fer.oprpp2.hw05.dao;

import java.util.List;

import hr.fer.oprpp2.hw05.model.BlogComment;
import hr.fer.oprpp2.hw05.model.BlogEntry;
import hr.fer.oprpp2.hw05.model.BlogUser;

/**
 * Data access object interface
 *
 */
public interface DAO {

	/**
	 * Get blog entry by id
	 * @param id
	 * @return
	 * @throws DAOException
	 */
	public BlogEntry getBlogEntry(Long id) throws DAOException;
	
	/**
	 * Get all blog entries from given author
	 * @param author
	 * @return
	 * @throws DAOException
	 */
	public List<BlogEntry> getBlogEntriesByAuthor(BlogUser author) throws DAOException;
	
	/**
	 * Get blog user by nickname
	 * @param nick
	 * @return
	 * @throws DAOException
	 */
	public BlogUser getBlogUserByNick(String nick) throws DAOException;	

	/**
	 * Get all blog users
	 * @return
	 * @throws DAOException
	 */
	public List<BlogUser> getBlogUsers() throws DAOException;
	
	
	/**
	 * Persist blog entry
	 * @param blogEntry
	 * @throws DAOException
	 */
	public void addBlogEntry(BlogEntry blogEntry) throws DAOException;
	
	/**
	 * Persist blog comment
	 * @param blogComment
	 * @throws DAOException
	 */
	public void addBlogComment(BlogComment blogComment) throws DAOException;
	
	/**
	 * Persist blog user
	 * @param blogUser
	 * @throws DAOException
	 */
	public void addBlogUser(BlogUser blogUser) throws DAOException;
 	
	
	
}
