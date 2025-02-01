package ca.bc.gov.mal.cirras.underwriting.model.v1;

import java.io.Serializable;


//
// This is not going to be a resource.
//
public class VerifiableCommodity implements Serializable {
	private static final long serialVersionUID = 1L;

	private Integer cropCommodityId;
	private String cropCommodityName;
	private Boolean isPedigreeInd;
	
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

	public Boolean getIsPedigreeInd() {
		return isPedigreeInd;
	}
	public void setIsPedigreeInd(Boolean isPedigreeInd) {
		this.isPedigreeInd = isPedigreeInd;
	}
}
