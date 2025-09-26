package ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints.impl;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import ca.bc.gov.nrs.wfone.common.rest.endpoints.BaseEndpointsImpl;
import ca.bc.gov.nrs.wfone.common.service.api.NotFoundException;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints.ContractedFieldDetailEndpoint;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints.security.Scopes;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.ContractedFieldDetailRsrc;
import ca.bc.gov.mal.cirras.underwriting.service.api.v1.LandDataSyncService;

public class ContractedFieldDetailEndpointImpl extends BaseEndpointsImpl implements ContractedFieldDetailEndpoint {
	
	private static final Logger logger = LoggerFactory.getLogger(ContractedFieldDetailEndpointImpl.class);
	
	@Autowired
	private LandDataSyncService landDataSyncService; 

	@Override
	public Response synchronizeContractedFieldDetail(ContractedFieldDetailRsrc resource) {
		logger.debug("<synchronizeContractedFieldDetail");
		Response response = null;
		
		logRequest();
		
		if(!hasAuthority(Scopes.UPDATE_SYNC_UNDERWRITING)) {
			return Response.status(Status.FORBIDDEN).build();
		}

		try {
			
			landDataSyncService.synchronizeContractedFieldDetail(
					resource, 
					getFactoryContext(), 
					getWebAdeAuthentication());

			response = Response.status(204).build();
		} catch (Throwable t) {
			response = getInternalServerErrorResponse(t);
		}
		
		logResponse(response);

		logger.debug(">synchronizeContractedFieldDetail " + response);
		return response;
	}
	
	
	
	@Override
	public Response getContractedFieldDetail(String contractedFieldDetailId) {
		logger.debug("<getContractedFieldDetail");

		Response response = null;
		
		logRequest();
		
		if(!hasAuthority(Scopes.GET_LAND)) {
			return Response.status(Status.FORBIDDEN).build();
		}
		
		try {
			
			ContractedFieldDetailRsrc result = (ContractedFieldDetailRsrc) landDataSyncService.getContractedFieldDetail(
					toInteger(contractedFieldDetailId),
					getFactoryContext(), 
					getWebAdeAuthentication());

			response = Response.ok(result).tag(result.getUnquotedETag()).build();

		} catch (NotFoundException e) {
			response = Response.status(Status.NOT_FOUND).build();
			
		} catch (Throwable t) {
			response = getInternalServerErrorResponse(t);
		}
		
		logResponse(response);

		logger.debug(">getContractedFieldDetail");
		return response;
	}	

	@Override
	public Response deleteContractedFieldDetail(String contractedFieldDetailId) {
		logger.debug("<deleteContractedFieldDetail");

		Response response = null;
		
		logRequest();
		
		if(!hasAuthority(Scopes.DELETE_SYNC_UNDERWRITING)) {
			return Response.status(Status.FORBIDDEN).build();
		}
			
		try {
			ContractedFieldDetailRsrc resource = (ContractedFieldDetailRsrc) landDataSyncService.getContractedFieldDetail(
					toInteger(contractedFieldDetailId),
					getFactoryContext(), 
					getWebAdeAuthentication());
			
			if(resource != null) {
				landDataSyncService.deleteContractedFieldDetail(toInteger(contractedFieldDetailId), getFactoryContext(), getWebAdeAuthentication());
			}
			response = Response.status(204).build();
		} catch (Throwable t) {
			response = getInternalServerErrorResponse(t);
		}
		
		logResponse(response);

		logger.debug(">deleteContractedFieldDetail " + response);
		return response;
	}	

}
