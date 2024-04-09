package ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints.impl;

import java.net.URI;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.springframework.beans.factory.annotation.Autowired;

import ca.bc.gov.nrs.common.wfone.rest.resource.MessageListRsrc;
import ca.bc.gov.nrs.wfone.common.rest.endpoints.BaseEndpointsImpl;
import ca.bc.gov.nrs.wfone.common.service.api.ValidationFailureException;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints.DopYieldContractListEndpoint;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints.security.Scopes;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.parameters.validation.ParameterValidator;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.DopYieldContractRsrc;
import ca.bc.gov.mal.cirras.underwriting.service.api.v1.CirrasDopYieldService;

public class DopYieldContractListEndpointImpl extends BaseEndpointsImpl implements DopYieldContractListEndpoint {

	@Autowired
	private CirrasDopYieldService cirrasDopYieldService;

	@Autowired
	private ParameterValidator parameterValidator;
	
	public void setCirrasDopYieldService(CirrasDopYieldService cirrasDopYieldService) {
		this.cirrasDopYieldService = cirrasDopYieldService;
	}

	public void setParameterValidator(ParameterValidator parameterValidator) {
		this.parameterValidator = parameterValidator;
	}
	
	@Override
	public Response createDopYieldContract(DopYieldContractRsrc dopYieldContract) {

		Response response = null;
		
		logRequest();

		if(!hasAuthority(Scopes.CREATE_DOP_YIELD_CONTRACT)) {
			return Response.status(Status.FORBIDDEN).build();
		}

		try {

			DopYieldContractRsrc result = (DopYieldContractRsrc) cirrasDopYieldService.createDopYieldContract(
					dopYieldContract, 
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

		return response;
	}
}
