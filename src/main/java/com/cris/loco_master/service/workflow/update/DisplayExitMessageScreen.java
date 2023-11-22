package com.cris.loco_master.service.workflow.update;

import com.onwbp.base.text.UserMessage;
import com.orchestranetworks.ui.UIButtonSpecNavigation;
import com.orchestranetworks.ui.UICSSClasses;
import com.orchestranetworks.ui.UIFormLabelSpec;
import com.orchestranetworks.ui.selection.TableViewEntitySelection;
import com.orchestranetworks.userservice.UserServiceDisplayConfigurator;
import com.orchestranetworks.userservice.UserServicePaneContext;
import com.orchestranetworks.userservice.UserServicePaneWriter;
import com.orchestranetworks.userservice.UserServiceSetupDisplayContext;

public class DisplayExitMessageScreen implements DisplayStep {

	String displayMessage;

	public DisplayExitMessageScreen(String displayMessage) {
		super();
		this.displayMessage = displayMessage;
	}

	@Override
	public void setupDisplay(UserServiceSetupDisplayContext<TableViewEntitySelection> context,
			UserServiceDisplayConfigurator config) {

		config.setContent(this::writeContent);
		{
			// Creates the close button on the final screen. It will close the current
			// screen and redirect you to initial screen.
			final UIButtonSpecNavigation closeButtonSpec = config.newCloseButton();
			closeButtonSpec.setDefaultButton(true);
			config.setRightButtons(closeButtonSpec);
		}

	}

	protected void writeContent(final UserServicePaneContext context, final UserServicePaneWriter writer) {
		writer.add("<div").addSafeAttribute("class", UICSSClasses.CONTAINER_WITH_TEXT).add(">");

		// if (this.result.hasFailed()) {
//			final UserMessage message = UserMessage
//					.createInfo("The request for transfer of locomotives has been failed.");
//			writer.addUILabel(new UIFormLabelSpec(message));
//		} else {
//			final UserMessage message = UserMessage
//					.createInfo("The request for transfer of locomotives has been submitted successfully.");
//			writer.addUILabel(new UIFormLabelSpec(message));
//		}
		final UserMessage message = UserMessage
				.createInfo("The request for Loco Update cannot be processed. " + displayMessage);
		writer.addUILabel(new UIFormLabelSpec(message));
		writer.add("</div>");
	}
}