package hr.fer.oprpp2.hw05.web.servlets;

import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hr.fer.oprpp2.hw05.dao.DAOException;
import hr.fer.oprpp2.hw05.dao.DAOProvider;
import hr.fer.oprpp2.hw05.forms.BlogCommentForm;
import hr.fer.oprpp2.hw05.forms.BlogEntryForm;
import hr.fer.oprpp2.hw05.model.BlogComment;
import hr.fer.oprpp2.hw05.model.BlogEntry;
import hr.fer.oprpp2.hw05.model.BlogUser;

/**
 * Author servlet
 *
 */
@WebServlet("/servleti/author/*")
public class AuthorServlet extends HttpServlet{

	private static final long serialVersionUID = 1L;
	
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		String pathInfo = req.getPathInfo();
		
		if (pathInfo == null || pathInfo.equals("/")) {
			req.setAttribute("errorMessage", "Invalid path!");
			req.getRequestDispatcher("/WEB-INF/pages/error.jsp").forward(req, resp);
			return;
		}
		
		String[] path = pathInfo.split("/");
		if (path.length > 4) {
			req.setAttribute("errorMessage", "Invalid path!");
			req.getRequestDispatcher("/WEB-INF/pages/error.jsp").forward(req, resp);
			return;
		}
		
		BlogUser blogUserAuthor = DAOProvider.getDAO().getBlogUserByNick(path[1]);
		if (blogUserAuthor == null) {
			req.setAttribute("errorMessage", "Blog user not found!");
			req.getRequestDispatcher("/WEB-INF/pages/error.jsp").forward(req, resp);
			return;
		}
		
		if (path.length == 2) {
			req.setAttribute("author", blogUserAuthor);
			req.setAttribute("authorBlogEntries", DAOProvider.getDAO().getBlogEntriesByAuthor(blogUserAuthor));
			req.getRequestDispatcher("/WEB-INF/pages/author.jsp").forward(req, resp);
			return;
		}
		
		if (path[2].equals("new")) {
			String currentUserNick = (String) req.getSession().getAttribute("current.user.nick");
			if (currentUserNick == null || !currentUserNick.equals(blogUserAuthor.getNick())) {
				req.setAttribute("errorMessage", "You do not have permission to create new!");
				req.getRequestDispatcher("/WEB-INF/pages/error.jsp").forward(req, resp);
				return;
			}
			
			req.setAttribute("action", req.getContextPath() + "/servleti/author/" + blogUserAuthor.getNick() + "/new");
			req.getRequestDispatcher("/WEB-INF/pages/blogForm.jsp").forward(req, resp);
			
		} else if (path[2].equals("edit")) {
			long id = Long.parseLong(path[3]);
			
			String currentUserNick = (String) req.getSession().getAttribute("current.user.nick");
			if (currentUserNick == null || !currentUserNick.equals(blogUserAuthor.getNick())) {
				req.setAttribute("errorMessage", "You do not have permission to edit!");
				req.getRequestDispatcher("/WEB-INF/pages/error.jsp").forward(req, resp);
				return;
			}
			
			BlogEntry blogEntry = DAOProvider.getDAO().getBlogEntry(id);
			BlogEntryForm blogEntryForm = new BlogEntryForm();
			blogEntryForm.fillFromEntityModel(blogEntry);
			
			req.setAttribute("form", blogEntryForm);
			req.setAttribute("action", req.getContextPath() + "/servleti/author/" + blogEntry.getAuthor().getNick() + "/edit/" + blogEntry.getId());
			req.getRequestDispatcher("/WEB-INF/pages/blogForm.jsp").forward(req, resp);
			
		} else {
			long id;
			BlogEntry blogEntry;
			try {
				id = Long.parseLong(path[2]);
				blogEntry = DAOProvider.getDAO().getBlogEntry(id);
			} catch (NumberFormatException | DAOException e ) {
				req.setAttribute("errorMessage", "Invalid path!");
				req.getRequestDispatcher("/WEB-INF/pages/error.jsp").forward(req, resp);
				return;
			}	
				
			req.setAttribute("blogEntry", blogEntry);
			req.getRequestDispatcher("/WEB-INF/pages/blogEntry.jsp").forward(req, resp);
			
		}
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String[] path = req.getPathInfo().split("/");
		BlogUser author = DAOProvider.getDAO().getBlogUserByNick(path[1]);
		
		if (path[2].equals("new")) {
			BlogEntryForm blogEntryForm = new BlogEntryForm();
			blogEntryForm.fillFromHttpRequest(req);
			blogEntryForm.validate();
			
			if (blogEntryForm.hasErrors()) {
				req.setAttribute("form", blogEntryForm);
				req.getRequestDispatcher("/WEB-INF/pages/blogForm.jsp").forward(req, resp);
				return;
			}
			
			BlogEntry blogEntry = new BlogEntry();
			blogEntryForm.fillEntityModel(blogEntry);
			blogEntry.setAuthor(author);
			blogEntry.setCreatedAt(new Date());
			
			DAOProvider.getDAO().addBlogEntry(blogEntry);
			
			resp.sendRedirect(req.getContextPath() + "/servleti/author/" + author.getNick());
			
		} else if (path[2].equals("edit")) {
			long id = Long.parseLong(path[3]);
			
			BlogEntryForm blogEntryForm = new BlogEntryForm();
			blogEntryForm.fillFromHttpRequest(req);
			blogEntryForm.validate();
			
			if (blogEntryForm.hasErrors()) {
				req.setAttribute("form", blogEntryForm);
				req.getRequestDispatcher("/WEB-INF/pages/blogForm.jsp").forward(req, resp);
				return;
			}
			
			BlogEntry blogEntry =  DAOProvider.getDAO().getBlogEntry(id);
			blogEntryForm.fillEntityModel(blogEntry);
			blogEntry.setLastModifiedAt(new Date());
			
			DAOProvider.getDAO().addBlogEntry(blogEntry);
			
			resp.sendRedirect(req.getContextPath() + "/servleti/author/" + author.getNick() + "/" + id);
			
		} else {
			long id = Long.parseLong(path[2]);
			BlogEntry blogEntry = DAOProvider.getDAO().getBlogEntry(id);
			
			BlogCommentForm blogCommentForm = new BlogCommentForm();
			blogCommentForm.fillFromHttpRequest(req);
			blogCommentForm.validate();
			
			if (blogCommentForm.hasErrors()) {
				req.setAttribute("form", blogCommentForm);
				req.getRequestDispatcher("/WEB-INF/pages/blogEntry.jsp").forward(req, resp);
				return;
			}
			
			BlogComment blogComment = new BlogComment();
			blogCommentForm.fillEntityModel(blogComment);
			blogComment.setBlogEntry(blogEntry);
			blogComment.setPostedOn(new Date());
			
			DAOProvider.getDAO().addBlogComment(blogComment);
			
			resp.sendRedirect(req.getContextPath() + "/servleti/author/" + author.getNick() + "/" + id);
					
		}
				
	}
	

}
