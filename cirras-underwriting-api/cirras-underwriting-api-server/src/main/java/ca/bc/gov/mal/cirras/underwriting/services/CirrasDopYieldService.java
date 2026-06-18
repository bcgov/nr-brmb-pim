package ca.bc.gov.mal.cirras.underwriting.services;

import java.util.Properties;
import java.util.stream.Collectors;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import ca.bc.gov.mal.cirras.underwriting.data.models.DopYieldContractCommodity;
import ca.bc.gov.mal.cirras.underwriting.data.models.UnderwritingComment;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.DeclaredYieldContractCommodityBerriesDao;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.DeclaredYieldContractCommodityDao;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.DeclaredYieldContractCommodityForageDao;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.DeclaredYieldContractDao;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.DeclaredYieldFieldCommodityBerriesDao;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.DeclaredYieldFieldDao;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.DeclaredYieldFieldForageDao;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.DeclaredYieldFieldRollupDao;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.DeclaredYieldFieldRollupForageDao;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.DeclaredYieldFieldVarietyBerriesDao;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.InventoryContractCommodityBerriesDao;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.InventoryContractCommodityDao;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.InventorySeededForageDao;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.PolicyDao;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.UnderwritingCommentDao;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.YieldMeasUnitTypeCodeDao;
import ca.bc.gov.mal.cirras.underwriting.data.resources.DopYieldContractRsrc;
import ca.bc.gov.mal.cirras.underwriting.data.resources.YieldMeasUnitTypeCodeListRsrc;
import ca.bc.gov.mal.cirras.underwriting.data.entities.DeclaredYieldContractCommodityBerriesDto;
import ca.bc.gov.mal.cirras.underwriting.data.entities.DeclaredYieldContractCommodityDto;
import ca.bc.gov.mal.cirras.underwriting.data.entities.DeclaredYieldContractCommodityForageDto;
import ca.bc.gov.mal.cirras.underwriting.data.entities.DeclaredYieldContractDto;
import ca.bc.gov.mal.cirras.underwriting.data.entities.DeclaredYieldFieldRollupDto;
import ca.bc.gov.mal.cirras.underwriting.data.entities.DeclaredYieldFieldRollupForageDto;
import ca.bc.gov.mal.cirras.underwriting.data.entities.InventoryContractCommodityBerriesDto;
import ca.bc.gov.mal.cirras.underwriting.data.entities.InventoryContractCommodityDto;
import ca.bc.gov.mal.cirras.underwriting.data.entities.InventorySeededForageDto;
import ca.bc.gov.mal.cirras.underwriting.data.entities.PolicyDto;
import ca.bc.gov.mal.cirras.underwriting.data.entities.UnderwritingCommentDto;
import ca.bc.gov.mal.cirras.underwriting.data.entities.YieldMeasUnitConversionDto;
import ca.bc.gov.mal.cirras.underwriting.data.entities.YieldMeasUnitTypeCodeDto;
import ca.bc.gov.nrs.wfone.common.model.Message;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.service.api.ConflictException;
import ca.bc.gov.nrs.wfone.common.service.api.ForbiddenException;
import ca.bc.gov.nrs.wfone.common.service.api.NotFoundException;
import ca.bc.gov.nrs.wfone.common.service.api.ServiceException;
import ca.bc.gov.nrs.wfone.common.service.api.ValidationFailureException;
import ca.bc.gov.nrs.wfone.common.service.api.model.factory.FactoryContext;
import ca.bc.gov.nrs.wfone.common.webade.authentication.WebAdeAuthentication;
import ca.bc.gov.mal.cirras.underwriting.data.assemblers.DopYieldContractRsrcFactory;
import ca.bc.gov.mal.cirras.underwriting.data.assemblers.YieldMeasUnitTypeCodeRsrcFactory;
import ca.bc.gov.mal.cirras.underwriting.services.reports.JasperReportService;
import ca.bc.gov.mal.cirras.underwriting.services.reports.JasperReportServiceException;
import ca.bc.gov.mal.cirras.underwriting.services.utils.BerriesService;
import ca.bc.gov.mal.cirras.underwriting.services.utils.FieldService;
import ca.bc.gov.mal.cirras.underwriting.services.utils.GrainForageService;
import ca.bc.gov.mal.cirras.underwriting.services.utils.InventoryServiceEnums.InsurancePlans;
import ca.bc.gov.mal.cirras.underwriting.services.utils.UnderwritingServiceHelper;

public class CirrasDopYieldService {

	private static final Logger logger = LoggerFactory.getLogger(CirrasDopYieldService.class);

	private Properties applicationProperties;

	public static final String MaximumResultsProperty = "maximum.results";

	public static final int DefaultMaximumResults = 800;

