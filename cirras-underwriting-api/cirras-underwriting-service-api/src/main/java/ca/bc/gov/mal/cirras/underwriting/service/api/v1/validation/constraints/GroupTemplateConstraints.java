package ca.bc.gov.mal.cirras.underwriting.service.api.v1.validation.constraints;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import ca.bc.gov.mal.cirras.underwriting.service.api.v1.validation.Errors;

public interface GroupTemplateConstraints {

	public String getResourceGroupTemplateGuid();

	@NotBlank(message=Errors.RESOURCE_GROUP_TEMPLATE_NAME_NOTBLANK, groups=GroupTemplateConstraints.class)
	@Size(min=0, max=250, message=Errors.RESOURCE_GROUP_TEMPLATE_NAME_SIZE, groups=GroupTemplateConstraints.class)
	public String getGroupTemplateName();

	public String getResourceClassificationDefinitionGuid();

	public String getResourceClassificationName();

	@Size(min=0, max=4000, message=Errors.DESCRIPTION_SIZE, groups=GroupTemplateConstraints.class)
	public String getDescription();

	public Long getGroupTemplateComplement();

}
