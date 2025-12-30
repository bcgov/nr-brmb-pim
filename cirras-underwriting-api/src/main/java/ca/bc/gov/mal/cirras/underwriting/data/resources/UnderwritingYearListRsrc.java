package ca.bc.gov.mal.cirras.underwriting.data.resources;

import java.util.ArrayList;
import java.util.List;

import jakarta.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import ca.bc.gov.nrs.common.wfone.rest.resource.BaseResource;
import ca.bc.gov.mal.cirras.underwriting.data.resources.types.ResourceTypes;

@XmlRootElement(namespace = ResourceTypes.NAMESPACE, name = ResourceTypes.UNDERWRITING_YEAR_LIST_NAME)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "@type")
@JsonSubTypes({ @Type(value = UnderwritingYearListRsrc.class, name = ResourceTypes.UNDERWRITING_YEAR_LIST) })
public class UnderwritingYearListRsrc extends BaseResource {
	private static final long serialVersionUID = 1L;

	private List<UnderwritingYearRsrc> collection = new ArrayList<UnderwritingYearRsrc>(0);

	public UnderwritingYearListRsrc() {
		collection = new ArrayList<UnderwritingYearRsrc>();
	}

	public List<UnderwritingYearRsrc> getCollection() {
		return collection;
	}

	public void setCollection(List<UnderwritingYearRsrc> collection) {
		this.collection = collection;
	}
}