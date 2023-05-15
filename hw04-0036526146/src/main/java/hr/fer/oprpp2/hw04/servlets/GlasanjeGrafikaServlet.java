package hr.fer.oprpp2.hw04.servlets;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;

import hr.fer.oprpp2.hw04.dao.DAOProvider;
import hr.fer.oprpp2.hw04.model.Poll;
import hr.fer.oprpp2.hw04.model.PollOption;

/**
 * Servlet implementation for generating pie chart from voting data
 *
 */
@WebServlet("/servleti/glasanje-grafika")
public class GlasanjeGrafikaServlet extends HttpServlet{

	private static final long serialVersionUID = 1L;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		Poll poll = (Poll) req.getSession().getAttribute("poll");
		
		List<PollOption> pollOptions = DAOProvider.getDao().getPollOptionsByPollId(poll.getPollId());
		
		
		resp.setContentType("image/png");
		
		DefaultPieDataset dataset = new DefaultPieDataset();
		
		if (pollOptions != null) {
			for (var b : pollOptions) {
				dataset.setValue(b.getOptionTitle(), b.getVotesCount());
			}
		}
        
        JFreeChart chart = ChartFactory.createPieChart("Rezultati glasanja", dataset, true, false, false);
		
        OutputStream oStream = resp.getOutputStream();
        
        ChartUtils.writeChartAsPNG(oStream, chart, 400, 400);
		
		
	}
	
}
