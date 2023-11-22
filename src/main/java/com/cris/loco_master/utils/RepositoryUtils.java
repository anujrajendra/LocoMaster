package com.cris.loco_master.utils;

import java.util.ArrayList;
import java.util.Arrays;

import java.util.List;

import com.onwbp.adaptation.Adaptation;
import com.onwbp.adaptation.AdaptationHome;
import com.onwbp.adaptation.AdaptationName;
import com.onwbp.adaptation.AdaptationTable;
import com.onwbp.adaptation.PrimaryKey;
import com.onwbp.adaptation.RequestResult;
import com.orchestranetworks.instance.HomeKey;
import com.orchestranetworks.instance.Repository;
import com.orchestranetworks.schema.Path;
import com.orchestranetworks.schema.SchemaNode;
import com.orchestranetworks.schema.info.AssociationLink;
import com.orchestranetworks.schema.info.SchemaFacetTableRef;
import com.orchestranetworks.service.LoggingCategory;

public final class RepositoryUtils {

	public static AdaptationHome getDataspace(String pDataspaceName) {

		if (pDataspaceName == null)
			return null;

		Repository repository = Repository.getDefault();
		return getDataspace(repository, pDataspaceName);
	}

	public static AdaptationHome getDataspace(Repository repository, String pDataspaceName) {

		if (repository == null || pDataspaceName == null)
			return null;

		HomeKey hkDataspace = HomeKey.forBranchName(pDataspaceName);
		AdaptationHome dataspace = repository.lookupHome(hkDataspace);
		return dataspace;
	}

	public static Adaptation getDataset(Repository repository, String pDataspaceName, String pDatasetName) {

		if (pDataspaceName == null || pDatasetName == null || repository == null)
			return null;

		AdaptationHome dataspace = getDataspace(repository, pDataspaceName);
		if (dataspace == null) {
			return null;
		}

		return getDataset(dataspace, pDatasetName);
	}

	public static AdaptationTable getTable(Adaptation dataset, String tablePath) {
		if (dataset == null)
			return null;

		try {
			return dataset.getTable(Path.parse(tablePath));
		} catch (Exception e) {
			LoggingCategory.getKernel().info("Failed with message: " + e.getMessage());
			return null;
		}
	}

	public static Adaptation getDataset(String pDataspaceName, String pDatasetName) {

		if (pDataspaceName == null || pDatasetName == null)
			return null;

		AdaptationHome dataspace = getDataspace(pDataspaceName);
		if (dataspace == null) {
			return null;
		}

		return getDataset(dataspace, pDatasetName);
	}

	public static Adaptation getDataset(AdaptationHome dataspace, String pDatasetName) {
		if (dataspace == null || pDatasetName == null)
			return null;

		return dataspace.findAdaptationOrNull(AdaptationName.forName(pDatasetName));
	}

	public static List<String> getTableListInDataset(Adaptation dataset, LoggingCategory logger) {
		SchemaNode rootNode = dataset.getSchemaNode().getNode(Path.parse("/root"));
		List<String> tableMap = getTableList(rootNode, logger);
		return tableMap;
	}

	public static List<String> getTableList(SchemaNode rootNode, LoggingCategory logger) {

		List<String> tableMap = new ArrayList<String>();

		List<SchemaNode> nodes = Arrays.asList(rootNode.getNodeChildren());
		for (SchemaNode node : nodes) {
			if (node.isTableNode()) {
				tableMap.add(node.getPathInSchema().format());
				continue;
			}
			if (node.isComplex()) {
				tableMap.addAll(getTableList(node, logger));
			}
		}

		return tableMap;
	}

	public static List<String> getFieldList(SchemaNode complexNode, LoggingCategory logger) {
		List<String> fieldList = new ArrayList<String>();

		List<SchemaNode> nodes = Arrays.asList(complexNode.getNodeChildren());
		for (SchemaNode node : nodes) {
			if (!node.isAssociationNode() && !node.isSelectNode()) {
				if (node.isComplex())
					fieldList.addAll(getFieldList(node, logger));
				else if (node.isTerminalValue())
					fieldList.add(String.valueOf(Path.SELF.format()) + node.getPathInAdaptation().format());
			}
		}
		return fieldList;
	}

	public static Adaptation getRecordFromPrimaryKey(String dataspaceName, String datasetName, String tablePath,
			String primaryKey) {
		AdaptationHome dataspace = getDataspace(dataspaceName);
		Adaptation dataset = getDataset(dataspace, datasetName);
		AdaptationTable table = getTable(dataset, tablePath);

		Adaptation record = table.lookupAdaptationByPrimaryKey(PrimaryKey.parseString(primaryKey));

		return record;
	}

	public static RequestResult getResultSetOfAssociatedRecords(Path associationNodePath, Adaptation record) {

		SchemaNode tableNode = record.getContainerTable().getTableNode();
		SchemaNode associationNode = tableNode.getNode(associationNodePath);
		AssociationLink associationLink = associationNode.getAssociationLink();
		RequestResult rsAssociatedRecords = associationLink.getAssociationResult(record);
		return rsAssociatedRecords;
	}

	public static List<Adaptation> getListOfAssociatedRecords(Path associationNodePath, Adaptation record) {
		List<Adaptation> listOfRecords = new ArrayList<Adaptation>();
		RequestResult rsAssociatedRecords = getResultSetOfAssociatedRecords(associationNodePath, record);
		Adaptation associatedRecord = null;
		while ((associatedRecord = rsAssociatedRecords.nextAdaptation()) != null) {
			listOfRecords.add(associatedRecord);
		}

		return listOfRecords;
	}

	public static Adaptation getLinkedRecords(Path foreignKeyNodePath, Adaptation record) {

		AdaptationTable tbClientPersonRole = record.getContainerTable();
		SchemaNode fkNode = tbClientPersonRole.getTableNode().getNode(foreignKeyNodePath);
		SchemaFacetTableRef tableRef = fkNode.getFacetOnTableReference();
		Adaptation personRecord = tableRef.getLinkedRecord(record);
		return personRecord;
	}
}