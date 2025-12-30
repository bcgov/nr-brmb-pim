package ca.bc.gov.mal.cirras.underwriting.data.models;

import java.io.Serializable;
import java.util.List;

import ca.bc.gov.nrs.wfone.common.model.Message;

public interface RemoveFieldValidation<M extends Message> extends Serializable {

	// Warnings.
	public static final String POLICY_HAS_PRODUCTS_MSG = "This Policy has products on it. Removing this field may impact insurable inventory.";
	public static final String FIELD_ON_OTHER_CONTRACTS_MSG = "This Field is associated with [numOtherContracts] other [policy].";
	public static final String FIELD_HAS_OTHER_INVENTORY_MSG = "This Field has Inventory for another year and/or plan.";
	public static final String FIELD_HAS_OTHER_COMMENTS_MSG = "This Field has one or more Comments for another year.";
	
	
	public Boolean getIsRemoveFromPolicyAllowed();
	public void setIsRemoveFromPolicyAllowed(Boolean isRemoveFromPolicyAllowed);

	public Boolean getIsDeleteFieldAllowed();
	public void setIsDeleteFieldAllowed(Boolean isDeleteFieldAllowed);
	
	public List<M> getRemoveFromPolicyWarnings();
	public void setRemoveFromPolicyWarnings(List<M> removeFromPolicyWarnings);

	public List<M> getDeleteFieldErrors();
	public void setDeleteFieldErrors(List<M> deleteFieldErrors);
		
}
