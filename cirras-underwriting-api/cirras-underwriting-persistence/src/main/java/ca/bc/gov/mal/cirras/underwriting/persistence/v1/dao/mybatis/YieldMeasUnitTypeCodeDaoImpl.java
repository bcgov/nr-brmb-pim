package ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.mybatis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.YieldMeasUnitTypeCodeDao;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.mybatis.mapper.YieldMeasUnitTypeCodeMapper;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.YieldMeasUnitTypeCodeDto;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.mybatis.BaseDao;


@Repository
public class YieldMeasUnitTypeCodeDaoImpl extends BaseDao implements YieldMeasUnitTypeCodeDao {

	private static final long serialVersionUID = 1L;

	private static final Logger logger = LoggerFactory.getLogger(YieldMeasUnitTypeCodeDaoImpl.class);

	@Autowired
	private YieldMeasUnitTypeCodeMapper mapper;

	@Override
	public YieldMeasUnitTypeCodeDto fetch(String yieldMeasUnitTypeCode) throws DaoException {
		logger.debug("<fetch");

		YieldMeasUnitTypeCodeDto result = null;

		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("yieldMeasUnitTypeCode", yieldMeasUnitTypeCode);
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
	public List<YieldMeasUnitTypeCodeDto> fetchAll() throws DaoException {

		logger.debug("<fetchAll");
		
		List<YieldMeasUnitTypeCodeDto> dtos = null;

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
	public void insert(YieldMeasUnitTypeCodeDto dto, String userId) throws DaoException {
		logger.debug("<insert");

		try {
			Map<String, Object> parameters = new HashMap<String, Object>();

			parameters.put("dto", dto);
			parameters.put("userId", userId);
			int count = this.mapper.insert(parameters);

			if(count==0) {
				throw new DaoException("Record not inserted: "+count);
			}
			
		} catch (RuntimeException e) {
			handleException(e);
		}

		logger.debug(">insert " + dto.getYieldMeasUnitTypeCode());
	}
	

	@Override
	public void update(YieldMeasUnitTypeCodeDto dto, String userId) 
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
	public void delete(String yieldMeasUnitTypeCode) throws DaoException {
		logger.debug("<delete");

		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("yieldMeasUnitTypeCode", yieldMeasUnitTypeCode);
			this.mapper.delete(parameters);

		} catch (RuntimeException e) {
			handleException(e);
		}

		logger.debug(">delete");
	}
	
	
	@Override
	public List<YieldMeasUnitTypeCodeDto> selectByPlan(
	    	Integer insurancePlanId
	) throws DaoException {

		List<YieldMeasUnitTypeCodeDto> dtos = null;

		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("insurancePlanId", insurancePlanId);
			dtos = this.mapper.selectByPlan(parameters);
		} catch (RuntimeException e) {
			handleException(e);
		}

		logger.debug(">select " + dtos);
		return dtos;

	}

}
