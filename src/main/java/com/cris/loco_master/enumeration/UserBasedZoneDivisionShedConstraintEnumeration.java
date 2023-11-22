package com.cris.loco_master.enumeration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import com.cris.loco_master.Paths;
import com.cris.loco_master.utils.DropDownUtils;
import com.orchestranetworks.instance.Repository;
import com.orchestranetworks.instance.ValueContext;
import com.orchestranetworks.instance.ValueContextForValidation;
import com.orchestranetworks.schema.ConstraintContext;
import com.orchestranetworks.schema.ConstraintEnumeration;
import com.orchestranetworks.schema.InvalidSchemaException;
import com.orchestranetworks.schema.Path;
import com.orchestranetworks.service.LoggingCategory;
import com.orchestranetworks.service.Role;
import com.orchestranetworks.service.UserReference;
import com.orchestranetworks.service.directory.DirectoryHandler;

public class UserBasedZoneDivisionShedConstraintEnumeration implements ConstraintEnumeration<String> {

	String attributeName;
	HashMap<String, String> map = new HashMap<String, String>();
	HashMap<String, String> valueMap = new HashMap<String, String>();

	@Override
	public void checkOccurrence(String arg0, ValueContextForValidation arg1) throws InvalidSchemaException {
		// TODO Auto-generated method stub

	}

	public String getAttributeName() {
		return attributeName;
	}

	public void setAttributeName(String attributeName) {
		this.attributeName = attributeName;
	}

