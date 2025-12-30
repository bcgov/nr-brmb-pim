package ca.bc.gov.mal.cirras.underwriting.data.assemblers;

import ca.bc.gov.nrs.wfone.common.rest.endpoints.resource.factory.BaseResourceFactory;
import ca.bc.gov.mal.cirras.underwriting.data.entities.LegalLandFieldXrefDto;
import ca.bc.gov.mal.cirras.underwriting.data.resources.AnnualFieldRsrc;

public class LegalLandFieldXrefRsrcFactory extends BaseResourceFactory { 

	public void createLegalLandFieldXref(LegalLandFieldXrefDto dto, AnnualFieldRsrc model) {

		dto.setLegalLandId(model.getLegalLandId());
		dto.setFieldId(model.getFieldId());
		
	}
}
