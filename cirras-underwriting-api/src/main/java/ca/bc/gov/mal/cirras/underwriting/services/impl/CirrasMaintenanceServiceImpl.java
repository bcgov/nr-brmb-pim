package ca.bc.gov.mal.cirras.underwriting.services.impl;

import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.mal.cirras.underwriting.data.models.CropVarietyInsurability;
import ca.bc.gov.mal.cirras.underwriting.data.models.CropVarietyPlantInsurability;
import ca.bc.gov.mal.cirras.underwriting.data.models.GradeModifierType;
import ca.bc.gov.mal.cirras.underwriting.data.models.SeedingDeadline;
import ca.bc.gov.mal.cirras.underwriting.data.models.YieldMeasUnitConversion;
import ca.bc.gov.mal.cirras.underwriting.data.entities.CommodityTypeCodeDto;
import ca.bc.gov.mal.cirras.underwriting.data.entities.CropVarietyInsPlantInsXrefDto;
import ca.bc.gov.mal.cirras.underwriting.data.entities.CropVarietyInsurabilityDto;
import ca.bc.gov.mal.cirras.underwriting.data.entities.DeclaredYieldContractCommodityDto;
import ca.bc.gov.mal.cirras.underwriting.data.entities.DeclaredYieldContractCommodityForageDto;
import ca.bc.gov.mal.cirras.underwriting.data.entities.DeclaredYieldFieldDto;
import ca.bc.gov.mal.cirras.underwriting.data.entities.DeclaredYieldFieldForageDto;
import ca.bc.gov.mal.cirras.underwriting.data.entities.DeclaredYieldFieldRollupDto;
import ca.bc.gov.mal.cirras.underwriting.data.entities.DeclaredYieldFieldRollupForageDto;
import ca.bc.gov.mal.cirras.underwriting.data.entities.GradeModifierDto;
import ca.bc.gov.mal.cirras.underwriting.data.entities.GradeModifierTypeCodeDto;
import ca.bc.gov.mal.cirras.underwriting.data.entities.SeedingDeadlineDto;
import ca.bc.gov.mal.cirras.underwriting.data.entities.UnderwritingYearDto;
import ca.bc.gov.mal.cirras.underwriting.data.entities.YieldMeasUnitConversionDto;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.CommodityTypeCodeDao;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.CropVarietyInsPlantInsXrefDao;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.CropVarietyInsurabilityDao;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.DeclaredYieldContractCommodityDao;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.DeclaredYieldContractCommodityForageDao;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.DeclaredYieldFieldDao;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.DeclaredYieldFieldForageDao;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.DeclaredYieldFieldRollupDao;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.DeclaredYieldFieldRollupForageDao;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.GradeModifierDao;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.GradeModifierTypeCodeDao;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.SeedingDeadlineDao;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.UnderwritingYearDao;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.YieldMeasUnitConversionDao;
import ca.bc.gov.mal.cirras.underwriting.data.resources.CommodityTypeCodeListRsrc;
import ca.bc.gov.mal.cirras.underwriting.data.resources.CropVarietyInsurabilityListRsrc;
import ca.bc.gov.mal.cirras.underwriting.data.resources.GradeModifierListRsrc;
import ca.bc.gov.mal.cirras.underwriting.data.resources.GradeModifierRsrc;
import ca.bc.gov.mal.cirras.underwriting.data.resources.GradeModifierTypeListRsrc;
import ca.bc.gov.mal.cirras.underwriting.data.resources.SeedingDeadlineListRsrc;
import ca.bc.gov.mal.cirras.underwriting.data.resources.UnderwritingYearListRsrc;
import ca.bc.gov.mal.cirras.underwriting.data.resources.UnderwritingYearRsrc;
import ca.bc.gov.mal.cirras.underwriting.data.resources.YieldMeasUnitConversionListRsrc;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;
import ca.bc.gov.nrs.wfone.common.service.api.ConflictException;
import ca.bc.gov.nrs.wfone.common.service.api.ForbiddenException;
import ca.bc.gov.nrs.wfone.common.service.api.NotFoundException;
import ca.bc.gov.nrs.wfone.common.service.api.ServiceException;
import ca.bc.gov.nrs.wfone.common.service.api.ValidationFailureException;
import ca.bc.gov.nrs.wfone.common.service.api.model.factory.FactoryContext;
import ca.bc.gov.nrs.wfone.common.webade.authentication.WebAdeAuthentication;
import ca.bc.gov.mal.cirras.underwriting.services.CirrasMaintenanceService;
import ca.bc.gov.mal.cirras.underwriting.data.assemblers.CommodityTypeCodeRsrcFactory;
import ca.bc.gov.mal.cirras.underwriting.data.assemblers.CropVarietyInsurabilityRsrcFactory;
import ca.bc.gov.mal.cirras.underwriting.data.assemblers.CommodityRsrcFactory;
import ca.bc.gov.mal.cirras.underwriting.data.assemblers.GradeModifierRsrcFactory;
import ca.bc.gov.mal.cirras.underwriting.data.assemblers.GradeModifierTypeRsrcFactory;
import ca.bc.gov.mal.cirras.underwriting.data.assemblers.SeedingDeadlineRsrcFactory;
import ca.bc.gov.mal.cirras.underwriting.data.assemblers.UnderwritingYearRsrcFactory;
import ca.bc.gov.mal.cirras.underwriting.data.assemblers.YieldMeasUnitConversionRsrcFactory;
import ca.bc.gov.mal.cirras.underwriting.services.utils.InventoryServiceEnums.InsurancePlans;

public class CirrasMaintenanceServiceImpl implements CirrasMaintenanceService {

	private static final Logger logger = LoggerFactory.getLogger(CirrasMaintenanceServiceImpl.class);

	private Properties applicationProperties;

	// factories
	private CommodityTypeCodeRsrcFactory commodityTypeCodeRsrcFactory;
	private SeedingDeadlineRsrcFactory seedingDeadlineRsrcFactory;
	private UnderwritingYearRsrcFactory underwritingYearRsrcFactory;
	private GradeModifierRsrcFactory gradeModifierRsrcFactory;
	private GradeModifierTypeRsrcFactory gradeModifierTypeRsrcFactory;
	private CropVarietyInsurabilityRsrcFactory cropVarietyInsurabilityRsrcFactory;
	private CommodityRsrcFactory commodityRsrcFactory;
	private YieldMeasUnitConversionRsrcFactory yieldMeasUnitConversionRsrcFactory;

