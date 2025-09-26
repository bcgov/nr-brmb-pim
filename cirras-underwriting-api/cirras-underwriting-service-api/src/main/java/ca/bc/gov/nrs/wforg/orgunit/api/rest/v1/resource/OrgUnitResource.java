package ca.bc.gov.nrs.wforg.orgunit.api.rest.v1.resource;

import java.util.Date;

import jakarta.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import ca.bc.gov.nrs.common.rest.resource.BaseResource;
import ca.bc.gov.nrs.wforg.orgunit.api.rest.v1.resource.types.ResourceTypes;
import ca.bc.gov.nrs.wforg.orgunit.model.v1.OrgUnit;

@XmlRootElement(namespace = ResourceTypes.NAMESPACE, name = ResourceTypes.ORG_UNIT_NAME)
@JsonSubTypes({ @Type(value = OrgUnitResource.class, name = ResourceTypes.ORG_UNIT) })
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "@type")
public class OrgUnitResource extends BaseResource implements OrgUnit {
	private static final long serialVersionUID = 1L;

	private String orgUnitIdentifier;
	private String orgUnitTypeCode;
	private String name;
	private Integer integerAlias;
	private String characterAlias;	
	private Date effectiveDate;
	private Date expiryDate;

	public String getOrgUnitIdentifier() {
		return orgUnitIdentifier;
	}

	public void setOrgUnitIdentifier(String orgUnitIdentifier) {
		this.orgUnitIdentifier = orgUnitIdentifier;
	}

	@Override
	public String getOrgUnitTypeCode() {
		return orgUnitTypeCode;
	}

	@Override
	public void setOrgUnitTypeCode(String orgUnitTypeCode) {
		this.orgUnitTypeCode = orgUnitTypeCode;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public Integer getIntegerAlias() {
		return integerAlias;
	}

	@Override
	public void setIntegerAlias(Integer integerAlias) {
		this.integerAlias = integerAlias;
	}

	@Override
	public String getCharacterAlias() {
		return characterAlias;
	}

	@Override
	public void setCharacterAlias(String characterAlias) {
		this.characterAlias = characterAlias;
	}

	@Override
	public Date getEffectiveDate() {
		return effectiveDate;
	}

	@Override
	public void setEffectiveDate(Date effectiveDate) {
		this.effectiveDate = effectiveDate;
	}

	@Override
	public Date getExpiryDate() {
		return expiryDate;
	}

	@Override
	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}

}
