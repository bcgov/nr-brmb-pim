package ca.bc.gov.mal.cirras.underwriting.services.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import ca.bc.gov.mal.cirras.underwriting.data.models.DopYieldContractCommodityForage;
import ca.bc.gov.mal.cirras.underwriting.data.models.DopYieldFieldForage;
import ca.bc.gov.mal.cirras.underwriting.data.models.DopYieldFieldForageCut;
import ca.bc.gov.mal.cirras.underwriting.data.models.DopYieldFieldGrain;
import ca.bc.gov.mal.cirras.underwriting.data.models.DopYieldFieldRollup;
import ca.bc.gov.mal.cirras.underwriting.data.models.DopYieldFieldRollupForage;
import ca.bc.gov.mal.cirras.underwriting.data.models.VerifiedYieldContractCommodity;
import ca.bc.gov.mal.cirras.underwriting.data.models.VerifiedYieldGrainBasket;
import ca.bc.gov.mal.cirras.underwriting.data.models.VerifiedYieldSummary;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.DeclaredYieldContractCommodityForageDao;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.DeclaredYieldFieldDao;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.DeclaredYieldFieldForageDao;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.DeclaredYieldFieldRollupDao;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.DeclaredYieldFieldRollupForageDao;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.InventorySeededForageDao;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.InventorySeededGrainDao;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.ProductDao;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.VerifiedYieldGrainBasketDao;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.YieldMeasUnitConversionDao;
import ca.bc.gov.mal.cirras.underwriting.data.resources.AnnualFieldRsrc;
import ca.bc.gov.mal.cirras.underwriting.data.resources.DopYieldContractRsrc;
import ca.bc.gov.mal.cirras.underwriting.data.resources.VerifiedYieldContractRsrc;
import ca.bc.gov.mal.cirras.underwriting.services.utils.InventoryServiceEnums.InsurancePlans;
import ca.bc.gov.mal.cirras.underwriting.data.entities.DeclaredYieldContractCommodityForageDto;
import ca.bc.gov.mal.cirras.underwriting.data.entities.DeclaredYieldContractDto;
import ca.bc.gov.mal.cirras.underwriting.data.entities.DeclaredYieldFieldDto;
import ca.bc.gov.mal.cirras.underwriting.data.entities.DeclaredYieldFieldForageDto;
import ca.bc.gov.mal.cirras.underwriting.data.entities.DeclaredYieldFieldRollupDto;
import ca.bc.gov.mal.cirras.underwriting.data.entities.DeclaredYieldFieldRollupForageDto;
import ca.bc.gov.mal.cirras.underwriting.data.entities.InventoryFieldDto;
import ca.bc.gov.mal.cirras.underwriting.data.entities.InventorySeededForageDto;
import ca.bc.gov.mal.cirras.underwriting.data.entities.InventorySeededGrainDto;
import ca.bc.gov.mal.cirras.underwriting.data.entities.ProductDto;
import ca.bc.gov.mal.cirras.underwriting.data.entities.VerifiedYieldContractDto;
import ca.bc.gov.mal.cirras.underwriting.data.entities.VerifiedYieldGrainBasketDto;
import ca.bc.gov.mal.cirras.underwriting.data.entities.YieldMeasUnitConversionDto;
import ca.bc.gov.mal.cirras.underwriting.data.assemblers.DopYieldContractRsrcFactory;
import ca.bc.gov.mal.cirras.underwriting.data.assemblers.VerifiedYieldContractRsrcFactory;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.service.api.ServiceException;
import ca.bc.gov.nrs.wfone.common.webade.authentication.WebAdeAuthentication;

public class GrainForageService {

	private static final Logger logger = LoggerFactory.getLogger(GrainForageService.class);

	private InventorySeededForageDao inventorySeededForageDao;
	private InventorySeededGrainDao inventorySeededGrainDao;
	private VerifiedYieldGrainBasketDao verifiedYieldGrainBasketDao;
	private ProductDao productDao;
	private DeclaredYieldFieldDao declaredYieldFieldDao;
	private DeclaredYieldFieldForageDao declaredYieldFieldForageDao;
	private DeclaredYieldFieldRollupDao declaredYieldFieldRollupDao;
	private YieldMeasUnitConversionDao yieldMeasUnitConversionDao;
	private DeclaredYieldContractCommodityForageDao declaredYieldContractCommodityForageDao;
	private DeclaredYieldFieldRollupForageDao declaredYieldFieldRollupForageDao;

	private VerifiedYieldContractRsrcFactory verifiedYieldContractRsrcFactory;
	private DopYieldContractRsrcFactory dopYieldContractRsrcFactory;

	private UnderwritingServiceHelper underwritingServiceHelper;

	
	public final String PRODUCT_STATUS_FINAL = "FINAL";
	
	public void setInventorySeededGrainDao(InventorySeededGrainDao inventorySeededGrainDao) {
		this.inventorySeededGrainDao = inventorySeededGrainDao;
	}

	public void setInventorySeededForageDao(InventorySeededForageDao inventorySeededForageDao) {
		this.inventorySeededForageDao = inventorySeededForageDao;
	}

	public void setVerifiedYieldGrainBasketDao(VerifiedYieldGrainBasketDao verifiedYieldGrainBasketDao) {
		this.verifiedYieldGrainBasketDao = verifiedYieldGrainBasketDao;
	}
	
