package ca.bc.gov.mal.cirras.underwriting.model.v1;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public interface InventoryContract<A extends AnnualField> extends Serializable {

	public String getInventoryContractGuid();
	public void setInventoryContractGuid(String inventoryContractGuid);

	public Integer getContractId();
	public void setContractId(Integer contractId);

	public Integer getCropYear();
	public void setCropYear(Integer cropYear);

	public Integer getGrowerContractYearId();
	public void setGrowerContractYearId(Integer growerContractYearId);

	public Integer getInsurancePlanId();
	public void setInsurancePlanId(Integer insurancePlanId);

 	public String getInsurancePlanName();
	public void setInsurancePlanName(String insurancePlanName);
	
	public Boolean getUnseededIntentionsSubmittedInd();
	public void setUnseededIntentionsSubmittedInd(Boolean unseededIntentionsSubmittedInd);

	public Boolean getSeededCropReportSubmittedInd();
	public void setSeededCropReportSubmittedInd(Boolean seededCropReportSubmittedInd);

	public Boolean getFertilizerInd();
	public void setFertilizerInd(Boolean fertilizerInd);

	public Boolean getHerbicideInd();
	public void setHerbicideInd(Boolean herbicideInd);

	public Boolean getTilliageInd();
	public void setTilliageInd(Boolean tilliageInd);

	public Boolean getOtherChangesInd();
	public void setOtherChangesInd(Boolean otherChangesInd);

	public String getOtherChangesComment();
	public void setOtherChangesComment(String otherChangesComment);

	public Boolean getGrainFromPrevYearInd();
	public void setGrainFromPrevYearInd(Boolean grainFromPrevYearInd);
	
	public Date getInvUpdateTimestamp();
	public void setInvUpdateTimestamp(Date invUpdateTimestamp);

	public String getInvUpdateUser();
	public void setInvUpdateUser(String invUpdateUser);

	public List<InventoryContractCommodity> getCommodities();
	public void setCommodities(List<InventoryContractCommodity> commodities);

	public List<InventoryCoverageTotalForage> getInventoryCoverageTotalForages();
	public void setInventoryCoverageTotalForages(List<InventoryCoverageTotalForage> inventoryCoverageTotalForages);
	
	public List<InventoryContractCommodityBerries> getInventoryContractCommodityBerries();
	public void setInventoryContractCommodityBerries(List<InventoryContractCommodityBerries> inventoryContractCommodityBerries);

	public List<A> getFields();
	public void setFields(List<A> fields);
	
 	public String getPolicyNumber();
	public void setPolicyNumber(String policyNumber);

 	public Integer getGrowerNumber();
	public void setGrowerNumber(Integer growerNumber);
 
 	public String getGrowerName();
	public void setGrowerName(String growerName);
	
}
