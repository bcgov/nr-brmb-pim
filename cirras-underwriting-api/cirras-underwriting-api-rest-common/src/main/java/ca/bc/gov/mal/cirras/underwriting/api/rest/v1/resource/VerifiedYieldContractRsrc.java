package ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.types.ResourceTypes;
import ca.bc.gov.mal.cirras.underwriting.model.v1.VerifiedYieldContract;
import ca.bc.gov.mal.cirras.underwriting.model.v1.VerifiedYieldContractCommodity;
import ca.bc.gov.nrs.common.wfone.rest.resource.BaseResource;

@XmlRootElement(namespace = ResourceTypes.NAMESPACE, name = ResourceTypes.VERIFIED_YIELD_CONTRACT_NAME)
@XmlSeeAlso({ VerifiedYieldContractRsrc.class })
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "@type")
public class VerifiedYieldContractRsrc extends BaseResource implements VerifiedYieldContract<AnnualFieldRsrc> {

	private static final long serialVersionUID = 1L;

	private String verifiedYieldContractGuid;
	private String declaredYieldContractGuid;
	private Integer contractId;
	private Integer cropYear;
	private Date verifiedYieldUpdateTimestamp;
	private String verifiedYieldUpdateUser;
	private String defaultYieldMeasUnitTypeCode;

	private Integer insurancePlanId;
	private Integer growerContractYearId;
	
	private List<VerifiedYieldContractCommodity> verifiedYieldContractCommodities = new ArrayList<VerifiedYieldContractCommodity>();

	public String getVerifiedYieldContractGuid() {
		return verifiedYieldContractGuid;
	}
	public void setVerifiedYieldContractGuid(String verifiedYieldContractGuid) {
		this.verifiedYieldContractGuid = verifiedYieldContractGuid;
	}

	public String getDeclaredYieldContractGuid() {
		return declaredYieldContractGuid;
	}
	public void setDeclaredYieldContractGuid(String declaredYieldContractGuid) {
		this.declaredYieldContractGuid = declaredYieldContractGuid;
	}

	public Integer getContractId() {
		return contractId;
	}
	public void setContractId(Integer contractId) {
		this.contractId = contractId;
	}

	public Integer getCropYear() {
		return cropYear;
	}
	public void setCropYear(Integer cropYear) {
		this.cropYear = cropYear;
	}
	
	public Date getVerifiedYieldUpdateTimestamp() {
		return verifiedYieldUpdateTimestamp;
	}
	public void setVerifiedYieldUpdateTimestamp(Date verifiedYieldUpdateTimestamp) {
		this.verifiedYieldUpdateTimestamp = verifiedYieldUpdateTimestamp;
	}

	public String getVerifiedYieldUpdateUser() {
		return verifiedYieldUpdateUser;
	}
	public void setVerifiedYieldUpdateUser(String verifiedYieldUpdateUser) {
		this.verifiedYieldUpdateUser = verifiedYieldUpdateUser;
	}

	public String getDefaultYieldMeasUnitTypeCode() {
		return defaultYieldMeasUnitTypeCode;
	}
	public void setDefaultYieldMeasUnitTypeCode(String defaultYieldMeasUnitTypeCode) {
		this.defaultYieldMeasUnitTypeCode = defaultYieldMeasUnitTypeCode;
	}

	public Integer getInsurancePlanId() {
		return insurancePlanId;
	}
	public void setInsurancePlanId(Integer insurancePlanId) {
		this.insurancePlanId = insurancePlanId;
	}

	public Integer getGrowerContractYearId() {
		return growerContractYearId;
	}
	public void setGrowerContractYearId(Integer growerContractYearId) {
		this.growerContractYearId = growerContractYearId;
	}

	public List<VerifiedYieldContractCommodity> getVerifiedYieldContractCommodities() {
		return verifiedYieldContractCommodities;
	}
	public void setVerifiedYieldContractCommodities(List<VerifiedYieldContractCommodity> verifiedYieldContractCommodities) {
		this.verifiedYieldContractCommodities = verifiedYieldContractCommodities;
	}
}
