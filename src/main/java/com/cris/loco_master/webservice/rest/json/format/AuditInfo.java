package com.cris.loco_master.webservice.rest.json.format;

public class AuditInfo {

	String Source_Record_Id;
	String Source_System_Name;
	String Source_Event_Type;
	
	public String getSource_Record_Id() {
		return Source_Record_Id;
	}
	public void setSource_Record_Id(String source_Record_Id) {
		Source_Record_Id = source_Record_Id;
	}
	public String getSource_System_Name() {
		return Source_System_Name;
	}
	public void setSource_System_Name(String source_System_Name) {
		Source_System_Name = source_System_Name;
	}
	public String getSource_Event_Type() {
		return Source_Event_Type;
	}
	public void setSource_Event_Type(String source_Event_Type) {
		Source_Event_Type = source_Event_Type;
	}

}
