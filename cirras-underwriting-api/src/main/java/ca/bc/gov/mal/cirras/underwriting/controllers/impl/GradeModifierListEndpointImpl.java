package ca.bc.gov.mal.cirras.underwriting.controllers.impl;

import jakarta.ws.rs.core.EntityTag;
import jakarta.ws.rs.core.GenericEntity;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.ResponseBuilder;
import jakarta.ws.rs.core.Response.Status;

import org.springframework.beans.factory.annotation.Autowired;

import ca.bc.gov.nrs.common.wfone.rest.resource.MessageListRsrc;
import ca.bc.gov.nrs.wfone.common.rest.endpoints.BaseEndpointsImpl;
import ca.bc.gov.nrs.wfone.common.service.api.NotFoundException;
import ca.bc.gov.nrs.wfone.common.service.api.ValidationFailureException;
import ca.bc.gov.mal.cirras.underwriting.controllers.GradeModifierListEndpoint;
import ca.bc.gov.mal.cirras.underwriting.controllers.scopes.Scopes;
import ca.bc.gov.mal.cirras.underwriting.data.resources.GradeModifierListRsrc;
import ca.bc.gov.mal.cirras.underwriting.services.CirrasMaintenanceService;

public class GradeModifierListEndpointImpl extends BaseEndpointsImpl implements GradeModifierListEndpoint {

	@Autowired
	private CirrasMaintenanceService cirrasMaintenanceService;

	@Override
	public Response getGradeModifierList(String cropYear, String insurancePlanId, String cropCommodityId) {
		
		Response response = null;
		
		logRequest();
		
		if(!hasAuthority(Scopes.GET_GRADE_MODIFIERS)) {
			return Response.status(Status.FORBIDDEN).build();
		}

		try {
			GradeModifierListRsrc results = (GradeModifierListRsrc) cirrasMaintenanceService.getGradeModifierList(
					toInteger(cropYear),
					toInteger(insurancePlanId),
					toInteger(cropCommodityId),
					getFactoryContext(), 
					getWebAdeAuthentication());

			GenericEntity<GradeModifierListRsrc> entity = new GenericEntity<GradeModifierListRsrc>(results) {
				/* do nothing */
			};

			response = Response.ok(entity).tag(results.getUnquotedETag()).build();
			
		} catch (Throwable t) {
			response = getInternalServerErrorResponse(t);
		}
		
		logResponse(response);

		return response;
	}


	@Override
	public Response saveGradeModifiers(
			GradeModifierListRsrc gradeModifiers,
			String cropYear, 
			String insurancePlanId, 
			String cropCommodityId
			) {

		Response response = null;
		
		logRequest();
		
		if(!hasAuthority(Scopes.SAVE_GRADE_MODIFIERS)) {
			return Response.status(Status.FORBIDDEN).build();
		}
			
		try {
			GradeModifierListRsrc currentGradeModifiers = (GradeModifierListRsrc) cirrasMaintenanceService.getGradeModifierList(
					toInteger(cropYear),
					toInteger(insurancePlanId),
					toInteger(cropCommodityId),
					getFactoryContext(), 
					getWebAdeAuthentication());

			EntityTag currentTag = EntityTag.valueOf(currentGradeModifiers.getQuotedETag());

			ResponseBuilder responseBuilder = this.evaluatePreconditions(currentTag);

			if (responseBuilder == null) {
				// Preconditions Are Met

				GradeModifierListRsrc result = (GradeModifierListRsrc) cirrasMaintenanceService.saveGradeModifiers(
						toInteger(cropYear),
						toInteger(insurancePlanId),
						toInteger(cropCommodityId),
						gradeModifiers, 
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

		return response;	}

}
