package ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource;

import java.util.ArrayList;
import java.util.List;

import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlSeeAlso;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.types.ResourceTypes;
import ca.bc.gov.mal.cirras.underwriting.model.v1.RenameLegalValidation;
import ca.bc.gov.nrs.common.wfone.rest.resource.BaseResource;
import ca.bc.gov.nrs.common.wfone.rest.resource.MessageRsrc;

@XmlRootElement(namespace = ResourceTypes.NAMESPACE, name = ResourceTypes.RENAME_LEGAL_VALIDATION_NAME)
@XmlSeeAlso({ RenameLegalValidationRsrc.class })
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "@type")
public class RenameLegalValidationRsrc extends BaseResource implements RenameLegalValidation<MessageRsrc, LegalLandRsrc, AnnualFieldRsrc> {

	private static final long serialVersionUID = 1L;

	private Boolean isWarningLegalsWithSameLoc;
	private MessageRsrc legalsWithSameLocMsg;
	private List<LegalLandRsrc> legalsWithSameLocList = new ArrayList<LegalLandRsrc>();

	private Boolean isWarningOtherFieldOnPolicy;
	private MessageRsrc otherFieldOnPolicyMsg;
	private List<AnnualFieldRsrc> otherFieldOnPolicyList = new ArrayList<AnnualFieldRsrc>();
	
	private Boolean isWarningFieldOnOtherPolicy;
	private MessageRsrc fieldOnOtherPolicyMsg;
	private List<AnnualFieldRsrc> fieldOnOtherPolicyList = new ArrayList<AnnualFieldRsrc>();

	private Boolean isWarningOtherLegalData;
	private MessageRsrc otherLegalDataMsg;
	private LegalLandRsrc otherLegalData;

	public Boolean getIsWarningLegalsWithSameLoc() {
		return isWarningLegalsWithSameLoc;
	}
	public void setIsWarningLegalsWithSameLoc(Boolean isWarningLegalsWithSameLoc) {
		this.isWarningLegalsWithSameLoc = isWarningLegalsWithSameLoc;
	}

	public MessageRsrc getLegalsWithSameLocMsg() {
		return legalsWithSameLocMsg;
	}
	public void setLegalsWithSameLocMsg(MessageRsrc legalsWithSameLocMsg) {
		this.legalsWithSameLocMsg = legalsWithSameLocMsg;
	}

	public List<LegalLandRsrc> getLegalsWithSameLocList() {
		return legalsWithSameLocList;
	}
	public void setLegalsWithSameLocList(List<LegalLandRsrc> legalsWithSameLocList) {
		this.legalsWithSameLocList = legalsWithSameLocList;
	}

	public Boolean getIsWarningOtherFieldOnPolicy() {
		return isWarningOtherFieldOnPolicy;
	}
	public void setIsWarningOtherFieldOnPolicy(Boolean isWarningOtherFieldOnPolicy) {
		this.isWarningOtherFieldOnPolicy = isWarningOtherFieldOnPolicy;
	}

	public MessageRsrc getOtherFieldOnPolicyMsg() {
		return otherFieldOnPolicyMsg;
	}
	public void setOtherFieldOnPolicyMsg(MessageRsrc otherFieldOnPolicyMsg) {
		this.otherFieldOnPolicyMsg = otherFieldOnPolicyMsg;
	}

	public List<AnnualFieldRsrc> getOtherFieldOnPolicyList() {
		return otherFieldOnPolicyList;
	}
	public void setOtherFieldOnPolicyList(List<AnnualFieldRsrc> otherFieldOnPolicyList) {
		this.otherFieldOnPolicyList = otherFieldOnPolicyList;
	}

	public Boolean getIsWarningFieldOnOtherPolicy() {
		return isWarningFieldOnOtherPolicy;
	}
	public void setIsWarningFieldOnOtherPolicy(Boolean isWarningFieldOnOtherPolicy) {
		this.isWarningFieldOnOtherPolicy = isWarningFieldOnOtherPolicy;
	}

	public MessageRsrc getFieldOnOtherPolicyMsg() {
		return fieldOnOtherPolicyMsg;
	}
	public void setFieldOnOtherPolicyMsg(MessageRsrc fieldOnOtherPolicyMsg) {
		this.fieldOnOtherPolicyMsg = fieldOnOtherPolicyMsg;
	}

	public List<AnnualFieldRsrc> getFieldOnOtherPolicyList() {
		return fieldOnOtherPolicyList;
	}
	public void setFieldOnOtherPolicyList(List<AnnualFieldRsrc> fieldOnOtherPolicyList) {
		this.fieldOnOtherPolicyList = fieldOnOtherPolicyList;
	}

	public Boolean getIsWarningOtherLegalData() {
		return isWarningOtherLegalData;
	}
	public void setIsWarningOtherLegalData(Boolean isWarningOtherLegalData) {
		this.isWarningOtherLegalData = isWarningOtherLegalData;
	}

	public MessageRsrc getOtherLegalDataMsg() {
		return otherLegalDataMsg;
	}
	public void setOtherLegalDataMsg(MessageRsrc otherLegalDataMsg) {
		this.otherLegalDataMsg = otherLegalDataMsg;
	}

	public LegalLandRsrc getOtherLegalData() {
		return otherLegalData;
	}
	public void setOtherLegalData(LegalLandRsrc otherLegalData) {
		this.otherLegalData = otherLegalData;
	}
		
}
