package com.cris.loco_master.service.workflow.condemn;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

import com.cris.commonjars.constraint.FutureDateRestrictionConstraint;
import com.cris.loco_master.Paths;
import com.cris.loco_master.constraint.CondemnationDateConstraint;
import com.cris.loco_master.enumeration.CondemnReasonConstraintEnumeration;
import com.cris.loco_master.service.workflow.transfer.ReferenceDocBeanClass;
import com.cris.loco_master.utils.DataspaceCreationUtils;
import com.cris.loco_master.utils.LocoRecordOperationRestrictionUtils;
import com.onwbp.adaptation.Adaptation;
import com.onwbp.adaptation.AdaptationHome;
import com.onwbp.adaptation.AdaptationName;
import com.onwbp.adaptation.AdaptationTable;
import com.onwbp.adaptation.PrimaryKey;
import com.onwbp.adaptation.Request;
import com.onwbp.adaptation.RequestResult;
import com.onwbp.base.text.Nomenclature;
import com.onwbp.base.text.UserMessage;
import com.onwbp.org.apache.commons.io.IOUtils;
import com.orchestranetworks.addon.dama.ext.bean.DigitalAsset;
import com.orchestranetworks.addon.dama.ext.bean.DigitalAssetSpec;
import com.orchestranetworks.addon.dama.ext.bean.DriveContext;
import com.orchestranetworks.addon.dama.ext.drivemanager.DriveManager;
import com.orchestranetworks.addon.dama.ext.exception.DAMException;
import com.orchestranetworks.addon.dama.ext.factory.DriveFactory;
import com.orchestranetworks.addon.dama.ext.factory.DriveFactoryProvider;
import com.orchestranetworks.addon.dama.ext.resource.FileResource;
import com.orchestranetworks.addon.dama.models.MediaType;
import com.orchestranetworks.instance.HomeKey;
import com.orchestranetworks.instance.Repository;
import com.orchestranetworks.instance.ValueContextForValidation;
import com.orchestranetworks.schema.Path;
import com.orchestranetworks.schema.SchemaTypeName;
import com.orchestranetworks.schema.dynamic.BeanDefinition;
import com.orchestranetworks.schema.dynamic.BeanElement;
import com.orchestranetworks.schema.types.file.UploadedFile;
import com.orchestranetworks.service.OperationException;
import com.orchestranetworks.service.Procedure;
import com.orchestranetworks.service.ProcedureContext;
import com.orchestranetworks.service.ProgrammaticService;
import com.orchestranetworks.service.Session;
import com.orchestranetworks.service.ValueContextForUpdate;
import com.orchestranetworks.ui.selection.TableViewEntitySelection;
import com.orchestranetworks.userservice.UserService;
import com.orchestranetworks.userservice.UserServiceDisplayConfigurator;
import com.orchestranetworks.userservice.UserServiceEventOutcome;
import com.orchestranetworks.userservice.UserServiceObjectContextBuilder;
import com.orchestranetworks.userservice.UserServiceProcessEventOutcomeContext;
import com.orchestranetworks.userservice.UserServiceSetupDisplayContext;
import com.orchestranetworks.userservice.UserServiceSetupObjectContext;
import com.orchestranetworks.userservice.UserServiceValidateContext;
import com.orchestranetworks.workflow.ProcessLauncher;
import com.orchestranetworks.workflow.PublishedProcessKey;
import com.orchestranetworks.workflow.WorkflowEngine;

public class LocoCondemnService implements UserService<TableViewEntitySelection> {

	private DisplayStep currentStep = null;

	public LocoCondemnService() {
		super();
		this.currentStep = new DisplayInputScreen();
		;
	}

