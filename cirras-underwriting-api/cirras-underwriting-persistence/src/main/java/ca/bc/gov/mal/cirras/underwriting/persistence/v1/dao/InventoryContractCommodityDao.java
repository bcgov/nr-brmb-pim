package ca.bc.gov.mal.cirras.underwriting.data.repositories;

import java.io.Serializable;
import java.util.List;

import ca.bc.gov.mal.cirras.underwriting.data.entities.InventoryContractCommodityDto;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;


public interface InventoryContractCommodityDao extends Serializable {
	
	InventoryContractCommodityDto fetch(String inventoryContractCommodityGuid) throws DaoException;
        
    void insert(InventoryContractCommodityDto dto, String userId) throws DaoException;
    
    void update(InventoryContractCommodityDto dto, String userId) throws DaoException, NotFoundDaoException;
    
    void delete(String inventoryContractCommodityGuid) throws DaoException, NotFoundDaoException;

    void deleteForInventoryContract(String inventoryContractGuid) throws DaoException, NotFoundDaoException;
    
    List<InventoryContractCommodityDto> select(String inventoryContractGuid) throws DaoException;

    List<InventoryContractCommodityDto> selectForDopContract(Integer contractId, Integer cropYear) throws DaoException;
}
