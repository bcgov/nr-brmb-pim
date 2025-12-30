package ca.bc.gov.mal.cirras.underwriting.data.repositories;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import ca.bc.gov.mal.cirras.underwriting.data.repositories.VerifiedYieldSummaryDao;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.mapper.VerifiedYieldSummaryMapper;
import ca.bc.gov.mal.cirras.underwriting.data.entities.VerifiedYieldSummaryDto;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.mybatis.BaseDao;


@Repository
public class VerifiedYieldSummaryDaoImpl extends BaseDao implements VerifiedYieldSummaryDao {

	private static final long serialVersionUID = 1L;

	private static final Logger logger = LoggerFactory.getLogger(VerifiedYieldSummaryDaoImpl.class);

	@Autowired
	private VerifiedYieldSummaryMapper mapper;

	@Override
	public VerifiedYieldSummaryDto fetch(String verifiedYieldSummaryGuid) throws DaoException {
		logger.debug("<fetch");

		VerifiedYieldSummaryDto result = null;

		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("verifiedYieldSummaryGuid", verifiedYieldSummaryGuid);
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
	public void insert(VerifiedYieldSummaryDto dto, String userId) throws DaoException {
		logger.debug("<insert");

		String verifiedYieldSummaryGuid = null;
		
		try {
			Map<String, Object> parameters = new HashMap<String, Object>();

			parameters.put("dto", dto);
			parameters.put("userId", userId);
			int count = this.mapper.insert(parameters);

			if(count==0) {
				throw new DaoException("Record not inserted: "+count);
			}

			verifiedYieldSummaryGuid = (String) parameters.get("verifiedYieldSummaryGuid");
			dto.setVerifiedYieldSummaryGuid(verifiedYieldSummaryGuid);
			
			
		} catch (RuntimeException e) {
			handleException(e);
		}

		logger.debug(">insert " + verifiedYieldSummaryGuid);
	}
	

	@Override
	public void update(VerifiedYieldSummaryDto dto, String userId) 
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
	public void delete(String verifiedYieldSummaryGuid) throws DaoException, NotFoundDaoException {
		logger.debug("<delete");

		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("verifiedYieldSummaryGuid", verifiedYieldSummaryGuid);
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
	public void deleteForVerifiedYieldContract(String verifiedYieldContractGuid) throws DaoException, NotFoundDaoException {

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
	
	@Override
	public List<VerifiedYieldSummaryDto> selectForVerifiedYieldContract(String verifiedYieldContractGuid) throws DaoException {

		logger.debug("<selectForVerifiedYieldContract");

		List<VerifiedYieldSummaryDto> dtos = null;

		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			
			parameters.put("verifiedYieldContractGuid", verifiedYieldContractGuid);
						
			dtos = this.mapper.selectForVerifiedYieldContract(parameters);

		} catch (RuntimeException e) {
			handleException(e);
		}

		logger.debug(">selectForVerifiedYieldContract " + dtos);
		return dtos;
	}
}
