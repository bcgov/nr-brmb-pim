package ca.bc.gov.mal.cirras.underwriting.controllers.impl;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import ca.bc.gov.nrs.wfone.common.rest.endpoints.BaseEndpointsImpl;
import ca.bc.gov.nrs.wfone.common.service.api.NotFoundException;
import ca.bc.gov.mal.cirras.underwriting.controllers.ContactEmailEndpoint;
import ca.bc.gov.mal.cirras.underwriting.controllers.scopes.Scopes;
import ca.bc.gov.mal.cirras.underwriting.data.resources.ContactEmailRsrc;
import ca.bc.gov.mal.cirras.underwriting.services.CirrasDataSyncService;

public class ContactEmailEndpointImpl extends BaseEndpointsImpl implements ContactEmailEndpoint {
	
	private static final Logger logger = LoggerFactory.getLogger(ContactEmailEndpointImpl.class);
	
	@Autowired
	private CirrasDataSyncService cirrasDataSyncService; 

	@Override
	public Response synchronizeContactEmail(ContactEmailRsrc resource) {
		logger.debug("<synchronizeContactEmail");
		Response response = null;
		
		logRequest();
		logResource(resource, "synchronizeContactEmail");

		
		if(!hasAuthority(Scopes.UPDATE_SYNC_UNDERWRITING)) {
			return Response.status(Status.FORBIDDEN).build();
		}

		try {
			
			cirrasDataSyncService.synchronizeContactEmail(
					resource, 
					getFactoryContext(), 
					getWebAdeAuthentication());

			response = Response.status(204).build();
		} catch (Throwable t) {
			response = getInternalServerErrorResponse(t);
		}
		
		logResponse(response);

		logger.debug(">synchronizeContactEmail " + response);
		return response;
	}
	
	
	
	@Override
	public Response getContactEmail(String contactEmailId) {
		logger.debug("<getContactEmail");

		Response response = null;
		
		logRequest();
		
		if(!hasAuthority(Scopes.GET_CONTACT_EMAIL)) {
			return Response.status(Status.FORBIDDEN).build();
		}
		
		try {
			
			ContactEmailRsrc result = (ContactEmailRsrc) cirrasDataSyncService.getContactEmail(
					toInteger(contactEmailId),
					getFactoryContext(), 
					getWebAdeAuthentication());

			response = Response.ok(result).tag(result.getUnquotedETag()).build();

		} catch (NotFoundException e) {
			response = Response.status(Status.NOT_FOUND).build();
			
		} catch (Throwable t) {
			response = getInternalServerErrorResponse(t);
		}
		
		logResponse(response);

		logger.debug(">getContactEmail");
		return response;
	}	

	@Override
	public Response deleteContactEmail(String contactEmailId) {
		logger.debug("<deleteContactEmail");

		Response response = null;
		
		logRequest();
		
		if(!hasAuthority(Scopes.DELETE_SYNC_UNDERWRITING)) {
			return Response.status(Status.FORBIDDEN).build();
		}
			
		try {
			ContactEmailRsrc resource = (ContactEmailRsrc) cirrasDataSyncService.getContactEmail(
					toInteger(contactEmailId),
					getFactoryContext(), 
					getWebAdeAuthentication());
			
			if(resource != null) {
				cirrasDataSyncService.deleteContactEmail(toInteger(contactEmailId), getFactoryContext(), getWebAdeAuthentication());
			}
			response = Response.status(204).build();
		} catch (Throwable t) {
			response = getInternalServerErrorResponse(t);
		}
		
		logResponse(response);

		logger.debug(">deleteContactEmail " + response);
		return response;
	}	

	private void logResource(ContactEmailRsrc resource, String prefix) {

		StringBuilder msgBldr = new StringBuilder();
		
		if ( resource == null ) {
			msgBldr.append(prefix)
		           .append(": Received Resource is null. ")
		    ;
			
		} else {		
			// Note that only non-sensitive data can be logged.
			msgBldr.append(prefix)
			       .append(": Received Resource: ")
			       .append("Type: ContactEmailRsrc")
			       .append(", ContactEmailId: ")
			       .append(resource.getContactEmailId())
			       .append(", ContactId: ")
			       .append(resource.getContactId())
			       .append(", Transaction Type: ")
			       .append(resource.getTransactionType())
			;
		}
		
		logger.info(msgBldr.toString());
	}
	
	
}
