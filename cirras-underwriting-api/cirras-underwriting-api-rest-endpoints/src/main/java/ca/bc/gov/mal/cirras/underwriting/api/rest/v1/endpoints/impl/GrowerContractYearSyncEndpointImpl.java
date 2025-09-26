package ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints.impl;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import ca.bc.gov.nrs.wfone.common.rest.endpoints.BaseEndpointsImpl;
import ca.bc.gov.nrs.wfone.common.service.api.NotFoundException;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints.GrowerContractYearSyncEndpoint;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints.security.Scopes;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.GrowerContractYearSyncRsrc;
import ca.bc.gov.mal.cirras.underwriting.service.api.v1.LandDataSyncService;

public class GrowerContractYearSyncEndpointImpl extends BaseEndpointsImpl implements GrowerContractYearSyncEndpoint {
	
	private static final Logger logger = LoggerFactory.getLogger(GrowerContractYearSyncEndpointImpl.class);
	
	@Autowired
	private LandDataSyncService landDataSyncService; 

	@Override
	public Response synchronizeGrowerContractYear(GrowerContractYearSyncRsrc resource) {
		logger.debug("<synchronizeGrowerContractYear");
		Response response = null;
		
		logRequest();
		logResource(resource, "synchronizeGrowerContractYear");

		
		if(!hasAuthority(Scopes.UPDATE_SYNC_UNDERWRITING)) {
			return Response.status(Status.FORBIDDEN).build();
		}

		try {
			
			landDataSyncService.synchronizeGrowerContractYear(
					resource, 
					getFactoryContext(), 
					getWebAdeAuthentication());

			response = Response.status(204).build();
		} catch (Throwable t) {
			response = getInternalServerErrorResponse(t);
		}
		
		logResponse(response);

		logger.debug(">synchronizeGrowerContractYear " + response);
		return response;
	}
	
	
	
	@Override
	public Response getGrowerContractYear(String growerContractYearId) {
		logger.debug("<getGrowerContractYear");

		Response response = null;
		
		logRequest();
		
		if(!hasAuthority(Scopes.GET_LAND)) {
			return Response.status(Status.FORBIDDEN).build();
		}
		
		try {
			
			GrowerContractYearSyncRsrc result = (GrowerContractYearSyncRsrc) landDataSyncService.getGrowerContractYear(
					toInteger(growerContractYearId),
					getFactoryContext(), 
					getWebAdeAuthentication());

			response = Response.ok(result).tag(result.getUnquotedETag()).build();

		} catch (NotFoundException e) {
			response = Response.status(Status.NOT_FOUND).build();
			
		} catch (Throwable t) {
			response = getInternalServerErrorResponse(t);
		}
		
		logResponse(response);

		logger.debug(">getGrowerContractYear");
		return response;
	}	

	@Override
	public Response deleteGrowerContractYear(String growerContractYearId) {
		logger.debug("<deleteGrowerContractYear");

		Response response = null;
		
		logRequest();
		
		if(!hasAuthority(Scopes.DELETE_SYNC_UNDERWRITING)) {
			return Response.status(Status.FORBIDDEN).build();
		}
			
		try {
			GrowerContractYearSyncRsrc resource = (GrowerContractYearSyncRsrc) landDataSyncService.getGrowerContractYear(
					toInteger(growerContractYearId),
					getFactoryContext(), 
					getWebAdeAuthentication());
			
			if(resource != null) {
				landDataSyncService.deleteGrowerContractYear(toInteger(growerContractYearId), getFactoryContext(), getWebAdeAuthentication());
			}
			response = Response.status(204).build();
		} catch (Throwable t) {
			response = getInternalServerErrorResponse(t);
		}
		
		logResponse(response);

		logger.debug(">deleteGrowerContractYear " + response);
		return response;
	}	

	private void logResource(GrowerContractYearSyncRsrc resource, String prefix) {

		StringBuilder msgBldr = new StringBuilder();
		
		if ( resource == null ) {
			msgBldr.append(prefix)
		           .append(": Received Resource is null. ")
		    ;
			
		} else {		
			// Note that only non-sensitive data can be logged.
			msgBldr.append(prefix)
			       .append(": Received Resource: ")
			       .append("Type: GrowerContractYearSyncRsrc")
			       .append(", GrowerContractYearId: ")
			       .append(resource.getGrowerContractYearId())
			       .append(", GrowerId: ")
			       .append(resource.getGrowerId())
			       .append(", ContractId: ")
			       .append(resource.getContractId())
			       .append(", CropYear: ")
			       .append(resource.getCropYear())
			       .append(", Transaction Type: ")
			       .append(resource.getTransactionType())
			;
		}
		
		logger.info(msgBldr.toString());
	}
	
	
}
