package ca.bc.gov.mal.cirras.underwriting.service.api.v1.impl;

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

import ca.bc.gov.mal.cirras.policies.api.rest.client.v1.CirrasPolicyService;
import ca.bc.gov.mal.cirras.policies.api.rest.client.v1.CirrasPolicyServiceException;
import ca.bc.gov.mal.cirras.policies.api.rest.v1.resource.ProductListRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.AnnualFieldRsrc;
import ca.bc.gov.mal.cirras.underwriting.model.v1.AddFieldValidation;
import ca.bc.gov.mal.cirras.underwriting.model.v1.AnnualField;
import ca.bc.gov.mal.cirras.underwriting.model.v1.Field;
import ca.bc.gov.mal.cirras.underwriting.model.v1.InventoryBerries;
import ca.bc.gov.mal.cirras.underwriting.model.v1.InventoryContract;
import ca.bc.gov.mal.cirras.underwriting.model.v1.InventoryContractCommodity;
import ca.bc.gov.mal.cirras.underwriting.model.v1.InventoryContractList;
import ca.bc.gov.mal.cirras.underwriting.model.v1.InventoryField;
import ca.bc.gov.mal.cirras.underwriting.model.v1.InventorySeededForage;
import ca.bc.gov.mal.cirras.underwriting.model.v1.InventorySeededGrain;
import ca.bc.gov.mal.cirras.underwriting.model.v1.InventoryUnseeded;
import ca.bc.gov.mal.cirras.underwriting.model.v1.LegalLand;
import ca.bc.gov.mal.cirras.underwriting.model.v1.LegalLandList;
import ca.bc.gov.mal.cirras.underwriting.model.v1.RemoveFieldValidation;
import ca.bc.gov.mal.cirras.underwriting.model.v1.RenameLegalValidation;
import ca.bc.gov.mal.cirras.underwriting.model.v1.ReplaceLegalValidation;
import ca.bc.gov.mal.cirras.underwriting.model.v1.UnderwritingComment;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.AnnualFieldDetailDao;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.ContractedFieldDetailDao;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.CropCommodityDao;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.DeclaredYieldContractDao;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.DeclaredYieldFieldDao;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.DeclaredYieldFieldForageDao;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.FieldDao;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.GrowerContractYearDao;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.InventoryContractCommodityDao;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.InventoryContractDao;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.InventoryCoverageTotalForageDao;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.InventoryFieldDao;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.InventorySeededForageDao;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.InventorySeededGrainDao;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.InventoryUnseededDao;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.LegalLandDao;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.LegalLandFieldXrefDao;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.InsurancePlanDao;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.InventoryBerriesDao;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.InventoryContractCommodityBerriesDao;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.PolicyDao;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.UnderwritingCommentDao;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.AnnualFieldDetailDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.ContractedFieldDetailDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.CropCommodityDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.DeclaredYieldContractDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.FieldDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.InventoryContractCommodityDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.InventoryContractDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.InventoryCoverageTotalForageDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.InventoryFieldDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.InventorySeededForageDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.InventorySeededGrainDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.InventoryUnseededDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.LegalLandDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.LegalLandFieldXrefDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.InsurancePlanDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.InventoryBerriesDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.InventoryContractCommodityBerriesDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.PolicyDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.UnderwritingCommentDto;
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
import ca.bc.gov.mal.cirras.underwriting.service.api.v1.CirrasInventoryService;
import ca.bc.gov.mal.cirras.underwriting.service.api.v1.model.factory.AnnualFieldDetailFactory;
import ca.bc.gov.mal.cirras.underwriting.service.api.v1.model.factory.AnnualFieldFactory;
import ca.bc.gov.mal.cirras.underwriting.service.api.v1.model.factory.ContractedFieldDetailFactory;
import ca.bc.gov.mal.cirras.underwriting.service.api.v1.model.factory.FieldFactory;
import ca.bc.gov.mal.cirras.underwriting.service.api.v1.model.factory.InventoryContractFactory;
import ca.bc.gov.mal.cirras.underwriting.service.api.v1.model.factory.LegalLandFactory;
import ca.bc.gov.mal.cirras.underwriting.service.api.v1.model.factory.LegalLandFieldXrefFactory;
import ca.bc.gov.mal.cirras.underwriting.service.api.v1.model.factory.UwContractFactory;
import ca.bc.gov.mal.cirras.underwriting.service.api.v1.reports.JasperReportService;
import ca.bc.gov.mal.cirras.underwriting.service.api.v1.reports.JasperReportServiceException;
import ca.bc.gov.mal.cirras.underwriting.service.api.v1.util.UnderwritingServiceHelper;
import ca.bc.gov.mal.cirras.underwriting.service.api.v1.util.BerriesService;
import ca.bc.gov.mal.cirras.underwriting.service.api.v1.util.InventoryServiceEnums;
import ca.bc.gov.mal.cirras.underwriting.service.api.v1.util.InventoryServiceEnums.InsurancePlans;
import ca.bc.gov.mal.cirras.underwriting.service.api.v1.util.InventoryServiceEnums.InventoryCalculationType;
import ca.bc.gov.mal.cirras.underwriting.service.api.v1.util.InventoryServiceEnums.InventoryReportType;
import ca.bc.gov.mal.cirras.underwriting.service.api.v1.util.LandUpdateTypes;
import ca.bc.gov.mal.cirras.underwriting.service.api.v1.util.OutOfSync;
import ca.bc.gov.mal.cirras.underwriting.service.api.v1.validation.ModelValidator;

public class CirrasInventoryServiceImpl implements CirrasInventoryService {

	private static final Logger logger = LoggerFactory.getLogger(CirrasInventoryServiceImpl.class);

	private Properties applicationProperties;

	private ModelValidator modelValidator;

	// factories
	private InventoryContractFactory inventoryContractFactory;
	private AnnualFieldFactory annualFieldFactory;
	private LegalLandFactory legalLandFactory;
	private FieldFactory fieldFactory; 
	private LegalLandFieldXrefFactory legalLandFieldXrefFactory;
	private AnnualFieldDetailFactory annualFieldDetailFactory; 
	private ContractedFieldDetailFactory contractedFieldDetailFactory; 
	private UwContractFactory uwContractFactory;

	// utils
	private OutOfSync outOfSync;

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
	private LegalLandFieldXrefDao legalLandFieldXrefDao;
	private InsurancePlanDao insurancePlanDao;
	private DeclaredYieldFieldDao declaredYieldFieldDao;
	private DeclaredYieldFieldForageDao declaredYieldFieldForageDao;
	private DeclaredYieldContractDao declaredYieldContractDao;
	private CropCommodityDao cropCommodityDao;
	private GrowerContractYearDao growerContractYearDao;

	// services
	private CirrasPolicyService cirrasPolicyService;

	// Jasper Reports
	private JasperReportService jasperReportService;
	
	// utils
	// @Autowired
	private UnderwritingServiceHelper underwritingServiceHelper;
	private BerriesService berriesService;

	public static final String MaximumResultsProperty = "maximum.results";

	public static final int DefaultMaximumResults = 800;

	public void setUnderwritingServiceHelper(UnderwritingServiceHelper underwritingServiceHelper) {
		this.underwritingServiceHelper = underwritingServiceHelper;
	}

	public void setBerriesService(BerriesService berriesService) {
		this.berriesService = berriesService;
	}
	
	public void setApplicationProperties(Properties applicationProperties) {
		this.applicationProperties = applicationProperties;
	}

	public void setModelValidator(ModelValidator modelValidator) {
		this.modelValidator = modelValidator;
	}

	public void setInventoryContractFactory(InventoryContractFactory inventoryContractFactory) {
		this.inventoryContractFactory = inventoryContractFactory;
	}

	public void setAnnualFieldFactory(AnnualFieldFactory annualFieldFactory) {
		this.annualFieldFactory = annualFieldFactory;
	}

	public void setLegalLandFactory(LegalLandFactory legalLandFactory) {
		this.legalLandFactory = legalLandFactory;
	}

	public void setFieldFactory(FieldFactory fieldFactory) {
		this.fieldFactory = fieldFactory;
	}

	public void setLegalLandFieldXrefFactory(LegalLandFieldXrefFactory legalLandFieldXrefFactory) {
		this.legalLandFieldXrefFactory = legalLandFieldXrefFactory;
	}

	public void setAnnualFieldDetailFactory(AnnualFieldDetailFactory annualFieldDetailFactory) {
		this.annualFieldDetailFactory = annualFieldDetailFactory;
	}

	public void setContractedFieldDetailFactory(ContractedFieldDetailFactory contractedFieldDetailFactory) {
		this.contractedFieldDetailFactory = contractedFieldDetailFactory;
	}

