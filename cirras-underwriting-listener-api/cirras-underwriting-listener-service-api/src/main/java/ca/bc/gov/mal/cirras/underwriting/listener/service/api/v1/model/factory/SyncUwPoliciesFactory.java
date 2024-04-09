package ca.bc.gov.mal.cirras.underwriting.listener.service.api.v1.model.factory;

import java.util.Date;

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

public interface SyncUwPoliciesFactory {

//	//Generic resource
	SyncCodeRsrc getSyncCodeRsrc(CodeSyncRsrc codeSyncRsrc, Date eventDate, String eventType);

	//Grower
	GrowerRsrc getGrowerRsrc(InsuredGrowerRsrc insuredGrower, Date eventDate, String eventType);
	GrowerRsrc getDeleteGrowerRsrc(String growerId, String eventType);
	
	//Policy
	PolicyRsrc getPolicyRsrc(InsurancePolicyRsrc insurancePolicy, Date eventDate, String eventType);
	PolicyRsrc getDeletePolicyRsrc(String policyId, String eventType);

	//Grower Contract Year
	GrowerContractYearSyncRsrc getGrowerContractYearSyncRsrc(GrowerContractYearRsrc growerContractYear, Date eventDate, String eventType);
	GrowerContractYearSyncRsrc getDeleteGrowerContractYearSyncRsrc(String growerContractYearId, String eventType);
	
	//CommodityVariety
	SyncCommodityVarietyRsrc getSyncCommodityVarietyRsrc(CommodityVarietySyncRsrc commodityVarietySyncRsrc, Date eventDate, String eventType);
	SyncCommodityVarietyRsrc getDeleteSyncCommodityVarietyRsrc(String cropId, Date eventDate, String eventType);

	//Contact
	ContactRsrc getContactRsrc(ContactsRsrc contactRsrc, Date eventDate, String eventType);
	ContactRsrc getDeleteContactRsrc(String contactsId, String eventType);

	//Grower Contact
	GrowerContactRsrc getGrowerContactRsrc(InsuredGrowerContactsRsrc insuredGrowerContactRsrc, Date eventDate, String eventType);
	GrowerContactRsrc getDeleteGrowerContactRsrc(String insuredGrowerContactId, String eventType);

	//Contact Email
	ContactEmailRsrc getContactEmailRsrc(EmailsRsrc emailsRsrc, Date eventDate, String eventType);
	ContactEmailRsrc getDeleteContactEmailRsrc(String emailsId, String eventType);

	//Contact Phone
	ContactPhoneRsrc getContactPhoneRsrc(TelecomRsrc telecomRsrc, Date eventDate, String eventType);
	ContactPhoneRsrc getDeleteContactPhoneRsrc(String telecomId, String eventType);

	//Commodity Type Code
	SyncCommodityTypeCodeRsrc getSyncCommodityTypeCodeRsrc(CommodityTypeCodeSyncRsrc commodityTypeCodeSyncRsrc, Date eventDate, String eventType);
	SyncCommodityTypeCodeRsrc getDeleteSyncCommodityTypeCodeRsrc(String commodityTypeCode, Date eventDate, String eventType);
	
	//Commodity Type Variety Xref
	SyncCommodityTypeVarietyXrefRsrc getSyncCommodityTypeVarietyXrefRsrc(String commodityTypeCode, String cropVarietyId, Date eventDate, String eventType);

}
