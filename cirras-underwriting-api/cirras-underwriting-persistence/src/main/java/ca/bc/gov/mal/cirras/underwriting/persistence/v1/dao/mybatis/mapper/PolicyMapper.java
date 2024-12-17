package ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.mybatis.mapper;

import java.util.List;
import java.util.Map;

import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.PolicyDto;

public interface PolicyMapper {

	PolicyDto fetch(Map<String, Object> parameters);
	
	int insert(Map<String, Object> parameters);

	int update(Map<String, Object> parameters);

	int delete(Map<String, Object> parameters);
	
	int selectCount(Map<String, Object> parameters);
	
	List<PolicyDto> select(Map<String, Object> parameters);
	
	List<PolicyDto> selectByFieldAndYear(Map<String, Object> parameters);

	List<PolicyDto> selectByOtherYearInventory(Map<String, Object> parameters);

	List<PolicyDto> selectByOtherYearDop(Map<String, Object> parameters);

	List<PolicyDto> selectByOtherYearVerified(Map<String, Object> parameters);
}
