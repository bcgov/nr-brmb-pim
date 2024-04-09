package ca.bc.gov.mal.cirras.underwriting.service.api.v1.model.factory;

import java.util.List;

import ca.bc.gov.mal.cirras.underwriting.model.v1.CropVarietyInsurability;
import ca.bc.gov.mal.cirras.underwriting.model.v1.CropVarietyInsurabilityList;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.CropVarietyInsPlantInsXrefDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.CropVarietyInsurabilityDto;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.service.api.model.factory.FactoryContext;
import ca.bc.gov.nrs.wfone.common.service.api.model.factory.FactoryException;
import ca.bc.gov.nrs.wfone.common.webade.authentication.WebAdeAuthentication;

public interface CropVarietyInsurabilityFactory {

	public CropVarietyInsurabilityList<? extends CropVarietyInsurability> getCropVarietyInsurabilityList(
			List<CropVarietyInsurabilityDto> dtos,
			List<CropVarietyInsurabilityDto> validationDtos,
			List<CropVarietyInsPlantInsXrefDto> cropVarietyInsPlantInsXrefDtos,
			Integer insurancePlanId,
			Boolean loadForEdit,
			FactoryContext context, 
			WebAdeAuthentication authentication
	) throws FactoryException, DaoException;
	
	public void updateDto(CropVarietyInsurabilityDto dto, CropVarietyInsurability model) throws FactoryException;

}