	public void setUwContractFactory(UwContractFactory uwContractFactory) {
		this.uwContractFactory = uwContractFactory;
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

	public void setLegalLandFieldXrefDao(LegalLandFieldXrefDao legalLandFieldXrefDao) {
		this.legalLandFieldXrefDao = legalLandFieldXrefDao;
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
	
	public void setGrowerContractYearDao(GrowerContractYearDao growerContractYearDao) {
		this.growerContractYearDao = growerContractYearDao;
	}

	public void setOutOfSync(OutOfSync outOfSync) {
		this.outOfSync = outOfSync;
	}

	public void setCirrasPolicyService(CirrasPolicyService cirrasPolicyService) {
		this.cirrasPolicyService = cirrasPolicyService;
	}

	public void setJasperReportService(JasperReportService jasperReportService) {
		this.jasperReportService = jasperReportService;
	}
	
	@Override
	public InventoryContract<? extends AnnualField> createInventoryContract(
			InventoryContract<? extends AnnualField> inventoryContract, FactoryContext factoryContext,
			WebAdeAuthentication authentication) throws ServiceException, NotFoundException, ValidationFailureException {
		logger.debug("<createInventoryContract");

		InventoryContract<? extends AnnualField> result = null;
		String userId = getUserId(authentication);

		try {
			List<Message> errors = new ArrayList<Message>();
			// errors.addAll(modelValidator.validateInsuranceClaim(insuranceClaim)); // TODO

			if (!errors.isEmpty()) {
				throw new ValidationFailureException(errors);
			}

			String inventoryContractGuid = insertInventoryContract(inventoryContract, userId);

			List<InventoryContractCommodity> commodities = inventoryContract.getCommodities();

			List<? extends AnnualField> fields = inventoryContract.getFields();
			if (fields != null && !fields.isEmpty()) {
				// Stores all contracts that need to be re-calculated at the end of the loop
				// It needs to be done for each contract a field is added to this contract
				HashSet<Integer> contractsToRecalculate = new HashSet<Integer>();

				for (AnnualField field : fields) {
					updateAnnualField(field, inventoryContract, userId, contractsToRecalculate);

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
									updateInventoryUnseeded(planting.getInventoryUnseeded(), inventoryFieldGuid, userId);
								}

								List<InventorySeededGrain> seededGrains = planting.getInventorySeededGrains();
								if (seededGrains != null && !seededGrains.isEmpty()) {
									for (InventorySeededGrain inventorySeededGrain : seededGrains) {
										updateInventorySeededGrain(inventorySeededGrain, inventoryFieldGuid, userId);
									}
								}

								List<InventorySeededForage> seededForages = planting.getInventorySeededForages();
								if (seededForages != null && !seededForages.isEmpty()) {
									for (InventorySeededForage inventorySeededForage : seededForages) {
										updateInventorySeededForage(inventorySeededForage, inventoryFieldGuid, userId);
									}
								}
								
								if (planting.getInventoryBerries() != null) {
									berriesService.updateInventoryBerries(planting.getInventoryBerries(), inventoryFieldGuid, userId);
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
					insertInventoryContractCommodity(commodity, inventoryContract.getFields(), inventoryContractGuid,
							userId);
				}
			}

			updateInventoryCoverageTotalForages(inventoryContract, inventoryContractGuid, userId);
			
			updateInventoryContractCommodityBerries(inventoryContract, inventoryContractGuid, userId);
			
			result = getInventoryContract(inventoryContractGuid, factoryContext, authentication);
		} catch (DaoException e) {
			throw new ServiceException("DAO threw an exception", e);
		}

		logger.debug(">createInventoryContract");
		return result;
	}

	private String insertInventoryContract(InventoryContract<? extends AnnualField> inventoryContract, String userId)
			throws DaoException {

		InventoryContractDto dto = new InventoryContractDto();
		inventoryContractFactory.updateDto(dto, inventoryContract, userId);

		dto.setInventoryContractGuid(null);

		inventoryContractDao.insert(dto, userId);

		return dto.getInventoryContractGuid();
	}

	private String insertInventoryContractCommodity(InventoryContractCommodity inventoryContractCommodity,
			List<? extends AnnualField> fields, String inventoryContractGuid, String userId) throws DaoException {

		logger.debug("<insertInventoryContractCommodity");

		// Calculate unseeded acres totals
		updateCalculatedAcres(inventoryContractCommodity, fields);

		InventoryContractCommodityDto dto = new InventoryContractCommodityDto();
		inventoryContractFactory.updateDto(dto, inventoryContractCommodity);

		dto.setInventoryContractCommodityGuid(null);
		dto.setInventoryContractGuid(inventoryContractGuid);

		inventoryContractCommodityDao.insert(dto, userId);

		logger.debug(">insertInventoryContractCommodity");

		return dto.getInventoryContractCommodityGuid();
	}

	private void updateCalculatedAcres(InventoryContractCommodity inventoryContractCommodity,
			List<? extends AnnualField> fields) {

		logger.debug("<updateCalculatedAcres");

		// Calculate total unseeded, seeded and spot-loss acres for a commodity
		Double totalUnseededAcres = (double) 0;
		Double totalSeededAcres = (double) 0;
		Double totalSpotLossAcres = (double) 0;

		for (AnnualField field : fields) {

			//Don't include field if it has been removed from the policy or deleted
			Boolean includeField = true;
			if(field.getLandUpdateType() != null && 
				(field.getLandUpdateType().equals(LandUpdateTypes.DELETE_FIELD) || 
				 field.getLandUpdateType().equals(LandUpdateTypes.REMOVE_FIELD_FROM_POLICY))){
				includeField = false;
			}
			if(includeField) {
	
				Double seededAcres = (double) 0;
				Double spotLossAcres = (double) 0;
	
				Double unseededAcres = getUnseededAcres(inventoryContractCommodity, field);
				Map<TotalAcresType, Double> acres = getSeededFieldTotals(inventoryContractCommodity, field);
	
				seededAcres = acres.get(TotalAcresType.SEEDED_ACRES);
				spotLossAcres = acres.get(TotalAcresType.SPOT_LOSS_ACRES);
				
				if (unseededAcres != null && unseededAcres > 0) {
					totalUnseededAcres += unseededAcres;
				}
	
				if (seededAcres != null && seededAcres > 0) {
					totalSeededAcres += seededAcres;
				}
	
				if (spotLossAcres != null && spotLossAcres > 0) {
					totalSpotLossAcres += spotLossAcres;
				}
			}
		}

		logger.debug("Total unseeded acres for " + inventoryContractCommodity.getCropCommodityName() + ": " + totalUnseededAcres);

		if (Double.compare(notNull(inventoryContractCommodity.getTotalUnseededAcres(), (double)-1), totalUnseededAcres) != 0) {
			inventoryContractCommodity.setTotalUnseededAcres(totalUnseededAcres);
		}
		if (Double.compare(notNull(inventoryContractCommodity.getTotalSeededAcres(), (double)-1), totalSeededAcres) != 0) {
			inventoryContractCommodity.setTotalSeededAcres(totalSeededAcres);
		}
		if (Double.compare(notNull(inventoryContractCommodity.getTotalSpotLossAcres(), (double)-1), totalSpotLossAcres) != 0) {
			inventoryContractCommodity.setTotalSpotLossAcres(totalSpotLossAcres);
		}

		logger.debug(">updateCalculatedAcres");

	}

	private Double getUnseededAcres(InventoryContractCommodity inventoryContractCommodity, AnnualField field) {

		logger.debug("<getUnseededAcres");

		Double unseededAcres = (double) 0;
		
		//Unseeded acres are always when pedigree = false
		if(inventoryContractCommodity.getIsPedigreeInd() == false) {
			// Get sum of acres of commodities that have a acres to be seeded value and are
			// not deleted
			// It's possible that commodities are not specified
			// Only commodities that are crop insurance eligible AND inventory crops are stored individually
			// all other commodities are saved as OTHER
			if (inventoryContractCommodity.getCropCommodityId() == null) {
				unseededAcres = field.getPlantings().stream()
						.filter(x -> (x.getInventoryUnseeded().getCropCommodityId() == null
										|| (x.getInventoryUnseeded().getCropCommodityId() != null
										&& x.getInventoryUnseeded().getCropVarietyId() == null //Only Grain commodities are in unseeded totals
										&& (Boolean.FALSE.equals(x.getInventoryUnseeded().getIsCropInsuranceEligibleInd())
											|| Boolean.FALSE.equals(x.getInventoryUnseeded().getIsInventoryCropInd()))))
								&& x.getInventoryUnseeded().getAcresToBeSeeded() != null
								&& (x.getInventoryUnseeded().getDeletedByUserInd() == null
										|| x.getInventoryUnseeded().getDeletedByUserInd() == false))
						.mapToDouble(x -> x.getInventoryUnseeded().getAcresToBeSeeded()).sum();

			} else {
				unseededAcres = field.getPlantings().stream()
						.filter(x -> x.getInventoryUnseeded().getCropCommodityId() != null 
								&& x.getInventoryUnseeded().getCropCommodityId().equals(inventoryContractCommodity.getCropCommodityId())
								&& x.getInventoryUnseeded().getCropVarietyId() == null //Only Grain commodities are in unseeded totals
								&& Boolean.TRUE.equals(x.getInventoryUnseeded().getIsCropInsuranceEligibleInd())
								&& Boolean.TRUE.equals(x.getInventoryUnseeded().getIsInventoryCropInd())
								&& x.getInventoryUnseeded().getAcresToBeSeeded() != null
								&& (x.getInventoryUnseeded().getDeletedByUserInd() == null
										|| x.getInventoryUnseeded().getDeletedByUserInd() == false))
						.mapToDouble(x -> x.getInventoryUnseeded().getAcresToBeSeeded()).sum();
			}
		}

		logger.debug(">getUnseededAcres " + unseededAcres);

		return unseededAcres;
	}
	
	private enum TotalAcresType {
		SEEDED_ACRES,
		SPOT_LOSS_ACRES
	}

	private Map<TotalAcresType, Double> getSeededFieldTotals(InventoryContractCommodity inventoryContractCommodity, AnnualField field) {

		logger.debug("<getSeededFieldTotals");
		
		Map<TotalAcresType, Double> acres = new HashMap<>();
		Double seededAcres = 0.0; 
		Double spotLossAcres = 0.0;

		// Get sum of acres of commodities that have a seeded acres value and are
		// not deleted
		// It's possible that commodities are not specified

		for (InventoryField planting : field.getPlantings()) {

			Double plantingSeededAcres = (double) 0;
			Double plantingSpotLossAcres = (double) 0;
			
			// Get Seeded Acres
			if (inventoryContractCommodity.getCropCommodityId() == null) {
				plantingSeededAcres = planting.getInventorySeededGrains().stream()
						.filter(x -> x.getCropCommodityId() == null && x.getSeededAcres() != null
								&& (x.getDeletedByUserInd() == null || x.getDeletedByUserInd() == false)
								&& x.getIsQuantityInsurableInd() == true
								&& x.getIsPedigreeInd().equals(inventoryContractCommodity.getIsPedigreeInd()))
						.mapToDouble(x -> x.getSeededAcres()).sum();

			} else {
				plantingSeededAcres = planting.getInventorySeededGrains().stream()
						.filter(x -> (x.getCropCommodityId() != null
								&& x.getCropCommodityId().equals(inventoryContractCommodity.getCropCommodityId()))
								&& x.getSeededAcres() != null
								&& (x.getDeletedByUserInd() == null || x.getDeletedByUserInd() == false)
								&& x.getIsQuantityInsurableInd() == true
								&& x.getIsPedigreeInd().equals(inventoryContractCommodity.getIsPedigreeInd()))
						.mapToDouble(x -> x.getSeededAcres()).sum();
			}

			if (plantingSeededAcres != null && plantingSeededAcres > 0) {
				seededAcres += plantingSeededAcres;
			}

			// Get Spot Loss Acres
			if (inventoryContractCommodity.getCropCommodityId() == null) {
				plantingSpotLossAcres = planting.getInventorySeededGrains().stream()
						.filter(x -> x.getCropCommodityId() == null && x.getSeededAcres() != null
								&& (x.getDeletedByUserInd() == null || x.getDeletedByUserInd() == false)
								&& x.getIsSpotLossInsurableInd() == true
								&& x.getIsPedigreeInd().equals(inventoryContractCommodity.getIsPedigreeInd()))
						.mapToDouble(x -> x.getSeededAcres()).sum();

			} else {
				plantingSpotLossAcres = planting.getInventorySeededGrains().stream()
						.filter(x -> (x.getCropCommodityId() != null
								&& x.getCropCommodityId().equals(inventoryContractCommodity.getCropCommodityId()))
								&& x.getSeededAcres() != null
								&& (x.getDeletedByUserInd() == null || x.getDeletedByUserInd() == false)
								&& x.getIsSpotLossInsurableInd() == true
								&& x.getIsPedigreeInd().equals(inventoryContractCommodity.getIsPedigreeInd()))
						.mapToDouble(x -> x.getSeededAcres()).sum();
			}

			if (plantingSpotLossAcres != null && plantingSpotLossAcres > 0) {
				spotLossAcres += plantingSpotLossAcres;
			}

		}

		acres.put(TotalAcresType.SEEDED_ACRES, seededAcres);
		acres.put(TotalAcresType.SPOT_LOSS_ACRES, spotLossAcres);

		logger.debug(">getSeededFieldTotals");

		return acres;
	}

	private void updateInventoryCoverageTotalForages(InventoryContract invContract, String inventoryContractGuid, String userId) throws DaoException {
		if ( invContract.getInsurancePlanName().equals(InventoryServiceEnums.InsurancePlans.FORAGE.toString())) {
			underwritingServiceHelper.updateInventoryCoverageTotalForages(invContract.getFields(), inventoryContractGuid, userId, InventoryCalculationType.Full);
		}
	}
	
	private void updateInventoryContractCommodityBerries(InventoryContract<? extends AnnualField> invContract, String inventoryContractGuid, String userId) throws DaoException {
		if ( invContract.getInsurancePlanName().equals(InventoryServiceEnums.InsurancePlans.BERRIES.toString())) {
			berriesService.updateInventoryContractCommodityBerries(invContract, inventoryContractGuid, userId);
		}
	}
	
	private String insertInventoryField(InventoryField inventoryField, String userId) throws DaoException {

		InventoryFieldDto dto = new InventoryFieldDto();
		inventoryContractFactory.updateDto(dto, inventoryField);

		dto.setInventoryFieldGuid(null);

		inventoryFieldDao.insert(dto, userId);

		return dto.getInventoryFieldGuid();
	}

	private String insertInventoryUnseeded(InventoryUnseeded inventoryUnseeded, String inventoryFieldGuid,
			String userId) throws DaoException {

		InventoryUnseededDto dto = new InventoryUnseededDto();
		inventoryContractFactory.updateDto(dto, inventoryUnseeded);

		dto.setInventoryUnseededGuid(null);
		dto.setInventoryFieldGuid(inventoryFieldGuid);

		inventoryUnseededDao.insert(dto, userId);

		return dto.getInventoryUnseededGuid();
	}

	private String insertInventorySeededGrain(InventorySeededGrain inventorySeededGrain, String inventoryFieldGuid,
			String userId) throws DaoException {

		InventorySeededGrainDto dto = new InventorySeededGrainDto();
		inventoryContractFactory.updateDto(dto, inventorySeededGrain);

		dto.setInventorySeededGrainGuid(null);
		dto.setInventoryFieldGuid(inventoryFieldGuid);

		inventorySeededGrainDao.insert(dto, userId);

		return dto.getInventorySeededGrainGuid();
	}
	
	private String insertInventorySeededForage(InventorySeededForage inventorySeededForage, String inventoryFieldGuid,
			String userId) throws DaoException {

		InventorySeededForageDto dto = new InventorySeededForageDto();
		inventoryContractFactory.updateDto(dto, inventorySeededForage);

		dto.setInventorySeededForageGuid(null);
		dto.setInventoryFieldGuid(inventoryFieldGuid);

		inventorySeededForageDao.insert(dto, userId);
		
		return dto.getInventorySeededForageGuid();
	}
	
	private String insertUnderwritingComment(UnderwritingComment underwritingComment, Integer annualFieldDetailId,
			String userId) throws DaoException {

		UnderwritingCommentDto dto = new UnderwritingCommentDto();
		inventoryContractFactory.updateDto(dto, underwritingComment);

		dto.setUnderwritingCommentGuid(null);
		dto.setAnnualFieldDetailId(annualFieldDetailId);

		underwritingCommentDao.insert(dto, userId);

		return dto.getUnderwritingCommentGuid();
	}

	private void updateAnnualField(AnnualField annualField, InventoryContract<? extends AnnualField> inventoryContract,
			String userId, HashSet<Integer> contractsToRecalculate) throws DaoException, NotFoundException, ServiceException {

		Boolean bUpdateContractedFieldDetails = false;
		Boolean updateFieldData = false;

		// If land is added it needs to be added in CIRRAS first
		if (annualField.getLandUpdateType() != null) {
			switch (annualField.getLandUpdateType()) {
			case LandUpdateTypes.NEW_LAND: // New Legal Land - New Field
			case LandUpdateTypes.ADD_NEW_FIELD: // Existing Legal Land - New Field
				insertNewLand(annualField, inventoryContract, userId);
				break;
			case LandUpdateTypes.ADD_EXISTING_LAND: // Existing Legal Land - Existing Field
				addExistingLand(annualField, inventoryContract, userId, contractsToRecalculate);
				deleteDopData(annualField, inventoryContract);
				updateFieldData = true;
				break;
			case LandUpdateTypes.RENAME_LEGAL_LOCATION:
				renameLegalLocation(annualField, userId);
				bUpdateContractedFieldDetails = true;
				break;
			case LandUpdateTypes.REPLACE_LEGAL_LOCATION_EXISTING:
				replaceLegalLocationExisting(annualField, userId);
				bUpdateContractedFieldDetails = true;
				break;
			case LandUpdateTypes.REPLACE_LEGAL_LOCATION_NEW:
				replaceLegalLocationNew(annualField, userId);
				bUpdateContractedFieldDetails = true;
				break;
			case LandUpdateTypes.REMOVE_FIELD_FROM_POLICY:
				removeFieldFromPolicy(annualField, userId);
				break;
			case LandUpdateTypes.DELETE_FIELD:
				deleteField(annualField, inventoryContract, contractsToRecalculate, userId);
				break;
			default:
				throw new ServiceException("Invalid landUpdateType: " + annualField.getLandUpdateType());
			}
		} else {
			bUpdateContractedFieldDetails = true;
		}

		if (bUpdateContractedFieldDetails || updateFieldData) {

			updateField(annualField, userId);

			if (bUpdateContractedFieldDetails) {
				// Update contracted field details
				updateContractedFieldDetails(annualField, userId);
			}
		}
	}

	private void deleteDopData(AnnualField annualField, InventoryContract<? extends AnnualField> inventoryContract) throws NotFoundDaoException, DaoException {

		if ( inventoryContract.getInsurancePlanName().equals(InventoryServiceEnums.InsurancePlans.FORAGE.toString()) ) { 
			declaredYieldFieldForageDao.deleteForFieldAndYear(annualField.getFieldId(), inventoryContract.getCropYear());
		} else if ( inventoryContract.getInsurancePlanName().equals(InventoryServiceEnums.InsurancePlans.GRAIN.toString()) ) {
			declaredYieldFieldDao.deleteForFieldAndYear(annualField.getFieldId(), inventoryContract.getCropYear());
		}
		
	}

	private void deleteField(AnnualField annualField, InventoryContract<? extends AnnualField> inventoryContract,
			HashSet<Integer> contractsToRecalculate, String userId) throws DaoException, NotFoundException {

		logger.debug("<deleteField");
		
		//Delete field
		Integer fieldId = annualField.getFieldId();

		//Delete inventory data of the field if it exists 
		inventoryFieldDao.removeLinkToPlantingForField(fieldId, userId);
		inventorySeededForageDao.deleteForField(fieldId);
		inventorySeededGrainDao.deleteForField(fieldId);
		inventoryUnseededDao.deleteForField(fieldId);
		inventoryFieldDao.deleteForField(fieldId);
		
		underwritingCommentDao.deleteForField(fieldId);
		contractedFieldDetailDao.deleteForField(fieldId);
		annualFieldDetailDao.deleteForField(fieldId);
		legalLandFieldXrefDao.deleteForField(fieldId);
		fieldDao.delete(fieldId);

		logger.debug(">deleteField");

	}

	private void removeFieldFromPolicy(
					AnnualField annualField, 
					String userId
				) throws DaoException, NotFoundException {
		
		logger.debug("<removeFieldFromPolicy");
		
		ContractedFieldDetailDto dto = contractedFieldDetailDao.fetch(annualField.getContractedFieldDetailId());

		if (dto == null) {
			throw new NotFoundException("Did not find the annual field: " + annualField.getContractedFieldDetailId());
		} else {
			//Delete contracted field detail record
			contractedFieldDetailDao.delete(dto.getContractedFieldDetailId());
			
			//Remove all links of this field's plantings
			inventoryFieldDao.removeLinkToPlantingForFieldAndYear(annualField.getFieldId(), annualField.getCropYear(), userId);
		}
		
		logger.debug(">removeFieldFromPolicy");
		
	}

	private void updateField(AnnualField annualField, String userId)
			throws DaoException, NotFoundException {
		
		FieldDto dto = fieldDao.fetch(annualField.getFieldId());

		if (dto == null) {
			throw new NotFoundException("Did not find the field: " + annualField.getFieldId());
		}
		
		Boolean update = false;

		// Updates field label if it's different.
		if (!notNull(dto.getFieldLabel(), "").equals(notNull(annualField.getFieldLabel(), ""))) {

			dto.setFieldLabel(annualField.getFieldLabel());
			update = true;
		}
		
		// Updates field location if it's different.
		if (!notNull(dto.getLocation(), "").equals(notNull(annualField.getFieldLocation(), ""))) {

			dto.setLocation(annualField.getFieldLocation());
			update = true;
		}

		if(update) {
			fieldDao.update(dto, userId);
		}
	}

	private void renameLegalLocation(AnnualField annualField, String userId)
			throws DaoException, NotFoundException{

		LegalLandDto dto = legalLandDao.fetch(annualField.getLegalLandId());
		if (dto == null) {
			throw new NotFoundException("Did not find the legal land: " + annualField.getLegalLandId());
		}

		// Updates other description in cuws database and CIRRAS if it's different.
		if (!notNull(dto.getOtherDescription(), "").equals(notNull(annualField.getOtherLegalDescription(), ""))) {

			dto.setOtherDescription(annualField.getOtherLegalDescription());
			legalLandDao.update(dto, userId);
		}

	}

	private void replaceLegalLocationExisting(AnnualField annualField, String userId)
			throws DaoException, NotFoundException {

		//Check if the new primary legal land exists
		LegalLandDto llDto = legalLandDao.fetch(annualField.getLegalLandId());
		if (llDto == null) {
			throw new NotFoundException("Did not find the legal land: " + annualField.getLegalLandId());
		}

		AnnualFieldDetailDto afdDto = annualFieldDetailDao.fetch(annualField.getAnnualFieldDetailId());
		if (afdDto == null) {
			throw new NotFoundException("Did not find the annual field detail: " + annualField.getAnnualFieldDetailId());
		}

		// Updates primary legal land in cuws database and CIRRAS if it's different.
		if (!afdDto.getLegalLandId().equals(llDto.getLegalLandId())) {
			
			if(afdDto.getLegalLandId() != null) {
				cleanupLegalLandFieldXref(afdDto.getLegalLandId(), afdDto.getFieldId());
			}

			afdDto.setLegalLandId(llDto.getLegalLandId());
			annualFieldDetailDao.update(afdDto, userId);

			LegalLandFieldXrefDto llfxDto = legalLandFieldXrefDao.fetch(annualField.getLegalLandId(), annualField.getFieldId());

			if (llfxDto == null) {
				llfxDto = new LegalLandFieldXrefDto();
				legalLandFieldXrefFactory.createLegalLandFieldXref(llfxDto, annualField);
				legalLandFieldXrefDao.insert(llfxDto, userId);
			}
		}
	}

	public void cleanupLegalLandFieldXref(Integer legalLandId, Integer fieldId)
			throws DaoException, NotFoundDaoException {
		//Check if there the field has been used for more than 1 year
		int totalAnnualRecords = annualFieldDetailDao.getTotalForLegalLandField(legalLandId, fieldId);
		if(totalAnnualRecords == 1) {
			//If it has been used in one year only, delete legal land lot xref
			legalLandFieldXrefDao.delete(legalLandId, fieldId);
		}
	}

	private void replaceLegalLocationNew(AnnualField annualField, String userId)
			throws DaoException, NotFoundException {

		AnnualFieldDetailDto afdDto = annualFieldDetailDao.fetch(annualField.getAnnualFieldDetailId());
		if (afdDto == null) {
			throw new NotFoundException(
					"Did not find the annual field detail: " + annualField.getAnnualFieldDetailId());
		}
		
		if(afdDto.getLegalLandId() != null) {
			cleanupLegalLandFieldXref(afdDto.getLegalLandId(), afdDto.getFieldId());
		}

		//Insert legal land
		insertQuickLegalLand(annualField, userId);

		//Update annual field detail
		afdDto.setLegalLandId(annualField.getLegalLandId());
		annualFieldDetailDao.update(afdDto, userId);

		//Insert legal land field mapping
		insertLegalLandFieldXref(annualField, userId);
	}

	private void addExistingLand(AnnualField annualField, InventoryContract<? extends AnnualField> inventoryContract,
			String userId, HashSet<Integer> contractsToRecalculate) throws DaoException, NotFoundException, ServiceException {

		// Adds a new annual field record if necessary
		processAnnualFieldDetail(annualField, userId);

		// Adds, updates or deletes a contracted field record
		processContractedFieldDetail(annualField, inventoryContract, userId, contractsToRecalculate);
	}

	private void processContractedFieldDetail(AnnualField annualField, InventoryContract<? extends AnnualField> inventoryContract, String userId,
			HashSet<Integer> contractsToRecalculate) throws DaoException {

		ContractedFieldDetailDto fromCfdDto = null;

		//getTransferFromGrowerContractYearId is only set if it's transferred from the same year
		if (annualField.getTransferFromGrowerContractYearId() != null) {
			// If the contract the land is transferred from is from another year it won't be
			// deleted or updated
			fromCfdDto = contractedFieldDetailDao.selectByGcyAndField(annualField.getTransferFromGrowerContractYearId(), annualField.getFieldId());

			// Transfer happened in the same year
			if (fromCfdDto != null) {
				// If there is a contracted field record in the same year and plan it needs to be updated
				annualField.setContractedFieldDetailId(fromCfdDto.getContractedFieldDetailId());
				updateContractedFieldDetails(annualField, inventoryContract, fromCfdDto, userId);
			} else {
				// If there is NO contracted field record. It needs to be inserted
				// Insert new Contracted Field Details record
				addContractedFieldDetail(annualField, inventoryContract, userId);
			}
			// Add source policy to list to recalculate inventory contract commodity totals
			// at the end of the save inventory process
			// Only necessary if transfered in the same year.
			contractsToRecalculate.add(annualField.getTransferFromGrowerContractYearId());
		} else {
			// If the contract the land is transferred from is from another year. It needs
			// to be inserted
			addContractedFieldDetail(annualField, inventoryContract, userId);
		}
	}

	private void updateContractedFieldDetails(AnnualField annualField, InventoryContract<? extends AnnualField> inventoryContract, 
			ContractedFieldDetailDto dto, String userId) throws DaoException, NotFoundDaoException {
		
		contractedFieldDetailFactory.createContractedFieldDetail(dto, annualField, inventoryContract);
		contractedFieldDetailDao.update(dto, userId);
		
	}

	private void addContractedFieldDetail(AnnualField annualField, InventoryContract<? extends AnnualField> inventoryContract, String userId)
			throws DaoException {

		ContractedFieldDetailDto dto = contractedFieldDetailDao.fetch(annualField.getContractedFieldDetailId());

		if (dto == null) {
			insertContractedFieldDetail(annualField, inventoryContract, userId);
		} else {
			updateContractedFieldDetails(annualField, inventoryContract, dto, userId);
		}
	}

	private void processAnnualFieldDetail(AnnualField annualField, String userId)
			throws DaoException {

		// Check if annual field detail record exists
		AnnualFieldDetailDto annualFieldDetailDto = annualFieldDetailDao.getByFieldAndCropYear(annualField.getFieldId(), annualField.getCropYear());

		if (annualFieldDetailDto == null) {
			// insert record if it doesn't exist.
			
			//getTransferFromGrowerContractYearId is only set if it's transferred from the same year
			if (annualField.getTransferFromGrowerContractYearId() == null) {
				
				//Field has never been associated with a policy or it's added from another crop year
				//Set legal land id of the closest annual field detail record
				setPrimaryLegalLandId(annualField);
			}
			
			insertAnnualFieldDetail(annualField, userId);
		} else {
			annualField.setLegalLandId(annualFieldDetailDto.getLegalLandId());
			annualField.setAnnualFieldDetailId(annualFieldDetailDto.getAnnualFieldDetailId());
		}
	}
	
	private void setPrimaryLegalLandId(AnnualField annualField) throws DaoException {

		Integer legalLandId = null;
		//Get previous and subsequent annual records
		AnnualFieldDetailDto dto = annualFieldDetailDao.getPreviousSubsequentRecords(annualField.getFieldId(), annualField.getCropYear());
		
		if (dto != null) {
			//Take legal land id from previous year if first subsequent year is same number of years or more apart
		    //from the year than the last previous year
			Integer yearsBefore = null;
			Integer yearsAfter = null;
			if(dto.getPreviousContractCropYear() != null) {
				yearsBefore = annualField.getCropYear() - dto.getPreviousContractCropYear();
			}
			if(dto.getSubsequentContractCropYear() != null) {
				yearsAfter = dto.getSubsequentContractCropYear() - annualField.getCropYear();
			}
			
			if (yearsBefore != null && yearsAfter != null) {
				if(yearsBefore <= yearsAfter) {
					//Take from previous year with contract
					legalLandId = dto.getPreviousContractLegalLandId();
				} else {
					//Take from subsequent year with contract
					legalLandId = dto.getSubsequentContractLegalLandId();
				}
			} else if (yearsBefore != null) {
				//Take from previous year with contract
				legalLandId = dto.getPreviousContractLegalLandId();
			} else if (yearsAfter != null) {
				//Take from subsequent year with contract
				legalLandId = dto.getSubsequentContractLegalLandId();
			} else {
				//No records with contract association found
				if(dto.getPreviousLegalLandId() != null) {
					//Take from previous year with no contract
					legalLandId = dto.getPreviousLegalLandId();
				} else if(dto.getSubsequentLegalLandId() != null) {
					//Take from subsequent year with no contract
					legalLandId = dto.getSubsequentLegalLandId();
				}
			}
		}
		
		annualField.setLegalLandId(legalLandId);
	}
	
	// This method is public for testing reasons
	public void insertNewLand(AnnualField annualField, InventoryContract<? extends AnnualField> inventoryContract, String userId)
			throws DaoException {

		// Legal Land doesn't have to be added if only a new field is added
		if (annualField.getLandUpdateType().equals(LandUpdateTypes.NEW_LAND)) {
			// Insert Legal Land
			insertQuickLegalLand(annualField, userId);
		}

		// Insert Field
		FieldDto fieldDto = new FieldDto();
		fieldFactory.createField(fieldDto, annualField);
		fieldDao.insert(fieldDto, userId);
		annualField.setFieldId(fieldDto.getFieldId());

		// Insert Annual Field Details
		insertAnnualFieldDetail(annualField, userId);

		// Insert Contracted Field Details
		insertContractedFieldDetail(annualField, inventoryContract, userId);

		// Insert Legal Land Field Maping
		insertLegalLandFieldXref(annualField, userId);
		
		// Set field id for all plantings
		List<InventoryField> plantings = annualField.getPlantings();
		if (plantings != null && !plantings.isEmpty()) {
			for (InventoryField planting : plantings) {
				planting.setFieldId(annualField.getFieldId());
			}
		}

	}

	private void insertLegalLandFieldXref(AnnualField annualField, String userId) throws DaoException {
		LegalLandFieldXrefDto legalLandFieldXrefDto = new LegalLandFieldXrefDto();
		legalLandFieldXrefFactory.createLegalLandFieldXref(legalLandFieldXrefDto, annualField);
		legalLandFieldXrefDao.insert(legalLandFieldXrefDto, userId);
	}

	private void insertQuickLegalLand(AnnualField annualField, String userId) throws DaoException {
		LegalLandDto legalLandDto = new LegalLandDto();
		String newPid = generatePID();
		legalLandFactory.createQuickLegalLand(legalLandDto, annualField, newPid);
		legalLandDao.insert(legalLandDto, userId);
		annualField.setLegalLandId(legalLandDto.getLegalLandId());
	}
	
	// This method is public for testing reasons
	// Generates a 12 character PID starting with GF followed by a sequence value lead by zeros
	public String generatePID() throws DaoException {
		
		logger.debug("<generatePID");
		
		Integer nextSequence = legalLandDao.getNextPidSequence();
		
		String newPid = "0000000000" + nextSequence.toString();
		//GF is meant to be used for Grain and Forage
		newPid = "GF" + newPid.substring(newPid.length() - 10);
		
		logger.debug(">generatePID");
		
		return newPid;
		
	}

	private void insertAnnualFieldDetail(AnnualField annualField, String userId) throws DaoException {

		logger.debug("<insertAnnualFieldDetail");
		
		AnnualFieldDetailDto annualFieldDetailDto = new AnnualFieldDetailDto();
		annualFieldDetailFactory.createAnnualFieldDetail(annualFieldDetailDto, annualField);
		annualFieldDetailDao.insert(annualFieldDetailDto, userId);
		
		annualField.setAnnualFieldDetailId(annualFieldDetailDto.getAnnualFieldDetailId());
		
		logger.debug(">insertAnnualFieldDetail");
	}

	private void insertContractedFieldDetail(AnnualField annualField, InventoryContract<? extends AnnualField> inventoryContract, String userId)
			throws DaoException {
		
		logger.debug("<insertContractedFieldDetail");
		
		// Insert Contracted Field Details
		ContractedFieldDetailDto contractedFieldDetailDto = new ContractedFieldDetailDto();
		contractedFieldDetailFactory.createContractedFieldDetail(contractedFieldDetailDto, annualField, inventoryContract);
		contractedFieldDetailDto.setContractedFieldDetailId(null);

		contractedFieldDetailDao.insert(contractedFieldDetailDto, userId);
		annualField.setContractedFieldDetailId(contractedFieldDetailDto.getContractedFieldDetailId());
		
		logger.debug(">insertContractedFieldDetail");
	}

	private void updateContractedFieldDetails(AnnualField annualField, String userId)
			throws DaoException, NotFoundException {

		ContractedFieldDetailDto dto = contractedFieldDetailDao.fetch(annualField.getContractedFieldDetailId());

		if (dto == null) {
			throw new NotFoundException("Did not find the annual field: " + annualField.getContractedFieldDetailId());
		}

		annualFieldFactory.updateDto(dto, annualField);

		contractedFieldDetailDao.update(dto, userId);
	}

	@Override
	public InventoryContractList<? extends InventoryContract<? extends AnnualField>> getInventoryContractList(
			Integer cropYear, Integer insurancePlanId, Integer officeId, String policyStatusCode, String policyNumber,
			String growerInfo, String sortColumn, String inventoryContractGuids, FactoryContext factoryContext,
			WebAdeAuthentication webAdeAuthentication)
			throws DaoException, TooManyRecordsException, ServiceException, NotFoundException {

		InventoryContractList<? extends InventoryContract<? extends AnnualField>> results = null;
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

			results = inventoryContractFactory.getInventoryContractList(inventoryContractDtos, cropYear,
					insurancePlanId, officeId, policyStatusCode, policyNumber, growerInfo, sortColumn,
					inventoryContractGuids, factoryContext, webAdeAuthentication);
		}

		// Sorting (Default by policy number)
		if (sortColumn == null || sortColumn.equals("policyNumber")) {
			results.getCollection().sort(Comparator.comparing(InventoryContract::getPolicyNumber));
		} else if (sortColumn.equals("growerName")) {
			results.getCollection().sort(Comparator.comparing(InventoryContract::getGrowerName));
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

	@Override
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

	
	@Override
	public InventoryContract<? extends AnnualField> getInventoryContract(String inventoryContractGuid,
			FactoryContext factoryContext, WebAdeAuthentication authentication)
			throws ServiceException, NotFoundException {
		logger.debug("<getInventoryContract");

		InventoryContract<? extends AnnualField> result = null;

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

	private InventoryContract<? extends AnnualField> loadInventoryContract(InventoryContractDto dto, Boolean loadCommentsAndAssociatedPolicies,
			FactoryContext factoryContext, WebAdeAuthentication authentication) throws DaoException {

		loadCommoditiesAndFields(dto, loadCommentsAndAssociatedPolicies);

		return inventoryContractFactory.getInventoryContract(dto, factoryContext, authentication);
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

	@Override
	public InventoryContract<? extends AnnualField> updateInventoryContract(String inventoryContractGuid,
			String optimisticLock, InventoryContract<? extends AnnualField> inventoryContract,
			FactoryContext factoryContext, WebAdeAuthentication authentication)
			throws ServiceException, NotFoundException, ForbiddenException, ConflictException,
			ValidationFailureException {
		logger.debug("<updateInventoryContract");

		InventoryContract<? extends AnnualField> result = null;
		String userId = getUserId(authentication);

		try {
			List<Message> errors = new ArrayList<Message>();
			// errors.addAll(modelValidator.validateInsuranceClaim(insuranceClaim)); // TODO

			if (!errors.isEmpty()) {
				throw new ValidationFailureException(errors);
			}

			updateInventoryContract(inventoryContract, userId);

			List<? extends AnnualField> fields = inventoryContract.getFields();
			if (fields != null && !fields.isEmpty()) {
				// Stores all contracts that need to be re-calculated at the end of the loop
				// It needs to be done for each contract a field is added to this contract
				HashSet<Integer> contractsToRecalculate = new HashSet<Integer>();

				for (AnnualField field : fields) {
					updateAnnualField(field, inventoryContract, userId, contractsToRecalculate);

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
										updateInventoryUnseeded(planting.getInventoryUnseeded(), inventoryFieldGuid, userId);
									}
	
									List<InventorySeededGrain> seededGrains = planting.getInventorySeededGrains();
									if (seededGrains != null && !seededGrains.isEmpty()) {
										
										int remainingSeededGrains = seededGrains.size();
										for (InventorySeededGrain inventorySeededGrain : seededGrains) {
											if ( Boolean.TRUE.equals(inventorySeededGrain.getDeletedByUserInd()) && remainingSeededGrains > 1 ) {
												// Can only delete if it is not the last one. The last one will be cleared, but the record is not deleted.
												deleteInventorySeededGrain(inventorySeededGrain);
												remainingSeededGrains--;
											} else {
												updateInventorySeededGrain(inventorySeededGrain, inventoryFieldGuid, userId);
											}
										}
									}
									
									List<InventorySeededForage> seededForages = planting.getInventorySeededForages();
									if (seededForages != null && !seededForages.isEmpty()) {
										
										int remainingSeededForages = seededForages.size();
										for (InventorySeededForage inventorySeededForage : seededForages) {
											if ( Boolean.TRUE.equals(inventorySeededForage.getDeletedByUserInd()) && remainingSeededForages > 1 ) {
												// Can only delete if it is not the last one. The last one will be cleared, but the record is not deleted.
												deleteInventorySeededForage(inventorySeededForage, userId);
												remainingSeededForages--;
											} else {
												updateInventorySeededForage(inventorySeededForage, inventoryFieldGuid, userId);
											}
											
											//Always delete the unseeded record if the seeded forage is deleted or the seeded crop is perennial
											if(planting.getInventoryUnseeded() != null) {
												if ( Boolean.TRUE.equals(inventorySeededForage.getDeletedByUserInd())){
													deleteInventoryUnseeded(planting.getInventoryUnseeded());
												} else {
													//Delete if there is no seeded crop
													if(inventorySeededForage.getCropCommodityId() == null) {
														deleteInventoryUnseeded(planting.getInventoryUnseeded());
													} else {
														//Check if seeded crop is perennial
														CropCommodityDto dto = cropCommodityDao.fetch(inventorySeededForage.getCropCommodityId());
	
														if (dto != null) {
															//Insert/Update if it's an annual crop and delete in all other cases
															if(dto.getPlantDurationTypeCode().equalsIgnoreCase(InventoryServiceEnums.PlantDurationType.ANNUAL.toString())) {
																updateInventoryUnseeded(planting.getInventoryUnseeded(), inventoryFieldGuid, userId);
															} else {
																deleteInventoryUnseeded(planting.getInventoryUnseeded());
															}
														} 
													}
												}
											}
										}
									}
									
									if (planting.getInventoryBerries() != null) {
										berriesService.updateInventoryBerries(planting.getInventoryBerries(), inventoryFieldGuid, userId);
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
			updateInventoryContractCommodities(inventoryContract, inventoryContractGuid, userId);
			updateInventoryCoverageTotalForages(inventoryContract, inventoryContractGuid, userId);
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

			InventoryContract<? extends AnnualField> sourceContract = null;

			for (Integer gcyId : contractsToRecalculate) {
				InventoryContractDto dto = inventoryContractDao.getByGrowerContract(gcyId);

				if (dto != null) {

					//Recalculate the inventory totals of a list of contracts
					sourceContract = loadInventoryContract(dto, false, factoryContext, authentication);
					if (sourceContract != null) {
						updateInventoryContractCommodities(sourceContract, sourceContract.getInventoryContractGuid(), userId);
						updateInventoryCoverageTotalForages(sourceContract, sourceContract.getInventoryContractGuid(), userId);
						updateInventoryContractCommodityBerries(sourceContract, sourceContract.getInventoryContractGuid(), userId);
					}
					
				}
				
				//Update Display order
				updateDisplayOrderForContract(gcyId, userId);
			}
		}
		logger.debug(">recalculateSourceInventoryContracts");
	}
	
	private void updateDisplayOrderForContract(Integer growerContractYearId, String userId)
			throws DaoException, NotFoundException {
		
		//Get contracted fields
		List<ContractedFieldDetailDto> dtos = contractedFieldDetailDao.selectForDisplayOrderUpdate(growerContractYearId);
		
		int i = 1;
		for (ContractedFieldDetailDto dto : dtos) {
			if(!dto.getDisplayOrder().equals(i)) {
				dto.setDisplayOrder(i);
				contractedFieldDetailDao.updateDisplayOrder(dto, userId);
			}
			i++;
		}
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
				removeLink(inventorySeededForage, userId);
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

	private void deleteInventorySeededGrain(InventorySeededGrain inventorySeededGrain)
			throws NotFoundDaoException, DaoException {
		logger.debug("<deleteInventorySeededGrain");

		inventorySeededGrainDao.delete(inventorySeededGrain.getInventorySeededGrainGuid());

		logger.debug(">deleteInventorySeededGrain");
	}

	private void deleteInventorySeededForage(InventorySeededForage inventorySeededForage, String userId)
			throws NotFoundDaoException, DaoException {
		logger.debug("<deleteInventorySeededForage");
		
		linkUnlinkPlantings(inventorySeededForage, userId);
		inventorySeededForageDao.delete(inventorySeededForage.getInventorySeededForageGuid());

		logger.debug(">deleteInventorySeededForage");
	}

	private void deleteInventoryUnseeded(InventoryUnseeded inventoryUnseeded)
			throws NotFoundDaoException, DaoException {
		logger.debug("<deleteInventoryUnseeded");
		
		InventoryUnseededDto dto = null;
		if (inventoryUnseeded.getInventoryUnseededGuid() != null) {
			dto = inventoryUnseededDao.fetch(inventoryUnseeded.getInventoryUnseededGuid());
		}

		if (dto != null) {
			inventoryUnseededDao.delete(inventoryUnseeded.getInventoryUnseededGuid());
		}

		logger.debug(">deleteInventoryUnseeded");
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
			Boolean userCanDeleteComment = inventoryContractFactory.checkUserCanDeleteComment(dto, authentication);
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
					if (!inventoryContractFactory.checkEmptyInventorySeededGrain(seededGrain) && !Boolean.TRUE.equals(seededGrain.getDeletedByUserInd())) { 
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

	private boolean handleDeletedInventorySeededGrains(InventoryField inventoryField) {

		logger.debug("<handleDeletedInventorySeededGrains");
		
		boolean doDeleteInventoryField = false;
		InventoryUnseeded unseeded = inventoryField.getInventoryUnseeded();
		List<InventorySeededGrain> seededGrains = inventoryField.getInventorySeededGrains();
				
		if ( seededGrains != null) {
			boolean tryDelete = false;
			for (InventorySeededGrain seededGrain : seededGrains) {
				if (Boolean.TRUE.equals(seededGrain.getDeletedByUserInd())) { 
					tryDelete = true;
					break;
				}
			}

			if (tryDelete) { 
				// Check if there is any user-entered unseeded or seeded grain data. If not, then planting can be deleted.
				boolean canDelete = true;
				if (unseeded != null && !inventoryContractFactory.checkEmptyInventoryUnseeded(unseeded) && !Boolean.TRUE.equals(unseeded.getDeletedByUserInd()) ) {
					canDelete = false;
				} else {
					for (InventorySeededGrain seededGrain : seededGrains) {
						if (!inventoryContractFactory.checkEmptyInventorySeededGrain(seededGrain) && !Boolean.TRUE.equals(seededGrain.getDeletedByUserInd())) { 
							canDelete = false;
							break;
						}
					}
				}
			
				doDeleteInventoryField = canDelete;

			}
		}

		logger.debug(">handleDeletedInventorySeededGrains");
		
		return doDeleteInventoryField;
	}	

	private boolean handleDeletedInventorySeededForage(InventoryField inventoryField) {

		logger.debug("<handleDeletedInventorySeededForage");
		
		boolean doDeleteInventoryField = false;
		List<InventorySeededForage> seededForages = inventoryField.getInventorySeededForages();
//		List<InventorySeededGrain> seededGrains = inventoryField.getInventorySeededGrains();
				
		if ( seededForages != null) {
			boolean tryDelete = false;
			for (InventorySeededForage seededForage : seededForages) {
				if (Boolean.TRUE.equals(seededForage.getDeletedByUserInd())) { 
					tryDelete = true;
					break;
				}
			}

			if (tryDelete) { 
				// Check if there is any user-entered seeded forage data. If not, then planting can be deleted.
				boolean canDelete = true;
				for (InventorySeededForage seededForage : seededForages) {
					if (!checkEmptyInventorySeededForage(seededForage) && !Boolean.TRUE.equals(seededForage.getDeletedByUserInd())) { 
						canDelete = false;
						break;
					}
				}
			
				doDeleteInventoryField = canDelete;

			}
		}		
	
		logger.debug(">handleDeletedInventorySeededForage");

		return doDeleteInventoryField;
	}
	
	private boolean checkEmptyInventorySeededForage(InventorySeededForage inventorySeededForage) {
		
		return inventorySeededForage.getCropVarietyId() == null && 
				inventorySeededForage.getFieldAcres() == null && 
				inventorySeededForage.getSeedingYear() == null &&
				inventorySeededForage.getSeedingDate() == null;
	}

	private boolean checkEmptyInventorySeededForage(InventorySeededForageDto inventorySeededForage) {
		
		return inventorySeededForage.getCropVarietyId() == null && 
				inventorySeededForage.getFieldAcres() == null && 
				inventorySeededForage.getSeedingYear() == null &&
				inventorySeededForage.getSeedingDate() == null;
	}
	
	// Returns a set of inventoryFieldGuid for plantings that are to be deleted, if any.
	public Set<String> handleDeletedPlantings(AnnualField field) throws ServiceException {

		logger.debug("<handleDeletedPlantings");
		
		Set<String> deletedInventoryFieldGuids = new HashSet<String>();
		
		// If there are any deleted plantings
		if (field.getPlantings() != null && field.getPlantings().size() > 0) {
			
			for (InventoryField planting : field.getPlantings() ) {
				boolean plantingDeleted = false;
			
				if (handleDeletedInventoryUnseeded(planting)) {
					plantingDeleted = true;
				}
				
				if (handleDeletedInventorySeededGrains(planting)) {
					plantingDeleted = true;
				}
				
				if (handleDeletedInventorySeededForage(planting)) {
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

	private void updateInventoryContract(InventoryContract<? extends AnnualField> inventoryContract, String userId)
			throws DaoException, NotFoundException {

		InventoryContractDto dto = inventoryContractDao.fetch(inventoryContract.getInventoryContractGuid());

		if (dto == null) {
			throw new NotFoundException(
					"Did not find the inventory contract: " + inventoryContract.getInventoryContractGuid());
		}

		inventoryContractFactory.updateDto(dto, inventoryContract, userId);

		inventoryContractDao.update(dto, userId);
	}

	private void updateInventoryContractCommodities(InventoryContract<? extends AnnualField> inventoryContract,
			String inventoryContractGuid, String userId) throws DaoException, NotFoundException {

		logger.debug("<updateInventoryContractCommodities");

		List<InventoryContractCommodity> commodities = inventoryContract.getCommodities();

		// Get commodities from database
		List<InventoryContractCommodityDto> dtoCommoditiesTemp = inventoryContractCommodityDao.select(inventoryContractGuid);

		// In order to modify this list for processing below, create a copy. MyBatis
		// uses the same reference it returned above in
		// its cache, so modifications to dtoCommoditiesTemp would modify the cache and
		// can cause it to return incorrect results later
		// in this transaction.
		List<InventoryContractCommodityDto> dtoCommodities = null;
		if (dtoCommoditiesTemp != null) {
			dtoCommodities = new ArrayList<InventoryContractCommodityDto>(dtoCommoditiesTemp);
		}

		if (commodities != null && !commodities.isEmpty()) {

			for (InventoryContractCommodity commodity : commodities) {

				logger.debug("Commodity: " + commodity.getCropCommodityName());

				// Check if there is an existing record
				List<InventoryContractCommodityDto> filteredCommodityDto = null;
				if (dtoCommodities != null && !dtoCommodities.isEmpty()) {

					// It's possible that commodities are not specified
					if (commodity.getCropCommodityId() == null) {
						filteredCommodityDto = dtoCommodities.stream().filter(x -> x.getCropCommodityId() == null
								&& x.getIsPedigreeInd().equals(commodity.getIsPedigreeInd()))
								.collect(Collectors.toList());
					} else {
						filteredCommodityDto = dtoCommodities.stream()
								.filter(x -> x.getCropCommodityId() != null
										&& x.getCropCommodityId().equals(commodity.getCropCommodityId())
										&& x.getIsPedigreeInd().equals(commodity.getIsPedigreeInd()))
								.collect(Collectors.toList());
					}
				}

				if (filteredCommodityDto == null || filteredCommodityDto.isEmpty()) {
					// Insert new record
					logger.debug("Contract Commodity Insert: " + commodity.getCropCommodityName());
					insertInventoryContractCommodity(commodity, inventoryContract.getFields(), inventoryContractGuid,
							userId);
				} else {
					logger.debug("Contract Commodity Update: " + commodity.getCropCommodityName());
					// Update existing record
					InventoryContractCommodityDto commodityDto = filteredCommodityDto.get(0);

					updateInventoryContractCommodity(commodity, commodityDto, inventoryContract.getFields(), userId);

					// Remove dto of that commodity from dto list to know which ones need to be
					// deleted
					dtoCommodities.remove(filteredCommodityDto.get(0));
				}
			}

			// Delete commodities that don't exist anymore. Those are the only ones left in
			// the dto list
			if (dtoCommodities != null && !dtoCommodities.isEmpty()) {
				for (InventoryContractCommodityDto dto : dtoCommodities) {
					logger.debug("Contract Commodity Delete: " + dto.getCropCommodityName());
					inventoryContractCommodityDao.delete(dto.getInventoryContractCommodityGuid());
				}
			}

		} else if (dtoCommodities != null && !dtoCommodities.isEmpty()) {
			// In this case all commodities have been removed and need to be removed from
			// the database
			inventoryContractCommodityDao.deleteForInventoryContract(inventoryContractGuid);
		}

		logger.debug(">updateInventoryContractCommodities");

	}

	private void updateInventoryContractCommodity(InventoryContractCommodity inventoryContractCommodity,
			InventoryContractCommodityDto dto, List<? extends AnnualField> fields, String userId)
			throws DaoException, NotFoundException {

		logger.debug("<updateInventoryContractCommodity");

		// Calculate calculated acres totals
		updateCalculatedAcres(inventoryContractCommodity, fields);

		inventoryContractFactory.updateDto(dto, inventoryContractCommodity);

		inventoryContractCommodityDao.update(dto, userId);

		logger.debug(">updateInventoryContractCommodity");
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

			inventoryContractFactory.updateDto(dto, inventoryField);

			inventoryFieldDao.update(dto, userId);
		}

		return inventoryFieldGuid;
	}

	private void updateInventoryUnseeded(InventoryUnseeded inventoryUnseeded, String inventoryFieldGuid, String userId)
			throws DaoException {

		// inventoryUnseeded.getInventoryUnseededGuid() might be null if it's a new crop
		InventoryUnseededDto dto = null;
		if (inventoryUnseeded.getInventoryUnseededGuid() != null) {
			dto = inventoryUnseededDao.fetch(inventoryUnseeded.getInventoryUnseededGuid());
		}

		if (dto == null) {
			// Insert if it doesn't exist
			insertInventoryUnseeded(inventoryUnseeded, inventoryFieldGuid, userId);
		} else {

			inventoryContractFactory.updateDto(dto, inventoryUnseeded);

			inventoryUnseededDao.update(dto, userId);
		}
	}

	private void updateInventorySeededGrain(InventorySeededGrain inventorySeededGrain, String inventoryFieldGuid,
			String userId) throws DaoException {

		InventorySeededGrainDto dto = null;

		if (inventorySeededGrain.getInventorySeededGrainGuid() != null) {
			dto = inventorySeededGrainDao.fetchSimple(inventorySeededGrain.getInventorySeededGrainGuid());
		}

		if (dto == null) {
			// Insert if it doesn't exist
			insertInventorySeededGrain(inventorySeededGrain, inventoryFieldGuid, userId);
		} else {

			inventoryContractFactory.updateDto(dto, inventorySeededGrain);

			inventorySeededGrainDao.update(dto, userId);
		}

	}
	
	private void updateInventorySeededForage(InventorySeededForage inventorySeededForage, String inventoryFieldGuid,
			String userId) throws DaoException, ServiceException {

		InventorySeededForageDto dto = null;

		if (inventorySeededForage.getInventorySeededForageGuid() != null) {
			dto = inventorySeededForageDao.fetchSimple(inventorySeededForage.getInventorySeededForageGuid());
		}

		if (dto == null) {
			// Insert if it doesn't exist
			inventorySeededForage.setInventorySeededForageGuid(insertInventorySeededForage(inventorySeededForage, inventoryFieldGuid, userId));
			
		} else {

			inventoryContractFactory.updateDto(dto, inventorySeededForage);

			inventorySeededForageDao.update(dto, userId);
		}
		
		linkUnlinkPlantings(inventorySeededForage, userId);

	}
	
	private void linkUnlinkPlantings(InventorySeededForage inventorySeededForage, String userId) throws DaoException, ServiceException {
	
		//Adds or removes link if the link planting type is set
		if(inventorySeededForage.getLinkPlantingType() != null) {
			if (inventorySeededForage.getLinkPlantingType().equalsIgnoreCase(InventoryServiceEnums.LinkPlantingType.ADD_LINK.toString())) {
				if (inventorySeededForage.getGrainInventoryFieldGuid() != null) {
					//Link plantings if there is a grain field guid set
					addLink(inventorySeededForage, userId);
				} else {
					throw new ServiceException("No GrainInventoryFieldGuid set to link planting.");
				}
			} else if (inventorySeededForage.getLinkPlantingType().equalsIgnoreCase(InventoryServiceEnums.LinkPlantingType.REMOVE_LINK.toString())) {
				//Remove link
				removeLink(inventorySeededForage, userId);
			}
		} else if (Boolean.TRUE.equals(inventorySeededForage.getDeletedByUserInd())) {
			//Remove link if planting is deleted
			removeLink(inventorySeededForage, userId);
		}
	}

	private void addLink(InventorySeededForage inventorySeededForage, String userId) throws DaoException, ServiceException {
		
		InventoryFieldDto ifDto = inventoryFieldDao.fetch(inventorySeededForage.getGrainInventoryFieldGuid());
		if(ifDto != null) {
			ifDto.setUnderseededInventorySeededForageGuid(inventorySeededForage.getInventorySeededForageGuid());
			inventoryFieldDao.update(ifDto, userId);
		}
		else {
			logger.info("AddLink, Grain planting to link to field not found. InventoryFieldGuid: " + inventorySeededForage.getGrainInventoryFieldGuid());
			throw new ServiceException("Grain planting to link to field not found.");
		}
		
	}

	private void removeLink(InventorySeededForage inventorySeededForage, String userId) throws DaoException {

		InventoryFieldDto ifDto = inventoryFieldDao.selectLinkedGrainPlanting(inventorySeededForage.getInventorySeededForageGuid());
		if(ifDto != null) {
			ifDto.setUnderseededInventorySeededForageGuid(null);
			inventoryFieldDao.update(ifDto, userId);
		}
		else {
			logger.info("RemoveLink, Planting to link to field not found. InventorySeededForageGuid: " + inventorySeededForage.getInventorySeededForageGuid());
		}
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
				Boolean userCanEditComment = inventoryContractFactory.checkUserCanEditComment(dto, authentication);
				if ( !Boolean.TRUE.equals(userCanEditComment) ) {
					logger.error("User " + userId + " attempted to edit comment " + underwritingComment.getUnderwritingCommentGuid() + " created by " + dto.getCreateUser());
					throw new ServiceException("The current user is not authorized to edit this comment.");
				}
				
			}
			
			inventoryContractFactory.updateDto(dto, underwritingComment);

			underwritingCommentDao.update(dto, userId);
		}

	}

	@Override
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
	
	@Override
	public InventoryContract<? extends AnnualField> rolloverInventoryContract(Integer policyId,
			FactoryContext factoryContext, WebAdeAuthentication authentication)
			throws ServiceException, NotFoundException {
		logger.debug("<rolloverInventoryContract");

		// Add inventory contract and rollover inventory data from the previous year and
		// if there is none, create a default resource
		InventoryContract<? extends AnnualField> result = null;

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
				
				//Get associated policies
				loadAssociatedPolicies(policyDto.getContractId(), cfdDto);

				AnnualFieldRsrc annualField = rolloverPlantingsForField(policyDto.getInsurancePlanId(), cfdDto, authentication);

				if (annualField != null) {
					fields.add(annualField);
				}
			}

			// Create Inventory Contract
			result = inventoryContractFactory.createRolloverInventoryContract(policyDto, fields, factoryContext,
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
			annualField = inventoryContractFactory.createAnnualField(cfdDto, authentication);
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

				annualField = inventoryContractFactory.addRolloverAnnualField(insurancePlanId, cfdDto, authentication);
			} else {
				// Create default planting for the field
				annualField = inventoryContractFactory.createDefaultAnnualField(insurancePlanId, cfdDto, authentication);
			}
		}
		return annualField;
	}

	@Override
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

	@Override
	public LegalLandList<? extends LegalLand<? extends Field>> getLegalLandList(			
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

		LegalLandList<? extends LegalLand<? extends Field>> results = null;

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

			results = legalLandFactory.getLegalLandList(
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

	@Override
	public AddFieldValidation<? extends Message> validateAddField(Integer policyId, Integer fieldId,
			Integer transferFromPolicyId, FactoryContext factoryContext, WebAdeAuthentication authentication)
			throws ServiceException, NotFoundException {

		logger.debug("<validateAddField");

		AddFieldValidation<? extends Message> result = null;

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
					errors.add(AddFieldValidation.FIELD_ALREADY_ON_POLICY_MSG);
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
						errors.add(AddFieldValidation.FIELD_ON_INCOMPATIBLE_PLAN_MSG.replace("[insurancePlans]]", insurancePlans));
						break;
					}
				}

			}

			if (errors.isEmpty() && assocPolicies.isEmpty() ) {

				if ( transferFromPolicyId != null ) {
					// Should never happen.
					errors.add(AddFieldValidation.TRANSFER_POLICY_ID_NOT_EMPTY_MSG);
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
						errors.add(AddFieldValidation.TRANSFER_POLICY_ID_NOT_EMPTY_MSG);
					}
					
					//Field is associated with another policy but it's not of the same plan
					//This is only possible for plans that allow fields from other plans (For Grain and Forage only at the moment)
					//FIELD_ON_INCOMPATIBLE_PLAN_MSG checks if a field has ever been on an ineligible plan.
					if (errors.isEmpty()) {
						warnings.add(AddFieldValidation.ADD_FIELD_TO_SECOND_POLICY_WARNING_MSG);
					}
					
				} else {

					if (!expectedTransferFromPolicyId.equals(transferFromPolicyId)) {
						// Should never happen.
						errors.add(AddFieldValidation.TRANSFER_POLICY_ID_INCORRECT_MSG);
					} else {
					
						//Check for DOP Data
						int totalDopRecords = 0;

						if ( InsurancePlans.GRAIN.getInsurancePlanId().equals(destPolicyDto.getInsurancePlanId()) ) { 
							totalDopRecords = declaredYieldFieldDao.getTotalDopRecordsWithYield(fieldId, destPolicyDto.getCropYear(), destPolicyDto.getInsurancePlanId());
						} else if ( InsurancePlans.FORAGE.getInsurancePlanId().equals(destPolicyDto.getInsurancePlanId()) ) {
							totalDopRecords = declaredYieldFieldForageDao.getTotalDopRecordsWithYield(fieldId, destPolicyDto.getCropYear(), destPolicyDto.getInsurancePlanId());
						}

						if (totalDopRecords > 0) {
							errors.add(AddFieldValidation.TRANSFER_POLICY_HAS_DOP_MSG);
						}
						
						ca.bc.gov.mal.cirras.policies.api.rest.v1.resource.EndpointsRsrc endpoints = cirrasPolicyService.getTopLevelEndpoints();
						ProductListRsrc productList = cirrasPolicyService.getProducts(endpoints, transferFromPolicyId.toString(), "false", null, null);
	
						if (productList.getCollection().size() > 0) {
							errors.add(AddFieldValidation.TRANSFER_POLICY_HAS_PRODUCTS_MSG);
						} 
	
						if ( errors.isEmpty() ) {
							warnings.add(AddFieldValidation.TRANSFER_POLICY_WARNING_MSG);
						}
					}
				}
			}

			result = uwContractFactory.getAddFieldValidation(warnings, errors, policyId, fieldId, transferFromPolicyId,
					factoryContext, authentication);

		} catch (DaoException e) {
			throw new ServiceException("DAO threw an exception", e);
		} catch (CirrasPolicyServiceException e) {
			throw new ServiceException("Policy Service threw an exception", e);
		}

		logger.debug(">validateAddField");
		return result;
	}

	@Override
	public RemoveFieldValidation<? extends Message> validateRemoveField(
			Integer policyId, 
			Integer fieldId,
			FactoryContext factoryContext, 
			WebAdeAuthentication authentication
	) throws ServiceException, NotFoundException {

		logger.debug("<validateRemoveField");

		RemoveFieldValidation<? extends Message> result = null;

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
				removeFromPolicyWarnings.add(RemoveFieldValidation.POLICY_HAS_PRODUCTS_MSG);
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
					deleteFieldErrors.add(RemoveFieldValidation.FIELD_ON_OTHER_CONTRACTS_MSG
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
							if ( !inventoryContractFactory.checkEmptyInventoryUnseeded(iuDto) ) {
								isEmpty = false;
								break;
							}
						}
						
						// Inventory Grain
						if (isEmpty && ifdDto.getInsurancePlanId().equals(InventoryServiceEnums.InsurancePlans.GRAIN.getInsurancePlanId())) {
							List<InventorySeededGrainDto> inventorySeededGrains = inventorySeededGrainDao.select(ifdDto.getInventoryFieldGuid());
							for (InventorySeededGrainDto isgDto : inventorySeededGrains ) {
								if ( !inventoryContractFactory.checkEmptyInventorySeededGrain(isgDto) ) {
									isEmpty = false;
									break;
								}
							}
						}
						
						//Inventory seeded Forage
						if (isEmpty && ifdDto.getInsurancePlanId().equals(InventoryServiceEnums.InsurancePlans.FORAGE.getInsurancePlanId())) {
							List<InventorySeededForageDto> inventorySeededForages = inventorySeededForageDao.select(ifdDto.getInventoryFieldGuid());
							for (InventorySeededForageDto isfDto : inventorySeededForages ) {
								if ( !checkEmptyInventorySeededForage(isfDto) ) {
									isEmpty = false;
									break;
								}
							}
						}
						
						if ( !isEmpty ) {
							deleteFieldErrors.add(RemoveFieldValidation.FIELD_HAS_OTHER_INVENTORY_MSG);
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
						deleteFieldErrors.add(RemoveFieldValidation.FIELD_HAS_OTHER_COMMENTS_MSG);
						break;
					}
				}
			}
			
			result = uwContractFactory.getRemoveFieldValidation(
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
	
	
	@Override
	public RenameLegalValidation<? extends Message, ? extends LegalLand<? extends Field>, ? extends AnnualField> validateRenameLegal(
			Integer policyId, Integer annualFieldDetailId, String newLegalLocation, FactoryContext factoryContext,
			WebAdeAuthentication authentication) throws ServiceException, NotFoundException {

		logger.debug("<validateRenameLegal");

		RenameLegalValidation<? extends Message, ? extends LegalLand<? extends Field>, ? extends AnnualField> result = null;

		try {

			PolicyDto policyDto = policyDao.fetch(policyId);

			if (policyDto == null) {
				throw new NotFoundException("Did not find the policy: " + policyId);
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
			PagedDtos<LegalLandDto> legalsWithSameLocList = legalLandDao.select(newLegalLocation, null, null, null, false, false, null, null, DefaultMaximumResults, null, null);

			if (legalsWithSameLocList.getResults().size() > 0) {
				isWarningLegalsWithSameLoc = true;
				legalsWithSameLocMsg = RenameLegalValidation.LEGALS_WITH_SAME_LOC_MSG;
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
				otherFieldOnPolicyMsg = RenameLegalValidation.OTHER_FIELD_ON_POLICY_MSG;
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
				fieldOnOtherPolicyMsg = RenameLegalValidation.FIELD_ON_OTHER_POLICY_MSG;
				fieldOnOtherPolicyList = diffPolicyFieldDtos;
			}

			// OtherLegalData
			Boolean isWarningOtherLegalData = false;
			String otherLegalDataMsg = null;
			LegalLandDto otherLegalData = null;

			// For GRAIN or FORAGE Fields added by CUWS, the Primary PID defaults to GF0N,
			// where N is zero-padded.
			if (llDto.getLegalDescription() != null || llDto.getLegalShortDescription() != null
					|| (llDto.getPrimaryPropertyIdentifier() != null
							&& !llDto.getPrimaryPropertyIdentifier().matches("GF\\d+"))) {

				isWarningOtherLegalData = true;
				otherLegalDataMsg = RenameLegalValidation.OTHER_LEGAL_DATA_MSG;
				otherLegalData = llDto;
			}

			result = uwContractFactory.getRenameLegalValidation(isWarningLegalsWithSameLoc, legalsWithSameLocMsg,
					legalsWithSameLocList.getResults(), isWarningOtherFieldOnPolicy, otherFieldOnPolicyMsg, otherFieldOnPolicyList,
					isWarningFieldOnOtherPolicy, fieldOnOtherPolicyMsg, fieldOnOtherPolicyList, isWarningOtherLegalData,
					otherLegalDataMsg, otherLegalData, policyId, annualFieldDetailId, newLegalLocation, factoryContext,
					authentication);

		} catch (DaoException e) {
			throw new ServiceException("DAO threw an exception", e);
		} catch (TooManyRecordsException e) {
			throw new ServiceException("DAO threw an exception", e);
		}

		logger.debug(">validateRenameLegal");
		return result;
	}

	@Override
	public ReplaceLegalValidation<? extends Message, ? extends LegalLand<? extends Field>, ? extends AnnualField> validateReplaceLegal(
			Integer policyId, Integer annualFieldDetailId, String fieldLabel, Integer legalLandId,
			FactoryContext factoryContext, WebAdeAuthentication authentication)
			throws ServiceException, NotFoundException {

		logger.debug("<validateReplaceLegal");

		ReplaceLegalValidation<? extends Message, ? extends LegalLand<? extends Field>, ? extends AnnualField> result = null;

		try {

			PolicyDto policyDto = policyDao.fetch(policyId);

			if (policyDto == null) {
				throw new NotFoundException("Did not find the policy: " + policyId);
			}

			AnnualFieldDetailDto afdDto = annualFieldDetailDao.fetch(annualFieldDetailId);
			if (afdDto == null) {
				throw new NotFoundException("Did not find the annual field detail: " + annualFieldDetailId);
			}

			// FieldOnOtherPolicy
			Boolean isWarningFieldOnOtherPolicy = false;
			String fieldOnOtherPolicyMsg = null;

			List<PolicyDto> assocPolicies = policyDao.selectByFieldAndYear(afdDto.getFieldId(), afdDto.getCropYear());

			if (!assocPolicies.isEmpty()) {
				for (PolicyDto assocPolicyDto : assocPolicies) {
					if (!assocPolicyDto.getPolicyId().equals(policyId)) {
						isWarningFieldOnOtherPolicy = true;
						fieldOnOtherPolicyMsg = ReplaceLegalValidation.FIELD_ON_OTHER_POLICY_MSG
								.replace("[fieldLabel]", fieldLabel)
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
				fieldHasOtherLegalLandMsg = ReplaceLegalValidation.FIELD_HAS_OTHER_LEGAL_MSG
						.replace("[fieldLabel]", fieldLabel).replace("[fieldId]", afdDto.getFieldId().toString());
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

					isWarningOtherFieldsOnLegal = true;
					otherFieldsOnLegalMsg = ReplaceLegalValidation.OTHER_FIELD_ON_LEGAL_MSG
							.replace("[otherDescription]", llDto.getOtherDescription());
				}
			}

			result = uwContractFactory.getReplaceLegalValidation(isWarningFieldOnOtherPolicy, fieldOnOtherPolicyMsg,
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
