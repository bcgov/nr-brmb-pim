package ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.factory;

import ca.bc.gov.nrs.wfone.common.rest.endpoints.resource.factory.BaseResourceFactory;
import ca.bc.gov.mal.cirras.underwriting.model.v1.LegalLandRiskArea;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.LegalLandRiskAreaXrefDto;
import ca.bc.gov.mal.cirras.underwriting.service.api.v1.model.factory.LegalLandRiskAreaXrefFactory;

public class LegalLandRiskAreaXrefRsrcFactory extends BaseResourceFactory implements LegalLandRiskAreaXrefFactory { 
	
	@Override
	public void createLegalLandRiskAreaXref(LegalLandRiskAreaXrefDto dto, LegalLandRiskArea model) {

		dto.setLegalLandId(model.getLegalLandId());
		dto.setRiskAreaId(model.getRiskAreaId());
		dto.setActiveFromCropYear(1980); //Not currently used
		dto.setActiveToCropYear(null); //Not currently used
		
	}
}
