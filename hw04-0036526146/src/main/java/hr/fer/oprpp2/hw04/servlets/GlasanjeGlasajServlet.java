package hr.fer.oprpp2.hw04.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hr.fer.oprpp2.hw04.dao.DAOProvider;

@WebServlet("/servleti/glasanje-glasaj")
public class GlasanjeGlasajServlet extends HttpServlet{

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		long pollOptionId = Long.valueOf(req.getParameter("pollOptionId"));
		
		DAOProvider.getDao().addVote(pollOptionId);
		
		resp.sendRedirect(req.getContextPath() + "/servleti/glasanje-rezultati");

	}
	
}
