package ca.bc.gov.mal.cirras.underwriting.controllers.impl;

import java.util.ArrayList;
import java.util.List;

import jakarta.ws.rs.core.GenericEntity;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

import org.springframework.beans.factory.annotation.Autowired;

import ca.bc.gov.nrs.common.wfone.rest.resource.MessageListRsrc;
import ca.bc.gov.nrs.wfone.common.model.Message;
import ca.bc.gov.nrs.wfone.common.rest.endpoints.BaseEndpointsImpl;
import ca.bc.gov.mal.cirras.underwriting.controllers.UwContractListEndpoint;
import ca.bc.gov.mal.cirras.underwriting.controllers.scopes.Scopes;
import ca.bc.gov.mal.cirras.underwriting.controllers.parameters.PagingQueryParameters;
import ca.bc.gov.mal.cirras.underwriting.controllers.parameters.validation.ParameterValidator;
import ca.bc.gov.mal.cirras.underwriting.data.resources.UwContractListRsrc;
import ca.bc.gov.mal.cirras.underwriting.services.CirrasUnderwritingService;

public class UwContractListEndpointImpl extends BaseEndpointsImpl implements UwContractListEndpoint {
		
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
	public Response getUwContractList(
			String cropYear,
			String insurancePlanId, 
			String officeId,
			String policyStatusCode,
			String policyNumber,
			String growerInfo,
    		String datasetType,
			String sortColumn,
			String sortDirection,
			String pageNumber,
			String pageRowCount) {
		
		Response response = null;
		
		logRequest();
		
		if(!hasAuthority(Scopes.SEARCH_UWCONTRACTS)) {
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

				UwContractListRsrc results = (UwContractListRsrc) cirrasUnderwritingService.getUwContractList(
						toInteger(cropYear),
						toInteger(insurancePlanId),
						toInteger(officeId),
						toString(policyStatusCode),
						toString(policyNumber),
						toString(growerInfo),
						toString(datasetType),
						toString(sortColumn),
						toString(sortDirection),
						toInteger(pageNumber), 
						toInteger(pageRowCount), 
						getFactoryContext(), 
						getWebAdeAuthentication());



				GenericEntity<UwContractListRsrc> entity = new GenericEntity<UwContractListRsrc>(results) {
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

}
