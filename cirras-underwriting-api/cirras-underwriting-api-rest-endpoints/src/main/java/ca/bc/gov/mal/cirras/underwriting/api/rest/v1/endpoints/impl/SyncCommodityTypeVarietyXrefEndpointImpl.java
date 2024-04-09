package ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints.impl;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import ca.bc.gov.nrs.wfone.common.rest.endpoints.BaseEndpointsImpl;
import ca.bc.gov.nrs.wfone.common.service.api.NotFoundException;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints.SyncCommodityTypeVarietyXrefEndpoint;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints.security.Scopes;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.SyncCommodityTypeVarietyXrefRsrc;
import ca.bc.gov.mal.cirras.underwriting.service.api.v1.CirrasDataSyncService;

public class SyncCommodityTypeVarietyXrefEndpointImpl extends BaseEndpointsImpl implements SyncCommodityTypeVarietyXrefEndpoint {
	
	private static final Logger logger = LoggerFactory.getLogger(SyncCommodityTypeVarietyXrefEndpointImpl.class);
	
	@Autowired
	private CirrasDataSyncService cirrasDataSyncService; 

	@Override
	public Response synchronizeCommodityTypeVarietyXref(SyncCommodityTypeVarietyXrefRsrc resource) {
		logger.debug("<synchronizeCommodityTypeVarietyXref");
		Response response = null;
		
		logRequest();
		logResource(resource, "synchronizeCommodityTypeVarietyXref");
		
		if(!hasAuthority(Scopes.UPDATE_SYNC_UNDERWRITING)) {
			return Response.status(Status.FORBIDDEN).build();
		}

		try {
			
			cirrasDataSyncService.synchronizeCommodityTypeVarietyXref(
					resource, 
					getFactoryContext(), 
					getWebAdeAuthentication());

			response = Response.status(204).build();
		} catch (Throwable t) {
			response = getInternalServerErrorResponse(t);
		}
		
		logResponse(response);

		logger.debug(">synchronizeCommodityTypeVarietyXref " + response);
		return response;
	}
	
	
	
	@Override
	public Response getCommodityTypeVarietyXref(String commodityTypeCode, String cropVarietyId) {
		logger.debug("<getCommodityTypeVarietyXref");

		Response response = null;
		
		logRequest();
		
		if(!hasAuthority(Scopes.GET_COMMODITY_TYPE_VARIETY_XREF)) {
			return Response.status(Status.FORBIDDEN).build();
		}
		
		try {
			
			SyncCommodityTypeVarietyXrefRsrc result = (SyncCommodityTypeVarietyXrefRsrc) cirrasDataSyncService.getCommodityTypeVarietyXref(
					commodityTypeCode,
					toInteger(cropVarietyId),
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

		logger.debug(">getCommodityTypeVarietyXref");
		return response;
	}	

	@Override
	public Response deleteCommodityTypeVarietyXref(String commodityTypeCode, String cropVarietyId) {
		logger.debug("<deleteCommodityTypeVarietyXref");

		Response response = null;
		
		logRequest();
		
		if(!hasAuthority(Scopes.DELETE_SYNC_UNDERWRITING)) {
			return Response.status(Status.FORBIDDEN).build();
		}
			
		try {
			SyncCommodityTypeVarietyXrefRsrc resource = (SyncCommodityTypeVarietyXrefRsrc) cirrasDataSyncService.getCommodityTypeVarietyXref(
					commodityTypeCode,
					toInteger(cropVarietyId),
					getFactoryContext(), 
					getWebAdeAuthentication());
			
			if(resource != null) {
				cirrasDataSyncService.deleteCommodityTypeVarietyXref(
						commodityTypeCode,
						toInteger(cropVarietyId),
						getFactoryContext(), 
						getWebAdeAuthentication());
			}
			response = Response.status(204).build();
		} catch (Throwable t) {
			response = getInternalServerErrorResponse(t);
		}
		
		logResponse(response);

		logger.debug(">deleteCommodityTypeVarietyXref " + response);
		return response;
	}	

	private void logResource(SyncCommodityTypeVarietyXrefRsrc resource, String prefix) {

		StringBuilder msgBldr = new StringBuilder();
		
		if ( resource == null ) {
			msgBldr.append(prefix)
		           .append(": Received Resource is null. ")
		    ;
			
		} else {		
			msgBldr.append(prefix)
			       .append(": Received Resource: ")
			       .append("Type: SyncCommodityTypeVarietyXrefRsrc")
			       .append(", CommodityTypeCode: ")
			       .append(resource.getCommodityTypeCode())
			       .append(", CropVarietyId: ")
			       .append(resource.getCropVarietyId())
			       .append(", Transaction Type: ")
			       .append(resource.getTransactionType())
			;
		}
		
		logger.info(msgBldr.toString());
	}
	
	
}
