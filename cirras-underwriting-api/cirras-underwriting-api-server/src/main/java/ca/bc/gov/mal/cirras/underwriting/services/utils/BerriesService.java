package ca.bc.gov.mal.cirras.underwriting.services.utils;

import java.util.ArrayList;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.mal.cirras.underwriting.data.models.DopYieldContractCommodityBerries;
import ca.bc.gov.mal.cirras.underwriting.data.models.DopYieldFieldCommodityBerries;
import ca.bc.gov.mal.cirras.underwriting.data.models.DopYieldFieldVarietyBerries;
import ca.bc.gov.mal.cirras.underwriting.data.models.InventoryBerries;
import ca.bc.gov.mal.cirras.underwriting.data.models.InventoryContractCommodityBerries;
import ca.bc.gov.mal.cirras.underwriting.data.models.InventoryField;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.DeclaredYieldContractCommodityBerriesDao;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.DeclaredYieldFieldCommodityBerriesDao;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.DeclaredYieldFieldVarietyBerriesDao;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.InventoryBerriesDao;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.InventoryContractCommodityBerriesDao;
import ca.bc.gov.mal.cirras.underwriting.data.resources.AnnualFieldRsrc;
import ca.bc.gov.mal.cirras.underwriting.data.resources.DopYieldContractRsrc;
import ca.bc.gov.mal.cirras.underwriting.data.resources.InventoryContractRsrc;
import ca.bc.gov.mal.cirras.underwriting.data.entities.CommodityMaturityScaleDto;
import ca.bc.gov.mal.cirras.underwriting.data.entities.ContractedFieldDetailDto;
import ca.bc.gov.mal.cirras.underwriting.data.entities.DeclaredYieldContractCommodityBerriesDto;
import ca.bc.gov.mal.cirras.underwriting.data.entities.DeclaredYieldFieldCommodityBerriesDto;
import ca.bc.gov.mal.cirras.underwriting.data.entities.DeclaredYieldFieldVarietyBerriesDto;
import ca.bc.gov.mal.cirras.underwriting.data.entities.InventoryBerriesDto;
import ca.bc.gov.mal.cirras.underwriting.data.entities.InventoryContractCommodityBerriesDto;
import ca.bc.gov.mal.cirras.underwriting.data.entities.InventoryFieldDto;
import ca.bc.gov.mal.cirras.underwriting.data.assemblers.DopYieldContractRsrcFactory;
import ca.bc.gov.mal.cirras.underwriting.data.assemblers.InventoryContractRsrcFactory;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.service.api.ServiceException;

public class BerriesService {

	private static final Logger logger = LoggerFactory.getLogger(BerriesService.class);

	//Daos
	private InventoryContractCommodityBerriesDao inventoryContractCommodityBerriesDao;
	private InventoryBerriesDao inventoryBerriesDao;

	private DeclaredYieldContractCommodityBerriesDao declaredYieldContractCommodityBerriesDao;
	private DeclaredYieldFieldCommodityBerriesDao declaredYieldFieldCommodityBerriesDao;
	private DeclaredYieldFieldVarietyBerriesDao declaredYieldFieldVarietyBerriesDao;
	
	//Factories
	private InventoryContractRsrcFactory inventoryContractRsrcFactory;
	private DopYieldContractRsrcFactory dopYieldContractRsrcFactory;

	
	public void setInventoryContractCommodityBerriesDao(InventoryContractCommodityBerriesDao inventoryContractCommodityBerriesDao) {
		this.inventoryContractCommodityBerriesDao = inventoryContractCommodityBerriesDao;
	}

	public void setInventoryBerriesDao(InventoryBerriesDao inventoryBerriesDao) {
		this.inventoryBerriesDao = inventoryBerriesDao;
	}

	public void setDeclaredYieldContractCommodityBerriesDao(DeclaredYieldContractCommodityBerriesDao declaredYieldContractCommodityBerriesDao) {
		this.declaredYieldContractCommodityBerriesDao = declaredYieldContractCommodityBerriesDao;
	}
	
	public void setDeclaredYieldFieldCommodityBerriesDao(DeclaredYieldFieldCommodityBerriesDao declaredYieldFieldCommodityBerriesDao) {
		this.declaredYieldFieldCommodityBerriesDao = declaredYieldFieldCommodityBerriesDao;
	}

