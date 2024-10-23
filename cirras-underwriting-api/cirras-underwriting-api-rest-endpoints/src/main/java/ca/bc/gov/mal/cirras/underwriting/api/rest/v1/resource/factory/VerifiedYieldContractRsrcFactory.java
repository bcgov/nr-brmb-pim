package ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.factory;

import ca.bc.gov.nrs.wfone.common.rest.endpoints.resource.factory.BaseResourceFactory;
import ca.bc.gov.nrs.wfone.common.service.api.model.factory.FactoryContext;
import ca.bc.gov.nrs.wfone.common.service.api.model.factory.FactoryException;
import ca.bc.gov.nrs.wfone.common.webade.authentication.WebAdeAuthentication;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.VerifiedYieldContractRsrc;
import ca.bc.gov.mal.cirras.underwriting.model.v1.AnnualField;
import ca.bc.gov.mal.cirras.underwriting.model.v1.VerifiedYieldContract;
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
		
		// Declared Yield Contract Commodity
		// TODO
/*		if (!dycDto.getDeclaredYieldContractCommodities().isEmpty()) {
			List<DopYieldContractCommodity> dopContractCommodities = new ArrayList<DopYieldContractCommodity>();

			for (DeclaredYieldContractCommodityDto dyccDto : dycDto.getDeclaredYieldContractCommodities()) {
				DopYieldContractCommodity dyccModel = createDopYieldContractCommodities(dyccDto);
				dopContractCommodities.add(dyccModel);
			}

			resource.setDopYieldContractCommodities(dopContractCommodities);
		}
*/						
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
   	
	
// TODO
/*
	private DopYieldContractCommodity createDopYieldContractCommodities(DeclaredYieldContractCommodityDto dto) {
		DopYieldContractCommodity model = new DopYieldContractCommodity();
		
		model.setDeclaredYieldContractCommodityGuid(dto.getDeclaredYieldContractCommodityGuid());
		model.setDeclaredYieldContractGuid(dto.getDeclaredYieldContractGuid());
		model.setCropCommodityId(dto.getCropCommodityId());
		model.setCropCommodityName(dto.getCropCommodityName());
		model.setIsPedigreeInd(dto.getIsPedigreeInd());
		model.setHarvestedAcres(dto.getHarvestedAcres());
		model.setStoredYield(dto.getStoredYield());
		model.setStoredYieldDefaultUnit(dto.getStoredYieldDefaultUnit());
		model.setSoldYield(dto.getSoldYield());
		model.setSoldYieldDefaultUnit(dto.getSoldYieldDefaultUnit());
		model.setGradeModifierTypeCode(dto.getGradeModifierTypeCode());

		model.setTotalInsuredAcres(dto.getTotalInsuredAcres());

		return model;
	}
*/		
	
}
