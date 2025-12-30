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
import ca.bc.gov.mal.cirras.underwriting.controllers.InventoryContractListEndpoint;
import ca.bc.gov.mal.cirras.underwriting.controllers.SeedingDeadlineListEndpoint;
import ca.bc.gov.mal.cirras.underwriting.data.resources.SeedingDeadlineListRsrc;
import ca.bc.gov.mal.cirras.underwriting.data.resources.types.ResourceTypes;
import ca.bc.gov.mal.cirras.underwriting.data.models.SeedingDeadline;
import ca.bc.gov.mal.cirras.underwriting.data.models.SeedingDeadlineList;
import ca.bc.gov.mal.cirras.underwriting.data.entities.SeedingDeadlineDto;

public class SeedingDeadlineRsrcFactory extends BaseResourceFactory { 

	
	public SeedingDeadlineList<? extends SeedingDeadline> getSeedingDeadlineList(
			List<SeedingDeadlineDto> dtos,
			Integer cropYear,
			FactoryContext context, 
			WebAdeAuthentication authentication
	) throws FactoryException, DaoException {
		
		List<SeedingDeadline> resources = new ArrayList<SeedingDeadline>();
		
		for (SeedingDeadlineDto dto : dtos) {
			SeedingDeadline resource = new SeedingDeadline();
						
			populateResource(resource, dto);

			resources.add(resource);
		}
		
		SeedingDeadlineListRsrc result = new SeedingDeadlineListRsrc();
		result.setCollection(resources);

		String eTag = getEtag(result);
		result.setETag(eTag);

		URI baseUri = getBaseURI(context);
		setSelfLink(cropYear, result, baseUri);
		
		
		
		return result;
	}
	
	
	public void updateDto(SeedingDeadlineDto dto, SeedingDeadline model) throws FactoryException {

		dto.setSeedingDeadlineGuid(model.getSeedingDeadlineGuid());
		dto.setCommodityTypeCode(model.getCommodityTypeCode());
		dto.setCropYear(model.getCropYear());
		dto.setFullCoverageDeadlineDate(model.getFullCoverageDeadlineDate());
		dto.setFullCoverageDeadlineDateDefault(model.getFullCoverageDeadlineDateDefault());
		dto.setFinalCoverageDeadlineDate(model.getFinalCoverageDeadlineDate());
		dto.setFinalCoverageDeadlineDateDefault(model.getFinalCoverageDeadlineDateDefault());
		
	}
	
	private void populateResource(SeedingDeadline model, SeedingDeadlineDto dto) {

		model.setSeedingDeadlineGuid(dto.getSeedingDeadlineGuid());
		model.setCommodityTypeCode(dto.getCommodityTypeCode());
		model.setCropYear(dto.getCropYear());
		model.setFullCoverageDeadlineDate(dto.getFullCoverageDeadlineDate());
		model.setFullCoverageDeadlineDateDefault(dto.getFullCoverageDeadlineDateDefault());
		model.setFinalCoverageDeadlineDate(dto.getFinalCoverageDeadlineDate());
		model.setFinalCoverageDeadlineDateDefault(dto.getFinalCoverageDeadlineDateDefault());

	}
	
	static void setSelfLink(Integer cropYear, SeedingDeadlineListRsrc resource, URI baseUri) {
		String selfUri = getSeedingDeadlineListSelfUri(cropYear, baseUri);
		resource.getLinks().add(new RelLink(ResourceTypes.SELF, selfUri, "GET"));
	}
	
	public static String getSeedingDeadlineListSelfUri(Integer cropYear, URI baseUri) {

		String result = UriBuilder.fromUri(baseUri)
				.path(SeedingDeadlineListEndpoint.class)
				.queryParam("cropYear", nvl(toString(cropYear), ""))
				.build()
				.toString();		

		return result;
	}

	
//	public static String getSeedingDeadlineListSelfUri(URI baseUri) {
//
//		String result = UriBuilder.fromUri(baseUri).path(SeedingDeadlineListEndpoint.class).build().toString();		
//		return result;
//	}

}