	// factories
	private DopYieldContractRsrcFactory dopYieldContractRsrcFactory;
	private YieldMeasUnitTypeCodeRsrcFactory yieldMeasUnitTypeCodeRsrcFactory;

	// daos
	private YieldMeasUnitTypeCodeDao yieldMeasUnitTypeCodeDao;
	private PolicyDao policyDao;
	private DeclaredYieldContractDao declaredYieldContractDao;
	private DeclaredYieldFieldDao declaredYieldFieldDao;
	private DeclaredYieldFieldForageDao declaredYieldFieldForageDao;
	private DeclaredYieldFieldRollupDao declaredYieldFieldRollupDao;
	private DeclaredYieldContractCommodityDao declaredYieldContractCommodityDao;
	private DeclaredYieldContractCommodityForageDao declaredYieldContractCommodityForageDao;
	private DeclaredYieldFieldRollupForageDao declaredYieldFieldRollupForageDao;
	private InventoryContractCommodityDao inventoryContractCommodityDao;
	private InventorySeededForageDao inventorySeededForageDao;
	private UnderwritingCommentDao underwritingCommentDao;
	private InventoryContractCommodityBerriesDao inventoryContractCommodityBerriesDao;
	private DeclaredYieldContractCommodityBerriesDao declaredYieldContractCommodityBerriesDao;
	private DeclaredYieldFieldCommodityBerriesDao declaredYieldFieldCommodityBerriesDao;
	private DeclaredYieldFieldVarietyBerriesDao declaredYieldFieldVarietyBerriesDao;

	// Jasper Reports
	private JasperReportService jasperReportService;

	// Utils
	private BerriesService berriesService;
	private FieldService fieldService;
	private GrainForageService grainForageService;
	private UnderwritingServiceHelper underwritingServiceHelper;

	public void setApplicationProperties(Properties applicationProperties) {
		this.applicationProperties = applicationProperties;
	}

	public void setBerriesService(BerriesService berriesService) {
		this.berriesService = berriesService;
	}
	
	public void setGrainForageService(GrainForageService grainForageService) {
		this.grainForageService = grainForageService;
	}

	public void setFieldService(FieldService fieldService) {
		this.fieldService = fieldService;
	}
	
	public void setUnderwritingServiceHelper(UnderwritingServiceHelper underwritingServiceHelper) {
		this.underwritingServiceHelper = underwritingServiceHelper;
	}

	public void setDopYieldContractRsrcFactory(DopYieldContractRsrcFactory dopYieldContractRsrcFactory) {
		this.dopYieldContractRsrcFactory = dopYieldContractRsrcFactory;
	}

	public void setYieldMeasUnitTypeCodeRsrcFactory(YieldMeasUnitTypeCodeRsrcFactory yieldMeasUnitTypeCodeRsrcFactory) {
		this.yieldMeasUnitTypeCodeRsrcFactory = yieldMeasUnitTypeCodeRsrcFactory;
	}

	public void setYieldMeasUnitTypeCodeDao(YieldMeasUnitTypeCodeDao yieldMeasUnitTypeCodeDao) {
		this.yieldMeasUnitTypeCodeDao = yieldMeasUnitTypeCodeDao;
	}

	public void setPolicyDao(PolicyDao policyDao) {
		this.policyDao = policyDao;
	}

	public void setDeclaredYieldContractDao(DeclaredYieldContractDao declaredYieldContractDao) {
		this.declaredYieldContractDao = declaredYieldContractDao;
	}

	public void setDeclaredYieldFieldDao(DeclaredYieldFieldDao declaredYieldFieldDao) {
		this.declaredYieldFieldDao = declaredYieldFieldDao;
	}

	public void setDeclaredYieldFieldForageDao(DeclaredYieldFieldForageDao declaredYieldFieldForageDao) {
		this.declaredYieldFieldForageDao = declaredYieldFieldForageDao;
	}
	
	public void setDeclaredYieldFieldRollupDao(DeclaredYieldFieldRollupDao declaredYieldFieldRollupDao) {
		this.declaredYieldFieldRollupDao = declaredYieldFieldRollupDao;
	}

	public void setDeclaredYieldContractCommodityDao(
			DeclaredYieldContractCommodityDao declaredYieldContractCommodityDao) {
		this.declaredYieldContractCommodityDao = declaredYieldContractCommodityDao;
	}

	public void setDeclaredYieldContractCommodityForageDao(DeclaredYieldContractCommodityForageDao declaredYieldContractCommodityForageDao) {
		this.declaredYieldContractCommodityForageDao = declaredYieldContractCommodityForageDao;
	}

	public void setDeclaredYieldFieldRollupForageDao(DeclaredYieldFieldRollupForageDao declaredYieldFieldRollupForageDao) {
		this.declaredYieldFieldRollupForageDao = declaredYieldFieldRollupForageDao;
	}

