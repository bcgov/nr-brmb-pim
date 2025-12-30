package ca.bc.gov.mal.cirras.underwriting.controllers;

import java.util.Calendar;
import java.util.Date;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.mal.cirras.underwriting.clients.CirrasUnderwritingService;
import ca.bc.gov.mal.cirras.underwriting.clients.CirrasUnderwritingServiceException;
import ca.bc.gov.mal.cirras.underwriting.clients.ValidationException;
import ca.bc.gov.mal.cirras.underwriting.controllers.scopes.Scopes;
import ca.bc.gov.mal.cirras.underwriting.data.resources.EndpointsRsrc;
import ca.bc.gov.mal.cirras.underwriting.data.resources.PolicyRsrc;
import ca.bc.gov.mal.cirras.underwriting.test.EndpointsTest;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;
import ca.bc.gov.nrs.wfone.common.webade.oauth2.token.client.Oauth2ClientException;
import ca.bc.gov.mal.cirras.underwriting.data.resources.PoliciesSyncEventTypes;


public class PolicyEndpointTest extends EndpointsTest {
	private static final Logger logger = LoggerFactory.getLogger(PolicyEndpointTest.class);


	private static final String[] SCOPES = {
		Scopes.GET_TOP_LEVEL, 
		Scopes.UPDATE_SYNC_UNDERWRITING,
		Scopes.DELETE_SYNC_UNDERWRITING,
		Scopes.GET_POLICY
	};
	
	private CirrasUnderwritingService service;
	private EndpointsRsrc topLevelEndpoints;
	private Integer policyId = 99999999;
	
	@Before
	public void prepareTests() throws CirrasUnderwritingServiceException, Oauth2ClientException, NotFoundDaoException, DaoException{
		service = getService(SCOPES);
		topLevelEndpoints = service.getTopLevelEndpoints();

		deletePolicy();

	}

	@After 
	public void cleanUp() throws CirrasUnderwritingServiceException, NotFoundDaoException, DaoException {
		deletePolicy();
	}

	
	private void deletePolicy() throws NotFoundDaoException, DaoException, CirrasUnderwritingServiceException{

		delete(policyId);

	}
	
	private void delete(Integer policyId) throws NotFoundDaoException, DaoException, CirrasUnderwritingServiceException{

		
		service.deletePolicy(topLevelEndpoints, policyId.toString());
		
	}
	
	@Test
	public void testCreateUpdateDeletePolicy() throws CirrasUnderwritingServiceException, Oauth2ClientException, ValidationException, NotFoundDaoException, DaoException {
		logger.debug("<testCreateUpdatePolicy");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}
		
