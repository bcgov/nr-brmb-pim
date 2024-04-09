package ca.bc.gov.mal.cirras.underwriting.service.api.v1.model.factory;

import ca.bc.gov.mal.cirras.underwriting.model.v1.AnnualField;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.LegalLandFieldXrefDto;

public interface LegalLandFieldXrefFactory {
	
	void createLegalLandFieldXref(LegalLandFieldXrefDto dto, AnnualField model);

}
