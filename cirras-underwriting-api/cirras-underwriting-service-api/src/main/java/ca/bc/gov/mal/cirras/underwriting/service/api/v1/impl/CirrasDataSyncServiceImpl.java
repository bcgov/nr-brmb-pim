package ca.bc.gov.mal.cirras.underwriting.services.impl;

import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.mal.cirras.underwriting.data.resources.GrowerRsrc;
import ca.bc.gov.mal.cirras.underwriting.data.resources.PolicyRsrc;
import ca.bc.gov.mal.cirras.underwriting.data.resources.ProductRsrc;
import ca.bc.gov.mal.cirras.underwriting.data.resources.SyncCodeRsrc;
import ca.bc.gov.mal.cirras.underwriting.data.models.CommodityTypeCode;
import ca.bc.gov.mal.cirras.underwriting.data.models.CommodityTypeVarietyXref;
import ca.bc.gov.mal.cirras.underwriting.data.models.Contact;
import ca.bc.gov.mal.cirras.underwriting.data.models.ContactEmail;
import ca.bc.gov.mal.cirras.underwriting.data.models.ContactPhone;
import ca.bc.gov.mal.cirras.underwriting.data.models.Grower;
import ca.bc.gov.mal.cirras.underwriting.data.models.GrowerContact;
import ca.bc.gov.mal.cirras.underwriting.data.models.Policy;
import ca.bc.gov.mal.cirras.underwriting.data.models.Product;
import ca.bc.gov.mal.cirras.underwriting.data.models.SyncCode;
import ca.bc.gov.mal.cirras.underwriting.data.models.SyncCommodityVariety;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.GrowerDao;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.OfficeDao;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.PolicyDao;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.PolicyStatusCodeDao;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.ProductDao;
import ca.bc.gov.mal.cirras.underwriting.data.entities.GrowerDto;
import ca.bc.gov.mal.cirras.underwriting.data.entities.OfficeDto;
import ca.bc.gov.mal.cirras.underwriting.data.entities.PolicyDto;
import ca.bc.gov.mal.cirras.underwriting.data.entities.PolicyStatusCodeDto;
import ca.bc.gov.mal.cirras.underwriting.data.entities.ProductDto;
import ca.bc.gov.mal.cirras.underwriting.data.entities.CommodityTypeCodeDto;
import ca.bc.gov.mal.cirras.underwriting.data.entities.CommodityTypeVarietyXrefDto;
import ca.bc.gov.mal.cirras.underwriting.data.entities.ContactDto;
import ca.bc.gov.mal.cirras.underwriting.data.entities.ContactEmailDto;
import ca.bc.gov.mal.cirras.underwriting.data.entities.ContactPhoneDto;
import ca.bc.gov.mal.cirras.underwriting.data.entities.CropCommodityDto;
import ca.bc.gov.mal.cirras.underwriting.data.entities.CropVarietyDto;
import ca.bc.gov.mal.cirras.underwriting.data.entities.GrowerContactDto;
import ca.bc.gov.mal.cirras.underwriting.services.CirrasDataSyncService;
import ca.bc.gov.mal.cirras.underwriting.services.model.factory.CirrasDataSyncFactory;
import ca.bc.gov.mal.cirras.underwriting.services.validation.ModelValidator;

import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.service.api.NotFoundException;
import ca.bc.gov.nrs.wfone.common.service.api.ServiceException;
import ca.bc.gov.nrs.wfone.common.service.api.model.factory.FactoryContext;
import ca.bc.gov.nrs.wfone.common.webade.authentication.WebAdeAuthentication;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.CommodityTypeCodeDao;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.CommodityTypeVarietyXrefDao;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.ContactDao;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.ContactEmailDao;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.ContactPhoneDao;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.CropCommodityDao;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.CropVarietyDao;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.GrowerContactDao;
import ca.bc.gov.mal.cirras.policies.model.v1.CodeTableTypes;
import ca.bc.gov.mal.cirras.underwriting.data.resources.PoliciesSyncEventTypes;

public class CirrasDataSyncServiceImpl implements CirrasDataSyncService {

	private static final Logger logger = LoggerFactory.getLogger(CirrasDataSyncServiceImpl.class);

	private Properties applicationProperties;

	private ModelValidator modelValidator;

	// factories
	private CirrasDataSyncFactory cirrasDataSyncFactory;

	// daos
	private PolicyStatusCodeDao policyStatusCodeDao;
	private GrowerDao growerDao;
	private PolicyDao policyDao;
	private ProductDao productDao;
	private CropCommodityDao cropCommodityDao;
	private CropVarietyDao cropVarietyDao;
	private OfficeDao officeDao;
	private ContactDao contactDao;
	private GrowerContactDao growerContactDao;
	private ContactEmailDao contactEmailDao;
	private ContactPhoneDao contactPhoneDao;
	private CommodityTypeCodeDao commodityTypeCodeDao;
	private CommodityTypeVarietyXrefDao commodityTypeVarietyXrefDao;

	// utils
	//private CirrasServiceHelper cirrasServiceHelper;

	public static final String MaximumResultsProperty = "maximum.results";

	public static final int DefaultMaximumResults = 800;

	public void setApplicationProperties(Properties applicationProperties) {
		this.applicationProperties = applicationProperties;
	}

	public void setModelValidator(ModelValidator modelValidator) {
		this.modelValidator = modelValidator;
	}

	public void setCirrasDataSyncFactory(CirrasDataSyncFactory cirrasDataSyncFactory) {
		this.cirrasDataSyncFactory = cirrasDataSyncFactory;
	}

//	public void setCirrasServiceHelper(CirrasServiceHelper cirrasServiceHelper) {
//		this.cirrasServiceHelper = cirrasServiceHelper;
//	}

	public void setPolicyStatusCodeDao(PolicyStatusCodeDao policyStatusCodeDao) {
		this.policyStatusCodeDao = policyStatusCodeDao;
	}

	public void setGrowerDao(GrowerDao growerDao) {
		this.growerDao = growerDao;
	}

	public void setPolicyDao(PolicyDao policyDao) {
		this.policyDao = policyDao;
	}

	public void setProductDao(ProductDao productDao) {
		this.productDao = productDao;
	}
	
	public void setCropCommodityDao(CropCommodityDao cropCommodityDao) {
		this.cropCommodityDao = cropCommodityDao;
	}

	public void setCropVarietyDao(CropVarietyDao cropVarietyDao) {
		this.cropVarietyDao = cropVarietyDao;
	}

	public void setOfficeDao(OfficeDao officeDao) {
		this.officeDao = officeDao;
	}

	public void setContactDao(ContactDao contactDao) {
		this.contactDao = contactDao;
	}

	public void setGrowerContactDao(GrowerContactDao growerContactDao) {
		this.growerContactDao = growerContactDao;
	}