	// daos
	private CommodityTypeCodeDao commodityTypeCodeDao;
	private SeedingDeadlineDao seedingDeadlineDao;
	private UnderwritingYearDao underwritingYearDao;
	private GradeModifierDao gradeModifierDao;
	private GradeModifierTypeCodeDao gradeModifierTypeCodeDao;
	private CropVarietyInsurabilityDao cropVarietyInsurabilityDao;
	private CropVarietyInsPlantInsXrefDao cropVarietyInsPlantInsXrefDao;
	private YieldMeasUnitConversionDao yieldMeasUnitConversionDao;
	private DeclaredYieldContractCommodityDao declaredYieldContractCommodityDao;
	private DeclaredYieldFieldRollupDao declaredYieldFieldRollupDao;
	private DeclaredYieldFieldDao declaredYieldFieldDao;
	private DeclaredYieldContractCommodityForageDao declaredYieldContractCommodityForageDao;
	private DeclaredYieldFieldRollupForageDao declaredYieldFieldRollupForageDao;
	private DeclaredYieldFieldForageDao declaredYieldFieldForageDao;

	public static final String MaximumResultsProperty = "maximum.results";

	public static final int DefaultMaximumResults = 800;

	public void setApplicationProperties(Properties applicationProperties) {
		this.applicationProperties = applicationProperties;
	}
	
	public void setCommodityTypeCodeRsrcFactory(CommodityTypeCodeRsrcFactory commodityTypeCodeRsrcFactory) {
		this.commodityTypeCodeRsrcFactory = commodityTypeCodeRsrcFactory;
	}	
	
	public void setSeedingDeadlineRsrcFactory(SeedingDeadlineRsrcFactory seedingDeadlineRsrcFactory) {
		this.seedingDeadlineRsrcFactory = seedingDeadlineRsrcFactory;
	}
	
	public void setUnderwritingYearRsrcFactory(UnderwritingYearRsrcFactory underwritingYearRsrcFactory) {
		this.underwritingYearRsrcFactory = underwritingYearRsrcFactory;
	}
	
	public void setGradeModifierRsrcFactory(GradeModifierRsrcFactory gradeModifierRsrcFactory) {
		this.gradeModifierRsrcFactory = gradeModifierRsrcFactory;
	}
	
	public void setGradeModifierTypeRsrcFactory(GradeModifierTypeRsrcFactory gradeModifierTypeRsrcFactory) {
		this.gradeModifierTypeRsrcFactory = gradeModifierTypeRsrcFactory;
	}
	
	public void setCropVarietyInsurabilityRsrcFactory(CropVarietyInsurabilityRsrcFactory cropVarietyInsurabilityRsrcFactory) {
		this.cropVarietyInsurabilityRsrcFactory = cropVarietyInsurabilityRsrcFactory;
	}
	
	public void setCommodityRsrcFactory(CommodityRsrcFactory commodityRsrcFactory) {
		this.commodityRsrcFactory = commodityRsrcFactory;
	}
	
	public void setYieldMeasUnitConversionRsrcFactory(YieldMeasUnitConversionRsrcFactory yieldMeasUnitConversionRsrcFactory) {
		this.yieldMeasUnitConversionRsrcFactory = yieldMeasUnitConversionRsrcFactory;
	}

	public void setCommodityTypeCodeDao(CommodityTypeCodeDao commodityTypeCodeDao) {
		this.commodityTypeCodeDao = commodityTypeCodeDao;
	}
	
	public void setSeedingDeadlineDao(SeedingDeadlineDao seedingDeadlineDao) {
		this.seedingDeadlineDao = seedingDeadlineDao;
	}
	
	public void setUnderwritingYearDao(UnderwritingYearDao underwritingYearDao) {
		this.underwritingYearDao = underwritingYearDao;
	}

	public void setGradeModifierDao(GradeModifierDao gradeModifierDao) {
		this.gradeModifierDao = gradeModifierDao;
	}

	public void setGradeModifierTypeCodeDao(GradeModifierTypeCodeDao gradeModifierTypeCodeDao) {
		this.gradeModifierTypeCodeDao = gradeModifierTypeCodeDao;
	}

	public void setCropVarietyInsurabilityDao(CropVarietyInsurabilityDao cropVarietyInsurabilityDao) {
		this.cropVarietyInsurabilityDao = cropVarietyInsurabilityDao;
	}

	public void setCropVarietyInsPlantInsXrefDao(CropVarietyInsPlantInsXrefDao cropVarietyInsPlantInsXrefDao) {
		this.cropVarietyInsPlantInsXrefDao = cropVarietyInsPlantInsXrefDao;
	}

	public void setYieldMeasUnitConversionDao(YieldMeasUnitConversionDao yieldMeasUnitConversionDao) {
		this.yieldMeasUnitConversionDao = yieldMeasUnitConversionDao;
	}

	public void setDeclaredYieldContractCommodityDao(DeclaredYieldContractCommodityDao declaredYieldContractCommodityDao) {
		this.declaredYieldContractCommodityDao = declaredYieldContractCommodityDao;
	}

	public void setDeclaredYieldFieldRollupDao(DeclaredYieldFieldRollupDao declaredYieldFieldRollupDao) {
		this.declaredYieldFieldRollupDao = declaredYieldFieldRollupDao;
	}

	public void setDeclaredYieldFieldDao(DeclaredYieldFieldDao declaredYieldFieldDao) {
		this.declaredYieldFieldDao = declaredYieldFieldDao;
	}

	public void setDeclaredYieldContractCommodityForageDao(DeclaredYieldContractCommodityForageDao declaredYieldContractCommodityForageDao) {
		this.declaredYieldContractCommodityForageDao = declaredYieldContractCommodityForageDao;
	}
	
	public void setDeclaredYieldFieldRollupForageDao(DeclaredYieldFieldRollupForageDao declaredYieldFieldRollupForageDao) {
		this.declaredYieldFieldRollupForageDao = declaredYieldFieldRollupForageDao;
	}

	public void setDeclaredYieldFieldForageDao(DeclaredYieldFieldForageDao declaredYieldFieldForageDao) {
		this.declaredYieldFieldForageDao = declaredYieldFieldForageDao;
	}
		
	@Override
	public CommodityTypeCodeListRsrc getCommodityTypeCodeList(
			Integer insurancePlanId,
			FactoryContext context,
			WebAdeAuthentication authentication) throws ServiceException {

		logger.debug("<getCommodityTypeCodeList");
		
		CommodityTypeCodeListRsrc results = null;
		
		try {
			
			List<CommodityTypeCodeDto> dtos = commodityTypeCodeDao.selectByCropCommodityPlan(insurancePlanId);
			
			results = commodityTypeCodeRsrcFactory.getCommodityTypeCodeList(dtos, insurancePlanId, context, authentication);
			
		} catch (DaoException e) {
			throw new ServiceException("DAO threw an exception", e);
		}
		
		logger.debug(">getCommodityTypeCodeList");

		return results;

	}

