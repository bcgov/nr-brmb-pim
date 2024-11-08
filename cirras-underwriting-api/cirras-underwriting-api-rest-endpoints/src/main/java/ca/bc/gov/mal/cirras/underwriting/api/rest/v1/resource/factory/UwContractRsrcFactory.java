package ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.factory;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.UriBuilder;

import ca.bc.gov.nrs.common.wfone.rest.resource.MessageRsrc;
import ca.bc.gov.nrs.common.wfone.rest.resource.RelLink;
import ca.bc.gov.nrs.wfone.common.model.Message;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dto.PagedDtos;
import ca.bc.gov.nrs.wfone.common.rest.endpoints.resource.factory.BaseResourceFactory;
import ca.bc.gov.nrs.wfone.common.service.api.model.factory.FactoryContext;
import ca.bc.gov.nrs.wfone.common.service.api.model.factory.FactoryException;
import ca.bc.gov.nrs.wfone.common.webade.authentication.WebAdeAuthentication;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints.DopYieldContractEndpoint;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints.InventoryContractEndpoint;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints.UwContractEndpoint;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints.UwContractListEndpoint;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints.UwContractRolloverDopYieldEndpoint;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints.UwContractRolloverInvEndpoint;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints.UwContractRolloverVerifiedYieldEndpoint;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints.UwContractValidateAddFieldEndpoint;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints.UwContractValidateRemoveFieldEndpoint;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints.UwContractValidateRenameLegalEndpoint;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints.UwContractValidateReplaceLegalEndpoint;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints.VerifiedYieldContractEndpoint;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints.security.Scopes;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.AddFieldValidationRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.AnnualFieldRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.LegalLandRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.RemoveFieldValidationRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.RenameLegalValidationRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.ReplaceLegalValidationRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.UwContractListRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.UwContractRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.types.ResourceTypes;
import ca.bc.gov.mal.cirras.underwriting.model.v1.AddFieldValidation;
import ca.bc.gov.mal.cirras.underwriting.model.v1.AnnualField;
import ca.bc.gov.mal.cirras.underwriting.model.v1.Field;
import ca.bc.gov.mal.cirras.underwriting.model.v1.LegalLand;
import ca.bc.gov.mal.cirras.underwriting.model.v1.PolicySimple;
import ca.bc.gov.mal.cirras.underwriting.model.v1.RemoveFieldValidation;
import ca.bc.gov.mal.cirras.underwriting.model.v1.RenameLegalValidation;
import ca.bc.gov.mal.cirras.underwriting.model.v1.ReplaceLegalValidation;
import ca.bc.gov.mal.cirras.underwriting.model.v1.UwContract;
import ca.bc.gov.mal.cirras.underwriting.model.v1.UwContractList;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.FieldDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.LegalLandDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.PolicyDto;
import ca.bc.gov.mal.cirras.underwriting.service.api.v1.model.factory.UwContractFactory;
import ca.bc.gov.mal.cirras.underwriting.service.api.v1.util.InventoryServiceEnums;

public class UwContractRsrcFactory extends BaseResourceFactory implements UwContractFactory { 
	
	//======================================================================================================================
	// Uw Contract List (Uw Contract Search)
	//======================================================================================================================

