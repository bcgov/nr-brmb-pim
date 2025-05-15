package ca.bc.gov.mal.cirras.underwriting.model.v1;

import java.io.Serializable;

//
// This is not going to be a resource.
//
public class VerifiedYieldAmendment implements Serializable {
	private static final long serialVersionUID = 1L;

	private String verifiedYieldAmendmentGuid;
	private String verifiedYieldAmendmentCode;
	private String verifiedYieldContractGuid;
	private Integer cropCommodityId;
	private Integer cropVarietyId;
	private Boolean isPedigreeInd;
	private Integer fieldId;
	private Double yieldPerAcre;
	private Double acres;
	private String rationale;

	// Extended columns
	private String cropCommodityName;
	private String cropVarietyName;
	private String fieldLabel;
	
	private Boolean deletedByUserInd;
	
	public String getVerifiedYieldAmendmentGuid() {
		return verifiedYieldAmendmentGuid;
	}
	public void setVerifiedYieldAmendmentGuid(String verifiedYieldAmendmentGuid) {
		this.verifiedYieldAmendmentGuid = verifiedYieldAmendmentGuid;
	}

	public String getVerifiedYieldAmendmentCode() {
		return verifiedYieldAmendmentCode;
	}
	public void setVerifiedYieldAmendmentCode(String verifiedYieldAmendmentCode) {
		this.verifiedYieldAmendmentCode = verifiedYieldAmendmentCode;
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

	public Integer getCropVarietyId() {
		return cropVarietyId;
	}

	public void setCropVarietyId(Integer cropVarietyId) {
		this.cropVarietyId = cropVarietyId;
	}

	public Boolean getIsPedigreeInd() {
		return isPedigreeInd;
	}
	public void setIsPedigreeInd(Boolean isPedigreeInd) {
		this.isPedigreeInd = isPedigreeInd;
	}

	public Integer getFieldId() {
		return fieldId;
	}
	public void setFieldId(Integer fieldId) {
		this.fieldId = fieldId;
	}

	public Double getYieldPerAcre() {
		return yieldPerAcre;
	}
	public void setYieldPerAcre(Double yieldPerAcre) {
		this.yieldPerAcre = yieldPerAcre;
	}

	public Double getAcres() {
		return acres;
	}
	public void setAcres(Double acres) {
		this.acres = acres;
	}

	public String getRationale() {
		return rationale;
	}
	public void setRationale(String rationale) {
		this.rationale = rationale;
	}

	public String getCropCommodityName() {
		return cropCommodityName;
	}
	public void setCropCommodityName(String cropCommodityName) {
		this.cropCommodityName = cropCommodityName;
	}

	public String getCropVarietyName() {
		return cropVarietyName;
	}

	public void setCropVarietyName(String cropVarietyName) {
		this.cropVarietyName = cropVarietyName;
	}
	
	public String getFieldLabel() {
		return fieldLabel;
	}
	public void setFieldLabel(String fieldLabel) {
		this.fieldLabel = fieldLabel;
	}

	public Boolean getDeletedByUserInd() {
		return deletedByUserInd;
	}
	public void setDeletedByUserInd(Boolean deletedByUserInd) {
		this.deletedByUserInd = deletedByUserInd;
	}
}
