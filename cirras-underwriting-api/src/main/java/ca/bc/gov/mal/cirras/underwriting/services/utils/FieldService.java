package ca.bc.gov.mal.cirras.underwriting.services.utils;

import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import ca.bc.gov.mal.cirras.underwriting.data.assemblers.AnnualFieldDetailRsrcFactory;
import ca.bc.gov.mal.cirras.underwriting.data.assemblers.AnnualFieldRsrcFactory;
import ca.bc.gov.mal.cirras.underwriting.data.assemblers.ContractedFieldDetailRsrcFactory;
import ca.bc.gov.mal.cirras.underwriting.data.assemblers.FieldRsrcFactory;
import ca.bc.gov.mal.cirras.underwriting.data.assemblers.LegalLandFieldXrefRsrcFactory;
import ca.bc.gov.mal.cirras.underwriting.data.assemblers.LegalLandRsrcFactory;
import ca.bc.gov.mal.cirras.underwriting.data.entities.AnnualFieldDetailDto;
import ca.bc.gov.mal.cirras.underwriting.data.entities.ContractedFieldDetailDto;
import ca.bc.gov.mal.cirras.underwriting.data.entities.DeclaredYieldContractDto;
import ca.bc.gov.mal.cirras.underwriting.data.entities.FieldDto;
import ca.bc.gov.mal.cirras.underwriting.data.entities.InventoryFieldDto;
import ca.bc.gov.mal.cirras.underwriting.data.entities.LegalLandDto;
import ca.bc.gov.mal.cirras.underwriting.data.entities.LegalLandFieldXrefDto;
import ca.bc.gov.mal.cirras.underwriting.data.entities.VerifiedYieldContractDto;
import ca.bc.gov.mal.cirras.underwriting.data.entities.YieldMeasUnitConversionDto;
import ca.bc.gov.mal.cirras.underwriting.data.models.DopYieldFieldCommodityBerries;
import ca.bc.gov.mal.cirras.underwriting.data.models.DopYieldFieldForage;
import ca.bc.gov.mal.cirras.underwriting.data.models.DopYieldFieldForageCut;
import ca.bc.gov.mal.cirras.underwriting.data.models.DopYieldFieldGrain;
import ca.bc.gov.mal.cirras.underwriting.data.models.InventoryField;
import ca.bc.gov.mal.cirras.underwriting.data.models.UnderwritingComment;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.AnnualFieldDetailDao;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.ContractedFieldDetailDao;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.DeclaredYieldFieldForageDao;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.FieldDao;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.InventoryBerriesDao;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.InventoryFieldDao;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.InventorySeededForageDao;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.InventorySeededGrainDao;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.InventoryUnseededDao;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.LegalLandDao;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.LegalLandFieldXrefDao;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.UnderwritingCommentDao;
import ca.bc.gov.mal.cirras.underwriting.data.resources.AnnualFieldRsrc;
import ca.bc.gov.mal.cirras.underwriting.data.resources.DopYieldContractRsrc;
import ca.bc.gov.mal.cirras.underwriting.data.resources.InventoryContractRsrc;
import ca.bc.gov.mal.cirras.underwriting.services.utils.InventoryServiceEnums.InsurancePlans;
import ca.bc.gov.mal.cirras.underwriting.services.utils.InventoryServiceEnums.LandIdentifierTypeCode;
import ca.bc.gov.mal.cirras.underwriting.services.utils.InventoryServiceEnums.PrimaryReferenceTypeCode;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;
import ca.bc.gov.nrs.wfone.common.service.api.NotFoundException;
import ca.bc.gov.nrs.wfone.common.service.api.ServiceException;
import ca.bc.gov.nrs.wfone.common.webade.authentication.WebAdeAuthentication;

public class FieldService {

	private static final Logger logger = LoggerFactory.getLogger(FieldService.class);

	private ContractedFieldDetailDao contractedFieldDetailDao;
	private InventoryFieldDao inventoryFieldDao;
	private DeclaredYieldFieldForageDao declaredYieldFieldForageDao;
	private InventorySeededForageDao inventorySeededForageDao;
	private InventorySeededGrainDao inventorySeededGrainDao;
	private InventoryUnseededDao inventoryUnseededDao;
	private InventoryBerriesDao inventoryBerriesDao;
	private UnderwritingCommentDao underwritingCommentDao;
	private AnnualFieldDetailDao annualFieldDetailDao;
	private LegalLandFieldXrefDao legalLandFieldXrefDao;
	private FieldDao fieldDao;
	private LegalLandDao legalLandDao;

