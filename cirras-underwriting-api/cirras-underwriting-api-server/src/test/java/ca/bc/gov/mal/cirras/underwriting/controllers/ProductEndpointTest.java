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
import ca.bc.gov.mal.cirras.underwriting.data.resources.GrowerRsrc;
import ca.bc.gov.mal.cirras.underwriting.data.resources.PolicyRsrc;
import ca.bc.gov.mal.cirras.underwriting.data.resources.ProductRsrc;
import ca.bc.gov.mal.cirras.underwriting.test.EndpointsTest;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;
import ca.bc.gov.nrs.wfone.common.webade.oauth2.token.client.Oauth2ClientException;
import ca.bc.gov.mal.cirras.underwriting.data.resources.PoliciesSyncEventTypes;


public class ProductEndpointTest extends EndpointsTest {
	private static final Logger logger = LoggerFactory.getLogger(ProductEndpointTest.class);


	private static final String[] SCOPES = {
		Scopes.GET_TOP_LEVEL, 
		Scopes.UPDATE_SYNC_UNDERWRITING,
		Scopes.DELETE_SYNC_UNDERWRITING,
		Scopes.GET_PRODUCT
	};
	
	private CirrasUnderwritingService service;
	private EndpointsRsrc topLevelEndpoints;

	private Integer contractId = 90000001;
	private Integer policyId = 90000002;
	private String policyNumber = "998877-22";
	private String contractNumber = "998877";
	private Integer cropYear = 2022;
	private Integer growerId = 90000003;
	private Integer productId = 90000004;
	
	
	
	
	@Before
	public void prepareTests() throws CirrasUnderwritingServiceException, Oauth2ClientException, NotFoundDaoException, DaoException{
		service = getService(SCOPES);
		topLevelEndpoints = service.getTopLevelEndpoints();

		delete();

	}

	@After 
	public void cleanUp() throws CirrasUnderwritingServiceException, NotFoundDaoException, DaoException {
		delete();
	}

		
	private void delete() throws NotFoundDaoException, DaoException, CirrasUnderwritingServiceException{
		
		service.deleteProduct(topLevelEndpoints, productId.toString());
		service.deletePolicy(topLevelEndpoints, policyId.toString());
		service.deleteGrower(topLevelEndpoints, growerId.toString());
		
	}

	private void createPolicy(
			Integer policyId, 
			Integer growerId, 
			Integer insurancePlanId, 
			String policyNumber, 
			String contractNumber, 
			Integer contractId, 
			Integer cropYear, 
			Date createTransactionDate
	) throws ValidationException, CirrasUnderwritingServiceException {

		PolicyRsrc resource = new PolicyRsrc();
		
		resource.setPolicyId(policyId);
		resource.setGrowerId(growerId);
		resource.setInsurancePlanId(insurancePlanId);
		resource.setPolicyStatusCode("ACTIVE");
		resource.setOfficeId(1);
		resource.setPolicyNumber(policyNumber);
		resource.setContractNumber(contractNumber);
		resource.setContractId(contractId);
		resource.setCropYear(cropYear);
		
		resource.setDataSyncTransDate(createTransactionDate);
		resource.setTransactionType(PoliciesSyncEventTypes.PolicyCreated);
		
		service.synchronizePolicy(resource);
	}
	
	
	private void createGrower(Integer growerId, Integer growerNumber, String growerName, Date createTransactionDate) throws DaoException, CirrasUnderwritingServiceException, ValidationException {
		GrowerRsrc resource = new GrowerRsrc();
		
		String growerAddressLine1 = "address line 1";
		String growerAddressLine2 = "address line 2";
		String growerPostalCode = "V8P 4N8";
		String growerCity = "Victoria";
		Integer cityId = 1;
		String growerProvince = "BC";
		
		//INSERT
		resource.setGrowerId(growerId);
		resource.setGrowerNumber(growerNumber);
		resource.setGrowerName(growerName);
		resource.setGrowerAddressLine1(growerAddressLine1);
		resource.setGrowerAddressLine2(growerAddressLine2);
		resource.setGrowerPostalCode(growerPostalCode);
		resource.setGrowerCity(growerCity);
		resource.setCityId(cityId);
		resource.setGrowerProvince(growerProvince);

		resource.setDataSyncTransDate(createTransactionDate);
		resource.setTransactionType(PoliciesSyncEventTypes.GrowerCreated);

		service.synchronizeGrower(resource);
	}
	
	
	@Test
	public void testCreateUpdateDeleteProduct() throws CirrasUnderwritingServiceException, Oauth2ClientException, ValidationException, NotFoundDaoException, DaoException {
		logger.debug("<testCreateUpdateDeleteProduct");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}
		
