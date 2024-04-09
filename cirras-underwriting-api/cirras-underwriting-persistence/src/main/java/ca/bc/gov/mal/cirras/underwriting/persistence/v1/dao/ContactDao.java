package ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao;


import java.io.Serializable;
import java.util.List;

import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.ContactDto;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;


public interface ContactDao extends Serializable {
	
	ContactDto fetch(
   		Integer contactId
	) throws DaoException;

    List<ContactDto> fetchAll() throws DaoException;

    void insert(
    	ContactDto dto, 
        String userId
    ) throws DaoException;
    
    void update(
   		ContactDto dto, 
        String userId
    ) throws DaoException, NotFoundDaoException;
    
    void delete(
   		Integer contactId
   ) throws DaoException, NotFoundDaoException;

   
}
