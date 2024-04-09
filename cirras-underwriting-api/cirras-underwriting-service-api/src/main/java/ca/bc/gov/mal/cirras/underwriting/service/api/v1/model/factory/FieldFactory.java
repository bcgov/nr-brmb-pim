package ca.bc.gov.mal.cirras.underwriting.service.api.v1.model.factory;

import ca.bc.gov.mal.cirras.underwriting.model.v1.AnnualField;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.FieldDto;

public interface FieldFactory {

	void createField(FieldDto dto, AnnualField model);

}
