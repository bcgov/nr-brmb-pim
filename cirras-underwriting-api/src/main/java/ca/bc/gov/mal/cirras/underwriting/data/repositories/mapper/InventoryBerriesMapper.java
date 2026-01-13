package ca.bc.gov.mal.cirras.underwriting.data.repositories.mapper;

import java.util.List;
import java.util.Map;

import ca.bc.gov.mal.cirras.underwriting.data.entities.InventoryBerriesDto;

public interface InventoryBerriesMapper {

	InventoryBerriesDto fetch(Map<String, Object> parameters);
	
	int insert(Map<String, Object> parameters);

	int update(Map<String, Object> parameters);

	int delete(Map<String, Object> parameters);

	int deleteForInventoryField(Map<String, Object> parameters);
		
	int deleteForField(Map<String, Object> parameters);
	
	int deleteForInventoryContract(Map<String, Object> parameters);
	
	List<InventoryBerriesDto> select(Map<String, Object> parameters);

	InventoryBerriesDto selectForRollover(Map<String, Object> parameters);
}
