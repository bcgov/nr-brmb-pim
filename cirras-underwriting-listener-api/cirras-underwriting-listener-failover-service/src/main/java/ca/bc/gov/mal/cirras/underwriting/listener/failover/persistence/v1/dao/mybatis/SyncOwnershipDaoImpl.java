package ca.bc.gov.mal.cirras.underwriting.listener.failover.persistence.v1.dao.mybatis;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.mybatis.BaseDao;
import ca.bc.gov.mal.cirras.underwriting.listener.failover.persistence.v1.dao.SyncOwnershipDao;
import ca.bc.gov.mal.cirras.underwriting.listener.failover.persistence.v1.dao.mybatis.mapper.SyncOwnershipMapper;
import ca.bc.gov.mal.cirras.underwriting.listener.failover.persistence.v1.dto.SyncOwnershipDto;

@Repository
public class SyncOwnershipDaoImpl extends BaseDao implements
	SyncOwnershipDao {

	private static final long serialVersionUID = 1L;

	private static final Logger logger = LoggerFactory.getLogger(SyncOwnershipDaoImpl.class);

	@Autowired
	private SyncOwnershipMapper syncOwnershipMapper;

	@Override
	public SyncOwnershipDto select(String processName) throws DaoException {	
		logger.debug("<select");
		SyncOwnershipDto result = null;
		
		try {

			Map<String, Object> parameters = new HashMap<String, Object>();
	
			parameters.put("processName", processName);
			
			result = this.syncOwnershipMapper.select(parameters);
			
		} catch (RuntimeException e) {
			handleException(e);
		}
		
		logger.debug(">select");
		return result;
	}

	@Override
	public SyncOwnershipDto selectForUpdate(String processName) throws DaoException {	
		logger.debug("<selectForUpdate");
		SyncOwnershipDto result = null;
		
		try {

			Map<String, Object> parameters = new HashMap<String, Object>();
	
			parameters.put("processName", processName);
			
			result = this.syncOwnershipMapper.selectForUpdate(parameters);
			
		} catch (RuntimeException e) {
			handleException(e);
		}
		
		logger.debug(">selectForUpdate");
		return result;
	}

	@Override
	public void insert(SyncOwnershipDto dto, Integer nodeExpiryMinutes, String userId) throws DaoException {
		logger.debug("<insert");

		String syncOwnershipGuid = null; 
		
		try {

			Map<String, Object> parameters = new HashMap<String, Object>();

			parameters.put("dto", dto);
			parameters.put("nodeExpiryMinutes", nodeExpiryMinutes);
			parameters.put("userId", userId);
			int count = this.syncOwnershipMapper.insert(parameters);

			if(count==0) {
				throw new DaoException("Record not inserted: "+count);
			}
			
			syncOwnershipGuid = (String) parameters.get("syncOwnershipGuid");
			
			dto.setSyncOwnershipGuid(syncOwnershipGuid);
			
		} catch (RuntimeException e) {
			handleException(e);
		}

		logger.debug(">insert " + syncOwnershipGuid);
	}

	@Override
	public boolean update(String syncOwnershipGuid, SyncOwnershipDto dto, Integer nodeExpiryMinutes, String userId) 
			throws DaoException, NotFoundDaoException {
		logger.debug("<update");
		
		boolean result = false;
			
		try {

			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("syncOwnershipGuid", syncOwnershipGuid);
			parameters.put("dto", dto);
			parameters.put("nodeExpiryMinutes", nodeExpiryMinutes);
			parameters.put("userId", userId);
			int count = this.syncOwnershipMapper.update(parameters);

			if(count==0) {
				throw new DaoException("Record not updated: "+count);
			}
					
			result = true;

		} catch (RuntimeException e) {
			handleException(e);
		}
		
		logger.info(">update "+result);
		return result;
	}

	@Override
	public void delete(String syncOwnershipGuid) throws DaoException, NotFoundDaoException {
		logger.debug("<delete");

		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("syncOwnershipGuid", syncOwnershipGuid);
			int count = this.syncOwnershipMapper.delete(parameters);

			if(count==0) {
				throw new DaoException("Record not deleted: "+count);
			}
		} catch (RuntimeException e) {
			handleException(e);
		}

		logger.debug(">delete");
		
	}

}