	@Override
	public SeedingDeadlineListRsrc saveSeedingDeadlines(
			SeedingDeadlineListRsrc seedingDeadlines, 
			Integer cropYear,
			FactoryContext factoryContext,
			WebAdeAuthentication authentication
			) throws ServiceException, NotFoundException, ValidationFailureException {
		
		SeedingDeadlineListRsrc result = null;
		String userId = getUserId(authentication);

		try {

			if(seedingDeadlines != null && seedingDeadlines.getCollection().size() > 0) {
				for (SeedingDeadline seedingDeadline : seedingDeadlines.getCollection()) {
					if ( Boolean.TRUE.equals(seedingDeadline.getDeletedByUserInd())) {
						//delete
						deleteSeedingDeadline(seedingDeadline);
					} else {
						//insert or update
						updateSeedingDeadline(seedingDeadline, userId);
					}
				}
			}
			
			result = getSeedingDeadlines(cropYear, factoryContext, authentication);
			
		} catch (DaoException e) {
			throw new ServiceException("DAO threw an exception", e);
		}
		
		
		return result;
		
	}
	
	private void deleteSeedingDeadline(SeedingDeadline seedingDeadline)
			throws NotFoundDaoException, DaoException {
		logger.debug("<deleteSeedingDeadline");

		if(seedingDeadline.getSeedingDeadlineGuid() != null) {
			seedingDeadlineDao.delete(seedingDeadline.getSeedingDeadlineGuid());
		}

		logger.debug(">deleteSeedingDeadline");
	}
	
	private void updateSeedingDeadline(SeedingDeadline seedingDeadline, String userId) throws DaoException {

		logger.debug("<updateSeedingDeadline");

		SeedingDeadlineDto dto = null;

		if (seedingDeadline.getSeedingDeadlineGuid() != null) {
			dto = seedingDeadlineDao.fetch(seedingDeadline.getSeedingDeadlineGuid());
		}

		if (dto == null) {
			// Insert if it doesn't exist
			insertSeedingDeadline(seedingDeadline, userId);
		} else {

			seedingDeadlineRsrcFactory.updateDto(dto, seedingDeadline);

			seedingDeadlineDao.update(dto, userId);
		}
		
		logger.debug(">updateSeedingDeadline");

	}
	
	private String insertSeedingDeadline(SeedingDeadline seedingDeadline, String userId) throws DaoException {

		logger.debug("<insertSeedingDeadline");

		SeedingDeadlineDto dto = new SeedingDeadlineDto();
		seedingDeadlineRsrcFactory.updateDto(dto, seedingDeadline);

		dto.setSeedingDeadlineGuid(null);

		seedingDeadlineDao.insert(dto, userId);
		
		logger.debug(">insertSeedingDeadline");

		return dto.getSeedingDeadlineGuid();
	}

	@Override
	public SeedingDeadlineListRsrc getSeedingDeadlines(
			Integer cropYear,
			FactoryContext factoryContext, 
			WebAdeAuthentication authentication
			) throws ServiceException, NotFoundException {

		logger.debug("<getSeedingDeadlines");

		SeedingDeadlineListRsrc result = null;
		
		try {
			List<SeedingDeadlineDto> dtos = seedingDeadlineDao.selectByYear(cropYear);
			result = seedingDeadlineRsrcFactory.getSeedingDeadlineList(dtos, cropYear, factoryContext, authentication);

		} catch (DaoException e) {
			throw new ServiceException("DAO threw an exception", e);
		}

		logger.debug(">getSeedingDeadlines");

		return result;
	}
	

	@Override
	public UnderwritingYearRsrc createUnderwritingYear(
			UnderwritingYearRsrc underwritingYear, 
			FactoryContext factoryContext,
			WebAdeAuthentication authentication
		) throws ServiceException, NotFoundException, ValidationFailureException {

		logger.debug("<createUnderwritingYear");
		
		UnderwritingYearRsrc result = null;
		String userId = getUserId(authentication);
		
		try {

			UnderwritingYearDto dto = underwritingYearDao.selectByCropYear(underwritingYear.getCropYear());
			
			if(dto == null) {
				
				dto = new UnderwritingYearDto();
				underwritingYearRsrcFactory.updateDto(dto, underwritingYear);

				dto.setUnderwritingYearGuid(null);

				underwritingYearDao.insert(dto, userId);
				
				result = getUnderwritingYear(dto.getCropYear(), factoryContext, authentication);
				
			} else {
				throw new ServiceException("Underwriting Year " + underwritingYear.getCropYear() + " already exists");
			}

		} catch (DaoException e) {
			throw new ServiceException("DAO threw an exception", e);
		}
		
		logger.debug(">createUnderwritingYear");
		
		return result;

	}

	@Override
	public UnderwritingYearRsrc getUnderwritingYear(
			Integer cropYear, 
			FactoryContext factoryContext,
			WebAdeAuthentication authentication
		) throws ServiceException, NotFoundException {
		
		logger.debug("<getUnderwritingYear");

		UnderwritingYearRsrc result = null;

		try {
			
			UnderwritingYearDto dto = underwritingYearDao.selectByCropYear(cropYear);
			

			if (dto == null) {
				throw new NotFoundException("Did not find underwriting year: " + cropYear);
			}
			
			result = underwritingYearRsrcFactory.getUnderwritingYear(dto, factoryContext, authentication);
			
		} catch (DaoException e) {
			throw new ServiceException("DAO threw an exception", e);
		}

		logger.debug(">getUnderwritingYear");
		return result;	
	}

	@Override
	public void deleteUnderwritingYear(
			Integer cropYear, 
			String optimisticLock, 
			WebAdeAuthentication authentication
		) throws ServiceException, NotFoundException, ForbiddenException, ConflictException {
		
		logger.debug("<deleteUnderwritingYear");

		try {
			UnderwritingYearDto dto = underwritingYearDao.selectByCropYear(cropYear);
			
			if(dto != null) {
				underwritingYearDao.delete(dto.getUnderwritingYearGuid());
			}
		} catch (DaoException e) {
			throw new ServiceException("DAO threw an exception", e);
		}

		logger.debug(">deleteUnderwritingYear");
		
	}

	@Override
	public UnderwritingYearListRsrc getUnderwritingYearList(
			FactoryContext factoryContext,
			WebAdeAuthentication authentication
		) throws ServiceException, NotFoundException, ForbiddenException, ConflictException {

		logger.debug("<getUnderwritingYearList");

		UnderwritingYearListRsrc result = null;
		
		try {
			List<UnderwritingYearDto> dtos = underwritingYearDao.fetchAll();
			result = underwritingYearRsrcFactory.getUnderwritingYearList(dtos, factoryContext, authentication);

		} catch (DaoException e) {
			throw new ServiceException("DAO threw an exception", e);
		}

		logger.debug(">getUnderwritingYearList");

		return result;
	}
	
