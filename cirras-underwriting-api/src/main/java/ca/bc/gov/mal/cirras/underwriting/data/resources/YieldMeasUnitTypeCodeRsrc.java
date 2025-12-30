package ca.bc.gov.mal.cirras.underwriting.data.resources;

import java.util.Date;

import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlSeeAlso;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

import ca.bc.gov.mal.cirras.underwriting.data.resources.types.ResourceTypes;
import ca.bc.gov.mal.cirras.underwriting.data.models.YieldMeasUnitTypeCode;
import ca.bc.gov.nrs.common.wfone.rest.resource.BaseResource;

@XmlRootElement(namespace = ResourceTypes.NAMESPACE, name = ResourceTypes.YIELD_MEAS_UNIT_TYPE_CODE_NAME)
@XmlSeeAlso({ YieldMeasUnitTypeCodeRsrc.class })
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "@type")
public class YieldMeasUnitTypeCodeRsrc extends BaseResource implements YieldMeasUnitTypeCode {

	private static final long serialVersionUID = 1L;

	private String yieldMeasUnitTypeCode;
	private String description;
	private Integer decimalPrecision;
	private Date effectiveDate;
	private Date expiryDate;
	private Boolean isDefaultYieldUnitInd;
	private Integer insurancePlanId;
	
 	public String getYieldMeasUnitTypeCode() {
		return yieldMeasUnitTypeCode;
	}

	public void setYieldMeasUnitTypeCode(String yieldMeasUnitTypeCode) {
		this.yieldMeasUnitTypeCode = yieldMeasUnitTypeCode;
	}
 
 	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
 
 	public Integer getDecimalPrecision() {
		return decimalPrecision;
	}

	public void setDecimalPrecision(Integer decimalPrecision) {
		this.decimalPrecision = decimalPrecision;
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
 
 	public Boolean getIsDefaultYieldUnitInd() {
		return isDefaultYieldUnitInd;
	}

	public void setIsDefaultYieldUnitInd(Boolean isDefaultYieldUnitInd) {
		this.isDefaultYieldUnitInd = isDefaultYieldUnitInd;
	}
 
 	public Integer getInsurancePlanId() {
		return insurancePlanId;
	}

	public void setInsurancePlanId(Integer insurancePlanId) {
		this.insurancePlanId = insurancePlanId;
	}
	
}
