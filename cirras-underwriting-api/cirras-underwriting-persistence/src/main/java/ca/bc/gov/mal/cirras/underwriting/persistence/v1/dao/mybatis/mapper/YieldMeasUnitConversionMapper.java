package ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.mybatis.mapper;

import java.util.List;
import java.util.Map;

import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.YieldMeasUnitConversionDto;

public interface YieldMeasUnitConversionMapper {
	
	YieldMeasUnitConversionDto fetch(Map<String, Object> parameters);

	List<YieldMeasUnitConversionDto> fetchAll(Map<String, Object> parameters);

	int insert(Map<String, Object> parameters);

	int update(Map<String, Object> parameters);

	int delete(Map<String, Object> parameters);
	
	List<YieldMeasUnitConversionDto> selectByYearAndPlan(Map<String, Object> parameters);
	
	List<YieldMeasUnitConversionDto> selectLatestVersionByPlan(Map<String, Object> parameters);

}
