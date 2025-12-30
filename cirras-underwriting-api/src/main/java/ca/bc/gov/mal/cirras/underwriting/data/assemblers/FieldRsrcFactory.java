package ca.bc.gov.mal.cirras.underwriting.data.assemblers;

import ca.bc.gov.nrs.wfone.common.rest.endpoints.resource.factory.BaseResourceFactory;
import ca.bc.gov.mal.cirras.underwriting.data.entities.FieldDto;
import ca.bc.gov.mal.cirras.underwriting.data.resources.AnnualFieldRsrc;

public class FieldRsrcFactory extends BaseResourceFactory { 
	
	public void createField(FieldDto dto, AnnualFieldRsrc model) {

		dto.setFieldId(null);
		dto.setFieldLabel(model.getFieldLabel());
		dto.setLocation(model.getFieldLocation());
		dto.setActiveFromCropYear(model.getCropYear());
		dto.setActiveToCropYear(null);

	}
	
}
