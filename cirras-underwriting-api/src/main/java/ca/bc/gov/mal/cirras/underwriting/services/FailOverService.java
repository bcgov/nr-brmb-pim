package ca.bc.gov.mal.cirras.underwriting.services;

import org.springframework.transaction.annotation.Transactional;

public interface FailOverService {
	
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public boolean asyncCheckForMaster(String processName, String nodeId, Integer nodeExpiryMinutes, String userId)
			throws FailOverServiceException;

}