	@Override
	public GradeModifierListRsrc getGradeModifierList(
			Integer cropYear, 
			Integer insurancePlanId, 
			Integer cropCommodityId,
			FactoryContext context, 
			WebAdeAuthentication authentication) throws ServiceException {

		logger.debug("<getGradeModifierList");

		GradeModifierListRsrc result = null;

		try {
			List<GradeModifierDto> dtos = gradeModifierDao.selectByYearPlanCommodity(cropYear, insurancePlanId, cropCommodityId);
			result = gradeModifierRsrcFactory.getGradeModifierList(dtos, cropYear, insurancePlanId, cropCommodityId, context,
					authentication);

		} catch (DaoException e) {
			throw new ServiceException("DAO threw an exception", e);
		}

		logger.debug(">getGradeModifierList");

		return result;
	}
	
	@Override
	public GradeModifierListRsrc saveGradeModifiers(
			Integer cropYear, 
			Integer insurancePlanId, 
			Integer cropCommodityId,
			GradeModifierListRsrc gradeModifiers, 
			FactoryContext factoryContext,
			WebAdeAuthentication authentication
			) throws ServiceException, NotFoundException, ValidationFailureException {
		
		GradeModifierListRsrc result = null;
		String userId = getUserId(authentication);

		try {

			if(gradeModifiers != null && gradeModifiers.getCollection().size() > 0) {
				for (GradeModifierRsrc gradeModifier : gradeModifiers.getCollection()) {
					if ( Boolean.TRUE.equals(gradeModifier.getDeletedByUserInd())) {
						//delete
						deleteGradeModifier(gradeModifier);
					} else {
						//insert or update
						updateGradeModifier(gradeModifier, userId);
					}
				}
			} else {
				throw new ServiceException("Empty grade modifier list");
			}
			
			result = getGradeModifierList(cropYear, insurancePlanId, cropCommodityId, factoryContext, authentication);
			
		} catch (DaoException e) {
			throw new ServiceException("DAO threw an exception", e);
		}
		
		
		return result;
		
	}	

	private void deleteGradeModifier(GradeModifierRsrc gradeModifier)
			throws NotFoundDaoException, DaoException {
		logger.debug("<deleteGradeModifier");

		if(gradeModifier.getGradeModifierGuid() != null) {
			gradeModifierDao.delete(gradeModifier.getGradeModifierGuid());
		}

		logger.debug(">deleteGradeModifier");
	}
	
	private void updateGradeModifier(GradeModifierRsrc gradeModifier, String userId) throws DaoException {

		logger.debug("<updateGradeModifier");

		GradeModifierDto dto = null;

		if (gradeModifier.getGradeModifierGuid() != null) {
			dto = gradeModifierDao.fetch(gradeModifier.getGradeModifierGuid());
		}

		if (dto == null) {
			// Insert if it doesn't exist
			insertGradeModifier(gradeModifier, userId);
		} else {

			gradeModifierRsrcFactory.updateDto(dto, gradeModifier);

			gradeModifierDao.update(dto, userId);
		}
		
		logger.debug(">updateGradeModifier");

	}
	
	private String insertGradeModifier(GradeModifierRsrc gradeModifier, String userId) throws DaoException {

		logger.debug("<insertGradeModifier");

		GradeModifierDto dto = new GradeModifierDto();
		gradeModifierRsrcFactory.updateDto(dto, gradeModifier);

		dto.setGradeModifierGuid(null);

		gradeModifierDao.insert(dto, userId);
		
		logger.debug(">insertGradeModifier");

		return dto.getGradeModifierGuid();
	}

	@Override
	public GradeModifierTypeListRsrc getGradeModifierTypeList(
			Integer cropYear,
			FactoryContext context,
			WebAdeAuthentication authentication
		) throws ServiceException {

		logger.debug("<getGradeModifierTypeList");

		GradeModifierTypeListRsrc result = null;

		try {
			List<GradeModifierTypeCodeDto> dtos = gradeModifierTypeCodeDao.select(cropYear);
			result = gradeModifierTypeRsrcFactory.getGradeModifierTypeList(dtos, cropYear, context,
					authentication);

		} catch (DaoException e) {
			throw new ServiceException("DAO threw an exception", e);
		}

		logger.debug(">getGradeModifierTypeList");

		return result;
	}

	@Override
	public GradeModifierTypeListRsrc saveGradeModifierTypes(
			Integer cropYear,
			GradeModifierTypeListRsrc gradeModifierTypes, 
			FactoryContext factoryContext,
			WebAdeAuthentication authentication
		)
			throws ServiceException, NotFoundException, ValidationFailureException {

		logger.debug("<saveGradeModifierTypes");

		GradeModifierTypeListRsrc result = null;
		String userId = getUserId(authentication);

		try {

			if(gradeModifierTypes != null && gradeModifierTypes.getCollection().size() > 0) {
				for (GradeModifierType gradeModifierType : gradeModifierTypes.getCollection()) {
					if ( Boolean.TRUE.equals(gradeModifierType.getDeletedByUserInd())) {
						//delete
						deleteGradeModifierType(gradeModifierType);
					} else {
						//insert or update
						updateGradeModifierType(gradeModifierType, userId);
					}
				}
			} else {
				throw new ServiceException("Empty grade modifier type list");
			}
			
			result = getGradeModifierTypeList(cropYear, factoryContext, authentication);
			
		} catch (DaoException e) {
			throw new ServiceException("DAO threw an exception", e);
		}
		
		logger.debug(">saveGradeModifierTypes");

		return result;
		
	}	

	private void deleteGradeModifierType(GradeModifierType gradeModifierType)
			throws NotFoundDaoException, DaoException {
		logger.debug("<deleteGradeModifierType");

		if(gradeModifierType.getGradeModifierTypeCode() != null) {
			gradeModifierTypeCodeDao.delete(gradeModifierType.getGradeModifierTypeCode());
		}

		logger.debug(">deleteGradeModifierType");
	}
	
	private void updateGradeModifierType(GradeModifierType gradeModifierType, String userId) throws DaoException {

		logger.debug("<updateGradeModifierType");

		GradeModifierTypeCodeDto dto = null;

		if (gradeModifierType.getGradeModifierTypeCode() != null) {
			dto = gradeModifierTypeCodeDao.fetch(gradeModifierType.getGradeModifierTypeCode());
		}

		if (dto == null) {
			// Insert if it doesn't exist
			insertGradeModifierType(gradeModifierType, userId);
		} else {

			gradeModifierTypeRsrcFactory.updateDto(dto, gradeModifierType);

			gradeModifierTypeCodeDao.update(dto, userId);
		}
		
		logger.debug(">updateGradeModifierType");

	}
	
	private void insertGradeModifierType(GradeModifierType gradeModifierType, String userId) throws DaoException {

		logger.debug("<insertGradeModifierType");

		GradeModifierTypeCodeDto dto = new GradeModifierTypeCodeDto();
		gradeModifierTypeRsrcFactory.updateDto(dto, gradeModifierType);

		gradeModifierTypeCodeDao.insert(dto, userId);
		
		logger.debug(">insertGradeModifierType");
	}
	
