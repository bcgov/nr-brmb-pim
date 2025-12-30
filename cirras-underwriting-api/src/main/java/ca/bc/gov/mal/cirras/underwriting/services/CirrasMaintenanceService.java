package ca.bc.gov.mal.cirras.underwriting.services;

import org.springframework.transaction.annotation.Transactional;

import ca.bc.gov.mal.cirras.underwriting.data.models.CommodityTypeCode;
import ca.bc.gov.mal.cirras.underwriting.data.models.CommodityTypeCodeList;
import ca.bc.gov.mal.cirras.underwriting.data.models.CropVarietyInsurability;
import ca.bc.gov.mal.cirras.underwriting.data.models.CropVarietyInsurabilityList;
import ca.bc.gov.mal.cirras.underwriting.data.models.GradeModifier;
import ca.bc.gov.mal.cirras.underwriting.data.models.GradeModifierList;
import ca.bc.gov.mal.cirras.underwriting.data.models.GradeModifierType;
import ca.bc.gov.mal.cirras.underwriting.data.models.GradeModifierTypeList;
import ca.bc.gov.mal.cirras.underwriting.data.models.SeedingDeadline;
import ca.bc.gov.mal.cirras.underwriting.data.models.SeedingDeadlineList;
import ca.bc.gov.mal.cirras.underwriting.data.models.UnderwritingYear;
import ca.bc.gov.mal.cirras.underwriting.data.models.UnderwritingYearList;
import ca.bc.gov.mal.cirras.underwriting.data.models.YieldMeasUnitConversion;
import ca.bc.gov.mal.cirras.underwriting.data.models.YieldMeasUnitConversionList;
import ca.bc.gov.nrs.wfone.common.service.api.ConflictException;
import ca.bc.gov.nrs.wfone.common.service.api.ForbiddenException;
import ca.bc.gov.nrs.wfone.common.service.api.NotFoundException;
import ca.bc.gov.nrs.wfone.common.service.api.ServiceException;
import ca.bc.gov.nrs.wfone.common.service.api.ValidationFailureException;
import ca.bc.gov.nrs.wfone.common.service.api.model.factory.FactoryContext;
import ca.bc.gov.nrs.wfone.common.webade.authentication.WebAdeAuthentication;

public interface CirrasMaintenanceService {


	@Transactional(readOnly = true, rollbackFor = Exception.class)
	CommodityTypeCodeList<? extends CommodityTypeCode> getCommodityTypeCodeList(
			Integer insurancePlanId, 
			FactoryContext factoryContext,
			WebAdeAuthentication webAdeAuthentication)
	throws ServiceException;

	@Transactional(readOnly = false, rollbackFor = Exception.class)
	SeedingDeadlineList<? extends SeedingDeadline> saveSeedingDeadlines(
		SeedingDeadlineList<? extends SeedingDeadline> seedingDeadlines,
		Integer cropYear,
		FactoryContext factoryContext, 
		WebAdeAuthentication authentication
	) 
	throws ServiceException, NotFoundException, ValidationFailureException;

	@Transactional(readOnly = true, rollbackFor = Exception.class)
	SeedingDeadlineList<? extends SeedingDeadline> getSeedingDeadlines(
		Integer cropYear, 
		FactoryContext factoryContext, 
		WebAdeAuthentication authentication
	)
	throws ServiceException, NotFoundException;

	@Transactional(readOnly = true, rollbackFor = Exception.class)
	UnderwritingYear getUnderwritingYear(
		Integer cropYear, 
		FactoryContext factoryContext, 
		WebAdeAuthentication authentication
	)
	throws ServiceException, NotFoundException;

	@Transactional(readOnly = false, rollbackFor = Exception.class)
	void deleteUnderwritingYear(
		Integer cropYear, 
		String optimisticLock, 
		WebAdeAuthentication authentication
	)
	throws ServiceException, NotFoundException, ForbiddenException, ConflictException;

