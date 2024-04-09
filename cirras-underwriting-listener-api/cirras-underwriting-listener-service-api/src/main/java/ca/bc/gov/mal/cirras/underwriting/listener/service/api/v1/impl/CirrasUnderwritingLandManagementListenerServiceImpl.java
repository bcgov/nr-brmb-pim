package ca.bc.gov.mal.cirras.underwriting.listener.service.api.v1.impl;

import java.time.Instant;
import java.util.Date;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

//PIM-1156: Disabling all code related to cirras-land-management-api so it can be shutdown.

//import ca.bc.gov.mal.cirras.underwriting.api.rest.client.v1.CirrasUnderwritingServiceException;
//import ca.bc.gov.mal.cirras.underwriting.api.rest.client.v1.ValidationException;
//import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.AnnualFieldDetailRsrc;
//import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.ContractedFieldDetailRsrc;
//import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.FieldRsrc;
//import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.GrowerContractYearSyncRsrc;
//import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.LegalLandFieldXrefRsrc;
//import ca.bc.gov.mal.cirras.land.management.api.rest.v1.resource.AnnualLotDetailRsrc;
//import ca.bc.gov.mal.cirras.land.management.api.rest.v1.resource.ContractedLotDetailRsrc;
//import ca.bc.gov.mal.cirras.land.management.api.rest.v1.resource.GrowerContractYearRsrc;
//import ca.bc.gov.mal.cirras.land.management.api.rest.v1.resource.InsuredLandRsrc;
//import ca.bc.gov.mal.cirras.land.management.model.v1.LandManagementEventTypes;
import ca.bc.gov.mal.cirras.underwriting.api.rest.client.v1.CirrasUnderwritingService;
import ca.bc.gov.mal.cirras.underwriting.listener.service.api.v1.CirrasUnderwritingLandManagementListenerService;
import ca.bc.gov.mal.cirras.underwriting.listener.service.api.v1.model.factory.SyncUwLandManagementFactory;
import ca.bc.gov.nrs.wfone.common.service.api.model.factory.FactoryContext;

public class CirrasUnderwritingLandManagementListenerServiceImpl implements CirrasUnderwritingLandManagementListenerService {

	private static final Logger logger = LoggerFactory.getLogger(CirrasUnderwritingLandManagementListenerServiceImpl.class);

	private static ObjectMapper mapper = new ObjectMapper();

	// services
	private CirrasUnderwritingService cirrasUnderwritingService;

	// factories
	private SyncUwLandManagementFactory syncUwLandManagementFactory;

	private Date eventDate;
	private String eventType;

	public void setCirrasUnderwritingService(CirrasUnderwritingService cirrasUnderwritingService) {
		this.cirrasUnderwritingService = cirrasUnderwritingService;
	}

	public void setSyncUnderwritingLandManagementFactory(SyncUwLandManagementFactory syncUwLandManagementFactory) {
		this.syncUwLandManagementFactory = syncUwLandManagementFactory;
	}

