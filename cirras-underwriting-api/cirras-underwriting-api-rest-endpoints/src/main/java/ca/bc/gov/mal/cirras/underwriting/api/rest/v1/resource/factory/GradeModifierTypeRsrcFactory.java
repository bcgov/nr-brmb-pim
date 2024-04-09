package ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.factory;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.UriBuilder;

import ca.bc.gov.nrs.common.wfone.rest.resource.RelLink;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.rest.endpoints.resource.factory.BaseResourceFactory;
import ca.bc.gov.nrs.wfone.common.service.api.model.factory.FactoryContext;
import ca.bc.gov.nrs.wfone.common.service.api.model.factory.FactoryException;
import ca.bc.gov.nrs.wfone.common.webade.authentication.WebAdeAuthentication;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints.GradeModifierTypeListEndpoint;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints.security.Scopes;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.GradeModifierTypeListRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.types.ResourceTypes;
import ca.bc.gov.mal.cirras.underwriting.model.v1.GradeModifierType;
import ca.bc.gov.mal.cirras.underwriting.model.v1.GradeModifierTypeList;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.GradeModifierTypeCodeDto;
import ca.bc.gov.mal.cirras.underwriting.service.api.v1.model.factory.GradeModifierTypeFactory;

public class GradeModifierTypeRsrcFactory extends BaseResourceFactory implements GradeModifierTypeFactory { 

	@Override
	public GradeModifierTypeList<? extends GradeModifierType> getGradeModifierTypeList(
			List<GradeModifierTypeCodeDto> dtos,
			Integer cropYear,
			FactoryContext context, 
			WebAdeAuthentication authentication
	) throws FactoryException, DaoException {
		
		List<GradeModifierType> resources = new ArrayList<GradeModifierType>();
		
		for (GradeModifierTypeCodeDto dto : dtos) {
			GradeModifierType resource = new GradeModifierType();
						
			populateResource(resource, dto);

			resources.add(resource);
		}
		
		GradeModifierTypeListRsrc result = new GradeModifierTypeListRsrc();
		result.setCollection(resources);

		String eTag = getEtag(result);
		result.setETag(eTag);

		URI baseUri = getBaseURI(context);
		setSelfLink(cropYear, result, baseUri);
		setLinks(cropYear, result, baseUri, authentication);
		
		return result;
	}

	
	private void populateResource(GradeModifierType model, GradeModifierTypeCodeDto dto) {

		model.setGradeModifierTypeCode(dto.getGradeModifierTypeCode());
		model.setDescription(dto.getDescription());
		model.setEffectiveYear(dto.getEffectiveYear());
		model.setExpiryYear(dto.getExpiryYear());

		Boolean deleteAllowed = false;
		//Deleting is only allowed if it hasn't been used
		if(dto.getTotalUsed() != null && dto.getTotalUsed().equals(0) && dto.getMaxYearUsed() == null) {
			deleteAllowed = true;
		}
		model.setDeleteAllowedInd(deleteAllowed);
		model.setMaxYearUsed(dto.getMaxYearUsed());
	}
	
	static void setSelfLink(Integer cropYear, GradeModifierTypeListRsrc resource, URI baseUri) {
		String selfUri = getGradeModifierTypeListSelfUri(cropYear, baseUri);
		resource.getLinks().add(new RelLink(ResourceTypes.SELF, selfUri, "GET"));
	}
	
	private static void setLinks(Integer cropYear, GradeModifierTypeListRsrc resource, URI baseUri, WebAdeAuthentication authentication) {

		String selfUri = getGradeModifierTypeListSelfUri(cropYear, baseUri);

		if (authentication.hasAuthority(Scopes.SAVE_GRADE_MODIFIERS)) {
			resource.getLinks().add(new RelLink(ResourceTypes.SAVE_GRADE_MODIFIER_TYPE_LIST, selfUri, "PUT"));
		}
		
	}
	
	public static String getGradeModifierTypeListSelfUri(Integer cropYear, URI baseUri) {

		String result = UriBuilder.fromUri(baseUri)
				.path(GradeModifierTypeListEndpoint.class)
				.queryParam("cropYear", nvl(toString(cropYear), ""))
				.build()
				.toString();		

		return result;
	}

	@Override
	public void updateDto(GradeModifierTypeCodeDto dto, GradeModifierType model) throws FactoryException {

		dto.setGradeModifierTypeCode(model.getGradeModifierTypeCode());
		dto.setDescription(model.getDescription());
		dto.setEffectiveYear(model.getEffectiveYear());
		dto.setExpiryYear(model.getExpiryYear());
		
	}

}
