package com.cris.loco_master.service.displaydq;

import com.orchestranetworks.ui.selection.TableViewEntitySelection;
import com.orchestranetworks.userservice.UserServiceDisplayConfigurator;
import com.orchestranetworks.userservice.UserServiceEventOutcome;
import com.orchestranetworks.userservice.UserServiceSetupDisplayContext;

public interface DisplayStep {

	enum EventOutcome implements UserServiceEventOutcome{
		//DISPLAY_FILTER, 
		//DISPLAY_CONFIRMATION,
		DISPLAY_RESULT
	}
	
	void setupDisplay(final UserServiceSetupDisplayContext<TableViewEntitySelection> context,
			final UserServiceDisplayConfigurator config);
}