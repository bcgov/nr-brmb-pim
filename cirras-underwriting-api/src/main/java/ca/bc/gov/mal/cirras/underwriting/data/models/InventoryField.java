package ca.bc.gov.mal.cirras.underwriting.data.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


//
// This is not going to be a resource.
//
public class InventoryField implements Serializable {
	private static final long serialVersionUID = 1L;

	private String inventoryFieldGuid;
	private Integer insurancePlanId;
	private Integer fieldId;
	private Integer lastYearCropCommodityId;
	private String lastYearCropCommodityName;
	private Integer lastYearCropVarietyId;
	private String lastYearCropVarietyName;
	private Integer cropYear;
	private Integer plantingNumber;
	private Boolean isHiddenOnPrintoutInd;
	private Integer underseededCropVarietyId;
	private String underseededCropVarietyName;
	private Double underseededAcres;
	private String underseededInventorySeededForageGuid;


	private InventoryUnseeded inventoryUnseeded;
	private InventoryBerries inventoryBerries;

	private LinkedPlanting linkedPlanting;

	// All but one InventorySeededGrain should have isReplacedInd=true, so that only one is current.
	private List<InventorySeededGrain> inventorySeededGrains = new ArrayList<InventorySeededGrain>();

	private List<InventorySeededForage> inventorySeededForages = new ArrayList<InventorySeededForage>();

	public String getInventoryFieldGuid() {
		return inventoryFieldGuid;
	}
	public void setInventoryFieldGuid(String inventoryFieldGuid) {
		this.inventoryFieldGuid = inventoryFieldGuid;
	}

	public Integer getInsurancePlanId() {
		return insurancePlanId;
	}
	public void setInsurancePlanId(Integer insurancePlanId) {
		this.insurancePlanId = insurancePlanId;
	}

	public Integer getFieldId() {
		return fieldId;
	}
	public void setFieldId(Integer fieldId) {
		this.fieldId = fieldId;
	}

	public Integer getLastYearCropCommodityId() {
		return lastYearCropCommodityId;
	}
	public void setLastYearCropCommodityId(Integer lastYearCropCommodityId) {
		this.lastYearCropCommodityId = lastYearCropCommodityId;
	}
	
	public String getLastYearCropCommodityName() {
		return lastYearCropCommodityName;
	}
	public void setLastYearCropCommodityName(String lastYearCropCommodityName) {
		this.lastYearCropCommodityName = lastYearCropCommodityName;
	}

	public Integer getLastYearCropVarietyId() {
		return lastYearCropVarietyId;
	}
	public void setLastYearCropVarietyId(Integer lastYearCropVarietyId) {
		this.lastYearCropVarietyId = lastYearCropVarietyId;
	}

	public String getLastYearCropVarietyName() {
		return lastYearCropVarietyName;
	}
	public void setLastYearCropVarietyName(String lastYearCropVarietyName) {
		this.lastYearCropVarietyName = lastYearCropVarietyName;
	}
		
	public Integer getCropYear() {
		return cropYear;
	}
	public void setCropYear(Integer cropYear) {
		this.cropYear = cropYear;
	}
	
	public Integer getPlantingNumber() {
		return plantingNumber;
	}
	public void setPlantingNumber(Integer plantingNumber) {
		this.plantingNumber = plantingNumber;
	}
	
	public Boolean getIsHiddenOnPrintoutInd() {
		return isHiddenOnPrintoutInd;
	}

	public void setIsHiddenOnPrintoutInd(Boolean isHiddenOnPrintoutInd) {
		this.isHiddenOnPrintoutInd = isHiddenOnPrintoutInd;
	}

 	public Integer getUnderseededCropVarietyId() {
		return underseededCropVarietyId;
	}

	public void setUnderseededCropVarietyId(Integer underseededCropVarietyId) {
		this.underseededCropVarietyId = underseededCropVarietyId;
	}
	
	public String getUnderseededCropVarietyName() {
		return underseededCropVarietyName;
	}
	public void setUnderseededCropVarietyName(String underseededCropVarietyName) {
		this.underseededCropVarietyName = underseededCropVarietyName;
	}
 
 	public Double getUnderseededAcres() {
		return underseededAcres;
	}

	public void setUnderseededAcres(Double underseededAcres) {
		this.underseededAcres = underseededAcres;
	}
	
	public InventoryUnseeded getInventoryUnseeded() {
		return inventoryUnseeded;
	}
	public void setInventoryUnseeded(InventoryUnseeded inventoryUnseeded) {
		this.inventoryUnseeded = inventoryUnseeded;
	}

	public LinkedPlanting getLinkedPlanting() {
		return linkedPlanting;
	}

	public void setLinkedPlanting(LinkedPlanting linkedPlanting) {
		this.linkedPlanting = linkedPlanting;
	}

	public List<InventorySeededGrain> getInventorySeededGrains() {
		return inventorySeededGrains;
	}
	public void setInventorySeededGrains(List<InventorySeededGrain> inventorySeededGrains) {
		this.inventorySeededGrains = inventorySeededGrains;
	}

	public List<InventorySeededForage> getInventorySeededForages() {
		return inventorySeededForages;
	}
	public void setInventorySeededForages(List<InventorySeededForage> inventorySeededForages) {
		this.inventorySeededForages = inventorySeededForages;
	}

	public String getUnderseededInventorySeededForageGuid() {
		return underseededInventorySeededForageGuid;
	}
	public void setUnderseededInventorySeededForageGuid(String underseededInventorySeededForageGuid) {
		this.underseededInventorySeededForageGuid = underseededInventorySeededForageGuid;
	}
	
	public InventoryBerries getInventoryBerries() {
		return inventoryBerries;
	}
	public void setInventoryBerries(InventoryBerries inventoryBerries) {
		this.inventoryBerries = inventoryBerries;
	}

}