	private GrainForageService grainForageService;
	private BerriesService berriesService;
	private UnderwritingServiceHelper underwritingServiceHelper;

	private LegalLandFieldXrefRsrcFactory legalLandFieldXrefRsrcFactory;
	private ContractedFieldDetailRsrcFactory contractedFieldDetailRsrcFactory; 
	private AnnualFieldRsrcFactory annualFieldRsrcFactory;
	private AnnualFieldDetailRsrcFactory annualFieldDetailRsrcFactory; 
	private FieldRsrcFactory fieldRsrcFactory; 
	private LegalLandRsrcFactory legalLandRsrcFactory;


	public void setContractedFieldDetailDao(ContractedFieldDetailDao contractedFieldDetailDao) {
		this.contractedFieldDetailDao = contractedFieldDetailDao;
	}

	public void setInventoryFieldDao(InventoryFieldDao inventoryFieldDao) {
		this.inventoryFieldDao = inventoryFieldDao;
	}

	public void setDeclaredYieldFieldForageDao(DeclaredYieldFieldForageDao declaredYieldFieldForageDao) {
		this.declaredYieldFieldForageDao = declaredYieldFieldForageDao;
	}	

	public void setInventorySeededForageDao(InventorySeededForageDao inventorySeededForageDao) {
		this.inventorySeededForageDao = inventorySeededForageDao;
	}

	public void setInventorySeededGrainDao(InventorySeededGrainDao inventorySeededGrainDao) {
		this.inventorySeededGrainDao = inventorySeededGrainDao;
	}

	public void setInventoryUnseededDao(InventoryUnseededDao inventoryUnseededDao) {
		this.inventoryUnseededDao = inventoryUnseededDao;
	}

	public void setInventoryBerriesDao(InventoryBerriesDao inventoryBerriesDao) {
		this.inventoryBerriesDao = inventoryBerriesDao;
	}

	public void setUnderwritingCommentDao(UnderwritingCommentDao underwritingCommentDao) {
		this.underwritingCommentDao = underwritingCommentDao;
	}
	
	public void setAnnualFieldDetailDao(AnnualFieldDetailDao annualFieldDetailDao) {
		this.annualFieldDetailDao = annualFieldDetailDao;
	}

	public void setLegalLandFieldXrefDao(LegalLandFieldXrefDao legalLandFieldXrefDao) {
		this.legalLandFieldXrefDao = legalLandFieldXrefDao;
	}

	public void setFieldDao(FieldDao fieldDao) {
		this.fieldDao = fieldDao;
	}
	
	public void setLegalLandDao(LegalLandDao legalLandDao) {
		this.legalLandDao = legalLandDao;
	}

	public void setGrainForageService(GrainForageService grainForageService) {
		this.grainForageService = grainForageService;
	}
	
	public void setBerriesService(BerriesService berriesService) {
		this.berriesService = berriesService;
	}
	
	public void setUnderwritingServiceHelper(UnderwritingServiceHelper underwritingServiceHelper) {
		this.underwritingServiceHelper = underwritingServiceHelper;
	}

	public void setLegalLandFieldXrefRsrcFactory(LegalLandFieldXrefRsrcFactory legalLandFieldXrefRsrcFactory) {
		this.legalLandFieldXrefRsrcFactory = legalLandFieldXrefRsrcFactory;
	}

	public void setContractedFieldDetailRsrcFactory(ContractedFieldDetailRsrcFactory contractedFieldDetailRsrcFactory) {
		this.contractedFieldDetailRsrcFactory = contractedFieldDetailRsrcFactory;
	}

	public void setAnnualFieldRsrcFactory(AnnualFieldRsrcFactory annualFieldRsrcFactory) {
		this.annualFieldRsrcFactory = annualFieldRsrcFactory;
	}

	public void setAnnualFieldDetailRsrcFactory(AnnualFieldDetailRsrcFactory annualFieldDetailRsrcFactory) {
		this.annualFieldDetailRsrcFactory = annualFieldDetailRsrcFactory;
	}

