package ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.mybatis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.YieldMeasUnitPlanXrefDao;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.mybatis.mapper.YieldMeasUnitPlanXrefMapper;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.YieldMeasUnitPlanXrefDto;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.mybatis.BaseDao;


@Repository
public class YieldMeasUnitPlanXrefDaoImpl extends BaseDao implements YieldMeasUnitPlanXrefDao {

	private static final long serialVersionUID = 1L;

	private static final Logger logger = LoggerFactory.getLogger(YieldMeasUnitPlanXrefDaoImpl.class);

	@Autowired
	private YieldMeasUnitPlanXrefMapper mapper;

	@Override
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
	
	@Override
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


	
	@Override
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
	

	@Override
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

	@Override
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
