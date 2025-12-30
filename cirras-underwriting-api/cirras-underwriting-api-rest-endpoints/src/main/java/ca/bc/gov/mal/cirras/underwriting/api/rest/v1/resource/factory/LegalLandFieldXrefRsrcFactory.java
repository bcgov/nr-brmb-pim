package ca.bc.gov.mal.cirras.underwriting.data.assemblers;

import ca.bc.gov.nrs.wfone.common.rest.endpoints.resource.factory.BaseResourceFactory;
import ca.bc.gov.mal.cirras.underwriting.data.models.AnnualField;
import ca.bc.gov.mal.cirras.underwriting.data.entities.LegalLandFieldXrefDto;
import ca.bc.gov.mal.cirras.underwriting.services.model.factory.LegalLandFieldXrefFactory;

public class LegalLandFieldXrefRsrcFactory extends BaseResourceFactory implements LegalLandFieldXrefFactory { 

	
	@Override
	public void createLegalLandFieldXref(LegalLandFieldXrefDto dto, AnnualField model) {

		dto.setLegalLandId(model.getLegalLandId());
		dto.setFieldId(model.getFieldId());
		
	}
}
