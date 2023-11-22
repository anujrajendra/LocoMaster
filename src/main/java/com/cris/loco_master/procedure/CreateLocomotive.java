package com.cris.loco_master.procedure;

import java.text.ParseException;

import com.cris.loco_master.Paths;
import com.cris.loco_master.utils.RulesUtils;
import com.cris.loco_master.webservice.rest.json.format.AuditInfo;
import com.cris.loco_master.webservice.rest.json.format.ErrorResponse;
import com.cris.loco_master.webservice.rest.json.format.Locomotive;
import com.cris.loco_master.webservice.rest.json.format.SuccessResponse;
import com.onwbp.adaptation.Adaptation;
import com.onwbp.adaptation.AdaptationTable;
import com.onwbp.adaptation.RequestResult;
import com.onwbp.adaptation.UnavailableContentError;
import com.orchestranetworks.instance.Repository;
import com.orchestranetworks.schema.ConstraintViolationException;
import com.orchestranetworks.schema.PathAccessException;
import com.orchestranetworks.service.OperationException;
import com.orchestranetworks.service.Procedure;
import com.orchestranetworks.service.ProcedureContext;
import com.orchestranetworks.service.ValueContextForUpdate;
import com.orchestranetworks.workflow.WorkflowEngine;

public class CreateLocomotive implements Procedure {

	String recordAction;
	Locomotive locomotive;
	SuccessResponse successResponse;
	ErrorResponse errorResponse;
	Adaptation dataset;

	public CreateLocomotive(String recordAction, Locomotive locomotive, SuccessResponse successResponse,
			ErrorResponse errorResponse, Adaptation dataset) {
		super();
		this.recordAction = recordAction;
		this.locomotive = locomotive;
		this.successResponse = successResponse;
		this.errorResponse = errorResponse;
		this.dataset = dataset;
	}

	@Override
	public void execute(ProcedureContext pContext) throws Exception {

		createLocomotiveRecords(pContext);
	}

	void createLocomotiveRecords(ProcedureContext pContext) throws Exception {

		AdaptationTable tbLocomotive = dataset.getTable(Paths._Root_Locomotive.getPathInSchema());
		ValueContextForUpdate vcfuLocomotive = null;
		Adaptation locomotiveAdaptation = null;

		// WebServiceUtils.isLocoNumberPresent(locomotive, locomotiveAdaptation);

		// WebServiceUtils.isLocoNumberPresent(recordAction, locomotiveAdaptation);
		if (recordAction.equalsIgnoreCase("add")) {
			vcfuLocomotive = pContext.getContextForNewOccurrence(tbLocomotive);
		} else {
			locomotiveAdaptation = getLocomotiveAdaptation(locomotive, tbLocomotive);
			vcfuLocomotive = pContext.getContext(locomotiveAdaptation.getAdaptationName());
		}

		setLocomotiveValueContext(vcfuLocomotive);

		String mdmId = "0";

		try {

			if (recordAction.equalsIgnoreCase("add")) {
				Adaptation locoRecord = pContext.doCreateOccurrence(vcfuLocomotive, tbLocomotive);
				mdmId = (String) locoRecord.get(Paths._Root_Locomotive._Root_Locomotive_Loco_Number);
			} else {

				Repository repository = Repository.getDefault();
				WorkflowEngine wfEngine = WorkflowEngine.getFromRepository(repository, pContext.getSession());

//				if (LocoRecordOperationRestrictionUtils.checkRunningProcesses(wfEngine, "loco_create_api", "record",
//						"/root/Locomotive[./Loco_Number=\""
//								+ locomotiveAdaptation.get(Paths._Root_Locomotive._Root_Locomotive_Loco_Number)
//								+ "\"]")) {
//					throw OperationException
//							.createError(" The selected record has already an operation (Loco Update API) in process.");
//				}
//				if (LocoRecordOperationRestrictionUtils.checkRunningProcesses(wfEngine, "loco_update_ui", "record",
//						locomotiveAdaptation.toXPathExpression())) {
//					throw OperationException
//							.createError(" The selected record has already an operation (Loco Update) in process.");
//				}
//
//				if (LocoRecordOperationRestrictionUtils.checkRunningProcesses(wfEngine, "loco_condemn_ui", "record",
//						locomotiveAdaptation.toXPathExpression())) {
//					throw OperationException
//							.createError(" The selected record has already an operation (Loco Condemn) in process.");
//
//				}
//
//				if (LocoRecordOperationRestrictionUtils.checkRunningProcesses(wfEngine, "loco_transfer_ui", "record",
//						locomotiveAdaptation.toXPathExpression())) {
//					throw OperationException
//							.createError(" The selected record has already an operation (Loco Transfer) in process.");
//
//				}

				Adaptation locoRecord = pContext.doModifyContent(locomotiveAdaptation, vcfuLocomotive);
				mdmId = (String) locoRecord.get(Paths._Root_Locomotive._Root_Locomotive_Loco_Number);
			}
		} catch (ConstraintViolationException | OperationException e) {
			errorResponse.setErrorCode("500");
			errorResponse.setErrorMessage(e.getMessage());
			e.printStackTrace();
			throw e;
		}

		successResponse.setMdmId(mdmId);
		successResponse.setSuccessCode("200");
	}

