package ca.bc.gov.mal.cirras.underwriting.data.repositories;


import java.io.Serializable;
import java.util.List;

import ca.bc.gov.mal.cirras.underwriting.data.entities.LegalLandRiskAreaXrefDto;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;


public interface LegalLandRiskAreaXrefDao extends Serializable {
	
	LegalLandRiskAreaXrefDto fetch(
   		Integer legalLandId,
   		Integer riskAreaId
	) throws DaoException;

    List<LegalLandRiskAreaXrefDto> fetchAll() throws DaoException;

    void insert(
    		LegalLandRiskAreaXrefDto dto, 
        String userId
    ) throws DaoException;
    
    void update(LegalLandRiskAreaXrefDto dto, String userId) throws DaoException;
    
    void delete(
   		Integer legalLandId,
   		Integer riskAreaId
   ) throws DaoException, NotFoundDaoException;
    
    void deleteForLegalLand(
   		Integer legalLandId
   ) throws DaoException, NotFoundDaoException;

   
}
