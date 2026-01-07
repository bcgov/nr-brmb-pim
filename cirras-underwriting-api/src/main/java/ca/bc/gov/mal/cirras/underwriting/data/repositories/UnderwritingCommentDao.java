package ca.bc.gov.mal.cirras.underwriting.data.repositories;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import ca.bc.gov.mal.cirras.underwriting.data.repositories.mapper.UnderwritingCommentMapper;
import ca.bc.gov.mal.cirras.underwriting.data.entities.UnderwritingCommentDto;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.mybatis.BaseDao;


@Repository
public class UnderwritingCommentDao extends BaseDao {

	private static final long serialVersionUID = 1L;

	private static final Logger logger = LoggerFactory.getLogger(UnderwritingCommentDao.class);

	@Autowired
	private UnderwritingCommentMapper mapper;

	
	public UnderwritingCommentDto fetch(String underwritingCommentGuid) throws DaoException {
		logger.debug("<fetch");

		UnderwritingCommentDto result = null;

		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("underwritingCommentGuid", underwritingCommentGuid);
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

	
	
	public void insert(UnderwritingCommentDto dto, String userId) throws DaoException {
		logger.debug("<insert");

		String underwritingCommentGuid = null;
		
		try {
			Map<String, Object> parameters = new HashMap<String, Object>();

			parameters.put("dto", dto);
			parameters.put("userId", userId);
			int count = this.mapper.insert(parameters);

			if(count==0) {
				throw new DaoException("Record not inserted: "+count);
			}

			underwritingCommentGuid = (String) parameters.get("underwritingCommentGuid");
			dto.setUnderwritingCommentGuid(underwritingCommentGuid);
			
			
		} catch (RuntimeException e) {
			handleException(e);
		}

		logger.debug(">insert " + underwritingCommentGuid);
	}
	

	
	public void update(UnderwritingCommentDto dto, String userId) 
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

	
	public void delete(String underwritingCommentGuid) throws DaoException, NotFoundDaoException {
		logger.debug("<delete");

		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("underwritingCommentGuid", underwritingCommentGuid);
			int count = this.mapper.delete(parameters);

			if(count==0) {
				throw new DaoException("Record not deleted: "+count);
			}
		} catch (RuntimeException e) {
			handleException(e);
		}

		logger.debug(">delete");
	}

	
	public void deleteForAnnualField(Integer annualFieldDetailId) throws DaoException, NotFoundDaoException {
		logger.debug("<deleteForAnnualField");

		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("annualFieldDetailId", annualFieldDetailId);
			this.mapper.deleteForAnnualField(parameters);

		} catch (RuntimeException e) {
			handleException(e);
		}

		logger.debug(">deleteForAnnualField");
	}
	
	
	public void deleteForDeclaredYieldContractGuid(String declaredYieldContractGuid) throws DaoException, NotFoundDaoException {
		logger.debug("<deleteForDeclaredYieldContractGuid");

		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("declaredYieldContractGuid", declaredYieldContractGuid);
			this.mapper.deleteForDeclaredYieldContractGuid(parameters);

		} catch (RuntimeException e) {
			handleException(e);
		}

		logger.debug(">deleteForDeclaredYieldContractGuid");
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
	
	
	public void deleteForVerifiedYieldSummaryGuid(String verifiedYieldSummaryGuid)
			throws DaoException, NotFoundDaoException {
		logger.debug("<deleteForVerifiedYieldSummaryGuid");

		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("verifiedYieldSummaryGuid", verifiedYieldSummaryGuid);
			this.mapper.deleteForVerifiedYieldSummaryGuid(parameters);

		} catch (RuntimeException e) {
			handleException(e);
		}

		logger.debug(">deleteForVerifiedYieldSummaryGuid");
	}


	
	public void deleteForVerifiedYieldContract(String verifiedYieldContractGuid)
			throws DaoException, NotFoundDaoException {
		logger.debug("<deleteForVerifiedYieldContract");

		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("verifiedYieldContractGuid", verifiedYieldContractGuid);
			this.mapper.deleteForVerifiedYieldContract(parameters);

		} catch (RuntimeException e) {
			handleException(e);
		}

		logger.debug(">deleteForVerifiedYieldContract");
	}	
	
	
	public List<UnderwritingCommentDto> select(Integer annualFieldDetailId) throws DaoException {
		List<UnderwritingCommentDto> dtos = null;

		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			
			parameters.put("annualFieldDetailId", annualFieldDetailId);
						
			dtos = this.mapper.select(parameters);

		} catch (RuntimeException e) {
			handleException(e);
		}

		logger.debug(">select " + dtos);
		return dtos;
	}
		
	
	public List<UnderwritingCommentDto> selectForDopContract(String declaredYieldContractGuid) throws DaoException {
		List<UnderwritingCommentDto> dtos = null;

		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			
			parameters.put("declaredYieldContractGuid", declaredYieldContractGuid);
						
			dtos = this.mapper.selectForDopContract(parameters);

		} catch (RuntimeException e) {
			handleException(e);
		}

		logger.debug(">selectForDopContract " + dtos);
		return dtos;
	}

	
	public List<UnderwritingCommentDto> selectForField(Integer fieldId) throws DaoException {
		List<UnderwritingCommentDto> dtos = null;

		logger.debug("<selectForField");

		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			
			parameters.put("fieldId", fieldId);
						
			dtos = this.mapper.selectForField(parameters);

		} catch (RuntimeException e) {
			handleException(e);
		}

		logger.debug(">selectForField " + dtos);
		return dtos;
	}


	
	public List<UnderwritingCommentDto> selectForVerifiedYieldSummary(String verifiedYieldSummaryGuid)
			throws DaoException {
		List<UnderwritingCommentDto> dtos = null;

		logger.debug("<selectForVerifiedYieldSummary");

		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			
			parameters.put("verifiedYieldSummaryGuid", verifiedYieldSummaryGuid);
						
			dtos = this.mapper.selectForVerifiedYieldSummary(parameters);

		} catch (RuntimeException e) {
			handleException(e);
		}

		logger.debug(">selectForVerifiedYieldSummary " + dtos);
		return dtos;
	}


}
