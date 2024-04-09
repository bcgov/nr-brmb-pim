package ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao;


import java.io.Serializable;
import java.util.List;

import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.ContactEmailDto;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;


public interface ContactEmailDao extends Serializable {
	
	ContactEmailDto fetch(
		Integer contactEmailId
	) throws DaoException;

    List<ContactEmailDto> fetchAll() throws DaoException;

    void insert(
    	ContactEmailDto dto, 
        String userId
    ) throws DaoException;
    
    void update(
   		ContactEmailDto dto, 
        String userId
    ) throws DaoException, NotFoundDaoException;
    
    void delete(
    	Integer contactEmailId
   ) throws DaoException, NotFoundDaoException;

   
}
