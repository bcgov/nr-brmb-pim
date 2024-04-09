package ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao;


import java.io.Serializable;
import java.util.List;

import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.InsurancePlanDto;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;


public interface InsurancePlanDao extends Serializable {
	
	InsurancePlanDto fetch(Integer insurancePlanId) throws DaoException;
    
    List<InsurancePlanDto> selectByField(Integer fieldId) throws DaoException;
   
}
