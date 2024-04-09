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
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints.YieldMeasUnitConversionListEndpoint;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints.security.Scopes;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.YieldMeasUnitConversionListRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.types.ResourceTypes;
import ca.bc.gov.mal.cirras.underwriting.model.v1.YieldMeasUnitConversion;
import ca.bc.gov.mal.cirras.underwriting.model.v1.YieldMeasUnitConversionList;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.YieldMeasUnitConversionDto;
import ca.bc.gov.mal.cirras.underwriting.service.api.v1.model.factory.YieldMeasUnitConversionFactory;

public class YieldMeasUnitConversionRsrcFactory extends BaseResourceFactory implements YieldMeasUnitConversionFactory { 

	@Override
	public YieldMeasUnitConversionList<? extends YieldMeasUnitConversion> getYieldMeasUnitConversionList(
			List<YieldMeasUnitConversionDto> yieldMeasUnitConversionDtos,
			Integer insurancePlanId,
			String srcYieldMeasUnitTypeCode,
			String targetYieldMeasUnitTypeCode,
			FactoryContext context, 
			WebAdeAuthentication authentication
	) throws FactoryException, DaoException {
		
		List<YieldMeasUnitConversion> resources = new ArrayList<YieldMeasUnitConversion>();
		
		for (YieldMeasUnitConversionDto dto : yieldMeasUnitConversionDtos) {
			
			YieldMeasUnitConversion resource = new YieldMeasUnitConversion();
						
			populateResource(resource, dto);

			resources.add(resource);
		}
		
		YieldMeasUnitConversionListRsrc result = new YieldMeasUnitConversionListRsrc();
		result.setCollection(resources);

		String eTag = getEtag(result);
		result.setETag(eTag);

		URI baseUri = getBaseURI(context);
		setSelfLink(insurancePlanId, srcYieldMeasUnitTypeCode, targetYieldMeasUnitTypeCode, result, baseUri);
		setLinks(insurancePlanId, srcYieldMeasUnitTypeCode, targetYieldMeasUnitTypeCode, result, baseUri, authentication);
		
		return result;
	}

	
	private void populateResource(
			YieldMeasUnitConversion model, 
			YieldMeasUnitConversionDto dto 
		) {
		
		model.setYieldMeasUnitConversionGuid(dto.getYieldMeasUnitConversionGuid());
		model.setCropCommodityId(dto.getCropCommodityId());
		model.setSrcYieldMeasUnitTypeCode(dto.getSrcYieldMeasUnitTypeCode());
		model.setTargetYieldMeasUnitTypeCode(dto.getTargetYieldMeasUnitTypeCode());
		model.setVersionNumber(dto.getVersionNumber());
		model.setConversionFactor(dto.getConversionFactor());
		model.setEffectiveCropYear(dto.getEffectiveCropYear());
		model.setExpiryCropYear(dto.getExpiryCropYear());
		
		model.setInsurancePlanId(dto.getInsurancePlanId());
		model.setCommodityName(dto.getCommodityName());


	}
	
	static void setSelfLink(Integer insurancePlanId, 
							String srcYieldMeasUnitTypeCode,
							String targetYieldMeasUnitTypeCode,
							YieldMeasUnitConversionListRsrc resource, 
							URI baseUri) {
		String selfUri = getYieldMeasUnitConversionListSelfUri(insurancePlanId, srcYieldMeasUnitTypeCode, targetYieldMeasUnitTypeCode, baseUri);
		resource.getLinks().add(new RelLink(ResourceTypes.SELF, selfUri, "GET"));
	}
	
	private static void setLinks(
			Integer insurancePlanId,
			String srcYieldMeasUnitTypeCode,
			String targetYieldMeasUnitTypeCode,
			YieldMeasUnitConversionListRsrc resource, 
			URI baseUri, 
			WebAdeAuthentication authentication) {

		String selfUri = getYieldMeasUnitConversionListSelfUri(insurancePlanId, srcYieldMeasUnitTypeCode, targetYieldMeasUnitTypeCode, baseUri);

		if (authentication.hasAuthority(Scopes.SAVE_YIELD_MEAS_UNIT_CONVERSIONS)) {
			resource.getLinks().add(new RelLink(ResourceTypes.SAVE_YIELD_MEAS_UNIT_CONVERSION_LIST, selfUri, "PUT"));
		}
		
	}
	
	public static String getYieldMeasUnitConversionListSelfUri(
			Integer insurancePlanId, 
			String srcYieldMeasUnitTypeCode,
			String targetYieldMeasUnitTypeCode,
			URI baseUri) {

		String result = UriBuilder.fromUri(baseUri)
				.path(YieldMeasUnitConversionListEndpoint.class)
				.queryParam("insurancePlanId", nvl(toString(insurancePlanId), ""))
				.queryParam("srcYieldMeasUnitTypeCode", nvl(srcYieldMeasUnitTypeCode, ""))
				.queryParam("targetYieldMeasUnitTypeCode", nvl(targetYieldMeasUnitTypeCode, ""))
				.build()
				.toString();		

		return result;
	}


	@Override
	public void updateDto(YieldMeasUnitConversionDto dto, YieldMeasUnitConversion model) throws FactoryException {
		
		dto.setYieldMeasUnitConversionGuid(model.getYieldMeasUnitConversionGuid());
		dto.setCropCommodityId(model.getCropCommodityId());
		dto.setSrcYieldMeasUnitTypeCode(model.getSrcYieldMeasUnitTypeCode());
		dto.setTargetYieldMeasUnitTypeCode(model.getTargetYieldMeasUnitTypeCode());
		dto.setVersionNumber(model.getVersionNumber());
		dto.setConversionFactor(model.getConversionFactor());
		dto.setEffectiveCropYear(model.getEffectiveCropYear());
		dto.setExpiryCropYear(model.getExpiryCropYear());

	}

}
