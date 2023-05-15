package hr.fer.oprpp2.hw02.webserver.workers;

import hr.fer.oprpp2.hw02.webserver.IWebWorker;
import hr.fer.oprpp2.hw02.webserver.RequestContext;

/**
 * IWebWorker implemetation
 */
public class SumWorker implements IWebWorker{

	@Override
	public void processRequest(RequestContext context) throws Exception {
		int a = 1;
		int b = 2;
		
		String aString = context.getParameter("a");
		if (aString != null) {
			try {
				a = Integer.parseInt(aString);
				context.setTemporaryParameter("varA", String.valueOf(a));
			} catch (Exception e) {
				
			}
		}
		
		String bString = context.getParameter("b");
		if (bString != null) {
			try {
				b = Integer.parseInt(bString);
				context.setTemporaryParameter("varB", String.valueOf(b));
			} catch (Exception e) {
				
			}
		}
		
		int sum = a+b;
		context.setTemporaryParameter("zbroj", String.valueOf(sum));
		if (sum % 2 == 0)
			context.setTemporaryParameter("imgName", "images/image1.jpg");
		else
			context.setTemporaryParameter("imgName", "images/image2.jpg");
		
		context.getDispatcher().dispatchRequest("/private/pages/calc.smscr");
		
		
		
	}

}
