package ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints.impl;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.springframework.beans.factory.annotation.Autowired;

import ca.bc.gov.nrs.wfone.common.rest.endpoints.BaseEndpointsImpl;
import ca.bc.gov.nrs.wfone.common.service.api.NotFoundException;
import ca.bc.gov.mal.cirras.underwriting.service.api.v1.CirrasInventoryService;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.RemoveFieldValidationRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints.UwContractValidateRemoveFieldEndpoint;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints.security.Scopes;

public class UwContractValidateRemoveFieldEndpointImpl extends BaseEndpointsImpl implements UwContractValidateRemoveFieldEndpoint {

	@Autowired
	private CirrasInventoryService cirrasInventoryService;
	

	@Override
	public Response validateRemoveField(String policyId, String fieldId) {
		
		Response response = null;
		
		logRequest();

		if(!hasAuthority(Scopes.CREATE_INVENTORY_CONTRACT)) {
			return Response.status(Status.FORBIDDEN).build();
		}
		
		try {
			RemoveFieldValidationRsrc result = (RemoveFieldValidationRsrc) cirrasInventoryService.validateRemoveField(
					toInteger(policyId),
					toInteger(fieldId),
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
