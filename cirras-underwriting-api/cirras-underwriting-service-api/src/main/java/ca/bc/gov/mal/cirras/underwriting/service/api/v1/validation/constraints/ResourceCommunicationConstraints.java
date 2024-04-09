package ca.bc.gov.mal.cirras.underwriting.service.api.v1.validation.constraints;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import ca.bc.gov.mal.cirras.underwriting.service.api.v1.validation.Errors;

public interface ResourceCommunicationConstraints {
	public String getResourceCommunicationGuid();

	@NotBlank(message=Errors.RESOURCE_COMMUNICATION_METHOD_TYPE_NOTBLANK, groups= ResourceCommunicationConstraints.class)
	public String getCommunicationMethodTypeCode();

	@NotBlank(message=Errors.RESOURCE_COMMUNICATION_DETAIL_NOTBLANK, groups= ResourceCommunicationConstraints.class)
	@Size(min=0, max=250, message=Errors.RESOURCE_COMMUNICATION_DETAIL_SIZE, groups= ResourceCommunicationConstraints.class)
	public String getCommunicationDetail();

}
