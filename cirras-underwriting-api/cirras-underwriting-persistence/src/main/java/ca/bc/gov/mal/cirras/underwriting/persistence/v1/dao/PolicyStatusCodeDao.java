package ca.bc.gov.mal.cirras.underwriting.data.repositories;


import java.io.Serializable;
import java.util.List;

import ca.bc.gov.mal.cirras.underwriting.data.entities.PolicyStatusCodeDto;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;


public interface PolicyStatusCodeDao extends Serializable {
	
	PolicyStatusCodeDto fetch(
		String policyStatusCode
	) throws DaoException;

    List<PolicyStatusCodeDto> fetchAll() throws DaoException;

    void insert(
    	PolicyStatusCodeDto dto, 
        String userId
    ) throws DaoException;
    
    void update(
   		PolicyStatusCodeDto dto, 
        String userId
    ) throws DaoException, NotFoundDaoException;
    
    void delete(
   		String policyStatusCode
   ) throws DaoException, NotFoundDaoException;

   
}
