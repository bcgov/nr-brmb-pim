package ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.mybatis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.DeclaredYieldFieldRollupDao;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.mybatis.mapper.DeclaredYieldFieldRollupMapper;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.DeclaredYieldFieldRollupDto;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.mybatis.BaseDao;


@Repository
public class DeclaredYieldFieldRollupDaoImpl extends BaseDao implements DeclaredYieldFieldRollupDao {

	private static final long serialVersionUID = 1L;

	private static final Logger logger = LoggerFactory.getLogger(DeclaredYieldFieldRollupDaoImpl.class);

	@Autowired
	private DeclaredYieldFieldRollupMapper mapper;

	@Override
	public DeclaredYieldFieldRollupDto fetch(String declaredYieldFieldRollupGuid) throws DaoException {
		logger.debug("<fetch");

		DeclaredYieldFieldRollupDto result = null;

		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("declaredYieldFieldRollupGuid", declaredYieldFieldRollupGuid);
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
	public void insert(DeclaredYieldFieldRollupDto dto, String userId) throws DaoException {
		logger.debug("<insert");

		String declaredYieldFieldRollupGuid = null;
		
		try {
			Map<String, Object> parameters = new HashMap<String, Object>();

			parameters.put("dto", dto);
			parameters.put("userId", userId);
			int count = this.mapper.insert(parameters);

			if(count==0) {
				throw new DaoException("Record not inserted: "+count);
			}

			declaredYieldFieldRollupGuid = (String) parameters.get("declaredYieldFieldRollupGuid");
			dto.setDeclaredYieldFieldRollupGuid(declaredYieldFieldRollupGuid);
			
			
		} catch (RuntimeException e) {
			handleException(e);
		}

		logger.debug(">insert " + declaredYieldFieldRollupGuid);
	}
	

	@Override
	public void update(DeclaredYieldFieldRollupDto dto, String userId) 
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
	public void delete(String declaredYieldFieldRollupGuid) throws DaoException, NotFoundDaoException {
		logger.debug("<delete");

		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("declaredYieldFieldRollupGuid", declaredYieldFieldRollupGuid);
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
	public void deleteForDeclaredYieldContract(String declaredYieldContractGuid) throws DaoException, NotFoundDaoException {

		logger.debug("<deleteForDeclaredYieldContract");

		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("declaredYieldContractGuid", declaredYieldContractGuid);
			this.mapper.deleteForDeclaredYieldContract(parameters);

		} catch (RuntimeException e) {
			handleException(e);
		}

		logger.debug(">deleteForDeclaredYieldContract");
		
	}	
	
	@Override
	public List<DeclaredYieldFieldRollupDto> selectForDeclaredYieldContract(String declaredYieldContractGuid) throws DaoException {

		logger.debug("<selectForDeclaredYieldContract");

		List<DeclaredYieldFieldRollupDto> dtos = null;

		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			
			parameters.put("declaredYieldContractGuid", declaredYieldContractGuid);
						
			dtos = this.mapper.selectForDeclaredYieldContract(parameters);

		} catch (RuntimeException e) {
			handleException(e);
		}

		logger.debug(">selectForDeclaredYieldContract " + dtos);
		return dtos;
	}
		
	@Override
	public  List<DeclaredYieldFieldRollupDto> selectToRecalculate(
	    		Integer cropCommodityId,
	    		Integer effectiveCropYear,
	    		Integer expiryCropYear
				) throws DaoException {

		logger.debug("<selectToRecalculate");

		List<DeclaredYieldFieldRollupDto> dtos = null;

		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			
			parameters.put("cropCommodityId", cropCommodityId);
			parameters.put("effectiveCropYear", effectiveCropYear);
			parameters.put("expiryCropYear", expiryCropYear);
						
			dtos = this.mapper.selectToRecalculate(parameters);

		} catch (RuntimeException e) {
			handleException(e);
		}

		logger.debug(">selectToRecalculate " + dtos);
		return dtos;
	}
}
