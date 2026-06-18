package ca.bc.gov.mal.cirras.underwriting.data.resources;

import java.util.ArrayList;
import java.util.List;

import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlSeeAlso;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

import ca.bc.gov.mal.cirras.underwriting.data.resources.types.ResourceTypes;
import ca.bc.gov.nrs.common.wfone.rest.resource.BaseResource;
import ca.bc.gov.nrs.common.wfone.rest.resource.MessageRsrc;

@XmlRootElement(namespace = ResourceTypes.NAMESPACE, name = ResourceTypes.REPLACE_LEGAL_VALIDATION_NAME)
@XmlSeeAlso({ ReplaceLegalValidationRsrc.class })
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "@type")
public class ReplaceLegalValidationRsrc extends BaseResource {

	private static final long serialVersionUID = 1L;

	// Warnings.
	public static final String FIELD_ON_OTHER_POLICY_MSG = "The field [fieldLocationOrfieldLabel] (ID: [fieldId]) is also on another policy [policyNumber] in this crop year.";
	public static final String FIELD_HAS_OTHER_LEGAL_MSG = "There are other [legalLocationOrPid]s associated with the field [fieldLocationOrfieldLabel] (ID: [fieldId]).";
	public static final String OTHER_FIELD_ON_LEGAL_MSG = "The [legalLocationOrPid] [otherDescriptionOrPid] is associated with other field(s).";

	private Boolean isWarningFieldOnOtherPolicy;
	private MessageRsrc fieldOnOtherPolicyMsg;

	private Boolean isWarningFieldHasOtherLegalLand;
	private MessageRsrc fieldHasOtherLegalLandMsg;
	private List<LegalLandRsrc> otherLegalLandOfFieldList = new ArrayList<LegalLandRsrc>();

	private Boolean isWarningOtherFieldsOnLegal;
	private MessageRsrc otherFieldsOnLegalMsg;
	private List<AnnualFieldRsrc> otherFieldsOnLegalLandList = new ArrayList<AnnualFieldRsrc>();


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

	
	public Boolean getIsWarningFieldHasOtherLegalLand() {
		return isWarningFieldHasOtherLegalLand;
	}

	
	public void setIsWarningFieldHasOtherLegalLand(Boolean isWarningFieldHasOtherLegalLand) {
		this.isWarningFieldHasOtherLegalLand = isWarningFieldHasOtherLegalLand;
	}
	
	
	public MessageRsrc getFieldHasOtherLegalLandMsg() {
		return fieldHasOtherLegalLandMsg;
	}

	
	public void setFieldHasOtherLegalLandMsg(MessageRsrc fieldHasOtherLegalLandMsg) {
		this.fieldHasOtherLegalLandMsg = fieldHasOtherLegalLandMsg;
	}
	
	
	public List<LegalLandRsrc> getOtherLegalLandOfFieldList() {
		return otherLegalLandOfFieldList;
	}
	
	
	public void setOtherLegalLandOfFieldList(List<LegalLandRsrc> otherLegalLandOfFieldList) {
		this.otherLegalLandOfFieldList = otherLegalLandOfFieldList;
	}
	
	
	public Boolean getIsWarningOtherFieldsOnLegal() {
		return isWarningOtherFieldsOnLegal;
	}
	
	
	public void setIsWarningOtherFieldsOnLegal(Boolean isWarningOtherFieldsOnLegal) {
		this.isWarningOtherFieldsOnLegal = isWarningOtherFieldsOnLegal;
	}
	
	
	public MessageRsrc getOtherFieldsOnLegalMsg() {
		return otherFieldsOnLegalMsg;
	}
	
	
	public void setOtherFieldsOnLegalMsg(MessageRsrc otherFieldsOnLegalMsg) {
		this.otherFieldsOnLegalMsg = otherFieldsOnLegalMsg;
	}
	
	
	public List<AnnualFieldRsrc> getOtherFieldsOnLegalLandList() {
		return otherFieldsOnLegalLandList;
	}
	
	
	public void setOtherFieldsOnLegalLandList(List<AnnualFieldRsrc> otherFieldsOnLegalLandList) {
		this.otherFieldsOnLegalLandList = otherFieldsOnLegalLandList;
	}
		
}
