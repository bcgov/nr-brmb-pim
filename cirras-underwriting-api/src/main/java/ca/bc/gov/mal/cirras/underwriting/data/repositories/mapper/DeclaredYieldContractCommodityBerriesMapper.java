package ca.bc.gov.mal.cirras.underwriting.data.repositories.mapper;

import java.util.List;
import java.util.Map;

import ca.bc.gov.mal.cirras.underwriting.data.entities.DeclaredYieldContractCommodityBerriesDto;

public interface DeclaredYieldContractCommodityBerriesMapper {

	DeclaredYieldContractCommodityBerriesDto fetch(Map<String, Object> parameters);
    
	int insert(Map<String, Object> parameters);
    
	int update(Map<String, Object> parameters);
    
	int delete(Map<String, Object> parameters);

	int deleteForDeclaredYieldContract(Map<String, Object> parameters);
    
    List<DeclaredYieldContractCommodityBerriesDto> selectForDeclaredYieldContract(Map<String, Object> parameters);

    List<DeclaredYieldContractCommodityBerriesDto> selectToRecalculate(Map<String, Object> parameters);

}