package ca.bc.gov.mal.cirras.underwriting.data.models;

import java.io.Serializable;

//
// This is not going to be a resource.
//
public class DopYieldContractCommodityBerriesOutbox extends BaseOutbox implements Serializable {
	private static final long serialVersionUID = 1L;

	private Integer declaredYieldContractCommodityBerriesOutboxId;
	private String declaredYieldContractCommodityBerriesGuid;
	
	public Integer getDeclaredYieldContractCommodityBerriesOutboxId() {
		return declaredYieldContractCommodityBerriesOutboxId;
	}
	public void setDeclaredYieldContractCommodityBerriesOutboxId(Integer declaredYieldContractCommodityBerriesOutboxId) {
		this.declaredYieldContractCommodityBerriesOutboxId = declaredYieldContractCommodityBerriesOutboxId;
	}

	public String getDeclaredYieldContractCommodityBerriesGuid() {
		return declaredYieldContractCommodityBerriesGuid;
	}
	public void setDeclaredYieldContractCommodityBerriesGuid(String declaredYieldContractCommodityBerriesGuid) {
		this.declaredYieldContractCommodityBerriesGuid = declaredYieldContractCommodityBerriesGuid;
	}

	@Override
	public String getSourceKey() {
		return declaredYieldContractCommodityBerriesGuid;
	}
	
	@Override
	public Integer getOutboxKey() {
		return declaredYieldContractCommodityBerriesOutboxId;
	}
}
