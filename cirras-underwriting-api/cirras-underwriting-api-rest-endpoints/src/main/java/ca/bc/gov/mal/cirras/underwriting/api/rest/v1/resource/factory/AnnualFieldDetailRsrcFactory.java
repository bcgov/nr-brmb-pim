package ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.factory;

import ca.bc.gov.nrs.wfone.common.rest.endpoints.resource.factory.BaseResourceFactory;
import ca.bc.gov.mal.cirras.underwriting.model.v1.AnnualField;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.AnnualFieldDetailDto;
import ca.bc.gov.mal.cirras.underwriting.service.api.v1.model.factory.AnnualFieldDetailFactory;

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
