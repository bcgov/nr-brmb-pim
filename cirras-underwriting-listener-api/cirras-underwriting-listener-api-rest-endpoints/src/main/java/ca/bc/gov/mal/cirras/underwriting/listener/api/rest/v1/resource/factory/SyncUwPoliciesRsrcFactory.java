package ca.bc.gov.mal.cirras.underwriting.listener.api.rest.v1.resource.factory;

import java.util.Date;

import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.ContactEmailRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.ContactPhoneRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.ContactRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.GrowerContactRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.GrowerContractYearSyncRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.GrowerRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.PoliciesSyncEventTypes;
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
import ca.bc.gov.mal.cirras.policies.model.v1.PoliciesEventTypes;
import ca.bc.gov.mal.cirras.underwriting.listener.service.api.v1.model.factory.SyncUwPoliciesFactory;
import ca.bc.gov.nrs.wfone.common.rest.endpoints.resource.factory.BaseResourceFactory;

public class SyncUwPoliciesRsrcFactory extends BaseResourceFactory implements SyncUwPoliciesFactory {

	//======================================================================================================================
	// CODE TABLES
	//======================================================================================================================
	@Override
	public SyncCodeRsrc getSyncCodeRsrc(CodeSyncRsrc codeSyncRsrc, Date eventDate, String eventType) {
		SyncCodeRsrc resource = new SyncCodeRsrc();
		
		resource.setCodeTableType(codeSyncRsrc.getCodeTableType());
		resource.setUniqueKeyString(codeSyncRsrc.getUniqueKeyString());
		resource.setUniqueKeyInteger(codeSyncRsrc.getUniqueKeyInteger());
		resource.setDescription(codeSyncRsrc.getDescription());
		resource.setIsActive(codeSyncRsrc.getIsActive());
		resource.setDataSyncTransDate(eventDate);
		resource.setTransactionType(eventType.replace(PoliciesEventTypes.EventTypeNamespace, PoliciesSyncEventTypes.EventTypeNamespace));
		
		return resource;
	}

	//======================================================================================================================
	// GROWER
	//======================================================================================================================
	@Override
	public GrowerRsrc getGrowerRsrc(InsuredGrowerRsrc insuredGrower, Date eventDate, String eventType) {
		GrowerRsrc resource = new GrowerRsrc();
		
		resource.setGrowerId(insuredGrower.getGrowerId());
		resource.setGrowerNumber(insuredGrower.getGrowerNumber());
		resource.setGrowerName(insuredGrower.getGrowerName());
		resource.setGrowerAddressLine1(insuredGrower.getGrowerAddressLine1());
		resource.setGrowerAddressLine2(insuredGrower.getGrowerAddressLine2());
		resource.setGrowerPostalCode(insuredGrower.getGrowerPostalCode());
		resource.setGrowerCity(insuredGrower.getGrowerCity());
		resource.setCityId(insuredGrower.getGrowerCityId());
		resource.setGrowerProvince(insuredGrower.getGrowerProvince());
		resource.setDataSyncTransDate(eventDate);
		resource.setTransactionType(eventType.replace(PoliciesEventTypes.EventTypeNamespace, PoliciesSyncEventTypes.EventTypeNamespace));
	
		return resource;

	}
	
	@Override
	public GrowerRsrc getDeleteGrowerRsrc(String growerId, String eventType) {
		GrowerRsrc resource = new GrowerRsrc();

		resource.setGrowerId(Integer.valueOf(growerId));
		resource.setTransactionType(eventType.replace(PoliciesEventTypes.EventTypeNamespace, PoliciesSyncEventTypes.EventTypeNamespace));
		
		return resource;
	}

	//======================================================================================================================
	// POLICY
	//======================================================================================================================
	@Override
	public PolicyRsrc getPolicyRsrc(InsurancePolicyRsrc insurancePolicy, Date eventDate, String eventType) {
		PolicyRsrc resource = new PolicyRsrc();
		
		resource.setPolicyId(insurancePolicy.getInsurancePolicyId());
		resource.setGrowerId(insurancePolicy.getGrowerId());
		resource.setInsurancePlanId(insurancePolicy.getInsurancePlanId());
		resource.setOfficeId(insurancePolicy.getOfficeId());
		resource.setPolicyStatusCode(insurancePolicy.getPolicyStatusCode());
		resource.setPolicyNumber(insurancePolicy.getPolicyNumber());
		resource.setContractNumber(insurancePolicy.getContractNumber());
		resource.setContractId(insurancePolicy.getContractId());
		resource.setCropYear(insurancePolicy.getCropYear());
		resource.setDataSyncTransDate(eventDate);
		resource.setTransactionType(eventType.replace(PoliciesEventTypes.EventTypeNamespace, PoliciesSyncEventTypes.EventTypeNamespace));
		
		return resource;

	}

