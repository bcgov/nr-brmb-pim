package ca.bc.gov.mal.cirras.underwriting.controllers.impl;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

import org.springframework.beans.factory.annotation.Autowired;

import ca.bc.gov.nrs.wfone.common.rest.endpoints.BaseEndpointsImpl;
import ca.bc.gov.nrs.wfone.common.service.api.NotFoundException;
import ca.bc.gov.mal.cirras.underwriting.services.CirrasInventoryService;
import ca.bc.gov.mal.cirras.underwriting.data.resources.RenameLegalValidationRsrc;
import ca.bc.gov.mal.cirras.underwriting.controllers.UwContractValidateRenameLegalEndpoint;
import ca.bc.gov.mal.cirras.underwriting.controllers.scopes.Scopes;

public class UwContractValidateRenameLegalEndpointImpl extends BaseEndpointsImpl implements UwContractValidateRenameLegalEndpoint {

	@Autowired
	private CirrasInventoryService cirrasInventoryService;
	

	@Override
	public Response validateRenameLegal(String policyId, String annualFieldDetailId, String newLegalLocation, String primaryPropertyIdentifier) {
		
		Response response = null;
		
		logRequest();

		if(!hasAuthority(Scopes.CREATE_INVENTORY_CONTRACT)) {
			return Response.status(Status.FORBIDDEN).build();
		}
		
		try {
			RenameLegalValidationRsrc result = (RenameLegalValidationRsrc) cirrasInventoryService.validateRenameLegal(
					toInteger(policyId),
					toInteger(annualFieldDetailId),
					toStringWithoutDecode(newLegalLocation),
					toStringWithoutDecode(primaryPropertyIdentifier),
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
	
	private static String toStringWithoutDecode(String value) {
		String result = null;
		if(value!=null&&value.trim().length()>0) {
			result = value;
		}
		return result;
	}	
}
