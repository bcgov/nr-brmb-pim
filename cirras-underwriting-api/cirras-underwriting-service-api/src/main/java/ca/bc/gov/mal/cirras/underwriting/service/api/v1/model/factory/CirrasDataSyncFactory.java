package ca.bc.gov.mal.cirras.underwriting.service.api.v1.model.factory;

import java.util.Date;

import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.CommodityTypeCodeDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.CommodityTypeVarietyXrefDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.ContactDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.ContactEmailDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.ContactPhoneDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.CropCommodityDto;
import ca.bc.gov.mal.cirras.underwriting.model.v1.SyncCommodityVariety;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.CropVarietyDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.GrowerContactDto;
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

public interface CirrasDataSyncFactory {

	//Policy Status Code
	void updatePolicyStatusCodeExpiryDate(PolicyStatusCodeDto dto, Date dataSyncTransDate);
	PolicyStatusCodeDto createPolicyStatusCode(SyncCode model);
	void updatePolicyStatusCode(PolicyStatusCodeDto dto, SyncCode model);

	//Grower
	void updateGrower(GrowerDto dto, Grower model);
	Grower getGrower(GrowerDto dto);

	//Policy
	void updatePolicy(PolicyDto dto, Policy model);
	Policy getPolicy(PolicyDto dto);

	//Product
	void updateProduct(ProductDto dto, Product model);
	Product getProduct(ProductDto dto);
	
	//Commodities
	CropCommodityDto createCropCommodity(SyncCommodityVariety model);
	void updateCropCommodity(CropCommodityDto dto, SyncCommodityVariety model);
	SyncCommodityVariety getSyncCommodityVarietyFromCropCommodity(CropCommodityDto dto);
	void updateCropCommodityExpiryDate(CropCommodityDto dto, Date dataSyncTransDate);

	//Varieties
	CropVarietyDto createCropVariety(SyncCommodityVariety model);
	void updateCropVariety(CropVarietyDto dto, SyncCommodityVariety model);
	SyncCommodityVariety getSyncCommodityVarietyFromVariety(CropVarietyDto dto);
	void updateCropVarietyExpiryDate(CropVarietyDto dto, Date dataSyncTransDate);
	
	//Office
	OfficeDto createOffice(OfficeDto dto, SyncCode model);

	//Contact
	void updateContact(ContactDto dto, Contact model);
	Contact getContact(ContactDto dto);

	//Grower Contact
	void updateGrowerContact(GrowerContactDto dto, GrowerContact model);
	GrowerContact getGrowerContact(GrowerContactDto dto);
	
	//Contact Email
	void updateContactEmail(ContactEmailDto dto, ContactEmail model);
	ContactEmail getContactEmail(ContactEmailDto dto);
	
	//Contact Phone
	void updateContactPhone(ContactPhoneDto dto, ContactPhone model);
	ContactPhone getContactPhone(ContactPhoneDto dto);

	//Commodity Type Code
	void updateCommodityTypeCodeExpiryDate(CommodityTypeCodeDto dto, Date dataSyncTransDate);
	void updateCommodityTypeCode(CommodityTypeCodeDto dto, CommodityTypeCode model);
	CommodityTypeCode getCommodityTypeCode(CommodityTypeCodeDto dto);

	//Commodity Type Variety Xref
	void updateCommodityTypeVarietyXref(CommodityTypeVarietyXrefDto dto, CommodityTypeVarietyXref model);
	CommodityTypeVarietyXref getCommodityTypeVarietyXref(CommodityTypeVarietyXrefDto dto);

}
