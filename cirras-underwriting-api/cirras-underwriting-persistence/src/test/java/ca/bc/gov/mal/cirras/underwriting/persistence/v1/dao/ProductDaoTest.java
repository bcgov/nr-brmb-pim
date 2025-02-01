package ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.GrowerDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.PolicyDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.ProductDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.spring.PersistenceSpringConfig;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes= {TestConfig.class, PersistenceSpringConfig.class})
public class ProductDaoTest {
	
	@Autowired 
	private PersistenceSpringConfig persistenceSpringConfig;
	
	private Integer productId = 99999999;
	private Integer productId2 = 79999999;

	private Integer policyId1 = 90000001;
	private Integer contractId1 = 90000002;
	private String policyNumber1 = "998877-20";
	private String contractNumber1 = "998877";
	private Integer cropYear1 = 2020;
		
	private Integer growerId1 = 90000007;
	
	@Before
	public void prepareTests() throws NotFoundDaoException, DaoException{
		delete();
	}

	@After 
	public void cleanUp() throws NotFoundDaoException, DaoException{
		delete();
	}
	

	private void delete() throws NotFoundDaoException, DaoException {

		deleteProduct(productId);
		deleteProduct(productId2);
		deletePolicy(policyId1);
		deleteGrower(growerId1);
	}

	private void deleteProduct(Integer productId) throws NotFoundDaoException, DaoException{
		
		ProductDao dao = persistenceSpringConfig.productDao();
		ProductDto dto = dao.fetch(productId);
		if (dto != null) {
			dao.delete(productId);
		}
	}
	
	private void deletePolicy(Integer policyId) throws NotFoundDaoException, DaoException{
		
		PolicyDao dao = persistenceSpringConfig.policyDao();
		PolicyDto dto = dao.fetch(policyId);
		if (dto != null) {
			dao.delete(policyId);
		}
	}
	
	private void deleteGrower(Integer growerId) throws DaoException {
		GrowerDao dao = persistenceSpringConfig.growerDao();
		GrowerDto dto = dao.fetch(growerId);
		if (dto != null) {
			dao.delete(growerId);
		}
	}

	private void createGrower(
		Integer growerId,
		Integer growerNumber,
		String growerName
	) throws DaoException {
		String userId = "JUNIT_TEST";

		//Date and Time without millisecond
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MILLISECOND, 0); //Set milliseconds to 0 becauce they are not set in the database
		Date transDate = cal.getTime();

		GrowerDao growerDao = persistenceSpringConfig.growerDao();
		GrowerDto growerDto = new GrowerDto();
		
		//INSERT
		growerDto.setGrowerId(growerId);
		growerDto.setGrowerNumber(growerNumber);
		growerDto.setGrowerName(growerName);
		growerDto.setGrowerAddressLine1("address line 1");
		growerDto.setGrowerAddressLine2("address line 2");
		growerDto.setGrowerPostalCode("V8P 4N8");
		growerDto.setGrowerCity("Victoria");
		growerDto.setCityId(1);
		growerDto.setGrowerProvince("BC");
		growerDto.setDataSyncTransDate(transDate);

