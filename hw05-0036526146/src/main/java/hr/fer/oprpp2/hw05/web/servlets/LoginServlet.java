package hr.fer.oprpp2.hw05.web.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hr.fer.oprpp2.hw05.dao.DAOProvider;
import hr.fer.oprpp2.hw05.forms.LoginForm;
import hr.fer.oprpp2.hw05.model.BlogUser;

/**
 * Login servlet
 *
 */
@WebServlet("/servleti/login")
public class LoginServlet extends HttpServlet{

	private static final long serialVersionUID = 1L;
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		LoginForm loginForm = new LoginForm();
		loginForm.fillFromHttpRequest(req);
		loginForm.validate();
		
		if (loginForm.hasErrors()) {
			req.setAttribute("form", loginForm);
			req.getRequestDispatcher("/WEB-INF/pages/main.jsp").forward(req, resp);
			return;
		}
		
		BlogUser blogUser = DAOProvider.getDAO().getBlogUserByNick(loginForm.getNick());
		
		if (blogUser == null || !loginForm.getPasswordHash().equals(blogUser.getPasswordHash())) {
			req.setAttribute("form", loginForm);
			req.setAttribute("error", "Invalid nickname or password!");
			req.getRequestDispatcher("/WEB-INF/pages/main.jsp").forward(req, resp);
			return;
		}
		
		req.getSession().setAttribute("current.user.id", blogUser.getId());
		req.getSession().setAttribute("current.user.fn", blogUser.getFirstName());
		req.getSession().setAttribute("current.user.ln", blogUser.getLastName());
		req.getSession().setAttribute("current.user.email", blogUser.getEmail());
		req.getSession().setAttribute("current.user.nick", blogUser.getNick());
		
		resp.sendRedirect(req.getContextPath() + "/servleti/main");
	}
	

}
