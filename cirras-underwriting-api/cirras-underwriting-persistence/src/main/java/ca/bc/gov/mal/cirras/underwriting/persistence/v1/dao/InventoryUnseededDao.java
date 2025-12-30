package ca.bc.gov.mal.cirras.underwriting.data.repositories;

import java.io.Serializable;
import java.util.List;

import ca.bc.gov.mal.cirras.underwriting.data.entities.InventoryUnseededDto;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;


public interface InventoryUnseededDao extends Serializable {
	
	InventoryUnseededDto fetch(String inventoryUnseededGuid) throws DaoException;
        
    void insert(InventoryUnseededDto dto, String userId) throws DaoException;
    
    void update(InventoryUnseededDto dto, String userId) throws DaoException, NotFoundDaoException;
    
    void delete(String inventoryUnseededGuid) throws DaoException, NotFoundDaoException;
    
    void deleteForInventoryField(String inventoryFieldGuid) throws DaoException, NotFoundDaoException;
    
    void deleteForField(Integer fieldId) throws DaoException, NotFoundDaoException;

    void deleteForInventoryContract(String inventoryContractGuid) throws DaoException, NotFoundDaoException;

    List<InventoryUnseededDto> select(String inventoryFieldGuid) throws DaoException;

    List<InventoryUnseededDto> selectTotalsForFieldYearPlan(Integer fieldId, Integer cropYear, Integer insurancePlanId) throws DaoException;

}
