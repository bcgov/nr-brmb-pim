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
import ca.bc.gov.mal.cirras.underwriting.controllers.YieldMeasUnitConversionListEndpoint;
import ca.bc.gov.mal.cirras.underwriting.controllers.scopes.Scopes;
import ca.bc.gov.mal.cirras.underwriting.data.resources.GradeModifierListRsrc;
import ca.bc.gov.mal.cirras.underwriting.data.resources.YieldMeasUnitConversionListRsrc;
import ca.bc.gov.mal.cirras.underwriting.services.CirrasMaintenanceService;

public class YieldMeasUnitConversionListEndpointImpl extends BaseEndpointsImpl implements YieldMeasUnitConversionListEndpoint {

	@Autowired
	private CirrasMaintenanceService cirrasMaintenanceService;

	@Override
	public Response getYieldMeasUnitConversions(String insurancePlanId, String srcYieldMeasUnitTypeCode, String targetYieldMeasUnitTypeCode) {
		
		Response response = null;
		
		logRequest();
		
		if(!hasAuthority(Scopes.GET_YIELD_MEAS_UNIT_CONVERSIONS)) {
			return Response.status(Status.FORBIDDEN).build();
		}

		try {
			YieldMeasUnitConversionListRsrc results = (YieldMeasUnitConversionListRsrc) cirrasMaintenanceService.getYieldMeasUnitConversions(
					toInteger(insurancePlanId),
					srcYieldMeasUnitTypeCode,
					targetYieldMeasUnitTypeCode,
					getFactoryContext(), 
					getWebAdeAuthentication());

			GenericEntity<YieldMeasUnitConversionListRsrc> entity = new GenericEntity<YieldMeasUnitConversionListRsrc>(results) {
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
	public Response saveYieldMeasUnitConversions(
			YieldMeasUnitConversionListRsrc yieldMeasUnitConversions,
			String insurancePlanId, 
			String srcYieldMeasUnitTypeCode, 
			String targetYieldMeasUnitTypeCode
			) {

		Response response = null;
		
		logRequest();
		
		if(!hasAuthority(Scopes.SAVE_YIELD_MEAS_UNIT_CONVERSIONS)) {
			return Response.status(Status.FORBIDDEN).build();
		}
			
		try {
			YieldMeasUnitConversionListRsrc currentYieldMeasUnitConversions = (YieldMeasUnitConversionListRsrc) cirrasMaintenanceService.getYieldMeasUnitConversions(
					toInteger(insurancePlanId),
					srcYieldMeasUnitTypeCode,
					targetYieldMeasUnitTypeCode,
					getFactoryContext(), 
					getWebAdeAuthentication());
			
			EntityTag currentTag = EntityTag.valueOf(currentYieldMeasUnitConversions.getQuotedETag());

			ResponseBuilder responseBuilder = this.evaluatePreconditions(currentTag);

			if (responseBuilder == null) {
				// Preconditions Are Met

				YieldMeasUnitConversionListRsrc result = (YieldMeasUnitConversionListRsrc) cirrasMaintenanceService.saveYieldMeasUnitConversions(
						yieldMeasUnitConversions,
						toInteger(insurancePlanId),
						srcYieldMeasUnitTypeCode,
						targetYieldMeasUnitTypeCode,
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
