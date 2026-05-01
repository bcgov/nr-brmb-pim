package ca.bc.gov.mal.cirras.underwriting.data.entities;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.nrs.wfone.common.persistence.dto.BaseDto;
import ca.bc.gov.nrs.wfone.common.persistence.utils.DtoUtils;

public class DeclaredYieldContractCommodityBerriesOutboxDto extends BaseDto<DeclaredYieldContractCommodityBerriesOutboxDto> {

	private static final long serialVersionUID = 1L;

	private static final Logger logger = LoggerFactory.getLogger(DeclaredYieldContractCommodityBerriesOutboxDto.class);

	private Integer declaredYieldContractCommodityBerriesOutboxId;
	private String declaredYieldContractCommodityBerriesGuid;
	private String auditTransactionTypeCode;
	
	private String createUser;
	private Date createDate;
	private String updateUser;
	private Date updateDate;
	
	public DeclaredYieldContractCommodityBerriesOutboxDto() {
	}
	
	
	public DeclaredYieldContractCommodityBerriesOutboxDto(DeclaredYieldContractCommodityBerriesOutboxDto dto) {

		this.declaredYieldContractCommodityBerriesOutboxId = dto.declaredYieldContractCommodityBerriesOutboxId;
		this.declaredYieldContractCommodityBerriesGuid = dto.declaredYieldContractCommodityBerriesGuid;
		this.auditTransactionTypeCode = dto.auditTransactionTypeCode;

		this.createUser = dto.createUser;
		this.createDate = dto.createDate;
		this.updateUser = dto.updateUser;
		this.updateDate = dto.updateDate;
	}
	

	@Override
	public boolean equalsBK(DeclaredYieldContractCommodityBerriesOutboxDto other) {
		throw new UnsupportedOperationException("Not Implemented");
	}

	@Override
	public boolean equalsAll(DeclaredYieldContractCommodityBerriesOutboxDto other) {
		boolean result = false;
		
		if(other!=null) {
			result = true;
			DtoUtils dtoUtils = new DtoUtils(getLogger());
			result = result&&dtoUtils.equals("declaredYieldContractCommodityBerriesOutboxId", declaredYieldContractCommodityBerriesOutboxId, other.declaredYieldContractCommodityBerriesOutboxId);
			result = result&&dtoUtils.equals("declaredYieldContractCommodityBerriesGuid", declaredYieldContractCommodityBerriesGuid, other.declaredYieldContractCommodityBerriesGuid);
			result = result&&dtoUtils.equals("auditTransactionTypeCode", auditTransactionTypeCode, other.auditTransactionTypeCode);
		}
		
		return result;
	}
	
	@Override
	public Logger getLogger() {
		return logger;
	}

	@Override
	public DeclaredYieldContractCommodityBerriesOutboxDto copy() {
		return new DeclaredYieldContractCommodityBerriesOutboxDto(this);
	}

	public Integer getDeclaredYieldContractCommodityBerriesOutboxId() {
		return declaredYieldContractCommodityBerriesOutboxId;
	}

	public void setDeclaredYieldContractCommodityBerriesOutboxId(Integer declaredYieldContractCommodityBerriesOutboxId) {
		this.declaredYieldContractCommodityBerriesOutboxId = declaredYieldContractCommodityBerriesOutboxId;
	}
	
 	public String getDeclaredYieldContractCommodityBerriesGuid() {
		return declaredYieldContractCommodityBerriesGuid;
	}

	public void setDeclaredYieldContractCommodityBerriesGuid(String declaredYieldContractCommodityBerriesGuid) {
		this.declaredYieldContractCommodityBerriesGuid = declaredYieldContractCommodityBerriesGuid;
	}

	public String getAuditTransactionTypeCode() {
		return auditTransactionTypeCode;
	}

	public void setAuditTransactionTypeCode(String auditTransactionTypeCode) {
		this.auditTransactionTypeCode = auditTransactionTypeCode;
	}

	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}
 
 	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
 
 	public String getUpdateUser() {
		return updateUser;
	}

	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}
 
 	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
 
}
