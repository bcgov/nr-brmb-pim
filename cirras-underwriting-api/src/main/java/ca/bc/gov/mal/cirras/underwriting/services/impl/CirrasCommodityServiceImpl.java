package ca.bc.gov.mal.cirras.underwriting.services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.mal.cirras.underwriting.data.entities.CommodityTypeCodeDto;
import ca.bc.gov.mal.cirras.underwriting.data.entities.CropCommodityDto;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.CommodityTypeCodeDao;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.CropCommodityDao;
import ca.bc.gov.mal.cirras.underwriting.data.entities.CropVarietyDto;
import ca.bc.gov.mal.cirras.underwriting.data.entities.CropVarietyInsPlantInsXrefDto;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.CropVarietyDao;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.CropVarietyInsPlantInsXrefDao;
import ca.bc.gov.mal.cirras.underwriting.data.resources.CropCommodityListRsrc;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.service.api.ServiceException;
import ca.bc.gov.nrs.wfone.common.service.api.model.factory.FactoryContext;
import ca.bc.gov.nrs.wfone.common.webade.authentication.WebAdeAuthentication;
import ca.bc.gov.mal.cirras.underwriting.services.CirrasCommodityService;
import ca.bc.gov.mal.cirras.underwriting.data.assemblers.CommodityRsrcFactory;

public class CirrasCommodityServiceImpl implements CirrasCommodityService {

	private static final Logger logger = LoggerFactory.getLogger(CirrasCommodityServiceImpl.class);

	private Properties applicationProperties;

	// factories
	private CommodityRsrcFactory commodityRsrcFactory;
	
	// daos
	private CropCommodityDao cropCommodityDao;
	private CropVarietyDao cropVarietyDao;
	private CommodityTypeCodeDao commodityTypeCodeDao;
	private CropVarietyInsPlantInsXrefDao cropVarietyInsPlantInsXrefDao;

	public void setApplicationProperties(Properties applicationProperties) {
		this.applicationProperties = applicationProperties;
	}

	public void setCommodityRsrcFactory(CommodityRsrcFactory commodityRsrcFactory) {
		this.commodityRsrcFactory = commodityRsrcFactory;
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
	public CropCommodityListRsrc getCropCommodityList(
			Integer insurancePlanId, 
			Integer cropYear,
	    	String commodityType,
	    	Boolean loadChildren,
			FactoryContext context, 
			WebAdeAuthentication authentication
	) 
	throws ServiceException {

		logger.debug("<getCropCommodityList");
		
		CropCommodityListRsrc results = null;
		
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
		
			results = commodityRsrcFactory.getCropCommodityList(dtos, cropVarietyDtos, commodityTypeCodeDtos, cropVarietyInsPlantInsXrefDtos);
			
		} catch (DaoException e) {
			throw new ServiceException("DAO threw an exception", e);
		}
		
		logger.debug(">getCropCommodityList");

		return results;

	}
	
}


