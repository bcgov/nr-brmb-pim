package ca.bc.gov.mal.cirras.underwriting.services.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.mal.cirras.underwriting.data.models.InventoryContractCommodity;
import ca.bc.gov.mal.cirras.underwriting.data.models.InventoryField;
import ca.bc.gov.mal.cirras.underwriting.data.models.InventorySeededForage;
import ca.bc.gov.mal.cirras.underwriting.data.models.InventorySeededGrain;
import ca.bc.gov.mal.cirras.underwriting.data.models.InventoryUnseeded;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.InventoryContractCommodityDao;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.InventoryFieldDao;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.InventorySeededForageDao;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.InventorySeededGrainDao;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.InventoryUnseededDao;
import ca.bc.gov.mal.cirras.underwriting.data.resources.AnnualFieldRsrc;
import ca.bc.gov.mal.cirras.underwriting.data.resources.InventoryContractRsrc;
import ca.bc.gov.mal.cirras.underwriting.services.utils.InventoryServiceEnums.InventoryCalculationType;
import ca.bc.gov.mal.cirras.underwriting.data.entities.InventoryContractCommodityDto;
import ca.bc.gov.mal.cirras.underwriting.data.entities.InventoryFieldDto;
import ca.bc.gov.mal.cirras.underwriting.data.entities.InventorySeededForageDto;
import ca.bc.gov.mal.cirras.underwriting.data.entities.InventorySeededGrainDto;
import ca.bc.gov.mal.cirras.underwriting.data.entities.InventoryUnseededDto;
import ca.bc.gov.mal.cirras.underwriting.data.assemblers.InventoryContractRsrcFactory;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;
import ca.bc.gov.nrs.wfone.common.service.api.NotFoundException;
import ca.bc.gov.nrs.wfone.common.service.api.ServiceException;

public class GrainForageInventoryService {

	private static final Logger logger = LoggerFactory.getLogger(GrainForageInventoryService.class);

	private InventorySeededForageDao inventorySeededForageDao;
	private InventorySeededGrainDao inventorySeededGrainDao;
	private InventoryUnseededDao inventoryUnseededDao;
	private InventoryFieldDao inventoryFieldDao;
	private InventoryContractCommodityDao inventoryContractCommodityDao;

	private InventoryContractRsrcFactory inventoryContractRsrcFactory;

	private UnderwritingServiceHelper underwritingServiceHelper;

	
	public final String PRODUCT_STATUS_FINAL = "FINAL";
	
	public void setInventorySeededGrainDao(InventorySeededGrainDao inventorySeededGrainDao) {
		this.inventorySeededGrainDao = inventorySeededGrainDao;
	}

	public void setInventorySeededForageDao(InventorySeededForageDao inventorySeededForageDao) {
		this.inventorySeededForageDao = inventorySeededForageDao;
	}
	
	public void setInventoryUnseededDao(InventoryUnseededDao inventoryUnseededDao) {
		this.inventoryUnseededDao = inventoryUnseededDao;
	}

	public void setInventoryFieldDao(InventoryFieldDao inventoryFieldDao) {
		this.inventoryFieldDao = inventoryFieldDao;
	}

	public void setInventoryContractCommodityDao(InventoryContractCommodityDao inventoryContractCommodityDao) {
		this.inventoryContractCommodityDao = inventoryContractCommodityDao;
	}

	public void setInventoryContractRsrcFactory(InventoryContractRsrcFactory inventoryContractRsrcFactory) {
		this.inventoryContractRsrcFactory = inventoryContractRsrcFactory;
	}
	
	public void setUnderwritingServiceHelper(UnderwritingServiceHelper underwritingServiceHelper) {
		this.underwritingServiceHelper = underwritingServiceHelper;
	}

