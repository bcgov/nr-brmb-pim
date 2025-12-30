package ca.bc.gov.mal.cirras.underwriting.controllers.impl;

import jakarta.ws.rs.core.EntityTag;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.ResponseBuilder;
import jakarta.ws.rs.core.Response.Status;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import ca.bc.gov.nrs.wfone.common.rest.endpoints.BaseEndpointsImpl;
import ca.bc.gov.nrs.wfone.common.service.api.ConflictException;
import ca.bc.gov.nrs.wfone.common.service.api.ForbiddenException;
import ca.bc.gov.nrs.wfone.common.service.api.NotFoundException;
import ca.bc.gov.mal.cirras.underwriting.services.CirrasMaintenanceService;
import ca.bc.gov.mal.cirras.underwriting.data.resources.UnderwritingYearRsrc;
import ca.bc.gov.mal.cirras.underwriting.controllers.UnderwritingYearEndpoint;
import ca.bc.gov.mal.cirras.underwriting.controllers.scopes.Scopes;

public class UnderwritingYearEndpointImpl extends BaseEndpointsImpl implements UnderwritingYearEndpoint {
	
	private static final Logger logger = LoggerFactory.getLogger(UnderwritingYearEndpointImpl.class);
	
	@Autowired
	private CirrasMaintenanceService cirrasMaintenanceService;
	
	@Override
	public Response getUnderwritingYear(String cropYear) {
		
		Response response = null;
		
		logRequest();
		
		if(!hasAuthority(Scopes.GET_UNDERWRITING_YEAR)) {
			return Response.status(Status.FORBIDDEN).build();
		}
		
		try {
			UnderwritingYearRsrc result = (UnderwritingYearRsrc) cirrasMaintenanceService.getUnderwritingYear(
					toInteger(cropYear),
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
	public Response deleteUnderwritingYear(String cropYear) {
		logger.debug("<deleteUnderwritingYear");

		Response response = null;
		
		logRequest();
		
		if(!hasAuthority(Scopes.DELETE_UNDERWRITING_YEAR)) {
			return Response.status(Status.FORBIDDEN).build();
		}
			
		try {
			UnderwritingYearRsrc current = (UnderwritingYearRsrc) this.cirrasMaintenanceService.getUnderwritingYear(
					toInteger(cropYear), 
					getFactoryContext(), 
					getWebAdeAuthentication());

			EntityTag currentTag = EntityTag.valueOf(current.getQuotedETag());

			ResponseBuilder responseBuilder = this.evaluatePreconditions(currentTag);

			if (responseBuilder == null) {
				// Preconditions Are Met
				
				String optimisticLock = getIfMatchHeader();

				cirrasMaintenanceService.deleteUnderwritingYear(
						toInteger(cropYear),
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

		logger.debug(">deleteUnderwritingYear " + response);
		return response;
	}
}
