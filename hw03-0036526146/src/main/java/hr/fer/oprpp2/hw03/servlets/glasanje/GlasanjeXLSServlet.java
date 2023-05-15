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

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import hr.fer.oprpp2.hw03.servlets.glasanje.model.Band;
import hr.fer.oprpp2.hw03.servlets.glasanje.model.Bands;

/**
 * Servlet implementation for generating xls file from voting data
 *
 */
@WebServlet("/glasanje-xls")
public class GlasanjeXLSServlet extends HttpServlet {

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
		
		
		resp.setContentType("application/xls");
		resp.setHeader("Content-Disposition", "attachment; filename=\"Rezultati-glasanja.xls\"");

		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet("Rezultati glasanja");
		HSSFRow row = sheet.createRow(0);
		row.createCell(0).setCellValue("Band");
		row.createCell(1).setCellValue("Votes");
		
		for (int i = 0; i < bandsList.size(); i++) {
			row = sheet.createRow(i+1);
			row.createCell(0).setCellValue(bandsList.get(i).getName());
			row.createCell(1).setCellValue(bandsList.get(i).getVotes());
		}

		OutputStream oStream = resp.getOutputStream();
		workbook.write(oStream);
		workbook.close();

	}

}
