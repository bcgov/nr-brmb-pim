package ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.nrs.wfone.common.persistence.dto.BaseDto;
import ca.bc.gov.nrs.wfone.common.persistence.utils.DtoUtils;

public class VerifiedYieldSummaryDto extends BaseDto<VerifiedYieldSummaryDto> {

	private static final long serialVersionUID = 1L;

	private static final Logger logger = LoggerFactory.getLogger(VerifiedYieldSummaryDto.class);

	private String verifiedYieldSummaryGuid;
	private String verifiedYieldContractGuid;
	private Integer cropCommodityId;
	private Boolean isPedigreeInd;
	private Double harvestedYield;
	private Double harvestedYieldPerAcre;
	private Double appraisedYield;
	private Double assessedYield;
	private Double yieldToCount;
	private Double yieldPercentPy;
	private Double productionGuarantee;
	private Double probableYield;
	private Double insurableValueHundredPercent;
	
	private String createUser;
	private Date createDate;
	private String updateUser;
	private Date updateDate;

	// Extended columns
	private String cropCommodityName;
	private Double totalInsuredAcres;
	
	//comments
	private List<UnderwritingCommentDto> uwComments = new ArrayList<UnderwritingCommentDto>();
	
	public VerifiedYieldSummaryDto() {
	}
	
	
	public VerifiedYieldSummaryDto(VerifiedYieldSummaryDto dto) {

		this.verifiedYieldSummaryGuid = dto.verifiedYieldSummaryGuid;
		this.verifiedYieldContractGuid = dto.verifiedYieldContractGuid;
		this.cropCommodityId = dto.cropCommodityId;
		this.isPedigreeInd = dto.isPedigreeInd;
		this.harvestedYield = dto.harvestedYield;
		this.harvestedYieldPerAcre = dto.harvestedYieldPerAcre;
		this.appraisedYield = dto.appraisedYield;
		this.assessedYield = dto.assessedYield;
		this.yieldToCount = dto.yieldToCount;
		this.yieldPercentPy = dto.yieldPercentPy;
		this.productionGuarantee = dto.productionGuarantee;
		this.probableYield = dto.probableYield;
		this.insurableValueHundredPercent = dto.insurableValueHundredPercent;
		
		this.createUser = dto.createUser;
		this.createDate = dto.createDate;
		this.updateUser = dto.updateUser;
		this.updateDate = dto.updateDate;

		this.cropCommodityName = dto.cropCommodityName;
		this.totalInsuredAcres = dto.totalInsuredAcres;

		if ( dto.uwComments != null ) {			
			this.uwComments = new ArrayList<>();
			
			for ( UnderwritingCommentDto ifDto : dto.uwComments) {
				this.uwComments.add(ifDto.copy());
			}
		}

	}
	

	@Override
	public boolean equalsBK(VerifiedYieldSummaryDto other) {
		throw new UnsupportedOperationException("Not Implemented");
	}

	@Override
	public boolean equalsAll(VerifiedYieldSummaryDto other) {
		boolean result = false;
		
		if(other!=null) {
			Integer decimalPrecision = 4;
			result = true;
			DtoUtils dtoUtils = new DtoUtils(getLogger());
			
			result = result&&dtoUtils.equals("verifiedYieldSummaryGuid", verifiedYieldSummaryGuid, other.verifiedYieldSummaryGuid);
			result = result&&dtoUtils.equals("verifiedYieldContractGuid", verifiedYieldContractGuid, other.verifiedYieldContractGuid);
			result = result&&dtoUtils.equals("cropCommodityId", cropCommodityId, other.cropCommodityId);
			result = result&&dtoUtils.equals("isPedigreeInd", isPedigreeInd, other.isPedigreeInd);
			result = result&&dtoUtils.equals("harvestedYield", harvestedYield, other.harvestedYield, decimalPrecision);
			result = result&&dtoUtils.equals("harvestedYieldPerAcre", harvestedYieldPerAcre, other.harvestedYieldPerAcre, decimalPrecision);
			result = result&&dtoUtils.equals("appraisedYield", appraisedYield, other.appraisedYield, decimalPrecision);
			result = result&&dtoUtils.equals("assessedYield", assessedYield, other.assessedYield, decimalPrecision);
			result = result&&dtoUtils.equals("yieldToCount", yieldToCount, other.yieldToCount, decimalPrecision);
			result = result&&dtoUtils.equals("yieldPercentPy", yieldPercentPy, other.yieldPercentPy, decimalPrecision);
			result = result&&dtoUtils.equals("productionGuarantee", productionGuarantee, other.productionGuarantee, decimalPrecision);
			result = result&&dtoUtils.equals("probableYield", probableYield, other.probableYield, decimalPrecision);
			result = result&&dtoUtils.equals("insurableValueHundredPercent", insurableValueHundredPercent, other.insurableValueHundredPercent, decimalPrecision);
		}
		
		return result;
	}
	
