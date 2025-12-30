package ca.bc.gov.mal.cirras.underwriting.data.repositories;


import java.io.Serializable;
import java.util.List;

import ca.bc.gov.mal.cirras.underwriting.data.entities.GrowerContactDto;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;


public interface GrowerContactDao extends Serializable {
	
	GrowerContactDto fetch(
   		Integer growerContactId
	) throws DaoException;

    List<GrowerContactDto> fetchAll() throws DaoException;

    void insert(
    	GrowerContactDto dto, 
        String userId
    ) throws DaoException;
    
    void update(
   		GrowerContactDto dto, 
        String userId
    ) throws DaoException, NotFoundDaoException;
    
    void delete(
   		Integer growerContactId
   ) throws DaoException, NotFoundDaoException;

   
}
