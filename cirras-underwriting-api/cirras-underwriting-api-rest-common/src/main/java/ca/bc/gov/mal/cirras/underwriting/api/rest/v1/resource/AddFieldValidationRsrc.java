package ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource;

import java.util.ArrayList;
import java.util.List;

import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlSeeAlso;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.types.ResourceTypes;
import ca.bc.gov.mal.cirras.underwriting.model.v1.AddFieldValidation;

import ca.bc.gov.nrs.common.wfone.rest.resource.BaseResource;
import ca.bc.gov.nrs.common.wfone.rest.resource.MessageRsrc;

@XmlRootElement(namespace = ResourceTypes.NAMESPACE, name = ResourceTypes.ADD_FIELD_VALIDATION_NAME)
@XmlSeeAlso({ AddFieldValidationRsrc.class })
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "@type")
public class AddFieldValidationRsrc extends BaseResource implements AddFieldValidation<MessageRsrc> {

	private static final long serialVersionUID = 1L;

	private List<MessageRsrc> warningMessages = new ArrayList<MessageRsrc>();
	
	private List<MessageRsrc> errorMessages = new ArrayList<MessageRsrc>();

	public List<MessageRsrc> getWarningMessages() {
		return warningMessages;
	}
	public void setWarningMessages(List<MessageRsrc> warningMessages) {
		this.warningMessages = warningMessages;
	}

	public List<MessageRsrc> getErrorMessages() {
		return errorMessages;
	}
	public void setErrorMessages(List<MessageRsrc> errorMessages) {
		this.errorMessages = errorMessages;
	}
		
}
