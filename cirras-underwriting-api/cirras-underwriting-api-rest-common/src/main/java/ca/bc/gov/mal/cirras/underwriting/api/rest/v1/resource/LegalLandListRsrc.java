package ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import ca.bc.gov.nrs.common.wfone.rest.resource.PagedResource;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.types.ResourceTypes;
import ca.bc.gov.mal.cirras.underwriting.model.v1.LegalLandList;

@XmlRootElement(namespace = ResourceTypes.NAMESPACE, name = ResourceTypes.LEGAL_LAND_LIST_NAME)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "@type")
@JsonSubTypes({ @Type(value = LegalLandListRsrc.class, name = ResourceTypes.LEGAL_LAND_LIST) })
public class LegalLandListRsrc extends PagedResource implements LegalLandList<LegalLandRsrc> {
	private static final long serialVersionUID = 1L;

	private List<LegalLandRsrc> collection = new ArrayList<LegalLandRsrc>(0);

	public LegalLandListRsrc() {
		collection = new ArrayList<LegalLandRsrc>();
	}

	@Override
	public List<LegalLandRsrc> getCollection() {
		return collection;
	}

	@Override
	public void setCollection(List<LegalLandRsrc> collection) {
		this.collection = collection;
	}
}