package ca.bc.gov.mal.cirras.underwriting.controllers.impl;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

import org.springframework.beans.factory.annotation.Autowired;

import ca.bc.gov.nrs.wfone.common.rest.endpoints.BaseEndpointsImpl;
import ca.bc.gov.nrs.wfone.common.service.api.NotFoundException;
import ca.bc.gov.mal.cirras.underwriting.services.CirrasInventoryService;
import ca.bc.gov.mal.cirras.underwriting.data.resources.AddFieldValidationRsrc;
import ca.bc.gov.mal.cirras.underwriting.controllers.UwContractValidateAddFieldEndpoint;
import ca.bc.gov.mal.cirras.underwriting.controllers.scopes.Scopes;

public class UwContractValidateAddFieldEndpointImpl extends BaseEndpointsImpl implements UwContractValidateAddFieldEndpoint {

	@Autowired
	private CirrasInventoryService cirrasInventoryService;
	

	@Override
	public Response validateAddField(String policyId, String fieldId, String transferFromPolicyId) {
		
		Response response = null;
		
		logRequest();

		if(!hasAuthority(Scopes.CREATE_INVENTORY_CONTRACT)) {
			return Response.status(Status.FORBIDDEN).build();
		}
		
		try {
			AddFieldValidationRsrc result = (AddFieldValidationRsrc) cirrasInventoryService.validateAddField(
					toInteger(policyId),
					toInteger(fieldId),
					toInteger(transferFromPolicyId),
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