	@Override
	public UwContractList<? extends UwContract<? extends UwContract<?>>> getUwContractList(
			PagedDtos<PolicyDto> dtos,
			Integer cropYear,
			Integer insurancePlanId,
			Integer officeId,
			String policyStatusCode,
			String policyNumber,
			String growerInfo,
    		String datasetType,
			String sortColumn,
			String sortDirection,
			Integer pageRowCount,
			FactoryContext context, 
			WebAdeAuthentication authentication
	) throws FactoryException, DaoException {
		
		URI baseUri = getBaseURI(context);
		
		UwContractListRsrc result = null;
		
		List<UwContractRsrc> resources = new ArrayList<UwContractRsrc>();

		for (PolicyDto dto : dtos.getResults()) {
			UwContractRsrc resource = new UwContractRsrc();
						
			populateResource(resource, dto);

			setSelfLink(dto.getPolicyId(), false, resource, baseUri);
			setLinks(resource, baseUri, authentication);

			resources.add(resource);
		}
		
		int pageNumber = dtos.getPageNumber();
		int totalRowCount = dtos.getTotalRowCount();
		pageRowCount = Integer.valueOf(pageRowCount==null?totalRowCount:pageRowCount.intValue());
		int totalPageCount = (int) Math.ceil(((double)totalRowCount)/((double)pageRowCount.intValue()));
		
		result = new UwContractListRsrc();
		result.setCollection(resources);
		result.setPageNumber(pageNumber);
		result.setPageRowCount(pageRowCount.intValue());
		result.setTotalRowCount(totalRowCount);
		result.setTotalPageCount(totalPageCount);
		
		String eTag = getEtag(result);
		result.setETag(eTag);		
		
		setSelfLink(
				result, 
				cropYear,
				insurancePlanId,
				officeId,
				policyStatusCode,
				policyNumber,
				growerInfo,
				datasetType,
				sortColumn,
				sortDirection,
				pageNumber, 
				pageRowCount.intValue(), 
				baseUri);
		
		setLinks(result, 
				cropYear,
				insurancePlanId,
				officeId,
				policyStatusCode,
				policyNumber,
				growerInfo,
				datasetType,
				sortColumn,
				sortDirection,
				pageNumber,
				pageRowCount.intValue(), 
				totalRowCount, 
				baseUri,
				authentication);
		
		return result;
	}

	@Override
	public UwContract<? extends UwContract<?>> getUwContract(
			PolicyDto dto, 
			List<PolicyDto> linkedPolicyDtos,
			Boolean loadLinkedPolicies,
			FactoryContext context, 
			WebAdeAuthentication authentication
		) throws FactoryException {

		URI baseUri = getBaseURI(context);
		
		UwContractRsrc resource = new UwContractRsrc();

		populateResource(resource, dto);

		// Linked Policies
		if ( linkedPolicyDtos != null && !linkedPolicyDtos.isEmpty() ) {
			List<UwContractRsrc> linkedPoliciesModel = new ArrayList<UwContractRsrc>();

			for (PolicyDto lpDto : linkedPolicyDtos) {

				UwContractRsrc linkedPolicyModel = new UwContractRsrc();
				populateResource(linkedPolicyModel, lpDto);				
				setSelfLink(lpDto.getPolicyId(), false, linkedPolicyModel, baseUri);
				setLinks(linkedPolicyModel, baseUri, authentication);				

				linkedPoliciesModel.add(linkedPolicyModel);
			}

			resource.setLinkedPolicies(linkedPoliciesModel);
		}
		
		
		String eTag = getEtag(resource);
		resource.setETag(eTag);

		setSelfLink(dto.getPolicyId(), loadLinkedPolicies, resource, baseUri);
		setLinks(resource, baseUri, authentication);

		return resource;
	}
	
	private void populateResource(UwContractRsrc resource, PolicyDto dto) {

		resource.setPolicyId(dto.getPolicyId());
		resource.setGrowerId(dto.getGrowerId());
		resource.setInsurancePlanId(dto.getInsurancePlanId());
		resource.setPolicyStatusCode(dto.getPolicyStatusCode());
		resource.setPolicyNumber(dto.getPolicyNumber());
		resource.setContractNumber(dto.getContractNumber());
		resource.setContractId(dto.getContractId());
		resource.setCropYear(dto.getCropYear());
		resource.setGrowerContractYearId(dto.getGrowerContractYearId());
		resource.setInsurancePlanName(dto.getInsurancePlanName());
		resource.setPolicyStatus(dto.getPolicyStatus());
		resource.setGrowerNumber(dto.getGrowerNumber());
		resource.setGrowerName(dto.getGrowerName());
		resource.setGrowerPrimaryEmail(dto.getGrowerPrimaryEmail());
		resource.setGrowerPrimaryPhone(dto.getGrowerPrimaryPhone());
		resource.setInventoryContractGuid(dto.getInventoryContractGuid());
		resource.setDeclaredYieldContractGuid(dto.getDeclaredYieldContractGuid());
		resource.setVerifiedYieldContractGuid(dto.getVerifiedYieldContractGuid());
		resource.setTotalDopEligibleInventory(dto.getTotalDopEligibleInventory());

		// TODO: Add remaining fields for resource (or remove from resource if not needed):
		// growerAddressLine1
		// growerAddressLine2
		// growerCity
		// growerPostalCode
		// growerProvince

	}

