package ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints.impl;

import java.util.ArrayList;
import java.util.List;

import jakarta.ws.rs.core.GenericEntity;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

import org.springframework.beans.factory.annotation.Autowired;

import ca.bc.gov.nrs.common.wfone.rest.resource.MessageListRsrc;
import ca.bc.gov.nrs.wfone.common.model.Message;
import ca.bc.gov.nrs.wfone.common.rest.endpoints.BaseEndpointsImpl;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints.AnnualFieldListEndpoint;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints.security.Scopes;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.parameters.PagingQueryParameters;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.parameters.validation.ParameterValidator;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.AnnualFieldListRsrc;
import ca.bc.gov.mal.cirras.underwriting.service.api.v1.CirrasUnderwritingService;

public class AnnualFieldListEndpointImpl extends BaseEndpointsImpl implements AnnualFieldListEndpoint {
		
	@Autowired
	private CirrasUnderwritingService cirrasUnderwritingService;

	@Autowired
	private ParameterValidator parameterValidator;
	
	public void setCirrasUnderwritingService(CirrasUnderwritingService cirrasUnderwritingService) {
		this.cirrasUnderwritingService = cirrasUnderwritingService;
	}

	public void setParameterValidator(ParameterValidator parameterValidator) {
		this.parameterValidator = parameterValidator;
	}
	

	@Override
	public Response getAnnualFieldList(
			String legalLandId,
			String fieldId,
			String cropYear) {
		
		Response response = null;
		
		logRequest();
		
		if(!hasAuthority(Scopes.SEARCH_ANNUAL_FIELDS)) {
			return Response.status(Status.FORBIDDEN).build();
		}

		try {

			AnnualFieldListRsrc results = (AnnualFieldListRsrc) cirrasUnderwritingService.getAnnualFieldForLegalLandList(
					toInteger(legalLandId),
					toInteger(fieldId),
					toInteger(cropYear),
					getFactoryContext(), 
					getWebAdeAuthentication());



			GenericEntity<AnnualFieldListRsrc> entity = new GenericEntity<AnnualFieldListRsrc>(results) {
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
