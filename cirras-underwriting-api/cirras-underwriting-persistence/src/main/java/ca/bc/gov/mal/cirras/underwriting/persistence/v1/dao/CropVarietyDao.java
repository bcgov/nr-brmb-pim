package ca.bc.gov.mal.cirras.underwriting.data.repositories;


import java.io.Serializable;
import java.util.List;

import ca.bc.gov.mal.cirras.underwriting.data.entities.CropVarietyDto;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;


public interface CropVarietyDao extends Serializable {
	
	CropVarietyDto fetch(
		Integer cropVarietyId
	) throws DaoException;

    List<CropVarietyDto> fetchAll() throws DaoException;

    void insert(
    	CropVarietyDto dto, 
        String userId
    ) throws DaoException;
    
    void update(
   		CropVarietyDto dto, 
        String userId
    ) throws DaoException, NotFoundDaoException;
    
    void delete(
    	Integer cropVarietyId
   ) throws DaoException, NotFoundDaoException;
    
    List<CropVarietyDto> select(
    		Integer insurancePlanId
        ) throws DaoException;
   
}
