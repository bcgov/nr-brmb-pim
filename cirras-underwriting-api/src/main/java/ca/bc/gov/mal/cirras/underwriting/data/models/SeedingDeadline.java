package ca.bc.gov.mal.cirras.underwriting.data.models;

import java.io.Serializable;
import java.util.Date;

//
//This is not going to be a resource.
//
public class SeedingDeadline implements Serializable {
	private static final long serialVersionUID = 1L;

	private String seedingDeadlineGuid;
	private String commodityTypeCode;
	private Integer cropYear;
	private Date fullCoverageDeadlineDate;
	private Date finalCoverageDeadlineDate;
	private Date fullCoverageDeadlineDateDefault;
	private Date finalCoverageDeadlineDateDefault;
	
	private Boolean deletedByUserInd;

	public String getSeedingDeadlineGuid() {
		return seedingDeadlineGuid;
	}

	public void setSeedingDeadlineGuid(String seedingDeadlineGuid) {
		this.seedingDeadlineGuid = seedingDeadlineGuid;
	}

	public String getCommodityTypeCode() {
		return commodityTypeCode;
	}

	public void setCommodityTypeCode(String commodityTypeCode) {
		this.commodityTypeCode = commodityTypeCode;
	}

	public Integer getCropYear() {
		return cropYear;
	}

	public void setCropYear(Integer cropYear) {
		this.cropYear = cropYear;
	}

	public Date getFullCoverageDeadlineDate() {
		return fullCoverageDeadlineDate;
	}

	public void setFullCoverageDeadlineDate(Date fullCoverageDeadlineDate) {
		this.fullCoverageDeadlineDate = fullCoverageDeadlineDate;
	}

	public Date getFullCoverageDeadlineDateDefault() {
		return fullCoverageDeadlineDateDefault;
	}

	public void setFullCoverageDeadlineDateDefault(Date fullCoverageDeadlineDateDefault) {
		this.fullCoverageDeadlineDateDefault = fullCoverageDeadlineDateDefault;
	}

	public Date getFinalCoverageDeadlineDate() {
		return finalCoverageDeadlineDate;
	}

	public void setFinalCoverageDeadlineDate(Date finalCoverageDeadlineDate) {
		this.finalCoverageDeadlineDate = finalCoverageDeadlineDate;
	}

	public Date getFinalCoverageDeadlineDateDefault() {
		return finalCoverageDeadlineDateDefault;
	}

	public void setFinalCoverageDeadlineDateDefault(Date finalCoverageDeadlineDateDefault) {
		this.finalCoverageDeadlineDateDefault = finalCoverageDeadlineDateDefault;
	}

	public Boolean getDeletedByUserInd() {
		return deletedByUserInd;
	}

	public void setDeletedByUserInd(Boolean deletedByUserInd) {
		this.deletedByUserInd = deletedByUserInd;
	}
}
