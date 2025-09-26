package ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import ca.bc.gov.nrs.common.wfone.rest.resource.HeaderConstants;
import ca.bc.gov.nrs.common.wfone.rest.resource.MessageListRsrc;
import ca.bc.gov.nrs.wfone.common.rest.endpoints.BaseEndpoints;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints.security.Scopes;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.VerifiedYieldContractSimpleRsrc;
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

@Path("/verifiedYieldContractSimple")
public interface VerifiedYieldContractSimpleEndpoint extends BaseEndpoints {
	
	@Operation(operationId = "Get the verified yield contract simple.", summary = "Get the verified yield contract simple", security = @SecurityRequirement(name = "Webade-OAUTH2", scopes = {Scopes.GET_VERIFIED_YIELD_CONTRACT_SIMPLE}), extensions = {@Extension(properties = {@ExtensionProperty(name = "auth-type", value = "#{wso2.x-auth-type.none}"), @ExtensionProperty(name = "throttling-tier", value = "Unlimited") })})
	@Parameters({
		@Parameter(name = HeaderConstants.REQUEST_ID_HEADER, description = HeaderConstants.REQUEST_ID_HEADER_DESCRIPTION, required = false, schema = @Schema(implementation = String.class), in = ParameterIn.HEADER),
		@Parameter(name = HeaderConstants.VERSION_HEADER, description = HeaderConstants.VERSION_HEADER_DESCRIPTION, required = false, schema = @Schema(implementation = Integer.class), in = ParameterIn.HEADER),
		@Parameter(name = HeaderConstants.CACHE_CONTROL_HEADER, description = HeaderConstants.CACHE_CONTROL_DESCRIPTION, required = false, schema = @Schema(implementation = String.class), in = ParameterIn.HEADER),
		@Parameter(name = HeaderConstants.PRAGMA_HEADER, description = HeaderConstants.PRAGMA_HEADER_DESCRIPTION, required = false, schema = @Schema(implementation = String.class), in = ParameterIn.HEADER),
		@Parameter(name = HeaderConstants.AUTHORIZATION_HEADER, description = HeaderConstants.AUTHORIZATION_HEADER_DESCRIPTION, required = false, schema = @Schema(implementation = String.class), in = ParameterIn.HEADER) 
	})
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = VerifiedYieldContractSimpleRsrc.class)), headers = @Header(name = HeaderConstants.ETAG_HEADER, schema = @Schema(implementation = String.class), description = HeaderConstants.ETAG_DESCRIPTION)),
		@ApiResponse(responseCode = "404", description = "Not Found"),
		@ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(implementation = MessageListRsrc.class))) })
	@GET
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	Response getVerifiedYieldContractSimple(
			@Parameter(description = "The id of the contract.", required = true) @QueryParam("contractId") String contractId,
			@Parameter(description = "The crop year of the contract.", required = true) @QueryParam("cropYear") String cropYear,
			@Parameter(description = "The id of the commodity. Can be null") @QueryParam("commodityId") String commodityId,
			@Parameter(description = "True if the commodity is pedigreed. Can be null") @QueryParam("isPedigreeInd") String isPedigreeInd,
			@Parameter(description = "True if the Verified Yield Contract Commodities need to be returned", required = true) @QueryParam("loadVerifiedYieldContractCommodities") String loadVerifiedYieldContractCommodities,
			@Parameter(description = "True if the Verified Yield Contract Amendments need to be returned", required = true) @QueryParam("loadVerifiedYieldAmendments") String loadVerifiedYieldAmendments,
			@Parameter(description = "True if the Verified Yield Contract Summaries need to be returned", required = true) @QueryParam("loadVerifiedYieldSummaries") String loadVerifiedYieldSummaries,
			@Parameter(description = "True if the Verified Yield Contract Grain Basket needs to be returned", required = true) @QueryParam("loadVerifiedYieldGrainBasket") String loadVerifiedYieldGrainBasket
	);
}
