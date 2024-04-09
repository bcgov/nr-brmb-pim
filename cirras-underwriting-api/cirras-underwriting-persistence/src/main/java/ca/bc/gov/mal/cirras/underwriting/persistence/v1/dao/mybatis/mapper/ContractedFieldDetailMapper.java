package ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.mybatis.mapper;

import java.util.List;
import java.util.Map;

import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.ContractedFieldDetailDto;

public interface ContractedFieldDetailMapper {

	ContractedFieldDetailDto fetch(Map<String, Object> parameters);
	
	ContractedFieldDetailDto fetchSimple(Map<String, Object> parameters);
	
	ContractedFieldDetailDto selectByGcyAndField(Map<String, Object> parameters);
	
	ContractedFieldDetailDto selectForFieldRollover(Map<String, Object> parameters);
	
	int insert(Map<String, Object> parameters);

	int insertDataSync(Map<String, Object> parameters);

	int update(Map<String, Object> parameters);

	int updateSync(Map<String, Object> parameters);

	int updateDisplayOrder(Map<String, Object> parameters);

	int delete(Map<String, Object> parameters);

	int deleteForField(Map<String, Object> parameters);
	
	int selectCount(Map<String, Object> parameters);
	
	List<ContractedFieldDetailDto> select(Map<String, Object> parameters);

	List<ContractedFieldDetailDto> selectForDeclaredYield(Map<String, Object> parameters);
	
	List<ContractedFieldDetailDto> selectForDisplayOrderUpdate(Map<String, Object> parameters);

	List<ContractedFieldDetailDto> selectForYearAndField(Map<String, Object> parameters);
	
	List<ContractedFieldDetailDto> selectForField(Map<String, Object> parameters);
}
