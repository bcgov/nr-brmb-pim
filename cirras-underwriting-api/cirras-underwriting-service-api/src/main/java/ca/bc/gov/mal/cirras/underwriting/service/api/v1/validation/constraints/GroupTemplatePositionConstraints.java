package ca.bc.gov.mal.cirras.underwriting.service.api.v1.validation.constraints;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import ca.bc.gov.mal.cirras.underwriting.service.api.v1.validation.Errors;

public interface GroupTemplatePositionConstraints {
	@NotBlank(message=Errors.RESOURCE_GROUP_POSITION_NAME_NOTBLANK, groups= GroupTemplatePositionConstraints.class)
	@Size(min=1, max=4000, message=Errors.RESOURCE_GROUP_POSITION_NAME_SIZE, groups= GroupTemplatePositionConstraints.class)
	public String getPositionName();

}
