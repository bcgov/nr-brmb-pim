package ca.bc.gov.mal.cirras.underwriting.service.api.v1.impl;

import java.util.Properties;
import java.util.stream.Collectors;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.mal.cirras.underwriting.model.v1.AnnualField;
import ca.bc.gov.mal.cirras.underwriting.model.v1.UnderwritingComment;
import ca.bc.gov.mal.cirras.underwriting.model.v1.VerifiedYieldAmendment;
import ca.bc.gov.mal.cirras.underwriting.model.v1.VerifiedYieldContract;
import ca.bc.gov.mal.cirras.underwriting.model.v1.VerifiedYieldContractCommodity;
import ca.bc.gov.mal.cirras.underwriting.model.v1.VerifiedYieldGrainBasket;
import ca.bc.gov.mal.cirras.underwriting.model.v1.VerifiedYieldSummary;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.ContractedFieldDetailDao;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.DeclaredYieldContractCommodityDao;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.DeclaredYieldContractCommodityForageDao;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.DeclaredYieldContractDao;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.InventoryFieldDao;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.InventorySeededForageDao;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.InventorySeededGrainDao;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.PolicyDao;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.ProductDao;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.UnderwritingCommentDao;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.VerifiedYieldAmendmentDao;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.VerifiedYieldContractCommodityDao;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.VerifiedYieldContractDao;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.VerifiedYieldSummaryDao;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.VerifiedYieldGrainBasketDao;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.ContractedFieldDetailDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.DeclaredYieldContractCommodityDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.DeclaredYieldContractCommodityForageDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.DeclaredYieldContractDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.InventoryFieldDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.InventorySeededForageDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.InventorySeededGrainDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.PolicyDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.ProductDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.UnderwritingCommentDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.VerifiedYieldAmendmentDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.VerifiedYieldContractCommodityDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.VerifiedYieldContractDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.VerifiedYieldSummaryDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.VerifiedYieldGrainBasketDto;
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
import ca.bc.gov.mal.cirras.underwriting.service.api.v1.CirrasVerifiedYieldService;
import ca.bc.gov.mal.cirras.underwriting.service.api.v1.model.factory.InventoryContractFactory;
import ca.bc.gov.mal.cirras.underwriting.service.api.v1.model.factory.VerifiedYieldContractFactory;
import ca.bc.gov.mal.cirras.underwriting.service.api.v1.util.CommodityCoverageCode;
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
	private InventoryContractFactory inventoryContractFactory;

	// daos
	private PolicyDao policyDao;
	private InventoryFieldDao inventoryFieldDao;
	private InventorySeededGrainDao inventorySeededGrainDao;
	private InventorySeededForageDao inventorySeededForageDao;
	private ContractedFieldDetailDao contractedFieldDetailDao;
	private DeclaredYieldContractDao declaredYieldContractDao;
	private DeclaredYieldContractCommodityDao declaredYieldContractCommodityDao;
	private DeclaredYieldContractCommodityForageDao declaredYieldContractCommodityForageDao;
	private VerifiedYieldContractDao verifiedYieldContractDao;
	private VerifiedYieldContractCommodityDao verifiedYieldContractCommodityDao;
	private VerifiedYieldAmendmentDao verifiedYieldAmendmentDao;
	private VerifiedYieldSummaryDao verifiedYieldSummaryDao;
	private VerifiedYieldGrainBasketDao verifiedYieldGrainBasketDao;
	private ProductDao productDao;
	private UnderwritingCommentDao underwritingCommentDao;

	public void setApplicationProperties(Properties applicationProperties) {
		this.applicationProperties = applicationProperties;
	}

	public void setModelValidator(ModelValidator modelValidator) {
		this.modelValidator = modelValidator;
	}
	
	public void setVerifiedYieldContractFactory(VerifiedYieldContractFactory verifiedYieldContractFactory) {
		this.verifiedYieldContractFactory = verifiedYieldContractFactory;
	}

	public void setInventoryContractFactory(InventoryContractFactory inventoryContractFactory) {
		this.inventoryContractFactory = inventoryContractFactory;
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

	public void setInventorySeededForageDao(InventorySeededForageDao inventorySeededForageDao) {
		this.inventorySeededForageDao = inventorySeededForageDao;
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
	
	public void setDeclaredYieldContractCommodityForageDao(DeclaredYieldContractCommodityForageDao declaredYieldContractCommodityForageDao) {
		this.declaredYieldContractCommodityForageDao = declaredYieldContractCommodityForageDao;
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

	public void setVerifiedYieldSummaryDao(VerifiedYieldSummaryDao verifiedYieldSummaryDao) {
		this.verifiedYieldSummaryDao = verifiedYieldSummaryDao;
	}
	
	public void setVerifiedYieldGrainBasketDao(VerifiedYieldGrainBasketDao verifiedYieldGrainBasketDao) {
		this.verifiedYieldGrainBasketDao = verifiedYieldGrainBasketDao;
	}
	
	public void setProductDao(ProductDao productDao) {
		this.productDao = productDao;
	}
	
	public void setUnderwritingCommentDao(UnderwritingCommentDao underwritingCommentDao) {
		this.underwritingCommentDao = underwritingCommentDao;
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

			if ( InsurancePlans.GRAIN.getInsurancePlanId().equals(policyDto.getInsurancePlanId()) ||
					InsurancePlans.FORAGE.getInsurancePlanId().equals(policyDto.getInsurancePlanId())) {
	
				DeclaredYieldContractDto dycDto = declaredYieldContractDao.fetch(policyDto.getDeclaredYieldContractGuid());
				
				if (dycDto == null) {
					throw new NotFoundException("Did not find the dop: " + policyDto.getDeclaredYieldContractGuid());
				}
	
				List<ProductDto> productDtos = loadProducts(dycDto.getContractId(), dycDto.getCropYear());

				loadDopYieldContractCommodities(dycDto);
				loadFields(dycDto);

				if ( InsurancePlans.FORAGE.getInsurancePlanId().equals(policyDto.getInsurancePlanId()) ) {
					rollupVerifiedYield(dycDto);
				}
				
				result = verifiedYieldContractFactory.getDefaultVerifiedYieldContract(policyDto, dycDto, productDtos, factoryContext, authentication);

				calculateVerifiedYieldContractCommodities(result);
				
			} else{ 
				throw new ServiceException("Insurance Plan must be GRAIN or FORAGE");
			}	
			
			
		} catch (DaoException e) {
			throw new ServiceException("DAO threw an exception", e);
		}

		logger.debug(">rolloverVerifiedYieldContract");
		return result;
	}
	
	private void rollupVerifiedYield(VerifiedYieldContract<? extends AnnualField, ? extends Message> vyc) {
		
		if ( InsurancePlans.FORAGE.getInsurancePlanId().equals(vyc.getInsurancePlanId()) ) {
		
			if(vyc.getVerifiedYieldContractCommodities() != null && vyc.getVerifiedYieldContractCommodities().size() > 0) {

				List<VerifiedYieldContractCommodity> rolledUpRows = new ArrayList<VerifiedYieldContractCommodity>();
				//Get all rolled up rows (commodity level)
				rolledUpRows = vyc.getVerifiedYieldContractCommodities().stream()
								.filter(x -> x.getCommodityTypeCode() == null && Boolean.TRUE.equals(x.getIsRolledupInd()))
								.collect(Collectors.toList());

				if(rolledUpRows != null && rolledUpRows.size() > 0) {
			
					for(VerifiedYieldContractCommodity vycc : rolledUpRows){

						//Set default values for override columns
						vycc.setHarvestedAcresOverride(null);
						vycc.setHarvestedYieldOverride(null);
						
						//Get all rows that are rolled up (commodity type level)
						List<VerifiedYieldContractCommodity> rowsToRollup = getVerifiedYieldContractCommoditiesToRollup(vycc.getCropCommodityId(), vyc.getVerifiedYieldContractCommodities());
						
						//Check if at least one row has an override value -> Only then an override value is set
						//Harvested Acres
						List<VerifiedYieldContractCommodity> rowsWithOverride = rowsToRollup.stream()
								.filter(x -> x.getHarvestedAcresOverride() != null)
								.collect(Collectors.toList());
						
						Boolean rollupHarvestedAcresOverride = (rowsWithOverride != null && rowsWithOverride.size() > 0);
						
						//Harvested Yield
						rowsWithOverride = rowsToRollup.stream()
								.filter(x -> x.getHarvestedYieldOverride() != null)
								.collect(Collectors.toList());
						
						Boolean rollupHarvestedYieldOverride = (rowsWithOverride != null && rowsWithOverride.size() > 0);
						
						if(rowsToRollup != null && rowsToRollup.size() > 0) {
							
							for(VerifiedYieldContractCommodity row : rowsToRollup) {
								
								if(Boolean.TRUE.equals(rollupHarvestedAcresOverride)) {
									//Take override value if it exists otherwise take the calculated value
									Double effectiveAcres = notNull(row.getHarvestedAcresOverride(), row.getHarvestedAcres());
									
									//Can be null if both values at commodity level are null
									if(effectiveAcres != null) {
										vycc.setHarvestedAcresOverride(notNull(vycc.getHarvestedAcresOverride(), 0.0) + effectiveAcres);
									}
								}
								
								if(Boolean.TRUE.equals(rollupHarvestedYieldOverride)) {
									//Take override value if it exists otherwise take the calculated value
									Double effectiveYield = notNull(row.getHarvestedYieldOverride(), row.getHarvestedYield());
									//Can be null if both values at commodity level are null
									if(effectiveYield != null) {
										vycc.setHarvestedYieldOverride(notNull(vycc.getHarvestedYieldOverride(), 0.0) + effectiveYield);
									}
								}
							}
						}
					}
				}
			}
		}
	}
	
	private List<VerifiedYieldContractCommodity> getVerifiedYieldContractCommoditiesToRollup(Integer cropCommodityId, List<VerifiedYieldContractCommodity> vyccList) {
		
		if(vyccList != null && !vyccList.isEmpty()) {
		
			return vyccList.stream()
					.filter(x -> x.getCropCommodityId().equals(cropCommodityId) && Boolean.FALSE.equals(x.getIsRolledupInd()))
					.collect(Collectors.toList());
		} else {
			return null;
		}
	}

	private void rollupVerifiedYield(DeclaredYieldContractDto dycDto) {
		//At this point there is no rolled up row yet
		if(dycDto.getDeclaredYieldContractCommodityForageList() != null ) {
			
			List<DeclaredYieldContractCommodityForageDto> rolledUpList = new ArrayList<DeclaredYieldContractCommodityForageDto>();
			
			DeclaredYieldContractCommodityForageDto newDto = null;
			Integer cropCommodityId = null;
			
			for(DeclaredYieldContractCommodityForageDto dto: dycDto.getDeclaredYieldContractCommodityForageList()) {
				//Records are sorted by commodity
				if(cropCommodityId == null) {
					//Set row values
					cropCommodityId = dto.getCropCommodityId();

					newDto = createRollupRow(dto);
					rolledUpList.add(newDto);

				} else if(cropCommodityId == dto.getCropCommodityId()) {

					//Rollup values
					newDto.setHarvestedAcres(notNull(newDto.getHarvestedAcres(), 0.0) + notNull(dto.getHarvestedAcres(), 0.0));
					newDto.setQuantityHarvestedTons(notNull(newDto.getQuantityHarvestedTons(), 0.0) + notNull(dto.getQuantityHarvestedTons(), 0.0));
					newDto.setTotalFieldAcres(notNull(newDto.getTotalFieldAcres(), 0.0) + notNull(dto.getTotalFieldAcres(), 0.0));
					
				} else if(cropCommodityId != dto.getCropCommodityId()) {
					
					//create new row and add it to the list
					newDto = createRollupRow(dto);
					rolledUpList.add(newDto);

					//Set commodity id
					cropCommodityId = dto.getCropCommodityId();

				}
				
				rolledUpList.add(dto);
			}
			
			//Add temporary list with rolled up rows to declared yield contract
			dycDto.setDeclaredYieldContractCommodityForageList(rolledUpList);
		}
	}

	private DeclaredYieldContractCommodityForageDto createRollupRow(DeclaredYieldContractCommodityForageDto dto) {
		DeclaredYieldContractCommodityForageDto newDto;
		newDto = new DeclaredYieldContractCommodityForageDto();
		newDto.setCropCommodityId(dto.getCropCommodityId());
		newDto.setCropCommodityName(dto.getCropCommodityName());
		newDto.setCommodityTypeCode(null);
		newDto.setCommodityTypeDescription(null);
		newDto.setIsRolledupInd(true);
		newDto.setHarvestedAcres(dto.getHarvestedAcres());
		newDto.setQuantityHarvestedTons(dto.getQuantityHarvestedTons());
		newDto.setTotalFieldAcres(dto.getTotalFieldAcres());

		return newDto;
	}

	private void loadDopYieldContractCommodities(DeclaredYieldContractDto dto) throws DaoException {

		if ( InsurancePlans.GRAIN.getInsurancePlanId().equals(dto.getInsurancePlanId()) ) {
			List<DeclaredYieldContractCommodityDto> dopCommodities = declaredYieldContractCommodityDao.selectForDeclaredYieldContract(dto.getDeclaredYieldContractGuid());
			dto.setDeclaredYieldContractCommodities(dopCommodities);
		} else if ( InsurancePlans.FORAGE.getInsurancePlanId().equals(dto.getInsurancePlanId()) ) {
			List<DeclaredYieldContractCommodityForageDto> dopCommodities = declaredYieldContractCommodityForageDao.selectForDeclaredYieldContract(dto.getDeclaredYieldContractGuid(), DeclaredYieldContractCommodityForageDao.sortOrder.CommodityNameCommodityType);
			dto.setDeclaredYieldContractCommodityForageList(dopCommodities);
		} else{ 
			throw new ServiceException("Insurance Plan must be GRAIN or FORAGE");
		}
	}

	private void calculateVerifiedYieldContractCommodities(VerifiedYieldContract<? extends AnnualField, ? extends Message> verifiedYieldContract) {
		if ( verifiedYieldContract.getVerifiedYieldContractCommodities() != null && !verifiedYieldContract.getVerifiedYieldContractCommodities().isEmpty() ) {
			for ( VerifiedYieldContractCommodity vycc : verifiedYieldContract.getVerifiedYieldContractCommodities() ) {

				if ( InsurancePlans.GRAIN.getInsurancePlanId().equals(verifiedYieldContract.getInsurancePlanId()) ) {
					// Calculate Harvested Yield
					Double harvestedYield = null;
					if ( vycc.getSoldYieldDefaultUnit() != null || vycc.getStoredYieldDefaultUnit() != null ) {
						harvestedYield = notNull(vycc.getSoldYieldDefaultUnit(), 0.0) + notNull(vycc.getStoredYieldDefaultUnit(), 0.0);
					}
					
					vycc.setHarvestedYield(harvestedYield);
					
				}

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
				loadSeededForage(ifDto);
			} else {
				throw new ServiceException("Insurance Plan must be GRAIN or FORAGE");
			}
		}
	}
	
	private void loadSeededGrains(InventoryFieldDto ifDto) throws DaoException {
		List<InventorySeededGrainDto> inventorySeededGrains = inventorySeededGrainDao.selectForVerifiedYield(ifDto.getInventoryFieldGuid());
		ifDto.setInventorySeededGrains(inventorySeededGrains);
	}
	
	private void loadSeededForage(InventoryFieldDto ifDto) throws DaoException {
		List<InventorySeededForageDto> inventorySeededForages = inventorySeededForageDao.selectForVerifiedYield(ifDto.getInventoryFieldGuid());
		ifDto.setInventorySeededForages(inventorySeededForages);
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
		loadVerifiedYieldSummaries(dto);
		loadVerifiedYieldGrainBasket(dto);

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
	
	private void loadVerifiedYieldSummaries(VerifiedYieldContractDto dto) throws DaoException {
		List<VerifiedYieldSummaryDto> verifiedSummaries = verifiedYieldSummaryDao.selectForVerifiedYieldContract(dto.getVerifiedYieldContractGuid());
		dto.setVerifiedYieldSummaries(verifiedSummaries);
		
		//Get comments
		for (VerifiedYieldSummaryDto vyDto : dto.getVerifiedYieldSummaries()) {
			loadUwComments(vyDto);
		}
	}
	
	private void loadUwComments(VerifiedYieldSummaryDto vyDto) throws DaoException {
		//Returning all comments of a verified yield summary record
		List<UnderwritingCommentDto> uwComments = underwritingCommentDao.selectForVerifiedYieldSummary(vyDto.getVerifiedYieldSummaryGuid());
		vyDto.setUwComments(uwComments);
	}
	
	private void loadVerifiedYieldGrainBasket(VerifiedYieldContractDto dto) throws DaoException {
		List<VerifiedYieldGrainBasketDto> verifiedGrainBaskets = verifiedYieldGrainBasketDao.selectForVerifiedYieldContract(dto.getVerifiedYieldContractGuid());
		if (verifiedGrainBaskets.size() > 0) {
			dto.setVerifiedYieldGrainBasket(verifiedGrainBaskets.get(0));
		}
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

			if ( InsurancePlans.GRAIN.getInsurancePlanId().equals(verifiedYieldContract.getInsurancePlanId()) 
					|| InsurancePlans.FORAGE.getInsurancePlanId().equals(verifiedYieldContract.getInsurancePlanId())) {

				if ( InsurancePlans.FORAGE.getInsurancePlanId().equals(verifiedYieldContract.getInsurancePlanId()) ) {
					//Rollup to commodity level has to be done before calculating contract commodities
					rollupVerifiedYield(verifiedYieldContract);
				}
				
				//Calculate harvested yield and yield per acre
				calculateVerifiedYieldContractCommodities(verifiedYieldContract);

				// Verified Yield Contract Commodity
				List<VerifiedYieldContractCommodity> verifiedContractCommodities = verifiedYieldContract.getVerifiedYieldContractCommodities();
				if (verifiedContractCommodities != null && !verifiedContractCommodities.isEmpty()) {
					for (VerifiedYieldContractCommodity verifiedContractCommodity : verifiedContractCommodities) {
						updateVerifiedYieldContractCommodity(verifiedYieldContractGuid, verifiedContractCommodity, null, false, verifiedYieldContract.getInsurancePlanId(), userId);
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
				
				//Verified Yield Summary
				calculateAndSaveVerifiedYieldSummaries(verifiedYieldContractGuid, verifiedYieldContract, null, userId, authentication);
				
				if ( InsurancePlans.GRAIN.getInsurancePlanId().equals(verifiedYieldContract.getInsurancePlanId()) ) {
					
					//Grain Basket
					calculateAndSaveGrainBasket(verifiedYieldContractGuid, verifiedYieldContract, null, userId, authentication);
				
				}
			}
			else {
				throw new ServiceException("Insurance Plan must be GRAIN or FORAGE");
			}

			result = getVerifiedYieldContract(verifiedYieldContractGuid, factoryContext, authentication);

		} catch (DaoException e) {
			throw new ServiceException("DAO threw an exception", e);
		}

		logger.debug(">createVerifiedYieldContract");
		return result;
	}	
	
	
	private void calculateAndSaveGrainBasket(
			String verifiedYieldContractGuid, 
			VerifiedYieldContract<? extends AnnualField, ? extends Message> verifiedYieldContract,
			List<ProductDto> productDtos, 
			String userId,
			WebAdeAuthentication authentication) throws DaoException {
		
		//Get products 
		if(productDtos == null) {
			productDtos = loadProducts(verifiedYieldContract.getContractId(), verifiedYieldContract.getCropYear());
		}
		
		//Get product
		ProductDto productDto = getProductDtoByCoverageCode(CommodityCoverageCode.GRAIN_BASKET, productDtos);

		//Only save or update if
		if(productDto != null && productDto.getProductStatusCode().equals(PRODUCT_STATUS_FINAL)) {
			if(verifiedYieldContract.getVerifiedYieldGrainBasket() == null) {
				//Create grain basket
				VerifiedYieldGrainBasket grainBasket = new VerifiedYieldGrainBasket();
				grainBasket.setVerifiedYieldContractGuid(verifiedYieldContractGuid);
				verifiedYieldContract.setVerifiedYieldGrainBasket(grainBasket);
			}
			
			//Set basket value if it's a new record or the user want to update it
			if((verifiedYieldContract.getVerifiedYieldGrainBasket() != null && verifiedYieldContract.getVerifiedYieldGrainBasket().getVerifiedYieldGrainBasketGuid() == null)
					|| Boolean.TRUE.equals(verifiedYieldContract.getUpdateProductValuesInd())) {
				verifiedYieldContract.getVerifiedYieldGrainBasket().setBasketValue(productDto.getCoverageDollars());
			}
		} else {
			//No product exists but grain basket in verified yield exists
			if(verifiedYieldContract.getVerifiedYieldGrainBasket() != null && Boolean.TRUE.equals(verifiedYieldContract.getUpdateProductValuesInd())) {
				//Delete record if user wants to update verified yield data
				deleteVerifiedYieldGrainBasket(verifiedYieldContract.getVerifiedYieldGrainBasket());
				verifiedYieldContract.setVerifiedYieldGrainBasket(null);
			}
		}
		
		if(verifiedYieldContract.getVerifiedYieldGrainBasket() != null) {
			//Calculate Harvested Value: SUM(Commodity YTC * Commodity 100%IV)
			Double harvestedValue = null;
			if((verifiedYieldContract.getVerifiedYieldSummaries() != null && !verifiedYieldContract.getVerifiedYieldSummaries().isEmpty())) {
				for(VerifiedYieldSummary vys : verifiedYieldContract.getVerifiedYieldSummaries()) {
					Double commodityHarvestedValue = null;
					
					if(vys.getYieldToCount() != null && vys.getInsurableValueHundredPercent() != null) {
						commodityHarvestedValue = vys.getYieldToCount() * vys.getInsurableValueHundredPercent();
					}
					
					if(commodityHarvestedValue != null) {
						harvestedValue = notNull(harvestedValue, 0.0) + commodityHarvestedValue;
					}
				}
			}
			
			verifiedYieldContract.getVerifiedYieldGrainBasket().setHarvestedValue(harvestedValue);

			//Save Grain Basket
			updateVerifiedYieldGrainBasket(verifiedYieldContractGuid, verifiedYieldContract.getVerifiedYieldGrainBasket(), userId);
		}
	}
	
	private ProductDto getProductDtoByCoverageCode(String coverageCode, List<ProductDto> productDtos) {
		
		ProductDto product = null;
		
		if(productDtos != null && productDtos.size() > 0) {
			//There is only one grain basket product for a contract
			List<ProductDto> products = productDtos.stream()
					.filter(x -> x.getCommodityCoverageCode().equalsIgnoreCase(coverageCode))
					.collect(Collectors.toList());
			
			if (products != null && products.size() > 0) {
				product = products.get(0);
			}
		}

		
		return product;
	}
	
	public static final String PRODUCT_STATUS_FINAL = "FINAL";
	
	private void calculateAndSaveVerifiedYieldSummaries(
			String verifiedYieldContractGuid, 
			VerifiedYieldContract<? extends AnnualField, ? extends Message> verifiedYieldContract,
			List<ProductDto> productDtos,
			String userId,
			WebAdeAuthentication authentication) throws DaoException {
		
		List<VerifiedYieldAmendment> verifiedAmendments = verifiedYieldContract.getVerifiedYieldAmendments();
		List<VerifiedYieldSummary> verifiedYieldSummaries = new ArrayList<VerifiedYieldSummary>();

		List<VerifiedYieldContractCommodity> verifiedContractCommodities = verifiedYieldContract.getVerifiedYieldContractCommodities();
		
		//Only Rolled up rows for FORAGE 
		if ( InsurancePlans.FORAGE.getInsurancePlanId().equals(verifiedYieldContract.getInsurancePlanId()) ) {
			verifiedContractCommodities = verifiedYieldContract.getVerifiedYieldContractCommodities().stream()
					.filter(x -> Boolean.TRUE.equals(x.getIsRolledupInd()))
					.collect(Collectors.toList());
		}
		
		//Summary records are calculated from commodity totals and amendments
		//Add a yield summary record for each commodity (pedigree/non-pedigree) which is either in commodity totals OR amendments 
		if ((verifiedContractCommodities != null && !verifiedContractCommodities.isEmpty())
			|| (verifiedAmendments != null && !verifiedAmendments.isEmpty())) {
			//Get products 
			if(productDtos == null) {
				productDtos = loadProducts(verifiedYieldContract.getContractId(), verifiedYieldContract.getCropYear());
			}
			
			if(verifiedContractCommodities != null && !verifiedContractCommodities.isEmpty()) {
				for (VerifiedYieldContractCommodity vycc : verifiedContractCommodities) {
					//Get verified yield summary from list. Create empty one if it doesn't exist yet.
					VerifiedYieldSummary vys = getVerifiedYieldSummary(verifiedYieldContractGuid, verifiedYieldContract.getVerifiedYieldSummaries(), vycc);
					
					//Set values and calculate
					if(vys != null) {
						Double effectiveYield = notNull(vycc.getHarvestedYieldOverride(), vycc.getHarvestedYield());
						
						vys.setHarvestedYield(effectiveYield);
						vys.setHarvestedYieldPerAcre(vycc.getYieldPerAcre());
						
						//Product Values (Production Guarantee and PY)
						setProductValues(verifiedYieldContract, productDtos, vys);	
						
						//Calculate appraised and assessed yield
						calculateAndSetAmendments(verifiedAmendments, vys, vycc);
						
						//Calculate yield to count: Harvested Yield + Appraised Yield
						Double yieldToCount = notNull(vys.getHarvestedYield(), 0.0) + notNull(vys.getAppraisedYield(), 0.0); 
						vys.setYieldToCount(yieldToCount);
						
						//Calculate yield percent of py if py > 0 exists and insured acres are > 0
						vys.setYieldPercentPy(null);
						if(notNull(vys.getProbableYield(), 0.0) > 0 && notNull(vycc.getTotalInsuredAcres(), 0.0) > 0 ) {
							//Yield to Count/(Insured Acres * PY)
							Double yieldPercentPy = notNull(vys.getYieldToCount(), 0.0) / (vys.getProbableYield() * vycc.getTotalInsuredAcres());
							vys.setYieldPercentPy(yieldPercentPy);
						}
						
						verifiedYieldSummaries.add(vys);
					}
				}
			}
			
			//Amendments can exist without a verified yield contract commodity
			if(verifiedAmendments != null && !verifiedAmendments.isEmpty()) {
				for (VerifiedYieldAmendment vya : verifiedAmendments) {
					//Only if it's not deleted by the user
					if(vya.getDeletedByUserInd() == null || vya.getDeletedByUserInd() == false) {
						
						VerifiedYieldSummary vys = null;
						
						if(verifiedYieldSummaries != null && !verifiedYieldSummaries.isEmpty() ) {
							vys = getVerifiedYieldSummary(vya.getCropCommodityId(), vya.getIsPedigreeInd(), verifiedYieldSummaries);
						} 
						
						//Check if commodity/is pedigree already exists in the updated list and if not add it
						if(vys == null) {
							
							//Check if it exists in the old list
							vys = getVerifiedYieldSummary(vya.getCropCommodityId(), vya.getIsPedigreeInd(), verifiedYieldContract.getVerifiedYieldSummaries());
							
							if(vys == null) {
								//Create new one if it doesn't exist
								vys = new VerifiedYieldSummary();
								vys.setVerifiedYieldSummaryGuid(null);
								vys.setVerifiedYieldContractGuid(verifiedYieldContractGuid);
								vys.setCropCommodityId(vya.getCropCommodityId());
								vys.setIsPedigreeInd(vya.getIsPedigreeInd());
							}
							
							//No verified yield contract commodity and therefore no harvested yield
							vys.setHarvestedYield(null);
							vys.setHarvestedYieldPerAcre(null);
		
							//Product Values (Production Guarantee and PY)
							setProductValues(verifiedYieldContract, productDtos, vys);	
		
							//Calculate appraised and assessed yield
							calculateAndSetAmendments(verifiedAmendments, vys, null);
							
							vys.setYieldToCount(notNull(vys.getAppraisedYield(), 0.0));
							
							vys.setYieldPercentPy(null);
							
							verifiedYieldSummaries.add(vys);
						}						
					}
				}
			}
			
			if((verifiedYieldContract.getVerifiedYieldSummaries() != null && !verifiedYieldContract.getVerifiedYieldSummaries().isEmpty())) {
				for(VerifiedYieldSummary vys : verifiedYieldContract.getVerifiedYieldSummaries()) {
					//Remove all records if they don't exist anymore
					VerifiedYieldSummary existingVys = null;
					
					if(verifiedYieldSummaries != null && !verifiedYieldSummaries.isEmpty() ) {
						existingVys = getVerifiedYieldSummary(vys.getCropCommodityId(), vys.getIsPedigreeInd(), verifiedYieldSummaries);
					}
					
					if(existingVys == null) {
						//Didn't find it in the new list, therefore delete it
						deleteVerifiedYieldSummary(vys);
					}
				}
			}
			
			//Save Verified Yield Summary Records
			if(verifiedYieldSummaries != null && !verifiedYieldSummaries.isEmpty() ) {
				for(VerifiedYieldSummary vys : verifiedYieldSummaries){
					updateVerifiedYieldSummary(vys, userId);
					
					// update underwriting comments
					List<UnderwritingComment> uwComments = vys.getUwComments();
					if (uwComments != null && !uwComments.isEmpty()) {
						for (UnderwritingComment underwritingComment : uwComments) {
							if (underwritingComment.getDeletedByUserInd() != null
									&& underwritingComment.getDeletedByUserInd()) {
								deleteYieldSummaryComment(underwritingComment, userId, authentication);
							} else {
								updateUnderwritingComment(underwritingComment, vys.getVerifiedYieldSummaryGuid(), userId, authentication);
							}
						}
					}
				}
			}
	
		} else {
			if(verifiedYieldContract.getVerifiedYieldContractGuid() != null) {
				underwritingCommentDao.deleteForVerifiedYieldContract(verifiedYieldContract.getVerifiedYieldContractGuid());
				verifiedYieldSummaryDao.deleteForVerifiedYieldContract(verifiedYieldContract.getVerifiedYieldContractGuid());
			}
		}
		
		verifiedYieldContract.setVerifiedYieldSummaries(verifiedYieldSummaries);
	
	}
	
	private void updateUnderwritingComment(
			UnderwritingComment underwritingComment, 
			String verifiedYieldSummaryGuid,
			String userId,
			WebAdeAuthentication authentication) throws DaoException, ServiceException {

		UnderwritingCommentDto dto = null;

		if (underwritingComment.getUnderwritingCommentGuid() != null) {
			dto = underwritingCommentDao.fetch(underwritingComment.getUnderwritingCommentGuid());
		}

		if (dto == null) {
			// Insert if it doesn't exist
			insertYieldSummaryComment(underwritingComment, verifiedYieldSummaryGuid, userId);
		} else {

			if (!dto.getUnderwritingComment().equals(underwritingComment.getUnderwritingComment()) || !dto
					.getUnderwritingCommentTypeCode().equals(underwritingComment.getUnderwritingCommentTypeCode())) {

				// Check that user is authorized to edit this comment.
				// Note that this could return null if the current user or create user cannot be
				// determined.
				Boolean userCanEditComment = inventoryContractFactory.checkUserCanEditComment(dto, authentication);
				if (!Boolean.TRUE.equals(userCanEditComment)) {
					logger.error("User " + userId + " attempted to edit comment "
							+ underwritingComment.getUnderwritingCommentGuid() + " created by " + dto.getCreateUser());
					throw new ServiceException("The current user is not authorized to edit this comment.");
				}

			}

			inventoryContractFactory.updateDto(dto, underwritingComment);

			underwritingCommentDao.update(dto, userId);
		}

	}
	
	private String insertYieldSummaryComment(
			UnderwritingComment underwritingComment, 
			String verifiedYieldSummaryGuid, 
			String userId) throws DaoException {

		logger.debug("<insertYieldSummaryComment");
		
		UnderwritingCommentDto dto = new UnderwritingCommentDto();
		inventoryContractFactory.updateDto(dto, underwritingComment);

		dto.setUnderwritingCommentGuid(null);
		dto.setVerifiedYieldSummaryGuid(verifiedYieldSummaryGuid);

		underwritingCommentDao.insert(dto, userId);
		
		logger.debug(">insertYieldSummaryComment");
		
		return dto.getUnderwritingCommentGuid();
	}
	
	private void deleteYieldSummaryComment(UnderwritingComment underwritingComment, String userId,
			WebAdeAuthentication authentication) throws NotFoundDaoException, DaoException {
		logger.debug("<deleteYieldSummaryComment");

		UnderwritingCommentDto dto = null;

		if (underwritingComment.getUnderwritingCommentGuid() != null) {
			dto = underwritingCommentDao.fetch(underwritingComment.getUnderwritingCommentGuid());
		}

		if (dto != null) {
			// Check that user is authorized to delete this comment.
			// Note that this could return false if the current user or create user cannot
			// be determined.
			Boolean userCanDeleteComment = inventoryContractFactory.checkUserCanDeleteComment(dto, authentication);
			if (!Boolean.TRUE.equals(userCanDeleteComment)) {
				logger.error("User " + userId + " attempted to delete comment " + dto.getUnderwritingCommentGuid()
						+ " created by " + dto.getCreateUser());
				throw new ServiceException("The current user is not authorized to delete this comment.");
			}

			underwritingCommentDao.delete(underwritingComment.getUnderwritingCommentGuid());
		}

		logger.debug(">deleteYieldSummaryComment");
	}	
	
	private void updateVerifiedYieldSummary(
			VerifiedYieldSummary verifiedSummary,
			String userId) throws DaoException {

		logger.debug("<updateVerifiedYieldSummary");
		
		VerifiedYieldSummaryDto dto = null;

		if (verifiedSummary.getVerifiedYieldSummaryGuid() != null) {
			dto = verifiedYieldSummaryDao.fetch(verifiedSummary.getVerifiedYieldSummaryGuid());
		}

		if (dto == null) {
			// Insert if it doesn't exist
			insertVerifiedYieldSummary(verifiedSummary, userId);
		} else {
			verifiedYieldContractFactory.updateDto(dto, verifiedSummary);

			verifiedYieldSummaryDao.update(dto, userId);
		}

		logger.debug(">updateVerifiedYieldSummary");
	}
	
	
	private void insertVerifiedYieldSummary(VerifiedYieldSummary verifiedYieldSummary, String userId) throws DaoException {

		logger.debug("<insertVerifiedYieldSummary");

		VerifiedYieldSummaryDto dto = new VerifiedYieldSummaryDto();

		verifiedYieldContractFactory.updateDto(dto, verifiedYieldSummary);

		dto.setVerifiedYieldSummaryGuid(null);

		verifiedYieldSummaryDao.insert(dto, userId);
		
		verifiedYieldSummary.setVerifiedYieldSummaryGuid(dto.getVerifiedYieldSummaryGuid());

		logger.debug(">insertVerifiedYieldSummary");

	}

	private void deleteVerifiedYieldSummary(VerifiedYieldSummary verifiedSummary) throws DaoException {

		logger.debug("<deleteVerifiedYieldSummary");

		if ( verifiedSummary.getVerifiedYieldSummaryGuid() != null ) {
			underwritingCommentDao.deleteForVerifiedYieldSummaryGuid(verifiedSummary.getVerifiedYieldSummaryGuid());
			verifiedYieldSummaryDao.delete(verifiedSummary.getVerifiedYieldSummaryGuid());
		}

		logger.debug(">deleteVerifiedYieldSummary");

	}
	
	private void deleteVerifiedYieldGrainBasket(VerifiedYieldGrainBasket verifiedGrainBasket) throws DaoException {

		logger.debug("<deleteVerifiedYieldGrainBasket");

		if ( verifiedGrainBasket.getVerifiedYieldGrainBasketGuid() != null ) {
			verifiedYieldGrainBasketDao.delete(verifiedGrainBasket.getVerifiedYieldGrainBasketGuid());
		}

		logger.debug(">deleteVerifiedYieldGrainBasket");

	}
	
	private void updateVerifiedYieldGrainBasket(
			String verifiedYieldContractGuid,
			VerifiedYieldGrainBasket verifiedGrainBasket,
			String userId) throws DaoException {

		logger.debug("<updateVerifiedYieldGrainBasket");
		
		VerifiedYieldGrainBasketDto dto = null;

		if (verifiedGrainBasket.getVerifiedYieldGrainBasketGuid() != null) {
			dto = verifiedYieldGrainBasketDao.fetch(verifiedGrainBasket.getVerifiedYieldGrainBasketGuid());
		}

		if (dto == null) {
			// Insert if it doesn't exist
			insertVerifiedYieldGrainBasket(verifiedYieldContractGuid, verifiedGrainBasket, userId);
		} else {
			verifiedYieldContractFactory.updateDto(dto, verifiedGrainBasket);

			verifiedYieldGrainBasketDao.update(dto, userId);
		}

		logger.debug(">updateVerifiedYieldGrainBasket");
	}
	
	
	private void insertVerifiedYieldGrainBasket(
			String verifiedYieldContractGuid,
			VerifiedYieldGrainBasket verifiedYieldGrainBasket, 
			String userId) throws DaoException {

		logger.debug("<insertVerifiedYieldGrainBasket");

		VerifiedYieldGrainBasketDto dto = new VerifiedYieldGrainBasketDto();

		verifiedYieldContractFactory.updateDto(dto, verifiedYieldGrainBasket);

		dto.setVerifiedYieldGrainBasketGuid(null);
		dto.setVerifiedYieldContractGuid(verifiedYieldContractGuid);

		verifiedYieldGrainBasketDao.insert(dto, userId);
		
		verifiedYieldGrainBasket.setVerifiedYieldGrainBasketGuid(dto.getVerifiedYieldGrainBasketGuid());

		logger.debug(">insertVerifiedYieldGrainBasket");

	}

	private void setProductValues(VerifiedYieldContract<? extends AnnualField, ? extends Message> verifiedYieldContract,
			List<ProductDto> productDtos, VerifiedYieldSummary vys) {
		//Get product
		ProductDto product = getProductDto(vys.getCropCommodityId(), vys.getIsPedigreeInd(), productDtos);
		Double productionGuarantee = vys.getProductionGuarantee();
		Double probableYield = vys.getProbableYield();
		Double insurableValueHundredPercent = vys.getInsurableValueHundredPercent();

		//Set or Update product values
		if(vys.getVerifiedYieldSummaryGuid() == null || Boolean.TRUE.equals(verifiedYieldContract.getUpdateProductValuesInd())) {
			if(product != null && product.getProductStatusCode().equals(PRODUCT_STATUS_FINAL)) {
				productionGuarantee = product.getProductionGuarantee();
				probableYield = product.getProbableYield();
				insurableValueHundredPercent = product.getInsurableValueHundredPercent();
			} else {
				productionGuarantee = null;
				probableYield = null;
				insurableValueHundredPercent = null;
			}
		}

		vys.setProductionGuarantee(productionGuarantee);
		vys.setProbableYield(probableYield);
		vys.setInsurableValueHundredPercent(insurableValueHundredPercent);
	}

	private void calculateAndSetAmendments(List<VerifiedYieldAmendment> verifiedAmendments, VerifiedYieldSummary vys, VerifiedYieldContractCommodity vycc) {
		
		Double appraisedYield = null;
		Double assessedYield = null;
		Double appraisedAcres = 0.0;
		
		List<VerifiedYieldAmendment> filteredAmendments = getVerifiedYieldAmendments(vys.getCropCommodityId(), vys.getIsPedigreeInd(), verifiedAmendments);
		if(filteredAmendments != null && !filteredAmendments.isEmpty()) {
			for(VerifiedYieldAmendment vya : filteredAmendments) {
				//Yield/acre * acres
				Double totalYield = vya.getYieldPerAcre() * vya.getAcres();
				
				if(vya.getVerifiedYieldAmendmentCode().equalsIgnoreCase(InventoryServiceEnums.AmendmentTypeCode.Appraisal.toString())) {
					appraisedYield = notNull(appraisedYield, 0.0) + notNull(totalYield, 0.0);
					appraisedAcres = notNull(appraisedAcres, 0.0) + notNull(vya.getAcres(), 0.0);
				} else if(vya.getVerifiedYieldAmendmentCode().equalsIgnoreCase(InventoryServiceEnums.AmendmentTypeCode.Assessment.toString())) {
					assessedYield = notNull(assessedYield, 0.0) + notNull(totalYield, 0.0);
				} else {
					throw new IllegalArgumentException("(updateVerifiedYieldSummaries) Unexpected Verified Yield Amendment Code: " + vya.getVerifiedYieldAmendmentCode());
				}
			}
		}
		vys.setAppraisedYield(appraisedYield);
		vys.setAssessedYield(assessedYield);
		Double effectiveHarvestedAcres = 0.0;
		if(vycc != null) {
			effectiveHarvestedAcres = notNull(vycc.getHarvestedAcresOverride(), vycc.getHarvestedAcres());
			//In case both are null
			effectiveHarvestedAcres = notNull(effectiveHarvestedAcres, 0.0);
		}
		//Production Acres = Appraised Acres + Harvested Acres
		vys.setProductionAcres(appraisedAcres + effectiveHarvestedAcres);
		
	}

	private VerifiedYieldSummary getVerifiedYieldSummary(
			String verifiedYieldContractGuid,
			List<VerifiedYieldSummary> verifiedYieldSummaries, 
			VerifiedYieldContractCommodity vycc) {

		VerifiedYieldSummary vys = null;
		
		if(verifiedYieldSummaries != null && !verifiedYieldSummaries.isEmpty() ) {
			vys = getVerifiedYieldSummary(vycc.getCropCommodityId(), vycc.getIsPedigreeInd(), verifiedYieldSummaries);
		} 
		
		if(vys == null) {
			
			//Create new one if it doesn't exist
			vys = new VerifiedYieldSummary();
			vys.setVerifiedYieldSummaryGuid(null);
			vys.setVerifiedYieldContractGuid(verifiedYieldContractGuid);
			vys.setCropCommodityId(vycc.getCropCommodityId());
			vys.setIsPedigreeInd(vycc.getIsPedigreeInd());
			vys.setProductionAcres(null);
			vys.setHarvestedYield(null);
			vys.setHarvestedYieldPerAcre(null);
			vys.setAppraisedYield(null);
			vys.setAssessedYield(null);
			vys.setYieldToCount(null);
			vys.setYieldPercentPy(null);
			vys.setProductionGuarantee(null);
			vys.setProbableYield(null);	
			vys.setInsurableValueHundredPercent(null);
		}
		
		return vys;
	}
	
	private VerifiedYieldSummary getVerifiedYieldSummary(Integer cropCommodityId, Boolean isPedigree, List<VerifiedYieldSummary> vysList) {
		
		VerifiedYieldSummary vys = null;
		
		if(vysList != null && !vysList.isEmpty()) {
			List<VerifiedYieldSummary> vysFiltered = vysList.stream()
					.filter(x -> x.getCropCommodityId().equals(cropCommodityId) && x.getIsPedigreeInd().equals(isPedigree) )
					.collect(Collectors.toList());
			
			if (vysFiltered != null && !vysFiltered.isEmpty()) {
				vys = vysFiltered.get(0);
			}
		}

		return vys;
	}
	
	private List<VerifiedYieldAmendment> getVerifiedYieldAmendments(Integer cropCommodityId, Boolean isPedigree, List<VerifiedYieldAmendment> vyaList) {
		
		if(vyaList != null && !vyaList.isEmpty()) {
		
			return vyaList.stream()
					.filter(x -> x.getCropCommodityId().equals(cropCommodityId) && x.getIsPedigreeInd().equals(isPedigree)
							&& (x.getDeletedByUserInd() == null || x.getDeletedByUserInd() == false))
					.collect(Collectors.toList());
		} else {
			return null;
		}
	}
	
	private ProductDto getProductDto(Integer cropCommodityId, Boolean isPedigree, List<ProductDto> productDtos) {
		
		ProductDto product = null;
		
		if(productDtos != null && !productDtos.isEmpty()) {
			//Products in CIRRAS use a different commodity id for pedigreed than in this app. A table maps the correct commodity ids and
			//are returned to the NonPedigreeCropCommodityId property
			List<ProductDto> products = productDtos.stream()
					.filter(x -> x.getNonPedigreeCropCommodityId().equals(cropCommodityId) 
							&& x.getIsPedigreeProduct().equals(isPedigree)
							&& verifiedYieldContractFactory.getForageGrainCoverageCodes().contains(x.getCommodityCoverageCode()))
					.collect(Collectors.toList());
			
			if (products != null && !products.isEmpty()) {
				product = products.get(0);
			}
		}

		
		return product;
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
			Integer insurancePlanId,
			String userId) throws DaoException {

		logger.debug("<updateVerifiedYieldContractCommodity");
		
		VerifiedYieldContractCommodityDto dto = null;

		if (verifiedContractCommodity.getVerifiedYieldContractCommodityGuid() != null) {
			dto = verifiedYieldContractCommodityDao.fetch(verifiedContractCommodity.getVerifiedYieldContractCommodityGuid());
		}

		if (dto == null) {
			// Insert if it doesn't exist
			insertVerifiedYieldContractCommodity(verifiedYieldContractGuid, verifiedContractCommodity, insurancePlanId, userId);
		} else {
			verifiedYieldContractFactory.updateDto(dto, verifiedContractCommodity, productDtos, updateProductValues, insurancePlanId);

			verifiedYieldContractCommodityDao.update(dto, userId);
		}

		logger.debug(">updateVerifiedYieldContractCommodity");
	}

	private void insertVerifiedYieldContractCommodity(
			String verifiedYieldContractGuid,
			VerifiedYieldContractCommodity verifiedContractCommodity, 
			Integer insurancePlanId,
			String userId) throws DaoException {

		logger.debug("<insertVerifiedYieldContractCommodity");

		VerifiedYieldContractCommodityDto dto = new VerifiedYieldContractCommodityDto();

		verifiedYieldContractFactory.updateDto(dto, verifiedContractCommodity, null, false, insurancePlanId);

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

			if ( InsurancePlans.GRAIN.getInsurancePlanId().equals(verifiedYieldContract.getInsurancePlanId()) 
					|| InsurancePlans.FORAGE.getInsurancePlanId().equals(verifiedYieldContract.getInsurancePlanId())) {

				if ( InsurancePlans.FORAGE.getInsurancePlanId().equals(verifiedYieldContract.getInsurancePlanId()) ) {
					//Rollup to commodity level has to be done before calculating contract commodities
					rollupVerifiedYield(verifiedYieldContract);
				}
			
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
						updateVerifiedYieldContractCommodity(verifiedYieldContractGuid, verifiedContractCommodity, productDtos, verifiedYieldContract.getUpdateProductValuesInd(), verifiedYieldContract.getInsurancePlanId(), userId);
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
				
				//Verified Yield Summary
				calculateAndSaveVerifiedYieldSummaries(verifiedYieldContractGuid, verifiedYieldContract, productDtos, userId, authentication);
			
				if ( InsurancePlans.GRAIN.getInsurancePlanId().equals(verifiedYieldContract.getInsurancePlanId()) ) {
					
					//Grain Basket
					calculateAndSaveGrainBasket(verifiedYieldContractGuid, verifiedYieldContract, productDtos, userId, authentication);
					
				}
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

		verifiedYieldContractCommodityDao.deleteForVerifiedYieldContract(verifiedYieldContractGuid);
		verifiedYieldAmendmentDao.deleteForVerifiedYieldContract(verifiedYieldContractGuid);
		underwritingCommentDao.deleteForVerifiedYieldContract(verifiedYieldContractGuid);
		verifiedYieldSummaryDao.deleteForVerifiedYieldContract(verifiedYieldContractGuid);

		
		if ( InsurancePlans.GRAIN.getInsurancePlanId().equals(dto.getInsurancePlanId()) ) {
			verifiedYieldGrainBasketDao.deleteForVerifiedYieldContract(verifiedYieldContractGuid);
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
