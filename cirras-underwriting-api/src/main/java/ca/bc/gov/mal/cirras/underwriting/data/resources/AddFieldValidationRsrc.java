package ca.bc.gov.mal.cirras.underwriting.data.resources;

import java.util.ArrayList;
import java.util.List;

import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlSeeAlso;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

import ca.bc.gov.mal.cirras.underwriting.data.resources.types.ResourceTypes;

import ca.bc.gov.nrs.common.wfone.rest.resource.BaseResource;
import ca.bc.gov.nrs.common.wfone.rest.resource.MessageRsrc;

@XmlRootElement(namespace = ResourceTypes.NAMESPACE, name = ResourceTypes.ADD_FIELD_VALIDATION_NAME)
@XmlSeeAlso({ AddFieldValidationRsrc.class })
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "@type")
public class AddFieldValidationRsrc extends BaseResource {

	private static final long serialVersionUID = 1L;
	
	// Errors
	public static final String FIELD_ALREADY_ON_POLICY_MSG = "This Field is already on this Policy. Please add a new planting instead.";
	public static final String FIELD_ON_INCOMPATIBLE_PLAN_MSG = "This Field is associated with another plan other than [insurancePlans].";
	public static final String TRANSFER_POLICY_ID_NOT_EMPTY_MSG = "This Field is not on another Policy of the same plan in this year, but a Transfer From policy has been specified.";
	public static final String TRANSFER_POLICY_ID_INCORRECT_MSG = "This Field is on another Policy of the same plan in this year, but the specified Transfer From policy does not match the expected policy.";
	public static final String TRANSFER_POLICY_HAS_DOP_MSG = "This Field is on another Policy of the same plan in the same year with DOP data, please remove the DOP data associated with this field to allow the transfer to proceed.";
	public static final String TRANSFER_POLICY_HAS_PRODUCTS_MSG = "This Field is on another Policy of the same plan in the same year with products on it. Please select another field or remove the products from the other policy in CIRRAS.";
	
	// Warnings
	public static final String ADD_FIELD_TO_SECOND_POLICY_WARNING_MSG = "This Field is already on a Policy of another plan in this year. This change will add the Field to this Policy but also keep it associated to the other Policy.";
	public static final String TRANSFER_POLICY_WARNING_MSG = "This Field is already on another Policy of the same plan in this year. This change will transfer all plantings on the Field for that plan to this Policy.";

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
