package ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.factory;

import ca.bc.gov.nrs.wfone.common.rest.endpoints.resource.factory.BaseResourceFactory;

import ca.bc.gov.mal.cirras.underwriting.model.v1.AnnualField;
import ca.bc.gov.mal.cirras.underwriting.model.v1.InventoryContract;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.ContractedFieldDetailDto;
import ca.bc.gov.mal.cirras.underwriting.service.api.v1.model.factory.ContractedFieldDetailFactory;

public class ContractedFieldDetailRsrcFactory extends BaseResourceFactory implements ContractedFieldDetailFactory { 
	
	@Override
	public void createContractedFieldDetail(ContractedFieldDetailDto dto, AnnualField model, InventoryContract<? extends AnnualField> inventoryContract) {

		dto.setAnnualFieldDetailId(model.getAnnualFieldDetailId());
		dto.setGrowerContractYearId(inventoryContract.getGrowerContractYearId());
		dto.setDisplayOrder(model.getDisplayOrder());
		///Not all plans use the flag and it might be not set
		dto.setIsLeasedInd(notNull(model.getIsLeasedInd(), false));

	}

	@Override
	public void createRolloverContractedFieldDetail(ContractedFieldDetailDto dto, Integer annualFieldDetailId, Integer growerContractYearId, Integer displayOrder, Boolean isLeasedInd) {		
		dto.setContractedFieldDetailId(null);
		dto.setAnnualFieldDetailId(annualFieldDetailId);
		dto.setGrowerContractYearId(growerContractYearId);
		dto.setDisplayOrder(displayOrder);
		///Not all plans use the flag and it might be null at this point
		dto.setIsLeasedInd(notNull(isLeasedInd, false));
	}
	
	private Boolean notNull(Boolean value, Boolean defaultValue) {
		return (value == null) ? defaultValue : value;
	}

}