	public void setContactEmailDao(ContactEmailDao contactEmailDao) {
		this.contactEmailDao = contactEmailDao;
	}

	public void setContactPhoneDao(ContactPhoneDao contactPhoneDao) {
		this.contactPhoneDao = contactPhoneDao;
	}

	public void setCommodityTypeCodeDao(CommodityTypeCodeDao commodityTypeCodeDao) {
		this.commodityTypeCodeDao = commodityTypeCodeDao;
	}

	public void setCommodityTypeVarietyXrefDao(CommodityTypeVarietyXrefDao commodityTypeVarietyXrefDao) {
		this.commodityTypeVarietyXrefDao = commodityTypeVarietyXrefDao;
	}

	//
	// The "proof of concept" REST service doesn't have any security
	//
	private String getUserId(WebAdeAuthentication authentication) {
		String userId = "DEFAULT_USERID";

		if (authentication != null) {
			userId = authentication.getUserId();
		}

		return userId;
	}



	////////////////////////////////////////////////////////////////////
	// Generic Code Tables
	////////////////////////////////////////////////////////////////////
	@Override
	public SyncCode getSyncCode(String codeTableType, String uniqueKey, FactoryContext factoryContext,
			WebAdeAuthentication authentication) throws ServiceException, DaoException {

		logger.debug("<getSyncCode");

		SyncCodeRsrc resource = new SyncCodeRsrc();

		// Determine the type of code that is synchronized
		switch (codeTableType) {
		case CodeTableTypes.PolicyStatusCode:
			PolicyStatusCodeDto policyStatusCodeDto = policyStatusCodeDao.fetch(uniqueKey);
			if (policyStatusCodeDto != null) {
				resource.setUniqueKeyString(policyStatusCodeDto.getPolicyStatusCode());
				return resource;
			}
			break;
		case CodeTableTypes.Offices:
			OfficeDto officeDto = officeDao.fetch(toInteger(uniqueKey));
			if (officeDto != null) {
				resource.setUniqueKeyInteger(officeDto.getOfficeId());
				return resource;
			}
			break;
		default:
			throw new ServiceException("Underwriting Service threw an exception. A codeTableType that is not supported or no codeTableType has been passed to the service (getSyncCode) : " + codeTableType);
		}

		logger.debug(">getSyncCode");

		return null;

	}

	@Override
	public void synchronizeCode(SyncCodeRsrc resource, FactoryContext factoryContext,
			WebAdeAuthentication authentication) throws ServiceException, DaoException {

		logger.debug("<synchronizeCode");

		// Determine the type of code that is synchronized
		switch (resource.getCodeTableType()) {
		case CodeTableTypes.PolicyStatusCode:
			synchronizePolicyStatusCode(resource, factoryContext, authentication);
			break;
		case CodeTableTypes.Offices:
			synchronizeOffice(resource, factoryContext, authentication);
			break;
		default:
			logger.info("Ignoring codeTableType as it's not supported by the underwriting api: " + resource.getCodeTableType());
			break;
		}

		logger.debug(">synchronizeCode");

	}

	private void synchronizePolicyStatusCode(SyncCodeRsrc resource, FactoryContext factoryContext,
			WebAdeAuthentication authentication) throws DaoException {

		logger.debug("<synchronizePolicyStatusCode");

		if (resource.getTransactionType().equalsIgnoreCase(PoliciesSyncEventTypes.CodeDeleted)) {
			// DELETE
			inactivatePolicyStatusCode(resource, authentication);

		} else if (resource.getTransactionType().equalsIgnoreCase(PoliciesSyncEventTypes.CodeCreated)
				|| resource.getTransactionType().equalsIgnoreCase(PoliciesSyncEventTypes.CodeUpdated)) {
			// INSERT OR UPDATE
			
			//Check if record already exist and call the correct method
			PolicyStatusCodeDto dto = policyStatusCodeDao.fetch(resource.getUniqueKeyString());

			
			if (dto == null) {
				createPolicyStatusCode(resource, factoryContext, authentication);
			} else {
				updatePolicyStatusCode(resource, dto, factoryContext, authentication);
			}
		}

		logger.debug(">synchronizePolicyStatusCode");

	}

	private void updatePolicyStatusCode(SyncCodeRsrc resource, PolicyStatusCodeDto dto, FactoryContext factoryContext,
			WebAdeAuthentication authentication) {

		logger.debug("<updatePolicyStatusCode");

		try {

			String userId = getUserId(authentication);

			cirrasDataSyncFactory.updatePolicyStatusCode(dto, resource);
			policyStatusCodeDao.update(dto, userId);

		} catch (DaoException e) {
			e.printStackTrace();
			throw new ServiceException("DAO threw an exception: " + e.getMessage(), e);
		}

		logger.debug(">updatePolicyStatusCode");

	}

	private void createPolicyStatusCode(SyncCodeRsrc resource, FactoryContext factoryContext,
			WebAdeAuthentication authentication) {

		logger.debug("<createPolicyStatusCode");

		try {

			String userId = getUserId(authentication);

			PolicyStatusCodeDto dto = cirrasDataSyncFactory.createPolicyStatusCode(resource);
			policyStatusCodeDao.insert(dto, userId);

		} catch (DaoException e) {
			e.printStackTrace();
			throw new ServiceException("DAO threw an exception: " + e.getMessage(), e);
		}

		logger.debug(">createPolicyStatusCode");

	}

	private void inactivatePolicyStatusCode(SyncCodeRsrc resource, WebAdeAuthentication authentication) throws DaoException {

		logger.debug("<inactivatePerilCode");

		String userId = getUserId(authentication);

		PolicyStatusCodeDto dto = policyStatusCodeDao.fetch(resource.getUniqueKeyString());

		if (dto != null) {
			cirrasDataSyncFactory.updatePolicyStatusCodeExpiryDate(dto, resource.getDataSyncTransDate());
			policyStatusCodeDao.update(dto, userId);
		}

		logger.debug(">inactivatePerilCode");

	}

	private void synchronizeOffice(SyncCodeRsrc resource, FactoryContext factoryContext,
			WebAdeAuthentication authentication) throws DaoException {

		logger.debug("<synchronizeOffice");

		//Check if record already exist and call the correct method
		OfficeDto dto = officeDao.fetch(resource.getUniqueKeyInteger());

		if (resource.getTransactionType().equalsIgnoreCase(PoliciesSyncEventTypes.CodeDeleted)) {
			// DELETE
			if (dto != null) {
				officeDao.delete(resource.getUniqueKeyInteger());
			}

		} else if (resource.getTransactionType().equalsIgnoreCase(PoliciesSyncEventTypes.CodeCreated)
				|| resource.getTransactionType().equalsIgnoreCase(PoliciesSyncEventTypes.CodeUpdated)) {
			// INSERT OR UPDATE
			
			if (dto == null) {
				createOffice(resource, factoryContext, authentication);
			} else {
				updateOffice(resource, dto, factoryContext, authentication);
			}
		}

		logger.debug(">synchronizeOffice");

	}

