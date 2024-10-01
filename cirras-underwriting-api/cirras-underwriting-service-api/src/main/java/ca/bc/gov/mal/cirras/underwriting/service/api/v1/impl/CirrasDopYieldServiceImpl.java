package ca.bc.gov.mal.cirras.underwriting.service.api.v1.impl;

import java.util.Properties;
import java.util.stream.Collectors;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.mal.cirras.underwriting.model.v1.AnnualField;
import ca.bc.gov.mal.cirras.underwriting.model.v1.DopYieldContract;
import ca.bc.gov.mal.cirras.underwriting.model.v1.DopYieldContractCommodity;
import ca.bc.gov.mal.cirras.underwriting.model.v1.DopYieldContractCommodityForage;
import ca.bc.gov.mal.cirras.underwriting.model.v1.DopYieldFieldForage;
import ca.bc.gov.mal.cirras.underwriting.model.v1.DopYieldFieldForageCut;
import ca.bc.gov.mal.cirras.underwriting.model.v1.DopYieldFieldGrain;
import ca.bc.gov.mal.cirras.underwriting.model.v1.DopYieldFieldRollup;
import ca.bc.gov.mal.cirras.underwriting.model.v1.DopYieldFieldRollupForage;
import ca.bc.gov.mal.cirras.underwriting.model.v1.UnderwritingComment;
import ca.bc.gov.mal.cirras.underwriting.model.v1.YieldMeasUnitTypeCode;
import ca.bc.gov.mal.cirras.underwriting.model.v1.YieldMeasUnitTypeCodeList;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.ContractedFieldDetailDao;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.DeclaredYieldContractCommodityDao;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.DeclaredYieldContractCommodityForageDao;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.DeclaredYieldContractDao;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.DeclaredYieldFieldDao;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.DeclaredYieldFieldForageDao;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.DeclaredYieldFieldRollupDao;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.DeclaredYieldFieldRollupForageDao;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.InventoryContractCommodityDao;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.InventoryFieldDao;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.InventorySeededForageDao;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.InventorySeededGrainDao;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.PolicyDao;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.UnderwritingCommentDao;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.YieldMeasUnitConversionDao;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.YieldMeasUnitTypeCodeDao;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.ContractedFieldDetailDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.DeclaredYieldContractCommodityDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.DeclaredYieldContractCommodityForageDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.DeclaredYieldContractDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.DeclaredYieldFieldDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.DeclaredYieldFieldForageDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.DeclaredYieldFieldRollupDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.DeclaredYieldFieldRollupForageDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.InventoryContractCommodityDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.InventoryFieldDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.InventorySeededForageDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.InventorySeededGrainDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.PolicyDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.UnderwritingCommentDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.YieldMeasUnitConversionDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.YieldMeasUnitTypeCodeDto;
import ca.bc.gov.nrs.wfone.common.model.Message;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;
import ca.bc.gov.nrs.wfone.common.service.api.ConflictException;
import ca.bc.gov.nrs.wfone.common.service.api.ForbiddenException;
import ca.bc.gov.nrs.wfone.common.service.api.NotFoundException;
import ca.bc.gov.nrs.wfone.common.service.api.ServiceException;
import ca.bc.gov.nrs.wfone.common.service.api.ValidationFailureException;
import ca.bc.gov.nrs.wfone.common.service.api.model.factory.FactoryContext;
import ca.bc.gov.nrs.wfone.common.webade.authentication.WebAdeAuthentication;
import ca.bc.gov.mal.cirras.underwriting.service.api.v1.CirrasDopYieldService;
import ca.bc.gov.mal.cirras.underwriting.service.api.v1.model.factory.DopYieldContractFactory;
import ca.bc.gov.mal.cirras.underwriting.service.api.v1.model.factory.InventoryContractFactory;
import ca.bc.gov.mal.cirras.underwriting.service.api.v1.model.factory.YieldMeasUnitTypeCodeFactory;
import ca.bc.gov.mal.cirras.underwriting.service.api.v1.reports.JasperReportService;
import ca.bc.gov.mal.cirras.underwriting.service.api.v1.reports.JasperReportServiceException;
import ca.bc.gov.mal.cirras.underwriting.service.api.v1.util.InventoryServiceEnums;
import ca.bc.gov.mal.cirras.underwriting.service.api.v1.util.InventoryServiceEnums.InsurancePlans;
import ca.bc.gov.mal.cirras.underwriting.service.api.v1.validation.ModelValidator;

public class CirrasDopYieldServiceImpl implements CirrasDopYieldService {

	private static final Logger logger = LoggerFactory.getLogger(CirrasDopYieldServiceImpl.class);

	private Properties applicationProperties;

	private ModelValidator modelValidator;

	public static final String MaximumResultsProperty = "maximum.results";

	public static final int DefaultMaximumResults = 800;

	// factories
	private InventoryContractFactory inventoryContractFactory;
	private DopYieldContractFactory dopYieldContractFactory;
	private YieldMeasUnitTypeCodeFactory yieldMeasUnitTypeCodeFactory;

	// daos
	private YieldMeasUnitTypeCodeDao yieldMeasUnitTypeCodeDao;
	private YieldMeasUnitConversionDao yieldMeasUnitConversionDao;
	private PolicyDao policyDao;
	private DeclaredYieldContractDao declaredYieldContractDao;
	private InventoryFieldDao inventoryFieldDao;
	private InventorySeededGrainDao inventorySeededGrainDao;
	private ContractedFieldDetailDao contractedFieldDetailDao;
	private DeclaredYieldFieldDao declaredYieldFieldDao;
	private DeclaredYieldFieldForageDao declaredYieldFieldForageDao;
	private DeclaredYieldFieldRollupDao declaredYieldFieldRollupDao;
	private DeclaredYieldContractCommodityDao declaredYieldContractCommodityDao;
	private DeclaredYieldContractCommodityForageDao declaredYieldContractCommodityForageDao;
	private DeclaredYieldFieldRollupForageDao declaredYieldFieldRollupForageDao;
	private InventoryContractCommodityDao inventoryContractCommodityDao;
	private InventorySeededForageDao inventorySeededForageDao;
	private UnderwritingCommentDao underwritingCommentDao;

	// Jasper Reports
	private JasperReportService jasperReportService;

	public void setApplicationProperties(Properties applicationProperties) {
		this.applicationProperties = applicationProperties;
	}

	public void setModelValidator(ModelValidator modelValidator) {
		this.modelValidator = modelValidator;
	}

	public void setInventoryContractFactory(InventoryContractFactory inventoryContractFactory) {
		this.inventoryContractFactory = inventoryContractFactory;
	}

	public void setDopYieldContractFactory(DopYieldContractFactory dopYieldContractFactory) {
		this.dopYieldContractFactory = dopYieldContractFactory;
	}

	public void setYieldMeasUnitTypeCodeFactory(YieldMeasUnitTypeCodeFactory yieldMeasUnitTypeCodeFactory) {
		this.yieldMeasUnitTypeCodeFactory = yieldMeasUnitTypeCodeFactory;
	}

	public void setYieldMeasUnitTypeCodeDao(YieldMeasUnitTypeCodeDao yieldMeasUnitTypeCodeDao) {
		this.yieldMeasUnitTypeCodeDao = yieldMeasUnitTypeCodeDao;
	}

	public void setYieldMeasUnitConversionDao(YieldMeasUnitConversionDao yieldMeasUnitConversionDao) {
		this.yieldMeasUnitConversionDao = yieldMeasUnitConversionDao;
	}

	public void setPolicyDao(PolicyDao policyDao) {
		this.policyDao = policyDao;
	}

	public void setDeclaredYieldContractDao(DeclaredYieldContractDao declaredYieldContractDao) {
		this.declaredYieldContractDao = declaredYieldContractDao;
	}

	public void setInventoryFieldDao(InventoryFieldDao inventoryFieldDao) {
		this.inventoryFieldDao = inventoryFieldDao;
	}

