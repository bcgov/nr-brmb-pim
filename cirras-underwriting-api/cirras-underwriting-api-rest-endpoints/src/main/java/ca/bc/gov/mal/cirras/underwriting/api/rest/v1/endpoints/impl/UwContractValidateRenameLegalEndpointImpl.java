package ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints.impl;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.springframework.beans.factory.annotation.Autowired;

import ca.bc.gov.nrs.wfone.common.rest.endpoints.BaseEndpointsImpl;
import ca.bc.gov.nrs.wfone.common.service.api.NotFoundException;
import ca.bc.gov.mal.cirras.underwriting.service.api.v1.CirrasInventoryService;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.RenameLegalValidationRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints.UwContractValidateRenameLegalEndpoint;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints.security.Scopes;

public class UwContractValidateRenameLegalEndpointImpl extends BaseEndpointsImpl implements UwContractValidateRenameLegalEndpoint {

	@Autowired
	private CirrasInventoryService cirrasInventoryService;
	

	@Override
	public Response validateRenameLegal(String policyId, String annualFieldDetailId, String newLegalLocation) {
		
		Response response = null;
		
		logRequest();

		if(!hasAuthority(Scopes.CREATE_INVENTORY_CONTRACT)) {
			return Response.status(Status.FORBIDDEN).build();
		}
		
		try {
			RenameLegalValidationRsrc result = (RenameLegalValidationRsrc) cirrasInventoryService.validateRenameLegal(
					toInteger(policyId),
					toInteger(annualFieldDetailId),
					newLegalLocation,
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
