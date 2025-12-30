package ca.bc.gov.mal.cirras.underwriting.data.repositories;

import java.io.Serializable;
import java.util.List;

import ca.bc.gov.mal.cirras.underwriting.data.entities.VerifiedYieldGrainBasketDto;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;


public interface VerifiedYieldGrainBasketDao extends Serializable {
	
	VerifiedYieldGrainBasketDto fetch(String verifiedYieldGrainBasketGuid) throws DaoException;
        
    void insert(VerifiedYieldGrainBasketDto dto, String userId) throws DaoException;
    
    void update(VerifiedYieldGrainBasketDto dto, String userId) throws DaoException, NotFoundDaoException;
    
    void delete(String verifiedYieldGrainBasketGuid) throws DaoException, NotFoundDaoException;

    void deleteForVerifiedYieldContract(String verifiedYieldContractGuid) throws DaoException, NotFoundDaoException;
    
    List<VerifiedYieldGrainBasketDto> selectForVerifiedYieldContract(String verifiedYieldContractGuid) throws DaoException;
}
