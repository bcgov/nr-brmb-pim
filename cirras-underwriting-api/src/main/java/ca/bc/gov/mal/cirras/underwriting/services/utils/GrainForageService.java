package ca.bc.gov.mal.cirras.underwriting.services.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.mal.cirras.underwriting.data.models.VerifiedYieldContractCommodity;
import ca.bc.gov.mal.cirras.underwriting.data.models.VerifiedYieldGrainBasket;
import ca.bc.gov.mal.cirras.underwriting.data.models.VerifiedYieldSummary;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.InventorySeededForageDao;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.InventorySeededGrainDao;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.ProductDao;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.VerifiedYieldGrainBasketDao;
import ca.bc.gov.mal.cirras.underwriting.data.resources.VerifiedYieldContractRsrc;
import ca.bc.gov.mal.cirras.underwriting.services.utils.InventoryServiceEnums.InsurancePlans;
import ca.bc.gov.mal.cirras.underwriting.data.entities.DeclaredYieldContractCommodityForageDto;
import ca.bc.gov.mal.cirras.underwriting.data.entities.DeclaredYieldContractDto;
import ca.bc.gov.mal.cirras.underwriting.data.entities.DeclaredYieldFieldRollupForageDto;
import ca.bc.gov.mal.cirras.underwriting.data.entities.InventoryFieldDto;
import ca.bc.gov.mal.cirras.underwriting.data.entities.InventorySeededForageDto;
import ca.bc.gov.mal.cirras.underwriting.data.entities.InventorySeededGrainDto;
import ca.bc.gov.mal.cirras.underwriting.data.entities.ProductDto;
import ca.bc.gov.mal.cirras.underwriting.data.entities.VerifiedYieldContractDto;
import ca.bc.gov.mal.cirras.underwriting.data.entities.VerifiedYieldGrainBasketDto;
import ca.bc.gov.mal.cirras.underwriting.data.assemblers.VerifiedYieldContractRsrcFactory;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.webade.authentication.WebAdeAuthentication;

public class GrainForageService {

	private static final Logger logger = LoggerFactory.getLogger(GrainForageService.class);

	private InventorySeededForageDao inventorySeededForageDao;
	private InventorySeededGrainDao inventorySeededGrainDao;
	private VerifiedYieldGrainBasketDao verifiedYieldGrainBasketDao;
	private ProductDao productDao;

	private VerifiedYieldContractRsrcFactory verifiedYieldContractRsrcFactory;
	
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

	public void setVerifiedYieldContractRsrcFactory(VerifiedYieldContractRsrcFactory verifiedYieldContractRsrcFactory) {
		this.verifiedYieldContractRsrcFactory = verifiedYieldContractRsrcFactory;
	}
	
	
	public void loadSeededGrains(InventoryFieldDto ifDto) throws DaoException {
		List<InventorySeededGrainDto> inventorySeededGrains = inventorySeededGrainDao.selectForVerifiedYield(ifDto.getInventoryFieldGuid());
		ifDto.setInventorySeededGrains(inventorySeededGrains);
	}
	
	public void loadSeededForage(InventoryFieldDto ifDto) throws DaoException {
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
