package ca.bc.gov.mal.cirras.underwriting.data.assemblers;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import jakarta.ws.rs.core.UriBuilder;

import ca.bc.gov.nrs.common.wfone.rest.resource.RelLink;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.rest.endpoints.resource.factory.BaseResourceFactory;
import ca.bc.gov.nrs.wfone.common.service.api.model.factory.FactoryContext;
import ca.bc.gov.nrs.wfone.common.service.api.model.factory.FactoryException;
import ca.bc.gov.nrs.wfone.common.webade.authentication.WebAdeAuthentication;
import ca.bc.gov.mal.cirras.underwriting.controllers.GradeModifierListEndpoint;
import ca.bc.gov.mal.cirras.underwriting.controllers.scopes.Scopes;
import ca.bc.gov.mal.cirras.underwriting.data.resources.GradeModifierListRsrc;
import ca.bc.gov.mal.cirras.underwriting.data.resources.GradeModifierRsrc;
import ca.bc.gov.mal.cirras.underwriting.data.resources.types.ResourceTypes;
import ca.bc.gov.mal.cirras.underwriting.data.entities.GradeModifierDto;

public class GradeModifierRsrcFactory extends BaseResourceFactory { 

	
	public GradeModifierListRsrc getGradeModifierList(
			List<GradeModifierDto> dtos,
			Integer cropYear,
			Integer insurancePlanId,
			Integer cropCommodityId,
			FactoryContext context, 
			WebAdeAuthentication authentication
	) throws FactoryException, DaoException {
		
		List<GradeModifierRsrc> resources = new ArrayList<GradeModifierRsrc>();
		
		for (GradeModifierDto dto : dtos) {
			GradeModifierRsrc resource = new GradeModifierRsrc();
						
			populateResource(resource, dto);

			resources.add(resource);
		}
		
		GradeModifierListRsrc result = new GradeModifierListRsrc();
		result.setCollection(resources);

		String eTag = getEtag(result);
		result.setETag(eTag);

		URI baseUri = getBaseURI(context);
		setSelfLink(cropYear, insurancePlanId, cropCommodityId, result, baseUri);
		setLinks(cropYear, insurancePlanId, cropCommodityId, result, baseUri, authentication);
		
		return result;
	}

	
	private void populateResource(GradeModifierRsrc resource, GradeModifierDto dto) {

		resource.setCropCommodityId(dto.getCropCommodityId());
		resource.setCropYear(dto.getCropYear());
		resource.setDescription(dto.getDescription());
		resource.setGradeModifierGuid(dto.getGradeModifierGuid());
		resource.setGradeModifierTypeCode(dto.getGradeModifierTypeCode());
		resource.setGradeModifierValue(dto.getGradeModifierValue());
		resource.setInsurancePlanId(dto.getInsurancePlanId());
		Boolean deleteAllowed = false;
		//Deleting is only allowed if it hasn't been used
		if(dto.getTotalUsed() != null && dto.getTotalUsed().equals(0)) {
			deleteAllowed = true;
		}
		resource.setDeleteAllowedInd(deleteAllowed);
	}
	
	static void setSelfLink(Integer cropYear, Integer insurancePlanId, Integer cropCommodityId, GradeModifierListRsrc resource, URI baseUri) {
		String selfUri = getGradeModifierListSelfUri(cropYear, insurancePlanId, cropCommodityId, baseUri);
		resource.getLinks().add(new RelLink(ResourceTypes.SELF, selfUri, "GET"));
	}
	
	private static void setLinks(Integer cropYear, Integer insurancePlanId, Integer cropCommodityId, GradeModifierListRsrc resource, URI baseUri, WebAdeAuthentication authentication) {

		String selfUri = getGradeModifierListSelfUri(cropYear, insurancePlanId, cropCommodityId, baseUri);

		if (authentication.hasAuthority(Scopes.SAVE_GRADE_MODIFIERS)) {
			resource.getLinks().add(new RelLink(ResourceTypes.SAVE_GRADE_MODIFIER_LIST, selfUri, "PUT"));
		}
		
	}
	
	public static String getGradeModifierListSelfUri(Integer cropYear, Integer insurancePlanId, Integer cropCommodityId, URI baseUri) {

		String result = UriBuilder.fromUri(baseUri)
				.path(GradeModifierListEndpoint.class)
				.queryParam("cropYear", nvl(toString(cropYear), ""))
				.queryParam("insurancePlanId", nvl(toString(insurancePlanId), ""))
				.queryParam("cropCommodityId", nvl(toString(cropCommodityId), ""))
				.build()
				.toString();		

		return result;
	}


	
	public void updateDto(GradeModifierDto dto, GradeModifierRsrc model) throws FactoryException {

		dto.setGradeModifierGuid(model.getGradeModifierGuid());
		dto.setCropCommodityId(model.getCropCommodityId());
		dto.setCropYear(model.getCropYear());
		dto.setGradeModifierTypeCode(model.getGradeModifierTypeCode());
		dto.setGradeModifierValue(model.getGradeModifierValue());
		
	}

}
