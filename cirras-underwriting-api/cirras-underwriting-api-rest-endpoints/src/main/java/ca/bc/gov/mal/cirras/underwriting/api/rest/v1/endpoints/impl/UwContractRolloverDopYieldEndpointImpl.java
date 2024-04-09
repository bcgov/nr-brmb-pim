package ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints.impl;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.springframework.beans.factory.annotation.Autowired;

import ca.bc.gov.nrs.wfone.common.rest.endpoints.BaseEndpointsImpl;
import ca.bc.gov.nrs.wfone.common.service.api.NotFoundException;
import ca.bc.gov.mal.cirras.underwriting.service.api.v1.CirrasDopYieldService;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.DopYieldContractRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints.UwContractRolloverDopYieldEndpoint;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints.security.Scopes;

public class UwContractRolloverDopYieldEndpointImpl extends BaseEndpointsImpl implements UwContractRolloverDopYieldEndpoint {

	@Autowired
	private CirrasDopYieldService cirrasDopYieldService;
	
	@Override
	public Response rolloverDopYieldContract(final String policyId) {
		
		Response response = null;
		
		logRequest();
		
		if(!hasAuthority(Scopes.CREATE_DOP_YIELD_CONTRACT)) {
			return Response.status(Status.FORBIDDEN).build();
		}
		
		try {
			DopYieldContractRsrc result = (DopYieldContractRsrc) cirrasDopYieldService.rolloverDopYieldContract(
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