	public void setProductDao(ProductDao productDao) {
		this.productDao = productDao;
	}

	public void setDeclaredYieldFieldDao(DeclaredYieldFieldDao declaredYieldFieldDao) {
		this.declaredYieldFieldDao = declaredYieldFieldDao;
	}

	public void setDeclaredYieldFieldForageDao(DeclaredYieldFieldForageDao declaredYieldFieldForageDao) {
		this.declaredYieldFieldForageDao = declaredYieldFieldForageDao;
	}
	
	public void setDeclaredYieldFieldRollupDao(DeclaredYieldFieldRollupDao declaredYieldFieldRollupDao) {
		this.declaredYieldFieldRollupDao = declaredYieldFieldRollupDao;
	}
	
	public void setYieldMeasUnitConversionDao(YieldMeasUnitConversionDao yieldMeasUnitConversionDao) {
		this.yieldMeasUnitConversionDao = yieldMeasUnitConversionDao;
	}

	public void setDeclaredYieldContractCommodityForageDao(DeclaredYieldContractCommodityForageDao declaredYieldContractCommodityForageDao) {
		this.declaredYieldContractCommodityForageDao = declaredYieldContractCommodityForageDao;
	}
	
	public void setDeclaredYieldFieldRollupForageDao(DeclaredYieldFieldRollupForageDao declaredYieldFieldRollupForageDao) {
		this.declaredYieldFieldRollupForageDao = declaredYieldFieldRollupForageDao;
	}
	
	public void setVerifiedYieldContractRsrcFactory(VerifiedYieldContractRsrcFactory verifiedYieldContractRsrcFactory) {
		this.verifiedYieldContractRsrcFactory = verifiedYieldContractRsrcFactory;
	}

	public void setDopYieldContractRsrcFactory(DopYieldContractRsrcFactory dopYieldContractRsrcFactory) {
		this.dopYieldContractRsrcFactory = dopYieldContractRsrcFactory;
	}
	
	public void setUnderwritingServiceHelper(UnderwritingServiceHelper underwritingServiceHelper) {
		this.underwritingServiceHelper = underwritingServiceHelper;
	}
	
	public void loadDeclaredSeededGrains(InventoryFieldDto ifDto) throws DaoException {
		List<InventorySeededGrainDto> inventorySeededGrains = inventorySeededGrainDao.selectForDeclaredYield(ifDto.getInventoryFieldGuid());
		ifDto.setInventorySeededGrains(inventorySeededGrains);
	}
	
	public void loadVerifiedSeededGrains(InventoryFieldDto ifDto) throws DaoException {
		List<InventorySeededGrainDto> inventorySeededGrains = inventorySeededGrainDao.selectForVerifiedYield(ifDto.getInventoryFieldGuid());
		ifDto.setInventorySeededGrains(inventorySeededGrains);
	}
	
	public void loadVerifiedSeededForage(InventoryFieldDto ifDto) throws DaoException {
		List<InventorySeededForageDto> inventorySeededForages = inventorySeededForageDao.selectForVerifiedYield(ifDto.getInventoryFieldGuid());
		ifDto.setInventorySeededForages(inventorySeededForages);
	}
	
	public void loadVerifiedYieldGrainBasket(VerifiedYieldContractDto dto) throws DaoException {
		List<VerifiedYieldGrainBasketDto> verifiedGrainBaskets = verifiedYieldGrainBasketDao.selectForVerifiedYieldContract(dto.getVerifiedYieldContractGuid());
		if (verifiedGrainBaskets.size() > 0) {
			dto.setVerifiedYieldGrainBasket(verifiedGrainBaskets.get(0));
		}
	}
	
