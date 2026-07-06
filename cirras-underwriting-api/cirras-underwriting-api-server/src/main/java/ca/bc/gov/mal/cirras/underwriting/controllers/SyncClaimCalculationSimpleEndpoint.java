package ca.bc.gov.mal.cirras.underwriting.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import ca.bc.gov.mal.cirras.underwriting.controllers.scopes.Scopes;
import ca.bc.gov.mal.cirras.underwriting.data.resources.SyncClaimCalculationSimpleRsrc;
import ca.bc.gov.mal.cirras.underwriting.services.ClaimDataSyncService;
import ca.bc.gov.nrs.common.wfone.rest.resource.HeaderConstants;
import ca.bc.gov.nrs.common.wfone.rest.resource.MessageListRsrc;
import ca.bc.gov.nrs.wfone.common.rest.endpoints.BaseEndpointsImpl;
import ca.bc.gov.nrs.wfone.common.service.api.NotFoundException;
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
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

@Path("/syncClaimCalculationSimple")
public class SyncClaimCalculationSimpleEndpoint extends BaseEndpointsImpl {
	
	private static final Logger logger = LoggerFactory.getLogger(SyncClaimCalculationSimpleEndpoint.class);
	
	@Autowired
	private ClaimDataSyncService claimDataSyncService;

