package ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.factory;

import ca.bc.gov.nrs.common.wfone.rest.resource.RelLink;
import ca.bc.gov.nrs.wfone.common.rest.endpoints.resource.factory.BaseResourceFactory;
import ca.bc.gov.nrs.wfone.common.service.api.model.factory.FactoryContext;
import ca.bc.gov.nrs.wfone.common.service.api.model.factory.FactoryException;
import ca.bc.gov.nrs.wfone.common.webade.authentication.WebAdeAuthentication;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.UriBuilder;

import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints.DopYieldContractListEndpoint;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints.VerifiedYieldContractEndpoint;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints.VerifiedYieldContractListEndpoint;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints.security.Scopes;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.VerifiedYieldContractRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.types.ResourceTypes;
import ca.bc.gov.mal.cirras.underwriting.model.v1.AnnualField;
import ca.bc.gov.mal.cirras.underwriting.model.v1.VerifiedYieldContract;
import ca.bc.gov.mal.cirras.underwriting.model.v1.VerifiedYieldContractCommodity;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.DeclaredYieldContractCommodityDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.DeclaredYieldContractDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.PolicyDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.VerifiedYieldContractCommodityDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.VerifiedYieldContractDto;
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

	@Override
	public VerifiedYieldContract<? extends AnnualField> getVerifiedYieldContract(VerifiedYieldContractDto dto,
			FactoryContext context, WebAdeAuthentication authentication) throws FactoryException {

		VerifiedYieldContractRsrc resource = new VerifiedYieldContractRsrc();

		populateResource(resource, dto);
		
		// Verified Yield Contract Commodity
		if (!dto.getVerifiedYieldContractCommodities().isEmpty()) {
			List<VerifiedYieldContractCommodity> verifiedContractCommodities = new ArrayList<VerifiedYieldContractCommodity>();

			for (VerifiedYieldContractCommodityDto vyccDto : dto.getVerifiedYieldContractCommodities()) {
				VerifiedYieldContractCommodity vyccModel = createVerifiedYieldContractCommodity(vyccDto);
				verifiedContractCommodities.add(vyccModel);
			}

			resource.setVerifiedYieldContractCommodities(verifiedContractCommodities);
		}
						
		String eTag = getEtag(resource);
		resource.setETag(eTag);

		URI baseUri = getBaseURI(context);
		setSelfLink(dto.getVerifiedYieldContractGuid(), resource, baseUri);
 		setLinks(dto.getVerifiedYieldContractGuid(), resource, baseUri, authentication);

		return resource;		
	}

	private void populateResource(VerifiedYieldContractRsrc resource, VerifiedYieldContractDto dto) {
		
		resource.setContractId(dto.getContractId());
		resource.setCropYear(dto.getCropYear());
		resource.setDeclaredYieldContractGuid(dto.getDeclaredYieldContractGuid());
		resource.setDefaultYieldMeasUnitTypeCode(dto.getDefaultYieldMeasUnitTypeCode());
		resource.setGrowerContractYearId(dto.getGrowerContractYearId());
		resource.setInsurancePlanId(dto.getInsurancePlanId());
		resource.setVerifiedYieldContractGuid(dto.getVerifiedYieldContractGuid());
		resource.setVerifiedYieldUpdateTimestamp(dto.getVerifiedYieldUpdateTimestamp());
		resource.setVerifiedYieldUpdateUser(dto.getVerifiedYieldUpdateUser());
		
	}

	private VerifiedYieldContractCommodity createVerifiedYieldContractCommodity(VerifiedYieldContractCommodityDto dto) {
		VerifiedYieldContractCommodity model = new VerifiedYieldContractCommodity();

		model.setCropCommodityId(dto.getCropCommodityId());
		model.setCropCommodityName(dto.getCropCommodityName());
		model.setHarvestedAcres(dto.getHarvestedAcres());
		model.setHarvestedAcresOverride(dto.getHarvestedAcresOverride());
		model.setHarvestedYield(dto.getHarvestedYield());
		model.setHarvestedYieldOverride(dto.getHarvestedYieldOverride());
		model.setIsPedigreeInd(dto.getIsPedigreeInd());
		model.setProductionGuarantee(dto.getProductionGuarantee());
		model.setSoldYieldDefaultUnit(dto.getSoldYieldDefaultUnit());
		model.setStoredYieldDefaultUnit(dto.getStoredYieldDefaultUnit());
		model.setTotalInsuredAcres(dto.getTotalInsuredAcres());
		model.setVerifiedYieldContractCommodityGuid(dto.getVerifiedYieldContractCommodityGuid());
		model.setVerifiedYieldContractGuid(dto.getVerifiedYieldContractGuid());
		model.setYieldPerAcre(dto.getYieldPerAcre());

		return model;
	}
	
	static void setSelfLink(String verifiedYieldContractGuid, VerifiedYieldContractRsrc resource, URI baseUri) {
		if (verifiedYieldContractGuid != null) {
			String selfUri = getVerifiedYieldContractSelfUri(verifiedYieldContractGuid, baseUri);
			resource.getLinks().add(new RelLink(ResourceTypes.SELF, selfUri, "GET"));
		}
	}

	public static String getVerifiedYieldContractSelfUri(String verifiedYieldContractGuid, URI baseUri) {
		String result = UriBuilder.fromUri(baseUri).path(VerifiedYieldContractEndpoint.class).build(verifiedYieldContractGuid).toString();
		return result;
	}
	
	public static String getVerifiedYieldContractListSelfUri(URI baseUri) {
		String result = UriBuilder.fromUri(baseUri).path(VerifiedYieldContractListEndpoint.class).build().toString();
		return result;
	}

	private static void setLinks(String verifiedYieldContractGuid, VerifiedYieldContractRsrc resource, URI baseUri, WebAdeAuthentication authentication) {		

		String result = UriBuilder.fromUri(baseUri).path(VerifiedYieldContractEndpoint.class).build(verifiedYieldContractGuid).toString();

		if (authentication.hasAuthority(Scopes.UPDATE_VERIFIED_YIELD_CONTRACT)) {
			resource.getLinks().add(new RelLink(ResourceTypes.UPDATE_VERIFIED_YIELD_CONTRACT, result, "PUT"));
		}
		
		if (authentication.hasAuthority(Scopes.DELETE_VERIFIED_YIELD_CONTRACT)) {
			resource.getLinks().add(new RelLink(ResourceTypes.DELETE_VERIFIED_YIELD_CONTRACT, result, "DELETE"));
		}
		
	}
	
}
