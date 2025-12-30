package ca.bc.gov.mal.cirras.underwriting.data.resources;

import java.util.ArrayList;
import java.util.List;

import jakarta.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import ca.bc.gov.nrs.common.wfone.rest.resource.BaseResource;
import ca.bc.gov.mal.cirras.underwriting.data.resources.types.ResourceTypes;
import ca.bc.gov.mal.cirras.underwriting.data.models.GradeModifierType;

@XmlRootElement(namespace = ResourceTypes.NAMESPACE, name = ResourceTypes.GRADE_MODIFIER_TYPE_LIST_NAME)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "@type")
@JsonSubTypes({ @Type(value = GradeModifierTypeListRsrc.class, name = ResourceTypes.GRADE_MODIFIER_TYPE_LIST) })
public class GradeModifierTypeListRsrc extends BaseResource {
	private static final long serialVersionUID = 1L;

	private List<GradeModifierType> collection = new ArrayList<GradeModifierType>(0);

	public GradeModifierTypeListRsrc() {
		collection = new ArrayList<GradeModifierType>();
	}

	public List<GradeModifierType> getCollection() {
		return collection;
	}

	public void setCollection(List<GradeModifierType> collection) {
		this.collection = collection;
	}
}