	private void updateOffice(SyncCodeRsrc resource, OfficeDto dto, FactoryContext factoryContext,
			WebAdeAuthentication authentication) {

		logger.debug("<updateOffice");

		try {

			String userId = getUserId(authentication);

			cirrasDataSyncFactory.createOffice(dto, resource);
			officeDao.update(dto, userId);

		} catch (DaoException e) {
			e.printStackTrace();
			throw new ServiceException("DAO threw an exception: " + e.getMessage(), e);
		}

		logger.debug(">updateOffice");

	}

	private void createOffice(SyncCodeRsrc resource, FactoryContext factoryContext,
			WebAdeAuthentication authentication) {

		logger.debug("<createOffice");

		try {

			String userId = getUserId(authentication);
			OfficeDto dto = new OfficeDto();

			cirrasDataSyncFactory.createOffice(dto, resource);
			officeDao.insert(dto, userId);

		} catch (DaoException e) {
			e.printStackTrace();
			throw new ServiceException("DAO threw an exception: " + e.getMessage(), e);
		}

		logger.debug(">createOffice");

	}



	// This is not acutally used if a crop gets deleted in CIRRAS at the moment.
	@Override
	public void deleteSyncCode(String codeTableType, String uniqueKey, FactoryContext factoryContext,
			WebAdeAuthentication authentication) throws ServiceException, DaoException {

		logger.debug("<deleteSyncCode");

		// Determine the type of code that is synchronized
		switch (codeTableType) {
		case CodeTableTypes.PolicyStatusCode:
			policyStatusCodeDao.delete(uniqueKey);
			break;
		case CodeTableTypes.Offices:
			officeDao.delete(toInteger(uniqueKey));
			break;
		default:
			throw new ServiceException("Underwriting Service threw an exception. A codeTableType that is not supported or no codeTableType has been passed to the service (deleteSyncCode) : " + codeTableType);
		}

		logger.debug(">deleteSyncCode");

	}

	@Override
	public Grower getGrower(Integer growerId, FactoryContext factoryContext, WebAdeAuthentication authentication)
			throws ServiceException, NotFoundException, DaoException {

		logger.debug("<getGrower");

		Grower result = null;

		try {
			GrowerDto dto = growerDao.fetch(growerId);

			if (dto != null) {
				result = cirrasDataSyncFactory.getGrower(dto);
			}
			else {
				//This method is only used for testing. Return null instead of an error
				//throw new NotFoundException("Did not find grower record: growerId: " + growerId);
			}

		} catch (DaoException e) {
			throw new ServiceException("DAO threw an exception", e);
		}

		logger.debug(">getGrower");
		return result;
	}

	@Override
	public void synchronizeGrower(GrowerRsrc resource, FactoryContext factoryContext,
			WebAdeAuthentication authentication) throws ServiceException, NotFoundException, DaoException {

		logger.debug("<synchronizeGrower");

		if (resource.getTransactionType().equalsIgnoreCase(PoliciesSyncEventTypes.GrowerDeleted)) {
			// DELETE
			deleteGrower(resource.getGrowerId(), factoryContext, authentication);
		
		} else if (resource.getTransactionType().equalsIgnoreCase(PoliciesSyncEventTypes.GrowerCreated)
				|| resource.getTransactionType().equalsIgnoreCase(PoliciesSyncEventTypes.GrowerUpdated)) {
			// INSERT OR UPDATE
			
			//Check if record already exist and call the correct method
			GrowerDto dto = growerDao.fetch(resource.getGrowerId());

			if (dto == null) {
				createGrower(resource, factoryContext, authentication);
			} else {
				updateGrower(resource, dto, factoryContext, authentication);
			}
		}

		logger.debug(">synchronizeGrower");

	}

	private void updateGrower(GrowerRsrc resource, GrowerDto dto, 
			FactoryContext factoryContext, WebAdeAuthentication authentication) {

		logger.debug("<updateGrower");

		try {

			String userId = getUserId(authentication);

			cirrasDataSyncFactory.updateGrower(dto, resource);
			growerDao.update(dto, userId);

		} catch (DaoException e) {
			e.printStackTrace();
			throw new ServiceException("DAO threw an exception: " + e.getMessage(), e);
		}

		logger.debug(">updateGrower");

	}

	private void createGrower(GrowerRsrc resource, FactoryContext factoryContext,
			WebAdeAuthentication authentication) {

		logger.debug("<createGrower");

		try {

			String userId = getUserId(authentication);
			
			GrowerDto dto = new GrowerDto();

			cirrasDataSyncFactory.updateGrower(dto, resource);
			growerDao.insert(dto, userId);

		} catch (DaoException e) {
			e.printStackTrace();
			throw new ServiceException("DAO threw an exception: " + e.getMessage(), e);
		}

		logger.debug(">createGrower");

	}

	@Override
	public void deleteGrower(Integer growerId,
			FactoryContext factoryContext, WebAdeAuthentication authentication)
			throws ServiceException, NotFoundException, DaoException {
		logger.debug("<deleteGrower");

		growerDao.delete(growerId);

		logger.debug(">deleteGrower");

	}
	
	@Override
	public Policy getPolicy(Integer policyId, FactoryContext factoryContext, WebAdeAuthentication authentication)
			throws ServiceException, NotFoundException, DaoException {

		logger.debug("<getPolicy");

		Policy result = null;

		try {
			PolicyDto dto = policyDao.fetch(policyId);

			if (dto != null) {
				result = cirrasDataSyncFactory.getPolicy(dto);
			}
			else {
				//This method is only used for testing. Return null instead of an error
				//throw new NotFoundException("Did not find grower record: policyId: " + policyId);
			}

		} catch (DaoException e) {
			throw new ServiceException("DAO threw an exception", e);
		}

		logger.debug(">getPolicy");
		return result;
	}

	@Override
	public void synchronizePolicy(PolicyRsrc resource, FactoryContext factoryContext,
			WebAdeAuthentication authentication) throws ServiceException, NotFoundException, DaoException {

		logger.debug("<synchronizePolicy");

		if (resource.getTransactionType().equalsIgnoreCase(PoliciesSyncEventTypes.PolicyDeleted)) {
			// DELETE
			deletePolicy(resource.getPolicyId(), factoryContext, authentication);
		
		} else if (resource.getTransactionType().equalsIgnoreCase(PoliciesSyncEventTypes.PolicyCreated)
				|| resource.getTransactionType().equalsIgnoreCase(PoliciesSyncEventTypes.PolicyUpdated)) {
			// INSERT OR UPDATE
			
			//Check if record already exist and call the correct method
			PolicyDto dto = policyDao.fetch(resource.getPolicyId());

			if (dto == null) {
				createPolicy(resource, factoryContext, authentication);
			} else {
				updatePolicy(resource, dto, factoryContext, authentication);
			}
		}

		logger.debug(">synchronizePolicy");

	}

