package ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints.impl;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import ca.bc.gov.nrs.wfone.common.rest.endpoints.BaseEndpointsImpl;
import ca.bc.gov.nrs.wfone.common.service.api.NotFoundException;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints.ContactPhoneEndpoint;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints.security.Scopes;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.ContactPhoneRsrc;
import ca.bc.gov.mal.cirras.underwriting.service.api.v1.CirrasDataSyncService;

public class ContactPhoneEndpointImpl extends BaseEndpointsImpl implements ContactPhoneEndpoint {
	
	private static final Logger logger = LoggerFactory.getLogger(ContactPhoneEndpointImpl.class);
	
	@Autowired
	private CirrasDataSyncService cirrasDataSyncService; 

	@Override
	public Response synchronizeContactPhone(ContactPhoneRsrc resource) {
		logger.debug("<synchronizeContactPhone");
		Response response = null;
		
		logRequest();
		logResource(resource, "synchronizeContactPhone");

		
		if(!hasAuthority(Scopes.UPDATE_SYNC_UNDERWRITING)) {
			return Response.status(Status.FORBIDDEN).build();
		}

		try {
			
			cirrasDataSyncService.synchronizeContactPhone(
					resource, 
					getFactoryContext(), 
					getWebAdeAuthentication());

			response = Response.status(204).build();
		} catch (Throwable t) {
			response = getInternalServerErrorResponse(t);
		}
		
		logResponse(response);

		logger.debug(">synchronizeContactPhone " + response);
		return response;
	}
	
	
	
	@Override
	public Response getContactPhone(String contactPhoneId) {
		logger.debug("<getContactPhone");

		Response response = null;
		
		logRequest();
		
		if(!hasAuthority(Scopes.GET_CONTACT_PHONE)) {
			return Response.status(Status.FORBIDDEN).build();
		}
		
		try {
			
			ContactPhoneRsrc result = (ContactPhoneRsrc) cirrasDataSyncService.getContactPhone(
					toInteger(contactPhoneId),
					getFactoryContext(), 
					getWebAdeAuthentication());
			if (result == null) {
				response = Response.status(Status.NOT_FOUND).build();
			} else {
				response = Response.ok(result).tag(result.getUnquotedETag()).build();
			}

		} catch (NotFoundException e) {
			response = Response.status(Status.NOT_FOUND).build();
			
		} catch (Throwable t) {
			response = getInternalServerErrorResponse(t);
		}
		
		logResponse(response);

		logger.debug(">getContactPhone");
		return response;
	}	

	@Override
	public Response deleteContactPhone(String contactPhoneId) {
		logger.debug("<deleteContactPhone");

		Response response = null;
		
		logRequest();
		
		if(!hasAuthority(Scopes.DELETE_SYNC_UNDERWRITING)) {
			return Response.status(Status.FORBIDDEN).build();
		}
			
		try {
			ContactPhoneRsrc resource = (ContactPhoneRsrc) cirrasDataSyncService.getContactPhone(
					toInteger(contactPhoneId),
					getFactoryContext(), 
					getWebAdeAuthentication());
			
			if(resource != null) {
				cirrasDataSyncService.deleteContactPhone(toInteger(contactPhoneId), getFactoryContext(), getWebAdeAuthentication());
			}
			response = Response.status(204).build();
		} catch (Throwable t) {
			response = getInternalServerErrorResponse(t);
		}
		
		logResponse(response);

		logger.debug(">deleteContactPhone " + response);
		return response;
	}	

	private void logResource(ContactPhoneRsrc resource, String prefix) {

		StringBuilder msgBldr = new StringBuilder();
		
		if ( resource == null ) {
			msgBldr.append(prefix)
		           .append(": Received Resource is null. ")
		    ;
			
		} else {		
			// Note that only non-sensitive data can be logged.
			msgBldr.append(prefix)
			       .append(": Received Resource: ")
			       .append("Type: ContactPhoneRsrc")
			       .append(", ContactPhoneId: ")
			       .append(resource.getContactPhoneId())
			       .append(", ContactId: ")
			       .append(resource.getContactId())
			       .append(", Transaction Type: ")
			       .append(resource.getTransactionType())
			;
		}
		
		logger.info(msgBldr.toString());
	}
	
	
}
