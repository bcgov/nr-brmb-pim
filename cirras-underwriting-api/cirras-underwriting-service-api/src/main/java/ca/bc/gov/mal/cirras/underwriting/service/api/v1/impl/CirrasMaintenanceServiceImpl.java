package ca.bc.gov.mal.cirras.underwriting.service.api.v1.impl;

import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.mal.cirras.underwriting.model.v1.CommodityTypeCode;
import ca.bc.gov.mal.cirras.underwriting.model.v1.CommodityTypeCodeList;
import ca.bc.gov.mal.cirras.underwriting.model.v1.CropVarietyInsurability;
import ca.bc.gov.mal.cirras.underwriting.model.v1.CropVarietyInsurabilityList;
import ca.bc.gov.mal.cirras.underwriting.model.v1.CropVarietyPlantInsurability;
import ca.bc.gov.mal.cirras.underwriting.model.v1.DopYieldContractCommodityForage;
import ca.bc.gov.mal.cirras.underwriting.model.v1.GradeModifier;
import ca.bc.gov.mal.cirras.underwriting.model.v1.GradeModifierList;
import ca.bc.gov.mal.cirras.underwriting.model.v1.GradeModifierType;
import ca.bc.gov.mal.cirras.underwriting.model.v1.GradeModifierTypeList;
import ca.bc.gov.mal.cirras.underwriting.model.v1.SeedingDeadline;
import ca.bc.gov.mal.cirras.underwriting.model.v1.SeedingDeadlineList;
import ca.bc.gov.mal.cirras.underwriting.model.v1.UnderwritingYear;
import ca.bc.gov.mal.cirras.underwriting.model.v1.UnderwritingYearList;
import ca.bc.gov.mal.cirras.underwriting.model.v1.YieldMeasUnitConversion;
import ca.bc.gov.mal.cirras.underwriting.model.v1.YieldMeasUnitConversionList;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.CommodityTypeCodeDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.CropVarietyInsPlantInsXrefDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.CropVarietyInsurabilityDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.DeclaredYieldContractCommodityDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.DeclaredYieldContractCommodityForageDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.DeclaredYieldFieldDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.DeclaredYieldFieldForageDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.DeclaredYieldFieldRollupDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.GradeModifierDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.GradeModifierTypeCodeDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.SeedingDeadlineDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.UnderwritingYearDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.YieldMeasUnitConversionDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.CommodityTypeCodeDao;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.CropVarietyInsPlantInsXrefDao;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.CropVarietyInsurabilityDao;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.DeclaredYieldContractCommodityDao;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.DeclaredYieldContractCommodityForageDao;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.DeclaredYieldFieldDao;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.DeclaredYieldFieldForageDao;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.DeclaredYieldFieldRollupDao;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.GradeModifierDao;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.GradeModifierTypeCodeDao;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.SeedingDeadlineDao;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.UnderwritingYearDao;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.YieldMeasUnitConversionDao;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;
import ca.bc.gov.nrs.wfone.common.service.api.ConflictException;
import ca.bc.gov.nrs.wfone.common.service.api.ForbiddenException;
import ca.bc.gov.nrs.wfone.common.service.api.NotFoundException;
import ca.bc.gov.nrs.wfone.common.service.api.ServiceException;
import ca.bc.gov.nrs.wfone.common.service.api.ValidationFailureException;
import ca.bc.gov.nrs.wfone.common.service.api.model.factory.FactoryContext;
import ca.bc.gov.nrs.wfone.common.webade.authentication.WebAdeAuthentication;
import ca.bc.gov.mal.cirras.underwriting.service.api.v1.CirrasMaintenanceService;
import ca.bc.gov.mal.cirras.underwriting.service.api.v1.model.factory.CommodityTypeCodeFactory;
import ca.bc.gov.mal.cirras.underwriting.service.api.v1.model.factory.CropVarietyInsurabilityFactory;
import ca.bc.gov.mal.cirras.underwriting.service.api.v1.model.factory.CommodityFactory;
import ca.bc.gov.mal.cirras.underwriting.service.api.v1.model.factory.GradeModifierFactory;
import ca.bc.gov.mal.cirras.underwriting.service.api.v1.model.factory.GradeModifierTypeFactory;
import ca.bc.gov.mal.cirras.underwriting.service.api.v1.model.factory.SeedingDeadlineFactory;
import ca.bc.gov.mal.cirras.underwriting.service.api.v1.model.factory.UnderwritingYearFactory;
import ca.bc.gov.mal.cirras.underwriting.service.api.v1.model.factory.YieldMeasUnitConversionFactory;
import ca.bc.gov.mal.cirras.underwriting.service.api.v1.util.InventoryServiceEnums.InsurancePlans;
import ca.bc.gov.mal.cirras.underwriting.service.api.v1.validation.ModelValidator;


