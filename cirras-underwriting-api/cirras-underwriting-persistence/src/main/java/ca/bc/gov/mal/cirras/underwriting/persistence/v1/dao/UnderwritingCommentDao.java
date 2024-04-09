package ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao;

import java.io.Serializable;
import java.util.List;

import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.UnderwritingCommentDto;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;


public interface UnderwritingCommentDao extends Serializable {
	
	UnderwritingCommentDto fetch(String underwritingCommentGuid) throws DaoException;
        
    void insert(UnderwritingCommentDto dto, String userId) throws DaoException;
    
    void update(UnderwritingCommentDto dto, String userId) throws DaoException, NotFoundDaoException;
    
    void delete(String underwritingCommentGuid) throws DaoException, NotFoundDaoException;
    
    void deleteForAnnualField(Integer annualFieldDetailId) throws DaoException, NotFoundDaoException;
    
    void deleteForDeclaredYieldContractGuid(String declaredYieldContractGuid) throws DaoException, NotFoundDaoException;
    
    void deleteForField(Integer fieldId) throws DaoException, NotFoundDaoException;
    
    List<UnderwritingCommentDto> select(Integer annualFieldDetailId) throws DaoException;
    
    List<UnderwritingCommentDto> selectForDopContract(String declaredYieldContractGuid) throws DaoException;

    List<UnderwritingCommentDto> selectForField(Integer fieldId) throws DaoException;
}
