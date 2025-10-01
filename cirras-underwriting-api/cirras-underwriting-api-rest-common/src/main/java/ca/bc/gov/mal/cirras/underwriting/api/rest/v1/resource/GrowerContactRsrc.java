package ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource;

import java.util.Date;


import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlSeeAlso;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.types.ResourceTypes;
import ca.bc.gov.mal.cirras.underwriting.model.v1.GrowerContact;
import ca.bc.gov.nrs.common.wfone.rest.resource.BaseResource;

@XmlRootElement(namespace = ResourceTypes.NAMESPACE, name = ResourceTypes.GROWER_CONTACT_NAME)
@XmlSeeAlso({ GrowerContactRsrc.class })
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "@type")
public class GrowerContactRsrc extends BaseResource implements GrowerContact {

	private static final long serialVersionUID = 1L;

	private Integer growerContactId;
	private Integer growerId;
	private Integer contactId;
	private Boolean isPrimaryContactInd;
	private Boolean isActivelyInvolvedInd;
	private Date dataSyncTransDate;
	private String transactionType;

 	public Integer getGrowerContactId() {
		return growerContactId;
	}

	public void setGrowerContactId(Integer growerContactId) {
		this.growerContactId = growerContactId;
	}
 
 	public Integer getGrowerId() {
		return growerId;
	}

	public void setGrowerId(Integer growerId) {
		this.growerId = growerId;
	}
 
 	public Integer getContactId() {
		return contactId;
	}

	public void setContactId(Integer contactId) {
		this.contactId = contactId;
	}
 
 	public Boolean getIsPrimaryContactInd() {
		return isPrimaryContactInd;
	}

	public void setIsPrimaryContactInd(Boolean isPrimaryContactInd) {
		this.isPrimaryContactInd = isPrimaryContactInd;
	}
 
 	public Boolean getIsActivelyInvolvedInd() {
		return isActivelyInvolvedInd;
	}

	public void setIsActivelyInvolvedInd(Boolean isActivelyInvolvedInd) {
		this.isActivelyInvolvedInd = isActivelyInvolvedInd;
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
