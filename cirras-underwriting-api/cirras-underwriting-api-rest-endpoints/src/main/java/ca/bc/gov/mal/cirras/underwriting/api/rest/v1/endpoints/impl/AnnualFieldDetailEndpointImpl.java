package ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints.impl;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import ca.bc.gov.nrs.wfone.common.rest.endpoints.BaseEndpointsImpl;
import ca.bc.gov.nrs.wfone.common.service.api.NotFoundException;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints.AnnualFieldDetailEndpoint;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints.security.Scopes;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.AnnualFieldDetailRsrc;
import ca.bc.gov.mal.cirras.underwriting.service.api.v1.LandDataSyncService;

public class AnnualFieldDetailEndpointImpl extends BaseEndpointsImpl implements AnnualFieldDetailEndpoint {
	
	private static final Logger logger = LoggerFactory.getLogger(AnnualFieldDetailEndpointImpl.class);
	
	@Autowired
	private LandDataSyncService landDataSyncService; 

	@Override
	public Response synchronizeAnnualFieldDetail(AnnualFieldDetailRsrc resource) {
		logger.debug("<synchronizeAnnualFieldDetail");
		Response response = null;
		
		logRequest();
		
		if(!hasAuthority(Scopes.UPDATE_SYNC_UNDERWRITING)) {
			return Response.status(Status.FORBIDDEN).build();
		}

		try {
			
			landDataSyncService.synchronizeAnnualFieldDetail(
					resource, 
					getFactoryContext(), 
					getWebAdeAuthentication());

			response = Response.status(204).build();
		} catch (Throwable t) {
			response = getInternalServerErrorResponse(t);
		}
		
		logResponse(response);

		logger.debug(">synchronizeAnnualFieldDetail " + response);
		return response;
	}
	
	
	
	@Override
	public Response getAnnualFieldDetail(String annualFieldDetailId) {
		logger.debug("<getAnnualFieldDetail");

		Response response = null;
		
		logRequest();
		
		if(!hasAuthority(Scopes.GET_LAND)) {
			return Response.status(Status.FORBIDDEN).build();
		}
		
		try {
			
			AnnualFieldDetailRsrc result = (AnnualFieldDetailRsrc) landDataSyncService.getAnnualFieldDetail(
					toInteger(annualFieldDetailId),
					getFactoryContext(), 
					getWebAdeAuthentication());

			response = Response.ok(result).tag(result.getUnquotedETag()).build();

		} catch (NotFoundException e) {
			response = Response.status(Status.NOT_FOUND).build();
			
		} catch (Throwable t) {
			response = getInternalServerErrorResponse(t);
		}
		
		logResponse(response);

		logger.debug(">getAnnualFieldDetail");
		return response;
	}	

	@Override
	public Response deleteAnnualFieldDetail(String annualFieldDetailId) {
		logger.debug("<deleteAnnualFieldDetail");

		Response response = null;
		
		logRequest();
		
		if(!hasAuthority(Scopes.DELETE_SYNC_UNDERWRITING)) {
			return Response.status(Status.FORBIDDEN).build();
		}
			
		try {
			AnnualFieldDetailRsrc resource = (AnnualFieldDetailRsrc) landDataSyncService.getAnnualFieldDetail(
					toInteger(annualFieldDetailId),
					getFactoryContext(), 
					getWebAdeAuthentication());
			
			if(resource != null) {
				landDataSyncService.deleteAnnualFieldDetail(toInteger(annualFieldDetailId), getFactoryContext(), getWebAdeAuthentication());
			}
			response = Response.status(204).build();
		} catch (Throwable t) {
			response = getInternalServerErrorResponse(t);
		}
		
		logResponse(response);

		logger.debug(">deleteAnnualFieldDetail " + response);
		return response;
	}	

}
