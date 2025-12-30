package ca.bc.gov.mal.cirras.underwriting.data.assemblers;

import ca.bc.gov.nrs.wfone.common.rest.endpoints.resource.factory.BaseResourceFactory;
import ca.bc.gov.mal.cirras.underwriting.data.models.AnnualField;
import ca.bc.gov.mal.cirras.underwriting.data.entities.LegalLandFieldXrefDto;

public class LegalLandFieldXrefRsrcFactory extends BaseResourceFactory { 

	public void createLegalLandFieldXref(LegalLandFieldXrefDto dto, AnnualField model) {

		dto.setLegalLandId(model.getLegalLandId());
		dto.setFieldId(model.getFieldId());
		
	}
}
