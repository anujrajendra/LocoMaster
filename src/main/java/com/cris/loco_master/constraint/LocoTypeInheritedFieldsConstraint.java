package com.cris.loco_master.constraint;

import java.util.Locale;

import com.onwbp.adaptation.Adaptation;
import com.onwbp.adaptation.AdaptationHome;
import com.onwbp.adaptation.AdaptationName;
import com.onwbp.adaptation.AdaptationTable;
import com.onwbp.adaptation.PrimaryKey;
import com.orchestranetworks.instance.HomeKey;
import com.orchestranetworks.instance.Repository;
import com.orchestranetworks.instance.ValueContext;
import com.orchestranetworks.instance.ValueContextForValidation;
import com.orchestranetworks.schema.Constraint;
import com.orchestranetworks.schema.ConstraintContext;
import com.orchestranetworks.schema.InvalidSchemaException;
import com.orchestranetworks.schema.Path;

public class LocoTypeInheritedFieldsConstraint implements Constraint<String> {

	String attributeName;

	public String getAttributeName() {
		return attributeName;
	}

	public void setAttributeName(String attributeName) {
		this.attributeName = attributeName;
	}

	@Override
	public void checkOccurrence(String arg0, ValueContextForValidation context) throws InvalidSchemaException {
		// TODO Auto-generated method stub
		String locoType = (String) context.getValue(Path.parse("../Loco_Type"));

		if (locoType != null && arg0 != null) {

			Repository repository = Repository.getDefault();
			final HomeKey locoDataSpaceKey = HomeKey.forBranchName("loco_data");
			final AdaptationHome locoDataspaceName = repository.lookupHome(locoDataSpaceKey);

			final AdaptationName locoDataSetKey = AdaptationName.forName("loco_data");
			final Adaptation locoDatasetName = locoDataspaceName.findAdaptationOrNull(locoDataSetKey);

			AdaptationTable locoTypeTable = locoDatasetName.getTable(Path.parse("/root/Loco_Type"));

			Adaptation locoTypeRecord = locoTypeTable.lookupAdaptationByPrimaryKey(PrimaryKey.parseString(locoType));
			if (locoTypeRecord != null) {
				String locoTypeField = (String) locoTypeRecord.get(Path.parse("./" + attributeName));
				if (!arg0.equalsIgnoreCase(locoTypeField)) {
					context.addError("Invalid Reference Value - " + attributeName);
				}

			}
		}
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
}