	public void calculateAndSaveGrainBasket(
			String verifiedYieldContractGuid, 
			VerifiedYieldContractRsrc verifiedYieldContract,
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
			
			//Set product values if it's a new record or the user want to update it
			if((verifiedYieldContract.getVerifiedYieldGrainBasket() != null && verifiedYieldContract.getVerifiedYieldGrainBasket().getVerifiedYieldGrainBasketGuid() == null)
					|| Boolean.TRUE.equals(verifiedYieldContract.getUpdateProductValuesInd())) {
				verifiedYieldContract.getVerifiedYieldGrainBasket().setBasketValue(productDto.getCoverageDollars());
				verifiedYieldContract.getVerifiedYieldGrainBasket().setTotalQuantityCoverageValue(calculateTotalQuantityCoverageValue(productDtos));
				
				Double totalCoverageValue = notNull(verifiedYieldContract.getVerifiedYieldGrainBasket().getBasketValue(), 0.0) + 
						verifiedYieldContract.getVerifiedYieldGrainBasket().getTotalQuantityCoverageValue();

				verifiedYieldContract.getVerifiedYieldGrainBasket().setTotalCoverageValue(totalCoverageValue);
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
	
	public void mergeYieldRollupToCommodityTotals(DeclaredYieldContractDto dycDto) {

		//For forage rollover the business rule is to take the rollup value if there is no manually entered
		//commodity totals by commodity
		if(dycDto.getDeclaredYieldFieldRollupForageList() != null && dycDto.getDeclaredYieldFieldRollupForageList().size() > 0) {
			if(dycDto.getDeclaredYieldContractCommodityForageList() != null ) {
				
				//Check for each record if Harvested Acres, # Bales/Loads, weight and Moisture % are null in the DOP commodity totals table
				for(DeclaredYieldContractCommodityForageDto dto: dycDto.getDeclaredYieldContractCommodityForageList()) {
					if(dto.getHarvestedAcres() == null && 
							dto.getTotalBalesLoads() == null && 
							dto.getWeight() == null && 
							dto.getMoisturePercent() == null) {

						//Look for the yield rollup
						List<DeclaredYieldFieldRollupForageDto> rollups = dycDto.getDeclaredYieldFieldRollupForageList().stream()
								.filter(x -> x.getCommodityTypeCode().equalsIgnoreCase(dto.getCommodityTypeCode()))
								.collect(Collectors.toList());
						
						if (rollups != null && rollups.size() > 0) {
							DeclaredYieldFieldRollupForageDto rollupYield = rollups.get(0);
							//Insured acres (fieldAcres) are already in the commodity totals table
							dto.setHarvestedAcres(rollupYield.getHarvestedAcres());
							dto.setQuantityHarvestedTons(rollupYield.getQuantityHarvestedTons());
						}
					}
				}
			}
		}
	}
	
	public void rollupVerifiedYield(VerifiedYieldContractRsrc vyc) {
		
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
	
	public void rollupVerifiedYield(DeclaredYieldContractDto dycDto) {
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
	
	public void loadDeclaredYieldField(InventoryFieldDto ifDto) throws DaoException {
		DeclaredYieldFieldDto dyfDto = declaredYieldFieldDao.getByInventoryField(ifDto.getInventoryFieldGuid());
		ifDto.setDeclaredYieldField(dyfDto);
	}
	
	public void loadSeededForage(InventoryFieldDto ifDto) throws DaoException {
		
		List<InventorySeededForageDto> inventorySeededForages = inventorySeededForageDao.selectForDeclaredYield(ifDto.getInventoryFieldGuid());
		ifDto.setInventorySeededForages(inventorySeededForages);
	}

	public void loadDeclaredYieldFieldForage(InventoryFieldDto ifDto) throws DaoException {

		List<DeclaredYieldFieldForageDto> dyffDtoList = declaredYieldFieldForageDao.getByInventoryField(ifDto.getInventoryFieldGuid());
		ifDto.setDeclaredYieldFieldForageList(dyffDtoList);
	}
	
	public void calculateForageDop(String declaredYieldContractGuid,
			DopYieldContractRsrc dopYieldContract,
			Map<String, YieldMeasUnitConversionDto> ymucMap) {

		logger.debug("<calculateForageDop");
		
		List<DopYieldFieldRollupForage> dopNewYieldFieldRollupForageList = new ArrayList<DopYieldFieldRollupForage>();
		List<DopYieldContractCommodityForage> dopNewYieldContractCommodityForageList = new ArrayList<DopYieldContractCommodityForage>();
		
		if (dopYieldContract.getFields() != null && dopYieldContract.getFields().size() > 0) {

			for (AnnualFieldRsrc field : dopYieldContract.getFields()) {
				
				if (field.getDopYieldFieldForageList() != null && field.getDopYieldFieldForageList().size() > 0) {

					// Loop through each dop field record
					for (DopYieldFieldForage dopField : field.getDopYieldFieldForageList()) {
						
						if(dopField.getCommodityTypeCode() != null) {
							
							//Calculate values
							//Insured Acres
							Double totalFieldAcres = (double)0;
							if(dopField.getFieldAcres() != null && dopField.getFieldAcres() > 0 && Boolean.TRUE.equals(dopField.getIsQuantityInsurableInd())) {
								totalFieldAcres = dopField.getFieldAcres();
							}
							
							//Get a list of eligible cuts
							List<DopYieldFieldForageCut> totalEligileCuts = null;
							if(dopField.getDopYieldFieldForageCuts() != null && dopField.getDopYieldFieldForageCuts().size() > 0) {
								totalEligileCuts = dopField.getDopYieldFieldForageCuts().stream()
										.filter(x -> x.getTotalBalesLoads() != null
												&& x.getTotalBalesLoads() > 0
												&& x.getWeightDefaultUnit() != null && x.getWeightDefaultUnit() > 0
												&& x.getDeletedByUserInd() == false)
										.collect(Collectors.toList());
							}
							
							Double harvestedAcres = (double)0;
							Double quantityHarvestedTons = (double)0;
							Integer totalBales = 0;
							if(totalEligileCuts != null && totalEligileCuts.size() > 0) {
								//Harvested Acres
								if(dopField.getFieldAcres() != null && dopField.getFieldAcres() > 0 && Boolean.TRUE.equals(dopField.getIsQuantityInsurableInd())) {
									harvestedAcres = dopField.getFieldAcres();
								}
	
								Double calculatedQuantityHarvest;
	
								for (DopYieldFieldForageCut cut : totalEligileCuts) {
									Integer bales = cut.getTotalBalesLoads();
									Double moisture = cut.getMoisturePercent();
									Double weight = cut.getWeightDefaultUnit(); //Needs to be the default unit
	
									calculatedQuantityHarvest = bales * weight * (1 - (moisture/100));
									
									//Quantity Harvested Tons
									if(dopField.getPlantDurationTypeCode().equalsIgnoreCase(InventoryServiceEnums.PlantDurationType.PERENNIAL.toString())) {
										//Perennials need another step in the calculation
										calculatedQuantityHarvest = calculatedQuantityHarvest / 0.85;
									}
									
									quantityHarvestedTons = quantityHarvestedTons + calculatedQuantityHarvest;
									totalBales = totalBales + bales;
									
								}
							}
							
							//Add to Yield Field Rollup Forage list ******************************************
							addYieldRollupForageToList(declaredYieldContractGuid, dopNewYieldFieldRollupForageList,
									dopField, totalFieldAcres, harvestedAcres, quantityHarvestedTons, totalBales);
							
							//Add to Yield Contract Commodity Forage list ******************************************
							addContractCommodityForageToList(declaredYieldContractGuid,
									dopNewYieldContractCommodityForageList, dopField, totalFieldAcres);
						}
					}
				}
			}
		}
		
		//Add Yield Field Rollup Forage list to contract ******************************************
		dopYieldContract.setDopYieldFieldRollupForageList(dopNewYieldFieldRollupForageList);
		
		//Calculate Contract Commodity values
		calculateContractCommoditiesForage(dopYieldContract, ymucMap, dopNewYieldContractCommodityForageList);
		
		//Add Yield Field Rollup Forage list to contract ******************************************
		dopYieldContract.setDopYieldContractCommodityForageList(dopNewYieldContractCommodityForageList);
		
		logger.debug(">calculateForageDop");

	}
	
	public String updateDeclaredYieldFieldForage(DopYieldFieldForageCut dopYieldFieldForage, String userId) throws DaoException {

		logger.debug("<updateDeclaredYieldFieldForage");

		DeclaredYieldFieldForageDto dto = null;

		if (dopYieldFieldForage.getDeclaredYieldFieldForageGuid() != null) {
			dto = declaredYieldFieldForageDao.fetch(dopYieldFieldForage.getDeclaredYieldFieldForageGuid());
		}

		String declaredYieldFieldForageGuid = null;

		if (dto == null) {
			// Insert if it doesn't exist
			declaredYieldFieldForageGuid = insertDeclaredYieldFieldForage(dopYieldFieldForage, userId);
		} else {
			declaredYieldFieldForageGuid = dto.getDeclaredYieldFieldForageGuid();

			dopYieldContractRsrcFactory.updateDto(dto, dopYieldFieldForage);

			declaredYieldFieldForageDao.update(dto, userId);
		}
		
		logger.debug(">updateDeclaredYieldFieldForage");

		return declaredYieldFieldForageGuid;
	}	
	
	private String insertDeclaredYieldFieldForage(DopYieldFieldForageCut dopYieldFieldForage, String userId) throws DaoException {

		DeclaredYieldFieldForageDto dto = new DeclaredYieldFieldForageDto();
		dopYieldContractRsrcFactory.updateDto(dto, dopYieldFieldForage);

		dto.setDeclaredYieldFieldForageGuid(null);
		dto.setInventoryFieldGuid(dopYieldFieldForage.getInventoryFieldGuid());

		declaredYieldFieldForageDao.insert(dto, userId);

		return dto.getDeclaredYieldFieldForageGuid();
	}
	
	public void updateDeclaredYieldFieldRollup(String declaredYieldContractGuid,
			DopYieldContractRsrc dopYieldContract, String userId,
			Map<String, YieldMeasUnitConversionDto> unitConversionMap) throws DaoException {

		// Delete rollup records
		declaredYieldFieldRollupDao.deleteForDeclaredYieldContract(declaredYieldContractGuid);

		if (dopYieldContract.getFields() != null && dopYieldContract.getFields().size() > 0) {
			// Convert units and calculate rollup values
			calculateYieldRollup(declaredYieldContractGuid, dopYieldContract, unitConversionMap);
			// Insert records
			for (DopYieldFieldRollup dyfr : dopYieldContract.getDopYieldFieldRollupList()) {
				DeclaredYieldFieldRollupDto dto = new DeclaredYieldFieldRollupDto();
				dopYieldContractRsrcFactory.updateDto(dto, dyfr);
				declaredYieldFieldRollupDao.insert(dto, userId);
			}
		}
	}
	
	public void updateDeclaredYieldContractCommodityForage(String declaredYieldContractGuid,
			DopYieldContractRsrc dopYieldContract, 
			Map<String, YieldMeasUnitConversionDto> ymucMap, String userId) throws DaoException {

		logger.debug("<updateDeclaredYieldContractCommodityForage");
		
		//Delete all existing records
		declaredYieldContractCommodityForageDao.deleteForDeclaredYieldContract(declaredYieldContractGuid);
		
		if (dopYieldContract.getFields() != null && dopYieldContract.getFields().size() > 0) {
			if(dopYieldContract.getDopYieldContractCommodityForageList() != null && dopYieldContract.getDopYieldContractCommodityForageList().size() > 0) {
				// Insert records
				for (DopYieldContractCommodityForage dyccf : dopYieldContract.getDopYieldContractCommodityForageList()) {
					DeclaredYieldContractCommodityForageDto dto = new DeclaredYieldContractCommodityForageDto();
					dopYieldContractRsrcFactory.updateDto(dto, dyccf);
					declaredYieldContractCommodityForageDao.insert(dto, userId);
				}
			}
		}

		logger.debug(">updateDeclaredYieldContractCommodityForage");
		
	}
	
	public void updateDeclaredYieldFieldRollupForage(String declaredYieldContractGuid,
			DopYieldContractRsrc dopYieldContract, String userId) throws DaoException {

		logger.debug("<updateDeclaredYieldFieldRollupForage");
		
		//Delete all existing records
		declaredYieldFieldRollupForageDao.deleteForDeclaredYieldContract(declaredYieldContractGuid);
		
		if (dopYieldContract.getFields() != null && dopYieldContract.getFields().size() > 0) {
			if(dopYieldContract.getDopYieldFieldRollupForageList() != null && dopYieldContract.getDopYieldFieldRollupForageList().size() > 0) {
				// Insert records
				for (DopYieldFieldRollupForage dyrf : dopYieldContract.getDopYieldFieldRollupForageList()) {
					DeclaredYieldFieldRollupForageDto dto = new DeclaredYieldFieldRollupForageDto();
					dopYieldContractRsrcFactory.updateDto(dto, dyrf);
					declaredYieldFieldRollupForageDao.insert(dto, userId);
				}
			}
		}

		logger.debug(">updateDeclaredYieldFieldRollupForage");
		
	}
	
	public String updateDeclaredYieldField(DopYieldFieldGrain dopYieldField, String userId) throws DaoException {

		logger.debug("<updateDeclaredYieldField");

		DeclaredYieldFieldDto dto = null;

		if (dopYieldField.getDeclaredYieldFieldGuid() != null) {
			dto = declaredYieldFieldDao.fetch(dopYieldField.getDeclaredYieldFieldGuid());
		}

		String declaredYieldFieldGuid = null;

		if (dto == null) {
			// Insert if it doesn't exist
			declaredYieldFieldGuid = insertDeclaredYieldField(dopYieldField, userId);
		} else {
			declaredYieldFieldGuid = dto.getDeclaredYieldFieldGuid();

			dopYieldContractRsrcFactory.updateDto(dto, dopYieldField);

			declaredYieldFieldDao.update(dto, userId);
		}
		
		logger.debug(">updateDeclaredYieldField");

		return declaredYieldFieldGuid;
	}
	
	private String insertDeclaredYieldField(DopYieldFieldGrain dopYieldField, String userId) throws DaoException {

		DeclaredYieldFieldDto dto = new DeclaredYieldFieldDto();
		dopYieldContractRsrcFactory.updateDto(dto, dopYieldField);

		dto.setDeclaredYieldFieldGuid(null);
		dto.setInventoryFieldGuid(dopYieldField.getInventoryFieldGuid());

		declaredYieldFieldDao.insert(dto, userId);

		return dto.getDeclaredYieldFieldGuid();
	}
	
	// This is only used in unit tests
	@Transactional(readOnly = true, rollbackFor = Exception.class)
	public DopYieldContractRsrc calculateYieldRollupTest(DopYieldContractRsrc dopYieldContract)
			throws ServiceException, DaoException {

		Map<String, YieldMeasUnitConversionDto> ymucMap = loadYieldMeasUnitConversionsMap(
				dopYieldContract.getCropYear(), dopYieldContract.getInsurancePlanId());

		calculateYieldRollup(dopYieldContract.getDeclaredYieldContractGuid(), dopYieldContract, ymucMap);

		return dopYieldContract;
	}

	private void calculateYieldRollup(String declaredYieldContractGuid,
			DopYieldContractRsrc dopYieldContract, Map<String, YieldMeasUnitConversionDto> ymucMap) {

		// Reset the list
		dopYieldContract.setDopYieldFieldRollupList(null);
		List<DopYieldFieldRollup> dopYieldFieldRollupList = new ArrayList<DopYieldFieldRollup>();

		if (dopYieldContract.getFields() != null && dopYieldContract.getFields().size() > 0) {

			for (AnnualFieldRsrc field : dopYieldContract.getFields()) {

				if (field.getDopYieldFieldGrainList() != null && field.getDopYieldFieldGrainList().size() > 0) {
					// Loop through each dop field record

					for (DopYieldFieldGrain dopField : field.getDopYieldFieldGrainList()) {
						// Only necessary if the yield is not null
						if (dopField.getEstimatedYieldPerAcre() != null) {

							List<DopYieldFieldRollup> dyfFiltered = null;

							if (dopYieldFieldRollupList != null && dopYieldFieldRollupList.size() > 0) {
								dyfFiltered = dopYieldFieldRollupList.stream()
										.filter(x -> x.getCropCommodityId().equals(dopField.getCropCommodityId())
												&& x.getIsPedigreeInd().equals(dopField.getIsPedigreeInd()))
										.collect(Collectors.toList());
							}

							double seededAcres = notNull(dopField.getSeededAcres(), (double) 0);
							// Multiply estimated yield per acre with seeded acres = total estimated yield
							double totalEstimatedYield = seededAcres * dopField.getEstimatedYieldPerAcre();

							if (dyfFiltered == null || dyfFiltered.size() == 0) {
								// commodity/is pedigree not in the list yet - Add it
								DopYieldFieldRollup dopYieldFieldRollup = new DopYieldFieldRollup();
								dopYieldFieldRollup.setDeclaredYieldContractGuid(declaredYieldContractGuid);
								dopYieldFieldRollup.setCropCommodityId(dopField.getCropCommodityId());
								dopYieldFieldRollup.setIsPedigreeInd(dopField.getIsPedigreeInd());
								dopYieldFieldRollup.setTotalAcres(seededAcres);
								dopYieldFieldRollup.setTotalYield(totalEstimatedYield);
								dopYieldFieldRollupList.add(dopYieldFieldRollup);

							} else {
								// commodity already exists in the list. Add the new values
								DopYieldFieldRollup dopYieldFieldRollup = dyfFiltered.get(0);
								dopYieldFieldRollup.setTotalAcres(seededAcres + dopYieldFieldRollup.getTotalAcres());
								dopYieldFieldRollup
										.setTotalYield(totalEstimatedYield + dopYieldFieldRollup.getTotalYield());
							}
						}
					}
				}
			}
		}

		dopYieldContract.setDopYieldFieldRollupList(dopYieldFieldRollupList);

		// Convert units
		if (dopYieldContract.getDopYieldFieldRollupList() != null
				&& dopYieldContract.getDopYieldFieldRollupList().size() > 0) {
			for (DopYieldFieldRollup dyfr : dopYieldContract.getDopYieldFieldRollupList()) {

				if (dyfr.getTotalYield() != null && dyfr.getTotalYield() > 0) {
					// Divide total estimated yield with total acres = total estimated yield per
					// acre
					double estimatedYieldPerAcre = dyfr.getTotalYield() / dyfr.getTotalAcres();

					dyfr.setEstimatedYieldPerAcreTonnes(underwritingServiceHelper.convertEstimatedYield(dopYieldContract, "TONNE",
							dyfr.getCropCommodityId(), estimatedYieldPerAcre, ymucMap));

					dyfr.setEstimatedYieldPerAcreBushels(underwritingServiceHelper.convertEstimatedYield(dopYieldContract, "BUSHEL",
							dyfr.getCropCommodityId(), estimatedYieldPerAcre, ymucMap));
				} else {
					dyfr.setEstimatedYieldPerAcreTonnes(null);
					dyfr.setEstimatedYieldPerAcreBushels(null);
				}
			}
		}
	}
	
	public Map<String, YieldMeasUnitConversionDto> loadYieldMeasUnitConversionsMap(Integer cropYear,
			Integer insurancePlanId) throws DaoException, ServiceException {

		Map<String, YieldMeasUnitConversionDto> dtoMap = new HashMap<String, YieldMeasUnitConversionDto>();
		List<YieldMeasUnitConversionDto> dtoList = yieldMeasUnitConversionDao.selectByYearAndPlan(cropYear,
				insurancePlanId);

		if (dtoList != null && !dtoList.isEmpty()) {
			for (YieldMeasUnitConversionDto dto : dtoList) {
				String key = dto.getCropCommodityId() + "::" + dto.getSrcYieldMeasUnitTypeCode() + "::"
						+ dto.getTargetYieldMeasUnitTypeCode();

				if (dtoMap.containsKey(key)) {
					// Should never happen; there is a duplicate conversion.
					throw new ServiceException("Found duplicate conversion for commodity id " + dto.getCropCommodityId()
							+ ", Src Unit " + dto.getSrcYieldMeasUnitTypeCode() + ", Target Unit "
							+ dto.getTargetYieldMeasUnitTypeCode() + ", for Crop Year " + cropYear);
				} else {
					dtoMap.put(key, dto);
				}
			}
		}

		return dtoMap;
	}
	
	// This is only used in unit tests
	@Transactional(readOnly = true, rollbackFor = Exception.class)
	public DopYieldContractRsrc calculateYieldFieldRollupForageTest(DopYieldContractRsrc dopYieldContract)
			throws ServiceException, DaoException {

		Map<String, YieldMeasUnitConversionDto> ymucMap = loadYieldMeasUnitConversionsMap(
				dopYieldContract.getCropYear(), dopYieldContract.getInsurancePlanId());

		calculateForageDop(dopYieldContract.getDeclaredYieldContractGuid(), dopYieldContract, ymucMap);

		return dopYieldContract;
	}
	
	// This is only used in unit tests
	@Transactional(readOnly = true, rollbackFor = Exception.class)
	public DopYieldContractRsrc calculateYieldContractCommodityForageTest(DopYieldContractRsrc dopYieldContract)
			throws ServiceException, DaoException {
		
		Map<String, YieldMeasUnitConversionDto> ymucMap = loadYieldMeasUnitConversionsMap(
				dopYieldContract.getCropYear(), dopYieldContract.getInsurancePlanId());

		calculateForageDop(dopYieldContract.getDeclaredYieldContractGuid(), dopYieldContract, ymucMap);

		return dopYieldContract;
	}
	
	// This is only used in unit tests
	@Transactional(readOnly = true, rollbackFor = Exception.class)
	public double convertEstimatedYieldTest(DopYieldContractRsrc dopYieldContract, String targetUnit,
			Integer cropCommodityId, double valueToConvert) throws ServiceException, DaoException {

		Map<String, YieldMeasUnitConversionDto> ymucMap = loadYieldMeasUnitConversionsMap(
				dopYieldContract.getCropYear(), dopYieldContract.getInsurancePlanId());

		return underwritingServiceHelper.convertEstimatedYield(dopYieldContract, targetUnit, cropCommodityId, valueToConvert, ymucMap);

	}
	
	private void addYieldRollupForageToList(String declaredYieldContractGuid,
			List<DopYieldFieldRollupForage> dopNewYieldFieldRollupForageList, DopYieldFieldForage dopField,
			Double totalFieldAcres, Double harvestedAcres, Double quantityHarvestedTons, Integer totalBales) {
		List<DopYieldFieldRollupForage> dyrfAddedToNewList = new ArrayList<DopYieldFieldRollupForage>();
		
		//Check if the record has been added to the new list already
		if (dopNewYieldFieldRollupForageList != null && dopNewYieldFieldRollupForageList.size() > 0) {
			dyrfAddedToNewList = dopNewYieldFieldRollupForageList.stream()
					.filter(x -> x.getCommodityTypeCode().equals(dopField.getCommodityTypeCode()))
					.collect(Collectors.toList());
		}
		
		DopYieldFieldRollupForage dyrf = null;
		
		if (dyrfAddedToNewList == null || dyrfAddedToNewList.size() == 0) {
			// commodity type not in the list yet - Add it
			dyrf = new DopYieldFieldRollupForage();
			dyrf.setDeclaredYieldContractGuid(declaredYieldContractGuid);
			dyrf.setCommodityTypeCode(dopField.getCommodityTypeCode());
			dyrf.setTotalFieldAcres(totalFieldAcres);
			dyrf.setHarvestedAcres(harvestedAcres);
			dyrf.setTotalBalesLoads(totalBales);
			dyrf.setQuantityHarvestedTons(quantityHarvestedTons);
			//Calculate yield per acre
			Double yieldPerAcre = (double)0;
			if(dyrf.getHarvestedAcres() > 0) {
				yieldPerAcre = dyrf.getQuantityHarvestedTons() / dyrf.getHarvestedAcres();
			}
			dyrf.setYieldPerAcre(yieldPerAcre);								
			dopNewYieldFieldRollupForageList.add(dyrf);

		} else {
			// commodity type already exists in the new list. Add the new values
			dyrf = dyrfAddedToNewList.get(0);
			dyrf.setTotalFieldAcres(totalFieldAcres + dyrf.getTotalFieldAcres());
			dyrf.setHarvestedAcres(harvestedAcres + dyrf.getHarvestedAcres());
			dyrf.setTotalBalesLoads(totalBales + dyrf.getTotalBalesLoads());
			dyrf.setQuantityHarvestedTons(quantityHarvestedTons + dyrf.getQuantityHarvestedTons());
			//Calculate yield per acre
			Double yieldPerAcre = (double)0;
			if(dyrf.getHarvestedAcres() > 0) {
				yieldPerAcre = dyrf.getQuantityHarvestedTons() / dyrf.getHarvestedAcres();
			}
			dyrf.setYieldPerAcre(yieldPerAcre);								
		}
	}
	
	private void addContractCommodityForageToList(String declaredYieldContractGuid,
			List<DopYieldContractCommodityForage> dopNewYieldContractCommodityForageList, DopYieldFieldForage dopField,
			Double totalFieldAcres) {
		
		List<DopYieldContractCommodityForage> dyccfAddedToNewList = new ArrayList<DopYieldContractCommodityForage>();
		//Check if the record has been added to the new list already
		if (dopNewYieldContractCommodityForageList != null && dopNewYieldContractCommodityForageList.size() > 0) {
			dyccfAddedToNewList = dopNewYieldContractCommodityForageList.stream()
					.filter(x -> x.getCommodityTypeCode().equals(dopField.getCommodityTypeCode()))
					.collect(Collectors.toList());
		}

		DopYieldContractCommodityForage dyccf = null;
		
		if (dyccfAddedToNewList == null || dyccfAddedToNewList.size() == 0) {
			// commodity type not in the list yet - Add it
			dyccf = new DopYieldContractCommodityForage();
			dyccf.setDeclaredYieldContractGuid(declaredYieldContractGuid);
			dyccf.setCommodityTypeCode(dopField.getCommodityTypeCode());
			dyccf.setTotalFieldAcres(totalFieldAcres);
			
			dopNewYieldContractCommodityForageList.add(dyccf);

		} else {
			// commodity type already exists in the new list. Add the new values
			dyccf = dyccfAddedToNewList.get(0);
			dyccf.setTotalFieldAcres(totalFieldAcres + dyccf.getTotalFieldAcres());
		}
	}

	private void calculateContractCommoditiesForage(DopYieldContractRsrc dopYieldContract,
			Map<String, YieldMeasUnitConversionDto> ymucMap,
			List<DopYieldContractCommodityForage> dopNewYieldContractCommodityForageList) {
		
		if(dopNewYieldContractCommodityForageList != null && dopNewYieldContractCommodityForageList.size() > 0) {

			for(DopYieldContractCommodityForage newContractCommodity : dopNewYieldContractCommodityForageList) {
				//Get existing record from contract 
				DopYieldContractCommodityForage existingContractCommodity = getExistingContractCommodity(dopYieldContract, newContractCommodity.getCommodityTypeCode());
				
				Double calculatedQuantityHarvest = null;
				//If it exists, check user entered values and calculate harvested tons and yld/acre
				if(existingContractCommodity != null) {
					newContractCommodity.setCropCommodityId(existingContractCommodity.getCropCommodityId());
					newContractCommodity.setPlantDurationTypeCode(existingContractCommodity.getPlantDurationTypeCode());
					newContractCommodity.setHarvestedAcres(existingContractCommodity.getHarvestedAcres());
					newContractCommodity.setTotalBalesLoads(existingContractCommodity.getTotalBalesLoads());
					newContractCommodity.setWeight(existingContractCommodity.getWeight());
					newContractCommodity.setWeightDefaultUnit(underwritingServiceHelper.convertDopYieldFieldAcresWeight(existingContractCommodity.getWeight(), existingContractCommodity.getCropCommodityId(), dopYieldContract, ymucMap));
					newContractCommodity.setMoisturePercent(existingContractCommodity.getMoisturePercent());
				
					
					if(newContractCommodity.getTotalBalesLoads() != null && newContractCommodity.getTotalBalesLoads() > 0 &&
							newContractCommodity.getWeightDefaultUnit() != null && newContractCommodity.getWeightDefaultUnit() > 0 &&
							newContractCommodity.getMoisturePercent() != null
							) {
						
						Integer bales = newContractCommodity.getTotalBalesLoads();
						Double moisture = newContractCommodity.getMoisturePercent();
						Double weight = newContractCommodity.getWeightDefaultUnit(); //Needs to be the default unit
						
						//Calculate Quantity Harvested Tons
						calculatedQuantityHarvest = bales * weight * (1 - (moisture/100));
						
						if(newContractCommodity.getPlantDurationTypeCode().equalsIgnoreCase(InventoryServiceEnums.PlantDurationType.PERENNIAL.toString())) {
							//Perennials need another step in the calculation
							calculatedQuantityHarvest = calculatedQuantityHarvest / 0.85;
						}
					}
				}
				
				//Calculate yield/acre
				newContractCommodity.setQuantityHarvestedTons(calculatedQuantityHarvest);
				Double yieldPerAcre = null;
				if(newContractCommodity.getHarvestedAcres() != null && newContractCommodity.getHarvestedAcres() > 0 && calculatedQuantityHarvest != null) {
					yieldPerAcre = calculatedQuantityHarvest / newContractCommodity.getHarvestedAcres();
				}
				newContractCommodity.setYieldPerAcre(yieldPerAcre);

			}
		}
	}
	
	private DopYieldContractCommodityForage getExistingContractCommodity(DopYieldContractRsrc dopYieldContract,
			String commodityTypeCode) {
		List<DopYieldContractCommodityForage> dyccfFiltered = new ArrayList<DopYieldContractCommodityForage>();
		
		if (dopYieldContract.getDopYieldContractCommodityForageList() != null && dopYieldContract.getDopYieldContractCommodityForageList().size() > 0) {
			dyccfFiltered = dopYieldContract.getDopYieldContractCommodityForageList().stream()
					.filter(x -> x.getCommodityTypeCode().equals(commodityTypeCode))
					.collect(Collectors.toList());
		}
		
		if (dyccfFiltered != null && dyccfFiltered.size() > 0) {
			// commodity type exists in the list
			return dyccfFiltered.get(0);
		}
		return null;
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

	private List<VerifiedYieldContractCommodity> getVerifiedYieldContractCommoditiesToRollup(Integer cropCommodityId, List<VerifiedYieldContractCommodity> vyccList) {
		
		if(vyccList != null && !vyccList.isEmpty()) {
		
			return vyccList.stream()
					.filter(x -> x.getCropCommodityId().equals(cropCommodityId) && Boolean.FALSE.equals(x.getIsRolledupInd()))
					.collect(Collectors.toList());
		} else {
			return null;
		}
	}
	
	// Calculates VerifiedYieldGrainBasket.totalQuantityCoverageValue.
	private double calculateTotalQuantityCoverageValue(List<ProductDto> products) {
		
		double result = 0.0;
		
		if ( products != null && !products.isEmpty() ) {
			for ( ProductDto prd : products ) {
				if ( prd.getCommodityCoverageCode().equals(CommodityCoverageCode.QUANTITY_GRAIN) && prd.getProductStatusCode().equals(PRODUCT_STATUS_FINAL) && prd.getCoverageDollars() != null ) {
					result += prd.getCoverageDollars();
				}
			}
		}
		
		return result;
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
			verifiedYieldContractRsrcFactory.updateDto(dto, verifiedGrainBasket);

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

		verifiedYieldContractRsrcFactory.updateDto(dto, verifiedYieldGrainBasket);

		dto.setVerifiedYieldGrainBasketGuid(null);
		dto.setVerifiedYieldContractGuid(verifiedYieldContractGuid);

		verifiedYieldGrainBasketDao.insert(dto, userId);
		
		verifiedYieldGrainBasket.setVerifiedYieldGrainBasketGuid(dto.getVerifiedYieldGrainBasketGuid());

		logger.debug(">insertVerifiedYieldGrainBasket");

	}
	
	private List<ProductDto> loadProducts(Integer contractId, Integer cropYear) throws DaoException {
		return productDao.getForPolicy(contractId, cropYear);
	}

	private Double notNull(Double value, Double defaultValue) {
		return (value == null) ? defaultValue : value;
	}
	
}
