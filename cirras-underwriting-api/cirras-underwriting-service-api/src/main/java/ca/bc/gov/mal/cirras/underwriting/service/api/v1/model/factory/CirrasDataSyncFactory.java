package ca.bc.gov.mal.cirras.underwriting.services.model.factory;

import java.util.Date;

import ca.bc.gov.mal.cirras.underwriting.data.entities.CommodityTypeCodeDto;
import ca.bc.gov.mal.cirras.underwriting.data.entities.CommodityTypeVarietyXrefDto;
import ca.bc.gov.mal.cirras.underwriting.data.entities.ContactDto;
import ca.bc.gov.mal.cirras.underwriting.data.entities.ContactEmailDto;
import ca.bc.gov.mal.cirras.underwriting.data.entities.ContactPhoneDto;
import ca.bc.gov.mal.cirras.underwriting.data.entities.CropCommodityDto;
import ca.bc.gov.mal.cirras.underwriting.data.models.SyncCommodityVariety;
import ca.bc.gov.mal.cirras.underwriting.data.entities.CropVarietyDto;
import ca.bc.gov.mal.cirras.underwriting.data.entities.GrowerContactDto;
import ca.bc.gov.mal.cirras.underwriting.data.models.CommodityTypeCode;
import ca.bc.gov.mal.cirras.underwriting.data.models.CommodityTypeVarietyXref;
import ca.bc.gov.mal.cirras.underwriting.data.models.Contact;
import ca.bc.gov.mal.cirras.underwriting.data.models.ContactEmail;
import ca.bc.gov.mal.cirras.underwriting.data.models.ContactPhone;
import ca.bc.gov.mal.cirras.underwriting.data.models.Grower;
import ca.bc.gov.mal.cirras.underwriting.data.models.GrowerContact;
import ca.bc.gov.mal.cirras.underwriting.data.models.Policy;
import ca.bc.gov.mal.cirras.underwriting.data.models.Product;
import ca.bc.gov.mal.cirras.underwriting.data.models.SyncCode;
import ca.bc.gov.mal.cirras.underwriting.data.entities.GrowerDto;
import ca.bc.gov.mal.cirras.underwriting.data.entities.OfficeDto;
import ca.bc.gov.mal.cirras.underwriting.data.entities.PolicyDto;
import ca.bc.gov.mal.cirras.underwriting.data.entities.PolicyStatusCodeDto;
import ca.bc.gov.mal.cirras.underwriting.data.entities.ProductDto;

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
