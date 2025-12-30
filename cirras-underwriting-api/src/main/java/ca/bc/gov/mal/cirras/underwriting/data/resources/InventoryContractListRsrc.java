package ca.bc.gov.mal.cirras.underwriting.data.resources;

import java.util.ArrayList;
import java.util.List;

import jakarta.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import ca.bc.gov.nrs.common.wfone.rest.resource.PagedResource;
import ca.bc.gov.mal.cirras.underwriting.data.resources.types.ResourceTypes;
import ca.bc.gov.mal.cirras.underwriting.data.models.InventoryContractList;

@XmlRootElement(namespace = ResourceTypes.NAMESPACE, name = ResourceTypes.INVENTORY_CONTRACT_LIST_NAME)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "@type")
@JsonSubTypes({ @Type(value = InventoryContractListRsrc.class, name = ResourceTypes.INVENTORY_CONTRACT_LIST) })
public class InventoryContractListRsrc extends PagedResource implements InventoryContractList<InventoryContractRsrc> {
	private static final long serialVersionUID = 1L;

	private List<InventoryContractRsrc> collection = new ArrayList<InventoryContractRsrc>(0);

	public InventoryContractListRsrc() {
		collection = new ArrayList<InventoryContractRsrc>();
	}

	@Override
	public List<InventoryContractRsrc> getCollection() {
		return collection;
	}

	@Override
	public void setCollection(List<InventoryContractRsrc> collection) {
		this.collection = collection;
	}

}