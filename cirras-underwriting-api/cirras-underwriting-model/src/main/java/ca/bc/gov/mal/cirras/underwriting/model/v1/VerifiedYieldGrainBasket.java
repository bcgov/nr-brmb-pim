package ca.bc.gov.mal.cirras.underwriting.model.v1;

import java.io.Serializable;

//
// This is not going to be a resource.
//
public class VerifiedYieldGrainBasket implements Serializable {
	private static final long serialVersionUID = 1L;

	private String verifiedYieldGrainBasketGuid;
	private String verifiedYieldContractGuid;
	private Double basketValue;
	private Double totalQuantityCoverageValue;
	private Double totalCoverageValue;
	private Double harvestedValue;
	private String comment;
	
	
	public String getVerifiedYieldGrainBasketGuid() {
		return verifiedYieldGrainBasketGuid;
	}

	public void setVerifiedYieldGrainBasketGuid(String verifiedYieldGrainBasketGuid) {
		this.verifiedYieldGrainBasketGuid = verifiedYieldGrainBasketGuid;
	}

	public String getVerifiedYieldContractGuid() {
		return verifiedYieldContractGuid;
	}

	public void setVerifiedYieldContractGuid(String verifiedYieldContractGuid) {
		this.verifiedYieldContractGuid = verifiedYieldContractGuid;
	}

	public Double getBasketValue() {
		return basketValue;
	}

	public void setBasketValue(Double basketValue) {
		this.basketValue = basketValue;
	}

	public Double getTotalQuantityCoverageValue() {
		return totalQuantityCoverageValue;
	}

	public void setTotalQuantityCoverageValue(Double totalQuantityCoverageValue) {
		this.totalQuantityCoverageValue = totalQuantityCoverageValue;
	}

	public Double getTotalCoverageValue() {
		return totalCoverageValue;
	}

	public void setTotalCoverageValue(Double totalCoverageValue) {
		this.totalCoverageValue = totalCoverageValue;
	}

	public Double getHarvestedValue() {
		return harvestedValue;
	}

	public void setHarvestedValue(Double harvestedValue) {
		this.harvestedValue = harvestedValue;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}
}
