package ca.bc.gov.mal.cirras.underwriting.data.repositories;

import java.io.Serializable;
import java.util.List;

import ca.bc.gov.mal.cirras.underwriting.data.entities.DeclaredYieldContractCommodityDto;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;


public interface DeclaredYieldContractCommodityDao extends Serializable {
	
	DeclaredYieldContractCommodityDto fetch(String declaredYieldContractCommodityGuid) throws DaoException;
        
    void insert(DeclaredYieldContractCommodityDto dto, String userId) throws DaoException;
    
    void update(DeclaredYieldContractCommodityDto dto, String userId) throws DaoException, NotFoundDaoException;
    
    void delete(String declaredYieldContractCommodityGuid) throws DaoException, NotFoundDaoException;

    void deleteForDeclaredYieldContract(String declaredYieldContractGuid) throws DaoException, NotFoundDaoException;
    
    List<DeclaredYieldContractCommodityDto> selectForDeclaredYieldContract(String declaredYieldContractGuid) throws DaoException;

    List<DeclaredYieldContractCommodityDto> selectToRecalculate(
    		Integer cropCommodityId,
    		String enteredYieldMeasUnitTypeCode,
    		Integer effectiveCropYear,
    		Integer expiryCropYear
    		) throws DaoException;
}
