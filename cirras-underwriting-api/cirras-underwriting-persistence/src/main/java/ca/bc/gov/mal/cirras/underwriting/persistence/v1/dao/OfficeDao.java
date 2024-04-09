package ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao;


import java.io.Serializable;
import java.util.List;

import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.OfficeDto;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;


public interface OfficeDao extends Serializable {
	
	OfficeDto fetch(
   		Integer officeId
	) throws DaoException;

    List<OfficeDto> fetchAll() throws DaoException;

    void insert(
    	OfficeDto dto, 
        String userId
    ) throws DaoException;
    
    void update(
   		OfficeDto dto, 
        String userId
    ) throws DaoException, NotFoundDaoException;
    
    void delete(
   		Integer officeId
   ) throws DaoException, NotFoundDaoException;

   
}