	static void setSelfLink(Integer policyId, Boolean loadLinkedPolicies, UwContractRsrc resource, URI baseUri) {
		if (policyId != null) {
			String selfUri = getUwContractSelfUri(policyId, loadLinkedPolicies, baseUri);
			resource.getLinks().add(new RelLink(ResourceTypes.SELF, selfUri, "GET"));
		}
	}

	public static String getUwContractSelfUri(Integer policyId, Boolean loadLinkedPolicies, URI baseUri) {
		String result = UriBuilder
				.fromUri(baseUri)
				.path(UwContractEndpoint.class)
				.queryParam("loadLinkedPolicies", nvl(toString(loadLinkedPolicies), ""))
				.build(policyId).toString();
		return result;
	}
	
	private static void setLinks(
		UwContractRsrc resource, 
		URI baseUri, 
		WebAdeAuthentication authentication) {

		if (resource.getInventoryContractGuid() != null && authentication.hasAuthority(Scopes.GET_INVENTORY_CONTRACT)) {
			String result = UriBuilder
				.fromUri(baseUri)
				.path(InventoryContractEndpoint.class)
				.build(resource.getInventoryContractGuid()).toString();
			resource.getLinks().add(new RelLink(ResourceTypes.INVENTORY_CONTRACT, result, "GET"));
			
		}
		else if (resource.getInventoryContractGuid() == null 
				&& resource.getPolicyId() != null 
				&& (resource.getInsurancePlanName().equals(InventoryServiceEnums.InsurancePlans.GRAIN.toString())
					|| resource.getInsurancePlanName().equals(InventoryServiceEnums.InsurancePlans.FORAGE.toString())) 
				&& authentication.hasAuthority(Scopes.CREATE_INVENTORY_CONTRACT)) {
			// Inventory does not exist, but could be rolled over.
			// TODO: Should perhaps be checking plan based on UnderwritingCommodity table or something rather than hard-coding.
			String result = UriBuilder
					.fromUri(baseUri)
					.path(UwContractRolloverInvEndpoint.class)
					.build(resource.getPolicyId()).toString();
			resource.getLinks().add(new RelLink(ResourceTypes.ROLLOVER_INVENTORY_CONTRACT, result, "GET"));
		}
		
		//Add dop yield url if there is an inventory contract with eligible inventory (at least one field with seeded commodity, seeded acres and not hidden on printout
		if (resource.getInventoryContractGuid() != null && resource.getTotalDopEligibleInventory() > 0) {

			if (resource.getDeclaredYieldContractGuid() != null && authentication.hasAuthority(Scopes.GET_DOP_YIELD_CONTRACT)) {
				String result = UriBuilder
					.fromUri(baseUri)
					.path(DopYieldContractEndpoint.class)
					.build(resource.getDeclaredYieldContractGuid()).toString();
				resource.getLinks().add(new RelLink(ResourceTypes.DOP_YIELD_CONTRACT, result, "GET"));				
			}
			else if(resource.getDeclaredYieldContractGuid() == null 
				&& resource.getPolicyId() != null 
				&& (resource.getInsurancePlanName().equals(InventoryServiceEnums.InsurancePlans.GRAIN.toString())
					|| resource.getInsurancePlanName().equals(InventoryServiceEnums.InsurancePlans.FORAGE.toString())) 
				&& authentication.hasAuthority(Scopes.CREATE_DOP_YIELD_CONTRACT)) {
				// Yield DOP does not exist, but could be rolled over.
				// TODO: Should perhaps be checking plan based on UnderwritingCommodity table or something rather than hard-coding.
				String result = UriBuilder
						.fromUri(baseUri)
						.path(UwContractRolloverDopYieldEndpoint.class)
						.build(resource.getPolicyId()).toString();
				resource.getLinks().add(new RelLink(ResourceTypes.ROLLOVER_DOP_YIELD_CONTRACT, result, "GET"));
			}
		}

		//Add verified yield url if there is a dop contract
		if (resource.getDeclaredYieldContractGuid() != null ) {

			if (resource.getVerifiedYieldContractGuid() != null && authentication.hasAuthority(Scopes.GET_VERIFIED_YIELD_CONTRACT)) {

				String result = UriBuilder
					.fromUri(baseUri)
					.path(VerifiedYieldContractEndpoint.class)
					.build(resource.getVerifiedYieldContractGuid()).toString();
				resource.getLinks().add(new RelLink(ResourceTypes.VERIFIED_YIELD_CONTRACT, result, "GET"));
			}
			else if(resource.getVerifiedYieldContractGuid() == null 
				&& resource.getPolicyId() != null 
				&& resource.getInsurancePlanName().equals(InventoryServiceEnums.InsurancePlans.GRAIN.toString())
				&& authentication.hasAuthority(Scopes.CREATE_VERIFIED_YIELD_CONTRACT)) {
				// Verified Yield does not exist, but could be rolled over.
				String result = UriBuilder
						.fromUri(baseUri)
						.path(UwContractRolloverVerifiedYieldEndpoint.class)
						.build(resource.getPolicyId()).toString();
				resource.getLinks().add(new RelLink(ResourceTypes.ROLLOVER_VERIFIED_YIELD_CONTRACT, result, "GET"));
			}
		}

		// TODO: Should perhaps be checking plan based on UnderwritingCommodity table or something rather than hard-coding.
		if (resource.getPolicyId() != null 
				&& (resource.getInsurancePlanName().equals(InventoryServiceEnums.InsurancePlans.GRAIN.toString()) 
						|| resource.getInsurancePlanName().equals(InventoryServiceEnums.InsurancePlans.FORAGE.toString()))
				&& authentication.hasAuthority(Scopes.CREATE_INVENTORY_CONTRACT)) {

			// Check for warnings or errors that would result from adding a given field to this policy.
			String result = getAddFieldValidationSelfUri(resource.getPolicyId(), null, null, baseUri);
			resource.getLinks().add(new RelLink(ResourceTypes.ADD_FIELD_VALIDATION, result, "GET"));

			// Check for warnings or errors that would result from removing a given field from this policy.
			result = getRemoveFieldValidationSelfUri(resource.getPolicyId(), null, baseUri);
			resource.getLinks().add(new RelLink(ResourceTypes.REMOVE_FIELD_VALIDATION, result, "GET"));
			
			// Check for warnings or errors that would result from renaming the legal location for a field on this policy.
			result = getRenameLegalValidationSelfUri(resource.getPolicyId(), null, null, baseUri);
			resource.getLinks().add(new RelLink(ResourceTypes.RENAME_LEGAL_VALIDATION, result, "GET"));

			// Check for warnings that would result from replacing the legal location for a field on this policy.
			result = getReplaceLegalValidationSelfUri(resource.getPolicyId(), null, null, null, baseUri);
			resource.getLinks().add(new RelLink(ResourceTypes.REPLACE_LEGAL_VALIDATION, result, "GET"));
		}
	}

	
	public static String getUwContractListSelfUri(
		Integer cropYear,
		Integer insurancePlanId,
		Integer officeId,
		String policyStatusCode,
		String policyNumber,
		String growerInfo,
		String datasetType,
		String sortColumn,
		String sortDirection,
		Integer pageNumber, 
		Integer pageRowCount, 
		URI baseUri) {

		String result = UriBuilder.fromUri(baseUri)
			.path(UwContractListEndpoint.class)
			.queryParam("cropYear", nvl(toString(cropYear), ""))
			.queryParam("insurancePlanId", nvl(toString(insurancePlanId), ""))
			.queryParam("officeId", nvl(toString(officeId), ""))
			.queryParam("policyStatusCode", nvl(policyStatusCode, ""))
			.queryParam("policyNumber", nvl(policyNumber, ""))
			.queryParam("growerInfo", nvl(growerInfo, ""))
			.queryParam("datasetType", nvl(datasetType, ""))
			.queryParam("sortColumn", nvl(sortColumn, ""))
			.queryParam("sortDirection", nvl(sortDirection, ""))
			.queryParam("pageNumber", nvl(toString(pageNumber), ""))
			.queryParam("pageRowCount", nvl(toString(pageRowCount), ""))
			.build()
			.toString();

		return result;
	}
	
