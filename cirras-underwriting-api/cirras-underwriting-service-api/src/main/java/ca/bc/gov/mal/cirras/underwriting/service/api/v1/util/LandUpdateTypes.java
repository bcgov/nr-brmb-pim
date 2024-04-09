package ca.bc.gov.mal.cirras.underwriting.service.api.v1.util;

public class LandUpdateTypes {

	public static final String NEW_LAND = "NEW_LAND";  //New Legal Land and New Insured Land
	public static final String ADD_NEW_FIELD = "ADD_NEW_FIELD";	//Existing Legal Land and New Insured Land  
	public static final String ADD_EXISTING_LAND = "ADD_EXISTING_LAND";  //Existing Legal Land and Existing Insured Land
	public static final String REPLACE_LEGAL_LOCATION_EXISTING = "REPLACE_LEGAL_LOCATION_EXISTING"; //Replace legal locatin of existing insured land with existing legal land 
	public static final String REPLACE_LEGAL_LOCATION_NEW = "REPLACE_LEGAL_LOCATION_NEW"; //Replace legal locatin of existing insured land with new legal land 
	
	public static final String RENAME_LEGAL_LOCATION = "RENAME_LEGAL_LOCATION";  //Updates the other description for a legal land

	public static final String REMOVE_FIELD_FROM_POLICY = "REMOVE_FIELD_FROM_POLICY";  //Removes field from policy but keeps inventory and comments
	public static final String DELETE_FIELD = "DELETE_FIELD";  //Deletes field completely
}
