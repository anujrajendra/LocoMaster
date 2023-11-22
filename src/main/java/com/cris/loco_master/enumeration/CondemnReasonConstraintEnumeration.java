package com.cris.loco_master.enumeration;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.orchestranetworks.instance.ValueContext;
import com.orchestranetworks.instance.ValueContextForValidation;
import com.orchestranetworks.schema.ConstraintContext;
import com.orchestranetworks.schema.ConstraintEnumeration;
import com.orchestranetworks.schema.InvalidSchemaException;
import com.orchestranetworks.schema.Path;

public class CondemnReasonConstraintEnumeration implements ConstraintEnumeration<String> {

	@Override
	public void checkOccurrence(String arg0, ValueContextForValidation arg1) throws InvalidSchemaException {
		// TODO Auto-generated method stub

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
	public String displayOccurrence(String list, ValueContext arg1, Locale arg2) throws InvalidSchemaException {
		// TODO Auto-generated method stub
		return list;
	}

	@Override
	public List<String> getValues(ValueContext context) throws InvalidSchemaException {
		// TODO Auto-generated method stub
		String condemnProposalType = (String) context.getValue(Path.parse("../Condemnation_Proposal_Type"));
		String locoType = (String) context.getValue(Path.parse("../Loco_Type"));

		List<String> list = new ArrayList<String>();
		if (condemnProposalType != null && locoType != null) {
			if (condemnProposalType.equalsIgnoreCase("Premature")) {
				list.add("Accidental Loco");
				list.add("Non Economical Repair Locos observed during schedule inspection/ POH shops");
			} else {
				// if (locoType.equalsIgnoreCase("Diesel")) {
				list.add("Completed codal life 36 Years (for Diesel Locos)");
				list.add("Completed codal life of 30 Years (for diesel Locos)");
				list.add("Surplus ALCO make diesel Locos Completed Above 15 years of age");
				list.add("IRFC funded locos with Lease fore clousre after approval Of RB");
				// } else {
				list.add("Completed codal Life of 35 Years (For Electrical Locos)");
				list.add("IRFC funded locos with Lease fore clousre after approval Of RB");
				// }
			}
		}
		return list;
	}

}
