package ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.factory;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.ws.rs.core.UriBuilder;

import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.rest.endpoints.resource.factory.BaseResourceFactory;
import ca.bc.gov.nrs.wfone.common.service.api.model.factory.FactoryException;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints.CropCommodityListEndpoint;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.CropCommodityListRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.CropCommodityRsrc;
import ca.bc.gov.mal.cirras.underwriting.model.v1.CropCommodity;
import ca.bc.gov.mal.cirras.underwriting.model.v1.CropCommodityList;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.CommodityTypeCodeDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.CropCommodityDto;
import ca.bc.gov.mal.cirras.underwriting.model.v1.CropVariety;
import ca.bc.gov.mal.cirras.underwriting.model.v1.CropVarietyCommodityType;
import ca.bc.gov.mal.cirras.underwriting.model.v1.CropVarietyPlantInsurability;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.CropVarietyDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.CropVarietyInsPlantInsXrefDto;
import ca.bc.gov.mal.cirras.underwriting.service.api.v1.model.factory.CommodityFactory;

public class CommodityRsrcFactory extends BaseResourceFactory implements CommodityFactory { 
	
	//======================================================================================================================
	// Uw Contract List (Uw Contract Search)
	//======================================================================================================================

	@Override
	public CropCommodityList<? extends CropCommodity> getCropCommodityList(
			List<CropCommodityDto> dtos, 
			List<CropVarietyDto> cropVarietyDtos,
			List<CommodityTypeCodeDto> commodityTypeCodeDtos,
			List<CropVarietyInsPlantInsXrefDto> cropVarietyInsPlantInsXrefDtos
		)  throws FactoryException, DaoException {
		
		CropCommodityListRsrc result = null;
		
		List<CropCommodityRsrc> resources = new ArrayList<CropCommodityRsrc>();
		
		List<CropVariety> cropVarietyList = populateCropVarietyList(cropVarietyDtos, commodityTypeCodeDtos, cropVarietyInsPlantInsXrefDtos);

		for (CropCommodityDto dto : dtos) {
			CropCommodityRsrc resource = new CropCommodityRsrc();
						
			populateResource(resource, dto, cropVarietyList);

			resources.add(resource);
		}
		
		result = new CropCommodityListRsrc();
		result.setCollection(resources);
		
		return result;
	}

	
	private void populateResource(CropCommodityRsrc resource, CropCommodityDto dto, List<CropVariety> cropVarieties) {

		resource.setCropCommodityId(dto.getCropCommodityId());
		resource.setInsurancePlanId(dto.getInsurancePlanId());
		resource.setCommodityName(dto.getCommodityName());
		resource.setShortLabel(dto.getShortLabel());
		resource.setPlantDurationTypeCode(dto.getPlantDurationTypeCode());
		resource.setIsInventoryCropInd(dto.getIsInventoryCropInd());
		resource.setIsYieldCropInd(dto.getIsYieldCropInd());
		resource.setIsUnderwritingCropInd(dto.getIsUnderwritingCropInd());
		resource.setIsProductInsurableInd(dto.getIsProductInsurableInd());
		resource.setIsCropInsuranceEligibleInd(dto.getIsCropInsuranceEligibleInd());
		resource.setIsPlantInsuranceEligibleInd(dto.getIsPlantInsuranceEligibleInd());
		resource.setIsOtherInsuranceEligibleInd(dto.getIsOtherInsuranceEligibleInd());
		resource.setYieldMeasUnitTypeCode(dto.getYieldMeasUnitTypeCode());
		resource.setYieldDecimalPrecision(dto.getYieldDecimalPrecision());
		resource.setDataSyncTransDate(dto.getDataSyncTransDate());
		resource.setInventoryTypeCode(dto.getInventoryTypeCode());
		resource.setYieldTypeCode(dto.getYieldTypeCode());
		
		List<CropVariety> filteredVarieties = cropVarieties.stream()
				.filter(x -> x.getCropCommodityId().equals(resource.getCropCommodityId()))
				.collect(Collectors.toList());
		
		resource.setCropVariety(filteredVarieties);
	}
	
	
	private void populateCropVariety(CropVariety variety, CropVarietyDto dto, 
									List<CropVarietyCommodityType> cropVarietyCommodityTypeList,
									List<CropVarietyPlantInsurability> cropVarietyPlantInsurabilityList) {
	
		variety.setCropVarietyId(dto.getCropVarietyId());
		variety.setCropCommodityId(dto.getCropCommodityId());
		variety.setVarietyName(dto.getVarietyName());
		variety.setInsurancePlanId(dto.getInsurancePlanId());
		variety.setDataSyncTransDate(dto.getDataSyncTransDate());
		
		variety.setIsAwpEligibleInd(dto.getIsAwpEligibleInd());
		variety.setIsPlantInsurableInd(dto.getIsPlantInsurableInd());
		variety.setIsQuantityInsurableInd(dto.getIsQuantityInsurableInd());
		variety.setIsUnseededInsurableInd(dto.getIsUnseededInsurableInd());
		variety.setIsUnderseedingEligibleInd(dto.getIsUnderseedingEligibleInd());
		variety.setIsGrainUnseededDefaultInd(dto.getIsGrainUnseededDefaultInd());
		
		List<CropVarietyCommodityType> filteredCropVarietyCommodityTypes = cropVarietyCommodityTypeList.stream()
				.filter(x -> x.getCropVarietyId().equals(variety.getCropVarietyId()))
				.collect(Collectors.toList());
		
		variety.setCropVarietyCommodityTypes(filteredCropVarietyCommodityTypes);
		
		List<CropVarietyPlantInsurability> filteredCropVarietyPlantInsurability = cropVarietyPlantInsurabilityList.stream()
				.filter(x -> x.getCropVarietyId().equals(variety.getCropVarietyId()))
				.collect(Collectors.toList());
		
		variety.setCropVarietyPlantInsurabilities(filteredCropVarietyPlantInsurability);
	 
	}
	
