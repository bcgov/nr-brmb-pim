package ca.bc.gov.mal.cirras.underwriting.data.models;

import java.io.Serializable;
import java.util.List;

import ca.bc.gov.nrs.wfone.common.model.Message;

public interface RenameLegalValidation<M extends Message, L extends LegalLand<? extends Field>, A extends AnnualField> extends Serializable {

	// Warnings.
	public static final String LEGALS_WITH_SAME_LOC_MSG = "Entered [LegalLocationOrPID] already exists in another Legal Land. If you would like to use the existing one please use the Replace Legal Land option." 
			+ " If you want to rename this one, please proceed.";
	public static final String OTHER_FIELD_ON_POLICY_MSG = "This legal land is associated with another field on the same contract, in this or another crop year. This will rename [LegalLocationOrPID] for all associated fields.";
	public static final String FIELD_ON_OTHER_POLICY_MSG = "This legal land is associated with the same or another field on a different policy. This will rename [LegalLocationOrPID] for all associated fields.";
	public static final String OTHER_LEGAL_DATA_MSG = "Legal Description, Short Legal Description or [PidOrLegalLocation] is filled in with non-default values. They may not match the new [LegalLocationOrPID].";

	
	public Boolean getIsWarningLegalsWithSameLoc();
	public void setIsWarningLegalsWithSameLoc(Boolean isWarningLegalsWithSameLoc);

	public M getLegalsWithSameLocMsg();
	public void setLegalsWithSameLocMsg(M legalsWithSameLocMsg);

	public List<L> getLegalsWithSameLocList();
	public void setLegalsWithSameLocList(List<L> legalsWithSameLocList);

	public Boolean getIsWarningOtherFieldOnPolicy();
	public void setIsWarningOtherFieldOnPolicy(Boolean isWarningOtherFieldOnPolicy);

	public M getOtherFieldOnPolicyMsg();
	public void setOtherFieldOnPolicyMsg(M otherFieldOnPolicyMsg);

	public List<A> getOtherFieldOnPolicyList();
	public void setOtherFieldOnPolicyList(List<A> otherFieldOnPolicyList);

	public Boolean getIsWarningFieldOnOtherPolicy();
	public void setIsWarningFieldOnOtherPolicy(Boolean isWarningFieldOnOtherPolicy);

	public M getFieldOnOtherPolicyMsg();
	public void setFieldOnOtherPolicyMsg(M fieldOnOtherPolicyMsg);

	public List<A> getFieldOnOtherPolicyList();
	public void setFieldOnOtherPolicyList(List<A> fieldOnOtherPolicyList);

	public Boolean getIsWarningOtherLegalData();
	public void setIsWarningOtherLegalData(Boolean isWarningOtherLegalData);

	public M getOtherLegalDataMsg();
	public void setOtherLegalDataMsg(M otherLegalDataMsg);

	public L getOtherLegalData();
	public void setOtherLegalData(L otherLegalData);
	
}
