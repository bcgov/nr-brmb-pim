package ca.bc.gov.mal.cirras.underwriting.data.repositories;

import java.io.Serializable;
import java.util.List;

import ca.bc.gov.mal.cirras.underwriting.data.entities.DeclaredYieldFieldForageDto;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;


public interface DeclaredYieldFieldForageDao extends Serializable {
	
	DeclaredYieldFieldForageDto fetch(String declaredYieldFieldForageGuid) throws DaoException;
        
    void insert(DeclaredYieldFieldForageDto dto, String userId) throws DaoException;
    
    void update(DeclaredYieldFieldForageDto dto, String userId) throws DaoException, NotFoundDaoException;
    
    void delete(String declaredYieldFieldForageGuid) throws DaoException, NotFoundDaoException;

    void deleteForField(Integer fieldId) throws DaoException, NotFoundDaoException;

    void deleteForFieldAndYear(Integer fieldId, Integer cropYear) throws DaoException, NotFoundDaoException;

    void deleteForDeclaredYieldContract(String declaredYieldContractGuid) throws DaoException, NotFoundDaoException;

    List<DeclaredYieldFieldForageDto> getByInventoryField(String inventoryFieldGuid) throws DaoException;

    int getTotalDopRecordsWithYield(Integer fieldId, Integer cropYear, Integer insurancePlanId) throws DaoException, NotFoundDaoException;
    
	List<DeclaredYieldFieldForageDto> selectToRecalculate(
    		Integer cropCommodityId,
    		String enteredYieldMeasUnitTypeCode,
    		Integer effectiveCropYear,
    		Integer expiryCropYear
    		) throws DaoException;

 }
