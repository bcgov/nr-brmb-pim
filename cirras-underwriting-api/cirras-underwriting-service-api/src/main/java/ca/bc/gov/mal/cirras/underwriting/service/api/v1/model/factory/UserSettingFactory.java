package ca.bc.gov.mal.cirras.underwriting.services.model.factory;

import ca.bc.gov.nrs.wfone.common.service.api.model.factory.FactoryContext;
import ca.bc.gov.nrs.wfone.common.service.api.model.factory.FactoryException;
import ca.bc.gov.nrs.wfone.common.webade.authentication.WebAdeAuthentication;

import ca.bc.gov.mal.cirras.underwriting.data.models.UserSetting;
import ca.bc.gov.mal.cirras.underwriting.data.entities.UserSettingDto;

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

	void updateDto(
			UserSettingDto dto, 
			UserSetting model
		) throws FactoryException;
}
