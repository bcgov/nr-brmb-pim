package ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import ca.bc.gov.nrs.common.wfone.rest.resource.BaseResource;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.types.ResourceTypes;
import ca.bc.gov.mal.cirras.underwriting.model.v1.UnderwritingYearList;

@XmlRootElement(namespace = ResourceTypes.NAMESPACE, name = ResourceTypes.UNDERWRITING_YEAR_LIST_NAME)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "@type")
@JsonSubTypes({ @Type(value = UnderwritingYearListRsrc.class, name = ResourceTypes.UNDERWRITING_YEAR_LIST) })
public class UnderwritingYearListRsrc extends BaseResource implements UnderwritingYearList<UnderwritingYearRsrc> {
	private static final long serialVersionUID = 1L;

	private List<UnderwritingYearRsrc> collection = new ArrayList<UnderwritingYearRsrc>(0);

	public UnderwritingYearListRsrc() {
		collection = new ArrayList<UnderwritingYearRsrc>();
	}

	@Override
	public List<UnderwritingYearRsrc> getCollection() {
		return collection;
	}

	@Override
	public void setCollection(List<UnderwritingYearRsrc> collection) {
		this.collection = collection;
	}
}