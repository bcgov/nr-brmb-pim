package ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.factory;

import java.net.URI;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.core.UriBuilder;

import ca.bc.gov.nrs.wfone.common.rest.endpoints.resource.factory.BaseResourceFactory;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.SyncCommodityVarietyRsrc;
import ca.bc.gov.mal.cirras.underwriting.model.v1.SyncCommodityVariety;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.CropVarietyDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.GrowerContactDto;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints.SyncCommodityVarietyEndpoint;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.CommodityTypeCodeDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.CommodityTypeVarietyXrefDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.ContactDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.ContactEmailDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.ContactPhoneDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.CropCommodityDto;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints.ContactEmailEndpoint;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints.ContactEndpoint;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints.ContactPhoneEndpoint;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints.GrowerContactEndpoint;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints.GrowerEndpoint;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints.PolicyEndpoint;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints.ProductEndpoint;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints.SyncCodeEndpoint;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints.SyncCommodityTypeCodeEndpoint;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints.SyncCommodityTypeVarietyXrefEndpoint;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.ContactEmailRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.ContactPhoneRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.ContactRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.GrowerContactRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.GrowerRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.PolicyRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.ProductRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.SyncCommodityTypeCodeRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.SyncCommodityTypeVarietyXrefRsrc;
import ca.bc.gov.mal.cirras.underwriting.model.v1.CommodityTypeCode;
import ca.bc.gov.mal.cirras.underwriting.model.v1.CommodityTypeVarietyXref;
import ca.bc.gov.mal.cirras.underwriting.model.v1.Contact;
import ca.bc.gov.mal.cirras.underwriting.model.v1.ContactEmail;
import ca.bc.gov.mal.cirras.underwriting.model.v1.ContactPhone;
import ca.bc.gov.mal.cirras.underwriting.model.v1.Grower;
import ca.bc.gov.mal.cirras.underwriting.model.v1.GrowerContact;
import ca.bc.gov.mal.cirras.underwriting.model.v1.Policy;
import ca.bc.gov.mal.cirras.underwriting.model.v1.Product;
import ca.bc.gov.mal.cirras.underwriting.model.v1.SyncCode;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.GrowerDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.OfficeDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.PolicyDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.PolicyStatusCodeDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.ProductDto;
import ca.bc.gov.mal.cirras.underwriting.service.api.v1.model.factory.CirrasDataSyncFactory;

public class CirrasDataSyncRsrcFactory extends BaseResourceFactory implements CirrasDataSyncFactory { 
	
	
	//======================================================================================================================
	// Policy Status Code
	//======================================================================================================================

	@Override
	public void updatePolicyStatusCodeExpiryDate(PolicyStatusCodeDto dto, Date dataSyncTransDate) {
		//Don't delete the record but set expiry date to today
		dto.setExpiryDate(new Date());
		dto.setDataSyncTransDate(dataSyncTransDate);
	}
	
	@Override
	public PolicyStatusCodeDto createPolicyStatusCode(SyncCode model) {
		
		PolicyStatusCodeDto dto = new PolicyStatusCodeDto();
		
		Map<String, Date> dates = calculateDates(model.getIsActive(), new Date(), getMaxExpiryDate());
		
		dto.setPolicyStatusCode(model.getUniqueKeyString());
		dto.setDescription(model.getDescription());
		dto.setEffectiveDate(dates.get(this.effectiveDateName));
		dto.setExpiryDate(dates.get(this.expiryDateName));
		dto.setDataSyncTransDate(model.getDataSyncTransDate());
		
		return dto;

	}

	@Override
	public void updatePolicyStatusCode(PolicyStatusCodeDto dto, SyncCode model) {

		Map<String, Date> dates = calculateDates(model.getIsActive(), dto.getEffectiveDate(), dto.getExpiryDate());
		
		dto.setDescription(model.getDescription());
		dto.setEffectiveDate(dates.get(this.effectiveDateName));
		dto.setExpiryDate(dates.get(this.expiryDateName));
		dto.setDataSyncTransDate(model.getDataSyncTransDate());
	}
	
	
	//======================================================================================================================
	// Grower
	//======================================================================================================================

