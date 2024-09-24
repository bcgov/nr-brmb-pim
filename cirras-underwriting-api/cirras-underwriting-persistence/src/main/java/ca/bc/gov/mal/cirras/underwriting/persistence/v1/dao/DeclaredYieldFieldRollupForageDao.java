package ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao;

import java.io.Serializable;
import java.util.List;

import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.DeclaredYieldFieldRollupForageDto;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;


public interface DeclaredYieldFieldRollupForageDao extends Serializable {
	
	DeclaredYieldFieldRollupForageDto fetch(String declaredYieldFieldRollupForageGuid) throws DaoException;
        
    void insert(DeclaredYieldFieldRollupForageDto dto, String userId) throws DaoException;
    
    void update(DeclaredYieldFieldRollupForageDto dto, String userId) throws DaoException, NotFoundDaoException;
    
    void delete(String declaredYieldFieldRollupForageGuid) throws DaoException, NotFoundDaoException;

    void deleteForDeclaredYieldContract(String declaredYieldContractGuid) throws DaoException, NotFoundDaoException;
    
    List<DeclaredYieldFieldRollupForageDto> selectForDeclaredYieldContract(String declaredYieldContractGuid) throws DaoException;

    List<DeclaredYieldFieldRollupForageDto> selectToRecalculate(
    		Integer cropCommodityId,
    		String enteredYieldMeasUnitTypeCode,
    		Integer effectiveCropYear,
    		Integer expiryCropYear
    		) throws DaoException;
}
