package ca.bc.gov.mal.cirras.underwriting.service.api.v1.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.BooleanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.mal.cirras.underwriting.model.v1.AnnualField;
import ca.bc.gov.mal.cirras.underwriting.model.v1.InventoryField;
import ca.bc.gov.mal.cirras.underwriting.model.v1.InventorySeededForage;
import ca.bc.gov.mal.cirras.underwriting.model.v1.InventoryUnseeded;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.InventoryCoverageTotalForageDao;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.InventoryCoverageTotalForageDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.InventoryUnseededDto;
import ca.bc.gov.mal.cirras.underwriting.service.api.v1.util.InventoryServiceEnums.InventoryCalculationType;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.service.api.ServiceException;

public class UnderwritingServiceHelper {

	private static final Logger logger = LoggerFactory.getLogger(UnderwritingServiceHelper.class);

	private InventoryCoverageTotalForageDao inventoryCoverageTotalForageDao;
	
	public void setInventoryCoverageTotalForageDao(InventoryCoverageTotalForageDao inventoryCoverageTotalForageDao) {
		this.inventoryCoverageTotalForageDao = inventoryCoverageTotalForageDao;
	}

	public void updateInventoryCoverageTotalForages(List<AnnualField> fields, String inventoryContractGuid, String userId, InventoryCalculationType calcType) throws DaoException {

		Map<Integer, InventoryCoverageTotalForageDto> cropTotalMap = new HashMap<Integer, InventoryCoverageTotalForageDto>();
		Map<String, InventoryCoverageTotalForageDto> plantInsTotalMap = new HashMap<String, InventoryCoverageTotalForageDto>();
		Map<Integer, InventoryCoverageTotalForageDto> unseededTotalMap = new HashMap<Integer, InventoryCoverageTotalForageDto>();

		populateInventoryCoverageTotalForageMaps(inventoryContractGuid, cropTotalMap, plantInsTotalMap, unseededTotalMap, calcType);

		calculateInventoryCoverageTotalForageDtos(fields, inventoryContractGuid, cropTotalMap, plantInsTotalMap, unseededTotalMap, calcType);

		List<InventoryCoverageTotalForageDto> totalDtos = new ArrayList<InventoryCoverageTotalForageDto>(cropTotalMap.values());
		totalDtos.addAll(plantInsTotalMap.values());
		totalDtos.addAll(unseededTotalMap.values());

		saveInventoryCoverageTotalForageDtos(totalDtos, userId);
	}
	
	private void populateInventoryCoverageTotalForageMaps(String inventoryContractGuid, 
			                                              Map<Integer, InventoryCoverageTotalForageDto> cropTotalMap, 
			                                              Map<String, InventoryCoverageTotalForageDto> plantInsTotalMap,
			                                              Map<Integer, InventoryCoverageTotalForageDto> unseededTotalMap,
			                                              InventoryCalculationType calcType) throws DaoException {

		List<InventoryCoverageTotalForageDto> ictfDtos = inventoryCoverageTotalForageDao.select(inventoryContractGuid);
				
		if ( ictfDtos != null ) {
			for ( InventoryCoverageTotalForageDto dto : ictfDtos ) {

				if ( calcType == InventoryCalculationType.Full ) {
					// Full re-calculation, so reset all totals first.
					dto.setTotalFieldAcres(null);
				}

				if ( (dto.getCropCommodityId() == null && dto.getPlantInsurabilityTypeCode() == null) || 
						(dto.getCropCommodityId() != null && dto.getPlantInsurabilityTypeCode() != null) ) {
					// Exactly one of cropCommodityId or plantInsurabilityTypeCode or isUnseededInsurableInd must be set.
					throw new ServiceException("InventoryCoverageTotalForageDto has invalid values for cropCommodityId " + dto.getCropCommodityId() + " and plantInsurabilityTypeCode " + dto.getPlantInsurabilityTypeCode());
				} else if ( Boolean.TRUE.equals(dto.getIsUnseededInsurableInd()) && dto.getCropCommodityId() != null ) {
					unseededTotalMap.put(dto.getCropCommodityId(), dto);
				} else if ( dto.getCropCommodityId() != null ) {
					cropTotalMap.put(dto.getCropCommodityId(), dto);
				} else if ( dto.getPlantInsurabilityTypeCode() != null ) {
					plantInsTotalMap.put(dto.getPlantInsurabilityTypeCode(), dto);
				} 
			}
		}		
		
	}

