package com.cris.loco_master.service.displaydq;

import com.cris.loco_master.Paths;
import com.orchestranetworks.service.ServiceKey;
import com.orchestranetworks.ui.selection.TableViewEntitySelection;
import com.orchestranetworks.userservice.UserService;
import com.orchestranetworks.userservice.declaration.ActivationContextOnTableView;
import com.orchestranetworks.userservice.declaration.UserServiceDeclaration;
import com.orchestranetworks.userservice.declaration.UserServicePropertiesDefinitionContext;
import com.orchestranetworks.userservice.declaration.WebComponentDeclarationContext;

public class LocoDQServiceDeclaration implements UserServiceDeclaration.OnTableView{

	public static final ServiceKey SERVICE_KEY = ServiceKey.forName("LocoDQService");
	
	@Override
	public ServiceKey getServiceKey() {
		// TODO Auto-generated method stub
		return SERVICE_KEY;
	}
	
	@Override
	public UserService<TableViewEntitySelection> createUserService() {
		// TODO Auto-generated method stub
		return new LocoDQService(new DisplayResultStep());
	}

	@Override
	public void declareWebComponent(WebComponentDeclarationContext context) {
		// TODO Auto-generated method stub
		context.setAvailableAsToolbarAction(true);
		
	}

	@Override
	public void defineActivation(ActivationContextOnTableView context) {
		// TODO Auto-generated method stub
		context.includeSchemaNodesMatching(Paths._Root_Loco_Sources_Second.getPathInSchema());
	}

	@Override
	public void defineProperties(UserServicePropertiesDefinitionContext context) {
		// TODO Auto-generated method stub
		context.setLabel("Loco DQ Score");
	}

	
}
