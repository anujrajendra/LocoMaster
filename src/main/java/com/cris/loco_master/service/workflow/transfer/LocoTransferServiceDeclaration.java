package com.cris.loco_master.service.workflow.transfer;

import com.cris.loco_master.Paths;
import com.orchestranetworks.service.ServiceKey;
import com.orchestranetworks.ui.selection.TableViewEntitySelection;
import com.orchestranetworks.userservice.UserService;
import com.orchestranetworks.userservice.declaration.ActivationContextOnTableView;
import com.orchestranetworks.userservice.declaration.UserServiceDeclaration;
import com.orchestranetworks.userservice.declaration.UserServicePropertiesDefinitionContext;
import com.orchestranetworks.userservice.declaration.WebComponentDeclarationContext;

public class LocoTransferServiceDeclaration implements UserServiceDeclaration.OnTableView {

	// Identifier for the service.
	public static final ServiceKey SERVICE_KEY = ServiceKey.forName("Loco_Transfer");

	// Creates a new user service implementation.
	@Override
	public UserService<TableViewEntitySelection> createUserService() {
		// Returns the object of the user service implementation.
		return new LocoTransferService();
	}

	@Override
	public void declareWebComponent(WebComponentDeclarationContext context) {
		context.setAvailableAsToolbarAction(true);
		context.setAvailableAsWorkflowUserTask(true);
	}

	@Override
	public void defineActivation(ActivationContextOnTableView context) {
		context.includeSchemaNodesMatching(Paths._Root_Locomotive.getPathInSchema());
		context.forbidEmptyRecordSelection();
		context.limitRecordSelection(1);

	}

	@Override
	public void defineProperties(UserServicePropertiesDefinitionContext context) {
		context.setLabel("Transfer Loco");
		context.setDescription("Service to transfer locomotive/s");
	}

	@Override
	public ServiceKey getServiceKey() {
		return SERVICE_KEY;
	}
}