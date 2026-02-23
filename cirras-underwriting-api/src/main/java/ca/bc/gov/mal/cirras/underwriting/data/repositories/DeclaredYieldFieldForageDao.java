package ca.bc.gov.mal.cirras.underwriting.data.repositories;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import ca.bc.gov.mal.cirras.underwriting.data.repositories.mapper.DeclaredYieldFieldForageMapper;
import ca.bc.gov.mal.cirras.underwriting.data.entities.DeclaredYieldFieldForageDto;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.mybatis.BaseDao;


@Repository
public class DeclaredYieldFieldForageDao extends BaseDao {

	private static final long serialVersionUID = 1L;

	private static final Logger logger = LoggerFactory.getLogger(DeclaredYieldFieldForageDao.class);

	@Autowired
	private DeclaredYieldFieldForageMapper mapper;

	
	public DeclaredYieldFieldForageDto fetch(String declaredYieldFieldForageGuid) throws DaoException {
		logger.debug("<fetch");

		DeclaredYieldFieldForageDto result = null;

		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("declaredYieldFieldForageGuid", declaredYieldFieldForageGuid);
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

	
	
	public void insert(DeclaredYieldFieldForageDto dto, String userId) throws DaoException {
		logger.debug("<insert");

		String declaredYieldFieldForageGuid = null;
		
		try {
			Map<String, Object> parameters = new HashMap<String, Object>();

			parameters.put("dto", dto);
			parameters.put("userId", userId);
			int count = this.mapper.insert(parameters);

			if(count==0) {
				throw new DaoException("Record not inserted: "+count);
			}

			declaredYieldFieldForageGuid = (String) parameters.get("declaredYieldFieldForageGuid");
			dto.setDeclaredYieldFieldForageGuid(declaredYieldFieldForageGuid);
			
			
		} catch (RuntimeException e) {
			handleException(e);
		}

		logger.debug(">insert " + declaredYieldFieldForageGuid);
	}
	

	
	public void update(DeclaredYieldFieldForageDto dto, String userId) 
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

	
	public void delete(String declaredYieldFieldForageGuid) throws DaoException, NotFoundDaoException {
		logger.debug("<delete");

		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("declaredYieldFieldForageGuid", declaredYieldFieldForageGuid);
			int count = this.mapper.delete(parameters);

			if(count==0) {
				throw new DaoException("Record not deleted: "+count);
			}
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
	
	
	public void deleteForFieldAndYear(Integer fieldId, Integer cropYear) throws DaoException, NotFoundDaoException {
		logger.debug("<deleteForFieldAndYear");

		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("fieldId", fieldId);
			parameters.put("cropYear", cropYear);
			this.mapper.deleteForFieldAndYear(parameters);

		} catch (RuntimeException e) {
			handleException(e);
		}

		logger.debug(">deleteForFieldAndYear");
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
	
	
	public List<DeclaredYieldFieldForageDto> getByInventoryField(String inventoryFieldGuid) throws DaoException {

		logger.debug("<getByInventoryField");

		List<DeclaredYieldFieldForageDto> dtos = null;

		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("inventoryFieldGuid", inventoryFieldGuid);
						
			dtos = this.mapper.getByInventoryField(parameters);

		} catch (RuntimeException e) {
			handleException(e);
		}

		logger.debug(">getByInventoryField " + dtos);
		return dtos;
	}

	
	
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
	
	
	
	public  List<DeclaredYieldFieldForageDto> selectToRecalculate(
	    		Integer cropCommodityId,
	    		String enteredYieldMeasUnitTypeCode,
	    		Integer effectiveCropYear,
	    		Integer expiryCropYear
				) throws DaoException {

		logger.debug("<selectToRecalculate");

		List<DeclaredYieldFieldForageDto> dtos = null;

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
	}}
