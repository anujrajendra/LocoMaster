package com.cris.loco_master.service.workflow.condemn;

import com.onwbp.base.text.UserMessage;
import com.orchestranetworks.ui.UIButtonSpec;
import com.orchestranetworks.ui.UIButtonSpecNavigation;
import com.orchestranetworks.ui.UICSSClasses;
import com.orchestranetworks.ui.UIFormLabelSpec;
import com.orchestranetworks.ui.form.widget.UITextBox;
import com.orchestranetworks.ui.form.widget.UIWidgetFileUpload;
import com.orchestranetworks.ui.form.widget.UIWidgetFileUploadFactory;
import com.orchestranetworks.ui.selection.TableViewEntitySelection;
import com.orchestranetworks.userservice.UserServiceDisplayConfigurator;
import com.orchestranetworks.userservice.UserServiceEventContext;
import com.orchestranetworks.userservice.UserServiceEventOutcome;
import com.orchestranetworks.userservice.UserServicePaneContext;
import com.orchestranetworks.userservice.UserServicePaneWriter;
import com.orchestranetworks.userservice.UserServiceSetupDisplayContext;

public class DisplayInputScreen implements DisplayStep {

	@Override
	public void setupDisplay(UserServiceSetupDisplayContext<TableViewEntitySelection> context,
			UserServiceDisplayConfigurator config) {

		config.setContent(this::writeContent);
		{
			// Creates a cancel button.
			final UIButtonSpec cancelButtonSpec = config.newCancelButton();
			config.setLeftButtons(cancelButtonSpec);
		}
		{
			// Creates a specification for a next button.
			// it takes an argument a call-back function which will be invoked when this
			// button is pressed.
			final UIButtonSpecNavigation nextButtonSpec = config.newNextButton(this::onNextPressed);
			nextButtonSpec.setLabel(UserMessage.createInfo("Submit"));
			nextButtonSpec.setDefaultButton(true);
			config.setRightButtons(nextButtonSpec);
			config.setFileUploadEnabled(true);
		}
	}

	protected void writeContent(final UserServicePaneContext context, final UserServicePaneWriter writer) {

		final UIWidgetFileUploadFactory fileUploadFactory = new UIWidgetFileUploadFactory();
		writer.setCurrentObject(CondemnServicePaths._objectKey);
		{
			final UserMessage message = UserMessage.createInfo("Locomotive Condemnation");
			writer.add("<div").addSafeAttribute("class", UICSSClasses.CONTAINER_WITH_TEXT).add(">");
			writer.addUILabel(new UIFormLabelSpec(message));
			writer.add("</div>");
		}
		// adds row to the screen.
		writer.startTableFormRow();

		UITextBox locoNumber = writer.newTextBox(CondemnServicePaths._loco_number);
		locoNumber.setEditorDisabled(true);

		UITextBox locoTractionType = writer.newTextBox(CondemnServicePaths._loco_traction);
		locoTractionType.setEditorDisabled(true);

		UITextBox locoCommissioningDate = writer.newTextBox(CondemnServicePaths._commissioning_date);
		locoCommissioningDate.setEditorDisabled(false);

		UITextBox locoType = writer.newTextBox(CondemnServicePaths._loco_type);
		locoType.setEditorDisabled(true);

		UITextBox locoZone = writer.newTextBox(CondemnServicePaths._zone);
		locoZone.setEditorDisabled(true);

		UITextBox locoDivision = writer.newTextBox(CondemnServicePaths._division);
		locoDivision.setEditorDisabled(true);

		UITextBox locoShed = writer.newTextBox(CondemnServicePaths._shed);
		locoShed.setEditorDisabled(true);

		writer.addFormRow(locoNumber);
		writer.addFormRow(locoTractionType);
		// writer.addFormRow(locoCommissioningDate);
		writer.addFormRow(locoType);
		writer.addFormRow(locoZone);
		writer.addFormRow(locoDivision);
		writer.addFormRow(locoShed);

		writer.addFormRow(CondemnServicePaths._condemnation_proposal_type);
		writer.addFormRow(CondemnServicePaths._condemnation_reason);
		writer.addFormRow(CondemnServicePaths._condemnation_date);

		writer.addFormRow(CondemnServicePaths._commissioning_date);

		writer.addFormRow(CondemnServicePaths._reference_document_number);
		writer.addFormRow(CondemnServicePaths._reference_document_date);
		UIWidgetFileUpload widget = writer.newCustomWidget(CondemnServicePaths._reference_document_attachement,
				fileUploadFactory);

//		widget.setAccept(".pdf");
//		widget.setAccept(".jpeg");
//		widget.setAccept(".png");

		writer.addFormRow(widget);

		writer.endTableFormRow();
	}

	protected UserServiceEventOutcome onNextPressed(final UserServiceEventContext context) {
		return EventOutcome.DISPLAY_CONFIRMATION; // Eventoutcome to navigate to the confirmation page.
	}
}