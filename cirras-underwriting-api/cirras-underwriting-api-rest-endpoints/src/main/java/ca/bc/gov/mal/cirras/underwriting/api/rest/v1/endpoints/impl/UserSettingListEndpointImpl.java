package ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints.impl;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.springframework.beans.factory.annotation.Autowired;

import ca.bc.gov.nrs.wfone.common.rest.endpoints.BaseEndpointsImpl;
import ca.bc.gov.nrs.wfone.common.service.api.NotFoundException;
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

}
