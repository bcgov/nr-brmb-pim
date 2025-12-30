package ca.bc.gov.mal.cirras.underwriting.data.resources;

import java.util.ArrayList;
import java.util.List;

import jakarta.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import ca.bc.gov.nrs.common.wfone.rest.resource.PagedResource;
import ca.bc.gov.mal.cirras.underwriting.data.resources.types.ResourceTypes;
import ca.bc.gov.mal.cirras.underwriting.data.models.UwContractList;

@XmlRootElement(namespace = ResourceTypes.NAMESPACE, name = ResourceTypes.UWCONTRACT_LIST_NAME)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "@type")
@JsonSubTypes({ @Type(value = UwContractListRsrc.class, name = ResourceTypes.UWCONTRACT_LIST) })
public class UwContractListRsrc extends PagedResource implements UwContractList<UwContractRsrc> {
	private static final long serialVersionUID = 1L;

	private List<UwContractRsrc> collection = new ArrayList<UwContractRsrc>(0);

	public UwContractListRsrc() {
		collection = new ArrayList<UwContractRsrc>();
	}

	@Override
	public List<UwContractRsrc> getCollection() {
		return collection;
	}

	@Override
	public void setCollection(List<UwContractRsrc> collection) {
		this.collection = collection;
	}
}