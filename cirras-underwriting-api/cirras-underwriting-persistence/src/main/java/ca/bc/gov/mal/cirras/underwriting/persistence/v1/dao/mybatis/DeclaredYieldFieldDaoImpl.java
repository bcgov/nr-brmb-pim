package ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.mybatis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.DeclaredYieldFieldDao;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.mybatis.mapper.DeclaredYieldFieldMapper;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.DeclaredYieldFieldDto;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.mybatis.BaseDao;


@Repository
public class DeclaredYieldFieldDaoImpl extends BaseDao implements DeclaredYieldFieldDao {

	private static final long serialVersionUID = 1L;

	private static final Logger logger = LoggerFactory.getLogger(DeclaredYieldFieldDaoImpl.class);

	@Autowired
	private DeclaredYieldFieldMapper mapper;

	@Override
	public DeclaredYieldFieldDto fetch(String declaredYieldFieldGuid) throws DaoException {
		logger.debug("<fetch");

		DeclaredYieldFieldDto result = null;

		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("declaredYieldFieldGuid", declaredYieldFieldGuid);
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
	public void insert(DeclaredYieldFieldDto dto, String userId) throws DaoException {
		logger.debug("<insert");

		String declaredYieldFieldGuid = null;
		
		try {
			Map<String, Object> parameters = new HashMap<String, Object>();

			parameters.put("dto", dto);
			parameters.put("userId", userId);
			int count = this.mapper.insert(parameters);

			if(count==0) {
				throw new DaoException("Record not inserted: "+count);
			}

			declaredYieldFieldGuid = (String) parameters.get("declaredYieldFieldGuid");
			dto.setDeclaredYieldFieldGuid(declaredYieldFieldGuid);
			
			
		} catch (RuntimeException e) {
			handleException(e);
		}

		logger.debug(">insert " + declaredYieldFieldGuid);
	}
	

	@Override
	public void update(DeclaredYieldFieldDto dto, String userId) 
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
	public void delete(String declaredYieldFieldGuid) throws DaoException, NotFoundDaoException {
		logger.debug("<delete");

		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("declaredYieldFieldGuid", declaredYieldFieldGuid);
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
	public DeclaredYieldFieldDto getByInventoryField(String inventoryFieldGuid) throws DaoException {
		logger.debug("<getByInventoryField");

		DeclaredYieldFieldDto result = null;

		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("inventoryFieldGuid", inventoryFieldGuid);
			result = this.mapper.getByInventoryField(parameters);
			
			if(result!=null) {
				result.resetDirty();
			}
		} catch (RuntimeException e) {
			handleException(e);
		}

		logger.debug(">getByInventoryField " + result);
		return result;	
	}


	@Override
	public int getTotalDopRecordsWithYield(Integer fieldId, Integer cropYear, Integer insurancePlanId)
			throws DaoException, NotFoundDaoException {
		logger.debug("<getTotalDopRecordsWithYield");

		int result = 0;

		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("fieldId", fieldId);
			parameters.put("cropYear", cropYear);
			parameters.put("insurancePlanId", insurancePlanId);

			result = this.mapper.getTotalDopRecordsWithYield(parameters);

		} catch (RuntimeException e) {
			handleException(e);
		}

		logger.debug(">getTotalDopRecordsWithYield: " + result);
		return result;	
	}
	
	@Override
	public  List<DeclaredYieldFieldDto> selectToRecalculate(
	    		Integer cropCommodityId,
	    		String enteredYieldMeasUnitTypeCode,
	    		Integer effectiveCropYear,
	    		Integer expiryCropYear
				) throws DaoException {

		logger.debug("<selectToRecalculate");

		List<DeclaredYieldFieldDto> dtos = null;

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