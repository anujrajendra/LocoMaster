package com.cris.loco_master.utils;

import com.onwbp.base.text.UserMessage;
import com.orchestranetworks.instance.Repository;
import com.orchestranetworks.service.Session;
import com.orchestranetworks.workflow.ProcessLauncher;
import com.orchestranetworks.workflow.PublishedProcessKey;
import com.orchestranetworks.workflow.WorkflowEngine;

public class WorkflowProcessLauncherUtils {

	Repository repository;
	Session session;
	String workflowName;
	String workflowLabel;
	String workflowDesc;

	public WorkflowProcessLauncherUtils(Repository repository, Session session, String workflowName,
			String workflowLabel, String workflowDesc) {
		super();
		this.repository = repository;
		this.session = session;
		this.workflowName = workflowName;
		this.workflowLabel = workflowLabel;
		this.workflowDesc = workflowDesc;
	}

	public ProcessLauncher getProcessLauncher() {
		WorkflowEngine wfEngine = WorkflowEngine.getFromRepository(repository, session);

		PublishedProcessKey processKey = PublishedProcessKey.forName(workflowName);
		ProcessLauncher processLauncher = wfEngine.getProcessLauncher(processKey);
		processLauncher.setLabel(UserMessage.createInfo(workflowLabel));
		processLauncher.setDescription(UserMessage.createInfo(workflowDesc));

		
		return processLauncher;
		
	}
	
}
