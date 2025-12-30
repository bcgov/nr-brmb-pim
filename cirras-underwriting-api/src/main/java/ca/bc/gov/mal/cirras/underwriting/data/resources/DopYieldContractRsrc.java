package ca.bc.gov.mal.cirras.underwriting.data.resources;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlSeeAlso;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

import ca.bc.gov.mal.cirras.underwriting.data.resources.types.ResourceTypes;
import ca.bc.gov.mal.cirras.underwriting.data.models.DopYieldContractCommodity;
import ca.bc.gov.mal.cirras.underwriting.data.models.DopYieldContractCommodityForage;
import ca.bc.gov.mal.cirras.underwriting.data.models.DopYieldFieldRollup;
import ca.bc.gov.mal.cirras.underwriting.data.models.DopYieldFieldRollupForage;
import ca.bc.gov.mal.cirras.underwriting.data.models.UnderwritingComment;
import ca.bc.gov.mal.cirras.underwriting.data.models.DopYieldContract;
import ca.bc.gov.nrs.common.wfone.rest.resource.BaseResource;

@XmlRootElement(namespace = ResourceTypes.NAMESPACE, name = ResourceTypes.DOP_YIELD_CONTRACT_NAME)
@XmlSeeAlso({ DopYieldContractRsrc.class })
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "@type")
public class DopYieldContractRsrc extends BaseResource implements DopYieldContract<AnnualFieldRsrc> {

	private static final long serialVersionUID = 1L;

	private String declaredYieldContractGuid;
	private Integer contractId;
	private Integer cropYear;
	private Date declarationOfProductionDate;
	private Date dopUpdateTimestamp;
	private String dopUpdateUser;
	private String enteredYieldMeasUnitTypeCode;
	private String defaultYieldMeasUnitTypeCode;
	private Boolean grainFromOtherSourceInd;
	private String balerWagonInfo;
	private Integer totalLivestock;
	private Integer insurancePlanId;
	private Integer growerContractYearId;
	
	private List<AnnualFieldRsrc> fields = new ArrayList<AnnualFieldRsrc>();
	private List<UnderwritingComment> uwComments = new ArrayList<UnderwritingComment>();
	private List<DopYieldFieldRollup> dopYieldFieldRollupList = new ArrayList<DopYieldFieldRollup>();
	private List<DopYieldContractCommodity> dopYieldContractCommodities = new ArrayList<DopYieldContractCommodity>();
	private List<DopYieldContractCommodityForage> dopYieldContractCommodityForageList = new ArrayList<DopYieldContractCommodityForage>();
	private List<DopYieldFieldRollupForage> dopYieldFieldRollupForageList = new ArrayList<DopYieldFieldRollupForage>();

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
 
 	public Date getDeclarationOfProductionDate() {
		return declarationOfProductionDate;
	}

	public void setDeclarationOfProductionDate(Date declarationOfProductionDate) {
		this.declarationOfProductionDate = declarationOfProductionDate;
	}
 
 	public Date getDopUpdateTimestamp() {
		return dopUpdateTimestamp;
	}

	public void setDopUpdateTimestamp(Date dopUpdateTimestamp) {
		this.dopUpdateTimestamp = dopUpdateTimestamp;
	}
 
 	public String getDopUpdateUser() {
		return dopUpdateUser;
	}

	public void setDopUpdateUser(String dopUpdateUser) {
		this.dopUpdateUser = dopUpdateUser;
	}
 
 	public String getEnteredYieldMeasUnitTypeCode() {
		return enteredYieldMeasUnitTypeCode;
	}

	public void setEnteredYieldMeasUnitTypeCode(String enteredYieldMeasUnitTypeCode) {
		this.enteredYieldMeasUnitTypeCode = enteredYieldMeasUnitTypeCode;
	}
 
 	public String getDefaultYieldMeasUnitTypeCode() {
		return defaultYieldMeasUnitTypeCode;
	}

	public void setDefaultYieldMeasUnitTypeCode(String defaultYieldMeasUnitTypeCode) {
		this.defaultYieldMeasUnitTypeCode = defaultYieldMeasUnitTypeCode;
	}
 
 	public Boolean getGrainFromOtherSourceInd() {
		return grainFromOtherSourceInd;
	}

	public void setGrainFromOtherSourceInd(Boolean grainFromOtherSourceInd) {
		this.grainFromOtherSourceInd = grainFromOtherSourceInd;
	}

	public String getBalerWagonInfo() {
		return balerWagonInfo;
	}

	public void setBalerWagonInfo(String balerWagonInfo) {
		this.balerWagonInfo = balerWagonInfo;
	}

	public Integer getTotalLivestock() {
		return totalLivestock;
	}

	public void setTotalLivestock(Integer totalLivestock) {
		this.totalLivestock = totalLivestock;
	}

	public Integer getInsurancePlanId() {
		return insurancePlanId;
	}

	public void setInsurancePlanId(Integer insurancePlanId) {
		this.insurancePlanId = insurancePlanId;
	}	
	
	public List<AnnualFieldRsrc> getFields() {
		return fields;
	}
	public void setFields(List<AnnualFieldRsrc> fields) {
		this.fields = fields;
	}
	
	public List<UnderwritingComment> getUwComments() {
		return uwComments;
	}

	public void setUwComments(List<UnderwritingComment> uwComments) {
		this.uwComments = uwComments;
	}
	
	public List<DopYieldFieldRollup> getDopYieldFieldRollupList() {
		return dopYieldFieldRollupList;
	}
	public void setDopYieldFieldRollupList(List<DopYieldFieldRollup> dopYieldFieldRollupList) {
		this.dopYieldFieldRollupList = dopYieldFieldRollupList;
	}
	
	public List<DopYieldContractCommodity> getDopYieldContractCommodities() {
		return dopYieldContractCommodities;
	}
	public void setDopYieldContractCommodities(List<DopYieldContractCommodity> dopYieldContractCommodities) {
		this.dopYieldContractCommodities = dopYieldContractCommodities;
	}
	
	public Integer getGrowerContractYearId() {
		return growerContractYearId;
	}

	public void setGrowerContractYearId(Integer growerContractYearId) {
		this.growerContractYearId = growerContractYearId;
	}
	
	public List<DopYieldContractCommodityForage> getDopYieldContractCommodityForageList() {
		return dopYieldContractCommodityForageList;
	}
	
	public void setDopYieldContractCommodityForageList(List<DopYieldContractCommodityForage> dopYieldContractCommodityForageList) {
		this.dopYieldContractCommodityForageList = dopYieldContractCommodityForageList;
	}	
	
	public List<DopYieldFieldRollupForage> getDopYieldFieldRollupForageList() {
		return dopYieldFieldRollupForageList;
	}
	
	public void setDopYieldFieldRollupForageList(List<DopYieldFieldRollupForage> dopYieldFieldRollupForageList) {
		this.dopYieldFieldRollupForageList = dopYieldFieldRollupForageList;
	}
	
}
