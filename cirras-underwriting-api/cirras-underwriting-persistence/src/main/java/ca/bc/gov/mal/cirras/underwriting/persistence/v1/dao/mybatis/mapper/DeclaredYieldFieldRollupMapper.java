package ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.mybatis.mapper;

import java.util.List;
import java.util.Map;

import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.DeclaredYieldFieldRollupDto;

public interface DeclaredYieldFieldRollupMapper {

	DeclaredYieldFieldRollupDto fetch(Map<String, Object> parameters);
    
	int insert(Map<String, Object> parameters);
    
	int update(Map<String, Object> parameters);
    
	int delete(Map<String, Object> parameters);

	int deleteForDeclaredYieldContract(Map<String, Object> parameters);
    
    List<DeclaredYieldFieldRollupDto> selectForDeclaredYieldContract(Map<String, Object> parameters);
	
    List<DeclaredYieldFieldRollupDto> selectToRecalculate(Map<String, Object> parameters);

}