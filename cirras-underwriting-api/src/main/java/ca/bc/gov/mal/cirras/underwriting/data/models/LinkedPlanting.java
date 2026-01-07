package ca.bc.gov.mal.cirras.underwriting.data.models;

import java.io.Serializable;

public class LinkedPlanting implements Serializable {
	private static final long serialVersionUID = 1L;

	private String underseededInventorySeededForageGuid;
	private String inventoryFieldGuid;
	private Integer cropVarietyId;
	private String varietyName;
	private Double acres;

	public String getUnderseededInventorySeededForageGuid() {
		return underseededInventorySeededForageGuid;
	}

	public void setUnderseededInventorySeededForageGuid(String underseededInventorySeededForageGuid) {
		this.underseededInventorySeededForageGuid = underseededInventorySeededForageGuid;
	}

	public String getInventoryFieldGuid() {
		return inventoryFieldGuid;
	}

	public void setInventoryFieldGuid(String inventoryFieldGuid) {
		this.inventoryFieldGuid = inventoryFieldGuid;
	}

	public Integer getCropVarietyId() {
		return cropVarietyId;
	}

	public void setCropVarietyId(Integer cropVarietyId) {
		this.cropVarietyId = cropVarietyId;
	}

	public String getVarietyName() {
		return varietyName;
	}

	public void setVarietyName(String varietyName) {
		this.varietyName = varietyName;
	}

	public Double getAcres() {
		return acres;
	}

	public void setAcres(Double acres) {
		this.acres = acres;
	}

}
