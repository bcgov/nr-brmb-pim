package ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints.impl;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import ca.bc.gov.nrs.wfone.common.rest.endpoints.BaseEndpointsImpl;
import ca.bc.gov.nrs.wfone.common.service.api.NotFoundException;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints.PolicyEndpoint;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints.security.Scopes;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.PolicyRsrc;
import ca.bc.gov.mal.cirras.underwriting.service.api.v1.CirrasDataSyncService;

public class PolicyEndpointImpl extends BaseEndpointsImpl implements PolicyEndpoint {
	
	private static final Logger logger = LoggerFactory.getLogger(PolicyEndpointImpl.class);
	
	@Autowired
	private CirrasDataSyncService cirrasDataSyncService; 

	@Override
	public Response synchronizePolicy(PolicyRsrc resource) {
		logger.debug("<synchronizePolicy");
		Response response = null;
		
		logRequest();
		logResource(resource, "synchronizePolicy");
		
		if(!hasAuthority(Scopes.UPDATE_SYNC_UNDERWRITING)) {
			return Response.status(Status.FORBIDDEN).build();
		}

		try {
			
			cirrasDataSyncService.synchronizePolicy(
					resource, 
					getFactoryContext(), 
					getWebAdeAuthentication());

			response = Response.status(204).build();
		} catch (Throwable t) {
			response = getInternalServerErrorResponse(t);
		}
		
		logResponse(response);

		logger.debug(">synchronizePolicy " + response);
		return response;
	}
	
	
	
	@Override
	public Response getPolicy(String policyId) {
		logger.debug("<getPolicy");

		Response response = null;
		
		logRequest();
		
		if(!hasAuthority(Scopes.GET_POLICY)) {
			return Response.status(Status.FORBIDDEN).build();
		}
		
		try {
			
			PolicyRsrc result = (PolicyRsrc) cirrasDataSyncService.getPolicy(
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

		logger.debug(">getPolicy");
		return response;
	}	

	@Override
	public Response deletePolicy(String policyId) {
		logger.debug("<deletePolicy");

		Response response = null;
		
		logRequest();
		
		if(!hasAuthority(Scopes.DELETE_SYNC_UNDERWRITING)) {
			return Response.status(Status.FORBIDDEN).build();
		}
			
		try {
			PolicyRsrc resource = (PolicyRsrc) cirrasDataSyncService.getPolicy(
					toInteger(policyId),
					getFactoryContext(), 
					getWebAdeAuthentication());
			
			if(resource != null) {
				cirrasDataSyncService.deletePolicy(toInteger(policyId), getFactoryContext(), getWebAdeAuthentication());
			}
			response = Response.status(204).build();
		} catch (Throwable t) {
			response = getInternalServerErrorResponse(t);
		}
		
		logResponse(response);

		logger.debug(">deletePolicy " + response);
		return response;
	}	

	private void logResource(PolicyRsrc resource, String prefix) {

		StringBuilder msgBldr = new StringBuilder();
		
		if ( resource == null ) {
			msgBldr.append(prefix)
		           .append(": Received Resource is null. ")
		    ;
			
		} else {		
			// Note that only non-sensitive data can be logged.
			msgBldr.append(prefix)
			       .append(": Received Resource: ")
			       .append("Type: PolicyRsrc")
			       .append(", PolicyId: ")
			       .append(resource.getPolicyId())
			       .append(", PolicyNumber: ")
			       .append(resource.getPolicyNumber())
			       .append(", Transaction Type: ")
			       .append(resource.getTransactionType())
			;
		}
		
		logger.info(msgBldr.toString());
	}
	
}
