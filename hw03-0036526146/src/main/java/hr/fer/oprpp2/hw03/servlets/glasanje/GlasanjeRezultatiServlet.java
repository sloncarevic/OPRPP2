package hr.fer.oprpp2.hw03.servlets.glasanje;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hr.fer.oprpp2.hw03.servlets.glasanje.model.Band;
import hr.fer.oprpp2.hw03.servlets.glasanje.model.Bands;

@WebServlet("/glasanje-rezultati")
public class GlasanjeRezultatiServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		// Pročitaj rezultate iz /WEB-INF/glasanje-rezultati.txt
		String fileName = req.getServletContext().getRealPath("/WEB-INF/glasanje-rezultati.txt");
		
		
		// Napravi datoteku ako je potrebno; inače je samo pročitaj...
		// ...
		
		if (!Files.exists(Paths.get(fileName))) {
			File file = new File(fileName);
			file.createNewFile();
		}
		
		String fileBands = req.getServletContext().getRealPath("/WEB-INF/glasanje-definicija.txt");
		
		Map<Integer, Band> bands = Bands.loadBand(fileBands);
		
		List<Band> bandsList = Bands.getBandsList(bands, fileName);
		
		
		req.setAttribute("bands", bandsList);
		
		// Pošalji ih JSP-u...		
		req.getRequestDispatcher("/WEB-INF/pages/glasanjeRez.jsp").forward(req, resp);
	
	}
	
}
