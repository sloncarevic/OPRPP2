package hr.fer.oprpp2.hw05.dao;

import hr.fer.oprpp2.hw05.dao.jpa.JPADAOImpl;

/**
 * DAO provider singleton
 *
 */
public class DAOProvider {

	private static DAO dao = new JPADAOImpl();
	
	public static DAO getDAO() {
		return dao;
	}
}
