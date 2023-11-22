package com.cris.loco_master.workflow.usertask;

import com.onwbp.adaptation.Adaptation;
import com.onwbp.adaptation.AdaptationHome;
import com.onwbp.adaptation.AdaptationName;
import com.onwbp.adaptation.AdaptationTable;
import com.onwbp.adaptation.PrimaryKey;
import com.onwbp.base.text.UserMessage;
import com.orchestranetworks.instance.HomeKey;
import com.orchestranetworks.instance.Repository;
import com.orchestranetworks.schema.Path;
import com.orchestranetworks.workflow.UserTask;
import com.orchestranetworks.workflow.UserTaskBeforeWorkItemCompletionContext;

public class MandatoryFieldsUserTask extends UserTask {

	private String dataspace;
	private String dataset;
	private String table;
	private String record;

	Repository repo;
	AdaptationHome dataspaceName;
	Adaptation datasetName;
	AdaptationTable adaptationTable = null;
	Adaptation adaptationRecord = null;

	@Override
	public void checkBeforeWorkItemCompletion(UserTaskBeforeWorkItemCompletionContext context) {
		// TODO Auto-generated method stub
		super.checkBeforeWorkItemCompletion(context);

		dataspace = context.getVariableString("childDataSpace");
		dataset = context.getVariableString("dataset");
		table = context.getVariableString("table");
		record = context.getVariableString("record");

		repo = Repository.getDefault();
		dataspaceName = repo.lookupHome(HomeKey.forBranchName(dataspace));
		datasetName = dataspaceName.findAdaptationOrNull(AdaptationName.forName(dataset));

		String tableString = record.substring(0, record.indexOf("["));
		String recordIdentifier = record.substring(record.indexOf("=") + 2, record.indexOf("]") - 1);

		adaptationTable = datasetName.getTable((Path.parse(tableString)));
		adaptationRecord = adaptationTable.lookupAdaptationByPrimaryKey(PrimaryKey.parseString(recordIdentifier));

		if (adaptationRecord.get(Path.parse("./Loco_Type")) == null
				|| adaptationRecord.get(Path.parse("./Loco_Commissioning_Date")) == null
				|| adaptationRecord.get(Path.parse("./Loco_Manufacturer")) == null
				|| adaptationRecord.get(Path.parse("./Loco_Control_Type")) == null
				|| adaptationRecord.get(Path.parse("./Loco_Permanent_Domain")) == null
				|| adaptationRecord.get(Path.parse("./Loco_Service_Type")) == null
				|| adaptationRecord.get(Path.parse("./Loco_Traction_Motor_Type")) == null
				|| adaptationRecord.get(Path.parse("./Loco_Owning_Zone")) == null
				|| adaptationRecord.get(Path.parse("./Loco_Owning_Division")) == null
				|| adaptationRecord.get(Path.parse("./Loco_Owning_Shed")) == null
				|| adaptationRecord.get(Path.parse("./Loco_Boogie_Type")) == null
				|| adaptationRecord.get(Path.parse("./Loco_Brake_Type")) == null
				|| adaptationRecord.get(Path.parse("./Loco_Rtis_Flag")) == null
				|| adaptationRecord.get(Path.parse("./Loco_Remmlot_Flag")) == null
				|| adaptationRecord.get(Path.parse("./Loco_Lease_Type")) == null
				|| adaptationRecord.get(Path.parse("./Loco_Manufacturing_Date")) == null
				|| adaptationRecord.get(Path.parse("./Loco_Receiving_Date")) == null
				|| adaptationRecord.get(Path.parse("./Loco_Entry_Date")) == null
				|| adaptationRecord.get(Path.parse("./Loco_Status")) == null
				|| adaptationRecord.get(Path.parse("./Loco_Hotel_Load")) == null
				|| adaptationRecord.get(Path.parse("./Loco_Cab1_AC")) == null
				|| adaptationRecord.get(Path.parse("./Loco_Cab2_AC")) == null
				|| adaptationRecord.get(Path.parse("./Loco_Auxilary")) == null
				|| adaptationRecord.get(Path.parse("./Loco_Brake_Sub_Type")) == null
				|| adaptationRecord.get(Path.parse("./Loco_Gauge_Type")) == null
				|| adaptationRecord.get(Path.parse("./Loco_Axle_Load")) == null
				|| adaptationRecord.get(Path.parse("./Loco_Hauling_Power")) == null
				|| adaptationRecord.get(Path.parse("./Loco_Traction_Code")) == null
				|| adaptationRecord.get(Path.parse("./Loco_Condemn_Proposal_Type")) == null
				|| adaptationRecord.get(Path.parse("./Loco_Condemn_Date")) == null
				|| adaptationRecord.get(Path.parse("./Loco_POH_Date")) == null
				|| adaptationRecord.get(Path.parse("./Loco_HRP_Flag")) == null
				|| adaptationRecord.get(Path.parse("./Loco_Hog_Flag")) == null
				|| adaptationRecord.get(Path.parse("./Loco_Kavach_Flag")) == null
				|| adaptationRecord.get(Path.parse("./Loco_Power_Type")) == null
				|| adaptationRecord.get(Path.parse("./Loco_CVVRS_Type")) == null
				|| adaptationRecord.get(Path.parse("./Loco_VCD_Flag")) == null
				|| adaptationRecord.get(Path.parse("./Loco_Pvt_Owner_Flag")) == null
				|| adaptationRecord.get(Path.parse("./Loco_Pvt_Party_Code")) == null
//		adaptationRecord.get(Path.parse("./Allotment_Letter")) == null || 
//		adaptationRecord.get(Path.parse("./Allotment_Letter/Allotment_Letter_Number")) == null || 
//		adaptationRecord.get(Path.parse("./Allotment_Letter/Allotment_Letter_Date")) == null || 
//		adaptationRecord.get(Path.parse("./Allotment_Letter/Allotment_Letter")) == null || 
//		adaptationRecord.get(Path.parse("./Allotment_Letter/Allotment_Letter/attachment")) == null || 
//		adaptationRecord.get(Path.parse("./Reference_Documents")) == null || 
//		adaptationRecord.get(Path.parse("./Reference_Documents/Reference_Document_Number")) == null || 
//		adaptationRecord.get(Path.parse("./Reference_Documents/Reference_Document_Date")) == null || 
//		adaptationRecord.get(Path.parse("./Reference_Documents/Reference_Document")) == null || 
//		adaptationRecord.get(Path.parse("./Reference_Documents/Reference_Document/attachment")) == null || 
//		adaptationRecord.get(Path.parse("./Audit_Info")) == null || 
//		adaptationRecord.get(Path.parse("./Audit_Info/Source_Record_Id")) == null || 
//		adaptationRecord.get(Path.parse("./Audit_Info/Source_System_Name")) == null || 
//		adaptationRecord.get(Path.parse("./Audit_Info/Source_Event_Type")) == null || 
//		adaptationRecord.get(Path.parse("./Audit_Info/Created_By_User")) == null || 
//		adaptationRecord.get(Path.parse("./Audit_Info/Logged_In_User")) == null
		) {
			context.reportMessage(UserMessage.createError("Fields cannot be empty"));
		}

	}

}
