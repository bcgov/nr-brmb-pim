package ca.bc.gov.mal.cirras.underwriting.service.api.v1.model.factory;

import ca.bc.gov.nrs.wfone.common.model.Message;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dto.PagedDtos;
import ca.bc.gov.nrs.wfone.common.service.api.model.factory.FactoryContext;
import ca.bc.gov.nrs.wfone.common.service.api.model.factory.FactoryException;
import ca.bc.gov.nrs.wfone.common.webade.authentication.WebAdeAuthentication;

import java.util.List;

import ca.bc.gov.mal.cirras.underwriting.model.v1.AddFieldValidation;
import ca.bc.gov.mal.cirras.underwriting.model.v1.AnnualField;
import ca.bc.gov.mal.cirras.underwriting.model.v1.Field;
import ca.bc.gov.mal.cirras.underwriting.model.v1.LegalLand;
import ca.bc.gov.mal.cirras.underwriting.model.v1.RemoveFieldValidation;
import ca.bc.gov.mal.cirras.underwriting.model.v1.RenameLegalValidation;
import ca.bc.gov.mal.cirras.underwriting.model.v1.ReplaceLegalValidation;
import ca.bc.gov.mal.cirras.underwriting.model.v1.UwContract;
import ca.bc.gov.mal.cirras.underwriting.model.v1.UwContractList;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.FieldDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.LegalLandDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.PolicyDto;

public interface UwContractFactory {

	UwContractList<? extends UwContract<? extends UwContract<?>>> getUwContractList(
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
		) 
		throws FactoryException, DaoException;
	
	UwContract<? extends UwContract<?>> getUwContract(
			PolicyDto dto, 
			List<PolicyDto> linkedPolicyDtos,
			List<PolicyDto> otherYearPolicyDtos,
			Boolean loadLinkedPolicies,
			Boolean loadOtherYearPolicies,
			String screenType,
			FactoryContext context, 
			WebAdeAuthentication authentication
		) throws FactoryException;

	AddFieldValidation<? extends Message> getAddFieldValidation(
			List<String> warnings,
			List<String> errors,
			Integer policyId,
			Integer fieldId,
			Integer transferFromPolicyId,
			FactoryContext context, 
			WebAdeAuthentication authentication
		) throws FactoryException;

	RemoveFieldValidation<? extends Message> getRemoveFieldValidation(
			Boolean isRemoveFromPolicyAllowed,
			Boolean isDeleteFieldAllowed,			
			List<String> removeFromPolicyWarnings,
			List<String> deleteFieldErrors,
			Integer policyId,
			Integer fieldId,
			FactoryContext context, 
			WebAdeAuthentication authentication
		) throws FactoryException;
	
	RenameLegalValidation<? extends Message, ? extends LegalLand<? extends Field>, ? extends AnnualField> getRenameLegalValidation(
			Boolean isWarningLegalsWithSameLoc,
			String legalsWithSameLocMsg,
			List<LegalLandDto> legalsWithSameLocList,
			Boolean isWarningOtherFieldOnPolicy,
			String otherFieldOnPolicyMsg,
			List<FieldDto> otherFieldOnPolicyList,
			Boolean isWarningFieldOnOtherPolicy,
			String fieldOnOtherPolicyMsg,
			List<FieldDto> fieldOnOtherPolicyList,
			Boolean isWarningOtherLegalData,
			String otherLegalDataMsg,
			LegalLandDto otherLegalData,
			Integer policyId,
			Integer annualFieldDetailId,
			String newLegalLocation,
			FactoryContext context, 
			WebAdeAuthentication authentication
		) throws FactoryException;
	
	ReplaceLegalValidation<? extends Message, ? extends LegalLand<? extends Field>, ? extends AnnualField> getReplaceLegalValidation(
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
		) throws FactoryException;
	
}

