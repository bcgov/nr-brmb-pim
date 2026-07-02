package ca.bc.gov.mal.cirras.underwriting.data.assemblers;

import java.net.URI;

import ca.bc.gov.mal.cirras.underwriting.controllers.PolicyEndpoint;
import ca.bc.gov.mal.cirras.underwriting.controllers.SyncClaimCalculationSimpleEndpoint;
import ca.bc.gov.mal.cirras.underwriting.data.entities.ClaimCalculationBerriesSyncDto;
import ca.bc.gov.mal.cirras.underwriting.data.models.SyncClaimCalculationBerries;
import ca.bc.gov.mal.cirras.underwriting.data.resources.SyncClaimCalculationSimpleRsrc;
import ca.bc.gov.nrs.wfone.common.rest.endpoints.resource.factory.BaseResourceFactory;
import jakarta.ws.rs.core.UriBuilder;

public class SyncClaimCalculationSimpleRsrcFactory extends BaseResourceFactory { 
	

	
	//======================================================================================================================
	// Sync Claims Calculation Simple Rsrc
	//======================================================================================================================

	public void updateSyncClaimCalculationSimple(ClaimCalculationBerriesSyncDto dto, SyncClaimCalculationSimpleRsrc resource) {
		dto.setCropCommodityId(resource.getCropCommodityId());
		dto.setContractId(resource.getContractId());
		dto.setCropYear(resource.getCropYear());
		dto.setClaimCalculationGuid(resource.getClaimCalculationGuid());
		dto.setCalculationStatusCode(resource.getCalculationStatusCode());
		dto.setCalculationVersion(resource.getCalculationVersion());
		dto.setDataSyncTransDate(resource.getDataSyncTransDate());

		dto.setClaimCalculationBerriesGuid(resource.getSyncClaimCalculationBerries().getClaimCalculationBerriesGuid());
		dto.setTotalYieldForCalculation(resource.getSyncClaimCalculationBerries().getTotalYieldForCalculation());

	}

	public SyncClaimCalculationSimpleRsrc getSyncClaimCalculationSimple(ClaimCalculationBerriesSyncDto dto) {
		SyncClaimCalculationSimpleRsrc resource = new SyncClaimCalculationSimpleRsrc();

		resource.setClaimCalculationGuid(dto.getClaimCalculationGuid());
		resource.setCalculationVersion(dto.getCalculationVersion());
		resource.setContractId(dto.getContractId());
		resource.setCropCommodityId(dto.getCropCommodityId());
		resource.setCropYear(dto.getCropYear());
		resource.setCalculationStatusCode(dto.getCalculationStatusCode());
		resource.setDataSyncTransDate(dto.getDataSyncTransDate());

		resource.setSyncClaimCalculationBerries(createClaimCalculationBerries(dto));
		
		return resource;
	}

	private SyncClaimCalculationBerries createClaimCalculationBerries(ClaimCalculationBerriesSyncDto dto) {

		SyncClaimCalculationBerries model = new SyncClaimCalculationBerries();

		model.setClaimCalculationBerriesGuid(dto.getClaimCalculationBerriesGuid());
		model.setTotalYieldForCalculation(dto.getTotalYieldForCalculation());

		return model;
	}

	public static String getSyncClaimCalculationSimpleSelfUri(
			URI baseUri) {

		String result = UriBuilder.fromUri(baseUri)
			.path(SyncClaimCalculationSimpleEndpoint.class)
			.build()
			.toString();

		return result;
	}

}