	public void setDeclaredYieldFieldVarietyBerriesDao(DeclaredYieldFieldVarietyBerriesDao declaredYieldFieldVarietyBerriesDao) {
		this.declaredYieldFieldVarietyBerriesDao = declaredYieldFieldVarietyBerriesDao;
	}
	
	public void setInventoryContractRsrcFactory(InventoryContractRsrcFactory inventoryContractRsrcFactory) {
		this.inventoryContractRsrcFactory = inventoryContractRsrcFactory;
	}

	public void setDopYieldContractRsrcFactory(DopYieldContractRsrcFactory dopYieldContractRsrcFactory) {
		this.dopYieldContractRsrcFactory = dopYieldContractRsrcFactory;
	}
	
	public void updateInventoryBerries(
			InventoryBerries inventoryBerries, 
			String inventoryFieldGuid, 
			List<CommodityMaturityScaleDto> scaleDto, 
			Integer cropYear,
			String userId)
			throws DaoException {

		// inventoryUnseeded.getInventoryUnseededGuid() might be null if it's a new crop
		InventoryBerriesDto dto = null;
		if (inventoryBerries.getInventoryBerriesGuid() != null) {
			dto = inventoryBerriesDao.fetch(inventoryBerries.getInventoryBerriesGuid());
		}
		
		//Calculates and sets total plants
		calculateTotalPlants(inventoryBerries);
		
		//Calculates ME Acres
		calculateMEAcres(inventoryBerries, scaleDto, cropYear);

		if (dto == null) {
			// Insert if it doesn't exist
			insertInventoryBerries(inventoryBerries, inventoryFieldGuid, userId);
		} else {

			inventoryContractRsrcFactory.updateDto(dto, inventoryBerries);

			inventoryBerriesDao.update(dto, userId);
		}
	}
	
	public String insertInventoryBerries(InventoryBerries inventoryBerries, String inventoryFieldGuid,
			String userId) throws DaoException {

		InventoryBerriesDto dto = new InventoryBerriesDto();
		inventoryContractRsrcFactory.updateDto(dto, inventoryBerries);

		dto.setInventoryBerriesGuid(null);
		dto.setInventoryFieldGuid(inventoryFieldGuid);

		inventoryBerriesDao.insert(dto, userId);

		return dto.getInventoryBerriesGuid();
	}
	
	private void calculateMEAcres(InventoryBerries inventoryBerries, List<CommodityMaturityScaleDto> scaleDto, Integer cropYear) {
		//Default = planted acres
		inventoryBerries.setMatureEquivalentAcres(inventoryBerries.getPlantedAcres());
		
		if(inventoryBerries.getPlantedAcres() != null 
				&& inventoryBerries.getPlantedYear() != null 
				&& inventoryBerries.getCropCommodityId() != null
				&& scaleDto != null && scaleDto.size() > 0) {
			//Calculate ME Acres if plants are not fully matured
			
			Integer plantAge = cropYear - inventoryBerries.getPlantedYear();
			
			List<CommodityMaturityScaleDto> filteredScales = scaleDto.stream()
						.filter(x -> x.getCropCommodityId().equals(inventoryBerries.getCropCommodityId())
								&& x.getPlantAge().equals(plantAge))
						.collect(Collectors.toList());
			
			if(filteredScales != null && filteredScales.size() == 1){
				double calculatedMea = filteredScales.get(0).getScale() * inventoryBerries.getPlantedAcres();
				inventoryBerries.setMatureEquivalentAcres(calculatedMea);
			} else if (filteredScales.size() > 1) {
				throw new ServiceException("Too many scale records found for commodityId " + inventoryBerries.getCropCommodityId() + ": " + filteredScales.size());
			}
		}
	}	
	
	private void calculateTotalPlants(InventoryBerries inventoryBerries) {
		//Default
		inventoryBerries.setTotalPlants(0);
		if(inventoryBerries.getPlantedAcres() != null && inventoryBerries.getRowSpacing() != null && inventoryBerries.getPlantSpacing() != null) {
			//Calculate total plants
			double spacing = inventoryBerries.getRowSpacing() * inventoryBerries.getPlantSpacing();
			if(spacing > 0) {
				double totalPlants = (inventoryBerries.getPlantedAcres() * 43560) / spacing; 
				inventoryBerries.setTotalPlants(notNull(Math.toIntExact(Math.round(totalPlants)), 0));
			}
		}
	}
	
