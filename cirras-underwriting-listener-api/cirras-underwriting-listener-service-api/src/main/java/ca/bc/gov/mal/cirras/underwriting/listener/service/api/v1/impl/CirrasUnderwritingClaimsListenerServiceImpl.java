package ca.bc.gov.mal.cirras.underwriting.listener.service.api.v1.impl;

import java.time.Instant;
import java.util.Date;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import ca.bc.gov.mal.cirras.underwriting.clients.CirrasUnderwritingServiceException;
import ca.bc.gov.mal.cirras.underwriting.clients.ValidationException;
import ca.bc.gov.mal.cirras.underwriting.data.resources.SyncClaimCalculationSimpleRsrc;
import ca.bc.gov.mal.cirras.claims.data.resources.ClaimCalculationSimpleRsrc;
import ca.bc.gov.mal.cirras.claims.data.resources.ClaimEventTypes;
import ca.bc.gov.mal.cirras.underwriting.clients.CirrasUnderwritingService;
import ca.bc.gov.mal.cirras.underwriting.listener.service.api.v1.CirrasUnderwritingClaimsListenerService;
import ca.bc.gov.mal.cirras.underwriting.listener.service.api.v1.model.factory.SyncUwClaimsFactory;
import ca.bc.gov.nrs.wfone.common.service.api.model.factory.FactoryContext;

public class CirrasUnderwritingClaimsListenerServiceImpl implements CirrasUnderwritingClaimsListenerService {

	private static final Logger logger = LoggerFactory.getLogger(CirrasUnderwritingClaimsListenerServiceImpl.class);

	private static ObjectMapper mapper = new ObjectMapper();

	// services
	private CirrasUnderwritingService cirrasUnderwritingService;

	// factories
	private SyncUwClaimsFactory syncUwClaimsFactory;

	private Date eventDate;
	private String eventType;

	public void setCirrasUnderwritingService(CirrasUnderwritingService cirrasUnderwritingService) {
		this.cirrasUnderwritingService = cirrasUnderwritingService;
	}
	
	public void setSyncUwClaimsFactory(SyncUwClaimsFactory syncUwClaimsFactory) {
		this.syncUwClaimsFactory = syncUwClaimsFactory;
	}

	public void processCirrasUnderwritingClaimsEvent(String eventMessageString, FactoryContext factoryContext) throws Throwable {
		logger.debug("<processCirrasUnderwritingClaimsEvent");

		try {
			Map<String, Object> claimsEventObjMap = null;
			try {
				claimsEventObjMap = mapper.readValue(eventMessageString, new TypeReference<Map<String, Object>>() {
				});
			} catch (Throwable t) {
				logger.error("When attempting to unmarshal " + eventMessageString + ", saw " + t.getMessage(), t);
				throw (t);
			}

			eventType = (String) claimsEventObjMap.get("eventType");
			Instant eventTimestamp = Instant.parse((String) claimsEventObjMap.get("eventTimestamp"));
			eventDate = Date.from(eventTimestamp);

			Map<String, String> sourceIdentifiers = (Map<String, String>) claimsEventObjMap.get("sourceIdentifiers");

			switch (eventType) {
			case ClaimEventTypes.ClaimCalculationBerriesCreated:
			case ClaimEventTypes.ClaimCalculationBerriesUpdated:
				ClaimCalculationSimpleRsrc claimCalculationSimpleRsrc = loadClaimCalculationSimpleResource(claimsEventObjMap);
				synchronizeClaimCalculationSimple(claimCalculationSimpleRsrc);
				break;
			case ClaimEventTypes.ClaimCalculationBerriesDeleted:
				String claimCalculationBerriesGuid = sourceIdentifiers.get("claimCalculationBerriesGuid");
				deleteClaimCalculationSimple(claimCalculationBerriesGuid);
				break;
			default:
				logger.info("Ignoring message of type " + eventType);
				break;
			}

		} catch (Throwable t) {
			logger.error(t.getMessage(), t);
			throw (t);
		}
		logger.debug(">processCirrasUnderwritingClaimsEvent");
	}

	private ClaimCalculationSimpleRsrc loadClaimCalculationSimpleResource(Map<String, Object> claimsEventObjMap)
			throws JsonProcessingException {

		return mapper.readValue(loadResourceAfterUpdateFromEventObject(claimsEventObjMap), ClaimCalculationSimpleRsrc.class);
	}	
	

	private String loadResourceAfterUpdateFromEventObject(Map<String, Object> claimsEventObjMap) throws JsonProcessingException {
		@SuppressWarnings("unchecked")
		Map<String, Object> resourceAfterUpdate = (Map<String, Object>) claimsEventObjMap.get("resourceAfterUpdate");
		String resourceAfterUpdateJson = mapper.writeValueAsString(resourceAfterUpdate);
		return resourceAfterUpdateJson;
	}

	private void synchronizeClaimCalculationSimple(ClaimCalculationSimpleRsrc claimCalculationSimpleRsrc)
			throws CirrasUnderwritingServiceException, ValidationException {
		
		SyncClaimCalculationSimpleRsrc resource = syncUwClaimsFactory.getSyncClaimCalculationSimpleRsrc(claimCalculationSimpleRsrc, eventDate, eventType);
		
		cirrasUnderwritingService.synchronizeClaimCalculationSimple(resource);
	}
	
	private void deleteClaimCalculationSimple(String claimCalculationBerriesGuid) throws CirrasUnderwritingServiceException, ValidationException {

		SyncClaimCalculationSimpleRsrc resource = syncUwClaimsFactory.getDeleteSyncClaimCalculationSimpleRsrc(claimCalculationBerriesGuid, eventDate, eventType);
		
		cirrasUnderwritingService.synchronizeClaimCalculationSimple(resource);
		
	}

}