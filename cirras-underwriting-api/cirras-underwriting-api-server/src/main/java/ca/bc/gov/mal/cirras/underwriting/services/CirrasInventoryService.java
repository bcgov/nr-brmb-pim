package ca.bc.gov.mal.cirras.underwriting.services;

import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import ca.bc.gov.mal.cirras.policies.api.rest.client.v1.CirrasPolicyService;
import ca.bc.gov.mal.cirras.policies.api.rest.client.v1.CirrasPolicyServiceException;
import ca.bc.gov.mal.cirras.policies.api.rest.v1.resource.ProductListRsrc;
import ca.bc.gov.mal.cirras.underwriting.data.resources.AddFieldValidationRsrc;
import ca.bc.gov.mal.cirras.underwriting.data.resources.AnnualFieldRsrc;
import ca.bc.gov.mal.cirras.underwriting.data.resources.InventoryContractListRsrc;
import ca.bc.gov.mal.cirras.underwriting.data.resources.InventoryContractRsrc;
import ca.bc.gov.mal.cirras.underwriting.data.resources.LegalLandListRsrc;
import ca.bc.gov.mal.cirras.underwriting.data.resources.RemoveFieldValidationRsrc;
import ca.bc.gov.mal.cirras.underwriting.data.resources.RenameLegalValidationRsrc;
import ca.bc.gov.mal.cirras.underwriting.data.resources.ReplaceLegalValidationRsrc;
import ca.bc.gov.mal.cirras.underwriting.data.models.InventoryContractCommodity;
import ca.bc.gov.mal.cirras.underwriting.data.models.InventoryField;
import ca.bc.gov.mal.cirras.underwriting.data.models.InventorySeededForage;
import ca.bc.gov.mal.cirras.underwriting.data.models.InventorySeededGrain;
import ca.bc.gov.mal.cirras.underwriting.data.models.InventoryUnseeded;
import ca.bc.gov.mal.cirras.underwriting.data.models.UnderwritingComment;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.AnnualFieldDetailDao;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.CommodityMaturityScaleDao;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.ContractedFieldDetailDao;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.CropCommodityDao;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.DeclaredYieldContractDao;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.DeclaredYieldFieldCommodityBerriesDao;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.DeclaredYieldFieldDao;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.DeclaredYieldFieldForageDao;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.FieldDao;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.InventoryContractCommodityDao;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.InventoryContractDao;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.InventoryCoverageTotalForageDao;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.InventoryFieldDao;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.InventorySeededForageDao;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.InventorySeededGrainDao;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.InventoryUnseededDao;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.LegalLandDao;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.InsurancePlanDao;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.InventoryBerriesDao;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.InventoryContractCommodityBerriesDao;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.PolicyDao;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.UnderwritingCommentDao;
import ca.bc.gov.mal.cirras.underwriting.data.entities.AnnualFieldDetailDto;
import ca.bc.gov.mal.cirras.underwriting.data.entities.CommodityMaturityScaleDto;
import ca.bc.gov.mal.cirras.underwriting.data.entities.ContractedFieldDetailDto;
import ca.bc.gov.mal.cirras.underwriting.data.entities.CropCommodityDto;
import ca.bc.gov.mal.cirras.underwriting.data.entities.DeclaredYieldContractDto;
import ca.bc.gov.mal.cirras.underwriting.data.entities.FieldDto;
import ca.bc.gov.mal.cirras.underwriting.data.entities.InventoryContractCommodityDto;
import ca.bc.gov.mal.cirras.underwriting.data.entities.InventoryContractDto;
import ca.bc.gov.mal.cirras.underwriting.data.entities.InventoryCoverageTotalForageDto;
import ca.bc.gov.mal.cirras.underwriting.data.entities.InventoryFieldDto;
import ca.bc.gov.mal.cirras.underwriting.data.entities.InventorySeededForageDto;
import ca.bc.gov.mal.cirras.underwriting.data.entities.InventorySeededGrainDto;
import ca.bc.gov.mal.cirras.underwriting.data.entities.InventoryUnseededDto;
import ca.bc.gov.mal.cirras.underwriting.data.entities.LegalLandDto;
import ca.bc.gov.mal.cirras.underwriting.data.entities.InsurancePlanDto;
import ca.bc.gov.mal.cirras.underwriting.data.entities.InventoryBerriesDto;
import ca.bc.gov.mal.cirras.underwriting.data.entities.InventoryContractCommodityBerriesDto;
import ca.bc.gov.mal.cirras.underwriting.data.entities.PolicyDto;
import ca.bc.gov.mal.cirras.underwriting.data.entities.UnderwritingCommentDto;
import ca.bc.gov.nrs.wfone.common.model.Message;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.TooManyRecordsException;
import ca.bc.gov.nrs.wfone.common.persistence.dto.PagedDtos;
import ca.bc.gov.nrs.wfone.common.service.api.ConflictException;
import ca.bc.gov.nrs.wfone.common.service.api.ForbiddenException;
import ca.bc.gov.nrs.wfone.common.service.api.MaxResultsExceededException;
import ca.bc.gov.nrs.wfone.common.service.api.NotFoundException;
import ca.bc.gov.nrs.wfone.common.service.api.ServiceException;
import ca.bc.gov.nrs.wfone.common.service.api.ValidationFailureException;
import ca.bc.gov.nrs.wfone.common.service.api.model.factory.FactoryContext;
import ca.bc.gov.nrs.wfone.common.webade.authentication.WebAdeAuthentication;
import ca.bc.gov.mal.cirras.underwriting.data.assemblers.InventoryContractRsrcFactory;
import ca.bc.gov.mal.cirras.underwriting.data.assemblers.LegalLandRsrcFactory;
import ca.bc.gov.mal.cirras.underwriting.data.assemblers.UwContractRsrcFactory;
import ca.bc.gov.mal.cirras.underwriting.services.reports.JasperReportService;
import ca.bc.gov.mal.cirras.underwriting.services.reports.JasperReportServiceException;
import ca.bc.gov.mal.cirras.underwriting.services.utils.BerriesService;
import ca.bc.gov.mal.cirras.underwriting.services.utils.FieldService;
import ca.bc.gov.mal.cirras.underwriting.services.utils.GrainForageInventoryService;
import ca.bc.gov.mal.cirras.underwriting.services.utils.InventoryServiceEnums;
import ca.bc.gov.mal.cirras.underwriting.services.utils.InventoryServiceEnums.InsurancePlans;
import ca.bc.gov.mal.cirras.underwriting.services.utils.InventoryServiceEnums.InventoryReportType;
import ca.bc.gov.mal.cirras.underwriting.services.utils.LandUpdateTypes;

public class CirrasInventoryService {

	private static final Logger logger = LoggerFactory.getLogger(CirrasInventoryService.class);

	private Properties applicationProperties;

	// factories
	private InventoryContractRsrcFactory inventoryContractRsrcFactory;
	private LegalLandRsrcFactory legalLandRsrcFactory;
	private UwContractRsrcFactory uwContractRsrcFactory;

	// daos
	private InventoryContractCommodityDao inventoryContractCommodityDao;
	private InventoryCoverageTotalForageDao inventoryCoverageTotalForageDao;
	private InventoryContractCommodityBerriesDao inventoryContractCommodityBerriesDao;
	private InventoryContractDao inventoryContractDao;
	private InventoryFieldDao inventoryFieldDao;
	private InventorySeededGrainDao inventorySeededGrainDao;
	private InventoryUnseededDao inventoryUnseededDao;
	private InventorySeededForageDao inventorySeededForageDao;
	private InventoryBerriesDao inventoryBerriesDao;
	private PolicyDao policyDao;
	private UnderwritingCommentDao underwritingCommentDao;
	private LegalLandDao legalLandDao;
	private FieldDao fieldDao;
	private AnnualFieldDetailDao annualFieldDetailDao;
	private ContractedFieldDetailDao contractedFieldDetailDao;
	private InsurancePlanDao insurancePlanDao;
	private DeclaredYieldFieldDao declaredYieldFieldDao;
	private DeclaredYieldFieldForageDao declaredYieldFieldForageDao;
	private DeclaredYieldContractDao declaredYieldContractDao;
	private CropCommodityDao cropCommodityDao;
	private DeclaredYieldFieldCommodityBerriesDao declaredYieldFieldCommodityBerriesDao;
	private CommodityMaturityScaleDao commodityMaturityScaleDao;

	// services
	private CirrasPolicyService cirrasPolicyService;

	// Jasper Reports
	private JasperReportService jasperReportService;
	
	// utils
	// @Autowired
	private BerriesService berriesService;
	private FieldService fieldService;
	private GrainForageInventoryService grainForageInventoryService;

	public static final String MaximumResultsProperty = "maximum.results";

	public static final int DefaultMaximumResults = 800;

	public void setBerriesService(BerriesService berriesService) {
		this.berriesService = berriesService;
	}
	
	public void setGrainForageInventoryService(GrainForageInventoryService grainForageInventoryService) {
		this.grainForageInventoryService = grainForageInventoryService;
	}

	public void setFieldService(FieldService fieldService) {
		this.fieldService = fieldService;
	}
	
	public void setApplicationProperties(Properties applicationProperties) {
		this.applicationProperties = applicationProperties;
	}

	public void setInventoryContractRsrcFactory(InventoryContractRsrcFactory inventoryContractRsrcFactory) {
		this.inventoryContractRsrcFactory = inventoryContractRsrcFactory;
	}

	public void setLegalLandRsrcFactory(LegalLandRsrcFactory legalLandRsrcFactory) {
		this.legalLandRsrcFactory = legalLandRsrcFactory;
	}

	public void setUwContractRsrcFactory(UwContractRsrcFactory uwContractRsrcFactory) {
		this.uwContractRsrcFactory = uwContractRsrcFactory;
	}

	public void setInventoryContractCommodityDao(InventoryContractCommodityDao inventoryContractCommodityDao) {
		this.inventoryContractCommodityDao = inventoryContractCommodityDao;
	}

	public void setInventoryCoverageTotalForageDao(InventoryCoverageTotalForageDao inventoryCoverageTotalForageDao) {
		this.inventoryCoverageTotalForageDao = inventoryCoverageTotalForageDao;
	}

	public void setInventoryContractCommodityBerriesDao(InventoryContractCommodityBerriesDao inventoryContractCommodityBerriesDao) {
		this.inventoryContractCommodityBerriesDao = inventoryContractCommodityBerriesDao;
	}
	
	public void setInventoryContractDao(InventoryContractDao inventoryContractDao) {
		this.inventoryContractDao = inventoryContractDao;
	}

	public void setInventoryFieldDao(InventoryFieldDao inventoryFieldDao) {
		this.inventoryFieldDao = inventoryFieldDao;
	}

