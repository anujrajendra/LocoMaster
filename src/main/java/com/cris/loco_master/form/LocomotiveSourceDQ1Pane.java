package com.cris.loco_master.form;

import com.cris.loco_master.Paths;
import com.onwbp.adaptation.Adaptation;
import com.orchestranetworks.query.Query;
import com.orchestranetworks.query.QueryResult;
import com.orchestranetworks.query.Tuple;
import com.orchestranetworks.ui.form.UIFormContext;
import com.orchestranetworks.ui.form.UIFormPane;
import com.orchestranetworks.ui.form.UIFormPaneWriter;

public class LocomotiveSourceDQ1Pane implements UIFormPane{

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
				"<td style='white-space: nowrap; color: gray; border: 1px solid;background: yellow;'>Overall DQ Score</div></td>");
		 
		
		pWriter.add(
				"<td style='white-space: nowrap; color: gray; border: 1px solid;background: yellow;'>Source</div></td>");
		 
		
		pWriter.add(
				"<td style='white-space: nowrap; color: gray; border: 1px solid;background: yellow;'>DQ Score</div></td>");
		//pWriter.add("<tr><td style='height: 10px;'></td></tr>");
		
		pWriter.add(
				"<td style='white-space: nowrap; color: gray; border: 1px solid;background: yellow;'>Loco Type</div></td>");
		
		
		pWriter.add("</tr>");
		
		Adaptation locomotiveRecord = aContext.getCurrentRecord();
		Integer locoNumber = (Integer) locomotiveRecord.get(Paths._Root_Locomotive._Root_Locomotive_Loco_Number);
		
		Adaptation adaptationDataset = locomotiveRecord.getContainer();
		
		
		String query = "Select s.\"$adaptation\" from \"/root/Loco_Sources_Second\" s where FK_AS_STRING('loco_data','/root/Loco_Sources_Second', s.Loco_Number) = '"+locoNumber+"'";
		Query<Tuple> queryTuple = adaptationDataset.createQuery(query);
		QueryResult<Tuple> sourceRecords = queryTuple.getResult();
		
		String locoNumberString ="";
		String overallDQScore = "";
		String locoSourceString = "";
		String dqScore = "";
		String locoTypeString ="";
				
		Integer count = 0;
		Integer countBottomBorder = 0;
		Integer displayFlag = 0;
		Integer printFlag = 0;
		Adaptation sourceRecord = null;
		
		for (Tuple result : sourceRecords) {
			count = count + 1;
		}
		if(count%2 == 0)
			displayFlag = (count/2);
		else
			displayFlag = (count/2) + 1;
		
		System.out.println("==Record count==="+count);
		
		for (Tuple result : sourceRecords) {
			sourceRecord = (Adaptation) result.get(0);
			
			printFlag = printFlag + 1;
			countBottomBorder = countBottomBorder + 1 ;
			
			locoSourceString = sourceRecord.getString(Paths._Root_Loco_Sources_Second._Root_Loco_Sources_Second_Loco_Sources);
			locoNumberString = locoNumber.toString();
			overallDQScore = sourceRecord.getString(Paths._Root_Loco_Sources_Second._Root_Loco_Sources_Second_Overall_DQ_Score);
			dqScore = sourceRecord.getString(Paths._Root_Loco_Sources_Second._Root_Loco_Sources_Second_DQ_Score);
			locoTypeString = sourceRecord.getString(Paths._Root_Loco_Sources_Second._Root_Loco_Sources_Second_Loco_Type);
			pWriter.add("<tr>");
			if(displayFlag == ((printFlag/2)+1))
			{
				pWriter.add(
						"<tr><td style='white-space: nowrap; color: gray; border-left: 1px solid; text-align:center;'>" + locoNumberString + "</div></td>");
				pWriter.add(
						"<td style='white-space: nowrap; color: gray; border-left: 1px solid; border-right: 1px solid; text-align:center;'>" + overallDQScore + "</div></td>");
				printFlag = printFlag + 2;
			}
			else {
				System.out.println("==Count Bottom ==="+count+"---"+countBottomBorder);
				if(count == countBottomBorder)
				{
				pWriter.add(
						"<tr><td style='white-space: nowrap; color: gray; border-left: 1px solid; border-bottom: 1px solid;'>" + "" + "</div></td>");
				pWriter.add(
						"<td style='white-space: nowrap; color: gray; border-left: 1px solid; border-right: 1px solid; border-bottom: 1px solid;'>" + "" + "</div></td>");
				System.out.println("===bottom border===");
				}
				else
				{
				pWriter.add(
						"<tr><td style='white-space: nowrap; color: gray; border-left: 1px solid; '>" + "" + "</div></td>");
				pWriter.add(
						"<td style='white-space: nowrap; color: gray; border-left: 1px solid; border-right: 1px solid;'>" + "" + "</div></td>");
				}
				
			}
			pWriter.add(
					"<td style='white-space: nowrap; color: gray; border: 1px solid;'>" + locoSourceString + "</div></td>");
			 
			
			pWriter.add(
					"<td style='white-space: nowrap; color: gray; border: 1px solid;'>" + dqScore + "</div></td>");
			
			pWriter.add(
					"<td style='white-space: nowrap; color: gray; border: 1px solid;'>" + locoTypeString + "</div></td>");
			
					
		}
		
		pWriter.startTableFormRow();
		pWriter.endTableFormRow();

		pWriter.add("</td></tr></table>");
		//pWriter.endBorder();
		pWriter.add("</div>");
		
		
		
	}
	}

