package ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource;

import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.types.ResourceTypes;
import ca.bc.gov.mal.cirras.underwriting.model.v1.RiskArea;
import ca.bc.gov.nrs.common.wfone.rest.resource.BaseResource;

@XmlRootElement(namespace = ResourceTypes.NAMESPACE, name = ResourceTypes.RISK_AREA_NAME)
@XmlSeeAlso({ RiskAreaRsrc.class })
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "@type")
public class RiskAreaRsrc extends BaseResource implements RiskArea {

	private static final long serialVersionUID = 1L;
	
	private Integer riskAreaId;
	private Integer insurancePlanId;
	private String riskAreaName;
	private String description;
	private Date effectiveDate;
	private Date expiryDate;

	private Integer legalLandId;
	private String insurancePlanName;

	public Integer getRiskAreaId() {
		return riskAreaId;
	}

	public void setRiskAreaId(Integer riskAreaId) {
		this.riskAreaId = riskAreaId;
	}

	public Integer getInsurancePlanId() {
		return insurancePlanId;
	}

	public void setInsurancePlanId(Integer insurancePlanId) {
		this.insurancePlanId = insurancePlanId;
	}

	public String getRiskAreaName() {
		return riskAreaName;
	}

	public void setRiskAreaName(String riskAreaName) {
		this.riskAreaName = riskAreaName;
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

	public Integer getLegalLandId() {
		return legalLandId;
	}

	public void setLegalLandId(Integer legalLandId) {
		this.legalLandId = legalLandId;
	}

	public String getInsurancePlanName() {
		return insurancePlanName;
	}

	public void setInsurancePlanName(String insurancePlanName) {
		this.insurancePlanName = insurancePlanName;
	}

}
