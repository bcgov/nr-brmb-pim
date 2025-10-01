package ca.bc.gov.nrs.wforg.orgunit.api.rest.v1.resource;

import java.util.ArrayList;
import java.util.List;

import jakarta.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import ca.bc.gov.nrs.wforg.orgunit.api.rest.v1.resource.types.ResourceTypes;
import ca.bc.gov.nrs.wforg.orgunit.model.v1.OrgUnitDetails;

@XmlRootElement(namespace = ResourceTypes.NAMESPACE, name = ResourceTypes.ORG_UNIT_DETAILS_NAME)
@JsonSubTypes({ @Type(value = OrgUnitDetailsResource.class, name = ResourceTypes.ORG_UNIT_DETAILS) })
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "@type")
public class OrgUnitDetailsResource extends OrgUnitResource implements OrgUnitDetails<OrgUnitResource> {
	private static final long serialVersionUID = 1L;

	private OrgUnitResource reportsToOrgUnit;
	private List<OrgUnitResource> responsibleForOrgUnits;

	public OrgUnitDetailsResource() {

		this.responsibleForOrgUnits = new ArrayList<OrgUnitResource>();
	}

	public OrgUnitResource getReportsToOrgUnit() {
		return reportsToOrgUnit;
	}

	public void setReportsToOrgUnit(OrgUnitResource reportsToOrgUnit) {
		this.reportsToOrgUnit = reportsToOrgUnit;
	}

	public List<OrgUnitResource> getResponsibleForOrgUnits() {
		return responsibleForOrgUnits;
	}

	public void setResponsibleForOrgUnits(List<OrgUnitResource> responsibleForOrgUnits) {
		this.responsibleForOrgUnits = responsibleForOrgUnits;
	}
}
