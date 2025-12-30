package ca.bc.gov.mal.cirras.underwriting.services.model.factory;

import ca.bc.gov.nrs.wfone.common.model.Message;
import ca.bc.gov.nrs.wfone.common.service.api.model.factory.FactoryContext;
import ca.bc.gov.nrs.wfone.common.service.api.model.factory.FactoryException;
import ca.bc.gov.nrs.wfone.common.webade.authentication.WebAdeAuthentication;

import java.util.List;
import java.util.Set;

import ca.bc.gov.mal.cirras.underwriting.data.models.AnnualField;
import ca.bc.gov.mal.cirras.underwriting.data.models.VerifiedYieldAmendment;
import ca.bc.gov.mal.cirras.underwriting.data.models.VerifiedYieldContract;
import ca.bc.gov.mal.cirras.underwriting.data.models.VerifiedYieldContractCommodity;
import ca.bc.gov.mal.cirras.underwriting.data.models.VerifiedYieldContractSimple;
import ca.bc.gov.mal.cirras.underwriting.data.models.VerifiedYieldSummary;
import ca.bc.gov.mal.cirras.underwriting.data.models.VerifiedYieldGrainBasket;
import ca.bc.gov.mal.cirras.underwriting.data.entities.DeclaredYieldContractDto;
import ca.bc.gov.mal.cirras.underwriting.data.entities.PolicyDto;
import ca.bc.gov.mal.cirras.underwriting.data.entities.ProductDto;
import ca.bc.gov.mal.cirras.underwriting.data.entities.VerifiedYieldAmendmentDto;
import ca.bc.gov.mal.cirras.underwriting.data.entities.VerifiedYieldContractCommodityDto;
import ca.bc.gov.mal.cirras.underwriting.data.entities.VerifiedYieldContractDto;
import ca.bc.gov.mal.cirras.underwriting.data.entities.VerifiedYieldSummaryDto;
import ca.bc.gov.mal.cirras.underwriting.data.entities.VerifiedYieldGrainBasketDto;

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

	VerifiedYieldContractSimple getVerifiedYieldContractSimple(
			VerifiedYieldContractDto dto, 
			Integer cropCommodityId,
			Boolean isPedigreeInd, 
			FactoryContext factoryContext, 
			WebAdeAuthentication authentication
			) throws FactoryException;

}
