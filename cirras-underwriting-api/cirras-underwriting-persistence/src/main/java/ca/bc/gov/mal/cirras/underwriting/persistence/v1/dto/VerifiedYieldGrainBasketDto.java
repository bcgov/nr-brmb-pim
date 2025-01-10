package ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.nrs.wfone.common.persistence.dto.BaseDto;
import ca.bc.gov.nrs.wfone.common.persistence.utils.DtoUtils;

public class VerifiedYieldGrainBasketDto extends BaseDto<VerifiedYieldGrainBasketDto> {

	private static final long serialVersionUID = 1L;

	private static final Logger logger = LoggerFactory.getLogger(VerifiedYieldGrainBasketDto.class);

	private String verifiedYieldGrainBasketGuid;
	private String verifiedYieldContractGuid;
	private Double basketValue;
	private Double harvestedValue;
	private String comment;
	private String createUser;
	private Date createDate;
	private String updateUser;
	private Date updateDate;
	
	public VerifiedYieldGrainBasketDto() {
	}
	
	
	public VerifiedYieldGrainBasketDto(VerifiedYieldGrainBasketDto dto) {

		this.verifiedYieldGrainBasketGuid = dto.verifiedYieldGrainBasketGuid;
		this.verifiedYieldContractGuid = dto.verifiedYieldContractGuid;
		this.basketValue = dto.basketValue;
		this.harvestedValue = dto.harvestedValue;
		this.comment = dto.comment;
		
		this.createUser = dto.createUser;
		this.createDate = dto.createDate;
		this.updateUser = dto.updateUser;
		this.updateDate = dto.updateDate;
	}
	

	@Override
	public boolean equalsBK(VerifiedYieldGrainBasketDto other) {
		throw new UnsupportedOperationException("Not Implemented");
	}

	@Override
	public boolean equalsAll(VerifiedYieldGrainBasketDto other) {
		boolean result = false;
		
		if(other!=null) {
			Integer decimalPrecision = 4;
			result = true;
			DtoUtils dtoUtils = new DtoUtils(getLogger());
			
			result = result&&dtoUtils.equals("verifiedYieldGrainBasketGuid", verifiedYieldGrainBasketGuid, other.verifiedYieldGrainBasketGuid);
			result = result&&dtoUtils.equals("verifiedYieldContractGuid", verifiedYieldContractGuid, other.verifiedYieldContractGuid);
			result = result&&dtoUtils.equals("basketValue", basketValue, other.basketValue, decimalPrecision);
			result = result&&dtoUtils.equals("harvestedValue", harvestedValue, other.harvestedValue, decimalPrecision);
			result = result&&dtoUtils.equals("comment", comment, other.comment);
		}
		
		return result;
	}
	
	@Override
	public Logger getLogger() {
		return logger;
	}

	@Override
	public VerifiedYieldGrainBasketDto copy() {
		return new VerifiedYieldGrainBasketDto(this);
	}

	public String getVerifiedYieldGrainBasketGuid() {
		return verifiedYieldGrainBasketGuid;
	}

	public void setVerifiedYieldGrainBasketGuid(String verifiedYieldGrainBasketGuid) {
		this.verifiedYieldGrainBasketGuid = verifiedYieldGrainBasketGuid;
	}

	public String getVerifiedYieldContractGuid() {
		return verifiedYieldContractGuid;
	}

	public void setVerifiedYieldContractGuid(String verifiedYieldContractGuid) {
		this.verifiedYieldContractGuid = verifiedYieldContractGuid;
	}

	public Double getBasketValue() {
		return basketValue;
	}

	public void setBasketValue(Double basketValue) {
		this.basketValue = basketValue;
	}

	public Double getHarvestedValue() {
		return harvestedValue;
	}

	public void setHarvestedValue(Double harvestedValue) {
		this.harvestedValue = harvestedValue;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
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
