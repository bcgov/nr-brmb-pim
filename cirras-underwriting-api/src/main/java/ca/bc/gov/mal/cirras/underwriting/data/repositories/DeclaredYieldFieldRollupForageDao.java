package ca.bc.gov.mal.cirras.underwriting.data.repositories;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import ca.bc.gov.mal.cirras.underwriting.data.repositories.mapper.DeclaredYieldFieldRollupForageMapper;
import ca.bc.gov.mal.cirras.underwriting.data.entities.DeclaredYieldFieldRollupForageDto;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.mybatis.BaseDao;


@Repository
public class DeclaredYieldFieldRollupForageDao extends BaseDao {

	private static final long serialVersionUID = 1L;

	private static final Logger logger = LoggerFactory.getLogger(DeclaredYieldFieldRollupForageDao.class);

	@Autowired
	private DeclaredYieldFieldRollupForageMapper mapper;

	
	public DeclaredYieldFieldRollupForageDto fetch(String declaredYieldFieldRollupForageGuid) throws DaoException {
		logger.debug("<fetch");

		DeclaredYieldFieldRollupForageDto result = null;

		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("declaredYieldFieldRollupForageGuid", declaredYieldFieldRollupForageGuid);
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

	
	
	public void insert(DeclaredYieldFieldRollupForageDto dto, String userId) throws DaoException {
		logger.debug("<insert");

		String declaredYieldFieldRollupForageGuid = null;
		
		try {
			Map<String, Object> parameters = new HashMap<String, Object>();

			parameters.put("dto", dto);
			parameters.put("userId", userId);
			int count = this.mapper.insert(parameters);

			if(count==0) {
				throw new DaoException("Record not inserted: "+count);
			}

			declaredYieldFieldRollupForageGuid = (String) parameters.get("declaredYieldFieldRollupForageGuid");
			dto.setDeclaredYieldFieldRollupForageGuid(declaredYieldFieldRollupForageGuid);
			
			
		} catch (RuntimeException e) {
			handleException(e);
		}

		logger.debug(">insert " + declaredYieldFieldRollupForageGuid);
	}
	

	
	public void update(DeclaredYieldFieldRollupForageDto dto, String userId) 
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

	
	public void delete(String declaredYieldFieldRollupForageGuid) throws DaoException, NotFoundDaoException {
		logger.debug("<delete");

		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("declaredYieldFieldRollupForageGuid", declaredYieldFieldRollupForageGuid);
			int count = this.mapper.delete(parameters);

			if(count==0) {
				throw new DaoException("Record not deleted: "+count);
			}
		} catch (RuntimeException e) {
			handleException(e);
		}

		logger.debug(">delete");
	}
	

	
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
	
	
	public List<DeclaredYieldFieldRollupForageDto> selectForDeclaredYieldContract(String declaredYieldContractGuid) throws DaoException {

		logger.debug("<selectForDeclaredYieldContract");

		List<DeclaredYieldFieldRollupForageDto> dtos = null;

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
	
	
	public  List<DeclaredYieldFieldRollupForageDto> selectToRecalculate(
	    		Integer cropCommodityId,
	    		String enteredYieldMeasUnitTypeCode,
	    		Integer effectiveCropYear,
	    		Integer expiryCropYear
				) throws DaoException {

		logger.debug("<selectToRecalculate");

		List<DeclaredYieldFieldRollupForageDto> dtos = null;

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
