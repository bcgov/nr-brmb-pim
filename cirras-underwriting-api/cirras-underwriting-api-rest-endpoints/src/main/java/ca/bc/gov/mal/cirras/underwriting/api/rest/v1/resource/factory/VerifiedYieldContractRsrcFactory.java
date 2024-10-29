package ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.factory;

import ca.bc.gov.nrs.wfone.common.rest.endpoints.resource.factory.BaseResourceFactory;
import ca.bc.gov.nrs.wfone.common.service.api.model.factory.FactoryContext;
import ca.bc.gov.nrs.wfone.common.service.api.model.factory.FactoryException;
import ca.bc.gov.nrs.wfone.common.webade.authentication.WebAdeAuthentication;

import java.util.ArrayList;
import java.util.List;

import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.VerifiedYieldContractRsrc;
import ca.bc.gov.mal.cirras.underwriting.model.v1.AnnualField;
import ca.bc.gov.mal.cirras.underwriting.model.v1.VerifiedYieldContract;
import ca.bc.gov.mal.cirras.underwriting.model.v1.VerifiedYieldContractCommodity;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.DeclaredYieldContractCommodityDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.DeclaredYieldContractDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.PolicyDto;
import ca.bc.gov.mal.cirras.underwriting.service.api.v1.model.factory.VerifiedYieldContractFactory;

public class VerifiedYieldContractRsrcFactory extends BaseResourceFactory implements VerifiedYieldContractFactory { 
	
	@Override
	public VerifiedYieldContract<? extends AnnualField> getDefaultVerifiedYieldContract(
			PolicyDto policyDto,
			DeclaredYieldContractDto dycDto,
			FactoryContext context, 
			WebAdeAuthentication authentication
		) throws FactoryException {
		
		VerifiedYieldContractRsrc resource = new VerifiedYieldContractRsrc();

		populateDefaultResource(resource, policyDto, dycDto);
		
		// Verified Yield Contract Commodity
		if (!dycDto.getDeclaredYieldContractCommodities().isEmpty()) {
			List<VerifiedYieldContractCommodity> vyContractCommodities = new ArrayList<VerifiedYieldContractCommodity>();

			for (DeclaredYieldContractCommodityDto dyccDto : dycDto.getDeclaredYieldContractCommodities()) {
				VerifiedYieldContractCommodity vyccModel = createDefaultVerifiedYieldContractCommodity(dyccDto);
				vyContractCommodities.add(vyccModel);
			}

			resource.setVerifiedYieldContractCommodities(vyContractCommodities);
		}
						
		String eTag = getEtag(resource);
		resource.setETag(eTag);

		return resource;
	}

	private void populateDefaultResource(VerifiedYieldContractRsrc resource, PolicyDto policyDto, DeclaredYieldContractDto dycDto) {

		resource.setContractId(policyDto.getContractId());
		resource.setCropYear(policyDto.getCropYear());
		resource.setDeclaredYieldContractGuid(policyDto.getDeclaredYieldContractGuid());
		resource.setDefaultYieldMeasUnitTypeCode(dycDto.getDefaultYieldMeasUnitTypeCode());
		resource.setGrowerContractYearId(policyDto.getGrowerContractYearId());
		resource.setInsurancePlanId(policyDto.getInsurancePlanId());
		resource.setVerifiedYieldContractGuid(null);
		resource.setVerifiedYieldUpdateTimestamp(null);
		resource.setVerifiedYieldUpdateUser(null);
	}
   	
	
	private VerifiedYieldContractCommodity createDefaultVerifiedYieldContractCommodity(DeclaredYieldContractCommodityDto dto) {
		VerifiedYieldContractCommodity model = new VerifiedYieldContractCommodity();

		model.setCropCommodityId(dto.getCropCommodityId());
		model.setCropCommodityName(dto.getCropCommodityName());
		model.setHarvestedAcres(dto.getHarvestedAcres());
		model.setHarvestedAcresOverride(null);
		model.setHarvestedYield(null);   // Calculated later
		model.setHarvestedYieldOverride(null);
		model.setIsPedigreeInd(dto.getIsPedigreeInd());
		model.setProductionGuarantee(null); // TODO: Will be implemented later.
		model.setSoldYieldDefaultUnit(dto.getSoldYieldDefaultUnit());
		model.setStoredYieldDefaultUnit(dto.getStoredYieldDefaultUnit());
		model.setTotalInsuredAcres(dto.getTotalInsuredAcres());
		model.setVerifiedYieldContractCommodityGuid(null);
		model.setVerifiedYieldContractGuid(null);
		model.setYieldPerAcre(null); // Calculated later

		return model;
	}
	
}
