package ca.bc.gov.mal.cirras.underwriting.data.assemblers;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import jakarta.ws.rs.core.UriBuilder;

import ca.bc.gov.nrs.common.wfone.rest.resource.RelLink;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.rest.endpoints.resource.factory.BaseResourceFactory;
import ca.bc.gov.nrs.wfone.common.service.api.model.factory.FactoryContext;
import ca.bc.gov.nrs.wfone.common.service.api.model.factory.FactoryException;
import ca.bc.gov.nrs.wfone.common.webade.authentication.WebAdeAuthentication;
import ca.bc.gov.mal.cirras.underwriting.controllers.CropVarietyInsurabilityListEndpoint;
import ca.bc.gov.mal.cirras.underwriting.controllers.scopes.Scopes;
import ca.bc.gov.mal.cirras.underwriting.data.resources.CropVarietyInsurabilityListRsrc;
import ca.bc.gov.mal.cirras.underwriting.data.resources.types.ResourceTypes;
import ca.bc.gov.mal.cirras.underwriting.data.models.CropVarietyInsurability;
import ca.bc.gov.mal.cirras.underwriting.data.models.CropVarietyInsurabilityList;
import ca.bc.gov.mal.cirras.underwriting.data.models.CropVarietyPlantInsurability;
import ca.bc.gov.mal.cirras.underwriting.data.entities.CropVarietyInsPlantInsXrefDto;
import ca.bc.gov.mal.cirras.underwriting.data.entities.CropVarietyInsurabilityDto;
import ca.bc.gov.mal.cirras.underwriting.services.model.factory.CropVarietyInsurabilityFactory;

public class CropVarietyInsurabilityRsrcFactory extends BaseResourceFactory implements CropVarietyInsurabilityFactory { 

	@Override
	public CropVarietyInsurabilityList<? extends CropVarietyInsurability> getCropVarietyInsurabilityList(
			List<CropVarietyInsurabilityDto> cropVarietyInsurabilityDtos,
			List<CropVarietyInsurabilityDto> validationDtos,
			List<CropVarietyInsPlantInsXrefDto> cropVarietyInsPlantInsXrefDtos,
			Integer insurancePlanId,
			Boolean loadForEdit,
			FactoryContext context, 
			WebAdeAuthentication authentication
	) throws FactoryException, DaoException {
		
		List<CropVarietyInsurability> resources = new ArrayList<CropVarietyInsurability>();
		
		List<CropVarietyPlantInsurability> cropVarietyPlantInsurabilityList = CommodityRsrcFactory.populateCropVarietyPlantInsurability(cropVarietyInsPlantInsXrefDtos);
		
		for (CropVarietyInsurabilityDto dto : cropVarietyInsurabilityDtos) {
			CropVarietyInsurability resource = new CropVarietyInsurability();
						
			populateResource(resource, dto, cropVarietyPlantInsurabilityList, validationDtos);

			resources.add(resource);
		}
		
		CropVarietyInsurabilityListRsrc result = new CropVarietyInsurabilityListRsrc();
		result.setCollection(resources);

		String eTag = getEtag(result);
		result.setETag(eTag);

		URI baseUri = getBaseURI(context);
		setSelfLink(insurancePlanId, loadForEdit, result, baseUri);
		setLinks(insurancePlanId, loadForEdit, result, baseUri, authentication);
		
		return result;
	}

	
	private void populateResource(
			CropVarietyInsurability model, 
			CropVarietyInsurabilityDto dto, 
			List<CropVarietyPlantInsurability> cropVarietyPlantInsurabilityList,
			List<CropVarietyInsurabilityDto> validationDtos
		) {

		model.setCropVarietyInsurabilityGuid(dto.getCropVarietyInsurabilityGuid());
		model.setCropVarietyId(dto.getCropVarietyId());
		model.setIsQuantityInsurableInd(dto.getIsQuantityInsurableInd());
		model.setIsUnseededInsurableInd(dto.getIsUnseededInsurableInd());
		model.setIsPlantInsurableInd(dto.getIsPlantInsurableInd());
		model.setIsAwpEligibleInd(dto.getIsAwpEligibleInd());
		model.setIsUnderseedingEligibleInd(dto.getIsUnderseedingEligibleInd());
		model.setIsGrainUnseededDefaultInd(dto.getIsGrainUnseededDefaultInd());
		model.setPlantDurationTypeCode(dto.getPlantDurationTypeCode());
		model.setVarietyName(dto.getVarietyName());
		
		List<CropVarietyPlantInsurability> filteredCropVarietyPlantInsurability = cropVarietyPlantInsurabilityList.stream()
				.filter(x -> x.getCropVarietyId().equals(model.getCropVarietyId()))
				.collect(Collectors.toList());

		model.setCropVarietyPlantInsurabilities(filteredCropVarietyPlantInsurability);
		
		if(validationDtos != null && validationDtos.size() > 0) {
			//Find validation dto for variety
			List<CropVarietyInsurabilityDto> filteredCropVarietyInsurabilityDtos = validationDtos.stream()
					.filter(x -> x.getCropVarietyId().equals(model.getCropVarietyId()))
					.collect(Collectors.toList());
			
			if(filteredCropVarietyInsurabilityDtos != null && filteredCropVarietyInsurabilityDtos.size() == 1) {
				CropVarietyInsurabilityDto validationDto = filteredCropVarietyInsurabilityDtos.get(0);
				model.setIsQuantityInsurableEditableInd(validationDto.getIsQuantityInsurableEditableInd());
				model.setIsUnseededInsurableEditableInd(validationDto.getIsUnseededInsurableEditableInd());
				model.setIsPlantInsurableEditableInd(validationDto.getIsPlantInsurableEditableInd());
				model.setIsAwpEligibleEditableInd(validationDto.getIsAwpEligibleEditableInd());
				model.setIsUnderseedingEligibleEditableInd(validationDto.getIsUnderseedingEligibleEditableInd());
			}
			
		}

	}
	