	@Override
	public void updateGrower(GrowerDto dto, Grower model) {

		dto.setGrowerId(model.getGrowerId());
		dto.setGrowerNumber(model.getGrowerNumber());
		dto.setGrowerName(model.getGrowerName());
		dto.setGrowerAddressLine1(model.getGrowerAddressLine1());
		dto.setGrowerAddressLine2(model.getGrowerAddressLine2());
		dto.setGrowerPostalCode(model.getGrowerPostalCode());
		dto.setGrowerCity(model.getGrowerCity());
		dto.setCityId(model.getCityId());
		dto.setGrowerProvince(model.getGrowerProvince());
		dto.setDataSyncTransDate(model.getDataSyncTransDate());
		
	}

	
	@Override
	public Grower getGrower(GrowerDto dto) {
		GrowerRsrc resource = new GrowerRsrc();
		
		resource.setGrowerId(dto.getGrowerId());
		resource.setGrowerNumber(dto.getGrowerNumber());
		resource.setGrowerName(dto.getGrowerName());
		resource.setGrowerAddressLine1(dto.getGrowerAddressLine1());
		resource.setGrowerAddressLine2(dto.getGrowerAddressLine2());
		resource.setGrowerPostalCode(dto.getGrowerPostalCode());
		resource.setGrowerCity(dto.getGrowerCity());
		resource.setCityId(dto.getCityId());
		resource.setGrowerProvince(dto.getGrowerProvince());
		resource.setDataSyncTransDate(dto.getDataSyncTransDate());

		return resource;
	}

	
	//======================================================================================================================
	// Policy
	//======================================================================================================================

	@Override
	public void updatePolicy(PolicyDto dto, Policy model) {

		dto.setPolicyId(model.getPolicyId());
		dto.setGrowerId(model.getGrowerId());
		dto.setInsurancePlanId(model.getInsurancePlanId());
		dto.setOfficeId(model.getOfficeId());
		dto.setPolicyStatusCode(model.getPolicyStatusCode());
		dto.setPolicyNumber(model.getPolicyNumber());
		dto.setContractNumber(model.getContractNumber());
		dto.setContractId(model.getContractId());
		dto.setCropYear(model.getCropYear());
		dto.setDataSyncTransDate(model.getDataSyncTransDate());
		
	}

	
	@Override
	public Policy getPolicy(PolicyDto dto) {
		PolicyRsrc resource = new PolicyRsrc();
		
		resource.setPolicyId(dto.getPolicyId());
		resource.setGrowerId(dto.getGrowerId());
		resource.setInsurancePlanId(dto.getInsurancePlanId());
		resource.setOfficeId(dto.getOfficeId());
		resource.setPolicyStatusCode(dto.getPolicyStatusCode());
		resource.setPolicyNumber(dto.getPolicyNumber());
		resource.setContractNumber(dto.getContractNumber());
		resource.setContractId(dto.getContractId());
		resource.setCropYear(dto.getCropYear());
		resource.setDataSyncTransDate(dto.getDataSyncTransDate());

		return resource;
	}

	//======================================================================================================================
	// Product
	//======================================================================================================================