	public boolean handleDeletedInventoryBerries(InventoryField inventoryField) {

		logger.debug("<handleDeletedInventoryBerries");
		
		boolean doDeleteInventoryField = false;
		InventoryBerries inventory = inventoryField.getInventoryBerries();
		
		if (inventory != null && Boolean.TRUE.equals(inventory.getDeletedByUserInd()) ) {

			// No checks necessary at the moment but might be necessary once yield is implemented
			doDeleteInventoryField = true;

		} 
		
		logger.debug(">handleDeletedInventoryBerries");

		return doDeleteInventoryField;
	}
	

	public void updateInventoryContractCommodityBerries(
			InventoryContractRsrc inventoryContract, 
			String inventoryContractGuid, 
			String userId) throws DaoException {

		//Delete all existing records
		inventoryContractCommodityBerriesDao.deleteForInventoryContract(inventoryContractGuid);
		inventoryContract.setInventoryContractCommodityBerries(null);
		
		if (inventoryContract.getFields() != null && inventoryContract.getFields().size() > 0) {
			//Calculate commodity totals
			calculateTotals(inventoryContract, inventoryContractGuid);
			// Insert records
			if(inventoryContract.getInventoryContractCommodityBerries() != null && inventoryContract.getInventoryContractCommodityBerries().size() > 0) {
				
				for(InventoryContractCommodityBerries iccb : inventoryContract.getInventoryContractCommodityBerries()) {
				
					InventoryContractCommodityBerriesDto dto = new InventoryContractCommodityBerriesDto();
					inventoryContractRsrcFactory.updateDto(dto, iccb);
					inventoryContractCommodityBerriesDao.insert(dto, userId);
				}
			}
		}

	}
	
	//Total Insured Acres are total Quantity insured acres
	//Total Insured Plants are total Plant insured plants
	private void calculateTotals(InventoryContractRsrc inventoryContract, 
			String inventoryContractGuid) throws DaoException {

		List<InventoryContractCommodityBerries> iccbList = new ArrayList<InventoryContractCommodityBerries>();
		
		for (AnnualFieldRsrc field : inventoryContract.getFields()) {
			for ( InventoryField planting : field.getPlantings() ) {
				if(planting.getInventoryBerries() != null) {
					InventoryBerries ib = planting.getInventoryBerries();
					if(!Boolean.TRUE.equals(ib.getDeletedByUserInd()) && ib.getCropCommodityId() != null){
						List<InventoryContractCommodityBerries> iccbFiltered = null;

						if (iccbList != null && iccbList.size() > 0) {
							iccbFiltered = iccbList.stream()
									.filter(x -> x.getCropCommodityId().equals(ib.getCropCommodityId()))
									.collect(Collectors.toList());
						}
						
						Double quantityInsuredAcres = 0.0;
						Double quantityUninsuredAcres = 0.0;
						Double plantInsuredAcres = 0.0;
						Double plantUninsuredAcres = 0.0;
						Integer insuredPlants = 0;
						Integer uninsuredPlants = 0;
						
						if(Boolean.TRUE.equals(ib.getIsQuantityInsurableInd())) {
							//Quantity insurable
							quantityInsuredAcres = notNull(ib.getPlantedAcres(), (double) 0);
						} else {
							//Not Quantity insurable
							quantityUninsuredAcres = notNull(ib.getPlantedAcres(), (double) 0);
						}
						
						if(Boolean.TRUE.equals(ib.getIsPlantInsurableInd())) {
							//Plant insurable
							insuredPlants = notNull(ib.getTotalPlants(), 0);
							plantInsuredAcres = notNull(ib.getPlantedAcres(), (double) 0);
						} else {
							//Not Plant insurable
							uninsuredPlants = notNull(ib.getTotalPlants(), 0);
							plantUninsuredAcres = notNull(ib.getPlantedAcres(), (double) 0);
						}

						if (iccbFiltered == null || iccbFiltered.size() == 0) {
							// commodity is not in the list yet - Add it
							InventoryContractCommodityBerries iccb = new InventoryContractCommodityBerries();
							iccb.setInventoryContractGuid(inventoryContractGuid);
							iccb.setCropCommodityId(ib.getCropCommodityId());
							iccb.setTotalQuantityInsuredAcres(quantityInsuredAcres);
							iccb.setTotalQuantityUninsuredAcres(quantityUninsuredAcres);
							iccb.setTotalPlantInsuredAcres(plantInsuredAcres);
							iccb.setTotalPlantUninsuredAcres(plantUninsuredAcres);
							iccb.setTotalInsuredPlants(insuredPlants);
							iccb.setTotalUninsuredPlants(uninsuredPlants);
							iccbList.add(iccb);

						} else {
							// commodity already exists in the list. Add the new values
							InventoryContractCommodityBerries iccb = iccbFiltered.get(0);
							iccb.setTotalQuantityInsuredAcres(quantityInsuredAcres + iccb.getTotalQuantityInsuredAcres());
							iccb.setTotalQuantityUninsuredAcres(quantityUninsuredAcres + iccb.getTotalQuantityUninsuredAcres());
							iccb.setTotalPlantInsuredAcres(plantInsuredAcres + iccb.getTotalPlantInsuredAcres());
							iccb.setTotalPlantUninsuredAcres(plantUninsuredAcres + iccb.getTotalPlantUninsuredAcres());
							iccb.setTotalInsuredPlants(insuredPlants + iccb.getTotalInsuredPlants());
							iccb.setTotalUninsuredPlants(uninsuredPlants + iccb.getTotalUninsuredPlants());

						}
					}
				}
			}
		}
		
		inventoryContract.setInventoryContractCommodityBerries(iccbList);
	}