	@Override
	public void setup(ConstraintContext arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public String toUserDocumentation(Locale arg0, ValueContext arg1) throws InvalidSchemaException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String displayOccurrence(String code, ValueContext arg1, Locale arg2) throws InvalidSchemaException {
		// TODO Auto-generated method stub
		return map.get(code);
	}

	@Override
	public List<String> getValues(ValueContext context) throws InvalidSchemaException {
		// TODO Auto-generated method stub

		LoggingCategory.getWorkflow().debug(attributeName);

		Repository repository = Repository.getDefault();

		DirectoryHandler directoryHandler = DirectoryHandler.getInstance(repository);

		String userId = (String) context
				.getValue(Path.PARENT.add(Paths._Root_Locomotive._Root_Locomotive_Audit_Info_Logged_In_User));

		LoggingCategory.getWorkflow().debug("Get Values User ID: " + userId);

		UserReference userReference = UserReference.forUser(userId);

		String zoneValue = (String) context
				.getValue(Path.PARENT.add(Paths._Root_Locomotive._Root_Locomotive_Loco_Owning_Zone));
		LoggingCategory.getWorkflow().debug(zoneValue);

		String divisionValue = (String) context
				.getValue(Path.PARENT.add(Paths._Root_Locomotive._Root_Locomotive_Loco_Owning_Division));
		LoggingCategory.getWorkflow().debug(divisionValue);

		boolean isRBUser = directoryHandler.isUserInRole(userReference, Role.forSpecificRole("RB_DS"))
				|| directoryHandler.isUserInRole(userReference, Role.forSpecificRole("RB_DAA"));
		LoggingCategory.getWorkflow().debug("Is RB User: " + isRBUser);

		boolean isPUUser = directoryHandler.isUserInRole(userReference, Role.forSpecificRole("PU_DAA"))
				|| directoryHandler.isUserInRole(userReference, Role.forSpecificRole("PU_DS"));
		LoggingCategory.getWorkflow().debug("Is PU User: " + isPUUser);

		boolean isAdminUser = directoryHandler.isUserInRole(userReference, Role.ADMINISTRATOR);
		LoggingCategory.getWorkflow().debug("Is Administrator: " + isAdminUser);

//		LoggingCategory.getWorkflow().debug("Check Builtin Role Role.ADMINISTRATOR): "
//				+ directoryHandler.isUserInRole(userReference, Role.ADMINISTRATOR));

//		LoggingCategory.getWorkflow().info("Check Builtin Role (Role.forBuiltInRole(\"Administrator\")): "
//				+ directoryHandler.isUserInRole(userReference, Role.forBuiltInRole("Administrator"))); -- Not Working

//		LoggingCategory.getWorkflow().info("Check Builtin Role (Role.forBuiltInRole(\"RAdministrator\")): "
//				+ directoryHandler.isUserInRole(userReference, Role.forBuiltInRole("RAdministrator"))); -- Not Working

//		LoggingCategory.getWorkflow().debug("Check Builtin Role (isBuiltIn): " + userReference.isBuiltIn()); // Not
//																											// working
//
//		LoggingCategory.getWorkflow()
//				.debug("Check Builtin Role (isBuiltInAdministrator): " + userReference.isBuiltInAdministrator()); // Not
//																													// working
//
//		LoggingCategory.getWorkflow().debug("Check Builtin Role (Role.forSpecificRole(\"Administrator\")): "
//				+ directoryHandler.isUserInRole(userReference, Role.forSpecificRole("Administrator"))); // -- Not
//																										// working
//
//		LoggingCategory.getWorkflow().debug("Check Builtin Role (Role.forSpecificRole(\"RAdministrator\")): "
//				+ directoryHandler.isUserInRole(userReference, Role.forSpecificRole("RAdministrator")));
//
//		LoggingCategory.getWorkflow()
//				.debug("isBuiltInAdministrator Disabled: " + directoryHandler.isBuiltInAdministratorDisabled());

		boolean isZonalAdmin = directoryHandler.isUserInRole(userReference, Role.forSpecificRole("Loco_Zonal_Admin"));
		LoggingCategory.getWorkflow().debug("Is Zonal Admin: " + isZonalAdmin);

		boolean isShedUser = directoryHandler.isUserInRole(userReference, Role.forSpecificRole("Shed_DAA"))
				|| directoryHandler.isUserInRole(userReference, Role.forSpecificRole("Shed_DS"));
		LoggingCategory.getWorkflow().debug("Is Shed User: " + isShedUser);

		if (isRBUser || isPUUser || isAdminUser) {
			LoggingCategory.getWorkflow().debug("RB User, PU User, Admin User");
			if (this.attributeName.equalsIgnoreCase("Zone"))
				valueMap = DropDownUtils.getAllZoneFromRef();
			else if (this.attributeName.equalsIgnoreCase("Division"))
				valueMap = DropDownUtils.getDivisionForZoneFromRef(zoneValue);
			else if (this.attributeName.equalsIgnoreCase("Shed"))
				valueMap = DropDownUtils.getShedForDivisionFromRef(zoneValue, divisionValue);

		} else if (isZonalAdmin) {
			LoggingCategory.getWorkflow().debug("Zonal Admin");
			if (this.attributeName.equalsIgnoreCase("Zone"))
				valueMap = DropDownUtils.getAllZoneFromRef();
			else if (this.attributeName.equalsIgnoreCase("Division"))
				valueMap = DropDownUtils.getDivisionsForZoneFromUR(userId, zoneValue);
			else if (this.attributeName.equalsIgnoreCase("Shed"))
				valueMap = DropDownUtils.getShedForDivisionFromUR(userId, zoneValue, divisionValue);

		} else if (isShedUser) {
			LoggingCategory.getWorkflow().debug("Shed User");
			if (this.attributeName.equalsIgnoreCase("Zone"))
				valueMap = DropDownUtils.getZonesForUser(userId);
			else if (this.attributeName.equalsIgnoreCase("Division"))
				valueMap = DropDownUtils.getDivisionsForZoneFromUR(userId, zoneValue);
			else if (this.attributeName.equalsIgnoreCase("Shed"))
				valueMap = DropDownUtils.getShedForDivisionFromUR(userId, zoneValue, divisionValue);
		}

		ArrayList<String> codeList = new ArrayList<String>(valueMap.keySet());

		LoggingCategory.getWorkflow()
				.info("_______________________________________________________________________________________");
		return codeList;

//		String userId = (String) context
//				.getValue(Path.PARENT.add(Paths._Root_Locomotive._Root_Locomotive_Audit_Info_Logged_In_User));
//
//		Repository repository = Repository.getDefault();
//		UserReference userReference = UserReference.forUser(userId);
//		Boolean userRoleFlag = DirectoryHandler.getInstance(repository).isUserInRole(userReference,
//				Role.forSpecificRole("RB_DS"));
//		if (!userRoleFlag) {
//			userRoleFlag = DirectoryHandler.getInstance(repository).isUserInRole(userReference,
//					Role.forSpecificRole("RB_DAA"));
//		}
//
//		String zoneValue = (String) context
//				.getValue(Path.PARENT.add(Paths._Root_Locomotive._Root_Locomotive_Loco_Owning_Zone));
//		String divisionValue = (String) context
//				.getValue(Path.PARENT.add(Paths._Root_Locomotive._Root_Locomotive_Loco_Owning_Division));
//
//		// Repository repository = Repository.getDefault();
//		final HomeKey userDataSpaceKey = HomeKey.forBranchName("user_data");
//		final AdaptationHome userDataspaceName = repository.lookupHome(userDataSpaceKey);
//
//		final AdaptationName userDataSetKey = AdaptationName.forName("user_data");
//		final Adaptation userDatasetName = userDataspaceName.findAdaptationOrNull(userDataSetKey);
//
//		AdaptationTable userRegistrationDetailsTable = userDatasetName
//				.getTable(Path.parse("/root/User_Registration_Details"));
//
//		final HomeKey referenceDataSpaceKey = HomeKey.forBranchName("reference_data");
//		final AdaptationHome referenceDataspaceName = repository.lookupHome(referenceDataSpaceKey);
//
//		final AdaptationName referenceDataSetKey = AdaptationName.forName("reference_data");
//		final Adaptation referenceDatasetName = referenceDataspaceName.findAdaptationOrNull(referenceDataSetKey);
//
//		AdaptationTable shedTable = referenceDatasetName.getTable(Path.parse("/root/Shed"));
//		AdaptationTable divisionTable = referenceDatasetName.getTable(Path.parse("/root/Division"));
//		AdaptationTable zoneTable = referenceDatasetName.getTable(Path.parse("/root/Zone"));
//
//		Adaptation userRegistrationDetailsRecord = null;
//		try {
//			userRegistrationDetailsRecord = userRegistrationDetailsTable
//					.lookupAdaptationByPrimaryKey(PrimaryKey.parseString(userId));
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//		List<String> list = new ArrayList<String>();
//
//		if (userRegistrationDetailsRecord != null && (!userRoleFlag)) {
//
//			Adaptation userPermissionsZoneRecord = null;
//
//			String userPermissionsZoneQuery = "Select s.\"$adaptation\" from \"/root/User_Permissions_Zone\" s where FK_AS_STRING('user_data','/root/User_Permissions_Zone', s.User_Id) = '"
//					+ userId + "'";
//			Query<Tuple> userPermissionsZoneQueryTuple = userDatasetName.createQuery(userPermissionsZoneQuery);
//			QueryResult<Tuple> userPermissionsZoneRecords = userPermissionsZoneQueryTuple.getResult();
//
//			String userPermissionsDivisionQuery = "Select s.\"$adaptation\" from \"/root/User_Permissions_Division\" s where FK_AS_STRING('user_data','/root/User_Permissions_Division', s.User_Id) = '"
//					+ userId + "'";
//			Query<Tuple> userPermissionsDivisionQueryTuple = userDatasetName.createQuery(userPermissionsDivisionQuery);
//			QueryResult<Tuple> userPermissionsDivisionRecords = userPermissionsDivisionQueryTuple.getResult();
//
//			String userPermissionsShedQuery = "Select s.\"$adaptation\" from \"/root/User_Permissions_Shed\" s where FK_AS_STRING('user_data','/root/User_Permissions_Shed', s.User_Id) = '"
//					+ userId + "'";
//			Query<Tuple> userPermissionsShedQueryTuple = userDatasetName.createQuery(userPermissionsShedQuery);
//			QueryResult<Tuple> userPermissionsShedRecords = userPermissionsShedQueryTuple.getResult();
//
//			if (userPermissionsZoneRecords.iterator().hasNext()) {
//				for (Tuple zoneResult : userPermissionsZoneRecords) {
//					userPermissionsZoneRecord = (Adaptation) zoneResult.get(0);
//					String zoneCode = (String) userPermissionsZoneRecord.get(Path.parse("./Zone_Code"));
//
//					if (attributeName.equalsIgnoreCase("Zone")) {
//						Adaptation zoneRecord = zoneTable
//								.lookupAdaptationByPrimaryKey(PrimaryKey.parseString(zoneCode));
//						String zoneName = zoneRecord.getString(Path.parse("./Zone_Name"));
//						zoneName = zoneName != null ? zoneName : "";
//
//						list.add(zoneCode);
//						if (zoneCode.equalsIgnoreCase(zoneName)) {
//							map.put(zoneCode, zoneName);
//						} else {
//							map.put(zoneCode, zoneCode + ", " + zoneName);
//						}
//					} else if (attributeName.equalsIgnoreCase("Division")) {
//
//						if (zoneValue != null) {
//							String divisionQuery = "Select s.\"$adaptation\" from \"/root/Division\" s ";
//							Query<Tuple> divisionQueryTuple = referenceDatasetName.createQuery(divisionQuery);
//							QueryResult<Tuple> divisionRecords = divisionQueryTuple.getResult();
//
//							Adaptation divisionRecord = null;
//							String divisionCode = "";
//							String divisionName = "";
//
//							for (Tuple divisionResult : divisionRecords) {
//								divisionRecord = (Adaptation) divisionResult.get(0);
//
//								divisionCode = divisionRecord.getString(Path.parse("./Division_Code"));
//								divisionName = divisionRecord.getString(Path.parse("./Division_Name"));
//
//								if (zoneValue.equalsIgnoreCase(divisionRecord.getString(Path.parse("./Zone_Code")))) {
//									divisionName = divisionName != null ? divisionName : "";
//									if (!list.contains(divisionCode)) {
//										list.add(divisionCode);
//										if (divisionCode.equalsIgnoreCase(divisionName)) {
//											map.put(divisionCode, divisionName);
//										} else {
//											map.put(divisionCode, divisionCode + ", " + divisionName);
//										}
//									}
//								}
//
//							}
//						}
//					} else if (attributeName.equalsIgnoreCase("Shed")) {
//
//						if (zoneValue != null && divisionValue != null) {
//							String shedQuery = "Select s.\"$adaptation\" from \"/root/Shed\" s ";
//							Query<Tuple> shedQueryTuple = referenceDatasetName.createQuery(shedQuery);
//							QueryResult<Tuple> shedRecords = shedQueryTuple.getResult();
//
//							Adaptation shedRecord = null;
//							String shedCode = "";
//							String shedName = "";
//							String shedSourceSystemName = "";
//
//							for (Tuple shedResult : shedRecords) {
//								shedRecord = (Adaptation) shedResult.get(0);
//
//								shedCode = shedRecord.getString(Path.parse("./Shed_Code"));
//								shedName = shedRecord.getString(Path.parse("./Shed_Name"));
//								shedSourceSystemName = shedRecord.getString(Path.parse("./Source_System_Name"));
//								if (shedSourceSystemName != null)
//									if (shedSourceSystemName.equalsIgnoreCase("SLAM"))
//										continue;
//
//								if (zoneValue.equalsIgnoreCase(shedRecord.getString(Path.parse("./Zone_Code")))
//										&& divisionValue.equalsIgnoreCase(
//												shedRecord.getString(Path.parse("./Division_Code")))) {
//									shedName = shedName != null ? shedName : "";
//									if (!list.contains(shedCode)) {
//										list.add(shedCode);
//										if (shedCode.equalsIgnoreCase(shedName)) {
//											map.put(shedCode, shedName);
//										} else {
//											map.put(shedCode, shedCode + ", " + shedName);
//										}
//									}
//								}
//
//							}
//
//						}
//					}
//				}
//
//			} else if (userPermissionsDivisionRecords.iterator().hasNext()) {
//				Adaptation userPermissionsDivisionRecord = null;
//				for (Tuple divisionResult : userPermissionsDivisionRecords) {
//					userPermissionsDivisionRecord = (Adaptation) divisionResult.get(0);
//					String divisionCode = (String) userPermissionsDivisionRecord.get(Path.parse("./Division_Code"));
//
//					Adaptation divisionRecord = divisionTable
//							.lookupAdaptationByPrimaryKey(PrimaryKey.parseString(divisionCode));
//					String divisionName = divisionRecord.getString(Path.parse("./Division_Name"));
//
//					String zoneCode = divisionRecord.getString(Path.parse("./Zone_Code"));
//					String zoneName = "";
//
//					if (attributeName.equalsIgnoreCase("Zone")) {
//						Adaptation zoneRecord = null;
//						zoneRecord = zoneTable.lookupAdaptationByPrimaryKey(PrimaryKey.parseString(zoneCode));
//						if (zoneRecord != null)
//							zoneName = zoneRecord.getString(Path.parse("./Zone_Name"));
//						else
//							continue;
//
//						zoneName = zoneName != null ? zoneName : "";
//						if (!list.contains(zoneCode)) {
//							list.add(zoneCode);
//							if (zoneCode.equalsIgnoreCase(zoneName)) {
//								map.put(zoneCode, zoneName);
//							} else {
//								map.put(zoneCode, zoneCode + ", " + zoneName);
//							}
//						}
//					} else if (attributeName.equalsIgnoreCase("Division")) {
//						if (zoneValue != null) {
//							if (zoneValue.equalsIgnoreCase(divisionRecord.getString(Path.parse("./Zone_Code")))) {
//								divisionName = divisionName != null ? divisionName : "";
//								if (!list.contains(divisionCode)) {
//									list.add(divisionCode);
//									if (divisionCode.equalsIgnoreCase(divisionName)) {
//										map.put(divisionCode, divisionName);
//									} else {
//										map.put(divisionCode, divisionCode + ", " + divisionName);
//									}
//								}
//							}
//						}
//					} else if (attributeName.equalsIgnoreCase("Shed")) {
//
//						if (zoneValue != null && divisionValue != null) {
//							String shedQuery = "Select s.\"$adaptation\" from \"/root/Shed\" s ";
//							Query<Tuple> shedQueryTuple = referenceDatasetName.createQuery(shedQuery);
//							QueryResult<Tuple> shedRecords = shedQueryTuple.getResult();
//
//							Adaptation shedRecord = null;
//							String shedCode = "";
//							String shedName = "";
//							String shedSourceSystemName = "";
//
//							for (Tuple shedResult : shedRecords) {
//								shedRecord = (Adaptation) shedResult.get(0);
//
//								shedCode = shedRecord.getString(Path.parse("./Shed_Code"));
//								shedName = shedRecord.getString(Path.parse("./Shed_Name"));
//								shedSourceSystemName = shedRecord.getString(Path.parse("./Source_System_Name"));
//								if (shedSourceSystemName != null)
//									if (shedSourceSystemName.equalsIgnoreCase("SLAM"))
//										continue;
//								if (zoneValue.equalsIgnoreCase(shedRecord.getString(Path.parse("./Zone_Code")))
//										&& divisionValue.equalsIgnoreCase(
//												shedRecord.getString(Path.parse("./Division_Code")))) {
//									shedName = shedName != null ? shedName : "";
//
//									list.add(shedCode);
//									if (shedCode.equalsIgnoreCase(shedName)) {
//										map.put(shedCode, shedName);
//									} else {
//										map.put(shedCode, shedCode + ", " + shedName);
//									}
//
//								}
//
//							}
//
//						}
//					}
//
//				}
//
//			} else if (userPermissionsShedRecords.iterator().hasNext()) {
//
//				Adaptation userPermissionsShedRecord = null;
//
//				for (Tuple shedResult : userPermissionsShedRecords) {
//					userPermissionsShedRecord = (Adaptation) shedResult.get(0);
//					String shedValue = (String) userPermissionsShedRecord.get(Path.parse("./Shed_Code"));
//
//					Adaptation shedRecord = null;
//					String shedCode = "";
//					String shedName = "";
//					String shedSourceSystemName = "";
//
//					String divisionCode = "";
//					String divisionName = "";
//					String zoneCode = "";
//					String zoneName = "";
//
//					if (shedValue != null) {
//						shedRecord = shedTable.lookupAdaptationByPrimaryKey(PrimaryKey.parseString(shedValue));
//					} else {
//						continue;
//					}
//
//					if (shedRecord != null) {
//						shedCode = shedRecord.getString(Path.parse("./Shed_Code"));
//						shedName = shedRecord.getString(Path.parse("./Shed_Name"));
//						shedSourceSystemName = shedRecord.getString(Path.parse("./Source_System_Name"));
//						if (shedSourceSystemName != null)
//							if (shedSourceSystemName.equalsIgnoreCase("SLAM"))
//								continue;
//						divisionCode = shedRecord.getString(Path.parse("./Division_Code"));
//						zoneCode = shedRecord.getString(Path.parse("./Zone_Code"));
//					} else {
//						continue;
//					}
//
//					if (attributeName.equalsIgnoreCase("Zone")) {
//						Adaptation zoneRecord = null;
//						zoneRecord = zoneTable.lookupAdaptationByPrimaryKey(PrimaryKey.parseString(zoneCode));
//						if (zoneRecord != null)
//							zoneName = zoneRecord.getString(Path.parse("./Zone_Name"));
//						else
//							continue;
//
//						zoneName = zoneName != null ? zoneName : "";
//						if (!list.contains(zoneCode)) {
//							list.add(zoneCode);
//							if (zoneCode.equalsIgnoreCase(zoneName)) {
//								map.put(zoneCode, zoneName);
//							} else {
//								map.put(zoneCode, zoneCode + ", " + zoneName);
//							}
//						}
//					} else if (attributeName.equalsIgnoreCase("Division")) {
//						if (zoneValue != null) {
//							Adaptation divisionRecord = null;
//							divisionRecord = divisionTable
//									.lookupAdaptationByPrimaryKey(PrimaryKey.parseString(divisionCode));
//							if (divisionRecord != null)
//								divisionName = divisionRecord.getString(Path.parse("./Division_Name"));
//							else
//								continue;
//							if (zoneValue.equalsIgnoreCase(divisionRecord.getString(Path.parse("./Zone_Code")))) {
//								divisionName = divisionName != null ? divisionName : "";
//								if (!list.contains(divisionCode)) {
//									list.add(divisionCode);
//									if (divisionCode.equalsIgnoreCase(divisionName)) {
//										map.put(divisionCode, divisionName);
//									} else {
//										map.put(divisionCode, divisionCode + ", " + divisionName);
//									}
//								}
//							}
//						}
//					} else if (attributeName.equalsIgnoreCase("Shed")) {
//						if (zoneValue != null && divisionValue != null) {
//							shedName = shedName != null ? shedName : "";
//							if (zoneValue.equalsIgnoreCase(zoneCode) && divisionValue.equalsIgnoreCase(divisionCode)) {
//								list.add(shedCode);
//								if (shedCode.equalsIgnoreCase(shedName)) {
//									map.put(shedValue, shedName);
//								} else {
//									map.put(shedValue, shedValue + ", " + shedName);
//								}
//							}
//						}
//					}
//				}
//
//			}
//
//		} else {
//			if (attributeName.equalsIgnoreCase("Zone")) {
//
//				String zoneQuery = "Select s.\"$adaptation\" from \"/root/Zone\" s ";
//				Query<Tuple> zoneQueryTuple = referenceDatasetName.createQuery(zoneQuery);
//				QueryResult<Tuple> zoneRecords = zoneQueryTuple.getResult();
//
//				Adaptation zoneRecord = null;
//				String zoneCode = "";
//				String zoneName = "";
//
//				for (Tuple zoneResult : zoneRecords) {
//
//					zoneRecord = (Adaptation) zoneResult.get(0);
//
//					zoneCode = zoneRecord.getString(Path.parse("./Zone_Code"));
//					zoneName = zoneRecord.getString(Path.parse("./Zone_Name"));
//
//					zoneName = zoneName != null ? zoneName : "";
//
//					list.add(zoneCode);
//					if (zoneCode.equalsIgnoreCase(zoneName)) {
//						map.put(zoneCode, zoneName);
//					} else {
//						map.put(zoneCode, zoneCode + ", " + zoneName);
//					}
//				}
//			} else if (attributeName.equalsIgnoreCase("Division")) {
//
//				if (zoneValue != null) {
//					String divisionQuery = "Select s.\"$adaptation\" from \"/root/Division\" s ";
//					Query<Tuple> divisionQueryTuple = referenceDatasetName.createQuery(divisionQuery);
//					QueryResult<Tuple> divisionRecords = divisionQueryTuple.getResult();
//
//					Adaptation divisionRecord = null;
//					String divisionCode = "";
//					String divisionName = "";
//
//					for (Tuple divisionResult : divisionRecords) {
//						divisionRecord = (Adaptation) divisionResult.get(0);
//
//						divisionCode = divisionRecord.getString(Path.parse("./Division_Code"));
//						divisionName = divisionRecord.getString(Path.parse("./Division_Name"));
//
//						if (zoneValue.equalsIgnoreCase(divisionRecord.getString(Path.parse("./Zone_Code")))) {
//							divisionName = divisionName != null ? divisionName : "";
//
//							list.add(divisionCode);
//							if (divisionCode.equalsIgnoreCase(divisionName)) {
//								map.put(divisionCode, divisionName);
//							} else {
//								map.put(divisionCode, divisionCode + ", " + divisionName);
//							}
//
//						}
//
//					}
//				}
//			} else if (attributeName.equalsIgnoreCase("Shed")) {
//
//				if (zoneValue != null && divisionValue != null) {
//					String shedQuery = "Select s.\"$adaptation\" from \"/root/Shed\" s ";
//					Query<Tuple> shedQueryTuple = referenceDatasetName.createQuery(shedQuery);
//					QueryResult<Tuple> shedRecords = shedQueryTuple.getResult();
//
//					Adaptation shedRecord = null;
//					String shedCode = "";
//					String shedName = "";
//					String shedSourceSystemName = "";
//
//					for (Tuple shedResult : shedRecords) {
//						shedRecord = (Adaptation) shedResult.get(0);
//
//						shedCode = shedRecord.getString(Path.parse("./Shed_Code"));
//						shedName = shedRecord.getString(Path.parse("./Shed_Name"));
//
//						shedSourceSystemName = shedRecord.getString(Path.parse("./Source_System_Name"));
//						if (shedSourceSystemName != null)
//							if (shedSourceSystemName.equalsIgnoreCase("SLAM"))
//								continue;
//
//						if (zoneValue.equalsIgnoreCase(shedRecord.getString(Path.parse("./Zone_Code"))) && divisionValue
//								.equalsIgnoreCase(shedRecord.getString(Path.parse("./Division_Code")))) {
//							shedName = shedName != null ? shedName : "";
//
//							list.add(shedCode);
//							if (shedCode.equalsIgnoreCase(shedName)) {
//								map.put(shedCode, shedName);
//							} else {
//								map.put(shedCode, shedCode + ", " + shedName);
//							}
//
//						}
//
//					}
//
//				}
//			}
//		}
//		return list;

	}
}
