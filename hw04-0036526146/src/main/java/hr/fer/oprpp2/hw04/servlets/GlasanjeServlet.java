package hr.fer.oprpp2.hw04.servlets;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hr.fer.oprpp2.hw04.dao.DAO;
import hr.fer.oprpp2.hw04.dao.DAOProvider;
import hr.fer.oprpp2.hw04.model.Poll;
import hr.fer.oprpp2.hw04.model.PollOption;

@WebServlet("/servleti/glasanje")
public class GlasanjeServlet extends HttpServlet{

	private static final long serialVersionUID = 1;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		int pollId = Integer.valueOf(req.getParameter("pollId"));
		
		DAO dao = DAOProvider.getDao();
		
		Poll poll = dao.getPollById(pollId);
		req.getSession().setAttribute("poll", poll);
		
		List<PollOption> pollOptions = dao.getPollOptionsByPollId(pollId);
		req.setAttribute("pollOptions", pollOptions);
		
		req.getRequestDispatcher("/WEB-INF/pages/glasanjeIndex.jsp").forward(req, resp);
	}
	

}
