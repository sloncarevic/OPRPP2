package hr.fer.oprpp2.hw03.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation for changing web page background color
 *
 */
@WebServlet("/setcolor")
public class SetColorServlet extends HttpServlet{

	private static final long serialVersionUID = 1L;
	
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		String color = req.getParameter("color");
		String colorNum = "";
		
		if (color != null) {
			switch (color.toLowerCase()) {
			case "white":
				colorNum = "FFFFFF";
				break;
			case "red":
				colorNum = "FF0000";
				break;
			case "green":
				colorNum = "00FF00";
				break;
			case "cyan":
				colorNum = "0000FF";
				break;
			default:
				colorNum = "FFFFFF";
				break;
			}
		}
		
		req.getSession().setAttribute("pickedBgCol", colorNum);
		
		req.getRequestDispatcher("colors.jsp").forward(req, resp);
	}

}
