package ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao;

import java.io.Serializable;
import java.util.List;

import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.DeclaredYieldContractCommodityForageDto;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;


public interface DeclaredYieldContractCommodityForageDao extends Serializable {
	
	DeclaredYieldContractCommodityForageDto fetch(String declaredYieldContractCmdtyForageGuid) throws DaoException;
        
    void insert(DeclaredYieldContractCommodityForageDto dto, String userId) throws DaoException;
    
    void update(DeclaredYieldContractCommodityForageDto dto, String userId) throws DaoException, NotFoundDaoException;
    
    void delete(String declaredYieldContractCmdtyForageGuid) throws DaoException, NotFoundDaoException;

    void deleteForDeclaredYieldContract(String declaredYieldContractGuid) throws DaoException, NotFoundDaoException;
    
    List<DeclaredYieldContractCommodityForageDto> selectForDeclaredYieldContract(String declaredYieldContractGuid) throws DaoException;

    List<DeclaredYieldContractCommodityForageDto> selectToRecalculate(
    		Integer cropCommodityId,
    		String enteredYieldMeasUnitTypeCode,
    		Integer effectiveCropYear,
    		Integer expiryCropYear
    		) throws DaoException;
}
