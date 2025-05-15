package ca.bc.gov.mal.cirras.underwriting.service.api.v1.util;

public class InventoryServiceEnums {
	
	public enum InsurancePlans{
		GRAPES(1),
		TREEFRUITS(2),
		BERRIES(3),
		GRAIN(4),
		FORAGE(5),
		VEGETABLES(6),
		FLOWERS(8);
		
		private Integer insurancePlanId;
		 
		InsurancePlans(Integer ipId) {
	        this.insurancePlanId = ipId;
	    }
	 
	    public Integer getInsurancePlanId() {
	        return insurancePlanId;
	    }
	}
	
	public enum TransactionTypes{
		Inserted("INSERTED"),
		Updated("UPDATED"),
		Deleted("DELETED");
		
		private String transactionType;
		 
		TransactionTypes(String trnType) {
	        this.transactionType = trnType;
	    }
	 
	    public String getCode() {
	        return transactionType;
	    }
		
	}

	public enum InventoryCalculationType {
		Full,
		IncrementalAdd,
		IncrementalSubtract
	}
	
	public enum PlantDurationType {
		ANNUAL,
		PERENNIAL
	}
	
	public enum LinkPlantingType{
		ADD_LINK,
		REMOVE_LINK
	}

	// Valid options for reportType parameter for Grain Inventory Report.
	public enum InventoryReportType {
		unseeded,
		seeded;
	}
	
	// Valid options for screenType parameter for GET UwContractRsrc.
	public enum ScreenType {
		INVENTORY,
		DOP,
		VERIFIED;
	}
	
	public enum AmendmentTypeCode {
		Appraisal,
		Assessment
	}	
}
