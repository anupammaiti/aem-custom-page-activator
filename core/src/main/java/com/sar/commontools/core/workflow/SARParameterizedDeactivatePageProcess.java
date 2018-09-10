package com.sar.commontools.core.workflow;

import org.apache.commons.lang.ArrayUtils;
import org.osgi.service.component.annotations.Component;

import com.day.cq.replication.Agent;
import com.day.cq.replication.AgentFilter;
import com.day.cq.replication.ReplicationOptions;
import com.day.cq.wcm.workflow.process.DeactivatePageProcess;
import com.day.cq.workflow.WorkflowException;
import com.day.cq.workflow.WorkflowSession;
import com.day.cq.workflow.exec.WorkItem;
import com.day.cq.workflow.exec.WorkflowProcess;
import com.day.cq.workflow.metadata.MetaDataMap;

@Component(service = WorkflowProcess.class, property = { "process.label= SAR Parameterized Deactivate Page Process" })
public class SARParameterizedDeactivatePageProcess extends DeactivatePageProcess {

	private static final String AGENT_ARG = "replicationAgent";

	private transient ThreadLocal<String[]> agentId = new ThreadLocal<String[]>();

	@Override
	public void execute(WorkItem workItem, WorkflowSession workflowSession, MetaDataMap args) throws WorkflowException {
		agentId.set(args.get(AGENT_ARG, new String[] {}));
		super.execute(workItem, workflowSession, args);

	}

	@Override
	protected ReplicationOptions prepareOptions(ReplicationOptions opts) {

		if (opts == null) {
			opts = new ReplicationOptions();
		}
		opts.setFilter(new AgentFilter() {

			@Override
			public boolean isIncluded(Agent agent) {

				if (ArrayUtils.isEmpty(agentId.get())) {
					return false;
				}
				return ArrayUtils.contains(agentId.get(), agent.getId());
			}
		});
		return opts;
	}

}