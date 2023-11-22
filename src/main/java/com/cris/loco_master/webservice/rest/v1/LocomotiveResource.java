package com.cris.loco_master.webservice.rest.v1;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

import com.cris.loco_master.procedure.CreateLocomotive;
import com.cris.loco_master.utils.DataspaceCreationUtils;
import com.cris.loco_master.utils.WorkflowProcessLauncherUtils;
import com.cris.loco_master.webservice.rest.json.format.ErrorResponse;
import com.cris.loco_master.webservice.rest.json.format.Locomotive;
import com.cris.loco_master.webservice.rest.json.format.SuccessResponse;
import com.cris.loco_master.webservice.rest.utils.WebServiceUtils;
import com.onwbp.adaptation.Adaptation;
import com.onwbp.adaptation.AdaptationHome;
import com.onwbp.adaptation.AdaptationName;
import com.onwbp.com.google.gson.Gson;
import com.onwbp.com.google.gson.JsonElement;
import com.onwbp.org.apache.cxf.message.Message;
import com.onwbp.org.apache.cxf.phase.PhaseInterceptorChain;
import com.orchestranetworks.instance.HomeKey;
import com.orchestranetworks.instance.Repository;
import com.orchestranetworks.service.LoggingCategory;
import com.orchestranetworks.service.OperationException;
import com.orchestranetworks.service.ProcedureResult;
import com.orchestranetworks.service.ProgrammaticService;
import com.orchestranetworks.service.Session;
import com.orchestranetworks.workflow.ProcessLauncher;

@Path("/")
public class LocomotiveResource {

	@SuppressWarnings("unused")
	private static Logger logger = Logger.getLogger(com.cris.loco_master.webservice.rest.v1.LocomotiveResource.class);

	@POST
	@Path("locomotive")
	@Consumes({ "application/json;charset=UTF-8" })
	@Produces({ "application/json;charset=UTF-8" })
	public Response locomotiveCreation(JsonElement jsonElement) throws Exception {
		LoggingCategory.getWorkflow().info("LocomotiveCreationResource");

		ErrorResponse errorResponse = new ErrorResponse();
		SuccessResponse successResponse = new SuccessResponse();

		Message message = PhaseInterceptorChain.getCurrentMessage();
		Session session = (Session) message.get("ebx.session");

		Repository repository = Repository.getDefault();
		AdaptationHome dataspace = repository.lookupHome(HomeKey.parse(HomeKey.forBranchName("loco_data").format()));

		DataspaceCreationUtils dataspaceCreation = new DataspaceCreationUtils(dataspace, repository, session);
		AdaptationHome childDataspace = dataspaceCreation.getDataspace();

		// AdaptationHome childDataspace = repository.createHome(childDataspaceSpec,
		// session);

		Adaptation dataset = childDataspace.findAdaptationOrNull(AdaptationName.forName("loco_data"));
		ProgrammaticService svc = ProgrammaticService.createForSession(session, childDataspace);

		Locomotive locomotive = WebServiceUtils.validateLocomotiveRequest(jsonElement, message, errorResponse, "add",
				dataset);

		if (locomotive == null) {
			String errorResponseString = (new Gson()).toJson(errorResponse);
			return Response.status(Response.Status.BAD_REQUEST).entity(errorResponseString).build();
		}
		locomotive.getLoco_Number();

		String recordAction;
		if (WebServiceUtils.isLocoNumberPresent(locomotive.getLoco_Number(), dataset)) {
			recordAction = "edit";
		} else {
			recordAction = "add";
		}
		CreateLocomotive createLocomotiveProcedure = new CreateLocomotive(recordAction, locomotive, successResponse,
				errorResponse, dataset);
		try {
			ProcedureResult pResult = svc.execute(createLocomotiveProcedure);
			if (pResult.hasFailed()) {
				String errorResponseString = (new Gson()).toJson(errorResponse);
				return Response.status(Response.Status.BAD_REQUEST).entity(errorResponseString).build();
			}
		} catch (Exception e) {
			String errorResponseString = (new Gson()).toJson(errorResponse);
			return Response.status(Response.Status.BAD_REQUEST).entity(errorResponseString).build();
		}

		WorkflowProcessLauncherUtils launcherUtils = new WorkflowProcessLauncherUtils(repository, session,
				"loco_create_api", "Locomotive Rest API", "Locomotive Rest API-Workflow");
		ProcessLauncher processLauncher = launcherUtils.getProcessLauncher();

		String recordPk = "/root/Locomotive[./Loco_Number=\"" + successResponse.getMdmId() + "\"]";

		processLauncher.setInputParameter("childDataSpace", dataspaceCreation.getDataspaceName());
		processLauncher.setInputParameter("record", recordPk);
		// processLauncher.setInputParameter("record", simplifiedXpath);

		try {
			processLauncher.launchProcess();
		} catch (OperationException e) {
			e.printStackTrace();
			LoggingCategory.getWorkflow().info(e.getMessage());
		}

		String successResponseString = (new Gson()).toJson(successResponse);

		return Response.status(Response.Status.OK).entity(successResponseString).build();
	}

