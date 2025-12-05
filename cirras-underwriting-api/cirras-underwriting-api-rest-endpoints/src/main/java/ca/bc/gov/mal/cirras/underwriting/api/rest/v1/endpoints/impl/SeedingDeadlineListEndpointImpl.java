package ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints.impl;

import jakarta.ws.rs.core.EntityTag;
import jakarta.ws.rs.core.GenericEntity;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.ResponseBuilder;
import jakarta.ws.rs.core.Response.Status;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import ca.bc.gov.nrs.common.wfone.rest.resource.MessageListRsrc;
import ca.bc.gov.nrs.wfone.common.rest.endpoints.BaseEndpointsImpl;
import ca.bc.gov.nrs.wfone.common.service.api.NotFoundException;
import ca.bc.gov.nrs.wfone.common.service.api.ValidationFailureException;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints.SeedingDeadlineListEndpoint;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints.security.Scopes;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.SeedingDeadlineListRsrc;
import ca.bc.gov.mal.cirras.underwriting.service.api.v1.CirrasMaintenanceService;

public class SeedingDeadlineListEndpointImpl extends BaseEndpointsImpl implements SeedingDeadlineListEndpoint {

	private static final Logger logger = LoggerFactory.getLogger(SeedingDeadlineListEndpointImpl.class);	
	
	@Autowired
	private CirrasMaintenanceService cirrasMaintenanceService;

	
	@Override
	public Response saveSeedingDeadlines(SeedingDeadlineListRsrc seedingDeadlineList, String cropYear) {
		logger.debug("<saveSeedingDeadlines");

		Response response = null;
		
		logRequest();
		
		if(!hasAuthority(Scopes.SAVE_SEEDING_DEADLINES)) {
			return Response.status(Status.FORBIDDEN).build();
		}
			
		try {
			SeedingDeadlineListRsrc currentSeedingDeadlines = (SeedingDeadlineListRsrc) cirrasMaintenanceService.getSeedingDeadlines(
					toInteger(cropYear),
					getFactoryContext(), 
					getWebAdeAuthentication());

			EntityTag currentTag = EntityTag.valueOf(currentSeedingDeadlines.getQuotedETag());

			ResponseBuilder responseBuilder = this.evaluatePreconditions(currentTag);

			if (responseBuilder == null) {
				// Preconditions Are Met

				SeedingDeadlineListRsrc result = (SeedingDeadlineListRsrc) cirrasMaintenanceService.saveSeedingDeadlines(
						seedingDeadlineList, 
						toInteger(cropYear),
						getFactoryContext(), 
						getWebAdeAuthentication());

				response = Response.ok(result).tag(result.getUnquotedETag()).build();
				
			} else {
				// Preconditions Are NOT Met

				response = responseBuilder.tag(currentTag).build();
			}			
			

		} catch(ValidationFailureException e) {
			response = Response.status(Status.BAD_REQUEST).entity(new MessageListRsrc(e.getValidationErrors())).build();
		} catch (NotFoundException e) {
			response = Response.status(Status.NOT_FOUND).build();
		} catch (Throwable t) {
			response = getInternalServerErrorResponse(t);
		}
		
		logResponse(response);

		logger.debug(">saveSeedingDeadlines " + response.getStatus());
		return response;
	}	
	
//	
//	@Override
//	public Response saveSeedingDeadlines(SeedingDeadlineListRsrc seedingDeadlineList, String cropYear) {
//		logger.debug("<saveSeedingDeadlines");
//		Response response = null;
//		
//		logRequest();
//
//		if(!hasAuthority(Scopes.SAVE_SEEDING_DEADLINES)) {
//			return Response.status(Status.FORBIDDEN).build();
//		}
//
//		try {
//
//			SeedingDeadlineListRsrc results = (SeedingDeadlineListRsrc) cirrasMaintenanceService.saveSeedingDeadlines(
//					seedingDeadlineList, 
//					toInteger(cropYear),
//					getFactoryContext(), 
//					getWebAdeAuthentication());
//
//			GenericEntity<SeedingDeadlineListRsrc> entity = new GenericEntity<SeedingDeadlineListRsrc>(results) {
//				/* do nothing */
//			};
//			
//			response = Response.ok(entity).tag(results.getUnquotedETag()).build();
//
//		} catch(ValidationFailureException e) {
//			response = Response.status(Status.BAD_REQUEST).entity(new MessageListRsrc(e.getValidationErrors())).build();
//		} catch (Throwable t) {
//			response = getInternalServerErrorResponse(t);
//		}
//		
//		logResponse(response);
//
//		logger.debug(">saveSeedingDeadlines " + response);
//		return response;
//	}

	@Override
	public Response getSeedingDeadlines(String cropYear) {
		
		Response response = null;
		
		logRequest();
		
		if(!hasAuthority(Scopes.GET_SEEDING_DEADLINES)) {
			return Response.status(Status.FORBIDDEN).build();
		}

		try {
			
			SeedingDeadlineListRsrc results = (SeedingDeadlineListRsrc) cirrasMaintenanceService.getSeedingDeadlines(
					toInteger(cropYear),
					getFactoryContext(), 
					getWebAdeAuthentication());

			GenericEntity<SeedingDeadlineListRsrc> entity = new GenericEntity<SeedingDeadlineListRsrc>(results) {
					/* do nothing */
			};

			response = Response.ok(entity).tag(results.getUnquotedETag()).build();
			
			
		} catch (Throwable t) {
			response = getInternalServerErrorResponse(t);
		}
		
		logResponse(response);

		return response;
	}
}
