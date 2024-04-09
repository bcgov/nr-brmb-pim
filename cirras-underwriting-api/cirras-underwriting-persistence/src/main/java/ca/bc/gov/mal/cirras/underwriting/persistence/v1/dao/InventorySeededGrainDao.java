package ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao;

import java.io.Serializable;
import java.util.List;

import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.InventorySeededGrainDto;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;


public interface InventorySeededGrainDao extends Serializable {
	
	InventorySeededGrainDto fetch(String inventorySeededGrainGuid) throws DaoException;
    
	InventorySeededGrainDto fetchSimple(String inventorySeededGrainGuid) throws DaoException;
    
    void insert(InventorySeededGrainDto dto, String userId) throws DaoException;
    
    void update(InventorySeededGrainDto dto, String userId) throws DaoException, NotFoundDaoException;
    
    void delete(String inventorySeededGrainGuid) throws DaoException, NotFoundDaoException;
    
    void deleteForInventoryField(String inventoryFieldGuid) throws DaoException, NotFoundDaoException;
    
    void deleteForField(Integer fieldId) throws DaoException, NotFoundDaoException;

    void deleteForInventoryContract(String inventoryContractGuid) throws DaoException, NotFoundDaoException;

    List<InventorySeededGrainDto> select(String inventoryFieldGuid) throws DaoException;

    List<InventorySeededGrainDto> selectForDeclaredYield(String inventoryFieldGuid) throws DaoException;
    
    List<InventorySeededGrainDto> selectTotalsForFieldYearPlan(Integer fieldId, Integer cropYear, Integer insurancePlanId) throws DaoException;

}
