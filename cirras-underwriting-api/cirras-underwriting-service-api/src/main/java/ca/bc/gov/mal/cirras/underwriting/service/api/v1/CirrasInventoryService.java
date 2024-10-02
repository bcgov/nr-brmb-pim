package ca.bc.gov.mal.cirras.underwriting.service.api.v1;

import org.springframework.transaction.annotation.Transactional;

import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.AnnualFieldRsrc;
import ca.bc.gov.mal.cirras.underwriting.model.v1.AddFieldValidation;
import ca.bc.gov.mal.cirras.underwriting.model.v1.AnnualField;
import ca.bc.gov.mal.cirras.underwriting.model.v1.Field;
import ca.bc.gov.mal.cirras.underwriting.model.v1.InventoryContract;
import ca.bc.gov.mal.cirras.underwriting.model.v1.InventoryContractList;
import ca.bc.gov.mal.cirras.underwriting.model.v1.LegalLand;
import ca.bc.gov.mal.cirras.underwriting.model.v1.LegalLandList;
import ca.bc.gov.mal.cirras.underwriting.model.v1.RemoveFieldValidation;
import ca.bc.gov.mal.cirras.underwriting.model.v1.RenameLegalValidation;
import ca.bc.gov.mal.cirras.underwriting.model.v1.ReplaceLegalValidation;
import ca.bc.gov.nrs.wfone.common.model.Message;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.TooManyRecordsException;
import ca.bc.gov.nrs.wfone.common.service.api.ConflictException;
import ca.bc.gov.nrs.wfone.common.service.api.ForbiddenException;
import ca.bc.gov.nrs.wfone.common.service.api.MaxResultsExceededException;
import ca.bc.gov.nrs.wfone.common.service.api.NotFoundException;
import ca.bc.gov.nrs.wfone.common.service.api.ServiceException;
import ca.bc.gov.nrs.wfone.common.service.api.ValidationFailureException;
import ca.bc.gov.nrs.wfone.common.service.api.model.factory.FactoryContext;
import ca.bc.gov.nrs.wfone.common.webade.authentication.WebAdeAuthentication;

public interface CirrasInventoryService {

	@Transactional(readOnly = false, rollbackFor = Exception.class)
	InventoryContract<? extends AnnualField> createInventoryContract(
		InventoryContract<? extends AnnualField> inventoryContract,
		FactoryContext factoryContext, 
		WebAdeAuthentication authentication
	) 
	throws ServiceException, NotFoundException, ValidationFailureException;

	@Transactional(readOnly = true, rollbackFor = Exception.class)
	InventoryContract<? extends AnnualField> getInventoryContract(
		String inventoryContractGuid, 
		FactoryContext factoryContext, 
		WebAdeAuthentication authentication
	)
	throws ServiceException, NotFoundException;

	@Transactional(readOnly = false, rollbackFor = Exception.class)
	InventoryContract<? extends AnnualField> updateInventoryContract(
		String inventoryContractGuid, 
		String optimisticLock,
		InventoryContract<? extends AnnualField> inventoryContract, 
		FactoryContext factoryContext, 
		WebAdeAuthentication authentication
	)
	throws ServiceException, NotFoundException, ForbiddenException, ConflictException, ValidationFailureException;

	@Transactional(readOnly = false, rollbackFor = Exception.class)
	void deleteInventoryContract(
		String inventoryContractGuid, 
		String optimisticLock, 
		WebAdeAuthentication authentication
	)
	throws ServiceException, NotFoundException, ForbiddenException, ConflictException;

	@Transactional(readOnly = true, rollbackFor = Exception.class)
	InventoryContract<? extends AnnualField> rolloverInventoryContract(
		Integer policyId, 
		FactoryContext factoryContext, 
		WebAdeAuthentication authentication
	)
	throws ServiceException, NotFoundException;
	
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	void insertNewLand(AnnualField annualField, InventoryContract<? extends AnnualField> inventoryContract, String userId)
			throws DaoException;
	
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	String generatePID() throws DaoException;
	
	@Transactional(readOnly = true, rollbackFor = Exception.class)
	LegalLandList<? extends LegalLand<? extends Field>> getLegalLandList(
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
	) 
	throws ServiceException, MaxResultsExceededException;

	@Transactional(readOnly = true, rollbackFor = Exception.class)
	AddFieldValidation<? extends Message> validateAddField(
		Integer policyId, 
		Integer fieldId,
		Integer transferFromPolicyId,
		FactoryContext factoryContext, 
		WebAdeAuthentication authentication
	)
	throws ServiceException, NotFoundException;

	@Transactional(readOnly = true, rollbackFor = Exception.class)
	RemoveFieldValidation<? extends Message> validateRemoveField(
		Integer policyId, 
		Integer fieldId,
		FactoryContext factoryContext, 
		WebAdeAuthentication authentication
	)
	throws ServiceException, NotFoundException;
	
	@Transactional(readOnly = true, rollbackFor = Exception.class)
	RenameLegalValidation<? extends Message, ? extends LegalLand<? extends Field>, ? extends AnnualField> validateRenameLegal(
		Integer policyId,
		Integer annualFieldDetailId,
		String newLegalLocation,
		FactoryContext factoryContext, 
		WebAdeAuthentication authentication
	)
	throws ServiceException, NotFoundException;

	@Transactional(readOnly = true, rollbackFor = Exception.class)
	ReplaceLegalValidation<? extends Message, ? extends LegalLand<? extends Field>, ? extends AnnualField> validateReplaceLegal(
		Integer policyId,
		Integer annualFieldDetailId,
		String fieldLabel,
		Integer legalLandId,
		FactoryContext factoryContext, 
		WebAdeAuthentication authentication
	)
	throws ServiceException, NotFoundException;

	@Transactional(readOnly = true, rollbackFor = Exception.class)
	AnnualFieldRsrc rolloverAnnualField(
			Integer fieldId, 
			Integer rolloverToCropYear,
			Integer insurancePlanId,
			FactoryContext factoryContext,
			WebAdeAuthentication webAdeAuthentication
		)
		throws ServiceException, NotFoundException, DaoException;

	InventoryContractList<? extends InventoryContract<? extends AnnualField>> getInventoryContractList(
			Integer cropYear,
			Integer insurancePlanId, 
			Integer officeId,
			String policyStatusCode,
			String policyNumber,
			String growerInfo,
			String sortColumn,
			String inventoryContractGuids,
			FactoryContext factoryContext, 
			WebAdeAuthentication webAdeAuthentication) throws DaoException, TooManyRecordsException, ServiceException, NotFoundException;

	@Transactional(readOnly = true, rollbackFor = Exception.class)
	byte[] generateInvReport(
		Integer cropYear,
		Integer insurancePlanId, 
		Integer officeId,
		String policyStatusCode,
		String policyNumber,
		String growerInfo,
		String sortColumn,
		String policyIds, 
		FactoryContext factoryContext, 
		WebAdeAuthentication authentication
	)
	throws ServiceException, NotFoundException;
		
}
