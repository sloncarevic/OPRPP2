package hr.fer.oprpp2.hw04.dao;

import java.util.List;

import javax.servlet.ServletContext;
import javax.sql.DataSource;

import hr.fer.oprpp2.hw04.model.Poll;
import hr.fer.oprpp2.hw04.model.PollOption;

/**
 * Data Access Object interface
 *
 */
public interface DAO {

	/**
	 * Check if DB contains all necessary data
	 * @param servletContext
	 * @param cpds
	 * @throws DAOException
	 */
	void checkDB(ServletContext servletContext, DataSource cpds) throws DAOException;
	
	/**
	 * Get poll by id
	 * @param pollId
	 * @return
	 * @throws DAOException
	 */
	Poll getPollById(long pollId) throws DAOException;
	
	/**
	 * Get all polls
	 * @return
	 * @throws DAOException
	 */
	List<Poll> getPolls() throws DAOException;
	
	/**
	 * Get poll option by id
	 * @param pollOptionId
	 * @return
	 * @throws DAOException
	 */
	PollOption getPollOptionById(long pollOptionId) throws DAOException;
	
	/**
	 * Get poll option by poll id
	 * @param pollId
	 * @return
	 * @throws DAOException
	 */
	List<PollOption> getPollOptionsByPollId(long pollId) throws DAOException;
	
	/** Add vote
	 * @param pollOptionId
	 * @throws DAOException
	 */
	void addVote(long pollOptionId) throws DAOException;
	
}