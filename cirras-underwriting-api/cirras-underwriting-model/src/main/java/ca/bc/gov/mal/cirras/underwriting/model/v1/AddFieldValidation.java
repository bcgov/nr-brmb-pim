package ca.bc.gov.mal.cirras.underwriting.model.v1;

import java.io.Serializable;
import java.util.List;

import ca.bc.gov.nrs.wfone.common.model.Message;

public interface AddFieldValidation<M extends Message> extends Serializable {

	// Errors
	public static final String FIELD_ALREADY_ON_POLICY_MSG = "This Field is already on this Policy. Please add a new planting instead.";
	public static final String FIELD_ON_INCOMPATIBLE_PLAN_MSG = "This Field is associated with another plan other than Grain or Forage.";
	public static final String TRANSFER_POLICY_ID_NOT_EMPTY_MSG = "This Field is not on another Policy of the same plan in this year, but a Transfer From policy has been specified.";
	public static final String TRANSFER_POLICY_ID_INCORRECT_MSG = "This Field is on another Policy of the same plan in this year, but the specified Transfer From policy does not match the expected policy.";
	public static final String TRANSFER_POLICY_HAS_DOP_MSG = "This Field is on another Policy of the same plan in the same year with DOP data, please remove the DOP data associated with this field to allow the transfer to proceed.";
	public static final String TRANSFER_POLICY_HAS_PRODUCTS_MSG = "This Field is on another Policy of the same plan in the same year with products on it. Please select another field or remove the products from the other policy in CIRRAS.";
	
	// Warnings
	public static final String ADD_FIELD_TO_SECOND_POLICY_WARNING_MSG = "This Field is already on a Policy of another plan in this year. This change will add the Field to this Policy but also keep it associated to the other Policy.";
	public static final String TRANSFER_POLICY_WARNING_MSG = "This Field is already on another Policy of the same plan in this year. This change will transfer all plantings on the Field for that plan to this Policy.";
	
	public List<M> getWarningMessages();
	public void setWarningMessages(List<M> warningMessages);

	public List<M> getErrorMessages();
	public void setErrorMessages(List<M> errorMessages);
	
}
