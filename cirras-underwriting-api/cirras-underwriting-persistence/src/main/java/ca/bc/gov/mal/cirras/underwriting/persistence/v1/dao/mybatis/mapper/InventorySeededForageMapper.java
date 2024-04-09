package ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.mybatis.mapper;

import java.util.List;
import java.util.Map;

import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.InventorySeededForageDto;

public interface InventorySeededForageMapper {

	InventorySeededForageDto fetch(Map<String, Object> parameters);
	
	InventorySeededForageDto fetchSimple(Map<String, Object> parameters);
	
	int insert(Map<String, Object> parameters);

	int update(Map<String, Object> parameters);

	int delete(Map<String, Object> parameters);

	int deleteForInventoryField(Map<String, Object> parameters);
		
	int deleteForField(Map<String, Object> parameters);

	int deleteForInventoryContract(Map<String, Object> parameters);
	
	List<InventorySeededForageDto> select(Map<String, Object> parameters);
	
	List<InventorySeededForageDto> selectForDeclaredYield(Map<String, Object> parameters);

	List<InventorySeededForageDto> selectForDopContractCommodityTotals(Map<String, Object> parameters);

}
