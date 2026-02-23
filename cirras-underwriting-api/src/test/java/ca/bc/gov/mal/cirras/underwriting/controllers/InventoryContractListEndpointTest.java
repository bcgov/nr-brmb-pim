package ca.bc.gov.mal.cirras.underwriting.controllers;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.mal.cirras.underwriting.clients.CirrasUnderwritingService;
import ca.bc.gov.mal.cirras.underwriting.clients.CirrasUnderwritingServiceException;
import ca.bc.gov.mal.cirras.underwriting.controllers.scopes.Scopes;
import ca.bc.gov.mal.cirras.underwriting.data.resources.EndpointsRsrc;
import ca.bc.gov.mal.cirras.underwriting.data.resources.InventoryContractListRsrc;
import ca.bc.gov.mal.cirras.underwriting.data.resources.InventoryContractRsrc;
import ca.bc.gov.mal.cirras.underwriting.data.resources.UwContractListRsrc;
import ca.bc.gov.mal.cirras.underwriting.data.resources.UwContractRsrc;
import ca.bc.gov.mal.cirras.underwriting.test.EndpointsTest;
import ca.bc.gov.nrs.wfone.common.webade.oauth2.token.client.Oauth2ClientException;

public class InventoryContractListEndpointTest extends EndpointsTest {
	private static final Logger logger = LoggerFactory.getLogger(InventoryContractListEndpointTest.class);


	private static final String[] SCOPES = {
		Scopes.GET_TOP_LEVEL,
		Scopes.PRINT_INVENTORY_CONTRACT,
		Scopes.SEARCH_UWCONTRACTS,
		Scopes.GET_UWCONTRACT,
		Scopes.GET_INVENTORY_CONTRACT
	};

	@Test
	public void testGetInventoryContractListByInvContractGuid() throws CirrasUnderwritingServiceException, Oauth2ClientException {
		logger.debug("<testGetInventoryContractListByInvContractGuid");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}
		
		CirrasUnderwritingService service = getService(SCOPES);

		EndpointsRsrc topLevelEndpoints = service.getTopLevelEndpoints();

		String insurancePlanId = "4";
		String cropYear = "2020";
		
		String officeId = null;
		String policyStatusCode = null;
		String policyNumber = "144733-20";  //Needs to be contract with inventory
		String growerInfo = null;
		String sortColumn = null;
		String inventoryContractGuids = "a10f0e3b811849d78b4ce2d054f6ed21"; //Must be the guid of the contract
		
		UwContractListRsrc uwContractList = getUnderwritingContractList(
					service,
					topLevelEndpoints, 
					cropYear, 
					insurancePlanId,
					policyNumber, 
					growerInfo,
					officeId,
					policyStatusCode);

		Assert.assertNotNull("uwContractList: ", uwContractList);
		Assert.assertTrue("uwContractList.getCollection().size(): ", uwContractList.getCollection().size() == 1);

		//Underwriting Contract for comparison
		UwContractRsrc uwContract = uwContractList.getCollection().get(0);

		//Inventory Contract for comparison
		InventoryContractRsrc selectedInvContract = getInventoryContract(service, topLevelEndpoints, uwContract);
			
		//Get contract list
		InventoryContractListRsrc searchResults = service.getInventoryContractList(
				topLevelEndpoints, 
				cropYear, 
				insurancePlanId, 
				officeId, 
				policyStatusCode, 
				policyNumber, 
				growerInfo, 
				sortColumn, 
				inventoryContractGuids);


		Assert.assertNotNull(searchResults);
		Assert.assertTrue(searchResults.getCollection().size() > 0);
		
		InventoryContractRsrc invContract = searchResults.getCollection().get(0);

		Assert.assertEquals("InventoryContractGuid", selectedInvContract.getInventoryContractGuid(), invContract.getInventoryContractGuid());
		Assert.assertEquals("ContractId", selectedInvContract.getContractId(), invContract.getContractId());
		Assert.assertEquals("CropYear", selectedInvContract.getCropYear(), invContract.getCropYear());
		Assert.assertEquals("FertilizerInd", selectedInvContract.getFertilizerInd(), invContract.getFertilizerInd());
		Assert.assertEquals("GrainFromPrevYearInd", selectedInvContract.getGrainFromPrevYearInd(), invContract.getGrainFromPrevYearInd());
		Assert.assertEquals("HerbicideInd", selectedInvContract.getHerbicideInd(), invContract.getHerbicideInd());
		Assert.assertEquals("OtherChangesComment", selectedInvContract.getOtherChangesComment(), invContract.getOtherChangesComment());
		Assert.assertEquals("OtherChangesInd", selectedInvContract.getOtherChangesInd(), invContract.getOtherChangesInd());
		Assert.assertEquals("SeededCropReportSubmittedInd", selectedInvContract.getSeededCropReportSubmittedInd(), invContract.getSeededCropReportSubmittedInd());
		Assert.assertEquals("TilliageInd", selectedInvContract.getTilliageInd(), invContract.getTilliageInd());
		Assert.assertEquals("UnseededIntentionsSubmittedInd", selectedInvContract.getUnseededIntentionsSubmittedInd(), invContract.getUnseededIntentionsSubmittedInd());
		Assert.assertEquals("GrowerContractYearId", uwContract.getGrowerContractYearId(), invContract.getGrowerContractYearId());
		Assert.assertEquals("PolicyNumber", uwContract.getPolicyNumber(), invContract.getPolicyNumber());
		Assert.assertEquals("GrowerName", uwContract.getGrowerName(), invContract.getGrowerName());
		Assert.assertEquals("GrowerNumber", uwContract.getGrowerNumber(), invContract.getGrowerNumber());