	public void processCirrasUnderwritingLandManagementEvent(String eventMessageString, FactoryContext factoryContext) throws Throwable {
		logger.debug("<processCirrasUnderwritingLandManagementEvent");

		try {
			Map<String, Object> landManagementEventObjMap = null;
			try {
				landManagementEventObjMap = mapper.readValue(eventMessageString, new TypeReference<Map<String, Object>>() {
				});
			} catch (Throwable t) {
				logger.error("When attempting to unmarshal " + eventMessageString + ", saw " + t.getMessage(), t);
				throw (t);
			}

			eventType = (String) landManagementEventObjMap.get("eventType");
			Instant eventTimestamp = Instant.parse((String) landManagementEventObjMap.get("eventTimestamp"));
			eventDate = Date.from(eventTimestamp);

			Map<String, String> sourceIdentifiers = (Map<String, String>) landManagementEventObjMap.get("sourceIdentifiers");

//			String legalLandId;
//			String lotId;
			
			// All land events are now ignored.
/*
			switch (eventType) {
			case LandManagementEventTypes.LegalLandCreated:
			case LandManagementEventTypes.LegalLandUpdated:
				ca.bc.gov.mal.cirras.land.management.api.rest.v1.resource.LegalLandRsrc legalLandRsrc = loadLegalLandResource(landManagementEventObjMap);
				synchronizeLegalLand(legalLandRsrc);
				break;
			case LandManagementEventTypes.LegalLandDeleted:
				legalLandId = sourceIdentifiers.get("legalLandId");
				deleteLegalLand(legalLandId);
				break;
			case LandManagementEventTypes.FieldCreated:
			case LandManagementEventTypes.FieldUpdated:
				InsuredLandRsrc insuredLandRsrc = loadFieldResource(landManagementEventObjMap);
				synchronizeField(insuredLandRsrc);
				break;
			case LandManagementEventTypes.FieldDeleted:
				lotId = sourceIdentifiers.get("lotId");
				deleteField(lotId);
				break;
			case LandManagementEventTypes.LegalLandFieldXrefCreated:
			case LandManagementEventTypes.LegalLandFieldXrefDeleted:
				legalLandId = sourceIdentifiers.get("legalLandId");
				lotId = sourceIdentifiers.get("lotId");
				synchronizeLegalLandFieldXref(legalLandId, lotId);
				break;
			case LandManagementEventTypes.AnnualFieldDetailCreated:
			case LandManagementEventTypes.AnnualFieldDetailUpdated:
				AnnualLotDetailRsrc annualLotDetailRsrc = loadAnnualFieldDetailResource(landManagementEventObjMap);
				synchronizeAnnualFieldDetail(annualLotDetailRsrc);
				break;
			case LandManagementEventTypes.AnnualFieldDetailDeleted:
				String annualLotDetailId = sourceIdentifiers.get("annualLotDetailId");
				deleteAnnualFieldDetail(annualLotDetailId);
				break;
			case LandManagementEventTypes.GrowerContractYearCreated:
			case LandManagementEventTypes.GrowerContractYearUpdated:
				GrowerContractYearRsrc growerContractYearRsrc = loadGrowerContractYearResource(landManagementEventObjMap);
				synchronizeGrowerContractYear(growerContractYearRsrc);
				break;
			case LandManagementEventTypes.GrowerContractYearDeleted:
				String growerContractYearId = sourceIdentifiers.get("growerContractYearId");
				deleteGrowerContractYear(growerContractYearId);
				break;
			case LandManagementEventTypes.ContractedFieldDetailCreated:
			case LandManagementEventTypes.ContractedFieldDetailUpdated:
				ContractedLotDetailRsrc contractedLotDetailRsrc = loadContractedFieldDetailResource(landManagementEventObjMap);
				synchronizeContractedFieldDetail(contractedLotDetailRsrc);
				break;
			case LandManagementEventTypes.ContractedFieldDetailDeleted:
				String contractedLotDetailId = sourceIdentifiers.get("contractedLotDetailId");
				deleteContractedFieldDetail(contractedLotDetailId);
				break;
			default:
				logger.info("Ignoring message of type " + eventType);
				break;
			}
*/
			logger.info("Ignoring message of type " + eventType);

		} catch (Throwable t) {
			logger.error(t.getMessage(), t);
			throw (t);
		}
		logger.debug(">processCirrasUnderwritingLandManagementEvent");
	}

/*
	PIM-1156: Disabling all code related to cirras-land-management-api so it can be shutdown.

	private ca.bc.gov.mal.cirras.land.management.api.rest.v1.resource.LegalLandRsrc loadLegalLandResource(Map<String, Object> landManagementEventObjMap)
			throws JsonProcessingException {

		return mapper.readValue(loadResourceAfterUpdateFromEventObject(landManagementEventObjMap), ca.bc.gov.mal.cirras.land.management.api.rest.v1.resource.LegalLandRsrc.class);
	}	
	
	private void synchronizeLegalLand(ca.bc.gov.mal.cirras.land.management.api.rest.v1.resource.LegalLandRsrc legalLandRsrc)
			throws CirrasUnderwritingServiceException, ValidationException {
		
		ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.LegalLandRsrc resource = syncUwLandManagementFactory.getLegalLandSyncRsrc(legalLandRsrc, eventDate, eventType);
		
		cirrasUnderwritingService.synchronizeLegalLand(resource);
	}

	private void deleteLegalLand(String legalLandId) throws CirrasUnderwritingServiceException, ValidationException {

		ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.LegalLandRsrc resource = syncUwLandManagementFactory.getDeleteLegalLandSyncRsrc(legalLandId, eventType);
		
		cirrasUnderwritingService.synchronizeLegalLand(resource);
		
	}

	private InsuredLandRsrc loadFieldResource(Map<String, Object> landManagementEventObjMap)
			throws JsonProcessingException {

		return mapper.readValue(loadResourceAfterUpdateFromEventObject(landManagementEventObjMap), InsuredLandRsrc.class);
	}	
	
	private void synchronizeField(InsuredLandRsrc insuredLandRsrc)
			throws CirrasUnderwritingServiceException, ValidationException {
		
		FieldRsrc resource = syncUwLandManagementFactory.getFieldRsrc(insuredLandRsrc, eventDate, eventType);
		
		cirrasUnderwritingService.synchronizeField(resource);
	}

	private void deleteField(String lotId) throws CirrasUnderwritingServiceException, ValidationException {

		FieldRsrc resource = syncUwLandManagementFactory.getDeleteFieldRsrc(lotId, eventType);
		
		cirrasUnderwritingService.synchronizeField(resource);
		
	}

	private void synchronizeLegalLandFieldXref(String legalLandId, String lotId) throws CirrasUnderwritingServiceException, ValidationException {

		LegalLandFieldXrefRsrc resource = syncUwLandManagementFactory.getLegalLandFieldXrefRsrc(legalLandId, lotId, eventDate, eventType);
		
		cirrasUnderwritingService.synchronizeLegalLandFieldXref(resource);
		
	}

	
	private AnnualLotDetailRsrc loadAnnualFieldDetailResource(Map<String, Object> landManagementEventObjMap)
			throws JsonProcessingException {

		return mapper.readValue(loadResourceAfterUpdateFromEventObject(landManagementEventObjMap), AnnualLotDetailRsrc.class);
	}	
	
	private void synchronizeAnnualFieldDetail(AnnualLotDetailRsrc annualLotDetailRsrc)
			throws CirrasUnderwritingServiceException, ValidationException {
		
		AnnualFieldDetailRsrc resource = syncUwLandManagementFactory.getAnnualFieldDetailRsrc(annualLotDetailRsrc, eventDate, eventType);
		
		cirrasUnderwritingService.synchronizeAnnualFieldDetail(resource);
	}

	private void deleteAnnualFieldDetail(String annualLotDetailId) throws CirrasUnderwritingServiceException, ValidationException {

		AnnualFieldDetailRsrc resource = syncUwLandManagementFactory.getDeleteAnnualFieldDetailRsrc(annualLotDetailId, eventType);
		
		cirrasUnderwritingService.synchronizeAnnualFieldDetail(resource);
		
	}
	
	private GrowerContractYearRsrc loadGrowerContractYearResource(Map<String, Object> landManagementEventObjMap)
			throws JsonProcessingException {

		return mapper.readValue(loadResourceAfterUpdateFromEventObject(landManagementEventObjMap), GrowerContractYearRsrc.class);
	}	
	
	private void synchronizeGrowerContractYear(GrowerContractYearRsrc growerContractYearRsrc)
			throws CirrasUnderwritingServiceException, ValidationException {
		
		GrowerContractYearSyncRsrc resource = syncUwLandManagementFactory.getGrowerContractYearSyncRsrc(growerContractYearRsrc, eventDate, eventType);
		
		cirrasUnderwritingService.synchronizeGrowerContractYear(resource);
	}

	private void deleteGrowerContractYear(String growerContractYearId) throws CirrasUnderwritingServiceException, ValidationException {

		GrowerContractYearSyncRsrc resource = syncUwLandManagementFactory.getDeleteGrowerContractYearSyncRsrc(growerContractYearId, eventType);
		
		cirrasUnderwritingService.synchronizeGrowerContractYear(resource);
		
	}
	
	private ContractedLotDetailRsrc loadContractedFieldDetailResource(Map<String, Object> landManagementEventObjMap)
			throws JsonProcessingException {

		return mapper.readValue(loadResourceAfterUpdateFromEventObject(landManagementEventObjMap), ContractedLotDetailRsrc.class);
	}	
	
	private void synchronizeContractedFieldDetail(ContractedLotDetailRsrc contractedLotDetailRsrc)
			throws CirrasUnderwritingServiceException, ValidationException {
		
		ContractedFieldDetailRsrc resource = syncUwLandManagementFactory.getContractedFieldDetailRsrc(contractedLotDetailRsrc, eventDate, eventType);
		
		cirrasUnderwritingService.synchronizeContractedFieldDetail(resource);
	}

	private void deleteContractedFieldDetail(String contractedLotDetailId) throws CirrasUnderwritingServiceException, ValidationException {

		ContractedFieldDetailRsrc resource = syncUwLandManagementFactory.getDeleteContractedFieldDetailRsrc(contractedLotDetailId, eventType);
		
		cirrasUnderwritingService.synchronizeContractedFieldDetail(resource);
		
	}
	
	private String loadResourceBeforeUpdateFromEventObject(Map<String, Object> legalLandEventObjMap) throws JsonProcessingException {
		@SuppressWarnings("unchecked")
		Map<String, Object> resourceBeforeUpdate = (Map<String, Object>) legalLandEventObjMap.get("resourceBeforeUpdate");
		String resourceBeforeUpdateJson = mapper.writeValueAsString(resourceBeforeUpdate);
		return resourceBeforeUpdateJson;
	}

	private String loadResourceAfterUpdateFromEventObject(Map<String, Object> legalLandEventObjMap) throws JsonProcessingException {
		@SuppressWarnings("unchecked")
		Map<String, Object> resourceAfterUpdate = (Map<String, Object>) legalLandEventObjMap.get("resourceAfterUpdate");
		String resourceAfterUpdateJson = mapper.writeValueAsString(resourceAfterUpdate);
		return resourceAfterUpdateJson;
	}

	private ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.EndpointsRsrc getUnderwritingServiceTopLevelEndpoints()
			throws CirrasUnderwritingServiceException {
		return cirrasUnderwritingService.getTopLevelEndpoints();
	}
*/

}