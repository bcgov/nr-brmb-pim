package ca.bc.gov.mal.cirras.underwriting.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import ca.bc.gov.mal.cirras.underwriting.data.resources.DopYieldContractSimpleRsrc;
import ca.bc.gov.mal.cirras.underwriting.data.resources.UnderwritingEventTypes;
import ca.bc.gov.mal.cirras.underwriting.data.models.DopYieldContractCommodityBerriesOutbox;
import ca.bc.gov.mal.cirras.underwriting.data.models.OutboxTransactionTypes;
import ca.bc.gov.mal.cirras.underwriting.data.entities.DeclaredYieldContractCommodityBerriesDto;
import ca.bc.gov.mal.cirras.underwriting.data.entities.DeclaredYieldContractCommodityBerriesOutboxDto;
import ca.bc.gov.mal.cirras.underwriting.controllers.publisher.EventPublisher;
import ca.bc.gov.mal.cirras.underwriting.controllers.publisher.EventPublisherException;
import ca.bc.gov.mal.cirras.underwriting.data.assemblers.DopYieldContractSimpleRsrcFactory;
import ca.bc.gov.mal.cirras.underwriting.data.assemblers.OutboxFactory;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.DeclaredYieldContractCommodityBerriesDao;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.DeclaredYieldContractCommodityBerriesOutboxDao;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.service.api.NotFoundException;
import ca.bc.gov.nrs.wfone.common.service.api.ServiceException;
import ca.bc.gov.nrs.wfone.common.webade.authentication.WebAdeAuthentication;

public class CirrasUnderwritingOutboxService {

	private static final Logger logger = LoggerFactory.getLogger(CirrasUnderwritingOutboxService.class);

	private Properties applicationProperties;

	// factories
	private OutboxFactory outboxFactory;
	private DopYieldContractSimpleRsrcFactory dopYieldContractSimpleRsrcFactory;

	// daos
	private DeclaredYieldContractCommodityBerriesOutboxDao declaredYieldContractCommodityBerriesOutboxDao;
	private DeclaredYieldContractCommodityBerriesDao declaredYieldContractCommodityBerriesDao;

	private EventPublisher eventPublisher;

	public void setApplicationProperties(Properties applicationProperties) {
		this.applicationProperties = applicationProperties;
	}

	public void setOutboxFactory(OutboxFactory outboxFactory) {
		this.outboxFactory = outboxFactory;
	}

	public void setDopYieldContractSimpleRsrcFactory(DopYieldContractSimpleRsrcFactory dopYieldContractSimpleRsrcFactory) {
		this.dopYieldContractSimpleRsrcFactory = dopYieldContractSimpleRsrcFactory;
	}

	public void setDeclaredYieldContractCommodityBerriesOutboxDao(DeclaredYieldContractCommodityBerriesOutboxDao declaredYieldContractCommodityBerriesOutboxDao) {
		this.declaredYieldContractCommodityBerriesOutboxDao = declaredYieldContractCommodityBerriesOutboxDao;
	}

	public void setDeclaredYieldContractCommodityBerriesDao(DeclaredYieldContractCommodityBerriesDao declaredYieldContractCommodityBerriesDao) {
		this.declaredYieldContractCommodityBerriesDao = declaredYieldContractCommodityBerriesDao;
	}
	
	public void setEventPublisher(EventPublisher eventPublisher) {
		this.eventPublisher = eventPublisher;
	}

