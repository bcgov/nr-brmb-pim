package ca.bc.gov.mal.cirras.underwriting.data.repositories.mapper;

import java.util.List;
import java.util.Map;

import ca.bc.gov.mal.cirras.underwriting.data.entities.UnderwritingYearDto;

public interface UnderwritingYearMapper {
	
	UnderwritingYearDto fetch(Map<String, Object> parameters);

	List<UnderwritingYearDto> fetchAll(Map<String, Object> parameters);
	
	UnderwritingYearDto selectByCropYear(Map<String, Object> parameters);

	int insert(Map<String, Object> parameters);

	int delete(Map<String, Object> parameters);

	int deleteByCropYear(Map<String, Object> parameters);
}
