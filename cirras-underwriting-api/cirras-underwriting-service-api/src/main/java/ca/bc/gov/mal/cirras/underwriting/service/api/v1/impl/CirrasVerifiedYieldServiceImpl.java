package ca.bc.gov.mal.cirras.underwriting.service.api.v1.impl;

import java.util.Properties;
import java.util.stream.Collectors;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.mal.cirras.underwriting.model.v1.AnnualField;
import ca.bc.gov.mal.cirras.underwriting.model.v1.DopYieldContract;
import ca.bc.gov.mal.cirras.underwriting.model.v1.DopYieldContractCommodity;
import ca.bc.gov.mal.cirras.underwriting.model.v1.DopYieldContractCommodityForage;
import ca.bc.gov.mal.cirras.underwriting.model.v1.DopYieldFieldForage;
import ca.bc.gov.mal.cirras.underwriting.model.v1.DopYieldFieldForageCut;
import ca.bc.gov.mal.cirras.underwriting.model.v1.DopYieldFieldGrain;
import ca.bc.gov.mal.cirras.underwriting.model.v1.DopYieldFieldRollup;
import ca.bc.gov.mal.cirras.underwriting.model.v1.DopYieldFieldRollupForage;
import ca.bc.gov.mal.cirras.underwriting.model.v1.UnderwritingComment;
import ca.bc.gov.mal.cirras.underwriting.model.v1.VerifiedYieldContract;
import ca.bc.gov.mal.cirras.underwriting.model.v1.YieldMeasUnitTypeCode;
import ca.bc.gov.mal.cirras.underwriting.model.v1.YieldMeasUnitTypeCodeList;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.ContractedFieldDetailDao;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.DeclaredYieldContractCommodityDao;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.DeclaredYieldContractCommodityForageDao;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.DeclaredYieldContractDao;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.DeclaredYieldFieldDao;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.DeclaredYieldFieldForageDao;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.DeclaredYieldFieldRollupDao;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.DeclaredYieldFieldRollupForageDao;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.InventoryContractCommodityDao;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.InventoryFieldDao;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.InventorySeededForageDao;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.InventorySeededGrainDao;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.PolicyDao;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.UnderwritingCommentDao;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.YieldMeasUnitConversionDao;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.YieldMeasUnitTypeCodeDao;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.ContractedFieldDetailDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.DeclaredYieldContractCommodityDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.DeclaredYieldContractCommodityForageDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.DeclaredYieldContractDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.DeclaredYieldFieldDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.DeclaredYieldFieldForageDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.DeclaredYieldFieldRollupDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.DeclaredYieldFieldRollupForageDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.InventoryContractCommodityDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.InventoryFieldDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.InventorySeededForageDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.InventorySeededGrainDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.PolicyDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.UnderwritingCommentDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.YieldMeasUnitConversionDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.YieldMeasUnitTypeCodeDto;
import ca.bc.gov.nrs.wfone.common.model.Message;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;
import ca.bc.gov.nrs.wfone.common.service.api.ConflictException;
import ca.bc.gov.nrs.wfone.common.service.api.ForbiddenException;
import ca.bc.gov.nrs.wfone.common.service.api.NotFoundException;
import ca.bc.gov.nrs.wfone.common.service.api.ServiceException;
import ca.bc.gov.nrs.wfone.common.service.api.ValidationFailureException;
import ca.bc.gov.nrs.wfone.common.service.api.model.factory.FactoryContext;
import ca.bc.gov.nrs.wfone.common.webade.authentication.WebAdeAuthentication;
import ca.bc.gov.mal.cirras.underwriting.service.api.v1.CirrasDopYieldService;
import ca.bc.gov.mal.cirras.underwriting.service.api.v1.CirrasVerifiedYieldService;
import ca.bc.gov.mal.cirras.underwriting.service.api.v1.model.factory.DopYieldContractFactory;
import ca.bc.gov.mal.cirras.underwriting.service.api.v1.model.factory.InventoryContractFactory;
import ca.bc.gov.mal.cirras.underwriting.service.api.v1.model.factory.VerifiedYieldContractFactory;
import ca.bc.gov.mal.cirras.underwriting.service.api.v1.model.factory.YieldMeasUnitTypeCodeFactory;
import ca.bc.gov.mal.cirras.underwriting.service.api.v1.reports.JasperReportService;
import ca.bc.gov.mal.cirras.underwriting.service.api.v1.reports.JasperReportServiceException;
import ca.bc.gov.mal.cirras.underwriting.service.api.v1.util.InventoryServiceEnums;
import ca.bc.gov.mal.cirras.underwriting.service.api.v1.util.InventoryServiceEnums.InsurancePlans;
import ca.bc.gov.mal.cirras.underwriting.service.api.v1.validation.ModelValidator;

