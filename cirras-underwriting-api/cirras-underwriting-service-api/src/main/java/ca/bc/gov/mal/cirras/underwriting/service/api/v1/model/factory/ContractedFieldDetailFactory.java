package ca.bc.gov.mal.cirras.underwriting.services.model.factory;

import ca.bc.gov.mal.cirras.underwriting.data.models.AnnualField;
import ca.bc.gov.mal.cirras.underwriting.data.models.InventoryContract;
import ca.bc.gov.mal.cirras.underwriting.data.entities.ContractedFieldDetailDto;

public interface ContractedFieldDetailFactory {

	void createContractedFieldDetail(ContractedFieldDetailDto dto, AnnualField model, InventoryContract<? extends AnnualField> inventoryContract);

	void createRolloverContractedFieldDetail(ContractedFieldDetailDto dto, Integer annualFieldDetailId, Integer growerContractYearId, Integer displayOrder, Boolean isLeasedInd);
}
