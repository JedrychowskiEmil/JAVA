package pl.ej.papierpicker.config;

import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import pl.ej.papierpicker.filters.CharsetFilter;



public class MySpringMvcDispatcherServletInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

	@Override
	protected Class<?>[] getRootConfigClasses() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Class<?>[] getServletConfigClasses() {
		return new Class[] { AppConfig.class };
	}

	@Override
	protected String[] getServletMappings() {
		return new String[] { "/" };
	}

	
	//Obsługa Polskich znaków
	@Override
	public void onStartup(ServletContext servletContext)
	        throws ServletException {
		
	      FilterRegistration.Dynamic encodingFilter = servletContext.addFilter("CharsetFilter", new CharsetFilter());
	      encodingFilter.addMappingForUrlPatterns(null, false, "/*");
	      
	    super.onStartup(servletContext);
	}

}






