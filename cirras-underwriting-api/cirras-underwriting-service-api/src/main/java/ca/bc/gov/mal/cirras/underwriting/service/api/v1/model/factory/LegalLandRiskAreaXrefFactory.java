package ca.bc.gov.mal.cirras.underwriting.services.model.factory;

import ca.bc.gov.mal.cirras.underwriting.data.models.LegalLandRiskArea;
import ca.bc.gov.mal.cirras.underwriting.data.entities.LegalLandRiskAreaXrefDto;

public interface LegalLandRiskAreaXrefFactory {
	
	void createLegalLandRiskAreaXref(LegalLandRiskAreaXrefDto dto, LegalLandRiskArea model);

}
