package ca.bc.gov.mal.cirras.underwriting.data.resources;

import java.util.Date;


import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlSeeAlso;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

import ca.bc.gov.mal.cirras.underwriting.data.resources.types.ResourceTypes;
import ca.bc.gov.nrs.common.wfone.rest.resource.BaseResource;

@XmlRootElement(namespace = ResourceTypes.NAMESPACE, name = ResourceTypes.CONTACT_NAME)
@XmlSeeAlso({ ContactRsrc.class })
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "@type")
public class ContactRsrc extends BaseResource {

	private static final long serialVersionUID = 1L;

	private Integer contactId;
	private String firstName;
	private String lastName;
	private Date dataSyncTransDate;
	private String transactionType;

 	public Integer getContactId() {
		return contactId;
	}

	public void setContactId(Integer contactId) {
		this.contactId = contactId;
	}
 
 	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
 
 	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
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
