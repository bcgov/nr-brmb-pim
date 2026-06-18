package ca.bc.gov.mal.cirras.underwriting.data.resources;

import java.util.Date;


import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlSeeAlso;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

import ca.bc.gov.mal.cirras.underwriting.data.resources.types.ResourceTypes;
import ca.bc.gov.nrs.common.wfone.rest.resource.BaseResource;

@XmlRootElement(namespace = ResourceTypes.NAMESPACE, name = ResourceTypes.CONTACT_EMAIL_NAME)
@XmlSeeAlso({ ContactEmailRsrc.class })
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "@type")
public class ContactEmailRsrc extends BaseResource {

	private static final long serialVersionUID = 1L;

	private Integer contactEmailId;
	private Integer contactId;
	private String emailAddress;
	private Boolean isPrimaryEmailInd;
	private Date effectiveDate;
	private Date expiryDate;
	private Boolean isActive;
	private Date dataSyncTransDate;
	private String transactionType;

 	public Integer getContactEmailId() {
		return contactEmailId;
	}

	public void setContactEmailId(Integer contactEmailId) {
		this.contactEmailId = contactEmailId;
	}
 
 	public Integer getContactId() {
		return contactId;
	}

	public void setContactId(Integer contactId) {
		this.contactId = contactId;
	}
 
 	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}
 
 	public Boolean getIsPrimaryEmailInd() {
		return isPrimaryEmailInd;
	}

	public void setIsPrimaryEmailInd(Boolean isPrimaryEmailInd) {
		this.isPrimaryEmailInd = isPrimaryEmailInd;
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
}
