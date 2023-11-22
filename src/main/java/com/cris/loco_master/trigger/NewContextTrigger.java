package com.cris.loco_master.trigger;

import java.util.Date;

import com.cris.loco_master.Paths;
import com.onwbp.adaptation.Adaptation;
import com.onwbp.adaptation.AdaptationHome;
import com.onwbp.adaptation.AdaptationName;
import com.onwbp.adaptation.AdaptationTable;
import com.onwbp.adaptation.PrimaryKey;
import com.orchestranetworks.instance.HomeKey;
import com.orchestranetworks.instance.Repository;
import com.orchestranetworks.query.Query;
import com.orchestranetworks.query.QueryResult;
import com.orchestranetworks.query.Tuple;
import com.orchestranetworks.schema.Path;
import com.orchestranetworks.schema.trigger.BeforeModifyOccurrenceContext;
import com.orchestranetworks.schema.trigger.NewTransientOccurrenceContext;
import com.orchestranetworks.schema.trigger.TableTrigger;
import com.orchestranetworks.schema.trigger.TriggerSetupContext;
import com.orchestranetworks.service.OperationException;
import com.orchestranetworks.service.Role;
import com.orchestranetworks.service.Session;
import com.orchestranetworks.service.UserReference;
import com.orchestranetworks.service.ValueContextForUpdate;

public class NewContextTrigger extends TableTrigger {

