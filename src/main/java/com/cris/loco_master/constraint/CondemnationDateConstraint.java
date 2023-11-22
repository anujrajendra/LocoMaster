package com.cris.loco_master.constraint;

import java.time.Duration;
import java.time.LocalDate;
import java.util.Date;
import java.util.Locale;

import com.orchestranetworks.instance.ValueContext;
import com.orchestranetworks.instance.ValueContextForValidation;
import com.orchestranetworks.schema.Constraint;
import com.orchestranetworks.schema.ConstraintContext;
import com.orchestranetworks.schema.InvalidSchemaException;
import com.orchestranetworks.schema.Path;

public class CondemnationDateConstraint implements Constraint<Date> {

	@Override
	public void checkOccurrence(Date condemnationDate, ValueContextForValidation context)
			throws InvalidSchemaException {
		Date commissioningDate = (Date) context.getValue(Path.parse("../Commissioning_Date"));
		String condemnProposalType = (String) context.getValue(Path.parse("../Condemnation_Proposal_Type"));
		// String locoType = (String) context.getValue(Path.parse("../Loco_Type"));
		String condemnationReason = (String) context.getValue(Path.parse("../Condemnation_Reason"));

//		LocalDate.of(condemnationDate.getYear(), condemnationDate.getMonth(), condemnationDate.getDay());
//		LocalDate.of(commissioningDate.getYear(), commissioningDate.getMonth(), commissioningDate.getDay());

//		System.out.println("===Duration Difference===" + (Duration.between(
//				(LocalDate.of(condemnationDate.getYear(), condemnationDate.getMonth(), condemnationDate.getDay()))
//						.atStartOfDay(),
//				(LocalDate.of(commissioningDate.getYear(), commissioningDate.getMonth(), commissioningDate.getDay()))
//						.atStartOfDay())
//				.toDays()) / 365);

		if (condemnationDate != null && condemnProposalType != null && condemnationReason != null) {
			long difference_in_years = 0;

			if (condemnProposalType.equalsIgnoreCase("Mature")) {
				// long difference_in_time = condemnationDate.getTime() -
				// commissioningDate.getTime();
				// difference_in_years = (difference_in_time / (10001 * 60 * 60 * 24 * 365));

				difference_in_years = (Duration.between(
						(LocalDate.of(commissioningDate.getYear(), commissioningDate.getMonth(),
								commissioningDate.getDay())).atStartOfDay(),
						(LocalDate.of(condemnationDate.getYear(), condemnationDate.getMonth(),
								condemnationDate.getDay())).atStartOfDay())
						.toDays()) / 365;

				if (commissioningDate.after(condemnationDate))
					context.addError(
							"Cannot be condemned, Loco Date of Commissiong should be less then the Date of Condemantion");
				else {
					if (condemnationReason.contains(" 36 Years ")) {

						if (difference_in_years < 36)
							context.addError(
									"Cannot be condemned, doesnot satisfy the condition of 36 years completion");
					} else if (condemnationReason.contains(" 30 Years ")) {

						if (difference_in_years < 30)
							context.addError(
									"Cannot be condemned, doesnot satisfy the condition of 30 years completion");
					} else if (condemnationReason.contains(" 35 Years ")) {

						if (difference_in_years < 35)
							context.addError(
									"Cannot be condemned, doesnot satisfy the condition of 35 years completion");
					} else if (condemnationReason.contains(" Surplus ALCO ")) {

						if (difference_in_years < 15)
							context.addError(
									"Cannot be condemned, doesnot satisfy the condition of 15 years completion");
					} else if (condemnationReason.contains(" IRFC funded ")) {

						if (difference_in_years < 0)
							context.addError(
									"Cannot be condemned, doesnot satisfy the condition of 0 years completion");
					}
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
