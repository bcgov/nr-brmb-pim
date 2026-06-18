package ca.bc.gov.mal.cirras.underwriting.data.entities;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.nrs.wfone.common.persistence.dto.BaseDto;
import ca.bc.gov.nrs.wfone.common.persistence.utils.DtoUtils;



public class DeclaredYieldFieldDto extends BaseDto<DeclaredYieldFieldDto> {

	private static final long serialVersionUID = 1L;

	private static final Logger logger = LoggerFactory.getLogger(DeclaredYieldFieldDto.class);

	private String declaredYieldFieldGuid;
	private String inventoryFieldGuid;
	private Double estimatedYieldPerAcre;
	private Double estimatedYieldPerAcreDefaultUnit;
	private Boolean unharvestedAcresInd;

	private String createUser;
	private Date createDate;
	private String updateUser;
	private Date updateDate;

	
	public DeclaredYieldFieldDto() {
	}
	
	
	public DeclaredYieldFieldDto(DeclaredYieldFieldDto dto) {

		this.declaredYieldFieldGuid = dto.declaredYieldFieldGuid;
		this.inventoryFieldGuid = dto.inventoryFieldGuid;
		this.estimatedYieldPerAcre = dto.estimatedYieldPerAcre;
		this.estimatedYieldPerAcreDefaultUnit = dto.estimatedYieldPerAcreDefaultUnit;
		this.unharvestedAcresInd = dto.unharvestedAcresInd;
		
		this.createUser = dto.createUser;
		this.createDate = dto.createDate;
		this.updateUser = dto.updateUser;
		this.updateDate = dto.updateDate;
	
	}
	

	@Override
	public boolean equalsBK(DeclaredYieldFieldDto other) {
		throw new UnsupportedOperationException("Not Implemented");
	}

	@Override
	public boolean equalsAll(DeclaredYieldFieldDto other) {
		boolean result = false;
		
		if(other!=null) {
			result = true;
			DtoUtils dtoUtils = new DtoUtils(getLogger());
			result = result&&dtoUtils.equals("declaredYieldFieldGuid", declaredYieldFieldGuid, other.declaredYieldFieldGuid);
			result = result&&dtoUtils.equals("inventoryFieldGuid", inventoryFieldGuid, other.inventoryFieldGuid);
			result = result&&dtoUtils.equals("estimatedYieldPerAcre", estimatedYieldPerAcre, other.estimatedYieldPerAcre, 4);
			result = result&&dtoUtils.equals("estimatedYieldPerAcreDefaultUnit", estimatedYieldPerAcreDefaultUnit, other.estimatedYieldPerAcreDefaultUnit, 4);
			result = result&&dtoUtils.equals("unharvestedAcresInd", unharvestedAcresInd, other.unharvestedAcresInd);
		}
		
		return result;
	}
	
	@Override
	public Logger getLogger() {
		return logger;
	}

	@Override
	public DeclaredYieldFieldDto copy() {
		return new DeclaredYieldFieldDto(this);
	}

	public String getDeclaredYieldFieldGuid() {
		return declaredYieldFieldGuid;
	}
	public void setDeclaredYieldFieldGuid(String declaredYieldFieldGuid) {
		this.declaredYieldFieldGuid = declaredYieldFieldGuid;
	}

	public String getInventoryFieldGuid() {
		return inventoryFieldGuid;
	}
	public void setInventoryFieldGuid(String inventoryFieldGuid) {
		this.inventoryFieldGuid = inventoryFieldGuid;
	}

	public Double getEstimatedYieldPerAcre() {
		return estimatedYieldPerAcre;
	}
	public void setEstimatedYieldPerAcre(Double estimatedYieldPerAcre) {
		this.estimatedYieldPerAcre = estimatedYieldPerAcre;
	}

	public Double getEstimatedYieldPerAcreDefaultUnit() {
		return estimatedYieldPerAcreDefaultUnit;
	}
	public void setEstimatedYieldPerAcreDefaultUnit(Double estimatedYieldPerAcreDefaultUnit) {
		this.estimatedYieldPerAcreDefaultUnit = estimatedYieldPerAcreDefaultUnit;
	}

	public Boolean getUnharvestedAcresInd() {
		return unharvestedAcresInd;
	}
	public void setUnharvestedAcresInd(Boolean unharvestedAcresInd) {
		this.unharvestedAcresInd = unharvestedAcresInd;
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