		growerDao.insert(growerDto, userId);
	}

	
	private void createPolicy(
			Integer policyId,
			Integer growerId,
			Integer contractId,
			Integer cropYear,
			String policyNumber,
			String contractNumber,
			Integer insurancePlanId,
			String policyStatus,
			Integer officeId) throws DaoException {
		String userId = "JUNIT_TEST";

		//Date and Time without millisecond
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MILLISECOND, 0); //Set milliseconds to 0 becauce they are not set in the database
		Date transDate = cal.getTime();		

		// Policy
		PolicyDao policyDao = persistenceSpringConfig.policyDao();
		
		PolicyDto policyDto = new PolicyDto();
		
		policyDto.setPolicyId(policyId);
		policyDto.setGrowerId(growerId);
		policyDto.setInsurancePlanId(insurancePlanId);
		policyDto.setPolicyStatusCode(policyStatus);
		policyDto.setOfficeId(officeId);
		policyDto.setPolicyNumber(policyNumber);
		policyDto.setContractNumber(contractNumber);
		policyDto.setContractId(contractId);
		policyDto.setCropYear(cropYear);
		policyDto.setDataSyncTransDate(transDate);
		
		//INSERT
		policyDao.insert(policyDto, userId);
	}
	

	@Test 
	public void testInsertUpdateDeleteProduct() throws Exception {

		Integer insurancePlanId = 4;
		String policyStatusCode = "ACTIVE";
		Integer officeId = 1;
		String userId = "UNITTEST";

		//Date and Time without millisecond
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MILLISECOND, 0); //Set milliseconds to 0 becauce they are not set in the database
		Date dateTime = cal.getTime();

		Date dataSyncTransDate = addSeconds(dateTime, -120);

		createGrower(growerId1, 999888, "grower name");
		createPolicy(policyId1, growerId1, contractId1, cropYear1, policyNumber1, contractNumber1, insurancePlanId, policyStatusCode, officeId);
		

		ProductDao dao = persistenceSpringConfig.productDao();
		
		ProductDto newDto = new ProductDto();

		newDto.setCommodityCoverageCode("CQG");
		newDto.setCropCommodityId(16);
		newDto.setDeductibleLevel(20);
		newDto.setInsuredByMeasType("ACRES");
		newDto.setPolicyId(policyId1);
		newDto.setProbableYield(12.34);
		newDto.setProductId(productId);
		newDto.setProductionGuarantee(56.78);
		newDto.setProductStatusCode("OFFER");
		newDto.setInsurableValueHundredPercent(150.5);
		newDto.setCoverageDollars(1155.7);


		newDto.setDataSyncTransDate(dataSyncTransDate);

		//INSERT
		dao.insert(newDto, userId);
		
		//FETCH
		ProductDto fetchedDto = dao.fetch(productId);
		
		checkProduct(newDto, fetchedDto);
		
		//UPDATE
		dataSyncTransDate = addSeconds(dateTime, -60);

		fetchedDto.setCommodityCoverageCode("GB");
		fetchedDto.setCropCommodityId(1010076);
		fetchedDto.setDeductibleLevel(30);
		fetchedDto.setInsuredByMeasType("UNKNOWN");
		fetchedDto.setPolicyId(policyId1);
		fetchedDto.setProbableYield(11.22);
		fetchedDto.setProductionGuarantee(33.44);
		fetchedDto.setProductStatusCode("FINAL");
		fetchedDto.setInsurableValueHundredPercent(210.5);
		fetchedDto.setCoverageDollars(8899.7);
		fetchedDto.setDataSyncTransDate(dataSyncTransDate);
		
		dao.update(fetchedDto, userId);

		//FETCH
		ProductDto updatedDto = dao.fetch(productId);

		checkProduct(fetchedDto, updatedDto);
		
		//Expect NO update becaus the transaction date is before the latest update
		userId = "JUNIT_TEST_NO_UPDATE";
		Date newDataSyncTransDate = addSeconds(dateTime, -200);
		updatedDto.setDataSyncTransDate(newDataSyncTransDate);

		dao.update(updatedDto, userId);
		
		//FETCH
		ProductDto notUpdatedDto = dao.fetch(productId);

		//DataSyncTransDate is still the same (no update happened)
		Assert.assertTrue("DataSyncTransDate 3", notUpdatedDto.getDataSyncTransDate().compareTo(dataSyncTransDate) == 0);

		
		//Test get for policy
		
		//Add second product
		dao = persistenceSpringConfig.productDao();
		
		newDto = new ProductDto();

		newDto.setCommodityCoverageCode("CQG");
		newDto.setCropCommodityId(25); //Pedigree product
		newDto.setDeductibleLevel(20);
		newDto.setInsuredByMeasType("ACRES");
		newDto.setPolicyId(policyId1);
		newDto.setProbableYield(22.2);
		newDto.setProductId(productId2);
		newDto.setProductionGuarantee(99.0);
		newDto.setInsurableValueHundredPercent(180.5);
		newDto.setCoverageDollars(3322.7);
		newDto.setProductStatusCode("FINAL");
		newDto.setNonPedigreeCropCommodityId(24);
		newDto.setIsPedigreeProduct(true);

		newDto.setDataSyncTransDate(dataSyncTransDate);

		//INSERT
		dao.insert(newDto, userId);

		//Set expected values for first product
		notUpdatedDto.setNonPedigreeCropCommodityId(notUpdatedDto.getCropCommodityId());
		notUpdatedDto.setIsPedigreeProduct(false);
		
		List<ProductDto> dtos = dao.getForPolicy(contractId1, cropYear1);
		Assert.assertEquals(2, dtos.size());
		
		for(ProductDto dto : dtos) {
			if(dto.getCropCommodityId().equals(newDto.getCropCommodityId())) {
				checkProduct(newDto, dto);
			} else {
				checkProduct(notUpdatedDto, dto);
			}
		}
		
		//DELETE
		dao.delete(productId);
		
		//FETCH
		ProductDto deletedDto = dao.fetch(productId);
		Assert.assertNull(deletedDto);
	}

	private void checkProduct(ProductDto expected, ProductDto actual) {
		Assert.assertEquals("CommodityCoverageCode", expected.getCommodityCoverageCode(), actual.getCommodityCoverageCode());
		Assert.assertEquals("CropCommodityId", expected.getCropCommodityId(), actual.getCropCommodityId());
		Assert.assertEquals("DeductibleLevel", expected.getDeductibleLevel(), actual.getDeductibleLevel());
		Assert.assertEquals("InsuredByMeasType", expected.getInsuredByMeasType(), actual.getInsuredByMeasType());
		Assert.assertEquals("PolicyId", expected.getPolicyId(), actual.getPolicyId());
		Assert.assertEquals("ProbableYield", expected.getProbableYield(), actual.getProbableYield());
		Assert.assertEquals("ProductId", expected.getProductId(), actual.getProductId());
		Assert.assertEquals("ProductionGuarantee", expected.getProductionGuarantee(), actual.getProductionGuarantee());
		Assert.assertEquals("ProductStatusCode", expected.getProductStatusCode(), actual.getProductStatusCode());
		Assert.assertEquals("InsurableValueHundredPercent", expected.getInsurableValueHundredPercent(), actual.getInsurableValueHundredPercent());
		Assert.assertEquals("CoverageDollars", expected.getCoverageDollars(), actual.getCoverageDollars());
		Assert.assertEquals("DataSyncTransDate", expected.getDataSyncTransDate(), actual.getDataSyncTransDate());
		
		Assert.assertEquals("NonPedigreeCropCommodityId", expected.getNonPedigreeCropCommodityId(), actual.getNonPedigreeCropCommodityId());
		Assert.assertEquals("IsPedigreeProduct", expected.getIsPedigreeProduct(), actual.getIsPedigreeProduct());

	}
	
	private static Date addSeconds(Date date, Integer seconds) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.SECOND, seconds);
		return cal.getTime();
	}
	
}
