package com.cris.loco_master.constant;

import com.cris.loco_master.utils.RepositoryUtils;
import com.onwbp.adaptation.Adaptation;
import com.onwbp.adaptation.AdaptationHome;
import com.onwbp.adaptation.AdaptationTable;
import com.orchestranetworks.schema.Path;

public class Constants {

	// REFERENCE DATASPACE, DATASET and TABLE
	final public static String DATASPACE_REFERENCE_NAME = "reference_data";
	final public static AdaptationHome DATASPACE_REFERENCE = RepositoryUtils.getDataspace(DATASPACE_REFERENCE_NAME);

	final public static String DATASET_REFERENCE_NAME = "reference_data";
	final public static Adaptation DATASET_REFERENCE = RepositoryUtils.getDataset(DATASPACE_REFERENCE,
			DATASET_REFERENCE_NAME);

	final public static String TABLE_ZONE_REFERENCE_NAME = "/root/Zone";
	final public static AdaptationTable TABLE_ZONE_REFERENCE = DATASET_REFERENCE
			.getTable(Path.parse(TABLE_ZONE_REFERENCE_NAME));

	final public static String TABLE_DIVISION_REFERENCE_NAME = "/root/Division";
	final public static AdaptationTable TABLE_DIVISION_REFERENCE = DATASET_REFERENCE
			.getTable(Path.parse(TABLE_DIVISION_REFERENCE_NAME));

	final public static String TABLE_SHED_REFERENCE_NAME = "/root/Shed";
	final public static AdaptationTable TABLE_SHED_REFERENCE = DATASET_REFERENCE
			.getTable(Path.parse(TABLE_SHED_REFERENCE_NAME));

	// USERREGISTRATION DATASPACE, DATASET and TABLE
	final public static String DATASPACE_USERREGISTRATION_NAME = "user_data";
	final public static AdaptationHome DATASPACE_USERREGISTRATION = RepositoryUtils
			.getDataspace(DATASPACE_USERREGISTRATION_NAME);

	final public static String DATASET_USERREGISTRATION_NAME = "user_data";
	final public static Adaptation DATASET_USERREGISTRATION = RepositoryUtils.getDataset(DATASPACE_USERREGISTRATION,
			DATASET_USERREGISTRATION_NAME);

	final public static String TABLE_USERREGISTRATION_NAME = "/root/User_Registration_Details";
	final public static AdaptationTable TABLE_USERREGISTRATION = DATASET_USERREGISTRATION
			.getTable(Path.parse(TABLE_USERREGISTRATION_NAME));

	final public static String TABLE_USERPERMISSION_ZONE_NAME = "/root/User_Permissions_Zone";
	final public static AdaptationTable TABLE_USERPERMISSION_ZONE = DATASET_USERREGISTRATION
			.getTable(Path.parse(TABLE_USERPERMISSION_ZONE_NAME));

	final public static String TABLE_USERPERMISSION_DIVISION_NAME = "/root/User_Permissions_Division";
	final public static AdaptationTable TABLE_USERPERMISSION_DIVISION = DATASET_USERREGISTRATION
			.getTable(Path.parse(TABLE_USERPERMISSION_DIVISION_NAME));

	final public static String TABLE_USERPERMISSION_SHED_NAME = "/root/User_Permissions_Shed";
	final public static AdaptationTable TABLE_USERPERMISSION_SHED = DATASET_USERREGISTRATION
			.getTable(Path.parse(TABLE_USERPERMISSION_SHED_NAME));
}