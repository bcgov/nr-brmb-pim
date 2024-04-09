package ca.bc.gov.mal.cirras.underwriting.model.v1;

import java.io.Serializable;

public class GradeModifierType implements Serializable {
	private static final long serialVersionUID = 1L;

	private String gradeModifierTypeCode;
	private String description;
	private Integer effectiveYear;
	private Integer expiryYear;
	
	private Boolean deleteAllowedInd;
	private Boolean deletedByUserInd;
	private Integer maxYearUsed;

	
	public String getGradeModifierTypeCode() {
		return gradeModifierTypeCode;
	}

	public void setGradeModifierTypeCode(String gradeModifierTypeCode) {
		this.gradeModifierTypeCode = gradeModifierTypeCode;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getEffectiveYear() {
		return effectiveYear;
	}

	public void setEffectiveYear(Integer effectiveYear) {
		this.effectiveYear = effectiveYear;
	}

	public Integer getExpiryYear() {
		return expiryYear;
	}

	public void setExpiryYear(Integer expiryYear) {
		this.expiryYear = expiryYear;
	}
	
	public Boolean getDeleteAllowedInd() {
		return deleteAllowedInd;
	}

	public void setDeleteAllowedInd(Boolean deleteAllowedInd) {
		this.deleteAllowedInd = deleteAllowedInd;
	}

	public Boolean getDeletedByUserInd() {
		return deletedByUserInd;
	}

	public void setDeletedByUserInd(Boolean deletedByUserInd) {
		this.deletedByUserInd = deletedByUserInd;
	}

	public Integer getMaxYearUsed() {
		return maxYearUsed;
	}

	public void setMaxYearUsed(Integer maxYearUsed) {
		this.maxYearUsed = maxYearUsed;
	}

}
