package com.cris.loco_master.form;

import com.cris.loco_master.Paths;
import com.onwbp.adaptation.Adaptation;
import com.onwbp.adaptation.AdaptationTable;
import com.orchestranetworks.query.Query;
import com.orchestranetworks.query.QueryResult;
import com.orchestranetworks.query.Tuple;
import com.orchestranetworks.ui.form.UIFormContext;
import com.orchestranetworks.ui.form.UIFormPane;
import com.orchestranetworks.ui.form.UIFormPaneWriter;

public class LocomotiveSourcesPaneTest implements UIFormPane{

	@Override
	public void writePane(UIFormPaneWriter pWriter, UIFormContext aContext) {
		// TODO Auto-generated method stub
		String floatDivStyle = "float:left; width:100%; ";
		
		pWriter.add("<div style='" + floatDivStyle + " min-width:200px;'>");
		//pWriter.startBorder(true);
		pWriter.add(
				"<table style= 'border-collapse: collapse;'><tr><td style='margin-top: 3px; width: 30px; min-width: 30px; max-width: 30px; vertical-align:top'>");

		
		pWriter.add(
				"<tr><td style='white-space: nowrap; color: gray; border: 1px solid; background: yellow;'>Loco Number</div></td>");
		
		
		pWriter.add(
				"<td style='white-space: nowrap; color: gray; border: 1px solid;background: yellow;'>Loco Source</div></td>");
		
		
		pWriter.add(
				"<td style='white-space: nowrap; color: gray; border: 1px solid;background: yellow;'>Loco Type</div></td>");
		
		
		pWriter.add(
				"<td style='white-space: nowrap; color: gray; border: 1px solid;background: yellow;'>Loco Manufacturer</div></td>");
		
		pWriter.add(
				"<td style='white-space: nowrap; color: gray; border: 1px solid;background: yellow;'>Owning Zone</div></td>");
		
		pWriter.add(
				"<td style='white-space: nowrap; color: gray; border: 1px solid;background: yellow;'>Owning Division</div></td>");
		
		pWriter.add(
				"<td style='white-space: nowrap; color: gray; border: 1px solid;background: yellow;'>Owning Shed</div></td>");
		
		pWriter.add("</tr>");
//		pWriter.startTableFormRow();
//		pWriter.endTableFormRow();
//
//		pWriter.add("</td></tr></table>");
//		//pWriter.endBorder();
//		pWriter.add("</div>");
		
		Adaptation locomotiveRecord = aContext.getCurrentRecord();
		Integer locoNumber = (Integer) locomotiveRecord.get(Paths._Root_Locomotive._Root_Locomotive_Loco_Number);
		
		Adaptation adaptationDataset = locomotiveRecord.getContainer();
		AdaptationTable locoTypeTable = adaptationDataset.getTable(Paths._Root_Loco_Type.getPathInSchema());
		
		
		String query = "Select s.\"$adaptation\" from \"/root/Loco_Sources\" s where FK_AS_STRING('loco_data','/root/Loco_Sources', s.Loco_Number) = '"+locoNumber+"'";
		Query<Tuple> queryTuple = adaptationDataset.createQuery(query);
		QueryResult<Tuple> sourceRecords = queryTuple.getResult();
		
		String locoSourceString = "";
		String locoNumberString ="";
		String locoTypePk ="";
		String locoTypeString ="";
		String locoManufacturerString ="";
		String locoOwningZoneString="";
		String locoOwingDivisionString="";
		String locoOwningShedString="";
		

		Adaptation sourceRecord = null;
		for (Tuple result : sourceRecords) {
			sourceRecord = (Adaptation) result.get(0);
			System.out.println("===loco mfg==="+sourceRecord.getString(Paths._Root_Loco_Sources._Root_Loco_Sources_Loco_Manufacturer));
			System.out.println("===loco type==="+sourceRecord.getString(Paths._Root_Loco_Sources._Root_Loco_Sources_Loco_Type));
			
			locoSourceString = sourceRecord.getString(Paths._Root_Loco_Sources._Root_Loco_Sources_Source);
			locoNumberString = locoNumber.toString();
			locoTypeString = sourceRecord.getString(Paths._Root_Loco_Sources._Root_Loco_Sources_Loco_Type);
			locoManufacturerString = sourceRecord.getString(Paths._Root_Loco_Sources._Root_Loco_Sources_Loco_Manufacturer);
			locoOwningZoneString = sourceRecord.getString(Paths._Root_Loco_Sources._Root_Loco_Sources_Loco_Owning_Zone);
			locoOwingDivisionString = sourceRecord.getString(Paths._Root_Loco_Sources._Root_Loco_Sources_Loco_Owning_Division);
			locoOwningShedString = sourceRecord.getString(Paths._Root_Loco_Sources._Root_Loco_Sources_Loco_Owning_Shed);
			
//			Adaptation locoTypeRecord = locoTypeTable.lookupAdaptationByPrimaryKey(
//					PrimaryKey.parseString(locoTypePk));
//			if(locoTypeRecord!=null)
//				locoTypeString = (String) locoTypeRecord.get(Paths._Root_Loco_Type._Root_Loco_Type_Loco_Type);
//			else
//				locoTypeString = "";

			pWriter.add(
					"<tr><td style='white-space: nowrap; color: gray; border: 1px solid; background: green;'>" + locoNumberString + "</div></td>");
			
			
			pWriter.add(
					"<td style='white-space: nowrap; color: gray; border: 1px solid;'>" + locoSourceString + "</div></td>");
			
			
			pWriter.add(
					"<td style='white-space: nowrap; color: gray; border: 1px solid;'>" + locoTypeString + "</div></td>");
			
			
			pWriter.add(
					"<td style='white-space: nowrap; color: gray; border: 1px solid;'>" + locoManufacturerString + "</div></td>");
			
			pWriter.add(
					"<td style='white-space: nowrap; color: gray; border: 1px solid;'>" + locoOwningZoneString + "</div></td>");
			
			pWriter.add(
					"<td style='white-space: nowrap; color: gray; border: 1px solid;'>" + locoOwingDivisionString + "</div></td>");
			
			pWriter.add(
					"<td style='white-space: nowrap; color: gray; border: 1px solid;'>" + locoOwningShedString + "</div></td>");
			
			
		}
		
		pWriter.startTableFormRow();
		pWriter.endTableFormRow();

		pWriter.add("</td></tr></table>");
		//pWriter.endBorder();
		pWriter.add("</div>");
				
	}

}
