package ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints.impl;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import ca.bc.gov.nrs.common.wfone.rest.resource.MessageListRsrc;
import ca.bc.gov.nrs.wfone.common.model.Message;
import ca.bc.gov.nrs.wfone.common.rest.endpoints.BaseEndpointsImpl;
import ca.bc.gov.nrs.wfone.common.service.api.ValidationFailureException;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints.InventoryContractListEndpoint;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints.security.Scopes;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.parameters.PagingQueryParameters;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.parameters.validation.ParameterValidator;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.InventoryContractListRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.InventoryContractRsrc;
import ca.bc.gov.mal.cirras.underwriting.service.api.v1.CirrasInventoryService;

public class InventoryContractListEndpointImpl extends BaseEndpointsImpl implements InventoryContractListEndpoint {

	private static final Logger logger = LoggerFactory.getLogger(InventoryContractListEndpointImpl.class);	
	
	@Autowired
	private CirrasInventoryService cirrasInventoryService;

	@Autowired
	private ParameterValidator parameterValidator;
	
	public void setCirrasInventoryService(CirrasInventoryService cirrasInventoryService) {
		this.cirrasInventoryService = cirrasInventoryService;
	}

	public void setParameterValidator(ParameterValidator parameterValidator) {
		this.parameterValidator = parameterValidator;
	}
	
	@Override
	public Response createInventoryContract(InventoryContractRsrc inventoryContract) {
		logger.debug("<createInventoryContract");
		Response response = null;
		
		logRequest();

		if(!hasAuthority(Scopes.CREATE_INVENTORY_CONTRACT)) {
			return Response.status(Status.FORBIDDEN).build();
		}

		try {

			InventoryContractRsrc result = (InventoryContractRsrc) cirrasInventoryService.createInventoryContract(
					inventoryContract, 
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

		logger.debug(">createInventoryContract " + response);
		return response;
	}

	@Override
	public Response getInventoryContractList(
			String cropYear,
			String insurancePlanId, 
			String officeId,
			String policyStatusCode,
			String policyNumber,
			String growerInfo,
			String sortColumn,
			String inventoryContractGuids) {
		
		Response response = null;
		
		logRequest();
		
		if(!hasAuthority(Scopes.PRINT_INVENTORY_CONTRACT)) {
			return Response.status(Status.FORBIDDEN).build();
		}

		try {
			
			InventoryContractListRsrc results = (InventoryContractListRsrc) cirrasInventoryService.getInventoryContractList(
					toInteger(cropYear),
					toInteger(insurancePlanId),
					toInteger(officeId),
					toString(policyStatusCode),
					toString(policyNumber),
					toString(growerInfo),
					toString(sortColumn),
					inventoryContractGuids, 
					getFactoryContext(), 
					getWebAdeAuthentication());



			GenericEntity<InventoryContractListRsrc> entity = new GenericEntity<InventoryContractListRsrc>(results) {
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
