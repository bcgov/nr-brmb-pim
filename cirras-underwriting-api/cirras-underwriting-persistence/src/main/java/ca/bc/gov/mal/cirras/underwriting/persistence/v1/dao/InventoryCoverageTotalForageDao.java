package ca.bc.gov.mal.cirras.underwriting.data.repositories;

import java.io.Serializable;
import java.util.List;

import ca.bc.gov.mal.cirras.underwriting.data.entities.InventoryCoverageTotalForageDto;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;


public interface InventoryCoverageTotalForageDao extends Serializable {
	
	InventoryCoverageTotalForageDto fetch(String inventoryCoverageTotalForageGuid) throws DaoException;
        
    void insert(InventoryCoverageTotalForageDto dto, String userId) throws DaoException;
    
    void update(InventoryCoverageTotalForageDto dto, String userId) throws DaoException, NotFoundDaoException;
    
    void delete(String inventoryCoverageTotalForageGuid) throws DaoException, NotFoundDaoException;

    void deleteForInventoryContract(String inventoryContractGuid) throws DaoException, NotFoundDaoException;
    
    List<InventoryCoverageTotalForageDto> select(String inventoryContractGuid) throws DaoException;

}
