package ca.bc.gov.mal.cirras.underwriting.service.api.v1.model.factory;

import ca.bc.gov.nrs.wfone.common.model.Message;
import ca.bc.gov.nrs.wfone.common.service.api.model.factory.FactoryContext;
import ca.bc.gov.nrs.wfone.common.service.api.model.factory.FactoryException;
import ca.bc.gov.nrs.wfone.common.webade.authentication.WebAdeAuthentication;

import java.util.List;
import java.util.Set;

import ca.bc.gov.mal.cirras.underwriting.model.v1.AnnualField;
import ca.bc.gov.mal.cirras.underwriting.model.v1.VerifiedYieldAmendment;
import ca.bc.gov.mal.cirras.underwriting.model.v1.VerifiedYieldContract;
import ca.bc.gov.mal.cirras.underwriting.model.v1.VerifiedYieldContractCommodity;
import ca.bc.gov.mal.cirras.underwriting.model.v1.VerifiedYieldSummary;
import ca.bc.gov.mal.cirras.underwriting.model.v1.VerifiedYieldGrainBasket;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.DeclaredYieldContractDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.PolicyDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.ProductDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.VerifiedYieldAmendmentDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.VerifiedYieldContractCommodityDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.VerifiedYieldContractDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.VerifiedYieldSummaryDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.VerifiedYieldGrainBasketDto;

public interface VerifiedYieldContractFactory {

	VerifiedYieldContract<? extends AnnualField, ? extends Message> getDefaultVerifiedYieldContract(
			PolicyDto policyDto,
			DeclaredYieldContractDto dycDto,
			List<ProductDto> productDtos,
			FactoryContext context, 
			WebAdeAuthentication authentication
		) throws FactoryException;

	VerifiedYieldContract<? extends AnnualField, ? extends Message> getVerifiedYieldContract(
			VerifiedYieldContractDto dto, 
			List<ProductDto> productDtos,
			FactoryContext context, 
			WebAdeAuthentication authentication
		) throws FactoryException;

	void updateDto(
			VerifiedYieldContractDto dto, 
			VerifiedYieldContract<? extends AnnualField, ? extends Message> model,
			String userId);

	void updateDto(
			VerifiedYieldContractCommodityDto dto, 
			VerifiedYieldContractCommodity model, 
			List<ProductDto> productDtos,
			Boolean updateProductValues, 
			Integer insurancePlanId);

	void updateDto(
			VerifiedYieldAmendmentDto dto, 
			VerifiedYieldAmendment model);
	
	void updateDto(
			VerifiedYieldSummaryDto dto, 
			VerifiedYieldSummary verifiedSummary);
	
	void updateDto(
			VerifiedYieldGrainBasketDto dto, 
			VerifiedYieldGrainBasket verifiedYieldGrainBasket);

	Set<String> getForageGrainCoverageCodes();

}