	@Transactional(readOnly = true, rollbackFor = Exception.class)
	UnderwritingYearList<? extends UnderwritingYear> getUnderwritingYearList(
			FactoryContext factoryContext,
			WebAdeAuthentication webAdeAuthentication
	)
	throws ServiceException, NotFoundException, ForbiddenException, ConflictException;

	@Transactional(readOnly = false, rollbackFor = Exception.class)
	UnderwritingYear createUnderwritingYear(
			UnderwritingYear underwritingYear, 
			FactoryContext factoryContext,
			WebAdeAuthentication webAdeAuthentication
	)
	throws ServiceException, NotFoundException, ValidationFailureException;

	@Transactional(readOnly = true, rollbackFor = Exception.class)
	GradeModifierList<? extends GradeModifier> getGradeModifierList(
			Integer cropYear, 
			Integer insurancePlanId, 
			Integer cropCommodityId,
			FactoryContext context, 
			WebAdeAuthentication authentication
	) 
	throws ServiceException;	
	
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	GradeModifierList<? extends GradeModifier> saveGradeModifiers(
			Integer cropYear, 
			Integer insurancePlanId, 
			Integer cropCommodityId,
			GradeModifierList<? extends GradeModifier> gradeModifiers, 
			FactoryContext factoryContext,
			WebAdeAuthentication webAdeAuthentication)
	throws ServiceException, NotFoundException, ValidationFailureException;

	@Transactional(readOnly = true, rollbackFor = Exception.class)
	GradeModifierTypeList<? extends GradeModifierType> getGradeModifierTypeList(
			Integer cropYear, 
			FactoryContext context, 
			WebAdeAuthentication authentication
	) 
	throws ServiceException;	
	
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	GradeModifierTypeList<? extends GradeModifierType> saveGradeModifierTypes(
			Integer cropYear, 
			GradeModifierTypeList<? extends GradeModifierType> gradeModifierTypes, 
			FactoryContext factoryContext,
			WebAdeAuthentication webAdeAuthentication)
	throws ServiceException, NotFoundException, ValidationFailureException;	

	@Transactional(readOnly = true, rollbackFor = Exception.class)
	CropVarietyInsurabilityList<? extends CropVarietyInsurability> getCropVarietyInsurabilities(
			Integer insurancePlanId,
			Boolean loadForEdit,
			FactoryContext context, 
			WebAdeAuthentication authentication
	) 
	throws ServiceException;	
	
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	CropVarietyInsurabilityList<? extends CropVarietyInsurability> saveCropVarietyInsurabilities(
			Integer insurancePlanId,
			Boolean loadForEdit,
			CropVarietyInsurabilityList<? extends CropVarietyInsurability> cropVarietyInsurabilities, 
			FactoryContext factoryContext,
			WebAdeAuthentication webAdeAuthentication)
	throws ServiceException, NotFoundException, ValidationFailureException;	
	

	@Transactional(readOnly = true, rollbackFor = Exception.class)
	YieldMeasUnitConversionList<? extends YieldMeasUnitConversion> getYieldMeasUnitConversions(
			Integer insurancePlanId,
			String srcYieldMeasUnitTypeCode,
			String targetYieldMeasUnitTypeCode,
			FactoryContext context, 
			WebAdeAuthentication authentication
	) 
	throws ServiceException;	
	
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	YieldMeasUnitConversionList<? extends YieldMeasUnitConversion> saveYieldMeasUnitConversions(
			YieldMeasUnitConversionList<? extends YieldMeasUnitConversion> yieldMeasUnitConversions,
			Integer insurancePlanId,
			String srcYieldMeasUnitTypeCode,
			String targetYieldMeasUnitTypeCode,
			FactoryContext factoryContext,
			WebAdeAuthentication webAdeAuthentication)
	throws ServiceException, NotFoundException, ValidationFailureException;	
	
}
