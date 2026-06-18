package ca.bc.gov.mal.cirras.underwriting.data.repositories;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import ca.bc.gov.mal.cirras.underwriting.data.repositories.mapper.InventoryFieldMapper;
import ca.bc.gov.mal.cirras.underwriting.data.entities.InventoryFieldDto;
import ca.bc.gov.mal.cirras.underwriting.data.utils.UnderwritingCodeEnums;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.mybatis.BaseDao;


@Repository
public class InventoryFieldDao extends BaseDao {

	private static final long serialVersionUID = 1L;

	private static final Logger logger = LoggerFactory.getLogger(InventoryFieldDao.class);

	@Autowired
	private InventoryFieldMapper mapper;

	
	public InventoryFieldDto fetch(String inventoryFieldGuid) throws DaoException {
		logger.debug("<fetch");

		InventoryFieldDto result = null;

		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("inventoryFieldGuid", inventoryFieldGuid);
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
	
	
	public InventoryFieldDto selectLinkedGrainPlanting(String inventorySeededForageGuid) throws DaoException {
		logger.debug("<selectLinkedGrainPlanting");

		InventoryFieldDto result = null;

		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("inventorySeededForageGuid", inventorySeededForageGuid);
			result = this.mapper.selectLinkedGrainPlanting(parameters);
			
			if(result!=null) {
				result.resetDirty();
			}
		} catch (RuntimeException e) {
			handleException(e);
		}

		logger.debug(">selectLinkedGrainPlanting " + result);
		return result;	
	}

	
	
	public void insert(InventoryFieldDto dto, String userId) throws DaoException {
		logger.debug("<insert");

		String inventoryFieldGuid = null;
		
		try {
			Map<String, Object> parameters = new HashMap<String, Object>();

			parameters.put("dto", dto);
			parameters.put("userId", userId);
			int count = this.mapper.insert(parameters);

			if(count==0) {
				throw new DaoException("Record not inserted: "+count);
			}

			inventoryFieldGuid = (String) parameters.get("inventoryFieldGuid");
			dto.setInventoryFieldGuid(inventoryFieldGuid);
			
			
		} catch (RuntimeException e) {
			handleException(e);
		}

		logger.debug(">insert " + inventoryFieldGuid);
	}
	

	
	public void update(InventoryFieldDto dto, String userId) 
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

	
	public void removeLinkToPlantingForInventoryContract(String inventoryContractGuid, String userId) 
			throws DaoException, NotFoundDaoException {
		logger.debug("<removeLinkToPlantingForInventoryContract");
		
		try {

			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("inventoryContractGuid", inventoryContractGuid);
			parameters.put("userId", userId);
			this.mapper.removeLinkToPlantingForInventoryContract(parameters);

		} catch (RuntimeException e) {
			handleException(e);
		}

		logger.debug(">removeLinkToPlantingForInventoryContract");
	}

	
	public void removeLinkToPlantingForField(Integer fieldId, String userId) throws DaoException, NotFoundDaoException {
		logger.debug("<removeLinkToPlantingForField");
		
		try {

			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("fieldId", fieldId);
			parameters.put("userId", userId);
			this.mapper.removeLinkToPlantingForField(parameters);

		} catch (RuntimeException e) {
			handleException(e);
		}

		logger.debug(">removeLinkToPlantingForField");
	}
	
	
	
	public void removeLinkToPlantingForFieldAndYear(Integer fieldId, Integer cropYear, String userId) throws DaoException, NotFoundDaoException {
		logger.debug("<removeLinkToPlantingForFieldAndYear");

		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("fieldId", fieldId);
			parameters.put("cropYear", cropYear);
			parameters.put("userId", userId);
			this.mapper.removeLinkToPlantingForFieldAndYear(parameters);

		} catch (RuntimeException e) {
			handleException(e);
		}

		logger.debug(">removeLinkToPlantingForFieldAndYear");
	}
	

	
	public void delete(String inventoryFieldGuid) throws DaoException, NotFoundDaoException {
		logger.debug("<delete");

		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("inventoryFieldGuid", inventoryFieldGuid);
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
	
		
	public void deleteForInventoryContract(String inventoryContractGuid) throws DaoException, NotFoundDaoException {
		logger.debug("<deleteForInventoryContract (Inventory Field)");

		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("inventoryContractGuid", inventoryContractGuid);
			this.mapper.deleteForInventoryContract(parameters);

		} catch (RuntimeException e) {
			handleException(e);
		}

		logger.debug(">deleteForInventoryContract (Inventory Field)");
		
	}
	
	
	public List<InventoryFieldDto> select(Integer fieldId, Integer cropYear, Integer insurancePlanId) throws DaoException {
		List<InventoryFieldDto> dtos = null;

		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			
			parameters.put("fieldId", fieldId);
			parameters.put("cropYear", cropYear);
			parameters.put("insurancePlanId", insurancePlanId);
						
			dtos = this.mapper.select(parameters);

		} catch (RuntimeException e) {
			handleException(e);
		}

		logger.debug(">select " + dtos);
		return dtos;
	}

	
	public List<InventoryFieldDto> selectForDeclaredYield(Integer fieldId, Integer cropYear, Integer insurancePlanId) throws DaoException {
		logger.debug("<selectForDeclaredYield");

		List<InventoryFieldDto> dtos = null;

		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			
			parameters.put("fieldId", fieldId);
			parameters.put("cropYear", cropYear);
			parameters.put("insurancePlanId", insurancePlanId);
						
			dtos = this.mapper.selectForDeclaredYield(parameters);

		} catch (RuntimeException e) {
			handleException(e);
		}

		logger.debug(">selectForDeclaredYield " + dtos);
		return dtos;
	}
	
	
	public List<InventoryFieldDto> selectForRollover(Integer fieldId, Integer cropYear, Integer insurancePlanId) throws DaoException {
		List<InventoryFieldDto> dtos = null;

		try {
			Map<String, Object> parameters = new HashMap<String, Object>();

			Boolean isGrainRollover = false;
			if ( insurancePlanId.equals(UnderwritingCodeEnums.InsurancePlans.GRAIN.getInsurancePlanId()) ) {
				isGrainRollover = true;
			}
			
			parameters.put("fieldId", fieldId);
			parameters.put("cropYear", cropYear);
			parameters.put("insurancePlanId", insurancePlanId);
			parameters.put("isGrainRollover", isGrainRollover);
						
			dtos = this.mapper.selectForRollover(parameters);

		} catch (RuntimeException e) {
			handleException(e);
		}

		logger.debug(">select " + dtos);
		return dtos;
	}
	
	
	public List<InventoryFieldDto> selectForField(Integer fieldId) throws DaoException {
		List<InventoryFieldDto> dtos = null;

		logger.debug("<selectForField");

		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			
			parameters.put("fieldId", fieldId);
						
			dtos = this.mapper.selectForField(parameters);

		} catch (RuntimeException e) {
			handleException(e);
		}

		logger.debug(">selectForField " + dtos);
		return dtos;
	}
}
