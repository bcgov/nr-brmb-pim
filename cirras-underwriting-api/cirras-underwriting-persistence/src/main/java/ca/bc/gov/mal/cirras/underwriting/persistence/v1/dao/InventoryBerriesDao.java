package ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao;

import java.io.Serializable;
import java.util.List;

import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.InventoryBerriesDto;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;


public interface InventoryBerriesDao extends Serializable {
	
	InventoryBerriesDto fetch(String inventoryBerriesGuid) throws DaoException;
    
    void insert(InventoryBerriesDto dto, String userId) throws DaoException;
    
    void update(InventoryBerriesDto dto, String userId) throws DaoException, NotFoundDaoException;
    
    void delete(String inventoryBerriesGuid) throws DaoException, NotFoundDaoException;
    
    void deleteForInventoryField(String inventoryFieldGuid) throws DaoException, NotFoundDaoException;
    
    void deleteForField(Integer fieldId) throws DaoException, NotFoundDaoException;

    void deleteForInventoryContract(String inventoryContractGuid) throws DaoException, NotFoundDaoException;

    List<InventoryBerriesDto> select(String inventoryFieldGuid) throws DaoException;
}
