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
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints.UnderwritingYearEndpoint;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints.UnderwritingYearListEndpoint;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints.security.Scopes;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.UnderwritingYearListRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.UnderwritingYearRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.types.ResourceTypes;
import ca.bc.gov.mal.cirras.underwriting.model.v1.UnderwritingYear;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.UnderwritingYearDto;
import ca.bc.gov.mal.cirras.underwriting.service.api.v1.model.factory.UnderwritingYearFactory;

public class UnderwritingYearRsrcFactory extends BaseResourceFactory implements UnderwritingYearFactory { 
	

	@Override
	public UnderwritingYearListRsrc getUnderwritingYearList(
			List<UnderwritingYearDto> dtos,
			FactoryContext context, 
			WebAdeAuthentication authentication
	) throws FactoryException, DaoException {

		URI baseUri = getBaseURI(context);
		
		List<UnderwritingYearRsrc> resources = new ArrayList<UnderwritingYearRsrc>();
		
		for (UnderwritingYearDto dto : dtos) {
			
			UnderwritingYearRsrc resource = new UnderwritingYearRsrc();
			
			populateResource(resource, dto);
			setSelfLink(resource.getCropYear(), resource, baseUri, authentication);
			setLinks(resource.getCropYear(), resource, baseUri, authentication);
			
			resources.add(resource);
		}

		UnderwritingYearListRsrc result = new UnderwritingYearListRsrc();
		result.setCollection(resources);

		String eTag = getEtag(result);
		result.setETag(eTag);

		setSelfLink(result, baseUri);
		
		
		return result;
	}

	@Override
	public void updateDto(UnderwritingYearDto dto, UnderwritingYear model) throws FactoryException {

		dto.setUnderwritingYearGuid(model.getUnderwritingYearGuid());
		dto.setCropYear(model.getCropYear());
		
	}
	
	static void populateResource(UnderwritingYearRsrc resource, UnderwritingYearDto dto) {
		
		resource.setUnderwritingYearGuid(dto.getUnderwritingYearGuid());
		resource.setCropYear(dto.getCropYear());

	}
	
	static void setSelfLink(
			Integer cropYearId, 
			UnderwritingYearRsrc resource, 
			URI baseUri,
			WebAdeAuthentication authentication) 
	{
		if (cropYearId != null) {
			
			String selfUri = getUnderwritingYearSelfUri(cropYearId, baseUri);
			resource.getLinks().add(new RelLink(ResourceTypes.SELF, selfUri, "GET"));
		}
	}
	

	private static String getUnderwritingYearSelfUri(Integer cropYear, URI baseUri) {
		String result = UriBuilder.fromUri(baseUri).path(UnderwritingYearEndpoint.class).build(cropYear).toString();
		return result;
	}
	
	private static void setSelfLink(
		UnderwritingYearListRsrc resource, 
		URI baseUri) {

		String selfUri = getUnderwritingYearListSelfUri(baseUri);
		
		resource.getLinks().add(new RelLink(ResourceTypes.SELF, selfUri, "GET"));
	}	
	
	public static String getUnderwritingYearListSelfUri(
			URI baseUri
		) {

			String result = UriBuilder.fromUri(baseUri)
				.path(UnderwritingYearListEndpoint.class)
				.build()
				.toString();

			return result;
	}
	
	@Override
	public UnderwritingYearRsrc getUnderwritingYear(
			UnderwritingYearDto dto, 
			FactoryContext context, 
			WebAdeAuthentication authentication
		) {
		
		UnderwritingYearRsrc resource = new UnderwritingYearRsrc();
		
		populateResource(resource, dto);
		
		String eTag = getEtag(resource);
		resource.setETag(eTag);

		URI baseUri = getBaseURI(context);
		setSelfLink(dto.getCropYear(), resource, baseUri, authentication);
		setLinks(dto.getCropYear(), resource, baseUri, authentication);
		
		return resource;
	}
	
	private static void setLinks(Integer cropYear, UnderwritingYearRsrc resource, URI baseUri, WebAdeAuthentication authentication) {

		String selfUri = getUnderwritingYearSelfUri(cropYear, baseUri);

		if (authentication.hasAuthority(Scopes.DELETE_UNDERWRITING_YEAR)) {
			resource.getLinks().add(new RelLink(ResourceTypes.DELETE_UNDERWRITING_YEAR, selfUri, "DELETE"));
		}
	}
	
}
