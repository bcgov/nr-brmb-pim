package ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.mybatis.mapper;

import java.util.List;
import java.util.Map;

import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.InventoryFieldDto;

public interface InventoryFieldMapper {

	InventoryFieldDto fetch(Map<String, Object> parameters);
	
	InventoryFieldDto selectLinkedGrainPlanting(Map<String, Object> parameters);
	
	int insert(Map<String, Object> parameters);

	int update(Map<String, Object> parameters);
	
	int removeLinkToPlantingForInventoryContract(Map<String, Object> parameters);

	int removeLinkToPlantingForField(Map<String, Object> parameters);
	
	int removeLinkToPlantingForFieldAndYear(Map<String, Object> parameters);

	int delete(Map<String, Object> parameters);
	
	int deleteForField(Map<String, Object> parameters);
	
	int deleteForInventoryContract(Map<String, Object> parameters);
	
	List<InventoryFieldDto> select(Map<String, Object> parameters);

	List<InventoryFieldDto> selectForDeclaredYield(Map<String, Object> parameters);
	
	List<InventoryFieldDto> selectForRollover(Map<String, Object> parameters);

	List<InventoryFieldDto> selectForField(Map<String, Object> parameters);
}
