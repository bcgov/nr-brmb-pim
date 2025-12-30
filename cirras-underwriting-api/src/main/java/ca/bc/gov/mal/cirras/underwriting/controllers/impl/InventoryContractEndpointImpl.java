package ca.bc.gov.mal.cirras.underwriting.controllers.impl;

import jakarta.ws.rs.core.EntityTag;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.ResponseBuilder;
import jakarta.ws.rs.core.Response.Status;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import ca.bc.gov.nrs.common.wfone.rest.resource.MessageListRsrc;
import ca.bc.gov.nrs.wfone.common.rest.endpoints.BaseEndpointsImpl;
import ca.bc.gov.nrs.wfone.common.service.api.ConflictException;
import ca.bc.gov.nrs.wfone.common.service.api.ForbiddenException;
import ca.bc.gov.nrs.wfone.common.service.api.NotFoundException;
import ca.bc.gov.nrs.wfone.common.service.api.ValidationFailureException;
import ca.bc.gov.nrs.wfone.common.service.api.model.factory.FactoryContext;
import ca.bc.gov.nrs.wfone.common.webade.authentication.WebAdeAuthentication;
import ca.bc.gov.mal.cirras.underwriting.services.CirrasInventoryService;
import ca.bc.gov.mal.cirras.underwriting.data.resources.InventoryContractRsrc;
import ca.bc.gov.mal.cirras.underwriting.data.models.InventoryContract;
import ca.bc.gov.mal.cirras.underwriting.controllers.InventoryContractEndpoint;
import ca.bc.gov.mal.cirras.underwriting.controllers.scopes.Scopes;

public class InventoryContractEndpointImpl extends BaseEndpointsImpl implements InventoryContractEndpoint {
	
	private static final Logger logger = LoggerFactory.getLogger(InventoryContractEndpointImpl.class);
	
	@Autowired
	private CirrasInventoryService cirrasInventoryService;
	
	
	@Override
	public Response getInventoryContract(final String inventoryContractGuid) {
		
		Response response = null;
		
		logRequest();
		
		if(!hasAuthority(Scopes.GET_INVENTORY_CONTRACT)) {
			return Response.status(Status.FORBIDDEN).build();
		}
		
		try {
			InventoryContractRsrc result = (InventoryContractRsrc) cirrasInventoryService.getInventoryContract(
					inventoryContractGuid,
					getFactoryContext(), 
					getWebAdeAuthentication());
			response = Response.ok(result).tag(result.getUnquotedETag()).build();

		} catch (NotFoundException e) {
			response = Response.status(Status.NOT_FOUND).build();
			
		} catch (Throwable t) {
			response = getInternalServerErrorResponse(t);
		}
		
		logResponse(response);

		return response;
	}

	
	@Override
	public Response updateInventoryContract(String inventoryContractGuid, InventoryContractRsrc inputResource) {
		logger.debug("<updateInventoryContract");

		Response response = null;
		
		logRequest();
		
		if(!hasAuthority(Scopes.UPDATE_INVENTORY_CONTRACT)) {
			return Response.status(Status.FORBIDDEN).build();
		}
			
		try {
			InventoryContractRsrc currentInventoryContract = (InventoryContractRsrc) cirrasInventoryService.getInventoryContract(
					inventoryContractGuid, 
					getFactoryContext(), 
					getWebAdeAuthentication());

			EntityTag currentTag = EntityTag.valueOf(currentInventoryContract.getQuotedETag());

			ResponseBuilder responseBuilder = this.evaluatePreconditions(currentTag);

			if (responseBuilder == null) {
				// Preconditions Are Met
				
				String optimisticLock = getIfMatchHeader();

				InventoryContractRsrc result = (InventoryContractRsrc)cirrasInventoryService.updateInventoryContract(
						inventoryContractGuid,
						optimisticLock, 
						inputResource, 
						getFactoryContext(), 
						getWebAdeAuthentication());

				response = Response.ok(result).tag(result.getUnquotedETag()).build();
				
			} else {
				// Preconditions Are NOT Met

				response = responseBuilder.tag(currentTag).build();
			}
		} catch (ForbiddenException e) {
			response = Response.status(Status.FORBIDDEN).build();
		} catch(ValidationFailureException e) {
			response = Response.status(Status.BAD_REQUEST).entity(new MessageListRsrc(e.getValidationErrors())).build();
		} catch (ConflictException e) {
			response = Response.status(Status.CONFLICT).entity(e.getMessage()).build();
		} catch (NotFoundException e) {
			response = Response.status(Status.NOT_FOUND).build();
		} catch (Throwable t) {
			response = getInternalServerErrorResponse(t);
		}
		
		logResponse(response);

		logger.debug(">updateInventoryContract " + response.getStatus());
		return response;
	}

	
	@Override
	public Response deleteInventoryContract(String inventoryContractGuid) {
		logger.debug("<deleteInventoryContract");

		Response response = null;
		
		logRequest();
		
		if(!hasAuthority(Scopes.DELETE_INVENTORY_CONTRACT)) {
			return Response.status(Status.FORBIDDEN).build();
		}
			
		try {
			InventoryContractRsrc current = (InventoryContractRsrc) this.cirrasInventoryService.getInventoryContract(
					inventoryContractGuid, 
					getFactoryContext(), 
					getWebAdeAuthentication());

			EntityTag currentTag = EntityTag.valueOf(current.getQuotedETag());

			ResponseBuilder responseBuilder = this.evaluatePreconditions(currentTag);

			if (responseBuilder == null) {
				// Preconditions Are Met
				
				String optimisticLock = getIfMatchHeader();

				cirrasInventoryService.deleteInventoryContract(
						inventoryContractGuid, 
						optimisticLock, 
						getWebAdeAuthentication());

				response = Response.status(204).build();
			} else {
				// Preconditions Are NOT Met

				response = responseBuilder.tag(currentTag).build();
			}
		} catch (ForbiddenException e) {
			response = Response.status(Status.FORBIDDEN).build();
		} catch (ConflictException e) {
			response = Response.status(Status.CONFLICT).entity(e.getMessage()).build();
		} catch (NotFoundException e) {
			response = Response.status(Status.NOT_FOUND).build();
		} catch (Throwable t) {
			response = getInternalServerErrorResponse(t);
		}
		
		logResponse(response);

		logger.debug(">deleteInventoryContract " + response);
		return response;
	}
}