	public void setInventorySeededGrainDao(InventorySeededGrainDao inventorySeededGrainDao) {
		this.inventorySeededGrainDao = inventorySeededGrainDao;
	}

	public void setInventoryUnseededDao(InventoryUnseededDao inventoryUnseededDao) {
		this.inventoryUnseededDao = inventoryUnseededDao;
	}
	
	public void setInventorySeededForageDao(InventorySeededForageDao inventorySeededForageDao) {
		this.inventorySeededForageDao = inventorySeededForageDao;
	}

	public void setInventoryBerriesDao(InventoryBerriesDao inventoryBerriesDao) {
		this.inventoryBerriesDao = inventoryBerriesDao;
	}

	public void setPolicyDao(PolicyDao policyDao) {
		this.policyDao = policyDao;
	}

	public void setUnderwritingCommentDao(UnderwritingCommentDao underwritingCommentDao) {
		this.underwritingCommentDao = underwritingCommentDao;
	}

	public void setLegalLandDao(LegalLandDao legalLandDao) {
		this.legalLandDao = legalLandDao;
	}

	public void setFieldDao(FieldDao fieldDao) {
		this.fieldDao = fieldDao;
	}

	public void setAnnualFieldDetailDao(AnnualFieldDetailDao annualFieldDetailDao) {
		this.annualFieldDetailDao = annualFieldDetailDao;
	}

	public void setContractedFieldDetailDao(ContractedFieldDetailDao contractedFieldDetailDao) {
		this.contractedFieldDetailDao = contractedFieldDetailDao;
	}

	public void setInsurancePlanDao(InsurancePlanDao insurancePlanDao) {
		this.insurancePlanDao = insurancePlanDao;
	}

	public void setDeclaredYieldFieldDao(DeclaredYieldFieldDao declaredYieldFieldDao) {
		this.declaredYieldFieldDao = declaredYieldFieldDao;
	}

	public void setDeclaredYieldFieldForageDao(DeclaredYieldFieldForageDao declaredYieldFieldForageDao) {
		this.declaredYieldFieldForageDao = declaredYieldFieldForageDao;
	}	
	
	public void setDeclaredYieldContractDao(DeclaredYieldContractDao declaredYieldContractDao) {
		this.declaredYieldContractDao = declaredYieldContractDao;
	}

	public void setCropCommodityDao(CropCommodityDao cropCommodityDao) {
		this.cropCommodityDao = cropCommodityDao;
	}
	
	public void setDeclaredYieldFieldCommodityBerriesDao(DeclaredYieldFieldCommodityBerriesDao declaredYieldFieldCommodityBerriesDao) {
		this.declaredYieldFieldCommodityBerriesDao = declaredYieldFieldCommodityBerriesDao;
	}
	
	public void setCommodityMaturityScaleDao(CommodityMaturityScaleDao commodityMaturityScaleDao) {
		this.commodityMaturityScaleDao = commodityMaturityScaleDao;
	}

	public void setCirrasPolicyService(CirrasPolicyService cirrasPolicyService) {
		this.cirrasPolicyService = cirrasPolicyService;
	}

	public void setJasperReportService(JasperReportService jasperReportService) {
		this.jasperReportService = jasperReportService;
	}
	
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public InventoryContractRsrc createInventoryContract(
			InventoryContractRsrc inventoryContract, FactoryContext factoryContext,
			WebAdeAuthentication authentication) throws ServiceException, NotFoundException, ValidationFailureException {
		logger.debug("<createInventoryContract");

		InventoryContractRsrc result = null;
		String userId = getUserId(authentication);

		try {
			List<Message> errors = new ArrayList<Message>();

			if (!errors.isEmpty()) {
				throw new ValidationFailureException(errors);
			}

			String inventoryContractGuid = insertInventoryContract(inventoryContract, userId);

			List<InventoryContractCommodity> commodities = inventoryContract.getCommodities();

			List<AnnualFieldRsrc> fields = inventoryContract.getFields();
			if (fields != null && !fields.isEmpty()) {
				// Stores all contracts that need to be re-calculated at the end of the loop
				// It needs to be done for each contract a field is added to this contract
				HashSet<Integer> contractsToRecalculate = new HashSet<Integer>();

				List<CommodityMaturityScaleDto> scaleDto = loadBerriesScaleTable(inventoryContract.getInsurancePlanId(), inventoryContract.getCropYear());

				for (AnnualFieldRsrc field : fields) {
					fieldService.updateAnnualField(field, inventoryContract, userId, contractsToRecalculate);

					//If field is being deleted these steps are not necessary 
					if(field.getLandUpdateType() == null || field.getLandUpdateType().equals(LandUpdateTypes.DELETE_FIELD) == false) {
						List<UnderwritingComment> uwComments = field.getUwComments();
						if (uwComments != null && !uwComments.isEmpty()) {
							for (UnderwritingComment underwritingComment : uwComments) {
								if ((underwritingComment.getDeletedByUserInd() != null
										&& underwritingComment.getDeletedByUserInd())) {
									deleteUnderwritingComment(underwritingComment, userId, authentication);
								} else {
									updateUnderwritingComment(underwritingComment, field.getAnnualFieldDetailId(), userId, authentication);
								}
							}
						}

						List<InventoryField> plantings = field.getPlantings();
						if (plantings != null && !plantings.isEmpty()) {
							for (InventoryField planting : plantings) {

								String inventoryFieldGuid = updateInventoryField(planting, userId);

								if (planting.getInventoryUnseeded() != null) {
									grainForageInventoryService.updateInventoryUnseeded(planting.getInventoryUnseeded(), inventoryFieldGuid, userId);
								}

								List<InventorySeededGrain> seededGrains = planting.getInventorySeededGrains();
								if (seededGrains != null && !seededGrains.isEmpty()) {
									for (InventorySeededGrain inventorySeededGrain : seededGrains) {
										grainForageInventoryService.updateInventorySeededGrain(inventorySeededGrain, inventoryFieldGuid, userId);
									}
								}

								List<InventorySeededForage> seededForages = planting.getInventorySeededForages();
								if (seededForages != null && !seededForages.isEmpty()) {
									for (InventorySeededForage inventorySeededForage : seededForages) {
										grainForageInventoryService.updateInventorySeededForage(inventorySeededForage, inventoryFieldGuid, userId);
									}
								}
								
								if (planting.getInventoryBerries() != null) {
									berriesService.updateInventoryBerries(planting.getInventoryBerries(), inventoryFieldGuid, scaleDto, inventoryContract.getCropYear(), userId);
								}

							}

						}
					}
				}

				// Recalculate source policies inventory contract commodity totals and display order if necessary
				recalculateSourceInventoryContracts(contractsToRecalculate, userId, factoryContext, authentication);

			}

			// This needs to be done after dealing with the fields and planting to correctly
			// verify the totals.
			if (commodities != null && !commodities.isEmpty()) {
				for (InventoryContractCommodity commodity : commodities) {
					grainForageInventoryService.insertInventoryContractCommodity(commodity, inventoryContract.getFields(), inventoryContractGuid,
							userId);
				}
			}

			grainForageInventoryService.updateInventoryCoverageTotalForages(inventoryContract, inventoryContractGuid, userId);
			
			updateInventoryContractCommodityBerries(inventoryContract, inventoryContractGuid, userId);
			
			result = getInventoryContract(inventoryContractGuid, factoryContext, authentication);
		} catch (DaoException e) {
			throw new ServiceException("DAO threw an exception", e);
		}

		logger.debug(">createInventoryContract");
		return result;
	}

	private List<CommodityMaturityScaleDto> loadBerriesScaleTable(Integer insurancePlanId, Integer cropYear)
			throws DaoException {
		
		List<CommodityMaturityScaleDto> scaleDto = null;
		if (insurancePlanId.equals(InventoryServiceEnums.InsurancePlans.BERRIES.getInsurancePlanId())) {
			//Load scale table
			scaleDto = commodityMaturityScaleDao.selectByYear(cropYear);
		}
		return scaleDto;
	}
	
	private String insertInventoryContract(InventoryContractRsrc inventoryContract, String userId)
			throws DaoException {

		InventoryContractDto dto = new InventoryContractDto();
		inventoryContractRsrcFactory.updateDto(dto, inventoryContract, userId);

		dto.setInventoryContractGuid(null);

		inventoryContractDao.insert(dto, userId);

		return dto.getInventoryContractGuid();
	}
	
	private void updateInventoryContractCommodityBerries(InventoryContractRsrc invContract, String inventoryContractGuid, String userId) throws DaoException {
		if ( invContract.getInsurancePlanName().equals(InventoryServiceEnums.InsurancePlans.BERRIES.toString())) {
			berriesService.updateInventoryContractCommodityBerries(invContract, inventoryContractGuid, userId);
		}
	}
	
	private String insertInventoryField(InventoryField inventoryField, String userId) throws DaoException {

		InventoryFieldDto dto = new InventoryFieldDto();
		inventoryContractRsrcFactory.updateDto(dto, inventoryField);

		dto.setInventoryFieldGuid(null);

		inventoryFieldDao.insert(dto, userId);

		return dto.getInventoryFieldGuid();
	}
	
	private String insertUnderwritingComment(UnderwritingComment underwritingComment, Integer annualFieldDetailId,
			String userId) throws DaoException {

		UnderwritingCommentDto dto = new UnderwritingCommentDto();
		inventoryContractRsrcFactory.updateDto(dto, underwritingComment);

		dto.setUnderwritingCommentGuid(null);
		dto.setAnnualFieldDetailId(annualFieldDetailId);

		underwritingCommentDao.insert(dto, userId);

		return dto.getUnderwritingCommentGuid();
	}