	@Override
	public void updateProduct(ProductDto dto, Product model) {

		dto.setCommodityCoverageCode(model.getCommodityCoverageCode());
		dto.setCropCommodityId(model.getCropCommodityId());
		dto.setDeductibleLevel(model.getDeductibleLevel());
		dto.setInsuredByMeasType(model.getInsuredByMeasType());
		dto.setPolicyId(model.getPolicyId());
		dto.setProbableYield(model.getProbableYield());
		dto.setProductId(model.getProductId());
		dto.setProductionGuarantee(model.getProductionGuarantee());
		dto.setProductStatusCode(model.getProductStatusCode());
		dto.setInsurableValueHundredPercent(model.getInsurableValueHundredPercent());
		dto.setCoverageDollars(model.getCoverageDollars());
		dto.setDataSyncTransDate(model.getDataSyncTransDate());
	}

	
	@Override
	public Product getProduct(ProductDto dto) {
		ProductRsrc resource = new ProductRsrc();
		
		resource.setCommodityCoverageCode(dto.getCommodityCoverageCode());
		resource.setCropCommodityId(dto.getCropCommodityId());
		resource.setDeductibleLevel(dto.getDeductibleLevel());
		resource.setInsuredByMeasType(dto.getInsuredByMeasType());
		resource.setPolicyId(dto.getPolicyId());
		resource.setProbableYield(dto.getProbableYield());
		resource.setProductId(dto.getProductId());
		resource.setProductionGuarantee(dto.getProductionGuarantee());
		resource.setProductStatusCode(dto.getProductStatusCode());
		resource.setInsurableValueHundredPercent(dto.getInsurableValueHundredPercent());
		resource.setCoverageDollars(dto.getCoverageDollars());
		resource.setDataSyncTransDate(dto.getDataSyncTransDate());

		return resource;
	}
	
	
	//======================================================================================================================
	// Crop Commodity
	//======================================================================================================================
	
	@Override
	public CropCommodityDto createCropCommodity(SyncCommodityVariety model) {
		CropCommodityDto dto = new CropCommodityDto();
		
		dto.setCropCommodityId(model.getCropId());
		dto.setCommodityName(model.getCropName());
		dto.setInsurancePlanId(model.getInsurancePlanId());
		dto.setShortLabel(model.getShortLabel());
		dto.setPlantDurationTypeCode(model.getPlantDurationTypeCode());
		dto.setIsInventoryCropInd(model.getIsInventoryCrop());
		dto.setIsYieldCropInd(model.getIsYieldCrop());
		dto.setIsUnderwritingCropInd(model.getIsUnderwritingCrop());
		dto.setIsProductInsurableInd(model.getIsProductInsurableInd());
		dto.setIsCropInsuranceEligibleInd(model.getIsCropInsuranceEligibleInd());
		dto.setIsPlantInsuranceEligibleInd(model.getIsPlantInsuranceEligibleInd());
		dto.setIsOtherInsuranceEligibleInd(model.getIsOtherInsuranceEligibleInd());
		dto.setYieldMeasUnitTypeCode(model.getYieldMeasUnitTypeCode());
		dto.setYieldDecimalPrecision(model.getYieldDecimalPrecision());
		dto.setEffectiveDate(new Date());
		dto.setExpiryDate(getMaxExpiryDate());
		dto.setDataSyncTransDate(model.getDataSyncTransDate());
		
		return dto;
	}

	@Override
	public void updateCropCommodity(CropCommodityDto dto, SyncCommodityVariety model) {

		dto.setCommodityName(model.getCropName());
		dto.setInsurancePlanId(model.getInsurancePlanId());
		dto.setShortLabel(model.getShortLabel());
		dto.setPlantDurationTypeCode(model.getPlantDurationTypeCode());
		dto.setIsInventoryCropInd(model.getIsInventoryCrop());
		dto.setIsYieldCropInd(model.getIsYieldCrop());
		dto.setIsUnderwritingCropInd(model.getIsUnderwritingCrop());
		dto.setIsProductInsurableInd(model.getIsProductInsurableInd());
		dto.setIsCropInsuranceEligibleInd(model.getIsCropInsuranceEligibleInd());
		dto.setIsPlantInsuranceEligibleInd(model.getIsPlantInsuranceEligibleInd());
		dto.setIsOtherInsuranceEligibleInd(model.getIsOtherInsuranceEligibleInd());
		dto.setYieldMeasUnitTypeCode(model.getYieldMeasUnitTypeCode());
		dto.setYieldDecimalPrecision(model.getYieldDecimalPrecision());
		dto.setDataSyncTransDate(model.getDataSyncTransDate());
		
	}

