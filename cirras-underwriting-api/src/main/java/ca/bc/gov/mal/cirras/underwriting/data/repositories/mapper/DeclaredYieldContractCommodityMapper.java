package ca.bc.gov.mal.cirras.underwriting.data.repositories.mapper;

import java.util.List;
import java.util.Map;

import ca.bc.gov.mal.cirras.underwriting.data.entities.DeclaredYieldContractCommodityDto;

public interface DeclaredYieldContractCommodityMapper {

	DeclaredYieldContractCommodityDto fetch(Map<String, Object> parameters);
    
	int insert(Map<String, Object> parameters);
    
	int update(Map<String, Object> parameters);
    
	int delete(Map<String, Object> parameters);

	int deleteForDeclaredYieldContract(Map<String, Object> parameters);
    
    List<DeclaredYieldContractCommodityDto> selectForDeclaredYieldContract(Map<String, Object> parameters);

    List<DeclaredYieldContractCommodityDto> selectToRecalculate(Map<String, Object> parameters);

}