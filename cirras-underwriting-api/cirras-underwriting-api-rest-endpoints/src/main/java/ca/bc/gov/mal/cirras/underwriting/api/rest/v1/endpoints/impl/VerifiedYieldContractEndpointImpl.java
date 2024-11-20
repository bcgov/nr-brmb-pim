package ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints.impl;

import javax.ws.rs.core.EntityTag;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;

import org.springframework.beans.factory.annotation.Autowired;

import ca.bc.gov.nrs.common.wfone.rest.resource.MessageListRsrc;
import ca.bc.gov.nrs.wfone.common.rest.endpoints.BaseEndpointsImpl;
import ca.bc.gov.nrs.wfone.common.service.api.ConflictException;
import ca.bc.gov.nrs.wfone.common.service.api.ForbiddenException;
import ca.bc.gov.nrs.wfone.common.service.api.NotFoundException;
import ca.bc.gov.nrs.wfone.common.service.api.ValidationFailureException;
import ca.bc.gov.mal.cirras.underwriting.service.api.v1.CirrasVerifiedYieldService;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.VerifiedYieldContractRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints.VerifiedYieldContractEndpoint;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints.security.Scopes;

public class VerifiedYieldContractEndpointImpl extends BaseEndpointsImpl implements VerifiedYieldContractEndpoint {
		
	@Autowired
	private CirrasVerifiedYieldService cirrasVerifiedYieldService;
	
	
	@Override
	public Response getVerifiedYieldContract(final String verifiedYieldContractGuid) {
		
		Response response = null;
		
		logRequest();
		
		if(!hasAuthority(Scopes.GET_VERIFIED_YIELD_CONTRACT)) {
			return Response.status(Status.FORBIDDEN).build();
		}
		
		try {
			VerifiedYieldContractRsrc result = (VerifiedYieldContractRsrc) cirrasVerifiedYieldService.getVerifiedYieldContract(
					verifiedYieldContractGuid,
					getFactoryContext(), 
					getWebAdeAuthentication());
			response = Response.ok(result).tag(result.getUnquotedETag()).build();

		} catch (NotFoundException e) {
			response = Response.status(Status.NOT_FOUND).build();
			
		} catch (Throwable t) {
			response = getInternalServerErrorResponse(t);
		}
		
		logResponse(response);

		return response;
	}


	@Override
	public Response updateVerifiedYieldContract(String declaredYieldContractGuid, VerifiedYieldContractRsrc verifiedYieldContract) {

		Response response = null;
		
		logRequest();
		
		if(!hasAuthority(Scopes.UPDATE_VERIFIED_YIELD_CONTRACT)) {
			return Response.status(Status.FORBIDDEN).build();
		}
			
		try {
			VerifiedYieldContractRsrc currentVerifiedYieldContract = (VerifiedYieldContractRsrc) cirrasVerifiedYieldService.getVerifiedYieldContract(
					declaredYieldContractGuid, 
					getFactoryContext(), 
					getWebAdeAuthentication());

			EntityTag currentTag = EntityTag.valueOf(currentVerifiedYieldContract.getQuotedETag());

			ResponseBuilder responseBuilder = this.evaluatePreconditions(currentTag);

			if (responseBuilder == null) {
				// Preconditions Are Met
				
				String optimisticLock = getIfMatchHeader();

				VerifiedYieldContractRsrc result = (VerifiedYieldContractRsrc)cirrasVerifiedYieldService.updateVerifiedYieldContract(
						declaredYieldContractGuid,
						optimisticLock, 
						verifiedYieldContract, 
						getFactoryContext(), 
						getWebAdeAuthentication());

				response = Response.ok(result).tag(result.getUnquotedETag()).build();
				
			} else {
				// Preconditions Are NOT Met

				response = responseBuilder.tag(currentTag).build();
			}
		} catch (ForbiddenException e) {
			response = Response.status(Status.FORBIDDEN).build();
		} catch(ValidationFailureException e) {
			response = Response.status(Status.BAD_REQUEST).entity(new MessageListRsrc(e.getValidationErrors())).build();
		} catch (ConflictException e) {
			response = Response.status(Status.CONFLICT).entity(e.getMessage()).build();
		} catch (NotFoundException e) {
			response = Response.status(Status.NOT_FOUND).build();
		} catch (Throwable t) {
			response = getInternalServerErrorResponse(t);
		}
		
		logResponse(response);

		return response;
	}	

	@Override
	public Response deleteVerifiedYieldContract(String declaredYieldContractGuid) {

		Response response = null;
		
		logRequest();
		
		if(!hasAuthority(Scopes.DELETE_VERIFIED_YIELD_CONTRACT)) {
			return Response.status(Status.FORBIDDEN).build();
		}
			
		try {
			VerifiedYieldContractRsrc current = (VerifiedYieldContractRsrc) cirrasVerifiedYieldService.getVerifiedYieldContract(
					declaredYieldContractGuid, 
					getFactoryContext(), 
					getWebAdeAuthentication());

			EntityTag currentTag = EntityTag.valueOf(current.getQuotedETag());

			ResponseBuilder responseBuilder = this.evaluatePreconditions(currentTag);

			if (responseBuilder == null) {
				// Preconditions Are Met
				
				String optimisticLock = getIfMatchHeader();

				cirrasVerifiedYieldService.deleteVerifiedYieldContract(
						declaredYieldContractGuid, 
						optimisticLock, 
						getWebAdeAuthentication());

				response = Response.status(204).build();
			} else {
				// Preconditions Are NOT Met

				response = responseBuilder.tag(currentTag).build();
			}
		} catch (ForbiddenException e) {
			response = Response.status(Status.FORBIDDEN).build();
		} catch (ConflictException e) {
			response = Response.status(Status.CONFLICT).entity(e.getMessage()).build();
		} catch (NotFoundException e) {
			response = Response.status(Status.NOT_FOUND).build();
		} catch (Throwable t) {
			response = getInternalServerErrorResponse(t);
		}
		
		logResponse(response);

		return response;
	}

}
