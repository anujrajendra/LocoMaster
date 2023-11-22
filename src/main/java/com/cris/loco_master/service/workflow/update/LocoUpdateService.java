package com.cris.loco_master.service.workflow.update;

import com.cris.loco_master.Paths;
import com.cris.loco_master.utils.LocoRecordOperationRestrictionUtils;
import com.onwbp.adaptation.Adaptation;
import com.onwbp.adaptation.AdaptationHome;
import com.onwbp.adaptation.AdaptationName;
import com.onwbp.adaptation.AdaptationTable;
import com.onwbp.adaptation.PrimaryKey;
import com.onwbp.adaptation.Request;
import com.onwbp.adaptation.RequestResult;
import com.onwbp.base.text.UserMessage;
import com.orchestranetworks.instance.HomeKey;
import com.orchestranetworks.instance.Repository;
import com.orchestranetworks.schema.Path;
import com.orchestranetworks.service.LoggingCategory;
import com.orchestranetworks.ui.UIButtonSpecSubmit;
import com.orchestranetworks.ui.selection.TableViewEntitySelection;
import com.orchestranetworks.userservice.UserService;
import com.orchestranetworks.userservice.UserServiceDisplayConfigurator;
import com.orchestranetworks.userservice.UserServiceEventContext;
import com.orchestranetworks.userservice.UserServiceEventOutcome;
import com.orchestranetworks.userservice.UserServiceNext;
import com.orchestranetworks.userservice.UserServiceObjectContextBuilder;
import com.orchestranetworks.userservice.UserServicePane;
import com.orchestranetworks.userservice.UserServicePaneContext;
import com.orchestranetworks.userservice.UserServicePaneWriter;
import com.orchestranetworks.userservice.UserServiceProcessEventOutcomeContext;
import com.orchestranetworks.userservice.UserServiceSetupDisplayContext;
import com.orchestranetworks.userservice.UserServiceSetupObjectContext;
import com.orchestranetworks.userservice.UserServiceValidateContext;
import com.orchestranetworks.workflow.ProcessLauncher;
import com.orchestranetworks.workflow.ProcessLauncherResult;
import com.orchestranetworks.workflow.PublishedProcessKey;
import com.orchestranetworks.workflow.WorkItemKey;
import com.orchestranetworks.workflow.WorkflowEngine;

public class LocoUpdateService implements UserService<TableViewEntitySelection> {

	private WorkItemKey wiKey;
	private DisplayStep currentStep = null;

	public LocoUpdateService() {
		super();

	}

	@Override
	public UserServiceEventOutcome processEventOutcome(
			UserServiceProcessEventOutcomeContext<TableViewEntitySelection> aContext,
			UserServiceEventOutcome eOutcome) {

		if (!(eOutcome instanceof DisplayStep.EventOutcome) && currentStep != null) {

			return eOutcome;
		}
		// TODO Auto-generated method stub
		try {

			Repository repository = aContext.getRepository();
			WorkflowEngine wfEngine = WorkflowEngine.getFromRepository(repository, aContext.getSession());
			ProcessLauncher launcher = null;

			launcher = wfEngine.getProcessLauncher(PublishedProcessKey.forName("loco_update_ui"));
			Request selectedRecord = aContext.getEntitySelection().getSelectedRecords();
			RequestResult result = selectedRecord.execute();

			if (result.isSizeGreaterOrEqual(1)) {

				Adaptation productRecord = result.nextAdaptation();

				String xPathExpression = productRecord.toXPathExpression();
				// LoggingCategory.getWorkflow().info("XPath Expression: " + xPathExpression);
				launcher.setInputParameter("record", xPathExpression);
			} else
				return UserServiceNext.nextClose();

			launcher.setLabel(UserMessage.createInfo("Loco Record Modification"));
			launcher.setDescription(UserMessage.createInfo("Loco Record Modification"));
			ProcessLauncherResult launcherResult = launcher.launchProcessWithResult();
			if (launcherResult != null) {
				wiKey = launcherResult.getWorkItemKey();
			}
		} catch (Exception e) {
			LoggingCategory.getWorkflow().info(e.getMessage());
		}
		if (wiKey != null)
			return UserServiceNext.nextWorkItem(wiKey);
		else
			return UserServiceNext.nextClose();
	}

