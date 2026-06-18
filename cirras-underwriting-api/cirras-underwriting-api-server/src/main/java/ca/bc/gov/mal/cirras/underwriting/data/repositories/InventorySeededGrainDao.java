package ca.bc.gov.mal.cirras.underwriting.data.repositories;

import java.util.HashMap;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import ca.bc.gov.mal.cirras.underwriting.data.repositories.mapper.InventorySeededGrainMapper;
import ca.bc.gov.mal.cirras.underwriting.data.entities.InventorySeededGrainDto;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.mybatis.BaseDao;


@Repository
public class InventorySeededGrainDao extends BaseDao {

	private static final long serialVersionUID = 1L;

	private static final Logger logger = LoggerFactory.getLogger(InventorySeededGrainDao.class);

	@Autowired
	private InventorySeededGrainMapper mapper;

	
	public InventorySeededGrainDto fetch(String inventorySeededGrainGuid) throws DaoException {
		logger.debug("<fetch");

		InventorySeededGrainDto result = null;

		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("inventorySeededGrainGuid", inventorySeededGrainGuid);
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

	
	public InventorySeededGrainDto fetchSimple(String inventorySeededGrainGuid) throws DaoException {
		logger.debug("<fetchSimple");

		InventorySeededGrainDto result = null;

		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("inventorySeededGrainGuid", inventorySeededGrainGuid);
			result = this.mapper.fetchSimple(parameters);
			
			if(result!=null) {
				result.resetDirty();
			}
		} catch (RuntimeException e) {
			handleException(e);
		}

		logger.debug(">fetchSimple " + result);
		return result;	
	}
	
	
	public void insert(InventorySeededGrainDto dto, String userId) throws DaoException {
		logger.debug("<insert");

		String inventorySeededGrainGuid = null;
		
		try {
			Map<String, Object> parameters = new HashMap<String, Object>();

			parameters.put("dto", dto);
			parameters.put("userId", userId);
			int count = this.mapper.insert(parameters);

			if(count==0) {
				throw new DaoException("Record not inserted: "+count);
			}

			inventorySeededGrainGuid = (String) parameters.get("inventorySeededGrainGuid");
			dto.setInventorySeededGrainGuid(inventorySeededGrainGuid);
			
			
		} catch (RuntimeException e) {
			handleException(e);
		}

		logger.debug(">insert " + inventorySeededGrainGuid);
	}
	

	
	public void update(InventorySeededGrainDto dto, String userId) 
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

	
	public void delete(String inventorySeededGrainGuid) throws DaoException, NotFoundDaoException {
		logger.debug("<delete");

		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("inventorySeededGrainGuid", inventorySeededGrainGuid);
			int count = this.mapper.delete(parameters);

			if(count==0) {
				throw new DaoException("Record not deleted: "+count);
			}
		} catch (RuntimeException e) {
			handleException(e);
		}

		logger.debug(">delete");
	}

	
	public void deleteForInventoryField(String inventoryFieldGuid) throws DaoException, NotFoundDaoException {
		logger.debug("<deleteForInventoryField (Seeded Grain)");

		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("inventoryFieldGuid", inventoryFieldGuid);
			int count = this.mapper.deleteForInventoryField(parameters);

			if(count==0) {
				throw new DaoException("Record not deleted: "+count);
			}
		} catch (RuntimeException e) {
			handleException(e);
		}

		logger.debug(">deleteForInventoryField (Seeded Grain)");

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

	
	public void deleteForInventoryContract(String inventoryContractGuid) throws DaoException, NotFoundDaoException {
		logger.debug("<deleteForInventoryContract (seeded Grain)");

		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("inventoryContractGuid", inventoryContractGuid);
			this.mapper.deleteForInventoryContract(parameters);

		} catch (RuntimeException e) {
			handleException(e);
		}

		logger.debug(">deleteForInventoryContract (seeded Grain)");
		
	}
	
	
	public List<InventorySeededGrainDto> select(String inventoryFieldGuid) throws DaoException {
		List<InventorySeededGrainDto> dtos = null;

		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			
			parameters.put("inventoryFieldGuid", inventoryFieldGuid);
						
			dtos = this.mapper.select(parameters);

		} catch (RuntimeException e) {
			handleException(e);
		}

		logger.debug(">select " + dtos);
		return dtos;
	}

	
	public List<InventorySeededGrainDto> selectForDeclaredYield(String inventoryFieldGuid) throws DaoException {
		logger.debug("<selectForDeclaredYield");

		List<InventorySeededGrainDto> dtos = null;

		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			
			parameters.put("inventoryFieldGuid", inventoryFieldGuid);
						
			dtos = this.mapper.selectForDeclaredYield(parameters);

		} catch (RuntimeException e) {
			handleException(e);
		}

		logger.debug(">selectForDeclaredYield " + dtos);
		return dtos;
	}

	
	public List<InventorySeededGrainDto> selectForVerifiedYield(String inventoryFieldGuid) throws DaoException {
		logger.debug("<selectForVerifiedYield");
	
		List<InventorySeededGrainDto> dtos = null;
	
		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			
			parameters.put("inventoryFieldGuid", inventoryFieldGuid);
						
			dtos = this.mapper.selectForVerifiedYield(parameters);
	
		} catch (RuntimeException e) {
			handleException(e);
		}
	
		logger.debug(">selectForVerifiedYield " + dtos);
		return dtos;
	}	
	
	public List<InventorySeededGrainDto> selectTotalsForFieldYearPlan(Integer fieldId, Integer cropYear, Integer insurancePlanId) throws DaoException {
		List<InventorySeededGrainDto> dtos = null;

		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			
			parameters.put("fieldId", fieldId);
			parameters.put("cropYear", cropYear);
			parameters.put("insurancePlanId", insurancePlanId);
						
			dtos = this.mapper.selectTotalsForFieldYearPlan(parameters);

		} catch (RuntimeException e) {
			handleException(e);
		}

		logger.debug(">select " + dtos);
		return dtos;
	}}
