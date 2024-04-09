package ca.bc.gov.mal.cirras.underwriting.service.api.v1.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.mal.cirras.underwriting.model.v1.CropCommodity;
import ca.bc.gov.mal.cirras.underwriting.model.v1.CropCommodityList;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.CommodityTypeCodeDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.CropCommodityDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.CommodityTypeCodeDao;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.CropCommodityDao;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.CropVarietyDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.CropVarietyInsPlantInsXrefDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.CropVarietyDao;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.CropVarietyInsPlantInsXrefDao;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.service.api.ServiceException;
import ca.bc.gov.nrs.wfone.common.service.api.model.factory.FactoryContext;
import ca.bc.gov.nrs.wfone.common.webade.authentication.WebAdeAuthentication;
import ca.bc.gov.mal.cirras.underwriting.service.api.v1.CirrasCommodityService;
import ca.bc.gov.mal.cirras.underwriting.service.api.v1.model.factory.CommodityFactory;
import ca.bc.gov.mal.cirras.underwriting.service.api.v1.validation.ModelValidator;


public class CirrasCommodityServiceImpl implements CirrasCommodityService {

	private static final Logger logger = LoggerFactory.getLogger(CirrasCommodityServiceImpl.class);

	private Properties applicationProperties;

	private ModelValidator modelValidator;

	// factories
	private CommodityFactory commodityFactory;
	
	// daos
	private CropCommodityDao cropCommodityDao;
	private CropVarietyDao cropVarietyDao;
	private CommodityTypeCodeDao commodityTypeCodeDao;
	private CropVarietyInsPlantInsXrefDao cropVarietyInsPlantInsXrefDao;

	public void setApplicationProperties(Properties applicationProperties) {
		this.applicationProperties = applicationProperties;
	}

	public void setModelValidator(ModelValidator modelValidator) {
		this.modelValidator = modelValidator;
	}

	public void setCommodityFactory(CommodityFactory commodityFactory) {
		this.commodityFactory = commodityFactory;
	}

	public void setCropCommodityDao(CropCommodityDao cropCommodityDao) {
		this.cropCommodityDao = cropCommodityDao;
	}
	
	public void setCommodityTypeCodeDao(CommodityTypeCodeDao commodityTypeCodeDao) {
		this.commodityTypeCodeDao = commodityTypeCodeDao;
	}
	
	public void setCropVarietyInsPlantInsXrefDao(CropVarietyInsPlantInsXrefDao cropVarietyInsPlantInsXrefDao) {
		this.cropVarietyInsPlantInsXrefDao = cropVarietyInsPlantInsXrefDao;
	}
	
	public void setCropVarietyDao(CropVarietyDao cropVarietyDao) {
		this.cropVarietyDao = cropVarietyDao;
	}
	
	@Override
	public CropCommodityList<? extends CropCommodity> getCropCommodityList(
			Integer insurancePlanId, 
			Integer cropYear,
	    	String commodityType,
	    	Boolean loadChildren,
			FactoryContext context, 
			WebAdeAuthentication authentication
	) 
	throws ServiceException {

		logger.debug("<getCropCommodityList");
		
		CropCommodityList<? extends CropCommodity> results = null;
		
		try {
			
			List<CropCommodityDto> dtos = cropCommodityDao.select(
					insurancePlanId,
					cropYear, 
					commodityType);
			
			List<CropVarietyDto> cropVarietyDtos = new ArrayList<CropVarietyDto>();
			List<CommodityTypeCodeDto> commodityTypeCodeDtos = new ArrayList<CommodityTypeCodeDto>(); 
			List<CropVarietyInsPlantInsXrefDto> cropVarietyInsPlantInsXrefDtos = new ArrayList<CropVarietyInsPlantInsXrefDto>();
			
			if(loadChildren) {
				
				cropVarietyDtos = cropVarietyDao.select(
						insurancePlanId);
				
				commodityTypeCodeDtos = commodityTypeCodeDao.selectByPlan(insurancePlanId, cropYear);
				
				cropVarietyInsPlantInsXrefDtos = cropVarietyInsPlantInsXrefDao.selectPlantInsForCropVarieties(null);
			}
		
			results = commodityFactory.getCropCommodityList(dtos, cropVarietyDtos, commodityTypeCodeDtos, cropVarietyInsPlantInsXrefDtos);
			
		} catch (DaoException e) {
			throw new ServiceException("DAO threw an exception", e);
		}
		
		logger.debug(">getCropCommodityList");

		return results;

	}
	
}


