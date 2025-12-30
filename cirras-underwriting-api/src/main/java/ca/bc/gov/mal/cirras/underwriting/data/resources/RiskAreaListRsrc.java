package ca.bc.gov.mal.cirras.underwriting.data.resources;

import java.util.ArrayList;
import java.util.List;

import jakarta.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import ca.bc.gov.nrs.common.wfone.rest.resource.BaseResource;
import ca.bc.gov.mal.cirras.underwriting.data.resources.types.ResourceTypes;
import ca.bc.gov.mal.cirras.underwriting.data.models.RiskAreaList;

@XmlRootElement(namespace = ResourceTypes.NAMESPACE, name = ResourceTypes.RISK_AREA_LIST_NAME)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "@type")
@JsonSubTypes({ @Type(value = RiskAreaListRsrc.class, name = ResourceTypes.RISK_AREA_LIST) })
public class RiskAreaListRsrc extends BaseResource implements RiskAreaList<RiskAreaRsrc> {
	private static final long serialVersionUID = 1L;

	private List<RiskAreaRsrc> collection = new ArrayList<RiskAreaRsrc>(0);

	public RiskAreaListRsrc() {
		collection = new ArrayList<RiskAreaRsrc>();
	}

	@Override
	public List<RiskAreaRsrc> getCollection() {
		return collection;
	}

	@Override
	public void setCollection(List<RiskAreaRsrc> collection) {
		this.collection = collection;
	}
}