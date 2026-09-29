package ca.bc.gov.mal.cirras.underwriting.listener.failover.service.api.v1.async;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.mal.cirras.underwriting.listener.failover.persistence.v1.dao.SyncOwnershipDao;
import ca.bc.gov.mal.cirras.underwriting.listener.failover.persistence.v1.dto.SyncOwnershipDto;

public class FailOverServiceImpl implements FailOverService {

	private static final Logger logger = LoggerFactory.getLogger(FailOverServiceImpl.class);

	private SyncOwnershipDao syncOwnershipDao;

	@Override
	public boolean asyncCheckForMaster(String processName, String nodeId, Integer nodeExpiryMinutes, String userId)
			throws ServiceException {
		logger.debug("<asyncCheckForMaster");
		boolean result = false;
		
		try {
			
			boolean skipCheck = false;
			
			SyncOwnershipDto dto = null;
			try {
			
				dto = this.syncOwnershipDao.selectForUpdate(processName);
				
			} catch (DaoException e) {
				// If we cannot get the lock then we will skip the check
				skipCheck = true;
				
				logger.warn("Skipping asyncCheckForMaster", e);
			}
			
			if(skipCheck) {
				
				// do nothing
				logger.warn("Failed to check for sync ownership.  Select for update timed out.");
				
			} else {
			
				if (dto == null) {
					
					logger.info("Process has no current master.");
					
					dto = new SyncOwnershipDto();
					dto.setNodeId(nodeId);
					dto.setProcessName(processName);
					this.syncOwnershipDao.insert(dto, nodeExpiryMinutes,userId);
					
					dto = this.syncOwnershipDao.select(processName);
					
					logger.info("This node is the new process master.");
					
					logger.info("Next process expiry {}", dto.getExpiredTimestamp());
					
					result = true;
					
				} else { 
					
					if(Boolean.TRUE.equals(dto.getExpiredInd())){
					
						logger.info("Process master has expired. {}, {}", dto.getNodeId(), dto.getExpiredTimestamp());
						
						dto.setNodeId(nodeId);
						this.syncOwnershipDao.update(dto.getSyncOwnershipGuid(), dto, nodeExpiryMinutes, userId);
						
						dto = this.syncOwnershipDao.select(processName);
						
						logger.info("This node is the new process master.");
						
						logger.info("Next process expiry {}", dto.getExpiredTimestamp());
						
						result = true;
						
					} else if(dto.getNodeId().equals(nodeId)) {
						// If this node is the master	
						
						logger.info("This node is the current process master.");
						
						this.syncOwnershipDao.update(dto.getSyncOwnershipGuid(), dto, nodeExpiryMinutes, userId);

						dto = this.syncOwnershipDao.select(processName);

						logger.info("Next process expiry {}", dto.getExpiredTimestamp());
						
						result = true;
						
					} else {
						// If this node is not the master
						
						logger.info("Node {} is the current process master.", dto.getNodeId());
						
						result = false;
					}
				}
			}
			
		} catch (DaoException e) {
			throw new ServiceException("DAO threw an exception", e);
		}
		
		logger.debug(">asyncCheckForMaster " + result);
		return result;
	}

	public void setSyncOwnershipDao(SyncOwnershipDao syncOwnershipDao) {
		this.syncOwnershipDao = syncOwnershipDao;
	}
}
