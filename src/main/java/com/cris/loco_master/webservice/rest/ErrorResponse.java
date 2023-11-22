package com.cris.loco_master.webservice.rest;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.onwbp.org.apache.cxf.message.Message;

public class ErrorResponse {

	public static void sendAuthError(Message message) {
		Response.Status status = Response.Status.UNAUTHORIZED;

		Response response = Response.status(status).type(MediaType.APPLICATION_JSON_TYPE)
				.entity("{\"code\": \"TIBCO-MDM-401\",\"errors\": [{\"message\": \"Authentication failed.\"}]}")
				.build();

		message.getExchange().put(Response.class, response);
	}

	public static Response getAuthErrorResponse() {
		Response.Status status = Response.Status.UNAUTHORIZED;

		Response response = Response.status(status).type(MediaType.APPLICATION_JSON_TYPE)
				.entity("{\"code\": \"TIBCO-MDM-401\",\"errors\": [{\"message\": \"Authentication failed.\"}]}")
				.build();

		return response;
	}
}