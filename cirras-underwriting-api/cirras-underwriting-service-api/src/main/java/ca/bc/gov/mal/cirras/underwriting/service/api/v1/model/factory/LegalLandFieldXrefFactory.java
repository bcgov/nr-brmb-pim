package ca.bc.gov.mal.cirras.underwriting.services.model.factory;

import ca.bc.gov.mal.cirras.underwriting.data.models.AnnualField;
import ca.bc.gov.mal.cirras.underwriting.data.entities.LegalLandFieldXrefDto;

public interface LegalLandFieldXrefFactory {
	
	void createLegalLandFieldXref(LegalLandFieldXrefDto dto, AnnualField model);

}