	private void calculateInventoryCoverageTotalForageDtos(List<AnnualField> fields, 
			                                               String inventoryContractGuid, 
			                                               Map<Integer, InventoryCoverageTotalForageDto> cropTotalMap, 
			                                               Map<String, InventoryCoverageTotalForageDto> plantInsTotalMap,
			                                               Map<Integer, InventoryCoverageTotalForageDto> unseededTotalMap,
			                                               InventoryCalculationType calcType) {

		if ( fields != null ) {
			for ( AnnualField field : fields ) {
				//Don't include field if it has been removed from the policy or deleted
				Boolean includeField = true;
				if(field.getLandUpdateType() != null && 
					(field.getLandUpdateType().equals(LandUpdateTypes.DELETE_FIELD) || 
					 field.getLandUpdateType().equals(LandUpdateTypes.REMOVE_FIELD_FROM_POLICY))){
					includeField = false;
				}
				if(includeField) {
					for ( InventoryField planting : field.getPlantings() ) {
						for ( InventorySeededForage isf : planting.getInventorySeededForages() ) {
							
							if ( !Boolean.TRUE.equals(isf.getDeletedByUserInd()) && isf.getFieldAcres() != null && isf.getFieldAcres() > 0.0 ) {
		
								if ( isf.getCropCommodityId() != null && Boolean.TRUE.equals(isf.getIsQuantityInsurableInd())) {
									InventoryCoverageTotalForageDto totalDto = cropTotalMap.get(isf.getCropCommodityId());
									if ( totalDto == null ) {
										totalDto = createInventoryCoverageTotalForageDto(inventoryContractGuid, isf.getCropCommodityId(), null, false);
										cropTotalMap.put(totalDto.getCropCommodityId(), totalDto);
									}
		
									addToInventoryCoverageTotalForageDto(totalDto, isf.getFieldAcres(), calcType);
								}
								
								if ( isf.getPlantInsurabilityTypeCode() != null ) {
									InventoryCoverageTotalForageDto totalDto = plantInsTotalMap.get(isf.getPlantInsurabilityTypeCode());
									if ( totalDto == null ) {
										totalDto = createInventoryCoverageTotalForageDto(inventoryContractGuid, null, isf.getPlantInsurabilityTypeCode(), false);
										plantInsTotalMap.put(totalDto.getPlantInsurabilityTypeCode(), totalDto);
									}
									
									addToInventoryCoverageTotalForageDto(totalDto, isf.getFieldAcres(), calcType);
								}
								
							}
						}
						
						// calculate unseeded insurable acres
						if ( planting.getInventoryUnseeded() != null) {
							InventoryUnseeded iu = planting.getInventoryUnseeded();
							//Add if unseeded insurable is checked and acres to be seeded greater 0
							if(!Boolean.TRUE.equals(iu.getDeletedByUserInd()) &&
							 iu.getIsUnseededInsurableInd() != null && 
							 Boolean.TRUE.equals(iu.getIsUnseededInsurableInd()) &&
							 iu.getAcresToBeSeeded() != null && iu.getAcresToBeSeeded() > 0.0) {
							
								InventoryCoverageTotalForageDto totalDto = unseededTotalMap.get(iu.getCropCommodityId());
								if ( totalDto == null ) {
									totalDto = createInventoryCoverageTotalForageDto(inventoryContractGuid, iu.getCropCommodityId(), null, iu.getIsUnseededInsurableInd());
									unseededTotalMap.put(totalDto.getCropCommodityId(), totalDto);
								}
		
								addToInventoryCoverageTotalForageDto(totalDto, iu.getAcresToBeSeeded(), calcType);
							}
						}
					}
				}
			}
		}
	}
	
	private InventoryCoverageTotalForageDto createInventoryCoverageTotalForageDto(String inventoryContractGuid, Integer cropCommodityId, String plantInsurabilityTypeCode, Boolean isUnseededInsurableInd) {

		InventoryCoverageTotalForageDto newDto = new InventoryCoverageTotalForageDto();
		newDto.setCropCommodityId(cropCommodityId);
		newDto.setInventoryContractGuid(inventoryContractGuid);
		newDto.setInventoryCoverageTotalForageGuid(null);
		newDto.setPlantInsurabilityTypeCode(plantInsurabilityTypeCode);
		newDto.setIsUnseededInsurableInd(isUnseededInsurableInd);
		newDto.setTotalFieldAcres(null);

		return newDto;
	}

	private void addToInventoryCoverageTotalForageDto(InventoryCoverageTotalForageDto totalDto, Double fieldAcres, InventoryCalculationType calcType) {

		Double currTotalAcres = totalDto.getTotalFieldAcres();
		if ( currTotalAcres == null ) {
			currTotalAcres = 0.0;
		}
		
		Double newTotalAcres = null;
		if ( calcType == InventoryCalculationType.IncrementalSubtract ) {
			newTotalAcres = currTotalAcres - fieldAcres;
			
			if ( newTotalAcres <= 0.0 ) {
				newTotalAcres = null;
			}
		} else {
			newTotalAcres = currTotalAcres + fieldAcres;
		}
		
		totalDto.setTotalFieldAcres(newTotalAcres);							
	}

	private void saveInventoryCoverageTotalForageDtos(List<InventoryCoverageTotalForageDto> totalDtos, String userId) throws DaoException {

		for ( InventoryCoverageTotalForageDto totalDto : totalDtos ) {
			if ( totalDto.getTotalFieldAcres() == null ) {
				if ( totalDto.getInventoryCoverageTotalForageGuid() != null ) {
					inventoryCoverageTotalForageDao.delete(totalDto.getInventoryCoverageTotalForageGuid());
				}
			} else if ( totalDto.getInventoryCoverageTotalForageGuid() == null ) {
				inventoryCoverageTotalForageDao.insert(totalDto, userId); 			
			} else {
				inventoryCoverageTotalForageDao.update(totalDto, userId);
			}
		}
	}
}
