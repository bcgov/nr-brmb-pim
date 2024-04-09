package ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.mybatis.mapper;

import java.util.Map;

import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.DeclaredYieldContractDto;

public interface DeclaredYieldContractMapper {

	DeclaredYieldContractDto fetch(Map<String, Object> parameters);
	
	int insert(Map<String, Object> parameters);

	int update(Map<String, Object> parameters);

	int delete(Map<String, Object> parameters);
		
	DeclaredYieldContractDto getByContractAndYear(Map<String, Object> parameters);

	DeclaredYieldContractDto getByGrowerContract(Map<String, Object> parameters);
	
}
