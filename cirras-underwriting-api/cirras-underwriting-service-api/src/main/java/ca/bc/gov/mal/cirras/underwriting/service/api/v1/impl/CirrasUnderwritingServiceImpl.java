package ca.bc.gov.mal.cirras.underwriting.service.api.v1.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import ca.bc.gov.mal.cirras.underwriting.model.v1.AnnualField;
import ca.bc.gov.mal.cirras.underwriting.model.v1.AnnualFieldList;
import ca.bc.gov.mal.cirras.underwriting.model.v1.UwContract;
import ca.bc.gov.mal.cirras.underwriting.model.v1.UwContractList;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.FieldDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.PolicyDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.FieldDao;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.PolicyDao;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.TooManyRecordsException;
import ca.bc.gov.nrs.wfone.common.persistence.dto.PagedDtos;
import ca.bc.gov.nrs.wfone.common.service.api.MaxResultsExceededException;
import ca.bc.gov.nrs.wfone.common.service.api.NotFoundException;
import ca.bc.gov.nrs.wfone.common.service.api.ServiceException;
import ca.bc.gov.nrs.wfone.common.service.api.model.factory.FactoryContext;
import ca.bc.gov.nrs.wfone.common.webade.authentication.WebAdeAuthentication;
import ca.bc.gov.mal.cirras.underwriting.service.api.v1.CirrasUnderwritingService;
import ca.bc.gov.mal.cirras.underwriting.service.api.v1.model.factory.AnnualFieldFactory;
import ca.bc.gov.mal.cirras.underwriting.service.api.v1.model.factory.UwContractFactory;
import ca.bc.gov.mal.cirras.underwriting.service.api.v1.util.UnderwritingServiceHelper;
import ca.bc.gov.mal.cirras.underwriting.service.api.v1.util.OutOfSync;
import ca.bc.gov.mal.cirras.underwriting.service.api.v1.validation.ModelValidator;


public class CirrasUnderwritingServiceImpl implements CirrasUnderwritingService {

	private static final Logger logger = LoggerFactory.getLogger(CirrasUnderwritingServiceImpl.class);

	private Properties applicationProperties;

	private ModelValidator modelValidator;

	// factories
	private AnnualFieldFactory annualFieldFactory;
	private UwContractFactory uwContractFactory;
	
	// utils
	private OutOfSync outOfSync;

	// daos
	private FieldDao fieldDao;
	private PolicyDao policyDao;

	//utils
	//@Autowired
	private UnderwritingServiceHelper underwritingServiceHelper;
	
	public static final String MaximumResultsProperty = "maximum.results";

	public static final int DefaultMaximumResults = 800;

	public void setUnderwritingServiceHelper(UnderwritingServiceHelper underwritingServiceHelper) {
		this.underwritingServiceHelper = underwritingServiceHelper;
	}
	
	public void setApplicationProperties(Properties applicationProperties) {
		this.applicationProperties = applicationProperties;
	}

	public void setModelValidator(ModelValidator modelValidator) {
		this.modelValidator = modelValidator;
	}

	public void setAnnualFieldFactory(AnnualFieldFactory annualFieldFactory) {
		this.annualFieldFactory = annualFieldFactory;
	}
	
	public void setUwContractFactory(UwContractFactory uwContractFactory) {
		this.uwContractFactory = uwContractFactory;
	}
	
	public void setFieldDao(FieldDao fieldDao) {
		this.fieldDao = fieldDao;
	}
	
	public void setPolicyDao(PolicyDao policyDao) {
		this.policyDao = policyDao;
	}

	public void setOutOfSync(OutOfSync outOfSync) {
		this.outOfSync = outOfSync;
	}
	
	@Override
	public UwContractList<? extends UwContract<? extends UwContract<?>>> getUwContractList(
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
			WebAdeAuthentication authentication) throws ServiceException, MaxResultsExceededException {

		logger.debug("<getUwContractList");
		
		UwContractList<? extends UwContract<? extends UwContract<?>>> results = null;

		try {
			int maximumRows = DefaultMaximumResults;
			
			PagedDtos<PolicyDto> dtos = policyDao.select(
					cropYear, 
					insurancePlanId,
					officeId,
					policyStatusCode,
					policyNumber,
					growerInfo,
					datasetType,
					sortColumn, 
					sortDirection, 
					maximumRows, 
					pageNumber, 
					pageRowCount);

			results = uwContractFactory.getUwContractList(
					dtos, 
					cropYear, 
					insurancePlanId,
					officeId,
					policyStatusCode,
					policyNumber,
					growerInfo,
					datasetType,
					sortColumn, 
					sortDirection, 
					pageRowCount, 
					context, 
					authentication);

		} catch (DaoException e) {
			throw new ServiceException("DAO threw an exception", e);
		} catch (TooManyRecordsException e) {
			throw new MaxResultsExceededException(e.getMessage(), e);
		}
		
		logger.debug(">getUwContractList");

		return results;

	}


	@Override
	public UwContract<? extends UwContract<?>> getUwContract(Integer policyId, Boolean loadLinkedPolicies, FactoryContext factoryContext, WebAdeAuthentication authentication)
			throws ServiceException, NotFoundException {
		logger.debug("<getUwContract");

		UwContract<? extends UwContract<?>> result = null;

		try {
			PolicyDto dto = policyDao.fetch(policyId);

			if (dto == null) {
				throw new NotFoundException("Did not find the policy: " + policyId);
			}

			List<PolicyDto> linkedPolicyDtos = null;
			if ( Boolean.TRUE.equals(loadLinkedPolicies) ) {
			
				PagedDtos<PolicyDto> linkedPolicyResultDtos = policyDao.select(
						null, 
						null, 
						null, 
						null, 
						dto.getPolicyNumber(),
						null,
						"LINKED_GF_POLICIES",
						"policyNumber", 
						"ASC", 
						DefaultMaximumResults, 
						null, 
						null);
							
				linkedPolicyDtos = linkedPolicyResultDtos.getResults();
			}

			result = uwContractFactory.getUwContract(dto, linkedPolicyDtos, loadLinkedPolicies, factoryContext, authentication);

		} catch (DaoException e) {
			throw new ServiceException("DAO threw an exception", e);
		} catch (TooManyRecordsException e) {
			throw new ServiceException("DAO threw an exception", e);
		}

		logger.debug(">getUwContract");
		return result;
	}

	@Override
	public AnnualFieldList<? extends AnnualField> getAnnualFieldForLegalLandList(
			Integer legalLandId, 
			Integer fieldId, 
			Integer cropYear,
			FactoryContext context,
			WebAdeAuthentication authentication) throws ServiceException {

		logger.debug("<getAnnualFieldList");
		
		AnnualFieldList<? extends AnnualField> results = null;

		try {
			
			List<FieldDto> dtos = fieldDao.selectForLegalLandOrField(legalLandId, fieldId, cropYear);
			
			for ( FieldDto fDto : dtos ) {

				List<PolicyDto> policies = policyDao.selectByFieldAndYear(fDto.getFieldId(), fDto.getMaxCropYear());
				fDto.setPolicies(policies);
			}
			
			
			results = annualFieldFactory.getAnnualFieldList(
					dtos, 
					legalLandId, 
					cropYear,
					context, 
					authentication);

		} catch (DaoException e) {
			throw new ServiceException("DAO threw an exception", e);
		}
		
		logger.debug(">getAnnualFieldList");

		return results;

	}
	
}