	public void setInventoryContractCommodityDao(InventoryContractCommodityDao inventoryContractCommodityDao) {
		this.inventoryContractCommodityDao = inventoryContractCommodityDao;
	}

	public void setInventorySeededForageDao(InventorySeededForageDao inventorySeededForageDao) {
		this.inventorySeededForageDao = inventorySeededForageDao;
	}

	public void setUnderwritingCommentDao(UnderwritingCommentDao underwritingCommentDao) {
		this.underwritingCommentDao = underwritingCommentDao;
	}

	public void setJasperReportService(JasperReportService jasperReportService) {
		this.jasperReportService = jasperReportService;
	}

	public void setInventoryContractCommodityBerriesDao(InventoryContractCommodityBerriesDao inventoryContractCommodityBerriesDao) {
		this.inventoryContractCommodityBerriesDao = inventoryContractCommodityBerriesDao;
	}
	
	public void setDeclaredYieldContractCommodityBerriesDao(DeclaredYieldContractCommodityBerriesDao declaredYieldContractCommodityBerriesDao) {
		this.declaredYieldContractCommodityBerriesDao = declaredYieldContractCommodityBerriesDao;
	}
	
	public void setDeclaredYieldFieldCommodityBerriesDao(DeclaredYieldFieldCommodityBerriesDao declaredYieldFieldCommodityBerriesDao) {
		this.declaredYieldFieldCommodityBerriesDao = declaredYieldFieldCommodityBerriesDao;
	}

	public void setDeclaredYieldFieldVarietyBerriesDao(DeclaredYieldFieldVarietyBerriesDao declaredYieldFieldVarietyBerriesDao) {
		this.declaredYieldFieldVarietyBerriesDao = declaredYieldFieldVarietyBerriesDao;
	}

	@Transactional(readOnly = true, rollbackFor = Exception.class)
	public DopYieldContractRsrc rolloverDopYieldContract(Integer policyId,
			FactoryContext factoryContext, WebAdeAuthentication authentication)
			throws ServiceException, NotFoundException {
		logger.debug("<rolloverDopYieldContract");

		// Add dop yield contract
		DopYieldContractRsrc result = null;

		try {

			PolicyDto policyDto = policyDao.fetch(policyId);

			if (policyDto == null) {
				throw new NotFoundException("Did not find the policy: " + policyId);
			}
			
			Integer insurancePlanId = policyDto.getInsurancePlanId();

			// Get measurement unit codes
			String defaultMeasurementUnitCode = getDefaultMeasurementUnitTypeCode(insurancePlanId);

			DeclaredYieldContractDto dycDto = new DeclaredYieldContractDto();
			dycDto.setContractId(policyDto.getContractId());
			dycDto.setCropYear(policyDto.getCropYear());
			dycDto.setInsurancePlanId(insurancePlanId);
			
			loadDopYieldContractCommodities(dycDto);
			fieldService.loadDeclaredFields(dycDto);

			result = dopYieldContractRsrcFactory.getDefaultDopYieldContract(policyDto, defaultMeasurementUnitCode,
					dycDto, factoryContext, authentication);

		} catch (DaoException e) {
			throw new ServiceException("DAO threw an exception", e);
		}

		logger.debug(">rolloverDopYieldContract");
		return result;
	}

	private String getDefaultMeasurementUnitTypeCode(Integer insurancePlanId) throws DaoException, NotFoundException {

		String defaultMeasurementUnitCode = null;
		List<YieldMeasUnitTypeCodeDto> yldmeasUnitDto = yieldMeasUnitTypeCodeDao.selectByPlan(insurancePlanId);
		if (yldmeasUnitDto != null && yldmeasUnitDto.size() > 0) {
			List<YieldMeasUnitTypeCodeDto> defaultMeasUnits = yldmeasUnitDto.stream()
					.filter(x -> x.getIsDefaultYieldUnitInd() == true).collect(Collectors.toList());
			if (defaultMeasUnits != null && defaultMeasUnits.size() > 0) {
				defaultMeasurementUnitCode = defaultMeasUnits.get(0).getYieldMeasUnitTypeCode();
			}
		}

		if (defaultMeasurementUnitCode == null) {
			throw new NotFoundException("No default measurement unit for the insurance plan found: " + insurancePlanId);
		}

		return defaultMeasurementUnitCode;

	}

	@Transactional(readOnly = true, rollbackFor = Exception.class)
	public DopYieldContractRsrc getDopYieldContract(
			String declaredYieldContractGuid,
			FactoryContext factoryContext, 
			WebAdeAuthentication authentication)
			throws ServiceException, NotFoundException {
		logger.debug("<getDopYieldContract");

		DopYieldContractRsrc result = null;

		try {
			DeclaredYieldContractDto dto = declaredYieldContractDao.fetch(declaredYieldContractGuid);

			if (dto == null) {
				throw new NotFoundException("Did not find the dop yield contract: " + declaredYieldContractGuid);
			}

			result = loadDopYieldContract(dto, factoryContext, authentication);

		} catch (DaoException e) {
			throw new ServiceException("DAO threw an exception", e);
		}

		logger.debug(">getDopYieldContract");
		return result;
	}

