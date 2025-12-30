package ca.bc.gov.mal.cirras.underwriting.services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.mal.cirras.underwriting.data.models.AnnualField;
import ca.bc.gov.mal.cirras.underwriting.data.models.AnnualFieldDetail;
import ca.bc.gov.mal.cirras.underwriting.data.models.ContractedFieldDetail;
import ca.bc.gov.mal.cirras.underwriting.data.models.Field;
import ca.bc.gov.mal.cirras.underwriting.data.models.GrowerContractYear;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.AnnualFieldDetailDao;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.ContractedFieldDetailDao;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.DeclaredYieldContractCommodityDao;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.DeclaredYieldContractCommodityForageDao;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.DeclaredYieldContractDao;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.DeclaredYieldFieldDao;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.DeclaredYieldFieldForageDao;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.DeclaredYieldFieldRollupDao;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.DeclaredYieldFieldRollupForageDao;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.FieldDao;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.GrowerContractYearDao;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.InventoryBerriesDao;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.InventoryContractCommodityDao;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.InventoryContractDao;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.InventoryCoverageTotalForageDao;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.InventoryFieldDao;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.InventorySeededForageDao;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.InventorySeededGrainDao;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.InventoryUnseededDao;
import ca.bc.gov.mal.cirras.underwriting.data.entities.AnnualFieldDetailDto;
import ca.bc.gov.mal.cirras.underwriting.data.entities.ContractedFieldDetailDto;
import ca.bc.gov.mal.cirras.underwriting.data.entities.DeclaredYieldContractDto;
import ca.bc.gov.mal.cirras.underwriting.data.entities.FieldDto;
import ca.bc.gov.mal.cirras.underwriting.data.entities.GrowerContractYearDto;
import ca.bc.gov.mal.cirras.underwriting.data.entities.InventoryContractCommodityDto;
import ca.bc.gov.mal.cirras.underwriting.data.entities.InventoryContractDto;
import ca.bc.gov.mal.cirras.underwriting.data.entities.InventoryFieldDto;
import ca.bc.gov.mal.cirras.underwriting.data.entities.InventorySeededForageDto;
import ca.bc.gov.mal.cirras.underwriting.data.entities.InventorySeededGrainDto;
import ca.bc.gov.mal.cirras.underwriting.data.entities.InventoryUnseededDto;
import ca.bc.gov.mal.cirras.underwriting.data.models.LegalLand;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.LegalLandDao;
import ca.bc.gov.mal.cirras.underwriting.data.entities.LegalLandDto;
import ca.bc.gov.mal.cirras.underwriting.data.models.LegalLandFieldXref;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.LegalLandFieldXrefDao;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.UnderwritingCommentDao;
import ca.bc.gov.mal.cirras.underwriting.data.entities.LegalLandFieldXrefDto;
import ca.bc.gov.mal.cirras.underwriting.services.LandDataSyncService;
import ca.bc.gov.mal.cirras.underwriting.data.assemblers.AnnualFieldDetailRsrcFactory;
import ca.bc.gov.mal.cirras.underwriting.data.assemblers.ContractedFieldDetailRsrcFactory;
import ca.bc.gov.mal.cirras.underwriting.data.assemblers.InventoryContractRsrcFactory;
import ca.bc.gov.mal.cirras.underwriting.data.assemblers.LandDataSyncRsrcFactory;
import ca.bc.gov.mal.cirras.underwriting.services.utils.InventoryServiceEnums;
import ca.bc.gov.mal.cirras.underwriting.services.utils.UnderwritingServiceHelper;
import ca.bc.gov.mal.cirras.underwriting.services.utils.InventoryServiceEnums.InsurancePlans;
import ca.bc.gov.mal.cirras.underwriting.services.utils.InventoryServiceEnums.InventoryCalculationType;
import ca.bc.gov.mal.cirras.underwriting.services.utils.LandManagementEventTypes;

import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;
import ca.bc.gov.nrs.wfone.common.service.api.NotFoundException;
import ca.bc.gov.nrs.wfone.common.service.api.ServiceException;
import ca.bc.gov.nrs.wfone.common.service.api.model.factory.FactoryContext;
import ca.bc.gov.nrs.wfone.common.webade.authentication.WebAdeAuthentication;
import ca.bc.gov.mal.cirras.underwriting.data.resources.PoliciesSyncEventTypes;

public class LandDataSyncServiceImpl implements LandDataSyncService {

	private static final Logger logger = LoggerFactory.getLogger(LandDataSyncServiceImpl.class);

	private Properties applicationProperties;

	// factories
	private LandDataSyncRsrcFactory landDataSyncRsrcFactory;
	private InventoryContractRsrcFactory inventoryContractRsrcFactory;
	private AnnualFieldDetailRsrcFactory annualFieldDetailRsrcFactory; 
	private ContractedFieldDetailRsrcFactory contractedFieldDetailRsrcFactory; 

	// daos
	private LegalLandDao legalLandDao;
	private FieldDao fieldDao;
	private LegalLandFieldXrefDao legalLandFieldXrefDao;
	private AnnualFieldDetailDao annualFieldDetailDao;
	private GrowerContractYearDao growerContractYearDao;
	private ContractedFieldDetailDao contractedFieldDetailDao;
	private InventoryFieldDao inventoryFieldDao;
	private InventoryUnseededDao inventoryUnseededDao;
	private InventorySeededGrainDao inventorySeededGrainDao;
	private InventorySeededForageDao inventorySeededForageDao;
	private InventoryBerriesDao inventoryBerriesDao;
	private UnderwritingCommentDao underwritingCommentDao;
	private InventoryContractDao inventoryContractDao;
	private InventoryContractCommodityDao inventoryContractCommodityDao;
	private InventoryCoverageTotalForageDao inventoryCoverageTotalForageDao;
	private DeclaredYieldContractDao declaredYieldContractDao;
	private DeclaredYieldFieldDao declaredYieldFieldDao;
	private DeclaredYieldFieldForageDao declaredYieldFieldForageDao;
	private DeclaredYieldFieldRollupDao declaredYieldFieldRollupDao;
	private DeclaredYieldContractCommodityDao declaredYieldContractCommodityDao;
	private DeclaredYieldContractCommodityForageDao declaredYieldContractCommodityForageDao;
	private DeclaredYieldFieldRollupForageDao declaredYieldFieldRollupForageDao;


	// utils
	private UnderwritingServiceHelper underwritingServiceHelper;
		

	public static final String MaximumResultsProperty = "maximum.results";

	public static final int DefaultMaximumResults = 800;

	public void setApplicationProperties(Properties applicationProperties) {
		this.applicationProperties = applicationProperties;
	}

	public void setLandDataSyncRsrcFactory(LandDataSyncRsrcFactory landDataSyncRsrcFactory) {
		this.landDataSyncRsrcFactory = landDataSyncRsrcFactory;
	}

	public void setInventoryContractRsrcFactory(InventoryContractRsrcFactory inventoryContractRsrcFactory) {
		this.inventoryContractRsrcFactory = inventoryContractRsrcFactory;
	}

	public void setAnnualFieldDetailRsrcFactory(AnnualFieldDetailRsrcFactory annualFieldDetailRsrcFactory) {
		this.annualFieldDetailRsrcFactory = annualFieldDetailRsrcFactory;
	}

	public void setContractedFieldDetailRsrcFactory(ContractedFieldDetailRsrcFactory contractedFieldDetailRsrcFactory) {
		this.contractedFieldDetailRsrcFactory = contractedFieldDetailRsrcFactory;
	}
	
