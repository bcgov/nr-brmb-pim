package ca.bc.gov.mal.cirras.underwriting.services;

import org.springframework.transaction.annotation.Transactional;

import ca.bc.gov.mal.cirras.underwriting.data.resources.CommodityTypeCodeListRsrc;
import ca.bc.gov.mal.cirras.underwriting.data.resources.CropVarietyInsurabilityListRsrc;
import ca.bc.gov.mal.cirras.underwriting.data.resources.GradeModifierListRsrc;
import ca.bc.gov.mal.cirras.underwriting.data.resources.GradeModifierTypeListRsrc;
import ca.bc.gov.mal.cirras.underwriting.data.resources.SeedingDeadlineListRsrc;
import ca.bc.gov.mal.cirras.underwriting.data.resources.UnderwritingYearListRsrc;
import ca.bc.gov.mal.cirras.underwriting.data.resources.UnderwritingYearRsrc;
import ca.bc.gov.mal.cirras.underwriting.data.resources.YieldMeasUnitConversionListRsrc;
import ca.bc.gov.nrs.wfone.common.service.api.ConflictException;
import ca.bc.gov.nrs.wfone.common.service.api.ForbiddenException;
import ca.bc.gov.nrs.wfone.common.service.api.NotFoundException;
import ca.bc.gov.nrs.wfone.common.service.api.ServiceException;
import ca.bc.gov.nrs.wfone.common.service.api.ValidationFailureException;
import ca.bc.gov.nrs.wfone.common.service.api.model.factory.FactoryContext;
import ca.bc.gov.nrs.wfone.common.webade.authentication.WebAdeAuthentication;

public interface CirrasMaintenanceService {


	@Transactional(readOnly = true, rollbackFor = Exception.class)
	CommodityTypeCodeListRsrc getCommodityTypeCodeList(
			Integer insurancePlanId, 
			FactoryContext factoryContext,
			WebAdeAuthentication webAdeAuthentication)
	throws ServiceException;

	@Transactional(readOnly = false, rollbackFor = Exception.class)
	SeedingDeadlineListRsrc saveSeedingDeadlines(
		SeedingDeadlineListRsrc seedingDeadlines,
		Integer cropYear,
		FactoryContext factoryContext, 
		WebAdeAuthentication authentication
	) 
	throws ServiceException, NotFoundException, ValidationFailureException;

	@Transactional(readOnly = true, rollbackFor = Exception.class)
	SeedingDeadlineListRsrc getSeedingDeadlines(
		Integer cropYear, 
		FactoryContext factoryContext, 
		WebAdeAuthentication authentication
	)
	throws ServiceException, NotFoundException;

	@Transactional(readOnly = true, rollbackFor = Exception.class)
	UnderwritingYearRsrc getUnderwritingYear(
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
	UnderwritingYearListRsrc getUnderwritingYearList(
			FactoryContext factoryContext,
			WebAdeAuthentication webAdeAuthentication
	)
	throws ServiceException, NotFoundException, ForbiddenException, ConflictException;

	@Transactional(readOnly = false, rollbackFor = Exception.class)
	UnderwritingYearRsrc createUnderwritingYear(
			UnderwritingYearRsrc underwritingYear, 
			FactoryContext factoryContext,
			WebAdeAuthentication webAdeAuthentication
	)
	throws ServiceException, NotFoundException, ValidationFailureException;

	@Transactional(readOnly = true, rollbackFor = Exception.class)
	GradeModifierListRsrc getGradeModifierList(
			Integer cropYear, 
			Integer insurancePlanId, 
			Integer cropCommodityId,
			FactoryContext context, 
			WebAdeAuthentication authentication
	) 
	throws ServiceException;	
	
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	GradeModifierListRsrc saveGradeModifiers(
			Integer cropYear, 
			Integer insurancePlanId, 
			Integer cropCommodityId,
			GradeModifierListRsrc gradeModifiers, 
			FactoryContext factoryContext,
			WebAdeAuthentication webAdeAuthentication)
	throws ServiceException, NotFoundException, ValidationFailureException;

	@Transactional(readOnly = true, rollbackFor = Exception.class)
	GradeModifierTypeListRsrc getGradeModifierTypeList(
			Integer cropYear, 
			FactoryContext context, 
			WebAdeAuthentication authentication
	) 
	throws ServiceException;	
	
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	GradeModifierTypeListRsrc saveGradeModifierTypes(
			Integer cropYear, 
			GradeModifierTypeListRsrc gradeModifierTypes, 
			FactoryContext factoryContext,
			WebAdeAuthentication webAdeAuthentication)
	throws ServiceException, NotFoundException, ValidationFailureException;	

	@Transactional(readOnly = true, rollbackFor = Exception.class)
	CropVarietyInsurabilityListRsrc getCropVarietyInsurabilities(
			Integer insurancePlanId,
			Boolean loadForEdit,
			FactoryContext context, 
			WebAdeAuthentication authentication
	) 
	throws ServiceException;	
	
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	CropVarietyInsurabilityListRsrc saveCropVarietyInsurabilities(
			Integer insurancePlanId,
			Boolean loadForEdit,
			CropVarietyInsurabilityListRsrc cropVarietyInsurabilities, 
			FactoryContext factoryContext,
			WebAdeAuthentication webAdeAuthentication)
	throws ServiceException, NotFoundException, ValidationFailureException;	
	

	@Transactional(readOnly = true, rollbackFor = Exception.class)
	YieldMeasUnitConversionListRsrc getYieldMeasUnitConversions(
			Integer insurancePlanId,
			String srcYieldMeasUnitTypeCode,
			String targetYieldMeasUnitTypeCode,
			FactoryContext context, 
			WebAdeAuthentication authentication
	) 
	throws ServiceException;	
	
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	YieldMeasUnitConversionListRsrc saveYieldMeasUnitConversions(
			YieldMeasUnitConversionListRsrc yieldMeasUnitConversions,
			Integer insurancePlanId,
			String srcYieldMeasUnitTypeCode,
			String targetYieldMeasUnitTypeCode,
			FactoryContext factoryContext,
			WebAdeAuthentication webAdeAuthentication)
	throws ServiceException, NotFoundException, ValidationFailureException;	
	
}
