package ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.mybatis.mapper;

import java.util.List;
import java.util.Map;

import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.InventoryContractCommodityDto;

public interface InventoryContractCommodityMapper {

	InventoryContractCommodityDto fetch(Map<String, Object> parameters);
	
	int insert(Map<String, Object> parameters);

	int update(Map<String, Object> parameters);

	int delete(Map<String, Object> parameters);
	
	int deleteForInventoryContract(Map<String, Object> parameters);
	
	List<InventoryContractCommodityDto> select(Map<String, Object> parameters);
	
	List<InventoryContractCommodityDto> selectForDopContract(Map<String, Object> parameters);

}
