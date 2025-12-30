package ca.bc.gov.mal.cirras.underwriting.data.resources;

import java.util.Date;


import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlSeeAlso;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

import ca.bc.gov.mal.cirras.underwriting.data.resources.types.ResourceTypes;
import ca.bc.gov.mal.cirras.underwriting.data.models.ContactPhone;
import ca.bc.gov.nrs.common.wfone.rest.resource.BaseResource;

@XmlRootElement(namespace = ResourceTypes.NAMESPACE, name = ResourceTypes.CONTACT_PHONE_NAME)
@XmlSeeAlso({ ContactPhoneRsrc.class })
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "@type")
public class ContactPhoneRsrc extends BaseResource implements ContactPhone {

	private static final long serialVersionUID = 1L;

	private Integer contactPhoneId;
	private Integer contactId;
	private String phoneNumber;
	private String extension;
	private Boolean isPrimaryPhoneInd;
	private String telecomTypeCode;
	private Date effectiveDate;
	private Date expiryDate;
	private Boolean isActive;
	private Date dataSyncTransDate;
	private String transactionType;

 	public Integer getContactPhoneId() {
		return contactPhoneId;
	}

	public void setContactPhoneId(Integer contactPhoneId) {
		this.contactPhoneId = contactPhoneId;
	}
 
 	public Integer getContactId() {
		return contactId;
	}

	public void setContactId(Integer contactId) {
		this.contactId = contactId;
	}
 
 	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
 
 	public String getExtension() {
		return extension;
	}

	public void setExtension(String extension) {
		this.extension = extension;
	}
 
 	public Boolean getIsPrimaryPhoneInd() {
		return isPrimaryPhoneInd;
	}

	public void setIsPrimaryPhoneInd(Boolean isPrimaryPhoneInd) {
		this.isPrimaryPhoneInd = isPrimaryPhoneInd;
	}
 

 	public String getTelecomTypeCode() {
		return telecomTypeCode;
	}

	public void setTelecomTypeCode(String telecomTypeCode) {
		this.telecomTypeCode = telecomTypeCode;
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

	@Override
	public String getTransactionType() {
		return transactionType;
	}

	@Override
	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}
}
