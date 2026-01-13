package ca.bc.gov.mal.cirras.underwriting.data.resources;

import java.util.ArrayList;
import java.util.List;

import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlSeeAlso;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

import ca.bc.gov.mal.cirras.underwriting.data.resources.types.ResourceTypes;
import ca.bc.gov.mal.cirras.underwriting.data.models.DopYieldFieldForage;
import ca.bc.gov.mal.cirras.underwriting.data.models.DopYieldFieldGrain;
import ca.bc.gov.mal.cirras.underwriting.data.models.InventoryField;
import ca.bc.gov.mal.cirras.underwriting.data.models.PolicySimple;
import ca.bc.gov.mal.cirras.underwriting.data.models.UnderwritingComment;
import ca.bc.gov.mal.cirras.underwriting.data.models.VerifiableCommodity;
import ca.bc.gov.mal.cirras.underwriting.data.models.VerifiableVariety;
import ca.bc.gov.nrs.common.wfone.rest.resource.BaseResource;

@XmlRootElement(namespace = ResourceTypes.NAMESPACE, name = ResourceTypes.ANNUAL_FIELD_NAME)
@XmlSeeAlso({ AnnualFieldRsrc.class })
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "@type")
public class AnnualFieldRsrc extends BaseResource {

	private static final long serialVersionUID = 1L;

	private Integer contractedFieldDetailId;
	private Integer annualFieldDetailId;
	private Integer fieldId;
	private Integer legalLandId;
	private String fieldLabel;
	private String otherLegalDescription;
	private String primaryPropertyIdentifier;
	private String fieldLocation;
	private Integer displayOrder;
	private Integer cropYear;
	private Boolean isLeasedInd;
	private String landUpdateType;
	private Integer transferFromGrowerContractYearId;

	private List<InventoryField> plantings = new ArrayList<InventoryField>();

	private List<DopYieldFieldGrain> dopYieldFieldGrainList = new ArrayList<DopYieldFieldGrain>();

	private List<DopYieldFieldForage> dopYieldFieldForageList = new ArrayList<DopYieldFieldForage>();
	
	private List<UnderwritingComment> uwComments = new ArrayList<UnderwritingComment>();
	
	private List<PolicySimple> policies = new ArrayList<PolicySimple>();

	private List<VerifiableCommodity> verifiableCommodities = new ArrayList<VerifiableCommodity>();
	
	private List<VerifiableVariety> verifiableVarieties = new ArrayList<VerifiableVariety>();
	
	public Integer getContractedFieldDetailId() {
		return contractedFieldDetailId;
	}
	public void setContractedFieldDetailId(Integer contractedFieldDetailId) {
		this.contractedFieldDetailId = contractedFieldDetailId;
	}

	public Integer getAnnualFieldDetailId() {
		return annualFieldDetailId;
	}
	public void setAnnualFieldDetailId(Integer annualFieldDetailId) {
		this.annualFieldDetailId = annualFieldDetailId;
	}

	public Integer getFieldId() {
		return fieldId;
	}
	public void setFieldId(Integer fieldId) {
		this.fieldId = fieldId;
	}

	public Integer getLegalLandId() {
		return legalLandId;
	}
	public void setLegalLandId(Integer legalLandId) {
		this.legalLandId = legalLandId;
	}

	public String getFieldLabel() {
		return fieldLabel;
	}
	public void setFieldLabel(String fieldLabel) {
		this.fieldLabel = fieldLabel;
	}

	public String getOtherLegalDescription() {
		return otherLegalDescription;
	}
	public void setOtherLegalDescription(String otherLegalDescription) {
		this.otherLegalDescription = otherLegalDescription;
	}
	
	public String getPrimaryPropertyIdentifier() {
		return primaryPropertyIdentifier;
	}

	public void setPrimaryPropertyIdentifier(String primaryPropertyIdentifier) {
		this.primaryPropertyIdentifier = primaryPropertyIdentifier;
	}
	
	public String getFieldLocation() {
		return fieldLocation;
	}

	public void setFieldLocation(String fieldLocation) {
		this.fieldLocation = fieldLocation;
	}

	public Integer getDisplayOrder() {
		return displayOrder;
	}
	public void setDisplayOrder(Integer displayOrder) {
		this.displayOrder = displayOrder;
	}

	public Integer getCropYear() {
		return cropYear;
	}
	public void setCropYear(Integer cropYear) {
		this.cropYear = cropYear;
	}

	public Boolean getIsLeasedInd() {
		return isLeasedInd;
	}

	public void setIsLeasedInd(Boolean isLeasedInd) {
		this.isLeasedInd = isLeasedInd;
	}
	
	
	public String getLandUpdateType() {
		return landUpdateType;
	}

	
	public void setLandUpdateType(String landUpdateType) {
		this.landUpdateType = landUpdateType;
	}
	
	
	public Integer getTransferFromGrowerContractYearId() {
		return transferFromGrowerContractYearId;
	}
	
	public void setTransferFromGrowerContractYearId(Integer transferFromGrowerContractYearId) {
		this.transferFromGrowerContractYearId = transferFromGrowerContractYearId;
	}
	
	public List<InventoryField> getPlantings() {
		return plantings;
	}
	public void setPlantings(List<InventoryField> plantings) {
		this.plantings = plantings;
	}	
	
	public List<DopYieldFieldGrain> getDopYieldFieldGrainList() {
		return dopYieldFieldGrainList;
	}

	public void setDopYieldFieldGrainList(List<DopYieldFieldGrain> dopYieldFieldGrainList) {
		this.dopYieldFieldGrainList = dopYieldFieldGrainList;
	}		
	
	public List<DopYieldFieldForage> getDopYieldFieldForageList() {
		return dopYieldFieldForageList;
	}

	public void setDopYieldFieldForageList(List<DopYieldFieldForage> dopYieldFieldForageList) {
		this.dopYieldFieldForageList = dopYieldFieldForageList;
	}		
	
	public List<UnderwritingComment> getUwComments() {
		return uwComments;
	}

	public void setUwComments(List<UnderwritingComment> uwComments) {
		this.uwComments = uwComments;
	}

	public List<PolicySimple> getPolicies() {
		return policies;
	}

	public void setPolicies(List<PolicySimple> policies) {
		this.policies = policies;
	}

	public List<VerifiableCommodity> getVerifiableCommodities() {
		return verifiableCommodities;
	}
	
	public void setVerifiableCommodities(List<VerifiableCommodity> verifiableCommodities) {
		this.verifiableCommodities = verifiableCommodities;
	}
	
	public List<VerifiableVariety> getVerifiableVarieties(){
		return verifiableVarieties;
	}
	
	public void setVerifiableVarieties(List<VerifiableVariety> verifiableVarieties) {
		this.verifiableVarieties = verifiableVarieties;
	}
	
}
