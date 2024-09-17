package ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao;

import java.io.Serializable;
import java.util.List;

import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.DeclaredYieldRollupForageDto;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;


public interface DeclaredYieldRollupForageDao extends Serializable {
	
	DeclaredYieldRollupForageDto fetch(String declaredYieldRollupForageGuid) throws DaoException;
        
    void insert(DeclaredYieldRollupForageDto dto, String userId) throws DaoException;
    
    void update(DeclaredYieldRollupForageDto dto, String userId) throws DaoException, NotFoundDaoException;
    
    void delete(String declaredYieldRollupForageGuid) throws DaoException, NotFoundDaoException;

    void deleteForDeclaredYieldContract(String declaredYieldContractGuid) throws DaoException, NotFoundDaoException;
    
    List<DeclaredYieldRollupForageDto> selectForDeclaredYieldContract(String declaredYieldContractGuid) throws DaoException;

    List<DeclaredYieldRollupForageDto> selectToRecalculate(
    		Integer cropCommodityId,
    		String enteredYieldMeasUnitTypeCode,
    		Integer effectiveCropYear,
    		Integer expiryCropYear
    		) throws DaoException;
}
