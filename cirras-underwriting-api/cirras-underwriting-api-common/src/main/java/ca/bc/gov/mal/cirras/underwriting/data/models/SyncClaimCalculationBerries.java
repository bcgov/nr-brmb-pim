package ca.bc.gov.mal.cirras.underwriting.data.models;

import java.io.Serializable;

public class SyncClaimCalculationBerries implements Serializable {
	private static final long serialVersionUID = 5726905902897809547L;

	private String claimCalculationBerriesGuid;
	private Double totalYieldForCalculation;

	public String getClaimCalculationBerriesGuid() {
		return claimCalculationBerriesGuid;
	}

	public void setClaimCalculationBerriesGuid(String claimCalculationBerriesGuid) {
		this.claimCalculationBerriesGuid = claimCalculationBerriesGuid;
	}
	
	public Double getTotalYieldForCalculation() {
		return totalYieldForCalculation;
	}
		
	public void setTotalYieldForCalculation(Double totalYieldForCalculation) {
		this.totalYieldForCalculation = totalYieldForCalculation;
	}
}