public class CirrasMaintenanceServiceImpl implements CirrasMaintenanceService {

	private static final Logger logger = LoggerFactory.getLogger(CirrasMaintenanceServiceImpl.class);

	private Properties applicationProperties;

	private ModelValidator modelValidator;

	// factories
	private CommodityTypeCodeFactory commodityTypeCodeFactory;
	private SeedingDeadlineFactory seedingDeadlineFactory;
	private UnderwritingYearFactory underwritingYearFactory;
	private GradeModifierFactory gradeModifierFactory;
	private GradeModifierTypeFactory gradeModifierTypeFactory;
	private CropVarietyInsurabilityFactory cropVarietyInsurabilityFactory;
	private CommodityFactory commodityFactory;
	private YieldMeasUnitConversionFactory yieldMeasUnitConversionFactory;

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
	private DeclaredYieldFieldForageDao declaredYieldFieldForageDao;

	public static final String MaximumResultsProperty = "maximum.results";

	public static final int DefaultMaximumResults = 800;

	public void setApplicationProperties(Properties applicationProperties) {
		this.applicationProperties = applicationProperties;
	}

	public void setModelValidator(ModelValidator modelValidator) {
		this.modelValidator = modelValidator;
	}
	
	public void setCommodityTypeCodeFactory(CommodityTypeCodeFactory commodityTypeCodeFactory) {
		this.commodityTypeCodeFactory = commodityTypeCodeFactory;
	}	
	
	public void setSeedingDeadlineFactory(SeedingDeadlineFactory seedingDeadlineFactory) {
		this.seedingDeadlineFactory = seedingDeadlineFactory;
	}
	
	public void setUnderwritingYearFactory(UnderwritingYearFactory underwritingYearFactory) {
		this.underwritingYearFactory = underwritingYearFactory;
	}
	
	public void setGradeModifierFactory(GradeModifierFactory gradeModifierFactory) {
		this.gradeModifierFactory = gradeModifierFactory;
	}
	
	public void setGradeModifierTypeFactory(GradeModifierTypeFactory gradeModifierTypeFactory) {
		this.gradeModifierTypeFactory = gradeModifierTypeFactory;
	}
	
	public void setCropVarietyInsurabilityFactory(CropVarietyInsurabilityFactory cropVarietyInsurabilityFactory) {
		this.cropVarietyInsurabilityFactory = cropVarietyInsurabilityFactory;
	}
	
	public void setCommodityFactory(CommodityFactory commodityFactory) {
		this.commodityFactory = commodityFactory;
	}
	
