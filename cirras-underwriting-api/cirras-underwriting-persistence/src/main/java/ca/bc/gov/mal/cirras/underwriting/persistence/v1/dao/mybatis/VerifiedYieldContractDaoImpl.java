package ca.bc.gov.mal.cirras.underwriting.data.repositories;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import ca.bc.gov.mal.cirras.underwriting.data.repositories.VerifiedYieldContractDao;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.mapper.VerifiedYieldContractMapper;
import ca.bc.gov.mal.cirras.underwriting.data.entities.VerifiedYieldContractDto;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.mybatis.BaseDao;


@Repository
public class VerifiedYieldContractDaoImpl extends BaseDao implements VerifiedYieldContractDao {

	private static final long serialVersionUID = 1L;

	private static final Logger logger = LoggerFactory.getLogger(VerifiedYieldContractDaoImpl.class);

	@Autowired
	private VerifiedYieldContractMapper mapper;

	@Override
	public VerifiedYieldContractDto fetch(String verifiedYieldContractGuid) throws DaoException {
		logger.debug("<fetch");

		VerifiedYieldContractDto result = null;

		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("verifiedYieldContractGuid", verifiedYieldContractGuid);
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
	public void insert(VerifiedYieldContractDto dto, String userId) throws DaoException {
		logger.debug("<insert");

		String verifiedYieldContractGuid = null;
		
		try {
			Map<String, Object> parameters = new HashMap<String, Object>();

			parameters.put("dto", dto);
			parameters.put("userId", userId);
			int count = this.mapper.insert(parameters);

			if(count==0) {
				throw new DaoException("Record not inserted: "+count);
			}

			verifiedYieldContractGuid = (String) parameters.get("verifiedYieldContractGuid");
			dto.setVerifiedYieldContractGuid(verifiedYieldContractGuid);
			
			
		} catch (RuntimeException e) {
			handleException(e);
		}

		logger.debug(">insert " + verifiedYieldContractGuid);
	}
	

	@Override
	public void update(VerifiedYieldContractDto dto, String userId) 
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
	public void delete(String verifiedYieldContractGuid) throws DaoException, NotFoundDaoException {
		logger.debug("<delete");

		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("verifiedYieldContractGuid", verifiedYieldContractGuid);
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
	public VerifiedYieldContractDto getByContractAndYear(Integer contractId, Integer cropYear) throws DaoException {

		logger.debug("<getByContractAndYear");

		VerifiedYieldContractDto result = null;

		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("contractId", contractId);
			parameters.put("cropYear", cropYear);
			result = this.mapper.getByContractAndYear(parameters);
			
			if(result!=null) {
				result.resetDirty();
			}
		} catch (RuntimeException e) {
			handleException(e);
		}

		logger.debug(">getByContractAndYear " + result);
		return result;	
	}
	
	@Override
	public VerifiedYieldContractDto getByGrowerContract(Integer growerContractYearId) throws DaoException {
		logger.debug("<getByGrowerContract");

		VerifiedYieldContractDto result = null;

		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("growerContractYearId", growerContractYearId);
			result = this.mapper.getByGrowerContract(parameters);
			
			if(result!=null) {
				result.resetDirty();
			}
		} catch (RuntimeException e) {
			handleException(e);
		}

		logger.debug(">getByGrowerContract " + result);
		return result;	
	}	
}
