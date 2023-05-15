package hr.fer.oprpp2.hw03.servlets;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;

/**
 * Servlet implementation for generating xls file
 *
 */
@WebServlet("/powers")
public class PowersServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		String aStr = req.getParameter("a");
		String bStr = req.getParameter("b");
		String nStr = req.getParameter("n");
		
		int a = 1, b = 100, n = 3;
		
		try {
			
			a = Integer.parseInt(aStr);
			b = Integer.parseInt(bStr);
			n = Integer.parseInt(nStr);
			
			if (a < -100 || a > 100)
				throw new Exception();
			if (b < -100 || b > 100)
				throw new Exception();
			if (n < 1 || n > 5)
				throw new Exception();
			
		} catch (NumberFormatException e) {
			req.setAttribute("error", "Can't parse given parameter!\n" + e.getMessage());
			req.getRequestDispatcher("error.jsp").forward(req, resp);
			return;
		} catch (NullPointerException e) {
			req.setAttribute("error", "Paramter can't be null!\n" + e.getMessage());
			req.getRequestDispatcher("error.jsp").forward(req, resp);
			return;
		} catch (Exception e) {
			req.setAttribute("error", "Given parameter in invalid range!\n a=[-100,100], b=[-100,100], n=[1,5]");
			req.getRequestDispatcher("error.jsp").forward(req, resp);
			return;
		}
		
		if (a > b) {
			int tmp = a;
			a = b;
			b = tmp;
		}
		
		resp.setContentType("application/xls");
		resp.setHeader("Content-Disposition", "attachment; filename=\"tablica.xls\"");
		
		HSSFWorkbook workbook = new HSSFWorkbook();
		
		for (int i = 1; i <= n; i++) {
			
			HSSFSheet sheet = workbook.createSheet(String.valueOf(i));
			HSSFRow row = sheet.createRow(0);
			Cell cell = row.createCell(0);
			cell.setCellValue("x");
			cell = row.createCell(1);
			cell.setCellValue("x^" + i);
			
			for (int j = a; j <= b; j++) {
				row = sheet.createRow(j-a+1);
				row.createCell(0).setCellValue(j);
				row.createCell(1).setCellValue(Math.pow(j, i));
			}
			
		}
		
		OutputStream oStream = resp.getOutputStream();
		workbook.write(oStream);
		workbook.close();
				
	}
		
}
