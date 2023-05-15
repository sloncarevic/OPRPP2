package hr.fer.oprpp2.hw05.dao.jpa;

import java.util.List;

import hr.fer.oprpp2.hw05.dao.DAO;
import hr.fer.oprpp2.hw05.dao.DAOException;
import hr.fer.oprpp2.hw05.model.BlogComment;
import hr.fer.oprpp2.hw05.model.BlogEntry;
import hr.fer.oprpp2.hw05.model.BlogUser;

/**
 * Implementation of DAO
 */
public class JPADAOImpl implements DAO{

	@Override
	public BlogEntry getBlogEntry(Long id) throws DAOException {
		
		BlogEntry blogEntry = JPAEMProvider.getEntityManager().find(BlogEntry.class, id);
		return blogEntry;
	}

	@Override
	public List<BlogEntry> getBlogEntriesByAuthor(BlogUser author) throws DAOException {
		
		return JPAEMProvider.getEntityManager().createNamedQuery("BlogEntry.getBlogEntriesByAuthor", BlogEntry.class)
				.setParameter("author", author)
				.getResultList();
	}

	@Override
	public BlogUser getBlogUserByNick(String nick) throws DAOException {
		
		List<BlogUser> res =  JPAEMProvider.getEntityManager().createNamedQuery("BlogUser.getBlogUserByNick", BlogUser.class)
				.setParameter("nick", nick)
				.getResultList();
		if (res.isEmpty())
			return null;
		return res.get(0);
	}

	@Override
	public List<BlogUser> getBlogUsers() throws DAOException {
		
		return JPAEMProvider.getEntityManager().createNamedQuery("BlogUser.getBlogUsers", BlogUser.class)
				.getResultList();
	}

	@Override
	public void addBlogEntry(BlogEntry blogEntry) throws DAOException {
		
		JPAEMProvider.getEntityManager().persist(blogEntry);
		
	}

	@Override
	public void addBlogComment(BlogComment blogComment) throws DAOException {
		
		JPAEMProvider.getEntityManager().persist(blogComment);
		
	}

	@Override
	public void addBlogUser(BlogUser blogUser) throws DAOException {
		
		JPAEMProvider.getEntityManager().persist(blogUser);
		
	}
	
}
