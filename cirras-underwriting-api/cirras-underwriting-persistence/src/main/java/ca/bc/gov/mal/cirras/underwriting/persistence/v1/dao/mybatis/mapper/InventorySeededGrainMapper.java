package ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.mybatis.mapper;

import java.util.List;
import java.util.Map;

import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.InventorySeededGrainDto;

public interface InventorySeededGrainMapper {

	InventorySeededGrainDto fetch(Map<String, Object> parameters);
	
	InventorySeededGrainDto fetchSimple(Map<String, Object> parameters);
	
	int insert(Map<String, Object> parameters);

	int update(Map<String, Object> parameters);

	int delete(Map<String, Object> parameters);

	int deleteForInventoryField(Map<String, Object> parameters);
		
	int deleteForField(Map<String, Object> parameters);
	
	int deleteForInventoryContract(Map<String, Object> parameters);
	
	List<InventorySeededGrainDto> select(Map<String, Object> parameters);

	List<InventorySeededGrainDto> selectForDeclaredYield(Map<String, Object> parameters);

	List<InventorySeededGrainDto> selectForVerifiedYield(Map<String, Object> parameters);

	List<InventorySeededGrainDto> selectTotalsForFieldYearPlan(Map<String, Object> parameters);
}
