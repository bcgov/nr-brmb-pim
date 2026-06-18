package ca.bc.gov.mal.cirras.underwriting.data.repositories;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import ca.bc.gov.mal.cirras.underwriting.data.repositories.mapper.AnnualFieldDetailMapper;
import ca.bc.gov.mal.cirras.underwriting.data.entities.AnnualFieldDetailDto;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.mybatis.BaseDao;


@Repository
public class AnnualFieldDetailDao extends BaseDao {

	private static final long serialVersionUID = 1L;

	private static final Logger logger = LoggerFactory.getLogger(AnnualFieldDetailDao.class);

	@Autowired
	private AnnualFieldDetailMapper mapper;

	public AnnualFieldDetailDto fetch(Integer annualFieldDetailId) throws DaoException {
		logger.debug("<fetch");

		AnnualFieldDetailDto result = null;

		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("annualFieldDetailId", annualFieldDetailId);
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
	

	public List<AnnualFieldDetailDto> fetchAll() throws DaoException {

		logger.debug("<fetchAll");
		
		List<AnnualFieldDetailDto> dtos = null;

		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			dtos = this.mapper.fetchAll(parameters);
		} catch (RuntimeException e) {
			handleException(e);
		}

		logger.debug(">fetchAll " + dtos);
		return dtos;
	}
	

	public AnnualFieldDetailDto getByFieldAndCropYear(Integer fieldId, Integer cropYear) throws DaoException {

		AnnualFieldDetailDto result = null;

		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("fieldId", fieldId);
			parameters.put("cropYear", cropYear);
			result = this.mapper.getByFieldAndCropYear(parameters);
			
			if(result!=null) {
				result.resetDirty();
			}
		} catch (RuntimeException e) {
			handleException(e);
		}

		logger.debug(">getByFieldAndCropYear " + result);
		return result;
	}
	

	public int getTotalForLegalLandField(Integer legalLandId, Integer fieldId)
			throws DaoException, NotFoundDaoException {
		logger.debug("<getTotalForLegalLand");

		int result = 0;

		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("legalLandId", legalLandId);
			parameters.put("fieldId", fieldId);

			result = this.mapper.getTotalForLegalLandField(parameters);

		} catch (RuntimeException e) {
			handleException(e);
		}

		logger.debug(">getTotalForLegalLand: " + result);
		return result;	
	}	
	

	public void insert(AnnualFieldDetailDto dto, String userId) throws DaoException {
		logger.debug("<insert");

		try {
			Map<String, Object> parameters = new HashMap<String, Object>();

			parameters.put("dto", dto);
			parameters.put("userId", userId);
			int count = this.mapper.insert(parameters);

			if(count==0) {
				throw new DaoException("Record not inserted: "+count);
			}
			
			Integer annualFieldDetailId = (Integer) parameters.get("annualFieldDetailId");
			dto.setAnnualFieldDetailId(annualFieldDetailId);

			
		} catch (RuntimeException e) {
			handleException(e);
		}

		logger.debug(">insert " + dto.getAnnualFieldDetailId());
	}	
	

	public void insertDataSync(AnnualFieldDetailDto dto, String userId) throws DaoException {
		logger.debug("<insertDataSync");

		try {
			Map<String, Object> parameters = new HashMap<String, Object>();

			parameters.put("dto", dto);
			parameters.put("userId", userId);
			int count = this.mapper.insertDataSync(parameters);

			if(count==0) {
				throw new DaoException("Record not inserted: "+count);
			}
			
		} catch (RuntimeException e) {
			handleException(e);
		}

		logger.debug(">insertDataSync " + dto.getAnnualFieldDetailId());
	}	


	public void update(AnnualFieldDetailDto dto, String userId) 
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


	public void delete(Integer annualFieldDetailId) throws DaoException {
		logger.debug("<delete");

		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("annualFieldDetailId", annualFieldDetailId);
			this.mapper.delete(parameters);

		} catch (RuntimeException e) {
			handleException(e);
		}

		logger.debug(">delete");
	}
	

	public void deleteForField(Integer fieldId) throws DaoException, NotFoundDaoException {
		logger.debug("<deleteForField");

		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("fieldId", fieldId);
			this.mapper.deleteForField(parameters);

		} catch (RuntimeException e) {
			handleException(e);
		}

		logger.debug(">deleteForField");
	}
	

	public AnnualFieldDetailDto getPreviousSubsequentRecords(
    		Integer fieldId, 
    		Integer cropYear
    		) throws DaoException {
		
		logger.debug("<getPreviousSubsequentRecords");

		//Returns crop year and legal land id for previous and subsequent years with and without a contract
		
		AnnualFieldDetailDto result = null;

		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("fieldId", fieldId);
			parameters.put("cropYear", cropYear);
			result = this.mapper.getPreviousSubsequentRecords(parameters);
			
			if(result!=null) {
				result.resetDirty();
			}
		} catch (RuntimeException e) {
			handleException(e);
		}

		logger.debug(">getPreviousSubsequentRecords " + result);
		return result;		
	}
}