	private DopYieldContractRsrc loadDopYieldContract(
			DeclaredYieldContractDto dto,
			FactoryContext factoryContext, 
			WebAdeAuthentication authentication) throws DaoException {

		loadDopYieldFieldRollups(dto);
		loadDopYieldContractCommodities(dto);
		fieldService.loadDeclaredFields(dto);
		loadContractUwComments(dto);

		return dopYieldContractRsrcFactory.getDopYieldContract(dto, factoryContext, authentication);
	}

	private void loadContractUwComments(DeclaredYieldContractDto dto) throws DaoException {
		if (!(dto.getDeclaredYieldContractGuid() == null)) {
			List<UnderwritingCommentDto> uwComments = underwritingCommentDao
					.selectForDopContract(dto.getDeclaredYieldContractGuid());
			dto.setUwComments(uwComments);
		}
	}

	private void loadDopYieldContractCommodities(DeclaredYieldContractDto dto) throws DaoException {
		
		if ( InsurancePlans.GRAIN.getInsurancePlanId().equals(dto.getInsurancePlanId()) ) {
			if (dto.getDeclaredYieldContractGuid() == null) {
				getInventoryContractCommodities(dto);
			} else {
				List<DeclaredYieldContractCommodityDto> dopCommodities = declaredYieldContractCommodityDao.selectForDeclaredYieldContract(dto.getDeclaredYieldContractGuid());
				dto.setDeclaredYieldContractCommodities(dopCommodities);
			}
		} else if ( InsurancePlans.FORAGE.getInsurancePlanId().equals(dto.getInsurancePlanId()) ) {
			if (dto.getDeclaredYieldContractGuid() == null) {
				getInventoryContractCommodityTypes(dto);
			} else {
				List<DeclaredYieldContractCommodityForageDto> dopForageCommodities = declaredYieldContractCommodityForageDao.selectForDeclaredYieldContract(dto.getDeclaredYieldContractGuid(), DeclaredYieldContractCommodityForageDao.sortOrder.CommodityType);
				dto.setDeclaredYieldContractCommodityForageList(dopForageCommodities);

				List<DeclaredYieldFieldRollupForageDto> dopForageRollup = declaredYieldFieldRollupForageDao.selectForDeclaredYieldContract(dto.getDeclaredYieldContractGuid());
				dto.setDeclaredYieldFieldRollupForageList(dopForageRollup);
			}
		} else if ( InsurancePlans.BERRIES.getInsurancePlanId().equals(dto.getInsurancePlanId()) ) {
			if (dto.getDeclaredYieldContractGuid() == null) {
				getInventoryContractCommodityBerries(dto);
			} else {
				List<DeclaredYieldContractCommodityBerriesDto> dopBerriesCommodities = declaredYieldContractCommodityBerriesDao.selectForDeclaredYieldContract(dto.getDeclaredYieldContractGuid());
				dto.setDeclaredYieldContractCommodityBerriesList(dopBerriesCommodities);
			}
		} else {
			throw new ServiceException("Insurance Plan must be GRAIN, FORAGE or BERRIES");
		}
	}
	
	private void getInventoryContractCommodityTypes(DeclaredYieldContractDto dto) throws DaoException {
		
		List<InventorySeededForageDto> dtos = inventorySeededForageDao.selectForDopContractCommodityTotals(dto.getContractId(), dto.getCropYear());
		//Contract Commodity Totals
		List<DeclaredYieldContractCommodityForageDto> declaredYieldContractCommodityForageList = dopYieldContractRsrcFactory.getDopForageCommoditiesFromInventorySeeded(dtos);
		dto.setDeclaredYieldContractCommodityForageList(declaredYieldContractCommodityForageList);

		//Rollup Totals
		List<DeclaredYieldFieldRollupForageDto> declaredYieldFieldRollupForageList = dopYieldContractRsrcFactory.getDopForageRollupCommoditiesFromInventorySeeded(dtos);
		dto.setDeclaredYieldFieldRollupForageList(declaredYieldFieldRollupForageList);

	}

	private void loadDopYieldFieldRollups(DeclaredYieldContractDto dto) throws DaoException {
		if ( InsurancePlans.GRAIN.getInsurancePlanId().equals(dto.getInsurancePlanId()) ) {
			List<DeclaredYieldFieldRollupDto> rollups = declaredYieldFieldRollupDao.selectForDeclaredYieldContract(dto.getDeclaredYieldContractGuid());
			dto.setDeclaredYieldFieldRollupList(rollups);
		}
	}