	private List<CropVariety> populateCropVarietyList( List<CropVarietyDto> varietyDtos, 
														List<CommodityTypeCodeDto> commodityTypeCodeDtos, 
														List<CropVarietyInsPlantInsXrefDto> cropVarietyInsPlantInsXrefDtos) {
		
		List<CropVariety> cropVarietyList = new ArrayList <CropVariety>();
		
		List<CropVarietyCommodityType> cropVarietyCommodityTypeList = populateCropVarietyCommodityType(commodityTypeCodeDtos);
		
		List<CropVarietyPlantInsurability> cropVarietyPlantInsurabilityList = populateCropVarietyPlantInsurability(cropVarietyInsPlantInsXrefDtos);

		for ( CropVarietyDto varietyDto : varietyDtos ) {

			CropVariety variety = new CropVariety(); 
			
			populateCropVariety(variety, varietyDto, cropVarietyCommodityTypeList, cropVarietyPlantInsurabilityList);

			cropVarietyList.add(variety);
		}

		return cropVarietyList;
	}
	
	private List<CropVarietyCommodityType> populateCropVarietyCommodityType( List<CommodityTypeCodeDto> commodityTypeCodeDtos) {
		
		List<CropVarietyCommodityType> cropVarietyCommodityTypeList = new ArrayList <CropVarietyCommodityType>();
		
		for ( CommodityTypeCodeDto dto : commodityTypeCodeDtos ) {

			CropVarietyCommodityType cropVarietyCommodityType = new CropVarietyCommodityType(); 
			
			populateCropVarietyCommodityType(cropVarietyCommodityType, dto);

			cropVarietyCommodityTypeList.add(cropVarietyCommodityType);
		}
		
		return cropVarietyCommodityTypeList;
	}	
	
	private void populateCropVarietyCommodityType(CropVarietyCommodityType model, CommodityTypeCodeDto dto) {

		model.setCommodityTypeCode(dto.getCommodityTypeCode());
		model.setDescription(dto.getDescription());
		model.setCropVarietyId(dto.getCropVarietyId());
		model.setInsurancePlanId(dto.getInsurancePlanId());
		model.setFullCoverageDeadlineDate(dto.getFullCoverageDeadlineDate());
		model.setFinalCoverageDeadlineDate(dto.getFinalCoverageDeadlineDate());

	}
	
// Plant Insurability
	private static void populateCropVarietyPlantInsurability(CropVarietyPlantInsurability model, CropVarietyInsPlantInsXrefDto dto) {

		model.setCropVarietyId(dto.getCropVarietyId());
		model.setPlantInsurabilityTypeCode(dto.getPlantInsurabilityTypeCode());
		model.setDescription(dto.getDescription());
		model.setCropVarietyInsurabilityGuid(dto.getCropVarietyInsurabilityGuid());
		model.setIsUsedInd(dto.getIsUsedInd());
	}
	
	public static List<CropVarietyPlantInsurability> populateCropVarietyPlantInsurability( List<CropVarietyInsPlantInsXrefDto> cropVarietyInsPlantInsXrefDtos) {
		
		List<CropVarietyPlantInsurability> cropVarietyPlantInsurabilityList = new ArrayList <CropVarietyPlantInsurability>();
		
		for ( CropVarietyInsPlantInsXrefDto dto : cropVarietyInsPlantInsXrefDtos ) {

			CropVarietyPlantInsurability cropVarietyPlantInsurability = new CropVarietyPlantInsurability(); 
			
			populateCropVarietyPlantInsurability(cropVarietyPlantInsurability, dto);

			cropVarietyPlantInsurabilityList.add(cropVarietyPlantInsurability);
		}
		
		return cropVarietyPlantInsurabilityList;
	}	
	
	@Override
	public void updateDto(CropVarietyInsPlantInsXrefDto dto, CropVarietyPlantInsurability model) throws FactoryException {

		dto.setCropVarietyInsurabilityGuid(model.getCropVarietyInsurabilityGuid());
		dto.setPlantInsurabilityTypeCode(model.getPlantInsurabilityTypeCode());
		
	}

	
// end of plant insurability
	
	public static String getCropCommodityListSelfUri(URI baseUri) {

		String result = UriBuilder.fromUri(baseUri)
			.path(CropCommodityListEndpoint.class)
			.build()
			.toString();

		return result;
	}

}
