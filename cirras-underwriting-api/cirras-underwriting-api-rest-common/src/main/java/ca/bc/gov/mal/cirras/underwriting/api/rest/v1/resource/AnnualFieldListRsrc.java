package ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import ca.bc.gov.nrs.common.wfone.rest.resource.BaseResource;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.types.ResourceTypes;
import ca.bc.gov.mal.cirras.underwriting.model.v1.AnnualFieldList;

@XmlRootElement(namespace = ResourceTypes.NAMESPACE, name = ResourceTypes.ANNUAL_FIELD_LIST_NAME)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "@type")
@JsonSubTypes({ @Type(value = AnnualFieldListRsrc.class, name = ResourceTypes.ANNUAL_FIELD_LIST) })
public class AnnualFieldListRsrc extends BaseResource implements AnnualFieldList<AnnualFieldRsrc> {
	private static final long serialVersionUID = 1L;

	private List<AnnualFieldRsrc> collection = new ArrayList<AnnualFieldRsrc>(0);

	public AnnualFieldListRsrc() {
		collection = new ArrayList<AnnualFieldRsrc>();
	}

	@Override
	public List<AnnualFieldRsrc> getCollection() {
		return collection;
	}

	@Override
	public void setCollection(List<AnnualFieldRsrc> collection) {
		this.collection = collection;
	}
}