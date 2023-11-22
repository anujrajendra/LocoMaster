package com.cris.loco_master.form;

import com.orchestranetworks.service.Role;
import com.orchestranetworks.ui.UIFormLabelSpec;
import com.orchestranetworks.ui.form.UIForm;
import com.orchestranetworks.ui.form.UIFormBody;
import com.orchestranetworks.ui.form.UIFormContext;
import com.orchestranetworks.ui.form.UIFormHeader;
import com.orchestranetworks.ui.form.UIFormPaneWithTabs;

public class LocomotiveForm extends UIForm {

	@Override
	public void defineHeader(final UIFormHeader header, final UIFormContext context) {
		if (context.isCreatingRecord()) {
			header.setTitle(new UIFormLabelSpec("Locomotives"));
		}
	}

	@Override
	public void defineBody(final UIFormBody body, final UIFormContext context) {
		UIFormPaneWithTabs tabs = new UIFormPaneWithTabs();

		if (context.getSession().isUserInRole(Role.forSpecificRole("RAdministrator"))) {
			tabs.addHomeTab(new LocomotiveMainPane());
			tabs.addTab("Audit Info", new LocomotiveAuditPane());
			tabs.addTab("Reference Documents", new LocomotiveReferenceDocsPane());
		} else {
			tabs.addHomeTab(new LocomotiveUserBasedDivisionsPane());
			tabs.addTab("Audit Info", new LocomotiveAuditPane());
			tabs.addTab("Reference Documents", new LocomotiveReferenceDocsPane());
		}

//		if(!context.isCreatingRecord())
//		{
//			tabs.addTab("Sources", new LocomotiveSourcesPaneTest());
//			tabs.addTab("DQ1", new LocomotiveSourceDQ1Pane());
//			tabs.addTab("DQ2", new LocomotiveSourceDQ2Pane());
//		}
//		tabs.addHomeTab(new LocomotivesMainPaneTest());
//		if(!context.isCreatingRecord())
//			tabs.addTab("Sources", new LocomotiveSourcesPaneTest());
//		//tabs.addTab("Main", new LocomotiveMainPane());
		// tabs.addTab(UserMessage.createInfo("Locomotives"), new LocomotiveMainPane());
		body.setContent(tabs);

	}

}
