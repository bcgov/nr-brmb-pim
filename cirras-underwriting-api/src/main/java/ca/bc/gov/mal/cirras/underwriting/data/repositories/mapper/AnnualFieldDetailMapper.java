package ca.bc.gov.mal.cirras.underwriting.data.repositories.mapper;

import java.util.List;
import java.util.Map;

import ca.bc.gov.mal.cirras.underwriting.data.entities.AnnualFieldDetailDto;

public interface AnnualFieldDetailMapper {
	
	AnnualFieldDetailDto fetch(Map<String, Object> parameters);

	List<AnnualFieldDetailDto> fetchAll(Map<String, Object> parameters);

	AnnualFieldDetailDto getByFieldAndCropYear(Map<String, Object> parameters);
	
	int getTotalForLegalLandField(Map<String, Object> parameters);
	
	int insert(Map<String, Object> parameters);

	int insertDataSync(Map<String, Object> parameters);

	int update(Map<String, Object> parameters);

	int delete(Map<String, Object> parameters);
	
	int deleteForField(Map<String, Object> parameters);
	
	AnnualFieldDetailDto getPreviousSubsequentRecords(Map<String, Object> parameters);
}
