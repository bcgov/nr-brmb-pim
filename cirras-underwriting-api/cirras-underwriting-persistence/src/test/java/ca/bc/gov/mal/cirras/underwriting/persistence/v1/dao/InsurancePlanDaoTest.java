package ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.InsurancePlanDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.spring.PersistenceSpringConfig;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes= {TestConfig.class, PersistenceSpringConfig.class})
public class InsurancePlanDaoTest {
	
	@Autowired 
	private PersistenceSpringConfig persistenceSpringConfig;

	@Test
	public void testFetchPlan() throws Exception {
		InsurancePlanDao dao = persistenceSpringConfig.insurancePlanDao();
		
		//FETCH
		InsurancePlanDto fetchedDto = dao.fetch(4);

		Assert.assertEquals("InsurancePlanId", 4, fetchedDto.getInsurancePlanId().intValue());
		Assert.assertEquals("InsurancePlanName", "GRAIN", fetchedDto.getInsurancePlanName());		
	}
	
	@Test 
	public void testSelectPlansByFieldId() throws Exception {	
		
		InsurancePlanDao dao = persistenceSpringConfig.insurancePlanDao();
		
		List<InsurancePlanDto> dtos = dao.selectByField(22151);
		
		Assert.assertNotNull(dtos);
		Assert.assertEquals(1, dtos.size());
		Assert.assertEquals(4, dtos.get(0).getInsurancePlanId().intValue());
		Assert.assertEquals("GRAIN", dtos.get(0).getInsurancePlanName());

		dtos = dao.selectByField(716);
		
		Assert.assertNotNull(dtos);
		Assert.assertEquals(0, dtos.size());
	}
}
