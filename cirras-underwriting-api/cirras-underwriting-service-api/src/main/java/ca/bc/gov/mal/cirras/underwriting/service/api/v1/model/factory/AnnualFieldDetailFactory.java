package ca.bc.gov.mal.cirras.underwriting.services.model.factory;

import ca.bc.gov.mal.cirras.underwriting.data.models.AnnualField;
import ca.bc.gov.mal.cirras.underwriting.data.entities.AnnualFieldDetailDto;

public interface AnnualFieldDetailFactory {

	void createAnnualFieldDetail(AnnualFieldDetailDto dto, AnnualField model);

	void createRolloverAnnualFieldDetail(AnnualFieldDetailDto dto, Integer legalLandId, Integer fieldId, Integer cropYear);
}
