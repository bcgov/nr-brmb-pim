package ca.bc.gov.mal.cirras.underwriting.service.api.v1.validation.constraints;

import jakarta.validation.constraints.NotBlank;

import ca.bc.gov.mal.cirras.underwriting.service.api.v1.validation.Errors;

public interface GroupPositionQualificationConstraints {
	@NotBlank(message=Errors.RESOURCE_POSITION_QUALIFICATION_CLASS_NAME_NOTBLANK, groups= GroupPositionQualificationConstraints.class)
	public String getResourceClassificationName();

}
