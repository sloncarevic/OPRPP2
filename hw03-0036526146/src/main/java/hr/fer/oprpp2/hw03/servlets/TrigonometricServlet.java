package hr.fer.oprpp2.hw03.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation for calculating triconometric functions for all int angles
 *
 */
@WebServlet("/trigonometric")
public class TrigonometricServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String aStr = req.getParameter("a");
		String bStr = req.getParameter("b");
		
		int a = 0;
		int b = 360;
		
		try {
			a = Integer.parseInt(aStr);
			b = Integer.parseInt(bStr);
		} catch (NumberFormatException e) {
			
		} catch (NullPointerException e) {
			
		}
		
		if (a > b) {
			int tmp = a;
			a = b;
			b = tmp;
		}
		
		if ((a + 720) < b)
			b = a + 720;
		
		List<List<String>> values = new ArrayList<List<String>>();
		
		for (int i = a; i <= b; i++) {
			double sinus = Math.sin(Math.toRadians(i));
			double cosinus = Math.cos(Math.toRadians(i));
			values.add(Arrays.asList(String.valueOf(i), String.valueOf(sinus), String.valueOf(cosinus)));
		}
		
		req.setAttribute("values", values);
		
		req.getRequestDispatcher("WEB-INF/pages/trigonometric.jsp").forward(req, resp);
		
	}

}
