package ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao;


import java.io.Serializable;
import java.util.List;

import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.YieldMeasUnitTypeCodeDto;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;


public interface YieldMeasUnitTypeCodeDao extends Serializable {
	
	YieldMeasUnitTypeCodeDto fetch(
		String yieldMeasUnitTypeCode
	) throws DaoException;

    List<YieldMeasUnitTypeCodeDto> fetchAll() throws DaoException;

    void insert(
    	YieldMeasUnitTypeCodeDto dto, 
        String userId
    ) throws DaoException;
    
    void update(
    	YieldMeasUnitTypeCodeDto dto, 
        String userId
    ) throws DaoException, NotFoundDaoException;
    
    void delete(
    	String yieldMeasUnitTypeCode
   ) throws DaoException, NotFoundDaoException;

    List<YieldMeasUnitTypeCodeDto> selectByPlan(
   		Integer insurancePlanId
        ) throws DaoException;
   
}
