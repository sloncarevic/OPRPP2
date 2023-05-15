package hr.fer.oprpp2.hw02.webserver.workers;

import hr.fer.oprpp2.hw02.webserver.IWebWorker;
import hr.fer.oprpp2.hw02.webserver.RequestContext;

public class Home implements IWebWorker {

	@Override
	public void processRequest(RequestContext context) throws Exception {
		String bgcolor = context.getPersistentParameter("bgcolor");
		if (bgcolor != null)
			context.setTemporaryParameter("background","#"+bgcolor);
		else
			context.setTemporaryParameter("background", "#7F7F7F");
		
		context.getDispatcher().dispatchRequest("/private/pages/home.smscr");
	}

}
