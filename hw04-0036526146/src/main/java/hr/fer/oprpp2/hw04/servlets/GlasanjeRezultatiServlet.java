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
import hr.fer.oprpp2.hw04.model.PollOption;


@WebServlet("/servleti/glasanje-rezultati")
public class GlasanjeRezultatiServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
			
		Poll poll = (Poll) req.getSession().getAttribute("poll");
		
		List<PollOption> pollOptions = DAOProvider.getDao().getPollOptionsByPollId(poll.getPollId());
		
		pollOptions.sort((a,b) -> (int)(b.getVotesCount() - a.getVotesCount()));
		
		req.setAttribute("pollOptions", pollOptions);
		
		req.getRequestDispatcher("/WEB-INF/pages/glasanjeRez.jsp").forward(req, resp);
	
	}
	
}
