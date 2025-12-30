package ca.bc.gov.mal.cirras.underwriting.data.repositories;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import ca.bc.gov.mal.cirras.underwriting.data.repositories.mapper.InventorySeededForageMapper;
import ca.bc.gov.mal.cirras.underwriting.data.entities.InventorySeededForageDto;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.mybatis.BaseDao;


@Repository
public class InventorySeededForageDao extends BaseDao {

	private static final long serialVersionUID = 1L;

	private static final Logger logger = LoggerFactory.getLogger(InventorySeededForageDao.class);

	@Autowired
	private InventorySeededForageMapper mapper;

	
	public InventorySeededForageDto fetch(String inventorySeededForageGuid) throws DaoException {
		logger.debug("<fetch");

		InventorySeededForageDto result = null;

		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("inventorySeededForageGuid", inventorySeededForageGuid);
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

	
	public InventorySeededForageDto fetchSimple(String inventorySeededForageGuid) throws DaoException {
		logger.debug("<fetchSimple");

		InventorySeededForageDto result = null;

		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("inventorySeededForageGuid", inventorySeededForageGuid);
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
	
	
	public void insert(InventorySeededForageDto dto, String userId) throws DaoException {
		logger.debug("<insert");

		String inventorySeededForageGuid = null;
		
		try {
			Map<String, Object> parameters = new HashMap<String, Object>();

			parameters.put("dto", dto);
			parameters.put("userId", userId);
			int count = this.mapper.insert(parameters);

			if(count==0) {
				throw new DaoException("Record not inserted: "+count);
			}

			inventorySeededForageGuid = (String) parameters.get("inventorySeededForageGuid");
			dto.setInventorySeededForageGuid(inventorySeededForageGuid);
			
			
		} catch (RuntimeException e) {
			handleException(e);
		}

		logger.debug(">insert " + inventorySeededForageGuid);
	}
	

	
	public void update(InventorySeededForageDto dto, String userId) 
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

	
	public void delete(String inventorySeededForageGuid) throws DaoException, NotFoundDaoException {
		logger.debug("<delete");

		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("inventorySeededForageGuid", inventorySeededForageGuid);
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
		logger.debug("<deleteForInventoryField (Seeded Forage)");

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

		logger.debug(">deleteForInventoryField (Seeded Forage)");

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
		logger.debug("<deleteForInventoryContract (seeded Forage)");

		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("inventoryContractGuid", inventoryContractGuid);
			this.mapper.deleteForInventoryContract(parameters);

		} catch (RuntimeException e) {
			handleException(e);
		}

		logger.debug(">deleteForInventoryContract (seeded Forage)");
		
	}
	
	
	public List<InventorySeededForageDto> select(String inventoryFieldGuid) throws DaoException {

		logger.debug("<select ");
		
		List<InventorySeededForageDto> dtos = null;

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

	
	public List<InventorySeededForageDto> selectForRollover(Integer fieldId, Integer cropYear, Integer insurancePlanId, Integer plantingNumber) throws DaoException {
		logger.debug("<selectForRollover");
		
		List<InventorySeededForageDto> dtos = null;

		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			
			parameters.put("fieldId", fieldId);
			parameters.put("cropYear", cropYear);
			parameters.put("insurancePlanId", insurancePlanId);
			parameters.put("plantingNumber", plantingNumber);
						
			dtos = this.mapper.selectForRollover(parameters);

		} catch (RuntimeException e) {
			handleException(e);
		}

		logger.debug(">selectForRollover " + dtos);
		return dtos;
	}
	
	
	public List<InventorySeededForageDto> selectForDeclaredYield(String inventoryFieldGuid) throws DaoException {
		logger.debug("<selectForDeclaredYield");

		List<InventorySeededForageDto> dtos = null;

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
	
	
	public List<InventorySeededForageDto> selectForDopContractCommodityTotals(Integer contractId, Integer cropYear) throws DaoException {

		logger.debug("<selectForDopContractCommodityTotals");
		
		List<InventorySeededForageDto> dtos = null;

		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			
			parameters.put("contractId", contractId);
			parameters.put("cropYear", cropYear);
						
			dtos = this.mapper.selectForDopContractCommodityTotals(parameters);

		} catch (RuntimeException e) {
			handleException(e);
		}

		logger.debug(">selectForDopContractCommodityTotals " + dtos);
		return dtos;
	}

	
	public List<InventorySeededForageDto> selectForVerifiedYield(String inventoryFieldGuid) throws DaoException {
		logger.debug("<selectForVerifiedYield");
	
		List<InventorySeededForageDto> dtos = null;
	
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
}