	@Override
	public void updateCropCommodityExpiryDate(CropCommodityDto dto, Date dataSyncTransDate) {
		//Don't delete the record but set expiry date to today
		dto.setExpiryDate(new Date());
		dto.setDataSyncTransDate(dataSyncTransDate);

	}

	@Override
	public SyncCommodityVariety getSyncCommodityVarietyFromCropCommodity(CropCommodityDto dto) {
		SyncCommodityVarietyRsrc resource = new SyncCommodityVarietyRsrc();
		
		resource.setCropId(dto.getCropCommodityId());
		resource.setCropName(dto.getCommodityName());
		resource.setInsurancePlanId(dto.getInsurancePlanId());
		resource.setShortLabel(dto.getShortLabel());
		resource.setPlantDurationTypeCode(dto.getPlantDurationTypeCode());
		resource.setIsYieldCrop(dto.getIsYieldCropInd());
		resource.setIsUnderwritingCrop(dto.getIsUnderwritingCropInd());
		resource.setIsProductInsurableInd(dto.getIsProductInsurableInd());
		resource.setIsCropInsuranceEligibleInd(dto.getIsCropInsuranceEligibleInd());
		resource.setIsPlantInsuranceEligibleInd(dto.getIsPlantInsuranceEligibleInd());
		resource.setIsOtherInsuranceEligibleInd(dto.getIsOtherInsuranceEligibleInd());
		resource.setYieldMeasUnitTypeCode(dto.getYieldMeasUnitTypeCode());
		resource.setYieldDecimalPrecision(dto.getYieldDecimalPrecision());
		resource.setIsInventoryCrop(dto.getIsInventoryCropInd());
		resource.setDataSyncTransDate(dto.getDataSyncTransDate());
		
		return resource;
	}
	

	//======================================================================================================================
	// Crop Varieties
	//======================================================================================================================

	@Override
	public CropVarietyDto createCropVariety(SyncCommodityVariety model) {
		CropVarietyDto dto = new CropVarietyDto();
		
		dto.setCropVarietyId(model.getCropId());
		dto.setCropCommodityId(model.getParentCropId());
		dto.setVarietyName(model.getCropName());
		dto.setEffectiveDate(new Date());
		dto.setExpiryDate(getMaxExpiryDate());
		dto.setDataSyncTransDate(model.getDataSyncTransDate());
		
		return dto;
	
	}

	@Override
	public void updateCropVariety(CropVarietyDto dto, SyncCommodityVariety model) {

		dto.setCropCommodityId(model.getParentCropId());
		dto.setVarietyName(model.getCropName());
		dto.setDataSyncTransDate(model.getDataSyncTransDate());
		
	}
	
	@Override
	public void updateCropVarietyExpiryDate(CropVarietyDto dto, Date dataSyncTransDate) {
		//Don't delete the record but set expiry date to today
		dto.setExpiryDate(new Date());
		dto.setDataSyncTransDate(dataSyncTransDate);
		
	}

	@Override
	public SyncCommodityVariety getSyncCommodityVarietyFromVariety(CropVarietyDto dto) {
		SyncCommodityVarietyRsrc resource = new SyncCommodityVarietyRsrc();
		
		resource.setCropId(dto.getCropVarietyId());
		resource.setParentCropId(dto.getCropCommodityId());
		resource.setCropName(dto.getVarietyName());
		resource.setDataSyncTransDate(dto.getDataSyncTransDate());

		return resource;
	}
	
	//======================================================================================================================
	// Office
	//======================================================================================================================

	@Override
	public OfficeDto createOffice(OfficeDto dto, SyncCode model) {
		
		dto.setOfficeId(model.getUniqueKeyInteger());
		dto.setOfficeName(model.getDescription());
		dto.setDataSyncTransDate(model.getDataSyncTransDate());
		
		return dto;

	}
	
