package ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.types.ResourceTypes;
import ca.bc.gov.mal.cirras.underwriting.model.v1.ReplaceLegalValidation;
import ca.bc.gov.nrs.common.wfone.rest.resource.BaseResource;
import ca.bc.gov.nrs.common.wfone.rest.resource.MessageRsrc;

@XmlRootElement(namespace = ResourceTypes.NAMESPACE, name = ResourceTypes.REPLACE_LEGAL_VALIDATION_NAME)
@XmlSeeAlso({ ReplaceLegalValidationRsrc.class })
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "@type")
public class ReplaceLegalValidationRsrc extends BaseResource implements ReplaceLegalValidation<MessageRsrc, LegalLandRsrc, AnnualFieldRsrc> {

	private static final long serialVersionUID = 1L;
	
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

	@Override
	public Boolean getIsWarningFieldHasOtherLegalLand() {
		return isWarningFieldHasOtherLegalLand;
	}

	@Override
	public void setIsWarningFieldHasOtherLegalLand(Boolean isWarningFieldHasOtherLegalLand) {
		this.isWarningFieldHasOtherLegalLand = isWarningFieldHasOtherLegalLand;
	}
	
	@Override
	public MessageRsrc getFieldHasOtherLegalLandMsg() {
		return fieldHasOtherLegalLandMsg;
	}

	@Override
	public void setFieldHasOtherLegalLandMsg(MessageRsrc fieldHasOtherLegalLandMsg) {
		this.fieldHasOtherLegalLandMsg = fieldHasOtherLegalLandMsg;
	}
	
	@Override
	public List<LegalLandRsrc> getOtherLegalLandOfFieldList() {
		return otherLegalLandOfFieldList;
	}
	
	@Override
	public void setOtherLegalLandOfFieldList(List<LegalLandRsrc> otherLegalLandOfFieldList) {
		this.otherLegalLandOfFieldList = otherLegalLandOfFieldList;
	}
	
	@Override
	public Boolean getIsWarningOtherFieldsOnLegal() {
		return isWarningOtherFieldsOnLegal;
	}
	
	@Override
	public void setIsWarningOtherFieldsOnLegal(Boolean isWarningOtherFieldsOnLegal) {
		this.isWarningOtherFieldsOnLegal = isWarningOtherFieldsOnLegal;
	}
	
	@Override
	public MessageRsrc getOtherFieldsOnLegalMsg() {
		return otherFieldsOnLegalMsg;
	}
	
	@Override
	public void setOtherFieldsOnLegalMsg(MessageRsrc otherFieldsOnLegalMsg) {
		this.otherFieldsOnLegalMsg = otherFieldsOnLegalMsg;
	}
	
	@Override
	public List<AnnualFieldRsrc> getOtherFieldsOnLegalLandList() {
		return otherFieldsOnLegalLandList;
	}
	
	@Override
	public void setOtherFieldsOnLegalLandList(List<AnnualFieldRsrc> otherFieldsOnLegalLandList) {
		this.otherFieldsOnLegalLandList = otherFieldsOnLegalLandList;
	}
		
}