public class CirrasVerifiedYieldServiceImpl implements CirrasVerifiedYieldService {

	private static final Logger logger = LoggerFactory.getLogger(CirrasVerifiedYieldServiceImpl.class);

	private Properties applicationProperties;

	private ModelValidator modelValidator;
	
	public static final String MaximumResultsProperty = "maximum.results";

	public static final int DefaultMaximumResults = 800;

	// factories
	private VerifiedYieldContractFactory verifiedYieldContractFactory;

	// daos
	private PolicyDao policyDao;
	private DeclaredYieldContractDao declaredYieldContractDao;
	private DeclaredYieldContractCommodityDao declaredYieldContractCommodityDao;

	public void setApplicationProperties(Properties applicationProperties) {
		this.applicationProperties = applicationProperties;
	}

	public void setModelValidator(ModelValidator modelValidator) {
		this.modelValidator = modelValidator;
	}
	
	public void setVerifiedYieldContractFactory(VerifiedYieldContractFactory verifiedYieldContractFactory) {
		this.verifiedYieldContractFactory = verifiedYieldContractFactory;
	}

	public void setPolicyDao(PolicyDao policyDao) {
		this.policyDao = policyDao;
	}

	public void setDeclaredYieldContractDao(DeclaredYieldContractDao declaredYieldContractDao) {
		this.declaredYieldContractDao = declaredYieldContractDao;
	}

	public void setDeclaredYieldContractCommodityDao(DeclaredYieldContractCommodityDao declaredYieldContractCommodityDao) {
		this.declaredYieldContractCommodityDao = declaredYieldContractCommodityDao;
	}

	@Override
	public VerifiedYieldContract<? extends AnnualField> rolloverVerifiedYieldContract(Integer policyId,
			FactoryContext factoryContext, WebAdeAuthentication authentication)
			throws ServiceException, NotFoundException {
		logger.debug("<rolloverVerifiedYieldContract");

		// Add verified yield contract
		VerifiedYieldContract<? extends AnnualField> result = null;

		try {

			PolicyDto policyDto = policyDao.fetch(policyId);

			if (policyDto == null) {
				throw new NotFoundException("Did not find the policy: " + policyId);
			}

			DeclaredYieldContractDto dycDto = declaredYieldContractDao.fetch(policyDto.getDeclaredYieldContractGuid());
			
			if (dycDto == null) {
				throw new NotFoundException("Did not find the dop: " + policyDto.getDeclaredYieldContractGuid());
			}

			loadDopYieldContractCommodities(dycDto);
			
			result = verifiedYieldContractFactory.getDefaultVerifiedYieldContract(policyDto, dycDto, factoryContext, authentication);

			// TODO: Run calcs.
			
		} catch (DaoException e) {
			throw new ServiceException("DAO threw an exception", e);
		}

		logger.debug(">rolloverVerifiedYieldContract");
		return result;
	}

	private void loadDopYieldContractCommodities(DeclaredYieldContractDto dto) throws DaoException {

		if ( InsurancePlans.GRAIN.getInsurancePlanId().equals(dto.getInsurancePlanId()) ) {
			List<DeclaredYieldContractCommodityDto> dopCommodities = declaredYieldContractCommodityDao.selectForDeclaredYieldContract(dto.getDeclaredYieldContractGuid());
			dto.setDeclaredYieldContractCommodities(dopCommodities);
		} else { 
			throw new ServiceException("Insurance Plan must be GRAIN");
		}
	}


