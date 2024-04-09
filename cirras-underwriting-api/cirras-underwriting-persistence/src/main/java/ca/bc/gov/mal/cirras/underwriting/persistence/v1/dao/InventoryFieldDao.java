package ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao;

import java.io.Serializable;
import java.util.List;

import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.InventoryFieldDto;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;


public interface InventoryFieldDao extends Serializable {
	
	InventoryFieldDto fetch(String inventoryFieldGuid) throws DaoException;

	InventoryFieldDto selectLinkedGrainPlanting(String inventorySeededForageGuid) throws DaoException;

    void insert(InventoryFieldDto dto, String userId) throws DaoException;
    
    void update(InventoryFieldDto dto, String userId) throws DaoException, NotFoundDaoException;
    
    void removeLinkToPlantingForInventoryContract(String inventoryContractGuid, String userId) throws DaoException, NotFoundDaoException;

    void removeLinkToPlantingForField(Integer fieldId, String userId) throws DaoException, NotFoundDaoException;
    
    void removeLinkToPlantingForFieldAndYear(Integer fieldId, Integer cropYear, String userId) throws DaoException, NotFoundDaoException;
    
    void delete(String inventoryFieldGuid) throws DaoException, NotFoundDaoException;
    
    void deleteForField(Integer fieldId) throws DaoException, NotFoundDaoException;
    
    void deleteForInventoryContract(String inventoryContractGuid) throws DaoException, NotFoundDaoException;

    List<InventoryFieldDto> select(Integer fieldId, Integer cropYear, Integer insurancePlanId) throws DaoException;

    List<InventoryFieldDto> selectForDeclaredYield(Integer fieldId, Integer cropYear, Integer insurancePlanId) throws DaoException;
    
    List<InventoryFieldDto> selectForRollover(Integer fieldId, Integer cropYear, Integer insurancePlanId) throws DaoException;

    List<InventoryFieldDto> selectForField(Integer fieldId) throws DaoException;
}
