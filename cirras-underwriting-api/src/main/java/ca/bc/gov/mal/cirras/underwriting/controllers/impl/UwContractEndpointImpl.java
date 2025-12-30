package ca.bc.gov.mal.cirras.underwriting.controllers.impl;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

import org.springframework.beans.factory.annotation.Autowired;

import ca.bc.gov.nrs.wfone.common.rest.endpoints.BaseEndpointsImpl;
import ca.bc.gov.nrs.wfone.common.service.api.NotFoundException;
import ca.bc.gov.mal.cirras.underwriting.services.CirrasUnderwritingService;
import ca.bc.gov.mal.cirras.underwriting.data.resources.UwContractRsrc;
import ca.bc.gov.mal.cirras.underwriting.controllers.UwContractEndpoint;
import ca.bc.gov.mal.cirras.underwriting.controllers.scopes.Scopes;

public class UwContractEndpointImpl extends BaseEndpointsImpl implements UwContractEndpoint {

	@Autowired
	private CirrasUnderwritingService cirrasUnderwritingService;
	
	@Override
	public Response getUwContract(final String policyId, String loadLinkedPolicies, String loadOtherYearPolicies, String screenType) {
		
		Response response = null;
		
		logRequest();
		
		if(!hasAuthority(Scopes.GET_UWCONTRACT)) {
			return Response.status(Status.FORBIDDEN).build();
		}
		
		try {
			UwContractRsrc result = (UwContractRsrc) cirrasUnderwritingService.getUwContract(
					toInteger(policyId),
					toBoolean(loadLinkedPolicies),
					toBoolean(loadOtherYearPolicies),
					toString(screenType),
					getFactoryContext(), 
					getWebAdeAuthentication());
			response = Response.ok(result).tag(result.getUnquotedETag()).build();

		} catch (NotFoundException e) {
			response = Response.status(Status.NOT_FOUND).build();
			
		} catch (Throwable t) {
			response = getInternalServerErrorResponse(t);
		}
		
		logResponse(response);

		return response;
	}
}
