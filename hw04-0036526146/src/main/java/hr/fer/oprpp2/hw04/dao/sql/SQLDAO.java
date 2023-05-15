package hr.fer.oprpp2.hw04.dao.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.sql.DataSource;

import hr.fer.oprpp2.hw04.dao.DAO;
import hr.fer.oprpp2.hw04.dao.DAOException;
import hr.fer.oprpp2.hw04.model.Poll;
import hr.fer.oprpp2.hw04.model.PollOption;

/**
 * Class implementation of DAO
 *
 */
public class SQLDAO implements DAO {

	@Override
	public void checkDB(ServletContext servletContext, DataSource cpds) throws DAOException {
		
		try (Connection connection = cpds.getConnection()) {
			
			try ( ResultSet rSetPolls = connection.getMetaData().getTables(null, null, "POLLS", null);
				  ResultSet rSetPollOptions = connection.getMetaData().getTables(null, null, "POLLOPTIONS", null); ){
				
				if ( ! rSetPolls.next()) {
					createTable(connection, """
										CREATE TABLE Polls
										 (id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
										 title VARCHAR(150) NOT NULL,
										 message CLOB(2048) NOT NULL
										)
										""" );
				}
				
				if ( ! rSetPollOptions.next()) {
					createTable(connection, """
										CREATE TABLE PollOptions
										 (id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
										 optionTitle VARCHAR(100) NOT NULL,
										 optionLink VARCHAR(150) NOT NULL,
										 pollID BIGINT,
										 votesCount BIGINT,
										 FOREIGN KEY (pollID) REFERENCES Polls(id)
										)
										""");
				}
			}
			
			PreparedStatement preparedStatement = connection.prepareStatement("SELECT Count(*) FROM Polls");
			ResultSet rSet = preparedStatement.executeQuery();
			rSet.next();
			if (rSet.getInt(1) == 0) {
				
				PreparedStatement pst = connection.prepareStatement("INSERT INTO Polls (title, message) VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS);
				pst.setString(1, "Glasanje za omiljeni bend");
				pst.setString(2, "Od sljedećih bendova, koji Vam je bend najdraži? Kliknite na link kako biste glasali!");

				pst.executeUpdate();
				ResultSet rset = pst.getGeneratedKeys();
				long id = 0;
				try {
					if(rset != null && rset.next()) {
						id = rset.getLong(1);
					}
				} finally {
					try { rset.close(); } catch(SQLException ex) {
						ex.printStackTrace();
					}
				}
				
				pst = connection.prepareStatement("INSERT INTO PollOptions (optionTitle, optionLink, pollID, votesCount) VALUES (?, ?, ?, ?)");
				
				pst.setString(1, "The Beatles"); pst.setString(2, "https://www.youtube.com/watch?v=z9ypq6_5bsg"); pst.setLong(3, id); pst.setLong(4, 150);
				pst.addBatch();
				pst.setString(1, "The Platters"); pst.setString(2, "https://www.youtube.com/watch?v=H2di83WAOhU"); pst.setLong(3, id); pst.setLong(4, 60);
				pst.addBatch();
				pst.setString(1, "The Beach Boys"); pst.setString(2, "https://www.youtube.com/watch?v=2s4slliAtQU"); pst.setLong(3, id); pst.setLong(4, 150);
				pst.addBatch();
				pst.setString(1, "The Four Seasons"); pst.setString(2, "https://www.youtube.com/watch?v=y8yvnqHmFds"); pst.setLong(3, id); pst.setLong(4, 20);
				pst.addBatch();
				pst.setString(1, "The Marcels"); pst.setString(2, "https://www.youtube.com/watch?v=qoi3TH59ZEs"); pst.setLong(3, id); pst.setLong(4, 33);
				pst.addBatch();
				pst.setString(1, "The Everly Brothers"); pst.setString(2, "https://www.youtube.com/watch?v=tbU3zdAgiX8"); pst.setLong(3, id); pst.setLong(4, 25);
				pst.addBatch();
				pst.setString(1, "The Mamas And The Papas"); pst.setString(2, "https://www.youtube.com/watch?v=N-aK6JnyFmk"); pst.setLong(3, id); pst.setLong(4, 20);
				pst.addBatch();
				
				pst.executeBatch();
				
				pst.close();
				
				
				pst = connection.prepareStatement("INSERT INTO Polls (title, message) VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS);
				pst.setString(1, "Glasanje za omiljeni glazeni instrument");
				pst.setString(2, "Od sljedećih instrumenata, koji Vam je najdraži? Kliknite na link kako biste glasali!");

				pst.executeUpdate();
				rset = pst.getGeneratedKeys();
				id = 0;
				try {
					if(rset != null && rset.next()) {
						id = rset.getLong(1);
					}
				} finally {
					try { rset.close(); } catch(SQLException ex) {
						ex.printStackTrace();
					}
				}
				
				pst = connection.prepareStatement("INSERT INTO PollOptions (optionTitle, optionLink, pollID, votesCount) VALUES (?, ?, ?, ?)");
				
				pst.setString(1, "Gitara"); pst.setString(2, "https://www.youtube.com/results?search_query=guitar"); pst.setLong(3, id); pst.setLong(4, 50);
				pst.addBatch();
				pst.setString(1, "Klavir"); pst.setString(2, "https://www.youtube.com/results?search_query=piano"); pst.setLong(3, id); pst.setLong(4, 60);
				pst.addBatch();
				pst.setString(1, "Flauta"); pst.setString(2, "https://www.youtube.com/results?search_query=flute"); pst.setLong(3, id); pst.setLong(4, 50);
				pst.addBatch();
				pst.setString(1, "Truba"); pst.setString(2, "https://www.youtube.com/results?search_query=trumpet"); pst.setLong(3, id); pst.setLong(4, 20);
				pst.addBatch();
				pst.setString(1, "Bubnjevi"); pst.setString(2, "https://www.youtube.com/results?search_query=drums"); pst.setLong(3, id); pst.setLong(4, 33);
				pst.addBatch();
				pst.setString(1, "Violina"); pst.setString(2, "https://www.youtube.com/results?search_query=violin"); pst.setLong(3, id); pst.setLong(4, 25);
				pst.addBatch();
				
				pst.executeBatch();
				
				pst.close();
				
			}
			rSet.close();
			preparedStatement.close();
			
		} catch (Exception e) {
			throw new DAOException(e.getMessage());
		}
		
	}
	
	private void createTable(Connection connection, String statment) throws SQLException {
		try (PreparedStatement pst = connection.prepareStatement(statment);) { 
			pst.executeUpdate();
		}
	}

	@Override
	public Poll getPollById(long pollId) throws DAOException {
		Connection connection = SQLConnectionProvider.getConnection();
		try (PreparedStatement pst = connection.prepareStatement("SELECT * FROM Polls WHERE id = (?)")) {
			
			pst.setLong(1, pollId);
			ResultSet rSet = pst.executeQuery();
			 
			Poll poll = null;
			if (rSet.next()) {
				poll = new Poll(rSet.getLong(1), rSet.getString(2), rSet.getString(3));
			}
			
			rSet.close();
			return poll;
			
		} catch (SQLException e) {
			throw new DAOException();
		}
	}
	
	@Override
	public List<Poll> getPolls() throws DAOException {
		Connection connection = SQLConnectionProvider.getConnection();
		try (PreparedStatement pst = connection.prepareStatement("SELECT * FROM Polls");
			 ResultSet rSet = pst.executeQuery();) {
			
			List<Poll> polls = new ArrayList<Poll>();
			while (rSet.next()) {
				polls.add(new Poll(rSet.getLong(1), rSet.getString(2), rSet.getString(3)));
			}
			
			return polls;
			
		} catch (SQLException e) {
			throw new DAOException();
		}
	}

	@Override
	public PollOption getPollOptionById(long pollOptionId) throws DAOException {
		Connection connection = SQLConnectionProvider.getConnection();
		try (PreparedStatement pst = connection.prepareStatement("SELECT * FROM PollOptions WHERE id = (?)")) {
			
			pst.setLong(1, pollOptionId);
			ResultSet rSet = pst.executeQuery();
			 
			PollOption pollOption = null;
			if (rSet.next()) {
				pollOption = new PollOption(rSet.getLong(1), rSet.getString(2), rSet.getString(3), rSet.getLong(4), rSet.getLong(5));
			}
			
			rSet.close();
			return pollOption;
			
		} catch (SQLException e) {
			throw new DAOException();
		}
	}

	@Override
	public List<PollOption> getPollOptionsByPollId(long pollId) throws DAOException {
		Connection connection = SQLConnectionProvider.getConnection();
		try (PreparedStatement pst = connection.prepareStatement("SELECT * FROM PollOptions WHERE pollID = (?)")) {
			pst.setLong(1, pollId);
			ResultSet rSet = pst.executeQuery();
			 
			List<PollOption> pollOptions = new ArrayList<>();
			while (rSet.next()) {
				pollOptions.add(new PollOption(rSet.getLong(1), rSet.getString(2), rSet.getString(3), rSet.getLong(4), rSet.getLong(5)));
			}
			
			rSet.close();
			return pollOptions;
			
		} catch (SQLException e) {
			throw new DAOException();
		}
	}
	
	@Override
	public void addVote(long pollOptionId) throws DAOException {
		PollOption pollOption = getPollOptionById(pollOptionId);
		pollOption.setVotesCount(pollOption.getVotesCount() + 1);
		
		Connection connection = SQLConnectionProvider.getConnection();
		try (PreparedStatement pst = connection.prepareStatement("UPDATE PollOptions SET votesCount = " + pollOption.getVotesCount() + " WHERE id = " + pollOptionId)) {
			pst.executeUpdate();
		} catch (SQLException e) {
			throw new DAOException();
		}	
	}


}