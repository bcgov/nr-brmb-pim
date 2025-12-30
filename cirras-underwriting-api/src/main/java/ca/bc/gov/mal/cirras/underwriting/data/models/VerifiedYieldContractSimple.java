package ca.bc.gov.mal.cirras.underwriting.data.models;

import java.io.Serializable;
import java.util.List;

public interface VerifiedYieldContractSimple extends Serializable {

 	public String getVerifiedYieldContractGuid();
	public void setVerifiedYieldContractGuid(String verifiedYieldContractGuid);

	public Integer getContractId();
	public void setContractId(Integer contractId);
 
 	public Integer getCropYear();
	public void setCropYear(Integer cropYear);
	
	public List<VerifiedYieldContractCommodity> getVerifiedYieldContractCommodities();
	public void setVerifiedYieldContractCommodities(List<VerifiedYieldContractCommodity> verifiedYieldContractCommodities);

	public List<VerifiedYieldAmendment> getVerifiedYieldAmendments();
	public void setVerifiedYieldAmendments(List<VerifiedYieldAmendment> verifiedYieldAmendments);
	
	public List<VerifiedYieldSummary> getVerifiedYieldSummaries();
	public void setVerifiedYieldSummaries(List<VerifiedYieldSummary> verifiedYieldSummaries);
	
	public VerifiedYieldGrainBasket getVerifiedYieldGrainBasket();
	public void setVerifiedYieldGrainBasket(VerifiedYieldGrainBasket verifiedYieldGrainBasket);

}
