package ca.bc.gov.mal.cirras.underwriting.data.repositories;

import java.io.Serializable;
import java.util.List;

import ca.bc.gov.mal.cirras.underwriting.data.entities.RiskAreaDto;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;


public interface RiskAreaDao extends Serializable {
	
	RiskAreaDto fetch(
		Integer RiskAreaId
	) throws DaoException;

    List<RiskAreaDto> fetchAll() throws DaoException;

    void insert(
    	RiskAreaDto dto, 
        String userId
    ) throws DaoException;
    
    void update(
   		RiskAreaDto dto, 
        String userId
    ) throws DaoException, NotFoundDaoException;
    
    void delete(
    	Integer riskAreaId
   ) throws DaoException, NotFoundDaoException;
    
    List<RiskAreaDto> select(
        	Integer insurancePlanId
        ) throws DaoException;
   
    List<RiskAreaDto> selectByLegalLand(
        	Integer legalLandId
        ) throws DaoException;
   
}
