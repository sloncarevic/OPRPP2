package hr.fer.oprpp2.hw04.listeners;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.Properties;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.mchange.v2.c3p0.DataSources;

import hr.fer.oprpp2.hw04.dao.DAOException;
import hr.fer.oprpp2.hw04.dao.DAOProvider;

/**
 * Class implementing ServletContextListener
 * Creates connection pool on startup and destroys it on shutdown
 *
 */
@WebListener
public class Inicijalizacija implements ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		
		ServletContext sContext = sce.getServletContext();
		Path propertiesPath = Paths.get(sContext.getRealPath("/WEB-INF/dbsettings.properties"));
		
		if (! Files.exists(propertiesPath))
			throw new IllegalArgumentException("File dbsettings.properties does not exits!");
		
		Properties properties = new Properties();
		try {
			properties.load(Files.newInputStream(propertiesPath));
		} catch (IOException e) {
			throw new RuntimeException();
		}
		
		if ( ! (properties.containsKey("host") &&
				properties.containsKey("port") &&
				properties.containsKey("name") &&
				properties.containsKey("user") &&
				properties.containsKey("password"))) {
			throw new RuntimeException("File dbsettings.properties does not contain neccessary data!");
		}
		
		
		String dbName = properties.getProperty("name");
		String connectionURL = "jdbc:derby://" + properties.getProperty("host")
								+ ":" +  properties.getProperty("port") 
								+ "/" + dbName + ";"
								+ "user=" + properties.getProperty("user") +";"
								+ "password=" + properties.getProperty("password");

		ComboPooledDataSource cpds = new ComboPooledDataSource();
		try {
			cpds.setDriverClass("org.apache.derby.jdbc.ClientDriver");
		} catch (PropertyVetoException e1) {
			throw new RuntimeException("Pogreška prilikom inicijalizacije poola.", e1);
		}
		cpds.setJdbcUrl(connectionURL);

		sce.getServletContext().setAttribute("hr.fer.zemris.dbpool", cpds);
		
		
		try {
			DAOProvider.getDao().checkDB(sContext, cpds);
		} catch (DAOException e) {
			System.err.println(e.getLocalizedMessage());
			contextDestroyed(sce);
			System.exit(1);
		}
		
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		ComboPooledDataSource cpds = (ComboPooledDataSource)sce.getServletContext().getAttribute("hr.fer.zemris.dbpool");
		if(cpds!=null) {
			try {
				DataSources.destroy(cpds);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

}