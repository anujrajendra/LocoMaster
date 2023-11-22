package com.cris.loco_master.workflow.usertask;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import com.cris.loco_master.Paths;
import com.cris.loco_master.utils.LocoRecordOperationRestrictionUtils;
import com.onwbp.adaptation.Adaptation;
import com.onwbp.adaptation.AdaptationHome;
import com.onwbp.adaptation.AdaptationName;
import com.onwbp.adaptation.AdaptationTable;
import com.onwbp.adaptation.PrimaryKey;
import com.onwbp.base.text.UserMessage;
import com.orchestranetworks.instance.HomeKey;
import com.orchestranetworks.instance.Repository;
import com.orchestranetworks.interactions.InteractionHelper.ParametersMap;
import com.orchestranetworks.interactions.SessionInteraction;
import com.orchestranetworks.query.Query;
import com.orchestranetworks.query.QueryResult;
import com.orchestranetworks.query.Tuple;
import com.orchestranetworks.schema.Path;
import com.orchestranetworks.service.OperationException;
import com.orchestranetworks.service.Procedure;
import com.orchestranetworks.service.ProcedureContext;
import com.orchestranetworks.service.Profile;
import com.orchestranetworks.service.ProgrammaticService;
import com.orchestranetworks.service.Role;
import com.orchestranetworks.service.UserReference;
import com.orchestranetworks.service.ValueContextForUpdate;
import com.orchestranetworks.service.directory.DirectoryHandler;
import com.orchestranetworks.workflow.CreationWorkItemSpec;
import com.orchestranetworks.workflow.UserTask;
import com.orchestranetworks.workflow.UserTaskBeforeWorkItemCompletionContext;
import com.orchestranetworks.workflow.UserTaskCreationContext;
import com.orchestranetworks.workflow.WorkflowEngine;

public class LocoTaskAssignmentUserTask extends UserTask {

	String specificRoleName;
	String workflowName;

	public String getSpecificRoleName() {
		return specificRoleName;
	}

	public void setSpecificRoleName(String specificRoleName) {
		this.specificRoleName = specificRoleName;
	}

	public String getWorkflowName() {
		return workflowName;
	}

	public void setWorkflowName(String workflowName) {
		this.workflowName = workflowName;
	}

	private String dataspace;
	private String dataset;
	private String table;
	private String record;

	Repository repo;
	AdaptationHome dataspaceName;
	Adaptation datasetName;
	AdaptationTable adaptationTable = null;
	Adaptation adaptationRecord = null;

	@Override
	public void checkBeforeWorkItemCompletion(UserTaskBeforeWorkItemCompletionContext context) {
		// TODO Auto-generated method stub
		super.checkBeforeWorkItemCompletion(context);

		dataspace = context.getVariableString("childDataSpace");
		dataset = context.getVariableString("dataset");
		table = context.getVariableString("table");
		record = context.getVariableString("record");

		repo = Repository.getDefault();
		dataspaceName = repo.lookupHome(HomeKey.forBranchName(dataspace));
		datasetName = dataspaceName.findAdaptationOrNull(AdaptationName.forName(dataset));

		if (workflowName.equalsIgnoreCase("create")) {
			if (context.isAcceptAction()) {
				SessionInteraction sessionInteraction = context.getSession().getInteraction(false);
				ParametersMap sessionParamMap = sessionInteraction.getInternalParameters();

				Iterator<String> variableNames = sessionParamMap.getVariableNames();

				String record = sessionParamMap.getVariableString("created");
				String locoNumber = record.substring(record.indexOf("=") + 1, record.lastIndexOf("]"));

				String tableString = record.substring(0, record.indexOf("["));
				String recordIdentifier = record.substring(record.indexOf("=") + 2, record.indexOf("]") - 1);

				adaptationTable = datasetName.getTable((Path.parse(tableString)));
				adaptationRecord = adaptationTable
						.lookupAdaptationByPrimaryKey(PrimaryKey.parseString(recordIdentifier));

				System.out.println("===Loco number===" + locoNumber);
				WorkflowEngine wfEngine = WorkflowEngine.getFromRepository(repo, context.getSession());

				if (LocoRecordOperationRestrictionUtils.checkRunningProcesses(wfEngine, "loco_create_ui", "record",
						adaptationRecord.toXPathExpression())) {

					context.reportMessage(UserMessage.createError(" The selected record Loco Number = " + locoNumber
							+ " has already an operation (Loco Create) in process. Please Cancel this operation"));
				}
				if (LocoRecordOperationRestrictionUtils.checkRunningProcesses(wfEngine, "loco_create_api", "record",
						"/root/Locomotive[./Loco_Number=\"" + locoNumber + "\"]")) {
					context.reportMessage(UserMessage.createError(" The selected record Loco Number = " + locoNumber
							+ " has already an operation (Loco Create API) in process. Please Cancel this operation"));
				}

			}

		} else {

			String tableString = record.substring(0, record.indexOf("["));
			String recordIdentifier = record.substring(record.indexOf("=") + 2, record.indexOf("]") - 1);

			adaptationTable = datasetName.getTable((Path.parse(tableString)));
			adaptationRecord = adaptationTable.lookupAdaptationByPrimaryKey(PrimaryKey.parseString(recordIdentifier));

			if (workflowName == null)
				workflowName = "";

			if (workflowName.equalsIgnoreCase("condemn")) {
				Procedure procedure = new Procedure() {
					@Override
					public void execute(ProcedureContext pContext) throws Exception {
						// TODO Auto-generated method stub

						ValueContextForUpdate vcfu = pContext.getContext(adaptationRecord.getAdaptationName());

						if (context.isAcceptAction()) {
							vcfu.setValueEnablingPrivilegeForNode("Condemned",
									Paths._Root_Locomotive._Root_Locomotive_Loco_Status);

							pContext.doModifyContent(adaptationRecord, vcfu);
						} else {
							vcfu.setValueEnablingPrivilegeForNode("",
									Paths._Root_Locomotive._Root_Locomotive_Loco_Status);

							pContext.doModifyContent(adaptationRecord, vcfu);
						}
					}
				};

				ProgrammaticService svc = ProgrammaticService.createForSession(context.getSession(), dataspaceName);
				svc.execute(procedure);
			} else if (workflowName.equalsIgnoreCase("transfer")) {
				Procedure procedure = new Procedure() {
					@Override
					public void execute(ProcedureContext pContext) throws Exception {
						// TODO Auto-generated method stub

						ValueContextForUpdate vcfu = pContext.getContext(adaptationRecord.getAdaptationName());

						if (context.isAcceptAction()) {

							vcfu.setPrivilegeForNode(Paths._Root_Locomotive._Root_Locomotive_Loco_Status);
							vcfu.setValueEnablingPrivilegeForNode(" ",
									Paths._Root_Locomotive._Root_Locomotive_Loco_Status);

							pContext.doModifyContent(adaptationRecord, vcfu);
						} else {
							vcfu.setValueEnablingPrivilegeForNode(" ",
									Paths._Root_Locomotive._Root_Locomotive_Loco_Status);

							pContext.doModifyContent(adaptationRecord, vcfu);

						}
					}
				};

				ProgrammaticService svc = ProgrammaticService.createForSession(context.getSession(), dataspaceName);
				svc.execute(procedure);
			}
		}
	}

