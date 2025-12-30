package ca.bc.gov.mal.cirras.underwriting.data.assemblers;

import java.net.URI;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import jakarta.ws.rs.core.UriBuilder;

import ca.bc.gov.nrs.wfone.common.rest.endpoints.resource.factory.BaseResourceFactory;
import ca.bc.gov.mal.cirras.underwriting.controllers.AnnualFieldDetailEndpoint;
import ca.bc.gov.mal.cirras.underwriting.controllers.ContractedFieldDetailEndpoint;
import ca.bc.gov.mal.cirras.underwriting.controllers.FieldEndpoint;
import ca.bc.gov.mal.cirras.underwriting.controllers.GrowerContractYearSyncEndpoint;
import ca.bc.gov.mal.cirras.underwriting.data.resources.AnnualFieldDetailRsrc;
import ca.bc.gov.mal.cirras.underwriting.data.resources.ContractedFieldDetailRsrc;
import ca.bc.gov.mal.cirras.underwriting.data.resources.FieldRsrc;
import ca.bc.gov.mal.cirras.underwriting.data.resources.GrowerContractYearSyncRsrc;
import ca.bc.gov.mal.cirras.underwriting.data.entities.AnnualFieldDetailDto;
import ca.bc.gov.mal.cirras.underwriting.data.entities.ContractedFieldDetailDto;
import ca.bc.gov.mal.cirras.underwriting.data.entities.FieldDto;
import ca.bc.gov.mal.cirras.underwriting.data.entities.GrowerContractYearDto;
import ca.bc.gov.mal.cirras.underwriting.controllers.LegalLandSyncEndpoint;
import ca.bc.gov.mal.cirras.underwriting.data.resources.LegalLandRsrc;
import ca.bc.gov.mal.cirras.underwriting.data.entities.LegalLandDto;
import ca.bc.gov.mal.cirras.underwriting.controllers.LegalLandFieldXrefEndpoint;
import ca.bc.gov.mal.cirras.underwriting.data.resources.LegalLandFieldXrefRsrc;
import ca.bc.gov.mal.cirras.underwriting.data.entities.LegalLandFieldXrefDto;

public class LandDataSyncRsrcFactory extends BaseResourceFactory { 
	
	
	
	
	//======================================================================================================================
	// Legal Land
	//======================================================================================================================

	
	public void updateLegalLand(LegalLandDto dto, LegalLandRsrc model) {

		dto.setLegalLandId(model.getLegalLandId());
		dto.setPrimaryPropertyIdentifier(model.getPrimaryPropertyIdentifier());
		dto.setPrimaryLandIdentifierTypeCode(model.getPrimaryLandIdentifierTypeCode());
		dto.setPrimaryReferenceTypeCode(model.getPrimaryReferenceTypeCode());
		dto.setLegalDescription(model.getLegalDescription());
		dto.setLegalShortDescription(model.getLegalShortDescription());
		dto.setOtherDescription(model.getOtherDescription());
		dto.setActiveFromCropYear(model.getActiveFromCropYear());
		dto.setActiveToCropYear(model.getActiveToCropYear());
		dto.setTotalAcres(model.getTotalAcres());

	}


	
	public LegalLandRsrc getLegalLandSync(LegalLandDto dto) {
		LegalLandRsrc resource = new LegalLandRsrc();
		
		resource.setLegalLandId(dto.getLegalLandId());
		resource.setPrimaryPropertyIdentifier(dto.getPrimaryPropertyIdentifier());
		resource.setPrimaryLandIdentifierTypeCode(dto.getPrimaryLandIdentifierTypeCode());
		resource.setPrimaryReferenceTypeCode(dto.getPrimaryReferenceTypeCode());
		resource.setLegalDescription(dto.getLegalDescription());
		resource.setLegalShortDescription(dto.getLegalShortDescription());
		resource.setOtherDescription(dto.getOtherDescription());
		resource.setActiveFromCropYear(dto.getActiveFromCropYear());
		resource.setActiveToCropYear(dto.getActiveToCropYear());
		resource.setTotalAcres(dto.getTotalAcres());

		return resource;
	}

	
	//======================================================================================================================
	// FIELD
	//======================================================================================================================

	
	public void updateField(FieldDto dto, FieldRsrc model) {

		dto.setFieldId(model.getFieldId());
		dto.setFieldLabel(model.getFieldLabel());
		dto.setLocation(model.getFieldLocation());
		dto.setActiveFromCropYear(model.getActiveFromCropYear());
		dto.setActiveToCropYear(model.getActiveToCropYear());

	}

	
	public FieldRsrc getField(FieldDto dto) {
		FieldRsrc resource = new FieldRsrc();
		
		resource.setFieldId(dto.getFieldId());
		resource.setFieldLabel(dto.getFieldLabel());
		resource.setFieldLocation(dto.getLocation());
		resource.setActiveFromCropYear(dto.getActiveFromCropYear());
		resource.setActiveToCropYear(dto.getActiveToCropYear());

		return resource;
	}
	
	//======================================================================================================================
	// Legal Land - Field Xref
	//======================================================================================================================

	
	public void updateLegalLandFieldXref(LegalLandFieldXrefDto dto, LegalLandFieldXrefRsrc model) {

		dto.setLegalLandId(model.getLegalLandId());
		dto.setFieldId(model.getFieldId());

	}


	
	public LegalLandFieldXrefRsrc getLegalLandFieldXrefSync(LegalLandFieldXrefDto dto) {
		LegalLandFieldXrefRsrc resource = new LegalLandFieldXrefRsrc();
		
		resource.setLegalLandId(dto.getLegalLandId());
		resource.setFieldId(dto.getFieldId());

		return resource;
	}

