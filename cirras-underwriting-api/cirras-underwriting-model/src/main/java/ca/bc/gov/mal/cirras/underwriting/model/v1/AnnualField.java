package ca.bc.gov.mal.cirras.underwriting.model.v1;

import java.io.Serializable;
import java.util.List;


public interface AnnualField extends Serializable {

	public Integer getContractedFieldDetailId();
	public void setContractedFieldDetailId(Integer contractedFieldDetailId);

	public Integer getAnnualFieldDetailId();
	public void setAnnualFieldDetailId(Integer annualFieldDetailId);

	public Integer getFieldId();
	public void setFieldId(Integer fieldId);

	public Integer getLegalLandId();
	public void setLegalLandId(Integer legalLandId);

	public String getFieldLabel();
	public void setFieldLabel(String fieldLabel);

	public String getOtherLegalDescription();
	public void setOtherLegalDescription(String otherLegalDescription);

	public Integer getDisplayOrder();
	public void setDisplayOrder(Integer displayOrder);

	public Integer getCropYear();
	public void setCropYear(Integer cropYear);
	
	public String getLandUpdateType();
	public void setLandUpdateType(String landUpdateType);
	
	public Integer getTransferFromGrowerContractYearId();
	public void setTransferFromGrowerContractYearId(Integer growerTransferFromContractYearId);

	public List<InventoryField> getPlantings();
	public void setPlantings(List<InventoryField> plantings);

	public List<DopYieldFieldGrain> getDopYieldFieldGrainList();
	public void setDopYieldFieldGrainList(List<DopYieldFieldGrain> dopYieldFieldGrainList);

	public List<DopYieldFieldForage> getDopYieldFieldForageList();
	public void setDopYieldFieldForageList(List<DopYieldFieldForage> dopYieldFieldForageList);
	
	public List<UnderwritingComment> getUwComments();
	public void setUwComments(List<UnderwritingComment> uwcomments);
	
	public List<PolicySimple> getPolicies();
	public void setPolicies(List<PolicySimple> policies);

	public List<VerifiableCommodity> getVerifiableCommodities();
	public void setVerifiableCommodities(List<VerifiableCommodity> verifiableCommodities);

	public List<VerifiableVariety> getVerifiableVarieties();
	public void setVerifiableVarieties(List<VerifiableVariety> verifiableVarieties);
	
}
