package ca.bc.gov.mal.cirras.underwriting.service.api.v1.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.mal.cirras.underwriting.model.v1.AnnualField;
import ca.bc.gov.mal.cirras.underwriting.model.v1.AnnualFieldList;
import ca.bc.gov.mal.cirras.underwriting.model.v1.UserSetting;
import ca.bc.gov.mal.cirras.underwriting.model.v1.UwContract;
import ca.bc.gov.mal.cirras.underwriting.model.v1.UwContractList;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.FieldDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.PolicyDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.UserSettingDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.FieldDao;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.PolicyDao;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.UserSettingDao;
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
import ca.bc.gov.mal.cirras.underwriting.service.api.v1.CirrasUnderwritingService;
import ca.bc.gov.mal.cirras.underwriting.service.api.v1.model.factory.AnnualFieldFactory;
import ca.bc.gov.mal.cirras.underwriting.service.api.v1.model.factory.UserSettingFactory;
import ca.bc.gov.mal.cirras.underwriting.service.api.v1.model.factory.UwContractFactory;
import ca.bc.gov.mal.cirras.underwriting.service.api.v1.util.UnderwritingServiceHelper;
import ca.bc.gov.mal.cirras.underwriting.service.api.v1.util.InventoryServiceEnums.ScreenType;
import ca.bc.gov.mal.cirras.underwriting.service.api.v1.util.OutOfSync;
import ca.bc.gov.mal.cirras.underwriting.service.api.v1.validation.ModelValidator;


public class CirrasUnderwritingServiceImpl implements CirrasUnderwritingService {

	private static final Logger logger = LoggerFactory.getLogger(CirrasUnderwritingServiceImpl.class);

	private Properties applicationProperties;

	private ModelValidator modelValidator;

	// factories
	private AnnualFieldFactory annualFieldFactory;
	private UwContractFactory uwContractFactory;
	private UserSettingFactory userSettingFactory;
	
	// utils
	private OutOfSync outOfSync;

	// daos
	private FieldDao fieldDao;
	private PolicyDao policyDao;
	private UserSettingDao userSettingDao;

	//utils
	//@Autowired
	private UnderwritingServiceHelper underwritingServiceHelper;
	
	public static final String MaximumResultsProperty = "maximum.results";

	public static final int DefaultMaximumResults = 800;

	// Number of past or future years to search when loading UwContract.otherYearPolicies.
	public static final Integer numOtherYearPoliciesToLoad = 5;
	
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

	public void setUserSettingFactory(UserSettingFactory userSettingFactory) {
		this.userSettingFactory = userSettingFactory;
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
	public UwContract<? extends UwContract<?>> getUwContract(Integer policyId, 
			                                                 Boolean loadLinkedPolicies, 
			                                         		 Boolean loadOtherYearPolicies,
			                                        		 String screenType,
			                                                 FactoryContext factoryContext, 
			                                                 WebAdeAuthentication authentication)
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

			result = uwContractFactory.getUwContract(dto, linkedPolicyDtos, otherYearPolicyDtos, loadLinkedPolicies, loadOtherYearPolicies, screenType, factoryContext, authentication);

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

	@Override
	public UserSetting searchUserSetting(FactoryContext factoryContext, WebAdeAuthentication authentication)
			throws ServiceException, NotFoundException {
		logger.debug("<searchUserSetting");

		UserSetting result = null;

		String loginUserGuid = null;
		if ( authentication != null && authentication.getUserGuid() != null ) {
			loginUserGuid = authentication.getUserGuid();
		
			try {
				UserSettingDto dto = userSettingDao.getByLoginUserGuid(loginUserGuid);
	
				if (dto == null) {
					result = userSettingFactory.getDefaultUserSetting(factoryContext, authentication);
				} else {	
					result = userSettingFactory.getUserSetting(dto, factoryContext, authentication);
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
	public UserSetting getUserSetting(
			String userSettingGuid, 
			FactoryContext factoryContext,
			WebAdeAuthentication authentication) throws ServiceException, NotFoundException {

		logger.debug("<getUserSetting");
		
		UserSetting result = null;

		try {
			
			UserSettingDto dto = userSettingDao.fetch(userSettingGuid);
			
			if (dto == null) {
				throw new NotFoundException("Did not find the user setting: " + userSettingGuid);
			}

			result = userSettingFactory.getUserSetting(dto, factoryContext, authentication);

		} catch (DaoException e) {
			throw new ServiceException("DAO threw an exception", e);
		}
		
		logger.debug(">getUserSetting");

		return result;
	}

	@Override
	public UserSetting createUserSetting(
			UserSetting userSetting, 
			FactoryContext factoryContext,
			WebAdeAuthentication authentication
			) throws ServiceException, NotFoundException, ValidationFailureException {

		logger.debug("<createUserSetting");

		UserSetting result = null;

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
	public UserSetting updateUserSetting(
			String userSettingGuid, 
			String optimisticLock, 
			UserSetting userSetting,
			FactoryContext factoryContext, 
			WebAdeAuthentication authentication
			) throws ServiceException, NotFoundException, ForbiddenException, ConflictException, ValidationFailureException {
		
		logger.debug("<updateUserSetting");

		UserSetting result = null;

		try {
			List<Message> errors = new ArrayList<Message>();
			// errors.addAll(modelValidator.validateInsuranceClaim(insuranceClaim)); // TODO

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
			UserSetting userSetting, 
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
			userSettingFactory.updateDto(dto, userSetting);

			userSettingDao.update(dto, userId);
		}

		logger.debug(">updateUserSetting");
	}

	private void insertUserSetting(UserSetting userSetting, String userId) throws DaoException {

		logger.debug("<insertUserSetting");

		UserSettingDto dto = new UserSettingDto();

		userSettingFactory.updateDto(dto, userSetting);

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
