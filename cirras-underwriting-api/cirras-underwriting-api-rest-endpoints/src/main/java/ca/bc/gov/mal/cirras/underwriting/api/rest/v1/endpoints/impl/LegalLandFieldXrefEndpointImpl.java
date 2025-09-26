package ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints.impl;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import ca.bc.gov.nrs.wfone.common.rest.endpoints.BaseEndpointsImpl;
import ca.bc.gov.nrs.wfone.common.service.api.NotFoundException;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints.LegalLandFieldXrefEndpoint;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints.security.Scopes;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.LegalLandFieldXrefRsrc;
import ca.bc.gov.mal.cirras.underwriting.service.api.v1.LandDataSyncService;

public class LegalLandFieldXrefEndpointImpl extends BaseEndpointsImpl implements LegalLandFieldXrefEndpoint {
	
	private static final Logger logger = LoggerFactory.getLogger(LegalLandFieldXrefEndpointImpl.class);
	
	@Autowired
	private LandDataSyncService landDataSyncService; 

	@Override
	public Response synchronizeLegalLandFieldXref(LegalLandFieldXrefRsrc resource) {
		logger.debug("<synchronizeLegalLandFieldXref");
		Response response = null;
		
		logRequest();
		
		if(!hasAuthority(Scopes.UPDATE_SYNC_UNDERWRITING)) {
			return Response.status(Status.FORBIDDEN).build();
		}

		try {
			
			landDataSyncService.synchronizeLegalLandFieldXref(
					resource, 
					getFactoryContext(), 
					getWebAdeAuthentication());

			response = Response.status(204).build();
		} catch (Throwable t) {
			response = getInternalServerErrorResponse(t);
		}
		
		logResponse(response);

		logger.debug(">synchronizeLegalLandFieldXref " + response);
		return response;
	}
	
	
	
	@Override
	public Response getLegalLandFieldXref(String legalLandId, String fieldId) {
		logger.debug("<getLegalLandFieldXref");

		Response response = null;
		
		logRequest();
		
		if(!hasAuthority(Scopes.GET_LAND)) {
			return Response.status(Status.FORBIDDEN).build();
		}
		
		try {
			
			LegalLandFieldXrefRsrc result = (LegalLandFieldXrefRsrc) landDataSyncService.getLegalLandFieldXref(
					toInteger(legalLandId),
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

		logger.debug(">getLegalLandFieldXref");
		return response;
	}	

	@Override
	public Response deleteLegalLandFieldXref(String legalLandId, String fieldId) {
		logger.debug("<deleteLegalLandFieldXref");

		Response response = null;
		
		logRequest();
		
		if(!hasAuthority(Scopes.DELETE_SYNC_UNDERWRITING)) {
			return Response.status(Status.FORBIDDEN).build();
		}
			
		try {
			LegalLandFieldXrefRsrc resource = (LegalLandFieldXrefRsrc) landDataSyncService.getLegalLandFieldXref(
					toInteger(legalLandId),
					toInteger(fieldId),
					getFactoryContext(), 
					getWebAdeAuthentication());
			
			if(resource != null) {
				landDataSyncService.deleteLegalLandFieldXref(toInteger(legalLandId), toInteger(fieldId), getFactoryContext(), getWebAdeAuthentication());
			}
			response = Response.status(204).build();
		} catch (Throwable t) {
			response = getInternalServerErrorResponse(t);
		}
		
		logResponse(response);

		logger.debug(">deleteLegalLandFieldXref " + response);
		return response;
	}	
	
	

}
