package ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints.impl;

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
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints.CropVarietyInsurabilityListEndpoint;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints.security.Scopes;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.CropVarietyInsurabilityListRsrc;
import ca.bc.gov.mal.cirras.underwriting.service.api.v1.CirrasMaintenanceService;

public class CropVarietyInsurabilityListEndpointImpl extends BaseEndpointsImpl implements CropVarietyInsurabilityListEndpoint {

	@Autowired
	private CirrasMaintenanceService cirrasMaintenanceService;

	@Override
	public Response getCropVarietyInsurabilities(String insurancePlanId, String loadForEdit) {
		
		Response response = null;
		
		logRequest();
		
		if(!hasAuthority(Scopes.GET_CROP_VARIETY_INSURABILITIES)) {
			return Response.status(Status.FORBIDDEN).build();
		}

		try {
			CropVarietyInsurabilityListRsrc results = (CropVarietyInsurabilityListRsrc) cirrasMaintenanceService.getCropVarietyInsurabilities(
					toInteger(insurancePlanId),
					toBoolean(loadForEdit),
					getFactoryContext(), 
					getWebAdeAuthentication());

			GenericEntity<CropVarietyInsurabilityListRsrc> entity = new GenericEntity<CropVarietyInsurabilityListRsrc>(results) {
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
	public Response saveCropVarietyInsurabilities(
			CropVarietyInsurabilityListRsrc cropVarietyInsurabilities,
			String insurancePlanId, 
			String loadForEdit
			) {

		Response response = null;
		
		logRequest();
		
		if(!hasAuthority(Scopes.SAVE_CROP_VARIETY_INSURABILITIES)) {
			return Response.status(Status.FORBIDDEN).build();
		}
			
		try {
			CropVarietyInsurabilityListRsrc currentCropVarietyInsurabilities = (CropVarietyInsurabilityListRsrc) cirrasMaintenanceService.getCropVarietyInsurabilities(
					toInteger(insurancePlanId),
					toBoolean(loadForEdit),
					getFactoryContext(), 
					getWebAdeAuthentication());

			EntityTag currentTag = EntityTag.valueOf(currentCropVarietyInsurabilities.getQuotedETag());

			ResponseBuilder responseBuilder = this.evaluatePreconditions(currentTag);

			if (responseBuilder == null) {
				// Preconditions Are Met

				CropVarietyInsurabilityListRsrc result = (CropVarietyInsurabilityListRsrc) cirrasMaintenanceService.saveCropVarietyInsurabilities(
						toInteger(insurancePlanId),
						toBoolean(loadForEdit),
						cropVarietyInsurabilities, 
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
