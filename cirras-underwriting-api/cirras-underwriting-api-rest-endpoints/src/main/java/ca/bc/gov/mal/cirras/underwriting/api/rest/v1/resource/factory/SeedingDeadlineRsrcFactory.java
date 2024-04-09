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
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints.InventoryContractListEndpoint;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints.SeedingDeadlineListEndpoint;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.SeedingDeadlineListRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.types.ResourceTypes;
import ca.bc.gov.mal.cirras.underwriting.model.v1.SeedingDeadline;
import ca.bc.gov.mal.cirras.underwriting.model.v1.SeedingDeadlineList;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.SeedingDeadlineDto;
import ca.bc.gov.mal.cirras.underwriting.service.api.v1.model.factory.SeedingDeadlineFactory;

public class SeedingDeadlineRsrcFactory extends BaseResourceFactory implements SeedingDeadlineFactory { 

	@Override
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
	
	@Override
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
