package ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao;


import java.io.Serializable;
import java.util.List;

import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.LegalLandFieldXrefDto;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;


public interface LegalLandFieldXrefDao extends Serializable {
	
	LegalLandFieldXrefDto fetch(
   		Integer legalLandId,
   		Integer fieldId
	) throws DaoException;

    List<LegalLandFieldXrefDto> fetchAll() throws DaoException;

    void insert(
    		LegalLandFieldXrefDto dto, 
        String userId
    ) throws DaoException;
    
    void delete(
   		Integer legalLandId,
   		Integer fieldId
   ) throws DaoException, NotFoundDaoException;

    void deleteForField(Integer fieldId) throws DaoException, NotFoundDaoException;
       
}
