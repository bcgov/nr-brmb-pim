package ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource;

import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlSeeAlso;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.types.ResourceTypes;
import ca.bc.gov.mal.cirras.underwriting.model.v1.UnderwritingYear;
import ca.bc.gov.nrs.common.wfone.rest.resource.BaseResource;

@XmlRootElement(namespace = ResourceTypes.NAMESPACE, name = ResourceTypes.UNDERWRITING_YEAR_NAME)
@XmlSeeAlso({ UnderwritingYearRsrc.class })
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "@type")
public class UnderwritingYearRsrc extends BaseResource implements UnderwritingYear {

	private static final long serialVersionUID = 1L;
	
	private String underwritingYearGuid;
	private Integer cropYear;

	public String getUnderwritingYearGuid() {
		return underwritingYearGuid;
	}
	
	public void setUnderwritingYearGuid(String underwritingYearGuid) {
		this.underwritingYearGuid = underwritingYearGuid;
	}

	public Integer getCropYear() {
		return cropYear;
	}
	public void setCropYear(Integer cropYear) {
		this.cropYear = cropYear;
	}
}