	static void setSelfLink(Integer insurancePlanId, Boolean loadForEdit, CropVarietyInsurabilityListRsrc resource, URI baseUri) {
		String selfUri = getCropVarietyInsurabilityListSelfUri(insurancePlanId, loadForEdit, baseUri);
		resource.getLinks().add(new RelLink(ResourceTypes.SELF, selfUri, "GET"));
	}
	
	private static void setLinks(
			Integer insurancePlanId,
			Boolean loadForEdit, 
			CropVarietyInsurabilityListRsrc resource, 
			URI baseUri, 
			WebAdeAuthentication authentication) {

		String selfUri = getCropVarietyInsurabilityListSelfUri(insurancePlanId, loadForEdit, baseUri);

		if (authentication.hasAuthority(Scopes.SAVE_CROP_VARIETY_INSURABILITIES)) {
			resource.getLinks().add(new RelLink(ResourceTypes.SAVE_CROP_VARIETY_INSURABILITY_LIST, selfUri, "PUT"));
		}
		
	}
	
	public static String getCropVarietyInsurabilityListSelfUri(Integer insurancePlanId, Boolean loadForEdit, URI baseUri) {

		String result = UriBuilder.fromUri(baseUri)
				.path(CropVarietyInsurabilityListEndpoint.class)
				.queryParam("cropYear", nvl(toString(insurancePlanId), ""))
				.queryParam("loadForEdit", nvl(toString(loadForEdit), ""))
				.build()
				.toString();		

		return result;
	}

	@Override
	public void updateDto(CropVarietyInsurabilityDto dto, CropVarietyInsurability model) throws FactoryException {

		dto.setCropVarietyInsurabilityGuid(model.getCropVarietyInsurabilityGuid());
		dto.setCropVarietyId(model.getCropVarietyId());
		dto.setIsQuantityInsurableInd(model.getIsQuantityInsurableInd());
		dto.setIsUnseededInsurableInd(model.getIsUnseededInsurableInd());
		dto.setIsPlantInsurableInd(model.getIsPlantInsurableInd());
		dto.setIsAwpEligibleInd(model.getIsAwpEligibleInd());
		dto.setIsUnderseedingEligibleInd(model.getIsUnderseedingEligibleInd());
		dto.setIsGrainUnseededDefaultInd(model.getIsGrainUnseededDefaultInd());
		
	}

}
