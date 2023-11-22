package com.cris.loco_master.service.workflow.transfer;

import com.orchestranetworks.schema.Path;
import com.orchestranetworks.userservice.ObjectKey;

public interface TransferServicePaths {

	// Identifies an object from a given context
	ObjectKey _objectKey = ObjectKey.forName("loco_transfer");

	// Path for the Zone field.
	Path _loco_number = Path.parse("Loco_Number");
	Path _loco_type = Path.parse("Loco_Type");
	Path _loco_traction = Path.parse("Loco_Traction");
	Path _old_zone = Path.parse("Old_Zone");
	Path _old_division = Path.parse("Old_Division");
	Path _old_shed = Path.parse("Old_Shed");

	Path _zone = Path.parse("Zone");
	Path _division = Path.parse("Division");
	Path _shed = Path.parse("Shed");
	Path _shed_of_record = Path.parse("Shed_Of_Current_Record");

	Path _transfer_date = Path.parse("Transfer_Date");

	Path _reference_document_number = Path.parse("DocumentNumber");
	Path _reference_document_date = Path.parse("DocumentDate");
	Path _reference_document_attachement = Path.parse("DocumentAttachment");
}