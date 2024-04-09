package ca.bc.gov.mal.cirras.underwriting.service.api.v1.model.factory;

import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.service.api.model.factory.FactoryException;

import java.util.List;

import ca.bc.gov.mal.cirras.underwriting.model.v1.CropCommodity;
import ca.bc.gov.mal.cirras.underwriting.model.v1.CropCommodityList;
import ca.bc.gov.mal.cirras.underwriting.model.v1.CropVarietyPlantInsurability;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.CommodityTypeCodeDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.CropCommodityDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.CropVarietyDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.CropVarietyInsPlantInsXrefDto;


/**************************************************************
  This is used for all commodity related data (Crop Commodity, Crop Variety, Crop Types etc.)
 **************************************************************/
public interface CommodityFactory {

	CropCommodityList<? extends CropCommodity> getCropCommodityList(List<CropCommodityDto> dtos,
			List<CropVarietyDto> cropVarietyDtos, List<CommodityTypeCodeDto> commodityTypeCodeDtos,
			List<CropVarietyInsPlantInsXrefDto> cropVarietyInsPlantInsXrefDtos) throws FactoryException, DaoException;

	void updateDto(CropVarietyInsPlantInsXrefDto dto, CropVarietyPlantInsurability model) throws FactoryException;
	
}



