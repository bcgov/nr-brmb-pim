package ca.bc.gov.mal.cirras.underwriting.data.models;

import java.io.Serializable;

public interface GradeModifier extends Serializable {

	public String getGradeModifierGuid();
	public void setGradeModifierGuid(String gradeModifierGuid);

	public Integer getCropCommodityId();
	public void setCropCommodityId(Integer cropCommodityId);

	public Integer getCropYear();
	public void setCropYear(Integer cropYear);

	public String getGradeModifierTypeCode();
	public void setGradeModifierTypeCode(String gradeModifierTypeCode);

	public Double getGradeModifierValue();
	public void setGradeModifierValue(Double gradeModifierValue);

 	public String getDescription();
	public void setDescription(String description);

	public Integer getInsurancePlanId();
	public void setInsurancePlanId(Integer insurancePlanId);

	public Boolean getDeleteAllowedInd();
	public void setDeleteAllowedInd(Boolean deleteAllowedInd);
	
	public Boolean getDeletedByUserInd();
	public void setDeletedByUserInd(Boolean deletedByUserInd);
}
