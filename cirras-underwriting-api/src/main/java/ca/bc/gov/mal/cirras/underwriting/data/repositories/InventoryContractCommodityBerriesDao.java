package ca.bc.gov.mal.cirras.underwriting.data.repositories;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import ca.bc.gov.mal.cirras.underwriting.data.repositories.mapper.InventoryContractCommodityBerriesMapper;
import ca.bc.gov.mal.cirras.underwriting.data.entities.InventoryContractCommodityBerriesDto;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.mybatis.BaseDao;


@Repository
public class InventoryContractCommodityBerriesDao extends BaseDao {

	private static final long serialVersionUID = 1L;

	private static final Logger logger = LoggerFactory.getLogger(InventoryContractCommodityBerriesDao.class);

	@Autowired
	private InventoryContractCommodityBerriesMapper mapper;

	
	public InventoryContractCommodityBerriesDto fetch(String inventoryContractCommodityBerriesGuid) throws DaoException {
		logger.debug("<fetch");

		InventoryContractCommodityBerriesDto result = null;

		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("inventoryContractCommodityBerriesGuid", inventoryContractCommodityBerriesGuid);
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

	
	
	public void insert(InventoryContractCommodityBerriesDto dto, String userId) throws DaoException {
		logger.debug("<insert");

		String inventoryContractCommodityBerriesGuid = null;
		
		try {
			Map<String, Object> parameters = new HashMap<String, Object>();

			parameters.put("dto", dto);
			parameters.put("userId", userId);
			int count = this.mapper.insert(parameters);

			if(count==0) {
				throw new DaoException("Record not inserted: "+count);
			}

			inventoryContractCommodityBerriesGuid = (String) parameters.get("inventoryContractCommodityBerriesGuid");
			dto.setInventoryContractCommodityBerriesGuid(inventoryContractCommodityBerriesGuid);
			
			
		} catch (RuntimeException e) {
			handleException(e);
		}

		logger.debug(">insert " + inventoryContractCommodityBerriesGuid);
	}
	

	
	public void update(InventoryContractCommodityBerriesDto dto, String userId) 
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

	
	public void delete(String inventoryContractCommodityBerriesGuid) throws DaoException, NotFoundDaoException {
		logger.debug("<delete");

		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("inventoryContractCommodityBerriesGuid", inventoryContractCommodityBerriesGuid);
			int count = this.mapper.delete(parameters);

			if(count==0) {
				throw new DaoException("Record not deleted: "+count);
			}
		} catch (RuntimeException e) {
			handleException(e);
		}

		logger.debug(">delete");
	}
	

	
	public void deleteForInventoryContract(String inventoryContractGuid) throws DaoException, NotFoundDaoException {

		logger.debug("<deleteForInventoryContract");

		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("inventoryContractGuid", inventoryContractGuid);
			this.mapper.deleteForInventoryContract(parameters);

		} catch (RuntimeException e) {
			handleException(e);
		}

		logger.debug(">deleteForInventoryContract");
		
	}	
	
	
	public List<InventoryContractCommodityBerriesDto> select(String inventoryContractGuid) throws DaoException {
		List<InventoryContractCommodityBerriesDto> dtos = null;

		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			
			parameters.put("inventoryContractGuid", inventoryContractGuid);
						
			dtos = this.mapper.select(parameters);

		} catch (RuntimeException e) {
			handleException(e);
		}

		logger.debug(">select " + dtos);
		return dtos;
	}
		
}
