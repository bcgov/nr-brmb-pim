package ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints.impl;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import jakarta.ws.rs.core.GenericEntity;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

import org.springframework.beans.factory.annotation.Autowired;

import ca.bc.gov.nrs.common.wfone.rest.resource.MessageListRsrc;
import ca.bc.gov.nrs.wfone.common.model.Message;
import ca.bc.gov.nrs.wfone.common.rest.endpoints.BaseEndpointsImpl;
import ca.bc.gov.nrs.wfone.common.service.api.ValidationFailureException;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints.LegalLandListEndpoint;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints.security.Scopes;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.parameters.PagingQueryParameters;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.parameters.validation.ParameterValidator;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.LegalLandListRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.LegalLandRsrc;
import ca.bc.gov.mal.cirras.underwriting.service.api.v1.CirrasInventoryService;
import ca.bc.gov.mal.cirras.underwriting.service.api.v1.CirrasUwLandManagementService;

public class LegalLandListEndpointImpl extends BaseEndpointsImpl implements LegalLandListEndpoint {
		
	@Autowired
	private CirrasInventoryService cirrasInventoryService;
	
	@Autowired
	private CirrasUwLandManagementService cirrasUwLandManagementService;
	
	@Autowired
	private ParameterValidator parameterValidator;
	
	public void setCirrasInventoryService(CirrasInventoryService cirrasInventoryService) {
		this.cirrasInventoryService = cirrasInventoryService;
	}

	public void setParameterValidator(ParameterValidator parameterValidator) {
		this.parameterValidator = parameterValidator;
	}
	
	@Override
	public Response getLegalLandList(
			String legalLocation, 
			String primaryPropertyIdentifier, 
			String growerInfo,
			String datasetType, 
			String isWildCardSearch, 
			String searchByLegalLocOrLegalDesc, 
			String sortColumn,
			String sortDirection, 
			String pageNumber, 
			String pageRowCount) {

		Response response = null;
		
		logRequest();
		
		if(!hasAuthority(Scopes.GET_LEGAL_LAND)) {
			return Response.status(Status.FORBIDDEN).build();
		}

		try {
			
			PagingQueryParameters parameters = new PagingQueryParameters();
	
			parameters.setPageNumber(pageNumber);
			parameters.setPageRowCount(pageRowCount);
			
			List<Message> validation = new ArrayList<>();
			validation.addAll(this.parameterValidator.validatePagingQueryParameters(parameters));
			
			MessageListRsrc validationMessages = new MessageListRsrc(validation);

			if (validationMessages.hasMessages()) {
				response = Response.status(Status.BAD_REQUEST).entity(validationMessages).build();
			} else {

				LegalLandListRsrc results = (LegalLandListRsrc) cirrasInventoryService.getLegalLandList(
						toStringWithoutDecode(legalLocation),
						toStringWithoutDecode(primaryPropertyIdentifier),
						toStringWithoutDecode(growerInfo),
						toStringWithoutDecode(datasetType),
						toBoolean(isWildCardSearch), 
						toBoolean(searchByLegalLocOrLegalDesc), 
						toStringWithoutDecode(sortColumn),
						toStringWithoutDecode(sortDirection),
						toInteger(pageNumber),
						toInteger(pageRowCount),
						getFactoryContext(), 
						getWebAdeAuthentication());
				
				GenericEntity<LegalLandListRsrc> entity = new GenericEntity<LegalLandListRsrc>(results) {
					/* do nothing */
				};
	
				response = Response.ok(entity).tag(results.getUnquotedETag()).build();
			}
			
		} catch (Throwable t) {
			response = getInternalServerErrorResponse(t);
		}
		
		logResponse(response);

		return response;
	}
	
	
	@Override
	public Response createLegalLand(LegalLandRsrc legalLand) {
		
		logRequest();
		Response response = null;
		
		if(!hasAuthority(Scopes.CREATE_LEGAL_LAND)) {
			return Response.status(Status.FORBIDDEN).build();
		}

		try {

			LegalLandRsrc result = (LegalLandRsrc) cirrasUwLandManagementService.createLegalLand(
					legalLand, 
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
	
	private static String toStringWithoutDecode(String value) {
		String result = null;
		if(value!=null&&value.trim().length()>0) {
			result = value;
		}
		return result;
	}
}
