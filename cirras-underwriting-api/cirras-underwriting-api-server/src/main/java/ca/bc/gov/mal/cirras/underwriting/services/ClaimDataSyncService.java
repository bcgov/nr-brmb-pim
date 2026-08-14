package ca.bc.gov.mal.cirras.underwriting.services;

import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import ca.bc.gov.mal.cirras.underwriting.data.resources.ClaimSyncEventTypes;
import ca.bc.gov.mal.cirras.underwriting.data.resources.SyncClaimCalculationSimpleRsrc;
import ca.bc.gov.mal.cirras.underwriting.data.entities.ClaimCalculationBerriesSyncDto;
import ca.bc.gov.mal.cirras.underwriting.data.assemblers.SyncClaimCalculationSimpleRsrcFactory;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.service.api.NotFoundException;
import ca.bc.gov.nrs.wfone.common.service.api.ServiceException;
import ca.bc.gov.nrs.wfone.common.service.api.model.factory.FactoryContext;
import ca.bc.gov.nrs.wfone.common.webade.authentication.WebAdeAuthentication;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.ClaimCalculationBerriesSyncDao;

public class ClaimDataSyncService {

	private static final Logger logger = LoggerFactory.getLogger(ClaimDataSyncService.class);

	private Properties applicationProperties;

	// factories
	private SyncClaimCalculationSimpleRsrcFactory syncClaimCalculationSimpleRsrcFactory;

	// daos
	private ClaimCalculationBerriesSyncDao claimCalculationBerriesSyncDao;

	// utils
	//private CirrasServiceHelper cirrasServiceHelper;

	public static final String MaximumResultsProperty = "maximum.results";

	public static final int DefaultMaximumResults = 800;

	public void setApplicationProperties(Properties applicationProperties) {
		this.applicationProperties = applicationProperties;
	}

	public void setSyncClaimCalculationSimpleRsrcFactory(SyncClaimCalculationSimpleRsrcFactory syncClaimCalculationSimpleRsrcFactory) {
		this.syncClaimCalculationSimpleRsrcFactory = syncClaimCalculationSimpleRsrcFactory;
	}