	private void updatePolicy(PolicyRsrc resource, PolicyDto dto, 
			FactoryContext factoryContext, WebAdeAuthentication authentication) {

		logger.debug("<updatePolicy");

		try {

			String userId = getUserId(authentication);

			cirrasDataSyncFactory.updatePolicy(dto, resource);
			policyDao.update(dto, userId);

		} catch (DaoException e) {
			e.printStackTrace();
			throw new ServiceException("DAO threw an exception: " + e.getMessage(), e);
		}

		logger.debug(">updatePolicy");

	}

	private void createPolicy(PolicyRsrc resource, FactoryContext factoryContext,
			WebAdeAuthentication authentication) {

		logger.debug("<createPolicy");

		try {

			String userId = getUserId(authentication);
			
			PolicyDto dto = new PolicyDto();

			cirrasDataSyncFactory.updatePolicy(dto, resource);
			policyDao.insert(dto, userId);

		} catch (DaoException e) {
			e.printStackTrace();
			throw new ServiceException("DAO threw an exception: " + e.getMessage(), e);
		}

		logger.debug(">createPolicy");

	}

	@Override
	public void deletePolicy(Integer policyId,
			FactoryContext factoryContext, WebAdeAuthentication authentication)
			throws ServiceException, NotFoundException, DaoException {
		logger.debug("<deletePolicy");

		policyDao.delete(policyId);

		logger.debug(">deletePolicy");

	}

	@Override
	public Product getProduct(Integer productId, FactoryContext factoryContext, WebAdeAuthentication authentication)
			throws ServiceException, NotFoundException, DaoException {

		logger.debug("<getProduct");

		Product result = null;

		try {
			ProductDto dto = productDao.fetch(productId);

			if (dto != null) {
				result = cirrasDataSyncFactory.getProduct(dto);
			}
			else {
				throw new NotFoundException("Did not find product record: productId: " + productId);
			}

		} catch (DaoException e) {
			throw new ServiceException("DAO threw an exception", e);
		}

		logger.debug(">getProduct");
		return result;
	}

	@Override
	public void synchronizeProduct(ProductRsrc resource, FactoryContext factoryContext,
			WebAdeAuthentication authentication) throws ServiceException, NotFoundException, DaoException {

		logger.debug("<synchronizeProduct");

		if (resource.getTransactionType().equalsIgnoreCase(PoliciesSyncEventTypes.ProductDeleted)) {
			// DELETE
			deleteProduct(resource.getProductId(), factoryContext, authentication);
		
		} else if (resource.getTransactionType().equalsIgnoreCase(PoliciesSyncEventTypes.ProductCreated)
				|| resource.getTransactionType().equalsIgnoreCase(PoliciesSyncEventTypes.ProductUpdated)) {
			// INSERT OR UPDATE
			
			//Check if record already exist and call the correct method
			ProductDto dto = productDao.fetch(resource.getProductId());

			if (dto == null) {
				createProduct(resource, factoryContext, authentication);
			} else {
				updateProduct(resource, dto, factoryContext, authentication);
			}
		}

		logger.debug(">synchronizeProduct");

	}

	private void updateProduct(ProductRsrc resource, ProductDto dto, 
			FactoryContext factoryContext, WebAdeAuthentication authentication) {

		logger.debug("<updateProduct");

		try {

			String userId = getUserId(authentication);

			cirrasDataSyncFactory.updateProduct(dto, resource);
			productDao.update(dto, userId);

		} catch (DaoException e) {
			e.printStackTrace();
			throw new ServiceException("DAO threw an exception: " + e.getMessage(), e);
		}

		logger.debug(">updateProduct");

	}

	private void createProduct(ProductRsrc resource, FactoryContext factoryContext,
			WebAdeAuthentication authentication) {

		logger.debug("<createProduct");

		try {

			String userId = getUserId(authentication);
			
			ProductDto dto = new ProductDto();

			cirrasDataSyncFactory.updateProduct(dto, resource);
			productDao.insert(dto, userId);

		} catch (DaoException e) {
			e.printStackTrace();
			throw new ServiceException("DAO threw an exception: " + e.getMessage(), e);
		}

		logger.debug(">createProduct");

	}

	@Override
	public void deleteProduct(Integer productId,
			FactoryContext factoryContext, WebAdeAuthentication authentication)
			throws ServiceException, NotFoundException, DaoException {
		logger.debug("<deleteProduct");

		productDao.delete(productId);

		logger.debug(">deleteProduct");

	}
	
	
	////////////////////////////////////////////////////////////////////
	// Commodity Variety
	////////////////////////////////////////////////////////////////////
	
	@Override
	public void synchronizeCommodityVariety(SyncCommodityVariety model, FactoryContext factoryContext,
			WebAdeAuthentication authentication) throws ServiceException, NotFoundException, DaoException {

		logger.debug("<synchronizeCommodityVariety");

		if (model.getTransactionType().equalsIgnoreCase(PoliciesSyncEventTypes.CommodityVarietyDeleted)) {
			// DELETE
			inactivateSyncCommodityVariety(model, authentication);

		} else if (model.getTransactionType().equalsIgnoreCase(PoliciesSyncEventTypes.CommodityVarietyCreated)
				|| model.getTransactionType().equalsIgnoreCase(PoliciesSyncEventTypes.CommodityVarietyUpdated)) {
			// INSERT OR UPDATE
			
			// Determine if it's commodity or variety update
			if (model != null && model.getParentCropId() == null) {
				// Commodity
				//Check if record already exist and call the correct method
				CropCommodityDto dto = cropCommodityDao.fetch(model.getCropId());

				if (dto == null) {
					createCropCommodity(model, factoryContext, authentication);
				} else {
					updateCropCommodity(model, dto, factoryContext, authentication);
				}
			} else if (model != null && model.getParentCropId() != null) {
				// Variety has the parent id set
				
				//Check if record already exist and call the correct method
				CropVarietyDto dto = cropVarietyDao.fetch(model.getCropId());

				if (dto == null) {
					createCropVariety(model, factoryContext, authentication);
				} else {
					updateCropVariety(model, dto, factoryContext, authentication);
				}
			}
		}

		logger.debug(">synchronizeCommodityVariety");

	}