	Adaptation getLocomotiveAdaptation(Locomotive locomotive, AdaptationTable tbLocomotive) {
		tbLocomotive = dataset.getTable(Paths._Root_Locomotive.getPathInSchema());
		RequestResult requestResult = tbLocomotive
				.createRequestResult(Paths._Root_Locomotive._Root_Locomotive_Loco_Number.format() + "= '"
						+ locomotive.getLoco_Number() + "'");

		if (requestResult.isSizeGreaterOrEqual(0))
			return requestResult.nextAdaptation();

		return null;
	}

	void setLocomotiveValueContext(ValueContextForUpdate vcfuLocomotive) {

		String Loco_Number = locomotive.getLoco_Number();
		if (Loco_Number != null)
			RulesUtils.setRecord(vcfuLocomotive, Loco_Number, Paths._Root_Locomotive._Root_Locomotive_Loco_Number);

		String Loco_Type = locomotive.getLoco_Type();
		if (Loco_Type != null)
			RulesUtils.setRecord(vcfuLocomotive, Loco_Type, Paths._Root_Locomotive._Root_Locomotive_Loco_Type);

		String Loco_Commissioning_Date = locomotive.getLoco_Commissioning_Date();
		if (Loco_Commissioning_Date != null)
			try {
				RulesUtils.setDateRecord(vcfuLocomotive, Loco_Commissioning_Date,
						Paths._Root_Locomotive._Root_Locomotive_Loco_Commissioning_Date);
			} catch (UnavailableContentError | PathAccessException | IllegalArgumentException | ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		String Loco_Manufacturer = locomotive.getLoco_Manufacturer();
		if (Loco_Manufacturer != null)
			RulesUtils.setRecord(vcfuLocomotive, Loco_Manufacturer,
					Paths._Root_Locomotive._Root_Locomotive_Loco_Manufacturer);

		String Loco_Control_Type = locomotive.getLoco_Control_Type();
		if (Loco_Control_Type != null)
			RulesUtils.setRecord(vcfuLocomotive, Loco_Control_Type,
					Paths._Root_Locomotive._Root_Locomotive_Loco_Control_Type);

		String Loco_Permanent_Domain = locomotive.getLoco_Permanent_Domain();
		if (Loco_Permanent_Domain != null)
			RulesUtils.setRecord(vcfuLocomotive, Loco_Permanent_Domain,
					Paths._Root_Locomotive._Root_Locomotive_Loco_Permanent_Domain);

		String Loco_Service_Type = locomotive.getLoco_Service_Type();
		if (Loco_Service_Type != null)
			RulesUtils.setRecord(vcfuLocomotive, Loco_Service_Type,
					Paths._Root_Locomotive._Root_Locomotive_Loco_Service_Type);

		String Loco_Traction_Motor_Type = locomotive.getLoco_Traction_Motor_Type();
		if (Loco_Traction_Motor_Type != null)
			RulesUtils.setRecord(vcfuLocomotive, Loco_Traction_Motor_Type,
					Paths._Root_Locomotive._Root_Locomotive_Loco_Traction_Motor_Type);

		String Loco_Owning_Zone = locomotive.getLoco_Owning_Zone();
		if (Loco_Owning_Zone != null)
			RulesUtils.setRecord(vcfuLocomotive, Loco_Owning_Zone,
					Paths._Root_Locomotive._Root_Locomotive_Loco_Owning_Zone);

		String Loco_Owning_Division = locomotive.getLoco_Owning_Division();
		if (Loco_Owning_Division != null)
			RulesUtils.setRecord(vcfuLocomotive, Loco_Owning_Division,
					Paths._Root_Locomotive._Root_Locomotive_Loco_Owning_Division);

		String Loco_Owning_Shed = locomotive.getLoco_Owning_Shed();
		if (Loco_Owning_Shed != null)
			RulesUtils.setRecord(vcfuLocomotive, Loco_Owning_Shed,
					Paths._Root_Locomotive._Root_Locomotive_Loco_Owning_Shed);

		String Loco_Boogie_Type = locomotive.getLoco_Boogie_Type();
		if (Loco_Boogie_Type != null)
			RulesUtils.setRecord(vcfuLocomotive, Loco_Boogie_Type,
					Paths._Root_Locomotive._Root_Locomotive_Loco_Boogie_Type);

		String Loco_Brake_Type = locomotive.getLoco_Brake_Type();
		if (Loco_Brake_Type != null)
			RulesUtils.setRecord(vcfuLocomotive, Loco_Brake_Type,
					Paths._Root_Locomotive._Root_Locomotive_Loco_Brake_Type);

		String Loco_Rtis_Flag = locomotive.getLoco_Rtis_Flag();
		if (Loco_Rtis_Flag != null)
			RulesUtils.setRecord(vcfuLocomotive, Loco_Rtis_Flag,
					Paths._Root_Locomotive._Root_Locomotive_Loco_Rtis_Flag);

		String Loco_Remmlot_Flag = locomotive.getLoco_Remmlot_Flag();
		if (Loco_Remmlot_Flag != null)
			RulesUtils.setRecord(vcfuLocomotive, Loco_Remmlot_Flag,
					Paths._Root_Locomotive._Root_Locomotive_Loco_Remmlot_Flag);

		String Loco_Lease_Type = locomotive.getLoco_Lease_Type();
		if (Loco_Lease_Type != null)
			RulesUtils.setRecord(vcfuLocomotive, Loco_Lease_Type,
					Paths._Root_Locomotive._Root_Locomotive_Loco_Lease_Type);

		String Loco_Manufacturing_Date = locomotive.getLoco_Manufacturing_Date();
		if (Loco_Manufacturing_Date != null)
			try {
				RulesUtils.setDateRecord(vcfuLocomotive, Loco_Manufacturing_Date,
						Paths._Root_Locomotive._Root_Locomotive_Loco_Manufacturing_Date);
			} catch (UnavailableContentError | PathAccessException | IllegalArgumentException | ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		String Loco_Receiving_Date = locomotive.getLoco_Receiving_Date();
		if (Loco_Receiving_Date != null)
			try {
				RulesUtils.setDateRecord(vcfuLocomotive, Loco_Receiving_Date,
						Paths._Root_Locomotive._Root_Locomotive_Loco_Receiving_Date);
			} catch (UnavailableContentError | PathAccessException | IllegalArgumentException | ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		String Loco_Entry_Date = locomotive.getLoco_Entry_Date();
		if (Loco_Entry_Date != null)
			try {
				RulesUtils.setDateRecord(vcfuLocomotive, Loco_Entry_Date,
						Paths._Root_Locomotive._Root_Locomotive_Loco_Entry_Date);
			} catch (UnavailableContentError | PathAccessException | IllegalArgumentException | ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		String Loco_Status = locomotive.getLoco_Status();
		if (Loco_Status != null)
			RulesUtils.setRecord(vcfuLocomotive, Loco_Status, Paths._Root_Locomotive._Root_Locomotive_Loco_Status);

		String Loco_Hotel_Load = locomotive.getLoco_Hotel_Load();
		if (Loco_Hotel_Load != null)
			RulesUtils.setRecord(vcfuLocomotive, Loco_Hotel_Load,
					Paths._Root_Locomotive._Root_Locomotive_Loco_Hotel_Load);

		String Loco_Cab1_AC = locomotive.getLoco_Cab1_AC();
		if (Loco_Cab1_AC != null)
			RulesUtils.setRecord(vcfuLocomotive, Loco_Cab1_AC, Paths._Root_Locomotive._Root_Locomotive_Loco_Cab1_AC);

		String Loco_Cab2_AC = locomotive.getLoco_Cab2_AC();
		if (Loco_Cab2_AC != null)
			RulesUtils.setRecord(vcfuLocomotive, Loco_Cab2_AC, Paths._Root_Locomotive._Root_Locomotive_Loco_Cab2_AC);

		String Loco_Auxilary = locomotive.getLoco_Auxilary();
		if (Loco_Auxilary != null)
			RulesUtils.setRecord(vcfuLocomotive, Loco_Auxilary, Paths._Root_Locomotive._Root_Locomotive_Loco_Auxilary);

		String Loco_Brake_Sub_Type = locomotive.getLoco_Brake_Sub_Type();
		if (Loco_Brake_Sub_Type != null)
			RulesUtils.setRecord(vcfuLocomotive, Loco_Brake_Sub_Type,
					Paths._Root_Locomotive._Root_Locomotive_Loco_Brake_Sub_Type);

		String Loco_Gauge_Type = locomotive.getLoco_Gauge_Type();
		if (Loco_Gauge_Type != null)
			RulesUtils.setRecord(vcfuLocomotive, Loco_Gauge_Type,
					Paths._Root_Locomotive._Root_Locomotive_Loco_Gauge_Type);

		String Loco_Axle_Load = locomotive.getLoco_Axle_Load();
		if (Loco_Axle_Load != null)
			RulesUtils.setRecord(vcfuLocomotive, Loco_Axle_Load,
					Paths._Root_Locomotive._Root_Locomotive_Loco_Axle_Load);

		String Loco_Hauling_Power = locomotive.getLoco_Hauling_Power();
		if (Loco_Hauling_Power != null)
			RulesUtils.setIntegerRecord(vcfuLocomotive, Loco_Hauling_Power,
					Paths._Root_Locomotive._Root_Locomotive_Loco_Hauling_Power);

		String Loco_Traction_Code = locomotive.getLoco_Traction_Code();
		if (Loco_Traction_Code != null)
			RulesUtils.setRecord(vcfuLocomotive, Loco_Traction_Code,
					Paths._Root_Locomotive._Root_Locomotive_Loco_Traction_Code);

		String Loco_Condemn_Proposal_Type = locomotive.getLoco_Condemn_Proposal_Type();
		if (Loco_Condemn_Proposal_Type != null)
			RulesUtils.setRecord(vcfuLocomotive, Loco_Condemn_Proposal_Type,
					Paths._Root_Locomotive._Root_Locomotive_Loco_Condemn_Proposal_Type);

		String Loco_Condemn_Date = locomotive.getLoco_Condemn_Date();
		if (Loco_Condemn_Date != null)
			try {
				RulesUtils.setDateRecord(vcfuLocomotive, Loco_Condemn_Date,
						Paths._Root_Locomotive._Root_Locomotive_Loco_Condemn_Date);
			} catch (UnavailableContentError | PathAccessException | IllegalArgumentException | ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		String Loco_POH_Date = locomotive.getLoco_POH_Date();
		if (Loco_POH_Date != null)
			try {
				RulesUtils.setDateRecord(vcfuLocomotive, Loco_POH_Date,
						Paths._Root_Locomotive._Root_Locomotive_Loco_POH_Date);
			} catch (UnavailableContentError | PathAccessException | IllegalArgumentException | ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		String Loco_HRP_Flag = locomotive.getLoco_HRP_Flag();
		if (Loco_HRP_Flag != null)
			RulesUtils.setRecord(vcfuLocomotive, Loco_HRP_Flag, Paths._Root_Locomotive._Root_Locomotive_Loco_HRP_Flag);

		String Loco_Hog_Flag = locomotive.getLoco_Hog_Flag();
		if (Loco_Hog_Flag != null)
			RulesUtils.setRecord(vcfuLocomotive, Loco_Hog_Flag, Paths._Root_Locomotive._Root_Locomotive_Loco_Hog_Flag);

		String Loco_Kavach_Flag = locomotive.getLoco_Kavach_Flag();
		if (Loco_Kavach_Flag != null)
			RulesUtils.setRecord(vcfuLocomotive, Loco_Kavach_Flag,
					Paths._Root_Locomotive._Root_Locomotive_Loco_Kavach_Flag);

		String Loco_Power_Type = locomotive.getLoco_Power_Type();
		if (Loco_Power_Type != null)
			RulesUtils.setRecord(vcfuLocomotive, Loco_Power_Type,
					Paths._Root_Locomotive._Root_Locomotive_Loco_Power_Type);

		String Loco_CVVRS_Type = locomotive.getLoco_CVVRS_Type();
		if (Loco_CVVRS_Type != null)
			RulesUtils.setRecord(vcfuLocomotive, Loco_CVVRS_Type,
					Paths._Root_Locomotive._Root_Locomotive_Loco_CVVRS_Type);

		String Loco_VCD_Flag = locomotive.getLoco_VCD_Flag();
		if (Loco_VCD_Flag != null)
			RulesUtils.setRecord(vcfuLocomotive, Loco_VCD_Flag, Paths._Root_Locomotive._Root_Locomotive_Loco_VCD_Flag);

		String Loco_Pvt_Owner_Flag = locomotive.getLoco_Pvt_Owner_Flag();
		if (Loco_Pvt_Owner_Flag != null)
			RulesUtils.setRecord(vcfuLocomotive, Loco_Pvt_Owner_Flag,
					Paths._Root_Locomotive._Root_Locomotive_Loco_Pvt_Owner_Flag);

		String Loco_Pvt_Party_Code = locomotive.getLoco_Pvt_Party_Code();
		if (Loco_Pvt_Party_Code != null)
			RulesUtils.setRecord(vcfuLocomotive, Loco_Pvt_Party_Code,
					Paths._Root_Locomotive._Root_Locomotive_Loco_Pvt_Party_Code);

		AuditInfo auditInfo = locomotive.getAuditInfo();
		if (auditInfo != null) {

			String Source_Record_Id = auditInfo.getSource_Record_Id();
			if (Source_Record_Id != null)
				RulesUtils.setRecord(vcfuLocomotive, Source_Record_Id,
						Paths._Root_Locomotive._Root_Locomotive_Audit_Info_Source_Record_Id);

			String Source_System_Name = auditInfo.getSource_System_Name();
			if (Source_System_Name != null)
				RulesUtils.setRecord(vcfuLocomotive, Source_System_Name,
						Paths._Root_Locomotive._Root_Locomotive_Audit_Info_Source_System_Name);

			String Source_Event_Type = auditInfo.getSource_Event_Type();
			if (Source_Event_Type != null)
				RulesUtils.setRecord(vcfuLocomotive, Source_Event_Type,
						Paths._Root_Locomotive._Root_Locomotive_Audit_Info_Source_Event_Type);
		}
	}

	public Locomotive getStation() {
		return locomotive;
	}

	public void setStation(Locomotive station) {
		this.locomotive = station;
	}

	public SuccessResponse getSuccessResponse() {
		return successResponse;
	}

	public void setSuccessResponse(SuccessResponse successResponse) {
		this.successResponse = successResponse;
	}

	public Adaptation getDataset() {
		return dataset;
	}

	public void setDataset(Adaptation dataset) {
		this.dataset = dataset;
	}

}
