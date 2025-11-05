package ca.bc.gov.mal.cirras.underwriting.service.api.v1.model.factory;

import ca.bc.gov.mal.cirras.underwriting.model.v1.AnnualField;
import ca.bc.gov.mal.cirras.underwriting.model.v1.InventoryContract;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.ContractedFieldDetailDto;

public interface ContractedFieldDetailFactory {

	void createContractedFieldDetail(ContractedFieldDetailDto dto, AnnualField model, InventoryContract<? extends AnnualField> inventoryContract);

	void createRolloverContractedFieldDetail(ContractedFieldDetailDto dto, Integer annualFieldDetailId, Integer growerContractYearId, Integer displayOrder, Boolean isLeasedInd);
}
