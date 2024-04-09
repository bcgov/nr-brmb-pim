package ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints.impl;

import javax.ws.rs.core.EntityTag;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;

import org.springframework.beans.factory.annotation.Autowired;

import ca.bc.gov.nrs.common.wfone.rest.resource.MessageListRsrc;
import ca.bc.gov.nrs.wfone.common.rest.endpoints.BaseEndpointsImpl;
import ca.bc.gov.nrs.wfone.common.service.api.NotFoundException;
import ca.bc.gov.nrs.wfone.common.service.api.ValidationFailureException;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints.YieldMeasUnitConversionListEndpoint;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints.security.Scopes;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.GradeModifierListRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.YieldMeasUnitConversionListRsrc;
import ca.bc.gov.mal.cirras.underwriting.service.api.v1.CirrasMaintenanceService;

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