	private void createCropCommodity(SyncCommodityVariety model, FactoryContext factoryContext,
			WebAdeAuthentication webAdeAuthentication) {

		logger.debug("<createCropCommodity");

		try {

			String userId = getUserId(webAdeAuthentication);

			CropCommodityDto dto = cirrasDataSyncFactory.createCropCommodity(model);
			cropCommodityDao.insert(dto, userId);

		} catch (DaoException e) {
			e.printStackTrace();
			throw new ServiceException("DAO threw an exception: " + e.getMessage(), e);
		}

		logger.debug(">createCropCommodity");

	}

	private void updateCropCommodity(SyncCommodityVariety model, CropCommodityDto dto, 
			FactoryContext factoryContext, WebAdeAuthentication webAdeAuthentication) {

		logger.debug("<updateCropCommodity");

		try {

			String userId = getUserId(webAdeAuthentication);

			cirrasDataSyncFactory.updateCropCommodity(dto, model);
			cropCommodityDao.update(dto, userId);

		} catch (DaoException e) {
			e.printStackTrace();
			throw new ServiceException("DAO threw an exception: " + e.getMessage(), e);
		}

		logger.debug(">updateCropCommodity");

	}

	private void createCropVariety(SyncCommodityVariety model, FactoryContext factoryContext,
			WebAdeAuthentication webAdeAuthentication) {

		logger.debug("<createCropVariety");

		try {

			String userId = getUserId(webAdeAuthentication);

			CropVarietyDto dto = cirrasDataSyncFactory.createCropVariety(model);
			cropVarietyDao.insert(dto, userId);

		} catch (DaoException e) {
			e.printStackTrace();
			throw new ServiceException("DAO threw an exception: " + e.getMessage(), e);
		}

		logger.debug(">createCropVariety");

	}

	private void updateCropVariety(SyncCommodityVariety model, CropVarietyDto dto, 
			FactoryContext factoryContext, WebAdeAuthentication webAdeAuthentication) {

		logger.debug("<updateCropVariety");

		try {

			String userId = getUserId(webAdeAuthentication);

			cirrasDataSyncFactory.updateCropVariety(dto, model);
			cropVarietyDao.update(dto, userId);

		} catch (DaoException e) {
			e.printStackTrace();
			throw new ServiceException("DAO threw an exception: " + e.getMessage(), e);
		}

		logger.debug(">updateCropVariety");

	}

	// The crop record is not deleted but the expiry date is set to the current
	// date.
	// It's possible that the record doesn't exist in the calculator.
	private void inactivateSyncCommodityVariety(SyncCommodityVariety model, WebAdeAuthentication authentication)
			throws ServiceException, NotFoundException, DaoException {

		logger.debug("<inactivateSyncCommodityVariety");

		String userId = getUserId(authentication);

		CropVarietyDto dtoVariety = cropVarietyDao.fetch(model.getCropId());

		if (dtoVariety != null) {
			cirrasDataSyncFactory.updateCropVarietyExpiryDate(dtoVariety, model.getDataSyncTransDate());
			cropVarietyDao.update(dtoVariety, userId);

		} else {
			CropCommodityDto dtoCommodity = cropCommodityDao.fetch(model.getCropId());
			if (dtoCommodity != null) {

				cirrasDataSyncFactory.updateCropCommodityExpiryDate(dtoCommodity, model.getDataSyncTransDate());
				cropCommodityDao.update(dtoCommodity, userId);
			}
		}

		logger.debug(">inactivateSyncCommodityVariety");

	}

	// This is not acutally used if a crop gets deleted in CIRRAS.
	@Override
	public void deleteSyncCommodityVariety(Integer crptId, FactoryContext factoryContext,
			WebAdeAuthentication authentication) throws ServiceException, NotFoundException, DaoException {

		logger.debug("<deleteSyncCommodityVariety");

		SyncCommodityVariety model = getSyncCommodityVariety(crptId, factoryContext, authentication);

		// Determine if it's commodity or variety update
		if (model != null && model.getParentCropId() == null) {
			// Commodity
			cropCommodityDao.delete(model.getCropId());
		} else if (model != null && model.getParentCropId() != null) {
			// Variety has parent id set
			cropVarietyDao.delete(model.getCropId());
		}

		logger.debug(">deleteSyncCommodityVariety");

	}

	// This method is used to determine if the crop is a commodity or a variety and
	// return the cropId and the parentId (if it's a variety)
	@Override
	public SyncCommodityVariety getSyncCommodityVariety(Integer crptId, FactoryContext factoryContext,
			WebAdeAuthentication authentication) throws ServiceException, NotFoundException {

		logger.debug("<getSyncCommodityVariety");

		SyncCommodityVariety model = null;

		if (crptId != null) {
			try {

				CropVarietyDto dtoVariety = cropVarietyDao.fetch(crptId);

				if (dtoVariety != null) {
					model = cirrasDataSyncFactory.getSyncCommodityVarietyFromVariety(dtoVariety);

				} else {
					CropCommodityDto dtoCommodity = cropCommodityDao.fetch(crptId);
					if (dtoCommodity != null) {

						model = cirrasDataSyncFactory.getSyncCommodityVarietyFromCropCommodity(dtoCommodity);
					} else {
						// No record found
						throw new NotFoundException("Did not find a variety nor a commodity with id: " + crptId);
					}
				}

			} catch (DaoException e) {
				e.printStackTrace();
				throw new ServiceException("DAO threw an exception: " + e.getMessage(), e);
			}
		}

		logger.debug(">getSyncCommodityVariety");

		return model;

	}
	

	/////////////////////////////////////////////////
	// CONTACT
	//////////////////////////////////////////////////
	@Override
	public Contact getContact(Integer contactId, FactoryContext factoryContext, WebAdeAuthentication authentication)
			throws ServiceException, NotFoundException {

		logger.debug("<getContact");

		Contact result = null;

		try {
			ContactDto dto = contactDao.fetch(contactId);

			if (dto != null) {
				result = cirrasDataSyncFactory.getContact(dto);
			}
			else {
				//This method is only used for testing. Return null instead of an error
			}

		} catch (DaoException e) {
			throw new ServiceException("DAO threw an exception", e);
		}

		logger.debug(">getContact");
		return result;
	}

	@Override
	public void synchronizeContact(Contact model, FactoryContext factoryContext,
			WebAdeAuthentication authentication) throws ServiceException, NotFoundException, DaoException {

		logger.debug("<synchronizeContact");

		if (model.getTransactionType().equalsIgnoreCase(PoliciesSyncEventTypes.ContactsDeleted)) {
			// DELETE
			deleteContact(model.getContactId(), factoryContext, authentication);
		
		} else if (model.getTransactionType().equalsIgnoreCase(PoliciesSyncEventTypes.ContactsCreated)
				|| model.getTransactionType().equalsIgnoreCase(PoliciesSyncEventTypes.ContactsUpdated)) {
			// INSERT OR UPDATE
			
			//Check if record already exist and call the correct method
			ContactDto dto = contactDao.fetch(model.getContactId());

			if (dto == null) {
				createContact(model, factoryContext, authentication);
			} else {
				updateContact(model, dto, factoryContext, authentication);
			}
		}

		logger.debug(">synchronizeContact");

	}

