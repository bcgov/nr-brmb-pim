package ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import ca.bc.gov.nrs.common.wfone.rest.resource.BaseResource;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.types.ResourceTypes;
import ca.bc.gov.mal.cirras.underwriting.model.v1.YieldMeasUnitTypeCodeList;

@XmlRootElement(namespace = ResourceTypes.NAMESPACE, name = ResourceTypes.YIELD_MEAS_UNIT_TYPE_CODE_LIST_NAME)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "@type")
@JsonSubTypes({ @Type(value = YieldMeasUnitTypeCodeListRsrc.class, name = ResourceTypes.YIELD_MEAS_UNIT_TYPE_CODE_LIST) })
public class YieldMeasUnitTypeCodeListRsrc extends BaseResource implements YieldMeasUnitTypeCodeList<YieldMeasUnitTypeCodeRsrc> {
	private static final long serialVersionUID = 1L;

	private List<YieldMeasUnitTypeCodeRsrc> collection = new ArrayList<YieldMeasUnitTypeCodeRsrc>(0);

	public YieldMeasUnitTypeCodeListRsrc() {
		collection = new ArrayList<YieldMeasUnitTypeCodeRsrc>();
	}

	@Override
	public List<YieldMeasUnitTypeCodeRsrc> getCollection() {
		return collection;
	}

	@Override
	public void setCollection(List<YieldMeasUnitTypeCodeRsrc> collection) {
		this.collection = collection;
	}
}