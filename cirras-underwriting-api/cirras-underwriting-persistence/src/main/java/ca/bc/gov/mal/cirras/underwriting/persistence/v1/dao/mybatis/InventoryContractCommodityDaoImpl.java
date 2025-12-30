package ca.bc.gov.mal.cirras.underwriting.data.repositories;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import ca.bc.gov.mal.cirras.underwriting.data.repositories.InventoryContractCommodityDao;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.mapper.InventoryContractCommodityMapper;
import ca.bc.gov.mal.cirras.underwriting.data.entities.InventoryContractCommodityDto;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.mybatis.BaseDao;


@Repository
public class InventoryContractCommodityDaoImpl extends BaseDao implements InventoryContractCommodityDao {

	private static final long serialVersionUID = 1L;

	private static final Logger logger = LoggerFactory.getLogger(InventoryContractCommodityDaoImpl.class);

	@Autowired
	private InventoryContractCommodityMapper mapper;

	@Override
	public InventoryContractCommodityDto fetch(String inventoryContractCommodityGuid) throws DaoException {
		logger.debug("<fetch");

		InventoryContractCommodityDto result = null;

		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("inventoryContractCommodityGuid", inventoryContractCommodityGuid);
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
	public void insert(InventoryContractCommodityDto dto, String userId) throws DaoException {
		logger.debug("<insert");

		String inventoryContractCommodityGuid = null;
		
		try {
			Map<String, Object> parameters = new HashMap<String, Object>();

			parameters.put("dto", dto);
			parameters.put("userId", userId);
			int count = this.mapper.insert(parameters);

			if(count==0) {
				throw new DaoException("Record not inserted: "+count);
			}

			inventoryContractCommodityGuid = (String) parameters.get("inventoryContractCommodityGuid");
			dto.setInventoryContractCommodityGuid(inventoryContractCommodityGuid);
			
			
		} catch (RuntimeException e) {
			handleException(e);
		}

		logger.debug(">insert " + inventoryContractCommodityGuid);
	}
	

	@Override
	public void update(InventoryContractCommodityDto dto, String userId) 
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
	public void delete(String inventoryContractCommodityGuid) throws DaoException, NotFoundDaoException {
		logger.debug("<delete");

		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("inventoryContractCommodityGuid", inventoryContractCommodityGuid);
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
	
	@Override
	public List<InventoryContractCommodityDto> select(String inventoryContractGuid) throws DaoException {
		List<InventoryContractCommodityDto> dtos = null;

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
	
	
	@Override
	public List<InventoryContractCommodityDto> selectForDopContract(Integer contractId, Integer cropYear) throws DaoException {

		logger.debug("<selectForDopContract");
		
		List<InventoryContractCommodityDto> dtos = null;

		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			
			parameters.put("contractId", contractId);
			parameters.put("cropYear", cropYear);
						
			dtos = this.mapper.selectForDopContract(parameters);

		} catch (RuntimeException e) {
			handleException(e);
		}

		logger.debug(">selectForDopContract " + dtos);
		return dtos;
	}
		
}
