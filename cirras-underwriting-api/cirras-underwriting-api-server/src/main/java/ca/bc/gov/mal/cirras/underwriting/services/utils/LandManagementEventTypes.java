package ca.bc.gov.mal.cirras.underwriting.services.utils;

public class LandManagementEventTypes {
	
	public static final String EventTypeNamespace = "ca.bc.gov.mal.cirras.underwriting";
	
	public static final String PolicyCreated = EventTypeNamespace + "PolicyCreated";    
    public static final String PolicyUpdated = EventTypeNamespace + "PolicyUpdated";
	public static final String PolicyDeleted = EventTypeNamespace + "PolicyDeleted";

	public static final String LegalLandCreated = EventTypeNamespace + "LegalLandCreated";    
    public static final String LegalLandUpdated = EventTypeNamespace + "LegalLandUpdated";
	public static final String LegalLandDeleted = EventTypeNamespace + "LegalLandDeleted";
	
	public static final String FieldCreated = EventTypeNamespace + "FieldCreated";    
    public static final String FieldUpdated = EventTypeNamespace + "FieldUpdated";
	public static final String FieldDeleted = EventTypeNamespace + "FieldDeleted";

	// There is no update event for LegalLandField. Instead it is recorded as delete then insert.
	public static final String LegalLandFieldXrefCreated = EventTypeNamespace + "LegalLandFieldXrefCreated";    
	public static final String LegalLandFieldXrefDeleted = EventTypeNamespace + "LegalLandFieldXrefDeleted";

	public static final String AnnualFieldDetailCreated = EventTypeNamespace + "AnnualFieldDetailCreated";    
    public static final String AnnualFieldDetailUpdated = EventTypeNamespace + "AnnualFieldDetailUpdated";
	public static final String AnnualFieldDetailDeleted = EventTypeNamespace + "AnnualFieldDetailDeleted";

	public static final String GrowerContractYearCreated = EventTypeNamespace + "GrowerContractYearCreated";    
    public static final String GrowerContractYearUpdated = EventTypeNamespace + "GrowerContractYearUpdated";
	public static final String GrowerContractYearDeleted = EventTypeNamespace + "GrowerContractYearDeleted";
 
	public static final String ContractedFieldDetailCreated = EventTypeNamespace + "ContractedFieldDetailCreated";    
    public static final String ContractedFieldDetailUpdated = EventTypeNamespace + "ContractedFieldDetailUpdated";
	public static final String ContractedFieldDetailDeleted = EventTypeNamespace + "ContractedFieldDetailDeleted";

}
