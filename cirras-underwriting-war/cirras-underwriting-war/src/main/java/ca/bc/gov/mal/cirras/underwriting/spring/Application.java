package ca.bc.gov.mal.cirras.underwriting.spring;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.servlet.DispatcherServlet;
import org.tuckey.web.filters.urlrewrite.UrlRewriteFilter;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletRegistration;
import java.util.EnumSet;

public class Application implements WebApplicationInitializer {

	
	private void registerEndpoint(ServletContext container, AnnotationConfigWebApplicationContext rootContext, String name, String mapping) {
        ServletRegistration.Dynamic dispatcher =
                container.addServlet(name, new DispatcherServlet(rootContext));
        dispatcher.setLoadOnStartup(1);
        dispatcher.addMapping(mapping);
	}
	
    @Override
    public void onStartup(ServletContext container) {

        // Set up annotation config context
        AnnotationConfigWebApplicationContext rootContext =
                new AnnotationConfigWebApplicationContext();
        rootContext.register(AppConfig.class);

        // Add delegating filter proxy and use springSecurityFilterChain bean, this is part of spring
        // when using @EnableWebSecurity as part of the AppConfig
        container.addListener(new ContextLoaderListener(rootContext));
        container.addFilter("springSecurityFilterChain", new DelegatingFilterProxy("springSecurityFilterChain"))
                .addMappingForUrlPatterns(null, true, "/*");

        // Register check token servlet, spring security context will be available from above configuration
        registerEndpoint(container, rootContext, "Check Token Servlet", "/checkToken.jsp");
        
        // Set up url rewrite filter - will automatically use WEB-INF/urlrewrite.xml
        // Used to allow direct url links to angular routes - otherwise you will get 404 as they don't exist as actual server resources
        FilterRegistration.Dynamic urlReWrite = container.addFilter("UrlRewriteFilter", new UrlRewriteFilter());
        EnumSet<DispatcherType> urlReWriteDispatcherTypes = EnumSet.of(DispatcherType.REQUEST, DispatcherType.FORWARD);
        urlReWrite.addMappingForUrlPatterns(urlReWriteDispatcherTypes, true, "/*");
    }
}
