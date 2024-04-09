package ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.mybatis.mapper;

import java.util.List;
import java.util.Map;

import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.CropVarietyDto;

public interface CropVarietyMapper {
	
	CropVarietyDto fetch(Map<String, Object> parameters);

	List<CropVarietyDto> fetchAll(Map<String, Object> parameters);

	int insert(Map<String, Object> parameters);

	int update(Map<String, Object> parameters);

	int delete(Map<String, Object> parameters);
	
	int selectCount(Map<String, Object> parameters);
	
	List<CropVarietyDto> select(Map<String, Object> parameters);
}
