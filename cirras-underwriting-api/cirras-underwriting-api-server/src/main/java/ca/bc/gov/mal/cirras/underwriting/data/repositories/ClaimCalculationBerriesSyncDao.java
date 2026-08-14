package ca.bc.gov.mal.cirras.underwriting.data.repositories;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import ca.bc.gov.mal.cirras.underwriting.data.repositories.mapper.ClaimCalculationBerriesSyncMapper;
import ca.bc.gov.mal.cirras.underwriting.data.entities.ClaimCalculationBerriesSyncDto;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.mybatis.BaseDao;


@Repository
public class ClaimCalculationBerriesSyncDao extends BaseDao {

	private static final long serialVersionUID = 1L;

	private static final Logger logger = LoggerFactory.getLogger(ClaimCalculationBerriesSyncDao.class);

	@Autowired
	private ClaimCalculationBerriesSyncMapper mapper;

	
	public ClaimCalculationBerriesSyncDto fetch(String claimCalculationBerriesGuid) throws DaoException {
		logger.debug("<fetch");

		ClaimCalculationBerriesSyncDto result = null;

		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("claimCalculationBerriesGuid", claimCalculationBerriesGuid);
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
	
	public List<ClaimCalculationBerriesSyncDto> fetchAll() throws DaoException {

		logger.debug("<fetchAll");
		
		List<ClaimCalculationBerriesSyncDto> dtos = null;

		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			dtos = this.mapper.fetchAll(parameters);
		} catch (RuntimeException e) {
			handleException(e);
		}

		logger.debug(">fetchAll " + dtos);
		return dtos;
	}

	public List<ClaimCalculationBerriesSyncDto> selectForContractAndYear(Integer contractId, Integer cropYear) throws DaoException {

		logger.debug("<selectForContractAndYear");

		List<ClaimCalculationBerriesSyncDto> dtos = null;

		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			
			parameters.put("contractId", contractId);
			parameters.put("cropYear", cropYear);
						
			dtos = this.mapper.selectForContractAndYear(parameters);

		} catch (RuntimeException e) {
			handleException(e);
		}

		logger.debug(">selectForContractAndYear " + dtos);
		return dtos;
	}
	
	
	public void insert(ClaimCalculationBerriesSyncDto dto, String userId) throws DaoException {
		logger.debug("<insert");

		try {
			Map<String, Object> parameters = new HashMap<String, Object>();

			parameters.put("dto", dto);
			parameters.put("userId", userId);
			int count = this.mapper.insert(parameters);

			if(count==0) {
				throw new DaoException("Record not inserted: "+count);
			}
			
			String claimCalculationBerriesSyncGuid = (String) parameters.get("claimCalculationBerriesSyncGuid");
			dto.setClaimCalculationBerriesSyncGuid(claimCalculationBerriesSyncGuid);
			
		} catch (RuntimeException e) {
			handleException(e);
		}

		logger.debug(">insert " + dto.getClaimCalculationBerriesSyncGuid());
	}
	

	
	public void update(ClaimCalculationBerriesSyncDto dto, String userId) 
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

	public void delete(String claimCalculationBerriesGuid) throws DaoException {
		logger.debug("<delete");

		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("claimCalculationBerriesGuid", claimCalculationBerriesGuid);
			this.mapper.delete(parameters);

		} catch (RuntimeException e) {
			handleException(e);
		}

		logger.debug(">delete");
	}

}
