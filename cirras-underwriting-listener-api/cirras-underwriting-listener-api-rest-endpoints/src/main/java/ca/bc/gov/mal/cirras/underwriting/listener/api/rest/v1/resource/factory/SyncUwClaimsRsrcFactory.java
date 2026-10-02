package ca.bc.gov.mal.cirras.underwriting.listener.api.rest.v1.resource.factory;

import java.util.Date;


import ca.bc.gov.mal.cirras.underwriting.data.resources.ClaimSyncEventTypes;
import ca.bc.gov.mal.cirras.underwriting.data.resources.SyncClaimCalculationSimpleRsrc;
import ca.bc.gov.mal.cirras.underwriting.data.models.SyncClaimCalculationBerries;
import ca.bc.gov.mal.cirras.claims.data.models.ClaimCalculationBerries;
import ca.bc.gov.mal.cirras.claims.data.resources.ClaimCalculationSimpleRsrc;
import ca.bc.gov.mal.cirras.claims.data.resources.ClaimEventTypes;
import ca.bc.gov.mal.cirras.underwriting.listener.service.api.v1.model.factory.SyncUwClaimsFactory;
import ca.bc.gov.nrs.wfone.common.rest.endpoints.resource.factory.BaseResourceFactory;

public class SyncUwClaimsRsrcFactory extends BaseResourceFactory implements SyncUwClaimsFactory {


	//======================================================================================================================
	// 
	//======================================================================================================================
	@Override
	public SyncClaimCalculationSimpleRsrc getSyncClaimCalculationSimpleRsrc(ClaimCalculationSimpleRsrc claimCalculationSimpleRsrc, Date eventDate, String eventType) {
		SyncClaimCalculationSimpleRsrc resource = new SyncClaimCalculationSimpleRsrc();
		
		ClaimCalculationBerries claimCalculationBerries = claimCalculationSimpleRsrc.getClaimCalculationBerries();
		
		resource.setClaimCalculationGuid(claimCalculationSimpleRsrc.getClaimCalculationGuid());
		resource.setCalculationVersion(claimCalculationSimpleRsrc.getCalculationVersion());
		resource.setContractId(claimCalculationSimpleRsrc.getContractId());
		resource.setCropCommodityId(claimCalculationSimpleRsrc.getCropCommodityId());
		resource.setCropYear(claimCalculationSimpleRsrc.getCropYear());
		resource.setCalculationStatusCode(claimCalculationSimpleRsrc.getCalculationStatusCode());
		resource.setDataSyncTransDate(eventDate);
		resource.setTransactionType(eventType.replace(ClaimEventTypes.EventTypeNamespace, ClaimSyncEventTypes.EventTypeNamespace));
		
		SyncClaimCalculationBerries model = new SyncClaimCalculationBerries();
		model.setClaimCalculationBerriesGuid(claimCalculationBerries.getClaimCalculationBerriesGuid());
		model.setTotalYieldForCalculation(claimCalculationBerries.getTotalYieldForCalculation());
		
		resource.setSyncClaimCalculationBerries(model);

		return resource;
	}

	
	@Override
	public 	SyncClaimCalculationSimpleRsrc getDeleteSyncClaimCalculationSimpleRsrc(String claimCalculationBerriesGuid, Date eventDate, String eventType) {
		SyncClaimCalculationSimpleRsrc resource = new SyncClaimCalculationSimpleRsrc();
		SyncClaimCalculationBerries model = new SyncClaimCalculationBerries();
		
		model.setClaimCalculationBerriesGuid(claimCalculationBerriesGuid);
		resource.setSyncClaimCalculationBerries(model);
		resource.setDataSyncTransDate(eventDate);
		resource.setTransactionType(eventType.replace(ClaimEventTypes.EventTypeNamespace, ClaimSyncEventTypes.EventTypeNamespace));
		
		return resource;
	}

}