	@Operation(operationId = "Insert or Update a Claim Calulcation record", summary = "Insert or Update a Claim Calulcation record", security = @SecurityRequirement(name = "Webade-OAUTH2", scopes = {Scopes.UPDATE_SYNC_UNDERWRITING}),  extensions = {@Extension(properties = {@ExtensionProperty(name = "auth-type", value = "#{wso2.x-auth-type.none}"), @ExtensionProperty(name = "throttling-tier", value = "Unlimited") })})
	@Parameters({
		@Parameter(name = HeaderConstants.REQUEST_ID_HEADER, description = HeaderConstants.REQUEST_ID_HEADER_DESCRIPTION, required = false, schema = @Schema(implementation = String.class), in = ParameterIn.HEADER),
		@Parameter(name = HeaderConstants.VERSION_HEADER, description = HeaderConstants.VERSION_HEADER_DESCRIPTION, required = false, schema = @Schema(implementation = Integer.class), in = ParameterIn.HEADER),
		@Parameter(name = HeaderConstants.CACHE_CONTROL_HEADER, description = HeaderConstants.CACHE_CONTROL_DESCRIPTION, required = false, schema = @Schema(implementation = String.class), in = ParameterIn.HEADER),
		@Parameter(name = HeaderConstants.PRAGMA_HEADER, description = HeaderConstants.PRAGMA_HEADER_DESCRIPTION, required = false, schema = @Schema(implementation = String.class), in = ParameterIn.HEADER),
		@Parameter(name = HeaderConstants.AUTHORIZATION_HEADER, description = HeaderConstants.AUTHORIZATION_HEADER_DESCRIPTION, required = false, schema = @Schema(implementation = String.class), in = ParameterIn.HEADER) 
	})
	@ApiResponses(value = {
		@ApiResponse(responseCode = "204", description = "No Content"),	
		@ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = MessageListRsrc.class))),
		@ApiResponse(responseCode = "403", description = "Forbidden"),
		@ApiResponse(responseCode = "404", description = "Not Found"),
		@ApiResponse(responseCode = "409", description = "Conflict", content = @Content(schema = @Schema(implementation = MessageListRsrc.class))),
		@ApiResponse(responseCode = "412", description = "Precondition Failed"),
		@ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(implementation = MessageListRsrc.class))) })
	@POST
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response synchronizeClaimCalculationSimple(
		@Parameter(name = "syncClaimCalculationSimple", description = "The Claim Calculation Simple resource containing the values from the claims calculator.", required = true) SyncClaimCalculationSimpleRsrc resource
	){
		logger.debug("<synchronizeClaimCalculationSimple");
		Response response = null;
		
		logRequest();
		logResource(resource, "synchronizeClaimCalculationSimple");

		
		if(!hasAuthority(Scopes.UPDATE_SYNC_UNDERWRITING)) {
			return Response.status(Status.FORBIDDEN).build();
		}

		try {
			
			claimDataSyncService.synchronizeSyncClaimCalculationSimple(
					resource,
					getFactoryContext(), 
					getWebAdeAuthentication());

			response = Response.status(204).build();
		} catch (Throwable t) {
			response = getInternalServerErrorResponse(t);
		}
		
		logResponse(response);

		logger.debug(">synchronizeClaimCalculationSimple " + response);
		return response;
	}
	
	
	
	@Operation(operationId = "Get a Claim Calulcation record", summary = "Get a Claim Calulcation record", security = @SecurityRequirement(name = "Webade-OAUTH2", scopes = {Scopes.GET_CLAIM_CALCULATION_SIMPLE}), extensions = {@Extension(properties = {@ExtensionProperty(name = "auth-type", value = "#{wso2.x-auth-type.none}"), @ExtensionProperty(name = "throttling-tier", value = "Unlimited") })})
	@Parameters({
		@Parameter(name = HeaderConstants.REQUEST_ID_HEADER, description = HeaderConstants.REQUEST_ID_HEADER_DESCRIPTION, required = false, schema = @Schema(implementation = String.class), in = ParameterIn.HEADER),
		@Parameter(name = HeaderConstants.VERSION_HEADER, description = HeaderConstants.VERSION_HEADER_DESCRIPTION, required = false, schema = @Schema(implementation = Integer.class), in = ParameterIn.HEADER),
		@Parameter(name = HeaderConstants.CACHE_CONTROL_HEADER, description = HeaderConstants.CACHE_CONTROL_DESCRIPTION, required = false, schema = @Schema(implementation = String.class), in = ParameterIn.HEADER),
		@Parameter(name = HeaderConstants.PRAGMA_HEADER, description = HeaderConstants.PRAGMA_HEADER_DESCRIPTION, required = false, schema = @Schema(implementation = String.class), in = ParameterIn.HEADER),
		@Parameter(name = HeaderConstants.AUTHORIZATION_HEADER, description = HeaderConstants.AUTHORIZATION_HEADER_DESCRIPTION, required = false, schema = @Schema(implementation = String.class), in = ParameterIn.HEADER) 
	})
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = SyncClaimCalculationSimpleRsrc.class)), headers = @Header(name = HeaderConstants.ETAG_HEADER, schema = @Schema(implementation = String.class), description = HeaderConstants.ETAG_DESCRIPTION)),
		@ApiResponse(responseCode = "404", description = "Not Found"),
		@ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(implementation = MessageListRsrc.class))) })
	@GET
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response getSyncClaimCalculationSimple(
		@Parameter(description = "The id of the Claim Calulcation Berries record in the claims calculator.") @QueryParam("claimCalculationBerriesGuid") String claimCalculationBerriesGuid
	){
		logger.debug("<getSyncClaimCalculationSimple");

		Response response = null;
		
		logRequest();
		
		if(!hasAuthority(Scopes.GET_CLAIM_CALCULATION_SIMPLE)) {
			return Response.status(Status.FORBIDDEN).build();
		}
		
		try {
			
			SyncClaimCalculationSimpleRsrc result = (SyncClaimCalculationSimpleRsrc) claimDataSyncService.getSyncClaimCalculationSimple(
					claimCalculationBerriesGuid,
					getFactoryContext(), 
					getWebAdeAuthentication());

			response = Response.ok(result).tag(result.getUnquotedETag()).build();

		} catch (NotFoundException e) {
			response = Response.status(Status.NOT_FOUND).build();
			
		} catch (Throwable t) {
			response = getInternalServerErrorResponse(t);
		}
		
		logResponse(response);

		logger.debug(">getSyncClaimCalculationSimple");
		return response;
	}	

	@Operation(operationId = "Delete a Claim Calulcation record", summary = "Delete a Claim Calulcation record", security = @SecurityRequirement(name = "Webade-OAUTH2", scopes = {Scopes.DELETE_SYNC_UNDERWRITING}), extensions = {@Extension(properties = {@ExtensionProperty(name = "auth-type", value = "#{wso2.x-auth-type.none}"), @ExtensionProperty(name = "throttling-tier", value = "Unlimited") })})
	@Parameters({
		@Parameter(name = HeaderConstants.REQUEST_ID_HEADER, description = HeaderConstants.REQUEST_ID_HEADER_DESCRIPTION, required = false, schema = @Schema(implementation = String.class), in = ParameterIn.HEADER),
		@Parameter(name = HeaderConstants.VERSION_HEADER, description = HeaderConstants.VERSION_HEADER_DESCRIPTION, required = false, schema = @Schema(implementation = Integer.class), in = ParameterIn.HEADER),
		@Parameter(name = HeaderConstants.CACHE_CONTROL_HEADER, description = HeaderConstants.CACHE_CONTROL_DESCRIPTION, required = false, schema = @Schema(implementation = String.class), in = ParameterIn.HEADER),
		@Parameter(name = HeaderConstants.PRAGMA_HEADER, description = HeaderConstants.PRAGMA_HEADER_DESCRIPTION, required = false, schema = @Schema(implementation = String.class), in = ParameterIn.HEADER),
		@Parameter(name = HeaderConstants.AUTHORIZATION_HEADER, description = HeaderConstants.AUTHORIZATION_HEADER_DESCRIPTION, required = false, schema = @Schema(implementation = String.class), in = ParameterIn.HEADER),
		@Parameter(name = HeaderConstants.IF_MATCH_HEADER, description = HeaderConstants.IF_MATCH_DESCRIPTION, required = true, schema = @Schema(implementation = String.class), in = ParameterIn.HEADER)
	})
	@ApiResponses(value = { @ApiResponse(responseCode = "204", description = "No Content"),
		@ApiResponse(responseCode = "403", description = "Forbidden"),
		@ApiResponse(responseCode = "404", description = "Not Found"),
		@ApiResponse(responseCode = "409", description = "Conflict"),
		@ApiResponse(responseCode = "412", description = "Precondition Failed"),
		@ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(implementation = MessageListRsrc.class))) })
	@DELETE
	public Response deleteSyncClaimCalculationSimple(
		@Parameter(description = "The id of the Claim Calulcation Berries record in the claims calculator.") @QueryParam("claimCalculationBerriesGuid") String claimCalculationBerriesGuid
	){
		logger.debug("<deleteSyncClaimCalculationSimple");

		Response response = null;
		
		logRequest();
		
		if(!hasAuthority(Scopes.DELETE_SYNC_UNDERWRITING)) {
			return Response.status(Status.FORBIDDEN).build();
		}
			
		try {
			SyncClaimCalculationSimpleRsrc resource = (SyncClaimCalculationSimpleRsrc) claimDataSyncService.getSyncClaimCalculationSimple(
					claimCalculationBerriesGuid,
					getFactoryContext(), 
					getWebAdeAuthentication());
			
			if(resource != null) {
				claimDataSyncService.deleteSyncClaimCalculationSimple(claimCalculationBerriesGuid, getFactoryContext(), getWebAdeAuthentication());
			}
			response = Response.status(204).build();
		} catch (NotFoundException e) {
			response = Response.status(Status.NOT_FOUND).build();
		} catch (Throwable t) {
			response = getInternalServerErrorResponse(t);
		}
		
		logResponse(response);

		logger.debug(">deleteSyncClaimCalculationSimple " + response);
		return response;
	}	

	private void logResource(SyncClaimCalculationSimpleRsrc resource, String prefix) {

		StringBuilder msgBldr = new StringBuilder();
		
		if ( resource == null ) {
			msgBldr.append(prefix)
		           .append(": Received Resource is null. ")
		    ;
			
		} else {
			
			String claimCalculationBerriesGuid = "ClaimCalculationBerriesGuid missing";
			if(resource.getSyncClaimCalculationBerries() != null) {
				claimCalculationBerriesGuid = resource.getSyncClaimCalculationBerries().getClaimCalculationBerriesGuid();
			}
			// Note that only non-sensitive data can be logged.
			msgBldr.append(prefix)
			       .append(": Received Resource: ")
			       .append("Type: SyncClaimCalculationSimpleRsrc")
			       .append(", ClaimCalculationGuid: ")
			       .append(resource.getClaimCalculationGuid())
			       .append(", ClaimCalculationBerriesGuid: ")
			       .append(claimCalculationBerriesGuid)
			       .append(", Transaction Type: ")
			       .append(resource.getTransactionType())
			;
		}
		
		logger.info(msgBldr.toString());
	}
	
	
}