	private void updateContact(Contact model, ContactDto dto, 
			FactoryContext factoryContext, WebAdeAuthentication authentication) {

		logger.debug("<updateContact");

		try {

			String userId = getUserId(authentication);

			cirrasDataSyncFactory.updateContact(dto, model);
			contactDao.update(dto, userId);

		} catch (DaoException e) {
			e.printStackTrace();
			throw new ServiceException("DAO threw an exception: " + e.getMessage(), e);
		}

		logger.debug(">updateContact");

	}

	private void createContact(Contact model, FactoryContext factoryContext,
			WebAdeAuthentication authentication) {

		logger.debug("<createContact");

		try {

			String userId = getUserId(authentication);
			
			ContactDto dto = new ContactDto();

			cirrasDataSyncFactory.updateContact(dto, model);
			contactDao.insert(dto, userId);

		} catch (DaoException e) {
			e.printStackTrace();
			throw new ServiceException("DAO threw an exception: " + e.getMessage(), e);
		}

		logger.debug(">createContact");

	}

	@Override
	public void deleteContact(Integer contactId,
			FactoryContext factoryContext, WebAdeAuthentication authentication)
			throws ServiceException, NotFoundException, DaoException {
		logger.debug("<deleteContact");

		contactDao.delete(contactId);

		logger.debug(">deleteContact");

	}

	/////////////////////////////////////////////////
	// GROWER CONTACT
	//////////////////////////////////////////////////
	@Override
	public GrowerContact getGrowerContact(Integer growerContactId, FactoryContext factoryContext, WebAdeAuthentication authentication)
			throws ServiceException, NotFoundException {

		logger.debug("<getGrowerContact");

		GrowerContact result = null;

		try {
			GrowerContactDto dto = growerContactDao.fetch(growerContactId);

			if (dto != null) {
				result = cirrasDataSyncFactory.getGrowerContact(dto);
			}
			else {
				//This method is only used for testing. Return null instead of an error
			}

		} catch (DaoException e) {
			throw new ServiceException("DAO threw an exception", e);
		}

		logger.debug(">getGrowerContact");
		return result;
	}

	@Override
	public void synchronizeGrowerContact(GrowerContact model, FactoryContext factoryContext,
			WebAdeAuthentication authentication) throws ServiceException, NotFoundException, DaoException {

		logger.debug("<synchronizeGrowerContact");

		if (model.getTransactionType().equalsIgnoreCase(PoliciesSyncEventTypes.InsuredGrowerContactsDeleted)) {
			// DELETE
			deleteGrowerContact(model.getGrowerContactId(), factoryContext, authentication);
		
		} else if (model.getTransactionType().equalsIgnoreCase(PoliciesSyncEventTypes.InsuredGrowerContactsCreated)
				|| model.getTransactionType().equalsIgnoreCase(PoliciesSyncEventTypes.InsuredGrowerContactsUpdated)) {

			// INSERT OR UPDATE
			
			//Check if record already exist and call the correct method
			GrowerContactDto dto = growerContactDao.fetch(model.getGrowerContactId());

			if (dto == null) {
				createGrowerContact(model, factoryContext, authentication);
			} else {
				updateGrowerContact(model, dto, factoryContext, authentication);
			}
		}

		logger.debug(">synchronizeGrowerContact");

	}

	private void updateGrowerContact(GrowerContact model, GrowerContactDto dto, 
			FactoryContext factoryContext, WebAdeAuthentication authentication) {

		logger.debug("<updateGrowerContact");

		try {

			String userId = getUserId(authentication);

			cirrasDataSyncFactory.updateGrowerContact(dto, model);
			growerContactDao.update(dto, userId);

		} catch (DaoException e) {
			e.printStackTrace();
			throw new ServiceException("DAO threw an exception: " + e.getMessage(), e);
		}

		logger.debug(">updateGrowerContact");

	}

	private void createGrowerContact(GrowerContact model, FactoryContext factoryContext,
			WebAdeAuthentication authentication) {

		logger.debug("<createGrowerContact");

		try {

			String userId = getUserId(authentication);
			
			GrowerContactDto dto = new GrowerContactDto();

			cirrasDataSyncFactory.updateGrowerContact(dto, model);
			growerContactDao.insert(dto, userId);

		} catch (DaoException e) {
			e.printStackTrace();
			throw new ServiceException("DAO threw an exception: " + e.getMessage(), e);
		}

		logger.debug(">createGrowerContact");

	}

	@Override
	public void deleteGrowerContact(Integer growerContactId,
			FactoryContext factoryContext, WebAdeAuthentication authentication)
			throws ServiceException, NotFoundException, DaoException {
		logger.debug("<deleteGrowerContact");

		growerContactDao.delete(growerContactId);

		logger.debug(">deleteGrowerContact");

	}

	/////////////////////////////////////////////////
	// CONTACT EMAIL
	//////////////////////////////////////////////////
	@Override
	public ContactEmail getContactEmail(Integer contactEmailId, FactoryContext factoryContext, WebAdeAuthentication authentication)
			throws ServiceException, NotFoundException {

		logger.debug("<getContactEmail");

		ContactEmail result = null;

		try {
			ContactEmailDto dto = contactEmailDao.fetch(contactEmailId);

			if (dto != null) {
				result = cirrasDataSyncFactory.getContactEmail(dto);
			}
			else {
				//This method is only used for testing. Return null instead of an error
			}

		} catch (DaoException e) {
			throw new ServiceException("DAO threw an exception", e);
		}

		logger.debug(">getContactEmail");
		return result;
	}

	@Override
	public void synchronizeContactEmail(ContactEmail model, FactoryContext factoryContext,
			WebAdeAuthentication authentication) throws ServiceException, NotFoundException, DaoException {

		logger.debug("<synchronizeContactEmail");
		
		if (model.getTransactionType().equalsIgnoreCase(PoliciesSyncEventTypes.EmailsDeleted)) {
			// DELETE
			deleteContactEmail(model.getContactEmailId(), factoryContext, authentication);
		
		} else if (model.getTransactionType().equalsIgnoreCase(PoliciesSyncEventTypes.EmailsCreated)
				|| model.getTransactionType().equalsIgnoreCase(PoliciesSyncEventTypes.EmailsUpdated)) {
			// INSERT OR UPDATE
			//Only if there is a contact associated with the e-mail
			if(model.getContactId() == null) {
				logger.info("E-Mail Address not inserted or updated because it's not associated with a contact. contact_email_id (EM_ID in CIRRAS): " + model.getContactEmailId());
			} else {
			
				//Check if record already exist and call the correct method
				ContactEmailDto dto = contactEmailDao.fetch(model.getContactEmailId());
	
				if (dto == null) {
					createContactEmail(model, factoryContext, authentication);
				} else {
					updateContactEmail(model, dto, factoryContext, authentication);
				}
			}
		}

		logger.debug(">synchronizeContactEmail");

	}

