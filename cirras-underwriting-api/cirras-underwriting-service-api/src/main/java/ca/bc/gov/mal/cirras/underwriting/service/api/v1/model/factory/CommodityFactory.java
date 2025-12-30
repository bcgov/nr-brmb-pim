package ca.bc.gov.mal.cirras.underwriting.services.model.factory;

import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.service.api.model.factory.FactoryException;

import java.util.List;

import ca.bc.gov.mal.cirras.underwriting.data.models.CropCommodity;
import ca.bc.gov.mal.cirras.underwriting.data.models.CropCommodityList;
import ca.bc.gov.mal.cirras.underwriting.data.models.CropVarietyPlantInsurability;
import ca.bc.gov.mal.cirras.underwriting.data.entities.CommodityTypeCodeDto;
import ca.bc.gov.mal.cirras.underwriting.data.entities.CropCommodityDto;
import ca.bc.gov.mal.cirras.underwriting.data.entities.CropVarietyDto;
import ca.bc.gov.mal.cirras.underwriting.data.entities.CropVarietyInsPlantInsXrefDto;


/**************************************************************
  This is used for all commodity related data (Crop Commodity, Crop Variety, Crop Types etc.)
 **************************************************************/
public interface CommodityFactory {

	CropCommodityList<? extends CropCommodity> getCropCommodityList(List<CropCommodityDto> dtos,
			List<CropVarietyDto> cropVarietyDtos, List<CommodityTypeCodeDto> commodityTypeCodeDtos,
			List<CropVarietyInsPlantInsXrefDto> cropVarietyInsPlantInsXrefDtos) throws FactoryException, DaoException;

	void updateDto(CropVarietyInsPlantInsXrefDto dto, CropVarietyPlantInsurability model) throws FactoryException;
	
}



