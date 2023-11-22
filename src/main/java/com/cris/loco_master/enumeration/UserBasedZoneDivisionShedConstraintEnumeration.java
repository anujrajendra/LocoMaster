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
import com.orchestranetworks.service.Role;
import com.orchestranetworks.service.UserReference;
import com.orchestranetworks.service.directory.DirectoryHandler;

public class UserBasedZoneDivisionShedConstraintEnumeration implements ConstraintEnumeration<String> {

	String attributeName;
	HashMap<String, String> valueMap = new HashMap<String, String>();

	@Override
	public String displayOccurrence(String code, ValueContext arg1, Locale arg2) throws InvalidSchemaException {
		return valueMap.get(code);
	}

	@Override
	public List<String> getValues(ValueContext context) throws InvalidSchemaException {

		// LoggingCategory.getWorkflow().debug(attributeName);

		Repository repository = Repository.getDefault();

		DirectoryHandler directoryHandler = DirectoryHandler.getInstance(repository);

		String userId = (String) context
				.getValue(Path.PARENT.add(Paths._Root_Locomotive._Root_Locomotive_Audit_Info_Logged_In_User));

		if (userId == null)
			return new ArrayList<String>();

		// LoggingCategory.getWorkflow().debug("Get Values User ID: " + userId);

		UserReference userReference = UserReference.forUser(userId);

		String zoneValue = (String) context
				.getValue(Path.PARENT.add(Paths._Root_Locomotive._Root_Locomotive_Loco_Owning_Zone));
		// LoggingCategory.getWorkflow().debug(zoneValue);

		String divisionValue = (String) context
				.getValue(Path.PARENT.add(Paths._Root_Locomotive._Root_Locomotive_Loco_Owning_Division));
		// LoggingCategory.getWorkflow().debug(divisionValue);

		boolean isRBUser = directoryHandler.isUserInRole(userReference, Role.forSpecificRole("RB_DS"))
				|| directoryHandler.isUserInRole(userReference, Role.forSpecificRole("RB_DAA"));
		// LoggingCategory.getWorkflow().debug("Is RB User: " + isRBUser);

		boolean isPUUser = directoryHandler.isUserInRole(userReference, Role.forSpecificRole("PU_DAA"))
				|| directoryHandler.isUserInRole(userReference, Role.forSpecificRole("PU_DS"));
		// LoggingCategory.getWorkflow().debug("Is PU User: " + isPUUser);

		boolean isAdminUser = directoryHandler.isUserInRole(userReference, Role.ADMINISTRATOR);
		// LoggingCategory.getWorkflow().debug("Is Administrator: " + isAdminUser);

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
		// LoggingCategory.getWorkflow().debug("Is Zonal Admin: " + isZonalAdmin);

		boolean isShedUser = directoryHandler.isUserInRole(userReference, Role.forSpecificRole("Shed_DAA"))
				|| directoryHandler.isUserInRole(userReference, Role.forSpecificRole("Shed_DS"));
		// LoggingCategory.getWorkflow().debug("Is Shed User: " + isShedUser);

		if (isRBUser || isPUUser || isAdminUser) {
			// LoggingCategory.getWorkflow().debug("RB User, PU User, Admin User");
			if (this.attributeName.equalsIgnoreCase("Zone"))
				valueMap = DropDownUtils.getAllZoneFromRef();
			else if (this.attributeName.equalsIgnoreCase("Division"))
				valueMap = DropDownUtils.getDivisionForZoneFromRef(zoneValue);
			else if (this.attributeName.equalsIgnoreCase("Shed"))
				valueMap = DropDownUtils.getShedForDivisionFromRef(zoneValue, divisionValue);

		} else if (isZonalAdmin) {
			// LoggingCategory.getWorkflow().debug("Zonal Admin");
			if (this.attributeName.equalsIgnoreCase("Zone"))
				valueMap = DropDownUtils.getAllZoneFromRef();
			else if (this.attributeName.equalsIgnoreCase("Division"))
				valueMap = DropDownUtils.getDivisionsForZoneFromUR(userId, zoneValue);
			else if (this.attributeName.equalsIgnoreCase("Shed"))
				valueMap = DropDownUtils.getShedForDivisionFromUR(userId, zoneValue, divisionValue);

		} else if (isShedUser) {
			// LoggingCategory.getWorkflow().debug("Shed User");
			if (this.attributeName.equalsIgnoreCase("Zone"))
				valueMap = DropDownUtils.getZonesForUser(userId);
			else if (this.attributeName.equalsIgnoreCase("Division"))
				valueMap = DropDownUtils.getDivisionsForZoneFromUR(userId, zoneValue);
			else if (this.attributeName.equalsIgnoreCase("Shed"))
				valueMap = DropDownUtils.getShedForDivisionFromUR(userId, zoneValue, divisionValue);
		}

		ArrayList<String> codeList = new ArrayList<String>(valueMap.keySet());

		// LoggingCategory.getWorkflow()
		// .info("_______________________________________________________________________________________");
		return codeList;
	}

	@Override
	public void checkOccurrence(String key, ValueContextForValidation contextValidation) throws InvalidSchemaException {
//		if (valueMap.get(key) == null) {
//			contextValidation.addError("Invalid Reference Value - " + attributeName);
//		}

	}

	@Override
	public void setup(ConstraintContext arg0) {

	}

	@Override
	public String toUserDocumentation(Locale arg0, ValueContext arg1) throws InvalidSchemaException {
		return null;
	}

	public String getAttributeName() {
		return attributeName;
	}

	public void setAttributeName(String attributeName) {
		this.attributeName = attributeName;
	}
}