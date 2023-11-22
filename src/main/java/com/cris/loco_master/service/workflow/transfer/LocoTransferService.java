package com.cris.loco_master.service.workflow.transfer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.cris.commonjars.constraint.FutureDateRestrictionConstraint;
import com.cris.loco_master.Paths;
import com.cris.loco_master.enumeration.DivisionConstraintEnumeration;
import com.cris.loco_master.enumeration.ShedConstraintEnumeration;
import com.cris.loco_master.utils.DataspaceCreationUtils;
import com.cris.loco_master.utils.LocoRecordOperationRestrictionUtils;
import com.onwbp.adaptation.Adaptation;
import com.onwbp.adaptation.AdaptationHome;
import com.onwbp.adaptation.AdaptationName;
import com.onwbp.adaptation.AdaptationTable;
import com.onwbp.adaptation.PrimaryKey;
import com.onwbp.adaptation.Request;
import com.onwbp.adaptation.RequestResult;
import com.onwbp.base.text.UserMessage;
import com.onwbp.org.apache.commons.io.IOUtils;
import com.orchestranetworks.addon.dama.ext.bean.DigitalAsset;
import com.orchestranetworks.addon.dama.ext.bean.DigitalAssetSpec;
import com.orchestranetworks.addon.dama.ext.bean.DriveContext;
import com.orchestranetworks.addon.dama.ext.drivemanager.DriveManager;
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

public class LocoTransferService implements UserService<TableViewEntitySelection> {

	private DisplayStep currentStep = null;

	LocoTransferService() {
		this.currentStep = new DisplayInputScreen();
	}

