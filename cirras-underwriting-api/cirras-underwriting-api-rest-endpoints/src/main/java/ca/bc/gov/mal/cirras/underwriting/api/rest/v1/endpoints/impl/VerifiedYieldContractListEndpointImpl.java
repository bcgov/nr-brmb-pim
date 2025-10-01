package ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints.impl;

import java.net.URI;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

import org.springframework.beans.factory.annotation.Autowired;

import ca.bc.gov.nrs.common.wfone.rest.resource.MessageListRsrc;
import ca.bc.gov.nrs.wfone.common.rest.endpoints.BaseEndpointsImpl;
import ca.bc.gov.nrs.wfone.common.service.api.ValidationFailureException;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints.VerifiedYieldContractListEndpoint;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints.security.Scopes;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.parameters.validation.ParameterValidator;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.VerifiedYieldContractRsrc;
import ca.bc.gov.mal.cirras.underwriting.service.api.v1.CirrasVerifiedYieldService;

public class VerifiedYieldContractListEndpointImpl extends BaseEndpointsImpl implements VerifiedYieldContractListEndpoint {

	@Autowired
	private CirrasVerifiedYieldService cirrasVerifiedYieldService;

	@Autowired
	private ParameterValidator parameterValidator;
	
	public void setCirrasVerifiedYieldService(CirrasVerifiedYieldService cirrasVerifiedYieldService) {
		this.cirrasVerifiedYieldService = cirrasVerifiedYieldService;
	}

	public void setParameterValidator(ParameterValidator parameterValidator) {
		this.parameterValidator = parameterValidator;
	}
	
	@Override
	public Response createVerifiedYieldContract(VerifiedYieldContractRsrc verifiedYieldContract) {

		Response response = null;
		
		logRequest();

		if(!hasAuthority(Scopes.CREATE_VERIFIED_YIELD_CONTRACT)) {
			return Response.status(Status.FORBIDDEN).build();
		}

		try {

			VerifiedYieldContractRsrc result = (VerifiedYieldContractRsrc) cirrasVerifiedYieldService.createVerifiedYieldContract(
					verifiedYieldContract, 
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