	public void setClaimCalculationBerriesSyncDao(ClaimCalculationBerriesSyncDao claimCalculationBerriesSyncDao) {
		this.claimCalculationBerriesSyncDao = claimCalculationBerriesSyncDao;
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

	
	@Transactional(readOnly = true, rollbackFor = Exception.class)
	public SyncClaimCalculationSimpleRsrc getSyncClaimCalculationSimple(
			String claimCalculationBerriesGuid, 
			FactoryContext factoryContext, 
			WebAdeAuthentication authentication)
			throws ServiceException, NotFoundException, DaoException {

		logger.debug("<getSyncClaimCalculationSimple");

		SyncClaimCalculationSimpleRsrc result = null;

		try {
			ClaimCalculationBerriesSyncDto dto = claimCalculationBerriesSyncDao.fetch(claimCalculationBerriesGuid);

			if (dto != null) {
				result = syncClaimCalculationSimpleRsrcFactory.getSyncClaimCalculationSimple(dto);
			} else {
				// No record found
				throw new NotFoundException("Did not find claim calculation berries sync with claimCalculationBerriesGuid: " + claimCalculationBerriesGuid);
			}
	
		} catch (DaoException e) {
			e.printStackTrace();
			throw new ServiceException("DAO threw an exception: " + e.getMessage(), e);
		}

		logger.debug(">getSyncClaimCalculationSimple");
		return result;
	}

	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public void synchronizeSyncClaimCalculationSimple(SyncClaimCalculationSimpleRsrc resource, FactoryContext factoryContext,
			WebAdeAuthentication authentication) throws ServiceException, NotFoundException, DaoException {

		logger.debug("<synchronizeSyncClaimCalculationSimple");

		if (resource.getTransactionType().equalsIgnoreCase(ClaimSyncEventTypes.ClaimCalculationBerriesDeleted)) {
			// DELETE
			if ( resource.getSyncClaimCalculationBerries() != null ) {
				deleteClaimCalculationBerriesSync(resource.getSyncClaimCalculationBerries().getClaimCalculationBerriesGuid(), factoryContext, authentication);
			} else {
				throw new ServiceException("Missing subtype: SyncClaimCalculationBerries");
			}
		} else if (resource.getTransactionType().equalsIgnoreCase(ClaimSyncEventTypes.ClaimCalculationBerriesCreated)
				|| resource.getTransactionType().equalsIgnoreCase(ClaimSyncEventTypes.ClaimCalculationBerriesUpdated)) {
			// INSERT OR UPDATE
			
			if ( resource.getSyncClaimCalculationBerries() != null ) {
				
				//Check if record already exist and call the correct method
				ClaimCalculationBerriesSyncDto dto = claimCalculationBerriesSyncDao.fetch(resource.getSyncClaimCalculationBerries().getClaimCalculationBerriesGuid());
	
				if (dto == null) {
					createClaimCalculationBerriesSync(resource, factoryContext, authentication);
				} else {
					updateClaimCalculationBerriesSync(resource, dto, factoryContext, authentication);
				}
			} else {
				throw new ServiceException("Missing subtype: SyncClaimCalculationBerries");
			}

		}

		logger.debug(">synchronizeSyncClaimCalculationSimple");

	}

	private void updateClaimCalculationBerriesSync(SyncClaimCalculationSimpleRsrc resource, ClaimCalculationBerriesSyncDto dto, 
			FactoryContext factoryContext, WebAdeAuthentication authentication) {

		logger.debug("<updateClaimCalculationBerriesSync");

		try {

			String userId = getUserId(authentication);

			syncClaimCalculationSimpleRsrcFactory.updateSyncClaimCalculationSimple(dto, resource);
			claimCalculationBerriesSyncDao.update(dto, userId);

		} catch (DaoException e) {
			e.printStackTrace();
			throw new ServiceException("DAO threw an exception: " + e.getMessage(), e);
		}

		logger.debug(">updateClaimCalculationBerriesSync");

	}

	private void createClaimCalculationBerriesSync(SyncClaimCalculationSimpleRsrc resource, FactoryContext factoryContext,
			WebAdeAuthentication authentication) {

		logger.debug("<createClaimCalculationBerriesSync");

		try {

			String userId = getUserId(authentication);
			
			ClaimCalculationBerriesSyncDto dto = new ClaimCalculationBerriesSyncDto();

			syncClaimCalculationSimpleRsrcFactory.updateSyncClaimCalculationSimple(dto, resource);
			claimCalculationBerriesSyncDao.insert(dto, userId);

		} catch (DaoException e) {
			e.printStackTrace();
			throw new ServiceException("DAO threw an exception: " + e.getMessage(), e);
		}

		logger.debug(">createClaimCalculationBerriesSync");

	}

	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public void deleteSyncClaimCalculationSimple(String claimCalculationBerriesGuid, FactoryContext factoryContext,
			WebAdeAuthentication authentication) throws ServiceException, NotFoundException {

		logger.debug("<deleteSyncClaimCalculationSimple");

		deleteClaimCalculationBerriesSync(claimCalculationBerriesGuid, factoryContext, authentication);

		logger.debug(">deleteSyncClaimCalculationSimple");

	}
	
	private void deleteClaimCalculationBerriesSync(String claimCalculationBerriesGuid, 
			FactoryContext factoryContext, WebAdeAuthentication webAdeAuthentication) {

		logger.debug("<deleteClaimCalculationBerriesSync");

		try {

			claimCalculationBerriesSyncDao.delete(claimCalculationBerriesGuid);

		} catch (DaoException e) {
			e.printStackTrace();
			throw new ServiceException("DAO threw an exception: " + e.getMessage(), e);
		}

		logger.debug(">deleteClaimCalculationBerriesSync");

	}

	public Integer toInteger(String value) {
		Integer result = null;
		if(value!=null&&value.trim().length()>0) {
			result = Integer.valueOf(value);
		}
		return result;
	}


}
