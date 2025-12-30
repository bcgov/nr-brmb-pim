package ca.bc.gov.mal.cirras.underwriting.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import ca.bc.gov.mal.cirras.underwriting.data.models.AnnualField;
import ca.bc.gov.mal.cirras.underwriting.data.models.VerifiedYieldAmendment;
import ca.bc.gov.mal.cirras.underwriting.data.models.VerifiedYieldContract;
import ca.bc.gov.mal.cirras.underwriting.data.models.VerifiedYieldContractCommodity;
import ca.bc.gov.mal.cirras.underwriting.data.models.VerifiedYieldContractSimple;
import ca.bc.gov.mal.cirras.underwriting.data.models.VerifiedYieldGrainBasket;
import ca.bc.gov.mal.cirras.underwriting.data.models.VerifiedYieldSummary;
import ca.bc.gov.nrs.wfone.common.model.Message;
import ca.bc.gov.nrs.wfone.common.service.api.ConflictException;
import ca.bc.gov.nrs.wfone.common.service.api.ForbiddenException;
import ca.bc.gov.nrs.wfone.common.service.api.NotFoundException;
import ca.bc.gov.nrs.wfone.common.service.api.ServiceException;
import ca.bc.gov.nrs.wfone.common.service.api.ValidationFailureException;
import ca.bc.gov.nrs.wfone.common.service.api.model.factory.FactoryContext;
import ca.bc.gov.nrs.wfone.common.webade.authentication.WebAdeAuthentication;

public interface CirrasVerifiedYieldService {

	@Transactional(readOnly = true, rollbackFor = Exception.class)
	VerifiedYieldContract<? extends AnnualField, ? extends Message> rolloverVerifiedYieldContract(
		Integer policyId, 
		FactoryContext factoryContext, 
		WebAdeAuthentication authentication
	)
	throws ServiceException, NotFoundException;

	@Transactional(readOnly = true, rollbackFor = Exception.class)
	VerifiedYieldContract<? extends AnnualField, ? extends Message> getVerifiedYieldContract(
		String verifiedYieldContractGuid, 
		FactoryContext factoryContext, 
		WebAdeAuthentication authentication
	)
	throws ServiceException, NotFoundException;	
	
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	VerifiedYieldContract<? extends AnnualField, ? extends Message> createVerifiedYieldContract(
		VerifiedYieldContract<? extends AnnualField, ? extends Message> verifiedYieldContract,
		FactoryContext factoryContext, 
		WebAdeAuthentication authentication
	) 
	throws ServiceException, NotFoundException, ValidationFailureException;

	@Transactional(readOnly = false, rollbackFor = Exception.class)
	VerifiedYieldContract<? extends AnnualField, ? extends Message> updateVerifiedYieldContract(
		String verifiedYieldContractGuid, 
		String optimisticLock,
		VerifiedYieldContract<? extends AnnualField, ? extends Message> verifiedYieldContract, 
		FactoryContext factoryContext, 
		WebAdeAuthentication authentication
	)
	throws ServiceException, NotFoundException, ForbiddenException, ConflictException, ValidationFailureException;

	@Transactional(readOnly = false, rollbackFor = Exception.class)
	void deleteVerifiedYieldContract(
		String verifiedYieldContractGuid, 
		String optimisticLock, 
		WebAdeAuthentication authentication
	)
	throws ServiceException, NotFoundException, ForbiddenException, ConflictException;
	

	@Transactional(readOnly = true, rollbackFor = Exception.class)
	VerifiedYieldContractSimple getVerifiedYieldContractSimple(
		Integer contractId,
		Integer cropYear,
		Integer cropCommodityId,
		Boolean isPedigreeInd,
		Boolean loadVerifiedYieldContractCommodities,
		Boolean loadVerifiedYieldAmendments,
		Boolean loadVerifiedYieldSummaries,
		Boolean loadVerifiedYieldGrainBasket,
		FactoryContext factoryContext, 
		WebAdeAuthentication authentication
	)
	throws ServiceException, NotFoundException;		
}
