package ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.mybatis.mapper;

import java.util.List;
import java.util.Map;

import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.SeedingDeadlineDto;

public interface SeedingDeadlineMapper {

	SeedingDeadlineDto fetch(Map<String, Object> parameters);
	
	int insert(Map<String, Object> parameters);

	int update(Map<String, Object> parameters);

	int delete(Map<String, Object> parameters);
	
	SeedingDeadlineDto selectForCommodityTypeAndYear(Map<String, Object> parameters);
	
	List<SeedingDeadlineDto> selectByYear(Map<String, Object> parameters);
	
}
