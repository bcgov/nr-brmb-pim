package ca.bc.gov.mal.cirras.underwriting.data.repositories;


import java.io.Serializable;
import java.util.List;

import ca.bc.gov.mal.cirras.underwriting.data.entities.YieldMeasUnitPlanXrefDto;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;


public interface YieldMeasUnitPlanXrefDao extends Serializable {
	
	YieldMeasUnitPlanXrefDto fetch(
		String yieldMeasUnitPlanXrefGuid
	) throws DaoException;

    List<YieldMeasUnitPlanXrefDto> fetchAll() throws DaoException;

    void insert(
    		YieldMeasUnitPlanXrefDto dto, 
        String userId
    ) throws DaoException;
    
    void update(
    		YieldMeasUnitPlanXrefDto dto, 
        String userId
    ) throws DaoException, NotFoundDaoException;
    
    void delete(
    	String yieldMeasUnitPlanXrefGuid
   ) throws DaoException, NotFoundDaoException;

}
