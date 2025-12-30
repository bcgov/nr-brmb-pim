package ca.bc.gov.mal.cirras.underwriting.data.repositories;


import java.io.Serializable;
import java.util.List;

import ca.bc.gov.mal.cirras.underwriting.data.entities.ContactPhoneDto;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;


public interface ContactPhoneDao extends Serializable {
	
	ContactPhoneDto fetch(
		Integer contactPhoneId
	) throws DaoException;

    List<ContactPhoneDto> fetchAll() throws DaoException;

    void insert(
    	ContactPhoneDto dto, 
        String userId
    ) throws DaoException;
    
    void update(
   		ContactPhoneDto dto, 
        String userId
    ) throws DaoException, NotFoundDaoException;
    
    void delete(
    	Integer contactPhoneId
   ) throws DaoException, NotFoundDaoException;

   
}
