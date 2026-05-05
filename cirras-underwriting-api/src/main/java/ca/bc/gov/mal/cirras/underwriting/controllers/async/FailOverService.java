package ca.bc.gov.mal.cirras.underwriting.controllers.async;

import org.springframework.transaction.annotation.Transactional;

import ca.bc.gov.nrs.wfone.common.service.api.NotFoundException;
import ca.bc.gov.nrs.wfone.common.service.api.ServiceException;
import ca.bc.gov.nrs.wfone.common.webade.authentication.WebAdeAuthentication;

public interface FailOverService {
	
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public boolean asyncCheckForMaster(String nodeId, Integer nodeExpiryMinutes, WebAdeAuthentication webAdeAuthentication)
			throws ServiceException, NotFoundException;

}
