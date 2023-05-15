package hr.fer.oprpp2.hw03.servlets.glasanje;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hr.fer.oprpp2.hw03.servlets.glasanje.model.Band;
import hr.fer.oprpp2.hw03.servlets.glasanje.model.Bands;

@WebServlet("/glasanje")
public class GlasanjeServlet extends HttpServlet{

	private static final long serialVersionUID = 1;
	
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		// Učitaj raspoložive bendove
		String fileName = req.getServletContext().getRealPath("/WEB-INF/glasanje-definicija.txt");
		
		// ...
		Map<Integer, Band> bands = Bands.loadBand(fileName);
		req.setAttribute("bands", bands);
		
		// Pošalji ih JSP-u...
		req.getRequestDispatcher("/WEB-INF/pages/glasanjeIndex.jsp").forward(req, resp);
	}
	

}