	public void setFieldRsrcFactory(FieldRsrcFactory fieldRsrcFactory) {
		this.fieldRsrcFactory = fieldRsrcFactory;
	}
	
	public void setLegalLandRsrcFactory(LegalLandRsrcFactory legalLandRsrcFactory) {
		this.legalLandRsrcFactory = legalLandRsrcFactory;
	}

	public void loadVerifiedFields(DeclaredYieldContractDto dto) throws DaoException {

		List<ContractedFieldDetailDto> fields = contractedFieldDetailDao.selectForVerifiedYield(dto.getContractId(), dto.getCropYear());
		dto.setFields(fields);

		for (ContractedFieldDetailDto cfdDto : dto.getFields()) {
			loadPlantings(cfdDto);
		}
	}

	public void loadVerifiedFields(VerifiedYieldContractDto dto) throws DaoException {

		List<ContractedFieldDetailDto> fields = contractedFieldDetailDao.selectForVerifiedYield(dto.getContractId(), dto.getCropYear());
		dto.setFields(fields);

		for (ContractedFieldDetailDto cfdDto : dto.getFields()) {
			loadPlantings(cfdDto);
		}
	}
	
	private void loadPlantings(ContractedFieldDetailDto cfdDto) throws DaoException {
		
		List<InventoryFieldDto> plantings = inventoryFieldDao.select(cfdDto.getFieldId(), cfdDto.getCropYear(), cfdDto.getInsurancePlanId());
		cfdDto.setPlantings(plantings);

		for (InventoryFieldDto ifDto : plantings) {

			if ( InsurancePlans.GRAIN.getInsurancePlanId().equals(cfdDto.getInsurancePlanId()) ) {
				grainForageService.loadVerifiedSeededGrains(ifDto);
			} else if ( InsurancePlans.FORAGE.getInsurancePlanId().equals(cfdDto.getInsurancePlanId()) ) {			
				grainForageService.loadVerifiedSeededForage(ifDto);
			} else {
				throw new ServiceException("Insurance Plan must be GRAIN or FORAGE");
			}
		}
	}
	
	
	public void loadDeclaredFields(DeclaredYieldContractDto dto) throws DaoException {

		List<ContractedFieldDetailDto> fields = contractedFieldDetailDao.selectForDeclaredYield(dto.getContractId(), dto.getCropYear());
		dto.setFields(fields);

		for (ContractedFieldDetailDto cfdDto : dto.getFields()) {
			loadPlantings(cfdDto, dto.getInsurancePlanId());
			berriesService.loadDeclaredYieldFieldCommodityBerries(cfdDto);
			underwritingServiceHelper.loadUwComments(cfdDto);
		}
	}
	

	public void updateAnnualField(
			DopYieldContractRsrc dopYieldContract,
			Map<String, YieldMeasUnitConversionDto> ymucMap, 
			WebAdeAuthentication authentication,
			String userId) {

		logger.debug("<updateAnnualField");

		try {
			List<AnnualFieldRsrc> fields = dopYieldContract.getFields();
			if (fields != null && !fields.isEmpty()) {
				for (AnnualFieldRsrc field : fields) {

					if ( InsurancePlans.GRAIN.getInsurancePlanId().equals(dopYieldContract.getInsurancePlanId()) ) {
						List<DopYieldFieldGrain> dopYieldFields = field.getDopYieldFieldGrainList();
						if (dopYieldFields != null && !dopYieldFields.isEmpty()) {
							for (DopYieldFieldGrain dyf : dopYieldFields) {
								dyf.setEstimatedYieldPerAcreDefaultUnit(
										underwritingServiceHelper.convertDopYieldFieldAcresWeight(dyf.getEstimatedYieldPerAcre(), dyf.getCropCommodityId(), dopYieldContract, ymucMap));
								grainForageService.updateDeclaredYieldField(dyf, userId);
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
												cut.setWeightDefaultUnit(underwritingServiceHelper.convertDopYieldFieldAcresWeight(cut.getWeight(), dyf.getCropCommodityId(), dopYieldContract, ymucMap));
											}
											grainForageService.updateDeclaredYieldFieldForage(cut, userId);
										}
									}
								}
							}
						}
					} else if ( InsurancePlans.BERRIES.getInsurancePlanId().equals(dopYieldContract.getInsurancePlanId()) ) {
						List<DopYieldFieldCommodityBerries> dopYieldFieldCommodityBerriesList = field.getDopYieldFieldCommodityBerriesList();
						if (dopYieldFieldCommodityBerriesList != null && !dopYieldFieldCommodityBerriesList.isEmpty()) {
							for (DopYieldFieldCommodityBerries dyfcb : dopYieldFieldCommodityBerriesList) {
								// Calculate
								berriesService.calculateDeclaredYieldFieldVarietyBerriesList(dyfcb.getDopYieldFieldVarietyBerriesList());
								berriesService.calculateDeclaredYieldFieldCommodityBerries(dyfcb);

								// Save
								String declaredYieldFieldCommodityBerriesGuid = berriesService.updateDeclaredYieldFieldCommodityBerries(dyfcb, userId);
								berriesService.updateDeclaredYieldFieldVarietyBerriesList(declaredYieldFieldCommodityBerriesGuid, dyfcb.getDopYieldFieldVarietyBerriesList(), userId);
							}
						}
					}
					
