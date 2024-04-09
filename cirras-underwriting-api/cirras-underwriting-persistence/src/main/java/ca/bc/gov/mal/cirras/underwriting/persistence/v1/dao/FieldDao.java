package ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao;


import java.io.Serializable;
import java.util.List;

import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.FieldDto;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;


public interface FieldDao extends Serializable {
	
	FieldDto fetch(
   		Integer fieldId
	) throws DaoException;

    List<FieldDto> fetchAll() throws DaoException;

    void insert(
   		FieldDto dto, 
        String userId
    ) throws DaoException;
    
    void insertDataSync(
       		FieldDto dto, 
            String userId
    ) throws DaoException;
    
    void update(
    	FieldDto dto, 
        String userId
    ) throws DaoException, NotFoundDaoException;
    
    void delete(
   		Integer fieldId
   ) throws DaoException, NotFoundDaoException;

    List<FieldDto> selectForLegalLandOrField(
    		Integer legalLandId,
    		Integer fieldId,
    		Integer cropYear
    ) throws DaoException;

	List<FieldDto> selectByLastPolicyForLegalLand(
			Integer legalLandId, 
			Integer cropYear,
			Integer includeContractId,
			Integer excludeContractId,
			Integer excludeFieldId
	) throws DaoException;    

    List<FieldDto> selectOtherFieldsForLegalLand(
    		Integer legalLandId,
    		Integer fieldId,
			Integer cropYear 
    ) throws DaoException;

    List<FieldDto> selectForLegalLand(
    		Integer legalLandId 
    ) throws DaoException;
}
