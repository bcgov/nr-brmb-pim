package ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.nrs.wfone.common.persistence.dto.BaseDto;
import ca.bc.gov.nrs.wfone.common.persistence.utils.DtoUtils;



public class ContractedFieldDetailDto extends BaseDto<ContractedFieldDetailDto> {

	private static final long serialVersionUID = 1L;

	private static final Logger logger = LoggerFactory.getLogger(ContractedFieldDetailDto.class);

	private Integer contractedFieldDetailId;
	private Integer annualFieldDetailId;
	private Integer growerContractYearId;
	private Integer contractId;
	private Integer fieldId;
	private Integer legalLandId;
	private String fieldLabel;
	private String otherLegalDescription;
	private Integer displayOrder;
	private Integer cropYear;
	private Integer insurancePlanId;
	
	private String insurancePlanName;
	
	private List<InventoryFieldDto> plantings = new ArrayList<InventoryFieldDto>();

	private List<UnderwritingCommentDto> uwComments = new ArrayList<UnderwritingCommentDto>();
	
	private List<PolicyDto> policies = new ArrayList<PolicyDto>();

	private String createUser;
	private Date createDate;
	private String updateUser;
	private Date updateDate;

	
	public ContractedFieldDetailDto() {
	}
	
	
	public ContractedFieldDetailDto(ContractedFieldDetailDto dto) {

		this.contractedFieldDetailId = dto.contractedFieldDetailId;
		this.annualFieldDetailId = dto.annualFieldDetailId;
		this.growerContractYearId = dto.growerContractYearId;
		this.contractId = dto.contractId;
		this.fieldId = dto.fieldId;
		this.legalLandId = dto.legalLandId;
		this.fieldLabel = dto.fieldLabel;
		this.otherLegalDescription = dto.otherLegalDescription;
		this.displayOrder = dto.displayOrder;
		this.cropYear = dto.cropYear;
		this.insurancePlanId = dto.insurancePlanId;
		this.insurancePlanName = dto.insurancePlanName;

		if ( dto.plantings != null ) {			
			this.plantings = new ArrayList<>();
			
			for ( InventoryFieldDto ifDto : dto.plantings ) {
				this.plantings.add(ifDto.copy());
			}
		}		

		if ( dto.uwComments != null ) {			
			this.uwComments = new ArrayList<>();
			
			for ( UnderwritingCommentDto ifDto : dto.uwComments) {
				this.uwComments.add(ifDto.copy());
			}
		}	
		
		if ( dto.policies != null ) {			
			this.policies = new ArrayList<>();
			
			for ( PolicyDto ifDto : dto.policies) {
				this.policies.add(ifDto.copy());
			}
		}

		this.createUser = dto.createUser;
		this.createDate = dto.createDate;
		this.updateUser = dto.updateUser;
		this.updateDate = dto.updateDate;
	}
	

	@Override
	public boolean equalsBK(ContractedFieldDetailDto other) {
		throw new UnsupportedOperationException("Not Implemented");
	}

	@Override
	public boolean equalsAll(ContractedFieldDetailDto other) {
		boolean result = false;
		
		if(other!=null) {
			result = true;
			DtoUtils dtoUtils = new DtoUtils(getLogger());
			result = result&&dtoUtils.equals("contractedFieldDetailId", contractedFieldDetailId, other.contractedFieldDetailId);
			result = result&&dtoUtils.equals("annualFieldDetailId", annualFieldDetailId, other.annualFieldDetailId);
			result = result&&dtoUtils.equals("growerContractYearId", growerContractYearId, other.growerContractYearId);
			result = result&&dtoUtils.equals("displayOrder", displayOrder, other.displayOrder);
			
		}
		
		return result;
	}
	
	@Override
	public Logger getLogger() {
		return logger;
	}

	@Override
	public ContractedFieldDetailDto copy() {
		return new ContractedFieldDetailDto(this);
	}
	
	public Integer getContractedFieldDetailId() {
		return contractedFieldDetailId;
	}
	public void setContractedFieldDetailId(Integer contractedFieldDetailId) {
		this.contractedFieldDetailId = contractedFieldDetailId;
	}

	public Integer getAnnualFieldDetailId() {
		return annualFieldDetailId;
	}
	public void setAnnualFieldDetailId(Integer annualFieldDetailId) {
		this.annualFieldDetailId = annualFieldDetailId;
	}

	public Integer getGrowerContractYearId() {
		return growerContractYearId;
	}
	public void setGrowerContractYearId(Integer growerContractYearId) {
		this.growerContractYearId = growerContractYearId;
	}
	
	public Integer getContractId() {
		return contractId;
	}
	public void setContractId(Integer contractId) {
		this.contractId = contractId;
	}

	public Integer getFieldId() {
		return fieldId;
	}
	public void setFieldId(Integer fieldId) {
		this.fieldId = fieldId;
	}

	public Integer getLegalLandId() {
		return legalLandId;
	}
	public void setLegalLandId(Integer legalLandId) {
		this.legalLandId = legalLandId;
	}

	public String getFieldLabel() {
		return fieldLabel;
	}
	public void setFieldLabel(String fieldLabel) {
		this.fieldLabel = fieldLabel;
	}

	public String getOtherLegalDescription() {
		return otherLegalDescription;
	}
	public void setOtherLegalDescription(String otherLegalDescription) {
		this.otherLegalDescription = otherLegalDescription;
	}

	public Integer getDisplayOrder() {
		return displayOrder;
	}
	public void setDisplayOrder(Integer displayOrder) {
		this.displayOrder = displayOrder;
	}

	public Integer getCropYear() {
		return cropYear;
	}
	public void setCropYear(Integer cropYear) {
		this.cropYear = cropYear;
	}

	public Integer getInsurancePlanId() {
		return insurancePlanId;
	}
	public void setInsurancePlanId(Integer insurancePlanId) {
		this.insurancePlanId = insurancePlanId;
	}

 	public String getInsurancePlanName() {
		return insurancePlanName;
	}

	public void setInsurancePlanName(String insurancePlanName) {
		this.insurancePlanName = insurancePlanName;
	}

	public List<InventoryFieldDto> getPlantings() {
		return plantings;
	}
	public void setPlantings(List<InventoryFieldDto> plantings) {
		this.plantings = plantings;
	}

	public List<UnderwritingCommentDto> getUwComments() {
		return uwComments;
	}
	public void setUwComments(List<UnderwritingCommentDto> uwComments) {
		this.uwComments = uwComments;
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

	public List<PolicyDto> getPolicies() {
		return policies;
	}

	public void setPolicies(List<PolicyDto> policies) {
		this.policies = policies;
	}
}
