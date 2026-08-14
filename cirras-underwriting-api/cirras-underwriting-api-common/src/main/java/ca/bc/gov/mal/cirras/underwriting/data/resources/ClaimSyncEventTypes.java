package ca.bc.gov.mal.cirras.underwriting.data.resources;

public class ClaimSyncEventTypes {
	
	public static final String EventTypeNamespace = "ca.bc.gov.mal.cirras.underwriting.";
	
	public static final String ClaimCalculationBerriesCreated = EventTypeNamespace + "ClaimCalculationBerriesCreated";    
    public static final String ClaimCalculationBerriesUpdated = EventTypeNamespace + "ClaimCalculationBerriesUpdated";
	public static final String ClaimCalculationBerriesDeleted = EventTypeNamespace + "ClaimCalculationBerriesDeleted";
}
