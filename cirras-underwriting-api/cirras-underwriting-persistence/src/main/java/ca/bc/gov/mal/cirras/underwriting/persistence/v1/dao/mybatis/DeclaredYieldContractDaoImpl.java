package ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.mybatis;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.DeclaredYieldContractDao;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.mybatis.mapper.DeclaredYieldContractMapper;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.DeclaredYieldContractDto;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.mybatis.BaseDao;


@Repository
public class DeclaredYieldContractDaoImpl extends BaseDao implements DeclaredYieldContractDao {

	private static final long serialVersionUID = 1L;

	private static final Logger logger = LoggerFactory.getLogger(DeclaredYieldContractDaoImpl.class);

	@Autowired
	private DeclaredYieldContractMapper mapper;

	@Override
	public DeclaredYieldContractDto fetch(String declaredYieldContractGuid) throws DaoException {
		logger.debug("<fetch");

		DeclaredYieldContractDto result = null;

		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("declaredYieldContractGuid", declaredYieldContractGuid);
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
	public void insert(DeclaredYieldContractDto dto, String userId) throws DaoException {
		logger.debug("<insert");

		String declaredYieldContractGuid = null;
		
		try {
			Map<String, Object> parameters = new HashMap<String, Object>();

			parameters.put("dto", dto);
			parameters.put("userId", userId);
			int count = this.mapper.insert(parameters);

			if(count==0) {
				throw new DaoException("Record not inserted: "+count);
			}

			declaredYieldContractGuid = (String) parameters.get("declaredYieldContractGuid");
			dto.setDeclaredYieldContractGuid(declaredYieldContractGuid);
			
			
		} catch (RuntimeException e) {
			handleException(e);
		}

		logger.debug(">insert " + declaredYieldContractGuid);
	}
	

	@Override
	public void update(DeclaredYieldContractDto dto, String userId) 
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
	public void delete(String declaredYieldContractGuid) throws DaoException, NotFoundDaoException {
		logger.debug("<delete");

		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("declaredYieldContractGuid", declaredYieldContractGuid);
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
	public DeclaredYieldContractDto getByContractAndYear(Integer contractId, Integer cropYear) throws DaoException {

		logger.debug("<getByContractAndYear");

		DeclaredYieldContractDto result = null;

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
	public DeclaredYieldContractDto getByGrowerContract(Integer growerContractYearId) throws DaoException {
		logger.debug("<getByGrowerContract");

		DeclaredYieldContractDto result = null;

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
