package ca.bc.gov.mal.cirras.underwriting.data.repositories;


import java.io.Serializable;
import java.util.List;

import ca.bc.gov.mal.cirras.underwriting.data.entities.CommodityTypeVarietyXrefDto;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;


public interface CommodityTypeVarietyXrefDao extends Serializable {
	
	CommodityTypeVarietyXrefDto fetch(
		String commodityTypeCode,
		Integer cropVarietyId
	) throws DaoException;

    List<CommodityTypeVarietyXrefDto> fetchAll() throws DaoException;

    void insert(
    	CommodityTypeVarietyXrefDto dto, 
        String userId
    ) throws DaoException;
    
    void delete(
		String commodityTypeCode,
		Integer cropVarietyId
   ) throws DaoException, NotFoundDaoException;

   
}
