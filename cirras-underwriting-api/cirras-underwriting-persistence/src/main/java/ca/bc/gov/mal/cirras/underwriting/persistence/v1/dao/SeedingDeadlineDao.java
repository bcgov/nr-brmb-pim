package ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao;

import java.io.Serializable;
import java.util.List;

import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.SeedingDeadlineDto;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;


public interface SeedingDeadlineDao extends Serializable {
	
	SeedingDeadlineDto fetch(String seedingDeadlineGuid) throws DaoException;
        
    void insert(SeedingDeadlineDto dto, String userId) throws DaoException;
    
    void update(SeedingDeadlineDto dto, String userId) throws DaoException, NotFoundDaoException;
    
    void delete(String seedingDeadlineGuid) throws DaoException, NotFoundDaoException;
        
    SeedingDeadlineDto selectForCommodityTypeAndYear(String commodityTypeCode, Integer cropYear) throws DaoException;

    List<SeedingDeadlineDto> selectByYear(Integer cropYear) throws DaoException;
}
