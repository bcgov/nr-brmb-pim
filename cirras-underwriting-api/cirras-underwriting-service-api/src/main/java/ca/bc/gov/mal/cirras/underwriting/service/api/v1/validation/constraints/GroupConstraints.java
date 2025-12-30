package ca.bc.gov.mal.cirras.underwriting.services.validation.constraints;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import ca.bc.gov.mal.cirras.underwriting.services.validation.Errors;

public interface GroupConstraints {

	public String getResourceGroupGuid();

	@NotBlank(message=Errors.RESOURCE_GROUP_NAME_NOTBLANK, groups=GroupConstraints.class)
	@Size(min=0, max=250, message=Errors.RESOURCE_GROUP_NAME_SIZE, groups=GroupConstraints.class)
	public String getGroupName();

	@NotBlank(message=Errors.RESOURCE_GROUP_TEMPLATE_NAME_NOTBLANK, groups=GroupConstraints.class)
	public String getGroupTemplateName();

	@NotBlank(message=Errors.RESOURCE_AGENCY_CODE_NOT_BLANK, groups=GroupConstraints.class)
	public String getResourceAgencyCode();

	public String getResourceAgencyDescription();

	@Size(min=0, max=250, message=Errors.HOME_LOCATION_SIZE, groups=GroupConstraints.class)
	public String getHomeLocation();

	public Long getFireCentreIdentifier();

	public String getFireCentreName();

	public Long getZoneIdentifier();

	public String getZoneName();

	public String getGroupStatusCode();

	public String getGroupStatusEffectiveDate();

	public String getGroupStatusExpiryDate();

}
