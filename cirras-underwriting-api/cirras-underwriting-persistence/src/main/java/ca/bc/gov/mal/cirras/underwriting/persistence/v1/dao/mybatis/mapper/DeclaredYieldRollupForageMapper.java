package ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.mybatis.mapper;

import java.util.List;
import java.util.Map;

import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.DeclaredYieldRollupForageDto;

public interface DeclaredYieldRollupForageMapper {

	DeclaredYieldRollupForageDto fetch(Map<String, Object> parameters);
    
	int insert(Map<String, Object> parameters);
    
	int update(Map<String, Object> parameters);
    
	int delete(Map<String, Object> parameters);

	int deleteForDeclaredYieldContract(Map<String, Object> parameters);
    
    List<DeclaredYieldRollupForageDto> selectForDeclaredYieldContract(Map<String, Object> parameters);

    List<DeclaredYieldRollupForageDto> selectToRecalculate(Map<String, Object> parameters);
}