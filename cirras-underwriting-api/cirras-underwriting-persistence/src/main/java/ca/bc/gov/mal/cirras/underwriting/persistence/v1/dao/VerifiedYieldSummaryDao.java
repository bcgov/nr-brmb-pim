package ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao;

import java.io.Serializable;
import java.util.List;

import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.VerifiedYieldSummaryDto;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;


public interface VerifiedYieldSummaryDao extends Serializable {
	
	VerifiedYieldSummaryDto fetch(String verifiedYieldSummaryGuid) throws DaoException;
        
    void insert(VerifiedYieldSummaryDto dto, String userId) throws DaoException;
    
    void update(VerifiedYieldSummaryDto dto, String userId) throws DaoException, NotFoundDaoException;
    
    void delete(String verifiedYieldSummaryGuid) throws DaoException, NotFoundDaoException;

    void deleteForVerifiedYieldContract(String verifiedYieldContractGuid) throws DaoException, NotFoundDaoException;
    
    List<VerifiedYieldSummaryDto> selectForVerifiedYieldContract(String verifiedYieldContractGuid) throws DaoException;
}
