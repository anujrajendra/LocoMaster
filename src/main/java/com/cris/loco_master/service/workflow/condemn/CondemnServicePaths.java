package com.cris.loco_master.service.workflow.condemn;

import com.orchestranetworks.schema.Path;
import com.orchestranetworks.userservice.ObjectKey;

public interface CondemnServicePaths {

	// Identifies an object from a given context
	ObjectKey _objectKey = ObjectKey.forName("loco_condemn");

	Path _loco_number = Path.parse("Loco_Number");
	Path _zone = Path.parse("Zone");
	Path _division = Path.parse("Division");
	Path _shed = Path.parse("Shed");
	Path _loco_traction = Path.parse("Loco_Traction");

	Path _commissioning_date = Path.parse("Commissioning_Date");
	Path _loco_type = Path.parse("Loco_Type");

	Path _condemnation_date = Path.parse("Condemnation_Date");
	Path _condemnation_proposal_type = Path.parse("Condemnation_Proposal_Type");
	Path _condemnation_reason = Path.parse("Condemnation_Reason");

	Path _reference_document_number = Path.parse("DocumentNumber");
	Path _reference_document_date = Path.parse("DocumentDate");
	Path _reference_document_attachement = Path.parse("DocumentAttachment");
}