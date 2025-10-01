package ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints.impl;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import ca.bc.gov.nrs.wfone.common.rest.endpoints.BaseEndpointsImpl;
import ca.bc.gov.nrs.wfone.common.service.api.NotFoundException;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints.GrowerContactEndpoint;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints.security.Scopes;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.GrowerContactRsrc;
import ca.bc.gov.mal.cirras.underwriting.service.api.v1.CirrasDataSyncService;

public class GrowerContactEndpointImpl extends BaseEndpointsImpl implements GrowerContactEndpoint {
	
	private static final Logger logger = LoggerFactory.getLogger(GrowerContactEndpointImpl.class);
	
	@Autowired
	private CirrasDataSyncService cirrasDataSyncService; 

	@Override
	public Response synchronizeGrowerContact(GrowerContactRsrc resource) {
		logger.debug("<synchronizeGrowerContact");
		Response response = null;
		
		logRequest();
		logResource(resource, "synchronizeGrowerContact");

		
		if(!hasAuthority(Scopes.UPDATE_SYNC_UNDERWRITING)) {
			return Response.status(Status.FORBIDDEN).build();
		}

		try {
			
			cirrasDataSyncService.synchronizeGrowerContact(
					resource, 
					getFactoryContext(), 
					getWebAdeAuthentication());

			response = Response.status(204).build();
		} catch (Throwable t) {
			response = getInternalServerErrorResponse(t);
		}
		
		logResponse(response);

		logger.debug(">synchronizeGrowerContact " + response);
		return response;
	}
	
	
	
	@Override
	public Response getGrowerContact(String growerContactId) {
		logger.debug("<getGrowerContact");

		Response response = null;
		
		logRequest();
		
		if(!hasAuthority(Scopes.GET_GROWER_CONTACT)) {
			return Response.status(Status.FORBIDDEN).build();
		}
		
		try {
			
			GrowerContactRsrc result = (GrowerContactRsrc) cirrasDataSyncService.getGrowerContact(
					toInteger(growerContactId),
					getFactoryContext(), 
					getWebAdeAuthentication());

			response = Response.ok(result).tag(result.getUnquotedETag()).build();

		} catch (NotFoundException e) {
			response = Response.status(Status.NOT_FOUND).build();
			
		} catch (Throwable t) {
			response = getInternalServerErrorResponse(t);
		}
		
		logResponse(response);

		logger.debug(">getGrowerContact");
		return response;
	}	

	@Override
	public Response deleteGrowerContact(String growerContactId) {
		logger.debug("<deleteGrowerContact");

		Response response = null;
		
		logRequest();
		
		if(!hasAuthority(Scopes.DELETE_SYNC_UNDERWRITING)) {
			return Response.status(Status.FORBIDDEN).build();
		}
			
		try {
			GrowerContactRsrc resource = (GrowerContactRsrc) cirrasDataSyncService.getGrowerContact(
					toInteger(growerContactId),
					getFactoryContext(), 
					getWebAdeAuthentication());
			
			if(resource != null) {
				cirrasDataSyncService.deleteGrowerContact(toInteger(growerContactId), getFactoryContext(), getWebAdeAuthentication());
			}
			response = Response.status(204).build();
		} catch (Throwable t) {
			response = getInternalServerErrorResponse(t);
		}
		
		logResponse(response);

		logger.debug(">deleteGrowerContact " + response);
		return response;
	}	

	private void logResource(GrowerContactRsrc resource, String prefix) {

		StringBuilder msgBldr = new StringBuilder();
		
		if ( resource == null ) {
			msgBldr.append(prefix)
		           .append(": Received Resource is null. ")
		    ;
			
		} else {		
			// Note that only non-sensitive data can be logged.
			msgBldr.append(prefix)
			       .append(": Received Resource: ")
			       .append("Type: GrowerContactRsrc")
			       .append(", GrowerContactId: ")
			       .append(resource.getGrowerContactId())
			       .append(", GrowerId: ")
			       .append(resource.getGrowerId())
			       .append(", ContactId: ")
			       .append(resource.getContactId())
			       .append(", Transaction Type: ")
			       .append(resource.getTransactionType())
			;
		}
		
		logger.info(msgBldr.toString());
	}
	
	
}
