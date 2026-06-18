package ca.bc.gov.mal.cirras.underwriting.data.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

//
// This is not going to be a resource.
//
public class VerifiedYieldSummary implements Serializable {
	private static final long serialVersionUID = 1L;

	private String verifiedYieldSummaryGuid;
	private String verifiedYieldContractGuid;
	private Integer cropCommodityId;
	private Boolean isPedigreeInd;
	private Double productionAcres;
	private Double harvestedYield;
	private Double harvestedYieldPerAcre;
	private Double appraisedYield;
	private Double assessedYield;
	private Double yieldToCount;
	private Double yieldPercentPy;
	private Double productionGuarantee;
	private Double probableYield;
	private Double insurableValueHundredPercent;

	// Extended columns
	private String cropCommodityName;
	private Double totalInsuredAcres;
	
	private List<UnderwritingComment> uwComments = new ArrayList<UnderwritingComment>();
	
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
	
	public Double getProductionAcres() {
		return productionAcres;
	}
	
	public void setProductionAcres(Double productionAcres) {
		this.productionAcres = productionAcres;
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

	public String getCropCommodityName() {
		return cropCommodityName;
	}

	public void setCropCommodityName(String cropCommodityName) {
		this.cropCommodityName = cropCommodityName;
	}
	
	public List<UnderwritingComment> getUwComments() {
		return uwComments;
	}

	public void setUwComments(List<UnderwritingComment> uwComments) {
		this.uwComments = uwComments;
	}

	public Double getTotalInsuredAcres() {
		return totalInsuredAcres;
	}

	public void setTotalInsuredAcres(Double totalInsuredAcres) {
		this.totalInsuredAcres = totalInsuredAcres;
	}
	
}
