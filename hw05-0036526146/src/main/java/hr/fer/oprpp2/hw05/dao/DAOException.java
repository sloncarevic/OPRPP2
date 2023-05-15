package hr.fer.oprpp2.hw05.dao;

/**
 * DAO Exception
 *
 */
public class DAOException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;

	public DAOException(String message, Throwable cause) {
		super(message, cause);
	}

	public DAOException(String message) {
		super(message);
	}
}