	@Override
	public CropVarietyInsurabilityListRsrc getCropVarietyInsurabilities(
			Integer insurancePlanId, 
			Boolean loadForEdit, 
			FactoryContext context, 
			WebAdeAuthentication authentication
		) throws ServiceException {
		
		logger.debug("<getCropVarietyInsurabilities");

		CropVarietyInsurabilityListRsrc result = null;

		try {
			List<CropVarietyInsurabilityDto> cropVarietyInsurabilityDtos = cropVarietyInsurabilityDao.selectForInsurancePlan(insurancePlanId);
			
			List<CropVarietyInsPlantInsXrefDto> cropVarietyInsPlantInsXrefDtos = cropVarietyInsPlantInsXrefDao.selectForInsurancePlan(insurancePlanId);

			List<CropVarietyInsurabilityDto> validationDtos = null;
			if(Boolean.TRUE.equals(loadForEdit)) {
				validationDtos = cropVarietyInsurabilityDao.selectValidation(insurancePlanId);
			}

			result = cropVarietyInsurabilityRsrcFactory.getCropVarietyInsurabilityList(
					cropVarietyInsurabilityDtos, 
					validationDtos,
					cropVarietyInsPlantInsXrefDtos,
					insurancePlanId,
					loadForEdit, 
					context,
					authentication);

		} catch (DaoException e) {
			throw new ServiceException("DAO threw an exception", e);
		}

		logger.debug(">getCropVarietyInsurabilities");

		return result;
	}

	@Override
	public CropVarietyInsurabilityListRsrc saveCropVarietyInsurabilities(
			Integer insurancePlanId, 
			Boolean loadForEdit,
			CropVarietyInsurabilityListRsrc cropVarietyInsurabilities,
			FactoryContext factoryContext, 
			WebAdeAuthentication authentication
		) throws ServiceException, NotFoundException, ValidationFailureException {
		
		logger.debug("<saveCropVarietyInsurabilities");

		CropVarietyInsurabilityListRsrc result = null;
		String userId = getUserId(authentication);

		try {

			//Load the edit eligible flags to determine if removing insurability is ok  
			List<CropVarietyInsurabilityDto> validationDtos = cropVarietyInsurabilityDao.selectValidation(insurancePlanId);
			List<CropVarietyInsPlantInsXrefDto> validationPlantInsXrefDtos = cropVarietyInsPlantInsXrefDao.selectForInsurancePlan(insurancePlanId);

			
			if(cropVarietyInsurabilities != null && cropVarietyInsurabilities.getCollection().size() > 0) {
				
				for (CropVarietyInsurability cropVarietyInsurability : cropVarietyInsurabilities.getCollection()) {
					if ( Boolean.TRUE.equals(cropVarietyInsurability.getDeletedByUserInd())) {
						//delete
						deleteCropVarietyInsurability(cropVarietyInsurability);
					} else {
						//insert or update
						updateCropVarietyInsurability(cropVarietyInsurability, validationDtos, validationPlantInsXrefDtos, userId);
					}
				}
			} else {
				throw new ServiceException("Empty crop variety insurability list");
			}
			
			result = getCropVarietyInsurabilities(insurancePlanId, loadForEdit, factoryContext, authentication);
			
		} catch (DaoException e) {
			throw new ServiceException("DAO threw an exception", e);
		}
		
		logger.debug(">saveCropVarietyInsurabilities");

		return result;
	}


	private void deleteCropVarietyInsurability(CropVarietyInsurability cropVarietyInsurability)
			throws NotFoundDaoException, DaoException {
		logger.debug("<deleteCropVarietyInsurability");

		if(cropVarietyInsurability.getCropVarietyInsurabilityGuid() != null) {
			//Delete plant insurability xref first
			cropVarietyInsPlantInsXrefDao.deleteForVariety(cropVarietyInsurability.getCropVarietyInsurabilityGuid());
			
			cropVarietyInsurabilityDao.delete(cropVarietyInsurability.getCropVarietyId());
		}

		logger.debug(">deleteCropVarietyInsurability");
	}
	
	private void updateCropVarietyInsurability(
			CropVarietyInsurability cropVarietyInsurability, 
			List<CropVarietyInsurabilityDto> validationDtos,
			List<CropVarietyInsPlantInsXrefDto> validationPlantInsXrefDtos,
			String userId
		) throws DaoException {

		logger.debug("<updateCropVarietyInsurability");

		CropVarietyInsurabilityDto dto = null;

		if (cropVarietyInsurability.getCropVarietyInsurabilityGuid() != null) {
			dto = cropVarietyInsurabilityDao.fetch(cropVarietyInsurability.getCropVarietyId());
		}

		if (dto == null) {
			// Insert if it doesn't exist
			insertCropVarietyInsurability(cropVarietyInsurability, userId);
		} else {
			
			validateInsurabilityChanges(dto, cropVarietyInsurability, validationDtos);

			cropVarietyInsurabilityRsrcFactory.updateDto(dto, cropVarietyInsurability);

			cropVarietyInsurabilityDao.update(dto, userId);

		}
		
		updateCropVarietyPlantInsurability(cropVarietyInsurability, validationPlantInsXrefDtos, userId);
		
		logger.debug(">updateCropVarietyInsurability");

	}

	private void validateInsurabilityChanges(
			CropVarietyInsurabilityDto dto,
			CropVarietyInsurability cropVarietyInsurability,
			List<CropVarietyInsurabilityDto> validationDtos) 
	{
		logger.debug("<validateInsurabilityChanges");

		//Check if insurability has been removed and it's allowed.
		//Adding insurability is always allowed but removing it, only if it's nowhere used.
		
		CropVarietyInsurabilityDto validationDto = getCropVarietyInsurabilityDto(validationDtos, cropVarietyInsurability.getCropVarietyId());
		
		//Quantity Insurability
		if(dto.getIsQuantityInsurableInd() == true && 
				cropVarietyInsurability.getIsQuantityInsurableInd() == false &&
				validationDto.getIsQuantityInsurableEditableInd() == false) {
			throw new ServiceException("Quantity Insurability can't be removed for: " + cropVarietyInsurability.getCropVarietyId()); 
		}
		
		//Unseeded Insurability
		if(dto.getIsUnseededInsurableInd() == true && 
				cropVarietyInsurability.getIsUnseededInsurableInd() == false &&
				validationDto.getIsUnseededInsurableEditableInd() == false) {
			throw new ServiceException("Unseeded Insurability can't be removed for: " + cropVarietyInsurability.getCropVarietyId()); 
		}
		
		//AWP Eligibility
		if(dto.getIsAwpEligibleInd() == true && 
				cropVarietyInsurability.getIsAwpEligibleInd() == false &&
				validationDto.getIsAwpEligibleEditableInd() == false) {
			throw new ServiceException("AWP Eligibility can't be removed for: " + cropVarietyInsurability.getCropVarietyId()); 
		}
		
		//Underseeding Eligibility
		if(dto.getIsUnderseedingEligibleInd() == true && 
				cropVarietyInsurability.getIsUnderseedingEligibleInd() == false &&
				validationDto.getIsUnderseedingEligibleEditableInd() == false) {
			throw new ServiceException("Underseeding Eligibility can't be removed for: " + cropVarietyInsurability.getCropVarietyId()); 
		}
		
		//Plant Insurability
		if(dto.getIsPlantInsurableInd() == true && 
				cropVarietyInsurability.getIsPlantInsurableInd() == false &&
				validationDto.getIsPlantInsurableEditableInd() == false) {
			throw new ServiceException("Plant Insurability can't be removed for: " + cropVarietyInsurability.getCropVarietyId()); 
		}
		
		logger.debug(">validateInsurabilityChanges");

	}
	