	public void setYieldMeasUnitConversionFactory(YieldMeasUnitConversionFactory yieldMeasUnitConversionFactory) {
		this.yieldMeasUnitConversionFactory = yieldMeasUnitConversionFactory;
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

	public void setDeclaredYieldFieldForageDao(DeclaredYieldFieldForageDao declaredYieldFieldForageDao) {
		this.declaredYieldFieldForageDao = declaredYieldFieldForageDao;
	}
		
	@Override
	public CommodityTypeCodeList<? extends CommodityTypeCode> getCommodityTypeCodeList(
			Integer insurancePlanId,
			FactoryContext context,
			WebAdeAuthentication authentication) throws ServiceException {

		logger.debug("<getCommodityTypeCodeList");
		
		CommodityTypeCodeList<? extends CommodityTypeCode> results = null;
		
		try {
			
			List<CommodityTypeCodeDto> dtos = commodityTypeCodeDao.selectByCropCommodityPlan(insurancePlanId);
			
			results = commodityTypeCodeFactory.getCommodityTypeCodeList(dtos, insurancePlanId, context, authentication);
			
		} catch (DaoException e) {
			throw new ServiceException("DAO threw an exception", e);
		}
		
		logger.debug(">getCommodityTypeCodeList");

		return results;

	}

	@Override
	public SeedingDeadlineList<? extends SeedingDeadline> saveSeedingDeadlines(
			SeedingDeadlineList<? extends SeedingDeadline> seedingDeadlines, 
			Integer cropYear,
			FactoryContext factoryContext,
			WebAdeAuthentication authentication
			) throws ServiceException, NotFoundException, ValidationFailureException {
		
		SeedingDeadlineList<? extends SeedingDeadline> result = null;
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

			seedingDeadlineFactory.updateDto(dto, seedingDeadline);

			seedingDeadlineDao.update(dto, userId);
		}
		
		logger.debug(">updateSeedingDeadline");

	}
	
	private String insertSeedingDeadline(SeedingDeadline seedingDeadline, String userId) throws DaoException {

		logger.debug("<insertSeedingDeadline");

		SeedingDeadlineDto dto = new SeedingDeadlineDto();
		seedingDeadlineFactory.updateDto(dto, seedingDeadline);

		dto.setSeedingDeadlineGuid(null);

		seedingDeadlineDao.insert(dto, userId);
		
		logger.debug(">insertSeedingDeadline");

		return dto.getSeedingDeadlineGuid();
	}

	@Override
	public SeedingDeadlineList<? extends SeedingDeadline> getSeedingDeadlines(
			Integer cropYear,
			FactoryContext factoryContext, 
			WebAdeAuthentication authentication
			) throws ServiceException, NotFoundException {

		logger.debug("<getSeedingDeadlines");

		SeedingDeadlineList<? extends SeedingDeadline> result = null;
		
		try {
			List<SeedingDeadlineDto> dtos = seedingDeadlineDao.selectByYear(cropYear);
			result = seedingDeadlineFactory.getSeedingDeadlineList(dtos, cropYear, factoryContext, authentication);

		} catch (DaoException e) {
			throw new ServiceException("DAO threw an exception", e);
		}

		logger.debug(">getSeedingDeadlines");

		return result;
	}
	

