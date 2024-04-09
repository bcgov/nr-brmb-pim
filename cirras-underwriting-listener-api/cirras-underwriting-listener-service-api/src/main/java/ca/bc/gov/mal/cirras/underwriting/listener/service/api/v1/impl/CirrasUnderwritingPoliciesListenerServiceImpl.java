package ca.bc.gov.mal.cirras.underwriting.listener.service.api.v1.impl;

import java.time.Instant;
import java.util.Date;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import ca.bc.gov.mal.cirras.underwriting.api.rest.client.v1.CirrasUnderwritingServiceException;
import ca.bc.gov.mal.cirras.underwriting.api.rest.client.v1.ValidationException;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.ContactEmailRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.ContactPhoneRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.ContactRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.GrowerContactRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.GrowerContractYearSyncRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.GrowerRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.PolicyRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.SyncCodeRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.SyncCommodityTypeCodeRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.SyncCommodityTypeVarietyXrefRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.SyncCommodityVarietyRsrc;
import ca.bc.gov.mal.cirras.policies.api.rest.v1.resource.CodeSyncRsrc;
import ca.bc.gov.mal.cirras.policies.api.rest.v1.resource.CommodityTypeCodeSyncRsrc;
import ca.bc.gov.mal.cirras.policies.api.rest.v1.resource.CommodityVarietySyncRsrc;
import ca.bc.gov.mal.cirras.policies.api.rest.v1.resource.ContactsRsrc;
import ca.bc.gov.mal.cirras.policies.api.rest.v1.resource.EmailsRsrc;
import ca.bc.gov.mal.cirras.policies.api.rest.v1.resource.GrowerContractYearRsrc;
import ca.bc.gov.mal.cirras.policies.api.rest.v1.resource.InsurancePolicyRsrc;
import ca.bc.gov.mal.cirras.policies.api.rest.v1.resource.InsuredGrowerContactsRsrc;
import ca.bc.gov.mal.cirras.policies.api.rest.v1.resource.InsuredGrowerRsrc;
import ca.bc.gov.mal.cirras.policies.api.rest.v1.resource.TelecomRsrc;
import ca.bc.gov.mal.cirras.policies.model.v1.CodeTableTypes;
import ca.bc.gov.mal.cirras.policies.model.v1.PoliciesEventTypes;
import ca.bc.gov.mal.cirras.underwriting.api.rest.client.v1.CirrasUnderwritingService;
import ca.bc.gov.mal.cirras.underwriting.listener.service.api.v1.CirrasUnderwritingPoliciesListenerService;
import ca.bc.gov.mal.cirras.underwriting.listener.service.api.v1.model.factory.SyncUwPoliciesFactory;
import ca.bc.gov.nrs.wfone.common.service.api.model.factory.FactoryContext;

public class CirrasUnderwritingPoliciesListenerServiceImpl implements CirrasUnderwritingPoliciesListenerService {

	private static final Logger logger = LoggerFactory.getLogger(CirrasUnderwritingPoliciesListenerServiceImpl.class);

	private static ObjectMapper mapper = new ObjectMapper();

	// services
	private CirrasUnderwritingService cirrasUnderwritingService;

	// factories
	private SyncUwPoliciesFactory syncUwPoliciesFactory;

	private Date eventDate;
	private String eventType;

	public void setCirrasUnderwritingService(CirrasUnderwritingService cirrasUnderwritingService) {
		this.cirrasUnderwritingService = cirrasUnderwritingService;
	}

	public void setSyncUwPoliciesFactory(SyncUwPoliciesFactory syncUwPoliciesFactory) {
		this.syncUwPoliciesFactory = syncUwPoliciesFactory;
	}

