package ca.bc.gov.mal.cirras.underwriting.services.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.mal.cirras.underwriting.data.models.InventoryBerries;
import ca.bc.gov.mal.cirras.underwriting.data.models.InventoryContractCommodityBerries;
import ca.bc.gov.mal.cirras.underwriting.data.models.InventoryField;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.InventoryBerriesDao;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.InventoryContractCommodityBerriesDao;
import ca.bc.gov.mal.cirras.underwriting.data.resources.AnnualFieldRsrc;
import ca.bc.gov.mal.cirras.underwriting.data.resources.InventoryContractRsrc;
import ca.bc.gov.mal.cirras.underwriting.data.entities.InventoryBerriesDto;
import ca.bc.gov.mal.cirras.underwriting.data.entities.InventoryContractCommodityBerriesDto;
import ca.bc.gov.mal.cirras.underwriting.data.assemblers.InventoryContractRsrcFactory;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;

public class BerriesService {

	private static final Logger logger = LoggerFactory.getLogger(BerriesService.class);

	private InventoryContractCommodityBerriesDao inventoryContractCommodityBerriesDao;
	private InventoryBerriesDao inventoryBerriesDao;

	private InventoryContractRsrcFactory inventoryContractRsrcFactory;
	
	public void setInventoryContractCommodityBerriesDao(InventoryContractCommodityBerriesDao inventoryContractCommodityBerriesDao) {
		this.inventoryContractCommodityBerriesDao = inventoryContractCommodityBerriesDao;
	}

	public void setInventoryBerriesDao(InventoryBerriesDao inventoryBerriesDao) {
		this.inventoryBerriesDao = inventoryBerriesDao;
	}

	public void setInventoryContractRsrcFactory(InventoryContractRsrcFactory inventoryContractRsrcFactory) {
		this.inventoryContractRsrcFactory = inventoryContractRsrcFactory;
	}
	
	public void updateInventoryBerries(InventoryBerries inventoryBerries, String inventoryFieldGuid, String userId)
			throws DaoException {

		// inventoryUnseeded.getInventoryUnseededGuid() might be null if it's a new crop
		InventoryBerriesDto dto = null;
		if (inventoryBerries.getInventoryBerriesGuid() != null) {
			dto = inventoryBerriesDao.fetch(inventoryBerries.getInventoryBerriesGuid());
		}
		
		//Calculates and sets total plants
		calculateTotalPlants(inventoryBerries);

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

	private Integer notNull(Integer value, Integer defaultValue) {
		return (value == null) ? defaultValue : value;
	}

	private Double notNull(Double value, Double defaultValue) {
		return (value == null) ? defaultValue : value;
	}
	
}