		//Date and Time without millisecond
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MILLISECOND, 0); //Set milliseconds to 0 becauce they are not set in the database
		Date transactionDate = cal.getTime();

		Date createTransactionDate = addSeconds(transactionDate, -1);

		Integer insurancePlanId = 4;

		createGrower(growerId, 999888, "grower name", createTransactionDate);
		createPolicy(policyId, growerId, insurancePlanId, policyNumber, contractNumber, contractId, cropYear, createTransactionDate);
		
		
		
		//CREATE Product
		ProductRsrc resource = new ProductRsrc();
		
		resource.setCommodityCoverageCode("CQG");
		resource.setCropCommodityId(16);
		resource.setDeductibleLevel(20);
		resource.setInsuredByMeasType("ACRES");
		resource.setPolicyId(policyId);
		resource.setProbableYield(12.34);
		resource.setProductId(productId);
		resource.setProductionGuarantee(56.78);
		resource.setProductStatusCode("OFFER");
		resource.setInsurableValueHundredPercent(150.5);
		resource.setCoverageDollars(1155.7);


		resource.setDataSyncTransDate(createTransactionDate);
		resource.setTransactionType(PoliciesSyncEventTypes.ProductCreated);

		service.synchronizeProduct(resource);
		
		ProductRsrc fetchedResource = service.getProduct(topLevelEndpoints, productId.toString()); 

		Assert.assertEquals(resource.getCommodityCoverageCode(), fetchedResource.getCommodityCoverageCode());
		Assert.assertEquals(resource.getCropCommodityId(), fetchedResource.getCropCommodityId());
		Assert.assertEquals(resource.getDeductibleLevel(), fetchedResource.getDeductibleLevel());
		Assert.assertEquals(resource.getInsuredByMeasType(), fetchedResource.getInsuredByMeasType());
		Assert.assertEquals(resource.getPolicyId(), fetchedResource.getPolicyId());
		Assert.assertEquals(resource.getProbableYield(), fetchedResource.getProbableYield());
		Assert.assertEquals(resource.getProductId(), fetchedResource.getProductId());
		Assert.assertEquals(resource.getProductionGuarantee(), fetchedResource.getProductionGuarantee());
		Assert.assertEquals(resource.getProductStatusCode(), fetchedResource.getProductStatusCode());
		Assert.assertEquals(resource.getInsurableValueHundredPercent(), fetchedResource.getInsurableValueHundredPercent());
		Assert.assertEquals(resource.getCoverageDollars(), fetchedResource.getCoverageDollars());

		Assert.assertTrue(resource.getDataSyncTransDate().compareTo(fetchedResource.getDataSyncTransDate()) == 0);

		
		//UPDATE PRODUCT
		fetchedResource.setCommodityCoverageCode("GB");
		fetchedResource.setCropCommodityId(1010076);
		fetchedResource.setDeductibleLevel(30);
		fetchedResource.setInsuredByMeasType("UNKNOWN");
		fetchedResource.setPolicyId(policyId);
		fetchedResource.setProbableYield(11.22);
		fetchedResource.setProductionGuarantee(33.44);
		fetchedResource.setProductStatusCode("FINAL");
		fetchedResource.setInsurableValueHundredPercent(234.5);
		fetchedResource.setCoverageDollars(2233.5);


		fetchedResource.setDataSyncTransDate(addSeconds(transactionDate, +1));
		fetchedResource.setTransactionType(PoliciesSyncEventTypes.ProductUpdated);

		service.synchronizeProduct(fetchedResource);
		
		ProductRsrc updatedResource = service.getProduct(topLevelEndpoints, productId.toString());

		Assert.assertEquals(fetchedResource.getCommodityCoverageCode(), updatedResource.getCommodityCoverageCode());
		Assert.assertEquals(fetchedResource.getCropCommodityId(), updatedResource.getCropCommodityId());
		Assert.assertEquals(fetchedResource.getDeductibleLevel(), updatedResource.getDeductibleLevel());
		Assert.assertEquals(fetchedResource.getInsuredByMeasType(), updatedResource.getInsuredByMeasType());
		Assert.assertEquals(fetchedResource.getPolicyId(), updatedResource.getPolicyId());
		Assert.assertEquals(fetchedResource.getProbableYield(), updatedResource.getProbableYield());
		Assert.assertEquals(fetchedResource.getProductId(), updatedResource.getProductId());
		Assert.assertEquals(fetchedResource.getProductionGuarantee(), updatedResource.getProductionGuarantee());
		Assert.assertEquals(fetchedResource.getProductStatusCode(), updatedResource.getProductStatusCode());
		Assert.assertEquals(fetchedResource.getInsurableValueHundredPercent(), updatedResource.getInsurableValueHundredPercent());
		Assert.assertEquals(fetchedResource.getCoverageDollars(), updatedResource.getCoverageDollars());

		Assert.assertTrue(fetchedResource.getDataSyncTransDate().compareTo(updatedResource.getDataSyncTransDate()) == 0);
		
		//DELETE PRODUCT
		updatedResource.setDataSyncTransDate(addSeconds(transactionDate, +1));
		updatedResource.setTransactionType(PoliciesSyncEventTypes.ProductDeleted);
		
		service.synchronizeProduct(updatedResource);

		ProductRsrc deletedResource = service.getProduct(topLevelEndpoints, productId.toString());
		Assert.assertNull(deletedResource);

		//CREATE Product
		ProductRsrc resource2 = new ProductRsrc();
		
		resource2.setCommodityCoverageCode("CQG");
		resource2.setCropCommodityId(16);
		resource2.setDeductibleLevel(20);
		resource2.setInsuredByMeasType("ACRES");
		resource2.setPolicyId(policyId);
		resource2.setProbableYield(12.34);
		resource2.setProductId(productId);
		resource2.setProductionGuarantee(56.78);
		resource2.setProductStatusCode("OFFER");
		resource2.setInsurableValueHundredPercent(188.3);
		resource2.setCoverageDollars(8945.4);

		resource2.setDataSyncTransDate(createTransactionDate);
		resource2.setTransactionType(PoliciesSyncEventTypes.ProductCreated);

		service.synchronizeProduct(resource2);
		
		ProductRsrc fetchedResource2 = service.getProduct(topLevelEndpoints, productId.toString()); 

		Assert.assertEquals(resource2.getProductId(), fetchedResource2.getProductId());

		//DELETE PRODUCT using the other delete method.
		service.deleteProduct(topLevelEndpoints, productId.toString());

		ProductRsrc deletedResource2 = service.getProduct(topLevelEndpoints, productId.toString());
		Assert.assertNull(deletedResource2);
		
		logger.debug(">testCreateUpdateDeleteProduct");
	}

	
	@Test
	public void testUpdateProductWithoutRecordNoUpdate() throws CirrasUnderwritingServiceException, Oauth2ClientException, ValidationException, NotFoundDaoException, DaoException {
		logger.debug("<testUpdateProductWithoutRecordNoUpdate");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}

		//Date and Time without millisecond
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MILLISECOND, 0); //Set milliseconds to 0 becauce they are not set in the database
		Date transactionDate = cal.getTime();

		Date createTransactionDate = addSeconds(transactionDate, -1);

		Integer insurancePlanId = 4;

		createGrower(growerId, 999888, "grower name", createTransactionDate);
		createPolicy(policyId, growerId, insurancePlanId, policyNumber, contractNumber, contractId, cropYear, createTransactionDate);
		
		
		ProductRsrc resource = new ProductRsrc();
		
		resource.setCommodityCoverageCode("CQG");
		resource.setCropCommodityId(16);
		resource.setDeductibleLevel(20);
		resource.setInsuredByMeasType("ACRES");
		resource.setPolicyId(policyId);
		resource.setProbableYield(12.34);
		resource.setProductId(productId);
		resource.setProductionGuarantee(56.78);
		resource.setProductStatusCode("OFFER");
		resource.setInsurableValueHundredPercent(150.5);
		resource.setCoverageDollars(1155.7);
		

		resource.setDataSyncTransDate(createTransactionDate);

		
		//TRY TO DELETE A Product THAT DOESN'T EXIST (NO ERROR EXPECTED)
		resource.setTransactionType(PoliciesSyncEventTypes.ProductDeleted);
		service.synchronizeProduct(resource);

		//SHOULD RESULT IN AN INSERT
		resource.setTransactionType(PoliciesSyncEventTypes.ProductUpdated);

		service.synchronizeProduct(resource);

		ProductRsrc fetchedResource = service.getProduct(topLevelEndpoints, productId.toString()); 

		Assert.assertEquals(resource.getCommodityCoverageCode(), fetchedResource.getCommodityCoverageCode());
		Assert.assertEquals(resource.getCropCommodityId(), fetchedResource.getCropCommodityId());
		Assert.assertEquals(resource.getDeductibleLevel(), fetchedResource.getDeductibleLevel());
		Assert.assertEquals(resource.getInsuredByMeasType(), fetchedResource.getInsuredByMeasType());
		Assert.assertEquals(resource.getPolicyId(), fetchedResource.getPolicyId());
		Assert.assertEquals(resource.getProbableYield(), fetchedResource.getProbableYield());
		Assert.assertEquals(resource.getProductId(), fetchedResource.getProductId());
		Assert.assertEquals(resource.getProductionGuarantee(), fetchedResource.getProductionGuarantee());
		Assert.assertEquals(resource.getProductStatusCode(), fetchedResource.getProductStatusCode());
		Assert.assertEquals(resource.getInsurableValueHundredPercent(), fetchedResource.getInsurableValueHundredPercent());
		Assert.assertEquals(resource.getCoverageDollars(), fetchedResource.getCoverageDollars());

		Assert.assertTrue(resource.getDataSyncTransDate().compareTo(fetchedResource.getDataSyncTransDate()) == 0);
		
		//NO UPDATE EXPECTED BECAUSE TRANSACTION DATE IS EARLIER THAN STORED ONE
		fetchedResource.setDataSyncTransDate(addSeconds(createTransactionDate, -1));
		fetchedResource.setTransactionType(PoliciesSyncEventTypes.ProductUpdated);
		service.synchronizeProduct(fetchedResource);
		
		ProductRsrc notUpdatedResource = service.getProduct(topLevelEndpoints, productId.toString()); 

		Assert.assertTrue(resource.getDataSyncTransDate().compareTo(notUpdatedResource.getDataSyncTransDate()) == 0);


		//UPDATE PRODUCT
		notUpdatedResource.setCommodityCoverageCode("GB");
		notUpdatedResource.setCropCommodityId(1010076);
		notUpdatedResource.setDeductibleLevel(30);
		notUpdatedResource.setInsuredByMeasType("UNKNOWN");
		notUpdatedResource.setPolicyId(policyId);
		notUpdatedResource.setProbableYield(11.22);
		notUpdatedResource.setProductionGuarantee(33.44);
		notUpdatedResource.setProductStatusCode("FINAL");
		notUpdatedResource.setInsurableValueHundredPercent(222.5);
		notUpdatedResource.setCoverageDollars(1235.7);

		
		//UPDATE EXPECTED BECAUSE RECORD EXISTS IT WILL UPDATE IT
		notUpdatedResource.setTransactionType(PoliciesSyncEventTypes.ProductCreated);
		notUpdatedResource.setDataSyncTransDate(addSeconds(createTransactionDate, 10));
		service.synchronizeProduct(notUpdatedResource);
		
		ProductRsrc updatedResource = service.getProduct(topLevelEndpoints, productId.toString()); 

		Assert.assertEquals(notUpdatedResource.getCommodityCoverageCode(), updatedResource.getCommodityCoverageCode());
		Assert.assertEquals(notUpdatedResource.getCropCommodityId(), updatedResource.getCropCommodityId());
		Assert.assertEquals(notUpdatedResource.getDeductibleLevel(), updatedResource.getDeductibleLevel());
		Assert.assertEquals(notUpdatedResource.getInsuredByMeasType(), updatedResource.getInsuredByMeasType());
		Assert.assertEquals(notUpdatedResource.getPolicyId(), updatedResource.getPolicyId());
		Assert.assertEquals(notUpdatedResource.getProbableYield(), updatedResource.getProbableYield());
		Assert.assertEquals(notUpdatedResource.getProductId(), updatedResource.getProductId());
		Assert.assertEquals(notUpdatedResource.getProductionGuarantee(), updatedResource.getProductionGuarantee());
		Assert.assertEquals(notUpdatedResource.getProductStatusCode(), updatedResource.getProductStatusCode());
		Assert.assertEquals(notUpdatedResource.getInsurableValueHundredPercent(), updatedResource.getInsurableValueHundredPercent());
		Assert.assertEquals(notUpdatedResource.getCoverageDollars(), updatedResource.getCoverageDollars());

		Assert.assertTrue(notUpdatedResource.getDataSyncTransDate().compareTo(updatedResource.getDataSyncTransDate()) == 0);
		
		logger.debug(">testUpdateProductWithoutRecordNoUpdate");
	}

	private static Date addSeconds(Date date, Integer seconds) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.SECOND, seconds);
		return cal.getTime();
	}
	

}
