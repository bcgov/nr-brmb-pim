package ca.bc.gov.mal.cirras.underwriting.data.models;

import java.io.Serializable;

public class LegalLandRiskArea implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Integer riskAreaId;
	private Integer insurancePlanId;
	private String riskAreaName;
	private String description;
	private Integer legalLandId;
	private String insurancePlanName;
	private Boolean deletedByUserInd;

	
	public Integer getRiskAreaId() {
		return riskAreaId;
	}

	public void setRiskAreaId(Integer riskAreaId) {
		this.riskAreaId = riskAreaId;
	}

	public Integer getInsurancePlanId() {
		return insurancePlanId;
	}

	public void setInsurancePlanId(Integer insurancePlanId) {
		this.insurancePlanId = insurancePlanId;
	}

	public String getRiskAreaName() {
		return riskAreaName;
	}

	public void setRiskAreaName(String riskAreaName) {
		this.riskAreaName = riskAreaName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getLegalLandId() {
		return legalLandId;
	}

	public void setLegalLandId(Integer legalLandId) {
		this.legalLandId = legalLandId;
	}

	public String getInsurancePlanName() {
		return insurancePlanName;
	}

	public void setInsurancePlanName(String insurancePlanName) {
		this.insurancePlanName = insurancePlanName;
	}


	public Boolean getDeletedByUserInd() {
		return deletedByUserInd;
	}
	public void setDeletedByUserInd(Boolean deletedByUserInd) {
		this.deletedByUserInd = deletedByUserInd;
	}}
