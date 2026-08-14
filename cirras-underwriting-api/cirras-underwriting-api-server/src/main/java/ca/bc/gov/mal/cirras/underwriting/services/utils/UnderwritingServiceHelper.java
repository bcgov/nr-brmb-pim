package ca.bc.gov.mal.cirras.underwriting.services.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.mal.cirras.underwriting.data.models.InventoryField;
import ca.bc.gov.mal.cirras.underwriting.data.models.InventorySeededForage;
import ca.bc.gov.mal.cirras.underwriting.data.models.InventoryUnseeded;
import ca.bc.gov.mal.cirras.underwriting.data.models.UnderwritingComment;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.InventoryCoverageTotalForageDao;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.UnderwritingCommentDao;
import ca.bc.gov.mal.cirras.underwriting.data.resources.AnnualFieldRsrc;
import ca.bc.gov.mal.cirras.underwriting.data.resources.DopYieldContractRsrc;
import ca.bc.gov.mal.cirras.underwriting.data.assemblers.InventoryContractRsrcFactory;
import ca.bc.gov.mal.cirras.underwriting.data.entities.ContractedFieldDetailDto;
import ca.bc.gov.mal.cirras.underwriting.data.entities.InventoryCoverageTotalForageDto;
import ca.bc.gov.mal.cirras.underwriting.data.entities.UnderwritingCommentDto;
import ca.bc.gov.mal.cirras.underwriting.data.entities.YieldMeasUnitConversionDto;
import ca.bc.gov.mal.cirras.underwriting.services.utils.InventoryServiceEnums.InventoryCalculationType;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;
import ca.bc.gov.nrs.wfone.common.service.api.ServiceException;
import ca.bc.gov.nrs.wfone.common.webade.authentication.WebAdeAuthentication;

public class UnderwritingServiceHelper {

	private static final Logger logger = LoggerFactory.getLogger(UnderwritingServiceHelper.class);

	private InventoryCoverageTotalForageDao inventoryCoverageTotalForageDao;
	private UnderwritingCommentDao underwritingCommentDao;
	
	private InventoryContractRsrcFactory inventoryContractRsrcFactory;

	public void setInventoryCoverageTotalForageDao(InventoryCoverageTotalForageDao inventoryCoverageTotalForageDao) {
		this.inventoryCoverageTotalForageDao = inventoryCoverageTotalForageDao;
	}

	public void setUnderwritingCommentDao(UnderwritingCommentDao underwritingCommentDao) {
		this.underwritingCommentDao = underwritingCommentDao;
	}

	public void setInventoryContractRsrcFactory(InventoryContractRsrcFactory inventoryContractRsrcFactory) {
		this.inventoryContractRsrcFactory = inventoryContractRsrcFactory;
	}

