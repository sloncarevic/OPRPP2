package hr.fer.oprpp2.hw05.web.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hr.fer.oprpp2.hw05.dao.DAOProvider;
import hr.fer.oprpp2.hw05.forms.RegistrationForm;
import hr.fer.oprpp2.hw05.model.BlogUser;

/**
 * Register servlet
 *
 */
@WebServlet("/servleti/register")
public class RegisterServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;


	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		req.getRequestDispatcher("/WEB-INF/pages/register.jsp").forward(req, resp);
	}
	
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		RegistrationForm registrationForm = new RegistrationForm();
		registrationForm.fillFromHttpRequest(req);
		registrationForm.validate();
		
		if (registrationForm.hasErrors()) {
			req.setAttribute("form", registrationForm);
			req.getRequestDispatcher("/WEB-INF/pages/register.jsp").forward(req, resp);
			return;
		}
		
		BlogUser blogUser = DAOProvider.getDAO().getBlogUserByNick(registrationForm.getNick());
		
		if (blogUser != null ) {
			req.setAttribute("error", "Entered nickname is taken! Please choose another.");
			req.setAttribute("form", registrationForm);
			req.getRequestDispatcher("/WEB-INF/pages/register.jsp").forward(req, resp);
			return;
		}
		
		blogUser = new BlogUser();
		registrationForm.fillEntityModel(blogUser);
		
		DAOProvider.getDAO().addBlogUser(blogUser);
		
		resp.sendRedirect(req.getContextPath() + "/servleti/main");
	}

}