	private void updateContactEmail(ContactEmail model, ContactEmailDto dto, 
			FactoryContext factoryContext, WebAdeAuthentication authentication) {

		logger.debug("<updateContactEmail");

		try {

			String userId = getUserId(authentication);

			cirrasDataSyncFactory.updateContactEmail(dto, model);
			contactEmailDao.update(dto, userId);

		} catch (DaoException e) {
			e.printStackTrace();
			throw new ServiceException("DAO threw an exception: " + e.getMessage(), e);
		}

		logger.debug(">updateContactEmail");

	}

	private void createContactEmail(ContactEmail model, FactoryContext factoryContext,
			WebAdeAuthentication authentication) {

		logger.debug("<createContactEmail");

		try {

			String userId = getUserId(authentication);
			
			ContactEmailDto dto = new ContactEmailDto();

			cirrasDataSyncFactory.updateContactEmail(dto, model);
			contactEmailDao.insert(dto, userId);

		} catch (DaoException e) {
			e.printStackTrace();
			throw new ServiceException("DAO threw an exception: " + e.getMessage(), e);
		}

		logger.debug(">createContactEmail");

	}

	@Override
	public void deleteContactEmail(Integer contactEmailId,
			FactoryContext factoryContext, WebAdeAuthentication authentication)
			throws ServiceException, NotFoundException, DaoException {
		logger.debug("<deleteContactEmail");

		contactEmailDao.delete(contactEmailId);

		logger.debug(">deleteContactEmail");

	}

	/////////////////////////////////////////////////
	// CONTACT PHONE
	//////////////////////////////////////////////////
	@Override
	public ContactPhone getContactPhone(Integer contactPhoneId, FactoryContext factoryContext, WebAdeAuthentication authentication)
			throws ServiceException, NotFoundException {

		logger.debug("<getContactPhone");

		ContactPhone result = null;

		try {
			ContactPhoneDto dto = contactPhoneDao.fetch(contactPhoneId);

			if (dto != null) {
				result = cirrasDataSyncFactory.getContactPhone(dto);
			}
			else {
				//This method is only used for testing. Return null instead of an error
			}

		} catch (DaoException e) {
			throw new ServiceException("DAO threw an exception", e);
		}

		logger.debug(">getContactPhone");
		return result;
	}

	@Override
	public void synchronizeContactPhone(ContactPhone model, FactoryContext factoryContext,
			WebAdeAuthentication authentication) throws ServiceException, NotFoundException, DaoException {

		logger.debug("<synchronizeContactPhone");

		if (model.getTransactionType().equalsIgnoreCase(PoliciesSyncEventTypes.TelecomDeleted)) {
			// DELETE
			deleteContactPhone(model.getContactPhoneId(), factoryContext, authentication);
		
		} else if (model.getTransactionType().equalsIgnoreCase(PoliciesSyncEventTypes.TelecomCreated)
				|| model.getTransactionType().equalsIgnoreCase(PoliciesSyncEventTypes.TelecomUpdated)) {

			//Check if record already exist and call the correct method
			ContactPhoneDto dto = contactPhoneDao.fetch(model.getContactPhoneId());

			//Only Phone numbers are added. If it's a fax we delete it if it exists
			if(model.getTelecomTypeCode() != null && model.getTelecomTypeCode().equalsIgnoreCase("FAX")) {
				if (dto != null) {
					deleteContactPhone(model.getContactPhoneId(), factoryContext, authentication);
				}
			}
			else {
				// INSERT OR UPDATE

				if (dto == null) {
					createContactPhone(model, factoryContext, authentication);
				} else {
					updateContactPhone(model, dto, factoryContext, authentication);
				}
				
			}
		}

		logger.debug(">synchronizeContactPhone");

	}

	private void updateContactPhone(ContactPhone model, ContactPhoneDto dto, 
			FactoryContext factoryContext, WebAdeAuthentication authentication) {

		logger.debug("<updateContactPhone");

		try {

			String userId = getUserId(authentication);

			cirrasDataSyncFactory.updateContactPhone(dto, model);
			contactPhoneDao.update(dto, userId);

		} catch (DaoException e) {
			e.printStackTrace();
			throw new ServiceException("DAO threw an exception: " + e.getMessage(), e);
		}

		logger.debug(">updateContactPhone");

	}

	private void createContactPhone(ContactPhone model, FactoryContext factoryContext,
			WebAdeAuthentication authentication) {

		logger.debug("<createContactPhone");

		try {

			String userId = getUserId(authentication);
			
			ContactPhoneDto dto = new ContactPhoneDto();

			cirrasDataSyncFactory.updateContactPhone(dto, model);
			contactPhoneDao.insert(dto, userId);

		} catch (DaoException e) {
			e.printStackTrace();
			throw new ServiceException("DAO threw an exception: " + e.getMessage(), e);
		}

		logger.debug(">createContactPhone");

	}

	@Override
	public void deleteContactPhone(Integer contactPhoneId,
			FactoryContext factoryContext, WebAdeAuthentication authentication)
			throws ServiceException, NotFoundException, DaoException {
		logger.debug("<deleteContact");

		contactPhoneDao.delete(contactPhoneId);

		logger.debug(">deleteContactPhone");

	}
	
	/////////////////////////////////////////////////
	// COMMODITY TYPE CODE
	//////////////////////////////////////////////////
	@Override
	public CommodityTypeCode getCommodityTypeCode(String commodityTypeCode,
			FactoryContext factoryContext, WebAdeAuthentication authentication)
			throws ServiceException, NotFoundException {

		logger.debug("<getCommodityTypeCode");

		CommodityTypeCode result = null;

		try {
			CommodityTypeCodeDto dto = commodityTypeCodeDao.fetch(commodityTypeCode);

			if (dto != null) {
				result = cirrasDataSyncFactory.getCommodityTypeCode(dto);
			}
			else {
				//This method is only used for testing. Return null instead of an error
			}

		} catch (DaoException e) {
			throw new ServiceException("DAO threw an exception", e);
		}

		logger.debug(">getCommodityTypeCode");
		return result;
	}

