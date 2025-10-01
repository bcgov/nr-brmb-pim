package ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints.impl;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import ca.bc.gov.nrs.wfone.common.rest.endpoints.BaseEndpointsImpl;
import ca.bc.gov.nrs.wfone.common.service.api.NotFoundException;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints.LegalLandSyncEndpoint;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints.security.Scopes;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.LegalLandRsrc;
import ca.bc.gov.mal.cirras.underwriting.service.api.v1.LandDataSyncService;

public class LegalLandSyncEndpointImpl extends BaseEndpointsImpl implements LegalLandSyncEndpoint {
	
	private static final Logger logger = LoggerFactory.getLogger(LegalLandSyncEndpointImpl.class);
	
	@Autowired
	private LandDataSyncService landDataSyncService; 

	@Override
	public Response synchronizeLegalLand(LegalLandRsrc resource) {
		logger.debug("<synchronizeLegalLand");
		Response response = null;
		
		logRequest();
		
		if(!hasAuthority(Scopes.UPDATE_SYNC_UNDERWRITING)) {
			return Response.status(Status.FORBIDDEN).build();
		}

		try {
			
			landDataSyncService.synchronizeLegalLand(
					resource, 
					getFactoryContext(), 
					getWebAdeAuthentication());

			response = Response.status(204).build();
		} catch (Throwable t) {
			response = getInternalServerErrorResponse(t);
		}
		
		logResponse(response);

		logger.debug(">synchronizeLegalLand " + response);
		return response;
	}

	@Override
	public Response deleteLegalLandSync(String legalLandId) {
		logger.debug("<deleteLegalLandSync");

		Response response = null;
		
		logRequest();
		
		if(!hasAuthority(Scopes.DELETE_SYNC_UNDERWRITING)) {
			return Response.status(Status.FORBIDDEN).build();
		}
			
		try {
			LegalLandRsrc resource = (LegalLandRsrc) landDataSyncService.getLegalLand(
					toInteger(legalLandId),
					getFactoryContext(), 
					getWebAdeAuthentication());
			
			if(resource != null) {
				landDataSyncService.deleteLegalLand(toInteger(legalLandId), getFactoryContext(), getWebAdeAuthentication());
			}
			response = Response.status(204).build();
		} catch (Throwable t) {
			response = getInternalServerErrorResponse(t);
		}
		
		logResponse(response);

		logger.debug(">deleteLegalLandSync " + response);
		return response;
	}	

}
