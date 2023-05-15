package hr.fer.oprpp2.hw04.servlets;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import hr.fer.oprpp2.hw04.dao.DAOProvider;
import hr.fer.oprpp2.hw04.model.Poll;
import hr.fer.oprpp2.hw04.model.PollOption;

/**
 * Servlet implementation for generating xls file from voting data
 *
 */
@WebServlet("/servleti/glasanje-xls")
public class GlasanjeXLSServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		Poll poll = (Poll) req.getSession().getAttribute("poll");
		
		List<PollOption> pollOptions = DAOProvider.getDao().getPollOptionsByPollId(poll.getPollId());
		
		
		resp.setContentType("application/xls");
		resp.setHeader("Content-Disposition", "attachment; filename=\"Rezultati-glasanja.xls\"");

		
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet("Rezultati glasanja");
		HSSFRow row = sheet.createRow(0);
		row.createCell(0).setCellValue("Contestant");
		row.createCell(1).setCellValue("Votes");
		
		for (int i = 0; i < pollOptions.size(); i++) {
			row = sheet.createRow(i+1);
			row.createCell(0).setCellValue(pollOptions.get(i).getOptionTitle());
			row.createCell(1).setCellValue(pollOptions.get(i).getVotesCount());
		}

		OutputStream oStream = resp.getOutputStream();
		workbook.write(oStream);
		workbook.close();

	}

}
