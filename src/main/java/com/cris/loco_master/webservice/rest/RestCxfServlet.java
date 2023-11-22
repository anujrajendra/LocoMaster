package com.cris.loco_master.webservice.rest;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletConfig;

import com.cris.loco_master.webservice.rest.v1.LocomotiveResource;
import com.onwbp.org.apache.cxf.Bus;
import com.onwbp.org.apache.cxf.BusFactory;
import com.onwbp.org.apache.cxf.interceptor.Interceptor;
import com.onwbp.org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import com.onwbp.org.apache.cxf.message.Message;
import com.onwbp.org.apache.cxf.transport.servlet.CXFNonSpringServlet;

public class RestCxfServlet extends CXFNonSpringServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unchecked")
	public void loadBus(ServletConfig servletConfig) {
		super.loadBus(servletConfig);

		Bus bus = getBus();
		BusFactory.setDefaultBus(bus);

		JAXRSServerFactoryBean factory = new JAXRSServerFactoryBean();

		List<Interceptor<? extends Message>> inInterceptorsList = new ArrayList<>();
		inInterceptorsList.add((Interceptor<? extends Message>) new AuthenticationInterceptor());
		factory.setInInterceptors(inInterceptorsList);

		List<Object> providersList = new ArrayList<Object>();
//		providersList.add(new RestExceptionMapper());
		providersList.add(new GsonJaxrsProvider());
//		providersList.add(new CustomLoggingFilter());
		factory.setProviders(providersList);

		factory.setResourceClasses(
				new Class[] { ServiceTester.class, LocomotiveResource.class });

		factory.setAddress("/v1/");
		factory.create();
	}

}
