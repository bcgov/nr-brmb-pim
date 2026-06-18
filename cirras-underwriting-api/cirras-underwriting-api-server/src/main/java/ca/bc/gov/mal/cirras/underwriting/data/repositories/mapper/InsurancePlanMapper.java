package ca.bc.gov.mal.cirras.underwriting.data.repositories.mapper;

import java.util.List;
import java.util.Map;

import ca.bc.gov.mal.cirras.underwriting.data.entities.InsurancePlanDto;

public interface InsurancePlanMapper {
	
	InsurancePlanDto fetch(Map<String, Object> parameters);
	
	List<InsurancePlanDto> selectByField(Map<String, Object> parameters);
}
