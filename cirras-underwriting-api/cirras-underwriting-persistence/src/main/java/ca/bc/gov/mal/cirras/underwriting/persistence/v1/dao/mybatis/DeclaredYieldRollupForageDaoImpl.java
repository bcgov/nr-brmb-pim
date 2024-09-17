package ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.mybatis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.DeclaredYieldRollupForageDao;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.mybatis.mapper.DeclaredYieldRollupForageMapper;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.DeclaredYieldRollupForageDto;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.mybatis.BaseDao;


@Repository
public class DeclaredYieldRollupForageDaoImpl extends BaseDao implements DeclaredYieldRollupForageDao {

	private static final long serialVersionUID = 1L;

	private static final Logger logger = LoggerFactory.getLogger(DeclaredYieldRollupForageDaoImpl.class);

	@Autowired
	private DeclaredYieldRollupForageMapper mapper;

	@Override
	public DeclaredYieldRollupForageDto fetch(String declaredYieldRollupForageGuid) throws DaoException {
		logger.debug("<fetch");

		DeclaredYieldRollupForageDto result = null;

		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("declaredYieldRollupForageGuid", declaredYieldRollupForageGuid);
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
	public void insert(DeclaredYieldRollupForageDto dto, String userId) throws DaoException {
		logger.debug("<insert");

		String declaredYieldRollupForageGuid = null;
		
		try {
			Map<String, Object> parameters = new HashMap<String, Object>();

			parameters.put("dto", dto);
			parameters.put("userId", userId);
			int count = this.mapper.insert(parameters);

			if(count==0) {
				throw new DaoException("Record not inserted: "+count);
			}

			declaredYieldRollupForageGuid = (String) parameters.get("declaredYieldRollupForageGuid");
			dto.setDeclaredYieldRollupForageGuid(declaredYieldRollupForageGuid);
			
			
		} catch (RuntimeException e) {
			handleException(e);
		}

		logger.debug(">insert " + declaredYieldRollupForageGuid);
	}
	

	@Override
	public void update(DeclaredYieldRollupForageDto dto, String userId) 
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
	public void delete(String declaredYieldRollupForageGuid) throws DaoException, NotFoundDaoException {
		logger.debug("<delete");

		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("declaredYieldRollupForageGuid", declaredYieldRollupForageGuid);
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
	public List<DeclaredYieldRollupForageDto> selectForDeclaredYieldContract(String declaredYieldContractGuid) throws DaoException {

		logger.debug("<selectForDeclaredYieldContract");

		List<DeclaredYieldRollupForageDto> dtos = null;

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
	public  List<DeclaredYieldRollupForageDto> selectToRecalculate(
	    		Integer cropCommodityId,
	    		String enteredYieldMeasUnitTypeCode,
	    		Integer effectiveCropYear,
	    		Integer expiryCropYear
				) throws DaoException {

		logger.debug("<selectToRecalculate");

		List<DeclaredYieldRollupForageDto> dtos = null;

		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			
			parameters.put("cropCommodityId", cropCommodityId);
			parameters.put("enteredYieldMeasUnitTypeCode", enteredYieldMeasUnitTypeCode);
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