	private CropVarietyInsurabilityDto getCropVarietyInsurabilityDto(List<CropVarietyInsurabilityDto> dtos, Integer varietyId) {
		
		List<CropVarietyInsurabilityDto> filteredDtos = dtos.stream().filter(x -> x.getCropVarietyId().equals(varietyId)).collect(Collectors.toList());
		
		if(filteredDtos != null && filteredDtos.size() == 1) {
			return filteredDtos.get(0);
		}
		return null;
	}
	
	private CropVarietyInsPlantInsXrefDto getCropVarietyInsPlantInsXrefDto(List<CropVarietyInsPlantInsXrefDto> dtos, CropVarietyPlantInsurability plantInsurability) {

		List<CropVarietyInsPlantInsXrefDto> filteredDtos = dtos.stream()
				.filter(x -> x.getCropVarietyInsurabilityGuid().equals(plantInsurability.getCropVarietyInsurabilityGuid()) &&
						x.getPlantInsurabilityTypeCode().equals(plantInsurability.getPlantInsurabilityTypeCode())).collect(Collectors.toList());
		
		if(filteredDtos != null && filteredDtos.size() == 1) {
			return filteredDtos.get(0);
		}
		return null;
	}
	
	private void updateCropVarietyPlantInsurability(CropVarietyInsurability cropVarietyInsurability, List<CropVarietyInsPlantInsXrefDto> validationDtos, String userId)
			throws DaoException, NotFoundDaoException {

		logger.debug("<updateCropVarietyPlantInsurability");

		//Update plant insurability xref first
		if(cropVarietyInsurability.getCropVarietyPlantInsurabilities() != null && cropVarietyInsurability.getCropVarietyPlantInsurabilities().size() > 0) {
			for (CropVarietyPlantInsurability plantInsurability : cropVarietyInsurability.getCropVarietyPlantInsurabilities()) {

				//plantInsurability.getCropVarietyInsurabilityGuid could be null if a new crop variety insurability record is created 
				if(plantInsurability.getCropVarietyInsurabilityGuid() == null) {
					plantInsurability.setCropVarietyInsurabilityGuid(cropVarietyInsurability.getCropVarietyInsurabilityGuid());
				}
				
				//Check if it exists and insert if it doesn't exist 
				CropVarietyInsPlantInsXrefDto xrefDto = cropVarietyInsPlantInsXrefDao.fetch(plantInsurability.getCropVarietyInsurabilityGuid(), plantInsurability.getPlantInsurabilityTypeCode());

				if(Boolean.TRUE.equals(plantInsurability.getDeletedByUserInd()) && xrefDto != null) {
					
					validateCropVarietyPlantInsurabilityChanges(plantInsurability, validationDtos, cropVarietyInsurability.getVarietyName());
					
					//Delete record if it exists
					cropVarietyInsPlantInsXrefDao.delete(plantInsurability.getCropVarietyInsurabilityGuid(), plantInsurability.getPlantInsurabilityTypeCode());
					
				} else if (xrefDto == null) {
						
					//Insert record if it doesn't exist yet
					xrefDto = new CropVarietyInsPlantInsXrefDto();
					commodityRsrcFactory.updateDto(xrefDto, plantInsurability);

					cropVarietyInsPlantInsXrefDao.insert(xrefDto, userId);

				}
			}
		}
		
		logger.debug("<updateCropVarietyPlantInsurability");

	}
	
	private void validateCropVarietyPlantInsurabilityChanges(
			CropVarietyPlantInsurability plantInsurability,
			List<CropVarietyInsPlantInsXrefDto> validationDtos,
			String varietyName) 
	{
		CropVarietyInsPlantInsXrefDto validationDto = getCropVarietyInsPlantInsXrefDto(validationDtos, plantInsurability);
		
		//plant insurability can't be removed from the variety because it's used in inventory
		if(validationDto != null && validationDto.getIsUsedInd() == true) {
			throw new ServiceException("Plant Insurability " + plantInsurability.getPlantInsurabilityTypeCode() + " can't be removed from " + varietyName); 
		}
		
	}

	
	private void insertCropVarietyInsurability(CropVarietyInsurability cropVarietyInsurability, String userId) throws DaoException {

		logger.debug("<insertCropVarietyInsurability");

		CropVarietyInsurabilityDto dto = new CropVarietyInsurabilityDto();
		cropVarietyInsurabilityRsrcFactory.updateDto(dto, cropVarietyInsurability);

		cropVarietyInsurabilityDao.insert(dto, userId);
		
		cropVarietyInsurability.setCropVarietyInsurabilityGuid(dto.getCropVarietyInsurabilityGuid());
		
		logger.debug(">insertCropVarietyInsurability");
	}
	
	@Override
	public 	YieldMeasUnitConversionListRsrc getYieldMeasUnitConversions(
			Integer insurancePlanId,
			String srcYieldMeasUnitTypeCode,
			String targetYieldMeasUnitTypeCode,
			FactoryContext context, 
			WebAdeAuthentication authentication
	) throws ServiceException {
		
		logger.debug("<getYieldMeasUnitConversions");

		YieldMeasUnitConversionListRsrc result = null;

		try {
			List<YieldMeasUnitConversionDto> yieldMeasUnitConversionDtos = yieldMeasUnitConversionDao.selectLatestVersionByPlan(
					insurancePlanId, 
					srcYieldMeasUnitTypeCode, 
					targetYieldMeasUnitTypeCode);
			
			result = yieldMeasUnitConversionRsrcFactory.getYieldMeasUnitConversionList(
					yieldMeasUnitConversionDtos, 
					insurancePlanId,
					srcYieldMeasUnitTypeCode,
					targetYieldMeasUnitTypeCode,
					context,
					authentication);

		} catch (DaoException e) {
			throw new ServiceException("DAO threw an exception", e);
		}

		logger.debug(">getYieldMeasUnitConversions");

		return result;
	}	
	
