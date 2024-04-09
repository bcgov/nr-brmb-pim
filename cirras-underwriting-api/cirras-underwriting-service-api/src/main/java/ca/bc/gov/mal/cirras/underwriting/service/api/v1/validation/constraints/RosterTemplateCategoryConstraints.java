package ca.bc.gov.mal.cirras.underwriting.service.api.v1.validation.constraints;

import org.springframework.format.annotation.NumberFormat;

import ca.bc.gov.mal.cirras.underwriting.service.api.v1.validation.Errors;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;

public interface RosterTemplateCategoryConstraints {

	public String getRosterTemplateCategoryGuid();
	public void setRosterTemplateCategoryGuid(String rosterTemplateCategoryGuid);

	@NotBlank(message=Errors.ROSTER_TEMPLATE_CATEGORY_LABEL_NOTBLANK, groups=RosterTemplateCategoryConstraints.class)
	@Size(min=0, max=50, message=Errors.ROSTER_TEMPLATE_CATEGORY_LABEL_SIZE, groups=RosterTemplateCategoryConstraints.class)
	public String getRosterCategoryLabel();
	public void setRosterCategoryLabel(String rosterCategoryLabel);

	public String getEffectiveDate();
	public void setEffectiveDate(String effectiveDate);

	public String getExpiryDate();
	public void setExpiryDate(String expiryDate);

	@NotBlank(message=Errors.ROSTER_CATEGORY_RSRC_TYPE_CODE_NOT_BLANK, groups=RosterTemplateCategoryConstraints.class)
	public String getRosterCategoryRsrcTypeCode();
	public void setRosterCategoryRsrcTypeCode(String rosterCategoryRsrcTypeCode);

	@Size(min=0, max=150, message=Errors.ROSTER_TEMPLATE_CATEGORY_DESCRIPTION_SIZE, groups=RosterTemplateCategoryConstraints.class)
	public String getDescription();
	public void setDescription(String description);

	@Positive(message=Errors.ROSTER_TEMPLATE_CATEGORY_DISPLAY_ORDER_POSITIVE, groups=RosterTemplateCategoryConstraints.class)
	@Digits(integer=3, fraction=0, message=Errors.ROSTER_TEMPLATE_CATEGORY_DISPLAY_ORDER_DIGITS, groups=RosterTemplateCategoryConstraints.class)
	public Long getDisplayOrder();
	public void setDisplayOrder(Long displayOrder);

	@NotNull(message=Errors.ROSTER_TEMPLATE_CATEGORY_MANDATORY_CATEGORY_IND_NOTNULL, groups=RosterTemplateCategoryConstraints.class)
	public Boolean getMandatoryCategoryInd();
	public void setMandatoryCategoryInd(Boolean mandatoryCategoryInd);

}
