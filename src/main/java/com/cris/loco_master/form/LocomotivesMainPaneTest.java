package com.cris.loco_master.form;

import com.cris.loco_master.Paths;
import com.onwbp.adaptation.Adaptation;
import com.orchestranetworks.ui.UIFormLabelSpec;
import com.orchestranetworks.ui.form.UIFormContext;
import com.orchestranetworks.ui.form.UIFormPane;
import com.orchestranetworks.ui.form.UIFormPaneWriter;

public class LocomotivesMainPaneTest implements UIFormPane{

	@Override
	public void writePane(UIFormPaneWriter pWriter, UIFormContext aContext) {
		
		//String floatDivStyle = "float:left; width:48%; ";
		String LeftDivStyle = "display: inline-block; width: 49%; padding-bottom: 10px;";
		String RightDivStyle = "float:right; width:48%; ";
		String DownDivStyle = "display: inline-block; width: 49%;";

		// Left Side

		pWriter.add("<div style='" + LeftDivStyle + " min-width:200px;'>");
		//pWriter.startBorder(true);
		pWriter.add(
				"<table><tr><td style='margin-top: 3px; width: 30px; min-width: 30px; max-width: 30px; vertical-align:top'>");

		//UIUtils.addTitleFormRow(pWriter, "Person Details");
		pWriter.startTableFormRow();
		
		pWriter.addFormRow(Paths._Root_Locomotive._Root_Locomotive_Loco_Number);
		pWriter.addFormRow(Paths._Root_Locomotive._Root_Locomotive_Loco_Type);
		pWriter.addFormRow(Paths._Root_Locomotive._Root_Locomotive_Loco_Owning_Zone);
		pWriter.addFormRow(Paths._Root_Locomotive._Root_Locomotive_Loco_Owning_Division);
		pWriter.addFormRow(Paths._Root_Locomotive._Root_Locomotive_Loco_Owning_Shed);
		pWriter.addFormRow(Paths._Root_Locomotive._Root_Locomotive_Loco_Manufacturer);
		pWriter.addFormRow(Paths._Root_Locomotive._Root_Locomotive_Loco_Manufacturing_Date);
		pWriter.addFormRow(Paths._Root_Locomotive._Root_Locomotive_Loco_Entry_Date);
		pWriter.addFormRow(Paths._Root_Locomotive._Root_Locomotive_Loco_Commissioning_Date);
		pWriter.addFormRow(Paths._Root_Locomotive._Root_Locomotive_Loco_Control_Type);
		pWriter.addFormRow(Paths._Root_Locomotive._Root_Locomotive_Loco_Service_Type);
		pWriter.addFormRow(Paths._Root_Locomotive._Root_Locomotive_Loco_Brake_Type);
		pWriter.addFormRow(Paths._Root_Locomotive._Root_Locomotive_Loco_Brake_Sub_Type);
		pWriter.addFormRow(Paths._Root_Locomotive._Root_Locomotive_Loco_Hotel_Load);
		pWriter.addFormRow(Paths._Root_Locomotive._Root_Locomotive_Loco_Cab1_AC);
		pWriter.addFormRow(Paths._Root_Locomotive._Root_Locomotive_Loco_Cab2_AC);
		pWriter.addFormRow(Paths._Root_Locomotive._Root_Locomotive_Loco_Axle_Load);
		pWriter.addFormRow(Paths._Root_Locomotive._Root_Locomotive_Loco_Condemn_Proposal_Type);
		pWriter.addFormRow(Paths._Root_Locomotive._Root_Locomotive_Loco_CVVRS_Type);
		pWriter.addFormRow(Paths._Root_Locomotive._Root_Locomotive_Loco_Hauling_Power);
		
		
		pWriter.endTableFormRow();
		pWriter.add("</td></tr></table>");
		//pWriter.endBorder();
		pWriter.add("</div>");
		
		
		
		pWriter.add("<div style='" + RightDivStyle + " min-width:200px;'>");
		//pWriter.startBorder(true);
		pWriter.add(
				"<table><tr><td style='margin-top: 3px; width: 30px; min-width: 30px; max-width: 30px; vertical-align:top'>");
		pWriter.startTableFormRow();
		
		pWriter.addFormRow(Paths._Root_Locomotive._Root_Locomotive_Loco_Permanent_Domain);
		pWriter.addFormRow(Paths._Root_Locomotive._Root_Locomotive_Loco_Traction_Motor_Type);
		pWriter.addFormRow(Paths._Root_Locomotive._Root_Locomotive_Loco_Traction_Code);
		pWriter.addFormRow(Paths._Root_Locomotive._Root_Locomotive_Loco_Boogie_Type);
		pWriter.addFormRow(Paths._Root_Locomotive._Root_Locomotive_Loco_Lease_Type);
		pWriter.addFormRow(Paths._Root_Locomotive._Root_Locomotive_Loco_Status);
		pWriter.addFormRow(Paths._Root_Locomotive._Root_Locomotive_Loco_Receiving_Date);
		pWriter.addFormRow(Paths._Root_Locomotive._Root_Locomotive_Loco_Condemn_Date);
		pWriter.addFormRow(Paths._Root_Locomotive._Root_Locomotive_Loco_POH_Date);
		pWriter.addFormRow(Paths._Root_Locomotive._Root_Locomotive_Loco_Auxilary);
		pWriter.addFormRow(Paths._Root_Locomotive._Root_Locomotive_Loco_Gauge_Type);
		pWriter.addFormRow(Paths._Root_Locomotive._Root_Locomotive_Loco_Rtis_Flag);
		pWriter.addFormRow(Paths._Root_Locomotive._Root_Locomotive_Loco_Hog_Flag);
		pWriter.addFormRow(Paths._Root_Locomotive._Root_Locomotive_Loco_Power_Type);
		pWriter.addFormRow(Paths._Root_Locomotive._Root_Locomotive_Loco_Remmlot_Flag);
		pWriter.addFormRow(Paths._Root_Locomotive._Root_Locomotive_Loco_HRP_Flag);
		pWriter.addFormRow(Paths._Root_Locomotive._Root_Locomotive_Loco_Kavach_Flag);
		pWriter.addFormRow(Paths._Root_Locomotive._Root_Locomotive_Loco_VCD_Flag);
		pWriter.addFormRow(Paths._Root_Locomotive._Root_Locomotive_Loco_Pvt_Owner_Flag);
		pWriter.addFormRow(Paths._Root_Locomotive._Root_Locomotive_Loco_Pvt_Party_Code);
		
		
		
		pWriter.endTableFormRow();
		pWriter.add("</td></tr></table>");
		//pWriter.endBorder();
		pWriter.add("</div>");
		
		Adaptation locomotiveRecord = aContext.getCurrentRecord();
		
		String locoType;
		String locoTraction;
		String locoDescription;
		String locoLength;
		Integer locoHaulingPower;
		String locoAxleLoad;
		String locoAxleUnit;
		String locoGaugeCode;
		
		if(locomotiveRecord!= null)
		{
			Adaptation locoTypeRecord = locomotiveRecord.getSchemaNode().getNode(Paths._Root_Locomotive._Root_Locomotive_Loco_Type)
					.getFacetOnTableReference().getLinkedRecord(locomotiveRecord);
			
			if(locoTypeRecord != null)
			{
				pWriter.add("<div style='" + DownDivStyle + " min-width:100px;'>");
				pWriter.startBorder(true);
				pWriter.add(
						"<table><tr><td style='margin-top: 3px; width: 30px; height:50px; min-width: 30px; max-width: 30px; vertical-align:top'>");
				pWriter.addUILabel(new UIFormLabelSpec("Loco Type"));
				//GetLookupLabel b = new GetLookupLabel();

				locoType = locoTypeRecord.getString(Paths._Root_Loco_Type._Root_Loco_Type_Loco_Type);
				if (locoType == null) {
					locoType = "";
				}
				
				locoTraction = locoTypeRecord.getString(Paths._Root_Loco_Type._Root_Loco_Type_Loco_Traction);
				if(locoTraction == null)
					locoTraction = "";
				
				locoDescription = locoTypeRecord.getString(Paths._Root_Loco_Type._Root_Loco_Type_Loco_Description);
				if(locoDescription == null)
					locoDescription = "";
				
				locoLength = locoTypeRecord.getString(Paths._Root_Loco_Type._Root_Loco_Type_Loco_Length);
				if(locoLength == null)
					locoLength = "";
				
				
				locoHaulingPower = locoTypeRecord.get_int(Paths._Root_Loco_Type._Root_Loco_Type_Loco_Hauling_Power);
				
				
				locoAxleLoad = locoTypeRecord.getString(Paths._Root_Loco_Type._Root_Loco_Type_Loco_Axle_Load);
				if(locoAxleLoad == null)
					locoAxleLoad = "";
				
				locoAxleUnit = locoTypeRecord.getString(Paths._Root_Loco_Type._Root_Loco_Type_Loco_Axle_Unit);
				if(locoAxleUnit == null)
					locoAxleUnit = "";
				
				locoGaugeCode = locoTypeRecord.getString(Paths._Root_Loco_Type._Root_Loco_Type_Loco_Gauge_Code);
				if(locoGaugeCode == null)
					locoGaugeCode = "";

				pWriter.add(
						"<tr><td><div style='white-space: nowrap; color: gray;'>Loco Type</div></td><td><div style='margin-left: 50px;'><span>"
								+ locoType + "</span></td></tr>");
				pWriter.add("<tr><td style='height: 10px;'></td></tr>");
				pWriter.add("<style>td span { margin-left: 10px; }</style>");
				pWriter.add(
						"<tr><td style='white-space: nowrap; color: gray;'>Loco Traction</div></td><td><div style='margin-left: 50px;'><span>"
								+ locoTraction + "</span></td></tr>");
				pWriter.add("<tr><td style='height: 10px;'></td></tr>");
				pWriter.add(
						"<tr><td style='white-space: nowrap; color: gray; '>Loco Description</div></td><td><div style='margin-left: 50px;'><span>"
								+ locoDescription + "</span></td></tr>");
				pWriter.add("<tr><td style='height: 10px;'></td></tr>");
				pWriter.add(
						"<tr><td style='white-space: nowrap; color: gray; '>Loco Length</div></td><td><div style='margin-left: 50px;'><span>"
								+ locoLength + "</span></td></tr>");
				pWriter.add("<tr><td style='height: 10px;'></td></tr>");
				pWriter.add(
						"<tr><td style='white-space: nowrap; color: gray; '>Loco Hauling Power</div></td><td><div style='margin-left: 50px;'><span>"
								+ locoHaulingPower + "</span></td></tr>");
				pWriter.add("<tr><td style='height: 10px;'></td></tr>");
				pWriter.add(
						"<tr><td style='white-space: nowrap; color: gray;'>Loco Axle Load</div></td><td><div style='margin-left: 50px;'><span>"
								+ locoAxleLoad + "</span></td></tr>");
				pWriter.add("<tr><td style='height: 10px;'></td></tr>");
				pWriter.add(
						"<tr><td style='white-space: nowrap; color: gray;'>Loco Axle Unit</div></td><td><div style='margin-left: 50px;'><span>"
								+ locoAxleUnit + "</span></td></tr>");
				pWriter.add("<tr><td style='height: 10px;'></td></tr>");
				pWriter.add(
						"<tr><td style='white-space: nowrap; color: gray;'>Loco Gauge Code</div></td><td><div style='margin-left: 50px;'><span>"
								+ locoGaugeCode + "</span></td></tr>");
				
				pWriter.startTableFormRow();
				pWriter.endTableFormRow();

				pWriter.add("</td></tr></table>");
				pWriter.endBorder();
				pWriter.add("</div>");
			}
		}
	}

}
