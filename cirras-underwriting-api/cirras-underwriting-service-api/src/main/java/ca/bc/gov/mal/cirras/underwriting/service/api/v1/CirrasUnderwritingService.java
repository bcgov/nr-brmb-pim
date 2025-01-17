package ca.bc.gov.mal.cirras.underwriting.service.api.v1;

import org.springframework.transaction.annotation.Transactional;

import ca.bc.gov.mal.cirras.underwriting.model.v1.AnnualField;
import ca.bc.gov.mal.cirras.underwriting.model.v1.AnnualFieldList;
import ca.bc.gov.mal.cirras.underwriting.model.v1.UserSetting;
import ca.bc.gov.mal.cirras.underwriting.model.v1.UwContract;
import ca.bc.gov.mal.cirras.underwriting.model.v1.UwContractList;
import ca.bc.gov.nrs.wfone.common.service.api.MaxResultsExceededException;
import ca.bc.gov.nrs.wfone.common.service.api.NotFoundException;
import ca.bc.gov.nrs.wfone.common.service.api.ServiceException;
import ca.bc.gov.nrs.wfone.common.service.api.model.factory.FactoryContext;
import ca.bc.gov.nrs.wfone.common.webade.authentication.WebAdeAuthentication;

public interface CirrasUnderwritingService {

	@Transactional(readOnly = true, rollbackFor = Exception.class)
	UwContractList<? extends UwContract<? extends UwContract<?>>> getUwContractList(
			Integer cropYear, 
			Integer insurancePlanId, 
			Integer officeId,
			String policyStatusCode,
			String policyNumber,
			String growerInfo,
    		String datasetType,
			String sortColumn,
			String sortDirection,
		    Integer pageNumber,
			Integer pageRowCount,
			FactoryContext context, 
			WebAdeAuthentication authentication
	) 
	throws ServiceException, MaxResultsExceededException;	

	@Transactional(readOnly = true, rollbackFor = Exception.class)
	UwContract<? extends UwContract<?>> getUwContract(
		Integer policyId, 
		Boolean loadLinkedPolicies,
		Boolean loadOtherYearPolicies,
		String screenType,
		FactoryContext factoryContext, 
		WebAdeAuthentication authentication
	)
	throws ServiceException, NotFoundException;

	@Transactional(readOnly = true, rollbackFor = Exception.class)
	AnnualFieldList<? extends AnnualField> getAnnualFieldForLegalLandList(
			Integer legalLandId, 
			Integer fieldId, 
			Integer cropYear,
			FactoryContext context, 
			WebAdeAuthentication authentication
	) 
	throws ServiceException, MaxResultsExceededException;

	@Transactional(readOnly = true, rollbackFor = Exception.class)
	UserSetting searchUserSetting(
		FactoryContext factoryContext, 
		WebAdeAuthentication authentication
	)
	throws ServiceException, NotFoundException;

}
