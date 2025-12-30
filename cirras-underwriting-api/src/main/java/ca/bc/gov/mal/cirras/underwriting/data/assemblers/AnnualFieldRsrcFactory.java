package ca.bc.gov.mal.cirras.underwriting.data.assemblers;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import jakarta.ws.rs.core.UriBuilder;

import ca.bc.gov.nrs.common.wfone.rest.resource.RelLink;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.rest.endpoints.resource.factory.BaseResourceFactory;
import ca.bc.gov.nrs.wfone.common.service.api.model.factory.FactoryContext;
import ca.bc.gov.nrs.wfone.common.service.api.model.factory.FactoryException;
import ca.bc.gov.nrs.wfone.common.webade.authentication.WebAdeAuthentication;
import ca.bc.gov.mal.cirras.underwriting.controllers.AnnualFieldListEndpoint;
import ca.bc.gov.mal.cirras.underwriting.controllers.AnnualFieldRolloverInvEndpoint;
import ca.bc.gov.mal.cirras.underwriting.controllers.scopes.Scopes;
import ca.bc.gov.mal.cirras.underwriting.data.resources.AnnualFieldListRsrc;
import ca.bc.gov.mal.cirras.underwriting.data.resources.AnnualFieldRsrc;
import ca.bc.gov.mal.cirras.underwriting.data.resources.types.ResourceTypes;
import ca.bc.gov.mal.cirras.underwriting.data.models.PolicySimple;
import ca.bc.gov.mal.cirras.underwriting.data.entities.ContractedFieldDetailDto;
import ca.bc.gov.mal.cirras.underwriting.data.entities.FieldDto;
import ca.bc.gov.mal.cirras.underwriting.data.entities.PolicyDto;

public class AnnualFieldRsrcFactory extends BaseResourceFactory { 
	
	//======================================================================================================================
	// Annual Field List (Annual Field Search)
	//======================================================================================================================
	
	
	public AnnualFieldListRsrc getAnnualFieldList(
			List<FieldDto> dtos,
			Integer legalLandId, 
			Integer cropYear,
			FactoryContext context, 
			WebAdeAuthentication authentication
		)  throws FactoryException, DaoException {
		
		AnnualFieldListRsrc result = null;
		
		List<AnnualFieldRsrc> resources = new ArrayList<AnnualFieldRsrc>();
		
		URI baseUri = getBaseURI(context);

		for (FieldDto dto : dtos) {
			AnnualFieldRsrc resource = new AnnualFieldRsrc();
						
			populateResource(resource, dto);
			
			//Policies
			if (!dto.getPolicies().isEmpty()) {
				List<PolicySimple> policies = new ArrayList<PolicySimple>();

				for (PolicyDto pDto : dto.getPolicies()) {
					PolicySimple policyModel = createPolicy(pDto, cropYear);
					policies.add(policyModel);
				}

				resource.setPolicies(policies);
			}
			
			setLinks(resource, baseUri, authentication);

			resources.add(resource);
		}
		
		result = new AnnualFieldListRsrc();
		result.setCollection(resources);
		
		String eTag = getEtag(result);
		result.setETag(eTag);		

		
		return result;
	}



	static PolicySimple createPolicy(PolicyDto dto, Integer cropYear) {
		
		PolicySimple model = new PolicySimple();

		model.setPolicyId(dto.getPolicyId());
		model.setPolicyNumber(dto.getPolicyNumber());
		model.setCropYear(dto.getCropYear());
		model.setInsurancePlanId(dto.getInsurancePlanId());
		model.setInsurancePlanName(dto.getInsurancePlanName());
		model.setGrowerContractYearId(dto.getGrowerContractYearId());
		//Set inventory contract guid if the source policy is of the same crop year as the target policy
		if(dto.getCropYear() != null && dto.getCropYear().equals(cropYear)) {
			model.setInventoryContractGuid(dto.getInventoryContractGuid());
			model.setDeclaredYieldContractGuid(dto.getDeclaredYieldContractGuid());
		} else {
			model.setInventoryContractGuid(null);
		}

		model.setGrowerId(dto.getGrowerId());
		model.setGrowerNumber(dto.getGrowerNumber());
		model.setGrowerName(dto.getGrowerName());
		
		return model;
	}	

	static void populateResource(AnnualFieldRsrc resource, FieldDto dto) {

		resource.setFieldId(dto.getFieldId());
		resource.setFieldLabel(dto.getFieldLabel());
		resource.setFieldLocation(dto.getLocation());
		resource.setOtherLegalDescription(dto.getOtherLegalDescription());
		resource.setLegalLandId(dto.getLegalLandId());
		resource.setPrimaryPropertyIdentifier(dto.getPrimaryPropertyIdentifier());
	}

	static void populateResource(AnnualFieldRsrc resource, ContractedFieldDetailDto dto) {
		resource.setAnnualFieldDetailId(dto.getAnnualFieldDetailId());
		resource.setContractedFieldDetailId(dto.getContractedFieldDetailId());
		resource.setCropYear(dto.getCropYear());
		resource.setDisplayOrder(dto.getDisplayOrder());
		resource.setFieldId(dto.getFieldId());
		resource.setFieldLabel(dto.getFieldLabel());
		resource.setLegalLandId(dto.getLegalLandId());
		resource.setOtherLegalDescription(dto.getOtherLegalDescription());
		resource.setIsLeasedInd(dto.getIsLeasedInd());
		resource.setFieldLocation(dto.getFieldLocation());
		resource.setPrimaryPropertyIdentifier(dto.getPrimaryPropertyIdentifier()); 
	}

	// Only supports updates to members that are editable on the Inventory screen.
	
	public void updateDto(ContractedFieldDetailDto dto, AnnualFieldRsrc model) {
		dto.setDisplayOrder(model.getDisplayOrder());
		dto.setIsLeasedInd(model.getIsLeasedInd());
	}
	
	public static String getAnnualFieldListSelfUri(
		Integer legalLandId,
		Integer cropYear, 
		URI baseUri) {

		String result = UriBuilder.fromUri(baseUri)
			.path(AnnualFieldListEndpoint.class)
			.queryParam("legalLandId", nvl(toString(legalLandId), ""))
			.queryParam("cropYear", nvl(toString(cropYear), ""))
			.build()
			.toString();

		return result;
	}
	
	private static void setLinks(
			AnnualFieldRsrc resource, 
			URI baseUri, 
			WebAdeAuthentication authentication) {
		
		if (resource.getFieldId() != null 
			&& authentication.hasAuthority(Scopes.CREATE_INVENTORY_CONTRACT)) {

			String result = UriBuilder
					.fromUri(baseUri)
					.path(AnnualFieldRolloverInvEndpoint.class)
					.queryParam("rolloverToCropYear", "")
					.queryParam("insurancePlanId", "")
					.build(resource.getFieldId()).toString();
			resource.getLinks().add(new RelLink(ResourceTypes.ROLLOVER_ANNUAL_FIELD_INVENTORY, result, "GET"));
		}		
		
	}

	
	protected static String nvl(Integer value, String defaultValue) {
		return (value==null)?defaultValue:value.toString();
	}
	
}
