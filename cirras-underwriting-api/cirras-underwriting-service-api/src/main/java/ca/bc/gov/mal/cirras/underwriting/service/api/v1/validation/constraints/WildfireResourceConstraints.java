package ca.bc.gov.mal.cirras.underwriting.services.validation.constraints;

import jakarta.validation.constraints.Size;

import ca.bc.gov.mal.cirras.underwriting.services.validation.Errors;

public interface WildfireResourceConstraints {

	// no constraint
	public String getWildfireResourceId();
	
	// no constraint
	public String getResourceAgencyCode();

	@Size(min=0, max=250, message= Errors.CALL_SIGN_SIZE, groups=WildfireResourceConstraints.class)
	public String getCallSign();
}
