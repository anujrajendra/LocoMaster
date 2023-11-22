package com.cris.loco_master.extensions;

import com.cris.loco_master.Paths;
import com.cris.loco_master.access.LocoAttributeHiddenAccessRule;
import com.cris.loco_master.access.LocoAttributeReadWriteAccessRule;
import com.cris.loco_master.access.LocoUpdateAccessRule;
import com.cris.loco_master.access.LocoUserAccessRule;
import com.cris.loco_master.service.workflow.condemn.LocoCondemnServiceDeclaration;
import com.cris.loco_master.service.workflow.transfer.LocoTransferServiceDeclaration;
import com.cris.loco_master.service.workflow.update.LocoUpdateServiceDeclaration;
import com.orchestranetworks.schema.Path;
import com.orchestranetworks.schema.SchemaExtensions;
import com.orchestranetworks.schema.SchemaExtensionsContext;
import com.orchestranetworks.service.AccessRule;
import com.orchestranetworks.userservice.declaration.UserServiceDeclaration;

public class LocoExtensions implements SchemaExtensions {

	@Override
	public void defineExtensions(SchemaExtensionsContext context) {
		// TODO Auto-generated method stub

		final UserServiceDeclaration.OnTableView userServiceDeclarationUpdate = new LocoUpdateServiceDeclaration();
		context.registerUserService(userServiceDeclarationUpdate);

		final UserServiceDeclaration.OnTableView userServiceDeclarationTransfer = new LocoTransferServiceDeclaration();
		context.registerUserService(userServiceDeclarationTransfer);

		final UserServiceDeclaration.OnTableView userServiceDeclarationCondemn = new LocoCondemnServiceDeclaration();
		context.registerUserService(userServiceDeclarationCondemn);

//		final UserServiceDeclaration.OnTableView locoDQuserServiceDeclaration =
//				new LocoDQServiceDeclaration();
//		context.registerUserService(locoDQuserServiceDeclaration);

		final Path path = Paths._Root_Locomotive.getPathInSchema();
		final AccessRule locomotiveUserAccessRule = new LocoUserAccessRule();

		context.setAccessRuleOnOccurrence(path, locomotiveUserAccessRule);

		final Path locoZonePath = Paths._Root_Locomotive.getPathInSchema()
				.add(Paths._Root_Locomotive._Root_Locomotive_Loco_Owning_Zone);
		final Path locoDivisionPath = Paths._Root_Locomotive.getPathInSchema()
				.add(Paths._Root_Locomotive._Root_Locomotive_Loco_Owning_Division);
		final Path locoShedPath = Paths._Root_Locomotive.getPathInSchema()
				.add(Paths._Root_Locomotive._Root_Locomotive_Loco_Owning_Shed);

		final Path locoCondemnDatePath = Paths._Root_Locomotive.getPathInSchema()
				.add(Paths._Root_Locomotive._Root_Locomotive_Loco_Condemn_Date);
		final Path locoCondemnProposalTypePath = Paths._Root_Locomotive.getPathInSchema()
				.add(Paths._Root_Locomotive._Root_Locomotive_Loco_Condemn_Proposal_Type);

		final AccessRule locomotiveUpdateAccessRule = new LocoUpdateAccessRule();
		// final AccessRule locomotiveCondemnAccessRule = new
		// LocomotiveCondemnAccessRule();
		// final AccessRule locomotiveAttributeReadOnlyAccessRule = new
		// LocomotiveAttributeReadOnlyAccessRule();
		final AccessRule locomotiveAttributeReadWriteAccessRule = new LocoAttributeReadWriteAccessRule();

		context.setAccessRuleOnNode(locoZonePath, locomotiveUpdateAccessRule);
		context.setAccessRuleOnNode(locoDivisionPath, locomotiveUpdateAccessRule);
		context.setAccessRuleOnNode(locoShedPath, locomotiveUpdateAccessRule);

		final Path locoTypePath = Paths._Root_Locomotive.getPathInSchema()
				.add(Paths._Root_Locomotive._Root_Locomotive_Loco_Type);
		final Path locoTractionCodePath = Paths._Root_Locomotive.getPathInSchema()
				.add(Paths._Root_Locomotive._Root_Locomotive_Loco_Traction_Code);
		final Path locoPermanentDomainPath = Paths._Root_Locomotive.getPathInSchema()
				.add(Paths._Root_Locomotive._Root_Locomotive_Loco_Permanent_Domain);
		final Path locoManufacturerPath = Paths._Root_Locomotive.getPathInSchema()
				.add(Paths._Root_Locomotive._Root_Locomotive_Loco_Manufacturer);
		final Path locoAllotmentLetterNumberPath = Paths._Root_Locomotive.getPathInSchema()
				.add(Paths._Root_Locomotive._Root_Locomotive_Allotment_Letter_Allotment_Letter_Number);
		final Path locoAllotmentLetterDatePath = Paths._Root_Locomotive.getPathInSchema()
				.add(Paths._Root_Locomotive._Root_Locomotive_Allotment_Letter_Allotment_Letter_Date);
		final Path locoAllotmentLetterPath = Paths._Root_Locomotive.getPathInSchema()
				.add(Paths._Root_Locomotive._Root_Locomotive_Allotment_Letter_Allotment_Letter);

		final Path locoManufacturingDatePath = Paths._Root_Locomotive.getPathInSchema()
				.add(Paths._Root_Locomotive._Root_Locomotive_Loco_Manufacturing_Date);
		final Path locoLeasePath = Paths._Root_Locomotive.getPathInSchema()
				.add(Paths._Root_Locomotive._Root_Locomotive_Loco_Lease_Type);
		final Path locoGaugeTypePath = Paths._Root_Locomotive.getPathInSchema()
				.add(Paths._Root_Locomotive._Root_Locomotive_Loco_Gauge_Type);
		final Path locoHaulingPowerPath = Paths._Root_Locomotive.getPathInSchema()
				.add(Paths._Root_Locomotive._Root_Locomotive_Loco_Hauling_Power);
		final Path locoCab1ACPath = Paths._Root_Locomotive.getPathInSchema()
				.add(Paths._Root_Locomotive._Root_Locomotive_Loco_Cab1_AC);
		final Path locoCab2ACPath = Paths._Root_Locomotive.getPathInSchema()
				.add(Paths._Root_Locomotive._Root_Locomotive_Loco_Cab2_AC);
		final Path locoAxleLoadPath = Paths._Root_Locomotive.getPathInSchema()
				.add(Paths._Root_Locomotive._Root_Locomotive_Loco_Axle_Load);
		final Path locoHotelLoadPath = Paths._Root_Locomotive.getPathInSchema()
				.add(Paths._Root_Locomotive._Root_Locomotive_Loco_Hotel_Load);
		final Path locoBoogieTypePath = Paths._Root_Locomotive.getPathInSchema()
				.add(Paths._Root_Locomotive._Root_Locomotive_Loco_Boogie_Type);
		final Path locoAuxilaryPath = Paths._Root_Locomotive.getPathInSchema()
				.add(Paths._Root_Locomotive._Root_Locomotive_Loco_Auxilary);
		final Path locoTractionMotorTypePath = Paths._Root_Locomotive.getPathInSchema()
				.add(Paths._Root_Locomotive._Root_Locomotive_Loco_Traction_Motor_Type);
		final Path locoControlTypePath = Paths._Root_Locomotive.getPathInSchema()
				.add(Paths._Root_Locomotive._Root_Locomotive_Loco_Control_Type);
		final Path locoBrakeTypePath = Paths._Root_Locomotive.getPathInSchema()
				.add(Paths._Root_Locomotive._Root_Locomotive_Loco_Brake_Type);
		final Path locoSubBrakeTypePath = Paths._Root_Locomotive.getPathInSchema()
				.add(Paths._Root_Locomotive._Root_Locomotive_Loco_Brake_Sub_Type);
		final Path locoKavachFlagPath = Paths._Root_Locomotive.getPathInSchema()
				.add(Paths._Root_Locomotive._Root_Locomotive_Loco_Kavach_Flag);
		final Path locoCVVRSTypePath = Paths._Root_Locomotive.getPathInSchema()
				.add(Paths._Root_Locomotive._Root_Locomotive_Loco_CVVRS_Type);
		final Path locoVCDFlagPath = Paths._Root_Locomotive.getPathInSchema()
				.add(Paths._Root_Locomotive._Root_Locomotive_Loco_VCD_Flag);

		final Path locoEntryDatePath = Paths._Root_Locomotive.getPathInSchema()
				.add(Paths._Root_Locomotive._Root_Locomotive_Loco_Entry_Date);
		final Path locoCommissioningDatePath = Paths._Root_Locomotive.getPathInSchema()
				.add(Paths._Root_Locomotive._Root_Locomotive_Loco_Commissioning_Date);
		final Path locoServiceTypePath = Paths._Root_Locomotive.getPathInSchema()
				.add(Paths._Root_Locomotive._Root_Locomotive_Loco_Service_Type);
		final Path locoStatusPath = Paths._Root_Locomotive.getPathInSchema()
				.add(Paths._Root_Locomotive._Root_Locomotive_Loco_Status);
		final Path locoReceivingDatePath = Paths._Root_Locomotive.getPathInSchema()
				.add(Paths._Root_Locomotive._Root_Locomotive_Loco_Receiving_Date);
		final Path locoPOHDatePath = Paths._Root_Locomotive.getPathInSchema()
				.add(Paths._Root_Locomotive._Root_Locomotive_Loco_POH_Date);
		final Path locoRtisFlagPath = Paths._Root_Locomotive.getPathInSchema()
				.add(Paths._Root_Locomotive._Root_Locomotive_Loco_Rtis_Flag);
		final Path locoHogFlagPath = Paths._Root_Locomotive.getPathInSchema()
				.add(Paths._Root_Locomotive._Root_Locomotive_Loco_Hog_Flag);
		final Path locoPowerPath = Paths._Root_Locomotive.getPathInSchema()
				.add(Paths._Root_Locomotive._Root_Locomotive_Loco_Power_Type);
		final Path locoRemmoltFlagPath = Paths._Root_Locomotive.getPathInSchema()
				.add(Paths._Root_Locomotive._Root_Locomotive_Loco_Remmlot_Flag);
		final Path locoHRPFlagPath = Paths._Root_Locomotive.getPathInSchema()
				.add(Paths._Root_Locomotive._Root_Locomotive_Loco_HRP_Flag);
		final Path locoPvtOwnerPath = Paths._Root_Locomotive.getPathInSchema()
				.add(Paths._Root_Locomotive._Root_Locomotive_Loco_Pvt_Owner_Flag);
		final Path locoPvtPartyCodePath = Paths._Root_Locomotive.getPathInSchema()
				.add(Paths._Root_Locomotive._Root_Locomotive_Loco_Pvt_Party_Code);

//		context.setAccessRuleOnNode(locoCondemnDatePath, locomotiveCondemnAccessRule);
//		context.setAccessRuleOnNode(locoCondemnProposalTypePath, locomotiveCondemnAccessRule);
//
//		context.setAccessRuleOnNode(locoTypePath, locomotiveAttributeReadOnlyAccessRule);
//		context.setAccessRuleOnNode(locoTractionCodePath, locomotiveAttributeReadOnlyAccessRule);
//		context.setAccessRuleOnNode(locoPermanentDomainPath, locomotiveAttributeReadOnlyAccessRule);
//		context.setAccessRuleOnNode(locoManufacturerPath, locomotiveAttributeReadOnlyAccessRule);
//		context.setAccessRuleOnNode(locoAllotmentLetterNumberPath, locomotiveAttributeReadOnlyAccessRule);
//		context.setAccessRuleOnNode(locoAllotmentLetterDatePath, locomotiveAttributeReadOnlyAccessRule);
//		context.setAccessRuleOnNode(locoAllotmentLetterPath, locomotiveAttributeReadOnlyAccessRule);
//
//		context.setAccessRuleOnNode(locoManufacturingDatePath, locomotiveAttributeReadOnlyAccessRule);
//		context.setAccessRuleOnNode(locoLeasePath, locomotiveAttributeReadOnlyAccessRule);
//		context.setAccessRuleOnNode(locoGaugeTypePath, locomotiveAttributeReadOnlyAccessRule);
//		context.setAccessRuleOnNode(locoHaulingPowerPath, locomotiveAttributeReadOnlyAccessRule);
//		context.setAccessRuleOnNode(locoCab1ACPath, locomotiveAttributeReadOnlyAccessRule);
//		context.setAccessRuleOnNode(locoCab2ACPath, locomotiveAttributeReadOnlyAccessRule);
//		context.setAccessRuleOnNode(locoAxleLoadPath, locomotiveAttributeReadOnlyAccessRule);
//		context.setAccessRuleOnNode(locoHotelLoadPath, locomotiveAttributeReadOnlyAccessRule);
//		context.setAccessRuleOnNode(locoBoogieTypePath, locomotiveAttributeReadOnlyAccessRule);
//		context.setAccessRuleOnNode(locoAuxilaryPath, locomotiveAttributeReadOnlyAccessRule);
//		context.setAccessRuleOnNode(locoTractionMotorTypePath, locomotiveAttributeReadOnlyAccessRule);
//		context.setAccessRuleOnNode(locoControlTypePath, locomotiveAttributeReadOnlyAccessRule);
//		context.setAccessRuleOnNode(locoBrakeTypePath, locomotiveAttributeReadOnlyAccessRule);
//		context.setAccessRuleOnNode(locoSubBrakeTypePath, locomotiveAttributeReadOnlyAccessRule);
//		context.setAccessRuleOnNode(locoKavachFlagPath, locomotiveAttributeReadOnlyAccessRule);
//		context.setAccessRuleOnNode(locoCVVRSTypePath, locomotiveAttributeReadOnlyAccessRule);
//		context.setAccessRuleOnNode(locoVCDFlagPath, locomotiveAttributeReadOnlyAccessRule);
//
//		context.setAccessRuleOnNode(locoEntryDatePath, locomotiveAttributeReadOnlyAccessRule);
//		context.setAccessRuleOnNode(locoCommissioningDatePath, locomotiveAttributeReadOnlyAccessRule);
//		context.setAccessRuleOnNode(locoServiceTypePath, locomotiveAttributeReadOnlyAccessRule);
//		// context.setAccessRuleOnNode(locoStatusPath,
//		// locomotiveAttributeReadOnlyAccessRule);
//		context.setAccessRuleOnNode(locoReceivingDatePath, locomotiveAttributeReadOnlyAccessRule);
//		context.setAccessRuleOnNode(locoPOHDatePath, locomotiveAttributeReadOnlyAccessRule);
//		context.setAccessRuleOnNode(locoRtisFlagPath, locomotiveAttributeReadOnlyAccessRule);
//		context.setAccessRuleOnNode(locoHogFlagPath, locomotiveAttributeReadOnlyAccessRule);
//		context.setAccessRuleOnNode(locoPowerPath, locomotiveAttributeReadOnlyAccessRule);
//		context.setAccessRuleOnNode(locoRemmoltFlagPath, locomotiveAttributeReadOnlyAccessRule);
//		context.setAccessRuleOnNode(locoHRPFlagPath, locomotiveAttributeReadOnlyAccessRule);
//		context.setAccessRuleOnNode(locoPvtOwnerPath, locomotiveAttributeReadOnlyAccessRule);
//		context.setAccessRuleOnNode(locoPvtPartyCodePath, locomotiveAttributeReadOnlyAccessRule);

		context.setAccessRuleOnNodeAndAllDescendants(locoStatusPath, true, locomotiveAttributeReadWriteAccessRule);

		final AccessRule locomotiveHiddenAccessRule = new LocoAttributeHiddenAccessRule();
		context.setAccessRuleOnNodeAndAllDescendants(locoCondemnProposalTypePath, true, locomotiveHiddenAccessRule);

	}

}
