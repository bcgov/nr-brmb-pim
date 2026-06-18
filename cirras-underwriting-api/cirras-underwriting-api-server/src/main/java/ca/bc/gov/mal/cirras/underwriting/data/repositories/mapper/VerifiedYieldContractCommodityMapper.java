package ca.bc.gov.mal.cirras.underwriting.data.repositories.mapper;

import java.util.List;
import java.util.Map;

import ca.bc.gov.mal.cirras.underwriting.data.entities.VerifiedYieldContractCommodityDto;

public interface VerifiedYieldContractCommodityMapper {

	VerifiedYieldContractCommodityDto fetch(Map<String, Object> parameters);
    
	int insert(Map<String, Object> parameters);
    
	int update(Map<String, Object> parameters);
    
	int delete(Map<String, Object> parameters);

	int deleteForVerifiedYieldContract(Map<String, Object> parameters);
    
    List<VerifiedYieldContractCommodityDto> selectForVerifiedYieldContract(Map<String, Object> parameters);

}