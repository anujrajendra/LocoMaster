package com.cris.loco_master.form;

import com.cris.loco_master.Paths;
import com.orchestranetworks.ui.form.UIFormContext;
import com.orchestranetworks.ui.form.UIFormPane;
import com.orchestranetworks.ui.form.UIFormPaneWriter;

public class LocomotiveAuditPane implements UIFormPane {

	@Override
	public void writePane(UIFormPaneWriter pWriter, UIFormContext aContext) {
		// TODO Auto-generated method stub

		String floatDivStyle = "float:left; width:48%; ";

		pWriter.add("<div style='" + floatDivStyle + " min-width:200px;'>");
		pWriter.add(
				"<table><tr><td style='margin-top: 3px; width: 30px; min-width: 30px; max-width: 30px; vertical-align:top'>");

		pWriter.startTableFormRow();

		pWriter.addFormRow(Paths._Root_Locomotive._Root_Locomotive_Audit_Info_Source_Record_Id);
		pWriter.addFormRow(Paths._Root_Locomotive._Root_Locomotive_Audit_Info_Source_System_Name);
		pWriter.addFormRow(Paths._Root_Locomotive._Root_Locomotive_Audit_Info_Source_Event_Type);
		pWriter.addFormRow(Paths._Root_Locomotive._Root_Locomotive_Audit_Info_Created_By_User);

		String userId = aContext.getSession().getUserReference().getUserId();
		pWriter.addJS("var userId = '" + userId + "';\n");

		pWriter.addJS_setNodeValue("userId", Paths._Root_Locomotive._Root_Locomotive_Audit_Info_Logged_In_User);

		pWriter.addFormRow(Paths._Root_Locomotive._Root_Locomotive_Audit_Info_Logged_In_User);

		pWriter.endTableFormRow();
		pWriter.add("</td></tr></table>");
		// pWriter.endBorder();
		pWriter.add("</div>");
	}
}