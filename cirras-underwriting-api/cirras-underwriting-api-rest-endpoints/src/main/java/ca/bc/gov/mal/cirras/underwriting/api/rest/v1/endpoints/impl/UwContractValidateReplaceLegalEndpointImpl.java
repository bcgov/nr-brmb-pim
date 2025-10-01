package ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints.impl;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

import org.springframework.beans.factory.annotation.Autowired;

import ca.bc.gov.nrs.wfone.common.rest.endpoints.BaseEndpointsImpl;
import ca.bc.gov.nrs.wfone.common.service.api.NotFoundException;
import ca.bc.gov.mal.cirras.underwriting.service.api.v1.CirrasInventoryService;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.ReplaceLegalValidationRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints.UwContractValidateReplaceLegalEndpoint;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints.security.Scopes;

public class UwContractValidateReplaceLegalEndpointImpl extends BaseEndpointsImpl implements UwContractValidateReplaceLegalEndpoint {

	@Autowired
	private CirrasInventoryService cirrasInventoryService;
	

	@Override
	public Response validateReplaceLegal(String policyId, String annualFieldDetailId, String fieldLabel, String legalLandId) {
		
		Response response = null;
		
		logRequest();

		if(!hasAuthority(Scopes.CREATE_INVENTORY_CONTRACT)) {
			return Response.status(Status.FORBIDDEN).build();
		}
		
		try {
			ReplaceLegalValidationRsrc result = (ReplaceLegalValidationRsrc) cirrasInventoryService.validateReplaceLegal(
					toInteger(policyId),
					toInteger(annualFieldDetailId),
					fieldLabel,
					toInteger(legalLandId),
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
