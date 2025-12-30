package ca.bc.gov.mal.cirras.underwriting.data.resources;

import java.util.Date;


import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlSeeAlso;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

import ca.bc.gov.mal.cirras.underwriting.data.resources.types.ResourceTypes;
import ca.bc.gov.nrs.common.wfone.rest.resource.BaseResource;

@XmlRootElement(namespace = ResourceTypes.NAMESPACE, name = ResourceTypes.GROWER_NAME)
@XmlSeeAlso({ GrowerRsrc.class })
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "@type")
public class GrowerRsrc extends BaseResource {

	private static final long serialVersionUID = 1L;

	private Integer growerId;
	private Integer growerNumber;
	private String growerName;
	private String growerAddressLine1;
	private String growerAddressLine2;
	private String growerPostalCode;
	private String growerCity;
	private Integer cityId;
	private String growerProvince;
	private Date dataSyncTransDate;
	private String transactionType;

 	public Integer getGrowerId() {
		return growerId;
	}

	public void setGrowerId(Integer growerId) {
		this.growerId = growerId;
	}
 
 	public Integer getGrowerNumber() {
		return growerNumber;
	}

	public void setGrowerNumber(Integer growerNumber) {
		this.growerNumber = growerNumber;
	}
 
 	public String getGrowerName() {
		return growerName;
	}

	public void setGrowerName(String growerName) {
		this.growerName = growerName;
	}
 
 	public String getGrowerAddressLine1() {
		return growerAddressLine1;
	}

	public void setGrowerAddressLine1(String growerAddressLine1) {
		this.growerAddressLine1 = growerAddressLine1;
	}
 
 	public String getGrowerAddressLine2() {
		return growerAddressLine2;
	}

	public void setGrowerAddressLine2(String growerAddressLine2) {
		this.growerAddressLine2 = growerAddressLine2;
	}
 
 	public String getGrowerPostalCode() {
		return growerPostalCode;
	}

	public void setGrowerPostalCode(String growerPostalCode) {
		this.growerPostalCode = growerPostalCode;
	}
 
 	public String getGrowerCity() {
		return growerCity;
	}

	public void setGrowerCity(String growerCity) {
		this.growerCity = growerCity;
	}
 
 	public Integer getCityId() {
		return cityId;
	}

	public void setCityId(Integer cityId) {
		this.cityId = cityId;
	}
 
 	public String getGrowerProvince() {
		return growerProvince;
	}

	public void setGrowerProvince(String growerProvince) {
		this.growerProvince = growerProvince;
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
