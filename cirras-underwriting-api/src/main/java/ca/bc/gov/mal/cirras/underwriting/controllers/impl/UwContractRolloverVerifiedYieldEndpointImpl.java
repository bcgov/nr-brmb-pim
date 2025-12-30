package ca.bc.gov.mal.cirras.underwriting.controllers.impl;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

import org.springframework.beans.factory.annotation.Autowired;

import ca.bc.gov.nrs.wfone.common.rest.endpoints.BaseEndpointsImpl;
import ca.bc.gov.nrs.wfone.common.service.api.NotFoundException;
import ca.bc.gov.mal.cirras.underwriting.services.CirrasVerifiedYieldService;
import ca.bc.gov.mal.cirras.underwriting.data.resources.VerifiedYieldContractRsrc;
import ca.bc.gov.mal.cirras.underwriting.controllers.UwContractRolloverVerifiedYieldEndpoint;
import ca.bc.gov.mal.cirras.underwriting.controllers.scopes.Scopes;

public class UwContractRolloverVerifiedYieldEndpointImpl extends BaseEndpointsImpl implements UwContractRolloverVerifiedYieldEndpoint {

	@Autowired
	private CirrasVerifiedYieldService cirrasVerifiedYieldService;
	
	@Override
	public Response rolloverVerifiedYieldContract(final String policyId) {
		
		Response response = null;
		
		logRequest();
		
		if(!hasAuthority(Scopes.CREATE_VERIFIED_YIELD_CONTRACT)) {
			return Response.status(Status.FORBIDDEN).build();
		}
		
		try {
			VerifiedYieldContractRsrc result = (VerifiedYieldContractRsrc) cirrasVerifiedYieldService.rolloverVerifiedYieldContract(
					toInteger(policyId),
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
