package ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource;

import java.util.ArrayList;
import java.util.List;

import jakarta.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import ca.bc.gov.nrs.common.wfone.rest.resource.BaseResource;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.types.ResourceTypes;
import ca.bc.gov.mal.cirras.underwriting.model.v1.CropCommodityList;

@XmlRootElement(namespace = ResourceTypes.NAMESPACE, name = ResourceTypes.CROP_COMMODITY_LIST_NAME)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "@type")
@JsonSubTypes({ @Type(value = CropCommodityListRsrc.class, name = ResourceTypes.CROP_COMMODITY_LIST) })
public class CropCommodityListRsrc extends BaseResource implements CropCommodityList<CropCommodityRsrc> {
	private static final long serialVersionUID = 1L;

	private List<CropCommodityRsrc> collection = new ArrayList<CropCommodityRsrc>(0);

	public CropCommodityListRsrc() {
		collection = new ArrayList<CropCommodityRsrc>();
	}

	@Override
	public List<CropCommodityRsrc> getCollection() {
		return collection;
	}

	@Override
	public void setCollection(List<CropCommodityRsrc> collection) {
		this.collection = collection;
	}
}