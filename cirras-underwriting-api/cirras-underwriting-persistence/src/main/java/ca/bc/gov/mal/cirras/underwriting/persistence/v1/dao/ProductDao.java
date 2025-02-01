package ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao;


import java.io.Serializable;
import java.util.List;

import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.ProductDto;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;


public interface ProductDao extends Serializable {
	
	ProductDto fetch(
		Integer productId
	) throws DaoException;
	
	public List<ProductDto> getForPolicy(
		Integer contractId, 
		Integer cropYear
	) throws DaoException;
        
    void insert(
    	ProductDto dto, 
        String userId
    ) throws DaoException;
    
    void update(
    	ProductDto dto, 
        String userId
    ) throws DaoException, NotFoundDaoException;
    
    void delete(
    	Integer productId
   ) throws DaoException, NotFoundDaoException;
       
}
