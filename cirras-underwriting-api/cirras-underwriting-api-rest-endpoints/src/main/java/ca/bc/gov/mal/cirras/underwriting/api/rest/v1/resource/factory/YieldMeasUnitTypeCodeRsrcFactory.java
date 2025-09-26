package ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.factory;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import jakarta.ws.rs.core.UriBuilder;

import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.rest.endpoints.resource.factory.BaseResourceFactory;
import ca.bc.gov.nrs.wfone.common.service.api.model.factory.FactoryException;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints.YieldMeasUnitTypeCodeListEndpoint;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.YieldMeasUnitTypeCodeListRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.YieldMeasUnitTypeCodeRsrc;
import ca.bc.gov.mal.cirras.underwriting.model.v1.YieldMeasUnitTypeCode;
import ca.bc.gov.mal.cirras.underwriting.model.v1.YieldMeasUnitTypeCodeList;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.YieldMeasUnitTypeCodeDto;
import ca.bc.gov.mal.cirras.underwriting.service.api.v1.model.factory.YieldMeasUnitTypeCodeFactory;

public class YieldMeasUnitTypeCodeRsrcFactory extends BaseResourceFactory implements YieldMeasUnitTypeCodeFactory { 

	@Override
	public YieldMeasUnitTypeCodeList<? extends YieldMeasUnitTypeCode> getYieldMeasUnitTypeCodeList(
			List<YieldMeasUnitTypeCodeDto> dtos
		)  throws FactoryException, DaoException {
		
		List<YieldMeasUnitTypeCodeRsrc> resources = new ArrayList<YieldMeasUnitTypeCodeRsrc>();
		
		for (YieldMeasUnitTypeCodeDto dto : dtos) {
			YieldMeasUnitTypeCodeRsrc resource = new YieldMeasUnitTypeCodeRsrc();
						
			populateResource(resource, dto);

			resources.add(resource);
		}
		
		YieldMeasUnitTypeCodeListRsrc result = new YieldMeasUnitTypeCodeListRsrc();
		result.setCollection(resources);
		
		return result;
	}

	
	private void populateResource(YieldMeasUnitTypeCodeRsrc resource, YieldMeasUnitTypeCodeDto dto) {

		resource.setYieldMeasUnitTypeCode(dto.getYieldMeasUnitTypeCode());
		resource.setDescription(dto.getDescription());
		resource.setDecimalPrecision(dto.getDecimalPrecision());
		resource.setEffectiveDate(dto.getEffectiveDate());
		resource.setExpiryDate(dto.getExpiryDate());
		resource.setInsurancePlanId(dto.getInsurancePlanId());
		resource.setIsDefaultYieldUnitInd(dto.getIsDefaultYieldUnitInd());

	}
	
	
	public static String getYieldMeasUnitTypeCodeListSelfUri(Integer insurancePlanId, URI baseUri) {

		String result = UriBuilder.fromUri(baseUri)
				.path(YieldMeasUnitTypeCodeListEndpoint.class)
				.queryParam("insurancePlanId", nvl(toString(insurancePlanId), ""))
				.build()
				.toString();		

		return result;
	}

}
