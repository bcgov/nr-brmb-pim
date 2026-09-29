package ca.bc.gov.mal.cirras.underwriting.listener.service.api.v1;

import ca.bc.gov.nrs.wfone.common.service.api.ServiceException;
import ca.bc.gov.nrs.wfone.common.service.api.model.factory.FactoryContext;

public interface CirrasUnderwritingLandManagementListenerService {
  
	void processCirrasUnderwritingLandManagementEvent(String eventMessageString, FactoryContext factoryContext) throws ServiceException, Throwable;
}
