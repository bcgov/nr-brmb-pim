package ca.bc.gov.mal.cirras.underwriting.data.assemblers;

import ca.bc.gov.nrs.wfone.common.rest.endpoints.resource.factory.BaseResourceFactory;
import ca.bc.gov.mal.cirras.underwriting.data.models.AnnualField;
import ca.bc.gov.mal.cirras.underwriting.data.entities.AnnualFieldDetailDto;

public class AnnualFieldDetailRsrcFactory extends BaseResourceFactory { 
	
	
	public void createAnnualFieldDetail(AnnualFieldDetailDto dto, AnnualField model) {

		dto.setAnnualFieldDetailId(null);
		dto.setLegalLandId(model.getLegalLandId());
		dto.setFieldId(model.getFieldId());
		dto.setCropYear(model.getCropYear());

	}
	
	
	public void createRolloverAnnualFieldDetail(AnnualFieldDetailDto dto, Integer legalLandId, Integer fieldId, Integer cropYear) {
		dto.setAnnualFieldDetailId(null);
		dto.setLegalLandId(legalLandId);
		dto.setFieldId(fieldId);
		dto.setCropYear(cropYear);
	}
}
