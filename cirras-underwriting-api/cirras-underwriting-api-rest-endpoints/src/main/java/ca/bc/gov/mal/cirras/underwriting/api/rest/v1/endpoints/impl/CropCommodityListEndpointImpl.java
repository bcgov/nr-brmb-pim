package ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints.impl;

import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;

import ca.bc.gov.nrs.wfone.common.rest.endpoints.BaseEndpointsImpl;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints.CropCommodityListEndpoint;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.parameters.validation.ParameterValidator;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.CropCommodityListRsrc;
import ca.bc.gov.mal.cirras.underwriting.service.api.v1.CirrasCommodityService;

public class CropCommodityListEndpointImpl extends BaseEndpointsImpl implements CropCommodityListEndpoint {
		
	@Autowired
	private CirrasCommodityService cirrasCommodityService;

	@Autowired
	private ParameterValidator parameterValidator;
	
	public void setCirrasCommodityService(CirrasCommodityService cirrasCommodityService) {
		this.cirrasCommodityService = cirrasCommodityService;
	}

	public void setParameterValidator(ParameterValidator parameterValidator) {
		this.parameterValidator = parameterValidator;
	}
	

	@Override
	public Response getCropCommodityList(
			String insurancePlanId, 
			String cropYear,
			String commodityType,
			String loadChildren) {
		
		Response response = null;
		
		logRequest();

		try {

			CropCommodityListRsrc results = (CropCommodityListRsrc) cirrasCommodityService.getCropCommodityList(
					toInteger(insurancePlanId),
					toInteger(cropYear),
					toString(commodityType),
					toBoolean(loadChildren),
					getFactoryContext(), 
					getWebAdeAuthentication());

				GenericEntity<CropCommodityListRsrc> entity = new GenericEntity<CropCommodityListRsrc>(results) {
					/* do nothing */
				};

				response = Response.ok(entity).tag(results.getUnquotedETag()).build();
			
		} catch (Throwable t) {
			response = getInternalServerErrorResponse(t);
		}
		
		logResponse(response);

		return response;
	}

}
