package com.cris.loco_master.utils;

import java.util.List;

import com.orchestranetworks.workflow.ProcessInstanceKey;
import com.orchestranetworks.workflow.PublishedProcessKey;
import com.orchestranetworks.workflow.WorkflowEngine;

public class LocoRecordOperationRestrictionUtils {

	public static boolean checkRunningProcesses(WorkflowEngine wfEngine, String workflowName,
			String dataContextVariable, String xpathExpression) {

		List<ProcessInstanceKey> instanceKeysList = wfEngine.getProcessInstanceKeysForDataContextVariable(
				PublishedProcessKey.forName(workflowName), dataContextVariable, xpathExpression);

		for (ProcessInstanceKey processInstanceKey : instanceKeysList) {
			if (wfEngine.isProcessInstanceRunning(processInstanceKey))
				return true;
		}
		return false;
	}

//	public static boolean countRunningProcesses(WorkflowEngine wfEngine, String workflowName,
//			String dataContextVariable, String xpathExpression) {
//
//		//// ----------- To calculate open work items ----------
//		WorkflowEngine workflowEngine = WorkflowEngine.getFromRepository(repository, context.getSession());
//		List<WorkItem> workItems = workflowEngine.getAllWorkItems();
//		for (Iterator iterator = workItems.iterator(); iterator.hasNext();) {
//			WorkItem workItem = (WorkItem) iterator.next();
//			workflowEngine.isProcessInstanceRunning(workItem.getProcessInstanceKey());
//
//		}
//
//		List<ProcessInstanceKey> instanceKeysList = wfEngine.getProcessInstanceKeysForDataContextVariable(
//				PublishedProcessKey.forName(workflowName), dataContextVariable, xpathExpression);
//
//		for (ProcessInstanceKey processInstanceKey : instanceKeysList) {
//			if (wfEngine.isProcessInstanceRunning(processInstanceKey))
//				return true;
//		}
//		return false;
//	}
}
