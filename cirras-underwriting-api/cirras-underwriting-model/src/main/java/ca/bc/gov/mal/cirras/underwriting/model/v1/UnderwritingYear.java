package ca.bc.gov.mal.cirras.underwriting.model.v1;

import java.io.Serializable;

public interface UnderwritingYear extends Serializable {
	
	public String getUnderwritingYearGuid();
	public void setUnderwritingYearGuid(String underwritingYearGuid);

	public Integer getCropYear();
	public void setCropYear(Integer cropYear);

}