	private static void setSelfLink(
		UwContractListRsrc resource, 
		Integer cropYear,
		Integer insurancePlanId,
		Integer officeId,
		String policyStatusCode,
		String policyNumber,
		String growerInfo,
		String datasetType,
		String sortColumn,
		String sortDirection,
		int pageNumber, 
		int pageRowCount, 
		URI baseUri) {
		
		String selfUri = getUwContractListSelfUri(
				cropYear,
				insurancePlanId,
				officeId,
				policyStatusCode,
				policyNumber,
				growerInfo,
				datasetType,
				sortColumn,
				sortDirection,
				Integer.valueOf(pageNumber), 
				Integer.valueOf(pageRowCount), 
				baseUri);
		
		resource.getLinks().add(new RelLink(ResourceTypes.SELF, selfUri, "GET"));
	}	

	private static void setLinks(
		UwContractListRsrc resource, 
		Integer cropYear,
		Integer insurancePlanId,
		Integer officeId,
		String policyStatusCode,
		String policyNumber,
		String growerInfo,
		String datasetType,
		String sortColumn,
		String sortDirection,
		int pageNumber, 
		int pageRowCount, 
		int totalRowCount,
		URI baseUri, 
		WebAdeAuthentication authentication) {
		
		if(pageNumber > 1) {
			String previousUri = getUwContractListSelfUri(
					cropYear,
					insurancePlanId,
					officeId,
					policyStatusCode,
					policyNumber,
					growerInfo,
					datasetType,
					sortColumn,
					sortDirection,
					Integer.valueOf(pageNumber-1), 
					Integer.valueOf(pageRowCount), 
					baseUri);
			resource.getLinks().add(new RelLink(ResourceTypes.PREV, previousUri, "GET"));
		}
		
		if((pageNumber * pageRowCount) < totalRowCount) {
			String nextUri = getUwContractListSelfUri(
					cropYear,
					insurancePlanId,
					officeId,
					policyStatusCode,
					policyNumber,
					growerInfo,
					datasetType,
					sortColumn,
					sortDirection,
					Integer.valueOf(pageNumber+1),
					Integer.valueOf(pageRowCount), 
					baseUri);
			resource.getLinks().add(new RelLink(ResourceTypes.NEXT, nextUri, "GET"));
		}
	}