					// update underwriting comments
					List<UnderwritingComment> uwComments = field.getUwComments();
					if (uwComments != null && !uwComments.isEmpty()) {
						for (UnderwritingComment underwritingComment : uwComments) {
							if (underwritingComment.getDeletedByUserInd() != null
									&& underwritingComment.getDeletedByUserInd()) {
								underwritingServiceHelper.deleteUnderwritingComment(underwritingComment, userId, authentication);
							} else {
								underwritingServiceHelper.updateUnderwritingComment(underwritingComment, field.getAnnualFieldDetailId(), null,
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

	private void loadPlantings(ContractedFieldDetailDto cfdDto, Integer insurancePlanId) throws DaoException {

		List<InventoryFieldDto> plantings = inventoryFieldDao.selectForDeclaredYield(cfdDto.getFieldId(),
				cfdDto.getCropYear(), cfdDto.getInsurancePlanId());
		cfdDto.setPlantings(plantings);

		for (InventoryFieldDto ifDto : plantings) {

			if ( InsurancePlans.GRAIN.getInsurancePlanId().equals(cfdDto.getInsurancePlanId()) ) {
				grainForageService.loadDeclaredSeededGrains(ifDto);
				grainForageService.loadDeclaredYieldField(ifDto);
			} else if ( InsurancePlans.FORAGE.getInsurancePlanId().equals(cfdDto.getInsurancePlanId()) ) {
			
				grainForageService.loadSeededForage(ifDto);
				grainForageService.loadDeclaredYieldFieldForage(ifDto);
				
			} else if ( InsurancePlans.BERRIES.getInsurancePlanId().equals(cfdDto.getInsurancePlanId()) ) {
				berriesService.loadInventoryBerries(ifDto);
			} else {
				throw new ServiceException("Insurance Plan must be GRAIN, FORAGE or BERRIES");
			}			
		}
	}
	
	public void updateDisplayOrderForContract(Integer growerContractYearId, String userId)
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
	
	public void updateAnnualField(AnnualFieldRsrc annualField, InventoryContractRsrc inventoryContract,
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
				grainForageService.deleteDopData(annualField, inventoryContract);
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
				replaceLegalLocationNew(annualField, inventoryContract, userId);
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

	private void deleteField(AnnualFieldRsrc annualField, InventoryContractRsrc inventoryContract,
			HashSet<Integer> contractsToRecalculate, String userId) throws DaoException, NotFoundException {

		logger.debug("<deleteField");
		
		//Delete field
		Integer fieldId = annualField.getFieldId();

		//Delete inventory data of the field if it exists 
		inventoryFieldDao.removeLinkToPlantingForField(fieldId, userId);
		inventorySeededForageDao.deleteForField(fieldId);
		inventorySeededGrainDao.deleteForField(fieldId);
		inventoryUnseededDao.deleteForField(fieldId);
		inventoryBerriesDao.deleteForField(fieldId);
		inventoryFieldDao.deleteForField(fieldId);
		
		underwritingCommentDao.deleteForField(fieldId);
		contractedFieldDetailDao.deleteForField(fieldId);
		annualFieldDetailDao.deleteForField(fieldId);
		legalLandFieldXrefDao.deleteForField(fieldId);
		fieldDao.delete(fieldId);

		logger.debug(">deleteField");

	}
	
	private void removeFieldFromPolicy(
					AnnualFieldRsrc annualField, 
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

	private void updateField(AnnualFieldRsrc annualField, String userId)
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

	private void renameLegalLocation(AnnualFieldRsrc annualField, String userId)
			throws DaoException, NotFoundException{

		LegalLandDto dto = legalLandDao.fetch(annualField.getLegalLandId());
		if (dto == null) {
			throw new NotFoundException("Did not find the legal land: " + annualField.getLegalLandId());
		}

		// Updates pid and other description in cuws database and CIRRAS if it's different.
		if (!notNull(dto.getPrimaryPropertyIdentifier(), "").equals(notNull(annualField.getPrimaryPropertyIdentifier(), ""))) {
			
			dto.setPrimaryPropertyIdentifier(annualField.getPrimaryPropertyIdentifier());
			legalLandDao.update(dto, userId);
		}
		
		if (!notNull(dto.getOtherDescription(), "").equals(notNull(annualField.getOtherLegalDescription(), ""))) {

			dto.setOtherDescription(annualField.getOtherLegalDescription());
			legalLandDao.update(dto, userId);
		}

	}

	private void replaceLegalLocationExisting(AnnualFieldRsrc annualField, String userId)
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
				legalLandFieldXrefRsrcFactory.createLegalLandFieldXref(llfxDto, annualField);
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

	private void replaceLegalLocationNew(AnnualFieldRsrc annualField, InventoryContractRsrc inventoryContract, String userId)
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
		insertQuickLegalLand(annualField, inventoryContract, userId);

		//Update annual field detail
		afdDto.setLegalLandId(annualField.getLegalLandId());
		annualFieldDetailDao.update(afdDto, userId);

		//Insert legal land field mapping
		insertLegalLandFieldXref(annualField, userId);
	}

	private void addExistingLand(AnnualFieldRsrc annualField, InventoryContractRsrc inventoryContract,
			String userId, HashSet<Integer> contractsToRecalculate) throws DaoException, NotFoundException, ServiceException {

		// Adds a new annual field record if necessary
		processAnnualFieldDetail(annualField, userId);

		// Adds, updates or deletes a contracted field record
		processContractedFieldDetail(annualField, inventoryContract, userId, contractsToRecalculate);
	}

	private void processContractedFieldDetail(AnnualFieldRsrc annualField, InventoryContractRsrc inventoryContract, String userId,
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

	private void updateContractedFieldDetails(AnnualFieldRsrc annualField, InventoryContractRsrc inventoryContract, 
			ContractedFieldDetailDto dto, String userId) throws DaoException, NotFoundDaoException {
		
		contractedFieldDetailRsrcFactory.createContractedFieldDetail(dto, annualField, inventoryContract);
		contractedFieldDetailDao.update(dto, userId);
		
	}

	private void addContractedFieldDetail(AnnualFieldRsrc annualField, InventoryContractRsrc inventoryContract, String userId)
			throws DaoException {

		ContractedFieldDetailDto dto = contractedFieldDetailDao.fetch(annualField.getContractedFieldDetailId());

		if (dto == null) {
			insertContractedFieldDetail(annualField, inventoryContract, userId);
		} else {
			updateContractedFieldDetails(annualField, inventoryContract, dto, userId);
		}
	}

	private void processAnnualFieldDetail(AnnualFieldRsrc annualField, String userId)
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
	
	private void setPrimaryLegalLandId(AnnualFieldRsrc annualField) throws DaoException {

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
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public void insertNewLand(AnnualFieldRsrc annualField, InventoryContractRsrc inventoryContract, String userId)
			throws DaoException {

		// Legal Land doesn't have to be added if only a new field is added
		if (annualField.getLandUpdateType().equals(LandUpdateTypes.NEW_LAND)) {
			// Insert Legal Land
			insertQuickLegalLand(annualField, inventoryContract, userId);
		}

		// Insert Field
		FieldDto fieldDto = new FieldDto();
		fieldRsrcFactory.createField(fieldDto, annualField);
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

	private void insertLegalLandFieldXref(AnnualFieldRsrc annualField, String userId) throws DaoException {
		LegalLandFieldXrefDto legalLandFieldXrefDto = new LegalLandFieldXrefDto();
		legalLandFieldXrefRsrcFactory.createLegalLandFieldXref(legalLandFieldXrefDto, annualField);
		legalLandFieldXrefDao.insert(legalLandFieldXrefDto, userId);
	}

	private void insertQuickLegalLand(AnnualFieldRsrc annualField, InventoryContractRsrc inventoryContract, String userId) throws DaoException {
		LegalLandDto legalLandDto = new LegalLandDto();
		if(annualField.getPrimaryPropertyIdentifier() == null || annualField.getPrimaryPropertyIdentifier().isEmpty()) {
			annualField.setPrimaryPropertyIdentifier(generatePID());
		}
		
		String primaryReferenceTypeCode = PrimaryReferenceTypeCode.OTHER.toString();
		String landIdentifierTypeCode = LandIdentifierTypeCode.OTHER.toString();
		
		if(InsurancePlans.BERRIES.getInsurancePlanId().equals(inventoryContract.getInsurancePlanId())) {
			primaryReferenceTypeCode = PrimaryReferenceTypeCode.IDENTIFIER.toString();
			landIdentifierTypeCode = LandIdentifierTypeCode.PID.toString();
		}
		legalLandRsrcFactory.createQuickLegalLand(legalLandDto, annualField, primaryReferenceTypeCode, landIdentifierTypeCode);
		legalLandDao.insert(legalLandDto, userId);
		annualField.setLegalLandId(legalLandDto.getLegalLandId());
	}
	
	// This method is public for testing reasons
	// Generates a 12 character PID starting with GF followed by a sequence value lead by zeros
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public String generatePID() throws DaoException {
		
		logger.debug("<generatePID");
		
		Integer nextSequence = legalLandDao.getNextPidSequence();
		
		String newPid = "0000000000" + nextSequence.toString();
		//GF is meant to be used for Grain and Forage
		newPid = "GF" + newPid.substring(newPid.length() - 10);
		
		logger.debug(">generatePID");
		
		return newPid;
		
	}

	private void insertAnnualFieldDetail(AnnualFieldRsrc annualField, String userId) throws DaoException {

		logger.debug("<insertAnnualFieldDetail");
		
		AnnualFieldDetailDto annualFieldDetailDto = new AnnualFieldDetailDto();
		annualFieldDetailRsrcFactory.createAnnualFieldDetail(annualFieldDetailDto, annualField);
		annualFieldDetailDao.insert(annualFieldDetailDto, userId);
		
		annualField.setAnnualFieldDetailId(annualFieldDetailDto.getAnnualFieldDetailId());
		
		logger.debug(">insertAnnualFieldDetail");
	}

	private void insertContractedFieldDetail(AnnualFieldRsrc annualField, InventoryContractRsrc inventoryContract, String userId)
			throws DaoException {
		
		logger.debug("<insertContractedFieldDetail");
		
		// Insert Contracted Field Details
		ContractedFieldDetailDto contractedFieldDetailDto = new ContractedFieldDetailDto();
		contractedFieldDetailRsrcFactory.createContractedFieldDetail(contractedFieldDetailDto, annualField, inventoryContract);
		contractedFieldDetailDto.setContractedFieldDetailId(null);

		contractedFieldDetailDao.insert(contractedFieldDetailDto, userId);
		annualField.setContractedFieldDetailId(contractedFieldDetailDto.getContractedFieldDetailId());
		
		logger.debug(">insertContractedFieldDetail");
	}

	private void updateContractedFieldDetails(AnnualFieldRsrc annualField, String userId)
			throws DaoException, NotFoundException {

		ContractedFieldDetailDto dto = contractedFieldDetailDao.fetch(annualField.getContractedFieldDetailId());

		if (dto == null) {
			throw new NotFoundException("Did not find the annual field: " + annualField.getContractedFieldDetailId());
		}

		annualFieldRsrcFactory.updateDto(dto, annualField);

		contractedFieldDetailDao.update(dto, userId);
	}

	private String notNull(String value, String defaultValue) {
		return (value == null) ? defaultValue : value;
	}

}
