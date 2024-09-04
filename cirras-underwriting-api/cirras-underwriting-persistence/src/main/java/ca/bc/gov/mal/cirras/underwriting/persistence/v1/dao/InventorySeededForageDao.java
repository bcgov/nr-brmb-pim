package ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao;

import java.io.Serializable;
import java.util.List;

import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.InventorySeededForageDto;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;


public interface InventorySeededForageDao extends Serializable {
	
	InventorySeededForageDto fetch(String inventorySeededForageGuid) throws DaoException;
    
	InventorySeededForageDto fetchSimple(String inventorySeededForageGuid) throws DaoException;
    
    void insert(InventorySeededForageDto dto, String userId) throws DaoException;
    
    void update(InventorySeededForageDto dto, String userId) throws DaoException, NotFoundDaoException;
    
    void delete(String inventorySeededForageGuid) throws DaoException, NotFoundDaoException;
    
    void deleteForInventoryField(String inventoryFieldGuid) throws DaoException, NotFoundDaoException;
    
    void deleteForField(Integer fieldId) throws DaoException, NotFoundDaoException;
    
    void deleteForInventoryContract(String inventoryContractGuid) throws DaoException, NotFoundDaoException;

    List<InventorySeededForageDto> select(String inventoryFieldGuid) throws DaoException;

    List<InventorySeededForageDto> selectForRollover(Integer fieldId, Integer cropYear, Integer insurancePlanId, Integer plantingNumber) throws DaoException;
    
    List<InventorySeededForageDto> selectForDeclaredYield(String inventoryFieldGuid) throws DaoException;

    List<InventorySeededForageDto> selectForDopContractCommodityTotals(Integer contractId, Integer cropYear) throws DaoException;

}
