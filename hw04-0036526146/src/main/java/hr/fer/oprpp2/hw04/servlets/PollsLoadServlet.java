package hr.fer.oprpp2.hw04.servlets;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hr.fer.oprpp2.hw04.dao.DAOProvider;
import hr.fer.oprpp2.hw04.model.Poll;

@WebServlet("/servleti/index.html")
public class PollsLoadServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		List<Poll> pollsList = DAOProvider.getDao().getPolls();
		
		req.setAttribute("polls", pollsList);
		
		req.getRequestDispatcher("/WEB-INF/pages/index.jsp").forward(req, resp);
	}
}