	@Override
	public PolicyRsrc getDeletePolicyRsrc(String policyId, String eventType) {
		PolicyRsrc resource = new PolicyRsrc();

		resource.setPolicyId(Integer.valueOf(policyId));
		resource.setTransactionType(eventType.replace(PoliciesEventTypes.EventTypeNamespace, PoliciesSyncEventTypes.EventTypeNamespace));
		
		return resource;
	}
	

	//======================================================================================================================
	// GROWER CONTRACT YEAR
	//======================================================================================================================
	@Override
	public GrowerContractYearSyncRsrc getGrowerContractYearSyncRsrc(GrowerContractYearRsrc growerContractYear, Date eventDate, String eventType) {
		GrowerContractYearSyncRsrc resource = new GrowerContractYearSyncRsrc();
		
		resource.setGrowerContractYearId(growerContractYear.getGrowerContractYearId());
		resource.setGrowerId(growerContractYear.getGrowerId());
		resource.setInsurancePlanId(growerContractYear.getInsurancePlanId());
		resource.setContractId(growerContractYear.getContractId());
		resource.setCropYear(growerContractYear.getCropYear());
		resource.setDataSyncTransDate(eventDate);
		resource.setTransactionType(eventType.replace(PoliciesEventTypes.EventTypeNamespace, PoliciesSyncEventTypes.EventTypeNamespace));
		
		return resource;

	}

	@Override
	public GrowerContractYearSyncRsrc getDeleteGrowerContractYearSyncRsrc(String growerContractYearId, String eventType) {
		GrowerContractYearSyncRsrc resource = new GrowerContractYearSyncRsrc();

		resource.setGrowerContractYearId(Integer.valueOf(growerContractYearId));
		resource.setTransactionType(eventType.replace(PoliciesEventTypes.EventTypeNamespace, PoliciesSyncEventTypes.EventTypeNamespace));
		
		return resource;
	}
	
	
	//======================================================================================================================
	// COMMODITY VARIETY
	//======================================================================================================================
	@Override
	public SyncCommodityVarietyRsrc getSyncCommodityVarietyRsrc(CommodityVarietySyncRsrc commodityVarietySyncRsrc,
			Date eventDate, String eventType) {
		SyncCommodityVarietyRsrc resource = new SyncCommodityVarietyRsrc();

		resource.setCropId(commodityVarietySyncRsrc.getCropId());
		resource.setCropName(commodityVarietySyncRsrc.getCropName());
		resource.setParentCropId(commodityVarietySyncRsrc.getParentCropId());
		resource.setIsInventoryCrop(commodityVarietySyncRsrc.getIsInventoryCrop());
		resource.setInsurancePlanId(commodityVarietySyncRsrc.getInsurancePlanId());
		resource.setShortLabel(commodityVarietySyncRsrc.getShortLabel());
		resource.setPlantDurationTypeCode(commodityVarietySyncRsrc.getPlantDurationTypeCode());
		resource.setIsYieldCrop(commodityVarietySyncRsrc.getIsYieldCrop());
		resource.setIsUnderwritingCrop(commodityVarietySyncRsrc.getIsUnderwritingCrop());
		resource.setIsProductInsurableInd(commodityVarietySyncRsrc.getIsProductInsurable());
		resource.setIsCropInsuranceEligibleInd(commodityVarietySyncRsrc.getIsCropInsuranceEligible());
		resource.setIsPlantInsuranceEligibleInd(commodityVarietySyncRsrc.getIsPlantInsuranceEligible());
		resource.setIsOtherInsuranceEligibleInd(commodityVarietySyncRsrc.getIsOtherInsuranceEligible());

		resource.setYieldMeasUnitTypeCode(commodityVarietySyncRsrc.getYieldMeasUnitTypeCode());
		resource.setYieldDecimalPrecision(commodityVarietySyncRsrc.getYieldDecimalPrecision());
		resource.setDataSyncTransDate(eventDate);
		resource.setTransactionType(eventType.replace(PoliciesEventTypes.EventTypeNamespace, PoliciesSyncEventTypes.EventTypeNamespace));
		
		return resource;
	}

