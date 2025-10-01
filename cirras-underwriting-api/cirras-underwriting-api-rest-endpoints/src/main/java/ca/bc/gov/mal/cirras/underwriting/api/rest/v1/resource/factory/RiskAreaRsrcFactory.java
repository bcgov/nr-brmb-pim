package ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.factory;

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
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints.RiskAreaListEndpoint;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.RiskAreaListRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.RiskAreaRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.types.ResourceTypes;
import ca.bc.gov.mal.cirras.underwriting.model.v1.RiskArea;
import ca.bc.gov.mal.cirras.underwriting.model.v1.RiskAreaList;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.RiskAreaDto;
import ca.bc.gov.mal.cirras.underwriting.service.api.v1.model.factory.RiskAreaFactory;

public class RiskAreaRsrcFactory extends BaseResourceFactory implements RiskAreaFactory { 

	@Override
	public RiskAreaList<? extends RiskArea> getRiskAreaList(
			List<RiskAreaDto> dtos,
			Integer insurancePlanId,
			FactoryContext context, 
			WebAdeAuthentication authentication
	) throws FactoryException, DaoException {
		
		URI baseUri = getBaseURI(context);

		List<RiskAreaRsrc> resources = new ArrayList<RiskAreaRsrc>();
		
		for (RiskAreaDto dto : dtos) {
			RiskAreaRsrc resource = new RiskAreaRsrc();
						
			populateResource(resource, dto);

			resources.add(resource);
		}
		
		RiskAreaListRsrc result = new RiskAreaListRsrc();
		result.setCollection(resources);

		String eTag = getEtag(result);
		result.setETag(eTag);

		setSelfLinkList(insurancePlanId, result, baseUri);
		
		return result;
	}

	
	private void populateResource(RiskAreaRsrc resource, RiskAreaDto dto) {

		resource.setRiskAreaId(dto.getRiskAreaId());
		resource.setInsurancePlanId(dto.getInsurancePlanId());
		resource.setRiskAreaName(dto.getRiskAreaName());
		resource.setDescription(dto.getDescription());
		resource.setEffectiveDate(dto.getEffectiveDate());
		resource.setExpiryDate(dto.getExpiryDate());
		resource.setInsurancePlanName(dto.getInsurancePlanName());
		resource.setLegalLandId(dto.getLegalLandId());

	}
	
	static void setSelfLinkList(Integer insurancePlanId, RiskAreaListRsrc resource, URI baseUri) {
		String selfUri = getRiskAreaListSelfUri(insurancePlanId, baseUri);
		resource.getLinks().add(new RelLink(ResourceTypes.SELF, selfUri, "GET"));
	}
	
	public static String getRiskAreaListSelfUri(Integer insurancePlanId, URI baseUri) {

		String result = UriBuilder.fromUri(baseUri)
				.path(RiskAreaListEndpoint.class)
				.queryParam("insurancePlanId", nvl(toString(insurancePlanId), ""))
				.build()
				.toString();		

		return result;
	}

}
