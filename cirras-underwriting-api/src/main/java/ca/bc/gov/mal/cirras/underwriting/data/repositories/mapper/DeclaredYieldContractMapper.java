package ca.bc.gov.mal.cirras.underwriting.data.repositories.mapper;

import java.util.Map;

import ca.bc.gov.mal.cirras.underwriting.data.entities.DeclaredYieldContractDto;

public interface DeclaredYieldContractMapper {

	DeclaredYieldContractDto fetch(Map<String, Object> parameters);
	
	int insert(Map<String, Object> parameters);

	int update(Map<String, Object> parameters);

	int delete(Map<String, Object> parameters);
		
	DeclaredYieldContractDto getByContractAndYear(Map<String, Object> parameters);

	DeclaredYieldContractDto getByGrowerContract(Map<String, Object> parameters);
	
}
