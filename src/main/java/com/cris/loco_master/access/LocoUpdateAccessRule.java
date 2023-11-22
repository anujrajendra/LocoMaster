package com.cris.loco_master.access;

import com.onwbp.adaptation.Adaptation;
import com.orchestranetworks.schema.SchemaNode;
import com.orchestranetworks.service.AccessPermission;
import com.orchestranetworks.service.AccessRule;
import com.orchestranetworks.service.Session;

public class LocoUpdateAccessRule implements AccessRule {

	@Override
	public AccessPermission getPermission(Adaptation adaptation, Session session, SchemaNode node) {

		if (adaptation.isSchemaInstance()) {
			return AccessPermission.getReadWrite();
		}
		if (session.isInWorkflowInteraction(true)) {
			if (session.getTrackingInfo() != null)
				if (session.getTrackingInfo().equalsIgnoreCase("Loco_Update"))
					return AccessPermission.getReadOnly();
				else if (session.getTrackingInfo().equalsIgnoreCase("Loco_Condemn"))
					return AccessPermission.getReadOnly();
		}
		return AccessPermission.getReadWrite();
	}
}
