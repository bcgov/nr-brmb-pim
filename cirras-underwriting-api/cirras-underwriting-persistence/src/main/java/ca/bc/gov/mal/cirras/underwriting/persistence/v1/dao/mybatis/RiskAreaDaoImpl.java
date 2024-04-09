package ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.mybatis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.RiskAreaDao;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.mybatis.mapper.RiskAreaMapper;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.RiskAreaDto;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.mybatis.BaseDao;


@Repository
public class RiskAreaDaoImpl extends BaseDao implements RiskAreaDao {

	private static final long serialVersionUID = 1L;

	private static final Logger logger = LoggerFactory.getLogger(RiskAreaDaoImpl.class);

	@Autowired
	private RiskAreaMapper mapper;

	@Override
	public RiskAreaDto fetch(Integer riskAreaId) throws DaoException {
		logger.debug("<fetch");

		RiskAreaDto result = null;

		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("riskAreaId", riskAreaId);
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
	public List<RiskAreaDto> fetchAll() throws DaoException {

		logger.debug("<fetchAll");
		
		List<RiskAreaDto> dtos = null;

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
	public void insert(RiskAreaDto dto, String userId) throws DaoException {
		logger.debug("<insert");

		try {
			Map<String, Object> parameters = new HashMap<String, Object>();

			parameters.put("dto", dto);
			parameters.put("userId", userId);
			int count = this.mapper.insert(parameters);

			if(count==0) {
				throw new DaoException("Record not inserted: "+count);
			}
			
			Integer riskAreaId = (Integer) parameters.get("riskAreaId");
			dto.setRiskAreaId(riskAreaId);
			
		} catch (RuntimeException e) {
			handleException(e);
		}

		logger.debug(">insert " + dto.getRiskAreaId());
	}
	

	@Override
	public void update(RiskAreaDto dto, String userId) 
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
	public void delete(Integer riskAreaId) throws DaoException {
		logger.debug("<delete");

		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("riskAreaId", riskAreaId);
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
	public List<RiskAreaDto> select(
	    	Integer insurancePlanId
	) throws DaoException {

		logger.debug("<select");

		List<RiskAreaDto> dtos = null;

		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("insurancePlanId", insurancePlanId);
			dtos = this.mapper.select(parameters);
		} catch (RuntimeException e) {
			handleException(e);
		}

		logger.debug(">select " + dtos);
		return dtos;

	}
	
	@Override
	public List<RiskAreaDto> selectByLegalLand(
        	Integer legalLandId
        ) throws DaoException {
		
		logger.debug("<selectByLegalLand");

		List<RiskAreaDto> dtos = null;

		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("legalLandId", legalLandId);
			dtos = this.mapper.selectByLegalLand(parameters);
		} catch (RuntimeException e) {
			handleException(e);
		}

		logger.debug(">selectByLegalLand " + dtos);
		return dtos;

	}

}