	public void updateCalculatedAcres(InventoryContractCommodity inventoryContractCommodity,
			List<AnnualFieldRsrc> fields) {

		logger.debug("<updateCalculatedAcres");

		// Calculate total unseeded, seeded and spot-loss acres for a commodity
		Double totalUnseededAcres = (double) 0;
		Double totalSeededAcres = (double) 0;
		Double totalSpotLossAcres = (double) 0;

		for (AnnualFieldRsrc field : fields) {

			//Don't include field if it has been removed from the policy or deleted
			Boolean includeField = true;
			if(field.getLandUpdateType() != null && 
				(field.getLandUpdateType().equals(LandUpdateTypes.DELETE_FIELD) || 
				 field.getLandUpdateType().equals(LandUpdateTypes.REMOVE_FIELD_FROM_POLICY))){
				includeField = false;
			}
			if(includeField) {
	
				Double seededAcres = (double) 0;
				Double spotLossAcres = (double) 0;
	
				Double unseededAcres = getUnseededAcres(inventoryContractCommodity, field);
				Map<TotalAcresType, Double> acres = getSeededFieldTotals(inventoryContractCommodity, field);
	
				seededAcres = acres.get(TotalAcresType.SEEDED_ACRES);
				spotLossAcres = acres.get(TotalAcresType.SPOT_LOSS_ACRES);
				
				if (unseededAcres != null && unseededAcres > 0) {
					totalUnseededAcres += unseededAcres;
				}
	
				if (seededAcres != null && seededAcres > 0) {
					totalSeededAcres += seededAcres;
				}
	
				if (spotLossAcres != null && spotLossAcres > 0) {
					totalSpotLossAcres += spotLossAcres;
				}
			}
		}

		logger.debug("Total unseeded acres for " + inventoryContractCommodity.getCropCommodityName() + ": " + totalUnseededAcres);

		if (Double.compare(notNull(inventoryContractCommodity.getTotalUnseededAcres(), (double)-1), totalUnseededAcres) != 0) {
			inventoryContractCommodity.setTotalUnseededAcres(totalUnseededAcres);
		}
		if (Double.compare(notNull(inventoryContractCommodity.getTotalSeededAcres(), (double)-1), totalSeededAcres) != 0) {
			inventoryContractCommodity.setTotalSeededAcres(totalSeededAcres);
		}
		if (Double.compare(notNull(inventoryContractCommodity.getTotalSpotLossAcres(), (double)-1), totalSpotLossAcres) != 0) {
			inventoryContractCommodity.setTotalSpotLossAcres(totalSpotLossAcres);
		}

		logger.debug(">updateCalculatedAcres");

	}
	
	private Double getUnseededAcres(InventoryContractCommodity inventoryContractCommodity, AnnualFieldRsrc field) {

		logger.debug("<getUnseededAcres");

		Double unseededAcres = (double) 0;
		
		//Unseeded acres are always when pedigree = false
		if(inventoryContractCommodity.getIsPedigreeInd() == false) {
			// Get sum of acres of commodities that have a acres to be seeded value and are
			// not deleted
			// It's possible that commodities are not specified
			// Only commodities that are crop insurance eligible AND inventory crops are stored individually
			// all other commodities are saved as OTHER
			if (inventoryContractCommodity.getCropCommodityId() == null) {
				unseededAcres = field.getPlantings().stream()
						.filter(x -> (x.getInventoryUnseeded().getCropCommodityId() == null
										|| (x.getInventoryUnseeded().getCropCommodityId() != null
										&& x.getInventoryUnseeded().getCropVarietyId() == null //Only Grain commodities are in unseeded totals
										&& (Boolean.FALSE.equals(x.getInventoryUnseeded().getIsCropInsuranceEligibleInd())
											|| Boolean.FALSE.equals(x.getInventoryUnseeded().getIsInventoryCropInd()))))
								&& x.getInventoryUnseeded().getAcresToBeSeeded() != null
								&& (x.getInventoryUnseeded().getDeletedByUserInd() == null
										|| x.getInventoryUnseeded().getDeletedByUserInd() == false))
						.mapToDouble(x -> x.getInventoryUnseeded().getAcresToBeSeeded()).sum();

			} else {
				unseededAcres = field.getPlantings().stream()
						.filter(x -> x.getInventoryUnseeded().getCropCommodityId() != null 
								&& x.getInventoryUnseeded().getCropCommodityId().equals(inventoryContractCommodity.getCropCommodityId())
								&& x.getInventoryUnseeded().getCropVarietyId() == null //Only Grain commodities are in unseeded totals
								&& Boolean.TRUE.equals(x.getInventoryUnseeded().getIsCropInsuranceEligibleInd())
								&& Boolean.TRUE.equals(x.getInventoryUnseeded().getIsInventoryCropInd())
								&& x.getInventoryUnseeded().getAcresToBeSeeded() != null
								&& (x.getInventoryUnseeded().getDeletedByUserInd() == null
										|| x.getInventoryUnseeded().getDeletedByUserInd() == false))
						.mapToDouble(x -> x.getInventoryUnseeded().getAcresToBeSeeded()).sum();
			}
		}

		logger.debug(">getUnseededAcres " + unseededAcres);

		return unseededAcres;
	}
	
