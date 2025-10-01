package ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource;

import java.util.ArrayList;
import java.util.List;

import jakarta.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import ca.bc.gov.nrs.common.wfone.rest.resource.BaseResource;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.types.ResourceTypes;
import ca.bc.gov.mal.cirras.underwriting.model.v1.GradeModifierTypeList;
import ca.bc.gov.mal.cirras.underwriting.model.v1.GradeModifierType;

@XmlRootElement(namespace = ResourceTypes.NAMESPACE, name = ResourceTypes.GRADE_MODIFIER_TYPE_LIST_NAME)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "@type")
@JsonSubTypes({ @Type(value = GradeModifierTypeListRsrc.class, name = ResourceTypes.GRADE_MODIFIER_TYPE_LIST) })
public class GradeModifierTypeListRsrc extends BaseResource implements GradeModifierTypeList<GradeModifierType> {
	private static final long serialVersionUID = 1L;

	private List<GradeModifierType> collection = new ArrayList<GradeModifierType>(0);

	public GradeModifierTypeListRsrc() {
		collection = new ArrayList<GradeModifierType>();
	}

	@Override
	public List<GradeModifierType> getCollection() {
		return collection;
	}

	@Override
	public void setCollection(List<GradeModifierType> collection) {
		this.collection = collection;
	}
}