	//======================================================================================================================
	// Annual Field Detail
	//======================================================================================================================

	
	public void updateAnnualFieldDetail(AnnualFieldDetailDto dto, AnnualFieldDetailRsrc model) {

		dto.setAnnualFieldDetailId(model.getAnnualFieldDetailId());
		dto.setLegalLandId(model.getLegalLandId());
		dto.setFieldId(model.getFieldId());
		dto.setCropYear(model.getCropYear());

	}

	
	public AnnualFieldDetailRsrc getAnnualFieldDetail(AnnualFieldDetailDto dto) {
		AnnualFieldDetailRsrc resource = new AnnualFieldDetailRsrc();
		
		resource.setAnnualFieldDetailId(dto.getAnnualFieldDetailId());
		resource.setLegalLandId(dto.getLegalLandId());
		resource.setFieldId(dto.getFieldId());
		resource.setCropYear(dto.getCropYear());

		return resource;
	}

	
	//======================================================================================================================
	// Grower Contract Year
	//======================================================================================================================

	
	public void updateGrowerContractYear(GrowerContractYearDto dto, GrowerContractYearSyncRsrc model) {

		dto.setGrowerContractYearId(model.getGrowerContractYearId());
		dto.setContractId(model.getContractId());
		dto.setGrowerId(model.getGrowerId());
		dto.setInsurancePlanId(model.getInsurancePlanId());
		dto.setCropYear(model.getCropYear());
		dto.setDataSyncTransDate(model.getDataSyncTransDate());

	}


	
	public GrowerContractYearSyncRsrc getGrowerContractYear(GrowerContractYearDto dto) {
		GrowerContractYearSyncRsrc resource = new GrowerContractYearSyncRsrc();
		
		resource.setGrowerContractYearId(dto.getGrowerContractYearId());
		resource.setContractId(dto.getContractId());
		resource.setGrowerId(dto.getGrowerId());
		resource.setInsurancePlanId(dto.getInsurancePlanId());
		resource.setCropYear(dto.getCropYear());
		resource.setDataSyncTransDate(dto.getDataSyncTransDate());

		return resource;
	}
	
	//======================================================================================================================
	// Contracted Field Detail
	//======================================================================================================================

	
	public void updateContractedFieldDetail(ContractedFieldDetailDto dto, ContractedFieldDetailRsrc model) {

		dto.setContractedFieldDetailId(model.getContractedFieldDetailId());
		dto.setAnnualFieldDetailId(model.getAnnualFieldDetailId());
		dto.setGrowerContractYearId(model.getGrowerContractYearId());
		dto.setDisplayOrder(model.getDisplayOrder());
		dto.setIsLeasedInd(notNull(model.getIsLeasedInd(), false));

	}
	
	private Boolean notNull(Boolean value, Boolean defaultValue) {
		return (value == null) ? defaultValue : value;
	}

	
	public ContractedFieldDetailRsrc getContractedFieldDetail(ContractedFieldDetailDto dto) {
		ContractedFieldDetailRsrc resource = new ContractedFieldDetailRsrc();
		
		resource.setContractedFieldDetailId(dto.getContractedFieldDetailId());
		resource.setAnnualFieldDetailId(dto.getAnnualFieldDetailId());
		resource.setGrowerContractYearId(dto.getGrowerContractYearId());
		resource.setDisplayOrder(dto.getDisplayOrder());
		resource.setIsLeasedInd(dto.getIsLeasedInd());

		return resource;
	}
	
	//======================================================================================================================
	// Self Uris
	//======================================================================================================================


	public static String getLegalLandSelfUri(
			URI baseUri) {

		String result = UriBuilder.fromUri(baseUri)
			.path(LegalLandSyncEndpoint.class)
			.build()
			.toString();

		return result;
	}
	
	public static String getFieldSelfUri(
			URI baseUri) {

		String result = UriBuilder.fromUri(baseUri)
			.path(FieldEndpoint.class)
			.build()
			.toString();

		return result;
	}

	public static String getLegalLandFieldXrefSelfUri(
			URI baseUri) {

		String result = UriBuilder.fromUri(baseUri)
			.path(LegalLandFieldXrefEndpoint.class)
			.build()
			.toString();

		return result;
	}
	
	public static String getAnnualFieldDetailSelfUri(
			URI baseUri) {

		String result = UriBuilder.fromUri(baseUri)
			.path(AnnualFieldDetailEndpoint.class)
			.build()
			.toString();

		return result;
	}

	public static String getGrowerContractYearSelfUri(
			URI baseUri) {

		String result = UriBuilder.fromUri(baseUri)
			.path(GrowerContractYearSyncEndpoint.class)
			.build()
			.toString();

		return result;
	}	
	
	public static String getContractedFieldDetailSelfUri(
			URI baseUri) {

		String result = UriBuilder.fromUri(baseUri)
			.path(ContractedFieldDetailEndpoint.class)
			.build()
			.toString();

		return result;
	}
	
	//Sets effective and expiry date according to the isActive flag in CIRRAS
	private Map<String, Date> calculateDates(Boolean isActive, Date effectiveDate, Date expiryDate) {
		
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