	public void processCirrasUnderwritingPoliciesEvent(String eventMessageString, String messageId, FactoryContext factoryContext) throws Throwable {
		logger.debug("<processCirrasUnderwritingEvent");

		try {
			Map<String, Object> policiesEventObjMap = null;
			try {
				policiesEventObjMap = mapper.readValue(eventMessageString, new TypeReference<Map<String, Object>>() {
				});
			} catch (Throwable t) {
				logger.error("When attempting to unmarshal " + eventMessageString + ", saw " + t.getMessage(), t);
				throw (t);
			}

			eventType = (String) policiesEventObjMap.get("eventType");
			Instant eventTimestamp = Instant.parse((String) policiesEventObjMap.get("eventTimestamp"));
			eventDate = Date.from(eventTimestamp);

			Map<String, String> sourceIdentifiers = (Map<String, String>) policiesEventObjMap.get("sourceIdentifiers");

			switch (eventType) {
			case PoliciesEventTypes.CodeCreated:
			case PoliciesEventTypes.CodeUpdated:
			case PoliciesEventTypes.CodeDeleted:
				CodeSyncRsrc codeSyncRsrc = loadCodeResource(policiesEventObjMap);
				//Only sync if the uw service supports this code table type sync
				//It silently ignores the message and logs it
				if(isCodeTableSupported(codeSyncRsrc.getCodeTableType(), messageId)) {
					synchronizeCode(codeSyncRsrc);
				}
				break;
			case PoliciesEventTypes.GrowerCreated:
			case PoliciesEventTypes.GrowerUpdated:
				InsuredGrowerRsrc insuredGrowerRsrc = loadInsuredGrowerResource(policiesEventObjMap);
				synchronizeGrower(insuredGrowerRsrc);
				break;
			case PoliciesEventTypes.GrowerDeleted:
				String growerId = sourceIdentifiers.get("growerId");
				deleteGrower(growerId);
				break;
			case PoliciesEventTypes.PolicyCreated:
			case PoliciesEventTypes.PolicyUpdated:
				InsurancePolicyRsrc insurancePolicyRsrc = loadInsurancePolicyResource(policiesEventObjMap);
				synchronizePolicyData(insurancePolicyRsrc);
				break;
			case PoliciesEventTypes.PolicyDeleted:
				String policyId = sourceIdentifiers.get("insurancePolicyId");
				deletePolicy(policyId);
				break;
			case PoliciesEventTypes.GrowerContractYearCreated:
			case PoliciesEventTypes.GrowerContractYearUpdated:
				GrowerContractYearRsrc growerContractYearRsrc = loadGrowerContractYearResource(policiesEventObjMap);
				synchronizeGrowerContractYear(growerContractYearRsrc);
				break;
			case PoliciesEventTypes.GrowerContractYearDeleted:
				String growerContractYearId = sourceIdentifiers.get("growerContractYearId");
				deleteGrowerContractYear(growerContractYearId);
				break;
			case PoliciesEventTypes.CommodityVarietyCreated:
			case PoliciesEventTypes.CommodityVarietyUpdated:
				CommodityVarietySyncRsrc commodityVarietySyncRsrc = loadCommodityVarietyResource(policiesEventObjMap);
				synchronizeCommodityVariety(commodityVarietySyncRsrc);
				break;
			case PoliciesEventTypes.CommodityVarietyDeleted:
				String cropId = sourceIdentifiers.get("cropId");
				deleteCommodityVariety(cropId);
				break;
			case PoliciesEventTypes.ContactsCreated:
			case PoliciesEventTypes.ContactsUpdated:
				ContactsRsrc contactsRsrc = loadContactsResource(policiesEventObjMap);
				synchronizeContact(contactsRsrc);
				break;
			case PoliciesEventTypes.ContactsDeleted:
				String contactsId = sourceIdentifiers.get("contactsId");
				deleteContact(contactsId);
				break;
			case PoliciesEventTypes.InsuredGrowerContactsCreated:
			case PoliciesEventTypes.InsuredGrowerContactsUpdated:
				InsuredGrowerContactsRsrc insuredGrowerContactsRsrc = loadInsuredGrowerContactsResource(policiesEventObjMap);
				synchronizeGrowerContact(insuredGrowerContactsRsrc);
				break;
			case PoliciesEventTypes.InsuredGrowerContactsDeleted:
				String insuredGrowerContactsId = sourceIdentifiers.get("insuredGrowerContactsId");
				deleteGrowerContact(insuredGrowerContactsId);
				break;
			case PoliciesEventTypes.EmailsCreated:
			case PoliciesEventTypes.EmailsUpdated:
				EmailsRsrc emailsRsrc = loadEmailsResource(policiesEventObjMap);
				synchronizeContactEmail(emailsRsrc);
				break;
			case PoliciesEventTypes.EmailsDeleted:
				String emailsId = sourceIdentifiers.get("emailsId");
				deleteContactEmail(emailsId);
				break;
			case PoliciesEventTypes.TelecomCreated:
			case PoliciesEventTypes.TelecomUpdated:
				TelecomRsrc telecomRsrc = loadTelecomResource(policiesEventObjMap);
				synchronizeContactPhone(telecomRsrc);
				break;
			case PoliciesEventTypes.TelecomDeleted:
				String telecomId = sourceIdentifiers.get("telecomId");
				deleteContactPhone(telecomId);
				break;
			case PoliciesEventTypes.CommodityTypeCodeCreated:
			case PoliciesEventTypes.CommodityTypeCodeUpdated:
				CommodityTypeCodeSyncRsrc commodityTypeCodeSyncRsrc = loadCommodityTypeCodeSyncResource(policiesEventObjMap);
				synchronizeCommodityTypeCode(commodityTypeCodeSyncRsrc);
				break;
			case PoliciesEventTypes.CommodityTypeCodeDeleted:
				String commodityTypeCode = sourceIdentifiers.get("commodityTypeCode");
				deleteCommodityTypeCode(commodityTypeCode);
				break;
			case PoliciesEventTypes.CommodityTypeVarietyXrefCreated:
			case PoliciesEventTypes.CommodityTypeVarietyXrefDeleted:
				commodityTypeCode = sourceIdentifiers.get("commodityTypeCode");
				String cropVarietyId = sourceIdentifiers.get("varietyId");
				synchronizeCommodityTypeVarietyXref(commodityTypeCode, cropVarietyId);
				break;
			default:
				logger.info("Ignoring message of type " + eventType);
				break;
			}

		} catch (Throwable t) {
			logger.error(t.getMessage(), t);
			throw (t);
		}
		logger.debug(">processCirrasUnderwritingEvent");
	}