	@Override
	public AddFieldValidation<? extends Message> getAddFieldValidation(List<String> warnings, List<String> errors, Integer policyId, Integer fieldId, Integer transferFromPolicyId,
			FactoryContext context, WebAdeAuthentication authentication) throws FactoryException {
		AddFieldValidationRsrc resource = new AddFieldValidationRsrc();

		if (!warnings.isEmpty()) {
			resource.setWarningMessages(createMessageList(warnings));
		}
		
		if (!errors.isEmpty()) {
			resource.setErrorMessages(createMessageList(errors));
		}

		String eTag = getEtag(resource);
		resource.setETag(eTag);

		URI baseUri = getBaseURI(context);
		setSelfLink(policyId, fieldId, transferFromPolicyId, resource, baseUri);

		return resource;
	}

	List<MessageRsrc> createMessageList(List<String> messages) {
		List<MessageRsrc> messagesRsrc = new ArrayList<MessageRsrc>();
		
		for (String msg : messages) {
			MessageRsrc messageRsrc = new MessageRsrc(msg);
			messagesRsrc.add(messageRsrc);
		}
		
		return messagesRsrc;
	}
	
	static void setSelfLink(Integer policyId, Integer fieldId, Integer transferFromPolicyId, AddFieldValidationRsrc resource, URI baseUri) {
		if (policyId != null && fieldId != null) {
			String selfUri = getAddFieldValidationSelfUri(policyId, fieldId, transferFromPolicyId, baseUri);
			resource.getLinks().add(new RelLink(ResourceTypes.SELF, selfUri, "GET"));
		}
	}

	public static String getAddFieldValidationSelfUri(Integer policyId, Integer fieldId, Integer transferFromPolicyId, URI baseUri) {
		String result = UriBuilder.fromUri(baseUri)
				.path(UwContractValidateAddFieldEndpoint.class)
				.queryParam("fieldId", nvl(toString(fieldId), ""))
				.queryParam("transferFromPolicyId", nvl(toString(transferFromPolicyId), ""))
				.build(policyId)
				.toString();		
		return result;	
	}

