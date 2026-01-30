package ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao;

import java.io.Serializable;
import java.util.List;

import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.InventoryContractCommodityBerriesDto;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;


public interface InventoryContractCommodityBerriesDao extends Serializable {
	
	InventoryContractCommodityBerriesDto fetch(String inventorySeededGrainGuid) throws DaoException;
    
    void insert(InventoryContractCommodityBerriesDto dto, String userId) throws DaoException;
    
    void update(InventoryContractCommodityBerriesDto dto, String userId) throws DaoException, NotFoundDaoException;
    
    void delete(String inventoryContractCommodityBerriesGuid) throws DaoException, NotFoundDaoException;
    
    void deleteForInventoryContract(String inventoryContractGuid) throws DaoException, NotFoundDaoException;

    List<InventoryContractCommodityBerriesDto> select(String inventoryContractGuid) throws DaoException;
}
