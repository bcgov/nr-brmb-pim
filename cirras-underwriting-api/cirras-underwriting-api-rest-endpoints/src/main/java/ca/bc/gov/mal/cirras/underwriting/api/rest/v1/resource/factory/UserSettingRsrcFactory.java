package ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.factory;

import java.net.URI;

import jakarta.ws.rs.core.UriBuilder;

import ca.bc.gov.nrs.common.wfone.rest.resource.RelLink;
import ca.bc.gov.nrs.wfone.common.rest.endpoints.resource.factory.BaseResourceFactory;
import ca.bc.gov.nrs.wfone.common.service.api.model.factory.FactoryContext;
import ca.bc.gov.nrs.wfone.common.service.api.model.factory.FactoryException;
import ca.bc.gov.nrs.wfone.common.webade.authentication.WebAdeAuthentication;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints.UserSettingEndpoint;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints.UserSettingListEndpoint;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints.security.Scopes;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.UserSettingRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.types.ResourceTypes;
import ca.bc.gov.mal.cirras.underwriting.model.v1.UserSetting;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.UserSettingDto;
import ca.bc.gov.mal.cirras.underwriting.service.api.v1.model.factory.UserSettingFactory;

public class UserSettingRsrcFactory extends BaseResourceFactory implements UserSettingFactory { 

	@Override
	public UserSetting getDefaultUserSetting(FactoryContext context, WebAdeAuthentication authentication)
			throws FactoryException {

		UserSettingRsrc resource = new UserSettingRsrc();

		populateDefaultResource(resource, authentication);

		String eTag = getEtag(resource);
		resource.setETag(eTag);

		return resource;
	}

	private void populateDefaultResource(UserSettingRsrc resource, WebAdeAuthentication authentication) {
		resource.setFamilyName(authentication.getFamilyName());
		resource.setGivenName(authentication.getGivenName());
		resource.setLoginUserGuid(authentication.getUserGuid());
		resource.setLoginUserId(authentication.getUserId());
		resource.setLoginUserType(authentication.getUserTypeCode());
		resource.setPolicySearchCropYear(null);      // Default is current calendar year
		resource.setPolicySearchInsurancePlanId(null);
		resource.setPolicySearchInsurancePlanName(null);
		resource.setPolicySearchOfficeId(null);
		resource.setPolicySearchOfficeName(null);
		resource.setUserSettingGuid(null);
	}
	
	@Override
	public UserSetting getUserSetting(
			UserSettingDto dto, 
			FactoryContext context, 
			WebAdeAuthentication authentication
		) throws FactoryException {

		URI baseUri = getBaseURI(context);
		
		UserSettingRsrc resource = new UserSettingRsrc();

		populateResource(resource, dto);

		String eTag = getEtag(resource);
		resource.setETag(eTag);

		setSelfLink(dto.getUserSettingGuid(), resource, baseUri);
		setLinks(dto.getUserSettingGuid(), resource, baseUri, authentication);
		
		return resource;
	}
	
	@Override
	public void updateDto(UserSettingDto dto, UserSetting model) throws FactoryException {

		dto.setUserSettingGuid(model.getUserSettingGuid());
		dto.setLoginUserGuid(model.getLoginUserGuid());
		dto.setLoginUserId(model.getLoginUserId());
		dto.setLoginUserType(model.getLoginUserType());
		dto.setGivenName(model.getGivenName());
		dto.setFamilyName(model.getFamilyName());
		dto.setPolicySearchCropYear(model.getPolicySearchCropYear());
		dto.setPolicySearchInsurancePlanId(model.getPolicySearchInsurancePlanId());
		dto.setPolicySearchOfficeId(model.getPolicySearchOfficeId());
		
	}

	
	private void populateResource(UserSettingRsrc resource, UserSettingDto dto) {
		resource.setFamilyName(dto.getFamilyName());
		resource.setGivenName(dto.getGivenName());
		resource.setLoginUserGuid(dto.getLoginUserGuid());
		resource.setLoginUserId(dto.getLoginUserId());
		resource.setLoginUserType(dto.getLoginUserType());
		resource.setPolicySearchCropYear(dto.getPolicySearchCropYear());
		resource.setPolicySearchInsurancePlanId(dto.getPolicySearchInsurancePlanId());
		resource.setPolicySearchInsurancePlanName(dto.getPolicySearchInsurancePlanName());
		resource.setPolicySearchOfficeId(dto.getPolicySearchOfficeId());
		resource.setPolicySearchOfficeName(dto.getPolicySearchOfficeName());
		resource.setUserSettingGuid(dto.getUserSettingGuid());
	}
	
	static void setSelfLink(String userSettingGuid, UserSettingRsrc resource, URI baseUri) {
		if (userSettingGuid != null) {
			String selfUri = getUserSettingSelfUri(userSettingGuid, baseUri);
			resource.getLinks().add(new RelLink(ResourceTypes.SELF, selfUri, "GET"));
		}
	}

	public static String getUserSettingSelfUri(String userSettingGuid, URI baseUri) {

		String result = UriBuilder
				.fromUri(baseUri)
				.path(UserSettingEndpoint.class)
				.build(userSettingGuid).toString();
		return result;
	}
	
	private static void setLinks(
			String userSettingGuid,
			UserSettingRsrc resource, 
			URI baseUri, 
			WebAdeAuthentication authentication) {
			
		String result = getUserSettingSelfUri(userSettingGuid, baseUri);

		if (authentication.hasAuthority(Scopes.UPDATE_USER_SETTING)) {
			resource.getLinks().add(new RelLink(ResourceTypes.UPDATE_USER_SETTING, result, "PUT"));
		}
		
		if (authentication.hasAuthority(Scopes.DELETE_USER_SETTING)) {
			resource.getLinks().add(new RelLink(ResourceTypes.DELETE_USER_SETTING, result, "DELETE"));
		}
	}
	
	public static String getUserSettingListSelfUri(URI baseUri) {

		String result = UriBuilder.fromUri(baseUri)
			.path(UserSettingListEndpoint.class)
			.build()
			.toString();

		return result;
	}
	
	protected static String nvl(Integer value, String defaultValue) {
		return (value==null)?defaultValue:value.toString();
	}
	
}
