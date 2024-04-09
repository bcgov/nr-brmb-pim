package ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.AnnualFieldRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.InventoryContractRsrc;
import ca.bc.gov.mal.cirras.underwriting.model.v1.AnnualField;
import ca.bc.gov.mal.cirras.underwriting.model.v1.AnnualFieldDetail;
import ca.bc.gov.mal.cirras.underwriting.model.v1.ContractedFieldDetail;
import ca.bc.gov.mal.cirras.underwriting.model.v1.Field;
import ca.bc.gov.mal.cirras.underwriting.model.v1.LegalLand;
import ca.bc.gov.mal.cirras.underwriting.model.v1.LegalLandFieldXref;
import ca.bc.gov.mal.cirras.underwriting.service.api.v1.CirrasInventoryService;
import ca.bc.gov.mal.cirras.underwriting.service.api.v1.LandDataSyncService;
import ca.bc.gov.mal.cirras.underwriting.service.api.v1.util.LandUpdateTypes;
import ca.bc.gov.mal.cirras.underwriting.api.rest.test.EndpointsTest;


public class CreateNewLandServiceTest extends EndpointsTest {
	private static final Logger logger = LoggerFactory.getLogger(CreateNewLandServiceTest.class);

	private Integer growerContractYearId = 95132; //Needs to be an existing id
	private String userId = "Test User";

	@Test
	public void testInsertNewLand() throws Exception {
		logger.debug("<testInsertNewLand");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}

		String fieldLabel = "Field Label Create Land Test";
		String otherLegalDescription = "Other Description Create Land Test";
		Integer cropYear = 2022;
		Integer displayOrder = 25;
		Integer contractedFieldDetailId = null;
		Integer annualFieldDetailId = null;
		Integer legalLandId = null;
		Integer fieldId = null;

		
		CirrasInventoryService invService = (CirrasInventoryService)webApplicationContext.getBean("cirrasInventoryService");

		InventoryContractRsrc inventoryContract = new InventoryContractRsrc();

		inventoryContract.setGrowerContractYearId(growerContractYearId);

		AnnualField annualField = new AnnualFieldRsrc();
		
		annualField.setCropYear(cropYear);
		annualField.setFieldLabel(fieldLabel);
		annualField.setOtherLegalDescription(otherLegalDescription);
		annualField.setDisplayOrder(displayOrder);
		annualField.setLandUpdateType(LandUpdateTypes.NEW_LAND);

		invService.insertNewLand(annualField, inventoryContract, userId);
		
		contractedFieldDetailId = annualField.getContractedFieldDetailId();
		annualFieldDetailId = annualField.getAnnualFieldDetailId();
		legalLandId = annualField.getLegalLandId();
		fieldId = annualField.getFieldId();
		
		LandDataSyncService landDataSyncService = (LandDataSyncService)webApplicationContext.getBean("landDataSyncService");

		//Check Contracted Field Details
		ContractedFieldDetail contractedFieldDetail = landDataSyncService.getContractedFieldDetail(contractedFieldDetailId, null, null);
		
		Assert.assertEquals("ContractedFieldDetailId 1", contractedFieldDetailId, contractedFieldDetail.getContractedFieldDetailId());
		Assert.assertEquals("AnnualFieldDetailId 1", annualFieldDetailId, contractedFieldDetail.getAnnualFieldDetailId());
		Assert.assertEquals("GrowerContractYearId 1", growerContractYearId, contractedFieldDetail.getGrowerContractYearId());
		Assert.assertEquals("DisplayOrder 1", annualField.getDisplayOrder(), contractedFieldDetail.getDisplayOrder());

		//Check Annual Field Details
		AnnualFieldDetail annualFieldDetail = landDataSyncService.getAnnualFieldDetail(annualFieldDetailId, null, null);
		
		Assert.assertEquals("AnnualFieldDetailId 1", annualFieldDetailId, annualFieldDetail.getAnnualFieldDetailId());
		Assert.assertEquals("LegalLandId 1", legalLandId, annualFieldDetail.getLegalLandId());
		Assert.assertEquals("FieldId 1", fieldId, annualFieldDetail.getFieldId());
		Assert.assertEquals("CropYear 1", cropYear, annualFieldDetail.getCropYear());

