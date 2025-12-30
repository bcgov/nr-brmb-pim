package ca.bc.gov.mal.cirras.underwriting.data.assemblers;

import ca.bc.gov.nrs.wfone.common.rest.endpoints.resource.factory.BaseResourceFactory;
import ca.bc.gov.mal.cirras.underwriting.data.models.AnnualField;
import ca.bc.gov.mal.cirras.underwriting.data.entities.FieldDto;

public class FieldRsrcFactory extends BaseResourceFactory { 
	
	public void createField(FieldDto dto, AnnualField model) {

		dto.setFieldId(null);
		dto.setFieldLabel(model.getFieldLabel());
		dto.setLocation(model.getFieldLocation());
		dto.setActiveFromCropYear(model.getCropYear());
		dto.setActiveToCropYear(null);

	}
	
}
