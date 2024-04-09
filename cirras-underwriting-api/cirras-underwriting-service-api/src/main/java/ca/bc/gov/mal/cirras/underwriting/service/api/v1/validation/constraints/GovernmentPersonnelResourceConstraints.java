package ca.bc.gov.mal.cirras.underwriting.service.api.v1.validation.constraints;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import ca.bc.gov.mal.cirras.underwriting.service.api.v1.validation.Errors;

public interface GovernmentPersonnelResourceConstraints {

	@NotBlank(message=Errors.EMPLOYEE_NUMBER_NOTBLANK, groups=GovernmentPersonnelResourceConstraints.class)
	@Size(min=0, max=15, message=Errors.EMPLOYEE_NUMBER_SIZE, groups=GovernmentPersonnelResourceConstraints.class)
	public String getEmployeeNumber();

	@Size(min=0, max=250, message=Errors.POSITION_TITLE_SIZE, groups=GovernmentPersonnelResourceConstraints.class)
	public String getPositionTitle();

}
