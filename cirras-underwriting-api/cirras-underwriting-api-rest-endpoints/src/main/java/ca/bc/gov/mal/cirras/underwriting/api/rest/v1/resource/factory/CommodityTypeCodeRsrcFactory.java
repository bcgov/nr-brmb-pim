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
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints.CommodityTypeCodeListEndpoint;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.CommodityTypeCodeListRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.SyncCommodityTypeCodeRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.types.ResourceTypes;
import ca.bc.gov.mal.cirras.underwriting.model.v1.CommodityTypeCode;
import ca.bc.gov.mal.cirras.underwriting.model.v1.CommodityTypeCodeList;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.CommodityTypeCodeDto;
import ca.bc.gov.mal.cirras.underwriting.service.api.v1.model.factory.CommodityTypeCodeFactory;

public class CommodityTypeCodeRsrcFactory extends BaseResourceFactory implements CommodityTypeCodeFactory { 

	@Override
	public CommodityTypeCodeList<? extends CommodityTypeCode> getCommodityTypeCodeList(
			List<CommodityTypeCodeDto> dtos,
			Integer insurancePlanId,
			FactoryContext context, 
			WebAdeAuthentication authentication
	) throws FactoryException, DaoException {
		
		List<SyncCommodityTypeCodeRsrc> resources = new ArrayList<SyncCommodityTypeCodeRsrc>();
		
		for (CommodityTypeCodeDto dto : dtos) {
			SyncCommodityTypeCodeRsrc resource = new SyncCommodityTypeCodeRsrc();
						
			populateResource(resource, dto);

			resources.add(resource);
		}
		
		CommodityTypeCodeListRsrc result = new CommodityTypeCodeListRsrc();
		result.setCollection(resources);

		String eTag = getEtag(result);
		result.setETag(eTag);

		URI baseUri = getBaseURI(context);
		setSelfLink(insurancePlanId, result, baseUri);
		
		return result;
	}

	
	private void populateResource(SyncCommodityTypeCodeRsrc resource, CommodityTypeCodeDto dto) {

		resource.setCommodityTypeCode(dto.getCommodityTypeCode());
		resource.setCropCommodityId(dto.getCropCommodityId());
		resource.setDescription(dto.getDescription());
		resource.setEffectiveDate(dto.getEffectiveDate());
		resource.setExpiryDate(dto.getExpiryDate());
		resource.setInsurancePlanId(dto.getInsurancePlanId());

	}
	
	static void setSelfLink(Integer insurancePlanId, CommodityTypeCodeListRsrc resource, URI baseUri) {
		String selfUri = getCommodityTypeCodeListSelfUri(insurancePlanId, baseUri);
		resource.getLinks().add(new RelLink(ResourceTypes.SELF, selfUri, "GET"));
	}
	
	public static String getCommodityTypeCodeListSelfUri(Integer insurancePlanId, URI baseUri) {

		String result = UriBuilder.fromUri(baseUri)
				.path(CommodityTypeCodeListEndpoint.class)
				.queryParam("insurancePlanId", nvl(toString(insurancePlanId), ""))
				.build()
				.toString();		

		return result;
	}

}
