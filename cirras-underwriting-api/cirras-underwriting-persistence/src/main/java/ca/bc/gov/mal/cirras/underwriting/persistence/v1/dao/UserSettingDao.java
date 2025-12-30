package ca.bc.gov.mal.cirras.underwriting.data.repositories;

import java.io.Serializable;

import ca.bc.gov.mal.cirras.underwriting.data.entities.UserSettingDto;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;


public interface UserSettingDao extends Serializable {
	
	UserSettingDto fetch(String userSettingGuid) throws DaoException;
        
    void insert(UserSettingDto dto, String userId) throws DaoException;
    
    void update(UserSettingDto dto, String userId) throws DaoException, NotFoundDaoException;
    
    void delete(String userSettingGuid) throws DaoException, NotFoundDaoException;
    
    UserSettingDto getByLoginUserGuid(String loginUserGuid) throws DaoException;

}
