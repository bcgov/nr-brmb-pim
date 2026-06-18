package ca.bc.gov.mal.cirras.underwriting.data.repositories.mapper;

import java.util.List;
import java.util.Map;

import ca.bc.gov.mal.cirras.underwriting.data.entities.GradeModifierTypeCodeDto;

public interface GradeModifierTypeCodeMapper {
	
	GradeModifierTypeCodeDto fetch(Map<String, Object> parameters);

	List<GradeModifierTypeCodeDto> fetchAll(Map<String, Object> parameters);

	int insert(Map<String, Object> parameters);

	int update(Map<String, Object> parameters);

	int delete(Map<String, Object> parameters);
	
	List<GradeModifierTypeCodeDto> select(Map<String, Object> parameters);
}
