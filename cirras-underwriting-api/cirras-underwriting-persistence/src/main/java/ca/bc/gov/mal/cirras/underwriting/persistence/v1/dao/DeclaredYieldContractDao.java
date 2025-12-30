package ca.bc.gov.mal.cirras.underwriting.data.repositories;

import java.io.Serializable;

import ca.bc.gov.mal.cirras.underwriting.data.entities.DeclaredYieldContractDto;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;


public interface DeclaredYieldContractDao extends Serializable {
	
	DeclaredYieldContractDto fetch(String declaredYieldContractGuid) throws DaoException;
        
    void insert(DeclaredYieldContractDto dto, String userId) throws DaoException;
    
    void update(DeclaredYieldContractDto dto, String userId) throws DaoException, NotFoundDaoException;
    
    void delete(String declaredYieldContractGuid) throws DaoException, NotFoundDaoException;
    
    DeclaredYieldContractDto getByContractAndYear(Integer contractId, Integer cropYear) throws DaoException;

    DeclaredYieldContractDto getByGrowerContract(Integer growerContractYearId) throws DaoException;
       
}