	@Override
	public SyncCommodityVarietyRsrc getDeleteSyncCommodityVarietyRsrc(String cropId, Date eventDate, String eventType) {
		SyncCommodityVarietyRsrc resource = new SyncCommodityVarietyRsrc();

		resource.setCropId(Integer.valueOf(cropId));
		resource.setDataSyncTransDate(eventDate);
		resource.setTransactionType(eventType.replace(PoliciesEventTypes.EventTypeNamespace, PoliciesSyncEventTypes.EventTypeNamespace));
		
		return resource;
	}

	//======================================================================================================================
	// CONTACT
	//======================================================================================================================
	@Override
	public ContactRsrc getContactRsrc(ContactsRsrc contactsRsrc, Date eventDate, String eventType) {
		ContactRsrc resource = new ContactRsrc();
		
		resource.setContactId(contactsRsrc.getContactsId());
		resource.setFirstName(contactsRsrc.getFirstName());
		resource.setLastName(contactsRsrc.getLastName());
		resource.setDataSyncTransDate(eventDate);
		resource.setTransactionType(eventType.replace(PoliciesEventTypes.EventTypeNamespace, PoliciesSyncEventTypes.EventTypeNamespace));
	
		return resource;

	}
	
	@Override
	public ContactRsrc getDeleteContactRsrc(String contactsId, String eventType) {
		ContactRsrc resource = new ContactRsrc();

		resource.setContactId(Integer.valueOf(contactsId));
		resource.setTransactionType(eventType.replace(PoliciesEventTypes.EventTypeNamespace, PoliciesSyncEventTypes.EventTypeNamespace));
		
		return resource;
	}

	//======================================================================================================================
	// GROWER CONTACT
	//======================================================================================================================
	@Override
	public GrowerContactRsrc getGrowerContactRsrc(InsuredGrowerContactsRsrc insuredGrowerContactsRsrc, Date eventDate, String eventType) {
		GrowerContactRsrc resource = new GrowerContactRsrc();
		
		resource.setGrowerContactId(insuredGrowerContactsRsrc.getGrowerContactsId());
		resource.setGrowerId(insuredGrowerContactsRsrc.getGrowerId());
		resource.setContactId(insuredGrowerContactsRsrc.getContactsId());
		resource.setIsPrimaryContactInd(insuredGrowerContactsRsrc.getIsPrimaryContactInd());
		resource.setIsActivelyInvolvedInd(insuredGrowerContactsRsrc.getIsActivelyInvolvedInd());
		resource.setDataSyncTransDate(eventDate);
		resource.setTransactionType(eventType.replace(PoliciesEventTypes.EventTypeNamespace, PoliciesSyncEventTypes.EventTypeNamespace));
	
		return resource;

	}
	
	@Override
	public GrowerContactRsrc getDeleteGrowerContactRsrc(String insuredGrowerContactsId, String eventType) {
		GrowerContactRsrc resource = new GrowerContactRsrc();

		resource.setGrowerContactId(Integer.valueOf(insuredGrowerContactsId));
		resource.setTransactionType(eventType.replace(PoliciesEventTypes.EventTypeNamespace, PoliciesSyncEventTypes.EventTypeNamespace));
		
		return resource;
	}

	//======================================================================================================================
	// CONTACT EMAIL
	//======================================================================================================================
	@Override
	public ContactEmailRsrc getContactEmailRsrc(EmailsRsrc emailsRsrc, Date eventDate, String eventType) {
		ContactEmailRsrc resource = new ContactEmailRsrc();
		
		resource.setContactEmailId(emailsRsrc.getEmailsId());
		resource.setContactId(emailsRsrc.getContactsId());
		resource.setEmailAddress(emailsRsrc.getEmailAddress());
		resource.setIsPrimaryEmailInd(emailsRsrc.getIsPrimaryEmailInd());
		resource.setIsActive(emailsRsrc.getIsActive());
		resource.setDataSyncTransDate(eventDate);
		resource.setTransactionType(eventType.replace(PoliciesEventTypes.EventTypeNamespace, PoliciesSyncEventTypes.EventTypeNamespace));
		
		return resource;

	}
	
