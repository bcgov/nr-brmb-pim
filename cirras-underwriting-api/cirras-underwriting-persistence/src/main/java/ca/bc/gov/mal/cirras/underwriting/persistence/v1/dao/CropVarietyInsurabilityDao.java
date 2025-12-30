package ca.bc.gov.mal.cirras.underwriting.data.repositories;


import java.io.Serializable;
import java.util.List;

import ca.bc.gov.mal.cirras.underwriting.data.entities.CropVarietyInsurabilityDto;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;


public interface CropVarietyInsurabilityDao extends Serializable {
	
	CropVarietyInsurabilityDto fetch(
		Integer cropVarietyId
	) throws DaoException;

    List<CropVarietyInsurabilityDto> fetchAll() throws DaoException;

    void insert(
    	CropVarietyInsurabilityDto dto, 
        String userId
    ) throws DaoException;
    
    void update(
    	CropVarietyInsurabilityDto dto, 
        String userId
    ) throws DaoException, NotFoundDaoException;
    
    void delete(
    	Integer cropVarietyId
   ) throws DaoException, NotFoundDaoException;

   List<CropVarietyInsurabilityDto> selectForInsurancePlan(Integer insurancePlanId) throws DaoException;

   List<CropVarietyInsurabilityDto> selectValidation(Integer insurancePlanId) throws DaoException;

}
