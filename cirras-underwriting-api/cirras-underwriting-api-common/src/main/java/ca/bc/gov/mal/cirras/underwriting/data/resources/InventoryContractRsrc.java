package ca.bc.gov.mal.cirras.underwriting.data.resources;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlSeeAlso;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

import ca.bc.gov.mal.cirras.underwriting.data.resources.types.ResourceTypes;
import ca.bc.gov.mal.cirras.underwriting.data.models.InventoryContractCommodity;
import ca.bc.gov.mal.cirras.underwriting.data.models.InventoryContractCommodityBerries;
import ca.bc.gov.mal.cirras.underwriting.data.models.InventoryCoverageTotalForage;
import ca.bc.gov.nrs.common.wfone.rest.resource.BaseResource;

@XmlRootElement(namespace = ResourceTypes.NAMESPACE, name = ResourceTypes.INVENTORY_CONTRACT_NAME)
@XmlSeeAlso({ InventoryContractRsrc.class })
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "@type")
public class InventoryContractRsrc extends BaseResource {

	private static final long serialVersionUID = 1L;

	private String inventoryContractGuid;
	private Integer contractId;
	private Integer cropYear;
	private Integer growerContractYearId;
	private Integer insurancePlanId;
	private String insurancePlanName;
	private Boolean unseededIntentionsSubmittedInd;
	private Boolean seededCropReportSubmittedInd;
	private Boolean fertilizerInd;
	private Boolean herbicideInd;
	private Boolean tilliageInd;
	private Boolean otherChangesInd;
	private String otherChangesComment;
	private Boolean grainFromPrevYearInd;
	private Date invUpdateTimestamp;
	private String invUpdateUser;

	private String policyNumber;
	private Integer growerNumber;
	private String growerName;


	private List<InventoryContractCommodity> commodities = new ArrayList<InventoryContractCommodity>();
	private List<InventoryCoverageTotalForage> inventoryCoverageTotalForages = new ArrayList<InventoryCoverageTotalForage>();
	private List<InventoryContractCommodityBerries> inventoryContractCommodityBerries = new ArrayList<InventoryContractCommodityBerries>();
	private List<AnnualFieldRsrc> fields = new ArrayList<AnnualFieldRsrc>();

	public String getInventoryContractGuid() {
		return inventoryContractGuid;
	}
	public void setInventoryContractGuid(String inventoryContractGuid) {
		this.inventoryContractGuid = inventoryContractGuid;
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
	
	public Integer getGrowerContractYearId() {
		return growerContractYearId;
	}
	
	public void setGrowerContractYearId(Integer growerContractYearId) {
		this.growerContractYearId = growerContractYearId;
	}

	public Integer getInsurancePlanId() {
		return insurancePlanId;
	}

	public void setInsurancePlanId(Integer insurancePlanId) {
		this.insurancePlanId = insurancePlanId;
	}

 	public String getInsurancePlanName() {
		return insurancePlanName;
	}

	public void setInsurancePlanName(String insurancePlanName) {
		this.insurancePlanName = insurancePlanName;
	}
	
	public Boolean getUnseededIntentionsSubmittedInd() {
		return unseededIntentionsSubmittedInd;
	}
	public void setUnseededIntentionsSubmittedInd(Boolean unseededIntentionsSubmittedInd) {
		this.unseededIntentionsSubmittedInd = unseededIntentionsSubmittedInd;
	}

	public Boolean getSeededCropReportSubmittedInd() {
		return seededCropReportSubmittedInd;
	}
	public void setSeededCropReportSubmittedInd(Boolean seededCropReportSubmittedInd) {
		this.seededCropReportSubmittedInd = seededCropReportSubmittedInd;
	}

	public Boolean getFertilizerInd() {
		return fertilizerInd;
	}
	public void setFertilizerInd(Boolean fertilizerInd) {
		this.fertilizerInd = fertilizerInd;
	}

	public Boolean getHerbicideInd() {
		return herbicideInd;
	}
	public void setHerbicideInd(Boolean herbicideInd) {
		this.herbicideInd = herbicideInd;
	}

	public Boolean getTilliageInd() {
		return tilliageInd;
	}
	public void setTilliageInd(Boolean tilliageInd) {
		this.tilliageInd = tilliageInd;
	}

	public Boolean getOtherChangesInd() {
		return otherChangesInd;
	}
	public void setOtherChangesInd(Boolean otherChangesInd) {
		this.otherChangesInd = otherChangesInd;
	}

	public String getOtherChangesComment() {
		return otherChangesComment;
	}
	public void setOtherChangesComment(String otherChangesComment) {
		this.otherChangesComment = otherChangesComment;
	}

	public Boolean getGrainFromPrevYearInd() {
		return grainFromPrevYearInd;
	}
	public void setGrainFromPrevYearInd(Boolean grainFromPrevYearInd) {
		this.grainFromPrevYearInd = grainFromPrevYearInd;
	}

	public Date getInvUpdateTimestamp() {
		return invUpdateTimestamp;
	}

	public void setInvUpdateTimestamp(Date invUpdateTimestamp) {
		this.invUpdateTimestamp = invUpdateTimestamp;
	}

	public String getInvUpdateUser() {
		return invUpdateUser;
	}

	public void setInvUpdateUser(String invUpdateUser) {
		this.invUpdateUser = invUpdateUser;
	}

	public List<InventoryContractCommodity> getCommodities() {
		return commodities;
	}
	public void setCommodities(List<InventoryContractCommodity> commodities) {
		this.commodities = commodities;
	}

	public List<InventoryCoverageTotalForage> getInventoryCoverageTotalForages() {
		return inventoryCoverageTotalForages;
	}
	public void setInventoryCoverageTotalForages(List<InventoryCoverageTotalForage> inventoryCoverageTotalForages) {
		this.inventoryCoverageTotalForages = inventoryCoverageTotalForages;
	}

	public List<InventoryContractCommodityBerries> getInventoryContractCommodityBerries() {
		return inventoryContractCommodityBerries;
	}
	public void setInventoryContractCommodityBerries(List<InventoryContractCommodityBerries> inventoryContractCommodityBerries) {
		this.inventoryContractCommodityBerries = inventoryContractCommodityBerries;
	}	

	public List<AnnualFieldRsrc> getFields() {
		return fields;
	}
	public void setFields(List<AnnualFieldRsrc> fields) {
		this.fields = fields;
	}
	 
 	public String getPolicyNumber() {
		return policyNumber;
	}

	public void setPolicyNumber(String policyNumber) {
		this.policyNumber = policyNumber;
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
	
}
