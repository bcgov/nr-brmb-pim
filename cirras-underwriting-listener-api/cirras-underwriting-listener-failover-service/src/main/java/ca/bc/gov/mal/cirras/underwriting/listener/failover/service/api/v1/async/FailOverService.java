package ca.bc.gov.mal.cirras.underwriting.listener.failover.service.api.v1.async;

import org.springframework.transaction.annotation.Transactional;

public interface FailOverService {
	
	@Transactional(readOnly = false, transactionManager = "failoverTransactionManager")
	public boolean asyncCheckForMaster(String processName, String nodeId, Integer nodeExpiryMinutes, String userId)
			throws ServiceException;

}