	@Override
	public YieldMeasUnitConversionListRsrc saveYieldMeasUnitConversions(
			YieldMeasUnitConversionListRsrc yieldMeasUnitConversions,
			Integer insurancePlanId, 
			String srcYieldMeasUnitTypeCode, 
			String targetYieldMeasUnitTypeCode,
			FactoryContext factoryContext, 
			WebAdeAuthentication webAdeAuthentication
		) throws ServiceException, NotFoundException, ValidationFailureException {
		
		logger.debug("<saveYieldMeasUnitConversions");

		YieldMeasUnitConversionListRsrc result = null;
		String userId = getUserId(webAdeAuthentication);

		try {
			if(yieldMeasUnitConversions != null && yieldMeasUnitConversions.getCollection().size() > 0) {
				
				for (YieldMeasUnitConversion yieldMeasUnitConversion : yieldMeasUnitConversions.getCollection()) {
					if ( Boolean.TRUE.equals(yieldMeasUnitConversion.getDeletedByUserInd())) {
						//delete
						deleteYieldMeasUnitConversion(yieldMeasUnitConversion);
					} else {
						//insert or update
						updateYieldMeasUnitConversion(insurancePlanId, yieldMeasUnitConversion, userId);
					}
				}
				
			} else {
				throw new ServiceException("Empty crop yield meas unit conversion list");
			}
			
			result = getYieldMeasUnitConversions(insurancePlanId, srcYieldMeasUnitTypeCode, targetYieldMeasUnitTypeCode, factoryContext, webAdeAuthentication);
			
		} catch (DaoException e) {
			throw new ServiceException("DAO threw an exception", e);
		}
		
		logger.debug(">saveYieldMeasUnitConversions");

		return result;		
		
	}

	private void deleteYieldMeasUnitConversion(YieldMeasUnitConversion yieldMeasUnitConversion)
			throws NotFoundDaoException, DaoException {
		logger.debug("<deleteYieldMeasUnitConversion");

		if(yieldMeasUnitConversion.getYieldMeasUnitConversionGuid() != null) {
			
			yieldMeasUnitConversionDao.delete(yieldMeasUnitConversion.getYieldMeasUnitConversionGuid());
		}

		logger.debug(">deleteYieldMeasUnitConversion");
	}
	
	private void updateYieldMeasUnitConversion(
			Integer insurancePlanId,
			YieldMeasUnitConversion yieldMeasUnitConversion, 
			String userId
		) throws DaoException {

		logger.debug("<updateYieldMeasUnitConversion");

		YieldMeasUnitConversionDto dto = null;

		if (yieldMeasUnitConversion.getYieldMeasUnitConversionGuid() != null) {
			dto = yieldMeasUnitConversionDao.fetch(yieldMeasUnitConversion.getYieldMeasUnitConversionGuid());
		}

		if (dto == null) {
			// Insert if it doesn't exist
			insertYieldMeasUnitConversion(yieldMeasUnitConversion, userId);
		} else {
			
			//Throw an error if conversion factor is null for an existing conversion record
			if(yieldMeasUnitConversion.getConversionFactor() == null) {
				throw new ServiceException("Conversion Factor can't be null for an existing conversion. Commodity: " + yieldMeasUnitConversion.getCommodityName());
			} else {

				//Only save and recalculate existing yield if the conversion factor changed
				if(Double.compare(dto.getConversionFactor(), yieldMeasUnitConversion.getConversionFactor()) != 0){
					
					Double oldConversionFactor = dto.getConversionFactor();
					
					yieldMeasUnitConversionRsrcFactory.updateDto(dto, yieldMeasUnitConversion);

					yieldMeasUnitConversionDao.update(dto, userId);
					
					//Recalculate existing Yield
					recalculateYield(insurancePlanId, yieldMeasUnitConversion, oldConversionFactor, userId);
				}
			}

		}
		
		logger.debug(">updateYieldMeasUnitConversion");

	}

	private void recalculateYield(Integer insurancePlanId, YieldMeasUnitConversion yieldMeasUnitConversion, Double oldConversionFactor, String userId) throws DaoException {
		logger.debug("<recalculateYield");
		
		if ( InsurancePlans.GRAIN.getInsurancePlanId().equals(insurancePlanId) ) {
			recalculateYieldGrain(yieldMeasUnitConversion, userId);
		} else if ( InsurancePlans.FORAGE.getInsurancePlanId().equals(insurancePlanId) ) {
			recalculateYieldForage(yieldMeasUnitConversion, oldConversionFactor, userId);
		}
		
		logger.debug(">recalculateYield");
		
	}

