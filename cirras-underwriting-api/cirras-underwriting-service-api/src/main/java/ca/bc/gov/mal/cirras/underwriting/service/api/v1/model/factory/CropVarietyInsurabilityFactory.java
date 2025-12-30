package ca.bc.gov.mal.cirras.underwriting.services.model.factory;

import java.util.List;

import ca.bc.gov.mal.cirras.underwriting.data.models.CropVarietyInsurability;
import ca.bc.gov.mal.cirras.underwriting.data.models.CropVarietyInsurabilityList;
import ca.bc.gov.mal.cirras.underwriting.data.entities.CropVarietyInsPlantInsXrefDto;
import ca.bc.gov.mal.cirras.underwriting.data.entities.CropVarietyInsurabilityDto;
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
