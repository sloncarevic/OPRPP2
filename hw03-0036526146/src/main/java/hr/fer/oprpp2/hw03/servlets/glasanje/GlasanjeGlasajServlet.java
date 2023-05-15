package hr.fer.oprpp2.hw03.servlets.glasanje;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/glasanje-glasaj")
public class GlasanjeGlasajServlet extends HttpServlet{

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		// Zabiljezi glas...
		String fileName = req.getServletContext().getRealPath("/WEB-INF/glasanje-rezultati.txt");
		
		// Napravi datoteku ako je potrebno; ažuriraj podatke koji su u njoj...
		// ...
		// ...
		
		if (!Files.exists(Paths.get(fileName))) {
			File file = new File(fileName);
			file.createNewFile();
		}
		
		List<String> rows = null;
		rows = Files.readAllLines(Paths.get(fileName), StandardCharsets.UTF_8);
		
		List<String> newRows = new ArrayList<>();
		String id = req.getParameter("id");
		boolean updated = false;
		for (var row : rows) {
			if (row.startsWith(id)) {
				newRows.add(id + "\t" + (Integer.parseInt(row.split("\\t+")[1]) + 1) );
				updated = true;
				continue;
			}
			newRows.add(row);
		}
		
		if (!updated)
			newRows.add(id + "\t" + 1);
		
		Files.write(Paths.get(fileName), newRows, StandardCharsets.UTF_8);
		
		
		// Kad je gotovo, pošalji redirect pregledniku I dalje NE generiraj odgovor
		resp.sendRedirect(req.getContextPath() + "/glasanje-rezultati");

	}
	
}
