package ca.bc.gov.mal.cirras.underwriting.persistence.v1.utils;

public class UnderwritingCodeEnums {
	
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
	
}
