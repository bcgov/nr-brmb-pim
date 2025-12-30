package ca.bc.gov.mal.cirras.underwriting.services;

import java.util.Map;

import org.springframework.transaction.annotation.Transactional;

import ca.bc.gov.mal.cirras.underwriting.data.models.AnnualField;
import ca.bc.gov.mal.cirras.underwriting.data.models.DopYieldContract;
import ca.bc.gov.mal.cirras.underwriting.data.models.GradeModifier;
import ca.bc.gov.mal.cirras.underwriting.data.models.GradeModifierList;
import ca.bc.gov.mal.cirras.underwriting.data.models.YieldMeasUnitTypeCode;
import ca.bc.gov.mal.cirras.underwriting.data.models.YieldMeasUnitTypeCodeList;
import ca.bc.gov.mal.cirras.underwriting.data.entities.YieldMeasUnitConversionDto;
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
	DopYieldContract<? extends AnnualField> rolloverDopYieldContract(
		Integer policyId, 
		FactoryContext factoryContext, 
		WebAdeAuthentication authentication
	)
	throws ServiceException, NotFoundException;

	@Transactional(readOnly = true, rollbackFor = Exception.class)
	DopYieldContract<? extends AnnualField> getDopYieldContract(
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
	DopYieldContract<? extends AnnualField> createDopYieldContract(
		DopYieldContract<? extends AnnualField> dopYieldContract,
		FactoryContext factoryContext, 
		WebAdeAuthentication authentication
	) 
	throws ServiceException, NotFoundException, ValidationFailureException;

	@Transactional(readOnly = false, rollbackFor = Exception.class)
	DopYieldContract<? extends AnnualField> updateDopYieldContract(
		String declaredYieldContractGuid, 
		String optimisticLock,
		DopYieldContract<? extends AnnualField> dopYieldContract, 
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
	YieldMeasUnitTypeCodeList<? extends YieldMeasUnitTypeCode> getYieldMeasUnitTypeCodeList(
			Integer insurancePlanId, 
			FactoryContext context, 
			WebAdeAuthentication authentication
	) 
	throws ServiceException, DaoException;
	
	@Transactional(readOnly = true, rollbackFor = Exception.class)
	double convertEstimatedYieldTest(DopYieldContract<? extends AnnualField> dopYieldContract, 
			String targetUnit, 
			Integer cropCommodityId,
			double valueToConvert) throws ServiceException, DaoException;
	
	@Transactional(readOnly = true, rollbackFor = Exception.class)
	public DopYieldContract<? extends AnnualField> calculateYieldRollupTest(
			DopYieldContract<? extends AnnualField> dopYieldContract) throws ServiceException, DaoException;
	
	@Transactional(readOnly = true, rollbackFor = Exception.class)
	public DopYieldContract<?> calculateYieldContractCommodityForageTest(DopYieldContract<? extends AnnualField> dopYieldContract)
			throws ServiceException, DaoException;	

	@Transactional(readOnly = true, rollbackFor = Exception.class)
	public DopYieldContract<?> calculateYieldFieldRollupForageTest(DopYieldContract<? extends AnnualField> dopYieldContract)
			throws ServiceException, DaoException;	
}
