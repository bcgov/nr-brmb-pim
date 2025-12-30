package ca.bc.gov.mal.cirras.underwriting.data.resources;

import java.util.Date;


import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlSeeAlso;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

import ca.bc.gov.mal.cirras.underwriting.data.resources.types.ResourceTypes;
import ca.bc.gov.mal.cirras.underwriting.data.models.SyncCode;
import ca.bc.gov.nrs.common.wfone.rest.resource.BaseResource;

@XmlRootElement(namespace = ResourceTypes.NAMESPACE, name = ResourceTypes.SYNC_CODE_NAME)
@XmlSeeAlso({ SyncCodeRsrc.class })
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "@type")
public class SyncCodeRsrc extends BaseResource implements SyncCode {

	private static final long serialVersionUID = 1L;

	private String codeTableType;
	private String uniqueKeyString;
	private Integer uniqueKeyInteger;
	private String description;
	private Boolean isActive;
	private Date dataSyncTransDate;
	private String transactionType;

 	public String getCodeTableType() {
		return codeTableType;
	}

	public void setCodeTableType(String codeTableType) {
		this.codeTableType = codeTableType;
	}
 
 	public String getUniqueKeyString() {
		return uniqueKeyString;
	}

	public void setUniqueKeyString(String uniqueKeyString) {
		this.uniqueKeyString = uniqueKeyString;
	}
 
 	public Integer getUniqueKeyInteger() {
		return uniqueKeyInteger;
	}

	public void setUniqueKeyInteger(Integer uniqueKeyInteger) {
		this.uniqueKeyInteger = uniqueKeyInteger;
	}
 
 	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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

	@Override
	public String getTransactionType() {
		return transactionType;
	}

	@Override
	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}
}
