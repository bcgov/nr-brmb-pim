package ca.bc.gov.mal.cirras.underwriting.data.entities;

import java.util.Date;
import java.time.LocalDateTime;
import java.time.ZoneId;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.nrs.wfone.common.persistence.dto.BaseDto;
import ca.bc.gov.nrs.wfone.common.persistence.utils.DtoUtils;



public class PolicyDto extends BaseDto<PolicyDto> {

	private static final long serialVersionUID = 1L;

	private static final Logger logger = LoggerFactory.getLogger(PolicyDto.class);
	
	private Integer policyId;
	private Integer growerId;
	private Integer insurancePlanId;
	private String policyStatusCode;
	private Integer officeId;
	private String policyNumber;
	private String contractNumber;
	private Integer contractId;
	private Integer cropYear;
	private Date dataSyncTransDate;
	private String createUser;
	private Date createDate;
	private String updateUser;
	private Date updateDate;

	private Integer growerContractYearId;
	private String insurancePlanName;
	private String policyStatus;
	private Integer growerNumber;
	private String growerName;
	private String growerPrimaryEmail;
	private String growerPrimaryPhone;

	private String inventoryContractGuid;
	private String declaredYieldContractGuid;
	private String verifiedYieldContractGuid;
	private Integer totalDopEligibleInventory;
	
	public PolicyDto() {
	}
	
	
	public PolicyDto(PolicyDto dto) {
		this.policyId = dto.policyId;
		this.growerId = dto.growerId;
		this.insurancePlanId = dto.insurancePlanId;
		this.policyStatusCode = dto.policyStatusCode;
		this.officeId = dto.officeId;
		this.policyNumber = dto.policyNumber;
		this.contractNumber = dto.contractNumber;
		this.contractId = dto.contractId;
		this.cropYear = dto.cropYear;
		this.dataSyncTransDate = dto.dataSyncTransDate;
		this.createUser = dto.createUser;
		this.createDate = dto.createDate;
		this.updateUser = dto.updateUser;
		this.updateDate = dto.updateDate;

		this.growerContractYearId = dto.growerContractYearId;
		this.insurancePlanName = dto.insurancePlanName;
		this.policyStatus = dto.policyStatus;
		this.growerNumber = dto.growerNumber;
		this.growerName = dto.growerName;
		this.growerPrimaryEmail = dto.growerPrimaryEmail;
		this.growerPrimaryPhone = dto.growerPrimaryPhone;

		this.inventoryContractGuid = dto.inventoryContractGuid;
		this.declaredYieldContractGuid = dto.declaredYieldContractGuid;
		this.verifiedYieldContractGuid = dto.verifiedYieldContractGuid;
		this.totalDopEligibleInventory = dto.totalDopEligibleInventory;
	}
	

	@Override
	public boolean equalsBK(PolicyDto other) {
		throw new UnsupportedOperationException("Not Implemented");
	}

	@Override
	public boolean equalsAll(PolicyDto other) {
		boolean result = false;
		
		if(other!=null) {
			result = true;
			DtoUtils dtoUtils = new DtoUtils(getLogger());
			result = result&&dtoUtils.equals("policyId", policyId, other.policyId);
			result = result&&dtoUtils.equals("growerId", growerId, other.growerId);
			result = result&&dtoUtils.equals("insurancePlanId", insurancePlanId, other.insurancePlanId);
			result = result&&dtoUtils.equals("policyStatusCode", policyStatusCode, other.policyStatusCode);
			result = result&&dtoUtils.equals("officeId", officeId, other.officeId);
			result = result&&dtoUtils.equals("policyNumber", policyNumber, other.policyNumber);
			result = result&&dtoUtils.equals("contractNumber", contractNumber, other.contractNumber);
			result = result&&dtoUtils.equals("contractId", contractId, other.contractId);
			result = result&&dtoUtils.equals("cropYear", cropYear, other.cropYear);
			result = result&&dtoUtils.equals("dataSyncTransDate",
					LocalDateTime.ofInstant(dataSyncTransDate.toInstant(), ZoneId.systemDefault()), 
					LocalDateTime.ofInstant(other.dataSyncTransDate.toInstant(), ZoneId.systemDefault()));
		}
		
		return result;
	}
	
	@Override
	public Logger getLogger() {
		return logger;
	}

