package ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao;


import java.io.Serializable;
import java.util.List;

import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.CropCommodityDto;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;


public interface CropCommodityDao extends Serializable {
	
	CropCommodityDto fetch(
		Integer cropCommodityId
	) throws DaoException;

    List<CropCommodityDto> fetchAll() throws DaoException;

    void insert(
    	CropCommodityDto dto, 
        String userId
    ) throws DaoException;
    
    void update(
   		CropCommodityDto dto, 
        String userId
    ) throws DaoException, NotFoundDaoException;
    
    void delete(
    	Integer cropCommodityId
   ) throws DaoException, NotFoundDaoException;
    
    List<CropCommodityDto> select(
        	Integer insurancePlanId,
        	Integer cropYear,
        	String commodityType
        ) throws DaoException;
   
}
