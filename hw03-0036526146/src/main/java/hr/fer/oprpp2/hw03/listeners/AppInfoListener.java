package hr.fer.oprpp2.hw03.listeners;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * Listener implementation for saving startup time
 *
 */
@WebListener
public class AppInfoListener implements ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		sce.getServletContext().setAttribute("runningSince", System.currentTimeMillis());
		
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		sce.getServletContext().removeAttribute("runningSince");
	}

}
