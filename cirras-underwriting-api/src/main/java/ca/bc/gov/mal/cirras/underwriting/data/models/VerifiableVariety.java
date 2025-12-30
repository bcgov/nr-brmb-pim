package ca.bc.gov.mal.cirras.underwriting.data.models;

import java.io.Serializable;


//
// This is not going to be a resource.
//
public class VerifiableVariety implements Serializable {
	private static final long serialVersionUID = 1L;

	private Integer cropVarietyId;
	private String cropVarietyName;
	
	public Integer getCropVarietyId() {
		return cropVarietyId;
	}
	public void setCropVarietyId(Integer cropVarietyId) {
		this.cropVarietyId = cropVarietyId;
	}

	public String getCropVarietyName() {
		return cropVarietyName;
	}
	public void setCropVarietyName(String cropVarietyName) {
		this.cropVarietyName = cropVarietyName;
	}
}