	@Override
	public Logger getLogger() {
		return logger;
	}

	@Override
	public VerifiedYieldSummaryDto copy() {
		return new VerifiedYieldSummaryDto(this);
	}

	public String getVerifiedYieldSummaryGuid() {
		return verifiedYieldSummaryGuid;
	}

	public void setVerifiedYieldSummaryGuid(String verifiedYieldSummaryGuid) {
		this.verifiedYieldSummaryGuid = verifiedYieldSummaryGuid;
	}

	public String getVerifiedYieldContractGuid() {
		return verifiedYieldContractGuid;
	}

	public void setVerifiedYieldContractGuid(String verifiedYieldContractGuid) {
		this.verifiedYieldContractGuid = verifiedYieldContractGuid;
	}

	public Integer getCropCommodityId() {
		return cropCommodityId;
	}

	public void setCropCommodityId(Integer cropCommodityId) {
		this.cropCommodityId = cropCommodityId;
	}

	public Boolean getIsPedigreeInd() {
		return isPedigreeInd;
	}

	public void setIsPedigreeInd(Boolean isPedigreeInd) {
		this.isPedigreeInd = isPedigreeInd;
	}

	public Double getHarvestedYield() {
		return harvestedYield;
	}

	public void setHarvestedYield(Double harvestedYield) {
		this.harvestedYield = harvestedYield;
	}

	public Double getHarvestedYieldPerAcre() {
		return harvestedYieldPerAcre;
	}

	public void setHarvestedYieldPerAcre(Double harvestedYieldPerAcre) {
		this.harvestedYieldPerAcre = harvestedYieldPerAcre;
	}

	public Double getAppraisedYield() {
		return appraisedYield;
	}

	public void setAppraisedYield(Double appraisedYield) {
		this.appraisedYield = appraisedYield;
	}

	public Double getAssessedYield() {
		return assessedYield;
	}

	public void setAssessedYield(Double assessedYield) {
		this.assessedYield = assessedYield;
	}

	public Double getYieldToCount() {
		return yieldToCount;
	}

	public void setYieldToCount(Double yieldToCount) {
		this.yieldToCount = yieldToCount;
	}

	public Double getYieldPercentPy() {
		return yieldPercentPy;
	}

	public void setYieldPercentPy(Double yieldPercentPy) {
		this.yieldPercentPy = yieldPercentPy;
	}

	public Double getProductionGuarantee() {
		return productionGuarantee;
	}

	public void setProductionGuarantee(Double productionGuarantee) {
		this.productionGuarantee = productionGuarantee;
	}

	public Double getProbableYield() {
		return probableYield;
	}

	public void setProbableYield(Double probableYield) {
		this.probableYield = probableYield;
	}
	
	public Double getInsurableValueHundredPercent() {
		return insurableValueHundredPercent;
	}

	public void setInsurableValueHundredPercent(Double insurableValueHundredPercent) {
		this.insurableValueHundredPercent = insurableValueHundredPercent;
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

	public String getCropCommodityName() {
		return cropCommodityName;
	}

	public void setCropCommodityName(String cropCommodityName) {
		this.cropCommodityName = cropCommodityName;
	}

	public Double getTotalInsuredAcres() {
		return totalInsuredAcres;
	}

	public void setTotalInsuredAcres(Double totalInsuredAcres) {
		this.totalInsuredAcres = totalInsuredAcres;
	}

	public List<UnderwritingCommentDto> getUwComments() {
		return uwComments;
	}
	public void setUwComments(List<UnderwritingCommentDto> uwComments) {
		this.uwComments = uwComments;
	}	
}