	@Override
	public RemoveFieldValidation<? extends Message> getRemoveFieldValidation(
			Boolean isRemoveFromPolicyAllowed,
			Boolean isDeleteFieldAllowed, 
			List<String> removeFromPolicyWarnings, 
			List<String> deleteFieldErrors,
			Integer policyId, 
			Integer fieldId, 
			FactoryContext context, 
			WebAdeAuthentication authentication
	) throws FactoryException {

		RemoveFieldValidationRsrc resource = new RemoveFieldValidationRsrc();

		resource.setIsRemoveFromPolicyAllowed(isRemoveFromPolicyAllowed);
		resource.setIsDeleteFieldAllowed(isDeleteFieldAllowed);
		
		if (!removeFromPolicyWarnings.isEmpty()) {
			resource.setRemoveFromPolicyWarnings(createMessageList(removeFromPolicyWarnings));
		}
		
		if (!deleteFieldErrors.isEmpty()) {
			resource.setDeleteFieldErrors(createMessageList(deleteFieldErrors));
		}

		String eTag = getEtag(resource);
		resource.setETag(eTag);

		URI baseUri = getBaseURI(context);
		setSelfLink(policyId, fieldId, resource, baseUri);

		return resource;
	}

	static void setSelfLink(Integer policyId, Integer fieldId, RemoveFieldValidationRsrc resource, URI baseUri) {
		if (policyId != null && fieldId != null) {
			String selfUri = getRemoveFieldValidationSelfUri(policyId, fieldId, baseUri);
			resource.getLinks().add(new RelLink(ResourceTypes.SELF, selfUri, "GET"));
		}
	}

	public static String getRemoveFieldValidationSelfUri(Integer policyId, Integer fieldId, URI baseUri) {
		String result = UriBuilder.fromUri(baseUri)
				.path(UwContractValidateRemoveFieldEndpoint.class)
				.queryParam("fieldId", nvl(toString(fieldId), ""))
				.build(policyId)
				.toString();		
		return result;	
	}
	