	// TODO
/*	private void updateDeclaredYieldContractCommodity(String declaredYieldContractGuid,
			DopYieldContract<? extends AnnualField> dopYieldContract, DopYieldContractCommodity dopContractCommodity,
			Map<String, YieldMeasUnitConversionDto> ymucMap, String userId) throws DaoException {

		logger.debug("<updateDeclaredYieldContractCommodity");

		// Calculate default units
		if (dopContractCommodity.getSoldYield() == null || dopContractCommodity.getSoldYield() == 0) {
			dopContractCommodity.setSoldYieldDefaultUnit(dopContractCommodity.getSoldYield());
		} else {
			dopContractCommodity.setSoldYieldDefaultUnit(
					convertEstimatedYield(dopYieldContract, dopYieldContract.getDefaultYieldMeasUnitTypeCode(),
							dopContractCommodity.getCropCommodityId(), dopContractCommodity.getSoldYield(), ymucMap));
		}

		if (dopContractCommodity.getStoredYield() == null || dopContractCommodity.getStoredYield() == 0) {
			dopContractCommodity.setStoredYieldDefaultUnit(dopContractCommodity.getStoredYield());
		} else {
			dopContractCommodity.setStoredYieldDefaultUnit(
					convertEstimatedYield(dopYieldContract, dopYieldContract.getDefaultYieldMeasUnitTypeCode(),
							dopContractCommodity.getCropCommodityId(), dopContractCommodity.getStoredYield(), ymucMap));
		}

		DeclaredYieldContractCommodityDto dto = null;

		if (dopContractCommodity.getDeclaredYieldContractCommodityGuid() != null) {
			dto = declaredYieldContractCommodityDao.fetch(dopContractCommodity.getDeclaredYieldContractCommodityGuid());
		}

		if (dto == null) {
			// Insert if it doesn't exist
			insertDeclaredYieldContractCommodity(declaredYieldContractGuid, dopContractCommodity, userId);
		} else {
			dopYieldContractFactory.updateDto(dto, dopContractCommodity);

			declaredYieldContractCommodityDao.update(dto, userId);
		}

		logger.debug(">updateDeclaredYieldContractCommodity");
	}
*/

	@Override
	public VerifiedYieldContract<? extends AnnualField> getVerifiedYieldContract(String verifiedYieldContractGuid,
			FactoryContext factoryContext, WebAdeAuthentication authentication)
			throws ServiceException, NotFoundException {

		// TODO Auto-generated method stub
		throw new ServiceException("Not implemented");
	}	
	
	@Override
	public VerifiedYieldContract<? extends AnnualField> createVerifiedYieldContract(
			VerifiedYieldContract<? extends AnnualField> verifiedYieldContract, FactoryContext factoryContext,
			WebAdeAuthentication authentication)
			throws ServiceException, NotFoundException, ValidationFailureException {
		// TODO Auto-generated method stub
		throw new ServiceException("Not implemented");
	}	

	@Override
	public VerifiedYieldContract<? extends AnnualField> updateVerifiedYieldContract(String verifiedYieldContractGuid,
		String optimisticLock, VerifiedYieldContract<? extends AnnualField> verifiedYieldContract,
		FactoryContext factoryContext, WebAdeAuthentication authentication) throws ServiceException,
		NotFoundException, ForbiddenException, ConflictException, ValidationFailureException {
	// TODO Auto-generated method stub
		throw new ServiceException("Not implemented");
	}

	@Override
	public void deleteVerifiedYieldContract(String verifiedYieldContractGuid, String optimisticLock,
		WebAdeAuthentication authentication)
		throws ServiceException, NotFoundException, ForbiddenException, ConflictException {
	// TODO Auto-generated method stub
		throw new ServiceException("Not implemented");
	}

	//
	// The "proof of concept" REST service doesn't have any security
	//
	private String getUserId(WebAdeAuthentication authentication) {
		String userId = "DEFAULT_USERID";

		if (authentication != null) {
			userId = authentication.getUserId();
			authentication.getClientId();
		}

		return userId;
	}

	private Double notNull(Double value, Double defaultValue) {
		return (value == null) ? defaultValue : value;
	}

	private Integer notNull(Integer value, Integer defaultValue) {
		return (value == null) ? defaultValue : value;
	}

	private String notNull(String value, String defaultValue) {
		return (value == null) ? defaultValue : value;
	}

}
