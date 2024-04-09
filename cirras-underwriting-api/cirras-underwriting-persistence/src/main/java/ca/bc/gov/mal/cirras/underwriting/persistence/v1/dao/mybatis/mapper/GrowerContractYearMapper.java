package ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.mybatis.mapper;

import java.util.List;
import java.util.Map;

import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.GrowerContractYearDto;

public interface GrowerContractYearMapper {
	
	GrowerContractYearDto fetch(Map<String, Object> parameters);

	List<GrowerContractYearDto> fetchAll(Map<String, Object> parameters);

	GrowerContractYearDto selectInventoryContractForGcy(Map<String, Object> parameters);

	GrowerContractYearDto selectLastYear(Map<String, Object> parameters);
	
	int insert(Map<String, Object> parameters);

	int update(Map<String, Object> parameters);

	int delete(Map<String, Object> parameters);
	
}
