package ca.bc.gov.mal.cirras.underwriting.listener.service.api.v1;

import ca.bc.gov.mal.pit.common.service.api.ServiceException;
import ca.bc.gov.mal.pit.common.service.api.model.factory.FactoryContext;

public interface CirrasUnderwritingPoliciesListenerService {
  
	void processCirrasUnderwritingPoliciesEvent(String eventMessageString, FactoryContext factoryContext) throws ServiceException, Throwable;
}
