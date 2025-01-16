package ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.mybatis.mapper;

import java.util.Map;

import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.UserSettingDto;

public interface UserSettingMapper {

	UserSettingDto fetch(Map<String, Object> parameters);
	
	int insert(Map<String, Object> parameters);

	int update(Map<String, Object> parameters);

	int delete(Map<String, Object> parameters);
		
	UserSettingDto getByLoginUserGuid(Map<String, Object> parameters);
	
}
