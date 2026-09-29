package ca.bc.gov.mal.cirras.underwriting.listener.failover.persistence.v1.dto;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.nrs.wfone.common.persistence.dto.BaseDto;
import ca.bc.gov.nrs.wfone.common.persistence.utils.DtoUtils;

public class SyncOwnershipDto extends BaseDto<SyncOwnershipDto> {

	private static final long serialVersionUID = 1L;

	private static final Logger logger = LoggerFactory.getLogger(SyncOwnershipDto.class);

	private String syncOwnershipGuid;
	private String processName;
	private String nodeId;
	private Date expiredTimestamp;
	private Boolean expiredInd;
	
	public SyncOwnershipDto() {

	}
	
	public SyncOwnershipDto(SyncOwnershipDto dto) {
		
		this.syncOwnershipGuid = dto.syncOwnershipGuid;
		this.processName = dto.processName;
		this.nodeId = dto.nodeId;
		this.expiredTimestamp = dto.expiredTimestamp;
		this.expiredInd = dto.expiredInd;
	}
	
	public String getSyncOwnershipGuid() {
		return syncOwnershipGuid;
	}

	public void setSyncOwnershipGuid(String syncOwnershipGuid) {
		this.syncOwnershipGuid = syncOwnershipGuid;
	}

	public String getProcessName() {
		return processName;
	}

	public void setProcessName(String processName) {
		this.processName = processName;
	}

	public String getNodeId() {
		return nodeId;
	}

	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}
	
	public Date getExpiredTimestamp() {
		return expiredTimestamp;
	}

	public void setExpiredTimestamp(Date expiredTimestamp) {
		this.expiredTimestamp = expiredTimestamp;
	}

	public Boolean getExpiredInd() {
		return expiredInd;
	}

	public void setExpiredInd(Boolean expiredInd) {
		this.expiredInd = expiredInd;
	}

	@Override
	public SyncOwnershipDto copy() {
		return new SyncOwnershipDto(this);
	}

	@Override
	public boolean equalsBK(SyncOwnershipDto other) {
		boolean result = false;
		
		if(other!=null) {
			
			result = true;
			result = result&&((processName==null&&other.processName==null)||(processName!=null&&processName.equals(other.processName)));
		}

		
		return result;
	}

	@Override
	public boolean equalsAll(SyncOwnershipDto other) {
		boolean result = false;
		
		if(other==null) {
			logger.info("other DTO is null");
		} else {
			
			result = true;
			DtoUtils dtoUtils = new DtoUtils(getLogger());
			result = result&&dtoUtils.equals("syncOwnershipGuid", syncOwnershipGuid, other.syncOwnershipGuid);
			result = result&&dtoUtils.equals("processName", processName, other.processName);
			result = result&&dtoUtils.equals("nodeId", nodeId, other.nodeId);
			result = result&&dtoUtils.equals("expiredInd", expiredInd, other.expiredInd);
			
		}
		
		return result;
	}

	@Override
	public Logger getLogger() {
		return logger;
	}
}
