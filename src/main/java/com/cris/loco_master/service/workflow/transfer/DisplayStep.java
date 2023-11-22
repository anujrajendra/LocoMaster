package com.cris.loco_master.service.workflow.transfer;

import com.orchestranetworks.ui.selection.TableViewEntitySelection;
import com.orchestranetworks.userservice.UserServiceDisplayConfigurator;
import com.orchestranetworks.userservice.UserServiceEventOutcome;
import com.orchestranetworks.userservice.UserServiceSetupDisplayContext;

public interface DisplayStep {
	enum EventOutcome implements UserServiceEventOutcome {
		DISPLAY_RESULT, DISPLAY_INPUT_SCREEN, DISPLAY_CONFIRMATION, DISPLAY_EXIT_MESSAGE
	}

	void setupDisplay(final UserServiceSetupDisplayContext<TableViewEntitySelection> context,
			final UserServiceDisplayConfigurator config);
}