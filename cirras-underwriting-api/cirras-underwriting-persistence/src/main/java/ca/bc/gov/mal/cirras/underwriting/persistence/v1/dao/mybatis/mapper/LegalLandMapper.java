package ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.mybatis.mapper;

import java.util.List;
import java.util.Map;

import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.LegalLandDto;

public interface LegalLandMapper {
	
	LegalLandDto fetch(Map<String, Object> parameters);

	List<LegalLandDto> fetchAll(Map<String, Object> parameters);

	int insert(Map<String, Object> parameters);

	int insertDataSync(Map<String, Object> parameters);

	int update(Map<String, Object> parameters);

	int delete(Map<String, Object> parameters);
	
	int selectCount(Map<String, Object> parameters);
	
	List<LegalLandDto> select(Map<String, Object> parameters);
	
	List<LegalLandDto> searchOtherLegalLandForField(Map<String, Object> parameters);

	int getNextPidSequence();
}
