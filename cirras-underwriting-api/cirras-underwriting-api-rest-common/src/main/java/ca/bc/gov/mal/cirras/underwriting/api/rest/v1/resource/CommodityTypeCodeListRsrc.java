package ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import ca.bc.gov.nrs.common.wfone.rest.resource.BaseResource;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.types.ResourceTypes;
import ca.bc.gov.mal.cirras.underwriting.model.v1.CommodityTypeCodeList;

@XmlRootElement(namespace = ResourceTypes.NAMESPACE, name = ResourceTypes.COMMODITY_TYPE_CODE_LIST_NAME)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "@type")
@JsonSubTypes({ @Type(value = CommodityTypeCodeListRsrc.class, name = ResourceTypes.COMMODITY_TYPE_CODE_LIST) })
public class CommodityTypeCodeListRsrc extends BaseResource implements CommodityTypeCodeList<SyncCommodityTypeCodeRsrc> {
	private static final long serialVersionUID = 1L;

	private List<SyncCommodityTypeCodeRsrc> collection = new ArrayList<SyncCommodityTypeCodeRsrc>(0);

	public CommodityTypeCodeListRsrc() {
		collection = new ArrayList<SyncCommodityTypeCodeRsrc>();
	}

	@Override
	public List<SyncCommodityTypeCodeRsrc> getCollection() {
		return collection;
	}

	@Override
	public void setCollection(List<SyncCommodityTypeCodeRsrc> collection) {
		this.collection = collection;
	}
}