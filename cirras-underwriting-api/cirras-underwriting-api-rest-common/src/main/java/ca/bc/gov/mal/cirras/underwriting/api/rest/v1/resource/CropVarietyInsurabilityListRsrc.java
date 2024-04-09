package ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import ca.bc.gov.nrs.common.wfone.rest.resource.BaseResource;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.types.ResourceTypes;
import ca.bc.gov.mal.cirras.underwriting.model.v1.CropVarietyInsurabilityList;
import ca.bc.gov.mal.cirras.underwriting.model.v1.CropVarietyInsurability;

@XmlRootElement(namespace = ResourceTypes.NAMESPACE, name = ResourceTypes.CROP_VARIETY_INSURABILITY_LIST_NAME)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "@type")
@JsonSubTypes({ @Type(value = CropVarietyInsurabilityListRsrc.class, name = ResourceTypes.CROP_VARIETY_INSURABILITY_LIST) })
public class CropVarietyInsurabilityListRsrc extends BaseResource implements CropVarietyInsurabilityList<CropVarietyInsurability> {
	private static final long serialVersionUID = 1L;

	private List<CropVarietyInsurability> collection = new ArrayList<CropVarietyInsurability>(0);

	public CropVarietyInsurabilityListRsrc() {
		collection = new ArrayList<CropVarietyInsurability>();
	}

	@Override
	public List<CropVarietyInsurability> getCollection() {
		return collection;
	}

	@Override
	public void setCollection(List<CropVarietyInsurability> collection) {
		this.collection = collection;
	}
}