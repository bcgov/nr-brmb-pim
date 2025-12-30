package ca.bc.gov.mal.cirras.underwriting.data.resources;

import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlSeeAlso;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

import ca.bc.gov.mal.cirras.underwriting.data.resources.types.ResourceTypes;
import ca.bc.gov.mal.cirras.underwriting.data.models.Field;
import ca.bc.gov.nrs.common.wfone.rest.resource.BaseResource;

@XmlRootElement(namespace = ResourceTypes.NAMESPACE, name = ResourceTypes.FIELD_NAME)
@XmlSeeAlso({ FieldRsrc.class })
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "@type")
public class FieldRsrc extends BaseResource implements Field {

	private static final long serialVersionUID = 1L;

	private Integer fieldId;
	private String fieldLabel;
	private String fieldLocation;
	private Integer activeFromCropYear;
	private Integer activeToCropYear;
	private Integer totalLegalLand;
	private String transactionType;

 	public Integer getFieldId() {
		return fieldId;
	}

	public void setFieldId(Integer fieldId) {
		this.fieldId = fieldId;
	}
 
 	public String getFieldLabel() {
		return fieldLabel;
	}

	public void setFieldLabel(String fieldLabel) {
		this.fieldLabel = fieldLabel;
	}
	
	public String getFieldLocation() {
		return fieldLocation;
	}

	public void setFieldLocation(String fieldLocation) {
		this.fieldLocation = fieldLocation;
	}
 
 	public Integer getActiveFromCropYear() {
		return activeFromCropYear;
	}

	public void setActiveFromCropYear(Integer activeFromCropYear) {
		this.activeFromCropYear = activeFromCropYear;
	}
 
 	public Integer getActiveToCropYear() {
		return activeToCropYear;
	}

	public void setActiveToCropYear(Integer activeToCropYear) {
		this.activeToCropYear = activeToCropYear;
	}
	
	@Override
	public Integer getTotalLegalLand() {
		return totalLegalLand;
	}
	
	@Override
	public void setTotalLegalLand(Integer totalLegalLand) {
		this.totalLegalLand = totalLegalLand;
	}

	@Override
	public String getTransactionType() {
		return transactionType;
	}

	@Override
	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}
}
