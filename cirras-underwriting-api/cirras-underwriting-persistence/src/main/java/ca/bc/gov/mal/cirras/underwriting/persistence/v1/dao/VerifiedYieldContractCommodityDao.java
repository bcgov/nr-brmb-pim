package ca.bc.gov.mal.cirras.underwriting.data.repositories;

import java.io.Serializable;
import java.util.List;

import ca.bc.gov.mal.cirras.underwriting.data.entities.VerifiedYieldContractCommodityDto;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;


public interface VerifiedYieldContractCommodityDao extends Serializable {
	
	VerifiedYieldContractCommodityDto fetch(String verifiedYieldContractCommodityGuid) throws DaoException;
        
    void insert(VerifiedYieldContractCommodityDto dto, String userId) throws DaoException;
    
    void update(VerifiedYieldContractCommodityDto dto, String userId) throws DaoException, NotFoundDaoException;
    
    void delete(String verifiedYieldContractCommodityGuid) throws DaoException, NotFoundDaoException;

    void deleteForVerifiedYieldContract(String verifiedYieldContractGuid) throws DaoException, NotFoundDaoException;
    
    List<VerifiedYieldContractCommodityDto> selectForVerifiedYieldContract(String verifiedYieldContractGuid) throws DaoException;
}
