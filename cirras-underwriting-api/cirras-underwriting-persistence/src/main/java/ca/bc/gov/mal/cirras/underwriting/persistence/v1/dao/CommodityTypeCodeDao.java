package ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao;


import java.io.Serializable;
import java.util.List;

import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.CommodityTypeCodeDto;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;


public interface CommodityTypeCodeDao extends Serializable {
	
	CommodityTypeCodeDto fetch(
			String commodityTypeCode
	) throws DaoException;

    List<CommodityTypeCodeDto> fetchAll() throws DaoException;

    void insert(
    	CommodityTypeCodeDto dto, 
        String userId
    ) throws DaoException;
    
    void update(
   		CommodityTypeCodeDto dto, 
        String userId
    ) throws DaoException, NotFoundDaoException;
    
    void delete(
    	String commodityTypeCode
   ) throws DaoException, NotFoundDaoException;

    List<CommodityTypeCodeDto> selectByPlan(
    		Integer insurancePlanId,
    		Integer cropYear  // Optional: Only needed to retrieve seeding deadlines.
        ) throws DaoException;
   
    List<CommodityTypeCodeDto> selectByCropCommodityPlan(
    		Integer insurancePlanId
        ) throws DaoException;
   
}