	private void getInventoryContractCommodities(DeclaredYieldContractDto dto) throws DaoException {
		
		List<InventoryContractCommodityDto> dtos = inventoryContractCommodityDao.selectForDopContract(dto.getContractId(), dto.getCropYear());
		List<DeclaredYieldContractCommodityDto> dopCommodities = dopYieldContractRsrcFactory.getDopCommoditiesFromInventoryCommodities(dtos);
		dto.setDeclaredYieldContractCommodities(dopCommodities);

	}

	private void getInventoryContractCommodityBerries(DeclaredYieldContractDto dto) throws DaoException {

		List<InventoryContractCommodityBerriesDto> dtos = inventoryContractCommodityBerriesDao.selectForDopContract(dto.getContractId(), dto.getCropYear());
		List<DeclaredYieldContractCommodityBerriesDto> dopCommodities = dopYieldContractRsrcFactory.getDopBerriesCommoditiesFromInventoryBerriesCommodities(dtos);
		dto.setDeclaredYieldContractCommodityBerriesList(dopCommodities);
	}
	
	@Transactional(readOnly = true, rollbackFor = Exception.class)
	public byte[] generateDopReport(Integer cropYear, Integer insurancePlanId, Integer officeId, String policyStatusCode,
			String policyNumber, String growerInfo, String sortColumn, String policyIds, FactoryContext factoryContext,
			WebAdeAuthentication authentication) throws ServiceException, NotFoundException {

		logger.debug("<generateDopReport");

		// Result is a PDF, as a byte array.
		byte[] result = null;

		try {

			if (policyNumber != null) {
				policyNumber += "%";
			}

			String growerPhoneNumber = "";
			if (growerInfo != null) {

				growerPhoneNumber = policyDao.cleanGrowerPhoneNumber(growerInfo);

				// Add wildcard
				growerInfo = growerInfo.toUpperCase() + "%";
			}

			// Ignore crop year if the policy number contains the year (i.e. 111111-21)
			if (policyNumber != null && policyNumber.indexOf("-") > -1 && policyNumber.length() > 6) {
				cropYear = null;
			}

			Map<String, Object> queryParams = new HashMap<String, Object>();

			if (cropYear != null) queryParams.put("p_crop_year", cropYear);
			if (insurancePlanId != null) queryParams.put("p_insurance_plan_id", insurancePlanId);
			if (officeId != null) queryParams.put("p_office_id", officeId);
			if (StringUtils.isNotBlank(policyStatusCode)) queryParams.put("p_policy_status_code", policyStatusCode);
			if (StringUtils.isNotBlank(policyNumber)) queryParams.put("p_policy_number", policyNumber);
			if (StringUtils.isNotBlank(growerInfo)) queryParams.put("p_grower_info", growerInfo);
			if (StringUtils.isNotBlank(growerPhoneNumber)) queryParams.put("p_grower_phone_number", growerPhoneNumber);
			if (StringUtils.isNotBlank(sortColumn)) queryParams.put("p_sort_column", sortColumn);
			if (StringUtils.isNotBlank(policyIds)) queryParams.put("p_policy_ids", policyIds);

			// Pick the jasper report to run based on plan.
			if ( InsurancePlans.GRAIN.getInsurancePlanId().equals(insurancePlanId) ) {
				result = jasperReportService.generateDopGrainReport(queryParams);

			} else if ( InsurancePlans.FORAGE.getInsurancePlanId().equals(insurancePlanId) ) {
				result = jasperReportService.generateDopForageReport(queryParams);
				
			} else if ( InsurancePlans.BERRIES.getInsurancePlanId().equals(insurancePlanId) ) {
				result = jasperReportService.generateDopBerriesReport(queryParams);
			
			} else {
				throw new ServiceException("Insurance Plan must be GRAIN, FORAGE or BERRIES");
			}
			
			
			if (result == null) {
				throw new NotFoundException("Report generation failed");
			}

		} catch (JasperReportServiceException e) {
			throw new ServiceException("Jasper threw an exception", e);
		}

		logger.debug(">generateDopReport");

		return result;
	}

	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public DopYieldContractRsrc createDopYieldContract(
			DopYieldContractRsrc dopYieldContract, FactoryContext factoryContext,
			WebAdeAuthentication authentication)
			throws ServiceException, NotFoundException, ValidationFailureException {

		logger.debug("<createDopYieldContract");

		DopYieldContractRsrc result = null;
		String userId = getUserId(authentication);

		try {
			List<Message> errors = new ArrayList<Message>();

			if (!errors.isEmpty()) {
				throw new ValidationFailureException(errors);
			}

			Map<String, YieldMeasUnitConversionDto> ymucMap = grainForageService.loadYieldMeasUnitConversionsMap(
					dopYieldContract.getCropYear(), dopYieldContract.getInsurancePlanId());

			String declaredYieldContractGuid = insertDeclaredYieldContract(dopYieldContract, userId);

			// update annual field values
			fieldService.updateAnnualField(dopYieldContract, ymucMap, authentication, userId);

			if ( InsurancePlans.GRAIN.getInsurancePlanId().equals(dopYieldContract.getInsurancePlanId()) ) {
				// Estimated Yield/Commodity (Field Rollup)
				grainForageService.updateDeclaredYieldFieldRollup(declaredYieldContractGuid, dopYieldContract, userId, ymucMap);

				// Declared Yield Contract Commodity
				List<DopYieldContractCommodity> dopContractCommodities = dopYieldContract.getDopYieldContractCommodities();
				if (dopContractCommodities != null && !dopContractCommodities.isEmpty()) {
					for (DopYieldContractCommodity dopContractCommodity : dopContractCommodities) {
						updateDeclaredYieldContractCommodity(declaredYieldContractGuid, dopYieldContract, dopContractCommodity, ymucMap, userId);
					}
				}
			} else if ( InsurancePlans.FORAGE.getInsurancePlanId().equals(dopYieldContract.getInsurancePlanId()) ) {

				// Convert units and calculate contract commodity values and field rollups
				grainForageService.calculateForageDop(declaredYieldContractGuid, dopYieldContract, ymucMap);

				//Save commodity totals
				grainForageService.updateDeclaredYieldContractCommodityForage(declaredYieldContractGuid, dopYieldContract, ymucMap, userId);
				
				//Save field rollups
				grainForageService.updateDeclaredYieldFieldRollupForage(declaredYieldContractGuid, dopYieldContract, userId);

			} else if ( InsurancePlans.BERRIES.getInsurancePlanId().equals(dopYieldContract.getInsurancePlanId()) ) {
				berriesService.calculateDeclaredYieldContractCommodityBerriesList(dopYieldContract);
				berriesService.updateDeclaredYieldContractCommodityBerriesList(declaredYieldContractGuid, dopYieldContract, userId);
			} else {
				throw new ServiceException("Insurance Plan must be GRAIN, FORAGE or BERRIES");
			}

			// DOP contract level comments
			updateContractUwComments(declaredYieldContractGuid, dopYieldContract, userId, authentication);

			result = getDopYieldContract(declaredYieldContractGuid, factoryContext, authentication);

		} catch (DaoException e) {
			throw new ServiceException("DAO threw an exception", e);
		}

		logger.debug(">createDopYieldContract");
		return result;
	}

	
	private void updateDeclaredYieldContractCommodity(String declaredYieldContractGuid,
			DopYieldContractRsrc dopYieldContract, DopYieldContractCommodity dopContractCommodity,
			Map<String, YieldMeasUnitConversionDto> ymucMap, String userId) throws DaoException {

		logger.debug("<updateDeclaredYieldContractCommodity");

		// Calculate default units
		if (dopContractCommodity.getSoldYield() == null || dopContractCommodity.getSoldYield() == 0) {
			dopContractCommodity.setSoldYieldDefaultUnit(dopContractCommodity.getSoldYield());
		} else {
			dopContractCommodity.setSoldYieldDefaultUnit(
					underwritingServiceHelper.convertEstimatedYield(dopYieldContract, dopYieldContract.getDefaultYieldMeasUnitTypeCode(),
							dopContractCommodity.getCropCommodityId(), dopContractCommodity.getSoldYield(), ymucMap));
		}

		if (dopContractCommodity.getStoredYield() == null || dopContractCommodity.getStoredYield() == 0) {
			dopContractCommodity.setStoredYieldDefaultUnit(dopContractCommodity.getStoredYield());
		} else {
			dopContractCommodity.setStoredYieldDefaultUnit(
					underwritingServiceHelper.convertEstimatedYield(dopYieldContract, dopYieldContract.getDefaultYieldMeasUnitTypeCode(),
							dopContractCommodity.getCropCommodityId(), dopContractCommodity.getStoredYield(), ymucMap));
		}

		DeclaredYieldContractCommodityDto dto = null;

		if (dopContractCommodity.getDeclaredYieldContractCommodityGuid() != null) {
			dto = declaredYieldContractCommodityDao.fetch(dopContractCommodity.getDeclaredYieldContractCommodityGuid());
		}

		if (dto == null) {
			// Insert if it doesn't exist
			insertDeclaredYieldContractCommodity(declaredYieldContractGuid, dopContractCommodity, userId);
		} else {
			dopYieldContractRsrcFactory.updateDto(dto, dopContractCommodity);

			declaredYieldContractCommodityDao.update(dto, userId);
		}

		logger.debug(">updateDeclaredYieldContractCommodity");
	}
	
