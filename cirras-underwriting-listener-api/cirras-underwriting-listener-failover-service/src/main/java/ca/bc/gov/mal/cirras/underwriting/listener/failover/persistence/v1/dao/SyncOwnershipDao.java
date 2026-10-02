package ca.bc.gov.mal.cirras.underwriting.listener.failover.persistence.v1.dao;

import java.io.Serializable;

import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;
import ca.bc.gov.mal.cirras.underwriting.listener.failover.persistence.v1.dto.SyncOwnershipDto;

public interface SyncOwnershipDao extends Serializable {
	
	SyncOwnershipDto select(String processName) throws DaoException;
    
	SyncOwnershipDto selectForUpdate(String processName) throws DaoException;
	
	public void insert(SyncOwnershipDto dto, Integer nodeExpiryMinutes, String userId) throws DaoException;

	public boolean update(String syncOwnershipGuid, SyncOwnershipDto dto, Integer nodeExpiryMinutes, String userId) throws DaoException, NotFoundDaoException;

    void delete(String syncOwnershipGuid) throws DaoException, NotFoundDaoException;

}

