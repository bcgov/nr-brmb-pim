package ca.bc.gov.mal.cirras.underwriting.listener.failover.persistence.v1.dao.mybatis.mapper;

import java.util.Map;

import ca.bc.gov.mal.cirras.underwriting.listener.failover.persistence.v1.dto.SyncOwnershipDto;

public interface SyncOwnershipMapper {

	SyncOwnershipDto select(Map<String, Object> parameters);
	
	SyncOwnershipDto selectForUpdate(Map<String, Object> parameters);
	
	int insert(Map<String, Object> parameters);
	
	int update(Map<String, Object> parameters);

	int delete(Map<String, Object> parameters);

}
