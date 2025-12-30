package ca.bc.gov.mal.cirras.underwriting.data.models;

import java.io.Serializable;
import java.util.List;

public interface SeedingDeadlineList<E extends SeedingDeadline> extends Serializable {
	public List<E> getCollection();
	public void setCollection(List<E> collection);
}