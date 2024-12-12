package ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints.impl;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.springframework.beans.factory.annotation.Autowired;

import ca.bc.gov.nrs.wfone.common.rest.endpoints.BaseEndpointsImpl;
import ca.bc.gov.nrs.wfone.common.service.api.NotFoundException;
import ca.bc.gov.nrs.wfone.common.service.api.ServiceException;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints.InventoryContractReportEndpoint;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints.security.Scopes;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.parameters.validation.ParameterValidator;
import ca.bc.gov.mal.cirras.underwriting.service.api.v1.CirrasInventoryService;
import ca.bc.gov.mal.cirras.underwriting.service.api.v1.util.InventoryServiceEnums.InsurancePlans;

public class InventoryContractReportEndpointImpl extends BaseEndpointsImpl implements InventoryContractReportEndpoint {

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
	public Response generateInventoryReport(
			String cropYear,
			String insurancePlanId, 
			String officeId,
			String policyStatusCode,
			String policyNumber,
			String growerInfo,
			String sortColumn,
			String policyIds,
			String reportType) {

		Response response = null;
		
		logRequest();
		
		if(!hasAuthority(Scopes.PRINT_INVENTORY_CONTRACT)) {
			return Response.status(Status.FORBIDDEN).build();
		}
		
		try {

			String outputFileName = null;
			if ( InsurancePlans.GRAIN.getInsurancePlanId().toString().equals(toString(insurancePlanId)) ) {
				outputFileName = "inventory_grain.pdf";
			} else if ( InsurancePlans.FORAGE.getInsurancePlanId().toString().equals(toString(insurancePlanId)) ) {
				outputFileName = "inventory_forage.pdf";				
			} else {
				throw new ServiceException("Insurance Plan must be GRAIN or FORAGE");
			}
			
			byte[] result = cirrasInventoryService.generateInvReport(
					toInteger(cropYear),
					toInteger(insurancePlanId),
					toInteger(officeId),
					toString(policyStatusCode),
					toString(policyNumber),
					toString(growerInfo),
					toString(sortColumn),
					toString(policyIds), 
					toString(reportType),
					getFactoryContext(), 
					getWebAdeAuthentication());
			
			response = Response.ok(result, "application/pdf").header("Content-Disposition", "attachment; filename=" + outputFileName).build();

		} catch (NotFoundException e) {
			response = Response.status(Status.NOT_FOUND).build();
			
		} catch (Throwable t) {
			response = getInternalServerErrorResponse(t);
		}
		
		logResponse(response);

		return response;
	}
	
}
