package ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao;

import java.io.Serializable;
import java.util.List;

import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.DeclaredYieldFieldRollupDto;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;


public interface DeclaredYieldFieldRollupDao extends Serializable {
	
	DeclaredYieldFieldRollupDto fetch(String declaredYieldFieldRollupGuid) throws DaoException;
        
    void insert(DeclaredYieldFieldRollupDto dto, String userId) throws DaoException;
    
    void update(DeclaredYieldFieldRollupDto dto, String userId) throws DaoException, NotFoundDaoException;
    
    void delete(String declaredYieldFieldRollupGuid) throws DaoException, NotFoundDaoException;

    void deleteForDeclaredYieldContract(String declaredYieldContractGuid) throws DaoException, NotFoundDaoException;
    
    List<DeclaredYieldFieldRollupDto> selectForDeclaredYieldContract(String declaredYieldContractGuid) throws DaoException;

    List<DeclaredYieldFieldRollupDto> selectToRecalculate(
    		Integer cropCommodityId,
    		Integer effectiveCropYear,
    		Integer expiryCropYear
    		) throws DaoException;
}
