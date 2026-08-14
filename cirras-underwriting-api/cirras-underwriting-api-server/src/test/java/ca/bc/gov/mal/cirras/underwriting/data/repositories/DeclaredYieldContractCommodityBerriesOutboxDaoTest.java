package ca.bc.gov.mal.cirras.underwriting.data.repositories;

import java.time.LocalDate;
import java.time.ZoneId;
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

import ca.bc.gov.mal.cirras.underwriting.data.entities.GrowerContractYearDto;
import ca.bc.gov.mal.cirras.underwriting.data.models.OutboxTransactionTypes;
import ca.bc.gov.mal.cirras.underwriting.data.entities.DeclaredYieldContractCommodityBerriesDto;
import ca.bc.gov.mal.cirras.underwriting.data.entities.DeclaredYieldContractCommodityBerriesOutboxDto;
import ca.bc.gov.mal.cirras.underwriting.data.entities.DeclaredYieldContractDto;
import ca.bc.gov.mal.cirras.underwriting.spring.PersistenceSpringConfig;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes= {TestConfig.class, PersistenceSpringConfig.class})
public class DeclaredYieldContractCommodityBerriesOutboxDaoTest {
	
	@Autowired 
	private PersistenceSpringConfig persistenceSpringConfig;
	
	private String declaredYieldContractCommodityBerriesGuid = "abc1236843219";
	private Integer declaredYieldContractCommodityBerriesOutboxId1 = null;
	private Integer declaredYieldContractCommodityBerriesOutboxId2 = null;


	@Before
	public void prepareTests() throws NotFoundDaoException, DaoException{
		delete();
	}

	@After 
	public void cleanUp() throws NotFoundDaoException, DaoException{
		delete();
	}
	
	private void delete() throws NotFoundDaoException, DaoException{

		deleteDyccbo(declaredYieldContractCommodityBerriesOutboxId1);
		deleteDyccbo(declaredYieldContractCommodityBerriesOutboxId2);
				
	}

	private void deleteDyccbo(Integer declaredYieldContractCommodityBerriesOutboxId) throws NotFoundDaoException, DaoException{
		//DELETE Declared Yield Contract Commodity Berries Outbox Records
		DeclaredYieldContractCommodityBerriesOutboxDao dyccbDao = persistenceSpringConfig.declaredYieldContractCommodityBerriesOutboxDao();
		
		if (declaredYieldContractCommodityBerriesOutboxId != null) {
			DeclaredYieldContractCommodityBerriesOutboxDto dto = dyccbDao.fetch(declaredYieldContractCommodityBerriesOutboxId);
			if (dto != null) {
				dyccbDao.delete(declaredYieldContractCommodityBerriesOutboxId);
			}
		}
	}
	
	@Test 
	public void testDeclaredYieldContractCommodityBerriesOutbox() throws Exception {

		String userId = "UNITTEST";
		
		DeclaredYieldContractCommodityBerriesOutboxDao dao = persistenceSpringConfig.declaredYieldContractCommodityBerriesOutboxDao();

		// INSERT
		DeclaredYieldContractCommodityBerriesOutboxDto newDto = new DeclaredYieldContractCommodityBerriesOutboxDto();
		newDto.setAuditTransactionTypeCode(OutboxTransactionTypes.Insert);
		newDto.setDeclaredYieldContractCommodityBerriesGuid(declaredYieldContractCommodityBerriesGuid);

		dao.insert(newDto, userId);
		Assert.assertNotNull(newDto.getDeclaredYieldContractCommodityBerriesOutboxId());
		declaredYieldContractCommodityBerriesOutboxId1 = newDto.getDeclaredYieldContractCommodityBerriesOutboxId();
		
		//FETCH
		DeclaredYieldContractCommodityBerriesOutboxDto fetchedDto = dao.fetch(declaredYieldContractCommodityBerriesOutboxId1);
		Assert.assertEquals("DyccbObId", newDto.getDeclaredYieldContractCommodityBerriesOutboxId(), fetchedDto.getDeclaredYieldContractCommodityBerriesOutboxId());
		Assert.assertEquals("AuditTransactionTypeCode", newDto.getAuditTransactionTypeCode(), fetchedDto.getAuditTransactionTypeCode());
		Assert.assertEquals("DeclaredYieldContractCommodityBerriesGuid", newDto.getDeclaredYieldContractCommodityBerriesGuid(), fetchedDto.getDeclaredYieldContractCommodityBerriesGuid());
		
		//UPDATE
		fetchedDto.setAuditTransactionTypeCode(OutboxTransactionTypes.Update);
		
		dao.update(fetchedDto, userId);

		//FETCH
		DeclaredYieldContractCommodityBerriesOutboxDto updatedDto = dao.fetch(declaredYieldContractCommodityBerriesOutboxId1);

		Assert.assertEquals("AuditTransactionTypeCode", fetchedDto.getAuditTransactionTypeCode(), updatedDto.getAuditTransactionTypeCode());


		//INSERT second commodity
		DeclaredYieldContractCommodityBerriesOutboxDto newDto2 = new DeclaredYieldContractCommodityBerriesOutboxDto();
		newDto2.setAuditTransactionTypeCode(OutboxTransactionTypes.Insert);
		newDto2.setDeclaredYieldContractCommodityBerriesGuid(declaredYieldContractCommodityBerriesGuid);

		dao.insert(newDto2, userId);
		Assert.assertNotNull(newDto2.getDeclaredYieldContractCommodityBerriesOutboxId());
		declaredYieldContractCommodityBerriesOutboxId2 = newDto2.getDeclaredYieldContractCommodityBerriesOutboxId();

		//SELECT
		List<DeclaredYieldContractCommodityBerriesOutboxDto> dtos = dao.select(1); //Only get 1 record
		Assert.assertNotNull(dtos);
		Assert.assertEquals(1, dtos.size()); //Only expect one
		
		//DELETE 1
		dao.delete(declaredYieldContractCommodityBerriesOutboxId2);

		//FETCH
		DeclaredYieldContractCommodityBerriesOutboxDto deletedDto = dao.fetch(declaredYieldContractCommodityBerriesOutboxId2);
		Assert.assertNull(deletedDto);

		delete();
	}
	 
}
