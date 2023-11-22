package com.cris.loco_master.webservice.rest.json.format;

public class SuccessResponse {

	String successCode;
	String mdmId;

	public SuccessResponse(String successCode, String mdmId) {
		super();
		this.successCode = successCode;
		this.mdmId = mdmId;
	}

	public SuccessResponse() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getSuccessCode() {
		return successCode;
	}

	public void setSuccessCode(String successCode) {
		this.successCode = successCode;
	}

	public String getMdmId() {
		return mdmId;
	}

	public void setMdmId(String mdmId) {
		this.mdmId = mdmId;
	}
}