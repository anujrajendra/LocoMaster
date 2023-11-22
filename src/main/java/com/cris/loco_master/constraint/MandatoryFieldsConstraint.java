package com.cris.loco_master.constraint;

import java.util.Date;
import java.util.Locale;

import com.cris.loco_master.Paths;
import com.orchestranetworks.instance.Repository;
import com.orchestranetworks.instance.ValueContext;
import com.orchestranetworks.instance.ValueContextForValidation;
import com.orchestranetworks.schema.Constraint;
import com.orchestranetworks.schema.ConstraintContext;
import com.orchestranetworks.schema.ConstraintOnNull;
import com.orchestranetworks.schema.InvalidSchemaException;
import com.orchestranetworks.schema.Path;
import com.orchestranetworks.service.Role;
import com.orchestranetworks.service.UserReference;
import com.orchestranetworks.service.directory.DirectoryHandler;

public class MandatoryFieldsConstraint implements ConstraintOnNull, Constraint<Object> {

	String attributeName;

	public String getAttributeName() {
		return attributeName;
	}

	public void setAttributeName(String attributeName) {
		this.attributeName = attributeName;
	}

	@Override
	public void checkOccurrence(Object date, ValueContextForValidation context) throws InvalidSchemaException {
		// TODO Auto-generated method stub

		if (date != null) {
			if (attributeName.equalsIgnoreCase("Loco Manufacturing Date")) {
				Date commissioningDate = (Date) context.getValue(Path.parse("../Loco_Commissioning_Date"));
				if (commissioningDate != null)
					if (commissioningDate.after((Date) date))
						context.addError("Date of Manufacturing should be less than Date of Commissioning");
			} else if (attributeName.equalsIgnoreCase("Loco Receiving Date")) {
				Date commissioningDate = (Date) context.getValue(Path.parse("../Loco_Commissioning_Date"));
				if (commissioningDate != null)
					if (commissioningDate.before((Date) date))
						context.addError("Date of Receiving should be greater than Date of Commissioning");
			}
		}

//		Date commissioningDate = (Date) context.getValue(Path.parse("../Loco_Commissioning_Date"));
//		if (commissioningDate != null && date != null) {
//			if (attributeName.equalsIgnoreCase("Loco Manufacturing Date")) {
//				if (commissioningDate.before((Date) date))
//					context.addError("Date of Manufacturing should be less than Date of Commissioning");
//			} else if (attributeName.equalsIgnoreCase("Loco Receiving Date")) {
//				if (commissioningDate.after((Date) date))
//					context.addError("Date of Receiving should be greater than Date of Commissioning");
//			}
//		}

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
	public void checkNull(ValueContextForValidation context) throws InvalidSchemaException {
		// TODO Auto-generated method stub
		String userId = (String) context
				.getValue(Path.PARENT.add(Paths._Root_Locomotive._Root_Locomotive_Audit_Info_Logged_In_User));
		UserReference userReference = UserReference.forUser(userId);

		if ((DirectoryHandler.getInstance(Repository.getDefault()).isUserInRole(userReference,
				Role.forSpecificRole("Shed_DS")))
				|| (DirectoryHandler.getInstance(Repository.getDefault()).isUserInRole(userReference,
						Role.forSpecificRole("Shed_DAA"))))
			context.addError("Field \'" + attributeName + "\' is mandatory");
	}

}
