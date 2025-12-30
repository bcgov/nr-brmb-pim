package ca.bc.gov.mal.cirras.underwriting.data.models;

import java.io.Serializable;

public interface UnderwritingYear extends Serializable {
	
	public String getUnderwritingYearGuid();
	public void setUnderwritingYearGuid(String underwritingYearGuid);

	public Integer getCropYear();
	public void setCropYear(Integer cropYear);

}
