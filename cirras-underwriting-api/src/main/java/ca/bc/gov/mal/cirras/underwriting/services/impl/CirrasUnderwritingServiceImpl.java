package ca.bc.gov.mal.cirras.underwriting.services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.mal.cirras.underwriting.data.entities.FieldDto;
import ca.bc.gov.mal.cirras.underwriting.data.entities.PolicyDto;
import ca.bc.gov.mal.cirras.underwriting.data.entities.UserSettingDto;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.FieldDao;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.PolicyDao;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.UserSettingDao;
import ca.bc.gov.mal.cirras.underwriting.data.resources.AnnualFieldListRsrc;
import ca.bc.gov.mal.cirras.underwriting.data.resources.UserSettingRsrc;
import ca.bc.gov.mal.cirras.underwriting.data.resources.UwContractListRsrc;
import ca.bc.gov.mal.cirras.underwriting.data.resources.UwContractRsrc;
import ca.bc.gov.nrs.wfone.common.model.Message;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.TooManyRecordsException;
import ca.bc.gov.nrs.wfone.common.persistence.dto.PagedDtos;
import ca.bc.gov.nrs.wfone.common.service.api.ConflictException;
import ca.bc.gov.nrs.wfone.common.service.api.ForbiddenException;
import ca.bc.gov.nrs.wfone.common.service.api.MaxResultsExceededException;
import ca.bc.gov.nrs.wfone.common.service.api.NotFoundException;
import ca.bc.gov.nrs.wfone.common.service.api.ServiceException;
import ca.bc.gov.nrs.wfone.common.service.api.ValidationFailureException;
import ca.bc.gov.nrs.wfone.common.service.api.model.factory.FactoryContext;
import ca.bc.gov.nrs.wfone.common.webade.authentication.WebAdeAuthentication;
import ca.bc.gov.mal.cirras.underwriting.services.CirrasUnderwritingService;
import ca.bc.gov.mal.cirras.underwriting.data.assemblers.AnnualFieldRsrcFactory;
import ca.bc.gov.mal.cirras.underwriting.data.assemblers.UserSettingRsrcFactory;
import ca.bc.gov.mal.cirras.underwriting.data.assemblers.UwContractRsrcFactory;
import ca.bc.gov.mal.cirras.underwriting.services.utils.InventoryServiceEnums.ScreenType;

public class CirrasUnderwritingServiceImpl implements CirrasUnderwritingService {

	private static final Logger logger = LoggerFactory.getLogger(CirrasUnderwritingServiceImpl.class);

	private Properties applicationProperties;

	// factories
	private AnnualFieldRsrcFactory annualFieldRsrcFactory;
	private UwContractRsrcFactory uwContractRsrcFactory;
	private UserSettingRsrcFactory userSettingRsrcFactory;

	// daos
	private FieldDao fieldDao;
	private PolicyDao policyDao;
	private UserSettingDao userSettingDao;

	public static final String MaximumResultsProperty = "maximum.results";

	public static final int DefaultMaximumResults = 800;

	// Number of past or future years to search when loading UwContract.otherYearPolicies.
	public static final Integer numOtherYearPoliciesToLoad = 5;
	
	public void setApplicationProperties(Properties applicationProperties) {
		this.applicationProperties = applicationProperties;
	}

	public void setAnnualFieldRsrcFactory(AnnualFieldRsrcFactory annualFieldRsrcFactory) {
		this.annualFieldRsrcFactory = annualFieldRsrcFactory;
	}
	
	public void setUwContractRsrcFactory(UwContractRsrcFactory uwContractRsrcFactory) {
		this.uwContractRsrcFactory = uwContractRsrcFactory;
	}

	public void setUserSettingRsrcFactory(UserSettingRsrcFactory userSettingRsrcFactory) {
		this.userSettingRsrcFactory = userSettingRsrcFactory;
	}
	
	public void setFieldDao(FieldDao fieldDao) {
		this.fieldDao = fieldDao;
	}
	
	public void setPolicyDao(PolicyDao policyDao) {
		this.policyDao = policyDao;
	}

	public void setUserSettingDao(UserSettingDao userSettingDao) {
		this.userSettingDao = userSettingDao;
	}
	
