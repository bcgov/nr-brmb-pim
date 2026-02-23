package ca.bc.gov.mal.cirras.underwriting.data.resources;

import java.util.ArrayList;
import java.util.List;

import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlSeeAlso;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

import ca.bc.gov.mal.cirras.underwriting.data.resources.types.ResourceTypes;
import ca.bc.gov.mal.cirras.underwriting.data.models.LegalLandRiskArea;
import ca.bc.gov.nrs.common.wfone.rest.resource.BaseResource;

@XmlRootElement(namespace = ResourceTypes.NAMESPACE, name = ResourceTypes.LEGAL_LAND_NAME)
@XmlSeeAlso({ LegalLandRsrc.class })
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "@type")
public class LegalLandRsrc extends BaseResource {

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
	
	
	public String getTransactionType() {
		return transactionType;
	}

	
	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}

	
	public List<LegalLandRiskArea> getRiskAreas() {
		return riskAreas;
	}

	
	public void setRiskAreas(List<LegalLandRiskArea> riskAreas) {
		this.riskAreas = riskAreas;
	}
	
	
	public List<FieldRsrc> getFields() {
		return fields;
	}

	
	public void setFields(List<FieldRsrc> fields) {
		this.fields = fields;
	}

}
