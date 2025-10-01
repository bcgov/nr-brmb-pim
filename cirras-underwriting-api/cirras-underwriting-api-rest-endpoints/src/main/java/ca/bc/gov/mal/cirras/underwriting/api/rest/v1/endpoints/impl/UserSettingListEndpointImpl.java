package ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints.impl;

import java.net.URI;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

import org.springframework.beans.factory.annotation.Autowired;

import ca.bc.gov.nrs.common.wfone.rest.resource.MessageListRsrc;
import ca.bc.gov.nrs.wfone.common.rest.endpoints.BaseEndpointsImpl;
import ca.bc.gov.nrs.wfone.common.service.api.NotFoundException;
import ca.bc.gov.nrs.wfone.common.service.api.ValidationFailureException;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints.UserSettingListEndpoint;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints.security.Scopes;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.parameters.validation.ParameterValidator;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.UserSettingRsrc;
import ca.bc.gov.mal.cirras.underwriting.service.api.v1.CirrasUnderwritingService;

public class UserSettingListEndpointImpl extends BaseEndpointsImpl implements UserSettingListEndpoint {
		
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
	public Response getUserSetting() {

		// Searches for UserSettingRsrc for the current user.
		
		Response response = null;
		
		logRequest();
		
		if(!hasAuthority(Scopes.SEARCH_USER_SETTING)) {
			return Response.status(Status.FORBIDDEN).build();
		}

		try {

			UserSettingRsrc result = (UserSettingRsrc) cirrasUnderwritingService.searchUserSetting(
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
	public Response createUserSetting(UserSettingRsrc userSetting) {

		Response response = null;
		
		logRequest();

		if(!hasAuthority(Scopes.CREATE_USER_SETTING)) {
			return Response.status(Status.FORBIDDEN).build();
		}

		try {
			
			//A user can only create its own user setting
			if(userSetting != null && userSetting.getLoginUserGuid() != null && userSetting.getLoginUserGuid().equals(getWebAdeAuthentication().getUserGuid())) {

				UserSettingRsrc result = (UserSettingRsrc) cirrasUnderwritingService.createUserSetting(
						userSetting, 
						getFactoryContext(), 
						getWebAdeAuthentication());
	
				URI createdUri = URI.create(result.getSelfLink());
	
				response = Response.created(createdUri).entity(result).tag(result.getUnquotedETag()).build();
			} else {
				return Response.status(Status.FORBIDDEN).build();
			}

		} catch(ValidationFailureException e) {
			response = Response.status(Status.BAD_REQUEST).entity(new MessageListRsrc(e.getValidationErrors())).build();
		} catch (Throwable t) {
			response = getInternalServerErrorResponse(t);
		}
		
		logResponse(response);

		return response;
	}

}
