package ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints.impl;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import ca.bc.gov.nrs.wfone.common.rest.endpoints.BaseEndpointsImpl;
import ca.bc.gov.nrs.wfone.common.service.api.NotFoundException;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints.SyncCommodityVarietyEndpoint;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints.security.Scopes;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.SyncCommodityVarietyRsrc;
import ca.bc.gov.mal.cirras.underwriting.service.api.v1.CirrasDataSyncService;

public class SyncCommodityVarietyEndpointImpl extends BaseEndpointsImpl implements SyncCommodityVarietyEndpoint {
	
	private static final Logger logger = LoggerFactory.getLogger(SyncCommodityVarietyEndpointImpl.class);
	
	@Autowired
	private CirrasDataSyncService cirrasDataSyncService; 

	@Override
	public Response synchronizeCommodityVariety(SyncCommodityVarietyRsrc resource) {
		logger.debug("<synchronizeCommodityVariety");
		Response response = null;
		
		logRequest();
		logResource(resource, "synchronizeCommodityVariety");
		
		if(!hasAuthority(Scopes.UPDATE_SYNC_UNDERWRITING)) {
			return Response.status(Status.FORBIDDEN).build();
		}

		try {
			
			cirrasDataSyncService.synchronizeCommodityVariety(
					resource, 
					getFactoryContext(), 
					getWebAdeAuthentication());

			response = Response.status(200).build();
		} catch (Throwable t) {
			response = getInternalServerErrorResponse(t);
		}
		
		logResponse(response);

		logger.debug(">synchronizeCommodityVariety " + response);
		return response;
	}

	@Override
	public Response deleteSyncCommodityVariety(String crptId) {
		logger.debug("<deleteSyncCommodityVariety");

		Response response = null;
		
		logRequest();
		
		if(!hasAuthority(Scopes.DELETE_SYNC_UNDERWRITING)) {
			return Response.status(Status.FORBIDDEN).build();
		}
			
		try {
			SyncCommodityVarietyRsrc resource = (SyncCommodityVarietyRsrc) cirrasDataSyncService.getSyncCommodityVariety(
					toInteger(crptId),
					getFactoryContext(), 
					getWebAdeAuthentication());
			
			if(resource != null) {
				cirrasDataSyncService.deleteSyncCommodityVariety(toInteger(crptId), getFactoryContext(), getWebAdeAuthentication());
			}
			response = Response.status(204).build();
		} catch (NotFoundException e) {
			response = Response.status(Status.NOT_FOUND).build();
		} catch (Throwable t) {
			response = getInternalServerErrorResponse(t);
		}
		
		logResponse(response);

		logger.debug(">deleteSyncCommodityVariety " + response);
		return response;
	}
	
	@Override
	public Response getSyncCommodityVariety(String crptId) {
		logger.debug("<getSyncCommodityVariety");

		Response response = null;
		
		logRequest();
		
		if(!hasAuthority(Scopes.GET_COMMODITY_VARIETY)) {
			return Response.status(Status.FORBIDDEN).build();
		}
		
		try {
			
			SyncCommodityVarietyRsrc result = (SyncCommodityVarietyRsrc) cirrasDataSyncService.getSyncCommodityVariety(
					toInteger(crptId),
					getFactoryContext(), 
					getWebAdeAuthentication());
			
			response = Response.ok(result).tag(result.getUnquotedETag()).build();

		} catch (NotFoundException e) {
			response = Response.status(Status.NOT_FOUND).build();
			
		} catch (Throwable t) {
			response = getInternalServerErrorResponse(t);
		}
		
		logResponse(response);

		logger.debug(">getSyncCommodityVariety");
		return response;
	}
	
	private void logResource(SyncCommodityVarietyRsrc resource, String prefix) {

		StringBuilder msgBldr = new StringBuilder();
		
		if ( resource == null ) {
			msgBldr.append(prefix)
		           .append(": Received Resource is null. ")
		    ;
			
		} else {		
			msgBldr.append(prefix)
			       .append(": Received Resource: ")
			       .append("Type: SyncCommodityVarietyRsrc")
			       .append(", CropId: ")
			       .append(resource.getCropId())
			       .append(", CropName: ")
			       .append(resource.getCropName())
			       .append(", ParentCropId: ")
			       .append(resource.getParentCropId())
			       .append(", Transaction Type: ")
			       .append(resource.getTransactionType())
			;
		}
		
		logger.info(msgBldr.toString());
	}
	
}
