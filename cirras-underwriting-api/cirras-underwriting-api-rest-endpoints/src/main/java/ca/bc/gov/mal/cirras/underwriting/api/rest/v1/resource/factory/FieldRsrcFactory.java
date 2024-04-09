package ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.factory;

import ca.bc.gov.nrs.wfone.common.rest.endpoints.resource.factory.BaseResourceFactory;
import ca.bc.gov.mal.cirras.underwriting.model.v1.AnnualField;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.FieldDto;
import ca.bc.gov.mal.cirras.underwriting.service.api.v1.model.factory.FieldFactory;

public class FieldRsrcFactory extends BaseResourceFactory implements FieldFactory { 
	

	@Override
	public void createField(FieldDto dto, AnnualField model) {

		dto.setFieldId(null);
		dto.setFieldLabel(model.getFieldLabel());
		dto.setActiveFromCropYear(model.getCropYear());
		dto.setActiveToCropYear(null);

	}
	
}