	public void setLegalLandDao(LegalLandDao legalLandDao) {
		this.legalLandDao = legalLandDao;
	}

	public void setFieldDao(FieldDao fieldDao) {
		this.fieldDao = fieldDao;
	}
	
	public void setLegalLandFieldXrefDao(LegalLandFieldXrefDao legalLandFieldXrefDao) {
		this.legalLandFieldXrefDao = legalLandFieldXrefDao;
	}
	
	public void setAnnualFieldDetailDao(AnnualFieldDetailDao annualFieldDetailDao) {
		this.annualFieldDetailDao = annualFieldDetailDao;
	}
	
	public void setGrowerContractYearDao(GrowerContractYearDao growerContractYearDao) {
		this.growerContractYearDao = growerContractYearDao;
	}

	public void setContractedFieldDetailDao(ContractedFieldDetailDao contractedFieldDetailDao) {
		this.contractedFieldDetailDao = contractedFieldDetailDao;
	}
	
	public void setInventoryFieldDao(InventoryFieldDao inventoryFieldDao) {
		this.inventoryFieldDao = inventoryFieldDao;
	}
	
	public void setInventoryUnseededDao(InventoryUnseededDao inventoryUnseededDao) {
		this.inventoryUnseededDao = inventoryUnseededDao;
	}
	
	public void setInventorySeededGrainDao(InventorySeededGrainDao inventorySeededGrainDao) {
		this.inventorySeededGrainDao = inventorySeededGrainDao;
	}
	
	public void setInventorySeededForageDao(InventorySeededForageDao inventorySeededForageDao) {
		this.inventorySeededForageDao = inventorySeededForageDao;
	}
	
	public void setInventoryBerriesDao(InventoryBerriesDao inventoryBerriesDao) {
		this.inventoryBerriesDao = inventoryBerriesDao;
	}

	public void setUnderwritingCommentDao(UnderwritingCommentDao underwritingCommentDao) {
		this.underwritingCommentDao = underwritingCommentDao;
	}
	
	public void setInventoryContractDao(InventoryContractDao inventoryContractDao) {
		this.inventoryContractDao = inventoryContractDao;
	}
	
	public void setInventoryContractCommodityDao(InventoryContractCommodityDao inventoryContractCommodityDao) {
		this.inventoryContractCommodityDao = inventoryContractCommodityDao;
	}
	
	public void setInventoryCoverageTotalForageDao(InventoryCoverageTotalForageDao inventoryCoverageTotalForageDao) {
		this.inventoryCoverageTotalForageDao = inventoryCoverageTotalForageDao;
	}

	public void setUnderwritingServiceHelper(UnderwritingServiceHelper underwritingServiceHelper) {
		this.underwritingServiceHelper = underwritingServiceHelper;
	}
	
	public void setDeclaredYieldFieldDao(DeclaredYieldFieldDao declaredYieldFieldDao) {
		this.declaredYieldFieldDao = declaredYieldFieldDao;
	}
	
	public void setDeclaredYieldFieldForageDao(DeclaredYieldFieldForageDao declaredYieldFieldForageDao) {
		this.declaredYieldFieldForageDao = declaredYieldFieldForageDao;
	}
	
	public void setDeclaredYieldFieldRollupDao(DeclaredYieldFieldRollupDao declaredYieldFieldRollupDao) {
		this.declaredYieldFieldRollupDao = declaredYieldFieldRollupDao;
	}

	public void setDeclaredYieldContractCommodityDao(DeclaredYieldContractCommodityDao declaredYieldContractCommodityDao) {
		this.declaredYieldContractCommodityDao = declaredYieldContractCommodityDao;
	}

	public void setDeclaredYieldContractCommodityForageDao(DeclaredYieldContractCommodityForageDao declaredYieldContractCommodityForageDao) {
		this.declaredYieldContractCommodityForageDao = declaredYieldContractCommodityForageDao;
	}

	public void setDeclaredYieldFieldRollupForageDao(DeclaredYieldFieldRollupForageDao declaredYieldFieldRollupForageDao) {
		this.declaredYieldFieldRollupForageDao = declaredYieldFieldRollupForageDao;
	}