	private enum TotalAcresType {
		SEEDED_ACRES,
		SPOT_LOSS_ACRES
	}

	private Map<TotalAcresType, Double> getSeededFieldTotals(InventoryContractCommodity inventoryContractCommodity, AnnualFieldRsrc field) {

		logger.debug("<getSeededFieldTotals");
		
		Map<TotalAcresType, Double> acres = new HashMap<>();
		Double seededAcres = 0.0; 
		Double spotLossAcres = 0.0;

		// Get sum of acres of commodities that have a seeded acres value and are
		// not deleted
		// It's possible that commodities are not specified

		for (InventoryField planting : field.getPlantings()) {

			Double plantingSeededAcres = (double) 0;
			Double plantingSpotLossAcres = (double) 0;
			
			// Get Seeded Acres
			if (inventoryContractCommodity.getCropCommodityId() == null) {
				plantingSeededAcres = planting.getInventorySeededGrains().stream()
						.filter(x -> x.getCropCommodityId() == null && x.getSeededAcres() != null
								&& (x.getDeletedByUserInd() == null || x.getDeletedByUserInd() == false)
								&& x.getIsQuantityInsurableInd() == true
								&& x.getIsPedigreeInd().equals(inventoryContractCommodity.getIsPedigreeInd()))
						.mapToDouble(x -> x.getSeededAcres()).sum();

			} else {
				plantingSeededAcres = planting.getInventorySeededGrains().stream()
						.filter(x -> (x.getCropCommodityId() != null
								&& x.getCropCommodityId().equals(inventoryContractCommodity.getCropCommodityId()))
								&& x.getSeededAcres() != null
								&& (x.getDeletedByUserInd() == null || x.getDeletedByUserInd() == false)
								&& x.getIsQuantityInsurableInd() == true
								&& x.getIsPedigreeInd().equals(inventoryContractCommodity.getIsPedigreeInd()))
						.mapToDouble(x -> x.getSeededAcres()).sum();
			}

			if (plantingSeededAcres != null && plantingSeededAcres > 0) {
				seededAcres += plantingSeededAcres;
			}

			// Get Spot Loss Acres
			if (inventoryContractCommodity.getCropCommodityId() == null) {
				plantingSpotLossAcres = planting.getInventorySeededGrains().stream()
						.filter(x -> x.getCropCommodityId() == null && x.getSeededAcres() != null
								&& (x.getDeletedByUserInd() == null || x.getDeletedByUserInd() == false)
								&& x.getIsSpotLossInsurableInd() == true
								&& x.getIsPedigreeInd().equals(inventoryContractCommodity.getIsPedigreeInd()))
						.mapToDouble(x -> x.getSeededAcres()).sum();

			} else {
				plantingSpotLossAcres = planting.getInventorySeededGrains().stream()
						.filter(x -> (x.getCropCommodityId() != null
								&& x.getCropCommodityId().equals(inventoryContractCommodity.getCropCommodityId()))
								&& x.getSeededAcres() != null
								&& (x.getDeletedByUserInd() == null || x.getDeletedByUserInd() == false)
								&& x.getIsSpotLossInsurableInd() == true
								&& x.getIsPedigreeInd().equals(inventoryContractCommodity.getIsPedigreeInd()))
						.mapToDouble(x -> x.getSeededAcres()).sum();
			}

			if (plantingSpotLossAcres != null && plantingSpotLossAcres > 0) {
				spotLossAcres += plantingSpotLossAcres;
			}

		}

		acres.put(TotalAcresType.SEEDED_ACRES, seededAcres);
		acres.put(TotalAcresType.SPOT_LOSS_ACRES, spotLossAcres);

		logger.debug(">getSeededFieldTotals");

		return acres;
	}

