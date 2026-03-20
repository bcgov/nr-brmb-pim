package ca.bc.gov.mal.cirras.underwriting.services.utils;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.mal.cirras.underwriting.data.entities.ContractedFieldDetailDto;
import ca.bc.gov.mal.cirras.underwriting.data.entities.DeclaredYieldContractDto;
import ca.bc.gov.mal.cirras.underwriting.data.entities.InventoryFieldDto;
import ca.bc.gov.mal.cirras.underwriting.data.entities.UnderwritingCommentDto;
import ca.bc.gov.mal.cirras.underwriting.data.entities.VerifiedYieldContractDto;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.ContractedFieldDetailDao;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.InventoryFieldDao;
import ca.bc.gov.mal.cirras.underwriting.services.utils.InventoryServiceEnums.InsurancePlans;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.service.api.ServiceException;

public class FieldService {

	private static final Logger logger = LoggerFactory.getLogger(FieldService.class);

	private ContractedFieldDetailDao contractedFieldDetailDao;
	private InventoryFieldDao inventoryFieldDao;

	private GrainForageService grainForageService;
	private BerriesService berriesService;
	private UnderwritingServiceHelper underwritingServiceHelper;
	
	public void setContractedFieldDetailDao(ContractedFieldDetailDao contractedFieldDetailDao) {
		this.contractedFieldDetailDao = contractedFieldDetailDao;
	}

	public void setInventoryFieldDao(InventoryFieldDao inventoryFieldDao) {
		this.inventoryFieldDao = inventoryFieldDao;
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
