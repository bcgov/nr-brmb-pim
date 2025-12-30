package ca.bc.gov.mal.cirras.underwriting.data.repositories;


import java.io.Serializable;
import java.util.List;

import ca.bc.gov.mal.cirras.underwriting.data.entities.YieldMeasUnitConversionDto;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;


public interface YieldMeasUnitConversionDao extends Serializable {
	
	YieldMeasUnitConversionDto fetch(String yieldMeasUnitConversionGuid) throws DaoException;

    List<YieldMeasUnitConversionDto> fetchAll() throws DaoException;

    void insert(YieldMeasUnitConversionDto dto, String userId) throws DaoException;
    
    void update(YieldMeasUnitConversionDto dto, String userId) throws DaoException, NotFoundDaoException;
    
    void delete(String yieldMeasUnitConversionGuid) throws DaoException, NotFoundDaoException;

    List<YieldMeasUnitConversionDto> selectByYearAndPlan(Integer cropYear, Integer insurancePlanId) throws DaoException;

    List<YieldMeasUnitConversionDto> selectLatestVersionByPlan(Integer insurancePlanId, String srcYieldMeasUnitTypeCode, String targetYieldMeasUnitTypeCode) throws DaoException;

}
