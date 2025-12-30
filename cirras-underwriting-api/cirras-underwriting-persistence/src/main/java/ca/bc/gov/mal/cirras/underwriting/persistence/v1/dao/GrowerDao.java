package ca.bc.gov.mal.cirras.underwriting.data.repositories;


import java.io.Serializable;
import java.util.List;

import ca.bc.gov.mal.cirras.underwriting.data.entities.GrowerDto;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;


public interface GrowerDao extends Serializable {
	
	GrowerDto fetch(
   		Integer growerId
	) throws DaoException;

    List<GrowerDto> fetchAll() throws DaoException;

    void insert(
    	GrowerDto dto, 
        String userId
    ) throws DaoException;
    
    void update(
   		GrowerDto dto, 
        String userId
    ) throws DaoException, NotFoundDaoException;
    
    void delete(
   		Integer growerId
   ) throws DaoException, NotFoundDaoException;

   
}
