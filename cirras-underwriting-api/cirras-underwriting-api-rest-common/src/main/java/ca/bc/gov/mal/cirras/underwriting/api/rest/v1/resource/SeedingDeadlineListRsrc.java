package ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import ca.bc.gov.nrs.common.wfone.rest.resource.BaseResource;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.types.ResourceTypes;
import ca.bc.gov.mal.cirras.underwriting.model.v1.SeedingDeadline;
import ca.bc.gov.mal.cirras.underwriting.model.v1.SeedingDeadlineList;

@XmlRootElement(namespace = ResourceTypes.NAMESPACE, name = ResourceTypes.SEEDING_DEADLINE_LIST_NAME)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "@type")
@JsonSubTypes({ @Type(value = SeedingDeadlineListRsrc.class, name = ResourceTypes.SEEDING_DEADLINE_LIST) })
public class SeedingDeadlineListRsrc extends BaseResource implements SeedingDeadlineList<SeedingDeadline> {
	private static final long serialVersionUID = 1L;

	private List<SeedingDeadline> collection = new ArrayList<SeedingDeadline>(0);

	public SeedingDeadlineListRsrc() {
		collection = new ArrayList<SeedingDeadline>();
	}

	@Override
	public List<SeedingDeadline> getCollection() {
		return collection;
	}

	@Override
	public void setCollection(List<SeedingDeadline> collection) {
		this.collection = collection;
	}
}