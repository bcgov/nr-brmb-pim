package ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao;


import java.io.Serializable;
import java.util.List;

import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.CropVarietyInsPlantInsXrefDto;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;


public interface CropVarietyInsPlantInsXrefDao extends Serializable {
	
	CropVarietyInsPlantInsXrefDto fetch(
		String cropVarietyInsurabilityGuid,
		String plantInsurabilityTypeCode
	) throws DaoException;

    List<CropVarietyInsPlantInsXrefDto> fetchAll() throws DaoException;

    void insert(
    		CropVarietyInsPlantInsXrefDto dto, 
        String userId
    ) throws DaoException;
    
    void delete(
		String cropVarietyInsurabilityGuid,
		String plantInsurabilityTypeCode
   ) throws DaoException, NotFoundDaoException;    
   
    void deleteForVariety(
		String cropVarietyInsurabilityGuid
   ) throws DaoException, NotFoundDaoException;

    List<CropVarietyInsPlantInsXrefDto> selectPlantInsForCropVarieties (
  		  Integer cropVarietyId
  	) throws DaoException;
      
    List<CropVarietyInsPlantInsXrefDto> selectForInsurancePlan (
  		  Integer insurancePlanId
  	) throws DaoException;
      
}