	@Override
	public void synchronizeCommodityTypeCode(CommodityTypeCode model, FactoryContext factoryContext,
			WebAdeAuthentication authentication) throws ServiceException, NotFoundException, DaoException {

		logger.debug("<synchronizeCommodityTypeCode");

		if (model.getTransactionType().equalsIgnoreCase(PoliciesSyncEventTypes.CommodityTypeCodeDeleted)) {
			// DELETE (set inactive)
			inactivateCommodityTypeCode(model, authentication);
		
		} else if (model.getTransactionType().equalsIgnoreCase(PoliciesSyncEventTypes.CommodityTypeCodeCreated)
				|| model.getTransactionType().equalsIgnoreCase(PoliciesSyncEventTypes.CommodityTypeCodeUpdated)) {

			//Check if record already exist and call the correct method
			CommodityTypeCodeDto dto = commodityTypeCodeDao.fetch(model.getCommodityTypeCode());

			// INSERT OR UPDATE
			if (dto == null) {
				createCommodityTypeCode(model, factoryContext, authentication);
			} else {
				updateCommodityTypeCode(model, dto, factoryContext, authentication);
			}
		}

		logger.debug(">synchronizeCommodityTypeCode");

	}

	private void updateCommodityTypeCode(CommodityTypeCode model, CommodityTypeCodeDto dto, 
			FactoryContext factoryContext, WebAdeAuthentication authentication) {

		logger.debug("<updateCommodityTypeCode");

		try {

			String userId = getUserId(authentication);

			cirrasDataSyncFactory.updateCommodityTypeCode(dto, model);
			commodityTypeCodeDao.update(dto, userId);

		} catch (DaoException e) {
			e.printStackTrace();
			throw new ServiceException("DAO threw an exception: " + e.getMessage(), e);
		}

		logger.debug(">updateCommodityTypeCode");

	}

	private void createCommodityTypeCode(CommodityTypeCode model, FactoryContext factoryContext,
			WebAdeAuthentication authentication) {

		logger.debug("<createCommodityTypeCode");

		try {

			String userId = getUserId(authentication);
			
			CommodityTypeCodeDto dto = new CommodityTypeCodeDto();

			cirrasDataSyncFactory.updateCommodityTypeCode(dto, model);
			commodityTypeCodeDao.insert(dto, userId);

		} catch (DaoException e) {
			e.printStackTrace();
			throw new ServiceException("DAO threw an exception: " + e.getMessage(), e);
		}

		logger.debug(">createCommodityTypeCode");

	}
	
	private void inactivateCommodityTypeCode(CommodityTypeCode model, WebAdeAuthentication authentication) throws DaoException {

		logger.debug("<inactivateCommodityTypeCode");

		String userId = getUserId(authentication);

		CommodityTypeCodeDto dto = commodityTypeCodeDao.fetch(model.getCommodityTypeCode());

		if (dto != null) {
			cirrasDataSyncFactory.updateCommodityTypeCodeExpiryDate(dto, model.getDataSyncTransDate());
			commodityTypeCodeDao.update(dto, userId);
		}

		logger.debug(">inactivateCommodityTypeCode");

	}

	@Override
	public void deleteCommodityTypeCode(String commodityTypeCode,
			FactoryContext factoryContext, WebAdeAuthentication authentication)
			throws ServiceException, NotFoundException, DaoException {
		logger.debug("<deleteCommodityTypeCode");

		commodityTypeCodeDao.delete(commodityTypeCode);

		logger.debug(">deleteCommodityTypeCode");

	}
	
	/////////////////////////////////////////////////
	// COMMODITY TYPE VARIETY XREF
	//////////////////////////////////////////////////
	@Override
	public CommodityTypeVarietyXref getCommodityTypeVarietyXref(String commodityTypeCode, Integer cropVarietyId,
			FactoryContext factoryContext, WebAdeAuthentication authentication)
			throws ServiceException, NotFoundException {

		logger.debug("<getCommodityTypeVarietyXref");

		CommodityTypeVarietyXref result = null;

		try {
			CommodityTypeVarietyXrefDto dto = commodityTypeVarietyXrefDao.fetch(commodityTypeCode, cropVarietyId);

			if (dto != null) {
				result = cirrasDataSyncFactory.getCommodityTypeVarietyXref(dto);
			}
			else {
				//This method is only used for testing. Return null instead of an error
			}

		} catch (DaoException e) {
			throw new ServiceException("DAO threw an exception", e);
		}

		logger.debug(">getCommodityTypeVarietyXref");
		return result;
	}

	@Override
	public void synchronizeCommodityTypeVarietyXref(CommodityTypeVarietyXref model, FactoryContext factoryContext,
			WebAdeAuthentication authentication) throws ServiceException, NotFoundException, DaoException {

		logger.debug("<synchronizeCommodityTypeCode");

		if (model.getTransactionType().equalsIgnoreCase(PoliciesSyncEventTypes.CommodityTypeVarietyXrefDeleted)) {
			// DELETE
			deleteCommodityTypeVarietyXref(model.getCommodityTypeCode(), model.getCropVarietyId(), factoryContext, authentication);

		} else if (model.getTransactionType().equalsIgnoreCase(PoliciesSyncEventTypes.CommodityTypeVarietyXrefCreated)) {

			// INSERT ONLY, NO UPDATE

			//Check if record already exist and call the correct method
			CommodityTypeVarietyXrefDto dto = commodityTypeVarietyXrefDao.fetch(model.getCommodityTypeCode(), model.getCropVarietyId());

			if (dto == null) {
				createCommodityTypeVarietyXref(model, factoryContext, authentication);
			} 
		}

		logger.debug(">synchronizeCommodityTypeCode");

	}

	private void createCommodityTypeVarietyXref(CommodityTypeVarietyXref model, FactoryContext factoryContext,
			WebAdeAuthentication authentication) {

		logger.debug("<createCommodityTypeVarietyXref");

		try {

			String userId = getUserId(authentication);
			
			CommodityTypeVarietyXrefDto dto = new CommodityTypeVarietyXrefDto();

			cirrasDataSyncFactory.updateCommodityTypeVarietyXref(dto, model);
			commodityTypeVarietyXrefDao.insert(dto, userId);

		} catch (DaoException e) {
			e.printStackTrace();
			throw new ServiceException("DAO threw an exception: " + e.getMessage(), e);
		}

		logger.debug(">createCommodityTypeVarietyXref");

	}

	@Override
	public void deleteCommodityTypeVarietyXref(String commodityTypeCode, Integer cropVarietyId,
			FactoryContext factoryContext, WebAdeAuthentication authentication)
			throws ServiceException, NotFoundException, DaoException {
		logger.debug("<deleteCommodityTypeVarietyXref");

		commodityTypeVarietyXrefDao.delete(commodityTypeCode, cropVarietyId);

		logger.debug(">deleteCommodityTypeVarietyXref");

	}
	
	public Integer toInteger(String value) {
		Integer result = null;
		if(value!=null&&value.trim().length()>0) {
			result = Integer.valueOf(value);
		}
		return result;
	}


}
