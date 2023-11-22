package com.cris.loco_master.access;

import com.cris.loco_master.Paths;
import com.cris.loco_master.constant.Constants;
import com.onwbp.adaptation.Adaptation;
import com.onwbp.adaptation.PrimaryKey;
import com.orchestranetworks.instance.Repository;
import com.orchestranetworks.schema.SchemaNode;
import com.orchestranetworks.service.AccessPermission;
import com.orchestranetworks.service.AccessRule;
import com.orchestranetworks.service.LoggingCategory;
import com.orchestranetworks.service.Role;
import com.orchestranetworks.service.Session;
import com.orchestranetworks.service.UserReference;
import com.orchestranetworks.service.directory.DirectoryHandler;

public class LocoUserAccessRule implements AccessRule {

	@Override
	public AccessPermission getPermission(Adaptation adaptation, Session session, SchemaNode node) {

//		if (adaptation.isSchemaInstance()) {
//			return AccessPermission.getReadWrite();
//		}
//
//		String userId = session.getUserReference().getUserId();
//
//		if (session.isUserInRole(Role.forSpecificRole("RB_DS")) || session.isUserInRole(Role.forSpecificRole("RB_DAA"))
//				|| session.isUserInRole(Role.forSpecificRole("PU_DS"))
//				|| session.isUserInRole(Role.forSpecificRole("PU_DAA"))) {
//
//			return AccessPermission.getReadWrite();
//		}
////		if (session.isInWorkflowInteraction(true) && (session.isUserInRole(Role.forSpecificRole("Shed_DAA"))))
////			return AccessPermission.getReadOnly();
//
//		final String zoneValue = (String) adaptation.get(Paths._Root_Locomotive._Root_Locomotive_Loco_Owning_Zone);
//		final String shedValue = (String) adaptation.get(Paths._Root_Locomotive._Root_Locomotive_Loco_Owning_Shed);
//
//		Repository repository = Repository.getDefault();
//		final HomeKey dataSpaceKey = HomeKey.forBranchName("user_data");
//		final AdaptationHome dataspaceName = repository.lookupHome(dataSpaceKey);
//
//		final AdaptationName dataSetKey = AdaptationName.forName("user_data");
//		final Adaptation datasetName = dataspaceName.findAdaptationOrNull(dataSetKey);
//
//		AdaptationTable userRegistrationDetailsTable = datasetName
//				.getTable(Path.parse("/root/User_Registration_Details"));
//
//		AdaptationTable userPermissionsZoneTable = datasetName.getTable(Path.parse("/root/User_Permissions_Zone"));
//		AdaptationTable userPermissionsDivisionTable = datasetName
//				.getTable(Path.parse("/root/User_Permissions_Division"));
//		AdaptationTable userPermissionsShedTable = datasetName.getTable(Path.parse("/root/User_Permissions_Shed"));
//
//		Adaptation userRegistrationDetailsRecord = userRegistrationDetailsTable
//				.lookupAdaptationByPrimaryKey(PrimaryKey.parseString(userId));
//
//		if (userRegistrationDetailsRecord != null) {
//			if (session.isUserInRole(Role.forSpecificRole("Loco_Zonal_Admin")) && zoneValue != null) {
//
//				Adaptation userPermissionsZoneRecord = null;
//
//				userPermissionsZoneRecord = userPermissionsZoneTable
//						.lookupAdaptationByPrimaryKey(PrimaryKey.parseString(userId + "|" + zoneValue));
//				if (userPermissionsZoneRecord != null) {
//					return AccessPermission.getReadWrite();
//				}
//				return AccessPermission.getHidden();
//			}
//
//			else if ((session.isUserInRole(Role.forSpecificRole("Shed_DS"))
//					|| session.isUserInRole(Role.forSpecificRole("Shed_DAA"))) && shedValue != null) {
//
//				Adaptation userPermissionsShedRecord = null;
//
//				userPermissionsShedRecord = userPermissionsShedTable
//						.lookupAdaptationByPrimaryKey(PrimaryKey.parseString(userId + "|" + shedValue));
//				if (userPermissionsShedRecord != null) {
//					return AccessPermission.getReadWrite();
//				}
//				return AccessPermission.getHidden();
//			}
//		}
//		return AccessPermission.getReadWrite();

		if (adaptation.isSchemaInstance()) {
			return AccessPermission.getReadWrite();
		}

		Repository repository = Repository.getDefault();
		DirectoryHandler directoryHandler = DirectoryHandler.getInstance(repository);

		String userId = session.getUserReference().getUserId();
		LoggingCategory.getWorkflow().info("User ID: " + userId);

		UserReference userReference = UserReference.forUser(userId);

		final String zoneValue = (String) adaptation.get(Paths._Root_Locomotive._Root_Locomotive_Loco_Owning_Zone);
		final String shedValue = (String) adaptation.get(Paths._Root_Locomotive._Root_Locomotive_Loco_Owning_Shed);

		boolean isRBUser = directoryHandler.isUserInRole(userReference, Role.forSpecificRole("RB_DS"))
				|| directoryHandler.isUserInRole(userReference, Role.forSpecificRole("RB_DAA"));
		LoggingCategory.getWorkflow().debug("Is RB User: " + isRBUser);

		boolean isPUUser = directoryHandler.isUserInRole(userReference, Role.forSpecificRole("PU_DAA"))
				|| directoryHandler.isUserInRole(userReference, Role.forSpecificRole("PU_DS"));
		LoggingCategory.getWorkflow().debug("Is PU User: " + isPUUser);

		boolean isAdminUser = directoryHandler.isUserInRole(userReference, Role.ADMINISTRATOR);
		LoggingCategory.getWorkflow().debug("Is Administrator: " + isAdminUser);

		boolean isZonalAdmin = directoryHandler.isUserInRole(userReference, Role.forSpecificRole("Loco_Zonal_Admin"));
		LoggingCategory.getWorkflow().debug("Is Zonal Admin: " + isZonalAdmin);

		boolean isShedUser = directoryHandler.isUserInRole(userReference, Role.forSpecificRole("Shed_DAA"))
				|| directoryHandler.isUserInRole(userReference, Role.forSpecificRole("Shed_DS"));
		LoggingCategory.getWorkflow().debug("Is Shed User: " + isShedUser);

		if (isShedUser) {

			Adaptation userPermissionsShedRecord = Constants.TABLE_USERPERMISSION_SHED
					.lookupAdaptationByPrimaryKey(PrimaryKey.parseString(userId + "|" + shedValue));

			if (userPermissionsShedRecord == null) {
				return AccessPermission.getHidden();
			} else if (session.isInWorkflowInteraction(false)) {
				return AccessPermission.getReadWrite();
			} else {
				return AccessPermission.getReadOnly();
			}

		} else if (isZonalAdmin) {

			Adaptation userPermissionsZoneRecord = null;

			userPermissionsZoneRecord = Constants.TABLE_USERPERMISSION_ZONE
					.lookupAdaptationByPrimaryKey(PrimaryKey.parseString(userId + "|" + zoneValue));

			if (userPermissionsZoneRecord == null) {
				return AccessPermission.getHidden();
			} else if (session.isInWorkflowInteraction(false)) {
				return AccessPermission.getReadWrite();
			} else {
				return AccessPermission.getReadOnly();
			}

		} else if (isRBUser || isPUUser || isAdminUser) {

			if (session.isInWorkflowInteraction(false)) {
				return AccessPermission.getReadWrite();
			} else {
				return AccessPermission.getReadOnly();
			}
		}

		return AccessPermission.getReadOnly();
	}

}
