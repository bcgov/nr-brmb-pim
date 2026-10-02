package ca.bc.gov.mal.cirras.underwriting.listener.api.rest.v1.resource.factory;

//PIM-1156: Disabling all code related to cirras-land-management-api so it can be shutdown.

//import java.util.Date;

//import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.AnnualFieldDetailRsrc;
//import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.ContractedFieldDetailRsrc;
//import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.FieldRsrc;
//import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.GrowerContractYearSyncRsrc;
//import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.LegalLandFieldXrefRsrc;

//import ca.bc.gov.mal.cirras.land.management.api.rest.v1.resource.InsuredLandRsrc;
//import ca.bc.gov.mal.cirras.land.management.api.rest.v1.resource.AnnualLotDetailRsrc;
//import ca.bc.gov.mal.cirras.land.management.api.rest.v1.resource.ContractedLotDetailRsrc;
//import ca.bc.gov.mal.cirras.land.management.api.rest.v1.resource.GrowerContractYearRsrc;

import ca.bc.gov.mal.cirras.underwriting.listener.service.api.v1.model.factory.SyncUwLandManagementFactory;
import ca.bc.gov.nrs.wfone.common.rest.endpoints.resource.factory.BaseResourceFactory;

public class SyncUwLandManagementRsrcFactory extends BaseResourceFactory implements SyncUwLandManagementFactory {

/*
	PIM-1156: Disabling all code related to cirras-land-management-api so it can be shutdown.

	//======================================================================================================================
	// Legal Land
	//======================================================================================================================
	@Override
	public ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.LegalLandRsrc getLegalLandSyncRsrc(ca.bc.gov.mal.cirras.land.management.api.rest.v1.resource.LegalLandRsrc legalLand, Date eventDate, String eventType) {
		ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.LegalLandRsrc resource = new ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.LegalLandRsrc();
		
		resource.setLegalLandId(legalLand.getLegalLandId());
		resource.setPrimaryPropertyIdentifier(legalLand.getPrimaryPropertyIdentifier());
		resource.setPrimaryReferenceTypeCode(legalLand.getPrimaryReferenceTypeCode());
		resource.setLegalDescription(legalLand.getLegalDescription());
		resource.setLegalShortDescription(legalLand.getLegalShortDescription());
		resource.setOtherDescription(legalLand.getOtherDescription());
		resource.setActiveFromCropYear(legalLand.getActiveFromCropYear());
		resource.setActiveToCropYear(legalLand.getActiveToCropYear());
		resource.setTransactionType(eventType);
		
		return resource;

	}

	@Override
	public ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.LegalLandRsrc getDeleteLegalLandSyncRsrc(String legalLandId, String eventType) {
		ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.LegalLandRsrc resource = new ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.LegalLandRsrc();

		resource.setLegalLandId(Integer.valueOf(legalLandId));
		resource.setTransactionType(eventType);
		
		return resource;
	}

	//======================================================================================================================
	// FIELD
	//======================================================================================================================
	@Override
	public FieldRsrc getFieldRsrc(InsuredLandRsrc insuredLand, Date eventDate, String eventType) {
		
		FieldRsrc resource = new FieldRsrc();
		
		resource.setFieldId(insuredLand.getLotId());
		resource.setFieldLabel(insuredLand.getLotLabel());
		resource.setActiveFromCropYear(insuredLand.getActiveFromCropYear());
		resource.setActiveToCropYear(insuredLand.getActiveToCropYear());
		resource.setDataSyncTransDate(eventDate);
		resource.setTransactionType(eventType);
		
		return resource;
	}

	@Override
	public FieldRsrc getDeleteFieldRsrc(String lotId, String eventType) {
		
		FieldRsrc resource = new FieldRsrc();

		resource.setFieldId(Integer.valueOf(lotId));
		resource.setTransactionType(eventType);
		
		return resource;

	}

	@Override
	public LegalLandFieldXrefRsrc getLegalLandFieldXrefRsrc(String legalLandId, String lotId, Date eventDate, String eventType) {

		LegalLandFieldXrefRsrc resource = new LegalLandFieldXrefRsrc();

		resource.setLegalLandId(Integer.valueOf(legalLandId));
		resource.setFieldId(Integer.valueOf(lotId));
		resource.setDataSyncTransDate(eventDate);
		resource.setTransactionType(eventType);
		
		return resource;
	}
	
	
	//======================================================================================================================
	// ANNUAL FIELD DETAIL
	//======================================================================================================================
	@Override
	public AnnualFieldDetailRsrc getAnnualFieldDetailRsrc(AnnualLotDetailRsrc annualLotDetail, Date eventDate, String eventType) {
		
		AnnualFieldDetailRsrc resource = new AnnualFieldDetailRsrc();
		
		resource.setAnnualFieldDetailId(annualLotDetail.getAnnualLotDetailId());
		resource.setLegalLandId(annualLotDetail.getLegalLandId());
		resource.setFieldId(annualLotDetail.getLotId());
		resource.setCropYear(annualLotDetail.getCropYear());
		resource.setDataSyncTransDate(eventDate);
		resource.setTransactionType(eventType);
		
		return resource;
	}

	@Override
	public AnnualFieldDetailRsrc getDeleteAnnualFieldDetailRsrc(String annualLotDetailId, String eventType) {
		
		AnnualFieldDetailRsrc resource = new AnnualFieldDetailRsrc();

		resource.setAnnualFieldDetailId(Integer.valueOf(annualLotDetailId));
		resource.setTransactionType(eventType);
		
		return resource;

	}

	//======================================================================================================================
	// Legal Land
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
		resource.setTransactionType(eventType);
		
		return resource;

	}

	@Override
	public GrowerContractYearSyncRsrc getDeleteGrowerContractYearSyncRsrc(String growerContractYearId, String eventType) {
		GrowerContractYearSyncRsrc resource = new GrowerContractYearSyncRsrc();

		resource.setGrowerContractYearId(Integer.valueOf(growerContractYearId));
		resource.setTransactionType(eventType);
		
		return resource;
	}
	
		
	//======================================================================================================================
	// CONTRACTED FIELD DETAIL
	//======================================================================================================================
	@Override
	public ContractedFieldDetailRsrc getContractedFieldDetailRsrc(ContractedLotDetailRsrc contractedLotDetail, Date eventDate, String eventType) {
		
		ContractedFieldDetailRsrc resource = new ContractedFieldDetailRsrc();

		resource.setContractedFieldDetailId(contractedLotDetail.getContractedLotDetailId());
		resource.setAnnualFieldDetailId(contractedLotDetail.getAnnualLotDetailId());
		resource.setGrowerContractYearId(contractedLotDetail.getGrowerContractYearId());
		resource.setDisplayOrder(contractedLotDetail.getDisplayOrder());
		resource.setDataSyncTransDate(eventDate);
		resource.setTransactionType(eventType);
		
		return resource;
	}

	@Override
	public ContractedFieldDetailRsrc getDeleteContractedFieldDetailRsrc(String contractedLotDetailId, String eventType) {
		
		ContractedFieldDetailRsrc resource = new ContractedFieldDetailRsrc();

		resource.setContractedFieldDetailId(Integer.valueOf(contractedLotDetailId));
		resource.setTransactionType(eventType);
		
		return resource;

	}
*/	
}
