package ca.bc.gov.mal.cirras.underwriting.data.resources;

import java.util.ArrayList;
import java.util.List;

import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlSeeAlso;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

import ca.bc.gov.mal.cirras.underwriting.data.resources.types.ResourceTypes;
import ca.bc.gov.mal.cirras.underwriting.data.models.VerifiedYieldAmendment;
import ca.bc.gov.mal.cirras.underwriting.data.models.VerifiedYieldContractCommodity;
import ca.bc.gov.mal.cirras.underwriting.data.models.VerifiedYieldSummary;
import ca.bc.gov.mal.cirras.underwriting.data.models.VerifiedYieldGrainBasket;
import ca.bc.gov.mal.cirras.underwriting.data.models.VerifiedYieldContractSimple;
import ca.bc.gov.nrs.common.wfone.rest.resource.BaseResource;

@XmlRootElement(namespace = ResourceTypes.NAMESPACE, name = ResourceTypes.VERIFIED_YIELD_CONTRACT_SIMPLE_NAME)
@XmlSeeAlso({ VerifiedYieldContractSimpleRsrc.class })
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "@type")
public class VerifiedYieldContractSimpleRsrc extends BaseResource implements VerifiedYieldContractSimple {

	private static final long serialVersionUID = 1L;

	private String verifiedYieldContractGuid;
	private Integer contractId;
	private Integer cropYear;
	
	private List<VerifiedYieldContractCommodity> verifiedYieldContractCommodities = new ArrayList<VerifiedYieldContractCommodity>();
	private List<VerifiedYieldAmendment> verifiedYieldAmendments = new ArrayList<VerifiedYieldAmendment>();
	private List<VerifiedYieldSummary> verifiedYieldSummaries = new ArrayList<VerifiedYieldSummary>();
	private VerifiedYieldGrainBasket verifiedYieldGrainBasket;

	public String getVerifiedYieldContractGuid() {
		return verifiedYieldContractGuid;
	}
	public void setVerifiedYieldContractGuid(String verifiedYieldContractGuid) {
		this.verifiedYieldContractGuid = verifiedYieldContractGuid;
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
	
	public List<VerifiedYieldContractCommodity> getVerifiedYieldContractCommodities() {
		return verifiedYieldContractCommodities;
	}
	public void setVerifiedYieldContractCommodities(List<VerifiedYieldContractCommodity> verifiedYieldContractCommodities) {
		this.verifiedYieldContractCommodities = verifiedYieldContractCommodities;
	}

	public List<VerifiedYieldAmendment> getVerifiedYieldAmendments() {
		return verifiedYieldAmendments;
	}
	public void setVerifiedYieldAmendments(List<VerifiedYieldAmendment> verifiedYieldAmendments) {
		this.verifiedYieldAmendments = verifiedYieldAmendments;
	}

	public List<VerifiedYieldSummary> getVerifiedYieldSummaries() {
		return verifiedYieldSummaries;
	}
	public void setVerifiedYieldSummaries(List<VerifiedYieldSummary> verifiedYieldSummaries) {
		this.verifiedYieldSummaries = verifiedYieldSummaries;
	}
	
	public VerifiedYieldGrainBasket getVerifiedYieldGrainBasket() {
		return verifiedYieldGrainBasket;
	}
	public void setVerifiedYieldGrainBasket(VerifiedYieldGrainBasket verifiedYieldGrainBasket) {
		this.verifiedYieldGrainBasket = verifiedYieldGrainBasket;
	}

}
