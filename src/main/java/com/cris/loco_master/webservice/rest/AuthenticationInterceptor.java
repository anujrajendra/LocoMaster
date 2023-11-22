package com.cris.loco_master.webservice.rest;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.onwbp.org.apache.cxf.interceptor.Fault;
import com.onwbp.org.apache.cxf.interceptor.security.AuthenticationException;
import com.onwbp.org.apache.cxf.message.Message;
import com.onwbp.org.apache.cxf.phase.AbstractPhaseInterceptor;
import com.orchestranetworks.instance.Repository;
import com.orchestranetworks.service.Session;

@SuppressWarnings("rawtypes")
public class AuthenticationInterceptor extends AbstractPhaseInterceptor {

	private static final Logger LOGGER = Logger
			.getLogger(com.cris.loco_master.webservice.rest.AuthenticationInterceptor.class);

	public AuthenticationInterceptor() {
		super("pre-invoke");
	}

	public AuthenticationInterceptor(String phase) {
		super("pre-invoke");
	}

	public void handleMessage(Message message) throws Fault {
		Repository repository = Repository.getDefault();
		Session session = null;

		HttpServletRequest request = (HttpServletRequest) message.get("HTTP.REQUEST");

		try {
			session = repository.createSessionFromHttpRequest(request);
		} catch (AuthenticationException e) {
			LOGGER.warn("Failed to authenticate from HTTP Request : " + e.getMessage());
		}

		if (session == null) {
			try {
				session = AuthenticationHelper.createSessionFromBasicAuthentication(repository, request);
			} catch (AuthenticationException e) {
				LOGGER.warn("Failed to authenticate from basic authentication : " + e.getMessage());
			}
		}

		if (session == null) {
			ErrorResponse.sendAuthError(message);
		} else {
			message.put("ebx.session", session);
		}
	}

}