	private void recalculateYieldForage(YieldMeasUnitConversion yieldMeasUnitConversion, Double oldConversionFactor, String userId) throws DaoException {
		logger.debug("<recalculateYieldForage");
		
		//Get contract commodity totals
		List<DeclaredYieldContractCommodityForageDto> dyccDto = declaredYieldContractCommodityForageDao.selectToRecalculate(
				yieldMeasUnitConversion.getCropCommodityId(), 
				yieldMeasUnitConversion.getTargetYieldMeasUnitTypeCode(),
				yieldMeasUnitConversion.getEffectiveCropYear(),
				yieldMeasUnitConversion.getExpiryCropYear()
				);
		
		//Recalculate commodity totals
		if(dyccDto != null && dyccDto.size() > 0) {
			for (DeclaredYieldContractCommodityForageDto dto : dyccDto) {
				//Convert it into entered units with the old conversion factor
				dto.setQuantityHarvestedTons(calculateYieldMeasUnitConversion(oldConversionFactor, dto.getQuantityHarvestedTons(), true));
				//Convert it into default units with the new conversion factor
				dto.setQuantityHarvestedTons(calculateYieldMeasUnitConversion(yieldMeasUnitConversion.getConversionFactor(), dto.getQuantityHarvestedTons(), false));
				
				//Set yield per acre
				Double yieldPerAcre = (double)0;
				if(dto.getHarvestedAcres() != null && dto.getHarvestedAcres() > 0 && dto.getQuantityHarvestedTons() != null) {
					yieldPerAcre = dto.getQuantityHarvestedTons() / dto.getHarvestedAcres();
				}
				dto.setYieldPerAcre(yieldPerAcre);

				declaredYieldContractCommodityForageDao.update(dto, userId);
			}
		}
		
		//Get rollup
		List<DeclaredYieldFieldRollupForageDto> dyrDto = declaredYieldFieldRollupForageDao.selectToRecalculate(
				yieldMeasUnitConversion.getCropCommodityId(), 
				yieldMeasUnitConversion.getTargetYieldMeasUnitTypeCode(),
				yieldMeasUnitConversion.getEffectiveCropYear(),
				yieldMeasUnitConversion.getExpiryCropYear()
				);
		
		//Recalculate yield rollup
		if(dyrDto != null && dyrDto.size() > 0) {
			for (DeclaredYieldFieldRollupForageDto dto : dyrDto) {
				//Convert it into entered units with the old conversion factor
				dto.setQuantityHarvestedTons(calculateYieldMeasUnitConversion(oldConversionFactor, dto.getQuantityHarvestedTons(), true));
				//Convert it into default units with the new conversion factor
				dto.setQuantityHarvestedTons(calculateYieldMeasUnitConversion(yieldMeasUnitConversion.getConversionFactor(), dto.getQuantityHarvestedTons(), false));
				
				//Set yield per acre
				Double yieldPerAcre = (double)0;
				if(dto.getHarvestedAcres() != null && dto.getHarvestedAcres() > 0) {
					yieldPerAcre = dto.getQuantityHarvestedTons() / dto.getHarvestedAcres();
				}
				dto.setYieldPerAcre(yieldPerAcre);
				
				declaredYieldFieldRollupForageDao.update(dto, userId);
			}
		}		

		//Get Fields
		List<DeclaredYieldFieldForageDto> dyfDto = declaredYieldFieldForageDao.selectToRecalculate(
				yieldMeasUnitConversion.getCropCommodityId(), 
				yieldMeasUnitConversion.getTargetYieldMeasUnitTypeCode(),
				yieldMeasUnitConversion.getEffectiveCropYear(),
				yieldMeasUnitConversion.getExpiryCropYear()
				);
		
		//Recalculate
		if(dyfDto != null && dyfDto.size() > 0) {
			for (DeclaredYieldFieldForageDto dto : dyfDto) {
				dto.setWeightDefaultUnit(calculateYieldMeasUnitConversion(yieldMeasUnitConversion.getConversionFactor(), dto.getWeight(), false));
				declaredYieldFieldForageDao.update(dto, userId);
			}
		}
		
		logger.debug(">recalculateYieldForage");
		
	}
	
	
	private void recalculateYieldGrain(YieldMeasUnitConversion yieldMeasUnitConversion, String userId) throws DaoException {
		logger.debug("<recalculateYieldGrain");
		
		//Get contract commodities
		List<DeclaredYieldContractCommodityDto> dyccDto = declaredYieldContractCommodityDao.selectToRecalculate(
				yieldMeasUnitConversion.getCropCommodityId(), 
				yieldMeasUnitConversion.getTargetYieldMeasUnitTypeCode(),
				yieldMeasUnitConversion.getEffectiveCropYear(),
				yieldMeasUnitConversion.getExpiryCropYear()
				);
		
		//Recalculate
		if(dyccDto != null && dyccDto.size() > 0) {
			for (DeclaredYieldContractCommodityDto dto : dyccDto) {
				dto.setStoredYieldDefaultUnit(calculateYieldMeasUnitConversion(yieldMeasUnitConversion.getConversionFactor(), dto.getStoredYield(), false));
				dto.setSoldYieldDefaultUnit(calculateYieldMeasUnitConversion(yieldMeasUnitConversion.getConversionFactor(), dto.getSoldYield(), false));
				declaredYieldContractCommodityDao.update(dto, userId);
			}
		}


		//Get Fields
		List<DeclaredYieldFieldDto> dyfDto = declaredYieldFieldDao.selectToRecalculate(
				yieldMeasUnitConversion.getCropCommodityId(), 
				yieldMeasUnitConversion.getTargetYieldMeasUnitTypeCode(),
				yieldMeasUnitConversion.getEffectiveCropYear(),
				yieldMeasUnitConversion.getExpiryCropYear()
				);
		
		//Recalculate
		if(dyfDto != null && dyfDto.size() > 0) {
			for (DeclaredYieldFieldDto dto : dyfDto) {
				dto.setEstimatedYieldPerAcreDefaultUnit(calculateYieldMeasUnitConversion(yieldMeasUnitConversion.getConversionFactor(), dto.getEstimatedYieldPerAcre(), false));
				declaredYieldFieldDao.update(dto, userId);
			}
		}
		
		//Get field rollups
		List<DeclaredYieldFieldRollupDto> dyfrDto = declaredYieldFieldRollupDao.selectToRecalculate(
				yieldMeasUnitConversion.getCropCommodityId(), 
				yieldMeasUnitConversion.getEffectiveCropYear(),
				yieldMeasUnitConversion.getExpiryCropYear()
				);
		
		//Recalculate
		if(dyfrDto != null && dyfrDto.size() > 0) {
			for (DeclaredYieldFieldRollupDto dto : dyfrDto) {
				//multiply = true if source units is default units
				if(dto.getEnteredYieldMeasUnitTypeCode().equals(yieldMeasUnitConversion.getSrcYieldMeasUnitTypeCode())) {
					dto.setEstimatedYieldPerAcreBushels(calculateYieldMeasUnitConversion(yieldMeasUnitConversion.getConversionFactor(), dto.getEstimatedYieldPerAcreTonnes(), true));
				} else {
					dto.setEstimatedYieldPerAcreTonnes(calculateYieldMeasUnitConversion(yieldMeasUnitConversion.getConversionFactor(), dto.getEstimatedYieldPerAcreBushels(), false));
				}
				declaredYieldFieldRollupDao.update(dto, userId);
			}
		}
		
		logger.debug(">recalculateYieldGrain");
		
	}
	
	// Calculate srcValue converted to the target yield meas unit specified by conversionFactor
	// If srcValue is null, targetValue is null.
	// Otherwise the calculated targetValue is returned.
	private Double calculateYieldMeasUnitConversion(Double conversionFactor, Double srcValue, boolean multiply) {

		//multiply = true if source units is default units
		
		// Calculate value in target units.
		Double targetValue = null;

		if (srcValue != null) {
			if (multiply) {
				targetValue = srcValue * conversionFactor;
			} else {
				targetValue = srcValue / conversionFactor;
			}
		}

		return targetValue;
	}

	private void insertYieldMeasUnitConversion(YieldMeasUnitConversion yieldMeasUnitConversion, String userId) throws DaoException {

		logger.debug("<insertYieldMeasUnitConversion");
		
		//Only insert if there is a conversion factor
		if(yieldMeasUnitConversion.getConversionFactor() != null) {
			YieldMeasUnitConversionDto dto = new YieldMeasUnitConversionDto();
			yieldMeasUnitConversionRsrcFactory.updateDto(dto, yieldMeasUnitConversion);

			yieldMeasUnitConversionDao.insert(dto, userId);
			
			yieldMeasUnitConversion.setYieldMeasUnitConversionGuid(dto.getYieldMeasUnitConversionGuid());

			logger.debug("New record created");
		}
		
		logger.debug(">insertYieldMeasUnitConversion");
	}

	private String getUserId(WebAdeAuthentication authentication) {
		String userId = "DEFAULT_USERID";

		if (authentication != null) {
			userId = authentication.getUserId();
			authentication.getClientId();
		}

		return userId;
	}

}