	//Returns true if the uw service supports this code table type sync
	private boolean isCodeTableSupported(String codeTableType, String messageId) {
		
		if(codeTableType.equals(CodeTableTypes.PolicyStatusCode)
				|| codeTableType.equals(CodeTableTypes.Offices)) {
			return true;
		}
		else {
			logger.info("Ignoring codeTableType as it's not supported by the underwriting api: " + codeTableType + " - messageId: " + messageId);
		}
		return false;
	}

	private CodeSyncRsrc loadCodeResource(Map<String, Object> policiesEventObjMap)
			throws JsonProcessingException {

		if(eventType.equals(PoliciesEventTypes.CodeDeleted)) {
			//Delete reads the before object
			return mapper.readValue(loadResourceBeforeUpdateFromEventObject(policiesEventObjMap), CodeSyncRsrc.class);
		} else {
			return mapper.readValue(loadResourceAfterUpdateFromEventObject(policiesEventObjMap), CodeSyncRsrc.class);
		}
	}	

	private InsuredGrowerRsrc loadInsuredGrowerResource(Map<String, Object> policiesEventObjMap)
			throws JsonProcessingException {

		return mapper.readValue(loadResourceAfterUpdateFromEventObject(policiesEventObjMap), InsuredGrowerRsrc.class);
	}	

	private InsurancePolicyRsrc loadInsurancePolicyResource(Map<String, Object> policiesEventObjMap)
			throws JsonProcessingException {

		return mapper.readValue(loadResourceAfterUpdateFromEventObject(policiesEventObjMap), InsurancePolicyRsrc.class);
	}

	private GrowerContractYearRsrc loadGrowerContractYearResource(Map<String, Object> policiesEventObjMap)
			throws JsonProcessingException {

		return mapper.readValue(loadResourceAfterUpdateFromEventObject(policiesEventObjMap), GrowerContractYearRsrc.class);
	}
	
	private CommodityVarietySyncRsrc loadCommodityVarietyResource(Map<String, Object> policiesEventObjMap)
			throws JsonProcessingException {

		return mapper.readValue(loadResourceAfterUpdateFromEventObject(policiesEventObjMap), CommodityVarietySyncRsrc.class);
	}
	
	private ContactsRsrc loadContactsResource(Map<String, Object> policiesEventObjMap)
			throws JsonProcessingException {

		return mapper.readValue(loadResourceAfterUpdateFromEventObject(policiesEventObjMap), ContactsRsrc.class);
	}	
	
	private InsuredGrowerContactsRsrc loadInsuredGrowerContactsResource(Map<String, Object> policiesEventObjMap)
			throws JsonProcessingException {

		return mapper.readValue(loadResourceAfterUpdateFromEventObject(policiesEventObjMap), InsuredGrowerContactsRsrc.class);
	}	

	private EmailsRsrc loadEmailsResource(Map<String, Object> policiesEventObjMap)
			throws JsonProcessingException {

		return mapper.readValue(loadResourceAfterUpdateFromEventObject(policiesEventObjMap), EmailsRsrc.class);
	}	

	private TelecomRsrc loadTelecomResource(Map<String, Object> policiesEventObjMap)
			throws JsonProcessingException {

		return mapper.readValue(loadResourceAfterUpdateFromEventObject(policiesEventObjMap), TelecomRsrc.class);
	}	
	
