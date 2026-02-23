package ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.ContractedFieldDetailDto;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.TooManyRecordsException;
import ca.bc.gov.nrs.wfone.common.persistence.dto.PagedDtos;


public interface ContractedFieldDetailDao extends Serializable {
	
	ContractedFieldDetailDto fetch(Integer contractedFieldDetailId) throws DaoException;
	
	ContractedFieldDetailDto fetchSimple(Integer contractedFieldDetailId) throws DaoException;
	
	ContractedFieldDetailDto selectByGcyAndField(Integer growerContractYearId, Integer fieldId) throws DaoException;
    
	ContractedFieldDetailDto selectForFieldRollover(Integer fieldId, Integer cropYear, Integer insurancePlanId) throws DaoException;
    
	void insert(ContractedFieldDetailDto dto, String userId) throws DaoException;
	
	void insertDataSync(ContractedFieldDetailDto dto, String userId) throws DaoException;
    
    void update(ContractedFieldDetailDto dto, String userId) throws DaoException, NotFoundDaoException;
    
    void updateSync(ContractedFieldDetailDto dto, String userId) throws DaoException, NotFoundDaoException;
    
    void updateDisplayOrder(ContractedFieldDetailDto dto, String userId) throws DaoException, NotFoundDaoException;
    
    void delete(Integer contractedFieldDetailId) throws DaoException, NotFoundDaoException;
    
    void deleteForField(Integer fieldId) throws DaoException, NotFoundDaoException;
    
    List<ContractedFieldDetailDto> select(Integer contractId, Integer cropYear) throws DaoException;

    PagedDtos<ContractedFieldDetailDto> select(
    		Integer contractId, 
    		Integer cropYear,
    		String sortColumn,
    		String sortDirection,
        	int maximumRows,
        	Integer pageNumber, 
        	Integer pageRowCount
        ) throws DaoException, TooManyRecordsException;    

    List<ContractedFieldDetailDto> selectForDeclaredYield(Integer contractId, Integer cropYear) throws DaoException;

    List<ContractedFieldDetailDto> selectForVerifiedYield(Integer contractId, Integer cropYear) throws DaoException;
    
    List<ContractedFieldDetailDto> selectForDisplayOrderUpdate(Integer growerContractYearId) throws DaoException;

    List<ContractedFieldDetailDto> selectForYearAndField(Integer cropYear, Integer fieldId) throws DaoException;

    List<ContractedFieldDetailDto> selectForField(Integer fieldId) throws DaoException;
    
    ContractedFieldDetailDto selectForFieldYearAndContract(Integer fieldId, Integer cropYear, Integer contractId) throws DaoException;
}
