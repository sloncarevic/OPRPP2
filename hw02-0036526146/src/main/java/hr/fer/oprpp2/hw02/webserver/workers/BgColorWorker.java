package hr.fer.oprpp2.hw02.webserver.workers;

import hr.fer.oprpp2.hw02.webserver.IWebWorker;
import hr.fer.oprpp2.hw02.webserver.RequestContext;

public class BgColorWorker implements IWebWorker{

	@Override
	public void processRequest(RequestContext context) throws Exception {
		String bgcolor = context.getParameter("bgcolor");
		boolean valid = true;
		if (bgcolor.length() == 6) {
			for (var c : bgcolor.toCharArray())
				if ((c >= 'A' && c <= 'F') || Character.isDigit(c)) {
					
				} else {
					valid = false;
				}
		} 
		
		String message = "";
		if (valid) {
			context.setPersistentParameter("bgcolor", bgcolor);
			message = "Color has been updated";
		} else {
			message = "Color has not been updated";
		}
		
		String html = """
					<!DOCTYPE html>\r\n
					<html>\r\n
					<head>\r\n
					</head>\r\n
					<body>\r\n
					<p> msg <p>\r\n
					<p>Return to home page link:</p>\r\n
					<a href=\"index2.html\"">Home page</a>\r\n
					</body>\r\n
					</html>
				""".replace("msg", message);
		context.write(html);
	}

}
