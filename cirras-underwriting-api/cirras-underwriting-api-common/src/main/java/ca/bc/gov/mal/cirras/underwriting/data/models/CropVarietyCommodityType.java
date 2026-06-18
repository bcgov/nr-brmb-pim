package ca.bc.gov.mal.cirras.underwriting.data.models;

import java.io.Serializable;
import java.util.Date;

public class CropVarietyCommodityType implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String commodityTypeCode;
	private String description;
	private Integer cropVarietyId;
	private Integer insurancePlanId;
	private Date fullCoverageDeadlineDate;
	private Date finalCoverageDeadlineDate;

	
 	public String getCommodityTypeCode() {
		return commodityTypeCode;
	}

	public void setCommodityTypeCode(String commodityTypeCode) {
		this.commodityTypeCode = commodityTypeCode;
	}
	 
 	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getCropVarietyId() {
		return this.cropVarietyId;
	};
	
	public void setCropVarietyId(Integer cropVarietyId) {
		this.cropVarietyId = cropVarietyId;
	};
	
	public Integer getInsurancePlanId() {
		return this.insurancePlanId;
	};
	
	public void setInsurancePlanId(Integer insurancePlanId) {
		this.insurancePlanId = insurancePlanId;
	};

	public Date getFullCoverageDeadlineDate() {
		return fullCoverageDeadlineDate;
	}

	public void setFullCoverageDeadlineDate(Date fullCoverageDeadlineDate) {
		this.fullCoverageDeadlineDate = fullCoverageDeadlineDate;
	}

	public Date getFinalCoverageDeadlineDate() {
		return finalCoverageDeadlineDate;
	}

	public void setFinalCoverageDeadlineDate(Date finalCoverageDeadlineDate) {
		this.finalCoverageDeadlineDate = finalCoverageDeadlineDate;
	}

}