	@Transactional(readOnly = true, rollbackFor = Exception.class)
	public List<DopYieldContractCommodityBerriesOutbox> getNextDopYieldContractCommodityBerriesOutboxes(
		Integer maxRecords, 
		WebAdeAuthentication authentication
	) throws ServiceException
	{
		logger.debug("<getNextDopYieldContractCommodityBerriesOutboxes");

		List<DopYieldContractCommodityBerriesOutbox> results = null;

		try {
			List<DeclaredYieldContractCommodityBerriesOutboxDto> dtos = declaredYieldContractCommodityBerriesOutboxDao.select(maxRecords);
			results = outboxFactory.getDopYieldContractCommodityBerriesOutboxList(dtos);
		} catch (DaoException e) {
			throw new ServiceException("DAO threw an exception", e);
		}

		logger.debug(">getNextDopYieldContractCommodityBerriesOutboxes");
		
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
		logger.debug("<processDopYieldContractCommodityBerriesOutbox");

		try {

			if ( doPublishEvent.booleanValue() ) { 
				String eventType = null;
				DopYieldContractSimpleRsrc beforeDopYieldContractSimpleRsrc = null;
				DopYieldContractSimpleRsrc afterDopYieldContractSimpleRsrc = null;
				Map<String, String> sourceIdentifiers = new HashMap<>();
					
				if ( dopYieldContractCommodityBerriesOutbox.getTransactionType().equals(OutboxTransactionTypes.Insert) ) {
					eventType = UnderwritingEventTypes.DopYieldContractCommodityBerriesCreated;
					afterDopYieldContractSimpleRsrc = getDopYieldContractCommdityBerries(dopYieldContractCommodityBerriesOutbox.getDeclaredYieldContractCommodityBerriesGuid());
					sourceIdentifiers.put("declaredYieldContractCommodityBerriesGuid", afterDopYieldContractSimpleRsrc.getDopYieldContractCommodityBerries().getDeclaredYieldContractCommodityBerriesGuid());
						
				} else if (dopYieldContractCommodityBerriesOutbox.getTransactionType().equals(OutboxTransactionTypes.Update) ) {
					eventType = UnderwritingEventTypes.DopYieldContractCommodityBerriesUpdated;
					afterDopYieldContractSimpleRsrc = getDopYieldContractCommdityBerries(dopYieldContractCommodityBerriesOutbox.getDeclaredYieldContractCommodityBerriesGuid());
					sourceIdentifiers.put("declaredYieldContractCommodityBerriesGuid", afterDopYieldContractSimpleRsrc.getDopYieldContractCommodityBerries().getDeclaredYieldContractCommodityBerriesGuid());
						
				} else if (dopYieldContractCommodityBerriesOutbox.getTransactionType().equals(OutboxTransactionTypes.Delete) ) {
					eventType = UnderwritingEventTypes.DopYieldContractCommodityBerriesDeleted;

					// Since the delete has already happened, no resource is included in the event.
					sourceIdentifiers.put("declaredYieldContractCommodityBerriesGuid", dopYieldContractCommodityBerriesOutbox.getDeclaredYieldContractCommodityBerriesGuid());
						
				} else { 
					throw new ServiceException("Crop Type Outbox returned invalid transaction type");
				}

				// Delete Crop Type Outbox before publishing event. If the publish fails, the exception 
				// rolls back the delete.
				declaredYieldContractCommodityBerriesOutboxDao.delete(dopYieldContractCommodityBerriesOutbox.getDeclaredYieldContractCommodityBerriesOutboxId());
				eventPublisher.publish(eventType, beforeDopYieldContractSimpleRsrc, afterDopYieldContractSimpleRsrc, sourceIdentifiers);
			} else {
				// Not publishing an event because it would be a duplicate, so just delete the outbox record.
				declaredYieldContractCommodityBerriesOutboxDao.delete(dopYieldContractCommodityBerriesOutbox.getDeclaredYieldContractCommodityBerriesOutboxId());
			}

		} catch (NotFoundException e) {
			// If cropId does not exist, then there must be a delete event that will be processed later.
			// So we can ignore this insert/update event and just delete the outbox record.
			logger.info("Skipped insert/update event for declaredYieldContractCommodityBerriesGuid " + dopYieldContractCommodityBerriesOutbox.getDeclaredYieldContractCommodityBerriesOutboxId() + " as it no longer exists.");
			try { 
				declaredYieldContractCommodityBerriesOutboxDao.delete(dopYieldContractCommodityBerriesOutbox.getDeclaredYieldContractCommodityBerriesOutboxId());
			} catch (DaoException e2) { 
				throw new ServiceException("DAO threw an exception", e2);
			}

		} catch (DaoException e) {
			throw new ServiceException("DAO threw an exception", e);
		} catch (EventPublisherException e) {
			throw new ServiceException("Event Publisher threw an exception", e);
		}
		
		logger.debug(">processDopYieldContractCommodityBerriesOutbox");
	}
	
	private DopYieldContractSimpleRsrc getDopYieldContractCommdityBerries(String declaredYieldContractCommodityBerriesGuid) throws ServiceException, NotFoundException {
		logger.debug("<getDopYieldContractCommdityBerries");
			
		DopYieldContractSimpleRsrc result = null;

		try {
			DeclaredYieldContractCommodityBerriesDto berriesDto = declaredYieldContractCommodityBerriesDao.fetch(declaredYieldContractCommodityBerriesGuid);
				
			if(berriesDto == null) {
				throw new NotFoundException("no declared yield contract commodity berries record found for " + declaredYieldContractCommodityBerriesGuid);
			}
				
			result = dopYieldContractSimpleRsrcFactory.getDopYieldContractSimple(berriesDto);
		} catch (DaoException e) {
			throw new ServiceException("DAO threw an exception", e);
		}
			
		logger.debug(">getDopYieldContractCommdityBerries");
		return result;
	}



}
