package ca.bc.gov.mal.cirras.underwriting.data.repositories;

import java.io.Serializable;

import ca.bc.gov.mal.cirras.underwriting.data.entities.VerifiedYieldContractDto;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;


public interface VerifiedYieldContractDao extends Serializable {
	
	VerifiedYieldContractDto fetch(String verifiedYieldContractGuid) throws DaoException;
        
    void insert(VerifiedYieldContractDto dto, String userId) throws DaoException;
    
    void update(VerifiedYieldContractDto dto, String userId) throws DaoException, NotFoundDaoException;
    
    void delete(String verifiedYieldContractGuid) throws DaoException, NotFoundDaoException;
    
    VerifiedYieldContractDto getByContractAndYear(Integer contractId, Integer cropYear) throws DaoException;

    VerifiedYieldContractDto getByGrowerContract(Integer growerContractYearId) throws DaoException;
       
}
