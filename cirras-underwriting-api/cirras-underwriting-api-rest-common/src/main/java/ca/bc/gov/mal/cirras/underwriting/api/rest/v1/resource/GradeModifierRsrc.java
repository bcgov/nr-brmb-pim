package ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource;

import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlSeeAlso;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.types.ResourceTypes;
import ca.bc.gov.mal.cirras.underwriting.model.v1.GradeModifier;
import ca.bc.gov.nrs.common.wfone.rest.resource.BaseResource;

@XmlRootElement(namespace = ResourceTypes.NAMESPACE, name = ResourceTypes.GRADE_MODIFIER_NAME)
@XmlSeeAlso({ GradeModifierRsrc.class })
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "@type")
public class GradeModifierRsrc extends BaseResource implements GradeModifier {

	private static final long serialVersionUID = 1L;
	
	private String gradeModifierGuid;
	private Integer cropCommodityId;
	private Integer cropYear;
	private String gradeModifierTypeCode;
	private Double gradeModifierValue;
	
	//Extended fields
	private String description;
	private Integer insurancePlanId;
	private Boolean deleteAllowedInd;
	private Boolean deletedByUserInd;	

	public String getGradeModifierGuid() {
		return gradeModifierGuid;
	}

	public void setGradeModifierGuid(String gradeModifierGuid) {
		this.gradeModifierGuid = gradeModifierGuid;
	}

	public Integer getCropCommodityId() {
		return cropCommodityId;
	}

	public void setCropCommodityId(Integer cropCommodityId) {
		this.cropCommodityId = cropCommodityId;
	}

	public Integer getCropYear() {
		return cropYear;
	}

	public void setCropYear(Integer cropYear) {
		this.cropYear = cropYear;
	}

	public String getGradeModifierTypeCode() {
		return gradeModifierTypeCode;
	}

	public void setGradeModifierTypeCode(String gradeModifierTypeCode) {
		this.gradeModifierTypeCode = gradeModifierTypeCode;
	}

	public Double getGradeModifierValue() {
		return gradeModifierValue;
	}

	public void setGradeModifierValue(Double gradeModifierValue) {
		this.gradeModifierValue = gradeModifierValue;
	} 

 	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getInsurancePlanId() {
		return insurancePlanId;
	}

	public void setInsurancePlanId(Integer insurancePlanId) {
		this.insurancePlanId = insurancePlanId;
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
}