	@Transactional(readOnly = true, rollbackFor = Exception.class)
	public InventoryContractListRsrc getInventoryContractList(
			Integer cropYear, Integer insurancePlanId, Integer officeId, String policyStatusCode, String policyNumber,
			String growerInfo, String sortColumn, String inventoryContractGuids, FactoryContext factoryContext,
			WebAdeAuthentication webAdeAuthentication)
			throws DaoException, TooManyRecordsException, ServiceException, NotFoundException {

		InventoryContractListRsrc results = null;
		List<String> inventoryContractGuidsList = null;

		if (inventoryContractGuids == null || inventoryContractGuids.length() == 0) {
			// If inventoryContractGuids is null get all policies according to the search
			// criteria

			PagedDtos<PolicyDto> policyDtos = policyDao.select(
					cropYear, 
					insurancePlanId, 
					officeId, 
					policyStatusCode,
					policyNumber, 
					growerInfo,
					null, // datasetType
					null, // sortColumn
					null, // sortDirection
					10000, // maximumRows
					1, // pageNumber
					10000); // pageRowCount

			// Add inventory contract guid to list for each policy in the list
			if (policyDtos != null && policyDtos.getResults().size() > 0) {
				inventoryContractGuidsList = new ArrayList<String>();
				for (PolicyDto dto : policyDtos.getResults()) {
					if (dto.getInventoryContractGuid() != null) {
						// Add inventory contract Guid to list
						inventoryContractGuidsList.add(dto.getInventoryContractGuid());
					}
				}
			}
		} else {
			// Convert string array to list
			inventoryContractGuidsList = new ArrayList<String>(Arrays.asList(inventoryContractGuids.split(",")));
		}

		InventoryContractDto invContractDto = null;
		List<InventoryContractDto> inventoryContractDtos = new ArrayList<InventoryContractDto>();

		// Get inventory contract for each guids in the list
		if (inventoryContractGuidsList != null && inventoryContractGuidsList.size() > 0) {

			for (String inventoryContractGuid : inventoryContractGuidsList) {
				invContractDto = getInventoryContractDtoForPrintout(inventoryContractGuid);
				inventoryContractDtos.add(invContractDto);
			}

			results = inventoryContractRsrcFactory.getInventoryContractList(inventoryContractDtos, cropYear,
					insurancePlanId, officeId, policyStatusCode, policyNumber, growerInfo, sortColumn,
					inventoryContractGuids, factoryContext, webAdeAuthentication);
		}

		// Sorting (Default by policy number)
		if (sortColumn == null || sortColumn.equals("policyNumber")) {
			results.getCollection().sort(Comparator.comparing(InventoryContractRsrc::getPolicyNumber));
		} else if (sortColumn.equals("growerName")) {
			results.getCollection().sort(Comparator.comparing(InventoryContractRsrc::getGrowerName));
		}

		return results;

	}

	private InventoryContractDto getInventoryContractDtoForPrintout(String inventoryContractGuid)
			throws ServiceException, NotFoundException {
		logger.debug("<getInventoryContract");

		InventoryContractDto result = null;

		try {
			// Returns inventory contract including policy and grower data
			result = inventoryContractDao.selectForPrintout(inventoryContractGuid);

			if (result == null) {
				throw new NotFoundException("Did not find the inventory contract: " + inventoryContractGuid);
			}

			loadCommoditiesAndFields(result, false);

		} catch (DaoException e) {
			throw new ServiceException("DAO threw an exception", e);
		}

		logger.debug(">getInventoryContract");
		return result;
	}

