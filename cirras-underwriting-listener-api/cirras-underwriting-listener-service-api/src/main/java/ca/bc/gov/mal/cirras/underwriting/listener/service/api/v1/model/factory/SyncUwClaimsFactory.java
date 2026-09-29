package ca.bc.gov.mal.cirras.underwriting.listener.service.api.v1.model.factory;

import java.util.Date;

import ca.bc.gov.mal.cirras.claims.data.resources.ClaimCalculationSimpleRsrc;
import ca.bc.gov.mal.cirras.underwriting.data.resources.SyncClaimCalculationSimpleRsrc;

public interface SyncUwClaimsFactory {

	//Grower Contact
	SyncClaimCalculationSimpleRsrc getSyncClaimCalculationSimpleRsrc(ClaimCalculationSimpleRsrc claimCalculationSimpleRsrc, Date eventDate, String eventType);
	SyncClaimCalculationSimpleRsrc getDeleteSyncClaimCalculationSimpleRsrc(String claimCalculationBerriesGuid, Date eventDate, String eventType);
}
