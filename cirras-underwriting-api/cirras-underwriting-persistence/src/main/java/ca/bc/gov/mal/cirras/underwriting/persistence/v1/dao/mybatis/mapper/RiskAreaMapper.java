package ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.mybatis.mapper;

import java.util.List;
import java.util.Map;

import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.RiskAreaDto;

public interface RiskAreaMapper {
	
	RiskAreaDto fetch(Map<String, Object> parameters);

	List<RiskAreaDto> fetchAll(Map<String, Object> parameters);

	int insert(Map<String, Object> parameters);

	int update(Map<String, Object> parameters);

	int delete(Map<String, Object> parameters);
	
	List<RiskAreaDto> select(Map<String, Object> parameters);

	List<RiskAreaDto> selectByLegalLand(Map<String, Object> parameters);
}
