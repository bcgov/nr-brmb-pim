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
import ca.bc.gov.mal.cirras.underwriting.controllers.GradeModifierTypeListEndpoint;
import ca.bc.gov.mal.cirras.underwriting.controllers.scopes.Scopes;
import ca.bc.gov.mal.cirras.underwriting.data.resources.GradeModifierTypeListRsrc;
import ca.bc.gov.mal.cirras.underwriting.services.CirrasMaintenanceService;

public class GradeModifierTypeListEndpointImpl extends BaseEndpointsImpl implements GradeModifierTypeListEndpoint {

	@Autowired
	private CirrasMaintenanceService cirrasMaintenanceService;

	@Override
	public Response getGradeModifierTypeList(String cropYear) {
		
		Response response = null;
		
		logRequest();
		
		if(!hasAuthority(Scopes.GET_GRADE_MODIFIERS)) {
			return Response.status(Status.FORBIDDEN).build();
		}

		try {
			GradeModifierTypeListRsrc results = (GradeModifierTypeListRsrc) cirrasMaintenanceService.getGradeModifierTypeList(
					toInteger(cropYear),
					getFactoryContext(), 
					getWebAdeAuthentication());

			GenericEntity<GradeModifierTypeListRsrc> entity = new GenericEntity<GradeModifierTypeListRsrc>(results) {
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
	public Response saveGradeModifierTypes(
			GradeModifierTypeListRsrc gradeModifiers,
			String cropYear
			) {

		Response response = null;
		
		logRequest();
		
		if(!hasAuthority(Scopes.SAVE_GRADE_MODIFIERS)) {
			return Response.status(Status.FORBIDDEN).build();
		}
			
		try {
			GradeModifierTypeListRsrc currentGradeModifierTypes = (GradeModifierTypeListRsrc) cirrasMaintenanceService.getGradeModifierTypeList(
					toInteger(cropYear),
					getFactoryContext(), 
					getWebAdeAuthentication());

			EntityTag currentTag = EntityTag.valueOf(currentGradeModifierTypes.getQuotedETag());

			ResponseBuilder responseBuilder = this.evaluatePreconditions(currentTag);

			if (responseBuilder == null) {
				// Preconditions Are Met

				GradeModifierTypeListRsrc result = (GradeModifierTypeListRsrc) cirrasMaintenanceService.saveGradeModifierTypes(
						toInteger(cropYear),
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

		return response;	
	}

}