	private CommodityTypeCodeSyncRsrc loadCommodityTypeCodeSyncResource(Map<String, Object> policiesEventObjMap)
			throws JsonProcessingException {

		return mapper.readValue(loadResourceAfterUpdateFromEventObject(policiesEventObjMap), CommodityTypeCodeSyncRsrc.class);
	}	
	
	private String loadResourceBeforeUpdateFromEventObject(Map<String, Object> policiesEventObjMap) throws JsonProcessingException {
		@SuppressWarnings("unchecked")
		Map<String, Object> resourceBeforeUpdate = (Map<String, Object>) policiesEventObjMap.get("resourceBeforeUpdate");
		String resourceBeforeUpdateJson = mapper.writeValueAsString(resourceBeforeUpdate);
		return resourceBeforeUpdateJson;
	}

	private String loadResourceAfterUpdateFromEventObject(Map<String, Object> policiesEventObjMap) throws JsonProcessingException {
		@SuppressWarnings("unchecked")
		Map<String, Object> resourceAfterUpdate = (Map<String, Object>) policiesEventObjMap.get("resourceAfterUpdate");
		String resourceAfterUpdateJson = mapper.writeValueAsString(resourceAfterUpdate);
		return resourceAfterUpdateJson;
	}

	private void synchronizeCode(CodeSyncRsrc codeSyncRsrc)
			throws CirrasUnderwritingServiceException, ValidationException {
		
		SyncCodeRsrc syncCodeRsrc = syncUwPoliciesFactory.getSyncCodeRsrc(codeSyncRsrc, eventDate, eventType);
		
		cirrasUnderwritingService.synchronizeCode(syncCodeRsrc);
	}
	
	private void synchronizeGrower(InsuredGrowerRsrc insuredGrowerRsrc)
			throws CirrasUnderwritingServiceException, ValidationException {
		
		GrowerRsrc resource = syncUwPoliciesFactory.getGrowerRsrc(insuredGrowerRsrc, eventDate, eventType);
		
		cirrasUnderwritingService.synchronizeGrower(resource);
	}
	
	private void deleteGrower(String growerId) throws CirrasUnderwritingServiceException, ValidationException {

		GrowerRsrc resource = syncUwPoliciesFactory.getDeleteGrowerRsrc(growerId, eventType);
		
		cirrasUnderwritingService.synchronizeGrower(resource);
		
	}

	private void synchronizePolicyData(InsurancePolicyRsrc insurancePolicyRsrc)
			throws CirrasUnderwritingServiceException, ValidationException {

		PolicyRsrc resource = syncUwPoliciesFactory.getPolicyRsrc(insurancePolicyRsrc, eventDate,	eventType);
		cirrasUnderwritingService.synchronizePolicy(resource);

	}

	private void deletePolicy(String policyId) throws CirrasUnderwritingServiceException, ValidationException {

		PolicyRsrc resource = syncUwPoliciesFactory.getDeletePolicyRsrc(policyId, eventType);
		
		cirrasUnderwritingService.synchronizePolicy(resource);
		
	}

	private void synchronizeGrowerContractYear(GrowerContractYearRsrc growerContractYearRsrc)
			throws CirrasUnderwritingServiceException, ValidationException {
		
		GrowerContractYearSyncRsrc resource = syncUwPoliciesFactory.getGrowerContractYearSyncRsrc(growerContractYearRsrc, eventDate, eventType);
		
		cirrasUnderwritingService.synchronizeGrowerContractYear(resource);
	}

	private void deleteGrowerContractYear(String growerContractYearId) throws CirrasUnderwritingServiceException, ValidationException {

		GrowerContractYearSyncRsrc resource = syncUwPoliciesFactory.getDeleteGrowerContractYearSyncRsrc(growerContractYearId, eventType);
		
		cirrasUnderwritingService.synchronizeGrowerContractYear(resource);
		
	}
	
	private void synchronizeCommodityVariety(CommodityVarietySyncRsrc commodityVarietySyncRsrc)
			throws CirrasUnderwritingServiceException, ValidationException {
		
		SyncCommodityVarietyRsrc syncCommodityVarietyRsrc = syncUwPoliciesFactory.getSyncCommodityVarietyRsrc(commodityVarietySyncRsrc, eventDate, eventType);
		
		cirrasUnderwritingService.synchronizeCommodityVariety(syncCommodityVarietyRsrc);
	}
	
