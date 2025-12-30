package ca.bc.gov.mal.cirras.underwriting.services.validation.constraints;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import ca.bc.gov.mal.cirras.underwriting.services.validation.Errors;

import java.util.List;

public interface RosterTemplateConstraints {

	public String getRosterTemplateGuid();

	@NotBlank(message=Errors.ROSTER_TEMPLATE_NAME_NOTBLANK, groups=RosterTemplateConstraints.class)
	@Size(min=0, max=250, message=Errors.ROSTER_TEMPLATE_NAME_SIZE, groups=RosterTemplateConstraints.class)
	public String getRosterTemplateName();

	@NotBlank(message=Errors.ROSTER_TEMPLATE_TYPE_CODE_NOT_BLANK, groups=RosterTemplateConstraints.class)
	public String getRosterTemplateTypeCode();

	@Size(min=0, max=4000, message=Errors.ROSTER_TEMPLATE_DESCRIPTION_SIZE, groups=RosterTemplateConstraints.class)
	public String getRosterTemplateDesc();

	@NotBlank(message=Errors.ROSTER_PERIOD_TYPE_CODE_NOT_BLANK, groups=RosterTemplateConstraints.class)
	public String getRosterPeriodTypeCode();

	public String getTransitionDayCode();

	public String getEffectiveDate();

	public String getExpiryDate();

}
