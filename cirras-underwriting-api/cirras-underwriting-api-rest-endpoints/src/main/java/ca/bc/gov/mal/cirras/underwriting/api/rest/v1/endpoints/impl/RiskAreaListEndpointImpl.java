package ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints.impl;

import jakarta.ws.rs.core.GenericEntity;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

import org.springframework.beans.factory.annotation.Autowired;

import ca.bc.gov.nrs.wfone.common.rest.endpoints.BaseEndpointsImpl;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints.RiskAreaListEndpoint;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints.security.Scopes;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.RiskAreaListRsrc;
import ca.bc.gov.mal.cirras.underwriting.service.api.v1.CirrasUwLandManagementService;

public class RiskAreaListEndpointImpl extends BaseEndpointsImpl implements RiskAreaListEndpoint {
		
	@Autowired
	private CirrasUwLandManagementService cirrasUwLandManagementService;


	@Override
	public Response getRiskAreaList(String insurancePlanId) {
		
		Response response = null;
		
		logRequest();
		
		if(!hasAuthority(Scopes.GET_CODE_TABLES)) {
			return Response.status(Status.FORBIDDEN).build();
		}

		try {
			RiskAreaListRsrc results = (RiskAreaListRsrc) cirrasUwLandManagementService.getRiskAreaList(
					toInteger(insurancePlanId),
					getFactoryContext(), 
					getWebAdeAuthentication());

			GenericEntity<RiskAreaListRsrc> entity = new GenericEntity<RiskAreaListRsrc>(results) {
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
