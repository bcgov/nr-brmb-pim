package ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao;

import java.io.Serializable;
import java.util.List;

import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.InventoryContractDto;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;


public interface InventoryContractDao extends Serializable {
	
	InventoryContractDto fetch(String inventoryContractGuid) throws DaoException;
        
    void insert(InventoryContractDto dto, String userId) throws DaoException;
    
    void update(InventoryContractDto dto, String userId) throws DaoException, NotFoundDaoException;
    
    void delete(String inventoryContractGuid) throws DaoException, NotFoundDaoException;
    
    List<InventoryContractDto> select(Integer contractId, Integer cropYear) throws DaoException;

	InventoryContractDto getByGrowerContract(Integer growerContractYearId) throws DaoException;
   
	InventoryContractDto selectForPrintout(String inventoryContractGuid) throws DaoException;
    
}
