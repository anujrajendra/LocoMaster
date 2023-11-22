package com.cris.loco_master.enumeration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import com.onwbp.adaptation.Adaptation;
import com.onwbp.adaptation.AdaptationHome;
import com.onwbp.adaptation.AdaptationName;
import com.orchestranetworks.instance.HomeKey;
import com.orchestranetworks.instance.Repository;
import com.orchestranetworks.instance.ValueContext;
import com.orchestranetworks.instance.ValueContextForValidation;
import com.orchestranetworks.query.Query;
import com.orchestranetworks.query.QueryResult;
import com.orchestranetworks.query.Tuple;
import com.orchestranetworks.schema.ConstraintContext;
import com.orchestranetworks.schema.ConstraintEnumeration;
import com.orchestranetworks.schema.InvalidSchemaException;
import com.orchestranetworks.schema.Path;

public class DivisionConstraintEnumeration implements ConstraintEnumeration<String> {

	HashMap<String, String> map = new HashMap<String, String>();

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
	public String displayOccurrence(String code, ValueContext arg1, Locale arg2) throws InvalidSchemaException {
		// TODO Auto-generated method stub
		return map.get(code);
	}

	@Override
	public List<String> getValues(ValueContext context) throws InvalidSchemaException {
		// TODO Auto-generated method stub
		String zoneValue = (String) context.getValue(Path.parse("../Zone"));

		Repository repository = Repository.getDefault();
		final HomeKey referenceDataSpaceKey = HomeKey.forBranchName("reference_data");
		final AdaptationHome referenceDataspaceName = repository.lookupHome(referenceDataSpaceKey);

		final AdaptationName referenceDataSetKey = AdaptationName.forName("reference_data");
		final Adaptation referenceDatasetName = referenceDataspaceName.findAdaptationOrNull(referenceDataSetKey);

		List<String> list = new ArrayList<String>();

		if (zoneValue != null) {
			String divisionQuery = "Select s.\"$adaptation\" from \"/root/Division\" s ";
			Query<Tuple> divisionQueryTuple = referenceDatasetName.createQuery(divisionQuery);
			QueryResult<Tuple> divisionRecords = divisionQueryTuple.getResult();

			Adaptation divisionRecord = null;
			String divisionCode = "";
			String divisionName = "";

			for (Tuple divisionResult : divisionRecords) {
				divisionRecord = (Adaptation) divisionResult.get(0);

				divisionCode = divisionRecord.getString(Path.parse("./Division_Code"));
				divisionName = divisionRecord.getString(Path.parse("./Division_Name"));

				if (zoneValue.equalsIgnoreCase(divisionRecord.getString(Path.parse("./Zone_Code")))) {
					divisionName = divisionName != null ? divisionName : "";

					list.add(divisionCode);
					if (divisionCode.equalsIgnoreCase(divisionName)) {
						map.put(divisionCode, divisionName);
					} else {
						map.put(divisionCode, divisionCode + ", " + divisionName);
					}

				}

			}
		}
		return list;

	}

}