	public void setDeclaredYieldContractDao(DeclaredYieldContractDao declaredYieldContractDao) {
		this.declaredYieldContractDao = declaredYieldContractDao;
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



	/////////////////////////////////////////////////
	// LEGAL LAND
	//////////////////////////////////////////////////
	@Override
	public LegalLand<? extends Field> getLegalLand(Integer legalLandId, FactoryContext factoryContext, WebAdeAuthentication authentication)
			throws ServiceException, NotFoundException, DaoException {

		logger.debug("<getLegalLand");

		LegalLand<? extends Field> result = null;

		try {
			LegalLandDto dto = legalLandDao.fetch(legalLandId);

			if (dto != null) {
				result = landDataSyncRsrcFactory.getLegalLandSync(dto);
			}
			else {
				//This method is only used for testing. Return null instead of an error
				//throw new NotFoundException("Did not find legal land record: legalLandId: " + legalLandId);
			}

		} catch (DaoException e) {
			throw new ServiceException("DAO threw an exception", e);
		}

		logger.debug(">getLegalLand");
		return result;
	}

	@Override
	public void synchronizeLegalLand(LegalLand<? extends Field> model, FactoryContext factoryContext,
			WebAdeAuthentication authentication) throws ServiceException, NotFoundException, DaoException {

		logger.debug("<synchronizeLegalLand");

		if (model.getTransactionType().equalsIgnoreCase(LandManagementEventTypes.LegalLandDeleted)) {
			// DELETE
			deleteLegalLand(model.getLegalLandId(), factoryContext, authentication);
		
		} else if (model.getTransactionType().equalsIgnoreCase(LandManagementEventTypes.LegalLandCreated)
				|| model.getTransactionType().equalsIgnoreCase(LandManagementEventTypes.LegalLandUpdated)) {
			// INSERT OR UPDATE
			
			//Check if record already exist and call the correct method
			LegalLandDto dto = legalLandDao.fetch(model.getLegalLandId());

			if (dto == null) {
				createLegalLand(model, factoryContext, authentication);
			} else {
				updateLegalLand(model, dto, factoryContext, authentication);
			}
		}

		logger.debug(">synchronizeLegalLand");

	}

	private void updateLegalLand(LegalLand<? extends Field> model, LegalLandDto dto, 
			FactoryContext factoryContext, WebAdeAuthentication authentication) {

		logger.debug("<updateLegalLand");

		try {

			String userId = getUserId(authentication);

			landDataSyncRsrcFactory.updateLegalLand(dto, model);
			legalLandDao.update(dto, userId);

		} catch (DaoException e) {
			e.printStackTrace();
			throw new ServiceException("DAO threw an exception: " + e.getMessage(), e);
		}

		logger.debug(">updateLegalLand");

	}

	private void createLegalLand(LegalLand<? extends Field> model, FactoryContext factoryContext,
			WebAdeAuthentication authentication) {

		logger.debug("<createLegalLand");

		try {

			String userId = getUserId(authentication);
			
			LegalLandDto dto = new LegalLandDto();

			landDataSyncRsrcFactory.updateLegalLand(dto, model);
			legalLandDao.insertDataSync(dto, userId);

		} catch (DaoException e) {
			e.printStackTrace();
			throw new ServiceException("DAO threw an exception: " + e.getMessage(), e);
		}

		logger.debug(">createLegalLand");

	}

	@Override
	public void deleteLegalLand(Integer legalLandId,
			FactoryContext factoryContext, WebAdeAuthentication authentication)
			throws ServiceException, NotFoundException, DaoException {
		logger.debug("<deleteLegalLand");

		legalLandDao.delete(legalLandId);

		logger.debug(">deleteLegalLand");

	}
	
	/////////////////////////////////////////////////
	// FIELD
	//////////////////////////////////////////////////
	
	@Override
	public Field getField(Integer fieldId, FactoryContext factoryContext, WebAdeAuthentication authentication)
			throws ServiceException, NotFoundException, DaoException {

		logger.debug("<getField");

		Field result = null;

		try {
			FieldDto dto = fieldDao.fetch(fieldId);

			if (dto != null) {
				result = landDataSyncRsrcFactory.getField(dto);
			}
			else {
				//This method is only used for testing. Return null instead of an error
				//throw new NotFoundException("Did not find field record: fieldId: " + fieldId);
			}

		} catch (DaoException e) {
			throw new ServiceException("DAO threw an exception", e);
		}

		logger.debug(">getField");
		return result;
	}

	@Override
	public void synchronizeField(Field model, FactoryContext factoryContext,
			WebAdeAuthentication authentication) throws ServiceException, NotFoundException, DaoException {

		logger.debug("<synchronizeField");

		if (model.getTransactionType().equalsIgnoreCase(LandManagementEventTypes.FieldDeleted)) {
			// DELETE
			deleteField(model.getFieldId(), factoryContext, authentication);
		
		} else if (model.getTransactionType().equalsIgnoreCase(LandManagementEventTypes.FieldCreated)
				|| model.getTransactionType().equalsIgnoreCase(LandManagementEventTypes.FieldUpdated)) {
			// INSERT OR UPDATE
			
			//Check if record already exist and call the correct method
			FieldDto dto = fieldDao.fetch(model.getFieldId());

			if (dto == null) {
				createField(model, factoryContext, authentication);
			} else {
				updateField(model, dto, factoryContext, authentication);
			}
		}

		logger.debug(">synchronizeField");

	}

	private void updateField(Field model, FieldDto dto, 
			FactoryContext factoryContext, WebAdeAuthentication authentication) {

		logger.debug("<updateField");

		try {

			String userId = getUserId(authentication);

			landDataSyncRsrcFactory.updateField(dto, model);
			fieldDao.update(dto, userId);

		} catch (DaoException e) {
			e.printStackTrace();
			throw new ServiceException("DAO threw an exception: " + e.getMessage(), e);
		}

		logger.debug(">updateField");

	}

	private void createField(Field model, FactoryContext factoryContext,
			WebAdeAuthentication authentication) {

		logger.debug("<createField");

		try {

			String userId = getUserId(authentication);
			
			FieldDto dto = new FieldDto();

			landDataSyncRsrcFactory.updateField(dto, model);
			fieldDao.insertDataSync(dto, userId);

		} catch (DaoException e) {
			e.printStackTrace();
			throw new ServiceException("DAO threw an exception: " + e.getMessage(), e);
		}

		logger.debug(">createField");

	}

	@Override
	public void deleteField(Integer fieldId,
			FactoryContext factoryContext, WebAdeAuthentication authentication)
			throws ServiceException, NotFoundException, DaoException {
		logger.debug("<deleteField");
		
		//Delete inventory and yield data of the field if it exists
		declaredYieldFieldForageDao.deleteForField(fieldId);
		declaredYieldFieldDao.deleteForField(fieldId);

		String userId = getUserId(authentication);

		inventoryFieldDao.removeLinkToPlantingForField(fieldId, userId);
		inventorySeededGrainDao.deleteForField(fieldId);
		inventoryUnseededDao.deleteForField(fieldId);
		inventorySeededForageDao.deleteForField(fieldId);
		inventoryBerriesDao.deleteForField(fieldId);
		inventoryFieldDao.deleteForField(fieldId);

		fieldDao.delete(fieldId);

		logger.debug(">deleteField");

	}
	
	/////////////////////////////////////////////////
	// LEGAL LAND - FIELD XREF
	//////////////////////////////////////////////////
	@Override
	public LegalLandFieldXref getLegalLandFieldXref(Integer legalLandId, 
			Integer fieldId, FactoryContext factoryContext, WebAdeAuthentication authentication)
			throws ServiceException, NotFoundException, DaoException {
	
		logger.debug("<getLegalLandFieldXref");
		
		LegalLandFieldXref result = null;
		
		try {
			LegalLandFieldXrefDto dto = legalLandFieldXrefDao.fetch(legalLandId, fieldId);
			
			if (dto != null) {
				result = landDataSyncRsrcFactory.getLegalLandFieldXrefSync(dto);
			}
			else {
				throw new NotFoundException("Did not find legal land field xref record: legalLandId: " + legalLandId);
			}
			
		} catch (DaoException e) {
			throw new ServiceException("DAO threw an exception", e);
		}
		
		logger.debug(">getLegalLandFieldXref");
		return result;
	}

	@Override
	public void synchronizeLegalLandFieldXref(LegalLandFieldXref model, FactoryContext factoryContext,
			WebAdeAuthentication authentication) throws ServiceException, NotFoundException, DaoException {
	
		logger.debug("<synchronizeLegalLandFieldXref");
		
		if (model.getTransactionType().equalsIgnoreCase(LandManagementEventTypes.LegalLandFieldXrefDeleted)) {
			// DELETE
			deleteLegalLandFieldXref(model.getLegalLandId(), model.getFieldId(), factoryContext, authentication);
			
		} else if ( model.getTransactionType().equalsIgnoreCase(LandManagementEventTypes.LegalLandFieldXrefCreated) ) {

			// INSERT ONLY, NO UPDATE
			
			//Check if record already exist and call the correct method
			LegalLandFieldXrefDto dto = legalLandFieldXrefDao.fetch(model.getLegalLandId(), model.getFieldId());

			if (dto == null) {
				createLegalLandFieldXref(model, factoryContext, authentication);
			} 			
		}
		
		logger.debug(">synchronizeLegalLandFieldXref");
	
	}

	private void createLegalLandFieldXref(LegalLandFieldXref model, FactoryContext factoryContext,
			WebAdeAuthentication authentication) {
	
		logger.debug("<createLegalLandFieldXref");
		
		try {
		
		String userId = getUserId(authentication);
		
		LegalLandFieldXrefDto dto = new LegalLandFieldXrefDto();
		
		landDataSyncRsrcFactory.updateLegalLandFieldXref(dto, model);
		legalLandFieldXrefDao.insert(dto, userId);
		
		} catch (DaoException e) {
			e.printStackTrace();
			throw new ServiceException("DAO threw an exception: " + e.getMessage(), e);
		}
		
		logger.debug(">createLegalLandFieldXref");
	
	}

	@Override
	public void deleteLegalLandFieldXref(Integer legalLandId, Integer fieldId,
			FactoryContext factoryContext, WebAdeAuthentication authentication)
					throws ServiceException, NotFoundException, DaoException {
		
		logger.debug("<deleteLegalLandFieldXref");
		
		legalLandFieldXrefDao.delete(legalLandId, fieldId);
		
		logger.debug(">deleteLegalLandFieldXref");
	
	}


	
	/////////////////////////////////////////////////
	// ANNUAL FIELD DETAIL
	//////////////////////////////////////////////////
	
	@Override
	public AnnualFieldDetail getAnnualFieldDetail(Integer annualFieldDetailId, FactoryContext factoryContext, WebAdeAuthentication authentication)
			throws ServiceException, NotFoundException, DaoException {
	
		logger.debug("<getAnnualFieldDetail");
		
		AnnualFieldDetail result = null;
		
		try {
			AnnualFieldDetailDto dto = annualFieldDetailDao.fetch(annualFieldDetailId);
		
			if (dto != null) {
				result = landDataSyncRsrcFactory.getAnnualFieldDetail(dto);
			}
			else {
				//This method is only used for testing. Return null instead of an error
				//throw new NotFoundException("Did not find field record: fieldId: " + fieldId);
			}
		
		} catch (DaoException e) {
			throw new ServiceException("DAO threw an exception", e);
		}
		
		logger.debug(">getAnnualFieldDetail");
		return result;
	}

	@Override
	public void synchronizeAnnualFieldDetail(AnnualFieldDetail model, FactoryContext factoryContext,
			WebAdeAuthentication authentication) throws ServiceException, NotFoundException, DaoException {
	
		logger.debug("<synchronizeAnnualFieldDetail");
		
		if (model.getTransactionType().equalsIgnoreCase(LandManagementEventTypes.AnnualFieldDetailDeleted)) {
			// DELETE
			deleteAnnualFieldDetail(model.getAnnualFieldDetailId(), factoryContext, authentication);
		
		} else if (model.getTransactionType().equalsIgnoreCase(LandManagementEventTypes.AnnualFieldDetailCreated)
			|| model.getTransactionType().equalsIgnoreCase(LandManagementEventTypes.AnnualFieldDetailUpdated)) {
			// INSERT OR UPDATE
			
			//Check if record already exist and call the correct method
			AnnualFieldDetailDto dto = annualFieldDetailDao.fetch(model.getAnnualFieldDetailId());
			
			if (dto == null) {
				createAnnualFieldDetail(model, factoryContext, authentication);
			} else {
				updateAnnualFieldDetail(model, dto, factoryContext, authentication);
			}
		}
		
		logger.debug(">synchronizeAnnualFieldDetail");
	
	}

	private void updateAnnualFieldDetail(AnnualFieldDetail model, AnnualFieldDetailDto dto, 
			FactoryContext factoryContext, WebAdeAuthentication authentication) {
	
		logger.debug("<updateAnnualFieldDetail");
		
		try {
		
			String userId = getUserId(authentication);
			
			landDataSyncRsrcFactory.updateAnnualFieldDetail(dto, model);
			annualFieldDetailDao.update(dto, userId);
			
		} catch (DaoException e) {
			e.printStackTrace();
			throw new ServiceException("DAO threw an exception: " + e.getMessage(), e);
		}
		
		logger.debug(">updateAnnualFieldDetail");
	
	}

	private void createAnnualFieldDetail(AnnualFieldDetail model, FactoryContext factoryContext,
			WebAdeAuthentication authentication) {
	
		logger.debug("<createAnnualFieldDetail");
		
		try {
		
			String userId = getUserId(authentication);
			
			AnnualFieldDetailDto dto = new AnnualFieldDetailDto();
			
			landDataSyncRsrcFactory.updateAnnualFieldDetail(dto, model);
			annualFieldDetailDao.insertDataSync(dto, userId);
		
		} catch (DaoException e) {
			e.printStackTrace();
			throw new ServiceException("DAO threw an exception: " + e.getMessage(), e);
		}
		
		logger.debug(">createAnnualFieldDetail");
	
	}

	@Override
	public void deleteAnnualFieldDetail(Integer annualFieldDetailId,
		FactoryContext factoryContext, WebAdeAuthentication authentication)
		throws ServiceException, NotFoundException, DaoException {
		logger.debug("<deleteAnnualFieldDetail");
		
		underwritingCommentDao.deleteForAnnualField(annualFieldDetailId);
		annualFieldDetailDao.delete(annualFieldDetailId);
		
		logger.debug(">deleteAnnualFieldDetail");
	
	}


	/////////////////////////////////////////////////
	// GROWER CONTRACT YEAR
	//////////////////////////////////////////////////
	@Override
	public GrowerContractYear getGrowerContractYear(Integer growerContractYearId, FactoryContext factoryContext, WebAdeAuthentication authentication)
			throws ServiceException, NotFoundException, DaoException {

		logger.debug("<getGrowerContractYear");

		GrowerContractYear result = null;

		try {
			GrowerContractYearDto dto = growerContractYearDao.fetch(growerContractYearId);

			if (dto != null) {
				result = landDataSyncRsrcFactory.getGrowerContractYear(dto);
			}
			else {
				//This method is only used for testing. Return null instead of an error
				//throw new NotFoundException("Did not find legal land record: legalLandId: " + legalLandId);
			}

		} catch (DaoException e) {
			throw new ServiceException("DAO threw an exception", e);
		}

		logger.debug(">getGrowerContractYear");
		return result;
	}

	@Override
	public void synchronizeGrowerContractYear(GrowerContractYear model, FactoryContext factoryContext,
			WebAdeAuthentication authentication) throws ServiceException, NotFoundException, DaoException {

		logger.debug("<synchronizeGrowerContractYear");

		if (model.getTransactionType().equalsIgnoreCase(PoliciesSyncEventTypes.GrowerContractYearDeleted)) {
			// DELETE
			deleteGrowerContractYear(model.getGrowerContractYearId(), factoryContext, authentication);
		
		} else if (model.getTransactionType().equalsIgnoreCase(PoliciesSyncEventTypes.GrowerContractYearCreated)
				|| model.getTransactionType().equalsIgnoreCase(PoliciesSyncEventTypes.GrowerContractYearUpdated)) {
			// INSERT OR UPDATE
			
			//Check if record already exist and call the correct method
			GrowerContractYearDto dto = growerContractYearDao.fetch(model.getGrowerContractYearId());

			if (dto == null) {
				createGrowerContractYear(model, factoryContext, authentication);
				rolloverGrowerContractYear(model, authentication);
			} else {
				updateGrowerContractYear(model, dto, factoryContext, authentication);
			}
		}

		logger.debug(">synchronizeGrowerContractYear");

	}

	private void updateGrowerContractYear(GrowerContractYear model, GrowerContractYearDto dto, 
			FactoryContext factoryContext, WebAdeAuthentication authentication) {

		logger.debug("<updateGrowerContractYear");

		try {

			String userId = getUserId(authentication);

			landDataSyncRsrcFactory.updateGrowerContractYear(dto, model);
			growerContractYearDao.update(dto, userId);

		} catch (DaoException e) {
			e.printStackTrace();
			throw new ServiceException("DAO threw an exception: " + e.getMessage(), e);
		}

		logger.debug(">updateGrowerContractYear");

	}

	private void createGrowerContractYear(GrowerContractYear model, FactoryContext factoryContext,
			WebAdeAuthentication authentication) {

		logger.debug("<createGrowerContractYear");

		try {

			String userId = getUserId(authentication);
			
			GrowerContractYearDto dto = new GrowerContractYearDto();

			landDataSyncRsrcFactory.updateGrowerContractYear(dto, model);
			growerContractYearDao.insert(dto, userId);

		} catch (DaoException e) {
			e.printStackTrace();
			throw new ServiceException("DAO threw an exception: " + e.getMessage(), e);
		}

		logger.debug(">createGrowerContractYear");

	}

	private void rolloverGrowerContractYear(GrowerContractYear currGcyModel, WebAdeAuthentication authentication) {

		logger.debug("<rolloverGrowerContractYear");
		
		Integer insurancePlanId = currGcyModel.getInsurancePlanId();

		String userId = getUserId(authentication);

		// Only rollover for GRAIN, FORAGE and BERRIES.
		if ( InsurancePlans.GRAIN.getInsurancePlanId().equals(insurancePlanId) 
				|| InsurancePlans.FORAGE.getInsurancePlanId().equals(insurancePlanId)
				|| InsurancePlans.BERRIES.getInsurancePlanId().equals(insurancePlanId)) {

			try {
				GrowerContractYearDto lastGcyDto = growerContractYearDao.selectLastYear(currGcyModel.getContractId(), currGcyModel.getCropYear());
				
				if ( lastGcyDto != null ) { 
					List<ContractedFieldDetailDto> lastCfdDtos = contractedFieldDetailDao.select(lastGcyDto.getContractId(), lastGcyDto.getCropYear());
					
					if ( lastCfdDtos != null && lastCfdDtos.size() > 0 ) { 
						int currDisplayOrder = 1;
						for ( ContractedFieldDetailDto lastCfdDto : lastCfdDtos ) { 
							
							List<ContractedFieldDetailDto> currCfdDtos = contractedFieldDetailDao.selectForYearAndField(currGcyModel.getCropYear(), lastCfdDto.getFieldId());

							// Rollover field if not already associated with a contract in the same year on the same plan. Otherwise skip.
							boolean doRolloverField = true;
							if ( currCfdDtos != null ) {
								for ( ContractedFieldDetailDto existingCfdDto : currCfdDtos ) {
									if ( existingCfdDto.getInsurancePlanId().equals(insurancePlanId) ) {
										doRolloverField = false;
										break;
									}
								}
							}
							
							if ( doRolloverField ) {
								AnnualFieldDetailDto currAfdDto = annualFieldDetailDao.getByFieldAndCropYear(lastCfdDto.getFieldId(), currGcyModel.getCropYear());

								if ( currAfdDto == null ) {

									FieldDto fldDto = fieldDao.fetch(lastCfdDto.getFieldId());
									if ( fldDto.getActiveToCropYear() != null && fldDto.getActiveToCropYear() < currGcyModel.getCropYear() ) {
										// Field is inactive in the current crop year, so re-activate.
										fldDto.setActiveToCropYear(currGcyModel.getCropYear());
										fieldDao.update(fldDto, userId);
									}
									
									if ( lastCfdDto.getLegalLandId() != null ) { 
										LegalLandDto llDto = legalLandDao.fetch(lastCfdDto.getLegalLandId());
										if ( llDto.getActiveToCropYear() != null && llDto.getActiveToCropYear() < currGcyModel.getCropYear() ) {
											// Legal Land is inactive in the current crop year, so re-activate.
											llDto.setActiveToCropYear(currGcyModel.getCropYear());
											legalLandDao.update(llDto, userId);
										}
									}
									
									// No AnnualFieldDetail record in current crop year, so create.
									currAfdDto = new AnnualFieldDetailDto();
									annualFieldDetailRsrcFactory.createRolloverAnnualFieldDetail(currAfdDto, lastCfdDto.getLegalLandId(), lastCfdDto.getFieldId(), currGcyModel.getCropYear());
									annualFieldDetailDao.insert(currAfdDto, userId);
								}
								
								// Create ContractedFieldDetail record.
								ContractedFieldDetailDto currCfdDto = new ContractedFieldDetailDto();
								contractedFieldDetailRsrcFactory.createRolloverContractedFieldDetail(currCfdDto, currAfdDto.getAnnualFieldDetailId(), currGcyModel.getGrowerContractYearId(), currDisplayOrder++, lastCfdDto.getIsLeasedInd());
								contractedFieldDetailDao.insert(currCfdDto, userId);
							}
						}
					}
				}
			} catch (DaoException e) {
				throw new ServiceException("DAO threw an exception: " + e.getMessage(), e);
			}
		}

		logger.debug(">rolloverGrowerContractYear");
	}
	
	@Override
	public void deleteGrowerContractYear(Integer growerContractYearId,
			FactoryContext factoryContext, WebAdeAuthentication authentication)
			throws ServiceException, NotFoundException, DaoException {
		logger.debug("<deleteGrowerContractYear");

		//Before deleting the grower contract year record, all child records need to be deleted
		GrowerContractYearDto gcyDto = growerContractYearDao.fetch(growerContractYearId);
		
		if(gcyDto != null) {
			
			String userId = getUserId(authentication);
			
			
			//Get Declared Yield Contract
			DeclaredYieldContractDto dyDto = declaredYieldContractDao.getByGrowerContract(growerContractYearId);
			
			if(dyDto != null) {
				
				//DELETE ALL YIELD DATA

				//Grain
				declaredYieldFieldDao.deleteForDeclaredYieldContract(dyDto.getDeclaredYieldContractGuid());
				  //Declared Yield Field Rollup
				declaredYieldFieldRollupDao.deleteForDeclaredYieldContract(dyDto.getDeclaredYieldContractGuid());
				declaredYieldContractCommodityDao.deleteForDeclaredYieldContract(dyDto.getDeclaredYieldContractGuid());

				//Forage
				declaredYieldFieldForageDao.deleteForDeclaredYieldContract(dyDto.getDeclaredYieldContractGuid());
				  //Commodity Totals
				declaredYieldContractCommodityForageDao.deleteForDeclaredYieldContract(dyDto.getDeclaredYieldContractGuid());
				  //Commodity Rollup
				declaredYieldFieldRollupForageDao.deleteForDeclaredYieldContract(dyDto.getDeclaredYieldContractGuid());
				
				//Underwriting Comment
				underwritingCommentDao.deleteForDeclaredYieldContractGuid(dyDto.getDeclaredYieldContractGuid());

				//Declared Yield Contract
				declaredYieldContractDao.delete(dyDto.getDeclaredYieldContractGuid());
				
			}

			
			//Get InventoryContract 
			InventoryContractDto icDto = inventoryContractDao.getByGrowerContract(growerContractYearId);
			
			if(icDto != null) {
				
				//DELETE ALL INVENTORY CONTRACT DATA
				
				//Remove the link to seeded forage record before deleting it
				inventoryFieldDao.removeLinkToPlantingForInventoryContract(icDto.getInventoryContractGuid(), userId);
				//Seeded Forage
				inventorySeededForageDao.deleteForInventoryContract(icDto.getInventoryContractGuid());
				//Seeded Grain
				inventorySeededGrainDao.deleteForInventoryContract(icDto.getInventoryContractGuid());
				//Unseeded
				inventoryUnseededDao.deleteForInventoryContract(icDto.getInventoryContractGuid());
				//Berries
				inventoryBerriesDao.deleteForInventoryContract(icDto.getInventoryContractGuid());
				//Inventory Field
				inventoryFieldDao.deleteForInventoryContract(icDto.getInventoryContractGuid());
				
				//Inventory Contract Commodity
				inventoryContractCommodityDao.deleteForInventoryContract(icDto.getInventoryContractGuid());
				
				//Inventory Coverage Total Forage
				inventoryCoverageTotalForageDao.deleteForInventoryContract(icDto.getInventoryContractGuid());

				//Inventory Contract
				inventoryContractDao.delete(icDto.getInventoryContractGuid());
				
			}
			
			
			//FIELD DATA

			//Get contract fields
			List<ContractedFieldDetailDto> cfdDtos = contractedFieldDetailDao.select(gcyDto.getContractId(), gcyDto.getCropYear());

			if(cfdDtos != null && cfdDtos.isEmpty() == false) {
				for (ContractedFieldDetailDto cfdDto : cfdDtos) {
					
					List<ContractedFieldDetailDto> currCfdDtos = contractedFieldDetailDao.selectForYearAndField(gcyDto.getCropYear(), cfdDto.getFieldId());
					
					Integer totalContracts = currCfdDtos.size();
					
					//Contracted Field Detail
					contractedFieldDetailDao.delete(cfdDto.getContractedFieldDetailId());

					if(totalContracts == 1) {
						//Underwriting Comment and Annual Field Detail - if the field is only associated to this policy
						underwritingCommentDao.deleteForAnnualField(cfdDto.getAnnualFieldDetailId());
						annualFieldDetailDao.delete(cfdDto.getAnnualFieldDetailId());
					}
				}
			}
			
			//Delete grower contract year record
			growerContractYearDao.delete(growerContractYearId);
			
		}

		logger.debug(">deleteGrowerContractYear");

	}

	/////////////////////////////////////////////////
	// Contracted Field Detail
	//////////////////////////////////////////////////
	
	@Override
	public ContractedFieldDetail getContractedFieldDetail(Integer contractedFieldDetailId, FactoryContext factoryContext, WebAdeAuthentication authentication)
			throws ServiceException, NotFoundException, DaoException {
	
		logger.debug("<getContractedFieldDetail");
		
		ContractedFieldDetail result = null;
		
		try {
			ContractedFieldDetailDto dto = contractedFieldDetailDao.fetchSimple(contractedFieldDetailId);
		
			if (dto != null) {
				result = landDataSyncRsrcFactory.getContractedFieldDetail(dto);
			}
			else {
				//This method is only used for testing. Return null instead of an error
				//throw new NotFoundException("Did not find field record: fieldId: " + fieldId);
			}
		
		} catch (DaoException e) {
			throw new ServiceException("DAO threw an exception", e);
		}
		
		logger.debug(">getContractedFieldDetail");
		return result;
	}

	@Override
	public void synchronizeContractedFieldDetail(ContractedFieldDetail model, FactoryContext factoryContext,
			WebAdeAuthentication authentication) throws ServiceException, NotFoundException, DaoException {
	
		logger.debug("<synchronizeContractedFieldDetail");
		
		String userId = getUserId(authentication);
		
		//Get contracted field data for 
		ContractedFieldDetailDto dto = contractedFieldDetailDao.fetch(model.getContractedFieldDetailId());
		
		if (model.getTransactionType().equalsIgnoreCase(LandManagementEventTypes.ContractedFieldDetailDeleted)) {
			// DELETE
			deleteContractedFieldDetail(model.getContractedFieldDetailId(), factoryContext, authentication);
			if(dto != null) {
				recalculateInventoryCommodityTotals(dto, dto.getGrowerContractYearId(), subtractFromTotal, userId);
				recalculateInventoryCoverageTotalForages(dto, dto.getGrowerContractYearId(), subtractFromTotal, userId, authentication);
			}
		
		} else if (model.getTransactionType().equalsIgnoreCase(LandManagementEventTypes.ContractedFieldDetailCreated)
			|| model.getTransactionType().equalsIgnoreCase(LandManagementEventTypes.ContractedFieldDetailUpdated)) {
			// INSERT OR UPDATE
			
			//Check if record already exist and call the correct method
			if (dto == null) {
				createContractedFieldDetail(model, factoryContext, authentication);
				//Get grower contract year for the insurance plan
				dto = contractedFieldDetailDao.fetch(model.getContractedFieldDetailId());
				recalculateInventoryCommodityTotals(dto, model.getGrowerContractYearId(), addToTotal, userId);
				recalculateInventoryCoverageTotalForages(dto, model.getGrowerContractYearId(), addToTotal, userId, authentication);

			} else {
				updateContractedFieldDetail(model, dto, factoryContext, authentication);
				// TODO: This condition appears to be always false, because the call to updateContractedFieldDetail copies model into dto, so the gcy id would always be the same afterward?
				if(!model.getGrowerContractYearId().equals(dto.getGrowerContractYearId())) {
					//If land has been transferred recalculate commodity totals for both contracts
					//Subtract from old contract and add to new contract
					recalculateInventoryCommodityTotals(dto, dto.getGrowerContractYearId(), subtractFromTotal, userId);
					recalculateInventoryCommodityTotals(dto, model.getGrowerContractYearId(), addToTotal, userId);

					recalculateInventoryCoverageTotalForages(dto, dto.getGrowerContractYearId(), subtractFromTotal, userId, authentication);
					recalculateInventoryCoverageTotalForages(dto, model.getGrowerContractYearId(), addToTotal, userId, authentication);
				}
			}
		}
		
		logger.debug(">synchronizeContractedFieldDetail");
	
	}
	
	private static final String subtractFromTotal = "subtractFromTotal";
	private static final String addToTotal = "addToTotal";

	private void recalculateInventoryCommodityTotals(ContractedFieldDetailDto cfdDto, 
			Integer gcyId,
			String operation,
			String userId
			) throws DaoException {
		
		logger.debug("<recalculateInventoryCommodityTotals");

		if(cfdDto != null) {
			
			//Load totals by commodity for the field
			List<InventoryUnseededDto> dtoUnseededList = inventoryUnseededDao.selectTotalsForFieldYearPlan(cfdDto.getFieldId(), cfdDto.getCropYear(), cfdDto.getInsurancePlanId());
			List<InventorySeededGrainDto> dtoSeededGrainList = inventorySeededGrainDao.selectTotalsForFieldYearPlan(cfdDto.getFieldId(), cfdDto.getCropYear(), cfdDto.getInsurancePlanId());

			if((dtoUnseededList != null && dtoUnseededList.size() > 0) || dtoSeededGrainList != null && dtoSeededGrainList.size() > 0) {
				
				//Get inventory contract
				InventoryContractDto dtoInvContractList = inventoryContractDao.getByGrowerContract(gcyId);

				if (dtoInvContractList != null) {
					
					//Get inventory contract commodities
					List<InventoryContractCommodityDto> invCommodityDtos = inventoryContractCommodityDao.select(dtoInvContractList.getInventoryContractGuid());
					
					//Update Unseeded Acres
					updateUnseededInventoryCommodity(operation, userId, dtoUnseededList, dtoInvContractList, invCommodityDtos, cfdDto.getInsurancePlanId());
					//Update Seeded and Spot Loss Acres
					updateSeededGrainInventoryCommodity(operation, userId, dtoSeededGrainList, dtoInvContractList, invCommodityDtos);
				}
			}
		}
		logger.debug(">recalculateInventoryCommodityTotals");
		
	}

	private void updateSeededGrainInventoryCommodity(String operation, String userId,
			List<InventorySeededGrainDto> dtoSeededGrainList, InventoryContractDto dtoInvContractList,
			List<InventoryContractCommodityDto> invCommodityDtos) throws DaoException, NotFoundDaoException {

		logger.debug("<updateSeededGrainInventoryCommodity");
		
		if(dtoSeededGrainList != null && dtoSeededGrainList.size() > 0) {
			for(InventorySeededGrainDto dtoSeeded : dtoSeededGrainList) {
				
				List<InventoryContractCommodityDto> filteredCommodityDto = null;
				if (invCommodityDtos != null && !invCommodityDtos.isEmpty()) {

					// Commodities should always be specified for seeded grain but in case it changes in the future
					if (dtoSeeded.getCropCommodityId() == null) {
						filteredCommodityDto = invCommodityDtos.stream().filter(x -> x.getCropCommodityId() == null 
										&& x.getIsPedigreeInd().equals(dtoSeeded.getIsPedigreeInd()))
								.collect(Collectors.toList());
					} else {
						filteredCommodityDto = invCommodityDtos.stream()
								.filter(x -> x.getCropCommodityId() != null
										&& x.getCropCommodityId().equals(dtoSeeded.getCropCommodityId())
										&& x.getIsPedigreeInd().equals(dtoSeeded.getIsPedigreeInd()))
								.collect(Collectors.toList());
					}
				}
			
				if (filteredCommodityDto == null || filteredCommodityDto.isEmpty()) {
					if(operation.equals(addToTotal)) {
						// Insert new record if needs to be added total
						logger.debug("Contract Commodity Insert (Seeded Grain): " + dtoSeeded.getCropCommodityId());
						
						insertInventoryContractCommodity(userId, dtoInvContractList, invCommodityDtos, dtoSeeded.getCropCommodityId(),
								dtoSeeded.getTotalSeededAcres(), dtoSeeded.getTotalSpotLossAcres(), (double)0, dtoSeeded.getIsPedigreeInd());

					}
				} else {
					logger.debug("Contract Commodity Update (Seeded Grain): " + dtoSeeded.getCropCommodityId());
					// Update existing record
					InventoryContractCommodityDto commodityDto = filteredCommodityDto.get(0);
					
					Double currentTotalSeededAcres = notNull(commodityDto.getTotalSeededAcres(), (double)0);
					Double currentTotalSpotLossAcres = notNull(commodityDto.getTotalSpotLossAcres(), (double)0);
					Double newSeededAcres = notNull(dtoSeeded.getTotalSeededAcres(), (double)0);
					Double newSpotLossAcres = notNull(dtoSeeded.getTotalSpotLossAcres(), (double)0);
					
					if(operation.equals(addToTotal)) {
						Double newTotalSeededAcres = currentTotalSeededAcres + newSeededAcres;
						Double newTotalSpotLossAcres = currentTotalSpotLossAcres + newSpotLossAcres;
						commodityDto.setTotalSeededAcres(newTotalSeededAcres);
						commodityDto.setTotalSpotLossAcres(newTotalSpotLossAcres);
						inventoryContractCommodityDao.update(commodityDto, userId);
					} else {
						//Existing values
						Double currentTotalUnseededAcres = notNull(commodityDto.getTotalUnseededAcres(), (double)0);
						Double currentTotalUnseededOverrideAcres = notNull(commodityDto.getTotalUnseededAcresOverride(), (double)0);
						//New values
						Double newTotalSeededAcres =  (double)Math.max(0, currentTotalSeededAcres - newSeededAcres); //returns 0 or higher
						Double newTotalSpotLossAcres = (double)Math.max(0, currentTotalSpotLossAcres - newSpotLossAcres); //returns 0 or higher

						//Only delete if all values are 0
						if(currentTotalUnseededAcres == 0 
								&& currentTotalUnseededOverrideAcres == 0 
								&& newTotalSeededAcres == 0
								&& newTotalSpotLossAcres == 0) {
							//Last of the commodity has been removed from contract, delete inventory contract commodity record
							inventoryContractCommodityDao.delete(commodityDto.getInventoryContractCommodityGuid());
							invCommodityDtos.remove(commodityDto);
						} else {
							//Subtract from total or delete commodity total
							commodityDto.setTotalSeededAcres(newTotalSeededAcres);
							commodityDto.setTotalSpotLossAcres(newTotalSpotLossAcres);
							inventoryContractCommodityDao.update(commodityDto, userId);
						}
					}
				}
			}			
		}
		
		logger.debug(">updateSeededGrainInventoryCommodity");

	}

	private void insertInventoryContractCommodity(String userId, InventoryContractDto dtoInvContractList,
			List<InventoryContractCommodityDto> invCommodityDtos, Integer cropCommodityId, Double totalSeededAcres,
			Double totalSpotLossAcres, Double totalUnseededAcres, Boolean isPedigreeInd) throws DaoException {

		logger.debug("<insertInventoryContractCommodity");

		InventoryContractCommodityDto iccDto = new InventoryContractCommodityDto();
		
		iccDto.setInventoryContractCommodityGuid(null);
		iccDto.setInventoryContractGuid(dtoInvContractList.getInventoryContractGuid());
		iccDto.setCropCommodityId(cropCommodityId);
		iccDto.setTotalUnseededAcres(totalUnseededAcres);
		iccDto.setTotalSeededAcres(totalSeededAcres);
		iccDto.setTotalSpotLossAcres(totalSpotLossAcres);
		iccDto.setIsPedigreeInd(isPedigreeInd);

		inventoryContractCommodityDao.insert(iccDto, userId);
		
		invCommodityDtos.add(iccDto);

		logger.debug(">insertInventoryContractCommodity");
}	
	
	private void updateUnseededInventoryCommodity(String operation, String userId,
			List<InventoryUnseededDto> dtoUnseededList, InventoryContractDto dtoInvContractList,
			List<InventoryContractCommodityDto> invCommodityDtos, Integer insurancePlanId) throws DaoException, NotFoundDaoException {

		logger.debug("<updateUnseededInventoryCommodity");
		
		if(dtoUnseededList != null && dtoUnseededList.size() > 0) {
			
			for(InventoryUnseededDto dtoUnseeded : dtoUnseededList) {
				
				//Only rollup to contract level if the commodity is of the same plan or empty
				if(dtoUnseeded.getCropInsurancePlanId() == null || dtoUnseeded.getCropInsurancePlanId().equals(insurancePlanId)) {
				
					List<InventoryContractCommodityDto> filteredCommodityDto = null;
					if (invCommodityDtos != null && !invCommodityDtos.isEmpty()) {
	
						// It's possible that commodities are not specified (pedigree is always false for unseeded)
						if (dtoUnseeded.getCropCommodityId() == null) {
							filteredCommodityDto = invCommodityDtos.stream().filter(x -> x.getCropCommodityId() == null
												&& x.getIsPedigreeInd() == false)
									.collect(Collectors.toList());
						} else {
							filteredCommodityDto = invCommodityDtos.stream()
									.filter(x -> x.getCropCommodityId() != null
											&& x.getCropCommodityId().equals(dtoUnseeded.getCropCommodityId())
											&& x.getIsPedigreeInd() == false)
									.collect(Collectors.toList());
						}
					}
				
					if (filteredCommodityDto == null || filteredCommodityDto.isEmpty()) {
						if(operation.equals(addToTotal)) {
							// Insert new record if needs to be added total
							logger.debug("Contract Commodity Insert: (Unseeded) " + dtoUnseeded.getCropCommodityId());
	
							insertInventoryContractCommodity(userId, dtoInvContractList, invCommodityDtos, dtoUnseeded.getCropCommodityId(),
									(double)0, (double)0, dtoUnseeded.getTotalAcresToBeSeeded(), false);
	
						}
					} else {
						logger.debug("Contract Commodity Update: " + dtoUnseeded.getCropCommodityId());
						// Update existing record
						InventoryContractCommodityDto commodityDto = filteredCommodityDto.get(0);
						
						Double currentTotalAcres = notNull(commodityDto.getTotalUnseededAcres(), (double)0);
						Double newAcres = notNull(dtoUnseeded.getTotalAcresToBeSeeded(), (double)0);
						
						if(operation.equals(addToTotal)) {
							Double newTotalUnseededAcres = currentTotalAcres + newAcres;
							commodityDto.setTotalUnseededAcres(newTotalUnseededAcres);
							inventoryContractCommodityDao.update(commodityDto, userId);
						} else {
							
							//Existing values
							Double currentTotalSeededAcres = notNull(commodityDto.getTotalSeededAcres(), (double)0);
							Double currentTotalSpotLossAcres = notNull(commodityDto.getTotalSpotLossAcres(), (double)0);
							Double currentTotalUnseededOverrideAcres = notNull(commodityDto.getTotalUnseededAcresOverride(), (double)0);
							//New value
							Double newTotalUnseededAcres = (double)Math.max(0, currentTotalAcres - newAcres);  //returns 0 or higher
							
							//Only delete if all values are 0
							if(currentTotalSeededAcres == 0 
									&& currentTotalSpotLossAcres == 0 
									&& currentTotalUnseededOverrideAcres == 0
									&& newTotalUnseededAcres == 0) {
							
								//Last of the commodity has been removed from contract, delete inventory contract commodity record
								inventoryContractCommodityDao.delete(commodityDto.getInventoryContractCommodityGuid());
								invCommodityDtos.remove(commodityDto);
							} else {
								//Subtract from total or delete commodity total
								commodityDto.setTotalUnseededAcres(newTotalUnseededAcres);
								inventoryContractCommodityDao.update(commodityDto, userId);
							}
						}
					}
				}
			}			
		}
		
		logger.debug(">updateUnseededInventoryCommodity");

	}

	private void recalculateInventoryCoverageTotalForages(ContractedFieldDetailDto origCfdDto, Integer gcyId, String operation, String userId, WebAdeAuthentication authentication) throws DaoException {

		InventoryContractDto icDto = inventoryContractDao.getByGrowerContract(gcyId);
		
		if ( icDto != null && icDto.getInsurancePlanName().equals(InventoryServiceEnums.InsurancePlans.FORAGE.toString()) ) {
	
			ContractedFieldDetailDto cfdDto = origCfdDto.copy(); // So the original is not modified.

			List<InventoryFieldDto> plantings = inventoryFieldDao.select(cfdDto.getFieldId(), cfdDto.getCropYear(), icDto.getInsurancePlanId());
			cfdDto.setPlantings(plantings);
			
			for (InventoryFieldDto ifDto : cfdDto.getPlantings()) {
				List<InventorySeededForageDto> inventorySeededForages = inventorySeededForageDao.select(ifDto.getInventoryFieldGuid());
				ifDto.setInventorySeededForages(inventorySeededForages);				
			}

			AnnualField field = inventoryContractRsrcFactory.createAnnualField(cfdDto, authentication);

			List<AnnualField> fields = new ArrayList<AnnualField>();			
			fields.add(field);
			
			InventoryCalculationType calcType = null;
			
			if ( operation.equals(subtractFromTotal) ) {
				calcType = InventoryCalculationType.IncrementalSubtract;
			} else {
				calcType = InventoryCalculationType.IncrementalAdd;
			}
			
			underwritingServiceHelper.updateInventoryCoverageTotalForages(fields, icDto.getInventoryContractGuid(), userId, calcType);
		}
		
	}
	
	private void updateContractedFieldDetail(ContractedFieldDetail model, ContractedFieldDetailDto dto, 
			FactoryContext factoryContext, WebAdeAuthentication authentication) {
	
		logger.debug("<updateContractedFieldDetail");
		
		try {
		
			String userId = getUserId(authentication);
			
			landDataSyncRsrcFactory.updateContractedFieldDetail(dto, model);
			contractedFieldDetailDao.updateSync(dto, userId);
			
		} catch (DaoException e) {
			e.printStackTrace();
			throw new ServiceException("DAO threw an exception: " + e.getMessage(), e);
		}
		
		logger.debug(">updateContractedFieldDetail");
	
	}

	private void createContractedFieldDetail(ContractedFieldDetail model, FactoryContext factoryContext,
			WebAdeAuthentication authentication) {
	
		logger.debug("<createContractedFieldDetail");
		
		try {
		
			String userId = getUserId(authentication);
			
			ContractedFieldDetailDto dto = new ContractedFieldDetailDto();
			
			landDataSyncRsrcFactory.updateContractedFieldDetail(dto, model);
			contractedFieldDetailDao.insertDataSync(dto, userId);

			//Insert inventory field if necessary
			insertInventoryField(model, userId);
		
		} catch (DaoException e) {
			e.printStackTrace();
			throw new ServiceException("DAO threw an exception: " + e.getMessage(), e);
		}
		
		logger.debug(">createContractedFieldDetail");
	
	}
	
	private void insertInventoryField(ContractedFieldDetail model, String userId) 
			  throws DaoException {
		
		//Check if there is an inventory contract record for the policy and add a inventory field record if there is.
		GrowerContractYearDto gcyDto = growerContractYearDao.selectInventoryContractForGcy(model.getGrowerContractYearId());
		
		if(gcyDto != null && gcyDto.getInventoryContractGuid() != null) {

			//Get annual field record to get the field id
			AnnualFieldDetailDto afdDto = annualFieldDetailDao.fetch(model.getAnnualFieldDetailId());
			
			if(afdDto != null) {
				
				List<InventoryFieldDto> plantings = inventoryFieldDao.select(afdDto.getFieldId(), gcyDto.getCropYear(), gcyDto.getInsurancePlanId());
				
				//Only insert new inventory field record if there is none
				if(plantings == null || plantings.size() == 0) {
					InventoryFieldDto dto = new InventoryFieldDto();
					
					dto.setCropYear(gcyDto.getCropYear());
					dto.setFieldId(afdDto.getFieldId());
					dto.setInsurancePlanId(gcyDto.getInsurancePlanId());
					dto.setLastYearCropCommodityId(null);
					dto.setLastYearCropCommodityName(null);
					dto.setLastYearCropVarietyId(null);
					dto.setLastYearCropVarietyName(null);
					dto.setPlantingNumber(1);
					dto.setIsHiddenOnPrintoutInd(false);
					dto.setInventoryFieldGuid(null);
					dto.setUnderseededCropVarietyId(null);
					dto.setUnderseededAcres(null);
					dto.setUnderseededInventorySeededForageGuid(null);

					inventoryFieldDao.insert(dto, userId);

					// TODO: Only create unseeded for GRAIN.
					insertInventoryUnseeded(dto.getInventoryFieldGuid(), userId);
					
					// TODO: Create InventorySeededForage?
				}
			}
		}
	}
	
	private String insertInventoryUnseeded(String inventoryFieldGuid, String userId) 
			  throws DaoException {

				InventoryUnseededDto dto = new InventoryUnseededDto();

				dto.setAcresToBeSeeded(null);
				dto.setCropCommodityId(null);
				dto.setCropCommodityName(null);
				dto.setCropVarietyId(null);
				dto.setCropVarietyName(null);
				dto.setIsUnseededInsurableInd(false);
				dto.setInventoryUnseededGuid(null);
				dto.setInventoryFieldGuid(inventoryFieldGuid);

				inventoryUnseededDao.insert(dto, userId);

				return dto.getInventoryUnseededGuid();
			}


	@Override
	public void deleteContractedFieldDetail(Integer contractedFieldDetailId,
		FactoryContext factoryContext, WebAdeAuthentication authentication)
		throws ServiceException, NotFoundException, DaoException {
		logger.debug("<deleteContractedFieldDetail");
		
		// If there is no record to delete dao is designed to throw an error
		ContractedFieldDetailDto dto = contractedFieldDetailDao.fetchSimple(contractedFieldDetailId);
		
		if (dto != null) {
			contractedFieldDetailDao.delete(contractedFieldDetailId);
		}

		
		logger.debug(">deleteContractedFieldDetail");
	
	}

	private Double notNull(Double value, Double defaultValue) {
		return (value == null) ? defaultValue : value;
	}

	
}
