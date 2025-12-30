package ca.bc.gov.mal.cirras.underwriting.data.models;

import java.io.Serializable;
import java.util.List;

public interface CommodityTypeCodeList<E extends CommodityTypeCode> extends Serializable {
	public List<E> getCollection();
	public void setCollection(List<E> collection);
}