package ca.bc.gov.mal.cirras.underwriting.data.resources;

import java.util.Date;


import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlSeeAlso;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

import ca.bc.gov.mal.cirras.underwriting.data.resources.types.ResourceTypes;
import ca.bc.gov.mal.cirras.underwriting.data.models.Product;
import ca.bc.gov.nrs.common.wfone.rest.resource.BaseResource;

@XmlRootElement(namespace = ResourceTypes.NAMESPACE, name = ResourceTypes.PRODUCT_NAME)
@XmlSeeAlso({ ProductRsrc.class })
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "@type")
public class ProductRsrc extends BaseResource implements Product {

	private static final long serialVersionUID = 1L;

	private Integer productId;
	private Integer policyId;
	private Integer cropCommodityId;
	private String commodityCoverageCode;
	private String productStatusCode;
	private Integer deductibleLevel;
	private Double productionGuarantee;
	private Double probableYield;
	private String insuredByMeasType;
	private Double insurableValueHundredPercent;
	private Double coverageDollars;

	private Date dataSyncTransDate;
	private String transactionType;

	
	public Integer getProductId() {
		return productId;
	}
	public void setProductId(Integer productId) {
		this.productId = productId;
	}

	public Integer getPolicyId() {
		return policyId;
	}
	public void setPolicyId(Integer policyId) {
		this.policyId = policyId;
	}

	public Integer getCropCommodityId() {
		return cropCommodityId;
	}
	public void setCropCommodityId(Integer cropCommodityId) {
		this.cropCommodityId = cropCommodityId;
	}

	public String getCommodityCoverageCode() {
		return commodityCoverageCode;
	}
	public void setCommodityCoverageCode(String commodityCoverageCode) {
		this.commodityCoverageCode = commodityCoverageCode;
	}

	public String getProductStatusCode() {
		return productStatusCode;
	}
	public void setProductStatusCode(String productStatusCode) {
		this.productStatusCode = productStatusCode;
	}

	public Integer getDeductibleLevel() {
		return deductibleLevel;
	}
	public void setDeductibleLevel(Integer deductibleLevel) {
		this.deductibleLevel = deductibleLevel;
	}

	public Double getProductionGuarantee() {
		return productionGuarantee;
	}
	public void setProductionGuarantee(Double productionGuarantee) {
		this.productionGuarantee = productionGuarantee;
	}

	public Double getProbableYield() {
		return probableYield;
	}
	public void setProbableYield(Double probableYield) {
		this.probableYield = probableYield;
	}

	public String getInsuredByMeasType() {
		return insuredByMeasType;
	}
	public void setInsuredByMeasType(String insuredByMeasType) {
		this.insuredByMeasType = insuredByMeasType;
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

	public Double getInsurableValueHundredPercent() {
		return insurableValueHundredPercent;
	}

	public void setInsurableValueHundredPercent(Double insurableValueHundredPercent) {
		this.insurableValueHundredPercent = insurableValueHundredPercent;
	}

	public Double getCoverageDollars() {
		return coverageDollars;
	}

	public void setCoverageDollars(Double coverageDollars) {
		this.coverageDollars = coverageDollars;
	}
}
