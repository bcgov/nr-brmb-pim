package ca.bc.gov.mal.cirras.underwriting.services;

import org.springframework.transaction.annotation.Transactional;

import ca.bc.gov.mal.cirras.underwriting.data.models.RiskArea;
import ca.bc.gov.mal.cirras.underwriting.data.models.RiskAreaList;
import ca.bc.gov.mal.cirras.underwriting.data.models.Field;
import ca.bc.gov.mal.cirras.underwriting.data.models.LegalLand;
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
	LegalLand<? extends Field> createLegalLand(
		LegalLand<? extends Field> legalLand,
		FactoryContext factoryContext, 
		WebAdeAuthentication authentication
	) 
	throws ServiceException, NotFoundException, ValidationFailureException;

	
	@Transactional(readOnly = true, rollbackFor = Exception.class)
	LegalLand<? extends Field> getLegalLand(
			Integer legalLandId, 
		FactoryContext factoryContext, 
		WebAdeAuthentication authentication
	)
	throws ServiceException, NotFoundException;

	@Transactional(readOnly = false, rollbackFor = Exception.class)
	LegalLand<? extends Field> updateLegalLand(
			Integer legalLandId, 
		String optimisticLock,
		LegalLand<? extends Field> legalLand, 
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
	RiskAreaList<? extends RiskArea> getRiskAreaList(
			Integer insurancePlanId, 
			FactoryContext context, 
			WebAdeAuthentication authentication
	) 
	throws ServiceException;
}
