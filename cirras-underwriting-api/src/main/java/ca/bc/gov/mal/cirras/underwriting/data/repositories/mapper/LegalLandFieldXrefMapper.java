package ca.bc.gov.mal.cirras.underwriting.data.repositories.mapper;

import java.util.List;
import java.util.Map;

import ca.bc.gov.mal.cirras.underwriting.data.entities.LegalLandFieldXrefDto;

public interface LegalLandFieldXrefMapper {
	
	LegalLandFieldXrefDto fetch(Map<String, Object> parameters);

	List<LegalLandFieldXrefDto> fetchAll(Map<String, Object> parameters);

	int insert(Map<String, Object> parameters);

	int delete(Map<String, Object> parameters);
	
	int deleteForField(Map<String, Object> parameters);
	
}
