package ca.bc.gov.mal.cirras.underwriting.model.v1;

import java.io.Serializable;
import java.util.List;

import ca.bc.gov.nrs.wfone.common.model.Message;

public interface ReplaceLegalValidation<M extends Message, L extends LegalLand<? extends Field>, A extends AnnualField> extends Serializable {

	// Warnings.
	public static final String FIELD_ON_OTHER_POLICY_MSG = "The field [fieldLabel] (ID: [fieldId]) is also on another policy [policyNumber] in this crop year.";
	public static final String FIELD_HAS_OTHER_LEGAL_MSG = "There are other legal locations associated with the field [fieldLabel] (ID: [fieldId]).";
	public static final String OTHER_FIELD_ON_LEGAL_MSG = "The Legal Location [otherDescription] is associated with other field(s).";
	
	//Field on other policy
	public Boolean getIsWarningFieldOnOtherPolicy();
	public void setIsWarningFieldOnOtherPolicy(Boolean isWarningFieldOnOtherPolicy);

	public M getFieldOnOtherPolicyMsg();
	public void setFieldOnOtherPolicyMsg(M fieldOnOtherPolicyMsg);

	//Field associated with other legal land
	public Boolean getIsWarningFieldHasOtherLegalLand();
	public void setIsWarningFieldHasOtherLegalLand(Boolean isWarningFieldHasOtherLegalLand);

	public M getFieldHasOtherLegalLandMsg();
	public void setFieldHasOtherLegalLandMsg(M fieldHasOtherLegalLandMsg);

	public List<L> getOtherLegalLandOfFieldList();
	public void setOtherLegalLandOfFieldList(List<L> otherLegalLandOfFieldList);

	//Other fields associated with legal land
	public Boolean getIsWarningOtherFieldsOnLegal();
	public void setIsWarningOtherFieldsOnLegal(Boolean isWarningOtherFieldsOnLegal);

	public M getOtherFieldsOnLegalMsg();
	public void setOtherFieldsOnLegalMsg(M otherFieldsOnLegalMsg);
	
	public List<A> getOtherFieldsOnLegalLandList();
	public void setOtherFieldsOnLegalLandList(List<A> otherFieldsOnLegalLandList);
	
}
