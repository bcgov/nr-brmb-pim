package ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints.impl;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import ca.bc.gov.nrs.wfone.common.rest.endpoints.BaseEndpointsImpl;
import ca.bc.gov.nrs.wfone.common.service.api.NotFoundException;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints.SyncCodeEndpoint;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints.security.Scopes;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.SyncCodeRsrc;
import ca.bc.gov.mal.cirras.underwriting.service.api.v1.CirrasDataSyncService;

public class SyncCodeEndpointImpl extends BaseEndpointsImpl implements SyncCodeEndpoint {
	
	private static final Logger logger = LoggerFactory.getLogger(SyncCodeEndpointImpl.class);
	
	@Autowired
	private CirrasDataSyncService cirrasDataSyncService; 

	@Override
	public Response synchronizeCode(SyncCodeRsrc resource) {
		logger.debug("<synchronizeCode");
		Response response = null;
		
		logRequest();
		logResource(resource, "synchronizeCode");
		
		if(!hasAuthority(Scopes.UPDATE_SYNC_UNDERWRITING)) {
			return Response.status(Status.FORBIDDEN).build();
		}

		try {
			
			cirrasDataSyncService.synchronizeCode(
					resource, 
					getFactoryContext(), 
					getWebAdeAuthentication());

			response = Response.status(204).build();
		} catch (Throwable t) {
			response = getInternalServerErrorResponse(t);
		}
		
		logResponse(response);

		logger.debug(">synchronizeCode " + response);
		return response;
	}

	@Override
	public Response deleteSyncCode(String codeTableType, String uniqueKey) {
		logger.debug("<deleteSyncCode");

		Response response = null;
		
		logRequest();
		
		if(!hasAuthority(Scopes.DELETE_SYNC_UNDERWRITING)) {
			return Response.status(Status.FORBIDDEN).build();
		}
			
		try {
			SyncCodeRsrc resource = (SyncCodeRsrc) cirrasDataSyncService.getSyncCode(
					codeTableType,
					uniqueKey,
					getFactoryContext(), 
					getWebAdeAuthentication());
			
			if(resource != null) {
				cirrasDataSyncService.deleteSyncCode(codeTableType, uniqueKey, getFactoryContext(), getWebAdeAuthentication());
			}
			response = Response.status(204).build();
		} catch (Throwable t) {
			response = getInternalServerErrorResponse(t);
		}
		
		logResponse(response);

		logger.debug(">deleteSyncClaim " + response);
		return response;
	}

	private void logResource(SyncCodeRsrc resource, String prefix) {

		StringBuilder msgBldr = new StringBuilder();
		
		if ( resource == null ) {
			msgBldr.append(prefix)
		           .append(": Received Resource is null. ")
		    ;
			
		} else {		
			msgBldr.append(prefix)
			       .append(": Received Resource: ")
			       .append("Type: SyncCodeRsrc")
			       .append(", CodeTableType: ")
			       .append(resource.getCodeTableType())
			       .append(", UniqueKeyString: ")
			       .append(resource.getUniqueKeyString())
			       .append(", UniqueKeyInteger: ")
			       .append(resource.getUniqueKeyInteger())
			       .append(", Description: ")
			       .append(resource.getDescription())
			       .append(", Transaction Type: ")
			       .append(resource.getTransactionType())
			;
		}
		
		logger.info(msgBldr.toString());
	}
	
}
