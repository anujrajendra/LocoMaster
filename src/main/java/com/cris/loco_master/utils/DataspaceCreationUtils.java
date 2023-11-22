package com.cris.loco_master.utils;

import java.util.Locale;
import java.util.concurrent.ThreadLocalRandom;

import com.onwbp.adaptation.AdaptationHome;
import com.onwbp.base.text.UserMessageString;
import com.orchestranetworks.instance.HomeCreationSpec;
import com.orchestranetworks.instance.HomeKey;
import com.orchestranetworks.instance.Repository;
import com.orchestranetworks.service.OperationException;

public class DataspaceCreationUtils {

	AdaptationHome dataspace;
	com.orchestranetworks.instance.Repository repository;
	com.orchestranetworks.service.Session session;
	String dataspaceName;

	
	
	public DataspaceCreationUtils(AdaptationHome dataspace, Repository repository,
			com.orchestranetworks.service.Session session) {
		super();
		this.dataspace = dataspace;
		this.repository = repository;
		this.session = session;
		this.dataspaceName = "L_"+ThreadLocalRandom.current().nextInt()+"";
	}
	
	public String getDataspaceName() {
		return dataspaceName;
	}

	public AdaptationHome getDataspace() throws OperationException {
		HomeCreationSpec childDataspaceSpec = new HomeCreationSpec();
		childDataspaceSpec.setParent(dataspace);
		childDataspaceSpec.setOwner(dataspace.getOwner());
		String childDataspaceName = getDataspaceName(); // "L_"+ThreadLocalRandom.current().nextInt()+"";
		childDataspaceSpec
				.setKey(HomeKey.forBranchName(childDataspaceName));
		childDataspaceSpec.setLabel(new UserMessageString());

		childDataspaceSpec.setDescription((new UserMessageString()).setString(Locale.ENGLISH,
				"" + " (Activation Date: "  + ")"));

		AdaptationHome childDataspace = repository.createHome(childDataspaceSpec, session);

		return childDataspace;
	}

	
}
