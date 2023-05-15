package hr.fer.oprpp2.hw02.webserver;

/**
 * IWebWorker interface
 *
 */
public interface IWebWorker {

	public void processRequest(RequestContext context) throws Exception;
	
}
