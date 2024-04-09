package ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.mybatis.mapper;

import java.util.List;
import java.util.Map;

import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.CropVarietyInsurabilityDto;

public interface CropVarietyInsurabilityMapper {
	
	CropVarietyInsurabilityDto fetch(Map<String, Object> parameters);

	List<CropVarietyInsurabilityDto> fetchAll(Map<String, Object> parameters);

	int insert(Map<String, Object> parameters);

	int update(Map<String, Object> parameters);

	int delete(Map<String, Object> parameters);
	
	List<CropVarietyInsurabilityDto> selectForInsurancePlan(Map<String, Object> parameters);
	
	List<CropVarietyInsurabilityDto> selectValidation(Map<String, Object> parameters);

}
