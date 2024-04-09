package ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.factory;

import ca.bc.gov.nrs.wfone.common.rest.endpoints.resource.factory.BaseResourceFactory;
import ca.bc.gov.mal.cirras.underwriting.model.v1.AnnualField;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.LegalLandFieldXrefDto;
import ca.bc.gov.mal.cirras.underwriting.service.api.v1.model.factory.LegalLandFieldXrefFactory;

public class LegalLandFieldXrefRsrcFactory extends BaseResourceFactory implements LegalLandFieldXrefFactory { 

	
	@Override
	public void createLegalLandFieldXref(LegalLandFieldXrefDto dto, AnnualField model) {

		dto.setLegalLandId(model.getLegalLandId());
		dto.setFieldId(model.getFieldId());
		
	}
}
