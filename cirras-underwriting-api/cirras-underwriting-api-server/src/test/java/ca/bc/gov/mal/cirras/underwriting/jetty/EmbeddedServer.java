package ca.bc.gov.mal.cirras.underwriting.jetty;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.sql.DataSource;

import org.eclipse.jetty.ee10.annotations.AnnotationConfiguration;
import org.eclipse.jetty.annotations.ClassInheritanceHandler;
import org.eclipse.jetty.plus.jndi.Resource;
import org.eclipse.jetty.security.SecurityHandler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.ee10.webapp.Configuration;
import org.eclipse.jetty.ee10.webapp.JndiConfiguration;
import org.eclipse.jetty.ee10.webapp.WebAppContext;
import org.glassfish.jersey.server.spring.SpringWebApplicationInitializer;
import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.WebApplicationInitializer;

import ca.bc.gov.mal.cirras.underwriting.spring.ApplicationInitializer;

public class EmbeddedServer {
	
	private static final Logger logger = LoggerFactory.getLogger(EmbeddedServer.class);

	private static Server server;

	public static void startIfRequired(int port, String contextPath, Map<String, DataSource> dataSources) throws Exception {
		logger.debug("<startIfRequired "+port+"/"+contextPath);

		logger.debug("server="+server);
		if (server == null) {

			System.setProperty("java.naming.factory.url.pkgs", "org.eclipse.jetty.jndi");
			System.setProperty("java.naming.factory.initial", "org.eclipse.jetty.jndi.InitialContextFactory");

			server = new Server(port);

	        WebAppContext context = new WebAppContext();
	        AnnotationConfiguration annotationConfiguration = new AnnotationConfiguration() {
				@Override
				public void preConfigure(WebAppContext ctx) {
					super.preConfigure(ctx);
					ClassInheritanceMap map = new ClassInheritanceMap();
					Set<String> set = new HashSet<>();
					set.add(ApplicationInitializer.class.getName());
					set.add(SpringWebApplicationInitializer.class.getName());
					map.put(WebApplicationInitializer.class.getName(), set);
					ctx.setAttribute(CLASS_INHERITANCE_MAP, map);

					//_classInheritanceHandler = new ClassInheritanceHandler(map);
			        // After upgrading Jetty from version 9.4.28.v20200408 to 12.1, _classInheritanceHandler appears to have been moved.
					// context.getAttribute(STATE) is populated by super.preConfigure().
					// Not clear what this does or if it is needed; unit tests seem to run fine without it.
					State state = (State)ctx.getAttribute(STATE);
					state._classInheritanceHandler = new ClassInheritanceHandler(map);
				}
			};
			
			// Jetty disables JNDI by default in newer versions, and throws java.lang.ClassNotFoundException: org.eclipse.jetty.jndi.InitialContextFactory 
			// when attempting a lookup. The class is there, but WebAppClassLoader deliberately hides it if JNDI is disabled.
			JndiConfiguration jndiConfiguration = new JndiConfiguration();

			context.setConfigurations(new Configuration[] {annotationConfiguration, jndiConfiguration});
	        
	        context.setContextPath(contextPath);

	        // After upgrading Jetty from version 9.4.28.v20200408, this function was removed without any direct replacement.
	        //context.setResourceBase("src/main/webapp");

	        // This doesn't appear to be needed, but if it is needed in the future, this code is almost equivalent, except that 
	        // it requires the directory to exist, whereas the previous function seemed to auto-create it.
			//context.setBaseResource(context.newResource("src/main/webapp"));
	        
	        context.setParentLoaderPriority(true);
	        
			SecurityHandler securityHandler = context.getSecurityHandler();
			Assert.assertNotNull(securityHandler);
			securityHandler.setLoginService(new TestLoginService());

			if(dataSources!=null) {
				for(String dataSourceName:dataSources.keySet()) {
					DataSource dataSource = dataSources.get(dataSourceName);
					Resource resource = new Resource("java:comp/env/" + dataSourceName, dataSource);
					server.setAttribute(dataSourceName, resource);
				}
			}

			server.setHandler(context);
			server.start();
		}

		logger.debug(">startIfRequired");
	}

	public static void stop() throws Exception {
		if (server != null) {
			server.stop();
			server.join();
			server.destroy();
			server = null;
		}
	}
}