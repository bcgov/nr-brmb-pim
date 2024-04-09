package ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.mybatis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.SeedingDeadlineDao;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.mybatis.mapper.SeedingDeadlineMapper;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.RiskAreaDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.SeedingDeadlineDto;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.mybatis.BaseDao;


@Repository
public class SeedingDeadlineDaoImpl extends BaseDao implements SeedingDeadlineDao {

	private static final long serialVersionUID = 1L;

	private static final Logger logger = LoggerFactory.getLogger(SeedingDeadlineDaoImpl.class);

	@Autowired
	private SeedingDeadlineMapper mapper;

	@Override
	public SeedingDeadlineDto fetch(String seedingDeadlineGuid) throws DaoException {
		logger.debug("<fetch");

		SeedingDeadlineDto result = null;

		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("seedingDeadlineGuid", seedingDeadlineGuid);
			result = this.mapper.fetch(parameters);
			
			if(result!=null) {
				result.resetDirty();
			}
		} catch (RuntimeException e) {
			handleException(e);
		}

		logger.debug(">fetch " + result);
		return result;	
	}

	
	@Override
	public void insert(SeedingDeadlineDto dto, String userId) throws DaoException {
		logger.debug("<insert");

		String seedingDeadlineGuid = null;
		
		try {
			Map<String, Object> parameters = new HashMap<String, Object>();

			parameters.put("dto", dto);
			parameters.put("userId", userId);
			int count = this.mapper.insert(parameters);

			if(count==0) {
				throw new DaoException("Record not inserted: "+count);
			}

			seedingDeadlineGuid = (String) parameters.get("seedingDeadlineGuid");
			dto.setSeedingDeadlineGuid(seedingDeadlineGuid);
			
			
		} catch (RuntimeException e) {
			handleException(e);
		}

		logger.debug(">insert " + seedingDeadlineGuid);
	}
	

	@Override
	public void update(SeedingDeadlineDto dto, String userId) 
			throws DaoException, NotFoundDaoException {
		logger.debug("<update");
		
		if(dto.isDirty()) {
			try {
	
				Map<String, Object> parameters = new HashMap<String, Object>();
				parameters.put("dto", dto);
				parameters.put("userId", userId);
				int count = this.mapper.update(parameters);
	
				if(count==0) {
					throw new DaoException("Record not updated: "+count);
				}
	
			} catch (RuntimeException e) {
				handleException(e);
			}
		} else {
			
			logger.info("Skipping update because dto is not dirty");
		}

		logger.debug(">update");
	}

	@Override
	public void delete(String seedingDeadlineGuid) throws DaoException, NotFoundDaoException {
		logger.debug("<delete");

		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("seedingDeadlineGuid", seedingDeadlineGuid);
			int count = this.mapper.delete(parameters);

			if(count==0) {
				throw new DaoException("Record not deleted: "+count);
			}
		} catch (RuntimeException e) {
			handleException(e);
		}

		logger.debug(">delete");
	}

	@Override
	public SeedingDeadlineDto selectForCommodityTypeAndYear(String commodityTypeCode, Integer cropYear) throws DaoException {

		logger.debug("<selectForCommodityTypeAndYear");

		SeedingDeadlineDto result = null;

		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("commodityTypeCode", commodityTypeCode);
			parameters.put("cropYear", cropYear);
						
			result = this.mapper.selectForCommodityTypeAndYear(parameters);
			
			if(result!=null) {
				result.resetDirty();
			}
		} catch (RuntimeException e) {
			handleException(e);
		}

		logger.debug(">selectForCommodityTypeAndYear " + result);
		return result;			
		
	}

	@Override
	public List<SeedingDeadlineDto> selectByYear(Integer cropYear) throws DaoException {

		logger.debug("<selectByYear");

		List<SeedingDeadlineDto> dtos = null;

		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("cropYear", cropYear);
						
			dtos = this.mapper.selectByYear(parameters);
			
		} catch (RuntimeException e) {
			handleException(e);
		}

		logger.debug(">selectByYear " + dtos);
		return dtos;			
		
	}
		
}
