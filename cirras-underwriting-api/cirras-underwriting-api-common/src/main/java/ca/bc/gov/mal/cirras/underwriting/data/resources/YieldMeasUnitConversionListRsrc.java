package ca.bc.gov.mal.cirras.underwriting.data.resources;

import java.util.ArrayList;
import java.util.List;

import jakarta.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import ca.bc.gov.nrs.common.wfone.rest.resource.BaseResource;
import ca.bc.gov.mal.cirras.underwriting.data.resources.types.ResourceTypes;
import ca.bc.gov.mal.cirras.underwriting.data.models.YieldMeasUnitConversion;

@XmlRootElement(namespace = ResourceTypes.NAMESPACE, name = ResourceTypes.YIELD_MEAS_UNIT_CONVERSION_LIST_NAME)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "@type")
@JsonSubTypes({ @Type(value = YieldMeasUnitConversionListRsrc.class, name = ResourceTypes.YIELD_MEAS_UNIT_CONVERSION_LIST) })
public class YieldMeasUnitConversionListRsrc extends BaseResource {
	private static final long serialVersionUID = 1L;

	private List<YieldMeasUnitConversion> collection = new ArrayList<YieldMeasUnitConversion>(0);

	public YieldMeasUnitConversionListRsrc() {
		collection = new ArrayList<YieldMeasUnitConversion>();
	}

	public List<YieldMeasUnitConversion> getCollection() {
		return collection;
	}

	public void setCollection(List<YieldMeasUnitConversion> collection) {
		this.collection = collection;
	}
}