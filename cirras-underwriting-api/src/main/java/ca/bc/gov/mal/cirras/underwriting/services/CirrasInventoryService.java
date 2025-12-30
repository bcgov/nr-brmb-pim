package ca.bc.gov.mal.cirras.underwriting.services;

import org.springframework.transaction.annotation.Transactional;

import ca.bc.gov.mal.cirras.underwriting.data.resources.AddFieldValidationRsrc;
import ca.bc.gov.mal.cirras.underwriting.data.resources.AnnualFieldRsrc;
import ca.bc.gov.mal.cirras.underwriting.data.resources.InventoryContractListRsrc;
import ca.bc.gov.mal.cirras.underwriting.data.resources.InventoryContractRsrc;
import ca.bc.gov.mal.cirras.underwriting.data.resources.LegalLandListRsrc;
import ca.bc.gov.mal.cirras.underwriting.data.resources.RemoveFieldValidationRsrc;
import ca.bc.gov.mal.cirras.underwriting.data.resources.RenameLegalValidationRsrc;
import ca.bc.gov.mal.cirras.underwriting.data.resources.ReplaceLegalValidationRsrc;
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
	InventoryContractRsrc createInventoryContract(
		InventoryContractRsrc inventoryContract,
		FactoryContext factoryContext, 
		WebAdeAuthentication authentication
	) 
	throws ServiceException, NotFoundException, ValidationFailureException;

	@Transactional(readOnly = true, rollbackFor = Exception.class)
	InventoryContractRsrc getInventoryContract(
		String inventoryContractGuid, 
		FactoryContext factoryContext, 
		WebAdeAuthentication authentication
	)
	throws ServiceException, NotFoundException;

	@Transactional(readOnly = false, rollbackFor = Exception.class)
	InventoryContractRsrc updateInventoryContract(
		String inventoryContractGuid, 
		String optimisticLock,
		InventoryContractRsrc inventoryContract, 
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
	InventoryContractRsrc rolloverInventoryContract(
		Integer policyId, 
		FactoryContext factoryContext, 
		WebAdeAuthentication authentication
	)
	throws ServiceException, NotFoundException;
	
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	void insertNewLand(AnnualFieldRsrc annualField, InventoryContractRsrc inventoryContract, String userId)
			throws DaoException;
	
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	String generatePID() throws DaoException;
	
	@Transactional(readOnly = true, rollbackFor = Exception.class)
	LegalLandListRsrc getLegalLandList(
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
	AddFieldValidationRsrc validateAddField(
		Integer policyId, 
		Integer fieldId,
		Integer transferFromPolicyId,
		FactoryContext factoryContext, 
		WebAdeAuthentication authentication
	)
	throws ServiceException, NotFoundException;

	@Transactional(readOnly = true, rollbackFor = Exception.class)
	RemoveFieldValidationRsrc validateRemoveField(
		Integer policyId, 
		Integer fieldId,
		FactoryContext factoryContext, 
		WebAdeAuthentication authentication
	)
	throws ServiceException, NotFoundException;
	
	@Transactional(readOnly = true, rollbackFor = Exception.class)
	RenameLegalValidationRsrc validateRenameLegal(
		Integer policyId,
		Integer annualFieldDetailId,
		String newLegalLocation,
		String primaryPropertyIdentifier,
		FactoryContext factoryContext, 
		WebAdeAuthentication authentication
	)
	throws ServiceException, NotFoundException;

	@Transactional(readOnly = true, rollbackFor = Exception.class)
	ReplaceLegalValidationRsrc validateReplaceLegal(
		Integer policyId,
		Integer annualFieldDetailId,
		String fieldLabel,
		Integer legalLandId,
		String fieldLocation,
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

	InventoryContractListRsrc getInventoryContractList(
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
		String reportType,
		FactoryContext factoryContext, 
		WebAdeAuthentication authentication
	)
	throws ServiceException, NotFoundException;
		
}