	@PUT
	@Path("locomotive")
	@Consumes({ "application/json;charset=UTF-8" })
	@Produces({ "application/json;charset=UTF-8" })
	public Response locomotiveUpdation(JsonElement jsonElement) throws Exception {
		LoggingCategory.getWorkflow().info("LocomotiveCreationResource");

		ErrorResponse errorResponse = new ErrorResponse();
		SuccessResponse successResponse = new SuccessResponse();

		Message message = PhaseInterceptorChain.getCurrentMessage();
		Session session = (Session) message.get("ebx.session");

		Repository repository = Repository.getDefault();
		AdaptationHome dataspace = repository.lookupHome(HomeKey.parse(HomeKey.forBranchName("loco_data").format()));

		DataspaceCreationUtils dataspaceCreation = new DataspaceCreationUtils(dataspace, repository, session);
		AdaptationHome childDataspace = dataspaceCreation.getDataspace();

		Adaptation dataset = childDataspace.findAdaptationOrNull(AdaptationName.forName("loco_data"));
		ProgrammaticService svc = ProgrammaticService.createForSession(session, childDataspace);

		Locomotive locomotive = WebServiceUtils.validateLocomotiveRequest(jsonElement, message, errorResponse, "edit",
				dataset);

		if (locomotive == null) {
			String errorResponseString = (new Gson()).toJson(errorResponse);
			return Response.status(Response.Status.BAD_REQUEST).entity(errorResponseString).build();
		}

		String recordAction;
		if (WebServiceUtils.isLocoNumberPresent(locomotive.getLoco_Number(), dataset)) {
			recordAction = "edit";
		} else {
			recordAction = "add";
		}
		CreateLocomotive createLocomotiveProcedure = new CreateLocomotive(recordAction, locomotive, successResponse,
				errorResponse, dataset);
		try {
			ProcedureResult pResult = svc.execute(createLocomotiveProcedure);
			if (pResult.hasFailed()) {
				String errorResponseString = (new Gson()).toJson(errorResponse);
				return Response.status(Response.Status.BAD_REQUEST).entity(errorResponseString).build();
			}
		} catch (Exception e) {
			String errorResponseString = (new Gson()).toJson(errorResponse);
			return Response.status(Response.Status.BAD_REQUEST).entity(errorResponseString).build();
		}

		WorkflowProcessLauncherUtils launcherUtils = new WorkflowProcessLauncherUtils(repository, session,
				"loco_update_rest_test", "Locomotive Workflow", "Locomotive Workflow");
		ProcessLauncher processLauncher = launcherUtils.getProcessLauncher();

		String recordPk = "/root/Locomotive[./Loco_Number=\"" + successResponse.getMdmId() + "\"]";

		processLauncher.setInputParameter("childDataSpace", dataspaceCreation.getDataspaceName());
		processLauncher.setInputParameter("record", recordPk);
		// processLauncher.setInputParameter("record", simplifiedXpath);

		try {
			processLauncher.launchProcess();
		} catch (OperationException e) {
			e.printStackTrace();
			LoggingCategory.getWorkflow().info(e.getMessage());
		}

		String successResponseString = (new Gson()).toJson(successResponse);

		return Response.status(Response.Status.OK).entity(successResponseString).build();
	}
}