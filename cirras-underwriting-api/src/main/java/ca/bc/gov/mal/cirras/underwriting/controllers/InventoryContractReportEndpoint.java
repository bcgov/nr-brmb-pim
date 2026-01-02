package ca.bc.gov.mal.cirras.underwriting.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import ca.bc.gov.mal.cirras.underwriting.controllers.scopes.Scopes;
import ca.bc.gov.mal.cirras.underwriting.services.CirrasInventoryService;
import ca.bc.gov.mal.cirras.underwriting.services.utils.InventoryServiceEnums.InsurancePlans;
import ca.bc.gov.nrs.common.wfone.rest.resource.HeaderConstants;
import ca.bc.gov.nrs.common.wfone.rest.resource.MessageListRsrc;
import ca.bc.gov.nrs.wfone.common.rest.endpoints.BaseEndpointsImpl;
import ca.bc.gov.nrs.wfone.common.service.api.NotFoundException;
import ca.bc.gov.nrs.wfone.common.service.api.ServiceException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.extensions.Extension;
import io.swagger.v3.oas.annotations.extensions.ExtensionProperty;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

@RestController
@Path("/inventoryContracts/report")
public class InventoryContractReportEndpoint extends BaseEndpointsImpl {

	@Autowired
	private CirrasInventoryService cirrasInventoryService;
	
	public void setCirrasInventoryService(CirrasInventoryService cirrasInventoryService) {
		this.cirrasInventoryService = cirrasInventoryService;
	}

	@Operation(operationId = "Generate the inventory report.", summary = "Generate the inventory report", security = @SecurityRequirement(name = "Webade-OAUTH2", scopes = {Scopes.PRINT_INVENTORY_CONTRACT}), extensions = {@Extension(properties = {@ExtensionProperty(name = "auth-type", value = "#{wso2.x-auth-type.none}"), @ExtensionProperty(name = "throttling-tier", value = "Unlimited") })})
	@Parameters({
		@Parameter(name = HeaderConstants.REQUEST_ID_HEADER, description = HeaderConstants.REQUEST_ID_HEADER_DESCRIPTION, required = false, schema = @Schema(implementation = String.class), in = ParameterIn.HEADER),
		@Parameter(name = HeaderConstants.VERSION_HEADER, description = HeaderConstants.VERSION_HEADER_DESCRIPTION, required = false, schema = @Schema(implementation = Integer.class), in = ParameterIn.HEADER),
		@Parameter(name = HeaderConstants.CACHE_CONTROL_HEADER, description = HeaderConstants.CACHE_CONTROL_DESCRIPTION, required = false, schema = @Schema(implementation = String.class), in = ParameterIn.HEADER),
		@Parameter(name = HeaderConstants.PRAGMA_HEADER, description = HeaderConstants.PRAGMA_HEADER_DESCRIPTION, required = false, schema = @Schema(implementation = String.class), in = ParameterIn.HEADER),
		@Parameter(name = HeaderConstants.AUTHORIZATION_HEADER, description = HeaderConstants.AUTHORIZATION_HEADER_DESCRIPTION, required = false, schema = @Schema(implementation = String.class), in = ParameterIn.HEADER) 
	})
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = byte[].class)), headers = @Header(name = HeaderConstants.ETAG_HEADER, schema = @Schema(implementation = String.class), description = HeaderConstants.ETAG_DESCRIPTION)),
		@ApiResponse(responseCode = "404", description = "Not Found"),
		@ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(implementation = MessageListRsrc.class))) })
	@GET
	@Produces({ MediaType.WILDCARD })
	public Response generateInventoryReport(
		@Parameter(description = "Filter the results by the year") @QueryParam("cropYear") String cropYear,
		@Parameter(description = "Filter the results by the insurance plan", required = true) @QueryParam("insurancePlanId") String insurancePlanId,
		@Parameter(description = "Filter the results by the office") @QueryParam("officeId") String officeId,
		@Parameter(description = "Filter the results by the policy status") @QueryParam("policyStatusCode") String policyStatusCode,
		@Parameter(description = "Filter the results by the policy number") @QueryParam("policyNumber") String policyNumber,
		@Parameter(description = "Filter the results by the grower info (Name, Number, Phone, Email") @QueryParam("growerInfo") String growerInfo,
		@Parameter(description = "Sort by column") @QueryParam("sortColumn") String sortColumn,
		@Parameter(description = "IDs of policies to be included") @QueryParam("policyIds") String policyIds,
		@Parameter(description = "Type of report: unseeded or seeded (GRAIN only)") @QueryParam("reportType") String reportType
	){

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
			} else if ( InsurancePlans.BERRIES.getInsurancePlanId().toString().equals(toString(insurancePlanId)) ) {
				outputFileName = "inventory_berries.pdf";				
			} else {
				throw new ServiceException("Insurance Plan must be GRAIN, FORAGE or BERRIES");
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
