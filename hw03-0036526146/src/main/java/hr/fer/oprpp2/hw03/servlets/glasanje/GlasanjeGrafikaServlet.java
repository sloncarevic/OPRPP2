package hr.fer.oprpp2.hw03.servlets.glasanje;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;

import hr.fer.oprpp2.hw03.servlets.glasanje.model.Band;
import hr.fer.oprpp2.hw03.servlets.glasanje.model.Bands;

/**
 * Servlet implementation for generating pie chart from voting data
 *
 */
@WebServlet("/glasanje-grafika")
public class GlasanjeGrafikaServlet extends HttpServlet{

	private static final long serialVersionUID = 1L;

	@SuppressWarnings({ "rawtypes", "unchecked" })
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

		
		resp.setContentType("image/png");
		
		DefaultPieDataset dataset = new DefaultPieDataset();
		
		if (bandsList != null) {
			for (var b : bandsList) {
				dataset.setValue(b.getName(), b.getVotes());
			}
		}
        
        JFreeChart chart = ChartFactory.createPieChart("Rezultati glasanja", dataset, true, false, false);
		
        OutputStream oStream = resp.getOutputStream();
        
        ChartUtils.writeChartAsPNG(oStream, chart, 400, 400);
		
		
	}
	
}