	public void calculateDeclaredYieldContractCommodityBerriesList(DopYieldContractRsrc dopYieldContract) {

		logger.debug("<calculateDeclaredYieldContractCommodityBerriesList");

		List<DopYieldContractCommodityBerries> dopYieldContractCommodityBerriesList = dopYieldContract.getDopYieldContractCommodityBerriesList();
		if (dopYieldContractCommodityBerriesList != null && !dopYieldContractCommodityBerriesList.isEmpty()) {
			for (DopYieldContractCommodityBerries dopYieldContractCommodityBerries : dopYieldContractCommodityBerriesList) {
				calculateDeclaredYieldContractCommodityBerries(dopYieldContract, dopYieldContractCommodityBerries);
			}
		}
		
		logger.debug(">calculateDeclaredYieldContractCommodityBerriesList");
	}
	
	private void calculateDeclaredYieldContractCommodityBerries(DopYieldContractRsrc dopYieldContract, DopYieldContractCommodityBerries dopContractCommodityBerries) {

		logger.debug("<calculateDeclaredYieldContractCommodityBerries");

		Double totalProduction = null;
		Double totalSoldShippedYield = null;
		Double totalSalesYield = null;
		Double totalAbandonmentYield = null;


		List<AnnualFieldRsrc> fields = dopYieldContract.getFields();
		if (fields != null && !fields.isEmpty()) {

			for (AnnualFieldRsrc field : fields) {
				List<DopYieldFieldCommodityBerries> dopYieldFieldCommodityBerriesList = field.getDopYieldFieldCommodityBerriesList();
				if (dopYieldFieldCommodityBerriesList != null && !dopYieldFieldCommodityBerriesList.isEmpty()) {

					for (DopYieldFieldCommodityBerries dyfcb : dopYieldFieldCommodityBerriesList) {
						if ( dyfcb.getCropCommodityId().equals(dopContractCommodityBerries.getCropCommodityId()) ) {
							Double currTotalProduction = notNull(dyfcb.getTotalProductionOverride(), dyfcb.getTotalProduction());
							totalProduction = safeAdd(totalProduction, currTotalProduction);
							
							//Calculate other totals from field variety level to contract commodity level
							if(dyfcb.getDopYieldFieldVarietyBerriesList() != null && !dyfcb.getDopYieldFieldVarietyBerriesList().isEmpty()) {
								for (DopYieldFieldVarietyBerries dyfvb : dyfcb.getDopYieldFieldVarietyBerriesList()) {
									totalSoldShippedYield = safeAdd(totalSoldShippedYield, dyfvb.getSoldShippedYield());
									totalSalesYield = safeAdd(totalSalesYield, dyfvb.getSalesYield());
									totalAbandonmentYield = safeAdd(totalAbandonmentYield, dyfvb.getAbandonmentYield());
								}
							}
						}
					}
				}
			}
		}

		dopContractCommodityBerries.setTotalProduction(totalProduction);
		dopContractCommodityBerries.setTotalSoldShippedYield(totalSoldShippedYield);
		dopContractCommodityBerries.setTotalSalesYield(totalSalesYield);
		dopContractCommodityBerries.setTotalAbandonmentYield(totalAbandonmentYield);


		logger.debug(">calculateDeclaredYieldContractCommodityBerries");
	}
	