	public void updateInventoryUnseeded(InventoryUnseeded inventoryUnseeded, String inventoryFieldGuid, String userId)
			throws DaoException {

		// inventoryUnseeded.getInventoryUnseededGuid() might be null if it's a new crop
		InventoryUnseededDto dto = null;
		if (inventoryUnseeded.getInventoryUnseededGuid() != null) {
			dto = inventoryUnseededDao.fetch(inventoryUnseeded.getInventoryUnseededGuid());
		}

		if (dto == null) {
			// Insert if it doesn't exist
			insertInventoryUnseeded(inventoryUnseeded, inventoryFieldGuid, userId);
		} else {

			inventoryContractRsrcFactory.updateDto(dto, inventoryUnseeded);

			inventoryUnseededDao.update(dto, userId);
		}
	}

	public void updateInventorySeededGrain(InventorySeededGrain inventorySeededGrain, String inventoryFieldGuid,
			String userId) throws DaoException {

		InventorySeededGrainDto dto = null;

		if (inventorySeededGrain.getInventorySeededGrainGuid() != null) {
			dto = inventorySeededGrainDao.fetchSimple(inventorySeededGrain.getInventorySeededGrainGuid());
		}

		if (dto == null) {
			// Insert if it doesn't exist
			insertInventorySeededGrain(inventorySeededGrain, inventoryFieldGuid, userId);
		} else {

			inventoryContractRsrcFactory.updateDto(dto, inventorySeededGrain);

			inventorySeededGrainDao.update(dto, userId);
		}

	}
	
	public void updateInventorySeededForage(InventorySeededForage inventorySeededForage, String inventoryFieldGuid,
			String userId) throws DaoException, ServiceException {

		InventorySeededForageDto dto = null;

		if (inventorySeededForage.getInventorySeededForageGuid() != null) {
			dto = inventorySeededForageDao.fetchSimple(inventorySeededForage.getInventorySeededForageGuid());
		}

		if (dto == null) {
			// Insert if it doesn't exist
			inventorySeededForage.setInventorySeededForageGuid(insertInventorySeededForage(inventorySeededForage, inventoryFieldGuid, userId));
			
		} else {

			inventoryContractRsrcFactory.updateDto(dto, inventorySeededForage);

			inventorySeededForageDao.update(dto, userId);
		}
		
		linkUnlinkPlantings(inventorySeededForage, userId);

	}
	
