package ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints.impl;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import ca.bc.gov.nrs.wfone.common.rest.endpoints.BaseEndpointsImpl;
import ca.bc.gov.nrs.wfone.common.service.api.NotFoundException;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints.GrowerEndpoint;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints.security.Scopes;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.GrowerRsrc;
import ca.bc.gov.mal.cirras.underwriting.service.api.v1.CirrasDataSyncService;

public class GrowerEndpointImpl extends BaseEndpointsImpl implements GrowerEndpoint {
	
	private static final Logger logger = LoggerFactory.getLogger(GrowerEndpointImpl.class);
	
	@Autowired
	private CirrasDataSyncService cirrasDataSyncService; 

	@Override
	public Response synchronizeGrower(GrowerRsrc resource) {
		logger.debug("<synchronizeGrower");
		Response response = null;
		
		logRequest();
		logResource(resource, "synchronizeGrower");
		
		if(!hasAuthority(Scopes.UPDATE_SYNC_UNDERWRITING)) {
			return Response.status(Status.FORBIDDEN).build();
		}

		try {
			
			cirrasDataSyncService.synchronizeGrower(
					resource, 
					getFactoryContext(), 
					getWebAdeAuthentication());

			response = Response.status(204).build();
		} catch (Throwable t) {
			response = getInternalServerErrorResponse(t);
		}
		
		logResponse(response);

		logger.debug(">synchronizeGrower " + response);
		return response;
	}
	
	
	
	@Override
	public Response getGrower(String growerId) {
		logger.debug("<getGrower");

		Response response = null;
		
		logRequest();
		
		if(!hasAuthority(Scopes.GET_GROWER)) {
			return Response.status(Status.FORBIDDEN).build();
		}
		
		try {
			
			GrowerRsrc result = (GrowerRsrc) cirrasDataSyncService.getGrower(
					toInteger(growerId),
					getFactoryContext(), 
					getWebAdeAuthentication());

			response = Response.ok(result).tag(result.getUnquotedETag()).build();

		} catch (NotFoundException e) {
			response = Response.status(Status.NOT_FOUND).build();
			
		} catch (Throwable t) {
			response = getInternalServerErrorResponse(t);
		}
		
		logResponse(response);

		logger.debug(">getGrower");
		return response;
	}	

	@Override
	public Response deleteGrower(String growerId) {
		logger.debug("<deleteGrower");

		Response response = null;
		
		logRequest();
		
		if(!hasAuthority(Scopes.DELETE_SYNC_UNDERWRITING)) {
			return Response.status(Status.FORBIDDEN).build();
		}
			
		try {
			GrowerRsrc resource = (GrowerRsrc) cirrasDataSyncService.getGrower(
					toInteger(growerId),
					getFactoryContext(), 
					getWebAdeAuthentication());
			
			if(resource != null) {
				cirrasDataSyncService.deleteGrower(toInteger(growerId), getFactoryContext(), getWebAdeAuthentication());
			}
			response = Response.status(204).build();
		} catch (Throwable t) {
			response = getInternalServerErrorResponse(t);
		}
		
		logResponse(response);

		logger.debug(">deleteGrower " + response);
		return response;
	}	

	private void logResource(GrowerRsrc resource, String prefix) {

		StringBuilder msgBldr = new StringBuilder();
		
		if ( resource == null ) {
			msgBldr.append(prefix)
		           .append(": Received Resource is null. ")
		    ;
			
		} else {		
			// Note that only non-sensitive data can be logged.
			msgBldr.append(prefix)
			       .append(": Received Resource: ")
			       .append("Type: GrowerRsrc")
			       .append(", GrowerId: ")
			       .append(resource.getGrowerId())
			       .append(", GrowerNumber: ")
			       .append(resource.getGrowerNumber())
			       .append(", Transaction Type: ")
			       .append(resource.getTransactionType())
			;
		}
		
		logger.info(msgBldr.toString());
	}
	
	
}
