package ca.bc.gov.mal.cirras.underwriting.data.repositories;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import ca.bc.gov.mal.cirras.underwriting.data.repositories.mapper.GrowerContractYearMapper;
import ca.bc.gov.mal.cirras.underwriting.data.entities.GrowerContractYearDto;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.mybatis.BaseDao;


@Repository
public class GrowerContractYearDao extends BaseDao {

	private static final long serialVersionUID = 1L;

	private static final Logger logger = LoggerFactory.getLogger(GrowerContractYearDao.class);

	@Autowired
	private GrowerContractYearMapper mapper;

	
	public GrowerContractYearDto fetch(Integer growerContractYearId) throws DaoException {
		logger.debug("<fetch");

		GrowerContractYearDto result = null;

		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("growerContractYearId", growerContractYearId);
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
	
	
	public List<GrowerContractYearDto> fetchAll() throws DaoException {

		logger.debug("<fetchAll");
		
		List<GrowerContractYearDto> dtos = null;

		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			dtos = this.mapper.fetchAll(parameters);
		} catch (RuntimeException e) {
			handleException(e);
		}

		logger.debug(">fetchAll " + dtos);
		return dtos;
	}

	
	public GrowerContractYearDto selectInventoryContractForGcy(Integer growerContractYearId) throws DaoException {
		logger.debug("<selectInventoryContractForGcy");

		GrowerContractYearDto result = null;

		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("growerContractYearId", growerContractYearId);
			result = this.mapper.selectInventoryContractForGcy(parameters);
			
			if(result!=null) {
				result.resetDirty();
			}
		} catch (RuntimeException e) {
			handleException(e);
		}

		logger.debug(">selectInventoryContractForGcy " + result);
		return result;	
	}

	
	public GrowerContractYearDto selectLastYear(Integer contractId, Integer currCropYear) throws DaoException {
		logger.debug("<selectLastYear");

		GrowerContractYearDto result = null;

		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("contractId", contractId);
			parameters.put("currCropYear", currCropYear);
			result = this.mapper.selectLastYear(parameters);
			
			if(result!=null) {
				result.resetDirty();
			}
		} catch (RuntimeException e) {
			handleException(e);
		}

		logger.debug(">selectLastYear " + result);
		return result;	
	}
	
	
	public void insert(GrowerContractYearDto dto, String userId) throws DaoException {
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

		logger.debug(">insert " + dto.getGrowerContractYearId());
	}
	

	
	public void update(GrowerContractYearDto dto, String userId) 
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

	
	public void delete(Integer growerContractYearId) throws DaoException {
		logger.debug("<delete");

		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("growerContractYearId", growerContractYearId);
			this.mapper.delete(parameters);

		} catch (RuntimeException e) {
			handleException(e);
		}

		logger.debug(">delete");
	}

}