	public void updateInventoryContractCommodities(InventoryContractRsrc inventoryContract,
			String inventoryContractGuid, String userId) throws DaoException, NotFoundException {

		logger.debug("<updateInventoryContractCommodities");

		List<InventoryContractCommodity> commodities = inventoryContract.getCommodities();

		// Get commodities from database
		List<InventoryContractCommodityDto> dtoCommoditiesTemp = inventoryContractCommodityDao.select(inventoryContractGuid);

		// In order to modify this list for processing below, create a copy. MyBatis
		// uses the same reference it returned above in
		// its cache, so modifications to dtoCommoditiesTemp would modify the cache and
		// can cause it to return incorrect results later
		// in this transaction.
		List<InventoryContractCommodityDto> dtoCommodities = null;
		if (dtoCommoditiesTemp != null) {
			dtoCommodities = new ArrayList<InventoryContractCommodityDto>(dtoCommoditiesTemp);
		}

		if (commodities != null && !commodities.isEmpty()) {

			for (InventoryContractCommodity commodity : commodities) {

				logger.debug("Commodity: " + commodity.getCropCommodityName());

				// Check if there is an existing record
				List<InventoryContractCommodityDto> filteredCommodityDto = null;
				if (dtoCommodities != null && !dtoCommodities.isEmpty()) {

					// It's possible that commodities are not specified
					if (commodity.getCropCommodityId() == null) {
						filteredCommodityDto = dtoCommodities.stream().filter(x -> x.getCropCommodityId() == null
								&& x.getIsPedigreeInd().equals(commodity.getIsPedigreeInd()))
								.collect(Collectors.toList());
					} else {
						filteredCommodityDto = dtoCommodities.stream()
								.filter(x -> x.getCropCommodityId() != null
										&& x.getCropCommodityId().equals(commodity.getCropCommodityId())
										&& x.getIsPedigreeInd().equals(commodity.getIsPedigreeInd()))
								.collect(Collectors.toList());
					}
				}

				if (filteredCommodityDto == null || filteredCommodityDto.isEmpty()) {
					// Insert new record
					logger.debug("Contract Commodity Insert: " + commodity.getCropCommodityName());
					insertInventoryContractCommodity(commodity, inventoryContract.getFields(), inventoryContractGuid,
							userId);
				} else {
					logger.debug("Contract Commodity Update: " + commodity.getCropCommodityName());
					// Update existing record
					InventoryContractCommodityDto commodityDto = filteredCommodityDto.get(0);

					updateInventoryContractCommodity(commodity, commodityDto, inventoryContract.getFields(), userId);

					// Remove dto of that commodity from dto list to know which ones need to be
					// deleted
					dtoCommodities.remove(filteredCommodityDto.get(0));
				}
			}

			// Delete commodities that don't exist anymore. Those are the only ones left in
			// the dto list
			if (dtoCommodities != null && !dtoCommodities.isEmpty()) {
				for (InventoryContractCommodityDto dto : dtoCommodities) {
					logger.debug("Contract Commodity Delete: " + dto.getCropCommodityName());
					inventoryContractCommodityDao.delete(dto.getInventoryContractCommodityGuid());
				}
			}

		} else if (dtoCommodities != null && !dtoCommodities.isEmpty()) {
			// In this case all commodities have been removed and need to be removed from
			// the database
			inventoryContractCommodityDao.deleteForInventoryContract(inventoryContractGuid);
		}

		logger.debug(">updateInventoryContractCommodities");

	}

	private void updateInventoryContractCommodity(InventoryContractCommodity inventoryContractCommodity,
			InventoryContractCommodityDto dto, List<AnnualFieldRsrc> fields, String userId)
			throws DaoException, NotFoundException {

		logger.debug("<updateInventoryContractCommodity");

		// Calculate calculated acres totals
		updateCalculatedAcres(inventoryContractCommodity, fields);

		inventoryContractRsrcFactory.updateDto(dto, inventoryContractCommodity);

		inventoryContractCommodityDao.update(dto, userId);

		logger.debug(">updateInventoryContractCommodity");
	}

	public String insertInventoryContractCommodity(InventoryContractCommodity inventoryContractCommodity,
			List<AnnualFieldRsrc> fields, String inventoryContractGuid, String userId) throws DaoException {

		logger.debug("<insertInventoryContractCommodity");

		// Calculate unseeded acres totals
		updateCalculatedAcres(inventoryContractCommodity, fields);

		InventoryContractCommodityDto dto = new InventoryContractCommodityDto();
		inventoryContractRsrcFactory.updateDto(dto, inventoryContractCommodity);

		dto.setInventoryContractCommodityGuid(null);
		dto.setInventoryContractGuid(inventoryContractGuid);

		inventoryContractCommodityDao.insert(dto, userId);

		logger.debug(">insertInventoryContractCommodity");

		return dto.getInventoryContractCommodityGuid();
	}
	
	public void updateInventoryCoverageTotalForages(InventoryContractRsrc invContract, String inventoryContractGuid, String userId) throws DaoException {
		if ( invContract.getInsurancePlanName().equals(InventoryServiceEnums.InsurancePlans.FORAGE.toString())) {
			underwritingServiceHelper.updateInventoryCoverageTotalForages(invContract.getFields(), inventoryContractGuid, userId, InventoryCalculationType.Full);
		}
	}

