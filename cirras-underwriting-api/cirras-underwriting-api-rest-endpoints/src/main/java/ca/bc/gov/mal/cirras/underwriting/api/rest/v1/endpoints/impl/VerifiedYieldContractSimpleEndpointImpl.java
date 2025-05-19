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
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.VerifiedYieldContractSimpleRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints.VerifiedYieldContractEndpoint;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints.VerifiedYieldContractSimpleEndpoint;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints.security.Scopes;

public class VerifiedYieldContractSimpleEndpointImpl extends BaseEndpointsImpl implements VerifiedYieldContractSimpleEndpoint {
		
	@Autowired
	private CirrasVerifiedYieldService cirrasVerifiedYieldService;
	
	
//	@Override
//	public Response getVerifiedYieldContract(final String verifiedYieldContractGuid) {
//		
//		Response response = null;
//		
//		logRequest();
//		
//		if(!hasAuthority(Scopes.GET_VERIFIED_YIELD_CONTRACT)) {
//			return Response.status(Status.FORBIDDEN).build();
//		}
//		
//		try {
//			VerifiedYieldContractRsrc result = (VerifiedYieldContractRsrc) cirrasVerifiedYieldService.getVerifiedYieldContract(
//					verifiedYieldContractGuid,
//					getFactoryContext(), 
//					getWebAdeAuthentication());
//			response = Response.ok(result).tag(result.getUnquotedETag()).build();
//
//		} catch (NotFoundException e) {
//			response = Response.status(Status.NOT_FOUND).build();
//			
//		} catch (Throwable t) {
//			response = getInternalServerErrorResponse(t);
//		}
//		
//		logResponse(response);
//
//		return response;
//	}

	@Override
	public Response getVerifiedYieldContractSimple(
			String contractId, 
			String cropYear, 
			String commodityId,
			String isPedigreeInd, 
			String loadVerifiedYieldContractCommodities,
			String loadVerifiedYieldAmendments, 
			String loadVerifiedYieldSummaries, 
			String loadVerifiedYieldGrainBasket) {

		Response response = null;
		
		logRequest();
		
		if(!hasAuthority(Scopes.GET_VERIFIED_YIELD_CONTRACT_SIMPLE)) {
			return Response.status(Status.FORBIDDEN).build();
		}
		
		try {
			VerifiedYieldContractSimpleRsrc result = (VerifiedYieldContractSimpleRsrc) cirrasVerifiedYieldService.getVerifiedYieldContractSimple(
					toInteger(contractId),
					toInteger(cropYear),
					toInteger(commodityId),
					toBoolean(isPedigreeInd),
					toBoolean(loadVerifiedYieldContractCommodities),
					toBoolean(loadVerifiedYieldAmendments),
					toBoolean(loadVerifiedYieldSummaries),
					toBoolean(loadVerifiedYieldGrainBasket),
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

}