	public void updateDeclaredYieldContractCommodityBerriesList(String declaredYieldContractGuid, DopYieldContractRsrc dopYieldContract, String userId) throws DaoException {

		logger.debug("<updateDeclaredYieldContractCommodityBerriesList");

		List<DopYieldContractCommodityBerries> dopYieldContractCommodityBerriesList = dopYieldContract.getDopYieldContractCommodityBerriesList();
		if (dopYieldContractCommodityBerriesList != null && !dopYieldContractCommodityBerriesList.isEmpty()) {
			for (DopYieldContractCommodityBerries dopYieldContractCommodityBerries : dopYieldContractCommodityBerriesList) {
				updateDeclaredYieldContractCommodityBerries(declaredYieldContractGuid, dopYieldContract, dopYieldContractCommodityBerries, userId);
			}
		}
		
		logger.debug(">updateDeclaredYieldContractCommodityBerriesList");
	}
	
	private void updateDeclaredYieldContractCommodityBerries(String declaredYieldContractGuid, DopYieldContractRsrc dopYieldContract, 
			DopYieldContractCommodityBerries dopContractCommodityBerries, String userId) throws DaoException {

		logger.debug("<updateDeclaredYieldContractCommodityBerries");

		DeclaredYieldContractCommodityBerriesDto dto = null;

		if (dopContractCommodityBerries.getDeclaredYieldContractCommodityBerriesGuid() != null) {
			dto = declaredYieldContractCommodityBerriesDao.fetch(dopContractCommodityBerries.getDeclaredYieldContractCommodityBerriesGuid());
		}

		if (dto == null) {
			// Insert if it doesn't exist
			insertDeclaredYieldContractCommodityBerries(declaredYieldContractGuid, dopContractCommodityBerries, userId);
		} else {
			dopYieldContractRsrcFactory.updateDto(dto, dopContractCommodityBerries);

			declaredYieldContractCommodityBerriesDao.update(dto, userId);
		}

		logger.debug(">updateDeclaredYieldContractCommodityBerries");
	}
	
	private void insertDeclaredYieldContractCommodityBerries(String declaredYieldContractGuid,
			DopYieldContractCommodityBerries dopContractCommodityBerries, String userId) throws DaoException {

		logger.debug("<insertDeclaredYieldContractCommodityBerries");

		DeclaredYieldContractCommodityBerriesDto dto = new DeclaredYieldContractCommodityBerriesDto();

		dopYieldContractRsrcFactory.updateDto(dto, dopContractCommodityBerries);

		dto.setDeclaredYieldContractCommodityBerriesGuid(null);
		dto.setDeclaredYieldContractGuid(declaredYieldContractGuid);

		declaredYieldContractCommodityBerriesDao.insert(dto, userId);

		logger.debug(">insertDeclaredYieldContractCommodityBerries");
	}	

	public void calculateDeclaredYieldFieldCommodityBerries(DopYieldFieldCommodityBerries dopYieldFieldCommodityBerries) {

		logger.debug("<calculateDeclaredYieldFieldCommodityBerries");

		Double totalProduction = null;
		List<DopYieldFieldVarietyBerries> dyfvbList = dopYieldFieldCommodityBerries.getDopYieldFieldVarietyBerriesList();
		if (dyfvbList != null && !dyfvbList.isEmpty()) {
			for (DopYieldFieldVarietyBerries dyfvb : dyfvbList) {
				Double currTotalProduction = notNull(dyfvb.getTotalProductionOverride(), dyfvb.getTotalProduction());
				totalProduction = safeAdd(totalProduction, currTotalProduction);
			}
		}
		
		dopYieldFieldCommodityBerries.setTotalProduction(totalProduction);
		
		logger.debug(">calculateDeclaredYieldFieldCommodityBerries");
	}
	
