package com.cris.loco_master.utils;

import java.util.HashMap;

import com.cris.loco_master.constant.Constants;
import com.onwbp.adaptation.Adaptation;
import com.onwbp.adaptation.PrimaryKey;
import com.orchestranetworks.query.Query;
import com.orchestranetworks.query.QueryResult;
import com.orchestranetworks.query.Tuple;
import com.orchestranetworks.schema.Path;

public class DropDownUtils {

	public static HashMap<String, String> getAllZoneFromRef() {

		HashMap<String, String> zoneMap = new HashMap<String, String>();

		String zoneQuery = "SELECT t.\"$adaptation\" FROM \"" + Constants.TABLE_ZONE_REFERENCE_NAME + "\" t";
		Query<Tuple> zoneQueryTuple = Constants.DATASET_REFERENCE.createQuery(zoneQuery);
		QueryResult<Tuple> zoneRecords = zoneQueryTuple.getResult();

		for (Tuple zoneTuple : zoneRecords) {

			Adaptation zoneRecord = (Adaptation) zoneTuple.get(0);

			String zoneCode = zoneRecord.getString(Path.parse("./Zone_Code"));
			String zoneName = zoneRecord.getString(Path.parse("./Zone_Name"));

			zoneName = zoneName != null ? zoneName : "";

			if (zoneCode.equalsIgnoreCase(zoneName)) {
				zoneMap.put(zoneCode, zoneName);
			} else {
				zoneMap.put(zoneCode, zoneCode + ", " + zoneName);
			}
		}
		return zoneMap;
	}

	public static HashMap<String, String> getZonesForUser(String userId) {

		HashMap<String, String> zoneMap = new HashMap<String, String>();

		String userPermissionsZoneQuery = "SELECT s.\"$adaptation\" FROM \"/root/User_Permissions_Zone\" s "
				+ "WHERE FK_AS_STRING('user_data','/root/User_Registration_Details', s.User_Id) = '" + userId + "'";

		Query<Tuple> userPermissionsZoneQueryTuple = Constants.DATASET_USERREGISTRATION
				.createQuery(userPermissionsZoneQuery);

		QueryResult<Tuple> userPermissionsZoneRecords = userPermissionsZoneQueryTuple.getResult();

		for (Tuple zoneResult : userPermissionsZoneRecords) {
			Adaptation userPermissionsZoneRecord = (Adaptation) zoneResult.get(0);
			String zoneCode = (String) userPermissionsZoneRecord.get(Path.parse("./Zone_Code"));

			Adaptation zoneRecord = Constants.TABLE_ZONE_REFERENCE
					.lookupAdaptationByPrimaryKey(PrimaryKey.parseString(zoneCode));
			String zoneName = zoneRecord.getString(Path.parse("./Zone_Name"));
			zoneName = zoneName != null ? zoneName : "";

			if (zoneCode.equalsIgnoreCase(zoneName)) {
				zoneMap.put(zoneCode, zoneName);
			} else {
				zoneMap.put(zoneCode, zoneCode + ", " + zoneName);
			}
		}

		return zoneMap;
	}

	public static HashMap<String, String> getDivisionForZoneFromRef(String zone) {

		HashMap<String, String> divisonMap = new HashMap<String, String>();

		if (zone != null) {

			String divisionQuery = "SELECT t.\"$adaptation\" FROM \"" + Constants.TABLE_DIVISION_REFERENCE_NAME
					+ "\" t " + "WHERE FK_AS_STRING('" + Constants.DATASET_REFERENCE_NAME + "','"
					+ Constants.TABLE_ZONE_REFERENCE_NAME + "', t.Zone_Code) = ?";

			Query<Tuple> divisionQueryTuple = Constants.DATASET_REFERENCE.createQuery(divisionQuery);

			divisionQueryTuple.setParameter(0, zone);

			QueryResult<Tuple> divisionRecords = divisionQueryTuple.getResult();

			for (Tuple divisionResult : divisionRecords) {
				Adaptation divisionRecord = (Adaptation) divisionResult.get(0);

				String divisionCode = divisionRecord.getString(Path.parse("./Division_Code"));
				String divisionName = divisionRecord.getString(Path.parse("./Division_Name"));

				divisionName = divisionName != null ? divisionName : "";

				if (divisionCode.equalsIgnoreCase(divisionName)) {
					divisonMap.put(divisionCode, divisionName);
				} else {
					divisonMap.put(divisionCode, divisionCode + ", " + divisionName);
				}

			}
		}
		return divisonMap;
	}