	@Override
	public UserServiceEventOutcome processEventOutcome(
			UserServiceProcessEventOutcomeContext<TableViewEntitySelection> context,
			UserServiceEventOutcome eventOutcome) {
		// TODO Auto-generated method stub

		if (!(eventOutcome instanceof DisplayStep.EventOutcome)) {
			return eventOutcome;
		}
		final ValueContextForValidation valueContext = context.getValueContext(CondemnServicePaths._objectKey);

		switch ((DisplayStep.EventOutcome) eventOutcome) {

		case DISPLAY_INPUT_SCREEN:
			this.currentStep = new DisplayInputScreen();
			return null;
		case DISPLAY_CONFIRMATION:
			this.currentStep = new DisplayConfirmationStep("Loco Number - "
					+ valueContext.getValue(CondemnServicePaths._loco_number) + "<BR> Loco Traction Type - "
					+ valueContext.getValue(CondemnServicePaths._loco_traction) + "<BR> Loco Commissioning Date - "
					+ valueContext.getValue(CondemnServicePaths._commissioning_date) + "<BR> Loco Condemnation Date - "
					+ valueContext.getValue(CondemnServicePaths._condemnation_date) + "<BR> Loco Type - "
					+ valueContext.getValue(CondemnServicePaths._loco_type) + "<BR> Loco Zone - "
					+ valueContext.getValue(CondemnServicePaths._zone) + "<BR> Loco Division - "
					+ valueContext.getValue(CondemnServicePaths._division) + "\n Loco Shed - "
					+ valueContext.getValue(CondemnServicePaths._shed) + "<BR> Condemnation reason - "
					+ valueContext.getValue(CondemnServicePaths._condemnation_reason)
					+ "<BR> Condemnation proposal type - "
					+ valueContext.getValue(CondemnServicePaths._condemnation_proposal_type));
			return null;
		case DISPLAY_RESULT:

			try {
				condemnLocomotive(context);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			this.currentStep = new com.cris.loco_master.service.workflow.condemn.DisplayResultStep(
					"Loco Number - " + valueContext.getValue(CondemnServicePaths._loco_number)
							+ " Loco Traction Type - " + valueContext.getValue(CondemnServicePaths._loco_traction));
			return null;

		default:
			return null;

		}

	}

	private void condemnLocomotive(UserServiceProcessEventOutcomeContext<TableViewEntitySelection> context)
			throws IOException, DAMException, OperationException {
		// TODO Auto-generated method stub

		Session session = context.getSession();
		Repository repository = Repository.getDefault();
		AdaptationHome dataspace = repository.lookupHome(HomeKey.parse(HomeKey.forBranchName("loco_data").format()));
		AdaptationTable tbLoco = context.getEntitySelection().getTable();

		final ValueContextForValidation valueContext = context.getValueContext(CondemnServicePaths._objectKey);

		final Date condemnationDate = (Date) valueContext.getValue(CondemnServicePaths._condemnation_date);
		final String condemnProposalType = (String) valueContext
				.getValue(CondemnServicePaths._condemnation_proposal_type);

		final String docNumber = (String) valueContext.getValue(CondemnServicePaths._reference_document_number);
		final Date docDate = (Date) valueContext.getValue(CondemnServicePaths._reference_document_date);
		UploadedFile uploadedFile = (UploadedFile) valueContext
				.getValue(CondemnServicePaths._reference_document_attachement);

		final String PREFIX = uploadedFile.getName().substring(0, uploadedFile.getName().lastIndexOf("."));
		final String SUFFIX = uploadedFile.getName().substring(uploadedFile.getName().lastIndexOf("."));

		InputStream inputStream = uploadedFile.getInputStream();

		final File tempFile = File.createTempFile(PREFIX, SUFFIX);
		tempFile.deleteOnExit();
		try (FileOutputStream out = new FileOutputStream(tempFile)) {
			IOUtils.copy(inputStream, out);
		}

		DriveContext driveContext = new DriveContext(tbLoco,
				Paths._Root_Locomotive._Root_Locomotive_Reference_Documents_Reference_Document, context.getSession());

		DriveFactory driveFactory = DriveFactoryProvider.getDriveFactory();
		DriveManager driveManager = driveFactory.getDriveManager(driveContext);

		FileResource fileResource = new FileResource(tempFile);
		DigitalAssetSpec digitalAssetSpec = new DigitalAssetSpec(fileResource);
		digitalAssetSpec.setLabel(UserMessage.createInfo(uploadedFile.getName()));
		digitalAssetSpec.setDescription(UserMessage.createInfo("new description"));
		DigitalAsset newAsset = driveManager.create(digitalAssetSpec);
		String assetId = newAsset.getAssetID();

		WorkflowEngine wfEngine = WorkflowEngine.getFromRepository(repository, context.getSession());
		ProcessLauncher launcher = null;

		launcher = wfEngine.getProcessLauncher(PublishedProcessKey.forName("loco_condemn_ui"));

		Request request = context.getEntitySelection().getSelectedRecords();
		RequestResult result = request.execute();

		for (Adaptation record; (record = result.nextAdaptation()) != null;) {

			DataspaceCreationUtils dataspaceCreation = new DataspaceCreationUtils(dataspace, repository, session);
			AdaptationHome childDataspace = null;
			try {
				childDataspace = dataspaceCreation.getDataspace();

			} catch (OperationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			Adaptation dataset = childDataspace.findAdaptationOrNull(AdaptationName.forName("loco_data"));
			AdaptationTable locomotiveAdaptationTable = dataset.getTable((Path.parse("/root/Locomotive")));

			String locoNumber = (String) record.get(Paths._Root_Locomotive._Root_Locomotive_Loco_Number);
			Adaptation locoRecord = locomotiveAdaptationTable
					.lookupAdaptationByPrimaryKey(PrimaryKey.parseString(locoNumber));

			Adaptation locoRecord1 = tbLoco.lookupAdaptationByPrimaryKey(PrimaryKey.parseString(locoNumber));

			String xPathExpression = locoRecord.toXPathExpression();
			MediaType mediaType = new MediaType();
			mediaType.setAttachment(assetId);

			final Procedure procedure1 = new Procedure() {

				@Override
				public void execute(ProcedureContext procedureContext) throws Exception {

					ValueContextForUpdate vcfuContextForUpdate = procedureContext
							.getContext(locoRecord1.getAdaptationName());
					vcfuContextForUpdate.setValueEnablingPrivilegeForNode("RS - Request sent for condemnation",
							Paths._Root_Locomotive._Root_Locomotive_Loco_Status);

					procedureContext.doModifyContent(locoRecord1, vcfuContextForUpdate);
				}
			};

			ProgrammaticService svc1 = ProgrammaticService.createForSession(context.getSession(), dataspace);
			svc1.execute(procedure1);

			final Procedure procedure = new Procedure() {

				@Override
				public void execute(ProcedureContext procedureContext) throws Exception {

					ValueContextForUpdate vcfuRecord = procedureContext.getContext(locoRecord.getAdaptationName());

					List<ReferenceDocBeanClass> referenceDocBeanList = (List<ReferenceDocBeanClass>) vcfuRecord
							.getValue(Path.parse("./Reference_Documents"));

					ReferenceDocBeanClass firstBean = new ReferenceDocBeanClass();

					firstBean.setReference_Document_Number(docNumber);
					firstBean.setReference_Document_Date(docDate);
					firstBean.setReference_Document(mediaType);

					referenceDocBeanList.add(firstBean);
					vcfuRecord.setValueEnablingPrivilegeForNode(referenceDocBeanList,
							Path.parse("./Reference_Documents"));

					vcfuRecord.setValueEnablingPrivilegeForNode(condemnationDate,
							Paths._Root_Locomotive._Root_Locomotive_Loco_Condemn_Date);
					vcfuRecord.setValueEnablingPrivilegeForNode(condemnProposalType,
							Paths._Root_Locomotive._Root_Locomotive_Loco_Condemn_Proposal_Type);
					vcfuRecord.setValueEnablingPrivilegeForNode("RS - Request sent for condemnation",
							Paths._Root_Locomotive._Root_Locomotive_Loco_Status);

					procedureContext.doModifyContent(locoRecord, vcfuRecord);
				}

			};
			ProgrammaticService svc = ProgrammaticService.createForSession(context.getSession(), childDataspace);
			svc.execute(procedure);

			launcher.setInputParameter("childDataSpace", dataspaceCreation.getDataspaceName());
			launcher.setInputParameter("record", xPathExpression);
			launcher.setLabel(UserMessage.createInfo("Loco Condemn"));
			launcher.setDescription(UserMessage.createInfo("Loco Condemn"));
			launcher.launchProcess();

		}

	}

	@Override
	public void setupDisplay(UserServiceSetupDisplayContext<TableViewEntitySelection> aContext,
			UserServiceDisplayConfigurator aConfig) {
		// TODO Auto-generated method stub

		this.currentStep.setupDisplay(aContext, aConfig);

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
		Date commissioningDate = (Date) record.get(Paths._Root_Locomotive._Root_Locomotive_Loco_Commissioning_Date);
		String locoType = (String) record.get(Paths._Root_Locomotive._Root_Locomotive_Loco_Type);
		String shedCode = (String) record.get(Paths._Root_Locomotive._Root_Locomotive_Loco_Owning_Shed);
		String divisionCode = (String) record.get(Paths._Root_Locomotive._Root_Locomotive_Loco_Owning_Division);
		String zoneCode = (String) record.get(Paths._Root_Locomotive._Root_Locomotive_Loco_Owning_Zone);
		String locoTraction = (String) record.get(Paths._Root_Locomotive._Root_Locomotive_Loco_Traction_Motor_Type);

		WorkflowEngine wfEngine = WorkflowEngine.getFromRepository(repository, context.getSession());
		if (LocoRecordOperationRestrictionUtils.checkRunningProcesses(wfEngine, "loco_condemn_ui", "record",
				record.toXPathExpression())) {
			this.currentStep = new DisplayExitMessageScreen("The selected record Loco Number = " + locoNumber
					+ " has already an operation (Loco Condemn) in process.");
			return;
		}
		if (LocoRecordOperationRestrictionUtils.checkRunningProcesses(wfEngine, "loco_transfer_ui", "record",
				record.toXPathExpression())) {
			this.currentStep = new DisplayExitMessageScreen("The selected record Loco Number = " + locoNumber
					+ " has already an operation (Loco Transfer) in process.");
			return;
		}

		final HomeKey referenceDataSpaceKey = HomeKey.forBranchName("reference_data");
		final AdaptationHome referenceDataspaceName = repository.lookupHome(referenceDataSpaceKey);

		final AdaptationName referenceDataSetKey = AdaptationName.forName("reference_data");
		final Adaptation referenceDatasetName = referenceDataspaceName.findAdaptationOrNull(referenceDataSetKey);

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
						"Please use SLAM application to condemn Locomotive records for the selected shed");
				return;
			}
		if (commissioningDate == null) {
			this.currentStep = new DisplayExitMessageScreen("As Loco Commissioning Date is Empty.");
			return;
		}
		if (locoType == null) {
			this.currentStep = new DisplayExitMessageScreen("As Loco Type is Empty.");
			return;
		}

		final BeanDefinition beanDefinition = builder.createBeanDefinition();

		final BeanElement locoNumberBeanElement = beanDefinition.createElement(CondemnServicePaths._loco_number,
				SchemaTypeName.XS_STRING);
		locoNumberBeanElement.setLabel("Loco Number");
		locoNumberBeanElement.setDefaultValue(locoNumber);

		final BeanElement zoneBeanElement = beanDefinition.createElement(CondemnServicePaths._zone,
				SchemaTypeName.XS_STRING);
		zoneBeanElement.setLabel("Zone");
		zoneBeanElement.setDefaultValue(zoneCode);

		final BeanElement divisionBeanElement = beanDefinition.createElement(CondemnServicePaths._division,
				SchemaTypeName.XS_STRING);
		divisionBeanElement.setLabel("Division");
		divisionBeanElement.setDefaultValue(divisionCode);

		final BeanElement shedBeanElement = beanDefinition.createElement(CondemnServicePaths._shed,
				SchemaTypeName.XS_STRING);
		shedBeanElement.setLabel("Shed");
		shedBeanElement.setDefaultValue(shedCode);

		final BeanElement locoTractionBeanElement = beanDefinition.createElement(CondemnServicePaths._loco_traction,
				SchemaTypeName.XS_STRING);
		locoTractionBeanElement.setLabel("Loco Traction Type");
		locoTractionBeanElement.setDefaultValue(locoTraction);

		final BeanElement commissioningDateElement = beanDefinition
				.createElement(CondemnServicePaths._commissioning_date, SchemaTypeName.XS_DATE);
		commissioningDateElement.setLabel("Commissioning Date");
		commissioningDateElement.setDefaultValue(commissioningDate);

		final BeanElement locoTypeDateElement = beanDefinition.createElement(CondemnServicePaths._loco_type,
				SchemaTypeName.XS_STRING);
		locoTypeDateElement.setLabel("Loco Type");
		locoTypeDateElement.setDefaultValue(locoType);

		final BeanElement condemnDateElement = beanDefinition.createElement(CondemnServicePaths._condemnation_date,
				SchemaTypeName.XS_DATE);
		condemnDateElement.setLabel("Condemnation Date");
		condemnDateElement.setMinOccurs(1);
		condemnDateElement.setMaxOccurs(1);
		condemnDateElement.addFacetConstraint(CondemnationDateConstraint.class);
		condemnDateElement.addFacetConstraint(FutureDateRestrictionConstraint.class);

		Nomenclature<String> condemnProposalTypeNomenclature = new Nomenclature<String>();
		condemnProposalTypeNomenclature.addItemValue("Premature", "Premature");
		condemnProposalTypeNomenclature.addItemValue("Mature", "Mature");

		final BeanElement condemnProposalTypeElement = beanDefinition
				.createElement(CondemnServicePaths._condemnation_proposal_type, SchemaTypeName.XS_STRING);
		condemnProposalTypeElement.setLabel("Condemnation Proposal Type");
		condemnProposalTypeElement.setMinOccurs(1);
		condemnProposalTypeElement.setMaxOccurs(1);
		condemnProposalTypeElement.addFacetEnumeration(condemnProposalTypeNomenclature);

		final BeanElement condemnReasonElement = beanDefinition.createElement(CondemnServicePaths._condemnation_reason,
				SchemaTypeName.XS_STRING);
		condemnReasonElement.setLabel("Condemnation Reason");
		condemnReasonElement.setMinOccurs(1);
		condemnReasonElement.setMaxOccurs(1);
		condemnReasonElement.addFacetConstraint(CondemnReasonConstraintEnumeration.class);

		final BeanElement docNumberBeanElement = beanDefinition
				.createElement(CondemnServicePaths._reference_document_number, SchemaTypeName.XS_STRING);
		docNumberBeanElement.setMinOccurs(1);
		docNumberBeanElement.setLabel("Doc Number");

		final BeanElement docDateBeanElement = beanDefinition
				.createElement(CondemnServicePaths._reference_document_date, SchemaTypeName.XS_DATE);
		docDateBeanElement.setMinOccurs(1);
		docDateBeanElement.setLabel("Doc Date");

		final BeanElement docAttachementBeanElement = beanDefinition
				.createElement(CondemnServicePaths._reference_document_attachement, BeanDefinition.OSD_FILE_UPLOAD);
		docAttachementBeanElement.setMinOccurs(1);
		docAttachementBeanElement.setLabel("Doc Upload");

		builder.registerBean(CondemnServicePaths._objectKey, beanDefinition);
	}

	@Override
	public void validate(UserServiceValidateContext<TableViewEntitySelection> arg0) {
		// TODO Auto-generated method stub

	}

}
