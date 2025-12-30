package ca.bc.gov.mal.cirras.underwriting.services;

import java.util.Map;

import org.springframework.transaction.annotation.Transactional;

import ca.bc.gov.mal.cirras.underwriting.data.entities.YieldMeasUnitConversionDto;
import ca.bc.gov.mal.cirras.underwriting.data.resources.DopYieldContractRsrc;
import ca.bc.gov.mal.cirras.underwriting.data.resources.YieldMeasUnitTypeCodeListRsrc;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.service.api.ConflictException;
import ca.bc.gov.nrs.wfone.common.service.api.ForbiddenException;
import ca.bc.gov.nrs.wfone.common.service.api.NotFoundException;
import ca.bc.gov.nrs.wfone.common.service.api.ServiceException;
import ca.bc.gov.nrs.wfone.common.service.api.ValidationFailureException;
import ca.bc.gov.nrs.wfone.common.service.api.model.factory.FactoryContext;
import ca.bc.gov.nrs.wfone.common.webade.authentication.WebAdeAuthentication;

public interface CirrasDopYieldService {

	@Transactional(readOnly = true, rollbackFor = Exception.class)
	DopYieldContractRsrc rolloverDopYieldContract(
		Integer policyId, 
		FactoryContext factoryContext, 
		WebAdeAuthentication authentication
	)
	throws ServiceException, NotFoundException;

	@Transactional(readOnly = true, rollbackFor = Exception.class)
	DopYieldContractRsrc getDopYieldContract(
		String declaredYieldContractGuid, 
		FactoryContext factoryContext, 
		WebAdeAuthentication authentication
	)
	throws ServiceException, NotFoundException;	

	@Transactional(readOnly = true, rollbackFor = Exception.class)
	byte[] generateDopReport(
		Integer cropYear,
		Integer insurancePlanId, 
		Integer officeId,
		String policyStatusCode,
		String policyNumber,
		String growerInfo,
		String sortColumn,
		String policyIds, 
		FactoryContext factoryContext, 
		WebAdeAuthentication authentication
	)
	throws ServiceException, NotFoundException;
	
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	DopYieldContractRsrc createDopYieldContract(
		DopYieldContractRsrc dopYieldContract,
		FactoryContext factoryContext, 
		WebAdeAuthentication authentication
	) 
	throws ServiceException, NotFoundException, ValidationFailureException;

	@Transactional(readOnly = false, rollbackFor = Exception.class)
	DopYieldContractRsrc updateDopYieldContract(
		String declaredYieldContractGuid, 
		String optimisticLock,
		DopYieldContractRsrc dopYieldContract, 
		FactoryContext factoryContext, 
		WebAdeAuthentication authentication
	)
	throws ServiceException, NotFoundException, ForbiddenException, ConflictException, ValidationFailureException;

	@Transactional(readOnly = false, rollbackFor = Exception.class)
	void deleteDopYieldContract(
		String declaredYieldContractGuid, 
		String optimisticLock, 
		WebAdeAuthentication authentication
	)
	throws ServiceException, NotFoundException, ForbiddenException, ConflictException;
	
	
	@Transactional(readOnly = true, rollbackFor = Exception.class)
	YieldMeasUnitTypeCodeListRsrc getYieldMeasUnitTypeCodeList(
			Integer insurancePlanId, 
			FactoryContext context, 
			WebAdeAuthentication authentication
	) 
	throws ServiceException, DaoException;
	
	@Transactional(readOnly = true, rollbackFor = Exception.class)
	double convertEstimatedYieldTest(DopYieldContractRsrc dopYieldContract, 
			String targetUnit, 
			Integer cropCommodityId,
			double valueToConvert) throws ServiceException, DaoException;
	
	@Transactional(readOnly = true, rollbackFor = Exception.class)
	public DopYieldContractRsrc calculateYieldRollupTest(
			DopYieldContractRsrc dopYieldContract) throws ServiceException, DaoException;
	
	@Transactional(readOnly = true, rollbackFor = Exception.class)
	public DopYieldContractRsrc calculateYieldContractCommodityForageTest(DopYieldContractRsrc dopYieldContract)
			throws ServiceException, DaoException;	

	@Transactional(readOnly = true, rollbackFor = Exception.class)
	public DopYieldContractRsrc calculateYieldFieldRollupForageTest(DopYieldContractRsrc dopYieldContract)
			throws ServiceException, DaoException;	
}
