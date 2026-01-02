package ca.bc.gov.mal.cirras.underwriting.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import ca.bc.gov.mal.cirras.underwriting.data.resources.CropCommodityListRsrc;
import ca.bc.gov.mal.cirras.underwriting.services.CirrasCommodityService;
import ca.bc.gov.nrs.common.wfone.rest.resource.HeaderConstants;
import ca.bc.gov.nrs.common.wfone.rest.resource.MessageListRsrc;
import ca.bc.gov.nrs.wfone.common.rest.endpoints.BaseEndpointsImpl;
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
import jakarta.ws.rs.core.GenericEntity;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@RestController
@Path("/cropcommodities")
public class CropCommodityListEndpoint extends BaseEndpointsImpl {
		
	@Autowired
	private CirrasCommodityService cirrasCommodityService;
	
	public void setCirrasCommodityService(CirrasCommodityService cirrasCommodityService) {
		this.cirrasCommodityService = cirrasCommodityService;
	}
	

	@Operation(operationId = "Get list of crop commodities.", summary = "Get list of crop commodities.", security = @SecurityRequirement(name = "Webade-OAUTH2", scopes = {"GETTOPLEVEL"}), extensions = {@Extension(properties = {@ExtensionProperty(name = "auth-type", value = "#{wso2.x-auth-type.none}"), @ExtensionProperty(name = "throttling-tier", value = "Unlimited") })})
	@Parameters({
		@Parameter(name = HeaderConstants.REQUEST_ID_HEADER, description = HeaderConstants.REQUEST_ID_HEADER_DESCRIPTION, required = false, schema = @Schema(implementation = String.class), in = ParameterIn.HEADER),
		@Parameter(name = HeaderConstants.VERSION_HEADER, description = HeaderConstants.VERSION_HEADER_DESCRIPTION, required = false, schema = @Schema(implementation = Integer.class), in = ParameterIn.HEADER),
		@Parameter(name = HeaderConstants.CACHE_CONTROL_HEADER, description = HeaderConstants.CACHE_CONTROL_DESCRIPTION, required = false, schema = @Schema(implementation = String.class), in = ParameterIn.HEADER),
		@Parameter(name = HeaderConstants.PRAGMA_HEADER, description = HeaderConstants.PRAGMA_HEADER_DESCRIPTION, required = false, schema = @Schema(implementation = String.class), in = ParameterIn.HEADER),
		@Parameter(name = HeaderConstants.AUTHORIZATION_HEADER, description = HeaderConstants.AUTHORIZATION_HEADER_DESCRIPTION, required = false, schema = @Schema(implementation = String.class), in = ParameterIn.HEADER) 
	})
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = CropCommodityListRsrc.class)), headers = @Header(name = HeaderConstants.ETAG_HEADER, schema = @Schema(implementation = String.class), description = HeaderConstants.ETAG_DESCRIPTION)),
		@ApiResponse(responseCode = "404", description = "Not Found"),
		@ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(implementation = MessageListRsrc.class))) 
	})
	@GET
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response getCropCommodityList(
		@Parameter(description = "Filter the results by the insurance plan") @QueryParam("insurancePlanId") String insurancePlanId,
		@Parameter(description = "Filter the results by the year") @QueryParam("cropYear") String cropYear,
		@Parameter(description = "Filter the results by the type of commodity (NULL = all, INV = Inventory, YLD = Yield, UW = Underwriting") @QueryParam("commodityType") String commodityType,
		@Parameter(description = "Y = load child table records, N = don't load them") @QueryParam("loadChildren") String loadChildren
	){
		
		Response response = null;
		
		logRequest();

		try {

			CropCommodityListRsrc results = (CropCommodityListRsrc) cirrasCommodityService.getCropCommodityList(
					toInteger(insurancePlanId),
					toInteger(cropYear),
					toString(commodityType),
					toBoolean(loadChildren),
					getFactoryContext(), 
					getWebAdeAuthentication());

				GenericEntity<CropCommodityListRsrc> entity = new GenericEntity<CropCommodityListRsrc>(results) {
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
