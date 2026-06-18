package ca.bc.gov.mal.cirras.underwriting.data.repositories;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import ca.bc.gov.mal.cirras.underwriting.data.repositories.mapper.YieldMeasUnitPlanXrefMapper;
import ca.bc.gov.mal.cirras.underwriting.data.entities.YieldMeasUnitPlanXrefDto;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.mybatis.BaseDao;


@Repository
public class YieldMeasUnitPlanXrefDao extends BaseDao {

	private static final long serialVersionUID = 1L;

	private static final Logger logger = LoggerFactory.getLogger(YieldMeasUnitPlanXrefDao.class);

	@Autowired
	private YieldMeasUnitPlanXrefMapper mapper;

	
	public YieldMeasUnitPlanXrefDto fetch(String yieldMeasUnitPlanXrefGuid) throws DaoException {
		logger.debug("<fetch");

		YieldMeasUnitPlanXrefDto result = null;

		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("yieldMeasUnitPlanXrefGuid", yieldMeasUnitPlanXrefGuid);
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
	
	
	public List<YieldMeasUnitPlanXrefDto> fetchAll() throws DaoException {

		logger.debug("<fetchAll");
		
		List<YieldMeasUnitPlanXrefDto> dtos = null;

		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			dtos = this.mapper.fetchAll(parameters);
		} catch (RuntimeException e) {
			handleException(e);
		}

		logger.debug(">fetchAll " + dtos);
		return dtos;
	}


	
	
	public void insert(YieldMeasUnitPlanXrefDto dto, String userId) throws DaoException {
		logger.debug("<insert");
		
		String yieldMeasUnitPlanXrefGuid = null;

		try {
			Map<String, Object> parameters = new HashMap<String, Object>();

			parameters.put("dto", dto);
			parameters.put("userId", userId);
			int count = this.mapper.insert(parameters);

			if(count==0) {
				throw new DaoException("Record not inserted: "+count);
			}
			
			yieldMeasUnitPlanXrefGuid = (String) parameters.get("yieldMeasUnitPlanXrefGuid");
			dto.setYieldMeasUnitPlanXrefGuid(yieldMeasUnitPlanXrefGuid);
			
		} catch (RuntimeException e) {
			handleException(e);
		}

		logger.debug(">insert " + dto.getYieldMeasUnitPlanXrefGuid());
	}
	

	
	public void update(YieldMeasUnitPlanXrefDto dto, String userId) 
			throws DaoException, NotFoundDaoException {
		logger.debug("<update");
		
		if(dto.isDirty()) {
			try {
	
				Map<String, Object> parameters = new HashMap<String, Object>();
				parameters.put("dto", dto);
				parameters.put("userId", userId);
				this.mapper.update(parameters);
	
			} catch (RuntimeException e) {
				handleException(e);
			}
		} else {
			
			logger.info("Skipping update because dto is not dirty");
		}

		logger.debug(">update");
	}

	
	public void delete(String yieldMeasUnitPlanXrefGuid) throws DaoException {
		logger.debug("<delete");

		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("yieldMeasUnitPlanXrefGuid", yieldMeasUnitPlanXrefGuid);
			this.mapper.delete(parameters);

		} catch (RuntimeException e) {
			handleException(e);
		}

		logger.debug(">delete");
	}

}
