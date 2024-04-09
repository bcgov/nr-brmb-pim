package ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.nrs.wfone.common.persistence.dto.BaseDto;
import ca.bc.gov.nrs.wfone.common.persistence.utils.DtoUtils;



public class DeclaredYieldFieldForageDto extends BaseDto<DeclaredYieldFieldForageDto> {

	private static final long serialVersionUID = 1L;

	private static final Logger logger = LoggerFactory.getLogger(DeclaredYieldFieldForageDto.class);

	private String declaredYieldFieldForageGuid;
	private String inventoryFieldGuid;
	private Integer cutNumber;
	private Integer totalBalesLoads;
	private Double weight;
	private Double weightDefaultUnit;
	private Double moisturePercent;

	private String createUser;
	private Date createDate;
	private String updateUser;
	private Date updateDate;

	public DeclaredYieldFieldForageDto() {
	}
	
	
	public DeclaredYieldFieldForageDto(DeclaredYieldFieldForageDto dto) {

		this.declaredYieldFieldForageGuid = dto.declaredYieldFieldForageGuid;
		this.inventoryFieldGuid = dto.inventoryFieldGuid;
		this.cutNumber = dto.cutNumber;
		this.totalBalesLoads = dto.totalBalesLoads;
		this.weight = dto.weight;
		this.weightDefaultUnit = dto.weightDefaultUnit;
		this.moisturePercent = dto.moisturePercent;
		
		this.createUser = dto.createUser;
		this.createDate = dto.createDate;
		this.updateUser = dto.updateUser;
		this.updateDate = dto.updateDate;
	
	}
	

	@Override
	public boolean equalsBK(DeclaredYieldFieldForageDto other) {
		throw new UnsupportedOperationException("Not Implemented");
	}

	@Override
	public boolean equalsAll(DeclaredYieldFieldForageDto other) {
		boolean result = false;
		
		if(other!=null) {
			result = true;
			DtoUtils dtoUtils = new DtoUtils(getLogger());
			result = result&&dtoUtils.equals("declaredYieldFieldForageGuid", declaredYieldFieldForageGuid, other.declaredYieldFieldForageGuid);
			result = result&&dtoUtils.equals("inventoryFieldGuid", inventoryFieldGuid, other.inventoryFieldGuid);
			result = result&&dtoUtils.equals("cutNumber", cutNumber, other.cutNumber);
			result = result&&dtoUtils.equals("totalBalesLoads", totalBalesLoads, other.totalBalesLoads);
			result = result&&dtoUtils.equals("weight", weight, other.weight, 4);
			result = result&&dtoUtils.equals("weightDefaultUnit", weightDefaultUnit, other.weightDefaultUnit, 4);
			result = result&&dtoUtils.equals("moisturePercent", moisturePercent, other.moisturePercent, 4);
		}
		
		return result;
	}
	
	@Override
	public Logger getLogger() {
		return logger;
	}

	@Override
	public DeclaredYieldFieldForageDto copy() {
		return new DeclaredYieldFieldForageDto(this);
	}


	public String getDeclaredYieldFieldForageGuid() {
		return declaredYieldFieldForageGuid;
	}
	public void setDeclaredYieldFieldForageGuid(String declaredYieldFieldForageGuid) {
		this.declaredYieldFieldForageGuid = declaredYieldFieldForageGuid;
	}

	public String getInventoryFieldGuid() {
		return inventoryFieldGuid;
	}
	public void setInventoryFieldGuid(String inventoryFieldGuid) {
		this.inventoryFieldGuid = inventoryFieldGuid;
	}

	public Integer getCutNumber() {
		return cutNumber;
	}
	public void setCutNumber(Integer cutNumber) {
		this.cutNumber = cutNumber;
	}

	public Integer getTotalBalesLoads() {
		return totalBalesLoads;
	}
	public void setTotalBalesLoads(Integer totalBalesLoads) {
		this.totalBalesLoads = totalBalesLoads;
	}

	public Double getWeight() {
		return weight;
	}
	public void setWeight(Double weight) {
		this.weight = weight;
	}

	public Double getWeightDefaultUnit() {
		return weightDefaultUnit;
	}
	public void setWeightDefaultUnit(Double weightDefaultUnit) {
		this.weightDefaultUnit = weightDefaultUnit;
	}

	public Double getMoisturePercent() {
		return moisturePercent;
	}
	public void setMoisturePercent(Double moisturePercent) {
		this.moisturePercent = moisturePercent;
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
