package ca.bc.gov.mal.cirras.underwriting.services.model.factory;

import ca.bc.gov.mal.cirras.underwriting.data.models.AnnualField;
import ca.bc.gov.mal.cirras.underwriting.data.entities.FieldDto;

public interface FieldFactory {

	void createField(FieldDto dto, AnnualField model);

}
