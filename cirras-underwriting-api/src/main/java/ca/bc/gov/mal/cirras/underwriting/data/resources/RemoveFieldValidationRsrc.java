package ca.bc.gov.mal.cirras.underwriting.data.resources;

import java.util.ArrayList;
import java.util.List;

import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlSeeAlso;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

import ca.bc.gov.mal.cirras.underwriting.data.resources.types.ResourceTypes;
import ca.bc.gov.mal.cirras.underwriting.data.models.RemoveFieldValidation;
import ca.bc.gov.nrs.common.wfone.rest.resource.BaseResource;
import ca.bc.gov.nrs.common.wfone.rest.resource.MessageRsrc;

@XmlRootElement(namespace = ResourceTypes.NAMESPACE, name = ResourceTypes.REMOVE_FIELD_VALIDATION_NAME)
@XmlSeeAlso({ RemoveFieldValidationRsrc.class })
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "@type")
public class RemoveFieldValidationRsrc extends BaseResource implements RemoveFieldValidation<MessageRsrc> {

	private static final long serialVersionUID = 1L;

	private Boolean isRemoveFromPolicyAllowed;
	private Boolean isDeleteFieldAllowed;
	
	private List<MessageRsrc> removeFromPolicyWarnings = new ArrayList<MessageRsrc>();	
	private List<MessageRsrc> deleteFieldErrors = new ArrayList<MessageRsrc>();


	public Boolean getIsRemoveFromPolicyAllowed() {
		return isRemoveFromPolicyAllowed;
	}
	public void setIsRemoveFromPolicyAllowed(Boolean isRemoveFromPolicyAllowed) {
		this.isRemoveFromPolicyAllowed = isRemoveFromPolicyAllowed;
	}

	public Boolean getIsDeleteFieldAllowed() {
		return isDeleteFieldAllowed;
	}
	public void setIsDeleteFieldAllowed(Boolean isDeleteFieldAllowed) {
		this.isDeleteFieldAllowed = isDeleteFieldAllowed;
	}
	
	public List<MessageRsrc> getRemoveFromPolicyWarnings() {
		return removeFromPolicyWarnings;
	}
	public void setRemoveFromPolicyWarnings(List<MessageRsrc> removeFromPolicyWarnings) {
		this.removeFromPolicyWarnings = removeFromPolicyWarnings;
	}

	public List<MessageRsrc> getDeleteFieldErrors() {
		return deleteFieldErrors;
	}
	public void setDeleteFieldErrors(List<MessageRsrc> deleteFieldErrors) {
		this.deleteFieldErrors = deleteFieldErrors;
	}
		
}