	//======================================================================================================================
	// Contact
	//======================================================================================================================

	@Override
	public void updateContact(ContactDto dto, Contact model) {

		dto.setContactId(model.getContactId());
		dto.setFirstName(model.getFirstName());
		dto.setLastName(model.getLastName());
		dto.setDataSyncTransDate(model.getDataSyncTransDate());

	}

	@Override
	public Contact getContact(ContactDto dto) {

		ContactRsrc resource = new ContactRsrc();
		
		resource.setContactId(dto.getContactId());
		resource.setFirstName(dto.getFirstName());
		resource.setLastName(dto.getLastName());
		resource.setDataSyncTransDate(dto.getDataSyncTransDate());
		
		return resource;
	}

	//======================================================================================================================
	// Grower Contact
	//======================================================================================================================

	@Override
	public void updateGrowerContact(GrowerContactDto dto, GrowerContact model) {

		dto.setGrowerContactId(model.getGrowerContactId());
		dto.setGrowerId(model.getGrowerId());
		dto.setContactId(model.getContactId());
		dto.setIsPrimaryContactInd(model.getIsPrimaryContactInd());
		dto.setIsActivelyInvolvedInd(model.getIsActivelyInvolvedInd());
		dto.setDataSyncTransDate(model.getDataSyncTransDate());
		
	}

	@Override
	public GrowerContact getGrowerContact(GrowerContactDto dto) {

		GrowerContactRsrc resource = new GrowerContactRsrc();
		
		resource.setGrowerContactId(dto.getGrowerContactId());
		resource.setGrowerId(dto.getGrowerId());
		resource.setContactId(dto.getContactId());
		resource.setIsPrimaryContactInd(dto.getIsPrimaryContactInd());
		resource.setIsActivelyInvolvedInd(dto.getIsActivelyInvolvedInd());
		resource.setDataSyncTransDate(dto.getDataSyncTransDate());

		return resource;
	}

	//======================================================================================================================
	// Contact Email
	//======================================================================================================================

	@Override
	public void updateContactEmail(ContactEmailDto dto, ContactEmail model) {

		Map<String, Date> dates = calculateDates(model.getIsActive(), dto.getEffectiveDate(), dto.getExpiryDate());
		
		dto.setContactEmailId(model.getContactEmailId());
		dto.setContactId(model.getContactId());
		dto.setEmailAddress(model.getEmailAddress());
		dto.setIsPrimaryEmailInd(model.getIsPrimaryEmailInd());
		dto.setEffectiveDate(dates.get(this.effectiveDateName));
		dto.setExpiryDate(dates.get(this.expiryDateName));
		dto.setDataSyncTransDate(model.getDataSyncTransDate());

		
	}

	@Override
	public ContactEmail getContactEmail(ContactEmailDto dto) {

		ContactEmailRsrc resource = new ContactEmailRsrc();
		
		resource.setContactEmailId(dto.getContactEmailId());
		resource.setContactId(dto.getContactId());
		resource.setEmailAddress(dto.getEmailAddress());
		resource.setIsPrimaryEmailInd(dto.getIsPrimaryEmailInd());
		resource.setEffectiveDate(dto.getEffectiveDate());
		resource.setExpiryDate(dto.getExpiryDate());
		resource.setDataSyncTransDate(dto.getDataSyncTransDate());

		return resource;
	}

	//======================================================================================================================
	// Contact Phone
	//======================================================================================================================

	@Override
	public void updateContactPhone(ContactPhoneDto dto, ContactPhone model) {

		Map<String, Date> dates = calculateDates(model.getIsActive(), dto.getEffectiveDate(), dto.getExpiryDate());

		dto.setContactPhoneId(model.getContactPhoneId());
		dto.setContactId(model.getContactId());
		dto.setPhoneNumber(model.getPhoneNumber());
		dto.setExtension(model.getExtension());
		dto.setIsPrimaryPhoneInd(model.getIsPrimaryPhoneInd());
		dto.setEffectiveDate(dates.get(this.effectiveDateName));
		dto.setExpiryDate(dates.get(this.expiryDateName));
		dto.setDataSyncTransDate(model.getDataSyncTransDate());

	}

