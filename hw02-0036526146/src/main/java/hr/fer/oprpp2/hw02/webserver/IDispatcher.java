package hr.fer.oprpp2.hw02.webserver;

/**
 * IDispatcher interface
 *
 */
public interface IDispatcher {

	void dispatchRequest(String urlPath) throws Exception;

}