	private void deleteCommodityVariety(String cropId)
			throws CirrasUnderwritingServiceException, ValidationException {
		
		SyncCommodityVarietyRsrc syncCommodityVarietyRsrc = syncUwPoliciesFactory.getDeleteSyncCommodityVarietyRsrc(cropId, eventDate, eventType);
		
		cirrasUnderwritingService.synchronizeCommodityVariety(syncCommodityVarietyRsrc);
	}

	private void synchronizeContact(ContactsRsrc contactsRsrc)
			throws CirrasUnderwritingServiceException, ValidationException {
		
		ContactRsrc resource = syncUwPoliciesFactory.getContactRsrc(contactsRsrc, eventDate, eventType);
		
		cirrasUnderwritingService.synchronizeContact(resource);
	}
	
	private void deleteContact(String contactId) throws CirrasUnderwritingServiceException, ValidationException {

		ContactRsrc resource = syncUwPoliciesFactory.getDeleteContactRsrc(contactId, eventType);
		
		cirrasUnderwritingService.synchronizeContact(resource);
	}

	private void synchronizeGrowerContact(InsuredGrowerContactsRsrc insuredGrowerContactsRsrc)
			throws CirrasUnderwritingServiceException, ValidationException {
		
		GrowerContactRsrc resource = syncUwPoliciesFactory.getGrowerContactRsrc(insuredGrowerContactsRsrc, eventDate, eventType);
		
		cirrasUnderwritingService.synchronizeGrowerContact(resource);
	}
	
	private void deleteGrowerContact(String growerContactId) throws CirrasUnderwritingServiceException, ValidationException {

		GrowerContactRsrc resource = syncUwPoliciesFactory.getDeleteGrowerContactRsrc(growerContactId, eventType);
		
		cirrasUnderwritingService.synchronizeGrowerContact(resource);
	}

	private void synchronizeContactEmail(EmailsRsrc emailsRsrc)
			throws CirrasUnderwritingServiceException, ValidationException {
		
		ContactEmailRsrc resource = syncUwPoliciesFactory.getContactEmailRsrc(emailsRsrc, eventDate, eventType);
		
		cirrasUnderwritingService.synchronizeContactEmail(resource);
	}
	
	private void deleteContactEmail(String contactEmailId) throws CirrasUnderwritingServiceException, ValidationException {

		ContactEmailRsrc resource = syncUwPoliciesFactory.getDeleteContactEmailRsrc(contactEmailId, eventType);
		
		cirrasUnderwritingService.synchronizeContactEmail(resource);
	}

	private void synchronizeContactPhone(TelecomRsrc telecomRsrc)
			throws CirrasUnderwritingServiceException, ValidationException {
		
		ContactPhoneRsrc resource = syncUwPoliciesFactory.getContactPhoneRsrc(telecomRsrc, eventDate, eventType);
		
		cirrasUnderwritingService.synchronizeContactPhone(resource);
	}
	
	private void deleteContactPhone(String contactPhoneId) throws CirrasUnderwritingServiceException, ValidationException {

		ContactPhoneRsrc resource = syncUwPoliciesFactory.getDeleteContactPhoneRsrc(contactPhoneId, eventType);
		
		cirrasUnderwritingService.synchronizeContactPhone(resource);
	}

	private void synchronizeCommodityTypeCode(CommodityTypeCodeSyncRsrc commodityTypeCodeSyncRsrc)
			throws CirrasUnderwritingServiceException, ValidationException {
		
		SyncCommodityTypeCodeRsrc resource = syncUwPoliciesFactory.getSyncCommodityTypeCodeRsrc(commodityTypeCodeSyncRsrc, eventDate, eventType);
		
		cirrasUnderwritingService.synchronizeCommodityTypeCode(resource);
	}
	
	private void deleteCommodityTypeCode(String commodityTypeCode) throws CirrasUnderwritingServiceException, ValidationException {

		SyncCommodityTypeCodeRsrc resource = syncUwPoliciesFactory.getDeleteSyncCommodityTypeCodeRsrc(commodityTypeCode, eventDate, eventType);
		
		cirrasUnderwritingService.synchronizeCommodityTypeCode(resource);
	}
	

	private void synchronizeCommodityTypeVarietyXref(String commodityTypeCode, String cropVarietyId) throws CirrasUnderwritingServiceException, ValidationException {

		SyncCommodityTypeVarietyXrefRsrc resource = syncUwPoliciesFactory.getSyncCommodityTypeVarietyXrefRsrc(commodityTypeCode, cropVarietyId, eventDate, eventType);
		
		cirrasUnderwritingService.synchronizeCommodityTypeVarietyXref(resource);
	}

}