	private void insertDeclaredYieldContractCommodity(String declaredYieldContractGuid,
			DopYieldContractCommodity dopContractCommodity, String userId) throws DaoException {

		logger.debug("<insertDeclaredYieldContractCommodity");

		DeclaredYieldContractCommodityDto dto = new DeclaredYieldContractCommodityDto();

		dopYieldContractRsrcFactory.updateDto(dto, dopContractCommodity);

		dto.setDeclaredYieldContractCommodityGuid(null);
		dto.setDeclaredYieldContractGuid(declaredYieldContractGuid);

		declaredYieldContractCommodityDao.insert(dto, userId);

		logger.debug(">insertDeclaredYieldContractCommodity");

	}
	
	private String insertDeclaredYieldContract(DopYieldContractRsrc dopYieldContract, String userId)
			throws DaoException {

		DeclaredYieldContractDto dto = new DeclaredYieldContractDto();
		dopYieldContractRsrcFactory.updateDto(dto, dopYieldContract, userId);
		dto.setDeclaredYieldContractGuid(null);
		declaredYieldContractDao.insert(dto, userId);

		return dto.getDeclaredYieldContractGuid();
	}

	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public DopYieldContractRsrc updateDopYieldContract(String declaredYieldContractGuid,
			String optimisticLock, DopYieldContractRsrc dopYieldContract,
			FactoryContext factoryContext, WebAdeAuthentication authentication) throws ServiceException,
			NotFoundException, ForbiddenException, ConflictException, ValidationFailureException {

		logger.debug("<updateDopYieldContract");

		DopYieldContractRsrc result = null;
		String userId = getUserId(authentication);

		try {
			List<Message> errors = new ArrayList<Message>();

			if (!errors.isEmpty()) {
				throw new ValidationFailureException(errors);
			}

			Map<String, YieldMeasUnitConversionDto> ymucMap = grainForageService.loadYieldMeasUnitConversionsMap(
					dopYieldContract.getCropYear(), dopYieldContract.getInsurancePlanId());

			updateDeclaredYieldContract(dopYieldContract, userId);

			// update annual field values
			fieldService.updateAnnualField(dopYieldContract, ymucMap, authentication, userId);

			if ( InsurancePlans.GRAIN.getInsurancePlanId().equals(dopYieldContract.getInsurancePlanId()) ) {
				// Estimated Yield/Commodity (Field Rollup)
				grainForageService.updateDeclaredYieldFieldRollup(dopYieldContract.getDeclaredYieldContractGuid(), dopYieldContract, userId,
						ymucMap);

				// Declared Yield Contract Commodity
				List<DopYieldContractCommodity> dopContractCommodities = dopYieldContract.getDopYieldContractCommodities();
				if (dopContractCommodities != null && !dopContractCommodities.isEmpty()) {
					for (DopYieldContractCommodity dopContractCommodity : dopContractCommodities) {
						updateDeclaredYieldContractCommodity(declaredYieldContractGuid, dopYieldContract,
								dopContractCommodity, ymucMap, userId);
					}
				}

			} else if ( InsurancePlans.FORAGE.getInsurancePlanId().equals(dopYieldContract.getInsurancePlanId()) ) {

				// Convert units and calculate contract commodity values and field rollups
				grainForageService.calculateForageDop(declaredYieldContractGuid, dopYieldContract, ymucMap);

				//Save commodity totals
				grainForageService.updateDeclaredYieldContractCommodityForage(declaredYieldContractGuid, dopYieldContract, ymucMap, userId);
				
				//Save field rollups
				grainForageService.updateDeclaredYieldFieldRollupForage(declaredYieldContractGuid, dopYieldContract, userId);

			} else if ( InsurancePlans.BERRIES.getInsurancePlanId().equals(dopYieldContract.getInsurancePlanId()) ) {
				berriesService.calculateDeclaredYieldContractCommodityBerriesList(dopYieldContract);
				berriesService.updateDeclaredYieldContractCommodityBerriesList(declaredYieldContractGuid, dopYieldContract, userId);
			} else {
				throw new ServiceException("Insurance Plan must be GRAIN, FORAGE or BERRIES");
			}
			
			// DOP contract level comments
			updateContractUwComments(declaredYieldContractGuid, dopYieldContract, userId, authentication);

			result = getDopYieldContract(declaredYieldContractGuid, factoryContext, authentication);

		} catch (DaoException e) {
			throw new ServiceException("DAO threw an exception", e);
		}

		logger.debug(">updateDopYieldContract");
		return result;

	}

