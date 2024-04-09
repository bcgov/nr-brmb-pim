package ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.mybatis.mapper;

import java.util.List;
import java.util.Map;

import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.LegalLandRiskAreaXrefDto;

public interface LegalLandRiskAreaXrefMapper {
	
	LegalLandRiskAreaXrefDto fetch(Map<String, Object> parameters);

	List<LegalLandRiskAreaXrefDto> fetchAll(Map<String, Object> parameters);

	int insert(Map<String, Object> parameters);
	
	int update(Map<String, Object> parameters);

	int delete(Map<String, Object> parameters);
	
	int deleteForLegalLand(Map<String, Object> parameters);
	
}