	@Override
	public UserServiceEventOutcome processEventOutcome(
			UserServiceProcessEventOutcomeContext<TableViewEntitySelection> context,
			UserServiceEventOutcome eventOutcome) {

		if (!(eventOutcome instanceof DisplayStep.EventOutcome)) {
			return eventOutcome;
		}

		switch ((DisplayStep.EventOutcome) eventOutcome) {

		case DISPLAY_RESULT:
			// final ProcedureResult result = updateRecords(context);
			try {
				transferLocomotive(context);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			this.currentStep = new DisplayResultStep(" of locomotives has been submitted successfully.");
			return null;

		default:
			return null;

		}

	}

	@Override
	public void setupDisplay(UserServiceSetupDisplayContext<TableViewEntitySelection> context,
			UserServiceDisplayConfigurator config) {
		this.currentStep.setupDisplay(context, config);
	}

	@Override
	public void setupObjectContext(UserServiceSetupObjectContext<TableViewEntitySelection> context,
			UserServiceObjectContextBuilder builder) {

		if (!context.isInitialDisplay())
			return;

		Repository repository = Repository.getDefault();
		Request request = context.getEntitySelection().getSelectedRecords();
		RequestResult result = request.execute();
		Adaptation record = result.nextAdaptation();

		String locoShed = (String) record.get(Paths._Root_Locomotive._Root_Locomotive_Loco_Owning_Shed);

		WorkflowEngine wfEngine = WorkflowEngine.getFromRepository(repository, context.getSession());
		List<String> recordList = new ArrayList<String>();

		result = request.execute();

		for (Adaptation record1; (record1 = result.nextAdaptation()) != null;) {
			if (LocoRecordOperationRestrictionUtils.checkRunningProcesses(wfEngine, "loco_transfer_ui", "record",
					record1.toXPathExpression())) {

				recordList.add((String) record1.get(Paths._Root_Locomotive._Root_Locomotive_Loco_Number));
			}
		}

		if (!recordList.isEmpty()) {
			this.currentStep = new DisplayResultStep(" cannot be processed. As Loco Number/s " + recordList
					+ " record/s has already an operation (Loco Transfer) in process.");
			return;
		}

		result = request.execute();

		for (Adaptation record1; (record1 = result.nextAdaptation()) != null;) {
			if (LocoRecordOperationRestrictionUtils.checkRunningProcesses(wfEngine, "loco_condemn_ui", "record",
					record1.toXPathExpression())) {

				recordList.add((String) record1.get(Paths._Root_Locomotive._Root_Locomotive_Loco_Number));
			}
		}

		if (!recordList.isEmpty()) {
			this.currentStep = new DisplayResultStep(" cannot be processed. As Loco Number/s " + recordList
					+ " record/s has already an operation (Loco Condemn) in process.");
			return;
		}

		final HomeKey referenceDataSpaceKey = HomeKey.forBranchName("reference_data");
		final AdaptationHome referenceDataspaceName = repository.lookupHome(referenceDataSpaceKey);

		final AdaptationName referenceDataSetKey = AdaptationName.forName("reference_data");
		final Adaptation referenceDatasetName = referenceDataspaceName.findAdaptationOrNull(referenceDataSetKey);

		// Zone Table
		AdaptationTable zoneTable = referenceDatasetName.getTable(Path.parse("/root/Zone"));

		final BeanDefinition beanDefinition = builder.createBeanDefinition();

		final BeanElement zoneBeanElement = beanDefinition.createElement(TransferServicePaths._zone,
				SchemaTypeName.XS_STRING);
		zoneBeanElement.setLabel("Zone");
		zoneBeanElement.setDescription("List of Zones");
		zoneBeanElement.setMinOccurs(1);
		zoneBeanElement.setMaxOccurs(1);
		zoneBeanElement.addFacetTableRef(zoneTable);

		final BeanElement divisionBeanElement = beanDefinition.createElement(TransferServicePaths._division,
				SchemaTypeName.XS_STRING);
		divisionBeanElement.setLabel("Division");
		divisionBeanElement.setDescription("List of Divisions");
		divisionBeanElement.setMinOccurs(1);
		divisionBeanElement.setMaxOccurs(1);
		divisionBeanElement.addFacetConstraint(DivisionConstraintEnumeration.class);
		// divisionBeanElement.addFacetTableRef(divisionTable);

		final BeanElement shedBeanElement = beanDefinition.createElement(TransferServicePaths._shed,
				SchemaTypeName.XS_STRING);
		shedBeanElement.setLabel("Shed");
		shedBeanElement.setDescription("List of Sheds");
		shedBeanElement.setMinOccurs(1);
		shedBeanElement.setMaxOccurs(1);
		shedBeanElement.addFacetConstraint(ShedConstraintEnumeration.class);
		// shedBeanElement.addFacetTableRef(shedTable);

		final BeanElement shedOfCurrentRecordBeanElement = beanDefinition
				.createElement(TransferServicePaths._shed_of_record, SchemaTypeName.XS_STRING);
		if (locoShed != null)
			shedOfCurrentRecordBeanElement.setDefaultValue(locoShed);
		else
			shedOfCurrentRecordBeanElement.setDefaultValue("");

		final BeanElement transferDateBeanElement = beanDefinition.createElement(TransferServicePaths._transfer_date,
				SchemaTypeName.XS_DATE);
		transferDateBeanElement.setMinOccurs(1);
		transferDateBeanElement.setLabel("Transfer Date");
		transferDateBeanElement.addFacetConstraint(FutureDateRestrictionConstraint.class);

		final BeanElement docNumberBeanElement = beanDefinition
				.createElement(TransferServicePaths._reference_document_number, SchemaTypeName.XS_STRING);
		docNumberBeanElement.setMinOccurs(1);
		docNumberBeanElement.setLabel("Doc Number");

		final BeanElement docDateBeanElement = beanDefinition
				.createElement(TransferServicePaths._reference_document_date, SchemaTypeName.XS_DATE);
		docDateBeanElement.setMinOccurs(1);
		docDateBeanElement.setLabel("Doc Date");

		final BeanElement docAttachementBeanElement = beanDefinition
				.createElement(TransferServicePaths._reference_document_attachement, BeanDefinition.OSD_FILE_UPLOAD);
		docAttachementBeanElement.setMinOccurs(1);
		docAttachementBeanElement.setLabel("Doc Upload");

		builder.registerBean(TransferServicePaths._objectKey, beanDefinition);
	}

	@Override
	public void validate(UserServiceValidateContext<TableViewEntitySelection> arg0) {

	}

	private static void transferLocomotive(
			final UserServiceProcessEventOutcomeContext<TableViewEntitySelection> context) throws Exception {

		Session session = context.getSession();
		Repository repository = Repository.getDefault();
		AdaptationHome dataspace = repository.lookupHome(HomeKey.parse(HomeKey.forBranchName("loco_data").format()));
		AdaptationTable tbLoco = context.getEntitySelection().getTable();

		final ValueContextForValidation valueContext = context.getValueContext(TransferServicePaths._objectKey);

		final String zone = (String) valueContext.getValue(TransferServicePaths._zone);
		final String division = (String) valueContext.getValue(TransferServicePaths._division);
		final String shed = (String) valueContext.getValue(TransferServicePaths._shed);

		final String docNumber = (String) valueContext.getValue(TransferServicePaths._reference_document_number);
		final Date docDate = (Date) valueContext.getValue(TransferServicePaths._reference_document_date);
		UploadedFile uploadedFile = (UploadedFile) valueContext
				.getValue(TransferServicePaths._reference_document_attachement);

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

		launcher = wfEngine.getProcessLauncher(PublishedProcessKey.forName("loco_transfer_ui"));

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
					vcfuContextForUpdate.setValueEnablingPrivilegeForNode("RS - Request sent for transfer",
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

					vcfuRecord.setValueEnablingPrivilegeForNode(zone,
							Paths._Root_Locomotive._Root_Locomotive_Loco_Owning_Zone);
					vcfuRecord.setValueEnablingPrivilegeForNode(division,
							Paths._Root_Locomotive._Root_Locomotive_Loco_Owning_Division);
					vcfuRecord.setValueEnablingPrivilegeForNode(shed,
							Paths._Root_Locomotive._Root_Locomotive_Loco_Owning_Shed);

					vcfuRecord.setValueEnablingPrivilegeForNode("RS - Request sent for transfer",
							Paths._Root_Locomotive._Root_Locomotive_Loco_Status);

					procedureContext.doModifyContent(locoRecord, vcfuRecord);
				}

			};
			ProgrammaticService svc = ProgrammaticService.createForSession(context.getSession(), childDataspace);
			svc.execute(procedure);

			launcher.setInputParameter("childDataSpace", dataspaceCreation.getDataspaceName());
			launcher.setInputParameter("record", xPathExpression);
			launcher.setLabel(UserMessage.createInfo("Loco Transfer"));
			launcher.setDescription(UserMessage.createInfo("Loco Transfer"));
			launcher.launchProcess();

		}

	}
}