	@Override
	public RenameLegalValidation<? extends Message, ? extends LegalLand<? extends Field>, ? extends AnnualField> getRenameLegalValidation(
		Boolean isWarningLegalsWithSameLoc, String legalsWithSameLocMsg, List<LegalLandDto> legalsWithSameLocList,
		Boolean isWarningOtherFieldOnPolicy, String otherFieldOnPolicyMsg, List<FieldDto> otherFieldOnPolicyList,
		Boolean isWarningFieldOnOtherPolicy, String fieldOnOtherPolicyMsg, List<FieldDto> fieldOnOtherPolicyList,
		Boolean isWarningOtherLegalData, String otherLegalDataMsg, LegalLandDto otherLegalData, 
		Integer policyId, Integer annualFieldDetailId, String newLegalLocation, 
		FactoryContext context, WebAdeAuthentication authentication) throws FactoryException {

		RenameLegalValidationRsrc resource = new RenameLegalValidationRsrc();

		// LegalsWithSameLoc
		if (isWarningLegalsWithSameLoc) {
			resource.setIsWarningLegalsWithSameLoc(true);
			resource.setLegalsWithSameLocMsg(new MessageRsrc(legalsWithSameLocMsg));

			List<LegalLandRsrc> llList = new ArrayList<LegalLandRsrc>();

			for (LegalLandDto llDto : legalsWithSameLocList) {
				LegalLandRsrc llModel = new LegalLandRsrc();
							
				LegalLandRsrcFactory.populateResource(llModel, llDto);

				llList.add(llModel);
			}
			
			resource.setLegalsWithSameLocList(llList);

		} else {
			resource.setIsWarningLegalsWithSameLoc(false);
			resource.setLegalsWithSameLocMsg(null);
			resource.setLegalsWithSameLocList(null);
		}
		
		// OtherFieldOnPolicy
		if (isWarningOtherFieldOnPolicy) {
			resource.setIsWarningOtherFieldOnPolicy(true);
			resource.setOtherFieldOnPolicyMsg(new MessageRsrc(otherFieldOnPolicyMsg));

			List<AnnualFieldRsrc> annualFields = new ArrayList<AnnualFieldRsrc>();
			
			for (FieldDto fDto : otherFieldOnPolicyList) {
				annualFields.add(createAnnualField(fDto));
			}
			
			resource.setOtherFieldOnPolicyList(annualFields);
			
		} else {
			resource.setIsWarningOtherFieldOnPolicy(false);
			resource.setOtherFieldOnPolicyMsg(null);
			resource.setOtherFieldOnPolicyList(null);
		}
		

		// FieldOnOtherPolicy
		if (isWarningFieldOnOtherPolicy) {
			resource.setIsWarningFieldOnOtherPolicy(true);
			resource.setFieldOnOtherPolicyMsg(new MessageRsrc(fieldOnOtherPolicyMsg));

			List<AnnualFieldRsrc> annualFields = new ArrayList<AnnualFieldRsrc>();
			
			for (FieldDto fDto : fieldOnOtherPolicyList) {
				annualFields.add(createAnnualField(fDto));
			}
			
			resource.setFieldOnOtherPolicyList(annualFields);
			
		} else {
			resource.setIsWarningFieldOnOtherPolicy(false);
			resource.setFieldOnOtherPolicyMsg(null);
			resource.setFieldOnOtherPolicyList(null);
		}
		
		
		// OtherLegalData
		if (isWarningOtherLegalData) {
			resource.setIsWarningOtherLegalData(true);
			resource.setOtherLegalDataMsg(new MessageRsrc(otherLegalDataMsg));
			
			LegalLandRsrc llModel = new LegalLandRsrc();			
			LegalLandRsrcFactory.populateResource(llModel, otherLegalData);			
			resource.setOtherLegalData(llModel);

		} else {
			resource.setIsWarningOtherLegalData(false);
			resource.setOtherLegalDataMsg(null);
			resource.setOtherLegalData(null);
			
		}
		
		String eTag = getEtag(resource);
		resource.setETag(eTag);

		URI baseUri = getBaseURI(context);
		setSelfLink(policyId, annualFieldDetailId, newLegalLocation, resource, baseUri);
		
		return resource;
		
	}
	
	@Override
	public ReplaceLegalValidation<? extends Message, ? extends LegalLand<? extends Field>, ? extends AnnualField> getReplaceLegalValidation(
			Boolean isWarningFieldOnOtherPolicy,
			String fieldOnOtherPolicyMsg,
			Boolean isWarningFieldHasOtherLegalLand,
			String fieldHasOtherLegalLandMsg, 
			List<LegalLandDto> otherLegalLandOfFieldList,
			Boolean isWarningOtherFieldsOnLegal, 
			String otherFieldsOnLegalMsg, 
			List<FieldDto> otherFieldsOnLegalLandDtos,
			Integer policyId, 
			Integer annualFieldDetailId, 
			String fieldLabel,
			Integer legalLandId, 
			FactoryContext context, 
			WebAdeAuthentication authentication
			) throws FactoryException {

		ReplaceLegalValidationRsrc resource = new ReplaceLegalValidationRsrc();

		// FieldOnOtherPolicy
		if (isWarningFieldOnOtherPolicy) {
			resource.setIsWarningFieldOnOtherPolicy(true);
			resource.setFieldOnOtherPolicyMsg(new MessageRsrc(fieldOnOtherPolicyMsg));
			
		} else {
			resource.setIsWarningFieldOnOtherPolicy(false);
			resource.setFieldOnOtherPolicyMsg(null);
		}

		//Field associated with other legal land
		if (isWarningFieldHasOtherLegalLand) {
			resource.setIsWarningFieldHasOtherLegalLand(true);
			resource.setFieldHasOtherLegalLandMsg(new MessageRsrc(fieldHasOtherLegalLandMsg));

			List<LegalLandRsrc> llList = new ArrayList<LegalLandRsrc>();

			for (LegalLandDto llDto : otherLegalLandOfFieldList) {
				LegalLandRsrc llModel = new LegalLandRsrc();
							
				LegalLandRsrcFactory.populateResource(llModel, llDto);

				llList.add(llModel);
			}
			
			resource.setOtherLegalLandOfFieldList(llList);

		} else {
			resource.setIsWarningFieldHasOtherLegalLand(false);
			resource.setFieldHasOtherLegalLandMsg(null);
			resource.setOtherLegalLandOfFieldList(null);
		}
		
		//Other fields associated with legal land
		if (isWarningOtherFieldsOnLegal) {
			resource.setIsWarningOtherFieldsOnLegal(true);
			resource.setOtherFieldsOnLegalMsg(new MessageRsrc(otherFieldsOnLegalMsg));

			List<AnnualFieldRsrc> annualFields = new ArrayList<AnnualFieldRsrc>();
			
			for (FieldDto fDto : otherFieldsOnLegalLandDtos) {
				annualFields.add(createAnnualField(fDto));
			}
			
			resource.setOtherFieldsOnLegalLandList(annualFields);
			
		} else {
			resource.setIsWarningOtherFieldsOnLegal(false);
			resource.setOtherFieldsOnLegalMsg(null);
			resource.setOtherFieldsOnLegalLandList(null);
		}

		String eTag = getEtag(resource);
		resource.setETag(eTag);

		URI baseUri = getBaseURI(context);
		setSelfLink(policyId, annualFieldDetailId, fieldLabel, legalLandId, resource, baseUri);
		
		return resource;
		
	}
	