	private void updateContractUwComments(String declaredYieldContractGuid,
			DopYieldContractRsrc dopYieldContract, String userId,
			WebAdeAuthentication authentication) throws DaoException, ServiceException {

		List<UnderwritingComment> contractUwComments = dopYieldContract.getUwComments();
		if (contractUwComments != null && !contractUwComments.isEmpty()) {
			for (UnderwritingComment contractUnderwritingComment : contractUwComments) {
				if (contractUnderwritingComment.getDeletedByUserInd() != null
						&& contractUnderwritingComment.getDeletedByUserInd()) {
					underwritingServiceHelper.deleteUnderwritingComment(contractUnderwritingComment, userId, authentication);
				} else {
					underwritingServiceHelper.updateUnderwritingComment(contractUnderwritingComment, null,
							dopYieldContract.getGrowerContractYearId(), declaredYieldContractGuid, userId,
							authentication);
				}
			}
		}

	}

	private void updateDeclaredYieldContract(DopYieldContractRsrc dopYieldContract, String userId)
			throws DaoException, NotFoundException {

		DeclaredYieldContractDto dto = declaredYieldContractDao.fetch(dopYieldContract.getDeclaredYieldContractGuid());

		if (dto == null) {
			throw new NotFoundException(
					"Did not find the dop yield contract: " + dopYieldContract.getDeclaredYieldContractGuid());
		}

		dopYieldContractRsrcFactory.updateDto(dto, dopYieldContract, userId);
		declaredYieldContractDao.update(dto, userId);
	}

	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public void deleteDopYieldContract(String declaredYieldContractGuid, String optimisticLock,
			WebAdeAuthentication authentication)
			throws ServiceException, NotFoundException, ForbiddenException, ConflictException {

		logger.debug("<deleteDopYieldContract");

		try {

			deleteDeclaredYieldContract(declaredYieldContractGuid);

		} catch (DaoException e) {
			throw new ServiceException("DAO threw an exception", e);
		}

		logger.debug(">deleteDopYieldContract");
	}

