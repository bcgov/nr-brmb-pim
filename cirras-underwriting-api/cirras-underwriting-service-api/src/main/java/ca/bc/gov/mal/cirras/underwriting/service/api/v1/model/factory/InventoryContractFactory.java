package ca.bc.gov.mal.cirras.underwriting.service.api.v1.model.factory;

import java.util.List;

import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.service.api.model.factory.FactoryContext;
import ca.bc.gov.nrs.wfone.common.service.api.model.factory.FactoryException;
import ca.bc.gov.nrs.wfone.common.webade.authentication.WebAdeAuthentication;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.AnnualFieldRsrc;
import ca.bc.gov.mal.cirras.underwriting.model.v1.AnnualField;
import ca.bc.gov.mal.cirras.underwriting.model.v1.InventoryContract;
import ca.bc.gov.mal.cirras.underwriting.model.v1.InventoryContractCommodity;
import ca.bc.gov.mal.cirras.underwriting.model.v1.InventoryContractList;
import ca.bc.gov.mal.cirras.underwriting.model.v1.InventoryCoverageTotalForage;
import ca.bc.gov.mal.cirras.underwriting.model.v1.InventoryField;
import ca.bc.gov.mal.cirras.underwriting.model.v1.InventorySeededForage;
import ca.bc.gov.mal.cirras.underwriting.model.v1.InventorySeededGrain;
import ca.bc.gov.mal.cirras.underwriting.model.v1.InventoryUnseeded;
import ca.bc.gov.mal.cirras.underwriting.model.v1.UnderwritingComment;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.ContractedFieldDetailDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.InventoryContractCommodityDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.InventoryContractDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.InventoryCoverageTotalForageDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.InventoryFieldDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.InventorySeededForageDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.InventorySeededGrainDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.InventoryUnseededDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.PolicyDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.UnderwritingCommentDto;

public interface InventoryContractFactory {

	InventoryContract<? extends AnnualField> getInventoryContract(
			InventoryContractDto dto, 
			FactoryContext context, 
			WebAdeAuthentication authentication
		) throws FactoryException;

//	InventoryContract<? extends AnnualField> createDefaultInventoryContract(
//			PolicyDto policyDto,
//			List<ContractedFieldDetailDto> fieldDtos, 
//			FactoryContext context, 
//			WebAdeAuthentication authentication
//		) throws FactoryException;

	InventoryContract<? extends AnnualField> createRolloverInventoryContract(
			PolicyDto policyDto, 
			List<AnnualFieldRsrc> fields,
			FactoryContext context, 
			WebAdeAuthentication authentication
		) throws FactoryException;

	AnnualFieldRsrc addRolloverAnnualField(Integer insurancePlanId, ContractedFieldDetailDto dto, WebAdeAuthentication authentication);

	AnnualFieldRsrc createDefaultAnnualField(Integer insurancePlanId, ContractedFieldDetailDto dto, WebAdeAuthentication authentication);

	void updateDto(InventoryContractDto dto, InventoryContract<? extends AnnualField> model, String userId);

	void updateDto(InventoryContractCommodityDto dto, InventoryContractCommodity model);

	void updateDto(InventoryCoverageTotalForageDto dto, InventoryCoverageTotalForage model);
	
	void updateDto(InventoryFieldDto dto, InventoryField model);

	void updateDto(InventoryUnseededDto dto, InventoryUnseeded model);

	void updateDto(InventorySeededGrainDto dto, InventorySeededGrain model);

	void updateDto(InventorySeededForageDto dto, InventorySeededForage model);
	
	void updateDto(UnderwritingCommentDto dto, UnderwritingComment model);

	AnnualFieldRsrc createAnnualField(ContractedFieldDetailDto dto, WebAdeAuthentication authentication);

	InventoryContractList<? extends InventoryContract<? extends AnnualField>> getInventoryContractList(
			List<InventoryContractDto> inventoryContractDtos, 
			Integer cropYear, 
			Integer insurancePlanId,
			Integer officeId, 
			String policyStatusCode, 
			String policyNumber, 
			String growerInfo, 
			String sortColumn,
			String inventoryContractGuids,
			FactoryContext factoryContext, 
			WebAdeAuthentication webAdeAuthentication) throws FactoryException, DaoException;

	// checkEmpty methods return true if this record has no user-entered data, and can be safely deleted.
	boolean checkEmptyInventoryUnseeded(InventoryUnseeded inventoryUnseeded);
	boolean checkEmptyInventoryUnseeded(InventoryUnseededDto inventoryUnseeded);

	boolean checkEmptyInventorySeededGrain(InventorySeededGrain inventorySeededGrain);
	boolean checkEmptyInventorySeededGrain(InventorySeededGrainDto inventorySeededGrain);

	// Returns true if current user is authorized to edit this comment, false if not. If it cannot be determined, 
	// for instance because authentication is null, then returns null.
	Boolean checkUserCanEditComment(UnderwritingCommentDto dto, WebAdeAuthentication authentication);
	
	// Returns true if current user is authorized to delete this comment, false if not. If it cannot be determined, 
	// for instance because authentication is null, then returns false.
	Boolean checkUserCanDeleteComment(UnderwritingCommentDto dto, WebAdeAuthentication authentication);
	
}