	@Override
	public void handleCreate(UserTaskCreationContext context) throws OperationException {

		Collection<Profile> profiles = new ArrayList<Profile>();

		if (workflowName.equalsIgnoreCase("create")) {
			profiles.add(UserReference.forSpecificRole("RB_DS"));
			profiles.add(UserReference.forSpecificRole("Shed_DS"));
			profiles.add(UserReference.forSpecificRole("RB_DAA"));
			profiles.add(UserReference.forSpecificRole("Shed_DAA"));

			context.createWorkItem(CreationWorkItemSpec.forOfferring(profiles));

		} else {
			dataspace = context.getVariableString("childDataSpace");
			dataset = context.getVariableString("dataset");
			table = context.getVariableString("table");
			record = context.getVariableString("record");

			repo = Repository.getDefault();
			dataspaceName = repo.lookupHome(HomeKey.forBranchName(dataspace));
			datasetName = dataspaceName.findAdaptationOrNull(AdaptationName.forName(dataset));

			String tableString = record.substring(0, record.indexOf("["));
			String recordIdentifier = record.substring(record.indexOf("=") + 2, record.indexOf("]") - 1);

			adaptationTable = datasetName.getTable((Path.parse(tableString)));
			adaptationRecord = adaptationTable.lookupAdaptationByPrimaryKey(PrimaryKey.parseString(recordIdentifier));

			String shedCode = (String) adaptationRecord.get(Paths._Root_Locomotive._Root_Locomotive_Loco_Owning_Shed);
			String zoneCode = (String) adaptationRecord.get(Paths._Root_Locomotive._Root_Locomotive_Loco_Owning_Zone);

			Repository repository = Repository.getDefault();
			final HomeKey userDataSpaceKey = HomeKey.forBranchName("user_data");
			final AdaptationHome userDataspaceName = repository.lookupHome(userDataSpaceKey);

			final AdaptationName userDataSetKey = AdaptationName.forName("user_data");
			final Adaptation userDatasetName = userDataspaceName.findAdaptationOrNull(userDataSetKey);

			Adaptation userPermissionsShedRecord = null;
			Adaptation userPermissionsZoneRecord = null;
			Adaptation userPermissionsRecord = null;

			String userPermissionsQuery = "";
			Query<Tuple> userPermissionsQueryTuple;
			QueryResult<Tuple> userPermissionsQueryRecords;

			if (workflowName == null)
				workflowName = "";
			if (specificRoleName == null)
				specificRoleName = "";

			if (workflowName.equalsIgnoreCase("transfer")) {
				userPermissionsQuery = "Select s.\"$adaptation\" from \"/root/User_Permissions_Zone\" s where FK_AS_STRING('user_data','/root/User_Permissions_Zone', s.Zone_Code) = '"
						+ zoneCode + "'";
			} else {
				userPermissionsQuery = "Select s.\"$adaptation\" from \"/root/User_Permissions_Shed\" s where FK_AS_STRING('user_data','/root/User_Permissions_Shed', s.Shed_Code) = '"
						+ shedCode + "'";
			}
			userPermissionsQueryTuple = userDatasetName.createQuery(userPermissionsQuery);
			userPermissionsQueryRecords = userPermissionsQueryTuple.getResult();

			for (Tuple record : userPermissionsQueryRecords) {
				// System.out.println("===Inside for loop transfer handle Create===");
				userPermissionsRecord = (Adaptation) record.get(0);
				String userId = (String) userPermissionsRecord.get(Path.parse("./User_Id"));

				UserReference userReference = UserReference.forUser(userId);
				Boolean userRoleFlag = DirectoryHandler.getInstance(repository).isUserInRole(userReference,
						Role.forSpecificRole(specificRoleName));

				if (userRoleFlag) {
					profiles.add(Profile.forUser(userId));
				}
			}

			if (workflowName.equalsIgnoreCase("create_production"))
				profiles.add(UserReference.forSpecificRole("PU_DS"));

			if (!profiles.isEmpty()) {
				context.createWorkItem(CreationWorkItemSpec.forOfferring(profiles));
			} else {
				context.createWorkItem(CreationWorkItemSpec.forOfferring(UserReference.forUser("admin")));
			}

		}
	}

}
