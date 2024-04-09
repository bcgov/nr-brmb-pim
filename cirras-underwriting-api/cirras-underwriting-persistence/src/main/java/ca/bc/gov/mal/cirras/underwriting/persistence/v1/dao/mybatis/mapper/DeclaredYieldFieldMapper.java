package ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.mybatis.mapper;

import java.util.List;
import java.util.Map;

import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.DeclaredYieldFieldDto;

public interface DeclaredYieldFieldMapper {

	DeclaredYieldFieldDto fetch(Map<String, Object> parameters);
	
	int insert(Map<String, Object> parameters);

	int update(Map<String, Object> parameters);

	int delete(Map<String, Object> parameters);
	
	int deleteForField(Map<String, Object> parameters);

	int deleteForDeclaredYieldContract(Map<String, Object> parameters);
	
	DeclaredYieldFieldDto getByInventoryField(Map<String, Object> parameters);

	int getTotalDopRecordsWithYield(Map<String, Object> parameters);
	
    List<DeclaredYieldFieldDto> selectToRecalculate(Map<String, Object> parameters);

}