	public void updateInventoryCoverageTotalForages(List<AnnualFieldRsrc> fields, String inventoryContractGuid, String userId, InventoryCalculationType calcType) throws DaoException {

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
	
	public void loadUwComments(ContractedFieldDetailDto cfdDto) throws DaoException {
		//Returning all comments of a field
		List<UnderwritingCommentDto> uwComments = underwritingCommentDao.selectForField(cfdDto.getFieldId());
		cfdDto.setUwComments(uwComments);
	}
	
	public void updateUnderwritingComment(UnderwritingComment underwritingComment, Integer annualFieldDetailId,
			Integer growerContractYearId, String declaredYieldContractGuid, String userId,
			WebAdeAuthentication authentication) throws DaoException, ServiceException {

		UnderwritingCommentDto dto = null;

		if (underwritingComment.getUnderwritingCommentGuid() != null) {
			dto = underwritingCommentDao.fetch(underwritingComment.getUnderwritingCommentGuid());
		}

		if (dto == null) {
			// Insert if it doesn't exist
			insertUnderwritingComment(underwritingComment, annualFieldDetailId, growerContractYearId,
					declaredYieldContractGuid, userId);
		} else {

			if (!dto.getUnderwritingComment().equals(underwritingComment.getUnderwritingComment()) || !dto
					.getUnderwritingCommentTypeCode().equals(underwritingComment.getUnderwritingCommentTypeCode())) {

				// Check that user is authorized to edit this comment.
				// Note that this could return null if the current user or create user cannot be
				// determined.
				Boolean userCanEditComment = inventoryContractRsrcFactory.checkUserCanEditComment(dto, authentication);
				if (!Boolean.TRUE.equals(userCanEditComment)) {
					logger.error("User " + userId + " attempted to edit comment "
							+ underwritingComment.getUnderwritingCommentGuid() + " created by " + dto.getCreateUser());
					throw new ServiceException("The current user is not authorized to edit this comment.");
				}

			}

			inventoryContractRsrcFactory.updateDto(dto, underwritingComment);

			underwritingCommentDao.update(dto, userId);
		}

	}

	public String insertUnderwritingComment(UnderwritingComment underwritingComment, Integer annualFieldDetailId,
			Integer growerContractYearId, String declaredYieldContractGuid, String userId) throws DaoException {

		UnderwritingCommentDto dto = new UnderwritingCommentDto();
		inventoryContractRsrcFactory.updateDto(dto, underwritingComment);

		dto.setUnderwritingCommentGuid(null);
		dto.setAnnualFieldDetailId(annualFieldDetailId);
		dto.setGrowerContractYearId(growerContractYearId);
		dto.setDeclaredYieldContractGuid(declaredYieldContractGuid);

		underwritingCommentDao.insert(dto, userId);

		return dto.getUnderwritingCommentGuid();
	}

	public void deleteUnderwritingComment(UnderwritingComment underwritingComment, String userId,
			WebAdeAuthentication authentication) throws NotFoundDaoException, DaoException {
		logger.debug("<deleteUnderwritingComment");

		UnderwritingCommentDto dto = null;

		if (underwritingComment.getUnderwritingCommentGuid() != null) {
			dto = underwritingCommentDao.fetch(underwritingComment.getUnderwritingCommentGuid());
		}

		if (dto != null) {
			// Check that user is authorized to delete this comment.
			// Note that this could return false if the current user or create user cannot
			// be determined.
			Boolean userCanDeleteComment = inventoryContractRsrcFactory.checkUserCanDeleteComment(dto, authentication);
			if (!Boolean.TRUE.equals(userCanDeleteComment)) {
				logger.error("User " + userId + " attempted to delete comment " + dto.getUnderwritingCommentGuid()
						+ " created by " + dto.getCreateUser());
				throw new ServiceException("The current user is not authorized to delete this comment.");
			}

			underwritingCommentDao.delete(underwritingComment.getUnderwritingCommentGuid());
		}

		logger.debug(">deleteUnderwritingComment");
	}	
	
	private boolean multiplyUnitCoversion;

	/// converts a value (Grain = acres, Forage = weight) into the default units and returns the converted value
	public Double convertDopYieldFieldAcresWeight(Double valueToConvert, Integer cropCommodityId,
			DopYieldContractRsrc dopYieldContract, Map<String, YieldMeasUnitConversionDto> ymucMap)
			throws ServiceException {

		logger.debug("<convertDopYieldFieldAcresWeight");

		Double estYieldDefaultUnit = null;
		String srcUnit = dopYieldContract.getEnteredYieldMeasUnitTypeCode();
		String targetUnit = dopYieldContract.getDefaultYieldMeasUnitTypeCode();

		if (srcUnit.equals(targetUnit)) {
			estYieldDefaultUnit = valueToConvert;
		} else {
			YieldMeasUnitConversionDto ymucDto = lookupYieldMeasUnitConversion(ymucMap,
					cropCommodityId, srcUnit, targetUnit);

			if (ymucDto == null) {

				// Cannot calculate conversion.
				throw new ServiceException("No conversion is defined for commodity id "
						+ cropCommodityId + ", Src Unit " + srcUnit + ", Target Unit " + targetUnit
						+ ", for crop year " + dopYieldContract.getCropYear());
			} else {
				estYieldDefaultUnit = calculateYieldMeasUnitConversion(ymucDto,
						valueToConvert, multiplyUnitCoversion);
			}
		}
		
		logger.debug(">convertDopYieldFieldAcresWeight");

		return estYieldDefaultUnit;
	}
	
	public double convertEstimatedYield(DopYieldContractRsrc dopYieldContract, String targetUnit,
			Integer cropCommodityId, double valueToConvert, Map<String, YieldMeasUnitConversionDto> ymucMap) {

		String enteredUnit = dopYieldContract.getEnteredYieldMeasUnitTypeCode();

		if (enteredUnit.equals(targetUnit)) {
			// No need to convert
			return valueToConvert;
		} else {
			// At this point the entered unit is not equal to the unit to convert to

			YieldMeasUnitConversionDto ymucDto = lookupYieldMeasUnitConversion(ymucMap, cropCommodityId, enteredUnit,
					targetUnit);

			if (ymucDto == null) {

				// Cannot calculate conversion.
				throw new ServiceException(
						"No conversion is defined for commodity id " + cropCommodityId + ", Src Unit " + enteredUnit
								+ ", Target Unit " + targetUnit + ", for crop year " + dopYieldContract.getCropYear());
			} else {
				return calculateYieldMeasUnitConversion(ymucDto, valueToConvert, multiplyUnitCoversion);
			}
		}
	}
	

	// Lookup function for the map returned by loadYieldMeasUnitConversionsMap
	private YieldMeasUnitConversionDto lookupYieldMeasUnitConversion(Map<String, YieldMeasUnitConversionDto> ymucMap,
			Integer cropCommodityId, String srcUnit, String targetUnit) {
		multiplyUnitCoversion = true;
		String lookupKey = cropCommodityId + "::" + srcUnit + "::" + targetUnit;
		YieldMeasUnitConversionDto ymucDto = ymucMap.get(lookupKey);
		if (ymucDto == null) {
			// If there is no such key in the list, try the other way around
			lookupKey = cropCommodityId + "::" + targetUnit + "::" + srcUnit;
			ymucDto = ymucMap.get(lookupKey);
			multiplyUnitCoversion = false;
		}

		return ymucDto;
	}
	
	// Calculate srcValue converted to the target yield meas unit specified by
	// ymucDto.
	// If srcValue is null, targetValue is null.
	// Otherwise the calculated targetValue is returned.
	private Double calculateYieldMeasUnitConversion(YieldMeasUnitConversionDto ymucDto, Double srcValue,
			boolean multiply) {

		// Calculate value in target units.
		Double targetValue = null;

		if (srcValue != null) {
			if (multiply) {
				targetValue = srcValue * ymucDto.getConversionFactor();
			} else {
				targetValue = srcValue / ymucDto.getConversionFactor();
			}
		}

		return targetValue;
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

	private void calculateInventoryCoverageTotalForageDtos(List<AnnualFieldRsrc> fields, 
			                                               String inventoryContractGuid, 
			                                               Map<Integer, InventoryCoverageTotalForageDto> cropTotalMap, 
			                                               Map<String, InventoryCoverageTotalForageDto> plantInsTotalMap,
			                                               Map<Integer, InventoryCoverageTotalForageDto> unseededTotalMap,
			                                               InventoryCalculationType calcType) {

		if ( fields != null ) {
			for ( AnnualFieldRsrc field : fields ) {
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