	@Override
	public ContactPhone getContactPhone(ContactPhoneDto dto) {

		ContactPhoneRsrc resource = new ContactPhoneRsrc();

		resource.setContactPhoneId(dto.getContactPhoneId());
		resource.setContactId(dto.getContactId());
		resource.setPhoneNumber(dto.getPhoneNumber());
		resource.setExtension(dto.getExtension());
		resource.setIsPrimaryPhoneInd(dto.getIsPrimaryPhoneInd());
		resource.setEffectiveDate(dto.getEffectiveDate());
		resource.setExpiryDate(dto.getExpiryDate());
		resource.setDataSyncTransDate(dto.getDataSyncTransDate());

		return resource;
	}

	//======================================================================================================================
	// Commodity Type Code
	//======================================================================================================================
	
	@Override
	public void updateCommodityTypeCodeExpiryDate(CommodityTypeCodeDto dto, Date dataSyncTransDate) {
		//Don't delete the record but set expiry date to today
		dto.setExpiryDate(new Date());
		dto.setDataSyncTransDate(dataSyncTransDate);
		
	}

	@Override
	public void updateCommodityTypeCode(CommodityTypeCodeDto dto, CommodityTypeCode model) {

		Map<String, Date> dates = calculateDates(model.getIsActive(), dto.getEffectiveDate(), dto.getExpiryDate());

		dto.setCommodityTypeCode(model.getCommodityTypeCode());
		dto.setCropCommodityId(model.getCropCommodityId());
		dto.setDescription(model.getDescription());
		dto.setEffectiveDate(dates.get(this.effectiveDateName));
		dto.setExpiryDate(dates.get(this.expiryDateName));
		dto.setDataSyncTransDate(model.getDataSyncTransDate());
		
	}

	@Override
	public CommodityTypeCode getCommodityTypeCode(CommodityTypeCodeDto dto) {
		
		SyncCommodityTypeCodeRsrc resource = new SyncCommodityTypeCodeRsrc();
		
		resource.setCommodityTypeCode(dto.getCommodityTypeCode());
		resource.setCropCommodityId(dto.getCropCommodityId());
		resource.setDescription(dto.getDescription());
		resource.setEffectiveDate(dto.getEffectiveDate());
		resource.setExpiryDate(dto.getExpiryDate());
		resource.setDataSyncTransDate(dto.getDataSyncTransDate());
		
		return resource;
	}

	//======================================================================================================================
	// Commodity Type Variety Xref
	//======================================================================================================================

	@Override
	public void updateCommodityTypeVarietyXref(CommodityTypeVarietyXrefDto dto, CommodityTypeVarietyXref model) {

		dto.setCommodityTypeCode(model.getCommodityTypeCode());
		dto.setCropVarietyId(model.getCropVarietyId());
		dto.setDataSyncTransDate(model.getDataSyncTransDate());
		
	}

	@Override
	public CommodityTypeVarietyXref getCommodityTypeVarietyXref(CommodityTypeVarietyXrefDto dto) {

		SyncCommodityTypeVarietyXrefRsrc resource = new SyncCommodityTypeVarietyXrefRsrc();

		resource.setCommodityTypeCode(dto.getCommodityTypeCode());
		resource.setCropVarietyId(dto.getCropVarietyId());
		resource.setDataSyncTransDate(dto.getDataSyncTransDate());

		return resource;
	}
	
	//======================================================================================================================
	// Self Uris
	//======================================================================================================================
	
	public static String getSyncCodeSelfUri(
			URI baseUri) {

		String result = UriBuilder.fromUri(baseUri)
			.path(SyncCodeEndpoint.class)
			.build()
			.toString();

		return result;
	}

