package hr.fer.oprpp2.hw05.web.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hr.fer.oprpp2.hw05.dao.DAOProvider;

/**
 * Main servlet
 *
 */
@WebServlet("/servleti/main")
public class MainServlet extends HttpServlet{

	private static final long serialVersionUID = 1L;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		req.setAttribute("blogUsers", DAOProvider.getDAO().getBlogUsers());
		
		req.getRequestDispatcher("/WEB-INF/pages/main.jsp").forward(req, resp);
	}
	
	

}
