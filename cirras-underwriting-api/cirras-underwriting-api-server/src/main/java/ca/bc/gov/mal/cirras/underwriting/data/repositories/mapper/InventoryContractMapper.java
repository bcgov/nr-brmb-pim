package ca.bc.gov.mal.cirras.underwriting.data.repositories.mapper;

import java.util.List;
import java.util.Map;

import ca.bc.gov.mal.cirras.underwriting.data.entities.InventoryContractDto;

public interface InventoryContractMapper {

	InventoryContractDto fetch(Map<String, Object> parameters);
	
	int insert(Map<String, Object> parameters);

	int update(Map<String, Object> parameters);

	int delete(Map<String, Object> parameters);
		
	List<InventoryContractDto> select(Map<String, Object> parameters);

	InventoryContractDto getByGrowerContract(Map<String, Object> parameters);
	
	InventoryContractDto selectForPrintout(Map<String, Object> parameters);
	
}
