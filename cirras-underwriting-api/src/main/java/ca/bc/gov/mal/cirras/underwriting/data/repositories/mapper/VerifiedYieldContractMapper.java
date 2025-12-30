package ca.bc.gov.mal.cirras.underwriting.data.repositories.mapper;

import java.util.Map;

import ca.bc.gov.mal.cirras.underwriting.data.entities.VerifiedYieldContractDto;

public interface VerifiedYieldContractMapper {

	VerifiedYieldContractDto fetch(Map<String, Object> parameters);
	
	int insert(Map<String, Object> parameters);

	int update(Map<String, Object> parameters);

	int delete(Map<String, Object> parameters);
		
	VerifiedYieldContractDto getByContractAndYear(Map<String, Object> parameters);

	VerifiedYieldContractDto getByGrowerContract(Map<String, Object> parameters);
	
}