		//Check Legal Land
		LegalLand legalLand = landDataSyncService.getLegalLand(legalLandId, null, null); 

		Assert.assertEquals("LegalLandId 1", legalLandId, legalLand.getLegalLandId());
		//Assert.assertEquals("PrimaryReferenceTypeCode 1", contractedLotDetailRsrc.getAnnualLotDetail().getLegalLand().getPrimaryReferenceTypeCode(), legalLand.getPrimaryReferenceTypeCode());
		Assert.assertNull("LegalDescription 1", legalLand.getLegalDescription());
		Assert.assertNull("LegalShortDescription 1", legalLand.getLegalShortDescription());
		Assert.assertEquals("OtherDescription 1", otherLegalDescription, legalLand.getOtherDescription());
		Assert.assertEquals("ActiveFromCropYear 1", cropYear, legalLand.getActiveFromCropYear());
		Assert.assertNull("ActiveToCropYear 1", legalLand.getActiveToCropYear());
		
		//Check Field 
		Field field = landDataSyncService.getField(fieldId, null, null); 

		Assert.assertEquals("FieldId 1", fieldId, field.getFieldId());
		Assert.assertEquals("Field Label 1", fieldLabel, field.getFieldLabel());
		Assert.assertEquals("ActiveFromCropYear 1", cropYear, field.getActiveFromCropYear());
		Assert.assertNull("ActiveToCropYear 1", field.getActiveToCropYear());

		//Check Legal Land Field Xref
		LegalLandFieldXref xref = landDataSyncService.getLegalLandFieldXref(legalLandId, fieldId, null, null);
		
		Assert.assertEquals("FieldId 1", fieldId, xref.getFieldId());
		Assert.assertEquals("LegalLandId 1", legalLandId, xref.getLegalLandId());

		//delete data
		landDataSyncService.deleteLegalLandFieldXref(legalLandId, fieldId, null, null);
		landDataSyncService.deleteContractedFieldDetail(contractedFieldDetailId, null, null);
		landDataSyncService.deleteAnnualFieldDetail(annualFieldDetailId, null, null);
		landDataSyncService.deleteLegalLand(legalLandId, null, null);
		landDataSyncService.deleteField(fieldId, null, null);
		
		logger.debug(">testInsertNewLand");
	}
	
	@Test
	public void testCreateLand() throws Exception {
		logger.debug("<testCreateLand");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}
		
		String fieldLabel = "UW API Unit Test";
		String otherLegalDescription = "UW API Unit Test Legal";
		
		CirrasInventoryService invService = (CirrasInventoryService)webApplicationContext.getBean("cirrasInventoryService");

		AnnualField annualField = new AnnualFieldRsrc();
		
		//CREATE Contracted Lot Detail
		InventoryContractRsrc inventoryContract = new InventoryContractRsrc();

		inventoryContract.setGrowerContractYearId(growerContractYearId);
		
		annualField.setCropYear(2022);
		annualField.setFieldLabel(fieldLabel);
		annualField.setOtherLegalDescription(otherLegalDescription);
		annualField.setDisplayOrder(25);
		annualField.setLandUpdateType(LandUpdateTypes.NEW_LAND);

		invService.insertNewLand(annualField, inventoryContract, userId);
				
		logger.debug(">testCreateLand");
	}	
	
	@Test
	public void testGeneratePID() throws Exception {
		logger.debug("<testGeneratePID");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}
		
		CirrasInventoryService invService = (CirrasInventoryService)webApplicationContext.getBean("cirrasInventoryService");

		String newPID = invService.generatePID();
		
		Assert.assertEquals("PID not 12 characters", newPID.length(), 12);
		Assert.assertEquals("First letters of PID not correct", "GF", newPID.substring(0, 2));
				
		logger.debug(">testGeneratePID");
	}	

}