	@Override
	public void handleNewContext(NewTransientOccurrenceContext aContext) {
		// TODO Auto-generated method stub
		super.handleNewContext(aContext);

		// Applied on Locomotive table

		String name = aContext.getSession().getUserReference().getUserId();
		String userId = aContext.getSession().getUserReference().getUserId();
		// System.out.println("==Inside Trigger User ID==" + userId);

		ValueContextForUpdate valueContextForUpdate = aContext.getOccurrenceContextForUpdate();
		valueContextForUpdate.setValue(userId, Paths._Root_Locomotive._Root_Locomotive_Audit_Info_Logged_In_User);

		valueContextForUpdate.setValue(name, Paths._Root_Locomotive._Root_Locomotive_Audit_Info_Created_By_User);

		valueContextForUpdate.setValueEnablingPrivilegeForNode(new Date(),
				Paths._Root_Locomotive._Root_Locomotive_Loco_Entry_Date);

		Repository repository = Repository.getDefault();

		UserReference userReference = UserReference.forUser(userId);

		Boolean userRoleFlag = aContext.getSession().isUserInRole(Role.forSpecificRole("RB_DS"))
				|| aContext.getSession().isUserInRole(Role.forSpecificRole("RB_DAA"));

		final HomeKey userDataSpaceKey = HomeKey.forBranchName("user_data");
		final AdaptationHome userDataspaceName = repository.lookupHome(userDataSpaceKey);

		final AdaptationName userDataSetKey = AdaptationName.forName("user_data");
		final Adaptation userDatasetName = userDataspaceName.findAdaptationOrNull(userDataSetKey);

		AdaptationTable userRegistrationDetailsTable = userDatasetName
				.getTable(Path.parse("/root/User_Registration_Details"));

		final HomeKey referenceDataSpaceKey = HomeKey.forBranchName("reference_data");
		final AdaptationHome referenceDataspaceName = repository.lookupHome(referenceDataSpaceKey);

		final AdaptationName referenceDataSetKey = AdaptationName.forName("reference_data");
		final Adaptation referenceDatasetName = referenceDataspaceName.findAdaptationOrNull(referenceDataSetKey);

		AdaptationTable shedTable = referenceDatasetName.getTable(Path.parse("/root/Shed"));
		AdaptationTable divisionTable = referenceDatasetName.getTable(Path.parse("/root/Division"));
//		AdaptationTable zoneTable = referenceDatasetName.getTable(Path.parse("/root/Zone"));

		Adaptation userRegistrationDetailsRecord = null;
		try {
			userRegistrationDetailsRecord = userRegistrationDetailsTable
					.lookupAdaptationByPrimaryKey(PrimaryKey.parseString(userId));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (userRoleFlag) {

			String shedQuery = "Select s.\"$adaptation\" from \"/root/Shed\" s ";
			Query<Tuple> shedQueryTuple = referenceDatasetName.createQuery(shedQuery);
			QueryResult<Tuple> shedRecords = shedQueryTuple.getResult();

			Adaptation shedRecord = null;

			String zoneCode = "";
			String divisionCode = "";
			String shedCode = "";

			String shedSourceSystemName = "";

			for (Tuple shedResult : shedRecords) {
				shedRecord = (Adaptation) shedResult.get(0);
				if (shedRecord != null) {
					shedSourceSystemName = shedRecord.getString(Path.parse("./Source_System_Name"));
					if (shedSourceSystemName != null)
						if (shedSourceSystemName.equalsIgnoreCase("SLAM"))
							continue;
					zoneCode = shedRecord.getString(Path.parse("./Zone_Code"));
					divisionCode = shedRecord.getString(Path.parse("./Division_Code"));
					shedCode = shedRecord.getString(Path.parse("./Shed_Code"));

				} else {
					continue;
				}
				valueContextForUpdate.setValue(zoneCode, Paths._Root_Locomotive._Root_Locomotive_Loco_Owning_Zone);
				valueContextForUpdate.setValue(divisionCode,
						Paths._Root_Locomotive._Root_Locomotive_Loco_Owning_Division);
				valueContextForUpdate.setValue(shedCode, Paths._Root_Locomotive._Root_Locomotive_Loco_Owning_Shed);
				break;
			}
		} else if (userRegistrationDetailsRecord != null && (!userRoleFlag)) {
			Adaptation userPermissionsZoneRecord = null;

			String userPermissionsZoneQuery = "Select s.\"$adaptation\" from \"/root/User_Permissions_Zone\" s where FK_AS_STRING('user_data','/root/User_Permissions_Zone', s.User_Id) = '"
					+ userId + "'";
			Query<Tuple> userPermissionsZoneQueryTuple = userDatasetName.createQuery(userPermissionsZoneQuery);
			QueryResult<Tuple> userPermissionsZoneRecords = userPermissionsZoneQueryTuple.getResult();

			String userPermissionsDivisionQuery = "Select s.\"$adaptation\" from \"/root/User_Permissions_Division\" s where FK_AS_STRING('user_data','/root/User_Permissions_Division', s.User_Id) = '"
					+ userId + "'";
			Query<Tuple> userPermissionsDivisionQueryTuple = userDatasetName.createQuery(userPermissionsDivisionQuery);
			QueryResult<Tuple> userPermissionsDivisionRecords = userPermissionsDivisionQueryTuple.getResult();

			String userPermissionsShedQuery = "Select s.\"$adaptation\" from \"/root/User_Permissions_Shed\" s where FK_AS_STRING('user_data','/root/User_Permissions_Shed', s.User_Id) = '"
					+ userId + "'";
			Query<Tuple> userPermissionsShedQueryTuple = userDatasetName.createQuery(userPermissionsShedQuery);
			QueryResult<Tuple> userPermissionsShedRecords = userPermissionsShedQueryTuple.getResult();

			if (userPermissionsZoneRecords.iterator().hasNext()) {
				for (Tuple zoneResult : userPermissionsZoneRecords) {
					userPermissionsZoneRecord = (Adaptation) zoneResult.get(0);
					String zoneCode = (String) userPermissionsZoneRecord.get(Path.parse("./Zone_Code"));
					valueContextForUpdate.setValue(zoneCode, Paths._Root_Locomotive._Root_Locomotive_Loco_Owning_Zone);

					break;
				}

			} else if (userPermissionsDivisionRecords.iterator().hasNext()) {
				Adaptation userPermissionsDivisionRecord = null;
				for (Tuple divisionResult : userPermissionsDivisionRecords) {
					userPermissionsDivisionRecord = (Adaptation) divisionResult.get(0);
					String divisionCode = (String) userPermissionsDivisionRecord.get(Path.parse("./Division_Code"));

					Adaptation divisionRecord = divisionTable
							.lookupAdaptationByPrimaryKey(PrimaryKey.parseString(divisionCode));

					String zoneCode = divisionRecord.getString(Path.parse("./Zone_Code"));

					valueContextForUpdate.setValue(zoneCode, Paths._Root_Locomotive._Root_Locomotive_Loco_Owning_Zone);
					valueContextForUpdate.setValue(divisionCode,
							Paths._Root_Locomotive._Root_Locomotive_Loco_Owning_Division);
					break;
				}
			} else if (userPermissionsShedRecords.iterator().hasNext()) {
				Adaptation userPermissionsShedRecord = null;
				for (Tuple shedResult : userPermissionsShedRecords) {
					userPermissionsShedRecord = (Adaptation) shedResult.get(0);
					String shedValue = (String) userPermissionsShedRecord.get(Path.parse("./Shed_Code"));

					Adaptation shedRecord = null;

					String zoneCode = "";
					String divisionCode = "";
					String shedCode = "";
					String shedSourceSystemName = "";

					if (shedValue != null) {
						shedRecord = shedTable.lookupAdaptationByPrimaryKey(PrimaryKey.parseString(shedValue));
					} else {
						continue;
					}

					if (shedRecord != null) {
						shedSourceSystemName = shedRecord.getString(Path.parse("./Source_System_Name"));
						if (shedSourceSystemName != null)
							if (shedSourceSystemName.equalsIgnoreCase("SLAM"))
								continue;
						zoneCode = shedRecord.getString(Path.parse("./Zone_Code"));
						divisionCode = shedRecord.getString(Path.parse("./Division_Code"));
						shedCode = shedRecord.getString(Path.parse("./Shed_Code"));

					} else {
						continue;
					}
					valueContextForUpdate.setValue(zoneCode, Paths._Root_Locomotive._Root_Locomotive_Loco_Owning_Zone);
					valueContextForUpdate.setValue(divisionCode,
							Paths._Root_Locomotive._Root_Locomotive_Loco_Owning_Division);
					valueContextForUpdate.setValue(shedCode, Paths._Root_Locomotive._Root_Locomotive_Loco_Owning_Shed);
					break;
				}

			}
		}
	}

	@Override
	public void setup(TriggerSetupContext arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void handleBeforeModify(BeforeModifyOccurrenceContext aContext) throws OperationException {
		// TODO Auto-generated method stub
		super.handleBeforeModify(aContext);

		Session session = aContext.getSession();

		if (session.isInWorkflowInteraction(true)) {

			if (session.getTrackingInfo() != null) {

				if (session.getTrackingInfo().equalsIgnoreCase("Loco_Create_API")) {

					ValueContextForUpdate valueContextForUpdate = aContext.getOccurrenceContextForUpdate();
					valueContextForUpdate.setValueEnablingPrivilegeForNode("SLUP",
							Paths._Root_Locomotive._Root_Locomotive_Audit_Info_Source_Event_Type);
				}

			}

		}
	}
}