	public String updateDeclaredYieldFieldCommodityBerries(DopYieldFieldCommodityBerries dopYieldFieldCommodityBerries, String userId) throws DaoException {

		logger.debug("<updateDeclaredYieldFieldCommodityBerries");

		DeclaredYieldFieldCommodityBerriesDto dto = null;

		if (dopYieldFieldCommodityBerries.getDeclaredYieldFieldCommodityBerriesGuid() != null) {
			dto = declaredYieldFieldCommodityBerriesDao.fetch(dopYieldFieldCommodityBerries.getDeclaredYieldFieldCommodityBerriesGuid());
		}

		String declaredYieldFieldCommodityBerriesGuid = null;

		if (dto == null) {
			// Insert if it doesn't exist
			declaredYieldFieldCommodityBerriesGuid = insertDeclaredYieldFieldCommodityBerries(dopYieldFieldCommodityBerries, userId);
		} else {
			declaredYieldFieldCommodityBerriesGuid = dto.getDeclaredYieldFieldCommodityBerriesGuid();
			dopYieldContractRsrcFactory.updateDto(dto, dopYieldFieldCommodityBerries);

			declaredYieldFieldCommodityBerriesDao.update(dto, userId);
		}
		
		logger.debug(">updateDeclaredYieldFieldCommodityBerries");

		return declaredYieldFieldCommodityBerriesGuid;
	}	
	
	private String insertDeclaredYieldFieldCommodityBerries(DopYieldFieldCommodityBerries dopYieldFieldCommodityBerries, String userId) throws DaoException {

		DeclaredYieldFieldCommodityBerriesDto dto = new DeclaredYieldFieldCommodityBerriesDto();
		dopYieldContractRsrcFactory.updateDto(dto, dopYieldFieldCommodityBerries);

		dto.setDeclaredYieldFieldCommodityBerriesGuid(null);

		declaredYieldFieldCommodityBerriesDao.insert(dto, userId);

		return dto.getDeclaredYieldFieldCommodityBerriesGuid();
	}

	public void calculateDeclaredYieldFieldVarietyBerriesList(List<DopYieldFieldVarietyBerries> dopYieldFieldVarietyBerriesList) {

		logger.debug("<calculateDeclaredYieldFieldVarietyBerriesList");

		if (dopYieldFieldVarietyBerriesList != null && !dopYieldFieldVarietyBerriesList.isEmpty()) {
			for (DopYieldFieldVarietyBerries dyfvb : dopYieldFieldVarietyBerriesList) {
				calculateDeclaredYieldFieldVarietyBerries(dyfvb);
			}
		}

		logger.debug(">calculateDeclaredYieldFieldVarietyBerriesList");
	}
	
	private void calculateDeclaredYieldFieldVarietyBerries(DopYieldFieldVarietyBerries dopYieldFieldVarietyBerries) {

		logger.debug("<calculateDeclaredYieldFieldVarietyBerries");

		Double totalProduction = null;

		totalProduction = safeAdd(totalProduction, dopYieldFieldVarietyBerries.getAbandonmentYield());
		totalProduction = safeAdd(totalProduction, dopYieldFieldVarietyBerries.getSalesYield());
		totalProduction = safeAdd(totalProduction, dopYieldFieldVarietyBerries.getSoldShippedYield());
		
		dopYieldFieldVarietyBerries.setTotalProduction(totalProduction);
		
		logger.debug(">calculateDeclaredYieldFieldVarietyBerries");
	}	
	
	
	public void updateDeclaredYieldFieldVarietyBerriesList(String declaredYieldFieldCommodityBerriesGuid, List<DopYieldFieldVarietyBerries> dopYieldFieldVarietyBerriesList, String userId) throws DaoException {

		logger.debug("<updateDeclaredYieldFieldVarietyBerriesList");

		if (dopYieldFieldVarietyBerriesList != null && !dopYieldFieldVarietyBerriesList.isEmpty()) {
			for (DopYieldFieldVarietyBerries dyfvb : dopYieldFieldVarietyBerriesList) {
				updateDeclaredYieldFieldVarietyBerries(declaredYieldFieldCommodityBerriesGuid, dyfvb, userId);
			}
		}

		logger.debug(">updateDeclaredYieldFieldVarietyBerriesList");
	
	}
	

