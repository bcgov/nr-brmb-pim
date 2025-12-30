package ca.bc.gov.mal.cirras.underwriting.data.repositories;

import java.io.Serializable;
import java.util.List;

import ca.bc.gov.mal.cirras.underwriting.data.entities.UnderwritingYearDto;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;


public interface UnderwritingYearDao extends Serializable {
	
	UnderwritingYearDto fetch(
		String underwritingYearGuid
	) throws DaoException;

    List<UnderwritingYearDto> fetchAll() throws DaoException;

    UnderwritingYearDto selectByCropYear(
    	Integer cropYear
    ) throws DaoException;
    
    void insert(
    	UnderwritingYearDto dto, 
        String userId
    ) throws DaoException;
    
    void delete(
    	String underwritingYearGuid
   ) throws DaoException, NotFoundDaoException;
    
    void deleteByCropYear(
    	Integer cropYear
   ) throws DaoException, NotFoundDaoException;
   
}
