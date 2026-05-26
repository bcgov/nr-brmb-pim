package ca.bc.gov.mal.cirras.underwriting.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import ca.bc.gov.mal.cirras.underwriting.data.resources.ContactEmailRsrc;
import ca.bc.gov.mal.cirras.underwriting.data.resources.ContactPhoneRsrc;
import ca.bc.gov.mal.cirras.underwriting.data.resources.ContactRsrc;
import ca.bc.gov.mal.cirras.underwriting.data.resources.GrowerContactRsrc;
import ca.bc.gov.mal.cirras.underwriting.data.resources.GrowerRsrc;
import ca.bc.gov.mal.cirras.underwriting.data.resources.PolicyRsrc;
import ca.bc.gov.mal.cirras.underwriting.data.resources.ProductRsrc;
import ca.bc.gov.mal.cirras.underwriting.data.resources.SyncCodeRsrc;
import ca.bc.gov.mal.cirras.underwriting.data.resources.SyncCommodityTypeCodeRsrc;
import ca.bc.gov.mal.cirras.underwriting.data.resources.SyncCommodityTypeVarietyXrefRsrc;
import ca.bc.gov.mal.cirras.underwriting.data.resources.SyncCommodityVarietyRsrc;
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
import ca.bc.gov.mal.cirras.underwriting.data.models.DopYieldContractCommodityBerriesOutbox;
import ca.bc.gov.mal.cirras.underwriting.data.entities.CommodityTypeCodeDto;
import ca.bc.gov.mal.cirras.underwriting.data.entities.CommodityTypeVarietyXrefDto;
import ca.bc.gov.mal.cirras.underwriting.data.entities.ContactDto;
import ca.bc.gov.mal.cirras.underwriting.data.entities.ContactEmailDto;
import ca.bc.gov.mal.cirras.underwriting.data.entities.ContactPhoneDto;
import ca.bc.gov.mal.cirras.underwriting.data.entities.CropCommodityDto;
import ca.bc.gov.mal.cirras.underwriting.data.entities.CropVarietyDto;
import ca.bc.gov.mal.cirras.underwriting.data.entities.GrowerContactDto;
import ca.bc.gov.mal.cirras.underwriting.data.assemblers.CirrasDataSyncRsrcFactory;
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
import ca.bc.gov.mal.cirras.underwriting.data.resources.PoliciesSyncEventTypes;

public class CirrasUnderwritingOutboxService {

	private static final Logger logger = LoggerFactory.getLogger(CirrasUnderwritingOutboxService.class);

	private Properties applicationProperties;

	// factories
	private CirrasDataSyncRsrcFactory cirrasDataSyncRsrcFactory;

	// daos
	private PolicyStatusCodeDao policyStatusCodeDao;

	public void setApplicationProperties(Properties applicationProperties) {
		this.applicationProperties = applicationProperties;
	}

	public void setCirrasDataSyncRsrcFactory(CirrasDataSyncRsrcFactory cirrasDataSyncRsrcFactory) {
		this.cirrasDataSyncRsrcFactory = cirrasDataSyncRsrcFactory;
	}

	public void setPolicyStatusCodeDao(PolicyStatusCodeDao policyStatusCodeDao) {
		this.policyStatusCodeDao = policyStatusCodeDao;
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
	public List<DopYieldContractCommodityBerriesOutbox> getNextDopYieldContractCommodityBerriesOutboxes(
		Integer maxRecords, 
		WebAdeAuthentication authentication
	) throws ServiceException
	{
		logger.debug("<getNextCoveragePerilOutboxes");

		List<DopYieldContractCommodityBerriesOutbox> results = null;

		try {
			List<DopYieldContractCommodityBerriesOutboxDto> dtos = coveragePerilOutboxDao.select(maxRecords);
			results = outboxFactory.getCoveragePerilOutboxList(dtos);
		} catch (DaoException e) {
			throw new ServiceException("DAO threw an exception", e);
		}

		logger.debug(">getNextCoveragePerilOutboxes");
		
		return results;
	}
	
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public void processDopYieldContractCommodityBerriesOutbox(
			DopYieldContractCommodityBerriesOutbox dopYieldContractCommodityBerriesOutbox,
		Boolean doPublishEvent,
		WebAdeAuthentication authentication
	) 
	throws ServiceException
	{
		logger.debug("<processCoveragePerilOutbox");

		try {

			if ( doPublishEvent.booleanValue() ) { 
				String eventType = null;
				CoveragePerilSync beforeCoveragePerilSync = null;
				CoveragePerilSync afterCoveragePerilSync = null;
				Map<String, String> sourceIdentifiers = new HashMap<>();
					
				if ( dopYieldContractCommodityBerriesOutbox.getTransactionType().equals(OutboxTransactionTypes.Inserted) ) {
					eventType = PoliciesEventTypes.CoveragePerilCreated;
					afterCoveragePerilSync = getBaseCoveragePerilSyncById(dopYieldContractCommodityBerriesOutbox.getCoveragePerilId());

					sourceIdentifiers.put("coveragePerilId", afterCoveragePerilSync.getCoveragePerilId().toString());
						
				} else if (dopYieldContractCommodityBerriesOutbox.getTransactionType().equals(OutboxTransactionTypes.Updated) ) {
					eventType = PoliciesEventTypes.CoveragePerilUpdated;
					afterCoveragePerilSync = getBaseCoveragePerilSyncById(dopYieldContractCommodityBerriesOutbox.getCoveragePerilId());
					sourceIdentifiers.put("coveragePerilId", afterCoveragePerilSync.getCoveragePerilId().toString());
						
				} else if (dopYieldContractCommodityBerriesOutbox.getTransactionType().equals(OutboxTransactionTypes.Deleted) ) {
					eventType = PoliciesEventTypes.CoveragePerilDeleted;

					// Since the delete has already happened, no resource is included in the event.
					sourceIdentifiers.put("coveragePerilId", dopYieldContractCommodityBerriesOutbox.getCoveragePerilId().toString());
						
				} else { 
					throw new ServiceException("Crop Type Outbox returned invalid transaction type");
				}

				// Delete Crop Type Outbox before publishing event. If the publish fails, the exception 
				// rolls back the delete.
				coveragePerilOutboxDao.delete(dopYieldContractCommodityBerriesOutbox.getCoveragePerilOutboxId());
				eventPublisher.publish(eventType, beforeCoveragePerilSync, afterCoveragePerilSync, sourceIdentifiers);
			} else {
				// Not publishing an event because it would be a duplicate, so just delete the outbox record.
				coveragePerilOutboxDao.delete(dopYieldContractCommodityBerriesOutbox.getCoveragePerilOutboxId());
			}

		} catch (NotFoundException e) {
			// If cropId does not exist, then there must be a delete event that will be processed later.
			// So we can ignore this insert/update event and just delete the outbox record.
			logger.info("Skipped insert/update event for coveragePerilId " + coveragePerilOutbox.getCoveragePerilId() + " as it no longer exists.");
			try { 
				coveragePerilOutboxDao.delete(dopYieldContractCommodityBerriesOutbox.getCoveragePerilOutboxId());
			} catch (DaoException e2) { 
				throw new ServiceException("DAO threw an exception", e2);
			}

		} catch (DaoException e) {
			throw new ServiceException("DAO threw an exception", e);
		} catch (EventPublisherException e) {
			throw new ServiceException("Event Publisher threw an exception", e);
		}
		
		logger.debug(">processCoveragePerilOutbox");
	}



}
