package com.cris.loco_master.form;

import com.cris.loco_master.Paths;
import com.orchestranetworks.ui.form.UIFormContext;
import com.orchestranetworks.ui.form.UIFormPane;
import com.orchestranetworks.ui.form.UIFormPaneWriter;

public class LocomotiveReferenceDocsPane implements UIFormPane {

	@Override
	public void writePane(UIFormPaneWriter pWriter, UIFormContext aContext) {
		// TODO Auto-generated method stub

		String floatDivStyle = "float:left; width:48%; ";

		pWriter.add("<div style='" + floatDivStyle + " min-width:200px;'>");
		pWriter.add(
				"<table><tr><td style='margin-top: 3px; width: 30px; min-width: 30px; max-width: 30px; vertical-align:top'>");

		pWriter.startTableFormRow();

//		pWriter.addFormRow(Paths._Root_Locomotive._Root_Locomotive_Reference_Documents_Reference_Document_Number);
//		pWriter.addFormRow(Paths._Root_Locomotive._Root_Locomotive_Reference_Documents_Reference_Document_Date);
//		pWriter.addFormRow(Paths._Root_Locomotive._Root_Locomotive_Reference_Documents_Reference_Document);

		pWriter.addFormRow(Paths._Root_Locomotive._Root_Locomotive_Reference_Documents);

		pWriter.endTableFormRow();
		pWriter.add("</td></tr></table>");
		// pWriter.endBorder();
		pWriter.add("</div>");

	}
}
