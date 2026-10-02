package ca.bc.gov.mal.cirras.underwriting.listener.failover.persistence.v1.dao;

import static org.junit.Assert.assertNotNull;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import ca.bc.gov.mal.cirras.underwriting.listener.failover.persistence.v1.dto.SyncOwnershipDto;
import ca.bc.gov.mal.cirras.underwriting.listener.failover.persistence.v1.dao.SyncOwnershipDao;
import ca.bc.gov.mal.cirras.underwriting.listener.failover.persistence.v1.spring.FailoverPersistenceSpringConfig;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes= {TestConfig.class, FailoverPersistenceSpringConfig.class})
public class UnderwritingListenerFailoverDaoTest {
	
	@Autowired 
	private FailoverPersistenceSpringConfig persistenceSpringConfig;
	
	@Test 
	public void testInsertUpdateDeleteSyncOwnership() throws Exception {

		SyncOwnershipDao dao = persistenceSpringConfig.syncOwnershipDao();
		SyncOwnershipDto newDto = new SyncOwnershipDto();
		
		String processName = "UNDERWRITING_EVENT_PROCESSOR_TEST";
		String nodeId = "test node id";
		Integer nodeExpiryMinutes = 10;
		String userId = "JUNIT_TEST";

		//INSERT
		newDto.setProcessName(processName);
		newDto.setNodeId(nodeId);

		dao.insert(newDto, nodeExpiryMinutes, userId);
		
		//FETCH
		SyncOwnershipDto fetchedDto = dao.select(processName);

		Assert.assertEquals("Process Name 1", newDto.getProcessName(), fetchedDto.getProcessName());
		Assert.assertEquals("Service Node 1", newDto.getNodeId(), fetchedDto.getNodeId());
		Assert.assertEquals("Expired Indicator 1", false, fetchedDto.getExpiredInd());
		
		//UPDATE
		processName = "UNDERWRITING_EVENT_PROCESSOR_TEST_2";
		nodeId = "test node id 2";
		nodeExpiryMinutes = -20; //Want ExpiredInd to return Y
		String syncOwnershipGuid = fetchedDto.getSyncOwnershipGuid();

		fetchedDto.setProcessName(processName);
		fetchedDto.setNodeId(nodeId);

		dao.update(syncOwnershipGuid, fetchedDto, nodeExpiryMinutes, userId);
		
		//FETCH
		SyncOwnershipDto updatedDto = dao.select(processName);

		Assert.assertEquals("GUID 2", syncOwnershipGuid, updatedDto.getSyncOwnershipGuid());
		Assert.assertEquals("Process Name 2", fetchedDto.getProcessName(), updatedDto.getProcessName());
		Assert.assertEquals("Service Node 2", fetchedDto.getNodeId(), updatedDto.getNodeId());
		Assert.assertEquals("Expired Indicator 2", true, updatedDto.getExpiredInd());

		//DELETE
		dao.delete(syncOwnershipGuid);

		//FETCH
		SyncOwnershipDto deletedDto = dao.select(processName);
		Assert.assertNull(deletedDto);

	}
	

}
