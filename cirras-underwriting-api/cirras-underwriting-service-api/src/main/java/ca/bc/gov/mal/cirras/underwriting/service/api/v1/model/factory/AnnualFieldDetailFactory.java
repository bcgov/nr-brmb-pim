package ca.bc.gov.mal.cirras.underwriting.service.api.v1.model.factory;

import ca.bc.gov.mal.cirras.underwriting.model.v1.AnnualField;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.AnnualFieldDetailDto;

public interface AnnualFieldDetailFactory {

	void createAnnualFieldDetail(AnnualFieldDetailDto dto, AnnualField model);

	void createRolloverAnnualFieldDetail(AnnualFieldDetailDto dto, Integer legalLandId, Integer fieldId, Integer cropYear);
}
