package ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints.impl;

import java.net.URI;

import jakarta.ws.rs.core.GenericEntity;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import ca.bc.gov.nrs.common.wfone.rest.resource.MessageListRsrc;
import ca.bc.gov.nrs.wfone.common.rest.endpoints.BaseEndpointsImpl;
import ca.bc.gov.nrs.wfone.common.service.api.ValidationFailureException;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints.UnderwritingYearListEndpoint;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints.security.Scopes;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.parameters.validation.ParameterValidator;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.UnderwritingYearListRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.UnderwritingYearRsrc;
import ca.bc.gov.mal.cirras.underwriting.service.api.v1.CirrasMaintenanceService;

public class UnderwritingYearListEndpointImpl extends BaseEndpointsImpl implements UnderwritingYearListEndpoint {

	private static final Logger logger = LoggerFactory.getLogger(UnderwritingYearListEndpointImpl.class);	
	
	@Autowired
	private CirrasMaintenanceService cirrasMaintenanceService;

	@Autowired
	private ParameterValidator parameterValidator;

	public void setParameterValidator(ParameterValidator parameterValidator) {
		this.parameterValidator = parameterValidator;
	}
	
	@Override
	public Response createUnderwritingYear(UnderwritingYearRsrc underwritingYear) {
		logger.debug("<createUnderwritingYear");
		Response response = null;
		
		logRequest();

		if(!hasAuthority(Scopes.CREATE_UNDERWRITING_YEAR)) {
			return Response.status(Status.FORBIDDEN).build();
		}

		try {

			UnderwritingYearRsrc result = (UnderwritingYearRsrc) cirrasMaintenanceService.createUnderwritingYear(
					underwritingYear, 
					getFactoryContext(), 
					getWebAdeAuthentication());

			URI createdUri = URI.create(result.getSelfLink());

			response = Response.created(createdUri).entity(result).tag(result.getUnquotedETag()).build();

		} catch(ValidationFailureException e) {
			response = Response.status(Status.BAD_REQUEST).entity(new MessageListRsrc(e.getValidationErrors())).build();
		} catch (Throwable t) {
			response = getInternalServerErrorResponse(t);
		}
		
		logResponse(response);

		logger.debug(">createUnderwritingYear " + response);
		return response;
	}

	@Override
	public Response getUnderwritingYearList() {
		
		Response response = null;
		
		logRequest();
		
		if(!hasAuthority(Scopes.GET_UNDERWRITING_YEAR)) {
			return Response.status(Status.FORBIDDEN).build();
		}

		try {
			
			UnderwritingYearListRsrc results = (UnderwritingYearListRsrc) cirrasMaintenanceService.getUnderwritingYearList(
					getFactoryContext(), 
					getWebAdeAuthentication());

			GenericEntity<UnderwritingYearListRsrc> entity = new GenericEntity<UnderwritingYearListRsrc>(results) {
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