	public static String getGrowerSelfUri(
			URI baseUri) {

		String result = UriBuilder.fromUri(baseUri)
			.path(GrowerEndpoint.class)
			.build()
			.toString();

		return result;
	}

	public static String getPolicySelfUri(
			URI baseUri) {

		String result = UriBuilder.fromUri(baseUri)
			.path(PolicyEndpoint.class)
			.build()
			.toString();

		return result;
	}

	public static String getProductSelfUri(
			URI baseUri) {

		String result = UriBuilder.fromUri(baseUri)
			.path(ProductEndpoint.class)
			.build()
			.toString();

		return result;
	}
	
	
	public static String getSyncCommodityVarietySelfUri(
			URI baseUri) {

		String result = UriBuilder.fromUri(baseUri)
			.path(SyncCommodityVarietyEndpoint.class)
			.build()
			.toString();

		return result;
	}
	
	public static String getContactSelfUri(
			URI baseUri) {

		String result = UriBuilder.fromUri(baseUri)
			.path(ContactEndpoint.class)
			.build()
			.toString();

		return result;
	}
	
	public static String getGrowerContactSelfUri(
			URI baseUri) {

		String result = UriBuilder.fromUri(baseUri)
			.path(GrowerContactEndpoint.class)
			.build()
			.toString();

		return result;
	}
	
	public static String getContactEmailSelfUri(
			URI baseUri) {

		String result = UriBuilder.fromUri(baseUri)
			.path(ContactEmailEndpoint.class)
			.build()
			.toString();

		return result;
	}
	
	public static String getContactPhoneSelfUri(
			URI baseUri) {

		String result = UriBuilder.fromUri(baseUri)
			.path(ContactPhoneEndpoint.class)
			.build()
			.toString();

		return result;
	}	
	
	public static String getCommodityTypeCodeSelfUri(
			URI baseUri) {

		String result = UriBuilder.fromUri(baseUri)
			.path(SyncCommodityTypeCodeEndpoint.class)
			.build()
			.toString();

		return result;
	}	
	
	public static String getCommodityTypeVarietyXrefSelfUri(
			URI baseUri) {

		String result = UriBuilder.fromUri(baseUri)
			.path(SyncCommodityTypeVarietyXrefEndpoint.class)
			.build()
			.toString();

		return result;
	}


	
	//Sets effective and expiry date according to the isActive flag in CIRRAS
	private Map<String, Date> calculateDates(Boolean isActive, Date effectiveDate, Date expiryDate) {
		
		if (effectiveDate == null) {
			effectiveDate = new Date();
		}
		
		if (expiryDate == null) {
			expiryDate = getMaxExpiryDate();
		}
		
		Map<String, Date> dates = new HashMap<>();
		
		LocalDateTime currentDate = LocalDateTime.now(ZoneId.systemDefault());
		LocalDateTime currentExpiryDate = LocalDateTime.ofInstant(expiryDate.toInstant(), ZoneId.systemDefault());
		
		//Default
		Date newEffectiveDate = effectiveDate;
		Date newExpiryDate = expiryDate;
		
		if(isActive && currentDate.isAfter(currentExpiryDate)) {
			//Set to active
			//Inactive to Active: set effective date to now and expiry date to max expiry date 
			newEffectiveDate = new Date();
			newExpiryDate = getMaxExpiryDate();
		} else if (isActive == false && currentDate.isBefore(currentExpiryDate)) {
			//Set to inactive
			//Active to Inactive: set expiry date to now
			newExpiryDate = new Date();
		}
		
		dates.put(this.effectiveDateName, newEffectiveDate);
		dates.put(this.expiryDateName, newExpiryDate);
		
		return dates;
	}
	
	final String effectiveDateName = "effectiveDate";
	final String expiryDateName = "expiryDate";
	
	protected static Date getMaxExpiryDate() {
		Calendar cal = Calendar.getInstance();
		cal.set(9000,12,31);
		
		return cal.getTime();
		
	}

}
