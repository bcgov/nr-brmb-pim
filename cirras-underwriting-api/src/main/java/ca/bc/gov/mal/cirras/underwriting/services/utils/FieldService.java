package ca.bc.gov.mal.cirras.underwriting.services.utils;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.mal.cirras.underwriting.data.entities.ContractedFieldDetailDto;
import ca.bc.gov.mal.cirras.underwriting.data.entities.DeclaredYieldContractDto;
import ca.bc.gov.mal.cirras.underwriting.data.entities.InventoryFieldDto;
import ca.bc.gov.mal.cirras.underwriting.data.entities.VerifiedYieldContractDto;
import ca.bc.gov.mal.cirras.underwriting.data.entities.YieldMeasUnitConversionDto;
import ca.bc.gov.mal.cirras.underwriting.data.models.DopYieldFieldCommodityBerries;
import ca.bc.gov.mal.cirras.underwriting.data.models.DopYieldFieldForage;
import ca.bc.gov.mal.cirras.underwriting.data.models.DopYieldFieldForageCut;
import ca.bc.gov.mal.cirras.underwriting.data.models.DopYieldFieldGrain;
import ca.bc.gov.mal.cirras.underwriting.data.models.UnderwritingComment;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.ContractedFieldDetailDao;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.DeclaredYieldFieldForageDao;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.InventoryFieldDao;
import ca.bc.gov.mal.cirras.underwriting.data.resources.AnnualFieldRsrc;
import ca.bc.gov.mal.cirras.underwriting.data.resources.DopYieldContractRsrc;
import ca.bc.gov.mal.cirras.underwriting.services.utils.InventoryServiceEnums.InsurancePlans;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.service.api.ServiceException;
import ca.bc.gov.nrs.wfone.common.webade.authentication.WebAdeAuthentication;

public class FieldService {

	private static final Logger logger = LoggerFactory.getLogger(FieldService.class);

	private ContractedFieldDetailDao contractedFieldDetailDao;
	private InventoryFieldDao inventoryFieldDao;
	private DeclaredYieldFieldForageDao declaredYieldFieldForageDao;

	private GrainForageService grainForageService;
	private BerriesService berriesService;
	private UnderwritingServiceHelper underwritingServiceHelper;
	
	public void setContractedFieldDetailDao(ContractedFieldDetailDao contractedFieldDetailDao) {
		this.contractedFieldDetailDao = contractedFieldDetailDao;
	}

	public void setInventoryFieldDao(InventoryFieldDao inventoryFieldDao) {
		this.inventoryFieldDao = inventoryFieldDao;
	}

	public void setDeclaredYieldFieldForageDao(DeclaredYieldFieldForageDao declaredYieldFieldForageDao) {
		this.declaredYieldFieldForageDao = declaredYieldFieldForageDao;
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

}
