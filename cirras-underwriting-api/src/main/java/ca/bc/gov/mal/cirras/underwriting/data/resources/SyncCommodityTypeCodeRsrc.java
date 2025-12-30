package ca.bc.gov.mal.cirras.underwriting.data.resources;

import java.util.Date;


import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlSeeAlso;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

import ca.bc.gov.mal.cirras.underwriting.data.resources.types.ResourceTypes;
import ca.bc.gov.nrs.common.wfone.rest.resource.BaseResource;

@XmlRootElement(namespace = ResourceTypes.NAMESPACE, name = ResourceTypes.COMMODITY_TYPE_CODE_NAME)
@XmlSeeAlso({ SyncCommodityTypeCodeRsrc.class })
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "@type")
public class SyncCommodityTypeCodeRsrc extends BaseResource {

	private static final long serialVersionUID = 1L;

	private String commodityTypeCode;
	private Integer cropCommodityId;
	private String description;
	private Date effectiveDate;
	private Date expiryDate;
	private Boolean isActive;
	private Date dataSyncTransDate;
	private String transactionType;

	private Integer insurancePlanId;

 	public String getCommodityTypeCode() {
		return commodityTypeCode;
	}

	public void setCommodityTypeCode(String commodityTypeCode) {
		this.commodityTypeCode = commodityTypeCode;
	}
 
 	public Integer getCropCommodityId() {
		return cropCommodityId;
	}

	public void setCropCommodityId(Integer cropCommodityId) {
		this.cropCommodityId = cropCommodityId;
	}
 
 	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
 
 	public Date getEffectiveDate() {
		return effectiveDate;
	}

	public void setEffectiveDate(Date effectiveDate) {
		this.effectiveDate = effectiveDate;
	}
 
 	public Date getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}
 
 	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
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

	public Integer getInsurancePlanId() {
		return insurancePlanId;
	}

	public void setInsurancePlanId(Integer insurancePlanId) {
		this.insurancePlanId = insurancePlanId;
	}
}
