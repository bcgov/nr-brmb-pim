package ca.bc.gov.mal.cirras.underwriting.data.resources;

import java.util.Date;


import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlSeeAlso;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

import ca.bc.gov.mal.cirras.underwriting.data.resources.types.ResourceTypes;
import ca.bc.gov.nrs.common.wfone.rest.resource.BaseResource;

@XmlRootElement(namespace = ResourceTypes.NAMESPACE, name = ResourceTypes.COMMODITY_TYPE_VARIETY_XREF_NAME)
@XmlSeeAlso({ SyncCommodityTypeVarietyXrefRsrc.class })
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "@type")
public class SyncCommodityTypeVarietyXrefRsrc extends BaseResource {

	private static final long serialVersionUID = 1L;

	private String commodityTypeCode;
	private Integer cropVarietyId;
	private Date dataSyncTransDate;
	private String transactionType;

 	public String getCommodityTypeCode() {
		return commodityTypeCode;
	}

	public void setCommodityTypeCode(String commodityTypeCode) {
		this.commodityTypeCode = commodityTypeCode;
	}

	public Integer getCropVarietyId() {
		return cropVarietyId;
	}
	
	public void setCropVarietyId(Integer cropVarietyId) {
		this.cropVarietyId = cropVarietyId;
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
}