	public void linkUnlinkPlantings(InventorySeededForage inventorySeededForage, String userId) throws DaoException, ServiceException {
	
		//Adds or removes link if the link planting type is set
		if(inventorySeededForage.getLinkPlantingType() != null) {
			if (inventorySeededForage.getLinkPlantingType().equalsIgnoreCase(InventoryServiceEnums.LinkPlantingType.ADD_LINK.toString())) {
				if (inventorySeededForage.getGrainInventoryFieldGuid() != null) {
					//Link plantings if there is a grain field guid set
					addLink(inventorySeededForage, userId);
				} else {
					throw new ServiceException("No GrainInventoryFieldGuid set to link planting.");
				}
			} else if (inventorySeededForage.getLinkPlantingType().equalsIgnoreCase(InventoryServiceEnums.LinkPlantingType.REMOVE_LINK.toString())) {
				//Remove link
				removeLink(inventorySeededForage, userId);
			}
		} else if (Boolean.TRUE.equals(inventorySeededForage.getDeletedByUserInd())) {
			//Remove link if planting is deleted
			removeLink(inventorySeededForage, userId);
		}
	}


	private String insertInventoryUnseeded(InventoryUnseeded inventoryUnseeded, String inventoryFieldGuid,
			String userId) throws DaoException {

		InventoryUnseededDto dto = new InventoryUnseededDto();
		inventoryContractRsrcFactory.updateDto(dto, inventoryUnseeded);

		dto.setInventoryUnseededGuid(null);
		dto.setInventoryFieldGuid(inventoryFieldGuid);

		inventoryUnseededDao.insert(dto, userId);

		return dto.getInventoryUnseededGuid();
	}

	private String insertInventorySeededGrain(InventorySeededGrain inventorySeededGrain, String inventoryFieldGuid,
			String userId) throws DaoException {

		InventorySeededGrainDto dto = new InventorySeededGrainDto();
		inventoryContractRsrcFactory.updateDto(dto, inventorySeededGrain);

		dto.setInventorySeededGrainGuid(null);
		dto.setInventoryFieldGuid(inventoryFieldGuid);

		inventorySeededGrainDao.insert(dto, userId);

		return dto.getInventorySeededGrainGuid();
	}
	
	private String insertInventorySeededForage(InventorySeededForage inventorySeededForage, String inventoryFieldGuid,
			String userId) throws DaoException {

		InventorySeededForageDto dto = new InventorySeededForageDto();
		inventoryContractRsrcFactory.updateDto(dto, inventorySeededForage);

		dto.setInventorySeededForageGuid(null);
		dto.setInventoryFieldGuid(inventoryFieldGuid);

		inventorySeededForageDao.insert(dto, userId);
		
		return dto.getInventorySeededForageGuid();
	}
	

	public void deleteInventorySeededGrain(InventorySeededGrain inventorySeededGrain)
			throws NotFoundDaoException, DaoException {
		logger.debug("<deleteInventorySeededGrain");

		inventorySeededGrainDao.delete(inventorySeededGrain.getInventorySeededGrainGuid());

		logger.debug(">deleteInventorySeededGrain");
	}

	public void deleteInventorySeededForage(InventorySeededForage inventorySeededForage, String userId)
			throws NotFoundDaoException, DaoException {
		logger.debug("<deleteInventorySeededForage");
		
		linkUnlinkPlantings(inventorySeededForage, userId);
		inventorySeededForageDao.delete(inventorySeededForage.getInventorySeededForageGuid());

		logger.debug(">deleteInventorySeededForage");
	}

	public void deleteInventoryUnseeded(InventoryUnseeded inventoryUnseeded)
			throws NotFoundDaoException, DaoException {
		logger.debug("<deleteInventoryUnseeded");
		
		InventoryUnseededDto dto = null;
		if (inventoryUnseeded.getInventoryUnseededGuid() != null) {
			dto = inventoryUnseededDao.fetch(inventoryUnseeded.getInventoryUnseededGuid());
		}

		if (dto != null) {
			inventoryUnseededDao.delete(inventoryUnseeded.getInventoryUnseededGuid());
		}

		logger.debug(">deleteInventoryUnseeded");
	}	
	
