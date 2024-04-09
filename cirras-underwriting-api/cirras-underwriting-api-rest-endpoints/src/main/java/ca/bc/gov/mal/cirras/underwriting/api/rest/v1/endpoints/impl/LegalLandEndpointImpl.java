package ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints.impl;

import javax.ws.rs.core.EntityTag;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import ca.bc.gov.nrs.common.wfone.rest.resource.MessageListRsrc;
import ca.bc.gov.nrs.wfone.common.rest.endpoints.BaseEndpointsImpl;
import ca.bc.gov.nrs.wfone.common.service.api.ConflictException;
import ca.bc.gov.nrs.wfone.common.service.api.ForbiddenException;
import ca.bc.gov.nrs.wfone.common.service.api.NotFoundException;
import ca.bc.gov.nrs.wfone.common.service.api.ValidationFailureException;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.LegalLandRsrc;
import ca.bc.gov.mal.cirras.underwriting.service.api.v1.CirrasUwLandManagementService;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints.LegalLandEndpoint;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints.security.Scopes;

public class LegalLandEndpointImpl extends BaseEndpointsImpl implements LegalLandEndpoint {
	
	private static final Logger logger = LoggerFactory.getLogger(LegalLandEndpointImpl.class);
	
	@Autowired
	private CirrasUwLandManagementService cirrasUwLandManagementService;
	
	@Override
	public Response getLegalLand(final String legalLandId) {
		
		Response response = null;
		
		logRequest();
		
		if(!hasAuthority(Scopes.GET_LEGAL_LAND)) {
			return Response.status(Status.FORBIDDEN).build();
		}
		
		try {
			LegalLandRsrc result = (LegalLandRsrc) cirrasUwLandManagementService.getLegalLand(
					toInteger(legalLandId),
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
	public Response updateLegalLand(String legalLandId, LegalLandRsrc inputResource) {
		logger.debug("<updateLegalLand");

		Response response = null;
		
		logRequest();
		
		if(!hasAuthority(Scopes.UPDATE_LEGAL_LAND)) {
			return Response.status(Status.FORBIDDEN).build();
		}
			
		try {
			LegalLandRsrc currentLegalLand = (LegalLandRsrc) cirrasUwLandManagementService.getLegalLand(
					toInteger(legalLandId), 
					getFactoryContext(), 
					getWebAdeAuthentication());

			EntityTag currentTag = EntityTag.valueOf(currentLegalLand.getQuotedETag());

			ResponseBuilder responseBuilder = this.evaluatePreconditions(currentTag);

			if (responseBuilder == null) {
				// Preconditions Are Met
				
				String optimisticLock = getIfMatchHeader();

				LegalLandRsrc result = (LegalLandRsrc)cirrasUwLandManagementService.updateLegalLand(
						toInteger(legalLandId),
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

		logger.debug(">updateLegalLand " + response.getStatus());
		return response;
	}

	
	@Override
	public Response deleteLegalLand(String legalLandId) {
		logger.debug("<deleteLegalLand");

		Response response = null;
		
		logRequest();
		
		if(!hasAuthority(Scopes.DELETE_LEGAL_LAND)) {
			return Response.status(Status.FORBIDDEN).build();
		}
			
		try {
			LegalLandRsrc current = (LegalLandRsrc) this.cirrasUwLandManagementService.getLegalLand(
					toInteger(legalLandId), 
					getFactoryContext(), 
					getWebAdeAuthentication());

			EntityTag currentTag = EntityTag.valueOf(current.getQuotedETag());

			ResponseBuilder responseBuilder = this.evaluatePreconditions(currentTag);

			if (responseBuilder == null) {
				// Preconditions Are Met
				
				String optimisticLock = getIfMatchHeader();

				cirrasUwLandManagementService.deleteLegalLand(
						toInteger(legalLandId), 
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

		logger.debug(">deleteLegalLand " + response);
		return response;
	}
}
