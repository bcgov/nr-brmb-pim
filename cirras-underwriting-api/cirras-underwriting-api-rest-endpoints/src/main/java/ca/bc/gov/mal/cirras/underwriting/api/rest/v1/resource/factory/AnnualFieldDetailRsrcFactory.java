package ca.bc.gov.mal.cirras.underwriting.data.assemblers;

import ca.bc.gov.nrs.wfone.common.rest.endpoints.resource.factory.BaseResourceFactory;
import ca.bc.gov.mal.cirras.underwriting.data.models.AnnualField;
import ca.bc.gov.mal.cirras.underwriting.data.entities.AnnualFieldDetailDto;
import ca.bc.gov.mal.cirras.underwriting.services.model.factory.AnnualFieldDetailFactory;

public class AnnualFieldDetailRsrcFactory extends BaseResourceFactory implements AnnualFieldDetailFactory { 
	
	@Override
	public void createAnnualFieldDetail(AnnualFieldDetailDto dto, AnnualField model) {

		dto.setAnnualFieldDetailId(null);
		dto.setLegalLandId(model.getLegalLandId());
		dto.setFieldId(model.getFieldId());
		dto.setCropYear(model.getCropYear());

	}
	
	@Override
	public void createRolloverAnnualFieldDetail(AnnualFieldDetailDto dto, Integer legalLandId, Integer fieldId, Integer cropYear) {
		dto.setAnnualFieldDetailId(null);
		dto.setLegalLandId(legalLandId);
		dto.setFieldId(fieldId);
		dto.setCropYear(cropYear);
	}
}
