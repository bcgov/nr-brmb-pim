package ca.bc.gov.mal.cirras.underwriting.controllers.publisher;

import java.util.Map;
import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

import ca.bc.gov.mal.cirras.underwriting.clients.CirrasUnderwritingService;
import ca.bc.gov.mal.cirras.underwriting.clients.CirrasUnderwritingServiceException;
import ca.bc.gov.mal.cirras.underwriting.controllers.scopes.Scopes;
import ca.bc.gov.mal.cirras.underwriting.data.models.DopYieldContractCommodityBerries;
import ca.bc.gov.mal.cirras.underwriting.data.resources.DopYieldContractSimpleRsrc;
import ca.bc.gov.mal.cirras.underwriting.data.resources.EndpointsRsrc;
import ca.bc.gov.mal.cirras.underwriting.data.resources.UnderwritingEventTypes;
import ca.bc.gov.mal.cirras.underwriting.test.EndpointsTest;
import ca.bc.gov.nrs.wfone.common.webade.oauth2.token.client.Oauth2ClientException;

public class EventPublisherTest extends EndpointsTest {
	private static final Logger logger = LoggerFactory.getLogger(EventPublisherTest.class);

	private static ObjectMapper mapper = new ObjectMapper();
	
	private static final String[] SCOPES = {
			Scopes.GET_TOP_LEVEL, 
	};

	private CirrasUnderwritingService service;
	private EndpointsRsrc topLevelEndpoints;

		
	@Before
	public void prepareTests() throws CirrasUnderwritingServiceException, Oauth2ClientException {

		service = getService(SCOPES);
		topLevelEndpoints = service.getTopLevelEndpoints();
	}	
	
	@Test
	public void testSimplePublish() throws EventPublisherException {
		logger.debug("<testSimplePublish");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}

		EventPublisher eventPublisher = (EventPublisher)webApplicationContext.getBean("eventPublisher");
		eventPublisher.publish("HelloWorld", null, null, null);
				
		logger.debug(">testSimplePublish");		
	}
	
	@Test
	public void testDopContractCommodityBerriesEventPublish() throws Oauth2ClientException, EventPublisherException {
		logger.debug("<testDopContractCommodityBerriesEventPublish");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}
		
		//The unit test ClaimUnderwritingListenerTest.testClaimUnderwritingEventConsume in the Claims-Listener-api
		// is processing this message

		String underwritingEventType = UnderwritingEventTypes.DopYieldContractCommodityBerriesUpdated;
		Integer contractId = 987654;
		Integer cropYear = 2026;
		String declaredYieldContractGuid = "testDeclaredYieldContractGuid";
		String declaredYieldContractCommodityBerriesGuid = "testDopContractCommodityBerriesGuid";
		Integer cropCommodityId = 11111;
		String cropCommodityName = "testCommodity";
		Double totalProduction = 100.0;
		Double totalProductionOverride = 200.0;
		Double totalPlantedAcres = 10.0;
		Double totalMatureEquivalentAcres = 20.0;
		Double totalSoldShippedYield = 300.0;
		Double totalSalesYield = 400.0;
		Double totalAbandonmentYield = 500.0;

		//Declared Yield Contract
		DopYieldContractSimpleRsrc resource = new DopYieldContractSimpleRsrc();
		resource.setContractId(contractId);
		resource.setCropYear(cropYear);
		resource.setDeclaredYieldContractGuid(declaredYieldContractGuid);

		// Declared Yield Contract Commodity Berries
		DopYieldContractCommodityBerries model = new DopYieldContractCommodityBerries();
		model.setDeclaredYieldContractCommodityBerriesGuid(declaredYieldContractCommodityBerriesGuid);
		model.setCropCommodityId(cropCommodityId);
		model.setCropCommodityName(cropCommodityName);
		model.setDeclaredYieldContractGuid(declaredYieldContractGuid);
		model.setTotalProduction(totalProduction);
		model.setTotalProductionOverride(totalProductionOverride);
		model.setTotalPlantedAcres(totalPlantedAcres);
		model.setTotalMatureEquivalentAcres(totalMatureEquivalentAcres);
		model.setTotalSoldShippedYield(totalSoldShippedYield);
		model.setTotalSalesYield(totalSalesYield);
		model.setTotalAbandonmentYield(totalAbandonmentYield);

		resource.setDopYieldContractCommodityBerries(model);

		Map<String, String> sourceIdentifiers = new HashMap<>();
		sourceIdentifiers.put("declaredYieldContractCommodityBerriesGuid", model.getDeclaredYieldContractCommodityBerriesGuid().toString());
		
		EventPublisher eventPublisher = (EventPublisher)webApplicationContext.getBean("eventPublisher");
		eventPublisher.publish(underwritingEventType, null, resource, sourceIdentifiers);
				
		logger.debug(">testDopContractCommodityBerriesEventPublish");		
	}
}
