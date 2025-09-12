package ca.bc.gov.mal.cirras.underwriting.service.api.v1.validation.constraints;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import ca.bc.gov.mal.cirras.underwriting.service.api.v1.validation.Errors;

public interface PersonResourceConstraints {

	@NotBlank(message=Errors.FIRST_NAME_NOTBLANK, groups=PersonResourceConstraints.class)
	@Size(min=0, max=50, message=Errors.FIRST_NAME_SIZE, groups=PersonResourceConstraints.class)
	public String getFirstName();

	@Size(min=0, max=50, message=Errors.MIDDLE_NAME_SIZE, groups=PersonResourceConstraints.class)
	public String getMiddleName();

	@NotBlank(message=Errors.LAST_NAME_NOTBLANK, groups=PersonResourceConstraints.class)
	@Size(min=0, max=50, message=Errors.LAST_NAME_SIZE, groups=PersonResourceConstraints.class)
	public String getLastName();

	@Size(min=0, max=4000, message=Errors.HOME_LOCATION_SIZE, groups=PersonResourceConstraints.class)
	public String getHomeLocation();

}
