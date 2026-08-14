package ca.bc.gov.mal.cirras.underwriting.data.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


//
// This is not going to be a resource.
//
public class DopYieldFieldCommodityBerries implements Serializable {
	private static final long serialVersionUID = 1L;

	
	private String declaredYieldFieldCommodityBerriesGuid;
	private Integer fieldId;
	private Integer cropCommodityId;
	private String cropCommodityName;
	private Integer cropYear;
	private Double totalProduction;
	private Double totalProductionOverride;
	private Double totalPlantedAcres;
	private Double totalMatureEquivalentAcres;

	private List<DopYieldFieldVarietyBerries> dopYieldFieldVarietyBerriesList = new ArrayList<DopYieldFieldVarietyBerries>();
	
	public String getDeclaredYieldFieldCommodityBerriesGuid() {
		return declaredYieldFieldCommodityBerriesGuid;
	}
	public void setDeclaredYieldFieldCommodityBerriesGuid(String declaredYieldFieldCommodityBerriesGuid) {
		this.declaredYieldFieldCommodityBerriesGuid = declaredYieldFieldCommodityBerriesGuid;
	}

	public Integer getFieldId() {
		return fieldId;
	}
	public void setFieldId(Integer fieldId) {
		this.fieldId = fieldId;
	}

	public Integer getCropCommodityId() {
		return cropCommodityId;
	}
	public void setCropCommodityId(Integer cropCommodityId) {
		this.cropCommodityId = cropCommodityId;
	}

	public String getCropCommodityName() {
		return cropCommodityName;
	}
	public void setCropCommodityName(String cropCommodityName) {
		this.cropCommodityName = cropCommodityName;
	}

	public Integer getCropYear() {
		return cropYear;
	}
	public void setCropYear(Integer cropYear) {
		this.cropYear = cropYear;
	}

	public Double getTotalProduction() {
		return totalProduction;
	}
	public void setTotalProduction(Double totalProduction) {
		this.totalProduction = totalProduction;
	}

	public Double getTotalProductionOverride() {
		return totalProductionOverride;
	}
	public void setTotalProductionOverride(Double totalProductionOverride) {
		this.totalProductionOverride = totalProductionOverride;
	}

	public Double getTotalPlantedAcres() {
		return totalPlantedAcres;
	}

	public void setTotalPlantedAcres(Double totalPlantedAcres) {
		this.totalPlantedAcres = totalPlantedAcres;
	}

	public Double getTotalMatureEquivalentAcres() {
		return totalMatureEquivalentAcres;
	}

	public void setTotalMatureEquivalentAcres(Double totalMatureEquivalentAcres) {
		this.totalMatureEquivalentAcres = totalMatureEquivalentAcres;
	}
	
	public List<DopYieldFieldVarietyBerries> getDopYieldFieldVarietyBerriesList() {
		return dopYieldFieldVarietyBerriesList;
	}
	public void setDopYieldFieldVarietyBerriesList(List<DopYieldFieldVarietyBerries> dopYieldFieldVarietyBerriesList) {
		this.dopYieldFieldVarietyBerriesList = dopYieldFieldVarietyBerriesList;
	}	
}