	@Override
	public PolicyDto copy() {
		return new PolicyDto(this);
	}
	
 	public Integer getPolicyId() {
		return policyId;
	}

	public void setPolicyId(Integer policyId) {
		this.policyId = policyId;
	}
 
 	public Integer getGrowerId() {
		return growerId;
	}

	public void setGrowerId(Integer growerId) {
		this.growerId = growerId;
	}
 
 	public Integer getInsurancePlanId() {
		return insurancePlanId;
	}

	public void setInsurancePlanId(Integer insurancePlanId) {
		this.insurancePlanId = insurancePlanId;
	}
 
 	public String getPolicyStatusCode() {
		return policyStatusCode;
	}

	public void setPolicyStatusCode(String policyStatusCode) {
		this.policyStatusCode = policyStatusCode;
	}
	 
 	public Integer getOfficeId() {
		return officeId;
	}

	public void setOfficeId(Integer officeId) {
		this.officeId = officeId;
	}

 	public String getPolicyNumber() {
		return policyNumber;
	}

	public void setPolicyNumber(String policyNumber) {
		this.policyNumber = policyNumber;
	}
 
 	public String getContractNumber() {
		return contractNumber;
	}

	public void setContractNumber(String contractNumber) {
		this.contractNumber = contractNumber;
	}
 
 	public Integer getContractId() {
		return contractId;
	}

	public void setContractId(Integer contractId) {
		this.contractId = contractId;
	}
 
 	public Integer getCropYear() {
		return cropYear;
	}

	public void setCropYear(Integer cropYear) {
		this.cropYear = cropYear;
	}
 
 	public Date getDataSyncTransDate() {
		return dataSyncTransDate;
	}

	public void setDataSyncTransDate(Date dataSyncTransDate) {
		this.dataSyncTransDate = dataSyncTransDate;
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
 
	public Integer getGrowerContractYearId() {
		return growerContractYearId;
	}
	public void setGrowerContractYearId(Integer growerContractYearId) {
		this.growerContractYearId = growerContractYearId;
	}

 	public String getInsurancePlanName() {
		return insurancePlanName;
	}

	public void setInsurancePlanName(String insurancePlanName) {
		this.insurancePlanName = insurancePlanName;
	}
 
 	public String getPolicyStatus() {
		return policyStatus;
	}

	public void setPolicyStatus(String policyStatus) {
		this.policyStatus = policyStatus;
	}
 
	public Integer getGrowerNumber() {
		return growerNumber;
	}
	
	public void setGrowerNumber(Integer growerNumber) {
		this.growerNumber = growerNumber;
	}
	
		public String getGrowerName() {
		return growerName;
	}
	
	public void setGrowerName(String growerName) {
		this.growerName = growerName;
	}

	public String getGrowerPrimaryEmail() {
		return growerPrimaryEmail;
	}
	
	public void setGrowerPrimaryEmail(String growerPrimaryEmail) {
		this.growerPrimaryEmail = growerPrimaryEmail;
	}

	public String getGrowerPrimaryPhone() {
		return growerPrimaryPhone;
	}
	
	public void setGrowerPrimaryPhone(String growerPrimaryPhone) {
		this.growerPrimaryPhone = growerPrimaryPhone;
	}
	
	public String getInventoryContractGuid() {
		return inventoryContractGuid;
	}
	
	public void setInventoryContractGuid(String inventoryContractGuid) {
		this.inventoryContractGuid = inventoryContractGuid;
	}
	
	public String getDeclaredYieldContractGuid() {
		return declaredYieldContractGuid;
	}
	
	public void setDeclaredYieldContractGuid(String declaredYieldContractGuid) {
		this.declaredYieldContractGuid = declaredYieldContractGuid;
	}

	public String getVerifiedYieldContractGuid() {
		return verifiedYieldContractGuid;
	}

	public void setVerifiedYieldContractGuid(String verifiedYieldContractGuid) {
		this.verifiedYieldContractGuid = verifiedYieldContractGuid;
	}
	
	public Integer getTotalDopEligibleInventory() {
		return totalDopEligibleInventory;
	}
	
	public void setTotalDopEligibleInventory(Integer totalDopEligibleInventory) {
		this.totalDopEligibleInventory = totalDopEligibleInventory;
	}

}
