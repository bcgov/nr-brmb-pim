package ca.bc.gov.mal.cirras.underwriting.service.api.v1.model.factory;

import ca.bc.gov.nrs.wfone.common.service.api.model.factory.FactoryContext;
import ca.bc.gov.nrs.wfone.common.service.api.model.factory.FactoryException;
import ca.bc.gov.nrs.wfone.common.webade.authentication.WebAdeAuthentication;

import ca.bc.gov.mal.cirras.underwriting.model.v1.UserSetting;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.UserSettingDto;

public interface UserSettingFactory {

	UserSetting getDefaultUserSetting(
			FactoryContext context, 
			WebAdeAuthentication authentication
		) throws FactoryException;
	
	UserSetting getUserSetting(
			UserSettingDto dto, 
			FactoryContext context, 
			WebAdeAuthentication authentication
		) throws FactoryException;
}
