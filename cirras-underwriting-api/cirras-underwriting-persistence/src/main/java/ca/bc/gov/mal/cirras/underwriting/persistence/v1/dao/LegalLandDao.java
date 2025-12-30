package ca.bc.gov.mal.cirras.underwriting.data.repositories;


import java.io.Serializable;
import java.util.List;

import ca.bc.gov.mal.cirras.underwriting.data.entities.LegalLandDto;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.TooManyRecordsException;
import ca.bc.gov.nrs.wfone.common.persistence.dto.PagedDtos;


public interface LegalLandDao extends Serializable {
	
	LegalLandDto fetch(
   		Integer legalLandId
	) throws DaoException;

    List<LegalLandDto> fetchAll() throws DaoException;

    void insert(
   		LegalLandDto dto, 
        String userId
    ) throws DaoException;

    void insertDataSync(
   		LegalLandDto dto, 
        String userId
    ) throws DaoException;
    
    void update(
    	LegalLandDto dto, 
        String userId
    ) throws DaoException, NotFoundDaoException;
    
    void delete(
   		Integer legalLandId
   ) throws DaoException, NotFoundDaoException;

	PagedDtos<LegalLandDto> select(
			String otherDescription, 
			String primaryPropertyIdentifier,
			String growerInfo,
			String datasetType,
			Boolean wildCardSearch, 
			Boolean searchByOtherDescOrLegalDesc,    // If true, then the otherDescription criteria can match other_description or legal_description.
			String sortColumn,
			String sortDirection,
			int maximumRows,
			Integer pageNumber, 
			Integer pageRowCount
	) throws DaoException, TooManyRecordsException;
    
    List<LegalLandDto> searchOtherLegalLandForField(
    	Integer fieldId,
 		Integer legalLandId,
 		Integer cropYear
 	) throws DaoException;
    
    int getNextPidSequence() throws DaoException;
}