	@Transactional(readOnly = true, rollbackFor = Exception.class)
	public byte[] generateInvReport(Integer cropYear, Integer insurancePlanId, Integer officeId,
			String policyStatusCode, String policyNumber, String growerInfo, String sortColumn, String policyIds, String reportType,
			FactoryContext factoryContext, WebAdeAuthentication authentication)
			throws ServiceException, NotFoundException {

		logger.debug("<generateInvReport");
		
		// Result is a PDF, as a byte array.
		byte[] result = null;

		try {
			
			if(policyNumber != null) {
				policyNumber += "%";
			}

			String growerPhoneNumber = "";
			if(growerInfo != null) {
				
				growerPhoneNumber = policyDao.cleanGrowerPhoneNumber(growerInfo);

				//Add wildcard
				growerInfo = growerInfo.toUpperCase() + "%";
			}
			
			//Ignore crop year if the policy number contains the year (i.e. 111111-21)
			if(policyNumber != null && policyNumber.indexOf("-") > -1 && policyNumber.length() > 6) {
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
			queryParams.put("p_sort_column", StringUtils.isNotBlank(sortColumn) ? sortColumn : "policyNumber");
			if (StringUtils.isNotBlank(policyIds)) queryParams.put("p_policy_ids", policyIds);

			// Pick the jasper report to run based on plan.
			if ( InsurancePlans.GRAIN.getInsurancePlanId().equals(insurancePlanId) ) {
				
				if ( InventoryReportType.unseeded.name().equals(reportType) ) {
					result = jasperReportService.generateUnseededGrainReport(queryParams);
					
				} else if ( InventoryReportType.seeded.name().equals(reportType) ) {
					result = jasperReportService.generateSeededGrainReport(queryParams);
					
				} else {
					throw new ServiceException("Report Type for Grain Inventory must be unseeded or seeded");
				}

			} else if ( InsurancePlans.FORAGE.getInsurancePlanId().equals(insurancePlanId) ) {
				result = jasperReportService.generateInvForageReport(queryParams);
			
			} else if ( InsurancePlans.BERRIES.getInsurancePlanId().equals(insurancePlanId) ) {
				result = jasperReportService.generateInvBerriesReport(queryParams);

			} else {
				throw new ServiceException("Insurance Plan must be GRAIN, FORAGE or BERRIES");
			}
			
			if ( result == null ) { 
				throw new NotFoundException("Inventory Report generation failed");
			}

		} catch (JasperReportServiceException e) {
			throw new ServiceException("Jasper threw an exception", e);
		}

		logger.debug(">generateInvReport");
		
		return result;
	}

	
	@Transactional(readOnly = true, rollbackFor = Exception.class)
	public InventoryContractRsrc getInventoryContract(String inventoryContractGuid,
			FactoryContext factoryContext, WebAdeAuthentication authentication)
			throws ServiceException, NotFoundException {
		logger.debug("<getInventoryContract");

		InventoryContractRsrc result = null;

		try {
			InventoryContractDto dto = inventoryContractDao.fetch(inventoryContractGuid);

			if (dto == null) {
				throw new NotFoundException("Did not find the inventory contract: " + inventoryContractGuid);
			}

			result = loadInventoryContract(dto, true, factoryContext, authentication);

		} catch (DaoException e) {
			throw new ServiceException("DAO threw an exception", e);
		}

		logger.debug(">getInventoryContract");
		return result;
	}

	private InventoryContractRsrc loadInventoryContract(InventoryContractDto dto, Boolean loadCommentsAndAssociatedPolicies,
			FactoryContext factoryContext, WebAdeAuthentication authentication) throws DaoException {

		loadCommoditiesAndFields(dto, loadCommentsAndAssociatedPolicies);

		return inventoryContractRsrcFactory.getInventoryContract(dto, factoryContext, authentication);
	}

	private void loadCommoditiesAndFields(InventoryContractDto dto, Boolean loadCommentsAndAssociatedPolicies) throws DaoException {
		logger.debug("<loadCommoditiesAndFields");

		if ( dto.getInsurancePlanName().equals(InventoryServiceEnums.InsurancePlans.GRAIN.toString()) ) {
			List<InventoryContractCommodityDto> commodities = inventoryContractCommodityDao
					.select(dto.getInventoryContractGuid());
			dto.setCommodities(commodities);
		}

		if ( dto.getInsurancePlanName().equals(InventoryServiceEnums.InsurancePlans.FORAGE.toString()) ) { 
			List<InventoryCoverageTotalForageDto> inventoryCoverageTotalForages = inventoryCoverageTotalForageDao.select(dto.getInventoryContractGuid());
			dto.setInventoryCoverageTotalForages(inventoryCoverageTotalForages);
		}

		if ( dto.getInsurancePlanName().equals(InventoryServiceEnums.InsurancePlans.BERRIES.toString()) ) { 
			List<InventoryContractCommodityBerriesDto> iccbDto = inventoryContractCommodityBerriesDao.select(dto.getInventoryContractGuid());
			dto.setInventoryContractCommodityBerries(iccbDto);
		}
		
		List<ContractedFieldDetailDto> fields = contractedFieldDetailDao.select(dto.getContractId(), dto.getCropYear());
		dto.setFields(fields);

		for (ContractedFieldDetailDto cfdDto : dto.getFields()) {
			logger.debug("ContractedFieldDetailDto: " + cfdDto.getFieldId());

			List<InventoryFieldDto> plantings = inventoryFieldDao.select(cfdDto.getFieldId(), cfdDto.getCropYear(),
					cfdDto.getInsurancePlanId());
			
			loadInventoryFieldData(cfdDto, plantings, loadCommentsAndAssociatedPolicies);
			
			if(loadCommentsAndAssociatedPolicies) {
				//Get associated policies
				loadAssociatedPolicies(dto.getContractId(), cfdDto);
			}
		}
		logger.debug(">loadCommoditiesAndFields");
	}

	private void loadAssociatedPolicies(Integer contractId, ContractedFieldDetailDto cfdDto) throws DaoException {

		logger.debug("<loadAssociatedPolicies");

		//Get associated policies
		List<PolicyDto> assocPolicies = policyDao.selectByFieldAndYear(cfdDto.getFieldId(), cfdDto.getCropYear());
		List<PolicyDto> linkedPolicies = new ArrayList<PolicyDto>();
		if (!assocPolicies.isEmpty()) {
			for (PolicyDto assocPolicyDto : assocPolicies) {
				//Add policy to the field if it's associated
				if (contractId == null || !assocPolicyDto.getContractId().equals(contractId)) {
					linkedPolicies.add(assocPolicyDto);
				}
			}
			cfdDto.setPolicies(linkedPolicies);
		}

		logger.debug(">loadAssociatedPolicies");
	}

	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public InventoryContractRsrc updateInventoryContract(String inventoryContractGuid,
			String optimisticLock, InventoryContractRsrc inventoryContract,
			FactoryContext factoryContext, WebAdeAuthentication authentication)
			throws ServiceException, NotFoundException, ForbiddenException, ConflictException,
			ValidationFailureException {
		logger.debug("<updateInventoryContract");

		InventoryContractRsrc result = null;
		String userId = getUserId(authentication);

		try {
			List<Message> errors = new ArrayList<Message>();

			if (!errors.isEmpty()) {
				throw new ValidationFailureException(errors);
			}

			updateInventoryContract(inventoryContract, userId);

			List<AnnualFieldRsrc> fields = inventoryContract.getFields();
			if (fields != null && !fields.isEmpty()) {
				// Stores all contracts that need to be re-calculated at the end of the loop
				// It needs to be done for each contract a field is added to this contract
				HashSet<Integer> contractsToRecalculate = new HashSet<Integer>();
				
				List<CommodityMaturityScaleDto> scaleDto = loadBerriesScaleTable(inventoryContract.getInsurancePlanId(), inventoryContract.getCropYear());

				for (AnnualFieldRsrc field : fields) {
					fieldService.updateAnnualField(field, inventoryContract, userId, contractsToRecalculate);

					//If field is being deleted these steps are not necessary 
					if(field.getLandUpdateType() == null || field.getLandUpdateType().equals(LandUpdateTypes.DELETE_FIELD) == false) {

						// Maybe take care of delete plantings first?
						Set<String> deletedInventoryFieldGuids = handleDeletedPlantings(field);
	
						List<UnderwritingComment> uwComments = field.getUwComments();
						if (uwComments != null && !uwComments.isEmpty()) {
							for (UnderwritingComment underwritingComment : uwComments) {
								if (underwritingComment.getDeletedByUserInd() != null
										&& underwritingComment.getDeletedByUserInd()) {
									deleteUnderwritingComment(underwritingComment, userId, authentication);
								} else {
									updateUnderwritingComment(underwritingComment, field.getAnnualFieldDetailId(), userId, authentication);
								}
							}
						}
	
						List<InventoryField> plantings = field.getPlantings();
						if (plantings != null && !plantings.isEmpty()) {
							for (InventoryField planting : plantings) {
	
								boolean plantingDeleted = planting.getInventoryFieldGuid() != null && deletedInventoryFieldGuids.contains(planting.getInventoryFieldGuid());
								
								if (plantingDeleted) {
									deleteInventory(planting, userId);
								} else {
									// Only update if it hasn't been deleted

									if (field.getLandUpdateType() != null && field.getLandUpdateType().equals(LandUpdateTypes.REMOVE_FIELD_FROM_POLICY)) {
										//Remove link to other plantings if the field is removed from the policy
										planting.setUnderseededInventorySeededForageGuid(null);
									}
									String inventoryFieldGuid = updateInventoryField(planting, userId);
	
									//Only for Grain, Forage is handled with the seeded records
									if (planting.getInventoryUnseeded() != null && planting.getInsurancePlanId().equals(InventoryServiceEnums.InsurancePlans.GRAIN.getInsurancePlanId())) {
										grainForageInventoryService.updateInventoryUnseeded(planting.getInventoryUnseeded(), inventoryFieldGuid, userId);
									}
	
									List<InventorySeededGrain> seededGrains = planting.getInventorySeededGrains();
									if (seededGrains != null && !seededGrains.isEmpty()) {
										
										int remainingSeededGrains = seededGrains.size();
										for (InventorySeededGrain inventorySeededGrain : seededGrains) {
											if ( Boolean.TRUE.equals(inventorySeededGrain.getDeletedByUserInd()) && remainingSeededGrains > 1 ) {
												// Can only delete if it is not the last one. The last one will be cleared, but the record is not deleted.
												grainForageInventoryService.deleteInventorySeededGrain(inventorySeededGrain);
												remainingSeededGrains--;
											} else {
												grainForageInventoryService.updateInventorySeededGrain(inventorySeededGrain, inventoryFieldGuid, userId);
											}
										}
									}
									
									List<InventorySeededForage> seededForages = planting.getInventorySeededForages();
									if (seededForages != null && !seededForages.isEmpty()) {
										
										int remainingSeededForages = seededForages.size();
										for (InventorySeededForage inventorySeededForage : seededForages) {
											if ( Boolean.TRUE.equals(inventorySeededForage.getDeletedByUserInd()) && remainingSeededForages > 1 ) {
												// Can only delete if it is not the last one. The last one will be cleared, but the record is not deleted.
												grainForageInventoryService.deleteInventorySeededForage(inventorySeededForage, userId);
												remainingSeededForages--;
											} else {
												grainForageInventoryService.updateInventorySeededForage(inventorySeededForage, inventoryFieldGuid, userId);
											}
											
											//Always delete the unseeded record if the seeded forage is deleted or the seeded crop is perennial
											if(planting.getInventoryUnseeded() != null) {
												if ( Boolean.TRUE.equals(inventorySeededForage.getDeletedByUserInd())){
													grainForageInventoryService.deleteInventoryUnseeded(planting.getInventoryUnseeded());
												} else {
													//Delete if there is no seeded crop
													if(inventorySeededForage.getCropCommodityId() == null) {
														grainForageInventoryService.deleteInventoryUnseeded(planting.getInventoryUnseeded());
													} else {
														//Check if seeded crop is perennial
														CropCommodityDto dto = cropCommodityDao.fetch(inventorySeededForage.getCropCommodityId());
	
														if (dto != null) {
															//Insert/Update if it's an annual crop and delete in all other cases
															if(dto.getPlantDurationTypeCode().equalsIgnoreCase(InventoryServiceEnums.PlantDurationType.ANNUAL.toString())) {
																grainForageInventoryService.updateInventoryUnseeded(planting.getInventoryUnseeded(), inventoryFieldGuid, userId);
															} else {
																grainForageInventoryService.deleteInventoryUnseeded(planting.getInventoryUnseeded());
															}
														} 
													}
												}
											}
										}
									}
									
									if (planting.getInventoryBerries() != null) {
										berriesService.updateInventoryBerries(planting.getInventoryBerries(), inventoryFieldGuid, scaleDto, inventoryContract.getCropYear(), userId);
									}
									
								}
							}
						}
					}
				}

				// Recalculate source policies inventory contract commodity totals if necessary
				recalculateSourceInventoryContracts(contractsToRecalculate, userId, factoryContext, authentication);

			}

			// This needs to be done after dealing with the fields and planting to correctly
			// verify the totals.
			grainForageInventoryService.updateInventoryContractCommodities(inventoryContract, inventoryContractGuid, userId);
			grainForageInventoryService.updateInventoryCoverageTotalForages(inventoryContract, inventoryContractGuid, userId);
			updateInventoryContractCommodityBerries(inventoryContract, inventoryContractGuid, userId);

			result = getInventoryContract(inventoryContractGuid, factoryContext, authentication);

		} catch (DaoException e) {
			throw new ServiceException("DAO threw an exception", e);
		}

		logger.debug(">updateInventoryContract");
		return result;
	}
	
	// Recalculates the inventory totals and display order of a list of contracts
	private void recalculateSourceInventoryContracts(HashSet<Integer> contractsToRecalculate, String userId,
			FactoryContext factoryContext, WebAdeAuthentication authentication) throws DaoException, NotFoundException {

		logger.debug("<recalculateSourceInventoryContracts");

		if (contractsToRecalculate != null && contractsToRecalculate.size() > 0) {

			InventoryContractRsrc sourceContract = null;

			for (Integer gcyId : contractsToRecalculate) {
				InventoryContractDto dto = inventoryContractDao.getByGrowerContract(gcyId);

				if (dto != null) {

					//Recalculate the inventory totals of a list of contracts
					sourceContract = loadInventoryContract(dto, false, factoryContext, authentication);
					if (sourceContract != null) {
						grainForageInventoryService.updateInventoryContractCommodities(sourceContract, sourceContract.getInventoryContractGuid(), userId);
						grainForageInventoryService.updateInventoryCoverageTotalForages(sourceContract, sourceContract.getInventoryContractGuid(), userId);
						updateInventoryContractCommodityBerries(sourceContract, sourceContract.getInventoryContractGuid(), userId);
					}
					
				}
				
				//Update Display order
				fieldService.updateDisplayOrderForContract(gcyId, userId);
			}
		}
		logger.debug(">recalculateSourceInventoryContracts");
	}
	
	private void deleteInventory(InventoryField planting, String userId) throws NotFoundDaoException, DaoException {

		logger.debug("<deleteInventory");

		// Delete unseeded
		if (planting.getInventoryUnseeded() != null) {
			inventoryUnseededDao.deleteForInventoryField(planting.getInventoryFieldGuid());
		}

		// Delete grain seeded
		if (planting.getInventorySeededGrains() != null && planting.getInventorySeededGrains().size() > 0) {
			inventorySeededGrainDao.deleteForInventoryField(planting.getInventoryFieldGuid());
		}

		// Delete forage seeded
		if (planting.getInventorySeededForages() != null && planting.getInventorySeededForages().size() > 0) {
			for (InventorySeededForage inventorySeededForage : planting.getInventorySeededForages()) {
				//Remove possible link to grain planting
				grainForageInventoryService.removeLink(inventorySeededForage, userId);
			}
			inventorySeededForageDao.deleteForInventoryField(planting.getInventoryFieldGuid());
		}
		
		// Delete berries
		if (planting.getInventoryBerries() != null) {
			inventoryBerriesDao.deleteForInventoryField(planting.getInventoryFieldGuid());
		}

		// Delete inventory field
		inventoryFieldDao.delete(planting.getInventoryFieldGuid());

		logger.debug(">deleteInventory");

	}
	
	private void deleteUnderwritingComment(UnderwritingComment underwritingComment, String userId, WebAdeAuthentication authentication)
			throws NotFoundDaoException, DaoException {
		logger.debug("<deleteUnderwritingComment");
		
		UnderwritingCommentDto dto = null;

		if (underwritingComment.getUnderwritingCommentGuid() != null) {
			dto = underwritingCommentDao.fetch(underwritingComment.getUnderwritingCommentGuid());
		}

		if (dto != null) {
			// Check that user is authorized to delete this comment.
			// Note that this could return false if the current user or create user cannot be determined.
			Boolean userCanDeleteComment = inventoryContractRsrcFactory.checkUserCanDeleteComment(dto, authentication);
			if ( !Boolean.TRUE.equals(userCanDeleteComment) ) {
				logger.error("User " + userId + " attempted to delete comment " + dto.getUnderwritingCommentGuid() + " created by " + dto.getCreateUser());
				throw new ServiceException("The current user is not authorized to delete this comment.");
			}

			underwritingCommentDao.delete(underwritingComment.getUnderwritingCommentGuid());
		}

		logger.debug(">deleteUnderwritingComment");
	}

	private boolean handleDeletedInventoryUnseeded(InventoryField inventoryField) {

		logger.debug("<handleDeletedInventoryUnseeded");
		
		boolean doDeleteInventoryField = false;
		InventoryUnseeded unseeded = inventoryField.getInventoryUnseeded();
		List<InventorySeededGrain> seededGrains = inventoryField.getInventorySeededGrains();
		
		if (unseeded != null && Boolean.TRUE.equals(unseeded.getDeletedByUserInd()) ) {

			// Check if there is any user-entered seeded grain data. If not, then planting can be deleted.
			boolean canDelete = true;
			if ( seededGrains != null) {
				for (InventorySeededGrain seededGrain : seededGrains) {
					if (!inventoryContractRsrcFactory.checkEmptyInventorySeededGrain(seededGrain) && !Boolean.TRUE.equals(seededGrain.getDeletedByUserInd())) { 
						canDelete = false;
						break;
					}
				}
			}
			
			doDeleteInventoryField = canDelete;

		} 
		
		logger.debug(">handleDeletedInventoryUnseeded");

		return doDeleteInventoryField;
	}
	
	private boolean checkEmptyInventoryBerries(InventoryBerriesDto inventoryBerries) {
		return inventoryBerries.getCropVarietyId() == null && 
				inventoryBerries.getPlantedYear() == null &&
				inventoryBerries.getPlantedAcres() == null;
	}
	
	// Returns a set of inventoryFieldGuid for plantings that are to be deleted, if any.
	public Set<String> handleDeletedPlantings(AnnualFieldRsrc field) throws ServiceException {

		logger.debug("<handleDeletedPlantings");
		
		Set<String> deletedInventoryFieldGuids = new HashSet<String>();
		
		// If there are any deleted plantings
		if (field.getPlantings() != null && field.getPlantings().size() > 0) {
			
			for (InventoryField planting : field.getPlantings() ) {
				boolean plantingDeleted = false;
			
				if (handleDeletedInventoryUnseeded(planting)) {
					plantingDeleted = true;
				}
				
				if (grainForageInventoryService.handleDeletedInventorySeededGrains(planting)) {
					plantingDeleted = true;
				}
				
				if (grainForageInventoryService.handleDeletedInventorySeededForage(planting)) {
					plantingDeleted = true;
				}
				
				if (berriesService.handleDeletedInventoryBerries(planting)) {
					plantingDeleted = true;
				}

				if (plantingDeleted) {
					if (planting.getInventoryFieldGuid() == null) {
						// Should never happen. Plantings can only be flagged for deletion if they already exist in the db.
						throw new ServiceException("New Planting does not yet exist; it cannot be deleted");
					}

					deletedInventoryFieldGuids.add(planting.getInventoryFieldGuid());
				}
			}
			
			// Get all plantings that are marked as deleted
			int totalDeleted = deletedInventoryFieldGuids.size();

			// If not all plantings of a field are deleted the planting number of the
			// remaining needs to be updated
			if (totalDeleted > 0 && totalDeleted < field.getPlantings().size()) {

				// Get remaining plantings in order of the planting number
				List<InventoryField> remainingPlantings = field.getPlantings().stream()
						.filter(x -> x.getInventoryFieldGuid() == null || !deletedInventoryFieldGuids.contains(x.getInventoryFieldGuid()))
						.sorted(Comparator.comparingInt(InventoryField::getPlantingNumber))
						.collect(Collectors.toList());

				if (remainingPlantings != null && remainingPlantings.isEmpty() == false) {
					// Update planting number to fill potential gaps
					Integer plantingNumber = 1;
					for (InventoryField inventoryField : remainingPlantings) {
						inventoryField.setPlantingNumber(plantingNumber);
						plantingNumber += 1;
					}
				}
			} else if (totalDeleted == field.getPlantings().size()) {
				// Cannot delete all plantings. One must remain.
				throw new ServiceException("Cannot delete all plantings on field " + field.getFieldId());
			}
		}

		logger.debug(">handleDeletedPlantings");
		
		return deletedInventoryFieldGuids;
	}

	private void updateInventoryContract(InventoryContractRsrc inventoryContract, String userId)
			throws DaoException, NotFoundException {

		InventoryContractDto dto = inventoryContractDao.fetch(inventoryContract.getInventoryContractGuid());

		if (dto == null) {
			throw new NotFoundException(
					"Did not find the inventory contract: " + inventoryContract.getInventoryContractGuid());
		}

		inventoryContractRsrcFactory.updateDto(dto, inventoryContract, userId);

		inventoryContractDao.update(dto, userId);
	}

	private String updateInventoryField(InventoryField inventoryField, String userId) throws DaoException {

		InventoryFieldDto dto = null;

		if (inventoryField.getInventoryFieldGuid() != null) {
			dto = inventoryFieldDao.fetch(inventoryField.getInventoryFieldGuid());
		}

		String inventoryFieldGuid = null;

		if (dto == null) {
			// Insert if it doesn't exist
			inventoryFieldGuid = insertInventoryField(inventoryField, userId);
		} else {
			inventoryFieldGuid = dto.getInventoryFieldGuid();

			inventoryContractRsrcFactory.updateDto(dto, inventoryField);

			inventoryFieldDao.update(dto, userId);
		}

		return inventoryFieldGuid;
	}

	private void updateUnderwritingComment(UnderwritingComment underwritingComment, Integer annualFieldDetailId,
			String userId, WebAdeAuthentication authentication) throws DaoException, ServiceException {

		UnderwritingCommentDto dto = null;

		if (underwritingComment.getUnderwritingCommentGuid() != null) {
			dto = underwritingCommentDao.fetch(underwritingComment.getUnderwritingCommentGuid());
		}

		if (dto == null) {
			// Insert if it doesn't exist
			insertUnderwritingComment(underwritingComment, annualFieldDetailId, userId);
		} else {
			
			if ( !dto.getUnderwritingComment().equals(underwritingComment.getUnderwritingComment()) || 
				 !dto.getUnderwritingCommentTypeCode().equals(underwritingComment.getUnderwritingCommentTypeCode()) ) {

				// Check that user is authorized to edit this comment.
				// Note that this could return null if the current user or create user cannot be determined.
				Boolean userCanEditComment = inventoryContractRsrcFactory.checkUserCanEditComment(dto, authentication);
				if ( !Boolean.TRUE.equals(userCanEditComment) ) {
					logger.error("User " + userId + " attempted to edit comment " + underwritingComment.getUnderwritingCommentGuid() + " created by " + dto.getCreateUser());
					throw new ServiceException("The current user is not authorized to edit this comment.");
				}
				
			}
			
			inventoryContractRsrcFactory.updateDto(dto, underwritingComment);

			underwritingCommentDao.update(dto, userId);
		}

	}

	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public void deleteInventoryContract(String inventoryContractGuid, String optimisticLock,
			WebAdeAuthentication authentication)
			throws ServiceException, NotFoundException, ForbiddenException, ConflictException {
		
		logger.debug("<deleteInventoryContract");
		
		try {

			deleteInventoryContract(inventoryContractGuid, authentication);
			
		} catch (DaoException e) {
			throw new ServiceException("DAO threw an exception", e);
		}

		logger.debug(">deleteInventoryContract");
	}

	private void deleteInventoryContract(String inventoryContractGuid, WebAdeAuthentication authentication) throws ServiceException, DaoException, NotFoundException {

		logger.debug("<deleteInventoryContract");

		InventoryContractDto dto = inventoryContractDao.fetch(inventoryContractGuid);

		if (dto == null) {
			throw new NotFoundException("Did not find the inventory contract: " + inventoryContractGuid);
		}
		
		//Check for DOP
		DeclaredYieldContractDto declaredYieldContractDto = declaredYieldContractDao.getByContractAndYear(dto.getContractId(), dto.getCropYear());
		
		if(declaredYieldContractDto != null) {
			throw new ServiceException("Can't delete inventory of contract with yield data: " + inventoryContractGuid);
		}
		String userId = getUserId(authentication);
		//Remove the link to seeded forage record before deleting it
		inventoryFieldDao.removeLinkToPlantingForInventoryContract(inventoryContractGuid, userId);
		inventorySeededForageDao.deleteForInventoryContract(inventoryContractGuid);
		inventorySeededGrainDao.deleteForInventoryContract(inventoryContractGuid);
		inventoryUnseededDao.deleteForInventoryContract(inventoryContractGuid);
		inventoryBerriesDao.deleteForInventoryContract(inventoryContractGuid);
		inventoryContractCommodityBerriesDao.deleteForInventoryContract(inventoryContractGuid);
		inventoryFieldDao.deleteForInventoryContract(inventoryContractGuid);
		inventoryCoverageTotalForageDao.deleteForInventoryContract(inventoryContractGuid);
		inventoryContractCommodityDao.deleteForInventoryContract(inventoryContractGuid);
		inventoryContractDao.delete(inventoryContractGuid);

		logger.debug(">deleteInventoryContract");

	}
	
	@Transactional(readOnly = true, rollbackFor = Exception.class)
	public InventoryContractRsrc rolloverInventoryContract(Integer policyId,
			FactoryContext factoryContext, WebAdeAuthentication authentication)
			throws ServiceException, NotFoundException {
		logger.debug("<rolloverInventoryContract");

		// Add inventory contract and rollover inventory data from the previous year and
		// if there is none, create a default resource
		InventoryContractRsrc result = null;

		try {

			PolicyDto policyDto = policyDao.fetch(policyId);

			if (policyDto == null) {
				throw new NotFoundException("Did not find the policy: " + policyId);
			}

			// Get fields
			List<ContractedFieldDetailDto> fieldDtos = contractedFieldDetailDao.select(policyDto.getContractId(),
					policyDto.getCropYear());

			List<AnnualFieldRsrc> fields = new ArrayList<AnnualFieldRsrc>();

			// For each field
			for (ContractedFieldDetailDto cfdDto : fieldDtos) {
				
				//Get contracted field record from previous year to update isLeasedInd if necessary
				ContractedFieldDetailDto previousYearCfdDto = contractedFieldDetailDao.selectForFieldYearAndContract(cfdDto.getFieldId(), cfdDto.getCropYear() -1, cfdDto.getContractId());
				if(previousYearCfdDto != null) {
					cfdDto.setIsLeasedInd(previousYearCfdDto.getIsLeasedInd());
				}
				
				//Get associated policies
				loadAssociatedPolicies(policyDto.getContractId(), cfdDto);

				AnnualFieldRsrc annualField = rolloverPlantingsForField(policyDto.getInsurancePlanId(), cfdDto, authentication);

				if (annualField != null) {
					fields.add(annualField);
				}
			}

			// Create Inventory Contract
			result = inventoryContractRsrcFactory.createRolloverInventoryContract(policyDto, fields, factoryContext,
					authentication);

		} catch (DaoException e) {
			throw new ServiceException("DAO threw an exception", e);
		}

		logger.debug(">rolloverInventoryContract");
		return result;
	}

	private AnnualFieldRsrc rolloverPlantingsForField(Integer insurancePlanId, 
													  ContractedFieldDetailDto cfdDto, 
													  WebAdeAuthentication authentication
													 ) throws DaoException {

		// Check if there are already plantings for the current year
		List<InventoryFieldDto> plantings = inventoryFieldDao.select(cfdDto.getFieldId(), cfdDto.getCropYear(),
				cfdDto.getInsurancePlanId());

		AnnualFieldRsrc annualField = null;

		if (plantings != null && plantings.size() > 0) {
			// If there is inventory for the current year, Load all associated data for it
			loadInventoryFieldData(cfdDto, plantings, true);
			annualField = inventoryContractRsrcFactory.createAnnualField(cfdDto, authentication);
		} else {
			// Get the seeded crop data of the PREVIOUS year
			List<InventoryFieldDto> prevYearPlantings = inventoryFieldDao.selectForRollover(cfdDto.getFieldId(),
					cfdDto.getCropYear() - 1, cfdDto.getInsurancePlanId());

			//Load field level comments
			if(cfdDto.getAnnualFieldDetailId() != null) {
				//List<UnderwritingCommentDto> uwComments = underwritingCommentDao.select(cfdDto.getAnnualFieldDetailId());
				//Returning all comments of a field
				List<UnderwritingCommentDto> uwComments = underwritingCommentDao.selectForField(cfdDto.getFieldId());
				cfdDto.setUwComments(uwComments);
			}

			if (prevYearPlantings != null && prevYearPlantings.size() > 0) {
				// Rollover plantings
				cfdDto.setPlantings(prevYearPlantings);

				if (insurancePlanId.equals(InventoryServiceEnums.InsurancePlans.FORAGE.getInsurancePlanId())) {
					// Load InventorySeededForage from last year.
					for ( InventoryFieldDto prevYearPlanting : prevYearPlantings ) {
						List<InventorySeededForageDto> prevYearIsfDtos = inventorySeededForageDao.selectForRollover(cfdDto.getFieldId(),
								cfdDto.getCropYear() - 1, cfdDto.getInsurancePlanId(), prevYearPlanting.getPlantingNumber());
						prevYearPlanting.setInventorySeededForages(prevYearIsfDtos);
					}
				}

				if (insurancePlanId.equals(InventoryServiceEnums.InsurancePlans.BERRIES.getInsurancePlanId())) {
					// Load InventoryBerries from previous year.
					for ( InventoryFieldDto prevYearPlanting : prevYearPlantings ) {
						InventoryBerriesDto prevYearInventoryBerriesDto = inventoryBerriesDao.selectForRollover(cfdDto.getFieldId(),
								cfdDto.getCropYear() - 1, cfdDto.getInsurancePlanId(), prevYearPlanting.getPlantingNumber());
						prevYearPlanting.setInventoryBerries(prevYearInventoryBerriesDto);
					}
				}

				annualField = inventoryContractRsrcFactory.addRolloverAnnualField(insurancePlanId, cfdDto, authentication);
			} else {
				// Create default planting for the field
				annualField = inventoryContractRsrcFactory.createDefaultAnnualField(insurancePlanId, cfdDto, authentication);
			}
		}
		return annualField;
	}

	@Transactional(readOnly = true, rollbackFor = Exception.class)
	public AnnualFieldRsrc rolloverAnnualField(Integer fieldId, Integer rolloverToCropYear, Integer insurancePlanId,
			FactoryContext factoryContext, WebAdeAuthentication webAdeAuthentication)
			throws ServiceException, NotFoundException, DaoException {

		// Check if a field of the same year is added
		// Get ContractedFieldDetailDto
		ContractedFieldDetailDto cfdDto = contractedFieldDetailDao.selectForFieldRollover(fieldId, rolloverToCropYear,
				insurancePlanId);

		// if it's null load
		if (cfdDto == null) {
			cfdDto = new ContractedFieldDetailDto();
			cfdDto.setFieldId(fieldId);
			cfdDto.setCropYear(rolloverToCropYear);
		}
		cfdDto.setInsurancePlanId(insurancePlanId);
		
		//Get associated policies
		loadAssociatedPolicies(null, cfdDto);

		AnnualFieldRsrc annualField = rolloverPlantingsForField(insurancePlanId, cfdDto, webAdeAuthentication);

		return annualField;
	}

	private void loadInventoryFieldData(ContractedFieldDetailDto cfdDto, List<InventoryFieldDto> plantings,
			Boolean loadComments) throws DaoException {

		logger.debug("<loadInventoryFieldData");

		cfdDto.setPlantings(plantings);

		if (loadComments) {
			//List<UnderwritingCommentDto> uwComments = underwritingCommentDao.select(cfdDto.getAnnualFieldDetailId());
			//Returning all comments of a field
			List<UnderwritingCommentDto> uwComments = underwritingCommentDao.selectForField(cfdDto.getFieldId());
			cfdDto.setUwComments(uwComments);
		}

		for (InventoryFieldDto ifDto : cfdDto.getPlantings()) {

			List<InventoryUnseededDto> inventoryUnseededs = inventoryUnseededDao.select(ifDto.getInventoryFieldGuid());
			if (inventoryUnseededs.size() > 0) {
				ifDto.setInventoryUnseeded(inventoryUnseededs.get(0));
				logger.debug("Acres To Be Seeded: " + inventoryUnseededs.get(0).getCropCommodityName());
			}
			
			// Inventory Grain
			if (cfdDto.getInsurancePlanId().equals(InventoryServiceEnums.InsurancePlans.GRAIN.getInsurancePlanId())) {
				List<InventorySeededGrainDto> inventorySeededGrains = inventorySeededGrainDao.select(ifDto.getInventoryFieldGuid());
				ifDto.setInventorySeededGrains(inventorySeededGrains);
			}
			
			//Inventory seeded Forage
			if (cfdDto.getInsurancePlanId().equals(InventoryServiceEnums.InsurancePlans.FORAGE.getInsurancePlanId())) {
				List<InventorySeededForageDto> inventorySeededForages = inventorySeededForageDao.select(ifDto.getInventoryFieldGuid());
				ifDto.setInventorySeededForages(inventorySeededForages);
			}

			//Inventory Berries
			if (cfdDto.getInsurancePlanId().equals(InventoryServiceEnums.InsurancePlans.BERRIES.getInsurancePlanId())) {
				List<InventoryBerriesDto> inventoryBerries = inventoryBerriesDao.select(ifDto.getInventoryFieldGuid());
				if (inventoryBerries.size() > 0) {
					ifDto.setInventoryBerries(inventoryBerries.get(0));
				}
			}
		}
		logger.debug(">loadInventoryFieldData");
	}

	@Transactional(readOnly = true, rollbackFor = Exception.class)
	public LegalLandListRsrc getLegalLandList(			
			String legalLocation, 
			String primaryPropertyIdentifier, 
			String growerInfo,
			String datasetType, 
			Boolean isWildCardSearch, 
			Boolean searchByLegalLocOrLegalDesc, 
			String sortColumn,
			String sortDirection, 
			Integer pageNumber, 
			Integer pageRowCount,
			FactoryContext context, 
			WebAdeAuthentication authentication
	) throws ServiceException, MaxResultsExceededException {

		logger.debug("<getLegalLandList");

		LegalLandListRsrc results = null;

		try {
			int maximumRows = DefaultMaximumResults;

			PagedDtos<LegalLandDto> dtos = legalLandDao.select(
					legalLocation, 
					primaryPropertyIdentifier, 
					growerInfo, 
					datasetType, 
					isWildCardSearch, 
					searchByLegalLocOrLegalDesc, 
					sortColumn, 
					sortDirection, 
					maximumRows, 
					pageNumber, 
					pageRowCount);

			results = legalLandRsrcFactory.getLegalLandList(
					dtos, 
					legalLocation, 
					primaryPropertyIdentifier, 
					growerInfo, 
					datasetType, 
					isWildCardSearch, 
					searchByLegalLocOrLegalDesc, 
					sortColumn, 
					sortDirection, 
					pageRowCount, 
					context, 
					authentication);
			
		} catch (DaoException e) {
			throw new ServiceException("DAO threw an exception", e);
		} catch (TooManyRecordsException e) {
			throw new MaxResultsExceededException(e.getMessage(), e);
		}

		logger.debug(">getLegalLandList");

		return results;

	}

	@Transactional(readOnly = true, rollbackFor = Exception.class)
	public AddFieldValidationRsrc validateAddField(Integer policyId, Integer fieldId,
			Integer transferFromPolicyId, FactoryContext factoryContext, WebAdeAuthentication authentication)
			throws ServiceException, NotFoundException {

		logger.debug("<validateAddField");

		AddFieldValidationRsrc result = null;

		try {

			List<String> warnings = new ArrayList<String>();
			List<String> errors = new ArrayList<String>();

			PolicyDto destPolicyDto = policyDao.fetch(policyId);

			if (destPolicyDto == null) {
				throw new NotFoundException("Did not find the policy: " + policyId);
			}

			List<PolicyDto> assocPolicies = policyDao.selectByFieldAndYear(fieldId, destPolicyDto.getCropYear());

			for (PolicyDto assocPolicyDto : assocPolicies) {
				if (assocPolicyDto.getPolicyId().equals(destPolicyDto.getPolicyId())) {
					errors.add(AddFieldValidationRsrc.FIELD_ALREADY_ON_POLICY_MSG);
				}
			}

			if (errors.isEmpty()) {

				//Returns all plans the field has ever been associated with (for most plans it's
				List<InsurancePlanDto> assocPlans = insurancePlanDao.selectByField(fieldId);

				Set<String> validPlans = new HashSet<String>();
				String insurancePlans = "";

				if ( InsurancePlans.BERRIES.getInsurancePlanId().equals(destPolicyDto.getInsurancePlanId()) ) { 
					validPlans.add(InventoryServiceEnums.InsurancePlans.BERRIES.name());
					insurancePlans = "Berries";
				} else {
					validPlans.add(InventoryServiceEnums.InsurancePlans.GRAIN.name());
					validPlans.add(InventoryServiceEnums.InsurancePlans.FORAGE.name());
					insurancePlans = "Grain or Forage";
				}
				
				for (InsurancePlanDto assocPlanDto : assocPlans) {
					if (!validPlans.contains(assocPlanDto.getInsurancePlanName())) {
						errors.add(AddFieldValidationRsrc.FIELD_ON_INCOMPATIBLE_PLAN_MSG.replace("[insurancePlans]", insurancePlans));
						break;
					}
				}

			}

			if (errors.isEmpty() && assocPolicies.isEmpty() ) {

				if ( transferFromPolicyId != null ) {
					// Should never happen.
					errors.add(AddFieldValidationRsrc.TRANSFER_POLICY_ID_NOT_EMPTY_MSG);
				}
				
			} else if (errors.isEmpty() && !assocPolicies.isEmpty()) {

				Integer expectedTransferFromPolicyId = null;

				for (PolicyDto assocPolicyDto : assocPolicies) {
					if (assocPolicyDto.getInsurancePlanId().equals(destPolicyDto.getInsurancePlanId())) {
						expectedTransferFromPolicyId = assocPolicyDto.getPolicyId();
						break;
					}
				}

				if (expectedTransferFromPolicyId == null ) {
					
					if ( transferFromPolicyId != null ) {
						// Should never happen.
						errors.add(AddFieldValidationRsrc.TRANSFER_POLICY_ID_NOT_EMPTY_MSG);
					}
					
					//Field is associated with another policy but it's not of the same plan
					//This is only possible for plans that allow fields from other plans (For Grain and Forage only at the moment)
					//FIELD_ON_INCOMPATIBLE_PLAN_MSG checks if a field has ever been on an ineligible plan.
					if (errors.isEmpty()) {
						warnings.add(AddFieldValidationRsrc.ADD_FIELD_TO_SECOND_POLICY_WARNING_MSG);
					}
					
				} else {

					if (!expectedTransferFromPolicyId.equals(transferFromPolicyId)) {
						// Should never happen.
						errors.add(AddFieldValidationRsrc.TRANSFER_POLICY_ID_INCORRECT_MSG);
					} else {
					
						//Check for DOP Data
						int totalDopRecords = 0;

						if ( InsurancePlans.GRAIN.getInsurancePlanId().equals(destPolicyDto.getInsurancePlanId()) ) { 
							totalDopRecords = declaredYieldFieldDao.getTotalDopRecordsWithYield(fieldId, destPolicyDto.getCropYear(), destPolicyDto.getInsurancePlanId());
						} else if ( InsurancePlans.FORAGE.getInsurancePlanId().equals(destPolicyDto.getInsurancePlanId()) ) {
							totalDopRecords = declaredYieldFieldForageDao.getTotalDopRecordsWithYield(fieldId, destPolicyDto.getCropYear(), destPolicyDto.getInsurancePlanId());
						} else if ( InsurancePlans.BERRIES.getInsurancePlanId().equals(destPolicyDto.getInsurancePlanId()) ) {
							totalDopRecords = declaredYieldFieldCommodityBerriesDao.getTotalDopRecordsWithYield(fieldId, destPolicyDto.getCropYear());
						}

						if (totalDopRecords > 0) {
							errors.add(AddFieldValidationRsrc.TRANSFER_POLICY_HAS_DOP_MSG);
						}
						
						ca.bc.gov.mal.cirras.policies.api.rest.v1.resource.EndpointsRsrc endpoints = cirrasPolicyService.getTopLevelEndpoints();
						ProductListRsrc productList = cirrasPolicyService.getProducts(endpoints, transferFromPolicyId.toString(), "false", null, null);
	
						if (productList.getCollection().size() > 0) {
							errors.add(AddFieldValidationRsrc.TRANSFER_POLICY_HAS_PRODUCTS_MSG);
						} 
	
						if ( errors.isEmpty() ) {
							warnings.add(AddFieldValidationRsrc.TRANSFER_POLICY_WARNING_MSG);
						}
					}
				}
			}

			result = uwContractRsrcFactory.getAddFieldValidation(warnings, errors, policyId, fieldId, transferFromPolicyId,
					factoryContext, authentication);

		} catch (DaoException e) {
			throw new ServiceException("DAO threw an exception", e);
		} catch (CirrasPolicyServiceException e) {
			throw new ServiceException("Policy Service threw an exception", e);
		}

		logger.debug(">validateAddField");
		return result;
	}

	@Transactional(readOnly = true, rollbackFor = Exception.class)
	public RemoveFieldValidationRsrc validateRemoveField(
			Integer policyId, 
			Integer fieldId,
			FactoryContext factoryContext, 
			WebAdeAuthentication authentication
	) throws ServiceException, NotFoundException {

		logger.debug("<validateRemoveField");

		RemoveFieldValidationRsrc result = null;

		try {

			List<String> removeFromPolicyWarnings = new ArrayList<String>();
			List<String> deleteFieldErrors = new ArrayList<String>();

			PolicyDto targetPolicyDto = policyDao.fetch(policyId);

			if (targetPolicyDto == null) {
				throw new NotFoundException("Did not find the policy: " + policyId);
			}

			// Remove from Policy: Warning: Has Products.
			ca.bc.gov.mal.cirras.policies.api.rest.v1.resource.EndpointsRsrc endpoints = cirrasPolicyService.getTopLevelEndpoints();
			ProductListRsrc productList = cirrasPolicyService.getProducts(endpoints, policyId.toString(), "false", null, null);

			if (productList.getCollection().size() > 0) {
				removeFromPolicyWarnings.add(RemoveFieldValidationRsrc.POLICY_HAS_PRODUCTS_MSG);
			}

			Integer targetAnnualFieldDetailId = null;
			
			// Delete Field: Error: Field associated with another contract in any year other than the current policy.
			List<ContractedFieldDetailDto> cfdDtos = contractedFieldDetailDao.selectForField(fieldId);

			if ( cfdDtos != null ) {
				if ( cfdDtos.size() > 1 ) { 
					Integer numOtherContracts = cfdDtos.size() - 1;
					String policyText = "policies";
					if(numOtherContracts.equals(1)) {
						policyText = "policy";
					}
					deleteFieldErrors.add(RemoveFieldValidationRsrc.FIELD_ON_OTHER_CONTRACTS_MSG
												.replace("[numOtherContracts]", numOtherContracts.toString())
												.replace("[policy]", policyText));
				}
				
				for ( ContractedFieldDetailDto cfdDto : cfdDtos ) {
					if ( cfdDto.getContractId().equals(targetPolicyDto.getContractId()) && cfdDto.getCropYear().equals(targetPolicyDto.getCropYear()) ) {
						// Store annual_field_detail_id for subsequent checks.
						targetAnnualFieldDetailId = cfdDto.getAnnualFieldDetailId();
						break;
					} 				
				}
			}

			if ( targetAnnualFieldDetailId == null ) {
				throw new NotFoundException("Did not find the field " + fieldId + " on policy " + policyId);
			}
			
			// Delete Field: Error: Field has Inventory for another year or plan other than the current policy.
			List<InventoryFieldDto> ifdDtos = inventoryFieldDao.selectForField(fieldId);
			if ( ifdDtos != null ) {
				for ( InventoryFieldDto ifdDto : ifdDtos ) {
					if ( !ifdDto.getCropYear().equals(targetPolicyDto.getCropYear()) || !ifdDto.getInsurancePlanId().equals(targetPolicyDto.getInsurancePlanId()) ) {

						boolean isEmpty = true;
						
						// Check if the planting is empty.
						List<InventoryUnseededDto> inventoryUnseededs = inventoryUnseededDao.select(ifdDto.getInventoryFieldGuid());
						for (InventoryUnseededDto iuDto : inventoryUnseededs ) {
							if ( !inventoryContractRsrcFactory.checkEmptyInventoryUnseeded(iuDto) ) {
								isEmpty = false;
								break;
							}
						}
						
						// Inventory Grain
						if (isEmpty && ifdDto.getInsurancePlanId().equals(InventoryServiceEnums.InsurancePlans.GRAIN.getInsurancePlanId())) {
							List<InventorySeededGrainDto> inventorySeededGrains = inventorySeededGrainDao.select(ifdDto.getInventoryFieldGuid());
							for (InventorySeededGrainDto isgDto : inventorySeededGrains ) {
								if ( !inventoryContractRsrcFactory.checkEmptyInventorySeededGrain(isgDto) ) {
									isEmpty = false;
									break;
								}
							}
						}
						
						//Inventory seeded Forage
						if (isEmpty && ifdDto.getInsurancePlanId().equals(InventoryServiceEnums.InsurancePlans.FORAGE.getInsurancePlanId())) {
							List<InventorySeededForageDto> inventorySeededForages = inventorySeededForageDao.select(ifdDto.getInventoryFieldGuid());
							for (InventorySeededForageDto isfDto : inventorySeededForages ) {
								if ( !grainForageInventoryService.checkEmptyInventorySeededForage(isfDto) ) {
									isEmpty = false;
									break;
								}
							}
						}

						//Berries Inventory
						if (isEmpty && ifdDto.getInsurancePlanId().equals(InventoryServiceEnums.InsurancePlans.BERRIES.getInsurancePlanId())) {
							List<InventoryBerriesDto> inventoryBerries = inventoryBerriesDao.select(ifdDto.getInventoryFieldGuid());
							for (InventoryBerriesDto ibDto : inventoryBerries ) {
								if ( !checkEmptyInventoryBerries(ibDto) ) {
									isEmpty = false;
									break;
								}
							}
						}

						if ( !isEmpty ) {
							deleteFieldErrors.add(RemoveFieldValidationRsrc.FIELD_HAS_OTHER_INVENTORY_MSG);
							break;
						}
					}
				}
			}
			
			// Delete Field: Error: Field has Comments for another year other than the current policy.
			List<UnderwritingCommentDto> ucDtos = underwritingCommentDao.selectForField(fieldId);
			if ( ucDtos != null ) {
				for ( UnderwritingCommentDto ucDto : ucDtos ) {
					if ( !ucDto.getAnnualFieldDetailId().equals(targetAnnualFieldDetailId) ) {
						deleteFieldErrors.add(RemoveFieldValidationRsrc.FIELD_HAS_OTHER_COMMENTS_MSG);
						break;
					}
				}
			}
			
			result = uwContractRsrcFactory.getRemoveFieldValidation(
					true, // Remove from Policy is currently always allowed
					deleteFieldErrors.isEmpty(),  // Delete is allowed if there were no errors.
					removeFromPolicyWarnings, 
					deleteFieldErrors, 
					policyId, 
					fieldId, 
					factoryContext, 
					authentication);

		} catch (DaoException e) {
			throw new ServiceException("DAO threw an exception", e);
		} catch (CirrasPolicyServiceException e) {
			throw new ServiceException("Policy Service threw an exception", e);
		}

		logger.debug(">validateRemoveField");
		return result;
	}
	
	
	@Transactional(readOnly = true, rollbackFor = Exception.class)
	public RenameLegalValidationRsrc validateRenameLegal(
			Integer policyId, Integer annualFieldDetailId, String newLegalLocation, String primaryPropertyIdentifier, FactoryContext factoryContext,
			WebAdeAuthentication authentication) throws ServiceException, NotFoundException {

		logger.debug("<validateRenameLegal");

		RenameLegalValidationRsrc result = null;

		try {
			
			PolicyDto policyDto = policyDao.fetch(policyId);

			if (policyDto == null) {
				throw new NotFoundException("Did not find the policy: " + policyId);
			}

			//Select correct wording for warnings
			String legalLocationOrPid = "Legal Location"; //Default
			String pidOrLegalLocation = "PID"; //Default
			if(InsurancePlans.BERRIES.getInsurancePlanId().equals(policyDto.getInsurancePlanId())){
				legalLocationOrPid = "PID";
				pidOrLegalLocation = "Legal Location";
			}

			AnnualFieldDetailDto afdDto = annualFieldDetailDao.fetch(annualFieldDetailId);
			if (afdDto == null) {
				throw new NotFoundException("Did not find the annual field detail: " + annualFieldDetailId);
			}

			LegalLandDto llDto = legalLandDao.fetch(afdDto.getLegalLandId());
			if (llDto == null) {
				throw new NotFoundException("Did not find the legal land: " + afdDto.getLegalLandId());
			}

			// LegalsWithSameLoc
			Boolean isWarningLegalsWithSameLoc = false;
			String legalsWithSameLocMsg = null;
			PagedDtos<LegalLandDto> legalsWithSameLocList = new PagedDtos<LegalLandDto>();
			//Search by PID if it's provided
			if(primaryPropertyIdentifier != null) {
				legalsWithSameLocList = legalLandDao.select(null, primaryPropertyIdentifier, null, null, false, false, null, null, DefaultMaximumResults, null, null);
			} else {
				legalsWithSameLocList = legalLandDao.select(newLegalLocation, null, null, null, false, false, null, null, DefaultMaximumResults, null, null);
			}

			if (legalsWithSameLocList.getResults().size() > 0) {
				isWarningLegalsWithSameLoc = true;
				legalsWithSameLocMsg = RenameLegalValidationRsrc.LEGALS_WITH_SAME_LOC_MSG.replace("[LegalLocationOrPID]", legalLocationOrPid);

			}

			// OtherFieldOnPolicy
			Boolean isWarningOtherFieldOnPolicy = false;
			String otherFieldOnPolicyMsg = null;
			List<FieldDto> otherFieldOnPolicyList = null;

			List<FieldDto> samePolicyFieldDtos = fieldDao.selectByLastPolicyForLegalLand(afdDto.getLegalLandId(),
					afdDto.getCropYear(), policyDto.getContractId(), null, afdDto.getFieldId());

			if (samePolicyFieldDtos.size() > 0) {

				for (FieldDto fDto : samePolicyFieldDtos) {
					List<PolicyDto> policies = policyDao.selectByFieldAndYear(fDto.getFieldId(), fDto.getMaxCropYear());
					fDto.setPolicies(policies);
				}

				isWarningOtherFieldOnPolicy = true;
				otherFieldOnPolicyMsg = RenameLegalValidationRsrc.OTHER_FIELD_ON_POLICY_MSG.replace("[LegalLocationOrPID]", legalLocationOrPid);
				otherFieldOnPolicyList = samePolicyFieldDtos;
			}

			// FieldOnOtherPolicy
			Boolean isWarningFieldOnOtherPolicy = false;
			String fieldOnOtherPolicyMsg = null;
			List<FieldDto> fieldOnOtherPolicyList = null;

			List<FieldDto> diffPolicyFieldDtos = fieldDao.selectByLastPolicyForLegalLand(afdDto.getLegalLandId(),
					afdDto.getCropYear(), null, policyDto.getContractId(), null);

			if (diffPolicyFieldDtos.size() > 0) {

				for (FieldDto fDto : diffPolicyFieldDtos) {
					List<PolicyDto> policies = policyDao.selectByFieldAndYear(fDto.getFieldId(), fDto.getMaxCropYear());
					fDto.setPolicies(policies);
				}

				isWarningFieldOnOtherPolicy = true;
				fieldOnOtherPolicyMsg = RenameLegalValidationRsrc.FIELD_ON_OTHER_POLICY_MSG.replace("[LegalLocationOrPID]", legalLocationOrPid);
				fieldOnOtherPolicyList = diffPolicyFieldDtos;
			}

			// OtherLegalData
			Boolean isWarningOtherLegalData = false;
			String otherLegalDataMsg = null;
			LegalLandDto otherLegalData = null;

			if(InsurancePlans.BERRIES.getInsurancePlanId().equals(policyDto.getInsurancePlanId())){
				// For Berries Fields. Check if data that wasn't saved when creating quick legal land has values
				if (llDto.getLegalDescription() != null 
						|| llDto.getLegalShortDescription() != null
						|| llDto.getOtherDescription() != null) {

					isWarningOtherLegalData = true;
				}
			} else {
				// For GRAIN or FORAGE Fields added by CUWS, the Primary PID defaults to GF0N,
				// where N is zero-padded.
				if (llDto.getLegalDescription() != null || llDto.getLegalShortDescription() != null
						|| (llDto.getPrimaryPropertyIdentifier() != null
								&& !llDto.getPrimaryPropertyIdentifier().matches("GF\\d+"))) {

					isWarningOtherLegalData = true;
				}
			}
			
			if(isWarningOtherLegalData) {
				otherLegalDataMsg = RenameLegalValidationRsrc.OTHER_LEGAL_DATA_MSG
						.replace("[PidOrLegalLocation]", pidOrLegalLocation)
						.replace("[LegalLocationOrPID]", legalLocationOrPid);
				otherLegalData = llDto;
			}

			result = uwContractRsrcFactory.getRenameLegalValidation(isWarningLegalsWithSameLoc, legalsWithSameLocMsg,
					legalsWithSameLocList.getResults(), isWarningOtherFieldOnPolicy, otherFieldOnPolicyMsg, otherFieldOnPolicyList,
					isWarningFieldOnOtherPolicy, fieldOnOtherPolicyMsg, fieldOnOtherPolicyList, isWarningOtherLegalData,
					otherLegalDataMsg, otherLegalData, policyId, annualFieldDetailId, newLegalLocation, primaryPropertyIdentifier, factoryContext,
					authentication);

		} catch (DaoException e) {
			throw new ServiceException("DAO threw an exception", e);
		} catch (TooManyRecordsException e) {
			throw new ServiceException("DAO threw an exception", e);
		}

		logger.debug(">validateRenameLegal");
		return result;
	}

	@Transactional(readOnly = true, rollbackFor = Exception.class)
	public ReplaceLegalValidationRsrc validateReplaceLegal(
			Integer policyId, Integer annualFieldDetailId, String fieldLabel, Integer legalLandId, String fieldLocation, 
			FactoryContext factoryContext, WebAdeAuthentication authentication)
			throws ServiceException, NotFoundException {

		logger.debug("<validateReplaceLegal");

		ReplaceLegalValidationRsrc result = null;

		try {

			PolicyDto policyDto = policyDao.fetch(policyId);

			if (policyDto == null) {
				throw new NotFoundException("Did not find the policy: " + policyId);
			}

			AnnualFieldDetailDto afdDto = annualFieldDetailDao.fetch(annualFieldDetailId);
			if (afdDto == null) {
				throw new NotFoundException("Did not find the annual field detail: " + annualFieldDetailId);
			}
			
			//Select correct wording for warnings
			String legalLocationOrPid = "Legal Location"; //Default
			if(InsurancePlans.BERRIES.getInsurancePlanId().equals(policyDto.getInsurancePlanId())){
				legalLocationOrPid = "PID";
			}
			
			//Show field location if it's not null
			String fieldLocationOrfieldLabel = fieldLabel; //Default
			if(fieldLocation != null) {
				fieldLocationOrfieldLabel = fieldLocation;
			}

			// FieldOnOtherPolicy
			Boolean isWarningFieldOnOtherPolicy = false;
			String fieldOnOtherPolicyMsg = null;

			List<PolicyDto> assocPolicies = policyDao.selectByFieldAndYear(afdDto.getFieldId(), afdDto.getCropYear());

			if (!assocPolicies.isEmpty()) {
				for (PolicyDto assocPolicyDto : assocPolicies) {
					if (!assocPolicyDto.getPolicyId().equals(policyId)) {
						isWarningFieldOnOtherPolicy = true;
						fieldOnOtherPolicyMsg = ReplaceLegalValidationRsrc.FIELD_ON_OTHER_POLICY_MSG
								.replace("[fieldLocationOrfieldLabel]", fieldLocationOrfieldLabel)
								.replace("[fieldId]", afdDto.getFieldId().toString())
								.replace("[policyNumber]", assocPolicyDto.getPolicyNumber());
					}
				}
			}

			// Field associated with other legal land
			Boolean isWarningFieldHasOtherLegalLand = false;
			String fieldHasOtherLegalLandMsg = null;
			List<LegalLandDto> otherLegalLandOfFieldList = legalLandDao
					.searchOtherLegalLandForField(afdDto.getFieldId(), afdDto.getLegalLandId(), afdDto.getCropYear());

			if (!otherLegalLandOfFieldList.isEmpty()) {
				isWarningFieldHasOtherLegalLand = true;
				fieldHasOtherLegalLandMsg = ReplaceLegalValidationRsrc.FIELD_HAS_OTHER_LEGAL_MSG
						.replace("[legalLocationOrPid]", legalLocationOrPid)
						.replace("[fieldLocationOrfieldLabel]", fieldLocationOrfieldLabel)
						.replace("[fieldId]", afdDto.getFieldId().toString());
			}

			// Other fields associated with legal land
			Boolean isWarningOtherFieldsOnLegal = false;
			String otherFieldsOnLegalMsg = null;
			List<FieldDto> otherFieldsOnLegalLandDtos = null;

			// Only necessary if the user selects an existing one
			if (legalLandId != null) {

				LegalLandDto llDto = legalLandDao.fetch(legalLandId);
				if (llDto == null) {
					throw new NotFoundException("Did not find the legal land: " + afdDto.getLegalLandId());
				}

				otherFieldsOnLegalLandDtos = fieldDao.selectOtherFieldsForLegalLand(legalLandId, afdDto.getFieldId(),
						afdDto.getCropYear());

				if (otherFieldsOnLegalLandDtos.size() > 0) {

					for (FieldDto fDto : otherFieldsOnLegalLandDtos) {
						List<PolicyDto> policies = policyDao.selectByFieldAndYear(fDto.getFieldId(),
								fDto.getMaxCropYear());
						fDto.setPolicies(policies);
					}
					
					String otherDescriptionOrPid = llDto.getOtherDescription();
					if(InsurancePlans.BERRIES.getInsurancePlanId().equals(policyDto.getInsurancePlanId())){
						otherDescriptionOrPid = llDto.getPrimaryPropertyIdentifier();
					}

					isWarningOtherFieldsOnLegal = true;
					otherFieldsOnLegalMsg = ReplaceLegalValidationRsrc.OTHER_FIELD_ON_LEGAL_MSG
							.replace("[legalLocationOrPid]", legalLocationOrPid)
							.replace("[otherDescriptionOrPid]", otherDescriptionOrPid);
				}
			}

			result = uwContractRsrcFactory.getReplaceLegalValidation(isWarningFieldOnOtherPolicy, fieldOnOtherPolicyMsg,
					isWarningFieldHasOtherLegalLand, fieldHasOtherLegalLandMsg, otherLegalLandOfFieldList,
					isWarningOtherFieldsOnLegal, otherFieldsOnLegalMsg, otherFieldsOnLegalLandDtos, policyId,
					annualFieldDetailId, fieldLabel, legalLandId, factoryContext, authentication);

		} catch (DaoException e) {
			throw new ServiceException("DAO threw an exception", e);
		}

		logger.debug(">validateReplaceLegal");
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
