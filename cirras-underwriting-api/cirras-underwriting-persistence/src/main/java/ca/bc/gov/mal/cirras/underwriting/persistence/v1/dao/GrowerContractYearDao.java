package ca.bc.gov.mal.cirras.underwriting.data.repositories;


import java.io.Serializable;
import java.util.List;

import ca.bc.gov.mal.cirras.underwriting.data.entities.GrowerContractYearDto;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;


public interface GrowerContractYearDao extends Serializable {
	
	GrowerContractYearDto fetch(
   		Integer growerContractYearId
	) throws DaoException;

    List<GrowerContractYearDto> fetchAll() throws DaoException;

	GrowerContractYearDto selectInventoryContractForGcy(
	   		Integer growerContractYearId
		) throws DaoException;

	GrowerContractYearDto selectLastYear(Integer contractId, Integer currCropYear) throws DaoException;
	
	void insert(
   		GrowerContractYearDto dto, 
        String userId
    ) throws DaoException;
    
    void update(
    	GrowerContractYearDto dto, 
        String userId
    ) throws DaoException, NotFoundDaoException;
    
    void delete(
   		Integer growerContractYearId
   ) throws DaoException, NotFoundDaoException;

   
}
