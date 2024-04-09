package ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints.impl;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.springframework.beans.factory.annotation.Autowired;

import ca.bc.gov.nrs.wfone.common.rest.endpoints.BaseEndpointsImpl;
import ca.bc.gov.nrs.wfone.common.service.api.NotFoundException;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints.DopYieldContractReportEndpoint;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints.security.Scopes;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.parameters.validation.ParameterValidator;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.InventoryContractListRsrc;
import ca.bc.gov.mal.cirras.underwriting.service.api.v1.CirrasDopYieldService;

public class DopYieldContractReportEndpointImpl extends BaseEndpointsImpl implements DopYieldContractReportEndpoint {

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
	public Response generateDopReport(
			String cropYear,
			String insurancePlanId, 
			String officeId,
			String policyStatusCode,
			String policyNumber,
			String growerInfo,
			String sortColumn,
			String policyIds) {

		Response response = null;
		
		logRequest();
		
		if(!hasAuthority(Scopes.PRINT_DOP_YIELD_CONTRACT)) {
			return Response.status(Status.FORBIDDEN).build();
		}
		
		try {
			
			byte[] result = cirrasDopYieldService.generateDopReport(
					toString(cropYear),
					toString(insurancePlanId),
					toString(officeId),
					toString(policyStatusCode),
					toString(policyNumber),
					toString(growerInfo),
					toString(sortColumn),
					toString(policyIds), 
					getFactoryContext(), 
					getWebAdeAuthentication());
			
			response = Response.ok(result, "application/pdf").header("Content-Disposition", "attachment; filename=dop.pdf").build();

		} catch (NotFoundException e) {
			response = Response.status(Status.NOT_FOUND).build();
			
		} catch (Throwable t) {
			response = getInternalServerErrorResponse(t);
		}
		
		logResponse(response);

		return response;
	}
	
}
