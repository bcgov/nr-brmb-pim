package ca.bc.gov.mal.cirras.underwriting.data.repositories;

import java.io.Serializable;
import java.util.List;

import ca.bc.gov.mal.cirras.underwriting.data.entities.VerifiedYieldAmendmentDto;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;


public interface VerifiedYieldAmendmentDao extends Serializable {
	
	VerifiedYieldAmendmentDto fetch(String verifiedYieldAmendmentGuid) throws DaoException;
        
    void insert(VerifiedYieldAmendmentDto dto, String userId) throws DaoException;
    
    void update(VerifiedYieldAmendmentDto dto, String userId) throws DaoException, NotFoundDaoException;
    
    void delete(String verifiedYieldAmendmentGuid) throws DaoException, NotFoundDaoException;

    void deleteForVerifiedYieldContract(String verifiedYieldContractGuid) throws DaoException, NotFoundDaoException;
    
    List<VerifiedYieldAmendmentDto> selectForVerifiedYieldContract(String verifiedYieldContractGuid) throws DaoException;
}