	public static HashMap<String, String> getDivisionsForZoneFromUR(String userId, String zone) {

		HashMap<String, String> divisonMap = new HashMap<String, String>();

		String userPermissionsDivisionQuery = "SELECT s.\"$adaptation\" FROM \"/root/User_Permissions_Division\" s "
				+ "WHERE FK_AS_STRING('user_data','/root/User_Registration_Details', s.User_Id) = '" + userId + "'";

		Query<Tuple> userPermissionsDivisionQueryTuple = Constants.DATASET_USERREGISTRATION
				.createQuery(userPermissionsDivisionQuery);

		QueryResult<Tuple> userPermissionsDivisionRecords = userPermissionsDivisionQueryTuple.getResult();

		for (Tuple divisionResult : userPermissionsDivisionRecords) {

			Adaptation userPermissionsDivisionRecord = (Adaptation) divisionResult.get(0);
			String divisionCode = (String) userPermissionsDivisionRecord.get(Path.parse("./Division_Code"));

			Adaptation divisionRecord = Constants.TABLE_DIVISION_REFERENCE
					.lookupAdaptationByPrimaryKey(PrimaryKey.parseString(divisionCode));

			String divisionName = divisionRecord.getString(Path.parse("./Division_Name"));

			if (zone.equalsIgnoreCase(divisionRecord.getString(Path.parse("./Zone_Code")))) {
				divisionName = divisionName != null ? divisionName : "";

				if (divisionCode.equalsIgnoreCase(divisionName)) {
					divisonMap.put(divisionCode, divisionName);
				} else {
					divisonMap.put(divisionCode, divisionCode + ", " + divisionName);
				}
			}
		}
		return divisonMap;
	}

	public static HashMap<String, String> getShedForDivisionFromRef(String zone, String division) {

		HashMap<String, String> shedMap = new HashMap<String, String>();

		if (zone != null && division != null) {

			String shedQuery = "SELECT s.\"$adaptation\" FROM \"" + Constants.TABLE_SHED_REFERENCE_NAME
					+ "\" s WHERE FK_AS_STRING('" + Constants.DATASET_REFERENCE_NAME + "', '"
					+ Constants.TABLE_ZONE_REFERENCE_NAME + "', Zone_Code) = ? AND FK_AS_STRING('"
					+ Constants.DATASET_REFERENCE_NAME + "', '" + Constants.TABLE_DIVISION_REFERENCE_NAME
					+ "', Division_Code) = ?";

			Query<Tuple> shedQueryTuple = Constants.DATASET_REFERENCE.createQuery(shedQuery);
			shedQueryTuple.setParameter(0, zone);
			shedQueryTuple.setParameter(1, division);

			QueryResult<Tuple> shedRecords = shedQueryTuple.getResult();

			for (Tuple shedResult : shedRecords) {
				Adaptation shedRecord = (Adaptation) shedResult.get(0);

				String shedCode = shedRecord.getString(Path.parse("./Shed_Code"));
				String shedName = shedRecord.getString(Path.parse("./Shed_Name"));
				String shedSourceSystemName = shedRecord.getString(Path.parse("./Source_System_Name"));

				if (shedSourceSystemName != null)
					if (shedSourceSystemName.equalsIgnoreCase("SLAM"))
						continue;

				shedName = shedName != null ? shedName : "";

				if (shedCode.equalsIgnoreCase(shedName)) {
					shedMap.put(shedCode, shedName);
				} else {
					shedMap.put(shedCode, shedCode + ", " + shedName);
				}
			}
		}
		return shedMap;
	}

	public static HashMap<String, String> getShedForDivisionFromUR(String userId, String zone, String division) {

		HashMap<String, String> shedMap = new HashMap<String, String>();

		String userPermissionsShedQuery = "SELECT s.\"$adaptation\" FROM \"/root/User_Permissions_Shed\" s "
				+ "WHERE FK_AS_STRING('user_data','/root/User_Registration_Details', s.User_Id) = '" + userId + "'";

		Query<Tuple> userPermissionsShedQueryTuple = Constants.DATASET_USERREGISTRATION
				.createQuery(userPermissionsShedQuery);

		QueryResult<Tuple> userPermissionsShedRecords = userPermissionsShedQueryTuple.getResult();

		for (Tuple shedResult : userPermissionsShedRecords) {

			Adaptation userPermissionsShedRecord = (Adaptation) shedResult.get(0);
			String shedValue = (String) userPermissionsShedRecord.get(Path.parse("./Shed_Code"));

			Adaptation shedRecord = Constants.TABLE_SHED_REFERENCE
					.lookupAdaptationByPrimaryKey(PrimaryKey.parseString(shedValue));

			if (shedRecord != null) {

				String shedCode = shedRecord.getString(Path.parse("./Shed_Code"));
				String shedName = shedRecord.getString(Path.parse("./Shed_Name"));
				String shedSourceSystemName = shedRecord.getString(Path.parse("./Source_System_Name"));

				if (shedSourceSystemName != null && shedSourceSystemName.equalsIgnoreCase("SLAM"))
					continue;

				String divisionCode = shedRecord.getString(Path.parse("./Division_Code"));
				String zoneCode = shedRecord.getString(Path.parse("./Zone_Code"));

				if (zone != null && division != null) {
					shedName = shedName != null ? shedName : "";
					if (zone.equalsIgnoreCase(zoneCode) && division.equalsIgnoreCase(divisionCode)) {
						if (shedCode.equalsIgnoreCase(shedName)) {
							shedMap.put(shedValue, shedName);
						} else {
							shedMap.put(shedValue, shedValue + ", " + shedName);
						}
					}
				}
			}
		}
		return shedMap;
	}
}