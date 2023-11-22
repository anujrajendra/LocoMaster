package com.cris.loco_master.webservice.rest.utils;

import javax.ws.rs.BadRequestException;

import com.cris.loco_master.Paths;
import com.cris.loco_master.webservice.rest.json.format.ErrorResponse;
import com.cris.loco_master.webservice.rest.json.format.Locomotive;
import com.onwbp.adaptation.Adaptation;
import com.onwbp.adaptation.AdaptationTable;
import com.onwbp.adaptation.RequestResult;
import com.onwbp.com.google.gson.Gson;
import com.onwbp.com.google.gson.GsonBuilder;
import com.onwbp.com.google.gson.JsonElement;
import com.onwbp.org.apache.cxf.message.Message;
import com.orchestranetworks.service.LoggingCategory;

public class WebServiceUtils {

	public static Locomotive validateLocomotiveRequest(JsonElement jsonElement, Message message, ErrorResponse errorResponse,
			String recordAction, Adaptation dataset) throws BadRequestException {

		LoggingCategory.getWorkflow().info("validateStationRequest Issue");

		String errorMsg = null;

		if (jsonElement.isJsonNull()) {
			LoggingCategory.getWorkflow().info("Inside Bad Request Exception NO Body in Request");
			throw new BadRequestException("TIBCO-MDM-400 NO_BODY_IN_REQUEST");
		}

		if (!jsonElement.isJsonObject() || jsonElement.isJsonArray()) {
			LoggingCategory.getWorkflow().info("Inside Bad Request Exception NO Body in Request 2");
			throw new BadRequestException("TIBCO-MDM-400 MALFORMED_JSON");
		}

		Gson gson = (new GsonBuilder()).setDateFormat("dd/MM/yyyy").create();
		Locomotive locomotive = (Locomotive) gson.fromJson(jsonElement, Locomotive.class);


		String locoNumber = locomotive.getLoco_Number();
		if(locoNumber != null) {
			try {
			Integer.parseInt(locoNumber);
		} catch (Exception e) {
			errorMsg = setErrorMsg(errorMsg, "Field Numeric Code: Must be integer value, characters not allowed");
		}
		}else
			{
			errorMsg = setErrorMsg(errorMsg, "Loco Number is mandatory");
			}
	
		if (errorMsg != null) {
			errorResponse.setErrorMessage(errorMsg);
			errorResponse.setErrorCode("400");
			return null;
		} else
			return locomotive;
	}

	public static boolean isLocoNumberPresent(String locoNumber, Adaptation dataset) {
		AdaptationTable tbLocomotive = dataset.getTable(Paths._Root_Locomotive.getPathInSchema());
		RequestResult requestResult = tbLocomotive.createRequestResult(Paths._Root_Locomotive._Root_Locomotive_Loco_Number.format() + "= '" + locoNumber +"'");
			
		if (requestResult.isSizeEqual(0))
			return false;

		return true;
	}

	static String setErrorMsg(String errorMsg, String errorTxt) {
		if (errorMsg == null)
			errorMsg = errorTxt;
		else
			errorMsg += ", " + errorTxt + "\r\n";

		return errorMsg;

	}
}