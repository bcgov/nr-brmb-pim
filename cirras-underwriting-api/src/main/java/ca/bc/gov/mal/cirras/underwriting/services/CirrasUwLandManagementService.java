package ca.bc.gov.mal.cirras.underwriting.services;

import org.springframework.transaction.annotation.Transactional;

import ca.bc.gov.mal.cirras.underwriting.data.resources.LegalLandRsrc;
import ca.bc.gov.mal.cirras.underwriting.data.resources.RiskAreaListRsrc;
import ca.bc.gov.nrs.wfone.common.service.api.ConflictException;
import ca.bc.gov.nrs.wfone.common.service.api.ForbiddenException;
import ca.bc.gov.nrs.wfone.common.service.api.NotFoundException;
import ca.bc.gov.nrs.wfone.common.service.api.ServiceException;
import ca.bc.gov.nrs.wfone.common.service.api.ValidationFailureException;
import ca.bc.gov.nrs.wfone.common.service.api.model.factory.FactoryContext;
import ca.bc.gov.nrs.wfone.common.webade.authentication.WebAdeAuthentication;

public interface CirrasUwLandManagementService {

	////////////////////////////////////////////////////////////////////
	// Legal Land
	////////////////////////////////////////////////////////////////////
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	LegalLandRsrc createLegalLand(
		LegalLandRsrc legalLand,
		FactoryContext factoryContext, 
		WebAdeAuthentication authentication
	) 
	throws ServiceException, NotFoundException, ValidationFailureException;

	
	@Transactional(readOnly = true, rollbackFor = Exception.class)
	LegalLandRsrc getLegalLand(
			Integer legalLandId, 
		FactoryContext factoryContext, 
		WebAdeAuthentication authentication
	)
	throws ServiceException, NotFoundException;

	@Transactional(readOnly = false, rollbackFor = Exception.class)
	LegalLandRsrc updateLegalLand(
			Integer legalLandId, 
		String optimisticLock,
		LegalLandRsrc legalLand, 
		FactoryContext factoryContext, 
		WebAdeAuthentication authentication
	)
	throws ServiceException, NotFoundException, ForbiddenException, ConflictException, ValidationFailureException;

	@Transactional(readOnly = false, rollbackFor = Exception.class)
	void deleteLegalLand(
			Integer legalLandId, 
		String optimisticLock, 
		WebAdeAuthentication authentication
	)
	throws ServiceException, NotFoundException, ForbiddenException, ConflictException;	

	@Transactional(readOnly = true, rollbackFor = Exception.class)
	RiskAreaListRsrc getRiskAreaList(
			Integer insurancePlanId, 
			FactoryContext context, 
			WebAdeAuthentication authentication
	) 
	throws ServiceException;
}
