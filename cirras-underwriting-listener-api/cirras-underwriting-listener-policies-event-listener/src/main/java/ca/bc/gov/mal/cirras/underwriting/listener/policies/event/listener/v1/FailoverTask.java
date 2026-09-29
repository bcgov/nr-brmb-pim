package ca.bc.gov.mal.cirras.underwriting.listener.policies.event.listener.v1;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//import ca.bc.gov.nrs.wfone.failover.service.api.v1.async.FailOverService;
import ca.bc.gov.mal.cirras.underwriting.listener.failover.service.api.v1.async.FailOverService;

public class FailoverTask extends TimerTask {

	private static final Logger logger = LoggerFactory.getLogger(FailoverTask.class);

	private FailOverService failOverService;

	protected int nodeExpiryMinutes = 10;

	private String processName;

	private EventListener eventListener;

	private String NODE_NAME;

	private boolean initializedInd;

	public FailoverTask(FailOverService failOverService, EventListener eventListener) {

		this.processName = eventListener.getProcessName();
		this.eventListener = eventListener;
		this.failOverService = failOverService;
	}

	public void init() {
		logger.debug("<init");

		RuntimeMXBean runtimeBean = ManagementFactory.getRuntimeMXBean();
		NODE_NAME = runtimeBean.getName();
		logger.info("NODE_NAME = " + NODE_NAME);

		this.initializedInd = true;

		logger.debug(">init");
	}

	@Override
	public void run() {
		logger.info("<run (Underwriting Policies)");

		if (!this.initializedInd) {

			throw new IllegalStateException("Task has not been initialized.");
		}

		try {

			boolean masterNodeInd = this.failOverService.asyncCheckForMaster(processName, NODE_NAME,
					Integer.valueOf(nodeExpiryMinutes), NODE_NAME);

			if (masterNodeInd) {

				eventListener.startListening();
			} else {
				eventListener.stopListening();
			}

		} catch (Throwable e) {

			logger.error(e.getMessage(), e);
		}

		logger.info(">run");
	}

	@Override
	public boolean cancel() {
		logger.info("<cancel (Underwriting Policies)");
		boolean result;

		eventListener.stopListening();

		result = super.cancel();

		logger.debug(">cancel " + result);
		return result;
	}

}
