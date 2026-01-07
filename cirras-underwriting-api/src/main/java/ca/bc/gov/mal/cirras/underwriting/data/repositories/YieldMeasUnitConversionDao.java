package ca.bc.gov.mal.cirras.underwriting.data.repositories;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import ca.bc.gov.mal.cirras.underwriting.data.repositories.mapper.YieldMeasUnitConversionMapper;
import ca.bc.gov.mal.cirras.underwriting.data.entities.YieldMeasUnitConversionDto;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.mybatis.BaseDao;


@Repository
public class YieldMeasUnitConversionDao extends BaseDao {

	private static final long serialVersionUID = 1L;

	private static final Logger logger = LoggerFactory.getLogger(YieldMeasUnitConversionDao.class);

	@Autowired
	private YieldMeasUnitConversionMapper mapper;

	
	public YieldMeasUnitConversionDto fetch(String yieldMeasUnitConversionGuid) throws DaoException {
		logger.debug("<fetch");

		YieldMeasUnitConversionDto result = null;

		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("yieldMeasUnitConversionGuid", yieldMeasUnitConversionGuid);
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
	
	
	public List<YieldMeasUnitConversionDto> fetchAll() throws DaoException {

		logger.debug("<fetchAll");
		
		List<YieldMeasUnitConversionDto> dtos = null;

		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			dtos = this.mapper.fetchAll(parameters);
		} catch (RuntimeException e) {
			handleException(e);
		}

		logger.debug(">fetchAll " + dtos);
		return dtos;
	}


	
	
	public void insert(YieldMeasUnitConversionDto dto, String userId) throws DaoException {
		logger.debug("<insert");

		String yieldMeasUnitConversionGuid = null;
		
		try {
			Map<String, Object> parameters = new HashMap<String, Object>();

			parameters.put("dto", dto);
			parameters.put("userId", userId);
			int count = this.mapper.insert(parameters);

			if(count==0) {
				throw new DaoException("Record not inserted: "+count);
			}

			yieldMeasUnitConversionGuid = (String) parameters.get("yieldMeasUnitConversionGuid");
			dto.setYieldMeasUnitConversionGuid(yieldMeasUnitConversionGuid);
			
			
		} catch (RuntimeException e) {
			handleException(e);
		}

		logger.debug(">insert " + yieldMeasUnitConversionGuid);
	}
	

	
	public void update(YieldMeasUnitConversionDto dto, String userId) throws DaoException, NotFoundDaoException {
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

	
	public void delete(String yieldMeasUnitConversionGuid) throws DaoException, NotFoundDaoException {
		logger.debug("<delete");

		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("yieldMeasUnitConversionGuid", yieldMeasUnitConversionGuid);
			int count = this.mapper.delete(parameters);

			if(count==0) {
				throw new DaoException("Record not deleted: "+count);
			}
			
		} catch (RuntimeException e) {
			handleException(e);
		}

		logger.debug(">delete");
	}
	
	
	public List<YieldMeasUnitConversionDto> selectByYearAndPlan(Integer cropYear, Integer insurancePlanId) throws DaoException {

		logger.debug("<selectByYearAndPlan");

		List<YieldMeasUnitConversionDto> dtos = null;

		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("cropYear", cropYear);
			parameters.put("insurancePlanId", insurancePlanId);
			dtos = this.mapper.selectByYearAndPlan(parameters);
		} catch (RuntimeException e) {
			handleException(e);
		}

		logger.debug(">selectByYearAndPlan " + dtos);
		return dtos;

	}

	
	public List<YieldMeasUnitConversionDto> selectLatestVersionByPlan(
			Integer insurancePlanId, 
			String srcYieldMeasUnitTypeCode, 
			String targetYieldMeasUnitTypeCode
			) throws DaoException {

		logger.debug("<selectLatestVersionByPlan");

		List<YieldMeasUnitConversionDto> dtos = null;

		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("insurancePlanId", insurancePlanId);
			parameters.put("srcYieldMeasUnitTypeCode", srcYieldMeasUnitTypeCode);
			parameters.put("targetYieldMeasUnitTypeCode", targetYieldMeasUnitTypeCode);
			dtos = this.mapper.selectLatestVersionByPlan(parameters);
		} catch (RuntimeException e) {
			handleException(e);
		}

		logger.debug(">selectLatestVersionByPlan " + dtos);
		return dtos;

	}
	
}