	public void setInventorySeededGrainDao(InventorySeededGrainDao inventorySeededGrainDao) {
		this.inventorySeededGrainDao = inventorySeededGrainDao;
	}

	public void setContractedFieldDetailDao(ContractedFieldDetailDao contractedFieldDetailDao) {
		this.contractedFieldDetailDao = contractedFieldDetailDao;
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

	@Override
	public DopYieldContract<? extends AnnualField> rolloverDopYieldContract(Integer policyId,
			FactoryContext factoryContext, WebAdeAuthentication authentication)
			throws ServiceException, NotFoundException {
		logger.debug("<rolloverDopYieldContract");

		// Add dop yield contract
		DopYieldContract<? extends AnnualField> result = null;

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
			loadFields(dycDto);

			result = dopYieldContractFactory.getDefaultDopYieldContract(policyDto, defaultMeasurementUnitCode,
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

	@Override
	public DopYieldContract<? extends AnnualField> getDopYieldContract(
			String declaredYieldContractGuid,
			FactoryContext factoryContext, 
			WebAdeAuthentication authentication)
			throws ServiceException, NotFoundException {
		logger.debug("<getDopYieldContract");

		DopYieldContract<? extends AnnualField> result = null;

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

	private DopYieldContract<? extends AnnualField> loadDopYieldContract(
			DeclaredYieldContractDto dto,
			FactoryContext factoryContext, 
			WebAdeAuthentication authentication) throws DaoException {

		loadDopYieldFieldRollups(dto);
		loadDopYieldContractCommodities(dto);
		loadFields(dto);
		loadContractUwComments(dto);

		return dopYieldContractFactory.getDopYieldContract(dto, factoryContext, authentication);
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
				List<DeclaredYieldContractCommodityForageDto> dopForageCommodities = declaredYieldContractCommodityForageDao.selectForDeclaredYieldContract(dto.getDeclaredYieldContractGuid());
				dto.setDeclaredYieldContractCommodityForageList(dopForageCommodities);

				List<DeclaredYieldFieldRollupForageDto> dopForageRollup = declaredYieldFieldRollupForageDao.selectForDeclaredYieldContract(dto.getDeclaredYieldContractGuid());
				dto.setDeclaredYieldFieldRollupForageList(dopForageRollup);
			}
		} else {
			throw new ServiceException("Insurance Plan must be GRAIN or FORAGE");
		}
	}
	
	private void getInventoryContractCommodityTypes(DeclaredYieldContractDto dto) throws DaoException {
		
		List<InventorySeededForageDto> dtos = inventorySeededForageDao.selectForDopContractCommodityTotals(dto.getContractId(), dto.getCropYear());
		//Contract Commodity Totals
		List<DeclaredYieldContractCommodityForageDto> declaredYieldContractCommodityForageList = dopYieldContractFactory.getDopForageCommoditiesFromInventorySeeded(dtos);
		dto.setDeclaredYieldContractCommodityForageList(declaredYieldContractCommodityForageList);

		//Rollup Totals
		List<DeclaredYieldFieldRollupForageDto> declaredYieldFieldRollupForageList = dopYieldContractFactory.getDopForageRollupCommoditiesFromInventorySeeded(dtos);
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
		List<DeclaredYieldContractCommodityDto> dopCommodities = dopYieldContractFactory.getDopCommoditiesFromInventoryCommodities(dtos);
		dto.setDeclaredYieldContractCommodities(dopCommodities);

	}

	private void loadFields(DeclaredYieldContractDto dto) throws DaoException {

		List<ContractedFieldDetailDto> fields = contractedFieldDetailDao.selectForDeclaredYield(dto.getContractId(), dto.getCropYear());
		dto.setFields(fields);

		for (ContractedFieldDetailDto cfdDto : dto.getFields()) {
			loadPlantings(cfdDto, dto.getInsurancePlanId());
			loadUwComments(cfdDto);
		}
	}

	private void loadPlantings(ContractedFieldDetailDto cfdDto, Integer insurancePlanId) throws DaoException {
		
		List<InventoryFieldDto> plantings = inventoryFieldDao.selectForDeclaredYield(cfdDto.getFieldId(),
				cfdDto.getCropYear(), cfdDto.getInsurancePlanId());
		cfdDto.setPlantings(plantings);

		for (InventoryFieldDto ifDto : plantings) {

			if ( InsurancePlans.GRAIN.getInsurancePlanId().equals(cfdDto.getInsurancePlanId()) ) {
				loadSeededGrains(ifDto);
				loadDeclaredYieldField(ifDto);
			} else if ( InsurancePlans.FORAGE.getInsurancePlanId().equals(cfdDto.getInsurancePlanId()) ) {
			
				loadSeededForage(ifDto);
				loadDeclaredYieldFieldForage(ifDto);
				
			} else {
				throw new ServiceException("Insurance Plan must be GRAIN or FORAGE");
			}			

		}
	}

	private void loadUwComments(ContractedFieldDetailDto cfdDto) throws DaoException {
		//List<UnderwritingCommentDto> uwComments = underwritingCommentDao.select(cfdDto.getAnnualFieldDetailId());
		//Returning all comments of a field
		List<UnderwritingCommentDto> uwComments = underwritingCommentDao.selectForField(cfdDto.getFieldId());
		cfdDto.setUwComments(uwComments);
	}

	private void loadSeededGrains(InventoryFieldDto ifDto) throws DaoException {
		List<InventorySeededGrainDto> inventorySeededGrains = inventorySeededGrainDao.selectForDeclaredYield(ifDto.getInventoryFieldGuid());
		ifDto.setInventorySeededGrains(inventorySeededGrains);
	}

	private void loadDeclaredYieldField(InventoryFieldDto ifDto) throws DaoException {
		DeclaredYieldFieldDto dyfDto = declaredYieldFieldDao.getByInventoryField(ifDto.getInventoryFieldGuid());
		ifDto.setDeclaredYieldField(dyfDto);
	}
	
	private void loadSeededForage(InventoryFieldDto ifDto) throws DaoException {
		
		List<InventorySeededForageDto> inventorySeededForages = inventorySeededForageDao.selectForDeclaredYield(ifDto.getInventoryFieldGuid());
		ifDto.setInventorySeededForages(inventorySeededForages);
	}

	private void loadDeclaredYieldFieldForage(InventoryFieldDto ifDto) throws DaoException {

		List<DeclaredYieldFieldForageDto> dyffDtoList = declaredYieldFieldForageDao.getByInventoryField(ifDto.getInventoryFieldGuid());
		ifDto.setDeclaredYieldFieldForageList(dyffDtoList);
	}

	@Override
	public byte[] generateDopReport(String cropYear, String insurancePlanId, String officeId, String policyStatusCode,
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
				cropYear = "";
			}

			Map<String, String> queryParams = new HashMap<String, String>();

			queryParams.put("p_crop_year", notNull(cropYear, ""));
			queryParams.put("p_insurance_plan_id", notNull(insurancePlanId, ""));
			queryParams.put("p_office_id", notNull(officeId, ""));
			queryParams.put("p_policy_status_code", notNull(policyStatusCode, ""));
			queryParams.put("p_policy_number", notNull(policyNumber, ""));
			queryParams.put("p_grower_info", notNull(growerInfo, ""));
			queryParams.put("p_grower_phone_number", notNull(growerPhoneNumber, ""));
			queryParams.put("p_sort_column", notNull(sortColumn, "policyNumber"));
			queryParams.put("p_policy_ids", notNull(policyIds, ""));

			// Pick the jasper report to run based on plan.
			if ( InsurancePlans.GRAIN.getInsurancePlanId().toString().equals(insurancePlanId) ) {
				result = jasperReportService.generateDopGrainReport(queryParams);

			} else if ( InsurancePlans.FORAGE.getInsurancePlanId().toString().equals(insurancePlanId) ) {
				result = jasperReportService.generateDopForageReport(queryParams);
			
			} else {
				throw new ServiceException("Insurance Plan must be GRAIN or FORAGE");
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

	@Override
	public DopYieldContract<? extends AnnualField> createDopYieldContract(
			DopYieldContract<? extends AnnualField> dopYieldContract, FactoryContext factoryContext,
			WebAdeAuthentication authentication)
			throws ServiceException, NotFoundException, ValidationFailureException {

		logger.debug("<createDopYieldContract");

		DopYieldContract<? extends AnnualField> result = null;
		String userId = getUserId(authentication);

		try {
			List<Message> errors = new ArrayList<Message>();
			// errors.addAll(modelValidator.validateInsuranceClaim(insuranceClaim)); // TODO

			if (!errors.isEmpty()) {
				throw new ValidationFailureException(errors);
			}

			Map<String, YieldMeasUnitConversionDto> ymucMap = loadYieldMeasUnitConversionsMap(
					dopYieldContract.getCropYear(), dopYieldContract.getInsurancePlanId());

			String declaredYieldContractGuid = insertDeclaredYieldContract(dopYieldContract, userId);

			// update annual field values
			updateAnnualField(dopYieldContract, ymucMap, authentication);

			if ( InsurancePlans.GRAIN.getInsurancePlanId().equals(dopYieldContract.getInsurancePlanId()) ) {
				// Estimated Yield/Commodity (Field Rollup)
				updateDeclaredYieldFieldRollup(declaredYieldContractGuid, dopYieldContract, userId, ymucMap);

				// Declared Yield Contract Commodity
				List<DopYieldContractCommodity> dopContractCommodities = dopYieldContract.getDopYieldContractCommodities();
				if (dopContractCommodities != null && !dopContractCommodities.isEmpty()) {
					for (DopYieldContractCommodity dopContractCommodity : dopContractCommodities) {
						updateDeclaredYieldContractCommodity(declaredYieldContractGuid, dopYieldContract, dopContractCommodity, ymucMap, userId);
					}
				}
			} else if ( InsurancePlans.FORAGE.getInsurancePlanId().equals(dopYieldContract.getInsurancePlanId()) ) {

				// Convert units and calculate contract commodity values and field rollups
				calculateForageDop(declaredYieldContractGuid, dopYieldContract, ymucMap);

				//Save commodity totals
				updateDeclaredYieldContractCommodityForage(declaredYieldContractGuid, dopYieldContract, ymucMap, userId);
				
				//Save field rollups
				updateDeclaredYieldFieldRollupForage(declaredYieldContractGuid, dopYieldContract, userId);

			} else {
				throw new ServiceException("Insurance Plan must be GRAIN or FORAGE");
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

	private void updateAnnualField(DopYieldContract<? extends AnnualField> dopYieldContract,
			Map<String, YieldMeasUnitConversionDto> ymucMap, WebAdeAuthentication authentication) {

		logger.debug("<updateAnnualField");

		String userId = getUserId(authentication);

		try {
			List<? extends AnnualField> fields = dopYieldContract.getFields();
			if (fields != null && !fields.isEmpty()) {
				for (AnnualField field : fields) {

					if ( InsurancePlans.GRAIN.getInsurancePlanId().equals(dopYieldContract.getInsurancePlanId()) ) {
						List<DopYieldFieldGrain> dopYieldFields = field.getDopYieldFieldGrainList();
						if (dopYieldFields != null && !dopYieldFields.isEmpty()) {
							for (DopYieldFieldGrain dyf : dopYieldFields) {
								dyf.setEstimatedYieldPerAcreDefaultUnit(
										convertDopYieldFieldAcresWeight(dyf.getEstimatedYieldPerAcre(), dyf.getCropCommodityId(), dopYieldContract, ymucMap));
								updateDeclaredYieldField(dyf, userId);
							}
						}
					} else if ( InsurancePlans.FORAGE.getInsurancePlanId().equals(dopYieldContract.getInsurancePlanId()) ) {
						List<DopYieldFieldForage> dopYieldFields = field.getDopYieldFieldForageList();
						if (dopYieldFields != null && !dopYieldFields.isEmpty()) {
							for (DopYieldFieldForage dyf : dopYieldFields) {
								if(dyf.getDopYieldFieldForageCuts() != null && !dyf.getDopYieldFieldForageCuts().isEmpty()) {
									for(DopYieldFieldForageCut cut : dyf.getDopYieldFieldForageCuts()) {
										if ( Boolean.TRUE.equals(cut.getDeletedByUserInd())) {
											if(cut.getDeclaredYieldFieldForageGuid() != null) {
												declaredYieldFieldForageDao.delete(cut.getDeclaredYieldFieldForageGuid());
											}
										} else {
											if(dyf.getCropCommodityId() == null) {
												//Making sure there is no cut information saved if there is no commodity for the planting
												cut.setTotalBalesLoads(null);
												cut.setWeight(null);
												cut.setWeightDefaultUnit(null);
												cut.setMoisturePercent(null);
											} else {
												cut.setWeightDefaultUnit(convertDopYieldFieldAcresWeight(cut.getWeight(), dyf.getCropCommodityId(), dopYieldContract, ymucMap));
											}
											updateDeclaredYieldFieldForage(cut, userId);
										}
									}
								}
							}
						}
					}

					// update underwriting comments
					List<UnderwritingComment> uwComments = field.getUwComments();
					if (uwComments != null && !uwComments.isEmpty()) {
						for (UnderwritingComment underwritingComment : uwComments) {
							if (underwritingComment.getDeletedByUserInd() != null
									&& underwritingComment.getDeletedByUserInd()) {
								deleteUnderwritingComment(underwritingComment, userId, authentication);
							} else {
								updateUnderwritingComment(underwritingComment, field.getAnnualFieldDetailId(), null,
										null, userId, authentication);
							}
						}
					}
				}
			}
		} catch (DaoException e) {
			throw new ServiceException("DAO threw an exception", e);
		}

		logger.debug(">updateAnnualField");
	}
	
	private void updateDeclaredYieldContractCommodityForage(String declaredYieldContractGuid,
			DopYieldContract<? extends AnnualField> dopYieldContract, 
			Map<String, YieldMeasUnitConversionDto> ymucMap, String userId) throws DaoException {

		logger.debug("<updateDeclaredYieldContractCommodityForage");
		
		//Delete all existing records
		declaredYieldContractCommodityForageDao.deleteForDeclaredYieldContract(declaredYieldContractGuid);
		
		if (dopYieldContract.getFields() != null && dopYieldContract.getFields().size() > 0) {
			if(dopYieldContract.getDopYieldContractCommodityForageList() != null && dopYieldContract.getDopYieldContractCommodityForageList().size() > 0) {
				// Insert records
				for (DopYieldContractCommodityForage dyccf : dopYieldContract.getDopYieldContractCommodityForageList()) {
					DeclaredYieldContractCommodityForageDto dto = new DeclaredYieldContractCommodityForageDto();
					dopYieldContractFactory.updateDto(dto, dyccf);
					declaredYieldContractCommodityForageDao.insert(dto, userId);
				}
			}
		}

		logger.debug(">updateDeclaredYieldContractCommodityForage");
		
	}
	
	private void updateDeclaredYieldFieldRollupForage(String declaredYieldContractGuid,
			DopYieldContract<? extends AnnualField> dopYieldContract, String userId) throws DaoException {

		logger.debug("<updateDeclaredYieldFieldRollupForage");
		
		//Delete all existing records
		declaredYieldFieldRollupForageDao.deleteForDeclaredYieldContract(declaredYieldContractGuid);
		
		if (dopYieldContract.getFields() != null && dopYieldContract.getFields().size() > 0) {
			if(dopYieldContract.getDopYieldFieldRollupForageList() != null && dopYieldContract.getDopYieldFieldRollupForageList().size() > 0) {
				// Insert records
				for (DopYieldFieldRollupForage dyrf : dopYieldContract.getDopYieldFieldRollupForageList()) {
					DeclaredYieldFieldRollupForageDto dto = new DeclaredYieldFieldRollupForageDto();
					dopYieldContractFactory.updateDto(dto, dyrf);
					declaredYieldFieldRollupForageDao.insert(dto, userId);
				}
			}
		}

		logger.debug(">updateDeclaredYieldFieldRollupForage");
		
	}
	
	// This is only used in unit tests
	public DopYieldContract<?> calculateYieldFieldRollupForageTest(DopYieldContract<? extends AnnualField> dopYieldContract)
			throws ServiceException, DaoException {

		Map<String, YieldMeasUnitConversionDto> ymucMap = loadYieldMeasUnitConversionsMap(
				dopYieldContract.getCropYear(), dopYieldContract.getInsurancePlanId());

		calculateForageDop(dopYieldContract.getDeclaredYieldContractGuid(), dopYieldContract, ymucMap);

		return dopYieldContract;
	}
	
	// This is only used in unit tests
	public DopYieldContract<?> calculateYieldContractCommodityForageTest(DopYieldContract<? extends AnnualField> dopYieldContract)
			throws ServiceException, DaoException {
		
		Map<String, YieldMeasUnitConversionDto> ymucMap = loadYieldMeasUnitConversionsMap(
				dopYieldContract.getCropYear(), dopYieldContract.getInsurancePlanId());

		calculateForageDop(dopYieldContract.getDeclaredYieldContractGuid(), dopYieldContract, ymucMap);

		return dopYieldContract;
	}
	
	private void calculateForageDop(String declaredYieldContractGuid,
			DopYieldContract<? extends AnnualField> dopYieldContract,
			Map<String, YieldMeasUnitConversionDto> ymucMap) {

		logger.debug("<calculateForageDop");
		
		List<DopYieldFieldRollupForage> dopNewYieldFieldRollupForageList = new ArrayList<DopYieldFieldRollupForage>();
		List<DopYieldContractCommodityForage> dopNewYieldContractCommodityForageList = new ArrayList<DopYieldContractCommodityForage>();
		
		if (dopYieldContract.getFields() != null && dopYieldContract.getFields().size() > 0) {

			for (AnnualField field : dopYieldContract.getFields()) {
				
				if (field.getDopYieldFieldForageList() != null && field.getDopYieldFieldForageList().size() > 0) {

					// Loop through each dop field record
					for (DopYieldFieldForage dopField : field.getDopYieldFieldForageList()) {
						
						if(dopField.getCommodityTypeCode() != null) {
							
							//Calculate values
							//Insured Acres
							Double totalFieldAcres = (double)0;
							if(dopField.getFieldAcres() != null && dopField.getFieldAcres() > 0 && Boolean.TRUE.equals(dopField.getIsQuantityInsurableInd())) {
								totalFieldAcres = dopField.getFieldAcres();
							}
							
							//Get a list of eligible cuts
							List<DopYieldFieldForageCut> totalEligileCuts = null;
							if(dopField.getDopYieldFieldForageCuts() != null && dopField.getDopYieldFieldForageCuts().size() > 0) {
								totalEligileCuts = dopField.getDopYieldFieldForageCuts().stream()
										.filter(x -> x.getTotalBalesLoads() != null
												&& x.getTotalBalesLoads() > 0
												&& x.getWeightDefaultUnit() != null && x.getWeightDefaultUnit() > 0
												&& x.getDeletedByUserInd() == false)
										.collect(Collectors.toList());
							}
							
							Double harvestedAcres = (double)0;
							Double quantityHarvestedTons = (double)0;
							Integer totalBales = 0;
							if(totalEligileCuts != null && totalEligileCuts.size() > 0) {
								//Harvested Acres
								if(dopField.getFieldAcres() != null && dopField.getFieldAcres() > 0 && Boolean.TRUE.equals(dopField.getIsQuantityInsurableInd())) {
									harvestedAcres = dopField.getFieldAcres();
								}
	
								Double calculatedQuantityHarvest;
	
								for (DopYieldFieldForageCut cut : totalEligileCuts) {
									Integer bales = cut.getTotalBalesLoads();
									Double moisture = cut.getMoisturePercent();
									Double weight = cut.getWeightDefaultUnit(); //Needs to be the default unit
	
									calculatedQuantityHarvest = bales * weight * (1 - (moisture/100));
									
									//Quantity Harvested Tons
									if(dopField.getPlantDurationTypeCode().equalsIgnoreCase(InventoryServiceEnums.PlantDurationType.PERENNIAL.toString())) {
										//Perennials need another step in the calculation
										calculatedQuantityHarvest = calculatedQuantityHarvest / 0.85;
									}
									
									quantityHarvestedTons = quantityHarvestedTons + calculatedQuantityHarvest;
									totalBales = totalBales + bales;
									
								}
							}
							
							//Add to Yield Field Rollup Forage list ******************************************
							addYieldRollupForageToList(declaredYieldContractGuid, dopNewYieldFieldRollupForageList,
									dopField, totalFieldAcres, harvestedAcres, quantityHarvestedTons, totalBales);
							
							//Add to Yield Contract Commodity Forage list ******************************************
							addContractCommodityForageToList(declaredYieldContractGuid,
									dopNewYieldContractCommodityForageList, dopField, totalFieldAcres, harvestedAcres);
						}
					}
				}
			}
		}
		
		//Add Yield Field Rollup Forage list to contract ******************************************
		dopYieldContract.setDopYieldFieldRollupForageList(dopNewYieldFieldRollupForageList);
		
		//Calculate Contract Commodity values
		calculateContractCommoditiesForage(dopYieldContract, ymucMap, dopNewYieldContractCommodityForageList);
		
		//Add Yield Field Rollup Forage list to contract ******************************************
		dopYieldContract.setDopYieldContractCommodityForageList(dopNewYieldContractCommodityForageList);
		
		logger.debug(">calculateForageDop");

	}

	private void addYieldRollupForageToList(String declaredYieldContractGuid,
			List<DopYieldFieldRollupForage> dopNewYieldFieldRollupForageList, DopYieldFieldForage dopField,
			Double totalFieldAcres, Double harvestedAcres, Double quantityHarvestedTons, Integer totalBales) {
		List<DopYieldFieldRollupForage> dyrfAddedToNewList = new ArrayList<DopYieldFieldRollupForage>();
		
		//Check if the record has been added to the new list already
		if (dopNewYieldFieldRollupForageList != null && dopNewYieldFieldRollupForageList.size() > 0) {
			dyrfAddedToNewList = dopNewYieldFieldRollupForageList.stream()
					.filter(x -> x.getCommodityTypeCode().equals(dopField.getCommodityTypeCode()))
					.collect(Collectors.toList());
		}
		
		DopYieldFieldRollupForage dyrf = null;
		
		if (dyrfAddedToNewList == null || dyrfAddedToNewList.size() == 0) {
			// commodity type not in the list yet - Add it
			dyrf = new DopYieldFieldRollupForage();
			dyrf.setDeclaredYieldContractGuid(declaredYieldContractGuid);
			dyrf.setCommodityTypeCode(dopField.getCommodityTypeCode());
			dyrf.setTotalFieldAcres(totalFieldAcres);
			dyrf.setHarvestedAcres(harvestedAcres);
			dyrf.setTotalBalesLoads(totalBales);
			dyrf.setQuantityHarvestedTons(quantityHarvestedTons);
			//Calculate yield per acre
			Double yieldPerAcre = (double)0;
			if(dyrf.getHarvestedAcres() > 0) {
				yieldPerAcre = dyrf.getQuantityHarvestedTons() / dyrf.getHarvestedAcres();
			}
			dyrf.setYieldPerAcre(yieldPerAcre);								
			dopNewYieldFieldRollupForageList.add(dyrf);

		} else {
			// commodity type already exists in the new list. Add the new values
			dyrf = dyrfAddedToNewList.get(0);
			dyrf.setTotalFieldAcres(totalFieldAcres + dyrf.getTotalFieldAcres());
			dyrf.setHarvestedAcres(harvestedAcres + dyrf.getHarvestedAcres());
			dyrf.setTotalBalesLoads(totalBales + dyrf.getTotalBalesLoads());
			dyrf.setQuantityHarvestedTons(quantityHarvestedTons + dyrf.getQuantityHarvestedTons());
			//Calculate yield per acre
			Double yieldPerAcre = (double)0;
			if(dyrf.getHarvestedAcres() > 0) {
				yieldPerAcre = dyrf.getQuantityHarvestedTons() / dyrf.getHarvestedAcres();
			}
			dyrf.setYieldPerAcre(yieldPerAcre);								
		}
	}

	private void addContractCommodityForageToList(String declaredYieldContractGuid,
			List<DopYieldContractCommodityForage> dopNewYieldContractCommodityForageList, DopYieldFieldForage dopField,
			Double totalFieldAcres, Double harvestedAcres) {
		
		List<DopYieldContractCommodityForage> dyccfAddedToNewList = new ArrayList<DopYieldContractCommodityForage>();
		//Check if the record has been added to the new list already
		if (dopNewYieldContractCommodityForageList != null && dopNewYieldContractCommodityForageList.size() > 0) {
			dyccfAddedToNewList = dopNewYieldContractCommodityForageList.stream()
					.filter(x -> x.getCommodityTypeCode().equals(dopField.getCommodityTypeCode()))
					.collect(Collectors.toList());
		}

		DopYieldContractCommodityForage dyccf = null;
		
		if (dyccfAddedToNewList == null || dyccfAddedToNewList.size() == 0) {
			// commodity type not in the list yet - Add it
			dyccf = new DopYieldContractCommodityForage();
			dyccf.setDeclaredYieldContractGuid(declaredYieldContractGuid);
			dyccf.setCommodityTypeCode(dopField.getCommodityTypeCode());
			dyccf.setTotalFieldAcres(totalFieldAcres);
			dyccf.setHarvestedAcres(harvestedAcres);
			
			dopNewYieldContractCommodityForageList.add(dyccf);

		} else {
			// commodity type already exists in the new list. Add the new values
			dyccf = dyccfAddedToNewList.get(0);
			dyccf.setTotalFieldAcres(totalFieldAcres + dyccf.getTotalFieldAcres());
			dyccf.setHarvestedAcres(harvestedAcres + dyccf.getHarvestedAcres());
		}
	}

	private void calculateContractCommoditiesForage(DopYieldContract<? extends AnnualField> dopYieldContract,
			Map<String, YieldMeasUnitConversionDto> ymucMap,
			List<DopYieldContractCommodityForage> dopNewYieldContractCommodityForageList) {
		
		if(dopNewYieldContractCommodityForageList != null && dopNewYieldContractCommodityForageList.size() > 0) {

			for(DopYieldContractCommodityForage newContractCommodity : dopNewYieldContractCommodityForageList) {
				//Get existing record from contract 
				DopYieldContractCommodityForage existingContractCommodity = getExistingContractCommodity(dopYieldContract, newContractCommodity.getCommodityTypeCode());
				
				Double calculatedQuantityHarvest = null;
				//If it exists, check user entered values and calculate harvested tons and yld/acre
				if(existingContractCommodity != null) {
					newContractCommodity.setCropCommodityId(existingContractCommodity.getCropCommodityId());
					newContractCommodity.setPlantDurationTypeCode(existingContractCommodity.getPlantDurationTypeCode());
					newContractCommodity.setTotalBalesLoads(existingContractCommodity.getTotalBalesLoads());
					newContractCommodity.setWeight(existingContractCommodity.getWeight());
					newContractCommodity.setWeightDefaultUnit(convertDopYieldFieldAcresWeight(existingContractCommodity.getWeight(), existingContractCommodity.getCropCommodityId(), dopYieldContract, ymucMap));
					newContractCommodity.setMoisturePercent(existingContractCommodity.getMoisturePercent());
				
					
					if(newContractCommodity.getTotalBalesLoads() != null && newContractCommodity.getTotalBalesLoads() > 0 &&
							newContractCommodity.getWeightDefaultUnit() != null && newContractCommodity.getWeightDefaultUnit() > 0 &&
							newContractCommodity.getMoisturePercent() != null
							) {
						
						Integer bales = newContractCommodity.getTotalBalesLoads();
						Double moisture = newContractCommodity.getMoisturePercent();
						Double weight = newContractCommodity.getWeightDefaultUnit(); //Needs to be the default unit
						
						//Calculate Quantity Harvested Tons
						calculatedQuantityHarvest = bales * weight * (1 - (moisture/100));
						
						if(newContractCommodity.getPlantDurationTypeCode().equalsIgnoreCase(InventoryServiceEnums.PlantDurationType.PERENNIAL.toString())) {
							//Perennials need another step in the calculation
							calculatedQuantityHarvest = calculatedQuantityHarvest / 0.85;
						}
					}
				}
				
				//Calculate yield/acre
				newContractCommodity.setQuantityHarvestedTons(calculatedQuantityHarvest);
				Double yieldPerAcre = null;
				if(newContractCommodity.getHarvestedAcres() > 0 && calculatedQuantityHarvest != null) {
					yieldPerAcre = calculatedQuantityHarvest / newContractCommodity.getHarvestedAcres();
				}
				newContractCommodity.setYieldPerAcre(yieldPerAcre);

			}
		}
	}	


	private DopYieldContractCommodityForage getExistingContractCommodity(DopYieldContract<? extends AnnualField> dopYieldContract,
			String commodityTypeCode) {
		List<DopYieldContractCommodityForage> dyccfFiltered = new ArrayList<DopYieldContractCommodityForage>();
		
		if (dopYieldContract.getDopYieldContractCommodityForageList() != null && dopYieldContract.getDopYieldContractCommodityForageList().size() > 0) {
			dyccfFiltered = dopYieldContract.getDopYieldContractCommodityForageList().stream()
					.filter(x -> x.getCommodityTypeCode().equals(commodityTypeCode))
					.collect(Collectors.toList());
		}
		
		if (dyccfFiltered != null && dyccfFiltered.size() > 0) {
			// commodity type exists in the list
			return dyccfFiltered.get(0);
		}
		return null;
	}	


	private void updateDeclaredYieldContractCommodity(String declaredYieldContractGuid,
			DopYieldContract<? extends AnnualField> dopYieldContract, DopYieldContractCommodity dopContractCommodity,
			Map<String, YieldMeasUnitConversionDto> ymucMap, String userId) throws DaoException {

		logger.debug("<updateDeclaredYieldContractCommodity");

		// Calculate default units
		if (dopContractCommodity.getSoldYield() == null || dopContractCommodity.getSoldYield() == 0) {
			dopContractCommodity.setSoldYieldDefaultUnit(dopContractCommodity.getSoldYield());
		} else {
			dopContractCommodity.setSoldYieldDefaultUnit(
					convertEstimatedYield(dopYieldContract, dopYieldContract.getDefaultYieldMeasUnitTypeCode(),
							dopContractCommodity.getCropCommodityId(), dopContractCommodity.getSoldYield(), ymucMap));
		}

		if (dopContractCommodity.getStoredYield() == null || dopContractCommodity.getStoredYield() == 0) {
			dopContractCommodity.setStoredYieldDefaultUnit(dopContractCommodity.getStoredYield());
		} else {
			dopContractCommodity.setStoredYieldDefaultUnit(
					convertEstimatedYield(dopYieldContract, dopYieldContract.getDefaultYieldMeasUnitTypeCode(),
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
			dopYieldContractFactory.updateDto(dto, dopContractCommodity);

			declaredYieldContractCommodityDao.update(dto, userId);
		}

		logger.debug(">updateDeclaredYieldContractCommodity");
	}

	private void insertDeclaredYieldContractCommodity(String declaredYieldContractGuid,
			DopYieldContractCommodity dopContractCommodity, String userId) throws DaoException {

		logger.debug("<insertDeclaredYieldContractCommodity");

		DeclaredYieldContractCommodityDto dto = new DeclaredYieldContractCommodityDto();

		dopYieldContractFactory.updateDto(dto, dopContractCommodity);

		dto.setDeclaredYieldContractCommodityGuid(null);
		dto.setDeclaredYieldContractGuid(declaredYieldContractGuid);

		declaredYieldContractCommodityDao.insert(dto, userId);

		logger.debug(">insertDeclaredYieldContractCommodity");

	}

	private String insertDeclaredYieldContract(DopYieldContract<? extends AnnualField> dopYieldContract, String userId)
			throws DaoException {

		DeclaredYieldContractDto dto = new DeclaredYieldContractDto();
		dopYieldContractFactory.updateDto(dto, dopYieldContract, userId);
		dto.setDeclaredYieldContractGuid(null);
		declaredYieldContractDao.insert(dto, userId);

		return dto.getDeclaredYieldContractGuid();
	}

	private String insertDeclaredYieldField(DopYieldFieldGrain dopYieldField, String userId) throws DaoException {

		DeclaredYieldFieldDto dto = new DeclaredYieldFieldDto();
		dopYieldContractFactory.updateDto(dto, dopYieldField);

		dto.setDeclaredYieldFieldGuid(null);
		dto.setInventoryFieldGuid(dopYieldField.getInventoryFieldGuid());

		declaredYieldFieldDao.insert(dto, userId);

		return dto.getDeclaredYieldFieldGuid();
	}

	private String insertDeclaredYieldFieldForage(DopYieldFieldForageCut dopYieldFieldForage, String userId) throws DaoException {

		DeclaredYieldFieldForageDto dto = new DeclaredYieldFieldForageDto();
		dopYieldContractFactory.updateDto(dto, dopYieldFieldForage);

		dto.setDeclaredYieldFieldForageGuid(null);
		dto.setInventoryFieldGuid(dopYieldFieldForage.getInventoryFieldGuid());

		declaredYieldFieldForageDao.insert(dto, userId);

		return dto.getDeclaredYieldFieldForageGuid();
	}
	
	/// converts a value (Grain = acres, Forage = weight) into the default units and returns the converted value
	private Double convertDopYieldFieldAcresWeight(Double valueToConvert, Integer cropCommodityId,
			DopYieldContract<? extends AnnualField> dopYieldContract, Map<String, YieldMeasUnitConversionDto> ymucMap)
			throws ServiceException {

		logger.debug("<convertDopYieldFieldAcresWeight");

		Double estYieldDefaultUnit = null;
		String srcUnit = dopYieldContract.getEnteredYieldMeasUnitTypeCode();
		String targetUnit = dopYieldContract.getDefaultYieldMeasUnitTypeCode();

		if (srcUnit.equals(targetUnit)) {
			estYieldDefaultUnit = valueToConvert;
		} else {
			YieldMeasUnitConversionDto ymucDto = lookupYieldMeasUnitConversion(ymucMap,
					cropCommodityId, srcUnit, targetUnit);

			if (ymucDto == null) {

				// Cannot calculate conversion.
				throw new ServiceException("No conversion is defined for commodity id "
						+ cropCommodityId + ", Src Unit " + srcUnit + ", Target Unit " + targetUnit
						+ ", for crop year " + dopYieldContract.getCropYear());
			} else {
				estYieldDefaultUnit = calculateYieldMeasUnitConversion(ymucDto,
						valueToConvert, multiplyUnitCoversion);
			}
		}
		
		logger.debug(">convertDopYieldFieldAcresWeight");

		return estYieldDefaultUnit;
	}	

	@Override
	public DopYieldContract<? extends AnnualField> updateDopYieldContract(String declaredYieldContractGuid,
			String optimisticLock, DopYieldContract<? extends AnnualField> dopYieldContract,
			FactoryContext factoryContext, WebAdeAuthentication authentication) throws ServiceException,
			NotFoundException, ForbiddenException, ConflictException, ValidationFailureException {

		logger.debug("<updateDopYieldContract");

		DopYieldContract<? extends AnnualField> result = null;
		String userId = getUserId(authentication);

		try {
			List<Message> errors = new ArrayList<Message>();
			// errors.addAll(modelValidator.validateInsuranceClaim(insuranceClaim)); // TODO

			if (!errors.isEmpty()) {
				throw new ValidationFailureException(errors);
			}

			Map<String, YieldMeasUnitConversionDto> ymucMap = loadYieldMeasUnitConversionsMap(
					dopYieldContract.getCropYear(), dopYieldContract.getInsurancePlanId());

			updateDeclaredYieldContract(dopYieldContract, userId);

			// update annual field values
			updateAnnualField(dopYieldContract, ymucMap, authentication);

			if ( InsurancePlans.GRAIN.getInsurancePlanId().equals(dopYieldContract.getInsurancePlanId()) ) {
				// Estimated Yield/Commodity (Field Rollup)
				updateDeclaredYieldFieldRollup(dopYieldContract.getDeclaredYieldContractGuid(), dopYieldContract, userId,
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
				calculateForageDop(declaredYieldContractGuid, dopYieldContract, ymucMap);

				//Save commodity totals
				updateDeclaredYieldContractCommodityForage(declaredYieldContractGuid, dopYieldContract, ymucMap, userId);
				
				//Save field rollups
				updateDeclaredYieldFieldRollupForage(declaredYieldContractGuid, dopYieldContract, userId);

			} else {
				throw new ServiceException("Insurance Plan must be GRAIN or FORAGE");
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
			DopYieldContract<? extends AnnualField> dopYieldContract, String userId,
			WebAdeAuthentication authentication) throws DaoException, ServiceException {

		List<UnderwritingComment> contractUwComments = dopYieldContract.getUwComments();
		if (contractUwComments != null && !contractUwComments.isEmpty()) {
			for (UnderwritingComment contractUnderwritingComment : contractUwComments) {
				if (contractUnderwritingComment.getDeletedByUserInd() != null
						&& contractUnderwritingComment.getDeletedByUserInd()) {
					deleteUnderwritingComment(contractUnderwritingComment, userId, authentication);
				} else {
					updateUnderwritingComment(contractUnderwritingComment, null,
							dopYieldContract.getGrowerContractYearId(), declaredYieldContractGuid, userId,
							authentication);
				}
			}
		}

	}

	private void updateUnderwritingComment(UnderwritingComment underwritingComment, Integer annualFieldDetailId,
			Integer growerContractYearId, String declaredYieldContractGuid, String userId,
			WebAdeAuthentication authentication) throws DaoException, ServiceException {

		UnderwritingCommentDto dto = null;

		if (underwritingComment.getUnderwritingCommentGuid() != null) {
			dto = underwritingCommentDao.fetch(underwritingComment.getUnderwritingCommentGuid());
		}

		if (dto == null) {
			// Insert if it doesn't exist
			insertUnderwritingComment(underwritingComment, annualFieldDetailId, growerContractYearId,
					declaredYieldContractGuid, userId);
		} else {

			if (!dto.getUnderwritingComment().equals(underwritingComment.getUnderwritingComment()) || !dto
					.getUnderwritingCommentTypeCode().equals(underwritingComment.getUnderwritingCommentTypeCode())) {

				// Check that user is authorized to edit this comment.
				// Note that this could return null if the current user or create user cannot be
				// determined.
				Boolean userCanEditComment = inventoryContractFactory.checkUserCanEditComment(dto, authentication);
				if (!Boolean.TRUE.equals(userCanEditComment)) {
					logger.error("User " + userId + " attempted to edit comment "
							+ underwritingComment.getUnderwritingCommentGuid() + " created by " + dto.getCreateUser());
					throw new ServiceException("The current user is not authorized to edit this comment.");
				}

			}

			inventoryContractFactory.updateDto(dto, underwritingComment);

			underwritingCommentDao.update(dto, userId);
		}

	}

	private String insertUnderwritingComment(UnderwritingComment underwritingComment, Integer annualFieldDetailId,
			Integer growerContractYearId, String declaredYieldContractGuid, String userId) throws DaoException {

		UnderwritingCommentDto dto = new UnderwritingCommentDto();
		inventoryContractFactory.updateDto(dto, underwritingComment);

		dto.setUnderwritingCommentGuid(null);
		dto.setAnnualFieldDetailId(annualFieldDetailId);
		dto.setGrowerContractYearId(growerContractYearId);
		dto.setDeclaredYieldContractGuid(declaredYieldContractGuid);

		underwritingCommentDao.insert(dto, userId);

		return dto.getUnderwritingCommentGuid();
	}

	private void deleteUnderwritingComment(UnderwritingComment underwritingComment, String userId,
			WebAdeAuthentication authentication) throws NotFoundDaoException, DaoException {
		logger.debug("<deleteUnderwritingComment");

		UnderwritingCommentDto dto = null;

		if (underwritingComment.getUnderwritingCommentGuid() != null) {
			dto = underwritingCommentDao.fetch(underwritingComment.getUnderwritingCommentGuid());
		}

		if (dto != null) {
			// Check that user is authorized to delete this comment.
			// Note that this could return false if the current user or create user cannot
			// be determined.
			Boolean userCanDeleteComment = inventoryContractFactory.checkUserCanDeleteComment(dto, authentication);
			if (!Boolean.TRUE.equals(userCanDeleteComment)) {
				logger.error("User " + userId + " attempted to delete comment " + dto.getUnderwritingCommentGuid()
						+ " created by " + dto.getCreateUser());
				throw new ServiceException("The current user is not authorized to delete this comment.");
			}

			underwritingCommentDao.delete(underwritingComment.getUnderwritingCommentGuid());
		}

		logger.debug(">deleteUnderwritingComment");
	}

	private void updateDeclaredYieldContract(DopYieldContract<? extends AnnualField> dopYieldContract, String userId)
			throws DaoException, NotFoundException {

		DeclaredYieldContractDto dto = declaredYieldContractDao.fetch(dopYieldContract.getDeclaredYieldContractGuid());

		if (dto == null) {
			throw new NotFoundException(
					"Did not find the dop yield contract: " + dopYieldContract.getDeclaredYieldContractGuid());
		}

		dopYieldContractFactory.updateDto(dto, dopYieldContract, userId);
		declaredYieldContractDao.update(dto, userId);
	}

	private String updateDeclaredYieldFieldForage(DopYieldFieldForageCut dopYieldFieldForage, String userId) throws DaoException {

		logger.debug("<updateDeclaredYieldFieldForage");

		DeclaredYieldFieldForageDto dto = null;

		if (dopYieldFieldForage.getDeclaredYieldFieldForageGuid() != null) {
			dto = declaredYieldFieldForageDao.fetch(dopYieldFieldForage.getDeclaredYieldFieldForageGuid());
		}

		String declaredYieldFieldForageGuid = null;

		if (dto == null) {
			// Insert if it doesn't exist
			declaredYieldFieldForageGuid = insertDeclaredYieldFieldForage(dopYieldFieldForage, userId);
		} else {
			declaredYieldFieldForageGuid = dto.getDeclaredYieldFieldForageGuid();

			dopYieldContractFactory.updateDto(dto, dopYieldFieldForage);

			declaredYieldFieldForageDao.update(dto, userId);
		}
		
		logger.debug(">updateDeclaredYieldFieldForage");

		return declaredYieldFieldForageGuid;
	}
	
	private String updateDeclaredYieldField(DopYieldFieldGrain dopYieldField, String userId) throws DaoException {

		logger.debug("<updateDeclaredYieldField");

		DeclaredYieldFieldDto dto = null;

		if (dopYieldField.getDeclaredYieldFieldGuid() != null) {
			dto = declaredYieldFieldDao.fetch(dopYieldField.getDeclaredYieldFieldGuid());
		}

		String declaredYieldFieldGuid = null;

		if (dto == null) {
			// Insert if it doesn't exist
			declaredYieldFieldGuid = insertDeclaredYieldField(dopYieldField, userId);
		} else {
			declaredYieldFieldGuid = dto.getDeclaredYieldFieldGuid();

			dopYieldContractFactory.updateDto(dto, dopYieldField);

			declaredYieldFieldDao.update(dto, userId);
		}
		
		logger.debug(">updateDeclaredYieldField");

		return declaredYieldFieldGuid;
	}

	private void updateDeclaredYieldFieldRollup(String declaredYieldContractGuid,
			DopYieldContract<? extends AnnualField> dopYieldContract, String userId,
			Map<String, YieldMeasUnitConversionDto> unitConversionMap) throws DaoException {

		// Delete rollup records
		declaredYieldFieldRollupDao.deleteForDeclaredYieldContract(declaredYieldContractGuid);

		if (dopYieldContract.getFields() != null && dopYieldContract.getFields().size() > 0) {
			// Convert units and calculate rollup values
			calculateYieldRollup(declaredYieldContractGuid, dopYieldContract, unitConversionMap);
			// Insert records
			for (DopYieldFieldRollup dyfr : dopYieldContract.getDopYieldFieldRollupList()) {
				DeclaredYieldFieldRollupDto dto = new DeclaredYieldFieldRollupDto();
				dopYieldContractFactory.updateDto(dto, dyfr);
				declaredYieldFieldRollupDao.insert(dto, userId);
			}
		}
	}

	// This is only used in unit tests
	public DopYieldContract<?> calculateYieldRollupTest(DopYieldContract<? extends AnnualField> dopYieldContract)
			throws ServiceException, DaoException {

		Map<String, YieldMeasUnitConversionDto> ymucMap = loadYieldMeasUnitConversionsMap(
				dopYieldContract.getCropYear(), dopYieldContract.getInsurancePlanId());

		calculateYieldRollup(dopYieldContract.getDeclaredYieldContractGuid(), dopYieldContract, ymucMap);

		return dopYieldContract;
	}

	private void calculateYieldRollup(String declaredYieldContractGuid,
			DopYieldContract<? extends AnnualField> dopYieldContract, Map<String, YieldMeasUnitConversionDto> ymucMap) {

		// Reset the list
		dopYieldContract.setDopYieldFieldRollupList(null);
		List<DopYieldFieldRollup> dopYieldFieldRollupList = new ArrayList<DopYieldFieldRollup>();

		if (dopYieldContract.getFields() != null && dopYieldContract.getFields().size() > 0) {

			for (AnnualField field : dopYieldContract.getFields()) {

				if (field.getDopYieldFieldGrainList() != null && field.getDopYieldFieldGrainList().size() > 0) {
					// Loop through each dop field record

					for (DopYieldFieldGrain dopField : field.getDopYieldFieldGrainList()) {
						// Only necessary if the yield is not null
						if (dopField.getEstimatedYieldPerAcre() != null) {

							List<DopYieldFieldRollup> dyfFiltered = null;

							if (dopYieldFieldRollupList != null && dopYieldFieldRollupList.size() > 0) {
								dyfFiltered = dopYieldFieldRollupList.stream()
										.filter(x -> x.getCropCommodityId().equals(dopField.getCropCommodityId())
												&& x.getIsPedigreeInd().equals(dopField.getIsPedigreeInd()))
										.collect(Collectors.toList());
							}

							double seededAcres = notNull(dopField.getSeededAcres(), (double) 0);
							// Multiply estimated yield per acre with seeded acres = total estimated yield
							double totalEstimatedYield = seededAcres * dopField.getEstimatedYieldPerAcre();

							if (dyfFiltered == null || dyfFiltered.size() == 0) {
								// commodity/is pedigree not in the list yet - Add it
								DopYieldFieldRollup dopYieldFieldRollup = new DopYieldFieldRollup();
								dopYieldFieldRollup.setDeclaredYieldContractGuid(declaredYieldContractGuid);
								dopYieldFieldRollup.setCropCommodityId(dopField.getCropCommodityId());
								dopYieldFieldRollup.setIsPedigreeInd(dopField.getIsPedigreeInd());
								dopYieldFieldRollup.setTotalAcres(seededAcres);
								dopYieldFieldRollup.setTotalYield(totalEstimatedYield);
								dopYieldFieldRollupList.add(dopYieldFieldRollup);

							} else {
								// commodity already exists in the list. Add the new values
								DopYieldFieldRollup dopYieldFieldRollup = dyfFiltered.get(0);
								dopYieldFieldRollup.setTotalAcres(seededAcres + dopYieldFieldRollup.getTotalAcres());
								dopYieldFieldRollup
										.setTotalYield(totalEstimatedYield + dopYieldFieldRollup.getTotalYield());
							}
						}
					}
				}
			}
		}

		dopYieldContract.setDopYieldFieldRollupList(dopYieldFieldRollupList);

		// Convert units
		if (dopYieldContract.getDopYieldFieldRollupList() != null
				&& dopYieldContract.getDopYieldFieldRollupList().size() > 0) {
			for (DopYieldFieldRollup dyfr : dopYieldContract.getDopYieldFieldRollupList()) {

				if (dyfr.getTotalYield() != null && dyfr.getTotalYield() > 0) {
					// Divide total estimated yield with total acres = total estimated yield per
					// acre
					double estimatedYieldPerAcre = dyfr.getTotalYield() / dyfr.getTotalAcres();

					dyfr.setEstimatedYieldPerAcreTonnes(convertEstimatedYield(dopYieldContract, "TONNE",
							dyfr.getCropCommodityId(), estimatedYieldPerAcre, ymucMap));

					dyfr.setEstimatedYieldPerAcreBushels(convertEstimatedYield(dopYieldContract, "BUSHEL",
							dyfr.getCropCommodityId(), estimatedYieldPerAcre, ymucMap));
				} else {
					dyfr.setEstimatedYieldPerAcreTonnes(null);
					dyfr.setEstimatedYieldPerAcreBushels(null);
				}
			}
		}
	}

	// This is only used in unit tests
	public double convertEstimatedYieldTest(DopYieldContract<? extends AnnualField> dopYieldContract, String targetUnit,
			Integer cropCommodityId, double valueToConvert) throws ServiceException, DaoException {

		Map<String, YieldMeasUnitConversionDto> ymucMap = loadYieldMeasUnitConversionsMap(
				dopYieldContract.getCropYear(), dopYieldContract.getInsurancePlanId());

		return convertEstimatedYield(dopYieldContract, targetUnit, cropCommodityId, valueToConvert, ymucMap);

	}

	private double convertEstimatedYield(DopYieldContract<? extends AnnualField> dopYieldContract, String targetUnit,
			Integer cropCommodityId, double valueToConvert, Map<String, YieldMeasUnitConversionDto> ymucMap) {

		String enteredUnit = dopYieldContract.getEnteredYieldMeasUnitTypeCode();

		if (enteredUnit.equals(targetUnit)) {
			// No need to convert
			return valueToConvert;
		} else {
			// At this point the entered unit is not equal to the unit to convert to

			YieldMeasUnitConversionDto ymucDto = lookupYieldMeasUnitConversion(ymucMap, cropCommodityId, enteredUnit,
					targetUnit);

			if (ymucDto == null) {

				// Cannot calculate conversion.
				throw new ServiceException(
						"No conversion is defined for commodity id " + cropCommodityId + ", Src Unit " + enteredUnit
								+ ", Target Unit " + targetUnit + ", for crop year " + dopYieldContract.getCropYear());
			} else {
				return calculateYieldMeasUnitConversion(ymucDto, valueToConvert, multiplyUnitCoversion);
			}
		}
	}

	@Override
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
			
		} else {
			throw new ServiceException("Insurance Plan must be GRAIN or FORAGE");
		}

		underwritingCommentDao.deleteForDeclaredYieldContractGuid(declaredYieldContractGuid);
		declaredYieldContractDao.delete(declaredYieldContractGuid);
	}

	@Override
	public YieldMeasUnitTypeCodeList<? extends YieldMeasUnitTypeCode> getYieldMeasUnitTypeCodeList(
			Integer insurancePlanId, FactoryContext context, WebAdeAuthentication authentication)
			throws ServiceException, DaoException {

		List<YieldMeasUnitTypeCodeDto> dtos = yieldMeasUnitTypeCodeDao.selectByPlan(insurancePlanId);

		YieldMeasUnitTypeCodeList<? extends YieldMeasUnitTypeCode> result = yieldMeasUnitTypeCodeFactory
				.getYieldMeasUnitTypeCodeList(dtos);

		return result;
	}

	private Map<String, YieldMeasUnitConversionDto> loadYieldMeasUnitConversionsMap(Integer cropYear,
			Integer insurancePlanId) throws DaoException, ServiceException {

		Map<String, YieldMeasUnitConversionDto> dtoMap = new HashMap<String, YieldMeasUnitConversionDto>();
		List<YieldMeasUnitConversionDto> dtoList = yieldMeasUnitConversionDao.selectByYearAndPlan(cropYear,
				insurancePlanId);

		if (dtoList != null && !dtoList.isEmpty()) {
			for (YieldMeasUnitConversionDto dto : dtoList) {
				String key = dto.getCropCommodityId() + "::" + dto.getSrcYieldMeasUnitTypeCode() + "::"
						+ dto.getTargetYieldMeasUnitTypeCode();

				if (dtoMap.containsKey(key)) {
					// Should never happen; there is a duplicate conversion.
					throw new ServiceException("Found duplicate conversion for commodity id " + dto.getCropCommodityId()
							+ ", Src Unit " + dto.getSrcYieldMeasUnitTypeCode() + ", Target Unit "
							+ dto.getTargetYieldMeasUnitTypeCode() + ", for Crop Year " + cropYear);
				} else {
					dtoMap.put(key, dto);
				}
			}
		}

		return dtoMap;
	}

	private boolean multiplyUnitCoversion;

	// Lookup function for the map returned by loadYieldMeasUnitConversionsMap
	private YieldMeasUnitConversionDto lookupYieldMeasUnitConversion(Map<String, YieldMeasUnitConversionDto> ymucMap,
			Integer cropCommodityId, String srcUnit, String targetUnit) {
		multiplyUnitCoversion = true;
		String lookupKey = cropCommodityId + "::" + srcUnit + "::" + targetUnit;
		YieldMeasUnitConversionDto ymucDto = ymucMap.get(lookupKey);
		if (ymucDto == null) {
			// If there is no such key in the list, try the other way around
			lookupKey = cropCommodityId + "::" + targetUnit + "::" + srcUnit;
			ymucDto = ymucMap.get(lookupKey);
			multiplyUnitCoversion = false;
		}

		return ymucDto;
	}

	// Calculate srcValue converted to the target yield meas unit specified by
	// ymucDto.
	// If srcValue is null, targetValue is null.
	// Otherwise the calculated targetValue is returned.
	private Double calculateYieldMeasUnitConversion(YieldMeasUnitConversionDto ymucDto, Double srcValue,
			boolean multiply) {

		// Calculate value in target units.
		Double targetValue = null;

		if (srcValue != null) {
			if (multiply) {
				targetValue = srcValue * ymucDto.getConversionFactor();
			} else {
				targetValue = srcValue / ymucDto.getConversionFactor();
			}
		}

		return targetValue;
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

	private Double notNull(Double value, Double defaultValue) {
		return (value == null) ? defaultValue : value;
	}

	private Integer notNull(Integer value, Integer defaultValue) {
		return (value == null) ? defaultValue : value;
	}

	private String notNull(String value, String defaultValue) {
		return (value == null) ? defaultValue : value;
	}

}
