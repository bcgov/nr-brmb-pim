package ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.mybatis.mapper;

import java.util.List;
import java.util.Map;

import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.YieldMeasUnitTypeCodeDto;

public interface YieldMeasUnitTypeCodeMapper {
	
	YieldMeasUnitTypeCodeDto fetch(Map<String, Object> parameters);

	List<YieldMeasUnitTypeCodeDto> fetchAll(Map<String, Object> parameters);

	int insert(Map<String, Object> parameters);

	int update(Map<String, Object> parameters);

	int delete(Map<String, Object> parameters);
	
	List<YieldMeasUnitTypeCodeDto> selectByPlan(Map<String, Object> parameters);
}
