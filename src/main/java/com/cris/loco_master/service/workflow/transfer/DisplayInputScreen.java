package com.cris.loco_master.service.workflow.transfer;

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
		writer.setCurrentObject(TransferServicePaths._objectKey);
		{
			final UserMessage message = UserMessage.createInfo("Transfer selected records to Zone, Division, and Shed");
			writer.add("<div").addSafeAttribute("class", UICSSClasses.CONTAINER_WITH_TEXT).add(">");
			writer.addUILabel(new UIFormLabelSpec(message));
			writer.add("</div>");
		}
		// adds row to the screen.
		writer.startTableFormRow();

		UITextBox locoNumber = writer.newTextBox(TransferServicePaths._loco_number);
		locoNumber.setEditorDisabled(true);

		UITextBox locoType = writer.newTextBox(TransferServicePaths._loco_type);
		locoType.setEditorDisabled(true);

		UITextBox locoTraction = writer.newTextBox(TransferServicePaths._loco_traction);
		locoTraction.setEditorDisabled(true);

		UITextBox oldZone = writer.newTextBox(TransferServicePaths._old_zone);
		oldZone.setEditorDisabled(true);

		UITextBox oldDivision = writer.newTextBox(TransferServicePaths._old_division);
		oldDivision.setEditorDisabled(true);

		UITextBox oldShed = writer.newTextBox(TransferServicePaths._old_shed);
		oldShed.setEditorDisabled(true);

		writer.addFormRow(locoNumber);
		writer.addFormRow(locoType);
		writer.addFormRow(locoTraction);
		writer.addFormRow(oldZone);
		writer.addFormRow(oldDivision);
		writer.addFormRow(oldShed);

		writer.addFormRow(TransferServicePaths._zone);
		writer.addFormRow(TransferServicePaths._division);
		writer.addFormRow(TransferServicePaths._shed);
		writer.addFormRow(TransferServicePaths._transfer_date);
		writer.addFormRow(TransferServicePaths._reference_document_number);
		writer.addFormRow(TransferServicePaths._reference_document_date);

		UIWidgetFileUpload widget = writer.newCustomWidget(TransferServicePaths._reference_document_attachement,
				fileUploadFactory);
		writer.addFormRow(widget);
		writer.endTableFormRow();
	}

	protected UserServiceEventOutcome onNextPressed(final UserServiceEventContext context) {
		return EventOutcome.DISPLAY_CONFIRMATION; // Eventoutcome to navigate to the confirmation page.
	}
}