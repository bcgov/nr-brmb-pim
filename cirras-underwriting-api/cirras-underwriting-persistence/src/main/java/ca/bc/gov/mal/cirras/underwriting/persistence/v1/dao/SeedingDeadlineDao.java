package ca.bc.gov.mal.cirras.underwriting.data.repositories;

import java.io.Serializable;
import java.util.List;

import ca.bc.gov.mal.cirras.underwriting.data.entities.SeedingDeadlineDto;
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