		Assert.assertEquals("Commodities", selectedInvContract.getCommodities().size(), invContract.getCommodities().size());
		Assert.assertEquals("Fields", selectedInvContract.getFields().size(), invContract.getFields().size());
		
		logger.debug(">testGetInventoryContractListByInvContractGuid");
	}

	@Test
	public void testGetInventoryContractListByMultipleInvContractGuid() throws CirrasUnderwritingServiceException, Oauth2ClientException {
		logger.debug("<testGetInventoryContractListByMultipleInvContractGuid");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}
		
		CirrasUnderwritingService service = getService(SCOPES);

		EndpointsRsrc topLevelEndpoints = service.getTopLevelEndpoints();

		String insurancePlanId = "4";
		String cropYear = "2020";
		
		String officeId = null;
		String policyStatusCode = null;
		String policyNumber = "140160-22";  //Needs to be the policy of one of the inventory contracts below
		String growerInfo = null;
		String sortColumn = null;
		String inventoryContractGuids = "e97fcce3caa4435e81a81a8bf42974f8,ff3b5070d4e84fe7ac571edab03bf1eb"; //Must be the guid of the contract
		
		UwContractListRsrc uwContractList = getUnderwritingContractList(
					service,
					topLevelEndpoints, 
					cropYear, 
					insurancePlanId,
					policyNumber, 
					growerInfo,
					officeId,
					policyStatusCode);

		Assert.assertNotNull("uwContractList: ", uwContractList);
		Assert.assertTrue("uwContractList.getCollection().size(): ", uwContractList.getCollection().size() == 1);

		//Underwriting Contract for comparison
		UwContractRsrc uwContract = uwContractList.getCollection().get(0);

		//Inventory Contract for comparison
		InventoryContractRsrc selectedInvContract = getInventoryContract(service, topLevelEndpoints, uwContract);
			
		//Get contract list
		InventoryContractListRsrc searchResults = service.getInventoryContractList(
				topLevelEndpoints, 
				cropYear, 
				insurancePlanId, 
				officeId, 
				policyStatusCode, 
				policyNumber, 
				growerInfo, 
				sortColumn, 
				inventoryContractGuids);


		Assert.assertNotNull(searchResults);
		Assert.assertTrue(searchResults.getCollection().size() == 2);
		
		for (InventoryContractRsrc invContract : searchResults.getCollection()) {
			
			//Only checking one of the contracts
			if(uwContract.getPolicyNumber().equals(invContract.getPolicyNumber())) {
				Assert.assertEquals("InventoryContractGuid", selectedInvContract.getInventoryContractGuid(), invContract.getInventoryContractGuid());
				Assert.assertEquals("ContractId", selectedInvContract.getContractId(), invContract.getContractId());
				Assert.assertEquals("CropYear", selectedInvContract.getCropYear(), invContract.getCropYear());
				Assert.assertEquals("FertilizerInd", selectedInvContract.getFertilizerInd(), invContract.getFertilizerInd());
				Assert.assertEquals("GrainFromPrevYearInd", selectedInvContract.getGrainFromPrevYearInd(), invContract.getGrainFromPrevYearInd());
				Assert.assertEquals("HerbicideInd", selectedInvContract.getHerbicideInd(), invContract.getHerbicideInd());
				Assert.assertEquals("OtherChangesComment", selectedInvContract.getOtherChangesComment(), invContract.getOtherChangesComment());
				Assert.assertEquals("OtherChangesInd", selectedInvContract.getOtherChangesInd(), invContract.getOtherChangesInd());
				Assert.assertEquals("SeededCropReportSubmittedInd", selectedInvContract.getSeededCropReportSubmittedInd(), invContract.getSeededCropReportSubmittedInd());
				Assert.assertEquals("TilliageInd", selectedInvContract.getTilliageInd(), invContract.getTilliageInd());
				Assert.assertEquals("UnseededIntentionsSubmittedInd", selectedInvContract.getUnseededIntentionsSubmittedInd(), invContract.getUnseededIntentionsSubmittedInd());
				Assert.assertEquals("GrowerContractYearId", uwContract.getGrowerContractYearId(), invContract.getGrowerContractYearId());
				Assert.assertEquals("PolicyNumber", uwContract.getPolicyNumber(), invContract.getPolicyNumber());
				Assert.assertEquals("GrowerName", uwContract.getGrowerName(), invContract.getGrowerName());
				Assert.assertEquals("GrowerNumber", uwContract.getGrowerNumber(), invContract.getGrowerNumber());
				Assert.assertEquals("Commodities", selectedInvContract.getCommodities().size(), invContract.getCommodities().size());
				Assert.assertEquals("Fields", selectedInvContract.getFields().size(), invContract.getFields().size());
			} else {
				Assert.assertTrue("Commodities 2", invContract.getCommodities().size() > 0);
				Assert.assertTrue("Fields 2", invContract.getFields().size() > 0);
			}
	
		}

		
		logger.debug(">testGetInventoryContractListByMultipleInvContractGuid");
	}
	
	@Test
	public void testGetInventoryContractListBySearchCriteria() throws CirrasUnderwritingServiceException, Oauth2ClientException {
		logger.debug("<testGetInventoryContractListBySearchCriteria");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}
		
		CirrasUnderwritingService service = getService(SCOPES);

		EndpointsRsrc topLevelEndpoints = service.getTopLevelEndpoints();

		String insurancePlanId = "4";
		String cropYear = "2020";
		
		String officeId = "3";
		String policyStatusCode = "CANCELLED";
		String policyNumber = "144733-20";  //Needs to be contract with inventory
		String growerInfo = "WILLINGTON WESLEY";
		String sortColumn = null;
		String inventoryContractGuids = null;
		
		UwContractListRsrc uwContractList = getUnderwritingContractList(
					service,
					topLevelEndpoints, 
					cropYear,
					insurancePlanId,
					policyNumber, 
					growerInfo,
					officeId,
					policyStatusCode);

		Assert.assertNotNull("uwContractList: ", uwContractList);
		Assert.assertTrue("uwContractList.getCollection().size(): ", uwContractList.getCollection().size() == 1);

		//Underwriting Contract for comparison
		UwContractRsrc uwContract = uwContractList.getCollection().get(0);

		//Inventory Contract for comparison
		InventoryContractRsrc selectedInvContract = getInventoryContract(service, topLevelEndpoints, uwContract);
			
		//Get contract list
		InventoryContractListRsrc searchResults = service.getInventoryContractList(
				topLevelEndpoints, 
				cropYear, 
				insurancePlanId, 
				officeId, 
				policyStatusCode, 
				policyNumber, 
				growerInfo, 
				sortColumn, 
				inventoryContractGuids);


		Assert.assertNotNull(searchResults);
		Assert.assertTrue(searchResults.getCollection().size() > 0);
		
		InventoryContractRsrc invContract = searchResults.getCollection().get(0);

		Assert.assertEquals("InventoryContractGuid", selectedInvContract.getInventoryContractGuid(), invContract.getInventoryContractGuid());
		Assert.assertEquals("ContractId", selectedInvContract.getContractId(), invContract.getContractId());
		Assert.assertEquals("CropYear", selectedInvContract.getCropYear(), invContract.getCropYear());
		Assert.assertEquals("FertilizerInd", selectedInvContract.getFertilizerInd(), invContract.getFertilizerInd());
		Assert.assertEquals("GrainFromPrevYearInd", selectedInvContract.getGrainFromPrevYearInd(), invContract.getGrainFromPrevYearInd());
		Assert.assertEquals("HerbicideInd", selectedInvContract.getHerbicideInd(), invContract.getHerbicideInd());
		Assert.assertEquals("OtherChangesComment", selectedInvContract.getOtherChangesComment(), invContract.getOtherChangesComment());
		Assert.assertEquals("OtherChangesInd", selectedInvContract.getOtherChangesInd(), invContract.getOtherChangesInd());
		Assert.assertEquals("SeededCropReportSubmittedInd", selectedInvContract.getSeededCropReportSubmittedInd(), invContract.getSeededCropReportSubmittedInd());
		Assert.assertEquals("TilliageInd", selectedInvContract.getTilliageInd(), invContract.getTilliageInd());
		Assert.assertEquals("UnseededIntentionsSubmittedInd", selectedInvContract.getUnseededIntentionsSubmittedInd(), invContract.getUnseededIntentionsSubmittedInd());
		Assert.assertEquals("GrowerContractYearId", uwContract.getGrowerContractYearId(), invContract.getGrowerContractYearId());
		Assert.assertEquals("PolicyNumber", uwContract.getPolicyNumber(), invContract.getPolicyNumber());
		Assert.assertEquals("GrowerName", uwContract.getGrowerName(), invContract.getGrowerName());
		Assert.assertEquals("GrowerNumber", uwContract.getGrowerNumber(), invContract.getGrowerNumber());

		Assert.assertEquals("Commodities", selectedInvContract.getCommodities().size(), invContract.getCommodities().size());
		Assert.assertEquals("Fields", selectedInvContract.getFields().size(), invContract.getFields().size());
		
		logger.debug(">testGetInventoryContractListBySearchCriteria");
	}	
	
	@Test
	public void testGetInventoryContractListSorted() throws CirrasUnderwritingServiceException, Oauth2ClientException {
		logger.debug("<testGetInventoryContractListSorted");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}
		
		CirrasUnderwritingService service = getService(SCOPES);

		EndpointsRsrc topLevelEndpoints = service.getTopLevelEndpoints();

		String insurancePlanId = "4";
		String cropYear = "2023";
		
		String officeId = "3"; //Fort St. John
		String policyStatusCode = null;
		String policyNumber = null;  //Needs to be contract with inventory
		String growerInfo = null;
		String inventoryContractGuids = null;

		//SORT BY POLICY NUMBER
		String sortColumn = "policyNumber";
		
		//Get contract list
		InventoryContractListRsrc searchResults = service.getInventoryContractList(
				topLevelEndpoints, 
				cropYear, 
				insurancePlanId, 
				officeId, 
				policyStatusCode, 
				policyNumber, 
				growerInfo, 
				sortColumn, 
				inventoryContractGuids);


		Assert.assertNotNull(searchResults);
		Assert.assertTrue(searchResults.getCollection().size() > 0);
		
		String previousPolicyNumber = "";
		
		//Compare strings to verify sorting
		for (InventoryContractRsrc invContract : searchResults.getCollection()) {

			String newPolicyNumber = invContract.getPolicyNumber();

			if(!previousPolicyNumber.equals("")) {
				int compare = newPolicyNumber.compareTo(previousPolicyNumber); 
				//Compare is 0 or greater 0 if sorting worked
				Assert.assertTrue(newPolicyNumber + " is not greater " + previousPolicyNumber, compare >= 0);
			}

			previousPolicyNumber = newPolicyNumber;
		}

		//SORT BY GROWER NAME
		sortColumn = "growerName";
		
		//Get contract list
		searchResults = service.getInventoryContractList(
				topLevelEndpoints, 
				cropYear, 
				insurancePlanId, 
				officeId, 
				policyStatusCode, 
				policyNumber, 
				growerInfo, 
				sortColumn, 
				inventoryContractGuids);


		Assert.assertNotNull(searchResults);
		Assert.assertTrue(searchResults.getCollection().size() > 0);
		
		String previousGrowerName = "";
		
		//Compare strings to verify sorting
		for (InventoryContractRsrc invContract : searchResults.getCollection()) {

			String newGrowerName = invContract.getGrowerName();

			if(!previousGrowerName.equals("")) {
				int compare = newGrowerName.compareTo(previousGrowerName); 
				//Compare is 0 or greater 0 if sorting worked
				Assert.assertTrue(newGrowerName + " is not greater " + previousGrowerName, compare >= 0);
			}

			previousGrowerName = newGrowerName;
		}
		
		logger.debug(">testGetInventoryContractListSorted");
	}	
	
	private InventoryContractRsrc getInventoryContract(
			CirrasUnderwritingService service,
			EndpointsRsrc topLevelEndpoints,
			UwContractRsrc uwContract
			) throws CirrasUnderwritingServiceException {

		InventoryContractRsrc invContract = service.getInventoryContract(uwContract);
		
		return invContract;
	}
	
	private UwContractListRsrc getUnderwritingContractList(
			CirrasUnderwritingService service,
			EndpointsRsrc topLevelEndpoints,
			String cropYear,
			String insurancePlanId,
			String policyNumber,
			String growerInfo,
			String officeId,
			String policyStatusCode
			) throws CirrasUnderwritingServiceException {
		
		Integer pageNumber = new Integer(1);
		Integer pageRowCount = new Integer(100);

		UwContractListRsrc searchResults = service.getUwContractList(
				topLevelEndpoints, 
				cropYear, 
				insurancePlanId, 
				officeId,
				policyStatusCode,
				policyNumber,
				growerInfo,
				null, 
				null, 
				null, 
				pageNumber, pageRowCount);
		
		Assert.assertNotNull("searchResults null: ", searchResults);
		
		return searchResults;
	}	

}
