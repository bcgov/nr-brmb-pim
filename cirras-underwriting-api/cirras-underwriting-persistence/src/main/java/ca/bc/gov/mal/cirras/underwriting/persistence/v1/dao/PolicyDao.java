package ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao;


import java.io.Serializable;
import java.util.List;

import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.PolicyDto;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.TooManyRecordsException;
import ca.bc.gov.nrs.wfone.common.persistence.dto.PagedDtos;


public interface PolicyDao extends Serializable {
	
	PolicyDto fetch(
		Integer policyId
	) throws DaoException;
        
    void insert(
    		PolicyDto dto, 
        String userId
    ) throws DaoException;
    
    void update(
        PolicyDto dto, 
        String userId
    ) throws DaoException, NotFoundDaoException;
    
    void delete(
    	Integer policyId
   ) throws DaoException, NotFoundDaoException;
    
    PagedDtos<PolicyDto> select(
        	Integer cropYear,
        	Integer insurancePlanId,
    		Integer officeId,
    		String policyStatusCode,
    		String policyNumber,
    		String growerInfo,
    		String datasetType,
    		String sortColumn,
    		String sortDirection,
        	int maximumRows,
        	Integer pageNumber, 
        	Integer pageRowCount
        ) throws DaoException, TooManyRecordsException;
       
    List<PolicyDto> selectByFieldAndYear(
        	Integer fieldId,
        	Integer cropYear
        ) throws DaoException;

	List<PolicyDto> selectByOtherYearInventory(Integer contractId, Integer currentCropYear, Integer numYears) throws DaoException;

	List<PolicyDto> selectByOtherYearDop(Integer contractId, Integer currentCropYear, Integer numYears) throws DaoException;
	
	List<PolicyDto> selectByOtherYearVerified(Integer contractId, Integer currentCropYear, Integer numYears) throws DaoException;
    
	String cleanGrowerPhoneNumber(String growerInfo);
       
}
