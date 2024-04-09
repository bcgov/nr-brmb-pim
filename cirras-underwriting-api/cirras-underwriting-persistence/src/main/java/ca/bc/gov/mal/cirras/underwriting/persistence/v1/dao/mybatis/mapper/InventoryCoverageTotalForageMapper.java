package ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.mybatis.mapper;

import java.util.List;
import java.util.Map;

import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.InventoryCoverageTotalForageDto;

public interface InventoryCoverageTotalForageMapper {

	InventoryCoverageTotalForageDto fetch(Map<String, Object> parameters);
	
	int insert(Map<String, Object> parameters);

	int update(Map<String, Object> parameters);

	int delete(Map<String, Object> parameters);
	
	int deleteForInventoryContract(Map<String, Object> parameters);
	
	List<InventoryCoverageTotalForageDto> select(Map<String, Object> parameters);

}
