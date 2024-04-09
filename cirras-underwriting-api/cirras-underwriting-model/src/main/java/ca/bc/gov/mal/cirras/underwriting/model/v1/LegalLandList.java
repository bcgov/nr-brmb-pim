package ca.bc.gov.mal.cirras.underwriting.model.v1;

import java.io.Serializable;
import java.util.List;

public interface LegalLandList<E extends LegalLand<? extends Field>> extends Serializable {
	public List<E> getCollection();
	public void setCollection(List<E> collection);
}