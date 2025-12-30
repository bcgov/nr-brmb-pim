package ca.bc.gov.mal.cirras.underwriting.data.assemblers;

import ca.bc.gov.nrs.wfone.common.rest.endpoints.resource.factory.BaseResourceFactory;

import ca.bc.gov.mal.cirras.underwriting.data.models.AnnualField;
import ca.bc.gov.mal.cirras.underwriting.data.models.InventoryContract;
import ca.bc.gov.mal.cirras.underwriting.data.entities.ContractedFieldDetailDto;
import ca.bc.gov.mal.cirras.underwriting.services.model.factory.ContractedFieldDetailFactory;

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
