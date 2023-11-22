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

public class ShedConstraintEnumeration implements ConstraintEnumeration<String> {

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
		String divisionValue = (String) context.getValue(Path.parse("../Division"));
		String oldShed = (String) context.getValue(Path.parse("../Old_Shed"));

		Repository repository = Repository.getDefault();
		final HomeKey referenceDataSpaceKey = HomeKey.forBranchName("reference_data");
		final AdaptationHome referenceDataspaceName = repository.lookupHome(referenceDataSpaceKey);

		final AdaptationName referenceDataSetKey = AdaptationName.forName("reference_data");
		final Adaptation referenceDatasetName = referenceDataspaceName.findAdaptationOrNull(referenceDataSetKey);

		List<String> list = new ArrayList<String>();

		if (zoneValue != null && divisionValue != null) {
			String shedQuery = "Select s.\"$adaptation\" from \"/root/Shed\" s ";
			Query<Tuple> shedQueryTuple = referenceDatasetName.createQuery(shedQuery);
			QueryResult<Tuple> shedRecords = shedQueryTuple.getResult();

			Adaptation shedRecord = null;
			String shedCode = "";
			String shedName = "";

			for (Tuple shedResult : shedRecords) {
				shedRecord = (Adaptation) shedResult.get(0);

				shedCode = shedRecord.getString(Path.parse("./Shed_Code"));
				shedName = shedRecord.getString(Path.parse("./Shed_Name"));

				if (zoneValue.equalsIgnoreCase(shedRecord.getString(Path.parse("./Zone_Code")))
						&& divisionValue.equalsIgnoreCase(shedRecord.getString(Path.parse("./Division_Code")))
						&& (!oldShed.equalsIgnoreCase(shedCode))) {
					shedName = shedName != null ? shedName : "";

					list.add(shedCode);
					if (shedCode.equalsIgnoreCase(shedName)) {
						map.put(shedCode, shedName);
					} else {
						map.put(shedCode, shedCode + ", " + shedName);
					}

				}

			}

		}
		return list;

	}

}
