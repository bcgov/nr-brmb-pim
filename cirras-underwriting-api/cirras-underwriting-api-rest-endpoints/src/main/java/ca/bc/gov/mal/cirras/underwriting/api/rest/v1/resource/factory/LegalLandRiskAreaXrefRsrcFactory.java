package ca.bc.gov.mal.cirras.underwriting.data.assemblers;

import ca.bc.gov.nrs.wfone.common.rest.endpoints.resource.factory.BaseResourceFactory;
import ca.bc.gov.mal.cirras.underwriting.data.models.LegalLandRiskArea;
import ca.bc.gov.mal.cirras.underwriting.data.entities.LegalLandRiskAreaXrefDto;
import ca.bc.gov.mal.cirras.underwriting.services.model.factory.LegalLandRiskAreaXrefFactory;

public class LegalLandRiskAreaXrefRsrcFactory extends BaseResourceFactory implements LegalLandRiskAreaXrefFactory { 
	
	@Override
	public void createLegalLandRiskAreaXref(LegalLandRiskAreaXrefDto dto, LegalLandRiskArea model) {

		dto.setLegalLandId(model.getLegalLandId());
		dto.setRiskAreaId(model.getRiskAreaId());
		dto.setActiveFromCropYear(1980); //Not currently used
		dto.setActiveToCropYear(null); //Not currently used
		
	}
}
