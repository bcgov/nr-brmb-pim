package ca.bc.gov.mal.cirras.underwriting.service.api.v1.impl;

import java.util.Properties;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.mal.cirras.underwriting.model.v1.AnnualField;
import ca.bc.gov.mal.cirras.underwriting.model.v1.VerifiedYieldAmendment;
import ca.bc.gov.mal.cirras.underwriting.model.v1.VerifiedYieldContract;
import ca.bc.gov.mal.cirras.underwriting.model.v1.VerifiedYieldContractCommodity;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.ContractedFieldDetailDao;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.DeclaredYieldContractCommodityDao;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.DeclaredYieldContractDao;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.InventoryFieldDao;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.InventorySeededGrainDao;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.PolicyDao;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.ProductDao;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.VerifiedYieldAmendmentDao;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.VerifiedYieldContractCommodityDao;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.VerifiedYieldContractDao;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.ContractedFieldDetailDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.DeclaredYieldContractCommodityDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.DeclaredYieldContractDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.InventoryFieldDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.InventorySeededGrainDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.PolicyDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.ProductDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.VerifiedYieldAmendmentDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.VerifiedYieldContractCommodityDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.VerifiedYieldContractDto;
import ca.bc.gov.nrs.wfone.common.model.Message;
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
	private InventoryFieldDao inventoryFieldDao;
	private InventorySeededGrainDao inventorySeededGrainDao;
	private ContractedFieldDetailDao contractedFieldDetailDao;
	private DeclaredYieldContractDao declaredYieldContractDao;
	private DeclaredYieldContractCommodityDao declaredYieldContractCommodityDao;
	private VerifiedYieldContractDao verifiedYieldContractDao;
	private VerifiedYieldContractCommodityDao verifiedYieldContractCommodityDao;
	private VerifiedYieldAmendmentDao verifiedYieldAmendmentDao;
	private ProductDao productDao;

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

	public void setInventoryFieldDao(InventoryFieldDao inventoryFieldDao) {
		this.inventoryFieldDao = inventoryFieldDao;
	}

	public void setInventorySeededGrainDao(InventorySeededGrainDao inventorySeededGrainDao) {
		this.inventorySeededGrainDao = inventorySeededGrainDao;
	}

	public void setContractedFieldDetailDao(ContractedFieldDetailDao contractedFieldDetailDao) {
		this.contractedFieldDetailDao = contractedFieldDetailDao;
	}
	
	public void setDeclaredYieldContractDao(DeclaredYieldContractDao declaredYieldContractDao) {
		this.declaredYieldContractDao = declaredYieldContractDao;
	}

	public void setDeclaredYieldContractCommodityDao(DeclaredYieldContractCommodityDao declaredYieldContractCommodityDao) {
		this.declaredYieldContractCommodityDao = declaredYieldContractCommodityDao;
	}

	public void setVerifiedYieldContractDao(VerifiedYieldContractDao verifiedYieldContractDao) {
		this.verifiedYieldContractDao = verifiedYieldContractDao;
	}

	public void setVerifiedYieldContractCommodityDao(VerifiedYieldContractCommodityDao verifiedYieldContractCommodityDao) {
		this.verifiedYieldContractCommodityDao = verifiedYieldContractCommodityDao;
	}

	public void setVerifiedYieldAmendmentDao(VerifiedYieldAmendmentDao verifiedYieldAmendmentDao) {
		this.verifiedYieldAmendmentDao = verifiedYieldAmendmentDao;
	}
	
	public void setProductDao(ProductDao productDao) {
		this.productDao = productDao;
	}

	
	@Override
	public VerifiedYieldContract<? extends AnnualField, ? extends Message> rolloverVerifiedYieldContract(Integer policyId,
			FactoryContext factoryContext, WebAdeAuthentication authentication)
			throws ServiceException, NotFoundException {
		logger.debug("<rolloverVerifiedYieldContract");

		// Add verified yield contract
		VerifiedYieldContract<? extends AnnualField, ? extends Message> result = null;

		try {

			PolicyDto policyDto = policyDao.fetch(policyId);

			if (policyDto == null) {
				throw new NotFoundException("Did not find the policy: " + policyId);
			}

			DeclaredYieldContractDto dycDto = declaredYieldContractDao.fetch(policyDto.getDeclaredYieldContractGuid());
			
			if (dycDto == null) {
				throw new NotFoundException("Did not find the dop: " + policyDto.getDeclaredYieldContractGuid());
			}

			List<ProductDto> productDtos = loadProducts(dycDto.getContractId(), dycDto.getCropYear());
			loadDopYieldContractCommodities(dycDto);
			loadFields(dycDto);
			
			result = verifiedYieldContractFactory.getDefaultVerifiedYieldContract(policyDto, dycDto, productDtos, factoryContext, authentication);

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

	private void calculateVerifiedYieldContractCommodities(VerifiedYieldContract<? extends AnnualField, ? extends Message> verifiedYieldContract) {
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

	private void loadFields(DeclaredYieldContractDto dto) throws DaoException {

		List<ContractedFieldDetailDto> fields = contractedFieldDetailDao.selectForVerifiedYield(dto.getContractId(), dto.getCropYear());
		dto.setFields(fields);

		for (ContractedFieldDetailDto cfdDto : dto.getFields()) {
			loadPlantings(cfdDto);
		}
	}

	private void loadFields(VerifiedYieldContractDto dto) throws DaoException {

		List<ContractedFieldDetailDto> fields = contractedFieldDetailDao.selectForVerifiedYield(dto.getContractId(), dto.getCropYear());
		dto.setFields(fields);

		for (ContractedFieldDetailDto cfdDto : dto.getFields()) {
			loadPlantings(cfdDto);
		}
	}
	
	private void loadPlantings(ContractedFieldDetailDto cfdDto) throws DaoException {
		
		List<InventoryFieldDto> plantings = inventoryFieldDao.select(cfdDto.getFieldId(), cfdDto.getCropYear(), cfdDto.getInsurancePlanId());
		cfdDto.setPlantings(plantings);

		for (InventoryFieldDto ifDto : plantings) {

			if ( InsurancePlans.GRAIN.getInsurancePlanId().equals(cfdDto.getInsurancePlanId()) ) {
				loadSeededGrains(ifDto);
			} else if ( InsurancePlans.FORAGE.getInsurancePlanId().equals(cfdDto.getInsurancePlanId()) ) {			
				
			} else {
				throw new ServiceException("Insurance Plan must be GRAIN or FORAGE");
			}
		}
	}
	
	
	private void loadSeededGrains(InventoryFieldDto ifDto) throws DaoException {
		List<InventorySeededGrainDto> inventorySeededGrains = inventorySeededGrainDao.selectForVerifiedYield(ifDto.getInventoryFieldGuid());
		ifDto.setInventorySeededGrains(inventorySeededGrains);
	}
	
	@Override
	public VerifiedYieldContract<? extends AnnualField, ? extends Message> getVerifiedYieldContract(String verifiedYieldContractGuid,
			FactoryContext factoryContext, WebAdeAuthentication authentication)
			throws ServiceException, NotFoundException {

		logger.debug("<getVerifiedYieldContract");

		VerifiedYieldContract<? extends AnnualField, ? extends Message> result = null;

		try {
			VerifiedYieldContractDto dto = verifiedYieldContractDao.fetch(verifiedYieldContractGuid);

			if (dto == null) {
				throw new NotFoundException("Did not find the verified yield contract: " + verifiedYieldContractGuid);
			}

			result = loadVerifiedYieldContract(dto, factoryContext, authentication);

		} catch (DaoException e) {
			throw new ServiceException("DAO threw an exception", e);
		}

		logger.debug(">getVerifiedYieldContract");
		return result;
	}	

	private VerifiedYieldContract<? extends AnnualField, ? extends Message> loadVerifiedYieldContract(
			VerifiedYieldContractDto dto,
			FactoryContext factoryContext, 
			WebAdeAuthentication authentication) throws DaoException {

		List<ProductDto> productDtos = loadProducts(dto.getContractId(), dto.getCropYear());
		loadVerifiedYieldContractCommodities(dto);
		loadVerifiedYieldAmendments(dto);
		loadFields(dto);

		return verifiedYieldContractFactory.getVerifiedYieldContract(dto, productDtos, factoryContext, authentication);
	}
	
	private List<ProductDto> loadProducts(Integer contractId, Integer cropYear) throws DaoException {
		return productDao.getForPolicy(contractId, cropYear);
	}
	
	private void loadVerifiedYieldContractCommodities(VerifiedYieldContractDto dto) throws DaoException {
		List<VerifiedYieldContractCommodityDto> verifiedCommodities = verifiedYieldContractCommodityDao.selectForVerifiedYieldContract(dto.getVerifiedYieldContractGuid());
		dto.setVerifiedYieldContractCommodities(verifiedCommodities);
	}
	
	private void loadVerifiedYieldAmendments(VerifiedYieldContractDto dto) throws DaoException {
		List<VerifiedYieldAmendmentDto> verifiedAmendments = verifiedYieldAmendmentDao.selectForVerifiedYieldContract(dto.getVerifiedYieldContractGuid());
		dto.setVerifiedYieldAmendments(verifiedAmendments);
	}
	
	@Override
	public VerifiedYieldContract<? extends AnnualField, ? extends Message> createVerifiedYieldContract(
			VerifiedYieldContract<? extends AnnualField, ? extends Message> verifiedYieldContract, FactoryContext factoryContext,
			WebAdeAuthentication authentication)
			throws ServiceException, NotFoundException, ValidationFailureException {

		logger.debug("<createVerifiedYieldContract");

		VerifiedYieldContract<? extends AnnualField, ? extends Message> result = null;
		String userId = getUserId(authentication);

		try {
			List<Message> errors = new ArrayList<Message>();
			// errors.addAll(modelValidator.validateInsuranceClaim(insuranceClaim)); // TODO

			if (!errors.isEmpty()) {
				throw new ValidationFailureException(errors);
			}

			String verifiedYieldContractGuid = insertVerifiedYieldContract(verifiedYieldContract, userId);

			if ( InsurancePlans.GRAIN.getInsurancePlanId().equals(verifiedYieldContract.getInsurancePlanId()) ) {
				
				//Calculate harvested yield and yield per acre
				calculateVerifiedYieldContractCommodities(verifiedYieldContract);

				// Verified Yield Contract Commodity
				List<VerifiedYieldContractCommodity> verifiedContractCommodities = verifiedYieldContract.getVerifiedYieldContractCommodities();
				if (verifiedContractCommodities != null && !verifiedContractCommodities.isEmpty()) {
					for (VerifiedYieldContractCommodity verifiedContractCommodity : verifiedContractCommodities) {
						updateVerifiedYieldContractCommodity(verifiedYieldContractGuid, verifiedContractCommodity, null, false, userId);
					}
				}

				// Verified Yield Amendment
				List<VerifiedYieldAmendment> verifiedAmendments = verifiedYieldContract.getVerifiedYieldAmendments();
				if (verifiedAmendments != null && !verifiedAmendments.isEmpty()) {
					for (VerifiedYieldAmendment verifiedAmendment : verifiedAmendments) {

						// Double check that it wasn't added then deleted.
						if ( !Boolean.TRUE.equals(verifiedAmendment.getDeletedByUserInd()) ) {
							updateVerifiedYieldAmendment(verifiedYieldContractGuid, verifiedAmendment, userId);
						}
					}
				}
			
			} else if ( InsurancePlans.FORAGE.getInsurancePlanId().equals(verifiedYieldContract.getInsurancePlanId()) ) {

			} else {
				throw new ServiceException("Insurance Plan must be GRAIN or FORAGE");
			}

			result = getVerifiedYieldContract(verifiedYieldContractGuid, factoryContext, authentication);

		} catch (DaoException e) {
			throw new ServiceException("DAO threw an exception", e);
		}

		logger.debug(">createVerifiedYieldContract");
		return result;
	}	
	
	private String insertVerifiedYieldContract(VerifiedYieldContract<? extends AnnualField, ? extends Message> verifiedYieldContract, String userId)
			throws DaoException {

		VerifiedYieldContractDto dto = new VerifiedYieldContractDto();
		verifiedYieldContractFactory.updateDto(dto, verifiedYieldContract, userId);
		dto.setVerifiedYieldContractGuid(null);
		verifiedYieldContractDao.insert(dto, userId);

		return dto.getVerifiedYieldContractGuid();
	}
	
	private void updateVerifiedYieldContractCommodity(
			String verifiedYieldContractGuid, 
			VerifiedYieldContractCommodity verifiedContractCommodity,
			List<ProductDto> productDtos,
			Boolean updateProductValues,
			String userId) throws DaoException {

		logger.debug("<updateVerifiedYieldContractCommodity");
		
		VerifiedYieldContractCommodityDto dto = null;

		if (verifiedContractCommodity.getVerifiedYieldContractCommodityGuid() != null) {
			dto = verifiedYieldContractCommodityDao.fetch(verifiedContractCommodity.getVerifiedYieldContractCommodityGuid());
		}

		if (dto == null) {
			// Insert if it doesn't exist
			insertVerifiedYieldContractCommodity(verifiedYieldContractGuid, verifiedContractCommodity, userId);
		} else {
			verifiedYieldContractFactory.updateDto(dto, verifiedContractCommodity, productDtos, updateProductValues);

			verifiedYieldContractCommodityDao.update(dto, userId);
		}

		logger.debug(">updateVerifiedYieldContractCommodity");
	}

	private void insertVerifiedYieldContractCommodity(String verifiedYieldContractGuid,
			VerifiedYieldContractCommodity verifiedContractCommodity, String userId) throws DaoException {

		logger.debug("<insertVerifiedYieldContractCommodity");

		VerifiedYieldContractCommodityDto dto = new VerifiedYieldContractCommodityDto();

		verifiedYieldContractFactory.updateDto(dto, verifiedContractCommodity, null, false);

		dto.setVerifiedYieldContractCommodityGuid(null);
		dto.setVerifiedYieldContractGuid(verifiedYieldContractGuid);

		verifiedYieldContractCommodityDao.insert(dto, userId);

		logger.debug(">insertVerifiedYieldContractCommodity");

	}

	private void updateVerifiedYieldAmendment(
			String verifiedYieldContractGuid, 
			VerifiedYieldAmendment verifiedAmendment,
			String userId) throws DaoException {

		logger.debug("<updateVerifiedYieldAmendment");
		
		VerifiedYieldAmendmentDto dto = null;

		if (verifiedAmendment.getVerifiedYieldAmendmentGuid() != null) {
			dto = verifiedYieldAmendmentDao.fetch(verifiedAmendment.getVerifiedYieldAmendmentGuid());
		}

		if (dto == null) {
			// Insert if it doesn't exist
			insertVerifiedYieldAmendment(verifiedYieldContractGuid, verifiedAmendment, userId);
		} else {
			verifiedYieldContractFactory.updateDto(dto, verifiedAmendment);

			verifiedYieldAmendmentDao.update(dto, userId);
		}

		logger.debug(">updateVerifiedYieldAmendment");
	}
	
	
	private void insertVerifiedYieldAmendment(String verifiedYieldContractGuid,
			VerifiedYieldAmendment verifiedAmendment, String userId) throws DaoException {

		logger.debug("<insertVerifiedYieldAmendment");

		VerifiedYieldAmendmentDto dto = new VerifiedYieldAmendmentDto();

		verifiedYieldContractFactory.updateDto(dto, verifiedAmendment);

		dto.setVerifiedYieldAmendmentGuid(null);
		dto.setVerifiedYieldContractGuid(verifiedYieldContractGuid);

		verifiedYieldAmendmentDao.insert(dto, userId);

		logger.debug(">insertVerifiedYieldAmendment");

	}

	private void deleteVerifiedYieldAmendment(VerifiedYieldAmendment verifiedAmendment) throws DaoException {

		logger.debug("<deleteVerifiedYieldAmendment");

		if ( verifiedAmendment.getVerifiedYieldAmendmentGuid() != null ) {
			verifiedYieldAmendmentDao.delete(verifiedAmendment.getVerifiedYieldAmendmentGuid());
		}

		logger.debug(">deleteVerifiedYieldAmendment");

	}
	
	
	@Override
	public VerifiedYieldContract<? extends AnnualField, ? extends Message> updateVerifiedYieldContract(
			String verifiedYieldContractGuid,
			String optimisticLock, 
			VerifiedYieldContract<? extends AnnualField, ? extends Message> verifiedYieldContract,
			FactoryContext factoryContext, 
			WebAdeAuthentication authentication) throws ServiceException,
			NotFoundException, ForbiddenException, ConflictException, ValidationFailureException {

		logger.debug("<updateVerifiedYieldContract");

		VerifiedYieldContract<? extends AnnualField, ? extends Message> result = null;
		String userId = getUserId(authentication);

		try {
			List<Message> errors = new ArrayList<Message>();
			// errors.addAll(modelValidator.validateInsuranceClaim(insuranceClaim)); // TODO

			if (!errors.isEmpty()) {
				throw new ValidationFailureException(errors);
			}

			updateVerifiedYieldContract(verifiedYieldContract, userId);

			if ( InsurancePlans.GRAIN.getInsurancePlanId().equals(verifiedYieldContract.getInsurancePlanId()) ) {

				List<ProductDto> productDtos = null;
				//Get products if the user wants to update the product data
				if(Boolean.TRUE.equals(verifiedYieldContract.getUpdateProductValuesInd())) {
					productDtos = loadProducts(verifiedYieldContract.getContractId(), verifiedYieldContract.getCropYear());
				}
				
				//Calculate harvested yield and yield per acre
				calculateVerifiedYieldContractCommodities(verifiedYieldContract);

				// Verified Yield Contract Commodity
				List<VerifiedYieldContractCommodity> verifiedContractCommodities = verifiedYieldContract.getVerifiedYieldContractCommodities();
				if (verifiedContractCommodities != null && !verifiedContractCommodities.isEmpty()) {
					for (VerifiedYieldContractCommodity verifiedContractCommodity : verifiedContractCommodities) {
						updateVerifiedYieldContractCommodity(verifiedYieldContractGuid, verifiedContractCommodity, productDtos, verifiedYieldContract.getUpdateProductValuesInd(), userId);
					}
				}

				// Verified Yield Amendment
				List<VerifiedYieldAmendment> verifiedAmendments = verifiedYieldContract.getVerifiedYieldAmendments();
				if (verifiedAmendments != null && !verifiedAmendments.isEmpty()) {
					for (VerifiedYieldAmendment verifiedAmendment : verifiedAmendments) {
						if ( Boolean.TRUE.equals(verifiedAmendment.getDeletedByUserInd())) {
							deleteVerifiedYieldAmendment(verifiedAmendment);
						} else {
							updateVerifiedYieldAmendment(verifiedYieldContractGuid, verifiedAmendment, userId);
						}
					}
				}
				
			} else if ( InsurancePlans.FORAGE.getInsurancePlanId().equals(verifiedYieldContract.getInsurancePlanId()) ) {

			} else {
				throw new ServiceException("Insurance Plan must be GRAIN or FORAGE");
			}

			result = getVerifiedYieldContract(verifiedYieldContractGuid, factoryContext, authentication);

		} catch (DaoException e) {
			throw new ServiceException("DAO threw an exception", e);
		}

		logger.debug(">updateVerifiedYieldContract");
		return result;
	}
	
	private void updateVerifiedYieldContract(VerifiedYieldContract<? extends AnnualField, ? extends Message> verifiedYieldContract, String userId)
			throws DaoException, NotFoundException {

		VerifiedYieldContractDto dto = verifiedYieldContractDao.fetch(verifiedYieldContract.getVerifiedYieldContractGuid());

		if (dto == null) {
			throw new NotFoundException("Did not find the verified yield contract: " + verifiedYieldContract.getVerifiedYieldContractGuid());
		}

		verifiedYieldContractFactory.updateDto(dto, verifiedYieldContract, userId);
		verifiedYieldContractDao.update(dto, userId);
	}

	@Override
	public void deleteVerifiedYieldContract(String verifiedYieldContractGuid, String optimisticLock,
		WebAdeAuthentication authentication)
		throws ServiceException, NotFoundException, ForbiddenException, ConflictException {

		logger.debug("<deleteVerifiedYieldContract");

		try {

			deleteVerifiedYieldContract(verifiedYieldContractGuid);

		} catch (DaoException e) {
			throw new ServiceException("DAO threw an exception", e);
		}

		logger.debug(">deleteVerifiedYieldContract");
	}
	
	private void deleteVerifiedYieldContract(String verifiedYieldContractGuid) throws DaoException, NotFoundException {

		VerifiedYieldContractDto dto = verifiedYieldContractDao.fetch(verifiedYieldContractGuid);

		if (dto == null) {
			throw new NotFoundException("Did not find the verified yield contract: " + verifiedYieldContractGuid);
		}
		
		if ( InsurancePlans.GRAIN.getInsurancePlanId().equals(dto.getInsurancePlanId()) ) {
			
			verifiedYieldContractCommodityDao.deleteForVerifiedYieldContract(verifiedYieldContractGuid);
			verifiedYieldAmendmentDao.deleteForVerifiedYieldContract(verifiedYieldContractGuid);

		} else if ( InsurancePlans.FORAGE.getInsurancePlanId().equals(dto.getInsurancePlanId()) ) {

			
		} else {
			throw new ServiceException("Insurance Plan must be GRAIN or FORAGE");
		}

		verifiedYieldContractDao.delete(verifiedYieldContractGuid);
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
