package ca.bc.gov.mal.cirras.underwriting.data.repositories;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import ca.bc.gov.mal.cirras.underwriting.data.repositories.InventoryBerriesDao;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.mapper.InventoryBerriesMapper;
import ca.bc.gov.mal.cirras.underwriting.data.entities.InventoryBerriesDto;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.mybatis.BaseDao;


@Repository
public class InventoryBerriesDaoImpl extends BaseDao implements InventoryBerriesDao {

	private static final long serialVersionUID = 1L;

	private static final Logger logger = LoggerFactory.getLogger(InventoryBerriesDaoImpl.class);

	@Autowired
	private InventoryBerriesMapper mapper;

	@Override
	public InventoryBerriesDto fetch(String inventoryBerriesGuid) throws DaoException {
		logger.debug("<fetch");

		InventoryBerriesDto result = null;

		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("inventoryBerriesGuid", inventoryBerriesGuid);
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
	public void insert(InventoryBerriesDto dto, String userId) throws DaoException {
		logger.debug("<insert");

		String inventoryBerriesGuid = null;
		
		try {
			Map<String, Object> parameters = new HashMap<String, Object>();

			parameters.put("dto", dto);
			parameters.put("userId", userId);
			int count = this.mapper.insert(parameters);

			if(count==0) {
				throw new DaoException("Record not inserted: "+count);
			}

			inventoryBerriesGuid = (String) parameters.get("inventoryBerriesGuid");
			dto.setInventoryBerriesGuid(inventoryBerriesGuid);
			
			
		} catch (RuntimeException e) {
			handleException(e);
		}

		logger.debug(">insert " + inventoryBerriesGuid);
	}
	

	@Override
	public void update(InventoryBerriesDto dto, String userId) 
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
	public void delete(String inventoryBerriesGuid) throws DaoException, NotFoundDaoException {
		logger.debug("<delete");

		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("inventoryBerriesGuid", inventoryBerriesGuid);
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
	public void deleteForInventoryField(String inventoryFieldGuid) throws DaoException, NotFoundDaoException {
		logger.debug("<deleteForInventoryField (Berries)");

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

		logger.debug(">deleteForInventoryField (Berries)");

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
	public void deleteForInventoryContract(String inventoryContractGuid) throws DaoException, NotFoundDaoException {
		logger.debug("<deleteForInventoryContract (Berries)");

		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("inventoryContractGuid", inventoryContractGuid);
			this.mapper.deleteForInventoryContract(parameters);

		} catch (RuntimeException e) {
			handleException(e);
		}

		logger.debug(">deleteForInventoryContract (Berries)");
		
	}
	
	@Override
	public List<InventoryBerriesDto> select(String inventoryFieldGuid) throws DaoException {
		List<InventoryBerriesDto> dtos = null;

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

	@Override
	public InventoryBerriesDto selectForRollover(Integer fieldId, Integer cropYear, Integer insurancePlanId, Integer plantingNumber) throws DaoException {
		logger.debug("<selectForRollover");

		InventoryBerriesDto result = null;

		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("fieldId", fieldId);
			parameters.put("cropYear", cropYear);
			parameters.put("insurancePlanId", insurancePlanId);
			parameters.put("plantingNumber", plantingNumber);
						
			result = this.mapper.selectForRollover(parameters);

		} catch (RuntimeException e) {
			handleException(e);
		}

		logger.debug(">selectForRollover " + result);
		return result;	
	}
}