	@Override
	public UnderwritingYear createUnderwritingYear(
			UnderwritingYear underwritingYear, 
			FactoryContext factoryContext,
			WebAdeAuthentication authentication
		) throws ServiceException, NotFoundException, ValidationFailureException {

		logger.debug("<createUnderwritingYear");
		
		UnderwritingYear result = null;
		String userId = getUserId(authentication);
		
		try {

			UnderwritingYearDto dto = underwritingYearDao.selectByCropYear(underwritingYear.getCropYear());
			
			if(dto == null) {
				
				dto = new UnderwritingYearDto();
				underwritingYearFactory.updateDto(dto, underwritingYear);

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
	public UnderwritingYear getUnderwritingYear(
			Integer cropYear, 
			FactoryContext factoryContext,
			WebAdeAuthentication authentication
		) throws ServiceException, NotFoundException {
		
		logger.debug("<getUnderwritingYear");

		UnderwritingYear result = null;

		try {
			
			UnderwritingYearDto dto = underwritingYearDao.selectByCropYear(cropYear);
			

			if (dto == null) {
				throw new NotFoundException("Did not find underwriting year: " + cropYear);
			}
			
			result = underwritingYearFactory.getUnderwritingYear(dto, factoryContext, authentication);
			
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
	public UnderwritingYearList<? extends UnderwritingYear> getUnderwritingYearList(
			FactoryContext factoryContext,
			WebAdeAuthentication authentication
		) throws ServiceException, NotFoundException, ForbiddenException, ConflictException {

		logger.debug("<getUnderwritingYearList");

		UnderwritingYearList<? extends UnderwritingYear> result = null;
		
		try {
			List<UnderwritingYearDto> dtos = underwritingYearDao.fetchAll();
			result = underwritingYearFactory.getUnderwritingYearList(dtos, factoryContext, authentication);

		} catch (DaoException e) {
			throw new ServiceException("DAO threw an exception", e);
		}

		logger.debug(">getUnderwritingYearList");

		return result;
	}
	
	@Override
	public GradeModifierList<? extends GradeModifier> getGradeModifierList(
			Integer cropYear, 
			Integer insurancePlanId, 
			Integer cropCommodityId,
			FactoryContext context, 
			WebAdeAuthentication authentication) throws ServiceException {

		logger.debug("<getGradeModifierList");

		GradeModifierList<? extends GradeModifier> result = null;

		try {
			List<GradeModifierDto> dtos = gradeModifierDao.selectByYearPlanCommodity(cropYear, insurancePlanId, cropCommodityId);
			result = gradeModifierFactory.getGradeModifierList(dtos, cropYear, insurancePlanId, cropCommodityId, context,
					authentication);

		} catch (DaoException e) {
			throw new ServiceException("DAO threw an exception", e);
		}

		logger.debug(">getGradeModifierList");

		return result;
	}
	
	@Override
	public GradeModifierList<? extends GradeModifier> saveGradeModifiers(
			Integer cropYear, 
			Integer insurancePlanId, 
			Integer cropCommodityId,
			GradeModifierList<? extends GradeModifier> gradeModifiers, 
			FactoryContext factoryContext,
			WebAdeAuthentication authentication
			) throws ServiceException, NotFoundException, ValidationFailureException {
		
		GradeModifierList<? extends GradeModifier> result = null;
		String userId = getUserId(authentication);

		try {

			if(gradeModifiers != null && gradeModifiers.getCollection().size() > 0) {
				for (GradeModifier gradeModifier : gradeModifiers.getCollection()) {
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

	private void deleteGradeModifier(GradeModifier gradeModifier)
			throws NotFoundDaoException, DaoException {
		logger.debug("<deleteGradeModifier");

		if(gradeModifier.getGradeModifierGuid() != null) {
			gradeModifierDao.delete(gradeModifier.getGradeModifierGuid());
		}

		logger.debug(">deleteGradeModifier");
	}
	
	private void updateGradeModifier(GradeModifier gradeModifier, String userId) throws DaoException {

		logger.debug("<updateGradeModifier");

		GradeModifierDto dto = null;

		if (gradeModifier.getGradeModifierGuid() != null) {
			dto = gradeModifierDao.fetch(gradeModifier.getGradeModifierGuid());
		}

		if (dto == null) {
			// Insert if it doesn't exist
			insertGradeModifier(gradeModifier, userId);
		} else {

			gradeModifierFactory.updateDto(dto, gradeModifier);

			gradeModifierDao.update(dto, userId);
		}
		
		logger.debug(">updateGradeModifier");

	}
	
	private String insertGradeModifier(GradeModifier gradeModifier, String userId) throws DaoException {

		logger.debug("<insertGradeModifier");

		GradeModifierDto dto = new GradeModifierDto();
		gradeModifierFactory.updateDto(dto, gradeModifier);

		dto.setGradeModifierGuid(null);

		gradeModifierDao.insert(dto, userId);
		
		logger.debug(">insertGradeModifier");

		return dto.getGradeModifierGuid();
	}

	@Override
	public GradeModifierTypeList<? extends GradeModifierType> getGradeModifierTypeList(
			Integer cropYear,
			FactoryContext context,
			WebAdeAuthentication authentication
		) throws ServiceException {

		logger.debug("<getGradeModifierTypeList");

		GradeModifierTypeList<? extends GradeModifierType> result = null;

		try {
			List<GradeModifierTypeCodeDto> dtos = gradeModifierTypeCodeDao.select(cropYear);
			result = gradeModifierTypeFactory.getGradeModifierTypeList(dtos, cropYear, context,
					authentication);

		} catch (DaoException e) {
			throw new ServiceException("DAO threw an exception", e);
		}

		logger.debug(">getGradeModifierTypeList");

		return result;
	}

	@Override
	public GradeModifierTypeList<? extends GradeModifierType> saveGradeModifierTypes(
			Integer cropYear,
			GradeModifierTypeList<? extends GradeModifierType> gradeModifierTypes, 
			FactoryContext factoryContext,
			WebAdeAuthentication authentication
		)
			throws ServiceException, NotFoundException, ValidationFailureException {

		logger.debug("<saveGradeModifierTypes");

		GradeModifierTypeList<? extends GradeModifierType> result = null;
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

			gradeModifierTypeFactory.updateDto(dto, gradeModifierType);

			gradeModifierTypeCodeDao.update(dto, userId);
		}
		
		logger.debug(">updateGradeModifierType");

	}
	
	private void insertGradeModifierType(GradeModifierType gradeModifierType, String userId) throws DaoException {

		logger.debug("<insertGradeModifierType");

		GradeModifierTypeCodeDto dto = new GradeModifierTypeCodeDto();
		gradeModifierTypeFactory.updateDto(dto, gradeModifierType);

		gradeModifierTypeCodeDao.insert(dto, userId);
		
		logger.debug(">insertGradeModifierType");
	}
	
	@Override
	public CropVarietyInsurabilityList<? extends CropVarietyInsurability> getCropVarietyInsurabilities(
			Integer insurancePlanId, 
			Boolean loadForEdit, 
			FactoryContext context, 
			WebAdeAuthentication authentication
		) throws ServiceException {
		
		logger.debug("<getCropVarietyInsurabilities");

		CropVarietyInsurabilityList<? extends CropVarietyInsurability> result = null;

		try {
			List<CropVarietyInsurabilityDto> cropVarietyInsurabilityDtos = cropVarietyInsurabilityDao.selectForInsurancePlan(insurancePlanId);
			
			List<CropVarietyInsPlantInsXrefDto> cropVarietyInsPlantInsXrefDtos = cropVarietyInsPlantInsXrefDao.selectForInsurancePlan(insurancePlanId);

			List<CropVarietyInsurabilityDto> validationDtos = null;
			if(Boolean.TRUE.equals(loadForEdit)) {
				validationDtos = cropVarietyInsurabilityDao.selectValidation(insurancePlanId);
			}

			result = cropVarietyInsurabilityFactory.getCropVarietyInsurabilityList(
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
	public CropVarietyInsurabilityList<? extends CropVarietyInsurability> saveCropVarietyInsurabilities(
			Integer insurancePlanId, 
			Boolean loadForEdit,
			CropVarietyInsurabilityList<? extends CropVarietyInsurability> cropVarietyInsurabilities,
			FactoryContext factoryContext, 
			WebAdeAuthentication authentication
		) throws ServiceException, NotFoundException, ValidationFailureException {
		
		logger.debug("<saveCropVarietyInsurabilities");

		CropVarietyInsurabilityList<? extends CropVarietyInsurability> result = null;
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

			cropVarietyInsurabilityFactory.updateDto(dto, cropVarietyInsurability);

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
					commodityFactory.updateDto(xrefDto, plantInsurability);

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
		cropVarietyInsurabilityFactory.updateDto(dto, cropVarietyInsurability);

		cropVarietyInsurabilityDao.insert(dto, userId);
		
		cropVarietyInsurability.setCropVarietyInsurabilityGuid(dto.getCropVarietyInsurabilityGuid());
		
		logger.debug(">insertCropVarietyInsurability");
	}
	
	@Override
	public 	YieldMeasUnitConversionList<? extends YieldMeasUnitConversion> getYieldMeasUnitConversions(
			Integer insurancePlanId,
			String srcYieldMeasUnitTypeCode,
			String targetYieldMeasUnitTypeCode,
			FactoryContext context, 
			WebAdeAuthentication authentication
	) throws ServiceException {
		
		logger.debug("<getYieldMeasUnitConversions");

		YieldMeasUnitConversionList<? extends YieldMeasUnitConversion> result = null;

		try {
			List<YieldMeasUnitConversionDto> yieldMeasUnitConversionDtos = yieldMeasUnitConversionDao.selectLatestVersionByPlan(
					insurancePlanId, 
					srcYieldMeasUnitTypeCode, 
					targetYieldMeasUnitTypeCode);
			
			result = yieldMeasUnitConversionFactory.getYieldMeasUnitConversionList(
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
	public YieldMeasUnitConversionList<? extends YieldMeasUnitConversion> saveYieldMeasUnitConversions(
			YieldMeasUnitConversionList<? extends YieldMeasUnitConversion> yieldMeasUnitConversions,
			Integer insurancePlanId, 
			String srcYieldMeasUnitTypeCode, 
			String targetYieldMeasUnitTypeCode,
			FactoryContext factoryContext, 
			WebAdeAuthentication webAdeAuthentication
		) throws ServiceException, NotFoundException, ValidationFailureException {
		
		logger.debug("<saveYieldMeasUnitConversions");

		YieldMeasUnitConversionList<? extends YieldMeasUnitConversion> result = null;
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
					
					yieldMeasUnitConversionFactory.updateDto(dto, yieldMeasUnitConversion);

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
		
		//Get contract commodities
		List<DeclaredYieldContractCommodityForageDto> dyccDto = declaredYieldContractCommodityForageDao.selectToRecalculate(
				yieldMeasUnitConversion.getCropCommodityId(), 
				yieldMeasUnitConversion.getTargetYieldMeasUnitTypeCode(),
				yieldMeasUnitConversion.getEffectiveCropYear(),
				yieldMeasUnitConversion.getExpiryCropYear()
				);
		
		//Recalculate
		if(dyccDto != null && dyccDto.size() > 0) {
			for (DeclaredYieldContractCommodityForageDto dto : dyccDto) {
				//Convert it into entered units with the old conversion factor
				dto.setQuantityHarvestedTons(calculateYieldMeasUnitConversion(oldConversionFactor, dto.getQuantityHarvestedTons(), true));
				//Convert it into default units with the new conversion factor
				dto.setQuantityHarvestedTons(calculateYieldMeasUnitConversion(yieldMeasUnitConversion.getConversionFactor(), dto.getQuantityHarvestedTons(), false));
				
				//Set yield per acre if harvested tons is not overwritten
				if(dto.getQuantityHarvestedTonsOverride() == null) {
					//Take override harvested acres if it's not null
					Double harvestedAcresForCalculation = dto.getHarvestedAcresOverride() == null ? dto.getHarvestedAcres() : dto.getHarvestedAcresOverride();

					Double yieldPerAcre = (double)0;
					if(harvestedAcresForCalculation > 0) {
						yieldPerAcre = dto.getQuantityHarvestedTons() / harvestedAcresForCalculation;
					}
					dto.setYieldPerAcre(yieldPerAcre);
				}
				
				declaredYieldContractCommodityForageDao.update(dto, userId);
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
			yieldMeasUnitConversionFactory.updateDto(dto, yieldMeasUnitConversion);

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
