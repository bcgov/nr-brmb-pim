package ca.bc.gov.mal.cirras.underwriting.service.api.v1.model.factory;

import ca.bc.gov.mal.cirras.underwriting.model.v1.LegalLandRiskArea;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.LegalLandRiskAreaXrefDto;

public interface LegalLandRiskAreaXrefFactory {
	
	void createLegalLandRiskAreaXref(LegalLandRiskAreaXrefDto dto, LegalLandRiskArea model);

}
