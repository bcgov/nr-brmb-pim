package ca.bc.gov.mal.cirras.underwriting.controllers.impl;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import ca.bc.gov.nrs.wfone.common.rest.endpoints.BaseEndpointsImpl;
import ca.bc.gov.nrs.wfone.common.service.api.NotFoundException;
import ca.bc.gov.mal.cirras.underwriting.controllers.SyncCommodityTypeCodeEndpoint;
import ca.bc.gov.mal.cirras.underwriting.controllers.scopes.Scopes;
import ca.bc.gov.mal.cirras.underwriting.data.resources.SyncCommodityTypeCodeRsrc;
import ca.bc.gov.mal.cirras.underwriting.services.CirrasDataSyncService;

public class SyncCommodityTypeCodeEndpointImpl extends BaseEndpointsImpl implements SyncCommodityTypeCodeEndpoint {
	
	private static final Logger logger = LoggerFactory.getLogger(SyncCommodityTypeCodeEndpointImpl.class);
	
	@Autowired
	private CirrasDataSyncService cirrasDataSyncService; 

	@Override
	public Response synchronizeCommodityTypeCode(SyncCommodityTypeCodeRsrc resource) {
		logger.debug("<synchronizeCommodityTypeCode");
		Response response = null;
		
		logRequest();
		logResource(resource, "synchronizeCommodityTypeCode");
		
		if(!hasAuthority(Scopes.UPDATE_SYNC_UNDERWRITING)) {
			return Response.status(Status.FORBIDDEN).build();
		}

		try {
			
			cirrasDataSyncService.synchronizeCommodityTypeCode(
					resource, 
					getFactoryContext(), 
					getWebAdeAuthentication());

			response = Response.status(204).build();
		} catch (Throwable t) {
			response = getInternalServerErrorResponse(t);
		}
		
		logResponse(response);

		logger.debug(">synchronizeCommodityTypeCode " + response);
		return response;
	}
	
	
	
	@Override
	public Response getCommodityTypeCode(String commodityTypeCode) {
		logger.debug("<getCommodityTypeCode");

		Response response = null;
		
		logRequest();
		
		if(!hasAuthority(Scopes.GET_COMMODITY_TYPE_CODE)) {
			return Response.status(Status.FORBIDDEN).build();
		}
		
		try {
			
			SyncCommodityTypeCodeRsrc result = (SyncCommodityTypeCodeRsrc) cirrasDataSyncService.getCommodityTypeCode(
					commodityTypeCode,
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

		logger.debug(">getCommodityTypeCode");
		return response;
	}	

	@Override
	public Response deleteCommodityTypeCode(String commodityTypeCode) {
		logger.debug("<deleteCommodityTypeCode");

		Response response = null;
		
		logRequest();
		
		if(!hasAuthority(Scopes.DELETE_SYNC_UNDERWRITING)) {
			return Response.status(Status.FORBIDDEN).build();
		}
			
		try {
			SyncCommodityTypeCodeRsrc resource = (SyncCommodityTypeCodeRsrc) cirrasDataSyncService.getCommodityTypeCode(
					commodityTypeCode,
					getFactoryContext(), 
					getWebAdeAuthentication());
			
			if(resource != null) {
				cirrasDataSyncService.deleteCommodityTypeCode(
						commodityTypeCode,
						getFactoryContext(), 
						getWebAdeAuthentication());
			}
			response = Response.status(204).build();
		} catch (Throwable t) {
			response = getInternalServerErrorResponse(t);
		}
		
		logResponse(response);

		logger.debug(">deleteCommodityTypeCode " + response);
		return response;
	}	

	private void logResource(SyncCommodityTypeCodeRsrc resource, String prefix) {

		StringBuilder msgBldr = new StringBuilder();
		
		if ( resource == null ) {
			msgBldr.append(prefix)
		           .append(": Received Resource is null. ")
		    ;
			
		} else {		
			msgBldr.append(prefix)
			       .append(": Received Resource: ")
			       .append("Type: SyncCommodityTypeCodeRsrc")
			       .append(", CommodityTypeCode: ")
			       .append(resource.getCommodityTypeCode())
			       .append(", CropCommodityId: ")
			       .append(resource.getCropCommodityId())
			       .append(", Description: ")
			       .append(resource.getDescription())
			       .append(", Transaction Type: ")
			       .append(resource.getTransactionType())
			;
		}
		
		logger.info(msgBldr.toString());
	}
	
	
}
