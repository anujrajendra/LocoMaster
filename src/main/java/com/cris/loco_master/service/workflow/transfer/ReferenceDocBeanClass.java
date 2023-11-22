package com.cris.loco_master.service.workflow.transfer;

import java.util.Date;

import com.orchestranetworks.addon.dama.models.MediaType;

public class ReferenceDocBeanClass {
	String Reference_Document_Number;
	Date Reference_Document_Date;
	MediaType Reference_Document;

	public String getReference_Document_Number() {
		return Reference_Document_Number;
	}

	public void setReference_Document_Number(String reference_Document_Number) {
		Reference_Document_Number = reference_Document_Number;
	}

	public Date getReference_Document_Date() {
		return Reference_Document_Date;
	}

	public void setReference_Document_Date(Date reference_Document_Date) {
		Reference_Document_Date = reference_Document_Date;
	}

	public MediaType getReference_Document() {
		return Reference_Document;
	}

	public void setReference_Document(MediaType reference_Document) {
		Reference_Document = reference_Document;
	}

}