	public boolean handleDeletedInventorySeededGrains(InventoryField inventoryField) {

		logger.debug("<handleDeletedInventorySeededGrains");
		
		boolean doDeleteInventoryField = false;
		InventoryUnseeded unseeded = inventoryField.getInventoryUnseeded();
		List<InventorySeededGrain> seededGrains = inventoryField.getInventorySeededGrains();
				
		if ( seededGrains != null) {
			boolean tryDelete = false;
			for (InventorySeededGrain seededGrain : seededGrains) {
				if (Boolean.TRUE.equals(seededGrain.getDeletedByUserInd())) { 
					tryDelete = true;
					break;
				}
			}

			if (tryDelete) { 
				// Check if there is any user-entered unseeded or seeded grain data. If not, then planting can be deleted.
				boolean canDelete = true;
				if (unseeded != null && !inventoryContractRsrcFactory.checkEmptyInventoryUnseeded(unseeded) && !Boolean.TRUE.equals(unseeded.getDeletedByUserInd()) ) {
					canDelete = false;
				} else {
					for (InventorySeededGrain seededGrain : seededGrains) {
						if (!inventoryContractRsrcFactory.checkEmptyInventorySeededGrain(seededGrain) && !Boolean.TRUE.equals(seededGrain.getDeletedByUserInd())) { 
							canDelete = false;
							break;
						}
					}
				}
			
				doDeleteInventoryField = canDelete;

			}
		}

		logger.debug(">handleDeletedInventorySeededGrains");
		
		return doDeleteInventoryField;
	}	

	public boolean handleDeletedInventorySeededForage(InventoryField inventoryField) {

		logger.debug("<handleDeletedInventorySeededForage");
		
		boolean doDeleteInventoryField = false;
		List<InventorySeededForage> seededForages = inventoryField.getInventorySeededForages();
				
		if ( seededForages != null) {
			boolean tryDelete = false;
			for (InventorySeededForage seededForage : seededForages) {
				if (Boolean.TRUE.equals(seededForage.getDeletedByUserInd())) { 
					tryDelete = true;
					break;
				}
			}

			if (tryDelete) { 
				// Check if there is any user-entered seeded forage data. If not, then planting can be deleted.
				boolean canDelete = true;
				for (InventorySeededForage seededForage : seededForages) {
					if (!checkEmptyInventorySeededForage(seededForage) && !Boolean.TRUE.equals(seededForage.getDeletedByUserInd())) { 
						canDelete = false;
						break;
					}
				}
			
				doDeleteInventoryField = canDelete;

			}
		}		
	
		logger.debug(">handleDeletedInventorySeededForage");

		return doDeleteInventoryField;
	}
	
	public boolean checkEmptyInventorySeededForage(InventorySeededForage inventorySeededForage) {
		
		return inventorySeededForage.getCropVarietyId() == null && 
				inventorySeededForage.getFieldAcres() == null && 
				inventorySeededForage.getSeedingYear() == null &&
				inventorySeededForage.getSeedingDate() == null;
	}

	public boolean checkEmptyInventorySeededForage(InventorySeededForageDto inventorySeededForage) {
		
		return inventorySeededForage.getCropVarietyId() == null && 
				inventorySeededForage.getFieldAcres() == null && 
				inventorySeededForage.getSeedingYear() == null &&
				inventorySeededForage.getSeedingDate() == null;
	}

	private void addLink(InventorySeededForage inventorySeededForage, String userId) throws DaoException, ServiceException {
		
		InventoryFieldDto ifDto = inventoryFieldDao.fetch(inventorySeededForage.getGrainInventoryFieldGuid());
		if(ifDto != null) {
			ifDto.setUnderseededInventorySeededForageGuid(inventorySeededForage.getInventorySeededForageGuid());
			inventoryFieldDao.update(ifDto, userId);
		}
		else {
			logger.info("AddLink, Grain planting to link to field not found. InventoryFieldGuid: " + inventorySeededForage.getGrainInventoryFieldGuid());
			throw new ServiceException("Grain planting to link to field not found.");
		}
		
	}

	public void removeLink(InventorySeededForage inventorySeededForage, String userId) throws DaoException {

		InventoryFieldDto ifDto = inventoryFieldDao.selectLinkedGrainPlanting(inventorySeededForage.getInventorySeededForageGuid());
		if(ifDto != null) {
			ifDto.setUnderseededInventorySeededForageGuid(null);
			inventoryFieldDao.update(ifDto, userId);
		}
		else {
			logger.info("RemoveLink, Planting to link to field not found. InventorySeededForageGuid: " + inventorySeededForage.getInventorySeededForageGuid());
		}
	}

	private Double notNull(Double value, Double defaultValue) {
		return (value == null) ? defaultValue : value;
	}
}
