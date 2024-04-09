package ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao;

import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.UnderwritingYearDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.spring.PersistenceSpringConfig;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes= {TestConfig.class, PersistenceSpringConfig.class})
public class UnderwritingYearDaoTest {
	
	@Autowired 
	private PersistenceSpringConfig persistenceSpringConfig;
	
	private Integer cropYear1 = 2009;
	private Integer cropYear2 = 2010;
	private Integer cropYear3 = 2011;

	@Before
	public void prepareTests() throws NotFoundDaoException, DaoException{
		delete();
	}

	@After 
	public void cleanUp() throws NotFoundDaoException, DaoException{
		delete();
	}
	
	private void delete() throws NotFoundDaoException, DaoException{
		
		deleteByCropYear(cropYear1);
		deleteByCropYear(cropYear2);
		deleteByCropYear(cropYear3);

	}
	
	private void deleteByCropYear(Integer cropYear) throws DaoException {
		UnderwritingYearDao dao = persistenceSpringConfig.underwritingYearDao();
		UnderwritingYearDto dto = null;
		
		dto = dao.selectByCropYear(cropYear);

		if (dto != null) {
			dao.deleteByCropYear(cropYear);
		}
	}

	@Test 
	public void testInsertSelectDeleteUnderwritingYear() throws Exception {

		String userId = "JUNIT_TEST";

		String underwritingYearGuid = null;
		
		UnderwritingYearDao dao = persistenceSpringConfig.underwritingYearDao();
		UnderwritingYearDto newDto = new UnderwritingYearDto();
				
		List<UnderwritingYearDto> dtos = dao.fetchAll();
		
		int totalExistingRecords = 0;
		if(dtos != null && dtos.size() > 0) {
			totalExistingRecords = dtos.size();	
		}
		
		
		//INSERT Crop Years
		newDto.setCropYear(cropYear1);
		dao.insert(newDto, userId);

		newDto.setCropYear(cropYear2);
		dao.insert(newDto, userId);

		newDto.setCropYear(cropYear3);
		dao.insert(newDto, userId);

		underwritingYearGuid = newDto.getUnderwritingYearGuid();
		
		//FETCH
		UnderwritingYearDto fetchedDto = dao.fetch(newDto.getUnderwritingYearGuid());

		//expect cropYear3
		Assert.assertEquals("CropYear", newDto.getCropYear(), fetchedDto.getCropYear());
		Assert.assertEquals("UnderwritingYearGuid", newDto.getUnderwritingYearGuid(), fetchedDto.getUnderwritingYearGuid());

		// Fetch All
		dtos = dao.fetchAll();		
		Assert.assertNotNull(dtos);
		Assert.assertEquals(totalExistingRecords + 3, dtos.size());
		
		fetchedDto = dao.selectByCropYear(cropYear2);
		//expect cropYear2
		Assert.assertEquals("CropYear", cropYear2, fetchedDto.getCropYear());
		Assert.assertNotNull("UnderwritingYearGuid", fetchedDto.getUnderwritingYearGuid());
		
		//DELETE cropYear3
		dao.delete(underwritingYearGuid);

		//FETCH
		UnderwritingYearDto deletedDto = dao.fetch(underwritingYearGuid);
		Assert.assertNull(deletedDto);

		// Fetch All: Expect one less
		dtos = dao.fetchAll();		
		Assert.assertNotNull(dtos);
		Assert.assertEquals(totalExistingRecords + 2, dtos.size());
	}
	
	
}
