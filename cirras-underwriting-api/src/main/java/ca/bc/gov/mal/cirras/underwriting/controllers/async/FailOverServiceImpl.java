package ca.bc.gov.mal.cirras.underwriting.controllers.async;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.nrs.wfone.common.service.api.NotFoundException;
import ca.bc.gov.nrs.wfone.common.service.api.ServiceException;
import ca.bc.gov.nrs.wfone.common.webade.authentication.WebAdeAuthentication;

public class FailOverServiceImpl implements FailOverService {

	private static final Logger logger = LoggerFactory.getLogger(FailOverServiceImpl.class);

	@Override
	public boolean asyncCheckForMaster(String nodeId, Integer nodeExpiryMinutes, WebAdeAuthentication webAdeAuthentication)
			throws ServiceException, NotFoundException {
		logger.debug("<asyncCheckForMaster");
		boolean result = true;

		// TODO: Replace with real impl.		
		
		logger.debug(">asyncCheckForMaster " + result);
		return result;
	}

}