	private void deleteDeclaredYieldContract(String declaredYieldContractGuid) throws DaoException, NotFoundException {

		DeclaredYieldContractDto dto = declaredYieldContractDao.fetch(declaredYieldContractGuid);

		if (dto == null) {
			throw new NotFoundException("Did not find the dop yield contract: " + declaredYieldContractGuid);
		}
		
		if ( InsurancePlans.GRAIN.getInsurancePlanId().equals(dto.getInsurancePlanId()) ) {
			
			declaredYieldFieldDao.deleteForDeclaredYieldContract(declaredYieldContractGuid);
			declaredYieldFieldRollupDao.deleteForDeclaredYieldContract(declaredYieldContractGuid);
			declaredYieldContractCommodityDao.deleteForDeclaredYieldContract(declaredYieldContractGuid);

		} else if ( InsurancePlans.FORAGE.getInsurancePlanId().equals(dto.getInsurancePlanId()) ) {

			declaredYieldFieldForageDao.deleteForDeclaredYieldContract(declaredYieldContractGuid);
			declaredYieldContractCommodityForageDao.deleteForDeclaredYieldContract(declaredYieldContractGuid);
			declaredYieldFieldRollupForageDao.deleteForDeclaredYieldContract(declaredYieldContractGuid);

		} else if ( InsurancePlans.BERRIES.getInsurancePlanId().equals(dto.getInsurancePlanId()) ) {

			declaredYieldFieldVarietyBerriesDao.deleteForDeclaredYieldContract(declaredYieldContractGuid);
			declaredYieldFieldCommodityBerriesDao.deleteForDeclaredYieldContract(declaredYieldContractGuid);
			declaredYieldContractCommodityBerriesDao.deleteForDeclaredYieldContract(declaredYieldContractGuid);
			
		} else {
			throw new ServiceException("Insurance Plan must be GRAIN, FORAGE or BERRIES");
		}

		underwritingCommentDao.deleteForDeclaredYieldContractGuid(declaredYieldContractGuid);
		declaredYieldContractDao.delete(declaredYieldContractGuid);
	}

	@Transactional(readOnly = true, rollbackFor = Exception.class)
	public YieldMeasUnitTypeCodeListRsrc getYieldMeasUnitTypeCodeList(
			Integer insurancePlanId, FactoryContext context, WebAdeAuthentication authentication)
			throws ServiceException, DaoException {

		List<YieldMeasUnitTypeCodeDto> dtos = yieldMeasUnitTypeCodeDao.selectByPlan(insurancePlanId);

		YieldMeasUnitTypeCodeListRsrc result = yieldMeasUnitTypeCodeRsrcFactory.getYieldMeasUnitTypeCodeList(dtos);

		return result;
	}


	//
	// The "proof of concept" REST service doesn't have any security
	//
	private String getUserId(WebAdeAuthentication authentication) {
		String userId = "DEFAULT_USERID";

		if (authentication != null) {
			userId = authentication.getUserId();
			authentication.getClientId();
		}

		return userId;
	}

}
