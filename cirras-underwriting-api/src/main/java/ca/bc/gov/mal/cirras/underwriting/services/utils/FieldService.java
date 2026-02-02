package ca.bc.gov.mal.cirras.underwriting.services.utils;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.mal.cirras.underwriting.data.entities.ContractedFieldDetailDto;
import ca.bc.gov.mal.cirras.underwriting.data.entities.DeclaredYieldContractDto;
import ca.bc.gov.mal.cirras.underwriting.data.entities.InventoryFieldDto;
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

	
	public void setContractedFieldDetailDao(ContractedFieldDetailDao contractedFieldDetailDao) {
		this.contractedFieldDetailDao = contractedFieldDetailDao;
	}

	public void setInventoryFieldDao(InventoryFieldDao inventoryFieldDao) {
		this.inventoryFieldDao = inventoryFieldDao;
	}
	
	public void setGrainForageService(GrainForageService grainForageService) {
		this.grainForageService = grainForageService;
	}
	
	public void loadFields(DeclaredYieldContractDto dto) throws DaoException {

		List<ContractedFieldDetailDto> fields = contractedFieldDetailDao.selectForVerifiedYield(dto.getContractId(), dto.getCropYear());
		dto.setFields(fields);

		for (ContractedFieldDetailDto cfdDto : dto.getFields()) {
			loadPlantings(cfdDto);
		}
	}

	public void loadFields(VerifiedYieldContractDto dto) throws DaoException {

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
				grainForageService.loadSeededGrains(ifDto);
			} else if ( InsurancePlans.FORAGE.getInsurancePlanId().equals(cfdDto.getInsurancePlanId()) ) {			
				grainForageService.loadSeededForage(ifDto);
			} else {
				throw new ServiceException("Insurance Plan must be GRAIN or FORAGE");
			}
		}
	}
}
