package com.cris.loco_master.service.displaydq;

import java.util.HashMap;

import com.cris.loco_master.Paths;
import com.onwbp.adaptation.Adaptation;
import com.onwbp.adaptation.AdaptationHome;
import com.onwbp.adaptation.AdaptationName;
import com.orchestranetworks.instance.HomeKey;
import com.orchestranetworks.instance.Repository;
import com.orchestranetworks.query.Query;
import com.orchestranetworks.query.QueryResult;
import com.orchestranetworks.query.Tuple;
import com.orchestranetworks.ui.UIButtonSpecNavigation;
import com.orchestranetworks.ui.UICSSClasses;
import com.orchestranetworks.ui.selection.TableViewEntitySelection;
import com.orchestranetworks.userservice.UserServiceDisplayConfigurator;
import com.orchestranetworks.userservice.UserServiceEventContext;
import com.orchestranetworks.userservice.UserServiceEventOutcome;
import com.orchestranetworks.userservice.UserServicePaneContext;
import com.orchestranetworks.userservice.UserServicePaneWriter;
import com.orchestranetworks.userservice.UserServiceSetupDisplayContext;

public class DisplayResultStep implements DisplayStep{

	HashMap<Integer, Adaptation> map = new HashMap<Integer, Adaptation>();
	Integer nextPreviousPressCount = 0;
	
	
	@Override
	public void setupDisplay(UserServiceSetupDisplayContext<TableViewEntitySelection> context,
			UserServiceDisplayConfigurator config) {
		// TODO Auto-generated method stub
		config.setContent(this::writeContent);
		
		{
//			final UIButtonSpecNavigation previousButtonSpec = 
//					config.newActionButton("Previous",this::onPreviousPressed);
//			previousButtonSpec.setDisabled(this.nextPreviousPressCount < 1);
			final UIButtonSpecNavigation closeButtonSpec = 
					config.newCloseButton();
			closeButtonSpec.setDefaultButton(true);
		//	config.setLeftButtons(previousButtonSpec,closeButtonSpec);			
		}
		
//		{
//			final UIButtonSpecNavigation nextButtonSpec = 
//					config.newActionButton("Next",this::onNextPressed);
//			//modifyButtonSpec.setDefaultButton(this.selectedRecordCount > 0);
//			nextButtonSpec.setDisabled(true);
//			config.setRightButtons(nextButtonSpec);
//		}
		
	}

	private void writeContent(UserServicePaneContext aContext, UserServicePaneWriter pWriter) {
	
		pWriter.add("<div").addSafeAttribute("class", UICSSClasses.CONTAINER_WITH_TEXT).add(">");
		
		
		pWriter.add("</div>");
		
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
		
		Repository repository = Repository.getDefault();
		final HomeKey dataSpaceKey = HomeKey.forBranchName("loco_data");
		final AdaptationHome dataspaceName = repository.lookupHome(dataSpaceKey);
		
		final AdaptationName dataSetKey = AdaptationName.forName("loco_data");
		final Adaptation datasetName = dataspaceName.findAdaptationOrNull(dataSetKey);
		
		String locoQuery = "Select s.\"$adaptation\" from \"/root/Locomotive\" s ";
		Query<Tuple> locoQueryTuple = datasetName.createQuery(locoQuery);
		QueryResult<Tuple> locoRecords = locoQueryTuple.getResult();
		
		//String predicate = Paths._Root_Locomotive._Root_Locomotive_Loco_Number.format();
		
		Adaptation locoRecord = null;
		Integer count1 = 0;
		// next page logic
		for (Tuple result1 : locoRecords) {
			locoRecord = (Adaptation) result1.get(0);
			count1 = count1 + 1;
			map.put(count1, locoRecord);
			
		}
		
		for (Tuple locoResult : locoRecords) {
			locoRecord = (Adaptation) locoResult.get(0);
			
		//Adaptation locomotiveRecord = aContext.getCurrentRecord();
		Integer locoNumber = (Integer) locoRecord.get(Paths._Root_Locomotive._Root_Locomotive_Loco_Number);
		
		//Adaptation adaptationDataset = locomotiveRecord.getContainer();
		
		
		String query = "Select s.\"$adaptation\" from \"/root/Loco_Sources_Second\" s where FK_AS_STRING('loco_data','/root/Loco_Sources_Second', s.Loco_Number) = '"+locoNumber+"'";
		Query<Tuple> queryTuple = datasetName.createQuery(query);
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
		
		
		
		for (Tuple result1 : sourceRecords) {
			count = count + 1;
			}
		if(count%2 == 0)
			displayFlag = (count/2);
		else
			displayFlag = (count/2) + 1;
		
		
		Adaptation sourceRecord = null;
		for (Tuple sourceResult : sourceRecords) {
			sourceRecord = (Adaptation) sourceResult.get(0);
			
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
				if(count == countBottomBorder)
				{
				pWriter.add(
						"<tr><td style='white-space: nowrap; color: gray; border-left: 1px solid; border-bottom: 1px solid;'>" + "" + "</div></td>");
				pWriter.add(
						"<td style='white-space: nowrap; color: gray; border-left: 1px solid; border-right: 1px solid; border-bottom: 1px solid;'>" + "" + "</div></td>");
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
	}
		
		
		pWriter.startTableFormRow();
		pWriter.endTableFormRow();

		pWriter.add("</td></tr></table>");
		//pWriter.endBorder();
		pWriter.add("</div>");
	}
	
	private void customWriteContent(UserServicePaneContext aContext, UserServicePaneWriter pWriter, HashMap<Integer, Adaptation> map) {
		
	}

	private UserServiceEventOutcome onPreviousPressed(UserServiceEventContext userserviceeventcontext1) {
		nextPreviousPressCount = nextPreviousPressCount + 1;
		return null;
	}

	private UserServiceEventOutcome onNextPressed(UserServiceEventContext userserviceeventcontext1) {
		nextPreviousPressCount = nextPreviousPressCount - 1;
		return null;
	}

}
