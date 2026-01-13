package ca.bc.gov.mal.cirras.underwriting.data.repositories.mapper;

import java.util.List;
import java.util.Map;

import ca.bc.gov.mal.cirras.underwriting.data.entities.VerifiedYieldSummaryDto;

public interface VerifiedYieldSummaryMapper {

	VerifiedYieldSummaryDto fetch(Map<String, Object> parameters);
    
	int insert(Map<String, Object> parameters);
    
	int update(Map<String, Object> parameters);
    
	int delete(Map<String, Object> parameters);

	int deleteForVerifiedYieldContract(Map<String, Object> parameters);
    
    List<VerifiedYieldSummaryDto> selectForVerifiedYieldContract(Map<String, Object> parameters);

}