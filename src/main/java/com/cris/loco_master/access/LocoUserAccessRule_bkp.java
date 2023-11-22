package com.cris.loco_master.access;

import com.cris.loco_master.Paths;
import com.onwbp.adaptation.Adaptation;
import com.onwbp.adaptation.AdaptationHome;
import com.onwbp.adaptation.AdaptationName;
import com.onwbp.adaptation.AdaptationTable;
import com.onwbp.adaptation.PrimaryKey;
import com.orchestranetworks.instance.HomeKey;
import com.orchestranetworks.instance.Repository;
import com.orchestranetworks.schema.Path;
import com.orchestranetworks.schema.SchemaNode;
import com.orchestranetworks.service.AccessPermission;
import com.orchestranetworks.service.AccessRule;
import com.orchestranetworks.service.Role;
import com.orchestranetworks.service.Session;

public class LocoUserAccessRule_bkp implements AccessRule {

	@Override
	public AccessPermission getPermission(Adaptation adaptation, Session session, SchemaNode node) {

		if (adaptation.isSchemaInstance()) {
			return AccessPermission.getReadWrite();
		}

		String userId = session.getUserReference().getUserId();

		if (session.isUserInRole(Role.forSpecificRole("RB_DS")) || session.isUserInRole(Role.forSpecificRole("RB_DAA"))
				|| session.isUserInRole(Role.forSpecificRole("PU_DS"))
				|| session.isUserInRole(Role.forSpecificRole("PU_DAA"))) {

			return AccessPermission.getReadWrite();
		}
//		if (session.isInWorkflowInteraction(true) && (session.isUserInRole(Role.forSpecificRole("Shed_DAA"))))
//			return AccessPermission.getReadOnly();

		final String zoneValue = (String) adaptation.get(Paths._Root_Locomotive._Root_Locomotive_Loco_Owning_Zone);
		final String shedValue = (String) adaptation.get(Paths._Root_Locomotive._Root_Locomotive_Loco_Owning_Shed);

		Repository repository = Repository.getDefault();
		final HomeKey dataSpaceKey = HomeKey.forBranchName("user_data");
		final AdaptationHome dataspaceName = repository.lookupHome(dataSpaceKey);

		final AdaptationName dataSetKey = AdaptationName.forName("user_data");
		final Adaptation datasetName = dataspaceName.findAdaptationOrNull(dataSetKey);

		AdaptationTable userRegistrationDetailsTable = datasetName
				.getTable(Path.parse("/root/User_Registration_Details"));

		AdaptationTable userPermissionsZoneTable = datasetName.getTable(Path.parse("/root/User_Permissions_Zone"));
		AdaptationTable userPermissionsDivisionTable = datasetName
				.getTable(Path.parse("/root/User_Permissions_Division"));
		AdaptationTable userPermissionsShedTable = datasetName.getTable(Path.parse("/root/User_Permissions_Shed"));

		Adaptation userRegistrationDetailsRecord = userRegistrationDetailsTable
				.lookupAdaptationByPrimaryKey(PrimaryKey.parseString(userId));

		if (userRegistrationDetailsRecord != null) {
			if (session.isUserInRole(Role.forSpecificRole("Loco_Zonal_Admin")) && zoneValue != null) {

				Adaptation userPermissionsZoneRecord = null;

				userPermissionsZoneRecord = userPermissionsZoneTable
						.lookupAdaptationByPrimaryKey(PrimaryKey.parseString(userId + "|" + zoneValue));
				if (userPermissionsZoneRecord != null) {
					return AccessPermission.getReadWrite();
				}
				return AccessPermission.getHidden();
			}

			else if ((session.isUserInRole(Role.forSpecificRole("Shed_DS"))
					|| session.isUserInRole(Role.forSpecificRole("Shed_DAA"))) && shedValue != null) {

				Adaptation userPermissionsShedRecord = null;

				userPermissionsShedRecord = userPermissionsShedTable
						.lookupAdaptationByPrimaryKey(PrimaryKey.parseString(userId + "|" + shedValue));
				if (userPermissionsShedRecord != null) {
					return AccessPermission.getReadWrite();
				}
				return AccessPermission.getHidden();
			}
		}
		return AccessPermission.getReadWrite();
	}

}
