package ca.bc.gov.mal.cirras.underwriting.data.models;

import java.io.Serializable;
import java.util.Date;

//
// Base class for all outboxes.
//
public abstract class BaseOutbox implements Serializable {
	private static final long serialVersionUID = 1L;

	private String transactionType;
	private Date createDate;

	public String getTransactionType() {
		return transactionType;
	}
		
	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}	
	
	public Date getCreateDate() {
		return createDate;
	}
	
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	
	// Returns the primary key for the source record this outbox points to, as a string.
	abstract public String getSourceKey();
	
	// Returns the primary key for the outbox table.
	abstract public Integer getOutboxKey();

}
