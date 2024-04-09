package ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.mybatis.mapper;

import java.util.List;
import java.util.Map;

import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.InsurancePlanDto;

public interface InsurancePlanMapper {
	
	InsurancePlanDto fetch(Map<String, Object> parameters);
	
	List<InsurancePlanDto> selectByField(Map<String, Object> parameters);
}