	@Override
	public ContactEmailRsrc getDeleteContactEmailRsrc(String emailsId, String eventType) {
		ContactEmailRsrc resource = new ContactEmailRsrc();

		resource.setContactEmailId(Integer.valueOf(emailsId));
		resource.setTransactionType(eventType.replace(PoliciesEventTypes.EventTypeNamespace, PoliciesSyncEventTypes.EventTypeNamespace));
		
		return resource;
	}

	//======================================================================================================================
	// CONTACT PHONE
	//======================================================================================================================
	@Override
	public ContactPhoneRsrc getContactPhoneRsrc(TelecomRsrc telecomRsrc, Date eventDate, String eventType) {
		ContactPhoneRsrc resource = new ContactPhoneRsrc();
		
		resource.setContactPhoneId(telecomRsrc.getTelecomId());
		resource.setContactId(telecomRsrc.getContactsId());
		resource.setPhoneNumber(telecomRsrc.getPhoneNumber());
		resource.setExtension(telecomRsrc.getExtension());
		resource.setIsPrimaryPhoneInd(telecomRsrc.getIsPrimaryPhoneInd());
		resource.setIsActive(telecomRsrc.getIsActive());
		resource.setTelecomTypeCode(telecomRsrc.getTelecomTypeCode());
		resource.setDataSyncTransDate(eventDate);
		resource.setTransactionType(eventType.replace(PoliciesEventTypes.EventTypeNamespace, PoliciesSyncEventTypes.EventTypeNamespace));
		
		return resource;

	}
	
	@Override
	public ContactPhoneRsrc getDeleteContactPhoneRsrc(String telecomId, String eventType) {
		ContactPhoneRsrc resource = new ContactPhoneRsrc();

		resource.setContactPhoneId(Integer.valueOf(telecomId));
		resource.setTransactionType(eventType.replace(PoliciesEventTypes.EventTypeNamespace, PoliciesSyncEventTypes.EventTypeNamespace));
		
		return resource;
	}


	//======================================================================================================================
	// COMMODITY TYPE CODE
	//======================================================================================================================
	@Override
	public SyncCommodityTypeCodeRsrc getSyncCommodityTypeCodeRsrc(CommodityTypeCodeSyncRsrc commodityTypeCodeSyncRsrc,
			Date eventDate, String eventType) {
		SyncCommodityTypeCodeRsrc resource = new SyncCommodityTypeCodeRsrc();
		
		resource.setCommodityTypeCode(commodityTypeCodeSyncRsrc.getCommodityTypeCode());
		resource.setCropCommodityId(commodityTypeCodeSyncRsrc.getCropId());
		resource.setDescription(commodityTypeCodeSyncRsrc.getDescription());
		resource.setIsActive(commodityTypeCodeSyncRsrc.getIsActive());
		resource.setDataSyncTransDate(eventDate);
		resource.setTransactionType(eventType.replace(PoliciesEventTypes.EventTypeNamespace, PoliciesSyncEventTypes.EventTypeNamespace));
		
		return resource;

	}
	
	@Override
	public SyncCommodityTypeCodeRsrc getDeleteSyncCommodityTypeCodeRsrc(String commodityTypeCode, Date eventDate, String eventType) {
		SyncCommodityTypeCodeRsrc resource = new SyncCommodityTypeCodeRsrc();

		resource.setCommodityTypeCode(commodityTypeCode);
		resource.setDataSyncTransDate(eventDate);
		resource.setTransactionType(eventType.replace(PoliciesEventTypes.EventTypeNamespace, PoliciesSyncEventTypes.EventTypeNamespace));
		
		return resource;
	}
	
	//======================================================================================================================
	// COMMODITY TYPE VARIETY XREF
	//======================================================================================================================
	@Override
	public SyncCommodityTypeVarietyXrefRsrc getSyncCommodityTypeVarietyXrefRsrc(String commodityTypeCode, 
			String cropVarietyId, Date eventDate, String eventType) {

		SyncCommodityTypeVarietyXrefRsrc resource = new SyncCommodityTypeVarietyXrefRsrc();

		resource.setCommodityTypeCode(commodityTypeCode);
		resource.setCropVarietyId(Integer.valueOf(cropVarietyId));
		resource.setDataSyncTransDate(eventDate);
		resource.setTransactionType(eventType.replace(PoliciesEventTypes.EventTypeNamespace, PoliciesSyncEventTypes.EventTypeNamespace));
		
		return resource;
	}

}
