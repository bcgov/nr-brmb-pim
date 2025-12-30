package ca.bc.gov.mal.cirras.underwriting.services.validation.constraints;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import ca.bc.gov.mal.cirras.underwriting.services.validation.Errors;

public interface ResourceAddressConstraints {
	public String getResourceAddressGuid();

	@NotBlank(message=Errors.RESOURCE_ADDRESS_ADDRESS_TYPE_NOTBLANK, groups= ResourceAddressConstraints.class)
	public String getResourceAddressTypeCode();

	@NotBlank(message=Errors.RESOURCE_ADDRESS_DESCRIPTION_NOTBLANK, groups= ResourceAddressConstraints.class)
	@Size(min=0, max=200, message=Errors.RESOURCE_ADDRESS_DESCRIPTION_SIZE, groups= ResourceAddressConstraints.class)
	public String getAddressDescription();

	@NotBlank(message=Errors.RESOURCE_ADDRESS_MUNICIPALITY_NOTBLANK, groups= ResourceAddressConstraints.class)
	@Size(min=0, max=120, message=Errors.RESOURCE_ADDRESS_MUNICIPALITY_SIZE, groups= ResourceAddressConstraints.class)
	public String getMunicipality();

}
