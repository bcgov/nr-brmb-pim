package ca.bc.gov.mal.cirras.underwriting.data.repositories;


import java.io.Serializable;
import java.util.List;

import ca.bc.gov.mal.cirras.underwriting.data.entities.AnnualFieldDetailDto;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;


public interface AnnualFieldDetailDao extends Serializable {
	
	AnnualFieldDetailDto fetch(
   		Integer annualFieldDetailId
	) throws DaoException;

    List<AnnualFieldDetailDto> fetchAll() throws DaoException;

    AnnualFieldDetailDto getByFieldAndCropYear(
    		Integer fieldId, 
    		Integer cropYear
    		) throws DaoException;
    
    int getTotalForLegalLandField(Integer legalLandId, Integer fieldId) throws DaoException, NotFoundDaoException;

    void insert(
   		AnnualFieldDetailDto dto, 
        String userId
    ) throws DaoException;
    
    void insertDataSync(
   		AnnualFieldDetailDto dto, 
        String userId
    ) throws DaoException;
    
    void update(
    	AnnualFieldDetailDto dto, 
        String userId
    ) throws DaoException, NotFoundDaoException;
    
    void delete(
   		Integer annualFieldDetailId
   ) throws DaoException, NotFoundDaoException;

    void deleteForField(Integer fieldId) throws DaoException, NotFoundDaoException;
    
   AnnualFieldDetailDto getPreviousSubsequentRecords(
    		Integer fieldId, 
    		Integer cropYear
    		) throws DaoException;
    
   
}
