package ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.mybatis.mapper;

import java.util.List;
import java.util.Map;

import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.FieldDto;

public interface FieldMapper {
	
	FieldDto fetch(Map<String, Object> parameters);

	List<FieldDto> fetchAll(Map<String, Object> parameters);

	int insert(Map<String, Object> parameters);

	int insertDataSync(Map<String, Object> parameters);

	int update(Map<String, Object> parameters);

	int delete(Map<String, Object> parameters);
	
	List<FieldDto> selectForLegalLandOrField(Map<String, Object> parameters);

	List<FieldDto> selectByLastPolicyForLegalLand(Map<String, Object> parameters);
	
	List<FieldDto> selectOtherFieldsForLegalLand(Map<String, Object> parameters);

	List<FieldDto> selectForLegalLand(Map<String, Object> parameters);
	
}
