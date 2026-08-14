package ca.bc.gov.mal.cirras.underwriting.data.resources;

import java.util.ArrayList;
import java.util.List;

import jakarta.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import ca.bc.gov.nrs.common.wfone.rest.resource.BaseResource;
import ca.bc.gov.mal.cirras.underwriting.data.resources.types.ResourceTypes;

@XmlRootElement(namespace = ResourceTypes.NAMESPACE, name = ResourceTypes.GRADE_MODIFIER_LIST_NAME)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "@type")
@JsonSubTypes({ @Type(value = GradeModifierListRsrc.class, name = ResourceTypes.GRADE_MODIFIER_LIST) })
public class GradeModifierListRsrc extends BaseResource {
	private static final long serialVersionUID = 1L;

	private List<GradeModifierRsrc> collection = new ArrayList<GradeModifierRsrc>(0);

	public GradeModifierListRsrc() {
		collection = new ArrayList<GradeModifierRsrc>();
	}

	public List<GradeModifierRsrc> getCollection() {
		return collection;
	}

	public void setCollection(List<GradeModifierRsrc> collection) {
		this.collection = collection;
	}
}