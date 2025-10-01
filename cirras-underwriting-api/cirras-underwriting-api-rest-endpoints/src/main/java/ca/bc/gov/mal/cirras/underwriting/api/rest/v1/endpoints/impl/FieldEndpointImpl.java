package ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints.impl;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import ca.bc.gov.nrs.wfone.common.rest.endpoints.BaseEndpointsImpl;
import ca.bc.gov.nrs.wfone.common.service.api.NotFoundException;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints.FieldEndpoint;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints.security.Scopes;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.FieldRsrc;
import ca.bc.gov.mal.cirras.underwriting.service.api.v1.LandDataSyncService;

public class FieldEndpointImpl extends BaseEndpointsImpl implements FieldEndpoint {
	
	private static final Logger logger = LoggerFactory.getLogger(FieldEndpointImpl.class);
	
	@Autowired
	private LandDataSyncService landDataSyncService; 

	@Override
	public Response synchronizeField(FieldRsrc resource) {
		logger.debug("<synchronizeField");
		Response response = null;
		
		logRequest();
		
		if(!hasAuthority(Scopes.UPDATE_SYNC_UNDERWRITING)) {
			return Response.status(Status.FORBIDDEN).build();
		}

		try {
			
			landDataSyncService.synchronizeField(
					resource, 
					getFactoryContext(), 
					getWebAdeAuthentication());

			response = Response.status(204).build();
		} catch (Throwable t) {
			response = getInternalServerErrorResponse(t);
		}
		
		logResponse(response);

		logger.debug(">synchronizeField " + response);
		return response;
	}
	
	
	
	@Override
	public Response getField(String fieldId) {
		logger.debug("<getField");

		Response response = null;
		
		logRequest();
		
		if(!hasAuthority(Scopes.GET_LAND)) {
			return Response.status(Status.FORBIDDEN).build();
		}
		
		try {
			
			FieldRsrc result = (FieldRsrc) landDataSyncService.getField(
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

		logger.debug(">getField");
		return response;
	}	

	@Override
	public Response deleteField(String fieldId) {
		logger.debug("<deleteField");

		Response response = null;
		
		logRequest();
		
		if(!hasAuthority(Scopes.DELETE_SYNC_UNDERWRITING)) {
			return Response.status(Status.FORBIDDEN).build();
		}
			
		try {
			FieldRsrc resource = (FieldRsrc) landDataSyncService.getField(
					toInteger(fieldId),
					getFactoryContext(), 
					getWebAdeAuthentication());
			
			if(resource != null) {
				landDataSyncService.deleteField(toInteger(fieldId), getFactoryContext(), getWebAdeAuthentication());
			}
			response = Response.status(204).build();
		} catch (Throwable t) {
			response = getInternalServerErrorResponse(t);
		}
		
		logResponse(response);

		logger.debug(">deleteField " + response);
		return response;
	}	

}
