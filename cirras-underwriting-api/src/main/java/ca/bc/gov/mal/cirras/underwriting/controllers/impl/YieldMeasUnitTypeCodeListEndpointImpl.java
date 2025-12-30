package ca.bc.gov.mal.cirras.underwriting.controllers.impl;

import jakarta.ws.rs.core.GenericEntity;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

import org.springframework.beans.factory.annotation.Autowired;

import ca.bc.gov.nrs.wfone.common.rest.endpoints.BaseEndpointsImpl;
import ca.bc.gov.mal.cirras.underwriting.controllers.YieldMeasUnitTypeCodeListEndpoint;
import ca.bc.gov.mal.cirras.underwriting.controllers.scopes.Scopes;
import ca.bc.gov.mal.cirras.underwriting.data.resources.YieldMeasUnitTypeCodeListRsrc;
import ca.bc.gov.mal.cirras.underwriting.services.CirrasDopYieldService;

public class YieldMeasUnitTypeCodeListEndpointImpl extends BaseEndpointsImpl implements YieldMeasUnitTypeCodeListEndpoint {
		
	@Autowired
	private CirrasDopYieldService cirrasDopYieldService;

	@Override
	public Response getYieldMeasUnitTypeCodeList(
			String insurancePlanId) {
		
		Response response = null;
		
		logRequest();
		
		if(!hasAuthority(Scopes.GET_YIELD_MEAS_UNIT_TYPE_CODES)) {
			return Response.status(Status.FORBIDDEN).build();
		}

		try {
			YieldMeasUnitTypeCodeListRsrc results = (YieldMeasUnitTypeCodeListRsrc) cirrasDopYieldService.getYieldMeasUnitTypeCodeList(
					toInteger(insurancePlanId),
					getFactoryContext(), 
					getWebAdeAuthentication());

			GenericEntity<YieldMeasUnitTypeCodeListRsrc> entity = new GenericEntity<YieldMeasUnitTypeCodeListRsrc>(results) {
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
