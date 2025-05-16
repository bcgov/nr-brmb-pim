package ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.types.ResourceTypes;
import ca.bc.gov.mal.cirras.underwriting.model.v1.VerifiedYieldAmendment;
import ca.bc.gov.mal.cirras.underwriting.model.v1.VerifiedYieldContractCommodity;
import ca.bc.gov.mal.cirras.underwriting.model.v1.VerifiedYieldSummary;
import ca.bc.gov.mal.cirras.underwriting.model.v1.VerifiedYieldGrainBasket;
import ca.bc.gov.mal.cirras.underwriting.model.v1.VerifiedYieldContractSimple;
import ca.bc.gov.nrs.common.wfone.rest.resource.BaseResource;

@XmlRootElement(namespace = ResourceTypes.NAMESPACE, name = ResourceTypes.VERIFIED_YIELD_CONTRACT_SIMPLE_NAME)
@XmlSeeAlso({ VerifiedYieldContractSimpleRsrc.class })
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "@type")
public class VerifiedYieldContractSimpleRsrc extends BaseResource implements VerifiedYieldContractSimple {

	private static final long serialVersionUID = 1L;

	private Integer contractId;
	private Integer cropYear;
	
	private List<VerifiedYieldContractCommodity> verifiedYieldContractCommodities = new ArrayList<VerifiedYieldContractCommodity>();
	private List<VerifiedYieldAmendment> verifiedYieldAmendments = new ArrayList<VerifiedYieldAmendment>();
	private List<VerifiedYieldSummary> verifiedYieldSummaries = new ArrayList<VerifiedYieldSummary>();
	private VerifiedYieldGrainBasket verifiedYieldGrainBasket;

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
