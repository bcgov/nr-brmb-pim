package ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints.impl;

import jakarta.ws.rs.core.EntityTag;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.ResponseBuilder;
import jakarta.ws.rs.core.Response.Status;

import org.springframework.beans.factory.annotation.Autowired;

import ca.bc.gov.nrs.common.wfone.rest.resource.MessageListRsrc;
import ca.bc.gov.nrs.wfone.common.rest.endpoints.BaseEndpointsImpl;
import ca.bc.gov.nrs.wfone.common.service.api.ConflictException;
import ca.bc.gov.nrs.wfone.common.service.api.ForbiddenException;
import ca.bc.gov.nrs.wfone.common.service.api.NotFoundException;
import ca.bc.gov.nrs.wfone.common.service.api.ValidationFailureException;
import ca.bc.gov.mal.cirras.underwriting.service.api.v1.CirrasUnderwritingService;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.UserSettingRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints.UserSettingEndpoint;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints.security.Scopes;

public class UserSettingEndpointImpl extends BaseEndpointsImpl implements UserSettingEndpoint {
		
	@Autowired
	private CirrasUnderwritingService cirrasUnderwritingService;
	
	
	@Override
	public Response getUserSetting(final String userSettingGuid) {
		
		Response response = null;
		
		logRequest();
		
		if(!hasAuthority(Scopes.GET_USER_SETTING)) {
			return Response.status(Status.FORBIDDEN).build();
		}
		
		try {
			UserSettingRsrc result = (UserSettingRsrc) cirrasUnderwritingService.getUserSetting(
					userSettingGuid,
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


	@Override
	public Response updateUserSetting(String userSettingGuid, UserSettingRsrc userSetting) {

		Response response = null;
		
		logRequest();
		
		if(!hasAuthority(Scopes.UPDATE_USER_SETTING)) {
			return Response.status(Status.FORBIDDEN).build();
		}
			
		try {
			UserSettingRsrc currentUserSetting = (UserSettingRsrc) cirrasUnderwritingService.getUserSetting(
					userSettingGuid, 
					getFactoryContext(), 
					getWebAdeAuthentication());

			EntityTag currentTag = EntityTag.valueOf(currentUserSetting.getQuotedETag());

			ResponseBuilder responseBuilder = this.evaluatePreconditions(currentTag);

			if (responseBuilder == null) {
				// Preconditions Are Met
				
				//A user can only update its own user setting
				if(currentUserSetting.getLoginUserGuid().equals(getWebAdeAuthentication().getUserGuid())) {
				
					String optimisticLock = getIfMatchHeader();
	
					UserSettingRsrc result = (UserSettingRsrc)cirrasUnderwritingService.updateUserSetting(
							userSettingGuid,
							optimisticLock, 
							userSetting, 
							getFactoryContext(), 
							getWebAdeAuthentication());
	
					response = Response.ok(result).tag(result.getUnquotedETag()).build();
				} else {
					return Response.status(Status.FORBIDDEN).build();
				}
				
			} else {
				// Preconditions Are NOT Met

				response = responseBuilder.tag(currentTag).build();
			}
		} catch (ForbiddenException e) {
			response = Response.status(Status.FORBIDDEN).build();
		} catch(ValidationFailureException e) {
			response = Response.status(Status.BAD_REQUEST).entity(new MessageListRsrc(e.getValidationErrors())).build();
		} catch (ConflictException e) {
			response = Response.status(Status.CONFLICT).entity(e.getMessage()).build();
		} catch (NotFoundException e) {
			response = Response.status(Status.NOT_FOUND).build();
		} catch (Throwable t) {
			response = getInternalServerErrorResponse(t);
		}
		
		logResponse(response);

		return response;
	}	

	@Override
	public Response deleteUserSetting(String userSettingGuid) {

		Response response = null;
		
		logRequest();
		
		if(!hasAuthority(Scopes.DELETE_USER_SETTING)) {
			return Response.status(Status.FORBIDDEN).build();
		}
			
		try {
			UserSettingRsrc current = (UserSettingRsrc) cirrasUnderwritingService.getUserSetting(
					userSettingGuid, 
					getFactoryContext(), 
					getWebAdeAuthentication());

			EntityTag currentTag = EntityTag.valueOf(current.getQuotedETag());

			ResponseBuilder responseBuilder = this.evaluatePreconditions(currentTag);

			if (responseBuilder == null) {
				// Preconditions Are Met
				
				//A user can only delete its own user setting
				if(current.getLoginUserGuid().equals(getWebAdeAuthentication().getUserGuid())) {
					String optimisticLock = getIfMatchHeader();

					cirrasUnderwritingService.deleteUserSetting(
							userSettingGuid, 
							optimisticLock, 
							getWebAdeAuthentication());

					response = Response.status(204).build();
					
				} else {
					return Response.status(Status.FORBIDDEN).build();
				}
				
			} else {
				// Preconditions Are NOT Met

				response = responseBuilder.tag(currentTag).build();
			}
		} catch (ForbiddenException e) {
			response = Response.status(Status.FORBIDDEN).build();
		} catch (ConflictException e) {
			response = Response.status(Status.CONFLICT).entity(e.getMessage()).build();
		} catch (NotFoundException e) {
			response = Response.status(Status.NOT_FOUND).build();
		} catch (Throwable t) {
			response = getInternalServerErrorResponse(t);
		}
		
		logResponse(response);

		return response;
	}

}
