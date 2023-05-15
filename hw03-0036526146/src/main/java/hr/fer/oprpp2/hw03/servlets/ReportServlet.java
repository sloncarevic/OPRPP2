package hr.fer.oprpp2.hw03.servlets;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;

/**
 * Servlet implementation for generating pie chart
 *
 */
@WebServlet("/reportImage")
public class ReportServlet extends HttpServlet{

	private static final long serialVersionUID = 1L;
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("image/png");
		
		DefaultPieDataset dataset = new DefaultPieDataset();
		dataset.setValue("Linux", 29);
        dataset.setValue("Mac", 20);
        dataset.setValue("Windows", 51);
        
        JFreeChart chart = ChartFactory.createPieChart("OS usage", dataset, true, false, false);
		
        OutputStream oStream = resp.getOutputStream();
        
        ChartUtils.writeChartAsPNG(oStream, chart, 500, 500);
        
	}

}