	public void loadInventoryBerries(InventoryFieldDto ifDto) throws DaoException {

		List<InventoryBerriesDto> inventoryBerries = inventoryBerriesDao.selectForDeclaredYield(ifDto.getInventoryFieldGuid());
		if (inventoryBerries.size() > 0) {
			ifDto.setInventoryBerries(inventoryBerries.get(0));
		}
	}

	public void loadDeclaredYieldFieldCommodityBerries(ContractedFieldDetailDto cfdDto) throws DaoException {
		List<DeclaredYieldFieldCommodityBerriesDto> dyfcbDtoList = declaredYieldFieldCommodityBerriesDao.select(cfdDto.getFieldId(), cfdDto.getCropYear());
		cfdDto.setDeclaredYieldFieldCommodityBerriesList(dyfcbDtoList);

		for ( DeclaredYieldFieldCommodityBerriesDto dyfcbDto : dyfcbDtoList ) {
			loadDeclaredYieldFieldVarietyBerries(dyfcbDto);
		}	
	}

	private void loadDeclaredYieldFieldVarietyBerries(DeclaredYieldFieldCommodityBerriesDto dyfcbDto) throws DaoException {
		List<DeclaredYieldFieldVarietyBerriesDto> dyfvbDtoList = declaredYieldFieldVarietyBerriesDao.select(dyfcbDto.getDeclaredYieldFieldCommodityBerriesGuid());
		dyfcbDto.setDeclaredYieldFieldVarietyBerriesList(dyfvbDtoList);
	}

	
	private String updateDeclaredYieldFieldVarietyBerries(String declaredYieldFieldCommodityBerriesGuid, DopYieldFieldVarietyBerries dopYieldFieldVarietyBerries, String userId) throws DaoException {

		logger.debug("<updateDeclaredYieldFieldVarietyBerries");

		DeclaredYieldFieldVarietyBerriesDto dto = null;

		if (dopYieldFieldVarietyBerries.getDeclaredYieldFieldVarietyBerriesGuid() != null) {
			dto = declaredYieldFieldVarietyBerriesDao.fetch(dopYieldFieldVarietyBerries.getDeclaredYieldFieldVarietyBerriesGuid());
		}

		String declaredYieldFieldVarietyBerriesGuid = null;

		if (dto == null) {
			// Insert if it doesn't exist
			declaredYieldFieldVarietyBerriesGuid = insertDeclaredYieldFieldVarietyBerries(declaredYieldFieldCommodityBerriesGuid, dopYieldFieldVarietyBerries, userId);
		} else {
			declaredYieldFieldVarietyBerriesGuid = dto.getDeclaredYieldFieldVarietyBerriesGuid();
			dopYieldContractRsrcFactory.updateDto(dto, dopYieldFieldVarietyBerries);

			declaredYieldFieldVarietyBerriesDao.update(dto, userId);
		}
		
		logger.debug(">updateDeclaredYieldFieldVarietyBerries");

		return declaredYieldFieldVarietyBerriesGuid;
	}	
	
	private String insertDeclaredYieldFieldVarietyBerries(String declaredYieldFieldCommodityBerriesGuid, DopYieldFieldVarietyBerries dopYieldFieldVarietyBerries, String userId) throws DaoException {

		DeclaredYieldFieldVarietyBerriesDto dto = new DeclaredYieldFieldVarietyBerriesDto();
		dopYieldContractRsrcFactory.updateDto(dto, dopYieldFieldVarietyBerries);

		dto.setDeclaredYieldFieldVarietyBerriesGuid(null);
		dto.setDeclaredYieldFieldCommodityBerriesGuid(declaredYieldFieldCommodityBerriesGuid);
		
		declaredYieldFieldVarietyBerriesDao.insert(dto, userId);

		return dto.getDeclaredYieldFieldVarietyBerriesGuid();
	}
	
	
	private Integer notNull(Integer value, Integer defaultValue) {
		return (value == null) ? defaultValue : value;
	}

	private Double notNull(Double value, Double defaultValue) {
		return (value == null) ? defaultValue : value;
	}

	// Add op1 and op2, handling null specially.
	private Double safeAdd(Double op1, Double op2) {
		if ( op1 == null ) {
			return op2;
		} else if ( op2 == null ) {
			return op1;
		} else {
			return op1 + op2;			
		}
	}
	
}
