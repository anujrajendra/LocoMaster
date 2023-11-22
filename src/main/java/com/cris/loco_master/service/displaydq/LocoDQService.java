package com.cris.loco_master.service.displaydq;

import com.orchestranetworks.ui.selection.TableViewEntitySelection;
import com.orchestranetworks.userservice.UserService;
import com.orchestranetworks.userservice.UserServiceDisplayConfigurator;
import com.orchestranetworks.userservice.UserServiceEventOutcome;
import com.orchestranetworks.userservice.UserServiceObjectContextBuilder;
import com.orchestranetworks.userservice.UserServiceProcessEventOutcomeContext;
import com.orchestranetworks.userservice.UserServiceSetupDisplayContext;
import com.orchestranetworks.userservice.UserServiceSetupObjectContext;
import com.orchestranetworks.userservice.UserServiceValidateContext;

public class LocoDQService implements UserService<TableViewEntitySelection>{

	private DisplayStep currentStep;
	
	
	public LocoDQService(DisplayStep currentStep) {
		super();
		this.currentStep = new DisplayResultStep();
	}

	@Override
	public UserServiceEventOutcome processEventOutcome(
			UserServiceProcessEventOutcomeContext<TableViewEntitySelection> context, UserServiceEventOutcome eventOutcome) {
		if(!(eventOutcome instanceof DisplayStep.EventOutcome))
			return eventOutcome;
		
		switch((DisplayStep.EventOutcome)eventOutcome) {
		
		case DISPLAY_RESULT:
			this.currentStep= new DisplayResultStep();
			return null;
		default:
			return null;
		}
	}

	@Override
	public void setupDisplay(UserServiceSetupDisplayContext<TableViewEntitySelection> context,
			UserServiceDisplayConfigurator config) {
		// TODO Auto-generated method stub
		this.currentStep.setupDisplay(context, config);
	}

	@Override
	public void setupObjectContext(UserServiceSetupObjectContext<TableViewEntitySelection> context,
			UserServiceObjectContextBuilder builder) {
		// TODO Auto-generated method stub
		if (!context.isInitialDisplay()) {
			return;
		}
			
	}

	@Override
	public void validate(UserServiceValidateContext<TableViewEntitySelection> arg0) {
		// TODO Auto-generated method stub
		
	}


}
