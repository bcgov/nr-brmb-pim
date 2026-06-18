package ca.bc.gov.mal.cirras.underwriting.data.repositories.mapper;

import java.util.List;
import java.util.Map;

import ca.bc.gov.mal.cirras.underwriting.data.entities.DeclaredYieldFieldForageDto;

public interface DeclaredYieldFieldForageMapper {

	DeclaredYieldFieldForageDto fetch(Map<String, Object> parameters);
	
	int insert(Map<String, Object> parameters);

	int update(Map<String, Object> parameters);

	int delete(Map<String, Object> parameters);
	
	int deleteForField(Map<String, Object> parameters);

	int deleteForFieldAndYear(Map<String, Object> parameters);

	int deleteForDeclaredYieldContract(Map<String, Object> parameters);
		
	List<DeclaredYieldFieldForageDto> getByInventoryField(Map<String, Object> parameters);

	int getTotalDopRecordsWithYield(Map<String, Object> parameters);
	
    List<DeclaredYieldFieldForageDto> selectToRecalculate(Map<String, Object> parameters);
}