	private AnnualFieldRsrc createAnnualField(FieldDto dto) {

		AnnualFieldRsrc resource = new AnnualFieldRsrc();
						
		AnnualFieldRsrcFactory.populateResource(resource, dto);
			
		//Policies
		if (!dto.getPolicies().isEmpty()) {
			List<PolicySimple> policies = new ArrayList<PolicySimple>();

			for (PolicyDto pDto : dto.getPolicies()) {
				PolicySimple policyModel = AnnualFieldRsrcFactory.createPolicy(pDto, null);
				policies.add(policyModel);
			}

			resource.setPolicies(policies);
		}
		
		return resource;
	}
	
	static void setSelfLink(Integer policyId, Integer annualFieldDetailId, String fieldLabel,
			Integer legalLandId, ReplaceLegalValidationRsrc resource, URI baseUri) {
		if (policyId != null && annualFieldDetailId != null && fieldLabel != null) {
			String selfUri = getReplaceLegalValidationSelfUri(policyId, annualFieldDetailId, fieldLabel, legalLandId, baseUri);
			resource.getLinks().add(new RelLink(ResourceTypes.SELF, selfUri, "GET"));
		}
	}
	
	public static String getReplaceLegalValidationSelfUri(Integer policyId, Integer annualFieldDetailId, 
			String fieldLabel, Integer legalLandId, URI baseUri) {
		String result = UriBuilder.fromUri(baseUri)
				.path(UwContractValidateReplaceLegalEndpoint.class)
				.queryParam("annualFieldDetailId", nvl(toString(annualFieldDetailId), ""))
				.queryParam("fieldLabel", nvl(fieldLabel, ""))
				.queryParam("legalLandId", nvl(toString(legalLandId), ""))
				.build(policyId)
				.toString();		
		return result;
	}
	
	
	static void setSelfLink(Integer policyId, Integer annualFieldDetailId, String newLegalLocation, RenameLegalValidationRsrc resource, URI baseUri) {
		if (policyId != null && annualFieldDetailId != null && newLegalLocation != null) {
			String selfUri = getRenameLegalValidationSelfUri(policyId, annualFieldDetailId, newLegalLocation, baseUri);
			resource.getLinks().add(new RelLink(ResourceTypes.SELF, selfUri, "GET"));
		}
	}
	
	public static String getRenameLegalValidationSelfUri(Integer policyId, Integer annualFieldDetailId, String newLegalLocation, URI baseUri) {
		String result = UriBuilder.fromUri(baseUri)
				.path(UwContractValidateRenameLegalEndpoint.class)
				.queryParam("annualFieldDetailId", nvl(toString(annualFieldDetailId), ""))
				.queryParam("newLegalLocation", nvl(newLegalLocation, ""))
				.build(policyId)
				.toString();		
		return result;
	}
	
	protected static String nvl(Integer value, String defaultValue) {
		return (value==null)?defaultValue:value.toString();
	}
	
}
