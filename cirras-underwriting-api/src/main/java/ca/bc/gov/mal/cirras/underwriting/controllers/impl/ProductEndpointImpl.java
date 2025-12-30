package ca.bc.gov.mal.cirras.underwriting.controllers.impl;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import ca.bc.gov.nrs.wfone.common.rest.endpoints.BaseEndpointsImpl;
import ca.bc.gov.nrs.wfone.common.service.api.NotFoundException;
import ca.bc.gov.mal.cirras.underwriting.controllers.ProductEndpoint;
import ca.bc.gov.mal.cirras.underwriting.controllers.scopes.Scopes;
import ca.bc.gov.mal.cirras.underwriting.data.resources.ProductRsrc;
import ca.bc.gov.mal.cirras.underwriting.services.CirrasDataSyncService;

public class ProductEndpointImpl extends BaseEndpointsImpl implements ProductEndpoint {
	
	private static final Logger logger = LoggerFactory.getLogger(ProductEndpointImpl.class);
	
	@Autowired
	private CirrasDataSyncService cirrasDataSyncService; 

	@Override
	public Response synchronizeProduct(ProductRsrc resource) {
		logger.debug("<synchronizeProduct");
		Response response = null;
		
		logRequest();
		logResource(resource, "synchronizeProduct");
		
		if(!hasAuthority(Scopes.UPDATE_SYNC_UNDERWRITING)) {
			return Response.status(Status.FORBIDDEN).build();
		}

		try {
			
			cirrasDataSyncService.synchronizeProduct(
					resource, 
					getFactoryContext(), 
					getWebAdeAuthentication());

			response = Response.status(204).build();
		} catch (Throwable t) {
			response = getInternalServerErrorResponse(t);
		}
		
		logResponse(response);

		logger.debug(">synchronizeProduct " + response);
		return response;
	}
	
	
	
	@Override
	public Response getProduct(String productId) {
		logger.debug("<getProduct");

		Response response = null;
		
		logRequest();
		
		if(!hasAuthority(Scopes.GET_PRODUCT)) {
			return Response.status(Status.FORBIDDEN).build();
		}
		
		try {
			
			ProductRsrc result = (ProductRsrc) cirrasDataSyncService.getProduct(
					toInteger(productId),
					getFactoryContext(), 
					getWebAdeAuthentication());

			response = Response.ok(result).tag(result.getUnquotedETag()).build();

		} catch (NotFoundException e) {
			response = Response.status(Status.NOT_FOUND).build();
			
		} catch (Throwable t) {
			response = getInternalServerErrorResponse(t);
		}
		
		logResponse(response);

		logger.debug(">getProduct");
		return response;
	}	

	@Override
	public Response deleteProduct(String productId) {
		logger.debug("<deleteProduct");

		Response response = null;
		
		logRequest();
		
		if(!hasAuthority(Scopes.DELETE_SYNC_UNDERWRITING)) {
			return Response.status(Status.FORBIDDEN).build();
		}
			
		try {
			ProductRsrc resource = (ProductRsrc) cirrasDataSyncService.getProduct(
					toInteger(productId),
					getFactoryContext(), 
					getWebAdeAuthentication());
			
			if(resource != null) {
				cirrasDataSyncService.deleteProduct(toInteger(productId), getFactoryContext(), getWebAdeAuthentication());
			}
			response = Response.status(204).build();

		} catch (NotFoundException e) {
			response = Response.status(Status.NOT_FOUND).build();
		
		} catch (Throwable t) {
			response = getInternalServerErrorResponse(t);
		}
		
		logResponse(response);

		logger.debug(">deleteProduct " + response);
		return response;
	}	

	private void logResource(ProductRsrc resource, String prefix) {

		StringBuilder msgBldr = new StringBuilder();
		
		if ( resource == null ) {
			msgBldr.append(prefix)
		           .append(": Received Resource is null. ")
		    ;
			
		} else {		
			// Note that only non-sensitive data can be logged.
			msgBldr.append(prefix)
			       .append(": Received Resource: ")
			       .append("Type: ProductRsrc")
			       .append(", ProductId: ")
			       .append(resource.getProductId())
			       .append(", Transaction Type: ")
			       .append(resource.getTransactionType())
			;
		}
		
		logger.info(msgBldr.toString());
	}
	
}