	@Override
	public UwContractListRsrc getUwContractList(
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
		
		UwContractListRsrc results = null;

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

			results = uwContractRsrcFactory.getUwContractList(
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
	public UwContractRsrc getUwContract(Integer policyId, 
			                                                 Boolean loadLinkedPolicies, 
			                                         		 Boolean loadOtherYearPolicies,
			                                        		 String screenType,
			                                                 FactoryContext factoryContext, 
			                                                 WebAdeAuthentication authentication)
			throws ServiceException, NotFoundException {
		logger.debug("<getUwContract");

		UwContractRsrc result = null;

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

			List<PolicyDto> otherYearPolicyDtos = null;
			if ( Boolean.TRUE.equals(loadOtherYearPolicies) ) {
				if ( ScreenType.INVENTORY.name().equals(screenType) ) {
					otherYearPolicyDtos = policyDao.selectByOtherYearInventory(dto.getContractId(), dto.getCropYear(), numOtherYearPoliciesToLoad);
				} else if ( ScreenType.DOP.name().equals(screenType) ) {
					otherYearPolicyDtos = policyDao.selectByOtherYearDop(dto.getContractId(), dto.getCropYear(), numOtherYearPoliciesToLoad);
				} else if ( ScreenType.VERIFIED.name().equals(screenType) ) {
					otherYearPolicyDtos = policyDao.selectByOtherYearVerified(dto.getContractId(), dto.getCropYear(), numOtherYearPoliciesToLoad);
				} else {
					throw new ServiceException("Invalid value for screenType: " + screenType);
				}
			}

			result = uwContractRsrcFactory.getUwContract(dto, linkedPolicyDtos, otherYearPolicyDtos, loadLinkedPolicies, loadOtherYearPolicies, screenType, factoryContext, authentication);

		} catch (DaoException e) {
			throw new ServiceException("DAO threw an exception", e);
		} catch (TooManyRecordsException e) {
			throw new ServiceException("DAO threw an exception", e);
		}

		logger.debug(">getUwContract");
		return result;
	}

	@Override
	public AnnualFieldListRsrc getAnnualFieldForLegalLandList(
			Integer legalLandId, 
			Integer fieldId, 
			String fieldLocation,
			Integer cropYear,
			FactoryContext context,
			WebAdeAuthentication authentication) throws ServiceException {

		logger.debug("<getAnnualFieldList");
		
		AnnualFieldListRsrc results = null;

		try {
			
			List<FieldDto> dtos = fieldDao.selectForLegalLandOrField(legalLandId, fieldId, fieldLocation, cropYear);
			
			for ( FieldDto fDto : dtos ) {

				List<PolicyDto> policies = policyDao.selectByFieldAndYear(fDto.getFieldId(), fDto.getMaxCropYear());
				fDto.setPolicies(policies);
				
			}
			
			
			results = annualFieldRsrcFactory.getAnnualFieldList(
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

	@Override
	public UserSettingRsrc searchUserSetting(FactoryContext factoryContext, WebAdeAuthentication authentication)
			throws ServiceException, NotFoundException {
		logger.debug("<searchUserSetting");

		UserSettingRsrc result = null;

		String loginUserGuid = null;
		if ( authentication != null && authentication.getUserGuid() != null ) {
			loginUserGuid = authentication.getUserGuid();
		
			try {
				UserSettingDto dto = userSettingDao.getByLoginUserGuid(loginUserGuid);
	
				if (dto == null) {
					result = userSettingRsrcFactory.getDefaultUserSetting(factoryContext, authentication);
				} else {	
					result = userSettingRsrcFactory.getUserSetting(dto, factoryContext, authentication);
				}
	
			} catch (DaoException e) {
				throw new ServiceException("DAO threw an exception", e);
			}
		} else {
			throw new NotFoundException("Did not find user settings");
		}
		
		logger.debug(">searchUserSetting");
		return result;
	}

	@Override
	public UserSettingRsrc getUserSetting(
			String userSettingGuid, 
			FactoryContext factoryContext,
			WebAdeAuthentication authentication) throws ServiceException, NotFoundException {

		logger.debug("<getUserSetting");
		
		UserSettingRsrc result = null;

		try {
			
			UserSettingDto dto = userSettingDao.fetch(userSettingGuid);
			
			if (dto == null) {
				throw new NotFoundException("Did not find the user setting: " + userSettingGuid);
			}

			result = userSettingRsrcFactory.getUserSetting(dto, factoryContext, authentication);

		} catch (DaoException e) {
			throw new ServiceException("DAO threw an exception", e);
		}
		
		logger.debug(">getUserSetting");

		return result;
	}

	@Override
	public UserSettingRsrc createUserSetting(
			UserSettingRsrc userSetting, 
			FactoryContext factoryContext,
			WebAdeAuthentication authentication
			) throws ServiceException, NotFoundException, ValidationFailureException {

		logger.debug("<createUserSetting");

		UserSettingRsrc result = null;

		try {
			
			saveUserSetting(userSetting, authentication);
			
			result = getUserSetting(userSetting.getUserSettingGuid(), factoryContext, authentication);

		} catch (DaoException e) {
			e.printStackTrace();
			throw new ServiceException("DAO threw an exception: " + e.getMessage(), e);
		}

		logger.debug(">createUserSetting");

		return result;
	}
	

	@Override
	public UserSettingRsrc updateUserSetting(
			String userSettingGuid, 
			String optimisticLock, 
			UserSettingRsrc userSetting,
			FactoryContext factoryContext, 
			WebAdeAuthentication authentication
			) throws ServiceException, NotFoundException, ForbiddenException, ConflictException, ValidationFailureException {
		
		logger.debug("<updateUserSetting");

		UserSettingRsrc result = null;

		try {
			List<Message> errors = new ArrayList<Message>();

			if (!errors.isEmpty()) {
				throw new ValidationFailureException(errors);
			}
			
			saveUserSetting(userSetting, authentication);

			result = getUserSetting(userSettingGuid, factoryContext, authentication);

		} catch (DaoException e) {
			throw new ServiceException("DAO threw an exception", e);
		}

		logger.debug(">updateUserSetting");
		
		return result;
		
	}
	
	private void saveUserSetting(
			UserSettingRsrc userSetting, 
			WebAdeAuthentication authentication) throws DaoException {

		logger.debug("<updateUserSetting");
		
		String userId = getUserId(authentication);
		
		UserSettingDto dto = null;
		
		//Always update family and given name from authentication
		userSetting.setFamilyName(authentication.getFamilyName());
		userSetting.setGivenName(authentication.getGivenName());

		if (userSetting.getUserSettingGuid() != null) {
			dto = userSettingDao.fetch(userSetting.getUserSettingGuid());
		}

		if (dto == null) {
			// Insert if it doesn't exist
			insertUserSetting(userSetting, userId);
		} else {
			userSettingRsrcFactory.updateDto(dto, userSetting);

			userSettingDao.update(dto, userId);
		}

		logger.debug(">updateUserSetting");
	}

	private void insertUserSetting(UserSettingRsrc userSetting, String userId) throws DaoException {

		logger.debug("<insertUserSetting");

		UserSettingDto dto = new UserSettingDto();

		userSettingRsrcFactory.updateDto(dto, userSetting);

		userSetting.setUserSettingGuid(null);

		userSettingDao.insert(dto, userId);
		
		userSetting.setUserSettingGuid(dto.getUserSettingGuid());

		logger.debug(">insertUserSetting");

	}



	@Override
	public void deleteUserSetting(
			String userSettingGuid, 
			String optimisticLock,
			WebAdeAuthentication authentication)
			throws ServiceException, NotFoundException, ForbiddenException, ConflictException {
		
			logger.debug("<deleteUserSetting");

			try {
				
				UserSettingDto dto = userSettingDao.fetch(userSettingGuid);

				if (dto == null) {
					throw new NotFoundException("Did not find the user setting: " + userSettingGuid);
				}

				userSettingDao.delete(userSettingGuid);
				
			} catch (DaoException e) {
				throw new ServiceException("DAO threw an exception", e);
			}
			logger.debug(">deleteUserSetting");
		
	}
	
	private String getUserId(WebAdeAuthentication authentication) {
		String userId = "DEFAULT_USERID";

		if (authentication != null) {
			userId = authentication.getUserId();
			authentication.getClientId();
		}

		return userId;
	}
}
