package com.cris.loco_master.webservice.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

public class ServiceTester {
	
	@GET
	@Path("health-check")
	public Response customerHealthCheckGet() throws Exception {
		return Response.status(Response.Status.OK).entity("Service is Running fine").build();
	}
}