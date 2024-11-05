package ca.bc.gov.mal.cirras.underwriting.service.api.v1.impl;

import java.util.Properties;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.mal.cirras.underwriting.model.v1.AnnualField;
import ca.bc.gov.mal.cirras.underwriting.model.v1.VerifiedYieldContract;
import ca.bc.gov.mal.cirras.underwriting.model.v1.VerifiedYieldContractCommodity;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.DeclaredYieldContractCommodityDao;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.DeclaredYieldContractDao;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.PolicyDao;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.DeclaredYieldContractCommodityDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.DeclaredYieldContractDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.PolicyDto;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.service.api.ConflictException;
import ca.bc.gov.nrs.wfone.common.service.api.ForbiddenException;
import ca.bc.gov.nrs.wfone.common.service.api.NotFoundException;
import ca.bc.gov.nrs.wfone.common.service.api.ServiceException;
import ca.bc.gov.nrs.wfone.common.service.api.ValidationFailureException;
import ca.bc.gov.nrs.wfone.common.service.api.model.factory.FactoryContext;
import ca.bc.gov.nrs.wfone.common.webade.authentication.WebAdeAuthentication;
import ca.bc.gov.mal.cirras.underwriting.service.api.v1.CirrasVerifiedYieldService;
import ca.bc.gov.mal.cirras.underwriting.service.api.v1.model.factory.VerifiedYieldContractFactory;
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

			calculateVerifiedYieldContractCommodities(result);
			
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

	private void calculateVerifiedYieldContractCommodities(VerifiedYieldContract<? extends AnnualField> verifiedYieldContract) {
		if ( verifiedYieldContract.getVerifiedYieldContractCommodities() != null && !verifiedYieldContract.getVerifiedYieldContractCommodities().isEmpty() ) {
			for ( VerifiedYieldContractCommodity vycc : verifiedYieldContract.getVerifiedYieldContractCommodities() ) {

				// Calculate Harvested Yield
				Double harvestedYield = null;
				if ( vycc.getSoldYieldDefaultUnit() != null || vycc.getStoredYieldDefaultUnit() != null ) {
					harvestedYield = notNull(vycc.getSoldYieldDefaultUnit(), 0.0) + notNull(vycc.getStoredYieldDefaultUnit(), 0.0);
				}
				
				vycc.setHarvestedYield(harvestedYield);

				// Calculated Yield per Acre
				Double yieldPerAcre = null;
				
				Double effectiveAcres = notNull(vycc.getHarvestedAcresOverride(), vycc.getHarvestedAcres());
				Double effectiveYield = notNull(vycc.getHarvestedYieldOverride(), vycc.getHarvestedYield());

				if ( effectiveAcres != null && effectiveYield != null ) {
					if ( effectiveAcres == 0.0 ) {
						yieldPerAcre = 0.0;
					} else {
						yieldPerAcre = effectiveYield / effectiveAcres;
					}
				}
				
				vycc.setYieldPerAcre(yieldPerAcre);
			}
		}
	}
	
	
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
