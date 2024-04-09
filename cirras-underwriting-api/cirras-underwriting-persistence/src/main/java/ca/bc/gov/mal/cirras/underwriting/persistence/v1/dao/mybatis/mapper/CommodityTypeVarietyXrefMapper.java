package ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.mybatis.mapper;

import java.util.List;
import java.util.Map;

import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.CommodityTypeVarietyXrefDto;

public interface CommodityTypeVarietyXrefMapper {
	
	CommodityTypeVarietyXrefDto fetch(Map<String, Object> parameters);

	List<CommodityTypeVarietyXrefDto> fetchAll(Map<String, Object> parameters);

	int insert(Map<String, Object> parameters);

	int delete(Map<String, Object> parameters);
	
}