	@Override
	public void setupDisplay(UserServiceSetupDisplayContext<TableViewEntitySelection> aContext,
			UserServiceDisplayConfigurator aConfig) {
		// TODO Auto-generated method stub

		if (this.currentStep != null) {
			this.currentStep.setupDisplay(aContext, aConfig);
		} else {
			aConfig.setContent(new UserServicePane() {

				@Override
				public void writePane(UserServicePaneContext aContext, UserServicePaneWriter aWriter) {

					aWriter.add("<div style=\"text-align: center; padding: 25% 0;\">");
					aWriter.add("<br/>");
					aWriter.add("</div>");

					UIButtonSpecSubmit btn = aWriter.newSubmitButton("Submit", this::submitForm);

					btn.setId("InitiateWorkflow");
					aWriter.add("<div style='display:none;'>");
					aWriter.addButton(btn);
					aWriter.add("</div>");
					aWriter.addJS_cr("document.getElementById('InitiateWorkflow').click(); ");
				}

				protected UserServiceEventOutcome submitForm(final UserServiceEventContext context) {
					return UserServiceNext.nextClose();
				}
			});
		}
	}

	@Override
	public void setupObjectContext(UserServiceSetupObjectContext<TableViewEntitySelection> context,
			UserServiceObjectContextBuilder builder) {
		// TODO Auto-generated method stub
		if (!context.isInitialDisplay())
			return;
		Repository repository = Repository.getDefault();
		Request request = context.getEntitySelection().getSelectedRecords();
		RequestResult result = request.execute();
		Adaptation record = result.nextAdaptation();
		String locoNumber = (String) record.get(Paths._Root_Locomotive._Root_Locomotive_Loco_Number);
		String shedCode = (String) record.get(Paths._Root_Locomotive._Root_Locomotive_Loco_Owning_Shed);

		WorkflowEngine wfEngine = WorkflowEngine.getFromRepository(repository, context.getSession());

		if (LocoRecordOperationRestrictionUtils.checkRunningProcesses(wfEngine, "loco_create_api", "record",
				"/root/Locomotive[./Loco_Number=\"" + record.get(Paths._Root_Locomotive._Root_Locomotive_Loco_Number)
						+ "\"]")) {
			this.currentStep = new DisplayExitMessageScreen(" The selected record Loco Number = " + locoNumber
					+ " has already an operation (Loco Update API) in process.");
			return;
		}
		if (LocoRecordOperationRestrictionUtils.checkRunningProcesses(wfEngine, "loco_update_ui", "record",
				record.toXPathExpression())) {
			this.currentStep = new DisplayExitMessageScreen(" The selected record Loco Number = " + locoNumber
					+ " has already an operation (Loco Update) in process.");
			return;
		}

		if (LocoRecordOperationRestrictionUtils.checkRunningProcesses(wfEngine, "loco_condemn_ui", "record",
				record.toXPathExpression())) {
			this.currentStep = new DisplayExitMessageScreen(" The selected record Loco Number = " + locoNumber
					+ " has already an operation (Loco Condemn) in process.");
			return;
		}

		if (LocoRecordOperationRestrictionUtils.checkRunningProcesses(wfEngine, "loco_transfer_ui", "record",
				record.toXPathExpression())) {
			this.currentStep = new DisplayExitMessageScreen(" The selected record Loco Number = " + locoNumber
					+ " has already an operation (Loco Transfer) in process.");
			return;
		}

		final HomeKey referenceDataSpaceKey = HomeKey.forBranchName("reference_data");
		final AdaptationHome referenceDataspaceName = repository.lookupHome(referenceDataSpaceKey);

		final AdaptationName referenceDataSetKey = AdaptationName.forName("reference_data");
		final Adaptation referenceDatasetName = referenceDataspaceName.findAdaptationOrNull(referenceDataSetKey);

		// Zone Table
		AdaptationTable shedTable = referenceDatasetName.getTable(Path.parse("/root/Shed"));
		Adaptation shedRecord = null;
		String sourceSystemName = "";
		if (shedCode != null)
			shedRecord = shedTable.lookupAdaptationByPrimaryKey(PrimaryKey.parseString(shedCode));
		if (shedRecord != null) {
			sourceSystemName = (String) shedRecord.get(Path.parse("./Source_System_Name"));
		}
		if (sourceSystemName != null)
			if (sourceSystemName.equalsIgnoreCase("SLAM")) {
				this.currentStep = new DisplayExitMessageScreen(
						"Please use SLAM application to update Locomotive records for the selected shed");
				return;
			}
	}

	@Override
	public void validate(UserServiceValidateContext<TableViewEntitySelection> arg0) {
		// TODO Auto-generated method stub

	}

}