		//Date and Time without millisecond
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MILLISECOND, 0); //Set milliseconds to 0 becauce they are not set in the database
		Date transactionDate = cal.getTime();

		Date createTransactionDate = addSeconds(transactionDate, -1);

		Integer growerId = 8;
		Integer insurancePlanId = 5;
		String policyStatusCode = "ACTIVE";
		Integer officeId = 1;
		String policyNumber = "123123-21";
		String contractNumber = "123123";
		Integer contractId = 99998888;
		Integer cropYear = 2021;

		//CREATE Policy
		PolicyRsrc resource = new PolicyRsrc();
		
		resource.setPolicyId(policyId);
		resource.setGrowerId(growerId);
		resource.setInsurancePlanId(insurancePlanId);
		resource.setPolicyStatusCode(policyStatusCode);
		resource.setOfficeId(officeId);
		resource.setPolicyNumber(policyNumber);
		resource.setContractNumber(contractNumber);
		resource.setContractId(contractId);
		resource.setCropYear(cropYear);
		
		resource.setDataSyncTransDate(createTransactionDate);
		resource.setTransactionType(PoliciesSyncEventTypes.PolicyCreated);

		service.synchronizePolicy(resource);
		
		PolicyRsrc fetchedResource = service.getPolicy(topLevelEndpoints, policyId.toString()); 

		Assert.assertEquals("PolicyId 1", resource.getPolicyId(), fetchedResource.getPolicyId());
		Assert.assertEquals("GrowerId 1", resource.getGrowerId(), fetchedResource.getGrowerId());
		Assert.assertEquals("InsurancePlanId 1", resource.getInsurancePlanId(), fetchedResource.getInsurancePlanId());
		Assert.assertEquals("OfficeId 1", resource.getOfficeId(), fetchedResource.getOfficeId());
		Assert.assertEquals("PolicyStatusCode 1", resource.getPolicyStatusCode(), fetchedResource.getPolicyStatusCode());
		Assert.assertEquals("PolicyNumber 1", resource.getPolicyNumber(), fetchedResource.getPolicyNumber());
		Assert.assertEquals("ContractNumber 1", resource.getContractNumber(), fetchedResource.getContractNumber());
		Assert.assertEquals("ContractId 1", resource.getContractId(), fetchedResource.getContractId());
		Assert.assertEquals("CropYear 1", resource.getCropYear(), fetchedResource.getCropYear());
		Assert.assertTrue("DataSyncTransDate 1", resource.getDataSyncTransDate().compareTo(fetchedResource.getDataSyncTransDate()) == 0);
		
		//UPDATE CODE
		fetchedResource.setGrowerId(12);
		fetchedResource.setInsurancePlanId(4);
		fetchedResource.setPolicyStatusCode("UNDERWRITING");
		fetchedResource.setOfficeId(2);
		fetchedResource.setPolicyNumber("321321-22");
		fetchedResource.setContractNumber("321321");
		fetchedResource.setContractId(11112222);
		fetchedResource.setCropYear(2022);
		fetchedResource.setDataSyncTransDate(addSeconds(transactionDate, +1));
		fetchedResource.setTransactionType(PoliciesSyncEventTypes.PolicyUpdated);

		service.synchronizePolicy(fetchedResource);
		
		PolicyRsrc updatedResource = service.getPolicy(topLevelEndpoints, policyId.toString()); 

		Assert.assertEquals("PolicyId 2", fetchedResource.getPolicyId(), updatedResource.getPolicyId());
		Assert.assertEquals("GrowerId 2", fetchedResource.getGrowerId(), updatedResource.getGrowerId());
		Assert.assertEquals("InsurancePlanId 2", fetchedResource.getInsurancePlanId(), updatedResource.getInsurancePlanId());
		Assert.assertEquals("OfficeId 2", fetchedResource.getOfficeId(), updatedResource.getOfficeId());
		Assert.assertEquals("PolicyStatusCode 2", fetchedResource.getPolicyStatusCode(), updatedResource.getPolicyStatusCode());
		Assert.assertEquals("PolicyNumber 2", fetchedResource.getPolicyNumber(), updatedResource.getPolicyNumber());
		Assert.assertEquals("ContractNumber 2", fetchedResource.getContractNumber(), updatedResource.getContractNumber());
		Assert.assertEquals("ContractId 2", fetchedResource.getContractId(), updatedResource.getContractId());
		Assert.assertEquals("CropYear 2", fetchedResource.getCropYear(), updatedResource.getCropYear());
		Assert.assertTrue("DataSyncTransDate 2", fetchedResource.getDataSyncTransDate().compareTo(updatedResource.getDataSyncTransDate()) == 0);
		
		//CLEAN UP: DELETE CODE
		delete(policyId);
		
		logger.debug(">testCreateUpdatePolicy");
	}

	
	@Test
	public void testUpdatePolicyWithoutRecordNoUpdate() throws CirrasUnderwritingServiceException, Oauth2ClientException, ValidationException, NotFoundDaoException, DaoException {
		logger.debug("<testUpdatePolicyWithoutRecordNoUpdate");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}

		//Date and Time without millisecond
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MILLISECOND, 0); //Set milliseconds to 0 becauce they are not set in the database
		Date transactionDate = cal.getTime();

		Date createTransactionDate = addSeconds(transactionDate, -1);

		Integer growerId = 8;
		Integer insurancePlanId = 5;
		String policyStatusCode = "ACTIVE";
		Integer officeId = 1;
		String policyNumber = "123123-21";
		String contractNumber = "123123";
		Integer contractId = 99998888;
		Integer cropYear = 2021;

		//CREATE Policy
		PolicyRsrc resource = new PolicyRsrc();
		
		resource.setPolicyId(policyId);
		resource.setGrowerId(growerId);
		resource.setInsurancePlanId(insurancePlanId);
		resource.setPolicyStatusCode(policyStatusCode);
		resource.setOfficeId(officeId);
		resource.setPolicyNumber(policyNumber);
		resource.setContractNumber(contractNumber);
		resource.setContractId(contractId);
		resource.setCropYear(cropYear);
		resource.setDataSyncTransDate(createTransactionDate);
		
		
		//TRY TO DELETE A Code THAT DOESN'T EXIST (NO ERROR EXPECTED)
		resource.setTransactionType(PoliciesSyncEventTypes.PolicyDeleted);
		service.synchronizePolicy(resource);

		//SHOULD RESULT IN AN INSERT
		resource.setTransactionType(PoliciesSyncEventTypes.PolicyUpdated);

		service.synchronizePolicy(resource);

		PolicyRsrc fetchedResource = service.getPolicy(topLevelEndpoints, policyId.toString()); 

		Assert.assertEquals("PolicyId 1", resource.getPolicyId(), fetchedResource.getPolicyId());
		Assert.assertEquals("GrowerId 1", resource.getGrowerId(), fetchedResource.getGrowerId());
		Assert.assertEquals("InsurancePlanId 1", resource.getInsurancePlanId(), fetchedResource.getInsurancePlanId());
		Assert.assertEquals("OfficeId 1", resource.getOfficeId(), fetchedResource.getOfficeId());
		Assert.assertEquals("PolicyStatusCode 1", resource.getPolicyStatusCode(), fetchedResource.getPolicyStatusCode());
		Assert.assertEquals("PolicyNumber 1", resource.getPolicyNumber(), fetchedResource.getPolicyNumber());
		Assert.assertEquals("ContractNumber 1", resource.getContractNumber(), fetchedResource.getContractNumber());
		Assert.assertEquals("ContractId 1", resource.getContractId(), fetchedResource.getContractId());
		Assert.assertEquals("CropYear 1", resource.getCropYear(), fetchedResource.getCropYear());
		Assert.assertTrue("DataSyncTransDate 1", resource.getDataSyncTransDate().compareTo(fetchedResource.getDataSyncTransDate()) == 0);
		
		//NO UPDATE EXPECTED BECAUSE TRANSACTION DATE IS EARLIER THAN STORED ONE
		fetchedResource.setDataSyncTransDate(addSeconds(createTransactionDate, -1));
		fetchedResource.setTransactionType(PoliciesSyncEventTypes.PolicyUpdated);
		service.synchronizePolicy(fetchedResource);
		
		PolicyRsrc notUpdatedResource = service.getPolicy(topLevelEndpoints, policyId.toString()); 

		Assert.assertTrue("DataSyncTransDate 2", resource.getDataSyncTransDate().compareTo(notUpdatedResource.getDataSyncTransDate()) == 0);

		
		//UPDATE Policy
		notUpdatedResource.setGrowerId(12);
		notUpdatedResource.setInsurancePlanId(4);
		notUpdatedResource.setPolicyStatusCode("UNDERWRITING");
		notUpdatedResource.setOfficeId(2);
		notUpdatedResource.setPolicyNumber("321321-22");
		notUpdatedResource.setContractNumber("321321");
		notUpdatedResource.setContractId(11112222);
		notUpdatedResource.setCropYear(2022);

		//UPDATE EXPECTED BECAUSE RECORD EXISTS IT WILL UPDATE IT
		notUpdatedResource.setTransactionType(PoliciesSyncEventTypes.PolicyCreated);
		notUpdatedResource.setDataSyncTransDate(addSeconds(createTransactionDate, 10));
		service.synchronizePolicy(notUpdatedResource);
		
		PolicyRsrc updatedResource = service.getPolicy(topLevelEndpoints, policyId.toString()); 

		Assert.assertEquals("PolicyId 2", notUpdatedResource.getPolicyId(), updatedResource.getPolicyId());
		Assert.assertEquals("GrowerId 2", notUpdatedResource.getGrowerId(), updatedResource.getGrowerId());
		Assert.assertEquals("InsurancePlanId 2", notUpdatedResource.getInsurancePlanId(), updatedResource.getInsurancePlanId());
		Assert.assertEquals("OfficeId 2", notUpdatedResource.getOfficeId(), updatedResource.getOfficeId());
		Assert.assertEquals("PolicyStatusCode 2", notUpdatedResource.getPolicyStatusCode(), updatedResource.getPolicyStatusCode());
		Assert.assertEquals("PolicyNumber 2", notUpdatedResource.getPolicyNumber(), updatedResource.getPolicyNumber());
		Assert.assertEquals("ContractNumber 2", notUpdatedResource.getContractNumber(), updatedResource.getContractNumber());
		Assert.assertEquals("ContractId 2", notUpdatedResource.getContractId(), updatedResource.getContractId());
		Assert.assertEquals("CropYear 2", notUpdatedResource.getCropYear(), updatedResource.getCropYear());
		Assert.assertTrue("DataSyncTransDate 2", notUpdatedResource.getDataSyncTransDate().compareTo(updatedResource.getDataSyncTransDate()) == 0);

		//CLEAN UP: DELETE CODE
		delete(policyId);		
		
		logger.debug(">testUpdatePolicyWithoutRecordNoUpdate");
	}

	private static Date addSeconds(Date date, Integer seconds) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.SECOND, seconds);
		return cal.getTime();
	}
	

}
