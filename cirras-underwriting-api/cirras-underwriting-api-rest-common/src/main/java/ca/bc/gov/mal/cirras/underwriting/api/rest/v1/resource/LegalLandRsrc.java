package ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource;

import java.util.ArrayList;
import java.util.List;

import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlSeeAlso;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.types.ResourceTypes;
import ca.bc.gov.mal.cirras.underwriting.model.v1.LegalLand;
import ca.bc.gov.mal.cirras.underwriting.model.v1.LegalLandRiskArea;
import ca.bc.gov.nrs.common.wfone.rest.resource.BaseResource;

@XmlRootElement(namespace = ResourceTypes.NAMESPACE, name = ResourceTypes.LEGAL_LAND_NAME)
@XmlSeeAlso({ LegalLandRsrc.class })
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "@type")
public class LegalLandRsrc extends BaseResource implements LegalLand<FieldRsrc> {

	private static final long serialVersionUID = 1L;

	private Integer legalLandId;
	private String primaryPropertyIdentifier;
	private String primaryLandIdentifierTypeCode;
	private String primaryReferenceTypeCode;
	private String legalDescription;
	private String legalShortDescription;
	private String otherDescription;
	private Integer activeFromCropYear;
	private Integer activeToCropYear;
	private Double totalAcres;
	private String transactionType;
	
	private List<LegalLandRiskArea> riskAreas = new ArrayList<LegalLandRiskArea>();
	private List<FieldRsrc> fields = new ArrayList<FieldRsrc>();

 	public Integer getLegalLandId() {
		return legalLandId;
	}

	public void setLegalLandId(Integer legalLandId) {
		this.legalLandId = legalLandId;
	}
 
	public String getPrimaryPropertyIdentifier() {
		return primaryPropertyIdentifier;
	}

	public void setPrimaryPropertyIdentifier(String primaryPropertyIdentifier) {
		this.primaryPropertyIdentifier = primaryPropertyIdentifier;
	}

	public String getPrimaryLandIdentifierTypeCode() {
		return primaryLandIdentifierTypeCode;
	}

	public void setPrimaryLandIdentifierTypeCode(String primaryLandIdentifierTypeCode) {
		this.primaryLandIdentifierTypeCode = primaryLandIdentifierTypeCode;
	}
	
	public String getPrimaryReferenceTypeCode() {
		return primaryReferenceTypeCode;
	}

	public void setPrimaryReferenceTypeCode(String primaryReferenceTypeCode) {
		this.primaryReferenceTypeCode = primaryReferenceTypeCode;
	}
 
 	public String getLegalDescription() {
		return legalDescription;
	}

	public void setLegalDescription(String legalDescription) {
		this.legalDescription = legalDescription;
	}
 
 	public String getLegalShortDescription() {
		return legalShortDescription;
	}

	public void setLegalShortDescription(String legalShortDescription) {
		this.legalShortDescription = legalShortDescription;
	}
 
 	public String getOtherDescription() {
		return otherDescription;
	}

	public void setOtherDescription(String otherDescription) {
		this.otherDescription = otherDescription;
	}
 
 	public Integer getActiveFromCropYear() {
		return activeFromCropYear;
	}

	public void setActiveFromCropYear(Integer activeFromCropYear) {
		this.activeFromCropYear = activeFromCropYear;
	}
 
 	public Integer getActiveToCropYear() {
		return activeToCropYear;
	}

	public void setActiveToCropYear(Integer activeToCropYear) {
		this.activeToCropYear = activeToCropYear;
	}

	public Double getTotalAcres() {
		return totalAcres;
	}

	public void setTotalAcres(Double totalAcres) {
		this.totalAcres = totalAcres;
	}
	
	@Override
	public String getTransactionType() {
		return transactionType;
	}

	@Override
	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}

	@Override
	public List<LegalLandRiskArea> getRiskAreas() {
		return riskAreas;
	}

	@Override
	public void setRiskAreas(List<LegalLandRiskArea> riskAreas) {
		this.riskAreas = riskAreas;
	}
	
	@Override
	public List<FieldRsrc> getFields() {
		return fields;
	}

	@Override
	public void setFields(List<FieldRsrc> fields) {
		this.fields = fields;
	}

}
