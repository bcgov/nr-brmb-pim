package ca.bc.gov.mal.cirras.underwriting.data.resources;

import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlSeeAlso;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

import ca.bc.gov.mal.cirras.underwriting.data.resources.types.ResourceTypes;
import ca.bc.gov.mal.cirras.underwriting.data.models.SyncClaimCalculationBerries;
import ca.bc.gov.nrs.common.wfone.rest.resource.BaseResource;

@XmlRootElement(namespace = ResourceTypes.NAMESPACE, name = ResourceTypes.SYNC_CLAIM_CALCULATION_SIMPLE_NAME)
@XmlSeeAlso({ SyncClaimCalculationSimpleRsrc.class })
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "@type")
public class SyncClaimCalculationSimpleRsrc extends BaseResource {

	private static final long serialVersionUID = 1L;

	// calculation
	private String claimCalculationGuid;
	private Integer cropYear;
	private Integer contractId;
	private Integer cropCommodityId;
	private Integer calculationVersion;
	private String calculationStatusCode;

	private Date dataSyncTransDate;
	private String transactionType;

	//Sub table models specific values
	private SyncClaimCalculationBerries syncClaimCalculationBerries;

	
	public String getClaimCalculationGuid() {
		return claimCalculationGuid;
	}

	public void setClaimCalculationGuid(String claimCalculationGuid) {
		this.claimCalculationGuid = claimCalculationGuid;
	}

	public Integer getContractId() {
		return contractId;
	}

	public void setContractId(Integer contractId) {
		this.contractId = contractId;
	}

	public Integer getCropYear() {
		return cropYear;
	}

	public void setCropYear(Integer cropYear) {
		this.cropYear = cropYear;
	}

	public Integer getCropCommodityId() {
		return cropCommodityId;
	}

	public void setCropCommodityId(Integer cropCommodityId) {
		this.cropCommodityId = cropCommodityId;
	}

	public Integer getCalculationVersion() {
		return calculationVersion;
	}

	public void setCalculationVersion(Integer calculationVersion) {
		this.calculationVersion = calculationVersion;
	}
	
	public String getCalculationStatusCode() {
		return calculationStatusCode;
	}

	public void setCalculationStatusCode(String calculationStatusCode) {
		this.calculationStatusCode = calculationStatusCode;
	}

	public Date getDataSyncTransDate() {
		return dataSyncTransDate;
	}

	public void setDataSyncTransDate(Date dataSyncTransDate) {
		this.dataSyncTransDate = dataSyncTransDate;
	}

	public String getTransactionType() {
		return transactionType;
	}
	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}

	public SyncClaimCalculationBerries getSyncClaimCalculationBerries() {
		return syncClaimCalculationBerries;
	}

	public void setSyncClaimCalculationBerries(SyncClaimCalculationBerries syncClaimCalculationBerries) {
		this.syncClaimCalculationBerries = syncClaimCalculationBerries;
	}

}
