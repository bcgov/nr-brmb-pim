package ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao;

import java.io.Serializable;
import java.util.List;

import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.DeclaredYieldFieldDto;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;


public interface DeclaredYieldFieldDao extends Serializable {
	
	DeclaredYieldFieldDto fetch(String declaredYieldFieldGuid) throws DaoException;
        
    void insert(DeclaredYieldFieldDto dto, String userId) throws DaoException;
    
    void update(DeclaredYieldFieldDto dto, String userId) throws DaoException, NotFoundDaoException;
    
    void delete(String declaredYieldFieldGuid) throws DaoException, NotFoundDaoException;
    
    void deleteForField(Integer fieldId) throws DaoException, NotFoundDaoException;

    void deleteForDeclaredYieldContract(String declaredYieldContractGuid) throws DaoException, NotFoundDaoException;
    
    DeclaredYieldFieldDto getByInventoryField(String inventoryFieldGuid) throws DaoException;

    int getTotalDopRecordsWithYield(Integer fieldId, Integer cropYear, Integer insurancePlanId) throws DaoException, NotFoundDaoException;

    List<DeclaredYieldFieldDto> selectToRecalculate(
    		Integer cropCommodityId,
    		String enteredYieldMeasUnitTypeCode,
    		Integer effectiveCropYear,
    		Integer expiryCropYear
    		) throws DaoException;
 }