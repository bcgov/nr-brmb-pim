package ca.bc.gov.mal.cirras.underwriting.model.v1;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public interface DopYieldContract<A extends AnnualField> extends Serializable {

 	public String getDeclaredYieldContractGuid();
	public void setDeclaredYieldContractGuid(String declaredYieldContractGuid);
 
 	public Integer getContractId();
	public void setContractId(Integer contractId);
 
 	public Integer getCropYear();
	public void setCropYear(Integer cropYear);
 
 	public Date getDeclarationOfProductionDate();
	public void setDeclarationOfProductionDate(Date declarationOfProductionDate);
 
 	public Date getDopUpdateTimestamp();
	public void setDopUpdateTimestamp(Date dopUpdateTimestamp);
 
 	public String getDopUpdateUser();
	public void setDopUpdateUser(String dopUpdateUser);
 
 	public String getEnteredYieldMeasUnitTypeCode();
	public void setEnteredYieldMeasUnitTypeCode(String enteredYieldMeasUnitTypeCode);
 
 	public String getDefaultYieldMeasUnitTypeCode();
	public void setDefaultYieldMeasUnitTypeCode(String defaultYieldMeasUnitTypeCode);
 
 	public Boolean getGrainFromOtherSourceInd();
	public void setGrainFromOtherSourceInd(Boolean grainFromOtherSourceInd);

	public String getBalerWagonInfo();
	public void setBalerWagonInfo(String balerWagonInfo);

	public Integer getTotalLivestock();
	public void setTotalLivestock(Integer totalLivestock);

	public Integer getInsurancePlanId();
	public void setInsurancePlanId(Integer insurancePlanId);
	
	public List<A> getFields();
	public void setFields(List<A> fields);
	
	public List<UnderwritingComment> getUwComments();
	public void setUwComments(List<UnderwritingComment> uwcomments);

	public List<DopYieldFieldRollup> getDopYieldFieldRollupList();
	public void setDopYieldFieldRollupList(List<DopYieldFieldRollup> dopYieldFieldRollupList);
	
	public List<DopYieldContractCommodity> getDopYieldContractCommodities();
	public void setDopYieldContractCommodities(List<DopYieldContractCommodity> dopYieldContractCommodities);
	
	public Integer getGrowerContractYearId();
	public void setGrowerContractYearId(Integer contractId);
	
	public List<DopYieldContractCommodityForage> getDopYieldContractCommodityForageList();
	public void setDopYieldContractCommodityForageList(List<DopYieldContractCommodityForage> dopYieldContractCommodityForageList);
}
